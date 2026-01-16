package net.hydra.jojomod.mixin.justice;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IHumanoidArmorLayer;
import net.hydra.jojomod.access.IPlayerModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidArmorLayer.class)
public abstract class FogCloneHumanoidArmorLayer<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> implements IHumanoidArmorLayer {

    @Unique
    private int rdbt$killSwitch = 0;


    @Unique
    @Override
    public int getRdbt$killSwitch() {
        return rdbt$killSwitch;
    }

    @Unique
    @Override
    public void setRdbt$killSwitch(int rdbt$killSwitch) {
        this.rdbt$killSwitch = rdbt$killSwitch;
    }

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V", at = @At(value = "HEAD"),cancellable = true)
    protected void roundabout$tick(PoseStack $$0, MultiBufferSource $$1, int $$2, T $$3, float $$4, float $$5, float $$6, float $$7, float $$8, float $$9, CallbackInfo ci) {
        if (rdbt$killSwitch > 0 && $$3 != null) {
            EntityRenderDispatcher dispatch = Minecraft.getInstance().getEntityRenderDispatcher();
            EntityRenderer<?> ER = dispatch.getRenderer($$3);
            if (ER instanceof LivingEntityRenderer PR && PR.getModel() instanceof PlayerModel<?> pm) {
                if (rdbt$killSwitch == 1) {
                    if (!((IPlayerModel)pm).roundabout$getSlim()){
                        ci.cancel();
                    }
                } else {
                    if (((IPlayerModel)pm).roundabout$getSlim()){
                        ci.cancel();
                    }
                }
            }
        }
    }

    public FogCloneHumanoidArmorLayer(RenderLayerParent<T, M> $$0) {
        super($$0);
    }
}
