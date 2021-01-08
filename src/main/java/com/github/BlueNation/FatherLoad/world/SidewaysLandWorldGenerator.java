package com.github.BlueNation.FatherLoad.world;

import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

import java.util.Random;

import static com.github.BlueNation.FatherLoad.Reference.SIDEWAYS_LAND_WORLD_ID;

public class SidewaysLandWorldGenerator implements IWorldGenerator {
    private static final int CHUNKSIZE = 16;


    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
        if (world.provider.dimensionId == SIDEWAYS_LAND_WORLD_ID) {
            generateSidewaysLand(chunkX * CHUNKSIZE, chunkZ * CHUNKSIZE, world);
        }
    }

    private void generateSidewaysLand(int chunkX, int chunkZ, World worldIn) {

    }
}
