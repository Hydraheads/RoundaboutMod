package net.hydra.jojomod.chocolatedisco.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.chocolatedisco.client.models.stand.ChocolateDiscoModel;
import net.hydra.jojomod.chocolatedisco.client.models.stand.ChocolateDiscoSlimModel;
import net.hydra.jojomod.client.models.stand.renderers.StandRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.hydra.jojomod.chocolatedisco.ChocolateDiscoEntity;

public class ChocolateDiscoRenderer extends StandRenderer<ChocolateDiscoEntity> {

    private static final ResourceLocation[] TEXTURES = {
            new ResourceLocation(
                    "roundabout",
                    "textures/entity/chocolate_disco_default.png"
            ),
            new ResourceLocation(
                    "roundabout",
                    "textures/entity/chocolate_disco_black_white.png"
            ),
            new ResourceLocation(
                    "roundabout",
                    "textures/entity/chocolate_disco_p_cubed.png"
            ),
            new ResourceLocation(
                    "roundabout",
                    "textures/entity/chocolate_disco_battleship.png"
            ),
            new ResourceLocation(
                    "roundabout",
                    "textures/entity/chocolate_disco_ball.png"
            ),
            new ResourceLocation(
                    "roundabout",
                    "textures/entity/chocolate_disco_checkers.png"
            ),
            new ResourceLocation(
                    "roundabout",
                    "textures/entity/chocolate_disco_tablet.png"
            ),
            new ResourceLocation(
                    "roundabout",
                    "textures/entity/chocolate_disco_neopolitan.png"
            ),
            new ResourceLocation(
                    "roundabout",
                    "textures/entity/chocolate_disco_chocolate.png"
            )
    };

    private ChocolateDiscoSlimModel slimModel;

    public ChocolateDiscoRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new ChocolateDiscoModel(
                        context.bakeLayer(
                                net.hydra.jojomod.chocolatedisco.client.ChocolateDiscoClient.CHOCOLATE_DISCO_LAYER
                        )
                ),
                0.0F
        );

        slimModel = new ChocolateDiscoSlimModel(
                context.bakeLayer(
                        net.hydra.jojomod.chocolatedisco.client.ChocolateDiscoClient.CHOCOLATE_DISCO_SLIM_LAYER
                )
        );
    }

    @Override
    public ResourceLocation getTextureLocation(ChocolateDiscoEntity entity) {

        int skin =
                Math.max(
                        0,
                        Math.min(
                                entity.getSkin(),
                                TEXTURES.length - 1
                        )
                );

        return TEXTURES[skin];
    }

    @Override
    public void render(
            ChocolateDiscoEntity entity,
            float entityYaw,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight
    ) {
        if (!net.hydra.jojomod.client.ClientUtil.inPowerInventory) {
            return;
        }

        Player player = null;

        if (entity.getUser() instanceof Player) {
            player = (Player) entity.getUser();
        }

        if (player != null
                && player instanceof net.minecraft.client.player.AbstractClientPlayer clientPlayer
                && clientPlayer.getModelName().equals("slim")) {

            this.model = slimModel;
        } else {
            this.model =
                    new ChocolateDiscoModel(
                            Minecraft.getInstance()
                                    .getEntityModels()
                                    .bakeLayer(
                                            net.hydra.jojomod.chocolatedisco.client.ChocolateDiscoClient.CHOCOLATE_DISCO_LAYER
                                    )
                    );
        }

        super.render(
                entity,
                entityYaw,
                partialTick,
                poseStack,
                buffer,
                packedLight
        );
    }
}