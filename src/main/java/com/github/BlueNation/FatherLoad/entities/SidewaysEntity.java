package com.github.BlueNation.FatherLoad.entities;

import net.minecraft.world.World;

public class SidewaysEntity extends SimpleEntity{
    public SidewaysEntity(World worldObj) {
        super(worldObj);
    }

    @Override
    protected void applyGravity() {
        if (this.isGravityEnabled) {
            this.motionX += this.gravityAccel;
        }
    }

    @Override
    protected void applyForwardVelocity() {
        this.motionZ = this.forwardVelocity;
    }

    @Override
    protected void setYaw(float yaw) {
        float newYaw;
        if (yaw < 180F) {
            newYaw = 0;
        } else {
            newYaw = 180F;
        }
        this.setRotation(this.normalizeDegrees(newYaw), 0);
    }
}
