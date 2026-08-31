package net.hydra.jojomod.entity.visages;

import com.mojang.authlib.GameProfile;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.navigation.ActiveCloneManager;
import net.hydra.jojomod.entity.stand.StarPlatinumEntity;
import net.hydra.jojomod.entity.stand.WhitesnakeEntity;
import net.hydra.jojomod.event.powers.visagedata.VisageData;
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

import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;

public class CloneEntity extends PathfinderMob {
    @Unique
    private static final EntityDataAccessor<Optional<UUID>> PLAYER = SynchedEntityData.defineId(CloneEntity.class,
            EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<ItemStack> VISAGE = SynchedEntityData.defineId(CloneEntity.class,
            EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<String> DISGUISE_NAME = SynchedEntityData.defineId(
            CloneEntity.class, EntityDataSerializers.STRING);

    public boolean isBackingUp = false;
    public boolean isMovingForward = false;
    public boolean isSneaking = false;
    public boolean isSprinting = false;
    public boolean runaway = false;
    public boolean runawayTrue = false;

    public String getDisguiseName(){
        return entityData.get(DISGUISE_NAME);
    }
    public void setDisguiseName(String visage){
        entityData.set(DISGUISE_NAME,visage);
    }
    public ItemStack getVisage(){
        return entityData.get(VISAGE);
    }
    public void setVisage(ItemStack visage){
        entityData.set(VISAGE,visage);
    }
    @Override
    public double getMyRidingOffset() {
        return -0.35;
    }
    @Override
    public Component getDisplayName() {
        Player player = getPlayer();

            boolean characterType = true;
            if (getVisage() != null && !getVisage().isEmpty() && getVisage().getItem() instanceof MaskItem ME) {

                VisageData vd = ME.visageData.generateVisageData(player);
                characterType = vd.isCharacterVisage();

                if (ClientNetworking.getAppropriateConfig() != null  && ClientNetworking.getAppropriateConfig().nameTagSettings != null) {
                    if (characterType) {
                        if (ClientNetworking.getAppropriateConfig().nameTagSettings.renderActualCharactersNameUsingVisages) {

                            if (vd.swapName()){
                                return Component.translatable("item.roundabout."+vd.getSkinPath() + "_mask.tag");
                            }
                            return ME.getDisplayNameTag();
                        }
                    }
                }
            }

        if (player != null) {
            return player.getDisplayName();
        }


        if (this.name != null) {
            return this.name;
        }

        return super.getDisplayName();
    }

    public boolean turned = false;
    public Player player;
    public Component name;

    public void setPlayer(Player player){
        this.player = player;
        setPlayerUUID(player.getUUID());
        this.name = player.getDisplayName();
        setDisguiseName(String.valueOf(this.name));
    }

    public Player getPlayer(){
        if (this.player == null && this.getPlayerUUID().isPresent()){
            this.player = this.level().getPlayerByUUID(this.getPlayerUUID().get());
        }
        return this.player;
    }

    @Override
    public void remove(RemovalReason reason) {
        ActiveCloneManager.remove(this);
        super.remove(reason);
    }
    @Override
    protected void defineSynchedData() {
        if (!this.entityData.hasItem(PLAYER)) {
            super.defineSynchedData();
            this.entityData.define(PLAYER, Optional.empty());
            this.entityData.define(VISAGE, ItemStack.EMPTY);
            this.entityData.define(DISGUISE_NAME, "Player");
        }
    }
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        if (!getVisage().isEmpty() || tag.contains("roundabout.Mask", 10)) {
            CompoundTag compoundtag = new CompoundTag();
            tag.put("roundabout.Mask",getVisage().save(compoundtag));
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
                setVisage(itemstack);
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
    private void moveCloak() {
        this.xCloakO = this.xCloak;
        this.yCloakO = this.yCloak;
        this.zCloakO = this.zCloak;
        double $$0 = this.getX() - this.xCloak;
        double $$1 = this.getY() - this.yCloak;
        double $$2 = this.getZ() - this.zCloak;
        double $$3 = 10.0;
        if ($$0 > 10.0) {
            this.xCloak = this.getX();
            this.xCloakO = this.xCloak;
        }

        if ($$2 > 10.0) {
            this.zCloak = this.getZ();
            this.zCloakO = this.zCloak;
        }

        if ($$1 > 10.0) {
            this.yCloak = this.getY();
            this.yCloakO = this.yCloak;
        }

        if ($$0 < -10.0) {
            this.xCloak = this.getX();
            this.xCloakO = this.xCloak;
        }

        if ($$2 < -10.0) {
            this.zCloak = this.getZ();
            this.zCloakO = this.zCloak;
        }

        if ($$1 < -10.0) {
            this.yCloak = this.getY();
            this.yCloakO = this.yCloak;
        }

        this.xCloak += $$0 * 0.25;
        this.zCloak += $$2 * 0.25;
        this.yCloak += $$1 * 0.25;
    }

    public float oBob;
    public float bob;
    public void aiStep(){
        super.aiStep();
        this.oBob = this.bob;
        float $$4;
        if (this.onGround() && !this.isDeadOrDying()) {
            $$4 = (float)Math.min(0.1, this.getDeltaMovement().horizontalDistance());
        } else {
            $$4 = 0.0F;
        }

        this.bob = this.bob + ($$4 - this.bob) * 0.4F;
    }
    public double xCloakO;
    public double yCloakO;
    public double zCloakO;
    public double xCloak;
    public double yCloak;
    public double zCloak;
    public void tick(){
        moveCloak();
        super.tick();
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
    @javax.annotation.Nullable
    public GameProfile getDisguiseProfile() {
        Optional<UUID> id = getPlayerUUID();
        String name = entityData.get(DISGUISE_NAME);
        return id.isPresent() && !name.isEmpty() ? new GameProfile(id.get(), name) : null;
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.ATTACK_DAMAGE, 1).
                add(Attributes.FOLLOW_RANGE, 48.0D);
    }
}
