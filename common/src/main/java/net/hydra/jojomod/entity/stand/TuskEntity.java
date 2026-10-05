package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.projectile.TuskNailEntity;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class TuskEntity extends FollowingStandEntity {

    public TuskEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }

    public int getAct() { // maybe a little jank tbh but I didn't want to make multiple classes. My original plan was to make them all 1 entity but it fell apart
        try {
            String id = this.getName().toString();
            int act = id.indexOf("tusk_a") + 6;
            if (act != -1) {
                return Integer.parseInt(id.substring(act,act+1));
            }
        } catch (NumberFormatException e) {}
        return -1;
    }

    public static final byte DEATH_PUNCH = 50;
    public static final byte SHRINK = 51;

    public final AnimationState hideFists = new AnimationState();
    public final AnimationState deathPunch = new AnimationState();
    public final AnimationState shrink = new AnimationState();

    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();

        this.hideFists.animateWhen(this.getAnimation() != BARRAGE,this.tickCount);
        this.barrageAnimationState.animateWhen(this.getAnimation() == BARRAGE,this.tickCount);
        this.barrageChargeAnimationState.animateWhen(this.getAnimation() == BARRAGE_CHARGE,this.tickCount);
        this.deathPunch.animateWhen(this.getAnimation() == DEATH_PUNCH,this.tickCount);
        this.shrink.animateWhen(this.getAnimation() == SHRINK,this.tickCount);
    }

    @Override
    public boolean canBeHitByStands() {
        return this.getAct() == 4 && this.getOffsetType() == OffsetIndex.LOOSE;
    }

    @Override
    public boolean canStandBeHurt() {
        return this.getAct() == 4 && this.getOffsetType() == OffsetIndex.LOOSE;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getAnimation() == SHRINK && this.hurtTime == 0 && !this.level().isClientSide()) {
            this.discard();
            TuskNailEntity tuskNailEntity = new TuskNailEntity(this.getUser(),this.level(),(byte)4);
            tuskNailEntity.shootFromRotation(this,
                    redirectX,redirectY,-0.5F,1,0.05F);
            this.level().addFreshEntity(tuskNailEntity);
        }
    }


    private float redirectX = 0;
    private float redirectY = 0;

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() != null && source.equals(ModDamageTypes.STAND)) {
            this.setAnimation(SHRINK);
            redirectX = source.getEntity().getViewXRot(0);
            redirectY = source.getEntity().getViewYRot(0);
        }
        return super.hurt(source, amount);
    }

    @Override
    public float getDistanceOutModified() {
        if (!this.getDisplay()) {
            return switch (this.getAct()) {
                case 1, 2 -> super.getDistanceOutModified() * 0.5F;
                case 4 -> super.getDistanceOut() + 0.5F;
                default -> super.getDistanceOutModified();
            };
        }
        return super.getDistanceOutModified();
    }
    @Override
    public float getIdleYOffsetModified() {
        if (!this.getDisplay()) {
            return switch (this.getAct()) {
                case 1, 2 -> super.getIdleYOffsetModified() * 0.5F + 1;
                default -> super.getIdleYOffsetModified();
            };
        }
        return super.getIdleYOffsetModified();
    }

    @Override
    public float getAnchorPlaceModified() {
        return super.getAnchorPlaceModified() + (this.getAct() == 2 ? 10 : 0);
    }
}
