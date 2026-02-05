package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.entity.ModEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class EvilAuraProjectile extends RoundaboutGeneralProjectile{
    public EvilAuraProjectile(EntityType<? extends RoundaboutGeneralProjectile> $$0, Level $$1) {
        super($$0, $$1);
    }

    protected EvilAuraProjectile(EntityType<? extends RoundaboutGeneralProjectile> $$0, double $$1, double $$2, double $$3, Level $$4) {
        this($$0, $$4);
        this.setPos($$1, $$2, $$3);
    }
    protected EvilAuraProjectile(EntityType<RoundaboutGeneralProjectile> $$0, LivingEntity $$1, Level $$2) {
        this($$0, $$1.getX(), $$1.getEyeY() - 0.1F, $$1.getZ(), $$2);
        this.setOwner($$1);
    }

    public EvilAuraProjectile(LivingEntity $$1, Level $$2) {
        this(ModEntities.EVIL_AURA_PROJECTILE, $$1.getX(), $$1.getEyeY() - 0.1F, $$1.getZ(), $$2);
        this.setOwner($$1);
    }

    @Override
    public int getMaxLifeSpan(){
        return 20;
    }
}
