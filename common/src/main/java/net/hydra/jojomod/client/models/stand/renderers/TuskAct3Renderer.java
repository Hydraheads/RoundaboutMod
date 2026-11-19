package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.layers.StandGlowingLayer;
import net.hydra.jojomod.client.models.stand.TuskAct3Model;
import net.hydra.jojomod.entity.stand.TuskEntity;
import net.hydra.jojomod.stand.powers.PowersTusk;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class TuskAct3Renderer extends StandRenderer<TuskEntity> {
    public TuskAct3Renderer(EntityRendererProvider.Context context) {
        super(context, new TuskAct3Model<>(context.bakeLayer(ModEntityRendererClient.TUSK_A3_LAYER)), 0f);
        this.addLayer(new StandGlowingLayer<>(this,this));
    }
    @Override public ResourceLocation getTextureLocation(TuskEntity entity) {return PowersTusk.getSkin(entity.getSkin(),3);}
    @Override public RenderType getGlowingReference(byte skin) {
        ResourceLocation emissive = PowersTusk.getEmissiveSkin(skin,3);
        if (emissive != null) {
            return RenderType.eyes(emissive);
        }
        return null;
    }

    @Override
    public PoseStack modifyStack(PoseStack stack) {
        stack.translate(0,-0.1F,0);
        return super.modifyStack(stack);
    }

    @Override public boolean hasGlowingParts(byte skin, int packedLight) {return skin == PowersTusk.CREAKING;}

}
