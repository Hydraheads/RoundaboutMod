package net.hydra.jojomod.mixin.stand_entities;

import net.hydra.jojomod.entity.stand.StandEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(NearestAttackableTargetGoal.class)
public abstract class StandNearestAttackableTargetGoal<T extends LivingEntity> extends TargetGoal {

    /**Stand Entities are not targeted, their users are*/

    @Inject(method = "findTarget", at = @At(value = "TAIL"))
    protected void roundabout$findTarget(CallbackInfo ci) {
        if (this.target instanceof StandEntity SE) {
            if(SE.getUser() != null && !(SE.getUser() instanceof StandEntity)){
                if (SE.getUser() instanceof ServerPlayer PE && !PE.gameMode.isCreative() && !PE.isSpectator()){
                    this.target = PE;
                } else {
                    this.target = null;
                }
            } else if(SE.getUser() != null){
                if (SE.getUser() instanceof ServerPlayer PE && !PE.gameMode.isCreative() && !PE.isSpectator()){
                    this.target = PE;
                } else {
                    this.target = null;
                }
            }
        }
    }

    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */
    public StandNearestAttackableTargetGoal(Mob $$0, boolean $$1) {
        super($$0, $$1);
    }
    @Shadow
    @Nullable
    protected LivingEntity target;

}
