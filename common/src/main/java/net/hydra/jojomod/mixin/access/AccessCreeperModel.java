package net.hydra.jojomod.mixin.access;

import net.minecraft.client.model.CreeperModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.particle.Particle;
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