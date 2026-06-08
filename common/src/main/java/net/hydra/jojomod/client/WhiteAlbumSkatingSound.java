package net.hydra.jojomod.client;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersWhiteAlbum;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class WhiteAlbumSkatingSound extends AbstractTickableSoundInstance {
    private final Entity user;

    public WhiteAlbumSkatingSound(SoundEvent soundEvent, SoundSource category, float volume, float pitch, Entity entity) {
        super(soundEvent, category, SoundInstance.createUnseededRandom());
        this.user = entity;

        this.looping = true;
        this.delay = 0;
        this.volume = volume;
        this.attenuation = Attenuation.LINEAR;
        this.relative = false;

        this.x = (float) entity.getX();
        this.y = (float) entity.getY();
        this.z = (float) entity.getZ();
    }

    @Override
    public void tick() {
        if (!user.isAlive() || user.isRemoved()) {
            stop();
            return;
        }
        if(user instanceof LivingEntity LE){
            if (!(((StandUser)LE).roundabout$getStandPowers() instanceof PowersWhiteAlbum PWA && PWA.hasSkatesActivated() &&
            user.isSprinting())){
                stop();
                return;
            }
        }

        this.x = (float) user.getX();
        this.y = (float) user.getY();
        this.z = (float) user.getZ();
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
