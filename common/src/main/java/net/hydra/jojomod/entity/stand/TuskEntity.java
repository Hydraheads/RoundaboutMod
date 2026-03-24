package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.Roundabout;
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
