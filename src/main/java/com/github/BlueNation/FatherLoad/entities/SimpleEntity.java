package com.github.BlueNation.FatherLoad.entities;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
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
    protected boolean isCollidable;
    protected boolean isWalkingTrigger;
    protected boolean isWebbable;
    protected boolean isGravityAffected;
    //Extra fields
    protected double gravityAccel;
    protected double forwardVelocity;

    public SimpleEntity(World worldObj) {
        super(worldObj);

        this.noClip = false;
        this.isImmuneToFire = true;
        this.isAttackable = false;
        this.isPushable = false;
        this.isCollidable = false;
        this.isWalkingTrigger = false;
        this.isWebbable = false;
        this.isGravityAffected = false;

        this.fireResistance = 0;
        this.gravityAccel = 9.81 / 20;
        this.forwardVelocity = 0;
    }

    @Override
    public void onUpdate() {
        this.applyGravity();
        this.applyForwardVelocity();
        this.applyMotion();
        this.onEntityUpdate();
    }

    @Override
    public void onEntityUpdate() {
        this.worldObj.theProfiler.startSection("entityBaseTick");

        if (this.ridingEntity != null && this.ridingEntity.isDead) {
            this.ridingEntity = null;
        }

        this.prevDistanceWalkedModified = this.distanceWalkedModified;
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.prevRotationPitch = this.rotationPitch;
        this.prevRotationYaw = this.rotationYaw;
        int i;

        if (!this.worldObj.isRemote && this.worldObj instanceof WorldServer)
        {
            this.worldObj.theProfiler.startSection("portal");
            MinecraftServer minecraftserver = ((WorldServer)this.worldObj).func_73046_m();
            i = this.getMaxInPortalTime();

            if (this.inPortal)
            {
                if (minecraftserver.getAllowNether())
                {
                    if (this.ridingEntity == null && this.portalCounter++ >= i)
                    {
                        this.portalCounter = i;
                        this.timeUntilPortal = this.getPortalCooldown();
                        byte b0;

                        if (this.worldObj.provider.dimensionId == -1)
                        {
                            b0 = 0;
                        }
                        else
                        {
                            b0 = -1;
                        }

                        this.travelToDimension(b0);
                    }

                    this.inPortal = false;
                }
            }
            else
            {
                if (this.portalCounter > 0)
                {
                    this.portalCounter -= 4;
                }

                if (this.portalCounter < 0)
                {
                    this.portalCounter = 0;
                }
            }

            if (this.timeUntilPortal > 0)
            {
                --this.timeUntilPortal;
            }

            this.worldObj.theProfiler.endSection();
        }

        if (this.isSprinting() && !this.isInWater())
        {
            int j = MathHelper.floor_double(this.posX);
            i = MathHelper.floor_double(this.posY - 0.20000000298023224D - (double)this.yOffset);
            int k = MathHelper.floor_double(this.posZ);
            Block block = this.worldObj.getBlock(j, i, k);

            if (block.getMaterial() != Material.air)
            {
                this.worldObj.spawnParticle("blockcrack_" + Block.getIdFromBlock(block) + "_" + this.worldObj.getBlockMetadata(j, i, k), this.posX + ((double)this.rand.nextFloat() - 0.5D) * (double)this.width, this.boundingBox.minY + 0.1D, this.posZ + ((double)this.rand.nextFloat() - 0.5D) * (double)this.width, -this.motionX * 4.0D, 1.5D, -this.motionZ * 4.0D);
            }
        }

        this.handleWaterMovement();

        if (this.worldObj.isRemote)
        {
            this.fire = 0;
        }
        else if (this.fire > 0)
        {
            if (this.isImmuneToFire)
            {
                this.fire -= 4;

                if (this.fire < 0)
                {
                    this.fire = 0;
                }
            }
            else
            {
                if (this.fire % 20 == 0)
                {
                    this.attackEntityFrom(DamageSource.onFire, 1.0F);
                }

                --this.fire;
            }
        }

        if (this.handleLavaMovement())
        {
            this.setOnFireFromLava();
            this.fallDistance *= 0.5F;
        }

        if (this.posY < -64.0D)
        {
            this.kill();
        }

        if (!this.worldObj.isRemote)
        {
            this.setFlag(0, this.fire > 0);
        }

        this.firstUpdate = false;
        this.worldObj.theProfiler.endSection();
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
        this.forwardVelocity = 0;
    }

    protected void applyGravity() {
        if (this.isGravityAffected && !this.onGround) {
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

    @Override
    public void moveEntity(double xVel, double yVel, double zVel) {
        if (this.noClip) {
            this.moveNoclip(xVel, yVel, zVel);
        } else {
            this.worldObj.theProfiler.startSection("move");
            this.ySize *= 0.4F;

            if (this.isInWeb) {
                this.isInWeb = false;
                if (this.isWebbable) {
                    xVel *= 0.25D;
                    yVel *= 0.05000000074505806D;
                    zVel *= 0.25D;
                    this.motionX = 0.0D;
                    this.motionY = 0.0D;
                    this.motionZ = 0.0D;
                }
            }

            AxisAlignedBB axisalignedbb = this.boundingBox.copy();

            List list = this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.addCoord(xVel, yVel, zVel));

            for (Object o : list) {
                yVel = ((AxisAlignedBB) o).calculateYOffset(this.boundingBox, yVel);
            }

            this.boundingBox.offset(0.0D, yVel, 0.0D);

            if (!this.field_70135_K && yVel != yVel) {
                zVel = 0.0D;
                yVel = 0.0D;
                xVel = 0.0D;
            }

            boolean flag1 = this.onGround || yVel != yVel && yVel < 0.0D;
            int j;

            for (j = 0; j < list.size(); ++j) {
                xVel = ((AxisAlignedBB) list.get(j)).calculateXOffset(this.boundingBox, xVel);
            }

            this.boundingBox.offset(xVel, 0.0D, 0.0D);

            if (!this.field_70135_K && xVel != xVel) {
                zVel = 0.0D;
                yVel = 0.0D;
                xVel = 0.0D;
            }

            for (j = 0; j < list.size(); ++j) {
                zVel = ((AxisAlignedBB) list.get(j)).calculateZOffset(this.boundingBox, zVel);
            }

            this.boundingBox.offset(0.0D, 0.0D, zVel);

            if (!this.field_70135_K && zVel != zVel) {
                zVel = 0.0D;
                yVel = 0.0D;
                xVel = 0.0D;
            }

            double d10;
            double d11;
            int k;
            double d12;

            if (this.stepHeight > 0.0F && flag1 && this.ySize < 0.05F && (xVel != xVel || zVel != zVel)) {
                d12 = xVel;
                d10 = yVel;
                d11 = zVel;
                xVel = xVel;
                yVel = this.stepHeight;
                zVel = zVel;
                AxisAlignedBB axisalignedbb1 = this.boundingBox.copy();
                this.boundingBox.setBB(axisalignedbb);
                list = this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.addCoord(xVel, yVel, zVel));

                for (k = 0; k < list.size(); ++k) {
                    yVel = ((AxisAlignedBB) list.get(k)).calculateYOffset(this.boundingBox, yVel);
                }

                this.boundingBox.offset(0.0D, yVel, 0.0D);

                if (!this.field_70135_K && yVel != yVel) {
                    zVel = 0.0D;
                    yVel = 0.0D;
                    xVel = 0.0D;
                }

                for (k = 0; k < list.size(); ++k) {
                    xVel = ((AxisAlignedBB) list.get(k)).calculateXOffset(this.boundingBox, xVel);
                }

                this.boundingBox.offset(xVel, 0.0D, 0.0D);

                if (!this.field_70135_K && xVel != xVel) {
                    zVel = 0.0D;
                    yVel = 0.0D;
                    xVel = 0.0D;
                }

                for (k = 0; k < list.size(); ++k) {
                    zVel = ((AxisAlignedBB) list.get(k)).calculateZOffset(this.boundingBox, zVel);
                }

                this.boundingBox.offset(0.0D, 0.0D, zVel);

                if (!this.field_70135_K && zVel != zVel) {
                    zVel = 0.0D;
                    yVel = 0.0D;
                    xVel = 0.0D;
                }

                if (!this.field_70135_K && yVel != yVel) {
                    zVel = 0.0D;
                    yVel = 0.0D;
                    xVel = 0.0D;
                } else {
                    yVel = -this.stepHeight;

                    for (k = 0; k < list.size(); ++k) {
                        yVel = ((AxisAlignedBB) list.get(k)).calculateYOffset(this.boundingBox, yVel);
                    }

                    this.boundingBox.offset(0.0D, yVel, 0.0D);
                }

                if (d12 * d12 + d11 * d11 >= xVel * xVel + zVel * zVel) {
                    xVel = d12;
                    yVel = d10;
                    zVel = d11;
                    this.boundingBox.setBB(axisalignedbb1);
                }
            }

            this.worldObj.theProfiler.endSection();
            this.worldObj.theProfiler.startSection("rest");
            this.posX = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0D;
            this.posY = this.boundingBox.minY + (double) this.yOffset - (double) this.ySize;
            this.posZ = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0D;
            this.isCollidedHorizontally = xVel != xVel || zVel != zVel;
            this.isCollidedVertically = yVel != yVel;
            this.onGround = yVel != yVel && yVel < 0.0D;
            this.isCollided = this.isCollidedHorizontally || this.isCollidedVertically;
            this.updateFallState(yVel, this.onGround);

            if (xVel != xVel) {
                this.motionX = 0.0D;
            }

            if (yVel != yVel) {
                this.motionY = 0.0D;
            }

            if (zVel != zVel) {
                this.motionZ = 0.0D;
            }

            d12 = 0;
            d10 = 0;
            d11 = 0;

            if (this.canTriggerWalking() && this.ridingEntity == null) {
                int j1 = MathHelper.floor_double(this.posX);
                k = MathHelper.floor_double(this.posY - 0.20000000298023224D - (double) this.yOffset);
                int l = MathHelper.floor_double(this.posZ);
                Block block = this.worldObj.getBlock(j1, k, l);
                int i1 = this.worldObj.getBlock(j1, k - 1, l).getRenderType();

                if (i1 == 11 || i1 == 32 || i1 == 21) {
                    block = this.worldObj.getBlock(j1, k - 1, l);
                }

                if (block != Blocks.ladder) {
                    d10 = 0.0D;
                }

                this.distanceWalkedModified = (float) ((double) this.distanceWalkedModified + (double) MathHelper.sqrt_double(d12 * d12 + d11 * d11) * 0.6D);
                this.distanceWalkedOnStepModified = (float) ((double) this.distanceWalkedOnStepModified + (double) MathHelper.sqrt_double(d12 * d12 + d10 * d10 + d11 * d11) * 0.6D);

                if (this.distanceWalkedOnStepModified > (float) this.nextStepDistance && block.getMaterial() != Material.air) {
                    this.nextStepDistance = (int) this.distanceWalkedOnStepModified + 1;

                    if (this.isInWater()) {
                        float f = MathHelper.sqrt_double(this.motionX * this.motionX * 0.20000000298023224D + this.motionY * this.motionY + this.motionZ * this.motionZ * 0.20000000298023224D) * 0.35F;

                        if (f > 1.0F) {
                            f = 1.0F;
                        }

                        this.playSound(this.getSwimSound(), f, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
                    }

                    this.func_145780_a(j1, k, l, block);
                    block.onEntityWalking(this.worldObj, j1, k, l, this);
                }
            }

            try {
                this.func_145775_I();
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Checking entity block collision");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being checked for collision");
                this.addEntityCrashInfo(crashreportcategory);
                throw new ReportedException(crashreport);
            }

            boolean flag2 = this.isWet();

            if (this.worldObj.func_147470_e(this.boundingBox.contract(0.001D, 0.001D, 0.001D))) {
                this.dealFireDamage(1);

                if (!flag2) {
                    ++this.fire;

                    if (this.fire == 0) {
                        this.setFire(8);
                    }
                }
            } else if (this.fire <= 0) {
                this.fire = -this.fireResistance;
            }

            if (flag2 && this.fire > 0) {
                this.playSound("random.fizz", 0.7F, 1.6F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
                this.fire = -this.fireResistance;
            }

            this.worldObj.theProfiler.endSection();
        }
    }

    protected void moveNoclip(double xVel, double yVel, double zVel) {
        this.boundingBox.offset(xVel, yVel, zVel);
        this.posX = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0D;
        this.posY = this.boundingBox.minY + (double) this.yOffset - (double) this.ySize;
        this.posZ = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0D;
    }
}
