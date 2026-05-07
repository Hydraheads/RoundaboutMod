package net.hydra.jojomod.entity.TickableSoundInstances;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;

public class RoadRollerExplosionSound extends AbstractTickableSoundInstance {
    private final Entity hatEntity;

    public RoadRollerExplosionSound(SoundEvent soundEvent, SoundSource category, float volume, float pitch, Entity entity) {
        super(soundEvent, category, SoundInstance.createUnseededRandom());
        this.hatEntity = entity;

        this.looping = false;
        this.delay = 0;
        this.attenuation = Attenuation.LINEAR;
        this.relative = false;

        this.x = (float) entity.getX();
        this.y = (float) entity.getY();
        this.z = (float) entity.getZ();
    }

    @Override
    public void tick() {
        if (hatEntity.isRemoved()) {
            stop();
            return;
        }

        this.x = (float) hatEntity.getX();
        this.y = (float) hatEntity.getY();
        this.z = (float) hatEntity.getZ();
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
