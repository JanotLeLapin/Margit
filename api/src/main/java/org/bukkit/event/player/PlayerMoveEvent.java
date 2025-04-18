package org.bukkit.event.player;

import com.google.common.base.Preconditions;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Holds information for player movement events
 */
public class PlayerMoveEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;
    private Location from;
    private Location to;
    private long accurateMoveTime; // KigPaper

    public PlayerMoveEvent(final Player player, final Location from, final Location to, long accurateMoveTime) { // KigPaper - add accurate move time
        super(player);
        this.from = from;
        this.to = to;
        this.accurateMoveTime = accurateMoveTime;
    }

    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     * <p>
     * If a move or teleport event is cancelled, the player will be moved or
     * teleported back to the Location as defined by getFrom(). This will not
     * fire an event
     *
     * @return true if this event is cancelled
     */
    public boolean isCancelled() {
        return cancel;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     * <p>
     * If a move or teleport event is cancelled, the player will be moved or
     * teleported back to the Location as defined by getFrom(). This will not
     * fire an event
     *
     * @param cancel true if you wish to cancel this event
     */
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * Gets the location this player moved from
     *
     * @return Location the player moved from
     */
    public Location getFrom() {
        return from;
    }

    /**
     * Sets the location to mark as where the player moved from
     *
     * @param from New location to mark as the players previous location
     */
    public void setFrom(Location from) {
        validateLocation(from);
        this.from = from;
    }

    /**
     * Gets the location this player moved to
     *
     * @return Location the player moved to
     */
    public Location getTo() {
        return to;
    }

    /**
     * Sets the location that this player will move to
     *
     * @param to New Location this player will move to
     */
    public void setTo(Location to) {
        validateLocation(to);
        this.to = to;
    }

    // KigPaper start
    /**
     * The timestamp of the packet that triggered this move.
     * This timestamp is calculated on the Netty thread that decoded the packet, so it's not bound to a tick scheduler.
     * The timestamp is calculated using {@link System#nanoTime()}, which provides better granularity than the Minecraft tick system (50ms intervals).
     *
     * Note: some implementations return 0, though it (along with negative values) can still be a valid timestamp.
     * To check if the value is a valid timestamp, run {@link PlayerMoveEvent#hasAccurateMoveTime()} first.
     * @return the move's accurate timestamp
     */
    public long getAccurateMoveTime() {
        return accurateMoveTime;
    }

    /**
     * @return whether the event implementation contains an accurate move timestamp
     */
    public boolean hasAccurateMoveTime() {
        return !(this instanceof PlayerTeleportEvent);
    }
    // KigPaper end

    private void validateLocation(Location loc) {
        Preconditions.checkArgument(loc != null, "Cannot use null location!");
        Preconditions.checkArgument(loc.getWorld() != null, "Cannot use null location with null world!");
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
