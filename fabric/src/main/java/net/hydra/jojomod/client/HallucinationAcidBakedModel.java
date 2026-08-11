package net.hydra.jojomod.client;

import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.hydra.jojomod.event.ModEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public final class HallucinationAcidBakedModel extends ForwardingBakedModel {
    public HallucinationAcidBakedModel(BakedModel wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos,
                               Supplier<RandomSource> randomSupplier, RenderContext context) {
        if (!acidIsHidden()) super.emitBlockQuads(blockView, state, pos, randomSupplier, context);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face,
                                    RandomSource random) {
        return acidIsHidden() ? Collections.emptyList() : super.getQuads(state, face, random);
    }

    private static boolean acidIsHidden() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) return false;
        MobEffectInstance effect = minecraft.player.getEffect(ModEffects.HALLUCINATION);
        return effect != null;
    }
}
