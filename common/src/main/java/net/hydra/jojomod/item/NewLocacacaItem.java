package net.hydra.jojomod.item;

import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.LocacacaCurseIndex;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class NewLocacacaItem extends Item {

    /**32 is the default eating duration of a food. New Locaca takes notably longer to eat.*/
    @Override
    public int getUseDuration(ItemStack $$0) {
            return 64;
    }
    public NewLocacacaItem(Properties $$0) {
        super($$0);
    }


    @Override
    public void onUseTick(Level $$0, LivingEntity $$1, ItemStack $$2, int $$3) {
            LivingEntity ent = MainUtil.getStoneTarget($$0, $$1);
            if (ent != null) {
                float scale = ($$2.getUseDuration() - $$1.getUseItemRemainingTicks());
                if (!$$0.isClientSide) {
                    double random = (Math.random() * 1.2) - 0.6;
                    double random2 = (Math.random() * 1.2) - 0.6;
                    double random3 = (Math.random() * 1.2) - 0.6;
                    ((ServerLevel) $$0).sendParticles(new ItemParticleOption(ParticleTypes.ITEM,
                                    Blocks.STONE.asItem().getDefaultInstance()), ent.getX() + random,
                            ent.getY() + ent.getEyeHeight() + random2, ent.getZ() + random3,
                            0,
                            ($$1.getX() - ent.getX()), ($$1.getY() - ent.getY() + ent.getEyeHeight()), ($$1.getZ() - ent.getZ()),
                            0.08);
                }
                ent.setDeltaMovement(ent.getDeltaMovement().add(
                        ($$1.getX() - ent.getX())*0.018,
                        0,
                        ($$1.getZ() - ent.getZ())*0.018
                ));
            }
    }

    public float getFusionDamage(LivingEntity target, float basePower){
        if (target instanceof Player){
            basePower/=4;
        }
        return basePower;
    }


    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity entity) {
        ItemStack $$3 = super.finishUsingItem(itemStack, level, entity);
        byte curse = ((StandUser)entity).roundabout$getLocacacaCurse();
        LivingEntity ent = MainUtil.getStoneTarget(level, entity);
        if (ent != null) {
            if (!level.isClientSide && entity.isAlive()) {
                if (curse > -1){
                    float damage = 20;
                    if (curse == LocacacaCurseIndex.HEART || curse == LocacacaCurseIndex.CHEST){
                        damage = 40;
                    }
                    if (ent.hurt(ModDamageTypes.of(entity.level(), ModDamageTypes.FUSION, entity), damage)){
                        if (curse == LocacacaCurseIndex.MAIN_HAND || curse == LocacacaCurseIndex.OFF_HAND){
                            ent.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 1200, 1), entity);
                        } else if (curse == LocacacaCurseIndex.LEFT_LEG || curse == LocacacaCurseIndex.RIGHT_LEG){
                            ent.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1200, 1), entity);
                        } else if (curse == LocacacaCurseIndex.HEAD){
                            ent.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 1200, 0), entity);
                        }
                    }
                    ((StandUser)entity).roundabout$setLocacacaCurse((byte) -1);
                } else {
                    float health = entity.getHealth();
                    float maxHealth = entity.getMaxHealth();
                    float exchangeDamage = maxHealth - health;
                    health += (maxHealth*1F);
                    if (health > maxHealth){
                        health=maxHealth;
                    }
                    entity.setHealth(health);
                    ent.hurt(ModDamageTypes.of(entity.level(), ModDamageTypes.FUSION, entity), exchangeDamage);
                }
            }
        }

        return $$3;
    }
}
