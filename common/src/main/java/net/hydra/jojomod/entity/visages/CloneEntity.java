package net.hydra.jojomod.entity.visages;

import net.hydra.jojomod.entity.stand.StarPlatinumEntity;
import net.hydra.jojomod.item.MaskItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Unique;

import java.util.Optional;
import java.util.UUID;

public class CloneEntity extends PathfinderMob {
    @Unique
    private static final EntityDataAccessor<Optional<UUID>> PLAYER = SynchedEntityData.defineId(CloneEntity.class,
            EntityDataSerializers.OPTIONAL_UUID);

    public Player player;
    public Component name;
    public ItemStack visage = ItemStack.EMPTY;

    public void setPlayer(Player player){
        this.player = player;
        setPlayerUUID(player.getUUID());
        this.name = player.getDisplayName();
    }
    public Player getPlayer(){
        if (this.player == null && this.getPlayerUUID().isPresent()){
            this.player = this.level().getPlayerByUUID(this.getPlayerUUID().get());
        }
        return this.player;
    }
    @Override
    protected void defineSynchedData() {
        if (!this.entityData.hasItem(PLAYER)) {
            super.defineSynchedData();
            this.entityData.define(PLAYER, Optional.empty());
        }
    }
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        if (!visage.isEmpty() || tag.contains("roundabout.Mask", 10)) {
            CompoundTag compoundtag = new CompoundTag();
            tag.put("roundabout.Mask",visage.save(compoundtag));
        }

        getPlayerUUID().ifPresent(uuid -> tag.putUUID("PlayerUUID", uuid));
    }
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("roundabout.Mask", 10)) {
            CompoundTag compoundtag = tag.getCompound("roundabout.Mask");
            ItemStack itemstack = ItemStack.of(compoundtag);
            if (!itemstack.isEmpty() && itemstack.getItem() instanceof MaskItem SD) {
                visage = itemstack;
            }
        }

        if (tag.hasUUID("PlayerUUID")) {
            UUID uuid = tag.getUUID("PlayerUUID");
            setPlayerUUID(uuid);
        } else {
            setPlayerUUID(null);
            this.player = null;
        }
    }
    public final Optional<UUID> getPlayerUUID() {
        return this.entityData.get(PLAYER);
    }
    public final void setPlayerUUID(@Nullable UUID uuid) {
        this.entityData.set(PLAYER, Optional.ofNullable(uuid));
    }
    protected CloneEntity(EntityType<? extends PathfinderMob> $$0, Level $$1) {
        super($$0, $$1);
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.ATTACK_DAMAGE, 1).
                add(Attributes.FOLLOW_RANGE, 48.0D);
    }
}
