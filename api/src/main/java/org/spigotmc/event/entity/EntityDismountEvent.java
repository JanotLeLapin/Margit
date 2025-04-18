package org.spigotmc.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;

// PaperSpigot start
import org.bukkit.event.Cancellable;
// PaperSpigot end

/**
 * Called when an entity stops riding another entity.
 *
 */
public class EntityDismountEvent extends EntityEvent implements Cancellable // PaperSpigot - implement Cancellable
{

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private final Entity dismounted;
    private final DismountCause cause;

    public EntityDismountEvent(Entity what, Entity dismounted, DismountCause cause) // KigPaper - add cause
    {
        super( what );
        this.dismounted = dismounted;
        this.cause = cause;
    }

    public Entity getDismounted()
    {
        return dismounted;
    }

    @Override
    public HandlerList getHandlers()
    {
        return handlers;
    }

    public static HandlerList getHandlerList()
    {
        return handlers;
    }

    // PaperSpigot start - implement Cancellable methods
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
    // PaperSpigot end

    // KigPaper start - distinguish between natural dismount and Bukkit API dismount
    public enum DismountCause {
        NATURAL,
        BUKKIT
    }

    public DismountCause getCause() {
        return cause;
    }
    // KigPaper end
}
