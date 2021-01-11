package com.github.BlueNation.FatherLoad.proxy;

import com.github.BlueNation.FatherLoad.FatherLoad;
import com.github.BlueNation.FatherLoad.mechanics.BigBlockHandler;
import com.github.BlueNation.FatherLoad.thing.entity.BigBlockBreakingFX;
import com.github.BlueNation.FatherLoad.thing.entity.BrightParticleFX;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityBlockDustFX;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import static com.github.BlueNation.FatherLoad.FatherLoad.RANDOM;

public class ClientProxy extends CommonProxy {
    @Override
    public void bigBlockBreakingParticle(World w, double x, double y, double z, ForgeDirection side, int duration) {
        Minecraft.getMinecraft().effectRenderer.addEffect(new BigBlockBreakingFX(w,
                BigBlockHandler.getAligned(x),
                BigBlockHandler.getAligned(y),
                BigBlockHandler.getAligned(z),
                side,duration));
    }

    @Override
    public void sparkParticle(World w, double hitX, double hitY, double hitZ, ForgeDirection side){
        Minecraft.getMinecraft().effectRenderer.addEffect(new BrightParticleFX(w,hitX,hitY,hitZ,
                getSpeed2(side.offsetX),
                getSpeed2(side.offsetY),
                getSpeed2(side.offsetZ))
                .withScale(.25f)
                .withColorTint(1,.2f* RANDOM.nextFloat()+.8f,RANDOM.nextFloat()*.5f));
    }

    @Override
    public void hotParticle(World w, double hitX, double hitY, double hitZ, ForgeDirection side){
        Minecraft.getMinecraft().effectRenderer.addEffect(new BrightParticleFX(w,hitX,hitY,hitZ,
                getSpeed1(side.offsetX),
                getSpeed1(side.offsetY),
                getSpeed1(side.offsetZ))
                .withScale(.75f)
                .withColorTint(1,.2f* RANDOM.nextFloat(),.2f* RANDOM.nextFloat()));
    }

    @Override
    public void dustParticle(World w, double hitX, double hitY, double hitZ, ForgeDirection side, Block block, int meta){
        EntityBlockDustFX entityBlockDustFX = new EntityBlockDustFX(w, hitX, hitY, hitZ,
                getSpeed3(side.offsetX),
                getSpeed3(side.offsetY),
                getSpeed3(side.offsetZ),
                block, meta);
        entityBlockDustFX.particleGravity=0;
        Minecraft.getMinecraft().effectRenderer.addEffect(entityBlockDustFX);
    }

    protected double getSpeed1(int offset){
        return offset==0?RANDOM.nextFloat()*.1-.05:RANDOM.nextFloat()*.3*offset;
    }

    protected double getSpeed2(int offset){
        return offset==0?RANDOM.nextFloat()*.4-.2:RANDOM.nextFloat()*.3*offset;
    }

    protected double getSpeed3(int offset){
        return offset==0?RANDOM.nextFloat()*.1-.05:RANDOM.nextFloat()*.1*offset;
    }

    @Override
    public void renderDrillingParticlesOn(EntityLivingBase player){
        MovingObjectPosition movingObjectPosition = player.rayTrace(5, 1);
        if(movingObjectPosition!=null && movingObjectPosition.typeOfHit== MovingObjectPosition.MovingObjectType.BLOCK) {
            renderDrillingParticlesOn(player.worldObj,
                    movingObjectPosition.hitVec.xCoord,
                    movingObjectPosition.hitVec.yCoord,
                    movingObjectPosition.hitVec.zCoord,
                    ForgeDirection.getOrientation(movingObjectPosition.sideHit),
                    player.worldObj.getBlock(movingObjectPosition.blockX,movingObjectPosition.blockY,movingObjectPosition.blockZ),
                    player.worldObj.getBlockMetadata(movingObjectPosition.blockX,movingObjectPosition.blockY,movingObjectPosition.blockZ));
        }
    }

    @Override
    public void renderDrillingParticlesOn(World w,double hitX, double hitY, double hitZ, ForgeDirection side, Block block, int meta){
        for (int i = 0; i < 8; i++) {
            FatherLoad.proxy.sparkParticle(w, hitX, hitY, hitZ, side);
            FatherLoad.proxy.hotParticle(w, hitX, hitY, hitZ, side);
            FatherLoad.proxy.dustParticle(w, hitX, hitY, hitZ, side, block, meta);
        }
    }
}
