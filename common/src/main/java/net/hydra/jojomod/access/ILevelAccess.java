package net.hydra.jojomod.access;


import net.hydra.jojomod.entity.projectile.SoftAndWetPlunderBubbleEntity;
import net.minecraft.core.BlockPos;

public interface ILevelAccess {
    void roundabout$addPlunderBubble(SoftAndWetPlunderBubbleEntity plunder);
    void roundabout$removePlunderBubble(SoftAndWetPlunderBubbleEntity plunder);
    void roundabout$tickPlunderBubbleRemoval();
    boolean roundabout$isFrictionPlundered(BlockPos blockPos);
    boolean roundabout$isSoundPlundered(BlockPos blockPos);
}
