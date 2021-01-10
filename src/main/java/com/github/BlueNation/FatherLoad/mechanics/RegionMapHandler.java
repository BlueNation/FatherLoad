package com.github.BlueNation.FatherLoad.mechanics;

/**
 * Abstracts region slices into maps
 */
public class RegionMapHandler {
    public static final int REGION_BLOCKS_WIDTH =512;
    public static final int REGION_CHUNKS_WIDTH = REGION_BLOCKS_WIDTH /16;
    public static final int REGION_BIG_BLOCKS_WIDTH=REGION_BLOCKS_WIDTH/BigBlockHandler.BLOCK_SIZE;
}
