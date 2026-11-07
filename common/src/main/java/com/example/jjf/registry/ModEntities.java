package com.example.jjf.registry;

import com.example.jjf.JJFMod;
import com.example.jjf.entity.JJFGunpowderCloudEntity;
import com.example.jjf.entity.projectile.JJFProjectileEntity;
import com.example.jjf.entity.projectile.JJFSpitEntity;
import com.example.jjf.entity.stand.JumpinJackFlashEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public final class ModEntities {

    public static EntityType<JumpinJackFlashEntity>
            JUMPIN_JACK_FLASH;

    public static EntityType<JJFProjectileEntity>
            JJF_PROJECTILE;

    public static EntityType<JJFGunpowderCloudEntity>
            JJF_GUNPOWDER_CLOUD;

    public static EntityType<JJFSpitEntity>
            JJF_SPIT;

    private ModEntities() {
    }

    public static EntityType<JumpinJackFlashEntity>
    createJumpinJackFlash() {
        return EntityType.Builder
                .of(
                        JumpinJackFlashEntity::new,
                        MobCategory.MISC
                )
                .sized(
                        0.75F,
                        2.05F
                )
                .clientTrackingRange(14)
                .build(
                        JJFMod.id(
                                "jumpin_jack_flash"
                        ).toString()
                );
    }

    public static EntityType<JJFProjectileEntity>
    createProjectile() {
        return EntityType.Builder
                .<JJFProjectileEntity>of(
                        JJFProjectileEntity::new,
                        MobCategory.MISC
                )
                .sized(
                        0.25F,
                        0.25F
                )
                .clientTrackingRange(8)
                .build(
                        JJFMod.id(
                                "jjf_projectile"
                        ).toString()
                );
    }

    public static EntityType<JJFGunpowderCloudEntity>
    createGunpowderCloud() {
        return EntityType.Builder
                .<JJFGunpowderCloudEntity>of(
                        JJFGunpowderCloudEntity::new,
                        MobCategory.MISC
                )
                .sized(
                        3.5F,
                        2.0F
                )
                .clientTrackingRange(8)
                .updateInterval(2)
                .build(
                        JJFMod.id(
                                "jjf_gunpowder_cloud"
                        ).toString()
                );
    }

    public static EntityType<JJFSpitEntity>
    createSpit() {
        return EntityType.Builder
                .<JJFSpitEntity>of(
                        JJFSpitEntity::new,
                        MobCategory.MISC
                )
                .sized(
                        0.18F,
                        0.18F
                )
                .clientTrackingRange(8)
                .updateInterval(1)
                .build(
                        JJFMod.id(
                                "jjf_spit"
                        ).toString()
                );
    }
}
