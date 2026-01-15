package net.hydra.jojomod.mixin.items.visages;


import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.entity.visages.CloneEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.MaskItem;
import net.hydra.jojomod.stand.powers.PowersGreenDay;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = HumanoidArmorLayer.class, priority = 102)
public abstract class VisageHumanoidArmorLayer<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> {

    /**Hides armor while wearing a visage*/

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V",
    at = @At(value = "HEAD"),cancellable = true)
    public void roundabout$Render(PoseStack $$0, MultiBufferSource $$1, int $$2, T $$3, float $$4, float $$5, float $$6, float $$7, float $$8, float $$9, CallbackInfo ci) {
        if ($$3 instanceof Player PE) {
            if (!((IPlayerEntity)PE).roundabout$getMaskSlot().isEmpty()
                    && ((IPlayerEntity)PE).roundabout$getMaskSlot().getItem() instanceof MaskItem ME
                    && !ME.visageData.generateVisageData(PE).rendersArmor() &&
                    !($$3.isInvisible() && ((IEntityAndData) $$3).roundabout$getTrueInvisibility() <= -1)
            ){
                ci.cancel();
            }
        } else if ($$3 instanceof CloneEntity CE && CE.player != null) {
            if (!((IPlayerEntity)CE.player).roundabout$getMaskSlot().isEmpty()
                    && ((IPlayerEntity)CE.player).roundabout$getMaskSlot().getItem() instanceof MaskItem ME
                    && !ME.visageData.generateVisageData(CE.player).rendersArmor() &&
                    !($$3.isInvisible() && ((IEntityAndData) $$3).roundabout$getTrueInvisibility() <= -1)
            ){
                ci.cancel();
            }
        }
    }

    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

    @Inject(method= "renderArmorPiece",at = @At(value="HEAD"),cancellable = true)
    private void hidespecifcpieces(PoseStack $$0, MultiBufferSource $$1, T $$2, EquipmentSlot $$3, int $$4, A $$5, CallbackInfo ci) {
        if (((StandUser) $$2).roundabout$getStandPowers() instanceof PowersGreenDay pgd) {
            boolean lowerhalf = $$3.getName().equals("legs") || $$3.getName().equals("feet");
            if (pgd.legGoneTicks > 0 && lowerhalf) {
                ci.cancel();
            }
        }
    }
    public VisageHumanoidArmorLayer(RenderLayerParent<T, M> $$0) {
        super($$0);
    }
}
