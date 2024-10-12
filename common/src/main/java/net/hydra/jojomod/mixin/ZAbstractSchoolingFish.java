package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSchoolingFish.class)
public abstract class ZAbstractSchoolingFish extends AbstractFish {

    /**Stand User fish*/
    public ZAbstractSchoolingFish(EntityType<? extends AbstractFish> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Inject(method = "pathToLeader", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$pathToLeader(CallbackInfo ci) {
        if (!((StandUser)this).roundabout$getStandDisc().isEmpty() && this.getTarget() != null){
            ci.cancel();
        }
    }
}
