package net.hydra.jojomod.mixin;

import net.hydra.jojomod.item.ModItems;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SnowGolem.class)
public abstract class ZSnowGolem extends AbstractGolem implements Shearable,
        RangedAttackMob {
    /**You can sheer a snow golem with scissors as well*/
    protected ZSnowGolem(EntityType<? extends AbstractGolem> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Inject(method = "mobInteract", at = @At(value = "HEAD"),cancellable = true)
    public void roundaboutSinteract(Player $$0, InteractionHand $$1, CallbackInfoReturnable<InteractionResult> cir) {

        ItemStack $$2 = $$0.getItemInHand($$1);
        if ($$2.is(ModItems.SCISSORS) && this.readyForShearing()) {
            this.shear(SoundSource.PLAYERS);
            this.gameEvent(GameEvent.SHEAR, $$0);
            if (!this.level().isClientSide) {
                $$2.hurtAndBreak(2, $$0, $$1x -> $$1x.broadcastBreakEvent($$1));
            }

            cir.setReturnValue(InteractionResult.sidedSuccess(this.level().isClientSide));
        }
    }
}
