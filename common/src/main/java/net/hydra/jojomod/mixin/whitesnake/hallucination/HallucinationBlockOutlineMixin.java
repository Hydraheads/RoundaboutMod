package net.hydra.jojomod.mixin.whitesnake.hallucination;

import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.powers.whitesnake.HallucinationEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public abstract class HallucinationBlockOutlineMixin {

    @Inject(method = "shouldRenderBlockOutline()Z", at = @At("HEAD"), cancellable = true)
    private void roundaboutWhitesnake$hideAcidOutline(CallbackInfoReturnable<Boolean> cir) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.level == null
                || !HallucinationEffect.hasDistortion(minecraft.player.getEffect(ModEffects.HALLUCINATION))
                || !(minecraft.hitResult instanceof BlockHitResult hit)
                || hit.getType() != HitResult.Type.BLOCK) return;

        BlockState state = minecraft.level.getBlockState(hit.getBlockPos());
        if (state.is(ModBlocks.HALLUCINATORY_ACID)
                || state.is(ModBlocks.HALLUCINATORY_ACID_WALL)) {
            cir.setReturnValue(false);
        }
    }
}
