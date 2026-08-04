package net.hydra.jojomod.client.models.substand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.access.ILivingEntityAccess;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.KingCrimsonCloneEntity;
import net.hydra.jojomod.entity.KingCrimsonProjectionEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3f;

public class HologramCloneRenderer<T extends KingCrimsonProjectionEntity> extends CloneRenderer<T> {

    public HologramCloneRenderer(EntityRendererProvider.Context $$0) {
        super($$0);
    }




    @Override
    public void render(T entity, float entityYaw, float partialTick, PoseStack matrices, MultiBufferSource bufferSource, int packedLight) {
        entity.player = ClientUtil.getPlayer();
        if (entity.getPlayer() != null) {
            IPlayerEntity pl = ((IPlayerEntity) entity.getPlayer());
            ItemStack visage = pl.roundabout$getMaskSlot();
            entity.setVisage(visage);
        }
        if (entity.fadeInTick < entity.maxFadeInTick){
            entity.hurtTime = 2;
        }
        super.render(entity,entityYaw,partialTick,matrices,bufferSource,packedLight);

    }
}

