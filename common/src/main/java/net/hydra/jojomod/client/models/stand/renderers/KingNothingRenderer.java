package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.layers.StandGlowingLayer;
import net.hydra.jojomod.client.models.stand.KingNothingModel;
import net.hydra.jojomod.client.models.stand.StandModel;
import net.hydra.jojomod.entity.stand.KingNothingEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class KingNothingRenderer extends StandRenderer<KingNothingEntity> {

    private static final ResourceLocation MANGA = Roundabout.location("textures/stand/king_nothing/manga.png");
    private static final ResourceLocation SPINE = Roundabout.location("textures/stand/king_nothing/spine.png");
    private static final ResourceLocation NOTE = Roundabout.location("textures/stand/king_nothing/note_block.png");
    private static final ResourceLocation BETA = Roundabout.location("textures/stand/king_nothing/beta.png");
    private static final ResourceLocation WARDEN = Roundabout.location("textures/stand/king_nothing/warden.png");
    private static final ResourceLocation SNIFFER = Roundabout.location("textures/stand/king_nothing/sniffer.png");
    private static final ResourceLocation SOMETHING = Roundabout.location("textures/stand/king_nothing/something.png");
    private static final ResourceLocation RAM = Roundabout.location("textures/stand/king_nothing/ram.png");

    public KingNothingRenderer(EntityRendererProvider.Context context) {
        super(context, new KingNothingModel<>(context.bakeLayer(ModEntityRendererClient.KING_NOTHING_LAYER)), 0f);
        this.addLayer(new StandGlowingLayer<>(this, this));
    }

    public static ResourceLocation getSkin(byte bt) {
        return switch (bt) {
            case KingNothingEntity.SPINE -> SPINE;
            case KingNothingEntity.NOTE -> NOTE;
            case KingNothingEntity.BETA -> BETA;
            case KingNothingEntity.WARDEN -> WARDEN;
            case KingNothingEntity.SNIFFER -> SNIFFER;
            case KingNothingEntity.SOMETHING -> SOMETHING;
            case KingNothingEntity.RAM -> RAM;
            default -> MANGA;
        };
    }

    @Override
    public void render(KingNothingEntity mobEntity, float f, float g, PoseStack matrixStack,
            MultiBufferSource vertexConsumerProvider, int i) {
        float factor = 0.5F + (mobEntity.getSizePercent()/2);
        if(mobEntity.isBaby()){
            matrixStack.scale(0.50f * factor, 0.50f * factor, 0.50f * factor);
        } else {
            matrixStack.scale(0.87f * factor, 0.87f * factor, 0.87f * factor);
        }
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(KingNothingEntity entity) {
        return getSkin(entity.getSkin());
    }

    private static final RenderType WARDEN_E = RenderType.eyes(Roundabout.location("textures/stand/king_nothing/warden_e.png"));

    @Override
    public boolean hasGlowingParts(byte skin, int packedLight) {
        return getGlowingReference(skin) != null;
    }

    @Override
    public RenderType getGlowingReference(byte skin) {
        return switch (skin) {
            case KingNothingEntity.WARDEN -> WARDEN_E;
            default -> null;
        };
    }
}
