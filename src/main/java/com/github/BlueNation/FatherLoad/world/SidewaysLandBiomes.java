package com.github.BlueNation.FatherLoad.world;

import net.minecraft.world.biome.BiomeGenBase;

public class SidewaysLandBiomes {
    public static BiomeGenBase instanceOfBiomeGenSidewaysLandSurface;
    public static final int idBiomeGenSidewaysLandSurface = 141;

    public static void mainRegistry() {
        init();
    }

    private static void init() {
        instanceOfBiomeGenSidewaysLandSurface = new SidewaysLandSurfaceBiomeGen(idBiomeGenSidewaysLandSurface)
                .setBiomeName("Prehistoric Plains").setHeight(new BiomeGenBase.Height(0.18F, 0.05F));
    }
}
