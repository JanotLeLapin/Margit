package dev.rocco.kig.paper.impl.skin;

import com.mojang.authlib.GameProfile;
import dev.rocco.kig.paper.api.skin.SkinCache;
import net.minecraft.server.TileEntitySkull;

import java.util.UUID;

public class CraftSkinCache implements SkinCache {
    @Override
    public void loadAndRun(String name, Runnable runnable) {
        TileEntitySkull.b(new GameProfile(UUID.randomUUID(), name), $ -> {
            runnable.run();
            return false;
        });
    }
}
