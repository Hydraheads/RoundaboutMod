package net.hydra.jojomod.mixin;

import com.mojang.authlib.GameProfile;
import net.hydra.jojomod.event.powers.StandUser;
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

@Mixin(value = Villager.class)
public abstract class DisguiseVillager extends AbstractVillager implements ReputationEventHandler, VillagerDataHolder {

    @Shadow @Final private GossipContainer gossips;

    public DisguiseVillager(EntityType<? extends AbstractVillager> type, Level level) {
        super(type, level);
    }

    /**
     * If the player is disguised, the villager uses the disguised profile's UUID for reputation.
     */
    @Inject(method = "getPlayerReputation", at = @At("HEAD"), cancellable = true)
    private void roundabout$getDisguisedPlayerReputation(Player player, CallbackInfoReturnable<Integer> cir) {
        if (player instanceof StandUser su && su.roundabout$isDisguised()) {
            GameProfile disguise = su.roundabout$getDisguiseProfile();
            if (disguise != null && disguise.getId() != null) {
                cir.setReturnValue(this.gossips.getReputation(disguise.getId(), (gossipType) -> true));
            }
        }
    }
}