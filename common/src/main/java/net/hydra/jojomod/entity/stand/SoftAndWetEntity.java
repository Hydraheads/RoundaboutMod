package net.hydra.jojomod.entity.stand;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class SoftAndWetEntity extends StandEntity {
    public SoftAndWetEntity(EntityType<? extends Mob> entityType, Level world) { super(entityType, world); }

    public static final byte
            MANGA_SKIN = 1,
            LIGHT_SKIN = 2,
            KING_SKIN = 3,
            BETA_SKIN = 4,
            DROWNED_SKIN = 5,
            DROWNED_SKIN_2 = 6,
            FIGURE_SKIN = 7,
            STRIPED = 8,
            DEBUT = 9,
            KIRA = 10;

    @Override public Component getSkinName(byte skinId) {
        return switch (skinId)
        {
            case MANGA_SKIN -> Component.translatable("skins.roundabout.soft_and_wet.base");
            case BETA_SKIN -> Component.translatable("skins.roundabout.soft_and_wet.beta");
            case KING_SKIN -> Component.translatable("skins.roundabout.soft_and_wet.king");
            case DROWNED_SKIN -> Component.translatable("skins.roundabout.soft_and_wet.drowned");
            case DROWNED_SKIN_2 -> Component.translatable("skins.roundabout.soft_and_wet.drowned_2");
            case FIGURE_SKIN -> Component.translatable("skins.roundabout.soft_and_wet.figure");
            case STRIPED -> Component.translatable("skins.roundabout.soft_and_wet.striped");
            case DEBUT -> Component.translatable("skins.roundabout.soft_and_wet.debut");
            default -> Component.translatable("skins.roundabout.soft_and_wet.light");
        };
    }

    public final AnimationState hideFists = new AnimationState();
    public final AnimationState hideLeg = new AnimationState();
    public final AnimationState kick_barrage = new AnimationState();
    public final AnimationState kick_barrage_end = new AnimationState();
    public final AnimationState kick_barrage_windup = new AnimationState();
    public final AnimationState kick = new AnimationState();
    public final AnimationState kick_charge = new AnimationState();
    public final AnimationState encasement_punch = new AnimationState();


    public static final byte
            KICK = 25,
            ENCASEMENT_STRIKE = 26,
            KICK_CHARGE = 27;
    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        if (this.getUser() != null) {
            if (this.getAnimation() != BARRAGE) {
                this.hideFists.startIfStopped(this.tickCount);
            } else {
                this.hideFists.stop();
            }
            if (this.getAnimation() != KICK_BARRAGE) {
                this.hideLeg.startIfStopped(this.tickCount);
                this.kick_barrage.stop();
            } else {
                this.hideLeg.stop();
                this.kick_barrage.startIfStopped(this.tickCount);
            }

            if (this.getAnimation() == KICK) {
                this.kick.startIfStopped(this.tickCount);
            } else {
                this.kick.stop();
            }
            if (this.getAnimation() == ENCASEMENT_STRIKE) {
                this.encasement_punch.startIfStopped(this.tickCount);
            } else {
                this.encasement_punch.stop();
            }

            if (this.getAnimation() == KICK_CHARGE) {
                this.kick_charge.startIfStopped(this.tickCount);
            } else {
                this.kick_charge.stop();
            }
            if (this.getAnimation() == KICK_BARRAGE_WINDUP) {
                this.kick_barrage_windup.startIfStopped(this.tickCount);
            } else {
                this.kick_barrage_windup.stop();
            }

            if (this.getAnimation() == KICK_BARRAGE_END) {
                this.kick_barrage_end.startIfStopped(this.tickCount);
            } else {
                this.kick_barrage_end.stop();
            }
        }
    }

}

