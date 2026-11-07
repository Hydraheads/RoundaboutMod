package net.hydra.jojomod.mixin.effects.warding;

import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.stand.powers.PowersWhiteAlbum;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class WardingEntityMixin {
    /**Warding cancels being set on fire outside the context of fire or lava*/
    @Inject(method = "setSecondsOnFire(I)V", at = @At(value = "HEAD"),cancellable = true)
    public void rdbt$setSecondsOnFire(int $$0, CallbackInfo ci) {
        if (((net.minecraft.world.entity.Entity)(Object)this) instanceof LivingEntity LE){
            if (((StandUser)LE).roundabout$getStandPowers().surpassesFire()){
                ci.cancel();
                return;
            }

            if (LE.hasEffect(ModEffects.WARDING)){
                if (!(isInLava() || MainUtil.isPlayerInFireBlock(LE))){
                    ci.cancel();
                }
            }
        }
    }

    // there are so many entity mixins, i might as well just put this one here
    @Inject(method = "playStepSound", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$silenceSnakeSkinBootsMob(BlockPos pos, BlockState state, CallbackInfo ci) {
        // mob footstep cancellation
        if ((Object) this instanceof LivingEntity entity) {
            if (entity.getItemBySlot(EquipmentSlot.FEET).is(ModItems.SNAKE_SKIN_BOOTS)) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "spawnSprintParticle", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$silenceSnakeSkinParticles(CallbackInfo ci) {
        if ((Object) this instanceof LivingEntity entity) {
            if (entity.getItemBySlot(EquipmentSlot.FEET).is(ModItems.SNAKE_SKIN_BOOTS)) {
                ci.cancel();
            }
        }
    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */
    @Shadow public abstract boolean isInLava();
}