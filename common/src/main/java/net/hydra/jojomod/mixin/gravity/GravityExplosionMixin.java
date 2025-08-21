package net.hydra.jojomod.mixin.gravity;

import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Mixin(value = Explosion.class, priority = 1001)
public abstract class GravityExplosionMixin {

    @Shadow @Final private Map<Player, Vec3> hitPlayers;

    @Shadow public abstract DamageSource getDamageSource();

    @Shadow
    public static float getSeenPercent(Vec3 vec3, Entity entity) {
        return 0;
    }

    @Shadow @Final private double x;

    @Shadow @Final private double z;

    @Shadow @Final private double y;

    @Shadow @Final private Level level;

    @Shadow @Final @Nullable private Entity source;

    @Shadow @Final private float radius;

    @Shadow @Final private ExplosionDamageCalculator damageCalculator;

    @Shadow @Final private ObjectArrayList<BlockPos> toBlow;

    @Unique
    public Explosion rdbt$this(){
        return ((Explosion) (Object) this);
    }

    @Inject(
            method = "explode",
            at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/ObjectArrayList;addAll(Ljava/util/Collection;)Z",
            shift = At.Shift.AFTER),
            cancellable = true
    )
    private void rdbt$explode(CallbackInfo ci) {
        ci.cancel();

        float $$18 = this.radius * 2.0F;
        int $$19 = Mth.floor(this.x - (double)$$18 - 1.0);
        int $$20 = Mth.floor(this.x + (double)$$18 + 1.0);
        int $$21 = Mth.floor(this.y - (double)$$18 - 1.0);
        int $$22 = Mth.floor(this.y + (double)$$18 + 1.0);
        int $$23 = Mth.floor(this.z - (double)$$18 - 1.0);
        int $$24 = Mth.floor(this.z + (double)$$18 + 1.0);
        List<Entity> $$25 = this.level.getEntities(this.source, new AABB((double)$$19, (double)$$21, (double)$$23, (double)$$20, (double)$$22, (double)$$24));
        Vec3 $$26 = new Vec3(this.x, this.y, this.z);

        for (int $$27 = 0; $$27 < $$25.size(); $$27++) {
            Entity $$28 = $$25.get($$27);
            if (!$$28.ignoreExplosion()) {
                Direction gravityDirection = GravityAPI.getGravityDirection($$28);

                double $$29 = Math.sqrt($$28.distanceToSqr($$26)) / (double)$$18;
                if ($$29 <= 1.0) {
                    double $$30 = $$28.getEyePosition().x - this.x;
                    double $$31 = ($$28 instanceof PrimedTnt ? $$28.getY() : $$28.getEyePosition().y) - this.y;
                    double $$32 = $$28.getEyePosition().z - this.z;
                    double $$33 = Math.sqrt($$30 * $$30 + $$31 * $$31 + $$32 * $$32);
                    if ($$33 != 0.0) {
                        $$30 /= $$33;
                        $$31 /= $$33;
                        $$32 /= $$33;
                        double $$34 = (double)getSeenPercent($$26, $$28);
                        double $$35 = (1.0 - $$29) * $$34;
                        $$28.hurt(this.getDamageSource(), (float)((int)(($$35 * $$35 + $$35) / 2.0 * 7.0 * (double)$$18 + 1.0)));
                        double $$37;
                        if ($$28 instanceof LivingEntity $$36) {
                            $$37 = ProtectionEnchantment.getExplosionKnockbackAfterDampener($$36, $$35);
                        } else {
                            $$37 = $$35;
                        }

                        $$30 *= $$37;
                        $$31 *= $$37;
                        $$32 *= $$37;
                        Vec3 $$39 = new Vec3($$30, $$31, $$32);
                        if (gravityDirection == Direction.DOWN){
                            $$28.setDeltaMovement($$28.getDeltaMovement().add($$39));
                        } else {
                            $$28.setDeltaMovement(RotationUtil.vecWorldToPlayer(RotationUtil.vecPlayerToWorld($$28.getDeltaMovement(), gravityDirection).add($$39), gravityDirection));
                        }

                        if ($$28 instanceof Player) {
                            Player $$40 = (Player)$$28;
                            if (!$$40.isSpectator() && (!$$40.isCreative() || !$$40.getAbilities().flying)) {
                                this.hitPlayers.put($$40, $$39);
                            }
                        }
                    }
                }
            }
        }

    }

}
