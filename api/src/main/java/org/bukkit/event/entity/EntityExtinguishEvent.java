package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;

/**
 * Called when an entity is about to be extinguished.
 */
public class EntityExtinguishEvent extends EntityEvent {
    private static final HandlerList handlers = new HandlerList();

    public EntityExtinguishEvent(Entity toExtinguish) {
        super(toExtinguish);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
