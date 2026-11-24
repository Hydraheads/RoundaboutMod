package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.layers.StandGlowingLayer;
import net.hydra.jojomod.client.models.stand.TuskAct1Model;
import net.hydra.jojomod.entity.stand.TuskEntity;
import net.hydra.jojomod.stand.powers.PowersTusk;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class TuskAct1Renderer extends StandRenderer<TuskEntity> {
    public TuskAct1Renderer(EntityRendererProvider.Context context) {
        super(context, new TuskAct1Model<>(context.bakeLayer(ModEntityRendererClient.TUSK_A1_LAYER)), 0f);
        this.addLayer(new StandGlowingLayer<>(this,this));
    }
    @Override public ResourceLocation getTextureLocation(TuskEntity entity) {return PowersTusk.getSkin(entity.getSkin(),1);}
    @Override public RenderType getGlowingReference(byte skin) {
        ResourceLocation emissive = PowersTusk.getEmissiveSkin(skin,1);
        if (emissive != null) {
            return RenderType.eyes(emissive);
        }
        return null;
    }

    @Override
    public PoseStack modifyStack(PoseStack stack) {
        // reversed the pose stack changes in the model
        stack.translate(0,-0.7F,0);
        stack.scale(1.3333F,1.3333F,1.3333F);
        return super.modifyStack(stack);
    }

    @Override public boolean hasGlowingParts(byte skin, int packedLight) {return skin == PowersTusk.CREAKING;}
}
