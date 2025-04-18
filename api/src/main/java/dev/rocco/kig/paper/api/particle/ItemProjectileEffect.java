package dev.rocco.kig.paper.api.particle;

import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class ItemProjectileEffect extends ProjectileEffect {
    private ItemStack stack;

    public ItemProjectileEffect(ItemStack stack) {
        super(1);
        this.stack = stack;
    }

    @Override
    public void spawn(Location loc) {
        Item item = loc.getWorld().dropImmovableItem(loc, stack);
        item.setDespawnRate(5);
        item.noPickup();
        item.setAffectedByGravity(false);
        item.setVelocity(new Vector(0, 0, 0));
    }
}
