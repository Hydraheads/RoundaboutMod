package net.hydra.jojomod.mixin.items;

import net.hydra.jojomod.access.IItemEntityAccess;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raid;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;
import java.util.UUID;

@Mixin(Raid.class)
public class ExecutionerAxeRaid {
    /**Reward a winner of a raid with the executioner axe template*/
    @Unique
    public boolean roundabout$hasRewarded = false;
    @Inject(method = "tick", at = @At(value = "INVOKE",target="Lnet/minecraft/world/entity/LivingEntity;addEffect(Lnet/minecraft/world/effect/MobEffectInstance;)Z"))
    public void roundabout$tickMid(CallbackInfo ci) {
        if (!roundabout$hasRewarded){
            roundabout$hasRewarded = true;
            for(UUID uuid : this.heroesOfTheVillage) {
                Entity entity = this.level.getEntity(uuid);
                if (entity instanceof Player && !entity.isSpectator()) {
                    ItemEntity $$4 = new ItemEntity(level, entity.getX(),
                            entity.getY()+10, entity.getZ(),
                            ModItems.EXECUTION_UPGRADE.getDefaultInstance());
                    $$4.setPickUpDelay(40);
                    ((IItemEntityAccess)$$4).roundabout$setRaidSparkle(true);
                    level.addFreshEntity($$4);
                    ((IItemEntityAccess)$$4).roundabout$setRaidSparkle(true);
                    break;
                }
            }
        }
    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

    @Shadow
    @Final
    private Set<UUID> heroesOfTheVillage;
    @Shadow
    @Final
    private ServerLevel level;
}
