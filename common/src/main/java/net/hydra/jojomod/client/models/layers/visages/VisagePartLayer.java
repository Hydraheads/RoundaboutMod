package net.hydra.jojomod.client.models.layers.visages;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ModStrayModels;
import net.hydra.jojomod.item.MaskItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class VisagePartLayer<T extends LivingEntity, A extends HumanoidModel<T>> extends RenderLayer<T, A> {

    private static final Map<String, ResourceLocation> ARMOR_LOCATION_CACHE = Maps.newHashMap();
    private final EntityRenderDispatcher dispatcher;
    public VisagePartLayer(EntityRendererProvider.Context context, LivingEntityRenderer<T, A> livingEntityRenderer) {
        super(livingEntityRenderer);
        this.dispatcher = context.getEntityRenderDispatcher();
    }

    float scale = 1;
    private static final ResourceLocation TEXTURE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/soft_and_wet/projectiles/large_bubble.png");
    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks, float var9, float var10) {
        if (entity instanceof Player play){
            IPlayerEntity pl = ((IPlayerEntity) play);
            ItemStack visage = pl.roundabout$getMaskSlot();
            if (visage != null && !visage.isEmpty()) {
                if (visage.getItem() instanceof MaskItem MI) {
                    boolean isHurt = entity.hurtTime > 0;
                    float r = isHurt ? 1.0F : 1.0F;
                    float g = isHurt ? 0.6F : 1.0F;
                    float b = isHurt ? 0.6F : 1.0F;
                    String path = MI.visageData.getSkinPath();
                    if (MI.visageData.rendersBreast()){
                        renderNormalBreast(poseStack,bufferSource, packedLight, entity, xx, yy, zz, partialTicks, path,
                                r,g,b);
                    }
                    if (MI.visageData.rendersPonytail()){
                        renderPonytail(poseStack,bufferSource, packedLight, entity, xx, yy, zz, partialTicks, path,
                                r,g,b);
                    }
                }
            }
        }
    }
    public void renderNormalBreast(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks, String path,
                                   float r, float g, float b) {

        ModStrayModels.ChestPart.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                r, g, b, 1, path);
    }
    public void renderPonytail(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks, String path,
                                   float r, float g, float b) {

        ModStrayModels.PonytailPart.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                r, g, b, 1, path);
    }
}
