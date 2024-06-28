mod packet;
mod util;

use std::error::Error;

use bytes::{Buf, Bytes};
use tokio::{
    io::AsyncReadExt,
    net::{TcpListener, TcpStream},
};

pub enum ClientState {
    Handshake,
    Status,
    Login,
    Play,
}

pub async fn client(mut socket: TcpStream) -> Result<(), Box<dyn Error>> {
    let mut state = ClientState::Handshake;
    let mut protocol: u16 = 0;
    loop {
        let size = {
            let mut res: usize = 0;
            let mut pos: u8 = 0;
            let mut b: u8;

            loop {
                b = socket.read_u8().await?;
                res |= (b as usize & 0x7F) << pos;
                if (b & 0x80) == 0 {
                    break res;
                }

                pos += 7;
                if pos >= 32 {
                    return Err(Box::new(util::VarLenError::TooBig));
                }
            }
        };

        let mut data = vec![0u8; size];
        socket.read(&mut data).await?;
        println!("{data:?}");
        let mut data = Bytes::from(data);

        let id = util::read_varint(&mut data)?;
        match state {
            ClientState::Handshake => match id {
                0x00 => {
                    protocol = util::read_varint(&mut data)? as u16;
                    util::read_string(&mut data)?; // address
                    data.get_u16(); // port
                    state = match data.get_u8() {
                        1 => ClientState::Status,
                        2 => ClientState::Login,
                        next => unimplemented!("unknown next state: {next}"),
                    };
                }
                _ => unimplemented!("unknown id at handshake: {id}"),
            },
            ClientState::Status => match id {
                0x00 => packet::status::status::handle(&mut socket).await?,
                0x01 => {
                    packet::status::ping::handle(&mut socket, &mut data).await?;
                    break;
                }
                _ => unimplemented!("unknown id at status: {id}"),
            },
            _ => {}
        }
    }
    Ok(())
}

#[tokio::main]
pub async fn main() -> Result<(), Box<dyn Error>> {
    let addr = "127.0.0.1:25566";
    let listener = TcpListener::bind(&addr).await?;
    loop {
        let (socket, _) = listener.accept().await?;
        tokio::spawn(async move { client(socket).await.unwrap() });
    }
}
