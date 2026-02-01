package net.hydra.jojomod.mixin.access.models;

import net.minecraft.client.model.WolfModel;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WolfModel.class)
public interface AccessWolfModel {

    @Accessor("head")
    ModelPart roundabout$getHead();
}
