package net.hydra.jojomod.client;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersDiverDown;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;

public class DiverDownZipSound extends AbstractTickableSoundInstance {
    private final Entity user;

    public DiverDownZipSound(SoundEvent soundEvent, SoundSource category, float volume, float pitch, Entity entity) {
        super(soundEvent, category, SoundInstance.createUnseededRandom());
        this.user = entity;

        this.looping = true;
        this.delay = 0;
        this.volume = volume;
        this.pitch = pitch;
        this.attenuation = Attenuation.LINEAR;
        this.relative = false;

        if (entity != null) {
            this.x = (float) entity.getX();
            this.y = (float) entity.getY();
            this.z = (float) entity.getZ();
        }
    }

    @Override
    public void tick() {
        if (user == null || !user.isAlive() || user.isRemoved()) {
            this.stop();
            return;
        }

        if (user instanceof StandUser su && su.roundabout$getStandPowers() instanceof PowersDiverDown pdd) {
            if (!pdd.inZipMode()) {
                this.stop();
                return;
            }
        } else {
            this.stop();
            return;
        }

        // Keep updating position to follow the player
        this.x = (float) user.getX();
        this.y = (float) user.getY();
        this.z = (float) user.getZ();
    }

    public void stopSound() {
        this.stop();
    }

    @Override
    public boolean canPlaySound() {
        return !this.isStopped();
    }

    @Override
    public boolean isRelative() {
        return false;
    }
}