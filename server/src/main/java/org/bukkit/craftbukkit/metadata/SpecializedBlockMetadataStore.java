package org.bukkit.craftbukkit.metadata;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.metadata.MetadataStore;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.*;

/**
 * An alternative to {@link BlockMetadataStore} that uses specialized collections and bit-packing for faster
 * operations and a lower memory footprint.
 *
 * <p>This can be a drop-in replacement, though keep in mind methods will throw an {@link IllegalArgumentException}
 * if invalid block coordinates are provided.
 */
public class SpecializedBlockMetadataStore implements MetadataStore<Block> {
    // Allows storing 25 bits (up to 33,554,432 - MC supports up to 30,000,000) + sign
    private static final int MASK_26_BITS = 0b111_111_111_111_111_111_111_111_11;

    private final Map<StoreKey, Map<Plugin, MetadataValue>> metadata = new HashMap<>();
    private final World owningWorld;

    // Cached key used for (synchronized) lookups, saves allocations.
    // The key stored inside is usually cleared after use, so that the reference doesn't live too long.
    private final StoreKey cachedKey = new StoreKey(null, 0);

    public SpecializedBlockMetadataStore(World owningWorld) {
        this.owningWorld = owningWorld;
    }

    @Override
    public synchronized List<MetadataValue> getMetadata(Block block, String metadataKey) {
        Preconditions.checkArgument(block.getWorld() == owningWorld, "block is from another world");

        List<MetadataValue> list = Collections.emptyList();
        Map<Plugin, MetadataValue> pluginMetadata = getEntry(metadataKey, block);
        if (pluginMetadata == null) {
            return list;
        }
        list = ImmutableList.copyOf(pluginMetadata.values());

        return list;
    }

    @Override
    public synchronized boolean hasMetadata(Block block, String metadataKey) {
        Preconditions.checkArgument(block.getWorld() == owningWorld, "block is from another world");
        Map<Plugin, MetadataValue> pluginMetadata = getEntry(metadataKey, block);
        return pluginMetadata != null && !pluginMetadata.isEmpty();
    }

    @Override
    public synchronized void removeMetadata(Block block, String metadataKey, Plugin owningPlugin) {
        Preconditions.checkArgument(block.getWorld() == owningWorld, "block is from another world");
        Preconditions.checkNotNull(owningPlugin, "plugin is null");

        long id = getBlockId(block);
        Map<Plugin, MetadataValue> pluginMetadata = getEntry(metadataKey, id);
        if (pluginMetadata == null) {
            return;
        }
        // Clear empty entries
        if (pluginMetadata.remove(owningPlugin) != null && pluginMetadata.isEmpty()) {
            StoreKey key = getCachedKey(metadataKey, id);
            this.metadata.remove(key);
            key.metadataKey = null;
        }
    }

    @Override
    public synchronized void setMetadata(Block block, String metadataKey, MetadataValue newMetadataValue) {
        Preconditions.checkArgument(block.getWorld() == owningWorld, "block is from another world");
        Preconditions.checkNotNull(newMetadataValue, "value is null");
        Plugin owningPlugin = newMetadataValue.getOwningPlugin();
        Preconditions.checkNotNull(owningPlugin, "plugin is null");

        Map<Plugin, MetadataValue> entry = metadata.computeIfAbsent(new StoreKey(metadataKey, getBlockId(block)),
                k -> new WeakHashMap<>(1));
        entry.put(owningPlugin, newMetadataValue);
    }

    @Override
    public synchronized void invalidateAll(Plugin owningPlugin) {
        Preconditions.checkNotNull(owningPlugin, "plugin is null");
        for (Map<Plugin, MetadataValue> values : metadata.values()) {
            MetadataValue value = values.get(owningPlugin);
            if (value != null) {
                value.invalidate();
            }
        }
    }

    private Map<Plugin, MetadataValue> getEntry(String key, Block block) {
        return getEntry(key, getBlockId(block));
    }

    private Map<Plugin, MetadataValue> getEntry(String key, long id) {
        StoreKey storeKey = getCachedKey(key, id);
        Map<Plugin, MetadataValue> ret = metadata.get(storeKey);
        storeKey.metadataKey = null;
        return ret;
    }

    private StoreKey getCachedKey(String metadataKey, long id) {
        cachedKey.block = id;
        cachedKey.metadataKey = metadataKey;
        return cachedKey;
    }

    static long getBlockId(Block block) {
        int x = block.getX();
        int y = block.getY();
        int z = block.getZ();
        Preconditions.checkArgument(x >= -30_000_000 && x <= 30_000_000,
                "block X out of range");
        Preconditions.checkArgument(y >= 0 && y <= 255, "block Y out of range");
        Preconditions.checkArgument(z >= -30_000_000 && z <= 30_000_000,
                "block Z out of range");
        // X (26 bits) + Y (8 bits) + Z (26 bits)
        return ((long) (x & MASK_26_BITS) << 34) | ((long) (y & 0xFF) << 26) | (z & MASK_26_BITS);
    }

    /**
     * Used for tests.
     */
    static Location toLocation(World world, long id) {
        int x = (int) (id >> 34);
        int y = (int) ((id >> 26) & 0xFF);
        int z = (int) id;

        // Restore sign
        if ((x & (1 << 25)) > 0) {
            x |= ~MASK_26_BITS;
        } else {
            x &= MASK_26_BITS;
        }

        if ((z & (1 << 25)) > 0) {
            z |= ~MASK_26_BITS;
        } else {
            z &= MASK_26_BITS;
        }

        return new Location(world, x, y, z);
    }

    private static class StoreKey {
        private String metadataKey;
        private long block;

        private StoreKey(String metadataKey, long block) {
            this.metadataKey = metadataKey;
            this.block = block;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            StoreKey storeKey = (StoreKey) o;
            return block == storeKey.block && Objects.equal(metadataKey, storeKey.metadataKey);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(metadataKey, block);
        }
    }
}
