package net.hydra.jojomod.client.models.substand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.KingCrimsonModel;
import net.hydra.jojomod.client.models.stand.renderers.StandRenderer;
import net.hydra.jojomod.client.models.substand.SheerHeartAttackModel;
import net.hydra.jojomod.entity.stand.KillerQueenEntity;
import net.hydra.jojomod.entity.stand.KingCrimsonEntity;
import net.hydra.jojomod.entity.substand.SheerHeartAttackEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersKillerQueen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;



public class SheerHeartAttackRenderer extends SheerHeartAttackBaseRenderer<SheerHeartAttackEntity> {

    public SheerHeartAttackRenderer(EntityRendererProvider.Context context) {
        super(context, new SheerHeartAttackModel(context.bakeLayer(ModEntityRendererClient.SHEER_HEART_ATTACK_LAYER)));
    }
}