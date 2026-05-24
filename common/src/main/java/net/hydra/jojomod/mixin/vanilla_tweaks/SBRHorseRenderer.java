package net.hydra.jojomod.mixin.vanilla_tweaks;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.mobs.layers.HorseVampireEyeLayer;
import net.hydra.jojomod.util.config.ClientConfig;
import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.renderer.entity.AbstractHorseRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HorseRenderer;
import net.minecraft.client.renderer.entity.layers.HorseMarkingLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.horse.Horse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HorseRenderer.class)
public abstract class SBRHorseRenderer extends AbstractHorseRenderer<Horse, HorseModel<Horse>> {
    /**Makes horses named after sbr horses have custom skins>*/
    @Unique
    ResourceLocation roundabout$slowDancerTexture = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/slow_dancer.png");
    @Unique
    ResourceLocation roundabout$valkyrieTexture = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/valkyrie.png");
    @Unique
    ResourceLocation roundabout$silverBulletTexture = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/silver_bullet.png");
    @Unique
    ResourceLocation roundabout$getsUpTexture = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/gets_up.png");

    public SBRHorseRenderer(EntityRendererProvider.Context $$0, HorseModel<Horse> $$1, float $$2) {
        super($$0, $$1, $$2);
    }

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

    @Inject(method = "<init>(Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;)V", at = @At(value = "RETURN"))
    public void roundabout$init(EntityRendererProvider.Context $$0, CallbackInfo ci) {
        this.addLayer(new HorseVampireEyeLayer<>(this));
    }
}