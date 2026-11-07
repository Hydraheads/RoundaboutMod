package com.example.jjf.forge.registry;

import com.example.jjf.JJFMod;
import com.example.jjf.entity.JJFGunpowderCloudEntity;
import com.example.jjf.entity.projectile.JJFProjectileEntity;
import com.example.jjf.entity.projectile.JJFSpitEntity;
import com.example.jjf.entity.stand.JumpinJackFlashEntity;
import com.example.jjf.registry.ModEntities;
import com.example.jjf.registry.ModItems;
import com.example.jjf.registry.ModSounds;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.hydra.jojomod.item.StandDiscItem;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ForgeJJFRegistries {

    public static final DeferredRegister<Item>
            ITEMS =
            DeferredRegister.create(
                    ForgeRegistries.ITEMS,
                    JJFMod.MODID
            );

    public static final DeferredRegister<EntityType<?>>
            ENTITY_TYPES =
            DeferredRegister.create(
                    ForgeRegistries.ENTITY_TYPES,
                    JJFMod.MODID
            );

    public static final DeferredRegister<SoundEvent>
            SOUND_EVENTS =
            DeferredRegister.create(
                    ForgeRegistries.SOUND_EVENTS,
                    JJFMod.MODID
            );

    public static final RegistryObject<StandDiscItem>
            JJF_STAND_DISC =
            ITEMS.register(
                    "jumpin_jack_flash_disc",
                    () -> {
                        StandDiscItem value =
                                ModItems.createStandDisc();

                        ModItems.JJF_STAND_DISC =
                                value;

                        return value;
                    }
            );

    public static final RegistryObject<MaxStandDiscItem>
            JJF_STAND_DISC_MAX =
            ITEMS.register(
                    "jumpin_jack_flash_disc_max",
                    () -> {
                        MaxStandDiscItem value =
                                ModItems.createMaxStandDisc(
                                        JJF_STAND_DISC.get()
                                );

                        ModItems.JJF_STAND_DISC_MAX =
                                value;

                        return value;
                    }
            );

    public static final RegistryObject<EntityType<JumpinJackFlashEntity>>
            JUMPIN_JACK_FLASH =
            ENTITY_TYPES.register(
                    "jumpin_jack_flash",
                    () -> {
                        EntityType<JumpinJackFlashEntity> value =
                                ModEntities.createJumpinJackFlash();

                        ModEntities.JUMPIN_JACK_FLASH =
                                value;

                        return value;
                    }
            );

    public static final RegistryObject<EntityType<JJFProjectileEntity>>
            JJF_PROJECTILE =
            ENTITY_TYPES.register(
                    "jjf_projectile",
                    () -> {
                        EntityType<JJFProjectileEntity> value =
                                ModEntities.createProjectile();

                        ModEntities.JJF_PROJECTILE =
                                value;

                        return value;
                    }
            );

    public static final RegistryObject<EntityType<JJFGunpowderCloudEntity>>
            JJF_GUNPOWDER_CLOUD =
            ENTITY_TYPES.register(
                    "jjf_gunpowder_cloud",
                    () -> {
                        EntityType<JJFGunpowderCloudEntity> value =
                                ModEntities.createGunpowderCloud();

                        ModEntities.JJF_GUNPOWDER_CLOUD =
                                value;

                        return value;
                    }
            );

    public static final RegistryObject<EntityType<JJFSpitEntity>>
            JJF_SPIT =
            ENTITY_TYPES.register(
                    "jjf_spit",
                    () -> {
                        EntityType<JJFSpitEntity> value =
                                ModEntities.createSpit();

                        ModEntities.JJF_SPIT =
                                value;

                        return value;
                    }
            );

    public static final RegistryObject<SoundEvent>
            JUMPIN_JACK_FLASH_SUMMON =
            SOUND_EVENTS.register(
                    "jumpin_jack_flash_summon",
                    () -> {
                        SoundEvent value =
                                ModSounds.create(
                                        "jumpin_jack_flash_summon"
                                );

                        ModSounds.JUMPIN_JACK_FLASH_SUMMON =
                                value;

                        return value;
                    }
            );

    public static final RegistryObject<SoundEvent>
            SUCTION_LAUNCH =
            SOUND_EVENTS.register(
                    "suction_launch",
                    () -> {
                        SoundEvent value =
                                ModSounds.create(
                                        "suction_launch"
                                );

                        ModSounds.SUCTION_LAUNCH =
                                value;

                        return value;
                    }
            );

    public static final RegistryObject<SoundEvent>
            LAUNCHER_SHOOT =
            SOUND_EVENTS.register(
                    "launcher_shoot",
                    () -> {
                        SoundEvent value =
                                ModSounds.create(
                                        "launcher_shoot"
                                );

                        ModSounds.LAUNCHER_SHOOT =
                                value;

                        return value;
                    }
            );

    public static final RegistryObject<SoundEvent>
            LAUNCHER_BURST =
            SOUND_EVENTS.register(
                    "launcher_burst",
                    () -> {
                        SoundEvent value =
                                ModSounds.create(
                                        "launcher_burst"
                                );

                        ModSounds.LAUNCHER_BURST =
                                value;

                        return value;
                    }
            );

    private ForgeJJFRegistries() {
    }

    public static void register(
            IEventBus bus
    ) {
        ITEMS.register(
                bus
        );

        ENTITY_TYPES.register(
                bus
        );

        SOUND_EVENTS.register(
                bus
        );
    }
}
