package com.github.BlueNation.FatherLoad.entities;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class SimpleEntity extends Entity {
    protected boolean isAttackable = false;
    protected boolean isPushable = false;
    protected boolean isCollidable = false;
    protected boolean isWalkingTrigger = false;

    protected double gravityAccel = 9.81 / 20; //per tick
    protected double forwardVelocity = 0;

    public SimpleEntity(World worldObj) {
        super(worldObj);
    }

    /**
     * Runs once on entity init
     */
    @Override
    protected void entityInit() {
    }

    /**
     * Called when the entity is attacked.
     *
     * @param damageSource Source of damage
     * @param damageValue  Value of damage in hit points
     * @return Has the Entity been affected by the damage, used to consume durability on weapons
     */
    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float damageValue) {
        this.setDead();
        return true;
    }

    /**
     * Returns the bounding box for this entity, used for entities colliding with each other
     *
     * @return Collision box, can be null for non-solid Entities
     */
    @Override
    public AxisAlignedBB getBoundingBox() {
        AxisAlignedBB boundingBox = null;
        if (this.isCollidable && !this.isDead) {
            boundingBox = this.boundingBox;
        }
        return boundingBox;
    }

    //TODO what is this for??
    @Override
    public AxisAlignedBB getCollisionBox(Entity entity) {
        return entity.getBoundingBox();
    }

    protected void applyForwardVelocity() {
        this.motionX = this.forwardVelocity * Math.sin(degToRad(this.rotationYaw));
        this.motionZ = this.forwardVelocity * Math.cos(degToRad(this.rotationYaw));
    }

    protected void applyGravity() {
        if (!this.onGround) {
            this.motionY -= gravityAccel;
        }
    }

    /**
     * Applies motion
     */
    protected void applyMotion() {
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
    }

    /**
     * Rotates entity
     */
    protected void rotateEntity(float yawOffset) {
        setRotation(this.rotationYaw + yawOffset, 0);
    }

    /**
     * Returns true if the Entity can be attacked
     *
     * @return Can be attacked
     */
    @Override
    public boolean canBeCollidedWith() {
        return this.isAttackable && !this.isDead;
    }


    //TODO what is this for??
    @Override
    public boolean canBePushed() {
        return this.isPushable && !this.isDead;
    }

    //TODO what is this for??
    @Override
    protected boolean canTriggerWalking() {
        return this.isWalkingTrigger && !this.isDead;
    }

    protected float degToRad(float deg) {
        return (float) (deg * Math.PI / 180);
    }

    /**
     * Helper method to read subclass entity data from NBT.
     *
     * @param nbtTagCompound NBT Tag to read
     */
    @Override
    protected void readEntityFromNBT(NBTTagCompound nbtTagCompound) {
    }

    /**
     * Helper method to write subclass entity data to NBT.
     *
     * @param nbtTagCompound NBT Tag to write
     */
    @Override
    protected void writeEntityToNBT(NBTTagCompound nbtTagCompound) {
    }
}
