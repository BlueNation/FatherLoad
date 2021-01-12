package com.github.BlueNation.FatherLoad.models;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import static com.github.BlueNation.FatherLoad.Reference.MODID;

@SideOnly(Side.CLIENT)
public class ModelDriller extends ModelBase {
    private final IModelCustom drillerCabinModel = AdvancedModelLoader.loadModel(
            new ResourceLocation(MODID + ":textures/models/driller_cabin.obj"));
    private final IModelCustom drillerTracksModel = AdvancedModelLoader.loadModel(
            new ResourceLocation(MODID + ":textures/models/driller_tracks.obj"));
    private final IModelCustom drillerDrillModel = AdvancedModelLoader.loadModel(
            new ResourceLocation(MODID + ":textures/models/driller_drill.obj"));

    public ModelDriller() {
    }

    @Override
    public void render(Entity ent, float time, float swingSuppress, float par4, float headAngleY, float headAngleX,
                       float par7) {
        drillerCabinModel.renderAll();
        drillerTracksModel.renderAll();
        drillerDrillModel.renderAll();
    }
}
