package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.access.IEnderMan;
import net.hydra.jojomod.block.FogBlock;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.event.ModGamerules;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.PowersSoftAndWet;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;

import java.util.List;

public class SoftAndWetItemLaunchingBubbleEntity extends SoftAndWetBubbleEntity{
    public SoftAndWetItemLaunchingBubbleEntity(EntityType<? extends SoftAndWetItemLaunchingBubbleEntity> $$0, Level $$1) {
        super($$0, $$1);
    }

    public SoftAndWetItemLaunchingBubbleEntity(LivingEntity $$1, Level $$2) {
        super(ModEntities.ITEM_LAUNCHING_BUBBLE_ENTITY, $$1.getX(), $$1.getEyeY() - 0.1F, $$1.getZ(), $$2);
        this.setOwner($$1);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag $$0){
        CompoundTag compoundtag = new CompoundTag();
        $$0.put("roundabout.HeldItem",this.getHeldItem().save(compoundtag));
        $$0.putBoolean("roundabout.ditchedItem",hasDitchedItem);
        $$0.putFloat("roundabout.speed",getSped());
        super.addAdditionalSaveData($$0);
    }
    @Override
    public void readAdditionalSaveData(CompoundTag $$0){
        CompoundTag compoundtag = $$0.getCompound("roundabout.HeldItem");
        ItemStack itemstack = ItemStack.of(compoundtag);
        hasDitchedItem = $$0.getBoolean("roundabout.ditchedItem");
        setSped($$0.getFloat("roundabout.speed"));
        this.setHeldItem(itemstack);
        super.readAdditionalSaveData($$0);
    }
    @Override
    protected float getInertia() {
        return 0.97F;
    }
    @Override
    public int getDistanceUntilPopping(){
        return ClientNetworking.getAppropriateConfig().softAndWetSettings.maxExplosiveBubbleTravelDistanceBeforePopping;
    }

    protected static final EntityDataAccessor<ItemStack> HELD_ITEM = SynchedEntityData.defineId(SoftAndWetItemLaunchingBubbleEntity.class,
            EntityDataSerializers.ITEM_STACK);
    public final ItemStack getHeldItem() {
        return this.entityData.get(HELD_ITEM);
    }
    public final void setHeldItem(ItemStack stack) {
        this.entityData.set(HELD_ITEM, stack);
    }
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        if (!this.entityData.hasItem(HELD_ITEM)){
            this.entityData.define(HELD_ITEM, ItemStack.EMPTY);
        }
    }
    public void tick(){
        if (!this.level().isClientSide()) {
            lifeSpan--;
            if (lifeSpan <= 0 || (this.standUser == null || !(((StandUser) this.standUser).roundabout$getStandPowers() instanceof PowersSoftAndWet))) {
                popBubble();
                return;
            }
        }
        super.tick();
        if (!this.level().isClientSide()){
            if (!isRemoved()){

                Vec3 currentPos = this.position();
                Vec3 nextPos = currentPos.add(this.getDeltaMovement());
                AABB sweptBox = this.getBoundingBox()
                        .expandTowards(this.getDeltaMovement())
                        .inflate(this.getBbWidth() * 1 + 0.1); // Adjust as needed

                EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(
                        this.level(), this, currentPos, nextPos, sweptBox,
                        this::canHitEntity
                );

                if (entityHitResult != null) {
                    this.onHitEntity(entityHitResult);
                }
            }
        }
    }


    public boolean hasDitchedItem = false;
    public void addItemLight(){
        if (!hasDitchedItem) {
            if (standUser instanceof Player PE) {
                    ItemEntity $$4 = new ItemEntity(this.level(), this.getX(),
                            this.getY() + this.getEyeHeight(), this.getZ(),
                            getHeldItem());
                    $$4.setPickUpDelay(40);
                    $$4.setThrower(this.standUser.getUUID());
                    standUser.level().addFreshEntity($$4);
            } else {
                ItemEntity $$4 = new ItemEntity(this.level(), this.getX(),
                        this.getY() + this.getEyeHeight(), this.getZ(),
                        getHeldItem());
                $$4.setPickUpDelay(40);
                this.level().addFreshEntity($$4);
            }
        }
    }
    @Override
    public void remove(Entity.RemovalReason $$0) {
        if (!this.getHeldItem().isEmpty() && !this.level().isClientSide()) {
            addItemLight();
        }
        super.remove($$0);
    }
    public void popBubble(){
        if (!this.level().isClientSide()){
        this.level().playSound(null, this.blockPosition(), ModSounds.EXPLOSIVE_BUBBLE_POP_EVENT,
                SoundSource.PLAYERS, 2F, (float)(0.98+(Math.random()*0.04)));
            ((ServerLevel) this.level()).sendParticles(ModParticles.BUBBLE_POP,
                    this.getX(), this.getY() + this.getBbHeight(), this.getZ(),
                    1, 0, 0,0, 0.015);
            this.discard();
        }
    }
    @SuppressWarnings("deprecation")
    @Override
    protected void onHitBlock(BlockHitResult $$0) {
        if (!this.level().isClientSide()) {
            ((ServerLevel) this.level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, this.level().getBlockState($$0.getBlockPos())),
                    $$0.getLocation().x, $$0.getLocation().y, $$0.getLocation().z,
                    30, 0.2, 0.05, 0.2, 0.3);

            BlockState bs = this.level().getBlockState($$0.getBlockPos());
            if (MainUtil.getIsGamemodeApproriateForGrief(this.getOwner())){
                BlockPos relativePos = $$0.getBlockPos().relative($$0.getDirection());
                BlockState bs2 = this.level().getBlockState(relativePos);
                if (bs.getBlock() instanceof TntBlock tnt){
                        this.level().setBlock($$0.getBlockPos(), Blocks.AIR.defaultBlockState(), 3);
                        wasExploded(this.level(),$$0.getBlockPos());
                } else if (bs.getBlock() instanceof AbstractGlassBlock || bs.getBlock() instanceof StainedGlassPaneBlock
                        || bs.getBlock().defaultBlockState().is(Blocks.GLASS_PANE) || bs.is(BlockTags.LEAVES)
                        || bs.is(BlockTags.ICE)){
                    this.level().setBlock($$0.getBlockPos(), Blocks.AIR.defaultBlockState(), 3);
                    blockBreakParticles(bs.getBlock(),
                            new Vec3($$0.getBlockPos().getX()+0.5,
                                    $$0.getBlockPos().getY()+0.5,
                                    $$0.getBlockPos().getZ()+0.5));
                    this.playSound(bs.getBlock().defaultBlockState().getSoundType().getBreakSound(), 1.0F, 0.9F);
                } else if (bs.canBeReplaced() && !(bs.getBlock() instanceof FogBlock) && !bs.liquid()){
                    this.level().setBlock($$0.getBlockPos(), Blocks.AIR.defaultBlockState(), 3);
                    blockBreakParticles(bs.getBlock(),
                            new Vec3(relativePos.getX()+0.5,
                                    relativePos.getY()+0.5,
                                    relativePos.getZ()+0.5));
                    this.playSound(bs.getBlock().defaultBlockState().getSoundType().getBreakSound(), 1.0F, 0.9F);
                } else if (bs2.canBeReplaced() && !(bs2.getBlock() instanceof FogBlock) && !bs2.liquid()){
                    this.level().setBlock(relativePos, Blocks.AIR.defaultBlockState(), 3);
                    blockBreakParticles(bs2.getBlock(),
                            new Vec3(relativePos.getX()+0.5,
                                    relativePos.getY()+0.5,
                                    relativePos.getZ()+0.5));
                    this.playSound(bs2.getBlock().defaultBlockState().getSoundType().getBreakSound(), 1.0F, 0.9F);
                }
            }
        }
        popOnGroundWithForce($$0.getLocation());
    }

    public void blockBreakParticles(Block block, Vec3 pos){
        if (!this.level().isClientSide()) {
            ((ServerLevel) this.level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK,
                            block.defaultBlockState()),
                    pos.x, pos.y, pos.z,
                    100, 0, 0, 0, 0.5);
        }
    }
    public void wasExploded(Level $$0, BlockPos $$1) {
        if (!$$0.isClientSide) {
            PrimedTnt $$3 = new PrimedTnt($$0, (double)$$1.getX() + 0.5, (double)$$1.getY(), (double)$$1.getZ() + 0.5, null);
            int $$4 = $$3.getFuse();
            $$3.setFuse((short)($$0.random.nextInt($$4 / 4) + $$4 / 8));
            $$0.addFreshEntity($$3);
        }
    }
    @Override
    protected void onHitEntity(EntityHitResult $$0) {
        if (!this.level().isClientSide()) {

            if ($$0.getEntity() instanceof EnderMan em) {
                ((IEnderMan)em).roundabout$teleport();
                return;
            }
            Entity ent = $$0.getEntity();
            if (!(ent instanceof SoftAndWetBubbleEntity)) {
                if (this.getOwner() instanceof LivingEntity LE && ((StandUser)LE).roundabout$getStandPowers() instanceof PowersSoftAndWet PW) {
                    if (!(MainUtil.isMobOrItsMounts(ent, getOwner())) && !MainUtil.isCreativeOrInvincible(ent)) {

                        if (ent.hurt(ModDamageTypes.of(ent.level(), ModDamageTypes.EXPLOSIVE_STAND, this.getOwner()),
                                PW.getExplosiveBubbleStrength(ent))) {
                            //You don't need to hurt them to launch them
                        }

                        float degrees = MainUtil.getLookAtEntityYawWithAngle(ent.position().add(this.getDeltaMovement().reverse()), ent);
                        MainUtil.takeKnockbackWithY(ent, 1.05F,
                                Mth.sin(degrees * ((float) Math.PI / 180)),
                                Mth.sin(-17 * ((float) Math.PI / 180)),
                                -Mth.cos(degrees * ((float) Math.PI / 180)));
                        popBubble();
                    }
                }
            }
        }
    }

    public void popOnGroundWithForce(Vec3 location) {
        if (!this.level().isClientSide()) {
            Entity user = this.getOwner();
            if (user instanceof LivingEntity LE) {
                List<Entity> entityList = DamageHandler.genHitbox(LE, this.getX(), this.getY(),
                        this.getZ(), 3, 3, 3);
                if (!entityList.isEmpty()) {
                    for (Entity ent : entityList) {
                        if (!(ent instanceof SoftAndWetBubbleEntity)) {
                            if (((StandUser) LE).roundabout$getStandPowers() instanceof PowersSoftAndWet PW) {
                                if (!(MainUtil.isMobOrItsMounts(ent, getOwner())) && !MainUtil.isCreativeOrInvincible(ent)) {
                                    float degrees = MainUtil.getLookAtEntityYawWithAngle(location, ent);
                                    MainUtil.takeKnockbackWithY(ent, 0.6F,
                                            Mth.sin(degrees * ((float) Math.PI / 180)),
                                            Mth.sin(-17 * ((float) Math.PI / 180)),
                                            -Mth.cos(degrees * ((float) Math.PI / 180)));
                                }
                            }
                        }
                    }
                }
            }
            popBubble();
        }
    }

    public float getPopAccuracy(){
        return 0;
    }
    public float getBundleAccuracy(){
        return 0.5F;
    }
    public float getThrowAngle(){
        return -0.5F;
    }
    public float getThrowAngle2(){
        return 0.8F;
    }
    public float getThrowAngle3(){
        return -3.0F;
    }
    public boolean getCanPlace(LivingEntity self){
        return !(self instanceof Player PE && ((ServerPlayer) PE).gameMode.getGameModeForPlayer() == GameType.SPECTATOR)
                && !(self instanceof Player PE2 && ((ServerPlayer) PE2).gameMode.getGameModeForPlayer() == GameType.ADVENTURE)
                && self.level().getGameRules().getBoolean(ModGamerules.ROUNDABOUT_STAND_GRIEFING);
    }
    public void popWithForce(Vec3 launch2) {
        if (!this.level().isClientSide()) {
            Entity user = this.getOwner();
            if (user instanceof LivingEntity LE && launch2 != null) {
                if (!getHeldItem().isEmpty()) {
                    Vec2 yes = MainUtil.getRotationsBetween(this.position(),launch2);
                    float xRot = yes.x;
                    float yRot = yes.y;
                    if (ThrownObjectEntity.throwAnObject(LE, false, getHeldItem(), getPopAccuracy(), getBundleAccuracy(), getThrowAngle(),
                            getThrowAngle2(), getThrowAngle3(), getCanPlace(LE), false, xRot, yRot,
                            new Vec3(this.getX(), this.getEyeY() - 0.1F, this.getZ()),false)){
                        hasDitchedItem = true;
                    }
                }
            }
            popBubble();
        }
    }

}
