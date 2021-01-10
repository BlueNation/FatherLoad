package com.github.BlueNation.FatherLoad.world;

import net.minecraft.world.biome.BiomeGenBase;

import static com.github.BlueNation.FatherLoad.Reference.SIDEWAYS_LAND_SURFACE_BIOME_ID;

public class SLBiomes {
    public static BiomeGenBase instanceOfBiomeGenSidewaysLandSurface;

    public static void mainRegistry() {
        init();
    }

    private static void init() {
        instanceOfBiomeGenSidewaysLandSurface = new SLSurfaceBiomeGen(SIDEWAYS_LAND_SURFACE_BIOME_ID)
                .setBiomeName("Sideways Land Surface").setHeight(new BiomeGenBase.Height(0.18F, 0.05F));
    }
}
