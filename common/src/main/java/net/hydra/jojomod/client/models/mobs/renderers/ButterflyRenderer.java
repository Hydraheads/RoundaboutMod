package net.hydra.jojomod.client.models.mobs.renderers;

import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.mobs.ButterflyModel;
import net.hydra.jojomod.client.models.mobs.layers.ButterflyColorLayer;
import net.hydra.jojomod.entity.mobs.Butterfly;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;

public class ButterflyRenderer extends MobRenderer<Butterfly, ButterflyModel<Butterfly>> {
    private static final ResourceLocation BLANK_TEXTURE = new ResourceLocation("roundabout:textures/entity/butterfly/blank.png");

    // Body textures
    private static final ResourceLocation[] BODY_TEXTURES = {
            new ResourceLocation("roundabout:textures/entity/butterfly/morpho_body.png"),
            new ResourceLocation("roundabout:textures/entity/butterfly/monarch_body.png"),
            new ResourceLocation("roundabout:textures/entity/butterfly/swallowtail_body.png"),
            new ResourceLocation("roundabout:textures/entity/butterfly/vanessa_body.png")
    };

    // Inner wing textures
    private static final ResourceLocation[] INNER_WING_PATTERNS = {
            new ResourceLocation("roundabout:textures/entity/butterfly/morpho_inner.png"),
            new ResourceLocation("roundabout:textures/entity/butterfly/monarch_inner.png"),
            new ResourceLocation("roundabout:textures/entity/butterfly/swallowtail_inner.png"),
            new ResourceLocation("roundabout:textures/entity/butterfly/vanessa_inner.png")
    };

    // Outer wing textures
    private static final ResourceLocation[] OUTER_WING_PATTERNS = {
            new ResourceLocation("roundabout:textures/entity/butterfly/morpho_outer.png"),
            new ResourceLocation("roundabout:textures/entity/butterfly/monarch_outer.png"),
            new ResourceLocation("roundabout:textures/entity/butterfly/swallowtail_outer.png"),
            new ResourceLocation("roundabout:textures/entity/butterfly/vanessa_outer.png")
    };

    public ButterflyRenderer(EntityRendererProvider.Context context) {
        super(context, new ButterflyModel<>(context.bakeLayer(ModEntityRendererClient.BUTTERFLY_LAYER)), 0.2f);

        // color everything
        this.addLayer(new ButterflyColorLayer<>(this, new TextureAndColorProvider() {
            @Override
            public ResourceLocation getTexture(Butterfly entity) {
                return BODY_TEXTURES[entity.getPatternVariant()];
            }
            @Override
            public float[] getColor(Butterfly entity) {
                return entity.getBodyColor().getTextureDiffuseColors();
            }
        }));

        this.addLayer(new ButterflyColorLayer<>(this, new TextureAndColorProvider() {
            @Override
            public ResourceLocation getTexture(Butterfly entity) {
                return INNER_WING_PATTERNS[entity.getPatternVariant()];
            }
            @Override
            public float[] getColor(Butterfly entity) {
                return entity.getInnerWingColor().getTextureDiffuseColors();
            }
        }));

        this.addLayer(new ButterflyColorLayer<>(this, new TextureAndColorProvider() {
            @Override
            public ResourceLocation getTexture(Butterfly entity) {
                return OUTER_WING_PATTERNS[entity.getPatternVariant()];
            }
            @Override
            public float[] getColor(Butterfly entity) {
                return entity.getOuterWingColor().getTextureDiffuseColors();
            }
        }));
    }

    @Override
    public ResourceLocation getTextureLocation(Butterfly entity) {
        return BLANK_TEXTURE;
    }

    public interface TextureAndColorProvider {
        ResourceLocation getTexture(Butterfly entity);
        float[] getColor(Butterfly entity);
    }
}