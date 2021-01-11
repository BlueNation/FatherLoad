package com.github.BlueNation.FatherLoad.thing.entity;

import com.github.BlueNation.FatherLoad.mechanics.BigBlockHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

import static com.github.BlueNation.FatherLoad.mechanics.BigBlockHandler.BLOCK_SIZE;

@SideOnly(Side.CLIENT)
public class BigBlockBreakingFX extends EntityFX {
    public static final float MARGIN = .01f;
    public static final float MARGIN_HALF=MARGIN/2f;
    public static final float size = BLOCK_SIZE+MARGIN;
    public static final int BLOCK_HALF_SIZE_INC= BigBlockHandler.BLOCK_HALF_SIZE;
    public static final int BLOCK_BACK= -1;
    public static final int BLOCK_HALF_SIZE_DEC= BigBlockHandler.BLOCK_HALF_SIZE+BLOCK_BACK;

    final int direction;
    final ForgeDirection forgeDirection;
    final int x,y,z;
    int damage;

    public BigBlockBreakingFX(World world, int x, int y, int z, ForgeDirection side, int particleMaxAge) {
        super(world, x, y, z);
        this.particleMaxAge=particleMaxAge;
        particleGravity = 0;
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        noClip = true;
        direction = side.ordinal();
        forgeDirection=side;
        this.x=x;
        this.y=y;
        this.z=z;
        setParticleIcon(Minecraft.getMinecraft().renderGlobal.destroyBlockIcons[0]);
    }

    protected void setIcon(){
        int newDamage=Math.min(9, particleAge * 10 / particleMaxAge);
        if(damage!=newDamage){
            damage=newDamage;
            setParticleIcon(Minecraft.getMinecraft().renderGlobal.destroyBlockIcons[newDamage]);
        }
    }

    @Override
    public void renderParticle(Tessellator tes, float subTickTime,
                               float p_70539_3_, float p_70539_4_, float p_70539_5_, float p_70539_6_, float p_70539_7_) {
        float X = (float) (prevPosX + (posX - prevPosX) * (double) subTickTime - EntityFX.interpPosX)-MARGIN_HALF;
        float Y = (float) (prevPosY + (posY - prevPosY) * (double) subTickTime - EntityFX.interpPosY)-MARGIN_HALF;
        float Z = (float) (prevPosZ + (posZ - prevPosZ) * (double) subTickTime - EntityFX.interpPosZ)-MARGIN_HALF;
        GL11.glDepthMask(false);

        setIcon();
        double u=particleIcon.getMinU();
        double U=particleIcon.getMaxU();
        double v=particleIcon.getMinV();
        double V=particleIcon.getMaxV();

        //var8, var9 - X U
        //var 10, var 11 - Y V
        tes.setColorRGBA(255,255,255,128);
        tes.setBrightness(getBrightness());

        switch (direction){//{DOWN, UP, NORTH, SOUTH, WEST, EAST}
            case 0:
                tes.addVertexWithUV(X, Y, Z + size, u, V);
                tes.addVertexWithUV(X, Y, Z, u, v);
                tes.addVertexWithUV(X + size, Y, Z, U, v);
                tes.addVertexWithUV(X + size, Y, Z + size, U, V);
                break;
            case 1:
                tes.addVertexWithUV(X, Y + size, Z, u, v);
                tes.addVertexWithUV(X, Y + size, Z + size, u, V);
                tes.addVertexWithUV(X + size, Y + size, Z + size, U, V);
                tes.addVertexWithUV(X + size, Y + size, Z, U, v);
                break;
            case 2:
                tes.addVertexWithUV(X, Y, Z, U, V);
                tes.addVertexWithUV(X, Y + size, Z, U, v);
                tes.addVertexWithUV(X + size, Y + size, Z, u, v);
                tes.addVertexWithUV(X + size, Y, Z, u, V);
                break;
            case 3:
                tes.addVertexWithUV(X + size, Y, Z + size, U, V);
                tes.addVertexWithUV(X + size, Y + size, Z + size, U, v);
                tes.addVertexWithUV(X, Y + size, Z + size, u, v);
                tes.addVertexWithUV(X, Y, Z + size, u, V);
                break;
            case 4:
                tes.addVertexWithUV(X, Y, Z + size, U, V);
                tes.addVertexWithUV(X, Y + size, Z + size, U, v);
                tes.addVertexWithUV(X, Y + size, Z, u, v);
                tes.addVertexWithUV(X, Y, Z, u, V);
                break;
            case 5:
                tes.addVertexWithUV(X + size, Y, Z, U, V);
                tes.addVertexWithUV(X + size, Y + size, Z, U, v);
                tes.addVertexWithUV(X + size, Y + size, Z + size, u, v);
                tes.addVertexWithUV(X + size, Y, Z + size, u, V);
                break;
        }
        GL11.glDepthMask(true);
    }

    private int getBrightness() {
        switch (direction){//{DOWN, UP, NORTH, SOUTH, WEST, EAST}
            case 0:
                return (
                        Blocks.air.getMixedBrightnessForBlock(worldObj,
                                x+BLOCK_HALF_SIZE_DEC,
                                y+BLOCK_BACK,
                                z+BLOCK_HALF_SIZE_DEC)+
                        Blocks.air.getMixedBrightnessForBlock(worldObj,
                                x+BLOCK_HALF_SIZE_DEC,
                                y+BLOCK_BACK,
                                z+BLOCK_HALF_SIZE_INC)+
                        Blocks.air.getMixedBrightnessForBlock(worldObj,
                                x+BLOCK_HALF_SIZE_INC,
                                y+BLOCK_BACK,
                                z+BLOCK_HALF_SIZE_DEC)+
                        Blocks.air.getMixedBrightnessForBlock(worldObj,
                                x+BLOCK_HALF_SIZE_INC,
                                y+BLOCK_BACK,
                                z+BLOCK_HALF_SIZE_INC))/4;
            case 1:
                return (
                        Blocks.air.getMixedBrightnessForBlock(worldObj,
                                x+BLOCK_HALF_SIZE_DEC,
                                y+BLOCK_SIZE,
                                z+BLOCK_HALF_SIZE_DEC)+
                        Blocks.air.getMixedBrightnessForBlock(worldObj,
                                x+BLOCK_HALF_SIZE_DEC,
                                y+BLOCK_SIZE,
                                z+BLOCK_HALF_SIZE_INC)+
                        Blocks.air.getMixedBrightnessForBlock(worldObj,
                                x+BLOCK_HALF_SIZE_INC,
                                y+BLOCK_SIZE,
                                z+BLOCK_HALF_SIZE_DEC)+
                        Blocks.air.getMixedBrightnessForBlock(worldObj,
                                x+BLOCK_HALF_SIZE_INC,
                                y+BLOCK_SIZE,
                                z+BLOCK_HALF_SIZE_INC))/4;
            case 2:
                return (
                        Blocks.air.getMixedBrightnessForBlock(worldObj,
                                x+BLOCK_HALF_SIZE_DEC,
                                y+BLOCK_HALF_SIZE_DEC,
                                z+BLOCK_BACK)+
                        Blocks.air.getMixedBrightnessForBlock(worldObj,
                                x+BLOCK_HALF_SIZE_DEC,
                                y+BLOCK_HALF_SIZE_INC,
                                z+BLOCK_BACK)+
                        Blocks.air.getMixedBrightnessForBlock(worldObj,
                                x+BLOCK_HALF_SIZE_INC,
                                y+BLOCK_HALF_SIZE_DEC,
                                z+BLOCK_BACK)+
                        Blocks.air.getMixedBrightnessForBlock(worldObj,
                                x+BLOCK_HALF_SIZE_INC,
                                y+BLOCK_HALF_SIZE_INC,
                                z+BLOCK_BACK))/4;
            case 3:
                return (
                        Blocks.air.getMixedBrightnessForBlock(worldObj,
                                x+BLOCK_HALF_SIZE_DEC,
                                y+BLOCK_HALF_SIZE_DEC,
                                z+BLOCK_SIZE)+
                        Blocks.air.getMixedBrightnessForBlock(worldObj,
                                x+BLOCK_HALF_SIZE_DEC,
                                y+BLOCK_HALF_SIZE_INC,
                                z+BLOCK_SIZE)+
                        Blocks.air.getMixedBrightnessForBlock(worldObj,
                                x+BLOCK_HALF_SIZE_INC,
                                y+BLOCK_HALF_SIZE_DEC,
                                z+BLOCK_SIZE)+
                        Blocks.air.getMixedBrightnessForBlock(worldObj,
                                x+BLOCK_HALF_SIZE_INC,
                                y+BLOCK_HALF_SIZE_INC,
                                z+BLOCK_SIZE))/4;
            case 4:
                return (
                        Blocks.air.getMixedBrightnessForBlock(worldObj,
                                x+BLOCK_BACK,
                                y+BLOCK_HALF_SIZE_DEC,
                                z+BLOCK_HALF_SIZE_DEC)+
                        Blocks.air.getMixedBrightnessForBlock(worldObj,
                                x+BLOCK_BACK,
                                y+BLOCK_HALF_SIZE_DEC,
                                z+BLOCK_HALF_SIZE_INC)+
                        Blocks.air.getMixedBrightnessForBlock(worldObj,
                                x+BLOCK_BACK,
                                y+BLOCK_HALF_SIZE_INC,
                                z+BLOCK_HALF_SIZE_DEC)+
                        Blocks.air.getMixedBrightnessForBlock(worldObj,
                                x+BLOCK_BACK,
                                y+BLOCK_HALF_SIZE_INC,
                                z+BLOCK_HALF_SIZE_INC))/4;
            case 5:
                return (
                        Blocks.air.getMixedBrightnessForBlock(worldObj,
                                x+BLOCK_SIZE,
                                y+BLOCK_HALF_SIZE_DEC,
                                z+BLOCK_HALF_SIZE_DEC)+
                        Blocks.air.getMixedBrightnessForBlock(worldObj,
                                x+BLOCK_SIZE,
                                y+BLOCK_HALF_SIZE_DEC,
                                z+BLOCK_HALF_SIZE_INC)+
                        Blocks.air.getMixedBrightnessForBlock(worldObj,
                                x+BLOCK_SIZE,
                                y+BLOCK_HALF_SIZE_INC,
                                z+BLOCK_HALF_SIZE_DEC)+
                        Blocks.air.getMixedBrightnessForBlock(worldObj,
                                x+BLOCK_SIZE,
                                y+BLOCK_HALF_SIZE_INC,
                                z+BLOCK_HALF_SIZE_INC))/4;
        }
        return 16;
    }
}
