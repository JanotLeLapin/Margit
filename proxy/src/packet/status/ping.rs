use std::error::Error;

use bytes::{Buf, BufMut, Bytes, BytesMut};
use tokio::{io::AsyncWriteExt, net::TcpStream};

pub async fn handle(socket: &mut TcpStream, data: &mut Bytes) -> Result<(), Box<dyn Error>> {
    let mut res = BytesMut::with_capacity(10);
    res.put_u8(9);
    res.put_u8(0x01);
    res.put_u64(data.get_u64());

    socket.write_all(&res).await?;
    Ok(())
}
