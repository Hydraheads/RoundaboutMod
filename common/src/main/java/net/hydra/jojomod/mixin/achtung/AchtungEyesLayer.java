package net.hydra.jojomod.mixin.achtung;


import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(EyesLayer.class)
public abstract class AchtungEyesLayer<T extends Entity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    /**Turn spider eyes invisible.*/
    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/Entity;FFFFFF)V", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$render(PoseStack $$0, MultiBufferSource $$1, int $$2, T $$3, float $$4, float $$5, float $$6, float $$7, float $$8, float $$9, CallbackInfo ci) {
        if ($$0 != null) {
            if (MainUtil.getEntityIsTrulyInvisible($$3) && !ClientUtil.checkIfClientCanSeeInvisAchtung()){
                ci.cancel();
            }
        }
    }

    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */
    public AchtungEyesLayer(RenderLayerParent<T, M> $$0) {
        super($$0);
    }

}
