package net.hydra.jojomod.mixin;

import net.hydra.jojomod.client.KeyboardPilotInput;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public abstract class ZKeyboardInput extends Input {

    @Shadow @Final private Options options;

    @Unique
    private KeyboardPilotInput roundabout$keyPilot;

    @Inject(method = "tick(ZF)V", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$tick(boolean $$0, float $$1, CallbackInfo ci) {
        Player player = Minecraft.getInstance().player;
        if (player != null){
            StandUser user = ((StandUser) player);
            if (user.roundabout$getStandPowers().isPiloting()){
                if (roundabout$keyPilot == null){
                    roundabout$keyPilot = new KeyboardPilotInput(this.options);
                }

                Entity ent = user.roundabout$getStandPowers().getPilotingStand();
                if (ent != null){
                    roundabout$keyPilot.tick($$0, $$1);
                    this.up = false;
                    this.down = false;
                    this.left = false;
                    this.right = false;
                    this.forwardImpulse = 0;
                    this.leftImpulse = 0;
                    this.jumping = false;
                    this.shiftKeyDown = false;
                    ci.cancel();
                }
            }
        }
    }
}
