package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.layers.StandGlowingLayer;
import net.hydra.jojomod.client.models.stand.TuskAct2Model;
import net.hydra.jojomod.entity.stand.TuskEntity;
import net.hydra.jojomod.stand.powers.PowersTusk;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class TuskAct2Renderer extends StandRenderer<TuskEntity> {
    public TuskAct2Renderer(EntityRendererProvider.Context context) {
        super(context, new TuskAct2Model<>(context.bakeLayer(ModEntityRendererClient.TUSK_A2_LAYER)), 0f);
        this.addLayer(new StandGlowingLayer<>(this,this));
    }
    @Override public ResourceLocation getTextureLocation(TuskEntity entity) {return PowersTusk.getSkin(entity.getSkin(),2);}
    @Override public RenderType getGlowingReference(byte skin) {
        ResourceLocation emissive = PowersTusk.getEmissiveSkin(skin,2);
        if (emissive != null) {
            return RenderType.eyes(emissive);
        }
        return null;
    }

    @Override
    public PoseStack modifyStack(PoseStack stack) {
        stack.translate(0,-0.55F,0);
        stack.scale(1.33333F,1.33333F,1.33333F);
        return super.modifyStack(stack);
    }

    @Override public boolean hasGlowingParts(byte skin, int packedLight) {return skin == PowersTusk.CREAKING;}
}
