package net.hydra.jojomod.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;

public class DiverDownGroundDiveSound extends AbstractTickableSoundInstance {
    private final Entity standEntity;

    public DiverDownGroundDiveSound(SoundEvent soundEvent, SoundSource category, float volume, float pitch, Entity standEntity) {
        super(soundEvent, category, SoundInstance.createUnseededRandom());
        this.standEntity = standEntity;

        this.looping = true;
        this.delay = 0;
        this.volume = volume;
        this.pitch = pitch;
        this.attenuation = Attenuation.NONE;
        this.relative = true;

        if (standEntity != null) {
            this.x = (float) standEntity.getX();
            this.y = (float) standEntity.getY();
            this.z = (float) standEntity.getZ();
        }
    }

    @Override
    public void tick() {
        // Stop playing if stand is removed, dead, or player stopped diving
        if (standEntity == null || !standEntity.isAlive() || standEntity.isRemoved() || !DiverDownControlsClient.isDiving()) {
            this.stop();
            return;
        }

        // Follow the stand's position
        this.x = (float) standEntity.getX();
        this.y = (float) standEntity.getY();
        this.z = (float) standEntity.getZ();
    }

    @Override
    public boolean canPlaySound() {
        return !this.isStopped();
    }

    public void stopSound() {
        this.stop();
    }
}