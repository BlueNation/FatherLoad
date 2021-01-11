package com.github.BlueNation.FatherLoad.thing.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class BrightParticleFX extends EntityFX
{
    public BrightParticleFX(World world, double x, double y, double z, double dX, double dY, double dZ) {
        super(world, x, y, z, dX, dY, dZ);
        this.motionX = dX + (double)((float)(Math.random() * 2.0D - 1.0D) * 0.05F);
        this.motionY = dY + (double)((float)(Math.random() * 2.0D - 1.0D) * 0.05F);
        this.motionZ = dZ + (double)((float)(Math.random() * 2.0D - 1.0D) * 0.05F);
        this.particleScale = this.rand.nextFloat() * this.rand.nextFloat() * 6.0F + 1.0F;
        this.particleMaxAge = (int)(16.0D / ((double)this.rand.nextFloat() * 0.8D + 0.2D)) + 2;
        this.noClip=true;
    }

    public BrightParticleFX withRandomGray(){
        particleRed = particleGreen = particleBlue = rand.nextFloat() * 0.3F + 0.7F;
        return this;
    }

    public BrightParticleFX withColorTint(float r, float g, float b, float a){
        particleRed=r;
        particleGreen=g;
        particleBlue=b;
        return this;
    }

    public BrightParticleFX withScale(float scale){
        particleScale=scale;
        return this;
    }

    public BrightParticleFX withColorTint(float r, float g, float b){
        return withColorTint(r, g, b,255);
    }

    public int getBrightnessForRender(float p_70070_1_) {
        return 15728880;
    }

    public float getBrightness(float p_70013_1_) {
        return 1.0F;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge++ >= this.particleMaxAge)
        {
            this.setDead();
        }

        this.setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);
        //this.motionY += 0.004D;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.8999999761581421D;
        this.motionY *= 0.8999999761581421D;
        this.motionZ *= 0.8999999761581421D;
    }
}
