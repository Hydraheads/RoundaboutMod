package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersKhnum;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ReputationEventHandler;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerDataHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Villager.class)
public abstract class DisguiseVillager extends AbstractVillager implements ReputationEventHandler, VillagerDataHolder {

    public DisguiseVillager(EntityType<? extends AbstractVillager> type, Level level) {
        super(type, level);
    }

    /** Reputation is temporarily neutral while disguised; the underlying gossip is retained. */
    @Inject(method = "getPlayerReputation", at = @At("HEAD"), cancellable = true)
    private void roundabout$getDisguisedPlayerReputation(Player player, CallbackInfoReturnable<Integer> cir) {
        if (player instanceof StandUser su && su.roundabout$isDisguised()
                && su.roundabout$getStandPowers() instanceof PowersKhnum) {
            cir.setReturnValue(0);
        }
    }
}
