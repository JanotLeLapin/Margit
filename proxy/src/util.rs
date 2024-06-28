use bytes::{Buf, Bytes};
use thiserror::Error;

#[derive(Error, Debug)]
pub enum VarLenError {
    #[error("VarLen too big")]
    TooBig,
}

pub fn read_varint(bytes: &mut Bytes) -> Result<i32, VarLenError> {
    let mut res: i32 = 0;
    let mut pos: u8 = 0;
    let mut b: u8;

    loop {
        b = bytes.get_u8();
        res |= (b as i32 & 0x7F) << pos;
        if (b & 0x80) == 0 {
            break;
        }

        pos += 7;
        if pos >= 32 {
            return Err(VarLenError::TooBig);
        }
    }
    Ok(res)
}

#[derive(Error, Debug)]
pub enum StringError {
    #[error("String length too big")]
    LengthTooBig,
}

pub fn read_string(bytes: &mut Bytes) -> Result<String, StringError> {
    let length = read_varint(bytes).map_err(|_| StringError::LengthTooBig)? as usize;
    let mut res = vec![0u8; length];
    for i in 0..length {
        res[i] = bytes.get_u8();
    }
    Ok(String::from_utf8_lossy(&res).to_string())
}
