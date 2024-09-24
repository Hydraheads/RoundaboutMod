package net.hydra.jojomod.mixin;

import net.hydra.jojomod.item.ModItems;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(ModelBakery.class)
public abstract class ZModelBakery {
    @Shadow
    protected abstract void loadTopLevel(ModelResourceLocation modelId);

    @Shadow @Final private Map<ResourceLocation, UnbakedModel> topLevelModels;

    @Shadow
    public UnbakedModel getModel(ResourceLocation $$0) {
        return null;
    }

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    public void roundabout$addITEMNAME(BlockColors $$0, ProfilerFiller $$1, Map $$2, Map $$3, CallbackInfo ci) {
        this.loadTopLevel(ModItems.HARPOON_IN_HAND);
        this.loadTopLevel(ModItems.STAND_BOW);
        this.topLevelModels.get(ModItems.HARPOON_IN_HAND).resolveParents(this::getModel);
        this.topLevelModels.get(ModItems.STAND_BOW).resolveParents(this::getModel);
    }
}
