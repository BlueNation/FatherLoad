package com.github.BlueNation.FatherLoad.world;

import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import java.util.Arrays;
import java.util.List;

import static com.github.BlueNation.FatherLoad.Reference.PLAY_LEVEL;

public class SLChunkProvider implements IChunkProvider {
    private final World worldObj;
    private final Block[] cachedBlockIDs = new Block[256];
    private final byte[] cachedBlockMetadata = new byte[256];

    public SLChunkProvider(World worldObj) {
        this.worldObj = worldObj;
        Arrays.fill(cachedBlockIDs, Blocks.air);
        Arrays.fill(cachedBlockMetadata, (byte) 0);
        cachedBlockIDs[PLAY_LEVEL] = Blocks.stone;
    }

    //Every chunk will exist, thus always true
    @Override
    public boolean chunkExists(int chunkPosX, int chunkPosY) {
        return true;
    }

    //Will return back a chunk, if it doesn't exist and its not a MP client
    //it will generates all the blocks for the specified chunk from the map seed and chunk seed
    @Override
    public Chunk provideChunk(int chunkPosX, int chunkPosY) {
        Chunk chunk = new Chunk(this.worldObj, chunkPosX, chunkPosY);
        int l;

        for (int k = 0; k < this.cachedBlockIDs.length; ++k) {
            Block block = this.cachedBlockIDs[k];

            if (block != null) {
                l = k >> 4;
                ExtendedBlockStorage extendedblockstorage = chunk.getBlockStorageArray()[l];

                if (extendedblockstorage == null) {
                    extendedblockstorage = new ExtendedBlockStorage(k, !this.worldObj.provider.hasNoSky);
                    chunk.getBlockStorageArray()[l] = extendedblockstorage;
                }

                for (int i1 = 0; i1 < 16; ++i1) {
                    for (int j1 = 0; j1 < 16; ++j1) {
                        extendedblockstorage.func_150818_a(i1, k & 15, j1, block);
                        extendedblockstorage.setExtBlockMetadata(i1, k & 15, j1, this.cachedBlockMetadata[k]);
                    }
                }
            }
        }

        chunk.generateSkylightMap();
        BiomeGenBase[] abiomegenbase = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(null,
                chunkPosX * 16, chunkPosY * 16, 16, 16);
        byte[] abyte = chunk.getBiomeArray();

        for (l = 0; l < abyte.length; ++l) {
            abyte[l] = (byte) abiomegenbase[l].biomeID;
        }

        chunk.generateSkylightMap();
        return chunk;
    }

    //Loads or generates the chunk at the chunk location specified
    @Override
    public Chunk loadChunk(int chunkPosX, int chunkPosY) {
        return this.provideChunk(chunkPosX, chunkPosY);
    }

    //Here we can populate the chunk with ores etc etc
    @Override
    public void populate(IChunkProvider chunkProvider, int chunkPosX, int chunkPosY) {
    }

    //Two modes of operation: if passed true, save all Chunks in one go. If passed false, save up to two chunks.
    //Return true if all chunks have been saved.
    @Override
    public boolean saveChunks(boolean p_73151_1_, IProgressUpdate p_73151_2_) {
        return true;
    }

    //Unloads chunks that are marked to be unloaded. This is not guaranteed to unload every such chunk.
    @Override
    public boolean unloadQueuedChunks() {
        return false;
    }

    //Returns if the IChunkProvider supports saving.
    @Override
    public boolean canSave() {
        return true;
    }

    //Converts the instance data to a readable string.
    @Override
    public String makeString() {
        return "SidewaysLandSource";
    }

    @Override
    public List getPossibleCreatures(EnumCreatureType p_73155_1_, int p_73155_2_, int p_73155_3_, int p_73155_4_) {
        return null;
    }

    //Some stronghold function, always null since we don't do that here
    @Override
    public ChunkPosition func_147416_a(World worldObj, String str, int int_1, int int_2, int int_3) {
        return null;
    }

    //Not Implemented anyway
    @Override
    public int getLoadedChunkCount() {
        return 0;
    }

    @Override
    public void recreateStructures(int chunkPosX, int chunkPosY) {
    }

    @Override
    public void saveExtraData() {
    }
}
