package com.example.jjf.mixin;

import com.example.jjf.launcher.JJFLauncherAccess;
import com.example.jjf.launcher.JJFLauncherState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin implements JJFLauncherAccess {

    @Unique
    private final JJFLauncherState jjf$launcherState = new JJFLauncherState();

    @Unique
    private int jjf$loadedAmmoCount = 0;

    @Override
    public JJFLauncherState jjf$getLauncherState() {
        return jjf$launcherState;
    }

    @Override
    public int jjf$getLoadedAmmoCount() {
        return jjf$loadedAmmoCount;
    }

    @Override
    public void jjf$setLoadedAmmoCount(int count) {
        jjf$loadedAmmoCount = Math.max(0, count);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void jjf$saveLauncherState(CompoundTag tag, CallbackInfo ci) {
        CompoundTag jjfTag = tag.getCompound("jjf");
        jjfTag.put("Launcher", jjf$launcherState.save());
        tag.put("jjf", jjfTag);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void jjf$loadLauncherState(CompoundTag tag, CallbackInfo ci) {
        jjf$launcherState.clearLoadedItem();
        jjf$setLoadedAmmoCount(0);

        if (!tag.contains("jjf", Tag.TAG_COMPOUND)) {
            return;
        }

        CompoundTag jjfTag = tag.getCompound("jjf");
        if (!jjfTag.contains("Launcher", Tag.TAG_COMPOUND)) {
            return;
        }

        jjf$launcherState.load(jjfTag.getCompound("Launcher"));
        jjf$setLoadedAmmoCount(jjf$launcherState.getLoadedCount());
    }
}
