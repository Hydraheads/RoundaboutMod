package net.hydra.jojomod.entity.TickableSoundInstances;

import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.EntityBoundSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;

public class FleshBudSound extends EntityBoundSoundInstance {
    private final Entity hatEntity;

    public FleshBudSound(SoundEvent soundEvent, SoundSource category, float volume, float pitch, Entity entity, long seed) {
        super(soundEvent, category, volume,pitch, entity, seed);
        this.hatEntity = entity;
    }

    @Override
    public void tick() {
        if (hatEntity != null && ((StandUser)hatEntity).rdbt$getFleshBud() == null) {
            stop();
            return;
        }
        super.tick();
    }

    @Override
    public boolean isRelative() {
        return false;
    }

    @Override
    public boolean canPlaySound() {
        return !this.isStopped();
    }
}
