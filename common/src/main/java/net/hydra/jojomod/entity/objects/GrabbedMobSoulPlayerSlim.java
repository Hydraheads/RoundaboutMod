package net.hydra.jojomod.entity.objects;

import net.hydra.jojomod.entity.stand.StandEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class GrabbedMobSoulPlayerSlim extends GrabbedMobSoul{

    public GrabbedMobSoulPlayerSlim(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }

    int tick = 0;

    @Override
    public void tick(){
        super.tick();
        tick++;
        System.out.println(tick);
    }

}
