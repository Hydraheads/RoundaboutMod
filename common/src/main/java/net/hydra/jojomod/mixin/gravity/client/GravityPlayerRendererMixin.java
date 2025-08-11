package net.hydra.jojomod.mixin.gravity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PlayerRenderer.class)
public abstract class GravityPlayerRendererMixin {
    @ModifyVariable(
            method = "setupRotations(Lnet/minecraft/client/player/AbstractClientPlayer;Lcom/mojang/blaze3d/vertex/PoseStack;FFF)V",
            at = @At(
                    value = "STORE"
            ),
            ordinal = 0
    )
    private Vec3 modify_setupTransforms_Vec3d_0(Vec3 vec, AbstractClientPlayer instance, PoseStack $$1, float $$2, float $$3, float partialTick) {
        Direction gravityDirection = GravityAPI.getGravityDirection(instance);
        if (gravityDirection == Direction.DOWN)
            return vec;
        Vec3 viewVector = instance.getViewVector(partialTick);

        return RotationUtil.vecWorldToPlayer(viewVector, gravityDirection);
    }
}