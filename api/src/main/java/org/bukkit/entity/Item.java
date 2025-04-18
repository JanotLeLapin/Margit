package org.bukkit.entity;

import org.bukkit.inventory.ItemStack;

/**
 * Represents an Item.
 */
public interface Item extends Entity {

    /**
     * Gets the item stack associated with this item drop.
     *
     * @return An item stack.
     */
    public ItemStack getItemStack();

    /**
     * Sets the item stack associated with this item drop.
     *
     * @param stack An item stack.
     */
    public void setItemStack(ItemStack stack);

    /**
     * Gets the delay before this Item is available to be picked up by players
     *
     * @return Remaining delay
     */
    public int getPickupDelay();

    /**
     * Sets the delay before this Item is available to be picked up by players
     *
     * @param delay New delay
     */
    public void setPickupDelay(int delay);

    /**
     * @return the item's age
     */
    int getAge();

    /**
     * @return the item's despawn rate. If no custom value is set, this returns the rate according to the Spigot config.
     */
    int getDespawnRate();

    /**
     * Sets the item's respawn rate
     * @param rate the rate in ticks or 0 if the default should be used
     */
    void setDespawnRate(int rate);

    /**
     * @return whether the item is affected by gravity
     */
    boolean isAffectedByGravity();

    /**
     * Sets whether the item should be affected by gravity
     * @param gravity whether the item should be affected by gravity
     */
    void setAffectedByGravity(boolean gravity);

    /**
     * Prevents players from picking up this item.
     */
    default void noPickup() {
        setPickupDelay(32767);
    }
}
