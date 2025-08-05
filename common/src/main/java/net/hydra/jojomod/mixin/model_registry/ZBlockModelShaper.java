package net.hydra.jojomod.mixin.model_registry;

import net.minecraft.client.renderer.block.BlockModelShaper;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockModelShaper.class)
public abstract class ZBlockModelShaper {

    /**Dynamic fog blocks mixin*/

/**
    @Inject(method="stateToModelLocation(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/client/resources/model/ModelResourceLocation;", at=@At("HEAD"), cancellable = true)
    private static void roundabout$stateToModelLocation(ResourceLocation identifier, BlockState state, CallbackInfoReturnable<ModelResourceLocation> cir)
    {
        if (identifier.getPath().startsWith("fog_"))
            cir.setReturnValue(new ModelResourceLocation(Roundabout.location("fog_block"), statePropertiesToString(state.getValues())));
    }
    */
}
