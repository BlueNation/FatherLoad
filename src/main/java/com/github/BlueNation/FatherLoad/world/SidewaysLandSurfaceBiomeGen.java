package com.github.BlueNation.FatherLoad.world;

import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;

public class SidewaysLandSurfaceBiomeGen extends BiomeGenBase {
    public SidewaysLandSurfaceBiomeGen(int biomeId) {
        super(biomeId);
        this.setTemperatureRainfall(1.2F, 0.9F);
        this.setColor(2900485);
        this.topBlock = Blocks.grass;
        this.fillerBlock = Blocks.dirt;
        this.enableRain = true;
        this.theBiomeDecorator.generateLakes = true;

        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();

        this.theBiomeDecorator.treesPerChunk = -999;
        this.theBiomeDecorator.waterlilyPerChunk = 16;
        this.theBiomeDecorator.reedsPerChunk = 16;
    }
}
