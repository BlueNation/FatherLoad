package com.github.BlueNation.FatherLoad.entities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntityDriller extends SidewaysEntity {
    protected double thustAccel = 40 / 20;

    public EntityDriller(World worldObj) {
        super(worldObj);

        //Calculates and sets `this.boundingBox`
        //This is what will show up in-game if you use F3+B
        this.setSize(4F, 4F);

        this.isAttackable = true;
        this.isPushable = false;
        this.canCollide = false;
        this.canTrampleCrops = false;
        this.isGravityEnabled = true;
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate() {
        if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityLivingBase) {
            EntityLivingBase entity = (EntityLivingBase) this.riddenByEntity;

            if (entity.moveStrafing > 0) {
                this.setYaw(-180F);
                this.forwardVelocity = 0.25D;
            } else if (entity.moveStrafing < 0) {
                this.setYaw(180F);
                this.forwardVelocity = -0.25D;
            } else {
                this.forwardVelocity *= 0.7D;
            }

            if (entity.moveForward > 0) {
                applyThruster();
            }
        }
        super.onUpdate();
    }

    @Override
    public double getMountedYOffset() {
        return (double) this.height * 0.0D - 0.3D;
    }

    @Override
    public void updateRiderPosition() {
        if (this.riddenByEntity != null) {
            double d0 = Math.cos((double) this.rotationYaw * Math.PI / 180.0D) * 0.4D;
            double d1 = Math.sin((double) this.rotationYaw * Math.PI / 180.0D) * 0.4D;
            this.riddenByEntity.setPosition(this.posX + d0, this.posY + this.getMountedYOffset() +
                    this.riddenByEntity.getYOffset(), this.posZ + d1);
        }
    }

    @Override
    public boolean interactFirst(EntityPlayer player) {
        if (this.riddenByEntity == null || !(this.riddenByEntity instanceof EntityPlayer) ||
                this.riddenByEntity == player) {
            if (!this.worldObj.isRemote) {
                player.mountEntity(this);
            }
        }
        return true;
    }

    protected void applyThruster() {
        this.motionX -= this.thustAccel;
    }
}
