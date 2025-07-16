package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersD4C;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class D4CEntity extends FollowingStandEntity {
    public D4CEntity(EntityType<? extends Mob> entityType, Level world) { super(entityType, world); }

    public static final byte
        MANGA_SKIN = 0,
        WONDER_FESTIVAL = 1,
        PROMO = 2,
        PROMO_L = 3,
        SPECIAL = 4,
        GRAND = 5;



    public final AnimationState parallelWorldWindupAnimationState = new AnimationState();
    public final AnimationState hideFists = new AnimationState();
    public final AnimationState hideLeg = new AnimationState();
    public final AnimationState kick_barrage = new AnimationState();
    public final AnimationState kick_barrage_end = new AnimationState();
    public final AnimationState kick_barrage_windup = new AnimationState();
    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        if (this.getUser() != null) {
            if (this.getAnimation() != 12) {
                this.hideFists.startIfStopped(this.tickCount);
            } else {
                this.hideFists.stop();
            }
            if (this.getAnimation() != 80) {
                this.hideLeg.startIfStopped(this.tickCount);
                this.kick_barrage.stop();
            } else {
                this.hideLeg.stop();
                this.kick_barrage.startIfStopped(this.tickCount);
            }


            if (this.getAnimation() == 42) {
                this.kick_barrage_windup.startIfStopped(this.tickCount);
            } else {
                this.kick_barrage_windup.stop();
            }

            if (this.getAnimation() == 43) {
                this.kick_barrage_end.startIfStopped(this.tickCount);
            } else {
                this.kick_barrage_end.stop();
            }

            if (this.getAnimation() == 30)
                this.parallelWorldWindupAnimationState.startIfStopped(this.tickCount);
            else
                this.parallelWorldWindupAnimationState.stop();
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide)
            return;

        if (!this.hasUser())
            return;

        if (((StandUser)this.getUser()).roundabout$getStandPowers() instanceof PowersD4C PD4C) {
            if (PD4C.meltDodgeTicks != -1)
                PD4C.meltDodgeTicks += 1;

            if (PD4C.meltDodgeTicks >= 20) //insert melt dodge ticks cooldown here
                PD4C.meltDodgeTicks = -1;
        }
    }
}
