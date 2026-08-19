package net.hydra.jojomod.mixin.silver_chariot;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class SilverChariotEntityRenderMixin {
    @Shadow
    private Level level;

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private <T extends Entity> void render(T $$0, double $$1, double $$2, double $$3, float $$4, float $$5, PoseStack $$6, MultiBufferSource $$7, int $$8, CallbackInfo ci) {
        Entity cameraEntity = Minecraft.getInstance().getCameraEntity();
    }

}
