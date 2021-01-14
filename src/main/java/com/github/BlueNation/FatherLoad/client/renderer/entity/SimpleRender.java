package com.github.BlueNation.FatherLoad.client.renderer.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class SimpleRender extends Render {
    protected final ResourceLocation texture;
    protected final ModelBase model;

    /**
     * Simple Render Constructor
     *
     * @param model           Base model class
     * @param textureLocation Texture resource location
     * @param shadowSize      Shadow size
     */
    public SimpleRender(ModelBase model, String textureLocation, float shadowSize) {
        this.model = model;
        this.texture = new ResourceLocation(textureLocation);
        this.shadowSize = shadowSize;
    }

    /**
     * Called by the client to render the entity, calls all the other Simple Render methods in their sequence
     *
     * @param entity   Entity being rendered
     * @param posX     Entity X position
     * @param posY     Entity Y position
     * @param posZ     Entity Z position
     * @param rotYaw   Entity yaw rotation in the world
     * @param rotRatio Rotation ratio of the entity
     */
    @Override
    public void doRender(Entity entity, double posX, double posY, double posZ, float rotYaw, float rotRatio) {
        this.setupRender(entity, posX, posY, posZ, rotYaw, rotRatio);
        this.translateRender(entity, posX, posY, posZ, rotYaw, rotRatio);
        this.rotateRender(entity, posX, posY, posZ, rotYaw, rotRatio);
        this.renderModel(entity, posX, posY, posZ, rotYaw, rotRatio);
        this.postRender(entity, posX, posY, posZ, rotYaw, rotRatio);
    }

    /**
     * First step of render, disables render culling and pushes matrix
     * <p>
     * Disabling render culling here allows planes to be textured on both sides.
     *
     * @param entity   Entity being rendered
     * @param posX     Entity X position
     * @param posY     Entity Y position
     * @param posZ     Entity Z position
     * @param rotYaw   Entity yaw rotation in the world
     * @param rotRatio Rotation ratio of the entity
     */
    protected void setupRender(Entity entity, double posX, double posY, double posZ, float rotYaw, float rotRatio) {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

    /**
     * Translate render position to the X, Y and Z pos
     *
     * @param entity   Entity being rendered
     * @param posX     Entity X position
     * @param posY     Entity Y position
     * @param posZ     Entity Z position
     * @param rotYaw   Entity yaw rotation in the world
     * @param rotRatio Rotation ratio of the entity
     */
    protected void translateRender(Entity entity, double posX, double posY, double posZ, float rotYaw, float rotRatio) {
        GL11.glTranslatef((float) posX, (float) posY, (float) posZ);
    }

    /**
     * Rotate render by setting the yaw on the Y axis
     *
     * @param entity   Entity being rendered
     * @param posX     Entity X position
     * @param posY     Entity Y position
     * @param posZ     Entity Z position
     * @param rotYaw   Entity yaw rotation in the world
     * @param rotRatio Rotation ratio of the entity
     */
    protected void rotateRender(Entity entity, double posX, double posY, double posZ, float rotYaw, float rotRatio) {
        GL11.glRotatef(rotYaw, 0F, 1F, 0F);
    }

    /**
     * Binds the texture to the entity and renders the model
     *
     * @param entity   Entity being rendered
     * @param posX     Entity X position
     * @param posY     Entity Y position
     * @param posZ     Entity Z position
     * @param rotYaw   Entity yaw rotation in the world
     * @param rotRatio Rotation ratio of the entity
     */
    protected void renderModel(Entity entity, double posX, double posY, double posZ, float rotYaw, float rotRatio) {
        this.bindEntityTexture(entity);
        this.model.render(entity, 0F, 0F, 0F, 0F, 0F, 0F);
    }

    /**
     * Last step of render, re-enables render culling and pops matrix
     *
     * @param entity   Entity being rendered
     * @param posX     Entity X position
     * @param posY     Entity Y position
     * @param posZ     Entity Z position
     * @param rotYaw   Entity yaw rotation in the world
     * @param rotRatio Rotation ratio of the entity
     */
    protected void postRender(Entity entity, double posX, double posY, double posZ, float rotYaw, float rotRatio) {
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
    }

    /**
     * Getter for the entity texture, only called once the texture is bound.
     *
     * @param entity Entity being rendered
     * @return Texture resource location
     */
    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return this.texture;
    }
}
