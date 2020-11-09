package com.github.BlueNation.FatherLoad.world;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderFlat;

/**
 * Chunk provider
 */
public class SidewaysLandChunkProvider extends ChunkProviderFlat {
    public SidewaysLandChunkProvider(World world,
                                     long seed,
                                     String flatGeneratorString) {
        super(world, seed, false, flatGeneratorString);
    }

    @Override
    public Chunk provideChunk(int p_73154_1_, int p_73154_2_) {
        return super.provideChunk(p_73154_1_, p_73154_2_);
    }

    @Override
    public void populate(IChunkProvider p_73153_1_, int p_73153_2_, int p_73153_3_) {
        super.populate(p_73153_1_, p_73153_2_, p_73153_3_);
    }
}
