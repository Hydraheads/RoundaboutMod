package net.hydra.jojomod.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.stand.powers.Powers20thCenturyBoy;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;

public class CenturyBoyLayer<T extends LivingEntity, A extends HumanoidModel<T>> extends RenderLayer<T, A> {
    private final EntityRenderDispatcher dispatcher;
    public CenturyBoyLayer(EntityRendererProvider.Context context, LivingEntityRenderer<T, A> livingEntityRenderer) {
        super(livingEntityRenderer);
        this.dispatcher = context.getEntityRenderDispatcher();
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float var5, float var6, float var7, float partialTicks, float var9, float var10) {
        if (ClientUtil.canSeeStands(ClientUtil.getPlayer())){
            if (entity != null){
                if (((IEntityAndData)entity).roundabout$getTrueInvisibility() > - 1 && !ClientUtil.checkIfClientCanSeeInvisAchtung())
                    return;


                StandUser user = ((StandUser) entity);
                boolean hasCB = (user.roundabout$getStandPowers() instanceof Powers20thCenturyBoy);
                boolean standOut = (PowerTypes.hasStandActive(entity) && hasCB);
                if (!entity.isInvisible()){
                    int CBticks = user.roundabout$getCBVanishTicks();

                    if (CBticks > 0 || standOut){
                        byte skin = user.roundabout$getStandSkin();
                        if (user.roundabout$getLastStandSkin() != skin){
                            user.roundabout$setLastStandSkin(skin);
                            CBticks = 0;
                            user.roundabout$setCBVanishTicks(0);
                        }

                        float heyfull = 0;
                        float fixedPartial = partialTicks - (int) partialTicks;

                        if (((TimeStop) entity.level()).CanTimeStopEntity(entity)){
                            fixedPartial = 0;
                        }

                        if (standOut){
                            heyfull = CBticks + fixedPartial;
                            heyfull = Math.min(heyfull / 10, 1f);
                        } else {
                            heyfull = CBticks - fixedPartial;
                            heyfull = Math.max(heyfull / 10, 0)
                        }

                    }
                }
            }
        }
    }
}
