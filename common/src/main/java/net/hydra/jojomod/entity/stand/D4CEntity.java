package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersD4C;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class D4CEntity extends StandEntity {
    public D4CEntity(EntityType<? extends Mob> entityType, Level world) { super(entityType, world); }

    public static final byte
        MANGA_SKIN = 0,
        WONDER_FESTIVAL = 1,
        PROMO = 2,
        PROMO_L = 3,
        SPECIAL = 4,
        GRAND = 5;

    @Override public Component getSkinName(byte skinId) {
        switch (skinId)
        {
            case WONDER_FESTIVAL -> {return Component.translatable("skins.roundabout.d4c.wonder_festival");}
            case PROMO -> {return Component.translatable("skins.roundabout.d4c.promo");}
            case PROMO_L -> {return Component.translatable("skins.roundabout.d4c.promo_l");}
            case SPECIAL -> {return Component.translatable("skins.roundabout.d4c.special");}
            case GRAND -> {return Component.translatable("skins.roundabout.d4c.grand");}
            default -> {return Component.translatable("skins.roundabout.d4c.base");}
        }
    }

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

            if (PD4C.meltDodgeTicks >= ClientNetworking.getAppropriateConfig().durationsInTicks.D4CMeltDodgeTicks)
                PD4C.meltDodgeTicks = -1;
        }
    }
}
