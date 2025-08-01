package net.hydra.jojomod.mixin.vanilla_tweaks;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.util.config.ClientConfig;
import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.HorseMarkingLayer;
import net.minecraft.world.entity.animal.horse.Horse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HorseMarkingLayer.class)
public class SBRHorseMarkingLayer {
    /**Prevents horses with sbr names/skins from rendering other patterns*/

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/animal/horse/Horse;FFFFFF)V", at = @At(value = "HEAD"), cancellable = true)
    public void roundabout$render(PoseStack $$0, MultiBufferSource $$1, int $$2, Horse $$3, float $$4, float $$5, float $$6, float $$7, float $$8, float $$9, CallbackInfo ci) {
        ClientConfig clientC = ConfigManager.getClientConfig();
        if (clientC != null && clientC.vanillaMinecraftTweaks != null) {
            if (clientC.vanillaMinecraftTweaks.namedSBRHorseSkins) {
                String s = ChatFormatting.stripFormatting($$3.getName().getString());
                if ("Slow Dancer".equals(s) || "スロー・ダンサー".equals(s)) {
                    ci.cancel();
                } else if ("Valkyrie".equals(s) || "ヴァルキリー".equals(s)) {
                    ci.cancel();
                } else if ("Silver Bullet".equals(s)) {
                    ci.cancel();
                } else if ("Gets Up".equals(s) || "Brown".equals(s)) {
                    ci.cancel();
                }
            }
        }
    }
}
