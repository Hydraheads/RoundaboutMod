package net.hydra.jojomod.mixin.achtung;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.client.ClientUtil;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidArmorLayer.class)
public abstract class AchtungHumanoidArmorLayer<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> {

    /***
     * Code for Achtung Baby Armor Rendering!
     * When you switch to third person, the armor is fully solid without this mixin.
     * This mixin adds support for armor rendering transparency. :D
     *
     * HOWEVER, Forge replaced Minecraft armor rendering entirely (cringe), so
     * there is a separate forge only mixin for this as well.
     *
     */

    @Inject(method = "renderModel(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/item/ArmorItem;Lnet/minecraft/client/model/HumanoidModel;ZFFFLjava/lang/String;)V",
            at = @At(value = "HEAD"), cancellable = true)
    public void roundabout$Render (PoseStack $$0, MultiBufferSource $$1,int $$2, ArmorItem $$3, A $$4,boolean $$5,
    float $$6, float $$7, float $$8, String $$9, CallbackInfo ci){
        if (ClientUtil.getThrowFadeToTheEther() != 1) {
            VertexConsumer $$10 = $$1.getBuffer(RenderType.entityTranslucentCull(this.getArmorLocation($$3, $$5, $$9)));
            $$4.renderToBuffer($$0, $$10, $$2, OverlayTexture.NO_OVERLAY, $$6, $$7, $$8, 1.0F);
            ci.cancel();
        }
    }



    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

    public AchtungHumanoidArmorLayer(RenderLayerParent<T, M> $$0) {
        super($$0);
    }

    @Shadow protected abstract ResourceLocation getArmorLocation(ArmorItem $$0, boolean $$1, @Nullable String $$2);
}