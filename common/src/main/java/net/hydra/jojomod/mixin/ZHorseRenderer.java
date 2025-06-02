package net.hydra.jojomod.mixin;

import net.hydra.jojomod.Roundabout;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.entity.HorseRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.horse.Horse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HorseRenderer.class)
public class ZHorseRenderer {
    @Unique
    ResourceLocation roundabout$slowDancerTexture = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/slow_dancer.png");
    @Unique
    ResourceLocation roundabout$valkyrieTexture = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/valkyrie.png");
    @Inject(method = "getTextureLocation(Lnet/minecraft/world/entity/animal/horse/Horse;)Lnet/minecraft/resources/ResourceLocation;", at = @At(value = "HEAD"), cancellable = true)
    public void roundabout$getTextureLocation(Horse $$0, CallbackInfoReturnable<ResourceLocation> cir) {
        String s = ChatFormatting.stripFormatting($$0.getName().getString());
        if ("Slow Dancer".equals(s) || "Surō Dansā".equals(s) || "スロー・ダンサー".equals(s)) {
            cir.setReturnValue(roundabout$slowDancerTexture);
        } else if ("Valkyrie".equals(s) || "Varukirī".equals(s) || "ヴァルキリー".equals(s)) {
            cir.setReturnValue(roundabout$valkyrieTexture);
        }

    }
}