package com.github.BlueNation.FatherLoad.client.renderer.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class SidewaysRender extends SimpleRender {
    /**
     * Sideways Render Constructor
     *
     * @param model           Base model class
     * @param textureLocation Texture resource location
     * @param shadowSize      Shadow size
     */
    public SidewaysRender(ModelBase model, String textureLocation, float shadowSize) {
        super(model, textureLocation, shadowSize);
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
    @Override
    protected void translateRender(Entity entity, double posX, double posY, double posZ, float rotYaw, float rotRatio) {
        super.translateRender(entity, posX, posY, posZ, rotYaw, rotRatio);
        float halfWidth = entity.width / 2;
        GL11.glTranslatef(halfWidth, halfWidth, 0);
    }

    /**
     * Rotate render by setting the yaw on the X axis and rotating 90 degrees on the Z axis
     *
     * @param entity   Entity being rendered
     * @param posX     Entity X position
     * @param posY     Entity Y position
     * @param posZ     Entity Z position
     * @param rotYaw   Entity yaw rotation in the world
     * @param rotRatio Rotation ratio of the entity
     */
    @Override
    protected void rotateRender(Entity entity, double posX, double posY, double posZ, float rotYaw, float rotRatio) {
        GL11.glRotatef(rotYaw, 1F, 0F, 0F);
        GL11.glRotatef(90, 0F, 0F, 1F);
    }
}
