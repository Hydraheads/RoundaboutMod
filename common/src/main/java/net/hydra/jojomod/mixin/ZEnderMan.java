package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IEnderMan;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderMan.class)
public abstract class ZEnderMan extends Monster implements NeutralMob, IEnderMan {
    @Shadow protected abstract void registerGoals();

    @Shadow protected abstract boolean teleport();

    protected ZEnderMan(EntityType<? extends Monster> $$0, Level $$1) {
        super($$0, $$1);
    }

    /**Make sight stealing work for plunder*/
    @Unique
    public void roundabout$stripGoals(){
        this.goalSelector.removeAllGoals($$0 -> {return true;});
        this.targetSelector.removeAllGoals($$0 -> {return true;});
        registerGoals();
    }
    @Unique
    public void roundabout$teleport(){
        this.teleport();
    }

    @Inject(method = "isLookingAtMe(Lnet/minecraft/world/entity/player/Player;)Z", at = @At(value = "HEAD"), cancellable = true)
    public void roundabout$isLookingAtMe(Player $$0, CallbackInfoReturnable<Boolean> cir) {
        if (((StandUser)this).roundabout$getEyeSightTaken() != null && this.getLastHurtByMob() == null){
            cir.setReturnValue(false);
        }
    }
}
