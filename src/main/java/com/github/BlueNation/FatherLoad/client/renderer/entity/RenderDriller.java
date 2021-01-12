package com.github.BlueNation.FatherLoad.client.renderer.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import static com.github.BlueNation.FatherLoad.Reference.MODID;

@SideOnly(Side.CLIENT)
public class RenderDriller extends Render {
    protected ResourceLocation textureLocation = new ResourceLocation(MODID + ":textures/models/driller.png");
    protected ModelBase mainModel;

    public RenderDriller(ModelBase model) {
        this.mainModel = model;
        this.shadowSize = 0F;
    }

    @Override
    public void doRender(Entity entity, double posX, double posY, double posZ, float p_76986_8_, float rotationRatio) {
        //Push the matrix to checkpoint changes
        GL11.glPushMatrix();

        //Lets faces be textures on both sides
        GL11.glDisable(GL11.GL_CULL_FACE);

        //Get the rotational axis
        float rotZ = this.interpolateRotation(entity.prevRotationYaw, entity.rotationYaw, rotationRatio);

        //Position and rotate the drill
        this.renderDrillerAt(posX, posY, posZ);
        this.rotateDriller(rotZ);

        //Render the model
        this.renderModel(entity);

        //Sets the brightness of the model
        float brightness = entity.getBrightness(0F);
        GL11.glColor4f(brightness, 0.0F, 0.0F, 0.4F);

        //Re-enabled after drawing
        GL11.glEnable(GL11.GL_CULL_FACE);

        //Pop the matrix to restore state
        GL11.glPopMatrix();
    }

    protected void renderModel(Entity entity) {
        this.bindEntityTexture(entity);
        this.mainModel.render(entity, 0F, 0F, 0F, 0F, 0F, 0F);
    }

    //Translate the rendering to where the driller is
    protected void renderDrillerAt(double posX, double posY, double posZ) {
        GL11.glTranslatef((float) posX, (float) posY, (float) posZ);
    }

    //Rotate the driller to where it's looking
    protected void rotateDriller(float rotZ) {
        GL11.glRotatef(360F - rotZ, 0F, 1F, 0F);
    }

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

    @Override
    protected ResourceLocation getEntityTexture(Entity par1Entity) {
        return this.textureLocation;
    }
}
