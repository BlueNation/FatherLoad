package com.github.BlueNation.FatherLoad.entities;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.List;

public class SimpleEntity extends Entity {
    //Copied private fields from Entity
    protected boolean firstUpdate;
    protected int nextStepDistance;
    protected int fire;
    //Flags
    protected boolean isAttackable;
    protected boolean isPushable;
    protected boolean canCollide;
    protected boolean canTrampleCrops;
    protected boolean isWebbable;
    protected boolean isGravityEnabled;
    protected boolean isPortalable;
    protected boolean canKickDirt;
    //Collision Flags
    protected boolean isCollidedPositiveX;
    protected boolean isCollidedPositiveY;
    protected boolean isCollidedPositiveZ;
    protected boolean isCollidedNegativeX;
    protected boolean isCollidedNegativeY;
    protected boolean isCollidedNegativeZ;
    //Extra fields
    protected double gravityAccel;
    protected double forwardVelocity;

    public SimpleEntity(World worldObj) {
        super(worldObj);

        this.noClip = false;
        this.isImmuneToFire = true;
        this.isAttackable = false;
        this.isPushable = false;
        this.canCollide = false;
        this.canTrampleCrops = false;
        this.isWebbable = false;
        this.isGravityEnabled = false;
        this.isPortalable = false;
        this.canKickDirt = false;

        this.gravityAccel = 9.81 / 20;
        this.forwardVelocity = 0;

        this.stepHeight = 3;
        this.fireResistance = 1;
        this.width = 0.6F;
        this.height = 1.8F;
    }

    //region Entity Update
    /**
     * Runs once on entity init
     */
    @Override
    protected void entityInit() {
    }

    @Override
    public void onUpdate() {
        this.applyGravity();
        this.applyForwardVelocity();
        this.applyMotion();
        this.onEntityUpdate();
    }

    protected void applyGravity() {
        if (this.isGravityEnabled) {
            this.motionY -= gravityAccel;
        }
    }

    protected void applyForwardVelocity() {
        this.motionX = this.forwardVelocity * Math.sin(degToRad(this.rotationYaw));
        this.motionZ = this.forwardVelocity * Math.cos(degToRad(this.rotationYaw));
        this.forwardVelocity = 0;
    }

    protected float degToRad(float deg) {
        return (float) (deg * Math.PI / 180);
    }

    protected void applyMotion() {
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
    }

    protected void rotateEntity(float yawOffset) {
        setRotation(this.rotationYaw + yawOffset, 0);
    }

    @Override
    public void onEntityUpdate() {
        this.worldObj.theProfiler.startSection("entityBaseTick");
        this.handleRider();
        this.updatePrevious();
        this.handlePortal();
        this.kickingDirt();
        this.handleWaterMovement();
        this.handleLavaMovementUpdate();
        this.handleFire();
        this.handleVoidFall();
        this.firstUpdate = false;
        this.worldObj.theProfiler.endSection();
    }

    protected void handleRider() {
        if (this.ridingEntity != null) {
            if (this.ridingEntity.isDead) {
                this.ridingEntity = null;
            }
        }
    }

    protected void updatePrevious() {
        this.prevDistanceWalkedModified = this.distanceWalkedModified;
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.prevRotationPitch = this.rotationPitch;
        this.prevRotationYaw = this.rotationYaw;
    }

    protected void handlePortal() {
        if (this.isPortalable && !this.worldObj.isRemote && this.worldObj instanceof WorldServer) {
            this.worldObj.theProfiler.startSection("portal");
            MinecraftServer minecraftserver = ((WorldServer) this.worldObj).func_73046_m();
            int maxInPortalTime = this.getMaxInPortalTime();

            if (this.inPortal) {
                if (minecraftserver.getAllowNether()) {
                    if (this.ridingEntity == null && this.portalCounter++ >= maxInPortalTime) {
                        this.portalCounter = maxInPortalTime;
                        this.timeUntilPortal = this.getPortalCooldown();
                        byte b0;

                        if (this.worldObj.provider.dimensionId == -1) {
                            b0 = 0;
                        } else {
                            b0 = -1;
                        }

                        this.travelToDimension(b0);
                    }

                    this.inPortal = false;
                }
            } else {
                if (this.portalCounter > 0) {
                    this.portalCounter -= 4;
                }

                if (this.portalCounter < 0) {
                    this.portalCounter = 0;
                }
            }

            if (this.timeUntilPortal > 0) {
                --this.timeUntilPortal;
            }

            this.worldObj.theProfiler.endSection();
        }
    }

    protected void kickingDirt() {
        if (this.canKickDirt && this.isSprinting() && !this.isInWater()) {
            int i = MathHelper.floor_double(this.posX);
            int j = MathHelper.floor_double(this.posY - 0.20000000298023224D - (double) this.yOffset);
            int k = MathHelper.floor_double(this.posZ);
            Block block = this.worldObj.getBlock(i, j, k);

            if (block.getMaterial() != Material.air) {
                String pName = "blockcrack_" + Block.getIdFromBlock(block) + "_" +
                        this.worldObj.getBlockMetadata(i, j, k);
                double pPosX = this.posX + ((double) this.rand.nextFloat() - 0.5D) * (double) this.width;
                double pPosY = this.boundingBox.minY + 0.1D;
                double pPosZ = this.posZ + ((double) this.rand.nextFloat() - 0.5D) * (double) this.width;
                double pMotionX = -this.motionX * 4.0D;
                double pMotionY = 1.5D;
                double pMotionZ = -this.motionZ * 4.0D;
                this.worldObj.spawnParticle(pName, pPosX, pPosY, pPosZ, pMotionX, pMotionY, pMotionZ);
            }
        }
    }

    protected void handleLavaMovementUpdate() {
        if (this.handleLavaMovement()) {
            this.setOnFireFromLava();
            this.fallDistance *= 0.5F;
        }
    }

    protected void handleFire() {
        if (this.worldObj.isRemote) {
            this.fire = 0;
        } else if (this.fire > 0) {
            if (this.isImmuneToFire) {
                this.fire -= 4;

                if (this.fire < 0) {
                    this.fire = 0;
                }
            } else {
                if (this.fire % 20 == 0) {
                    this.attackEntityFrom(DamageSource.onFire, 1.0F);
                }

                --this.fire;
            }
        }

        if (!this.worldObj.isRemote) {
            this.setFlag(0, this.fire > 0);
        }

    }

    protected void handleVoidFall() {
        if (this.posY < -64.0D) {
            this.kill();
        }
    }
    //endregion

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
        if (this.canCollide && !this.isDead) {
            boundingBox = this.boundingBox;
        }
        return boundingBox;
    }

    @Override
    public AxisAlignedBB getCollisionBox(Entity entity) {
        return entity.getBoundingBox();
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

    @Override
    protected boolean canTriggerWalking() {
        return this.canTrampleCrops && !this.isDead;
    }

    //region NBT Helper
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
    //endregion

    //region Entity Movement
    @Override
    public void moveEntity(double velX, double velY, double velZ) {
        if (this.noClip) {
            handleNoclipMovement(velX, velY, velZ);
        } else {
            this.worldObj.theProfiler.startSection("move");
            double newVelX = velX;
            double newVelY = velY;
            double newVelZ = velZ;

            double[] postWebVel = handleWebbedMovement(newVelX, newVelY, newVelZ);
            newVelX = postWebVel[0];
            newVelY = postWebVel[1];
            newVelZ = postWebVel[2];

            double[] postCollisionVel = handleCollision(newVelX, newVelY, newVelZ);
            newVelX = postCollisionVel[0];
            newVelY = postCollisionVel[1];
            newVelZ = postCollisionVel[2];

            //TODO Handler for this.stepHeight

            translateEntity(newVelX, newVelY, newVelZ);
            this.worldObj.theProfiler.endSection();

            this.worldObj.theProfiler.startSection("rest");
            this.checkCollision(velX, velY, velZ, newVelX, newVelY, newVelZ);
            this.checkGrounded();
            //TODO Handler for this.canTriggerWalking()
            //TODO Handler for fire (Moving into fire/lava or Water)
            this.worldObj.theProfiler.endSection();
        }
    }

    protected void handleNoclipMovement(double velX, double velY, double velZ) {
        this.translateEntity(velX, velY, velZ);
    }

    protected double[] handleWebbedMovement(double velX, double velY, double velZ) {
        double newVelX = velX;
        double newVelY = velY;
        double newVelZ = velZ;

        if (this.isInWeb) {
            this.isInWeb = false;
            if (this.isWebbable) {
                this.motionX = 0D;
                this.motionY = 0D;
                this.motionZ = 0D;
                newVelX = velX * 0.25D;
                newVelY = velY * 0.05D;
                newVelZ = velZ * 0.25D;
            }
        }
        return new double[]{newVelX, newVelY, newVelZ};
    }

    protected double[] handleCollision(double velX, double velY, double velZ) {
        double newVelX = velX;
        double newVelY = velY;
        double newVelZ = velZ;

        @SuppressWarnings("unchecked")
        List<AxisAlignedBB> list = this.worldObj.getCollidingBoundingBoxes(this,
                this.boundingBox.addCoord(newVelX, newVelY, newVelZ));

        for (AxisAlignedBB bb : list) {
            newVelX = bb.calculateXOffset(this.boundingBox, newVelX);
            newVelY = bb.calculateYOffset(this.boundingBox, newVelY);
            newVelZ = bb.calculateZOffset(this.boundingBox, newVelZ);
        }
        return new double[]{newVelX, newVelY, newVelZ};
    }

    protected void translateEntity(double velX, double velY, double velZ) {
        this.boundingBox.offset(velX, velY, velZ);
        this.posX += velX;
        this.posY += velY;
        this.posZ += velZ;
    }

    protected void checkCollision(double velX, double velY, double velZ, double newVelX, double newVelY,
                                  double newVelZ) {
        this.isCollidedPositiveX = false;
        this.isCollidedPositiveY = false;
        this.isCollidedPositiveZ = false;
        this.isCollidedNegativeX = false;
        this.isCollidedNegativeY = false;
        this.isCollidedNegativeZ = false;

        if (velX != newVelX) {
            this.motionX = 0D;
            if (velX > 0) {
                this.isCollidedPositiveX = true;
            } else {
                this.isCollidedNegativeX = true;
            }
        }
        if (velY != newVelY) {
            this.motionY = 0D;
            if (velY > 0) {
                this.isCollidedPositiveY = true;
            } else {
                this.isCollidedNegativeY = true;
            }
        }
        if (velZ != newVelZ) {
            this.motionZ = 0D;
            if (velZ > 0) {
                this.isCollidedPositiveZ = true;
            } else {
                this.isCollidedNegativeZ = true;
            }
        }

        this.isCollidedHorizontally = this.isCollidedPositiveZ || this.isCollidedNegativeX || this.isCollidedNegativeY
                || this.isCollidedNegativeZ;
        this.isCollidedVertically = this.isCollidedPositiveY || this.isCollidedNegativeY;
        this.isCollided = this.isCollidedHorizontally || this.isCollidedVertically;
    }

    protected void checkGrounded() {
        this.onGround = this.isCollidedNegativeY;
    }
    //endregion
}
