package com.github.BlueNation.FatherLoad.world;

import net.minecraft.world.WorldProvider;

/**
 * The actual mini game world
 */
public class SidewaysLand extends WorldProvider {
    public static final int GROUND_LEVEL=96;
    public static final int PLAY_LEVEL=GROUND_LEVEL-2;
    public static final int PLAY_LEVEL_TOP=GROUND_LEVEL+2;//exclusive like iterators

    @Override
    public String getDimensionName() {
        return getClass().getSimpleName();
    }

    @Override
    public int getAverageGroundLevel() {
        return PLAY_LEVEL;
    }
}
