package dev.rocco.kig.paper.api.particle;

import org.bukkit.Location;

public abstract class ProjectileEffect {
    private int loopTimes;

    public ProjectileEffect(int loopTimes) {
        this.loopTimes = loopTimes;
    }

    public int getLoopTimes() {
        return loopTimes;
    }

    public abstract void spawn(Location loc);
}
