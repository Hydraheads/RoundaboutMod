package net.hydra.jojomod.access;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.player.Player;

public interface IPlayerModel {
    boolean roundabout$getSlim();
    boolean roundabout$setupFirstPersonAnimations(AbstractClientPlayer $$0, float $$1, float $$2, float $$3, float $$4, float $$5,
                                                  ModelPart one, ModelPart two, MultiBufferSource mb,
                                                  int packedLight, PoseStack ps);
}
