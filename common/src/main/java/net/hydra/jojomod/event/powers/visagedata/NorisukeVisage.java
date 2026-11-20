package net.hydra.jojomod.event.powers.visagedata;

import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.LivingEntity;

public class NorisukeVisage extends VisageData {
    public NorisukeVisage(LivingEntity self) {
        super(self);
    }

    @Override
    public VisageData generateVisageData(LivingEntity entity) {
        return new NorisukeVisage(self);
    }

    @Override
    public Vec3i getHairColor() {
        return new Vec3i(208, 167, 88);
    }

    @Override
    public String getSkinPath() {
        return "norisuke";
    }

    @Override
    public boolean rendersNorisukeHair() {
        return true;
    }
}
