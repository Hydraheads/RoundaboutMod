package com.example.jjf.fabric.registry;

import com.example.jjf.JJFMod;
import com.example.jjf.registry.ModEntities;
import com.example.jjf.registry.ModItems;
import com.example.jjf.registry.ModSounds;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public final class FabricJJFRegistries {

    private FabricJJFRegistries() {
    }

    public static void register() {
        ModEntities.JUMPIN_JACK_FLASH =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        JJFMod.id(
                                "jumpin_jack_flash"
                        ),
                        ModEntities.createJumpinJackFlash()
                );

        ModEntities.JJF_PROJECTILE =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        JJFMod.id(
                                "jjf_projectile"
                        ),
                        ModEntities.createProjectile()
                );

        ModEntities.JJF_GUNPOWDER_CLOUD =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        JJFMod.id(
                                "jjf_gunpowder_cloud"
                        ),
                        ModEntities.createGunpowderCloud()
                );

        ModEntities.JJF_SPIT =
                Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        JJFMod.id(
                                "jjf_spit"
                        ),
                        ModEntities.createSpit()
                );

        ModSounds.JUMPIN_JACK_FLASH_SUMMON =
                Registry.register(
                        BuiltInRegistries.SOUND_EVENT,
                        JJFMod.id(
                                "jumpin_jack_flash_summon"
                        ),
                        ModSounds.create(
                                "jumpin_jack_flash_summon"
                        )
                );

        ModSounds.SUCTION_LAUNCH =
                Registry.register(
                        BuiltInRegistries.SOUND_EVENT,
                        JJFMod.id(
                                "suction_launch"
                        ),
                        ModSounds.create(
                                "suction_launch"
                        )
                );

        ModSounds.LAUNCHER_SHOOT =
                Registry.register(
                        BuiltInRegistries.SOUND_EVENT,
                        JJFMod.id(
                                "launcher_shoot"
                        ),
                        ModSounds.create(
                                "launcher_shoot"
                        )
                );

        ModSounds.LAUNCHER_BURST =
                Registry.register(
                        BuiltInRegistries.SOUND_EVENT,
                        JJFMod.id(
                                "launcher_burst"
                        ),
                        ModSounds.create(
                                "launcher_burst"
                        )
                );

        ModItems.JJF_STAND_DISC =
                Registry.register(
                        BuiltInRegistries.ITEM,
                        JJFMod.id(
                                "jumpin_jack_flash_disc"
                        ),
                        ModItems.createStandDisc()
                );

        ModItems.JJF_STAND_DISC_MAX =
                Registry.register(
                        BuiltInRegistries.ITEM,
                        JJFMod.id(
                                "jumpin_jack_flash_disc_max"
                        ),
                        ModItems.createMaxStandDisc(
                                ModItems.JJF_STAND_DISC
                        )
                );
    }
}
