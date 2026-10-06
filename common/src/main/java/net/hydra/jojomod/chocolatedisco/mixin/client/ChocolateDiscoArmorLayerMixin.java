package net.hydra.jojomod.chocolatedisco.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.chocolatedisco.client.ChocolateDiscoAnimationState;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidArmorLayer.class)
public abstract class ChocolateDiscoArmorLayerMixin<
        T extends LivingEntity,
        M extends HumanoidModel<T>
        > extends RenderLayer<T, M> {

    protected ChocolateDiscoArmorLayerMixin() {
        super(null);
    }

    @Inject(
            method = "render",
            at = @At("HEAD"),
            cancellable = true
    )
    private void roundabout$hideArmorDuringSelfDisco(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            T entity,
            float limbSwing,
            float limbSwingAmount,
            float partialTick,
            float ageInTicks,
            float netHeadYaw,
            float headPitch,
            CallbackInfo ci
    ) {

        if (
                ChocolateDiscoAnimationState
                        .isSelfDiscoActive(entity.getUUID())
        ) {
            ci.cancel();
        }
    }
}