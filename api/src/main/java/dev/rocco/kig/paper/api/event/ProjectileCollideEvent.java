package dev.rocco.kig.paper.api.event;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;

/**
 * Fired when a {@link Projectile} is about to collide with an {@link Entity}.
 * Cancelling the event will make the projectile go through the entity.
 */
public class ProjectileCollideEvent extends EntityEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final Entity collided;
    private boolean cancel;

    public ProjectileCollideEvent(Projectile projectile, Entity collided) {
        super(projectile);
        this.collided = collided;
    }

    @Override
    public Projectile getEntity() {
        return (Projectile) entity;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Entity getCollided() {
        return collided;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
