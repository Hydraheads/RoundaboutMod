package net.hydra.jojomod.access;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

public interface IPlayerRenderer {
    Mob roundabout$getShapeShift(Player pe);
    void roundabout$setModelProperties(AbstractClientPlayer $$0);
}
