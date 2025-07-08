package net.hydra.jojomod.entity.stand;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IBucketItem;
import net.hydra.jojomod.access.ILivingEntityAccess;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.layers.PreRenderEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersSurvivor;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class SurvivorEntity extends MultipleTypeStand implements PreRenderEntity {
    /**
     * Initialize Stands
     *
     * @param entityType
     * @param world
     */
    public SurvivorEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }

    protected static final EntityDataAccessor<Float> RANDOM_SIZE = SynchedEntityData.defineId(SurvivorEntity.class,
            EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Boolean> ACTIVATED = SynchedEntityData.defineId(SurvivorEntity.class,
            EntityDataSerializers.BOOLEAN);
    public final void setRandomSize(float randSize) {
        this.entityData.set(RANDOM_SIZE, randSize);
    }
    public final void setActivated(boolean active) {
        this.entityData.set(ACTIVATED, active);
    }
    public final float getRandomSize() {
        return this.entityData.get(RANDOM_SIZE);
    }
    public final boolean getActivated() {
        return this.entityData.get(ACTIVATED);
    }

    public int dryUpInNetherTicks = 0;
    public int shockCountUpTicks = 0;
    @Override

    public boolean validatePowers(LivingEntity user){
        return (((StandUser)user).roundabout$getStandPowers() instanceof PowersSurvivor);
    }
    public boolean hasNoPhysics(){
        return false;
    }

    public void tick(){
        super.tick();
        if (!this.level().isClientSide()) {
            if (!getActivated() && isInWaterRainOrBubble()){
                setActivated(true);
            }
            if (this.level().dimension() == Level.NETHER) {
                int dryTickMax = ClientNetworking.getAppropriateConfig().survivorSettings.dryUpInNetherTicks;
                if (getActivated()) {
                    dryUpInNetherTicks++;
                    if (dryUpInNetherTicks > dryTickMax) {
                        setActivated(false);
                        this.level().playSound(null, this.blockPosition(), SoundEvents.FIRE_EXTINGUISH, SoundSource.NEUTRAL, 1F, (float) (0.9F + (Math.random() * 0.2F)));

                        ((ServerLevel) this.level()).sendParticles(ParticleTypes.LARGE_SMOKE,
                                this.getX(), this.getY(), this.getZ(),
                                0, 0, 0, 0, 0.015);
                    }
                } else {
                    dryUpInNetherTicks--;
                }
                dryUpInNetherTicks = Mth.clamp(dryUpInNetherTicks, 0, dryTickMax);
            }


            if (getActivated()) {
                shockCountUpTicks++;
                if (shockCountUpTicks > 15){
                    attemptShock();
                    shockCountUpTicks = 0;
                }
            } else {
                shockCountUpTicks--;
            }
            shockCountUpTicks = Mth.clamp(shockCountUpTicks, 0, 15);
        }
    }

    public void matchEntities(LivingEntity one, LivingEntity two){
        ((StandUser)one).roundabout$setZappedToID(two.getId());
        ((StandUser)one).roundabout$aggressivelyEnforceZapAggro();
        ((StandUser)two).roundabout$setZappedToID(one.getId());
        ((StandUser)two).roundabout$aggressivelyEnforceZapAggro();
        this.level().playSound(null, this.blockPosition(), ModSounds.SURVIVOR_SHOCK_EVENT, SoundSource.NEUTRAL, 1F, (float) (0.9F + (Math.random() * 0.2F)));
    }

    public void attemptShock(){
        List<Entity> mobsInRange = MainUtil.getEntitiesInRange(this.level(),this.blockPosition(), 7, this);
        LivingEntity firstTarget = null;
        if (!mobsInRange.isEmpty()) {
            for (Entity ent : mobsInRange) {
                if (ent.isAlive() && !ent.isRemoved() && (ent instanceof Mob || ent instanceof Player)
                && !(ent instanceof StandEntity) && ent.isPickable() && !ent.isInvulnerable() &&
                        !(ent instanceof Player PL && PL.isCreative()) &&
                        ent instanceof LivingEntity LE && ((StandUser)LE).roundabout$getZappedToID() <= -1){
                    if (firstTarget == null){
                        firstTarget = LE;
                    } else {
                        matchEntities(firstTarget,LE);
                        return;
                    }
                }
            }
        }
    }


    /**the user can interact with its own survivors**/
    @Override
    public boolean isPickable() {
        if (this.level().isClientSide()){
            return ClientUtil.isPlayer(this.getUser());
        }
        return false;
    }
    @SuppressWarnings("deprecation")
    @Override
    public InteractionResult interactAt(Player player, Vec3 location, InteractionHand intHand) {
        if (!player.level().isClientSide()) {
            if (player.isSpectator()) {
                return InteractionResult.SUCCESS;
            }

            if (!this.getActivated()) {
                ItemStack plrItem = player.getItemInHand(intHand);
                if ((!plrItem.isEmpty() && plrItem.getItem() instanceof PotionItem PI && PotionUtils.getPotion(plrItem) == Potions.WATER)) {
                    if (!player.getAbilities().instabuild) {
                        plrItem.shrink(1);
                        player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
                    }
                    this.setActivated(true);
                    return InteractionResult.SUCCESS;
                } if ((!plrItem.isEmpty() && plrItem.getItem() instanceof BucketItem BI && ((IBucketItem)BI).roundabout$getContents().is(FluidTags.WATER))) {
                    if (!player.getAbilities().instabuild) {
                        plrItem.shrink(1);
                        player.getInventory().add(new ItemStack(Items.BUCKET));
                    }
                    this.setActivated(true);
                    return InteractionResult.SUCCESS;
                }
            }

        }
        return InteractionResult.CONSUME;

    }
    @Override
    public boolean needsActive(){
        return false;
    }

    /**it instantly rotates towards its server rotation instead of awkwardly spinning for a few seconds*/
    @Override
    public boolean preRender(Entity ent, double $$1, double $$2, double $$3, float $$4, PoseStack pose, MultiBufferSource $$6) {
        float lerpYRot = (float) ((ILivingEntityAccess)ent).roundabout$getLerpYRot();
        ent.yRotO = lerpYRot;
        ent.setYRot(lerpYRot);
        ent.setYBodyRot(lerpYRot);
        ent.setYHeadRot(lerpYRot);
        return false;
    }
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(RANDOM_SIZE, 0F);
        this.entityData.define(ACTIVATED, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag $$0){
        $$0.putFloat("roundabout.randomSize",getRandomSize());
        $$0.putBoolean("roundabout.activated",getActivated());
        super.addAdditionalSaveData($$0);
    }
    @Override
    public void readAdditionalSaveData(CompoundTag $$0){
        setRandomSize($$0.getFloat("roundabout.randomSize"));
        setActivated($$0.getBoolean("roundabout.activated"));
        super.readAdditionalSaveData($$0);
    }
}
