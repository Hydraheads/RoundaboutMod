package net.hydra.jojomod.mixin.time_stop;

import net.hydra.jojomod.access.IEntityAndData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HangingEntity.class)
public abstract class TimeStopHangingEntityMixin extends Entity {

        /**Item frame ordinarily does not call the super tick function so it needs a custom mixin
         * to get the entity mixin tick calls that the mod adds*/

        @Inject(method = "tick", at = @At(value = "TAIL"), cancellable = true)
        protected void roundabout$tick(CallbackInfo ci) {
            ((IEntityAndData)this).roundabout$universalTick();
            ((IEntityAndData)this).roundabout$tickQVec();

        }


        /**Shadows, ignore
         * -------------------------------------------------------------------------------------------------------------
         * */
        public TimeStopHangingEntityMixin(EntityType<?> $$0, Level $$1) {
            super($$0, $$1);
        }
}
