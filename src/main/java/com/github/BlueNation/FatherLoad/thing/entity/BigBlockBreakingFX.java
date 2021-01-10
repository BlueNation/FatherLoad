package com.github.BlueNation.FatherLoad.thing.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class BigBlockBreakingFX extends EntityFX {

    protected BigBlockBreakingFX(World world, double x, double y, double z,int particleMaxAge) {
        super(world, x, y, z);
        this.particleMaxAge=particleMaxAge;
    }

    protected int getDamageLevel(){
        return Math.min(9,particleAge*10/particleMaxAge);
    }

    protected IIcon getIcon(){
        return Minecraft.getMinecraft().renderGlobal.destroyBlockIcons[getDamageLevel()];
    }

    @Override
    public void renderParticle(Tessellator tes, float subTickTime,
                               float p_70539_3_, float p_70539_4_, float p_70539_5_, float p_70539_6_, float p_70539_7_) {
        float size = 2f;
        float X = (float) (prevPosX + (posX - prevPosX) * (double) subTickTime - EntityFX.interpPosX);
        float Y = (float) (prevPosY + (posY - prevPosY) * (double) subTickTime - EntityFX.interpPosY) - size / 2;
        float Z = (float) (prevPosZ + (posZ - prevPosZ) * (double) subTickTime - EntityFX.interpPosZ);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDepthMask(false);
        //tes.setColorRGBA((int) (mRGBa[0] * .9F), (int) (mRGBa[1] * .95F), (int) (mRGBa[2] * 1F), 192);

        IIcon icon=getIcon();
        double u=icon.getMinU();
        double U=icon.getMaxU();
        double v=icon.getMinV();
        double V=icon.getMaxV();

        for(int i=0;i<6;i++){
            switch (i){//{DOWN, UP, NORTH, SOUTH, WEST, EAST}
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
        }
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDepthMask(true);
    }
}
