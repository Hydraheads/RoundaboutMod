package net.hydra.jojomod.event;

import net.hydra.jojomod.access.*;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.fates.powers.VampiricFate;
import net.hydra.jojomod.stand.powers.PowersWalkingHeart;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class DietSavedSecond {

    public Collection<MobEffectInstance> activeEffects =new ArrayList<>();
    public float health;
    public int onFireTicks;
    public int onStandFireTicks;
    public byte onStandFireType;
    public int gasolineTicks;
    public int heatLevel;
    public int foodLevel;
    public float saturationLevel;
    public float exhaustionLevel;


    public DietSavedSecond(Collection<MobEffectInstance> activeEffects, float health, int onFireTicks, int onStandFireTicks,
                             byte onStandFireType,
                             int gasolineTicks,int heatLevel,
                                int foodLevel, float saturationLevel, float exhaustionLevel){

        List<MobEffectInstance> effects = new ArrayList<>(activeEffects.stream().toList());
        if (!effects.isEmpty()) {
            for (MobEffectInstance value : effects) {
                if (!value.isInfiniteDuration()) {
                    this.activeEffects.add(new MobEffectInstance(value));
                }
            }
        }
        this.health = health;
        this.onFireTicks = onFireTicks;
        this.onStandFireTicks = onStandFireTicks;
        this.onStandFireType = onStandFireType;
        this.gasolineTicks = gasolineTicks;
        this.heatLevel = heatLevel;
        this.foodLevel = foodLevel;
        this.saturationLevel = saturationLevel;
        this.exhaustionLevel = exhaustionLevel;
    }
    public void loadTime(Entity ent){
        if (ent instanceof LivingEntity LE && LE.isAlive()) {
            List<MobEffectInstance> effects = new ArrayList<>(LE.getActiveEffects().stream().toList());
            if (!effects.isEmpty()) {
                for (MobEffectInstance value : effects) {
                    if (!value.isInfiniteDuration()) {
                        LE.removeEffect(value.getEffect());
                    }
                }
            }

            List<MobEffectInstance> effects2 = new ArrayList<>(this.activeEffects.stream().toList());
            if (!effects2.isEmpty()) {
                for (MobEffectInstance value : effects2) {
                    if (!value.isInfiniteDuration()) {
                        LE.addEffect(value);
                    }
                }
            }

            LE.setHealth(this.health);

            LE.setRemainingFireTicks(onFireTicks);
            StandUser user = ((StandUser) LE);
            user.roundabout$setRemainingStandFireTicks(onStandFireTicks);
            user.roundabout$setOnStandFire(onStandFireType);
            user.roundabout$setGasolineTime(gasolineTicks);
            user.roundabout$setHeat(heatLevel);
            if (ent instanceof Player PE) {
                IFoodData ifp = ((IFoodData) PE.getFoodData());
                ifp.roundabout$setExhaustionLevel(this.exhaustionLevel);
                ifp.roundabout$setSaturationLevel(this.saturationLevel);
                ifp.roundabout$setFoodLevel(this.foodLevel);
            }
        }
    }


    public static DietSavedSecond saveEntitySecond(Entity ent) {
        if (ent instanceof Player PL) {
            return new DietSavedSecond(
                    PL.getActiveEffects(),
                    PL.getHealth(),
                    PL.getRemainingFireTicks(),
                    ((StandUser)PL).roundabout$getRemainingFireTicks(),
                    ((StandUser)PL).roundabout$getOnStandFire(),
                    ((StandUser)PL).roundabout$getGasolineTime(),
                    ((StandUser)PL).roundabout$getHeat(),
                    PL.getFoodData().getFoodLevel(),
                    PL.getFoodData().getSaturationLevel(),
                    PL.getFoodData().getExhaustionLevel()
            );
        }
        return null;
    }
}
