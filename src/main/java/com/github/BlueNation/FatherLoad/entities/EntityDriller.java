package com.github.BlueNation.FatherLoad.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityDriller extends Entity {
    protected final boolean isAttackable;
    protected final boolean isPushable;
    protected final boolean isCollidable;
    protected final boolean isWalkingTrigger;

    protected double forwardVelocity = 0;

    public EntityDriller(World worldObj) {
        super(worldObj);

        //Calculates and sets `this.boundingBox`
        //This is what will show up in-game if you use F3+B
        this.setSize(5F, 3F);

        isAttackable = true;
        isPushable = false;
        isCollidable = false;
        isWalkingTrigger = false;
    }

    /**
     * Called when the entity is attacked.
     *
     * @param damageSource Source of damage
     * @param damageValue Value of damage in hit points
     * @return Has the Entity been affected by the damage, used to consume durability on weapons(?)
     */
    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float damageValue) {
        this.setDead();
        return true;
    }

    /**
     * Returns the bounding box for this entity, used for entities colliding with each other
     *
     * @return Collision box, can be null for non-solid Entites
     */
    @Override
    public AxisAlignedBB getBoundingBox() {
        AxisAlignedBB boundingBox = null;
        if (this.isCollidable && !this.isDead) {
            boundingBox = this.boundingBox;
        }
        return boundingBox;
    }

    @Override
    public AxisAlignedBB getCollisionBox(Entity ent) {
        //return null;
        return ent.getBoundingBox();
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {
    }

    @Override
    protected void entityInit() {
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate() {
        super.onUpdate();

        if (!this.onGround) {
            this.forwardVelocity = 0;
            this.motionY -= 9.81 / 20;
        } else {
            if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityLivingBase) {
                EntityLivingBase ent = (EntityLivingBase) this.riddenByEntity;

                if (ent.moveForward > 0) {
                    this.forwardVelocity = 0.25;
                } else if (ent.moveForward < 0) {
                    this.forwardVelocity = -0.25;
                }

                if (ent.moveStrafing > 0) {
                    this.rotateEntity(5);
                } else if (ent.moveStrafing < 0) {
                    this.rotateEntity(-5);
                }
            }
        }
        this.applyForwardVelocity();
        this.applyMotion();
    }

    protected void applyForwardVelocity() {
        this.motionX = this.forwardVelocity * Math.sin(degToRad(this.rotationYaw));
        this.motionZ = this.forwardVelocity * Math.cos(degToRad(this.rotationYaw));
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

    @Override
    public boolean canBePushed() {
        return this.isPushable && !this.isDead;
    }

    //Don't trigger walking, eg redstone ore or crops
    @Override
    protected boolean canTriggerWalking() {
        return this.isWalkingTrigger && !this.isDead;
    }

    protected float degToRad(float deg) {
        return (float) (deg * Math.PI / 180);
    }

    public double getMountedYOffset() {
        return (double) this.height * 0.0D - 0.30000001192092896D;
    }

    @Override
    public void updateRiderPosition() {
        if (this.riddenByEntity != null) {
            double d0 = Math.cos((double)this.rotationYaw * Math.PI / 180.0D) * 0.4D;
            double d1 = Math.sin((double)this.rotationYaw * Math.PI / 180.0D) * 0.4D;
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
}
