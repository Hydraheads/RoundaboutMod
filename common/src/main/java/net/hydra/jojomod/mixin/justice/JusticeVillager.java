package net.hydra.jojomod.mixin.justice;

import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.event.index.ShapeShifts;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ReputationEventHandler;
import net.minecraft.world.entity.ai.village.ReputationEventType;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerDataHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value= Villager.class, priority = 100)
public abstract class JusticeVillager extends AbstractVillager implements ReputationEventHandler, VillagerDataHolder {

    /**Villagers trade well with villager morphs*/
    @Inject(method = "getPlayerReputation", at = @At(value = "HEAD"),cancellable = true)
    private void roundabout$getPlayerRep(Player $$0, CallbackInfoReturnable<Integer> cir) {
        IPlayerEntity ple = ((IPlayerEntity) $$0);
        byte shape = ple.roundabout$getShapeShift();
        ShapeShifts shift = ShapeShifts.getShiftFromByte(shape);
        if (shift != ShapeShifts.PLAYER) {
            if (ShapeShifts.isVillager(shift)) {
                cir.setReturnValue(24);
                return;
            }
        }
    }

    /**Reputation of the player will NOT change at all if they are shapeshifted in general*/
    @Inject(method = "onReputationEventFrom", at = @At(value = "HEAD"),cancellable = true)
    private void roundabout$onReputationEventFrom(ReputationEventType $$0, Entity $$1, CallbackInfo ci) {
        if ($$1 instanceof Player PL) {
            IPlayerEntity ple = ((IPlayerEntity) PL);
            byte shape = ple.roundabout$getShapeShift();
            ShapeShifts shift = ShapeShifts.getShiftFromByte(shape);
            if (shift != ShapeShifts.PLAYER) {
                ci.cancel();
            }
        }
    }

    /**Villagers do not trade with zombie or skeleton morphs in general*/
    @Inject(method = "mobInteract(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;", at = @At(value = "HEAD"),cancellable = true)
    private void roundabout$mobInteract(Player $$0, InteractionHand $$1, CallbackInfoReturnable<InteractionResult> cir) {

        IPlayerEntity ple = ((IPlayerEntity) $$0);
        byte shape = ple.roundabout$getShapeShift();
        ShapeShifts shift = ShapeShifts.getShiftFromByte(shape);
        if (shift != ShapeShifts.PLAYER) {
            if (ShapeShifts.isZombie(shift) || ShapeShifts.isSkeleton(shift)) {
                cir.setReturnValue(InteractionResult.sidedSuccess(this.level().isClientSide));
            }
        }
    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

    public JusticeVillager(EntityType<? extends AbstractVillager> $$0, Level $$1) {
        super($$0, $$1);
    }
}
