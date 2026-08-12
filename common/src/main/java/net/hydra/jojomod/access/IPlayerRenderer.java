package net.hydra.jojomod.access;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

public interface IPlayerRenderer {
    Mob roundabout$getShapeShift(Player pe);
    void roundabout$setModelProperties(AbstractClientPlayer $$0);
    void rdbt$scale(AbstractClientPlayer $$0, PoseStack $$1, float $$2);
}
