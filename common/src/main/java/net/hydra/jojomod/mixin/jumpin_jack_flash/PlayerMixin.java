package net.hydra.jojomod.mixin.jumpin_jack_flash;

import net.hydra.jojomod.event.powers.jumpingJackFlash.JJFLauncherAccess;
import net.hydra.jojomod.event.powers.jumpingJackFlash.JJFLauncherState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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

    @Unique
    private ItemStack jjf$loadedAmmoIcon = ItemStack.EMPTY;

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

    @Override
    public ItemStack jjf$getLoadedAmmoIcon() {
        return jjf$loadedAmmoIcon.copy();
    }

    @Override
    public void jjf$setLoadedAmmoIcon(ItemStack item) {
        if (item == null || item.isEmpty()) {
            jjf$loadedAmmoIcon = ItemStack.EMPTY;
        } else {
            jjf$loadedAmmoIcon = item.copy();
            jjf$loadedAmmoIcon.setCount(1);
        }
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
        jjf$setLoadedAmmoIcon(ItemStack.EMPTY);

        if (!tag.contains("jjf", Tag.TAG_COMPOUND)) {
            return;
        }

        CompoundTag jjfTag = tag.getCompound("jjf");
        if (!jjfTag.contains("Launcher", Tag.TAG_COMPOUND)) {
            return;
        }

        jjf$launcherState.load(jjfTag.getCompound("Launcher"));
        jjf$setLoadedAmmoCount(jjf$launcherState.getLoadedCount());
        jjf$setLoadedAmmoIcon(jjf$launcherState.getLoadedItem());
    }
}
