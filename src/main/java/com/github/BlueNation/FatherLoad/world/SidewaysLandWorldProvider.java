package com.github.BlueNation.FatherLoad.world;

import com.github.BlueNation.FatherLoad.mechanics.BigBlockHandler;
import net.minecraft.world.WorldProvider;

/**
 * The actual mini game world
 */
public class SidewaysLandWorldProvider extends WorldProvider {
    public static final int PLAY_LEVEL=64;
    public static final int PLAY_LEVEL_TOP=PLAY_LEVEL+ BigBlockHandler.BLOCK_SIZE;//exclusive like iterators

    @Override
    public String getDimensionName() {
        return getClass().getSimpleName();
    }

    @Override
    public int getAverageGroundLevel() {
        return PLAY_LEVEL;
    }
}
