package com.example.jjf.launcher;

import com.example.jjf.entity.projectile.JJFProjectileEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;

public interface JJFLauncherAmmoItem {

    default boolean jjf$onHitEntity(
            JJFProjectileEntity projectile,
            ItemStack stack,
            Entity target
    ) {
        return false;
    }

    default boolean jjf$onHitBlock(
            JJFProjectileEntity projectile,
            ItemStack stack,
            BlockHitResult hit
    ) {
        return false;
    }

    default int jjf$getAmmoCapacity(
            ItemStack stack
    ) {
        return 1;
    }

    default double jjf$getImpactStrength(
            ItemStack stack
    ) {
        return 0.14D;
    }

    default boolean jjf$keepProjectileAfterHit(
            ItemStack stack
    ) {
        return false;
    }
}