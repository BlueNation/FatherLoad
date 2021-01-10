package com.github.BlueNation.FatherLoad.world;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkProvider;

import static com.github.BlueNation.FatherLoad.Reference.*;

public class SLWorldProvider extends WorldProvider {
    @Override
    public String getDimensionName() {
        return SIDEWAYS_LAND_NAME;
    }

    @Override
    public int getAverageGroundLevel() {
        return PLAY_LEVEL;
    }

    @Override
    public void registerWorldChunkManager() {
        this.worldChunkMgr = new SLWorldChunkManager(worldObj.getSeed(), terrainType);
        this.dimensionId = SIDEWAYS_LAND_WORLD_ID;
        this.hasNoSky = false;
    }

    @Override
    public IChunkProvider createChunkGenerator() {
        return new SLChunkProvider(worldObj);
    }

    @Override
    public boolean canRespawnHere() {
        return false;
    }

    @Override
    public int getRespawnDimension(EntityPlayerMP player) {
        return 0;
    }
}
