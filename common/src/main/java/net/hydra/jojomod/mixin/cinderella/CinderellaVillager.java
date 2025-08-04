package net.hydra.jojomod.mixin.cinderella;

import net.hydra.jojomod.event.ModEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ReputationEventHandler;
import net.minecraft.world.entity.ai.gossip.GossipContainer;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerDataHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value= Villager.class)
public abstract class CinderellaVillager extends AbstractVillager implements ReputationEventHandler, VillagerDataHolder {
    /**Makes lucky lipstick and faceless influence trading*/

    @Inject(method = "getPlayerReputation", at = @At(value = "HEAD"),cancellable = true)
    private void roundabout$getPlayerRep(Player $$0, CallbackInfoReturnable<Integer> cir) {
        if ($$0.hasEffect(ModEffects.FACELESS)) {
            cir.setReturnValue(
                    Math.round (((float)this.gossips.getReputation($$0.getUUID(), ($$0x) -> {
                        return true;
                    })) / 2)
            );
        } else if ($$0.hasEffect(ModEffects.CAPTURING_LOVE)) {
            cir.setReturnValue(
                    (this.gossips.getReputation($$0.getUUID(), ($$0x) -> {
                        return true;
                    })) + 25
            );
        }
    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */


    public CinderellaVillager(EntityType<? extends AbstractVillager> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Shadow @Final private GossipContainer gossips;
}
