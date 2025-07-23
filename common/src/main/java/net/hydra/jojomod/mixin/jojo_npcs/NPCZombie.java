package net.hydra.jojomod.mixin.jojo_npcs;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.npcs.Aesthetician;
import net.hydra.jojomod.entity.npcs.ZombieAesthetician;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Zombie.class)
public abstract class NPCZombie extends Monster {

    /**Zombie Aestheticians and Zombie versions of other JoJo NPCs need to be turned into when their base NPCs die*/

    @Inject(method = "killedEntity", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$killedEntity(ServerLevel serverLevel, LivingEntity livingEntity, CallbackInfoReturnable<Boolean> cir) {
        boolean bl = super.killedEntity(serverLevel, livingEntity);
        if ((serverLevel.getDifficulty() == Difficulty.NORMAL || serverLevel.getDifficulty() == Difficulty.HARD) && livingEntity instanceof Aesthetician aesthetician) {
            if (serverLevel.getDifficulty() != Difficulty.HARD && this.random.nextBoolean()) {
                cir.setReturnValue(bl);
                return;
            }

            ZombieAesthetician zombieAesthetician = (ZombieAesthetician)aesthetician.convertTo(ModEntities.ZOMBIE_AESTHETICIAN, false);
            if (zombieAesthetician != null) {
                zombieAesthetician.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(zombieAesthetician.blockPosition()), MobSpawnType.CONVERSION, new Zombie.ZombieGroupData(false, true), (CompoundTag)null);
                zombieAesthetician.setSkinNumber(aesthetician.getSkinNumber());
                if (!this.isSilent()) {
                    serverLevel.levelEvent((Player)null, 1026, this.blockPosition(), 0);
                }

                bl = false;
                cir.setReturnValue(false);
            }
        }
    }

    /**JoJo NPCs need to be attacked by zombies like villagers are*/
    @Inject(method = "addBehaviourGoals", at = @At(value = "TAIL"))
    protected void roundabout$addBehaviourGoals(CallbackInfo ci) {
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, JojoNPC.class, false));
    }



    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

    protected NPCZombie(EntityType<? extends Monster> $$0, Level $$1) {
        super($$0, $$1);
    }

}
