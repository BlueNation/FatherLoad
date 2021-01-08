package com.github.BlueNation.FatherLoad.world;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.DimensionManager;

import static com.github.BlueNation.FatherLoad.Reference.SIDEWAYS_LAND_WORLD_ID;

/**
 * The actual mini game world
 */
public class SidewaysLandWorldProvider extends WorldProvider {
    public static final int PLAY_LEVEL = 96;
    public static final int PLAY_LEVEL_TOP = PLAY_LEVEL + 4;//exclusive like iterators

    @Override
    public String getDimensionName() {
        return getClass().getSimpleName();
    }

    @Override
    public int getAverageGroundLevel() {
        return PLAY_LEVEL;
    }

    @Override
    public void registerWorldChunkManager() {
        this.worldChunkMgr = new SidewaysLandWorldChunkManager(worldObj.getSeed(), terrainType);
        this.dimensionId = SIDEWAYS_LAND_WORLD_ID;
        this.hasNoSky = false;
    }

    @Override
    public IChunkProvider createChunkGenerator() {
        return new SidewaysLandChunkProvider(worldObj, dimensionId, "");
    }

    public static WorldProvider getProviderForDimension(int id) {
        return DimensionManager.createProviderFor(SIDEWAYS_LAND_WORLD_ID);
    }

    @Override
    public boolean canRespawnHere() {
        return false;
    }

    @Override
    public int getRespawnDimension(EntityPlayerMP player) {
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getWelcomeMessage() {
        return "Welcome to FatherLoad!";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getDepartMessage() {
        return "Farewell to FatherLoad!";
    }
}
