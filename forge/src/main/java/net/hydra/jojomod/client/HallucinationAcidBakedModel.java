package net.hydra.jojomod.client;

import net.hydra.jojomod.event.ModEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public final class HallucinationAcidBakedModel extends BakedModelWrapper<BakedModel> {
    public HallucinationAcidBakedModel(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side,
                                    RandomSource random) {
        return acidIsHidden() ? Collections.emptyList() : super.getQuads(state, side, random);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side,
                                    RandomSource random, ModelData modelData,
                                    @Nullable RenderType renderType) {
        return acidIsHidden() ? Collections.emptyList()
                : super.getQuads(state, side, random, modelData, renderType);
    }

    private static boolean acidIsHidden() {
        Minecraft minecraft = Minecraft.getInstance();
        return minecraft.player != null
                && minecraft.player.hasEffect(ModEffects.HALLUCINATION);
    }
}
