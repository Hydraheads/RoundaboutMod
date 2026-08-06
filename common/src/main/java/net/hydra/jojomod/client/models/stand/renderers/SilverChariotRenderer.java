package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.SilverChariotModel;
import net.hydra.jojomod.entity.stand.SilverChariotEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class SilverChariotRenderer<T extends StandEntity> extends StandRenderer<SilverChariotEntity> {

    private static final ResourceLocation ANIME_PART_3 = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/anime_part_3.png");

    public SilverChariotRenderer(EntityRendererProvider.Context context) {
        super(context, new SilverChariotModel<>(context.bakeLayer(ModEntityRendererClient.SILVER_CHARIOT_LAYER)), 0f);
    }

    @Override
    public ResourceLocation getTextureLocation(SilverChariotEntity entity) {
        // Yes, I am aware that it is not proper to have only one case in a switch statement
        return switch (entity.getSkin()) {
            default -> ANIME_PART_3;
        };
    }

    @Override
    public void render(SilverChariotEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
        if (!(mobEntity.getUser() != null && Minecraft.getInstance().player != null &&
                mobEntity.getUser().is(Minecraft.getInstance().player))) {
            float factor = 0.5F + (mobEntity.getSizePercent()/2);
            if (mobEntity.isBaby()) {
                matrixStack.scale(0.5f*factor, 0.5f*factor, 0.5f*factor);
            } else {
                matrixStack.scale(0.87f * factor, 0.87f * factor, 0.87f * factor);
            }
            LivingEntity user = mobEntity.getUser();
            if (user != null) {
                Player pl = Minecraft.getInstance().player;
                StandUser standUser = ((StandUser) mobEntity.getUser());
                StandPowers standPowers = standUser.roundabout$getStandPowers();
                if (standPowers.isPiloting()) {
                    if (standPowers.getPilotingStand() != null &&
                            standPowers.getPilotingStand().is(mobEntity)
                    ) {
                        boolean fp = Minecraft.getInstance().options.getCameraType().isFirstPerson();
                        if (fp && !mobEntity.getDisplay() && pl != null && user.is(pl)) {
                            this.model.getHead().visible = false;
                        }
                    }
                }
                this.model.getHead().visible = true;
            }
        }
        // super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
        // this.model.getHead().visible = true;
    }

    @Nullable
    @Override
    protected RenderType getRenderType(SilverChariotEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderType(entity, showBody, true, showOutline);
    }
}
