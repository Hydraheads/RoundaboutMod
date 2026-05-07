package net.hydra.jojomod.mixin.manhattan;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.client.ClientUtil;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value =ElytraLayer.class)
public abstract class ManhattanElytraLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
  @Inject(method= "render",at = @At(value="HEAD"),cancellable = true)
    private void unrenderelytra(PoseStack $$0, MultiBufferSource $$1, int $$2, T $$3, float $$4, float $$5, float $$6, float $$7, float $$8, float $$9, CallbackInfo ci){
      if ($$3 != null) {
          IEntityAndData entityAndData = ((IEntityAndData) $$3);
          if(entityAndData.roundabout$getTrueInvisibilityManhattan() < 1 && ClientUtil.checkIfClientCanSeeMobsForWindVision()) {
              ci.cancel();
          }
      }
    }

    public ManhattanElytraLayer(RenderLayerParent<T, M> $$0) {
        super($$0);
    }
}
