package net.hydra.jojomod.mixin.access.models;

import net.minecraft.client.model.CreeperModel;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CreeperModel.class)
public interface AccessCreeperModel {
    @Accessor("head")
    ModelPart roundabout$getHead();
    @Accessor("root")
    ModelPart roundabout$getRoot();
    @Accessor("rightHindLeg")
    ModelPart roundabout$getRightHindLeg();
    @Accessor("leftHindLeg")
    ModelPart roundabout$getLeftHindLeg();
    @Accessor("rightFrontLeg")
    ModelPart roundabout$getRightFrontLeg();
    @Accessor("leftFrontLeg")
    ModelPart roundabout$getLeftFrontLeg();
}