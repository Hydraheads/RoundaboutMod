package net.hydra.jojomod.mixin.gravity;

import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.RamTarget;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;

@Mixin(RamTarget.class)
public abstract class GravityRamTargetMixin
{
    @Shadow protected abstract void finishRam(ServerLevel serverLevel, Goat goat);

    @Shadow @Final private Function<Goat, SoundEvent> getHornBreakSound;

    @Shadow @Final private Function<Goat, SoundEvent> getImpactSound;

    @Shadow protected abstract boolean hasRammedHornBreakingBlock(ServerLevel serverLevel, Goat goat);

    @Shadow @Final private ToDoubleFunction<Goat> getKnockbackForce;

    @Shadow private Vec3 ramDirection;

    @Shadow @Final private TargetingConditions ramTargeting;

    @Inject(
            method = "tick(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/animal/goat/Goat;J)V",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true)
    private void rdbt$tick(ServerLevel $$0, Goat $$1, long $$2, CallbackInfo ci) {
        ci.cancel();
        List<LivingEntity> $$3 = $$0.getNearbyEntities(LivingEntity.class, this.ramTargeting, $$1, $$1.getBoundingBox());
        Brain<?> $$4 = $$1.getBrain();
        if (!$$3.isEmpty()) {
            LivingEntity $$5 = $$3.get(0);
            $$5.hurt($$0.damageSources().noAggroMobAttack($$1), (float)$$1.getAttributeValue(Attributes.ATTACK_DAMAGE));
            int $$6 = $$1.hasEffect(MobEffects.MOVEMENT_SPEED) ? $$1.getEffect(MobEffects.MOVEMENT_SPEED).getAmplifier() + 1 : 0;
            int $$7 = $$1.hasEffect(MobEffects.MOVEMENT_SLOWDOWN) ? $$1.getEffect(MobEffects.MOVEMENT_SLOWDOWN).getAmplifier() + 1 : 0;
            float $$8 = 0.25F * (float)($$6 - $$7);
            float $$9 = Mth.clamp($$1.getSpeed() * 1.65F, 0.2F, 3.0F) + $$8;
            float $$10 = $$5.isDamageSourceBlocked($$0.damageSources().mobAttack($$1)) ? 0.5F : 1.0F;
            Direction gravityDirection = GravityAPI.getGravityDirection($$5);
            if (gravityDirection == Direction.DOWN) {
                $$5.knockback((double)($$10 * $$9) * this.getKnockbackForce.applyAsDouble($$1), this.ramDirection.x(), this.ramDirection.z());
            } else {
                Vec3 direction = RotationUtil.vecWorldToPlayer(this.ramDirection, gravityDirection);
                $$5.knockback((double)($$10 * $$9) * this.getKnockbackForce.applyAsDouble($$1), direction.x, direction.z);
            }
            this.finishRam($$0, $$1);
            $$0.playSound(null, $$1, this.getImpactSound.apply($$1), SoundSource.NEUTRAL, 1.0F, 1.0F);
        } else if (this.hasRammedHornBreakingBlock($$0, $$1)) {
            $$0.playSound(null, $$1, this.getImpactSound.apply($$1), SoundSource.NEUTRAL, 1.0F, 1.0F);
            boolean $$11 = $$1.dropHorn();
            if ($$11) {
                $$0.playSound(null, $$1, this.getHornBreakSound.apply($$1), SoundSource.NEUTRAL, 1.0F, 1.0F);
            }

            this.finishRam($$0, $$1);
        } else {
            Optional<WalkTarget> $$12 = $$4.getMemory(MemoryModuleType.WALK_TARGET);
            Optional<Vec3> $$13 = $$4.getMemory(MemoryModuleType.RAM_TARGET);
            boolean $$14 = $$12.isEmpty() || $$13.isEmpty() || $$12.get().getTarget().currentPosition().closerThan($$13.get(), 0.25);
            if ($$14) {
                this.finishRam($$0, $$1);
            }
        }
    }
}
