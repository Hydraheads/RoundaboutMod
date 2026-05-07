package net.hydra.jojomod.mixin.green_day;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersGreenDay;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value =HumanoidArmorLayer.class)
public abstract class GreenDayArmourLayer<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> {
    @Inject(method= "renderArmorPiece",at = @At(value="HEAD"),cancellable = true)
    private void hidespecifcpieces(PoseStack $$0, MultiBufferSource $$1, T $$2, EquipmentSlot $$3, int $$4, A $$5, CallbackInfo ci){
        if(((StandUser) $$2).roundabout$getStandPowers() instanceof PowersGreenDay pgd) {
            boolean lowerhalf = $$3.getName().equals("legs") || $$3.getName().equals("feet");
            if (pgd.legGoneTicks > 0 && lowerhalf) {
                ci.cancel();
            }
        }
    }

    public GreenDayArmourLayer(RenderLayerParent<T, M> $$0) {
        super($$0);
    }
}
