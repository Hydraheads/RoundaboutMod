package net.hydra.jojomod.mixin;

import net.hydra.jojomod.client.gui.NoCancelInputScreen;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class ZKeyboardHandler {

    @Shadow
    @Final
    private Minecraft minecraft;
    @Unique
    Screen roundabout$SaveScreen = null;
    @Inject(method = "keyPress(JIIII)V", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$KP1(long $$0, int $$1, int $$2, int $$3, int $$4, CallbackInfo ci) {
        Screen screen = this.minecraft.screen;
        if (screen instanceof NoCancelInputScreen){
            roundabout$SaveScreen = screen;
            this.minecraft.screen = null;
        }
    }
    @Inject(method = "keyPress(JIIII)V", at = @At(value = "TAIL"), cancellable = true)
    protected void roundabout$KP2(long l, int i, int j, int k, int m, CallbackInfo ci) {
        if (roundabout$SaveScreen != null){
            this.minecraft.screen = roundabout$SaveScreen;
            roundabout$SaveScreen = null;
            Screen screen =  this.minecraft.screen;
            if (screen != null) {
                boolean[] bls = new boolean[]{false};
                Screen.wrapScreenError(() -> {
                    if (k != 1 && k != 2) {
                        if (k == 0) {
                            bls[0] = this.minecraft.screen.keyReleased(i, j, m);
                        }
                    } else {
                        this.minecraft.screen.afterKeyboardAction();
                        bls[0] = this.minecraft.screen.keyPressed(i, j, m);
                    }

                }, "keyPressed event handler", screen.getClass().getCanonicalName());
                if (bls[0]) {
                    return;
                }
            }
        }
    }
}
