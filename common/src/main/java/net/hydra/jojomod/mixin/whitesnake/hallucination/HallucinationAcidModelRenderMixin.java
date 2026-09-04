package net.hydra.jojomod.mixin.whitesnake.hallucination;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.powers.whitesnake.HallucinationEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelBlockRenderer.class)
public abstract class HallucinationAcidModelRenderMixin {
    @Inject(method = "tesselateBlock", at = @At("HEAD"), cancellable = true)
    private void roundaboutWhitesnake$hideAcidModel(BlockAndTintGetter level, BakedModel model,
                                                     BlockState state, BlockPos pos, PoseStack pose,
                                                     VertexConsumer consumer, boolean checkSides,
                                                     RandomSource random, long seed, int overlay,
                                                     CallbackInfo ci) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || !(state.is(ModBlocks.HALLUCINATORY_ACID)
                || state.is(ModBlocks.HALLUCINATORY_ACID_WALL))) return;
        MobEffectInstance effect = minecraft.player.getEffect(ModEffects.HALLUCINATION);
        if (HallucinationEffect.hasDistortion(effect)) ci.cancel();
    }
}
