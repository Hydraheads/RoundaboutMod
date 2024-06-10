package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.PlayerItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandLayer.class)
public class ZItemInHandLayer<T extends LivingEntity, M extends EntityModel<T> & ArmedModel> extends RenderLayer<T, M> {
    public ZItemInHandLayer(RenderLayerParent<T, M> $$0) {
        super($$0);
    }


    public boolean cleared = false;
    public boolean roundaboutModifyEntity;
    public @Nullable ItemStack roundaboutRenderMainHand;
    public @Nullable ItemStack roundaboutRenderOffHand;
    public boolean dominantHand;
    @Inject(method = "render", at = @At(value = "HEAD"),cancellable = true)
    public void roundaboutRender(PoseStack $$0, MultiBufferSource $$1, int $$2, T $$3, float $$4, float $$5, float $$6, float $$7, float $$8, float $$9, CallbackInfo ci){
        dominantHand = $$3.getMainArm() == HumanoidArm.RIGHT;
        if ($$3 instanceof Player) {
            roundaboutModifyEntity = ((TimeStop) $$3.level()).CanTimeStopEntity($$3);
            if (roundaboutModifyEntity) {
                if (((IEntityAndData) $$3).getRoundaboutRenderMainHand() == null){
                    ((IEntityAndData) $$3).setRoundaboutRenderMainHand($$3.getMainHandItem().copy());
                }
                if (((IEntityAndData) $$3).getRoundaboutRenderOffHand() == null){
                    ((IEntityAndData) $$3).setRoundaboutRenderOffHand($$3.getOffhandItem().copy());
                }
                roundaboutRenderMainHand = ((IEntityAndData) $$3).getRoundaboutRenderMainHand();
                roundaboutRenderOffHand = ((IEntityAndData) $$3).getRoundaboutRenderOffHand();
            } else {
                if (((IEntityAndData) $$3).getRoundaboutRenderOffHand() != null){
                    ((IEntityAndData) $$3).setRoundaboutRenderOffHand(null);
                } else if (((IEntityAndData) $$3).getRoundaboutRenderMainHand() != null){
                    ((IEntityAndData) $$3).setRoundaboutRenderMainHand(null);
                }
            }
        } else {
            roundaboutModifyEntity = false;
        }
    }

    @ModifyVariable(method = "render", at = @At(value = "STORE"), ordinal = 0)
    public ItemStack roundaboutRender2(ItemStack it){
        if (roundaboutModifyEntity) {
            if (!dominantHand) {
                if (roundaboutRenderMainHand != null) {
                    return roundaboutRenderMainHand;
                }
            } else {
                if (roundaboutRenderOffHand != null) {
                    return roundaboutRenderOffHand;
                }
            }
        }
        return it;
    }

    @ModifyVariable(method = "render", at = @At(value = "STORE"), ordinal = 1)
    public ItemStack roundaboutRender3(ItemStack it){
        if (roundaboutModifyEntity) {
            if (dominantHand) {
                if (roundaboutRenderMainHand != null) {
                    return roundaboutRenderMainHand;
                }
            } else {
                if (roundaboutRenderOffHand != null) {
                    return roundaboutRenderOffHand;
                }
            }
        }
        return it;
    }

    @Shadow
    protected void renderArmWithItem(LivingEntity $$0, ItemStack $$1, ItemDisplayContext $$2, HumanoidArm $$3, PoseStack $$4, MultiBufferSource $$5, int $$6){
    }
    @Shadow
    public void render(PoseStack var1, MultiBufferSource var2, int var3, T var4, float var5, float var6, float var7, float var8, float var9, float var10) {
    }
}
