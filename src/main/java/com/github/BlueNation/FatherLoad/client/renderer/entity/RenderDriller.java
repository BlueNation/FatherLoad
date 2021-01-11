package com.github.BlueNation.FatherLoad.client.renderer.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import static com.github.BlueNation.FatherLoad.Reference.MODID;

@SideOnly(Side.CLIENT)
public class RenderDriller extends RenderLiving {
    protected ResourceLocation serpentTexture = new ResourceLocation(MODID + ":textures/models/driller.png");

    public RenderDriller(ModelBase model) {
        super(model, 1F);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity par1Entity) {
        return this.serpentTexture;
    }
}
