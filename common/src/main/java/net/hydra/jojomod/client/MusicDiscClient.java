package net.hydra.jojomod.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.EntityBoundSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

public final class MusicDiscClient {
    private static final Map<Entity, WeakReference<SoundInstance>> PLAYING = new WeakHashMap<>();

    private MusicDiscClient() {
    }

    public static void play(Entity entity, String soundId) {
        stop(entity);
        if (soundId.isEmpty()) return;
        ResourceLocation location = ResourceLocation.tryParse(soundId);
        if (location == null) return;
        SoundEvent sound = BuiltInRegistries.SOUND_EVENT.get(location);
        if (sound == null) return;
        SoundInstance soundInstance = new EntityBoundSoundInstance(sound, SoundSource.RECORDS,
                4.0F, 1.0F, entity, entity.level().getRandom().nextLong());
        PLAYING.put(entity, new WeakReference<>(soundInstance));
        Minecraft.getInstance().getSoundManager().play(soundInstance);
    }

    private static void stop(Entity entity) {
        WeakReference<SoundInstance> oldSound = PLAYING.remove(entity);
        SoundInstance sound = oldSound == null ? null : oldSound.get();
        if (sound != null) Minecraft.getInstance().getSoundManager().stop(sound);
    }
}
