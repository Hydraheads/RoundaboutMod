package net.hydra.jojomod.mixin.access;

import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(HorseModel.class)
public interface AccessHorseModel {

    @Accessor("body")
    ModelPart roundabout$getBody();

}
