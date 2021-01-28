package com.github.BlueNation.FatherLoad.mechanics;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

/**
 * Abstracts 4/4/4 blocks to 1/1/1 blocks and back
 */
public class BigBlockHandler {
    public static final int SHIFT_VALUE=2;
    public static final int BLOCK_SIZE=1<<SHIFT_VALUE;
    public static final int BLOCK_HALF_SIZE=BLOCK_SIZE>>1;
    public static final int OFFSET_PROBE=1;

    public static int getAligned(int xyz){
        return (xyz>>SHIFT_VALUE)<<SHIFT_VALUE;
    }

    public static int getAligned(double xyz){
        return ((int)xyz>>SHIFT_VALUE)<<SHIFT_VALUE;
    }

    public static int[] getAlignedXYZ(int x, int y, int z){
        return new int[]{getAligned(x),getAligned(y),getAligned(z)};
    }

    public static int[] getABC(int x, int y, int z){
        return new int[]{x>>SHIFT_VALUE,y>>SHIFT_VALUE,z>>SHIFT_VALUE};
    }

    public static int[] getCorner(int a,int b,int c){
        return new int[]{a<<SHIFT_VALUE,b<<SHIFT_VALUE,c<<SHIFT_VALUE};
    }

    public static int[] getProbe(int a,int b,int c){
        return new int[]{a<<SHIFT_VALUE+OFFSET_PROBE,b<<SHIFT_VALUE+OFFSET_PROBE,c<<SHIFT_VALUE+OFFSET_PROBE};
    }

    public static int[] getRange(int a,int b,int c){
        return new int[]{a<<SHIFT_VALUE,b<<SHIFT_VALUE,c<<SHIFT_VALUE,
                (a<<SHIFT_VALUE)+BLOCK_SIZE,(b<<SHIFT_VALUE)+BLOCK_SIZE,(c<<SHIFT_VALUE)+BLOCK_SIZE};
    }

    public static AxisAlignedBB getBox(int a,int b,int c){
        return AxisAlignedBB.getBoundingBox(a<<SHIFT_VALUE,b<<SHIFT_VALUE,c<<SHIFT_VALUE,
                (a<<SHIFT_VALUE)+BLOCK_SIZE,(b<<SHIFT_VALUE)+BLOCK_SIZE,(c<<SHIFT_VALUE)+BLOCK_SIZE);
    }

    public static void setBigBlock(World world,int a, int b, int c, Block block, int metaId){
        forEachBlockInBigBlock(world,a,b,c,(w,x,y,z)-> w.setBlock(x,y,z,block,metaId,3));
    }

    /**
     * would require ID per block, better to create entity (FX)
     * @param world
     * @param a
     * @param b
     * @param c
     * @param player
     * @param damage
     */
    @Deprecated
    public static void destroyBigBlockInWorldPartially(World world,int a, int b, int c,int player,int damage){
        forEachBlockInBigBlock(world,a,b,c,(w,x,y,z)-> w.destroyBlockInWorldPartially(player,x,y,z,damage));
    }

    public static void setBigBlockToAir(World world,int a, int b, int c){
        forEachBlockInBigBlock(world,a,b,c, World::setBlockToAir);
    }

    public static Block getBigBlock(World world,int a, int b, int c){
        return withProbeCoordinates(world,a,b,c, World::getBlock);
    }

    public static int getBigBlockMetadata(World world,int a, int b, int c){
        return withProbeCoordinates(world,a,b,c, World::getBlockMetadata);
    }

    public static ItemStack getBigBlockStack(World world,int a, int b, int c){
        return withProbeCoordinates(world,a,b,c,(World world1, int x, int y, int z) ->
                new ItemStack(world1.getBlock(x,y,z),world1.getBlockMetadata(x,y,z)));
    }

    public static <T> T withProbeCoordinates(World world,int a, int b, int c, IBlockPositionFunction<T> function){
        return function.apply(world,
                (a<<SHIFT_VALUE)+OFFSET_PROBE,
                (b<<SHIFT_VALUE)+OFFSET_PROBE,
                (c<<SHIFT_VALUE)+OFFSET_PROBE);
    }

    public static void withProbeCoordinates(World world,int a, int b, int c, IBlockPositionConsumer consumer){
        consumer.consume(world,
                (a<<SHIFT_VALUE)+OFFSET_PROBE,
                (b<<SHIFT_VALUE)+OFFSET_PROBE,
                (c<<SHIFT_VALUE)+OFFSET_PROBE);
    }

    public static void forEachBlockInBigBlock(World world,int a, int b, int c, IBlockPositionConsumer consumer){
        int x=a<<SHIFT_VALUE;
        int y=b<<SHIFT_VALUE;
        int z=c<<SHIFT_VALUE;
        int xEnd=x+BLOCK_SIZE;
        int yEnd=y+BLOCK_SIZE;
        int zEnd=z+BLOCK_SIZE;

        for (; x < xEnd; x++) {
            for (; y < yEnd; y++) {
                for (; z < zEnd; z++) {
                    consumer.consume(world,x,y,z);
                }
                z-=BLOCK_SIZE;
            }
            y-=BLOCK_SIZE;
        }
    }
}
