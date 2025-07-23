package net.hydra.jojomod.client;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.corpses.FallenPhantom;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.mixin.InputEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.Input;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.awt.event.InputEvent;
import java.util.Optional;

public class KeyboardPilotInput extends Input {
    private final Options options;
    public boolean dashKeyDown;
    public KeyboardPilotInput(Options $$0) {
        this.options = $$0;
    }

    private static float calculateImpulse(boolean $$0, boolean $$1) {
        if ($$0 == $$1) {
            return 0.0F;
        } else {
            return $$0 ? 1.0F : -1.0F;
        }
    }
    private boolean roundabout$sameKeyOne(KeyMapping key1){
        boolean skey = (key1.same(this.options.keyLoadHotbarActivator) && this.options.keyLoadHotbarActivator.isDown());
        if (skey){
            key1.setDown(false);
        }
        boolean skey2 = (key1.same(this.options.keySaveHotbarActivator) && this.options.keySaveHotbarActivator.isDown());
        if (skey2){
            key1.setDown(false);
        }

        return (key1.isDown() || skey
                || skey2);
    }
    @Override
    public void tick(boolean $$0, float $$1) {
        this.up = this.options.keyUp.isDown();
        this.down = this.options.keyDown.isDown();
        this.left = this.options.keyLeft.isDown();
        this.right = this.options.keyRight.isDown();
        this.forwardImpulse = calculateImpulse(this.up, this.down);
        this.leftImpulse = calculateImpulse(this.left, this.right);
        this.jumping = this.options.keyJump.isDown();
        this.shiftKeyDown = this.options.keyShift.isDown();
        this.dashKeyDown = roundabout$sameKeyOne(KeyInputRegistry.abilityThreeKey);
        if ($$0) {
            this.leftImpulse *= $$1;
            this.forwardImpulse *= $$1;
        }
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            StandUser user = ((StandUser) player);
            if (user.roundabout$getStandPowers().isPiloting()) {
                LivingEntity ent = user.roundabout$getStandPowers().getPilotingStand();
                if (ent != null) {
                    user.roundabout$getStandPowers().pilotStandControls(this,ent);
                }
            } else if (player.isPassenger() && player.getVehicle() instanceof FallenPhantom phant) {
                if(this.jumping && this.dashKeyDown) {
                    phant.yaccel = 0;
                }
                else if(this.jumping){
                    phant.yaccel = phant.changeHeightBy;
                } else if (this.dashKeyDown) {
                    phant.yaccel = -phant.changeHeightBy;

                } else{
                    phant.yaccel = 0;
                }
                
            }
        }
    }
}
