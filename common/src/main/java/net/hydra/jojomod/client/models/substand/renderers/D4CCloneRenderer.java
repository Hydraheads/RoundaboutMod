package net.hydra.jojomod.client.models.substand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.access.ILivingEntityAccess;
import net.hydra.jojomod.entity.D4CCloneEntity;
import net.hydra.jojomod.entity.KingCrimsonCloneEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import org.joml.Vector3f;

public class D4CCloneRenderer<T extends D4CCloneEntity> extends CloneRenderer<T> {

    public D4CCloneRenderer(EntityRendererProvider.Context $$0) {
        super($$0);
    }

}

