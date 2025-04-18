package dev.rocco.kig.paper.api.event;

/**
 * Reason for {@link org.bukkit.event.player.PlayerQuitEvent} and {@link org.bukkit.event.player.PlayerKickEvent}.
 */
public enum DisconnectReason {
    // Client-bound

    /**
     * The client disconnected normally
     */
    DISCONNECTED,
    /**
     * Read timed out on the connection
     */
    TIMED_OUT,
    /**
     * An unknown exception has occurred in the connection
     */
    CONNECTION_EXCEPTION,

    // Kicks

    /**
     * The server is shutting down
     */
    SERVER_SHUTTING_DOWN,
    /**
     * The player has died and the server is in hardcore mode
     */
    HARDCORE_DEATH,
    /**
     * The player was kicked because the same player logged in from a different location
     */
    KICK_ALREADY_LOGGED_IN,
    /**
     * The player sent an invalid packet (unknown, malformed etc.)
     */
    KICK_BAD_PACKET,
    /**
     * The player performed certain actions too quickly. Doesn't include spamming
     */
    KICK_RATE_LIMIT,
    /**
     * The player sent an invalid packet, and this could have caused harm to the server
     */
    KICK_BAD_PACKET_SEVERE,
    /**
     * The player has been idle for too long
     */
    KICK_IDLE,
    /**
     * The player has been sending messages/commands too quickly
     */
    KICK_SPAM,
    /**
     * The player was hovering for too long
     */
    KICK_FLYING,

    // Custom

    /**
     * The player was kicked by a plugin
     */
    KICK_PLUGIN
}
