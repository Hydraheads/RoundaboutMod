package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IMob;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.item.StandDiscItem;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Animal.class)
public abstract class ZAnimal extends AgeableMob {

    protected ZAnimal(EntityType<? extends AgeableMob> $$0, Level $$1) {
        super($$0, $$1);
    }
    @Shadow
    public void finalizeSpawnChildFromBreeding(ServerLevel $$0, Animal $$1, @Nullable AgeableMob $$2){
    }

    /**Stand User reproduction*/
    @Inject(method = "spawnChildFromBreeding", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$spawnChildFromBreeding(ServerLevel $$0, Animal $$1, CallbackInfo ci) {
        if ($$1 != null){
            boolean partnerUser = (!((StandUser)$$1).roundabout$getStandDisc().isEmpty());
            boolean partnerWorthy = ((IMob)$$1).roundabout$isWorthy();
            boolean thisUser = (!((StandUser)this).roundabout$getStandDisc().isEmpty());
            boolean thisWorthy = ((IMob)this).roundabout$isWorthy();
            if (partnerWorthy || partnerUser || thisUser || thisWorthy){
                ci.cancel();
                AgeableMob $$2 = this.getBreedOffspring($$0, $$1);
                if ($$2 != null) {
                    $$2.setBaby(true);

                    double UserOdds = MainUtil.getStandUserOdds($$2);
                    if (partnerUser){
                        UserOdds+= MainUtil.getStandUserBreedBonus($$2);
                    } if (thisUser){
                        UserOdds+= MainUtil.getStandUserBreedBonus($$2);
                    }

                    RandomSource $$5 = $$0.getRandom();
                    if ($$5.nextFloat() < UserOdds && !ModItems.STAND_ARROW_POOL_FOR_MOBS.isEmpty()) {
                        ((IMob)$$2).roundabout$setWorthy(true);
                        ((IMob)$$2).roundabout$setIsNaturalStandUser(true);
                        int index = (int) (Math.floor(Math.random()* ModItems.STAND_ARROW_POOL_FOR_MOBS.size()));
                        ItemStack stack = ModItems.STAND_ARROW_POOL_FOR_MOBS.get(index).getDefaultInstance();
                        if (!stack.isEmpty() && stack.getItem() instanceof StandDiscItem SD){
                            ((StandUser)$$2).roundabout$setStandDisc(stack);
                            SD.generateStandPowers($$2);
                            ((StandUser)$$2).roundabout$getStandPowers().rollSkin();
                        }
                    } else {
                        double WorthyOdds = MainUtil.getWorthyOdds($$2);
                        if (partnerWorthy){
                            WorthyOdds+= MainUtil.getWorthyBreedBonus($$2);
                        } if (partnerWorthy){
                            WorthyOdds+= MainUtil.getWorthyBreedBonus($$2);
                        }
                        if ($$5.nextFloat() < WorthyOdds) {
                            ((IMob)$$2).roundabout$setWorthy(true);
                        }
                    }

                    $$2.moveTo(this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F);
                    this.finalizeSpawnChildFromBreeding($$0, $$1, $$2);
                    $$0.addFreshEntityWithPassengers($$2);
                }
            }
        }
    }

}
