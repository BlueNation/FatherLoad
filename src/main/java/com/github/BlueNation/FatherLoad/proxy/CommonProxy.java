package com.github.BlueNation.FatherLoad.proxy;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class CommonProxy {
    public void bigBlockBreakingParticle(World w, double x, double y, double z, ForgeDirection side, int duration){}

    public void sparkParticle(World w, double hitX, double hitY, double hitZ, ForgeDirection side) {}
    public void hotParticle(World w, double hitX, double hitY, double hitZ, ForgeDirection side) {}
    public void dustParticle(World w, double hitX, double hitY, double hitZ, ForgeDirection side, Block block, int meta){}

    public void renderDrillingParticlesOn(EntityLivingBase player){}
    public void renderDrillingParticlesOn(World w,double hitX, double hitY, double hitZ, ForgeDirection side, Block block, int meta){}
}
