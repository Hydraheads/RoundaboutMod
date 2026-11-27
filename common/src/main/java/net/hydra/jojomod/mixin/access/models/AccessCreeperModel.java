package net.hydra.jojomod.mixin.access.models;

import net.minecraft.client.model.CreeperModel;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CreeperModel.class)
public interface AccessCreeperModel {
    @Accessor("head")
    ModelPart sba$getHead();
    @Accessor("root")
    ModelPart sba$getRoot();
    @Accessor("rightHindLeg")
    ModelPart sba$getRightHindLeg();
    @Accessor("leftHindLeg")
    ModelPart sba$getLeftHindLeg();
    @Accessor("rightFrontLeg")
    ModelPart sba$getRightFrontLeg();
    @Accessor("leftFrontLeg")
    ModelPart sba$getLeftFrontLeg();
}