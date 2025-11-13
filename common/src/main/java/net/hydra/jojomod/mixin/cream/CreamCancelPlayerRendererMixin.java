package net.hydra.jojomod.mixin.cream;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersCream;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public abstract class CreamCancelPlayerRendererMixin {

    protected CreamCancelPlayerRendererMixin() {
        super();
    }

    @Inject(method = "render(Lnet/minecraft/world/entity/Entity;DDDFFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("HEAD"), cancellable = true)
    public void roundabout$Render(Entity entity, double x, double y, double z, float yaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int light, CallbackInfo ci) {
        if (entity instanceof Player player) {
            if (player instanceof StandUser standUser && standUser.roundabout$getStandPowers() instanceof PowersCream PC && PC.insideVoidInt > 0) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "renderHitbox(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/entity/Entity;F)V", at = @At("HEAD"), cancellable = true)
    private static void roundabout$RenderHitboxes(PoseStack $$0, VertexConsumer $$1, Entity $$2, float $$3, CallbackInfo ci) {
        if ($$2 instanceof Player pl && pl instanceof StandUser standUser) {
            if (standUser.roundabout$getStandPowers() instanceof PowersCream cream && cream.insideVoidInt > 0) {
                ci.cancel();
            }
        }
    }
}
