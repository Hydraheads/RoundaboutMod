package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.BlackSabbathModel;
import net.hydra.jojomod.client.models.stand.CaliforniaKingBedModel;
import net.hydra.jojomod.client.models.stand.StandModel;
import net.hydra.jojomod.entity.stand.BlackSabbathEntity;
import net.hydra.jojomod.entity.stand.CaliforniaKingBedEntity;
import net.hydra.jojomod.entity.stand.ManhattanTransferEntity;
import net.hydra.jojomod.entity.stand.PollinationTransferEntity;
import net.hydra.jojomod.entity.zombie_minion.AxolotlMinion;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersBlackSabbath;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class BlackSabbathBaseRenderer extends StandRenderer<BlackSabbathEntity> {

    private static final ResourceLocation ANIME = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/black_sabbath/anime.png");
    private static final ResourceLocation MANGA = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/black_sabbath/manga.png");
    private static final ResourceLocation BURNING = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/black_sabbath/burning.png");
    private static final ResourceLocation GIO_GIO = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/black_sabbath/giogio.png");
    private static final ResourceLocation VERDANT = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/black_sabbath/green.png");
    private static final ResourceLocation NIGHT = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/black_sabbath/night.png");
    private static final ResourceLocation DEPARTURE = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/black_sabbath/shadow_departure.png");
    private static final ResourceLocation CHERRY = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/black_sabbath/cherry.png");
    private static final ResourceLocation GRAPE = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/black_sabbath/grape.png");
    private static final ResourceLocation MINT = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/black_sabbath/mint.png");
    private static final ResourceLocation TACO = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/black_sabbath/taco.png");
    private static final ResourceLocation WOOL = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/black_sabbath/woven.png");
    private static final ResourceLocation PHANTOM = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/black_sabbath/phantom.png");
    private static final ResourceLocation SWEET = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/black_sabbath/sweet.png");
    private static final ResourceLocation SACTHOTH = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/black_sabbath/sacthoth_sabbath.png");
    private static final ResourceLocation OCULUS = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/black_sabbath/oculus.png");
    private static final ResourceLocation BEACH = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/black_sabbath/beach_sabbath.png");
    private static final ResourceLocation MAGMA = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/black_sabbath/magma_sabbath.png");
    private static final ResourceLocation DAPPER = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/black_sabbath/dapper_sabbath.png");
    private static final ResourceLocation COPPER = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/black_sabbath/copper_sabbath.png");
    private static final ResourceLocation SANTA = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/black_sabbath/santabbath.png");
    private static final ResourceLocation COWBOY = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/black_sabbath/western.png");
    private static final ResourceLocation CRIMSON = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/black_sabbath/crimsoncrimson.png");
    private static final ResourceLocation FUNGUS = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/black_sabbath/mushroom_skin.png");

    public BlackSabbathBaseRenderer(EntityRendererProvider.Context context, StandModel<BlackSabbathEntity> entityModel, float f) {
        super(context, entityModel,f);
    }

    @Override
    public ResourceLocation getTextureLocation(BlackSabbathEntity entity) {
        byte BT = entity.getSkin();
        if (BT == BlackSabbathEntity.PART_5_ANIME) {
            return ANIME;
        }
        if (BT == BlackSabbathEntity.PART_5_MANGA) {
            return MANGA;
        }
        if (BT == BlackSabbathEntity.BURNING) {
            return BURNING;
        }
        if (BT == BlackSabbathEntity.GIO_GIO) {
            return GIO_GIO;
        }
        if (BT == BlackSabbathEntity.VERDANT) {
            return VERDANT;
        }
        if (BT == BlackSabbathEntity.NIGHT) {
            return NIGHT;
        }
        if (BT == BlackSabbathEntity.DEPARTURE) {
            return DEPARTURE;
        }
        if (BT == BlackSabbathEntity.CHERRY) {
            return CHERRY;
        }
        if (BT == BlackSabbathEntity.GRAPE) {
            return GRAPE;
        }
        if (BT == BlackSabbathEntity.MINT) {
            return MINT;
        }
        if (BT == BlackSabbathEntity.TACO) {
            return TACO;
        }
        if (BT == BlackSabbathEntity.WOOL) {
            return WOOL;
        }
        if (BT == BlackSabbathEntity.DAPPER) {
            return DAPPER;
        }
        if (BT == BlackSabbathEntity.COPPER) {
            return COPPER;
        }
        if (BT == BlackSabbathEntity.PHANTOM) {
            return PHANTOM;
        }
        if(BT == BlackSabbathEntity.SWEET){
            return SWEET;
        }
        if(BT == BlackSabbathEntity.OCULUS){
            return OCULUS;
        }
        if(BT == BlackSabbathEntity.SACTHOTH){
            return SACTHOTH;
        }
        if(BT == BlackSabbathEntity.COWBOY){
            return COWBOY;
        }
        if(BT == BlackSabbathEntity.BEACH){
            return BEACH;
        }
        if(BT == BlackSabbathEntity.MAGMA){
            return MAGMA;
        }
        if (BT == BlackSabbathEntity.SANTA) {
            return SANTA;
        }
        if (BT == BlackSabbathEntity.CRIMSON) {
            return CRIMSON;
        }
        if (BT == BlackSabbathEntity.FUNGUS) {
            return FUNGUS;
        }
        return ANIME;
    }

    @Override
    public void render(BlackSabbathEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        float factor = 1;
        if(mobEntity.isBaby()){
            matrixStack.scale(0.60f * factor, 0.60f * factor, 0.60f * factor);
        } else {
            matrixStack.scale(0.80f * factor, 0.80f * factor, 0.80f * factor);
        }
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Nullable
    @Override
    protected RenderType getRenderType(BlackSabbathEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        ResourceLocation $$4 = this.getTextureLocation(entity);
        return RenderType.entityTranslucent($$4);
    }
    @Override
    public boolean shouldRender(BlackSabbathEntity $$0, Frustum $$1, double $$2, double $$3, double $$4) {
        if ($$0.getCrippled() || $$0.getUnrender() || !$$0.getRiding() && $$0.getUnrender() && $$0.isBlackSabbathUnderLight()){
            return false;
        }
        return super.shouldRender($$0,$$1,$$2,$$3,$$4);
    }
}
