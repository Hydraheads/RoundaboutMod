package net.hydra.jojomod.entity.stand;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.access.ILivingEntityAccess;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.layers.PreRenderEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersSurvivor;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
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


    /** Stand does not take damage under normal circumstances.*/

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.getUser() != null && MainUtil.isStandDamage(source) && !forceDespawnSet){
            forceDespawnSet = true;
            return this.getUser().hurt(source,amount*0.5F);
        }
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


    public static void drawParticleLine(Entity from, Entity to, int particleCount) {
        if (!(from.level() instanceof ServerLevel serverLevel)) return;

        Vec3 start = from.position();
        Vec3 end = to.position();
        Vec3 diff = end.subtract(start);

        for (int i = 0; i <= particleCount; i++) {
            double progress = i / (double) particleCount;
            Vec3 point = start.add(diff.scale(progress));

            serverLevel.sendParticles(
                    ModParticles.ZAP, // change to whatever particle you want
                    point.x, point.y, point.z,
                    1, // count
                    0, 0.2F, 0, // offset
                    0       // speed
            );
        }
    }
    public void matchEntities(LivingEntity one, LivingEntity two){
        ((StandUser)one).roundabout$setZappedToID(two.getId());
        ((StandUser)one).roundabout$aggressivelyEnforceZapAggro();
        ((StandUser)two).roundabout$setZappedToID(one.getId());
        ((StandUser)two).roundabout$aggressivelyEnforceZapAggro();
        drawParticleLine(this,one,10);
        drawParticleLine(this,two,10);
        drawParticleLine(one,two,10);
        sendMessageTo(one,two);
        sendMessageTo(two,one);
        this.level().playSound(null, this.blockPosition(), ModSounds.SURVIVOR_SHOCK_EVENT, SoundSource.NEUTRAL, 1F, (float) (0.9F + (Math.random() * 0.2F)));
    }

    public void sendMessageTo(LivingEntity LE, LivingEntity LE2){
        if (LE instanceof ServerPlayer PE) {
            PE.displayClientMessage(Component.translatable("text.roundabout.survivor.match", LE2.getDisplayName()).
                    withStyle(ChatFormatting.RED), true);
        }
    }


    public static boolean canZapEntity(Entity ent){
        return (ent != null && ent.isAlive() && !ent.isRemoved() && (ent instanceof Mob || ent instanceof Player)
                && !(ent instanceof StandEntity) && ent.isPickable() && !ent.isInvulnerable() &&
                !(ent instanceof Player PL && PL.isCreative()) &&
                ent instanceof LivingEntity LE
                && !((StandUser) LE).roundabout$isBubbleEncased());
    }

    public void attemptShock(){
        LivingEntity user = this.getUser();
        if (user != null && !((StandUser)user).roundabout$getUniqueStandModeToggle()) {
            List<Entity> mobsInRange = MainUtil.getEntitiesInRange(this.level(), this.blockPosition(), ClientNetworking.getAppropriateConfig().survivorSettings.survivorRange, this);
            LivingEntity firstTarget = null;
            if (!mobsInRange.isEmpty()) {
                for (Entity ent : mobsInRange) {
                    if (canZapEntity(ent) && ent instanceof LivingEntity LE
                    && ((StandUser) LE).roundabout$getZappedToID() <= -1) {
                        if (firstTarget == null) {
                            firstTarget = LE;
                        } else {
                            matchEntities(firstTarget, LE);
                            return;
                        }
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
                    this.level().playSound(null, this.blockPosition(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1F, 0.9F);
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
