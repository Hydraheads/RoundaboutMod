package net.hydra.jojomod.access;


import net.hydra.jojomod.entity.projectile.SoftAndWetPlunderBubbleEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public interface ILevelAccess {
    void sba$addPlunderBubble(SoftAndWetPlunderBubbleEntity plunder);
    void sba$addPlunderBubbleEntity(SoftAndWetPlunderBubbleEntity plunder);
    void sba$removePlunderBubble(SoftAndWetPlunderBubbleEntity plunder);
    void sba$tickPlunderBubbleRemoval();
    boolean sba$isFrictionPlundered(BlockPos blockPos);
    boolean sba$isFrictionPlunderedEntity(Entity entity);
    boolean sba$isFrictionPlunderedEntity2(Entity entity);
    boolean sba$isSoundPlundered(BlockPos blockPos);
    SoftAndWetPlunderBubbleEntity sba$getSoundPlunderedBubble(BlockPos blockPos);
    SoftAndWetPlunderBubbleEntity sba$getSoundPlunderedBubbleEntity(Entity entity);
    boolean sba$isSoundPlunderedEntity(Entity entity);
    void sba$raptureBubbles(BlockPos pos);
    void rdbt$skipTime(LivingEntity self, int ticks, int range);
}
