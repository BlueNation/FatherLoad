package com.github.BlueNation.FatherLoad.world;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerVoronoiZoom;
import net.minecraft.world.gen.layer.GenLayerZoom;

public class SidewaysLandGenerateLayer extends GenLayer {
    public SidewaysLandGenerateLayer(long seed) {
        super(seed);
    }

    @Override
    public int[] getInts(int p_75904_1_, int p_75904_2_, int p_75904_3_, int p_75904_4_) {
        return new int[0];
    }

    public static GenLayer[] makeTheWorld(long seed) {
        GenLayer biomes = new SidewaysLandGenerateLayerBiomes(1L);

        // more GenLayerZoom = bigger biomes
        biomes = new GenLayerZoom(1000L, biomes);
        biomes = new GenLayerZoom(1001L, biomes);
        biomes = new GenLayerZoom(1002L, biomes);
        biomes = new GenLayerZoom(1003L, biomes);

        GenLayer genlayervoronoizoom = new GenLayerVoronoiZoom(10L, biomes);

        biomes.initWorldGenSeed(seed);
        genlayervoronoizoom.initWorldGenSeed(seed);

        return new GenLayer[] {biomes, genlayervoronoizoom};
    }
}
