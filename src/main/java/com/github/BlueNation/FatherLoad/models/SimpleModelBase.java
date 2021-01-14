package com.github.BlueNation.FatherLoad.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;

public abstract class SimpleModelBase extends ModelBase {
    /**
     * The actual render method called inside the model class. The rendering is already pre-wrapped
     * in an OpenGL push-pop matrix so don't worry about that. ModelDriller is a good example of what you can do.
     *
     * @param ent               Entity being rendered
     * @param limbSwingTime     Limb swing time, defines how far along arms or legs are
     * @param limbSwingDistance Limb swing distance, defines how far the arms or legs reach
     * @param limbSwingOffset   Limb swing offset, additional limb offset
     * @param headYaw           Head yaw of the rendered entity
     * @param headPitch         Head yaw of the rendered entity
     * @param headScale         Head scale of the entity
     */
    @Override
    public void render(Entity ent, float limbSwingTime, float limbSwingDistance, float limbSwingOffset, float headYaw,
                       float headPitch, float headScale) {
    }
}
