package net.hydra.jojomod.mixin.items;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.client.models.layers.WornStoneMaskLayer;
import net.hydra.jojomod.item.BowlerHatItem;
import net.hydra.jojomod.item.UltravioletBlasterItem;
import net.minecraft.client.model.ArmorStandArmorModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorStandRenderer.class)
public abstract class ArmorStandNitpickMixin extends LivingEntityRenderer<ArmorStand, ArmorStandArmorModel> {

    @Inject(method = "<init>(Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;)V", at = @At("RETURN"))
    private void rdbt$init(EntityRendererProvider.Context $$0, CallbackInfo ci) {
        this.addLayer(new WornStoneMaskLayer<>($$0, this));
    }



    public ArmorStandNitpickMixin(EntityRendererProvider.Context $$0, ArmorStandArmorModel $$1, float $$2) {
        super($$0, $$1, $$2);
    }

}
