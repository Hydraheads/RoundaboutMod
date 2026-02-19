package net.hydra.jojomod.mixin.access.models;

import net.minecraft.client.model.FoxModel;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FoxModel.class)
public interface AccessFoxModel {

    @Accessor("head")
    ModelPart roundabout$getHead();
}
