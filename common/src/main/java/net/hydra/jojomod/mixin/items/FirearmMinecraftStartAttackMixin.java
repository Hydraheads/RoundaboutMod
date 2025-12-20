package net.hydra.jojomod.mixin.items;
import net.hydra.jojomod.client.KeyInputRegistry;
import net.hydra.jojomod.item.FirearmItem;
import net.hydra.jojomod.item.SnubnoseRevolverItem;
import net.hydra.jojomod.item.TommyGunItem;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.HitResult;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Minecraft.class)
public abstract class FirearmMinecraftStartAttackMixin {

    @Shadow
    @Final
    public Options options;

    @Shadow
    protected abstract boolean startAttack();

    @Shadow
    @Nullable
    public LocalPlayer player;

    public boolean rdbt$firearmHoldingDownKey = false;

    @Inject(method = "handleKeybinds()V", at = @At("HEAD"))
    private void ifStatementForStartItemForFirearms(CallbackInfo ci) {
        if (player != null) {
            ItemStack itemStack = player.getMainHandItem();
            ItemStack itemStack2 = player.getOffhandItem();

            if ((this.player.isUsingItem() && itemStack.getItem() instanceof FirearmItem) || (this.player.isUsingItem() && itemStack2.getItem() instanceof FirearmItem)) {
                while(this.options.keyAttack.consumeClick()) {
                    this.startAttack();
                    return;
                }
                if (KeyInputRegistry.fire_firearms.isDown() && !rdbt$firearmHoldingDownKey) {
                    rdbt$firearmHoldingDownKey = true;
                    this.startAttack();
                    return;
                } else if (!KeyInputRegistry.fire_firearms.isDown() && rdbt$firearmHoldingDownKey) {
                    rdbt$firearmHoldingDownKey = false;
                }
            }

            if ((itemStack.getItem() instanceof FirearmItem) || (itemStack2.getItem() instanceof FirearmItem) && (!player.getCooldowns().isOnCooldown(itemStack.getItem()))) {
                if (options.keyAttack.isDown() || KeyInputRegistry.fire_firearms.isDown()) {
                    if (!(player.getUseItem().getItem() instanceof TommyGunItem)) return;
                    C2SPacketUtil.gunShot();
                    return;
                }
            }
        }
    }
}
