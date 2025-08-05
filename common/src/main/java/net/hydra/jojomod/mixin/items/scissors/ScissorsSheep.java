package net.hydra.jojomod.mixin.items.scissors;

import net.hydra.jojomod.item.ModItems;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Sheep.class)
public abstract class ScissorsSheep extends Animal implements Shearable {
    /**This mixin allows you to use scissors to shear sheep*/

    @Inject(method = "mobInteract", at = @At(value = "HEAD"),cancellable = true)
    public void roundabout$interact(Player $$0, InteractionHand $$1, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack $$2 = $$0.getItemInHand($$1);
        if ($$2.is(ModItems.SCISSORS)) {
            if (!this.level().isClientSide && this.readyForShearing()) {
                this.shear(SoundSource.PLAYERS);
                this.gameEvent(GameEvent.SHEAR, $$0);
                $$2.hurtAndBreak(2, $$0, $$1x -> $$1x.broadcastBreakEvent($$1));
                cir.setReturnValue(InteractionResult.SUCCESS);
            }
        }
    }

    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */


    protected ScissorsSheep(EntityType<? extends Animal> $$0, Level $$1) {
        super($$0, $$1);
    }



    @Shadow
    public boolean readyForShearing() {
        return false;
    }

    @Shadow
    public void shear(SoundSource $$0) {
    }
}
