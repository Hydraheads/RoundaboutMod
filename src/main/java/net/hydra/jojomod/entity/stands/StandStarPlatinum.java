package net.hydra.jojomod.entity.stands;

import net.hydra.jojomod.entity.StandEntity;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;

public class StandStarPlatinum extends StandEntity {

    public StandStarPlatinum(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected SoundEvent getSummonSound() {
        return ModSounds.STAR_SUMMON_SOUND_EVENT;
    }
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }
}
