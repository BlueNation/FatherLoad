package com.github.BlueNation.FatherLoad.models;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

import static com.github.BlueNation.FatherLoad.Reference.MODID;

@SideOnly(Side.CLIENT)
public class ModelDriller extends ModelBase {
    private final IModelCustom customModel = AdvancedModelLoader.loadModel(
            new ResourceLocation(MODID + ":textures/models/driller.obj"));

    public ModelDriller() {
    }

    @Override
    public void render(Entity ent, float time, float swingSuppress, float par4, float headAngleY, float headAngleX,
                       float par7) {
        GL11.glScalef(1F, 1F, 1F);
        GL11.glRotatef(180F, 1F, 0F, 0F);

        customModel.renderAll();
    }
}
