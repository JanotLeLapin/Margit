use std::error::Error;

use bytes::{BufMut, BytesMut};
use tokio::{io::AsyncWriteExt, net::TcpStream};

use crate::util;

const STATUS: &'static str = "{\"version\":{\"name\":\"1.8.8\",\"protocol\":47},\"players\":{\"max\":100,\"online\":0,\"sample\":[]},\"description\":{\"text\":\"Hello, World!\"}}";

pub async fn handle(socket: &mut TcpStream) -> Result<(), Box<dyn Error>> {
    let mut res = BytesMut::new();
    util::write_varint(&mut res, 0x00);
    util::write_string(&mut res, STATUS);

    let mut packet = BytesMut::new();
    util::write_varint(&mut packet, res.len() as i32);
    packet.put(res);

    socket.write_all(&packet.to_vec()).await.unwrap();

    Ok(())
}
