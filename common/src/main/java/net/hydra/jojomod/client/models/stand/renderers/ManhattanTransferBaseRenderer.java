package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.ManhattanTransferModel;
import net.hydra.jojomod.client.models.stand.StandModel;
import net.hydra.jojomod.entity.stand.DarkMirageEntity;
import net.hydra.jojomod.entity.stand.JusticeEntity;
import net.hydra.jojomod.entity.stand.ManhattanTransferEntity;
import net.hydra.jojomod.entity.stand.PollinationTransferEntity;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersManhattanTransfer;
import net.hydra.jojomod.stand.powers.PowersStarPlatinum;
import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

//import static net.hydra.jojomod.entity.stand.ManhattanTransferEntity.RADIOACTIVE_SKIN;

public class ManhattanTransferBaseRenderer extends StandRenderer<ManhattanTransferEntity> {

    public ManhattanTransferBaseRenderer(EntityRendererProvider.Context context, StandModel<ManhattanTransferEntity> entityModel, float f) {
        super(context, entityModel,f);
    }

    private static final ResourceLocation ANIME_SKIN = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/manhattan_transfer/manhattan_part_6.png");
    private static final ResourceLocation MANGA_SKIN = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/manhattan_transfer/manhattan_manga_part_6.png");
    private static final ResourceLocation AERO_TRANSFER_SKIN = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/manhattan_transfer/manhattan_aerosmith.png");
    private static final ResourceLocation BRAZIL_SKIN = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/manhattan_transfer/brazilian_transfer.png");
    private static final ResourceLocation RADIOACTIVE_SKIN = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/manhattan_transfer/radioactive_transfer.png");
    private static final ResourceLocation POLLINATION_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/manhattan_transfer/pollination_transfer.png");


    @Override
    public ResourceLocation getTextureLocation(ManhattanTransferEntity entity) {
        byte BT = entity.getSkin();
        if (BT == ManhattanTransferEntity.ANIME_SKIN) {
            return ANIME_SKIN;
        }
        if (BT == ManhattanTransferEntity.MANGA_SKIN) {
            return MANGA_SKIN;
        }
        if (BT == ManhattanTransferEntity.AERO_TRANSFER_SKIN) {
            return AERO_TRANSFER_SKIN;
        }
        if (BT == ManhattanTransferEntity.BRAZIL_SKIN) {
            return BRAZIL_SKIN;
        }
        if (BT == ManhattanTransferEntity.RADIOACTIVE_SKIN) {
            return RADIOACTIVE_SKIN;
        }
        if (BT == ManhattanTransferEntity.POLLINATION_SKIN) {
            return POLLINATION_SKIN;
        }
        return ANIME_SKIN;
    }

    @Override
    public void render(ManhattanTransferEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        matrixStack.scale(0.70f, 0.70f, 0.70f);

        Player pl = Minecraft.getInstance().player;
        LivingEntity user = mobEntity.getUser();
        if (user != null) {
            StandUser standUser = ((StandUser)mobEntity.getUser());
            StandPowers powers = standUser.roundabout$getStandPowers();

            if (powers.isPiloting()){
                if (powers.getPilotingStand() != null && powers.getPilotingStand().is(mobEntity)){
                    boolean fp = Minecraft.getInstance().options.getCameraType().isFirstPerson();
                    if (fp && !mobEntity.getDisplay() && pl != null && user.is(pl)){

                        this.model.root().visible = false;
                        if (mobEntity instanceof PollinationTransferEntity) {
                            this.model.root().visible = false;
                        } else {

                        }
                    }
                }
            }
        }

        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
        this.model.root().visible = true;
        if (mobEntity instanceof PollinationTransferEntity) {
            this.model.root().visible = true;
        } else {
            this.model.root().visible = true;
        }
    }

    @Nullable
    @Override
    protected RenderType getRenderType(ManhattanTransferEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderType(entity, showBody, true, showOutline);
    }
}
