package net.hydra.jojomod.mixin.access.models;

import net.minecraft.client.model.VillagerModel;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(VillagerModel.class)
public interface AccessVillagerModel {

    @Accessor("root")
    ModelPart roundabout$getRoot();
}
