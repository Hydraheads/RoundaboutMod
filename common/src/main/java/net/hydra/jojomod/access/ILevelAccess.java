package net.hydra.jojomod.access;


import net.hydra.jojomod.entity.projectile.SoftAndWetPlunderBubbleEntity;

public interface ILevelAccess {
    void roundabout$addPlunderBubble(SoftAndWetPlunderBubbleEntity plunder);
    void roundabout$removePlunderBubble(SoftAndWetPlunderBubbleEntity plunder);
    void roundabout$tickPlunderBubbleRemoval();
}
