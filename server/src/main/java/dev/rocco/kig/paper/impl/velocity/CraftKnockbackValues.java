package dev.rocco.kig.paper.impl.velocity;

import dev.rocco.kig.paper.api.velocity.KnockbackValues;
import org.github.paperspigot.PaperSpigotConfig;

public class CraftKnockbackValues implements KnockbackValues {
    private float horizontal, vertical, friction, sprintingHorizontal, sprintingVertical;

    public CraftKnockbackValues() {
        resetHorizontalMultiplier();
        resetVerticalMultiplier();
        resetSprintingHorizontalMultiplier();
        resetSprintingVerticalMultiplier();
        resetFriction();
    }

    @Override
    public float getHorizontalMultiplier() {
        return horizontal;
    }

    @Override
    public float getVerticalMultiplier() {
        return vertical;
    }

    @Override
    public float getFriction() {
        return friction;
    }

    @Override
    public float getSprintingHorizontalMultiplier() {
        return sprintingHorizontal;
    }

    @Override
    public float getSprintingVerticalMultiplier() {
        return sprintingVertical;
    }

    @Override
    public void setHorizontalMultiplier(float multiplier) {
        this.horizontal = multiplier;
    }

    @Override
    public void setVerticalMultiplier(float multiplier) {
        this.vertical = multiplier;
    }

    @Override
    public void setSprintingVerticalMultiplier(float multiplier) {
        this.sprintingVertical = multiplier;
    }

    @Override
    public void setSprintingHorizontalMultiplier(float multiplier) {
        this.sprintingHorizontal = multiplier;
    }

    @Override
    public void resetHorizontalMultiplier() {
        this.horizontal = PaperSpigotConfig.knockbackHorizontalMultiplier;
    }

    @Override
    public void resetVerticalMultiplier() {
        this.vertical = PaperSpigotConfig.knockbackVerticalMultiplier;
    }

    @Override
    public void resetFriction() {
        this.friction = PaperSpigotConfig.knockbackFriction;
    }

    @Override
    public void resetSprintingHorizontalMultiplier() {
        this.sprintingHorizontal = PaperSpigotConfig.knockbackHorizontalSprinting;
    }

    @Override
    public void resetSprintingVerticalMultiplier() {
        this.sprintingVertical = PaperSpigotConfig.knockbackVerticalSprinting;
    }

    @Override
    public void setFriction(float friction) {
        this.friction = friction;
    }
}
