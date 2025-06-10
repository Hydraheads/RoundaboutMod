package net.hydra.jojomod.mixin;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.util.ClientConfig;
import net.hydra.jojomod.util.Config;
import net.hydra.jojomod.util.ConfigManager;
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
    @Unique
    ResourceLocation roundabout$silverBulletTexture = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/silver_bullet.png");
    @Unique
    ResourceLocation roundabout$getsUpTexture = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/gets_up.png");
    @Inject(method = "getTextureLocation(Lnet/minecraft/world/entity/animal/horse/Horse;)Lnet/minecraft/resources/ResourceLocation;", at = @At(value = "HEAD"), cancellable = true)
    public void roundabout$getTextureLocation(Horse $$0, CallbackInfoReturnable<ResourceLocation> cir) {
        ClientConfig clientC = ConfigManager.getClientConfig();
        if (clientC != null && clientC.vanillaMinecraftTweaks != null) {
            if (clientC.vanillaMinecraftTweaks.namedSBRHorseSkins) {
                String s = ChatFormatting.stripFormatting($$0.getName().getString());
                if ("Slow Dancer".equals(s) || "スロー・ダンサー".equals(s)) {
                    cir.setReturnValue(roundabout$slowDancerTexture);
                } else if ("Valkyrie".equals(s) || "ヴァルキリー".equals(s)) {
                    cir.setReturnValue(roundabout$valkyrieTexture);
                } else if ("Silver Bullet".equals(s)) {
                    cir.setReturnValue(roundabout$silverBulletTexture);
                } else if ("Gets Up".equals(s) || "Brown".equals(s)) {
                    cir.setReturnValue(roundabout$getsUpTexture);
                }
            }
        }
    }
}