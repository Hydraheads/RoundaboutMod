package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IMob;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.item.StandDiscItem;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.VillagerMakeLove;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(VillagerMakeLove.class)
public class ZVillagerMakeLove {
    @Inject(method = "breed", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$spawnChildFromBreeding(ServerLevel $$0, Villager $$1, Villager $$2, CallbackInfoReturnable<Optional<Villager>> cir) {
        if ($$1 != null && $$2 != null) {
            boolean partnerUser = (!((StandUser) $$1).roundabout$getStandDisc().isEmpty());
            boolean partnerWorthy = ((IMob) $$1).roundabout$isWorthy();
            boolean thisUser = (!((StandUser) $$2).roundabout$getStandDisc().isEmpty());
            boolean thisWorthy = ((IMob) $$2).roundabout$isWorthy();
            if (partnerWorthy || partnerUser || thisUser || thisWorthy) {
                Villager villager = $$1.getBreedOffspring($$0, $$2);
                if (villager == null) {
                    cir.setReturnValue(Optional.empty());
                } else {
                    $$1.setAge(6000);
                    $$2.setAge(6000);
                    villager.setAge(-24000);
                    villager.moveTo($$1.getX(), $$1.getY(), $$1.getZ(), 0.0F, 0.0F);

                    double UserOdds = MainUtil.getStandUserOdds(villager);
                    if (partnerUser) {
                        UserOdds += MainUtil.getStandUserBreedBonus(villager);
                    }
                    if (thisUser) {
                        UserOdds += MainUtil.getStandUserBreedBonus(villager);
                    }
                    RandomSource $$5 = $$0.getRandom();
                    if ($$5.nextFloat() < UserOdds) {
                        ((IMob) villager).roundabout$setWorthy(true);
                        ((IMob) villager).roundabout$setIsNaturalStandUser(true);
                        int index = (int) (Math.floor(Math.random() * ModItems.STAND_ARROW_POOL.size()));
                        ItemStack stack = ModItems.STAND_ARROW_POOL.get(index).getDefaultInstance();
                        if (!stack.isEmpty() && stack.getItem() instanceof StandDiscItem SD) {
                            ((StandUser) villager).roundabout$setStandDisc(stack);
                            SD.generateStandPowers(villager);
                            ((StandUser)villager).roundabout$getStandPowers().rollSkin();
                        }
                    } else {
                        double WorthyOdds = MainUtil.getWorthyOdds(villager);
                        if (partnerWorthy) {
                            WorthyOdds += MainUtil.getWorthyBreedBonus(villager);
                        }
                        if (partnerWorthy) {
                            WorthyOdds += MainUtil.getWorthyBreedBonus(villager);
                        }
                        if ($$5.nextFloat() < WorthyOdds) {
                            ((IMob) villager).roundabout$setWorthy(true);
                        }
                        ((IMob) villager).roundabout$setIsNaturalStandUser(false);
                        ((StandUser) villager).roundabout$setStandDisc(ItemStack.EMPTY);
                    }


                    $$0.addFreshEntityWithPassengers(villager);
                    $$0.broadcastEntityEvent(villager, (byte)12);
                    cir.setReturnValue(Optional.of(villager));
                }

            }
        }
    }
}
