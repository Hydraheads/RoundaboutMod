package net.hydra.jojomod.mixin.cream;

import com.mojang.blaze3d.vertex.PoseStack;
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
    public void roundabout$Render(Entity $$0, double $$1, double $$2, double $$3, float $$4, float $$5, PoseStack $$6, MultiBufferSource $$7, int $$8, CallbackInfo ci) {
        if ($$0 instanceof Player pl && pl instanceof StandUser standUser) {
            if (standUser.roundabout$getStandPowers() instanceof PowersCream cream && cream.insideVoidInt > 0) {
                ci.cancel();
            }
        }
    }
}
