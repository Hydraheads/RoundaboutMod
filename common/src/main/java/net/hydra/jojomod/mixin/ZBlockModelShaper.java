package net.hydra.jojomod.mixin;

import net.hydra.jojomod.Roundabout;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.client.renderer.block.BlockModelShaper.statePropertiesToString;

@Mixin(BlockModelShaper.class)
public abstract class ZBlockModelShaper {
    @Inject(method="stateToModelLocation(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/client/resources/model/ModelResourceLocation;", at=@At("HEAD"), cancellable = true)
    private static void roundabout$stateToModelLocation(ResourceLocation identifier, BlockState state, CallbackInfoReturnable<ModelResourceLocation> cir)
    {
        if (identifier.getPath().startsWith("fog_"))
            cir.setReturnValue(new ModelResourceLocation(Roundabout.location("fog_block"), statePropertiesToString(state.getValues())));
    }
}
