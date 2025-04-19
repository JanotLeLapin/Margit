package dev.rocco.kig.paper.impl.event;

import dev.rocco.kig.paper.api.event.PlayerInteractUpdateEvent;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.EntityPlayer;
import org.bukkit.craftbukkit.event.CraftEventFactory;

public class KigEvents {
    public static boolean fireInteractUpdate(EntityPlayer player, BlockPosition blockPosition) {
        PlayerInteractUpdateEvent event = new PlayerInteractUpdateEvent(player.getBukkitEntity(), blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
        CraftEventFactory.callEvent(event);
        return !event.isCancelled();
    }
}
