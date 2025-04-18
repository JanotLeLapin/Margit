package org.bukkit.scoreboard;

import org.bukkit.entity.Player;

import java.lang.ref.WeakReference;
import java.util.Collection;

/**
 * Manager of Scoreboards
 */
public interface ScoreboardManager {

    /**
     * Gets the primary Scoreboard controlled by the server.
     * <p>
     * This Scoreboard is saved by the server, is affected by the /scoreboard
     * command, and is the scoreboard shown by default to players.
     *
     * @return the default sever scoreboard
     */
    Scoreboard getMainScoreboard();

    /**
     * Gets a new Scoreboard to be tracked by the server. This scoreboard will
     * be tracked as long as a reference is kept, either by a player or by a
     * plugin.
     *
     * @return the registered Scoreboard
     * @see WeakReference
     */
    Scoreboard getNewScoreboard();

    // KigPaper start
    /**
     * Updates scores in some scoreboards based on the given criteria.
     *
     * <p>Example: to send Notch's health to <pre>player1</pre> and <pre>player2</pre>, you can use:
     * <pre>updateScoresFor(Criterias.HEALTH, "Notch", Arrays.asList(player1, player2))</pre>
     *
     * @param criteria determines which score to update, must be a valid criteria name, see {@link Criterias}.
     * @param scoreboardEntry the entries to update scores for, usually the player name.
     *                        See {@link Scoreboard#getScores(String)}.
     * @param receivers the players that should receive the update
     */
    void updateScoresFor(String criteria, Collection<String> scoreboardEntry, Collection<Player> receivers);
    // KigPaper end
}
