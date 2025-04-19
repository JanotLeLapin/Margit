package dev.rocco.kig.paper.impl.cheetah;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.*;
import org.bukkit.Location;

import java.nio.ByteBuffer;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

public class SinkEntityPlayer extends EntityPlayer {
    private static final AtomicLong UUID_REGISTRY = new AtomicLong(100);
    private final Consumer<ByteBuffer> packetConsumer;

    public SinkEntityPlayer(WorldServer worldserver, Consumer<ByteBuffer> packetConsumer, Location spawnLoc) {
        super(worldserver.getMinecraftServer(), worldserver, new GameProfile(new UUID(0, UUID_REGISTRY.getAndIncrement()), "CH-" + UUID_REGISTRY.get()), new PlayerInteractManager(worldserver));
        this.packetConsumer = packetConsumer;
        this.viewDistance = 200;
        new SinkPlayerConnection(worldserver.getMinecraftServer(), this);
        // Add existing players
        playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, worldserver.players.stream().map(e -> (EntityPlayer) e).toArray(EntityPlayer[]::new)));
        worldserver.sinkPlayers.put(getUniqueID(), this);
        setLocation(spawnLoc.getX(), spawnLoc.getY(), spawnLoc.getZ(), 0f, 0f);
    }

    @Override
    public void d(Entity entity) {
        // Don't use removeQueue for entity destroy
        this.playerConnection.sendPacket(new PacketPlayOutEntityDestroy(entity.getId()));
    }

    Consumer<ByteBuffer> getPacketConsumer() {
        return packetConsumer;
    }
}
