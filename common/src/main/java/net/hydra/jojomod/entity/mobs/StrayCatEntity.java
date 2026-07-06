package net.hydra.jojomod.entity.mobs;

import net.hydra.jojomod.entity.stand.StandEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class StrayCatEntity extends TamableAnimal {

    public StrayCatEntity(EntityType<? extends TamableAnimal> $$0, Level $$1) {
        super($$0, $$1);
    }

    public static AttributeSupplier.Builder createStandAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED,
                0.0F).add(Attributes.MAX_HEALTH, 18.0).add(Attributes.ATTACK_DAMAGE, 5.0);
    }


    @Override
    public void tick() {

    }
    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }
}
