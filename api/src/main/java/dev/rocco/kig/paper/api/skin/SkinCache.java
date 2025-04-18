package dev.rocco.kig.paper.api.skin;

public interface SkinCache {
    /**
     * Loads the player's skin asynchronously, then executes the runnable when the load is complete.
     * @param name the player's name
     * @param runnable the code to execute
     */
    void loadAndRun(String name, Runnable runnable);
}
