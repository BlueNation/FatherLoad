package com.github.BlueNation.FatherLoad.client.renderer.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import static com.github.BlueNation.FatherLoad.Reference.MODID;

@SideOnly(Side.CLIENT)
public class RenderDriller extends Render {
    protected ResourceLocation texture = new ResourceLocation(MODID + ":textures/models/driller.png");

    private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    protected ModelBase mainModel;
    /** The model to be used during the render passes. */
    protected ModelBase renderPassModel;

    public RenderDriller(ModelBase model) {
        this.mainModel = model;
        this.shadowSize = 0F;
    }

    /**
     * Returns a rotation angle that is inbetween two other rotation angles. par1 and par2 are the angles between which
     * to interpolate, par3 is probably a float between 0.0 and 1.0 that tells us where "between" the two angles we are.
     * Example: angleA = 30, angleB = 50, ratio = 0.5, then return = 40
     */
    private float interpolateRotation(float angleA, float angleB, float ratio) {
        float delta = angleB - angleA;

        //Clamp the delta between -180F and 180F
        while (delta < -180.0F) {
            delta += 360.0F;
        }
        while (delta >= 180.0F) {
            delta -= 360.0F;
        }

        return angleA + ratio * delta;
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void func_76986_a(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(EntityLivingBase entity, double posX, double posY, double posZ, float p_76986_8_, float p_76986_9_) {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
        this.mainModel.onGround = this.renderSwingProgress(entity, p_76986_9_);

        if (this.renderPassModel != null) {
            this.renderPassModel.onGround = this.mainModel.onGround;
        }

        this.mainModel.isRiding = entity.isRiding();

        if (this.renderPassModel != null) {
            this.renderPassModel.isRiding = this.mainModel.isRiding;
        }

        this.mainModel.isChild = entity.isChild();

        if (this.renderPassModel != null) {
            this.renderPassModel.isChild = this.mainModel.isChild;
        }

        try {
            float f2 = this.interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, p_76986_9_);
            float f3 = this.interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, p_76986_9_);
            float f4;

            if (entity.isRiding() && entity.ridingEntity instanceof EntityLivingBase) {
                EntityLivingBase entitylivingbase1 = (EntityLivingBase) entity.ridingEntity;
                f2 = this.interpolateRotation(entitylivingbase1.prevRenderYawOffset, entitylivingbase1.renderYawOffset, p_76986_9_);
                f4 = MathHelper.wrapAngleTo180_float(f3 - f2);

                if (f4 < -85.0F) {
                    f4 = -85.0F;
                }

                if (f4 >= 85.0F) {
                    f4 = 85.0F;
                }

                f2 = f3 - f4;

                if (f4 * f4 > 2500.0F) {
                    f2 += f4 * 0.2F;
                }
            }

            float f13 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * p_76986_9_;
            this.renderDrillerAt(posX, posY, posZ);
            f4 = this.handleRotationFloat(entity, p_76986_9_);
            this.rotateDriller(f2);
            float f5 = 0.0625F;
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glScalef(-1.0F, -1.0F, 1.0F);
            GL11.glTranslatef(0.0F, -24.0F * f5 - 0.0078125F, 0.0F);
            float f6 = entity.prevLimbSwingAmount + (entity.limbSwingAmount - entity.prevLimbSwingAmount) * p_76986_9_;
            float f7 = entity.limbSwing - entity.limbSwingAmount * (1.0F - p_76986_9_);

            if (entity.isChild()) {
                f7 *= 3.0F;
            }

            if (f6 > 1.0F) {
                f6 = 1.0F;
            }

            GL11.glEnable(GL11.GL_ALPHA_TEST);
            this.mainModel.setLivingAnimations(entity, f7, f6, p_76986_9_);
            this.renderModel(entity, f7, f6, f4, f3 - f2, f13, f5);
            GL11.glDepthMask(true);
            float f14 = entity.getBrightness(p_76986_9_);
            OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);

            if (entity.hurtTime > 0 || entity.deathTime > 0) {
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glDepthFunc(GL11.GL_EQUAL);

                if (entity.hurtTime > 0 || entity.deathTime > 0) {
                    GL11.glColor4f(f14, 0.0F, 0.0F, 0.4F);
                    this.mainModel.render(entity, f7, f6, f4, f3 - f2, f13, f5);
                }

                GL11.glDepthFunc(GL11.GL_LEQUAL);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
            }

            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        } catch (Exception ignored) {
        }

        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
        this.passSpecialRender(entity, posX, posY, posZ);
    }

    protected void renderModel(EntityLivingBase p_77036_1_, float p_77036_2_, float p_77036_3_, float p_77036_4_, float p_77036_5_, float p_77036_6_, float p_77036_7_) {
        this.bindEntityTexture(p_77036_1_);

        if (!p_77036_1_.isInvisible()) {
            this.mainModel.render(p_77036_1_, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);
        } else if (!p_77036_1_.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer)) {
            GL11.glPushMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.15F);
            GL11.glDepthMask(false);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
            this.mainModel.render(p_77036_1_, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
            GL11.glPopMatrix();
            GL11.glDepthMask(true);
        } else {
            this.mainModel.setRotationAngles(p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_, p_77036_1_);
        }
    }

    //Translate the rendering to where the driller is
    protected void renderDrillerAt(double p_77039_2_, double p_77039_4_, double p_77039_6_) {
        GL11.glTranslatef((float) p_77039_2_, (float) p_77039_4_, (float) p_77039_6_);
    }

    //Rotate the driller to where it's looking
    protected void rotateDriller(float rotZ) {
        GL11.glRotatef(180.0F - rotZ, 0.0F, 1.0F, 0.0F);
    }

    protected float renderSwingProgress(EntityLivingBase entity, float p_77040_2_) {
        return entity.getSwingProgress(p_77040_2_);
    }

    /**
     * Defines what float the third param in setRotationAngles of ModelBase is
     */
    protected float handleRotationFloat(EntityLivingBase entity, float p_77044_2_) {
        return (float)entity.ticksExisted + p_77044_2_;
    }

    /**
     * Passes the specialRender and renders it
     */
    protected void passSpecialRender(EntityLivingBase entity, double posX, double posY, double posZ) {
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
        if (this.func_110813_b(entity)) {
            float f = 1.6F;
            float f1 = 0.016666668F * f;
            double d3 = entity.getDistanceSqToEntity(this.renderManager.livingPlayer);
            float f2 = 64;//

            if (d3 < (double) (f2 * f2)) {
                String s = entity.func_145748_c_().getFormattedText();

                if (entity.isSneaking()) {
                    FontRenderer fontrenderer = this.getFontRendererFromRenderManager();
                    GL11.glPushMatrix();
                    GL11.glTranslatef((float) posX + 0.0F, (float) posY + entity.height + 0.5F, (float) posZ);
                    GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
                    GL11.glScalef(-f1, -f1, f1);
                    GL11.glDisable(GL11.GL_LIGHTING);
                    GL11.glTranslatef(0.0F, 0.25F / f1, 0.0F);
                    GL11.glDepthMask(false);
                    GL11.glEnable(GL11.GL_BLEND);
                    OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                    Tessellator tessellator = Tessellator.instance;
                    GL11.glDisable(GL11.GL_TEXTURE_2D);
                    tessellator.startDrawingQuads();
                    int i = fontrenderer.getStringWidth(s) / 2;
                    tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
                    tessellator.addVertex((double) (-i - 1), -1.0D, 0.0D);
                    tessellator.addVertex((double) (-i - 1), 8.0D, 0.0D);
                    tessellator.addVertex((double) (i + 1), 8.0D, 0.0D);
                    tessellator.addVertex((double) (i + 1), -1.0D, 0.0D);
                    tessellator.draw();
                    GL11.glEnable(GL11.GL_TEXTURE_2D);
                    GL11.glDepthMask(true);
                    fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0, 553648127);
                    GL11.glEnable(GL11.GL_LIGHTING);
                    GL11.glDisable(GL11.GL_BLEND);
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    GL11.glPopMatrix();
                } else {
                    this.func_96449_a(entity, posX, posY, posZ, s, f1, d3);
                }
            }
        }
    }

    protected boolean func_110813_b(EntityLivingBase entity) {
        return Minecraft.isGuiEnabled() && entity != this.renderManager.livingPlayer &&
                !entity.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer) && entity.riddenByEntity == null;
    }

    protected void func_96449_a(EntityLivingBase entity, double p_96449_2_, double p_96449_4_, double p_96449_6_, String p_96449_8_, float p_96449_9_, double p_96449_10_) {
        if (entity.isPlayerSleeping()) {
            this.func_147906_a(entity, p_96449_8_, p_96449_2_, p_96449_4_ - 1.5D, p_96449_6_, 64);
        } else {
            this.func_147906_a(entity, p_96449_8_, p_96449_2_, p_96449_4_, p_96449_6_, 64);
        }
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void func_76986_a(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    @Override
    public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
        this.doRender((EntityLivingBase) p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity par1Entity) {
        return this.texture;
    }
}
