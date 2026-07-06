package net.hydra.jojomod.entity.mobs;

import net.hydra.jojomod.entity.goals.StrayCatBegGoal;
import net.hydra.jojomod.entity.goals.TerrierBegGoal;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.substand.SheerHeartAttackEntity;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public class StrayCatEntity extends TamableAnimal implements NeutralMob {
    public StrayCatEntity(EntityType<? extends TamableAnimal> $$0, Level $$1) {
        super($$0, $$1);
    }

    private static final EntityDataAccessor<Byte> BREED = SynchedEntityData.defineId(StrayCatEntity.class,
            EntityDataSerializers.BYTE);;
    private static final EntityDataAccessor<Boolean> POTTED = SynchedEntityData.defineId(StrayCatEntity.class,
            EntityDataSerializers.BOOLEAN);;

    @Override
    protected void defineSynchedData() {
        this.entityData.define(BREED, (byte) 0);
        this.entityData.define(POTTED, false);
    }

    public void setBreed(byte b) {
        this.entityData.define(BREED, b);
    }

    public void setPotted(boolean pot) {
        this.entityData.define(POTTED, pot);
    }


    public static AttributeSupplier.Builder createStandAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED,
                0.0F).add(Attributes.MAX_HEALTH, 18.0).add(Attributes.ATTACK_DAMAGE, 5.0);
    }

    public boolean canBeLeashed(Player $$0) { return false; }

    @Override
    public void tick() {

    }
    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }


    @Override
    public boolean isFood(ItemStack stack) {
        Item item = stack.getItem();
        return item.isEdible() && (Objects.requireNonNull(item.getFoodProperties()).isMeat() || stack.is(ModItems.COFFEE_GUM));
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.getAvailableGoals().removeIf(e -> e.getPriority() == 9);
        this.goalSelector.addGoal(9, new StrayCatBegGoal(this, 8.0f));
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return 0;
    }

    @Override
    public void setRemainingPersistentAngerTime(int i) {

    }

    @Override
    public @Nullable UUID getPersistentAngerTarget() {
        return null;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID uuid) {

    }

    @Override
    public void startPersistentAngerTimer() {

    }
}
