package dev.rocco.kig.paper.api.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called before a sign change by a player is processed. If you only want to listen for
 * successful sign changes, use {@link org.bukkit.event.block.SignChangeEvent} instead.
 * The {@code PreSignChangeEvent} is only useful if the events that you want to listen
 * for can be cancelled by the server e.g. because the sign that the player tries to
 * edit doesn't exist.
 *
 * <p>If you cancel this event, the sign will not be changed. Moreover, the server still
 * checks whether the sign change is valid, and its checks and the SignChangeEvent can
 * still prevent the sign change if you don't cancel this event.
 */
public class PreSignChangeEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final String[] lines;
    private final Location location;
    private boolean cancelled = false;

    /**
     * Creates a new {@code PreSignChangeEvent} for the given block, player, and changed
     * lines.
     *
     * @param location the location to create this event for
     * @param player   the player that initiate the sign change
     * @param lines    the lines that the sign was changed to in this event
     */
    public PreSignChangeEvent(Location location, Player player, String[] lines) {
        this.player = player;
        this.lines = lines;
        this.location = location;
    }

    /**
     * Returns the list of handlers that handle a {@code PreSignChangeEvent}.
     */
    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Returns the player that involved this sign change event.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns the lines that the sign's content was set to
     */
    public String[] getLines() {
        return lines;
    }

    /**
     * Returns the location of the block that this event was called for. The block at this location is <b>not</b>
     * guaranteed to exist.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Returns whether this event has been cancelled
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets the cancelled status for this event
     *
     * @param cancel true if you wish to cancel this event
     */
    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    /**
     * Returns the list of handlers for the {@code PreSignChangeEvent}
     */
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
