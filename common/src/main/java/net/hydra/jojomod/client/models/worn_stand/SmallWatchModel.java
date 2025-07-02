package net.hydra.jojomod.client.models.worn_stand;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.PsuedoHierarchicalModel;
import net.hydra.jojomod.client.models.layers.animations.MandomAnimations;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.stand.powers.PowersMandom;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class SmallWatchModel extends WatchModel {
    public SmallWatchModel() {
        super();
    }



    @Override
    public LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition watch = partdefinition.addOrReplaceChild("watch", CubeListBuilder.create(), PartPose.offset(0.0F, 0.5F, 0.0F));

        PartDefinition scaleDown = watch.addOrReplaceChild("scaleDown", CubeListBuilder.create().texOffs(1, 0).addBox(-4.175F, -0.9F, -3.0F, 4.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 8).addBox(-5.025F, -2.425F, -2.5F, 2.0F, 5.0F, 5.0F, new CubeDeformation(-0.5F)), PartPose.offset(1.5F, 5.5F, 0.0F));

        PartDefinition hand = scaleDown.addOrReplaceChild("hand", CubeListBuilder.create().texOffs(26, 0).addBox(-0.375F, -1.85F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.15F)), PartPose.offset(-4.525F, 0.075F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

}

