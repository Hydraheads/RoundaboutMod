package net.hydra.jojomod.access;


import net.hydra.jojomod.entity.projectile.SoftAndWetPlunderBubbleEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;

public interface ILevelAccess {
    void roundabout$addPlunderBubble(SoftAndWetPlunderBubbleEntity plunder);
    void roundabout$addPlunderBubbleEntity(SoftAndWetPlunderBubbleEntity plunder);
    void roundabout$removePlunderBubble(SoftAndWetPlunderBubbleEntity plunder);
    void roundabout$tickPlunderBubbleRemoval();
    boolean roundabout$isFrictionPlundered(BlockPos blockPos);
    boolean roundabout$isSoundPlundered(BlockPos blockPos);
    SoftAndWetPlunderBubbleEntity roundabout$getSoundPlunderedBubble(BlockPos blockPos);
    SoftAndWetPlunderBubbleEntity roundabout$getSoundPlunderedBubbleEntity(Entity entity);
    boolean roundabout$isSoundPlunderedEntity(Entity entity);
}
