package net.hydra.jojomod.entity;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersKillerQueen;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

import java.util.Collection;

public class BombPlantedArrow extends Arrow {
    public BombPlantedArrow(EntityType<? extends Arrow> $$0, Level $$1) {
        super($$0, $$1);
    }

    public BombPlantedArrow(Level $$0, double $$1, double $$2, double $$3) {
        super($$0, $$1, $$2, $$3);
    }

    public BombPlantedArrow(Level $$0, LivingEntity $$1) {
        super($$0, $$1);
    }

    private Collection<MobEffectInstance> effects = null;

    @Override
    public void setEffectsFromItem(ItemStack $$0) {
        super.setEffectsFromItem($$0);
        if ($$0.is(Items.TIPPED_ARROW)) {
            effects = PotionUtils.getCustomEffects($$0);
        }
    }

    public Collection<MobEffectInstance> getEffects() {
        return effects;
    }

    public void tick() {
        super.tick();
        if (!level().isClientSide()) {

            if (getOwner() == null || !(getOwner().isAlive() && ((StandUser)getOwner()).roundabout$getStandPowers() instanceof PowersKillerQueen PKQ
                && PKQ.bombEntity == this)) {
                defuse();
            }
        }
    }

    @Override
    protected void doPostHurtEffects(LivingEntity target) {
        super.doPostHurtEffects(target);
        if (target != getOwner() && getOwner() instanceof LivingEntity LE && ((StandUser) LE).roundabout$getStandPowers() instanceof PowersKillerQueen PKQ && PKQ.bombEntity.getId() == getId()) {
            PKQ.contactDetonate(target);
        }
    }


    public void defuse() {
        if (getOwner() instanceof Player) {
            this.pickup = AbstractArrow.Pickup.ALLOWED;
        }
    }
}
