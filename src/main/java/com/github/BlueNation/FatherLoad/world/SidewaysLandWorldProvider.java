package com.github.BlueNation.FatherLoad.world;

import net.minecraft.world.WorldProvider;

/**
 * The actual mini game world
 */
public class SidewaysLandWorldProvider extends WorldProvider {
    public static final int PLAY_LEVEL=96;
    public static final int PLAY_LEVEL_TOP=PLAY_LEVEL+4;//exclusive like iterators

    @Override
    public String getDimensionName() {
        return getClass().getSimpleName();
    }

    @Override
    public int getAverageGroundLevel() {
        return PLAY_LEVEL;
    }
}
