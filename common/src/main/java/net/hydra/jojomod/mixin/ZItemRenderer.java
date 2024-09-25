package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ModItemModels;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.client.model.TridentModel;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(ItemRenderer.class)
public class ZItemRenderer {

    @Shadow
    @Final
    private ItemModelShaper itemModelShaper;
    @ModifyVariable(method = "render", at = @At(value = "HEAD"), argsOnly = true)
    public BakedModel roundabout$render(
            BakedModel value, ItemStack stack, ItemDisplayContext renderMode, boolean leftHanded, PoseStack matrices,
            MultiBufferSource vertexConsumers, int light, int overlay, BakedModel $$7) {
        if (stack.is(ModItems.HARPOON) && renderMode != ItemDisplayContext.GUI && renderMode != ItemDisplayContext.GROUND) {
            return this.itemModelShaper.getModelManager().
                    getModel(ModItemModels.HARPOON_IN_HAND);
        }

        return value;
    }

    @ModifyVariable(method = "getModel", at = @At(value = "STORE"), ordinal = 0)
    public BakedModel roundabout$render(
            BakedModel value,ItemStack $$0, @Nullable Level $$1, @Nullable LivingEntity $$2, int $$3) {
        if ($$0.is(Items.BOW) && $$2 !=null){
            if ($$2.getProjectile($$0).getItem() == ModItems.STAND_ARROW){
                return this.itemModelShaper.getModelManager().getModel(ModItemModels.STAND_BOW);
            } else if ($$2.getProjectile($$0).getItem() == ModItems.STAND_BEETLE_ARROW){
                return this.itemModelShaper.getModelManager().getModel(ModItemModels.STAND_BEETLE_BOW);
            }
        }

        return value;
    }

}
