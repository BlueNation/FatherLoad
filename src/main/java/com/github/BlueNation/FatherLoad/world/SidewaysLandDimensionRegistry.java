package com.github.BlueNation.FatherLoad.world;

import net.minecraftforge.common.DimensionManager;

import static com.github.BlueNation.FatherLoad.Reference.SIDEWAYS_LAND_WORLD_ID;

public class SidewaysLandDimensionRegistry {

    public static void mainRegistry() {
        registerDimension();
    }

    private static void registerDimension() {
        DimensionManager.registerProviderType(SIDEWAYS_LAND_WORLD_ID, SidewaysLandWorldProvider.class, false);
        DimensionManager.registerDimension(SIDEWAYS_LAND_WORLD_ID, SIDEWAYS_LAND_WORLD_ID);
    }
}