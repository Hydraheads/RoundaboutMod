package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.PowersD4C;
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
            BETA_SKIN = 4;

    @Override public Component getSkinName(byte skinId) {
        return switch (skinId)
        {
            case LIGHT_SKIN -> Component.translatable("skins.roundabout.soft_and_wet.light");
            case BETA_SKIN -> Component.translatable("skins.roundabout.soft_and_wet.beta");
            case KING_SKIN -> Component.translatable("skins.roundabout.soft_and_wet.king");
            default -> Component.translatable("skins.roundabout.soft_and_wet.base");
        };
    }

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
        }
    }

}

