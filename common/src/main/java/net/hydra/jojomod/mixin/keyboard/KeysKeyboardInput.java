package net.hydra.jojomod.mixin.keyboard;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.KeyboardPilotInput;
import net.hydra.jojomod.entity.corpses.FallenPhantom;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.Powers20thCenturyBoy;
import net.hydra.jojomod.stand.powers.PowersCream;
import net.hydra.jojomod.stand.powers.PowersTusk;
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
public abstract class KeysKeyboardInput extends Input {

    /**Capture inputs for moving around entities that are not players.*/

    @Inject(method = "tick(ZF)V", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$tick(boolean $$0, float $$1, CallbackInfo ci) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {

            StandUser user = ((StandUser) player);

            boolean noKeys = user.roundabout$isPossessed()
                    || (user.roundabout$getStandPowers() instanceof Powers20thCenturyBoy CB && CB.invincibleState)
                    || (user.roundabout$getStandPowers() instanceof PowersCream PC && PC.getTransformTimer() > 0)
                    || (user.roundabout$getStandPowers() instanceof PowersTusk PT && PT.getActivePower() == PowersTusk.FLATTEN);

            if (user.roundabout$getStandPowers().isPiloting()){
                if (roundabout$keyPilot == null){
                    roundabout$keyPilot = new KeyboardPilotInput(this.options);
                }

                Entity ent = user.roundabout$getStandPowers().getPilotingStand();
                if (ent != null){
                    roundabout$keyPilot.tick($$0, $$1);
                    noKeys = true;
                }
            } else if (player.isPassenger() && player.getVehicle() instanceof FallenPhantom phantom) {
                if (roundabout$keyRider == null){
                    roundabout$keyRider = new KeyboardPilotInput(this.options);
                }
                roundabout$keyRider.tick($$0,$$1);

            }

            if (noKeys) {
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

    @Unique
    private KeyboardPilotInput roundabout$keyPilot;

    @Unique
    private KeyboardPilotInput roundabout$keyRider;



    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

    @Shadow @Final private Options options;

}
