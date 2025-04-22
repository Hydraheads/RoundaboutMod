package net.hydra.jojomod.entity.stand;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class CinderellaEntity extends StandEntity {
    public CinderellaEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }
    public static final byte
            PART_4_SKIN = 1,
            MANGA_SKIN = 2;

    @Override
    public Component getSkinName(byte skinId) {
        return getSkinNameT(skinId);
    }

    public static Component getSkinNameT(byte skinId){
        if (skinId == MANGA_SKIN) {
            return Component.translatable("skins.roundabout.cinderella.manga");
        }
        return Component.translatable("skins.roundabout.cinderella.base");
    }
    public final AnimationState deface = new AnimationState();
    public final AnimationState visages = new AnimationState();
    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        if (this.getUser() != null) {
            if (this.getAnimation() == 81) {
                this.deface.startIfStopped(this.tickCount);
            } else {
                this.deface.stop();
            }
            if (this.getAnimation() == 82) {
                this.visages.startIfStopped(this.tickCount);
            } else {
                this.visages.stop();
            }
        }
    }
}
