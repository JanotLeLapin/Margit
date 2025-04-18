package dev.rocco.kig.paper.api.velocity;

public interface KnockbackValues {
    float getHorizontalMultiplier();
    float getVerticalMultiplier();
    float getFriction();
    float getSprintingHorizontalMultiplier();
    float getSprintingVerticalMultiplier();
    void setHorizontalMultiplier(float multiplier);
    void setVerticalMultiplier(float multiplier);
    void setSprintingVerticalMultiplier(float multiplier);
    void setSprintingHorizontalMultiplier(float multiplier);
    void setFriction(float friction);
    void resetHorizontalMultiplier();
    void resetVerticalMultiplier();
    void resetFriction();
    void resetSprintingHorizontalMultiplier();
    void resetSprintingVerticalMultiplier();
}
