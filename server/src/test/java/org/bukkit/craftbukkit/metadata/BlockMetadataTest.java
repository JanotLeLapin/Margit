package org.bukkit.craftbukkit.metadata;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataStore;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

public class BlockMetadataTest {
    @Test
    public void oldNewComparison() {
        MetadataStore<Block> oldStore = new BlockMetadataStore(null);
        MetadataStore<Block> newStore = new SpecializedBlockMetadataStore(null);
        Plugin plugin = Mockito.mock(Plugin.class);

        for (int x = 2, y = 0, z = 2, i = 0; i < 20; i++, x += 300, z += 200, y += 10) {
            Block block = makeBlock(i % 2 == 0 ? x : -x, y, i % 2 == 0 ? z : -z);
            MetadataValue value = new FixedMetadataValue(plugin, i);

            oldStore.setMetadata(block, "test", value);
            oldStore.setMetadata(block, "test" + i, value);
            newStore.setMetadata(block, "test", value);
            newStore.setMetadata(block, "test" + i, value);

            Assert.assertTrue(newStore.hasMetadata(block, "test"));
            Assert.assertTrue(newStore.hasMetadata(block, "test" + i));

            List<MetadataValue> test = newStore.getMetadata(block, "test");
            Assert.assertEquals(test, oldStore.getMetadata(block, "test"));
            Assert.assertTrue("value in newStore[\"test\"]", test.contains(value));

            List<MetadataValue> indexTest = newStore.getMetadata(block, "test" + i);
            Assert.assertEquals(indexTest, oldStore.getMetadata(block, "test" + i));
            Assert.assertTrue("value in newStore[\"test\" + i]", indexTest.contains(value));
        }
    }

    @Test
    public void compareIds() {
        for (int x = 3, y = 1, z = 2, i = 0; i < 20; i++, x += 300, z += 200, y += 10) {
            Block block = makeBlock(i % 2 == 0 ? x : -x, y, i % 2 == 0 ? z : -z);
            Location location = new Location(null, block.getX(), block.getY(), block.getZ());

            Assert.assertEquals(location, SpecializedBlockMetadataStore.toLocation(null,
                    SpecializedBlockMetadataStore.getBlockId(block)));
        }
    }

    private static Block makeBlock(int x, int y, int z) {
        Block block = Mockito.mock(Block.class);
        Mockito.when(block.getX()).thenReturn(x);
        Mockito.when(block.getY()).thenReturn(y);
        Mockito.when(block.getZ()).thenReturn(z);
        return block;
    }
}
