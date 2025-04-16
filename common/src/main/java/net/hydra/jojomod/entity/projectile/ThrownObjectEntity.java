package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.block.*;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.*;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class ThrownObjectEntity extends ThrowableItemProjectile {
   public boolean places;

    private static final EntityDataAccessor<Boolean> ROUNDABOUT$SUPER_THROWN = SynchedEntityData.defineId(ThrownObjectEntity.class, EntityDataSerializers.BOOLEAN);

    private int superThrowTicks = -1;

    private void initDataTrackerRoundabout(CallbackInfo ci) {
    }

    public ThrownObjectEntity(EntityType<? extends ThrowableItemProjectile> $$0, Level $$1) {
        super($$0, $$1);
        this.places = false;
    }

    public ThrownObjectEntity(LivingEntity living, Level $$1) {
        super(ModEntities.THROWN_OBJECT, living, $$1);
        places = false;
    }

    public ThrownObjectEntity(LivingEntity living, Level $$1, ItemStack itemStack, boolean places) {
        super(ModEntities.THROWN_OBJECT, living, $$1);
        this.setItem(itemStack);
        this.places = places;
    }

    public void starThrowInit(){
        this.entityData.set(ROUNDABOUT$SUPER_THROWN, true);
        superThrowTicks = 50;
    }

    public ThrownObjectEntity(Level world, double p_36862_, double p_36863_, double p_36864_, ItemStack itemStack, boolean places) {
        super(ModEntities.THROWN_OBJECT, p_36862_, p_36863_, p_36864_, world);
        this.setItem(itemStack);
        this.places = places;
    }

    @Override
    public void tick(){
        Vec3 delta = this.getDeltaMovement();
        if (!this.level().isClientSide()) {
            if (this.getEntityData().get(ROUNDABOUT$SUPER_THROWN)) {

            }
        }
        super.tick();
        if (this.getEntityData().get(ROUNDABOUT$SUPER_THROWN)) {
            this.setDeltaMovement(delta);
        }
        if (!this.level().isClientSide()) {
            if (superThrowTicks > -1) {
                superThrowTicks--;
                if (superThrowTicks <= -1) {
                    this.entityData.set(ROUNDABOUT$SUPER_THROWN, false);
                } else {
                    if ((this.tickCount+2) % 4 == 0){
                        if (this.getItem().getItem() instanceof BlockItem){
                            ((ServerLevel) this.level()).sendParticles(ModParticles.AIR_CRACKLE,
                                    this.getX(), this.getY()+0.5F, this.getZ(),
                                    0, 0, 0, 0, 0);
                        } else {
                            ((ServerLevel) this.level()).sendParticles(ModParticles.AIR_CRACKLE,
                                    this.getX(), this.getY(), this.getZ(),
                                    0, 0, 0, 0, 0);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag $$0){
        $$0.putBoolean("roundabout.AcquireHeldItem",places);
        CompoundTag compoundtag = new CompoundTag();
        $$0.put("roundabout.HeldItem",this.getItem().save(compoundtag));
        super.addAdditionalSaveData($$0);
    }
    @Override
    public void readAdditionalSaveData(CompoundTag $$0){
        this.places = $$0.getBoolean("roundabout.AcquireHeldItem");
        CompoundTag compoundtag = $$0.getCompound("roundabout.HeldItem");
        ItemStack itemstack = ItemStack.of(compoundtag);
        this.setItem(itemstack);
        super.readAdditionalSaveData($$0);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(ROUNDABOUT$SUPER_THROWN, false);
    }
    public boolean getSuperThrow() {
        return this.getEntityData().get(ROUNDABOUT$SUPER_THROWN);
    }
    @Override
    protected Item getDefaultItem() {
        return Items.AIR;
    }

    @Override
    protected float getGravity() {
        if (getSuperThrow()){
            return 0;
        } else {
            return 0.03F;
        }
    }
    Direction tempDirection = Direction.UP;

    public boolean tryHitBlock(BlockHitResult $$0, BlockPos pos, BlockState state){

        if ((state.isAir() || state.canBeReplaced()) && (this.getOwner() != null && !((this.getOwner() instanceof Player &&
                (((Player) this.getOwner()).blockActionRestricted(this.getOwner().level(), pos, ((ServerPlayer)
                        this.getOwner()).gameMode.getGameModeForPlayer()))) ||
                !this.getOwner().level().mayInteract(((Player) this.getOwner()), pos)))){

            if (this.getItem().getItem() instanceof BlockItem) {
                Direction direction = this.getDirection();
                if (direction.getAxis() == Direction.Axis.X){
                    direction = direction.getOpposite();
                }
                if (((BlockItem) this.getItem().getItem()).getBlock() instanceof RotatedPillarBlock){
                    direction = $$0.getDirection();
                }

                if (((BlockItem)this.getItem().getItem()).place(new DirectionalPlaceContext(this.level(),
                        pos,
                        direction, this.getItem(),
                        direction)) != InteractionResult.FAIL){
                    this.tempDirection = direction;
                    return true;
                }
            }

        }
        return false;
    }

    //@SuppressWarnings("deprecation")
    @Override
    protected void onHitBlock(BlockHitResult $$0) {
        super.onHitBlock($$0);


        if (!this.level().isClientSide) {
            if (!this.getItem().isEmpty()) {
                BlockPos pos = $$0.getBlockPos().relative($$0.getDirection());
                BlockState state = this.level().getBlockState(pos);
                if (this.getItem().getItem() instanceof BlockItem) {
                    if (this.places) {
                        if (tryHitBlock($$0, pos, state)) {
                        } else if (tryHitBlock($$0, pos.relative(this.tempDirection), state)) {
                        } else if (tryHitBlock($$0, pos.above(), state)) {
                        } else {
                            dropItem(pos);
                        }
                    } else {
                        if (this.getItem().getItem() instanceof BlockItem){
                            Block blkk = (((BlockItem) this.getItem().getItem()).getBlock());
                            this.playSound(blkk.defaultBlockState().getSoundType().getBreakSound(), 1.0F, 0.9F);
                            blockBreakParticles(blkk,
                                    new Vec3(pos.getX()+0.5,
                                            pos.getY()+0.5,
                                            pos.getZ()+0.5));
                        }
                    }
                } else if (this.getItem().is(Items.BRICK) || this.getItem().is(Items.NETHER_BRICK)) {
                    Block blkk = this.level().getBlockState($$0.getBlockPos()).getBlock();
                    if (this.places && blkk instanceof AbstractGlassBlock || blkk instanceof StainedGlassPaneBlock
                            || blkk.defaultBlockState().is(Blocks.GLASS_PANE)){
                        if (this.level().removeBlock($$0.getBlockPos(),false)){
                            blockBreakParticles(blkk,
                                    new Vec3($$0.getBlockPos().getX()+0.5,
                                            $$0.getBlockPos().getY()+0.5,
                                            $$0.getBlockPos().getZ()+0.5));
                            this.playSound(blkk.defaultBlockState().getSoundType().getBreakSound(), 1.0F, 0.9F);
                            return;
                        }
                    } else {
                        dropItem(pos);
                    }
                } else if (this.getItem().getItem() instanceof BoneMealItem) {
                    if (this.places && useBonemeal(this.getItem(), $$0)){
                    } else {
                        dropItem(pos);
                    }
                } else if (this.getItem().getItem() instanceof HangingEntityItem he) {
                    if (this.places && $$0.getDirection().getAxis().isHorizontal() && he.useOn(new DirectionalPlaceContext(this.level(),
                                $$0.getBlockPos(),
                                $$0.getDirection(), this.getItem(),
                                $$0.getDirection())) != InteractionResult.FAIL){
                    } else {
                        dropItem(pos);
                    }
                } else if (this.getItem().getItem() instanceof SpawnEggItem se) {
                    if (this.places && se.useOn(new DirectionalPlaceContext(this.level(),
                            $$0.getBlockPos(),
                            $$0.getDirection(), this.getItem(),
                            $$0.getDirection())) != InteractionResult.FAIL) {
                    } else {
                        dropItem(pos);
                    }
                } else if (this.getItem().getItem() instanceof RecordItem se) {
                    BlockEntity blke = this.level().getBlockEntity($$0.getBlockPos());
                    if (blke instanceof JukeboxBlockEntity je && je.getFirstItem().isEmpty()) {
                        je.setItem(0,this.getItem());
                    } else if (blke instanceof StereoBlockEntity je && je.getFirstItem().isEmpty()) {
                        je.setItem(0,this.getItem());
                    } else {
                        dropItem(pos);
                    }
                } else {
                    dropItem(pos);
                }
            }
        }
        if (!this.level().isClientSide() || !(this.getItem().is(Items.BRICK) || this.getItem().is(Items.NETHER_BRICK))) {
            this.discard();
        }

    }

    public void dropItem(BlockPos pos){
        ItemEntity $$4 = new ItemEntity(this.level(), pos.getX() + 0.5F,
                pos.getY() + 0.25F, pos.getZ() + 0.5F,
                this.getItem());
        $$4.setPickUpDelay(40);
        $$4.setThrower(this.getUUID());
        $$4.setDeltaMovement(Math.random() * 0.1F - 0.05F, 0.2F, Math.random() * 0.18F - 0.05F);
        this.level().addFreshEntity($$4);
    }

    public float getDamage(Entity ent){
        float damage = 1;
        if (this.getItem().getItem() instanceof BlockItem){
            float DT =((BlockItem)this.getItem().getItem()).getBlock().defaultDestroyTime();
            if ((((BlockItem) this.getItem().getItem()).getBlock()) instanceof AbstractGlassBlock ||
                    (((BlockItem) this.getItem().getItem()).getBlock()) instanceof StainedGlassPaneBlock ||
                    this.getItem().is(Items.GLASS_PANE)) {
                damage = 12;
            } else if ((((BlockItem) this.getItem().getItem()).getBlock()) instanceof CactusBlock) {
                damage = 10;
            } else if ((((BlockItem) this.getItem().getItem()).getBlock()) instanceof AnvilBlock) {
                damage = 20.5F;
            } else if ((((BlockItem) this.getItem().getItem()).getBlock()) instanceof WebBlock) {
                damage = 1F;
            } else if ((((BlockItem) this.getItem().getItem()).getBlock()) instanceof StreetSignBlock) {
                damage = 18F;
            } else if (DT <= 0.4) {
                damage = 1;
            } else if (DT <= 1){
                damage = 4;
            } else if (DT <= 1.5){
                damage = 8;
            } else if (DT <= 2){
                damage = 10;
            } else if (DT <= 3){
                damage = 11;
            } else if (DT <= 5){
                damage = 12;
            } else if (DT <= 10){
                damage = 14;
            } else if (DT <= 25){
                damage = 15;
            } else {
                damage = 17;
            }
                    //stone = 2 seconds, obsidian = 50 seconds, dirt = 0.5 seconds
        } else {
            boolean enchant = false;
            if (this.getItem().getItem() instanceof SwordItem){
                damage = (float) (5+ (((SwordItem)this.getItem().getItem()).getDamage()*1.5));
                enchant = true;
            } else if (this.getItem().getItem() instanceof DiggerItem){
                damage = (float) (5+ (((DiggerItem)this.getItem().getItem()).getAttackDamage()*1.5));
                enchant = true;
            } else if (this.getItem().getItem() instanceof GlaiveItem){
                damage = (float) (5+ (((GlaiveItem)this.getItem().getItem()).getDamage()*1.5));
                enchant = true;
            } else if (this.getItem().is(Items.IRON_NUGGET)){
                damage = 10;
            } else if (this.getItem().is(Items.PRISMARINE_SHARD)){
                damage = 7;
            } else if (this.getItem().is(Items.BRICK)){
                damage = 6;
            } else if (this.getItem().is(Items.IRON_INGOT)){
                damage = 10;
            } else if (this.getItem().is(Items.DIAMOND)){
                damage = 11;
            } else if (this.getItem().is(Items.GOLD_NUGGET)){
                damage = 6;
            } else if (this.getItem().is(Items.GOLD_INGOT)){
                damage = 6;
            } else if (this.getItem().is(Items.COAL)){
                damage = 3;
            } else if (this.getItem().is(Items.FLINT)){
                damage = 5;
            } else if (this.getItem().is(Items.COPPER_INGOT)){
                damage = 5;
            } else if (this.getItem().is(Items.AMETHYST_SHARD)){
                damage = 7;
            }
            if (enchant){
                if (ent instanceof LivingEntity){
                    damage+= EnchantmentHelper.getDamageBonus(this.getItem(), ((LivingEntity)ent).getMobType());
                } else {
                    damage+= EnchantmentHelper.getDamageBonus(this.getItem(), MobType.UNDEFINED);
                }
            }
        }
        damage*= (float) (ClientNetworking.getAppropriateConfig().damageMultipliers.thrownBlocks*0.01);
        return damage;
    }

    public void blockBreakParticles(Block block, Vec3 pos){
        if (!this.level().isClientSide()) {
            ((ServerLevel) this.level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK,
                            block.defaultBlockState()),
                    pos.x, pos.y, pos.z,
                    100, 0, 0, 0, 0.5);
        }
    }


    @Override
    protected void onHitEntity(EntityHitResult $$0) {
        Entity $$1 = $$0.getEntity();
        if ($$1 instanceof LivingEntity LE){
            if (((StandUser)LE).roundabout$getStandPowers().dealWithProjectile(this)){
                this.discard();
                return;
            }
        }

        Entity $$4 = this.getOwner();

        DamageSource $$5 = ModDamageTypes.of($$1.level(), ModDamageTypes.THROWN_OBJECT, $$4);

        Vec3 DM = $$1.getDeltaMovement();

        boolean fire = false;
        boolean onFire = $$1.isOnFire();
        if (!this.getItem().isEmpty()) {
            int ench = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FIRE_ASPECT, this.getItem());
            if (ench >= 1) {
                $$1.setSecondsOnFire((ench) * 4);
                fire = true;
            } else if (this.getItem().getItem() instanceof FlintAndSteelItem
                    || this.getItem().is(Items.MAGMA_BLOCK)
                    || this.getItem().is(Items.CAMPFIRE)
                    || this.getItem().getItem() instanceof FireChargeItem) {
                $$1.setSecondsOnFire(4);
                fire = true;
            } else if (this.getItem().is(Items.LAVA_BUCKET)) {
                $$1.setSecondsOnFire(8);
                fire = true;
            }
        }

        if (this.getItem().getItem() instanceof NameTagItem) {
            if ($$1 instanceof LivingEntity && !this.useNametag(this.getItem(), ((LivingEntity) $$1))){
                this.dropItem($$1.getOnPos());
            }
        } else if (this.getItem().getItem() instanceof StandDiscItem SD) {
            if (this.places && $$1 instanceof LivingEntity LE && MainUtil.canGrantStand(LE)){
                ((StandUser) LE).roundabout$setStandDisc(this.getItem());
                SD.generateStandPowers(LE);
                ((StandUser) LE).roundabout$summonStand($$1.level(),true,true);
            } else {
                this.dropItem($$1.getOnPos());
            }
        } else if (this.getItem().getItem() instanceof DyeItem && $$1 instanceof LivingEntity) {
            if (!this.useDye(this.getItem(), ((LivingEntity) $$1))){
                this.dropItem($$1.getOnPos());
            }
        } else if (this.getItem().getItem() instanceof MaskItem && $$1 instanceof Player pe) {
            if (ClientNetworking.getAppropriateConfig().canThrowVisagesOntoOtherPlayers && ((IPlayerEntity)pe).roundabout$getMaskInventory().getItem(0).isEmpty()){
                ((IPlayerEntity)pe).roundabout$getMaskInventory().setItem(0,this.getItem().copy());

            } else {
                this.dropItem($$1.getOnPos());
            }
        } else if (this.getItem().getItem() instanceof SaddleItem && $$1 instanceof LivingEntity) {
            if (!this.useSaddle(this.getItem(), ((LivingEntity) $$1))){
                this.dropItem($$1.getOnPos());
            }
        } else if (this.getItem().getItem() instanceof LeadItem && $$1 instanceof Mob) {
            if (!this.useLeash(this.getItem(), ((Mob) $$1))){
                this.dropItem($$1.getOnPos());
            }
        } else if ($$1.hurt($$5, this.getDamage($$1))) {
            if ($$1.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (!this.getItem().isEmpty()) {

                if ($$1 instanceof LivingEntity L){

                    if (this.getOwner() instanceof Player PE){
                        ((StandUser)PE).roundabout$getStandPowers().addEXP(1);
                    }

                    if (this.getItem().is(Items.COBWEB)) {
                        L.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 0), this);
                    }
                    if (this.getItem().is(Items.PUFFERFISH)) {
                        L.addEffect(new MobEffectInstance(MobEffects.POISON, 200, 0), this);
                    }
                    if (this.getItem().is(Items.WITHER_ROSE)) {
                        L.addEffect(new MobEffectInstance(MobEffects.WITHER, 200, 0), this);
                    }
                    if (this.getItem().is(ModItems.GASOLINE_BUCKET)){
                        ((StandUser) L).roundabout$setGasolineTime(((StandUser) L).roundabout$getMaxBucketGasolineTime());
                    }
                }

                if (this.getItem().getItem() instanceof GlaiveItem || this.getItem().getItem() instanceof ScissorItem) {
                    MainUtil.makeBleed($$1, 0, 300, this);
                } else if (this.getItem().is(Items.PRISMARINE_SHARD)
                || this.getItem().is(Items.GLASS_BOTTLE)){
                    MainUtil.makeBleed($$1, 0, 200, this);

                }


                if (this.getItem().getItem() instanceof BlockItem && MainUtil.isThrownBlockItem(this.getItem().getItem())) {
                    Block blk = ((BlockItem) this.getItem().getItem()).getBlock();
                    if (blk instanceof CactusBlock || blk instanceof AbstractGlassBlock ||
                            blk instanceof StainedGlassPaneBlock || blk instanceof BarbedWireBlock
                            || blk instanceof BarbedWireBundleBlock || this.getItem().is(Items.GLASS_PANE)) {
                        MainUtil.makeBleed($$1, 0, 300, this);
                    }

                    SoundEvent SE = (blk).
                            defaultBlockState().getSoundType().getBreakSound();
                    this.playSound(SE, 1.0F, 0.9F);
                    if ($$1 instanceof LivingEntity $$7) {
                        float kb = 0.3f;

                        if (((BlockItem)this.getItem().getItem()).getBlock() instanceof SlimeBlock){
                            kb = 0.8f;
                        }
                        $$7.knockback(kb, this.getX() - $$7.getX(), this.getZ() - $$7.getZ());
                    }
                } else {
                    if ($$1 instanceof LivingEntity $$7) {
                        $$7.knockback(0.15f, this.getX() - $$7.getX(), this.getZ() - $$7.getZ());
                    }
                }

                if (this.getItem().isDamageableItem()){
                    if (!this.getItem().hurt(1,this.level().getRandom(),null)){
                        this.dropItem($$1.getOnPos());
                    }
                } else if (this.getItem().getItem() instanceof BlockItem){
                    blockBreakParticles((((BlockItem) this.getItem().getItem()).getBlock()),$$0.getEntity().getPosition(0F).add(0,$$0.getEntity().getEyeHeight(),0));
                }
            }
        } else {
            if (fire && !onFire){
                $$1.clearFire();
            }

            if (!this.getItem().isEmpty()) {
                if (this.getItem().getItem() instanceof GlaiveItem || this.getItem().getItem() instanceof AxeItem) {
                    if ($$1 instanceof Player PE) {
                        if (PE.isBlocking() && !((StandUser) PE).roundabout$isGuarding()) {
                            PE.getCooldowns().addCooldown(Items.SHIELD, 100);
                            PE.stopUsingItem();
                            PE.level().broadcastEntityEvent(this, (byte) 30);
                        }
                    }
                }
            }
            if (this.getItem().getItem() instanceof BlockItem) {
                if (this.places) {
                    this.dropItem($$1.getOnPos());
                }
            } else {
                this.dropItem($$1.getOnPos());
            }
        }
        this.discard();

    }

    public boolean useBonemeal(ItemStack item, BlockHitResult pos){
        Level $$1 = this.level();
        BlockPos $$2 = pos.getBlockPos();
        if (!this.level().getBlockState($$2.above()).isAir()){
            $$2 = $$2.above();
        }
        BlockPos $$3 = $$2.relative(pos.getDirection());
        if (BoneMealItem.growCrop(item, $$1, $$2)) {
            if (!$$1.isClientSide) {
                $$1.levelEvent(1505, $$2, 0);
            }
            return true;
        } else {
            BlockState $$4 = $$1.getBlockState($$2);
            boolean $$5 = $$4.isFaceSturdy($$1, $$2, pos.getDirection());
            if ($$5 && BoneMealItem.growWaterPlant(item, $$1, $$3, pos.getDirection())) {
                if (!$$1.isClientSide) {
                    $$1.levelEvent(1505, $$3, 0);
                }
                return true;
            }
        }
        return false;
    }
    public boolean useLeash(ItemStack stack, Mob entity) {
        if (this.getOwner() != null && this.getOwner() instanceof Player player){
            if (entity.canBeLeashed(player)) {
                entity.setLeashedTo(player, true);
                return true;
            }
        }
        return false;
    }

    public boolean useSaddle(ItemStack $$0, LivingEntity $$2){
        if ($$2 instanceof Saddleable && $$2.isAlive()) {
            Saddleable $$4 = (Saddleable)$$2;
            if (!$$4.isSaddled() && $$4.isSaddleable()) {
                if (!this.level().isClientSide) {
                    $$4.equipSaddle(SoundSource.NEUTRAL);
                    $$2.level().gameEvent($$2, GameEvent.EQUIP, $$2.position());
                    return true;
                }
            }
        }
        return false;
    }
    public boolean useDye(ItemStack stack, LivingEntity entity) {
        if (entity instanceof Sheep $$4 && $$4.isAlive() && !$$4.isSheared() && $$4.getColor() != ((DyeItem) stack.getItem()).getDyeColor()) {
            $$4.level().playSound(null, $$4, SoundEvents.DYE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
            if (!this.level().isClientSide()) {
                $$4.setColor(((DyeItem) stack.getItem()).getDyeColor());
                return true;
            }
        }
        return false;
    }

    public boolean useNametag(ItemStack stack, LivingEntity entity){
        if (stack.hasCustomHoverName() && !(entity instanceof Player)) {
            if (!this.level().isClientSide() && entity.isAlive()) {
                entity.setCustomName(stack.getHoverName());
                if (entity instanceof Mob) {
                    ((Mob) entity).setPersistenceRequired();
                }
                return true;
            }
        }
        return false;
    }

    public void shootWithVariance(double $$0, double $$1, double $$2, float $$3, float $$4) {
        Vec3 $$5 = new Vec3($$0, $$1, $$2)
                .normalize()
                .add(
                        this.random.triangle(0.0, 0.13 * (double)$$4),
                        this.random.triangle(0.0, 0.13 * (double)$$4),
                        this.random.triangle(0.0, 0.13 * (double)$$4)
                )
                .scale((double)$$3);
        this.setDeltaMovement($$5);
        double $$6 = $$5.horizontalDistance();
        this.setYRot((float)(Mth.atan2($$5.x, $$5.z) * 180.0F / (float)Math.PI));
        this.setXRot((float)(Mth.atan2($$5.y, $$6) * 180.0F / (float)Math.PI));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }


    public void shootFromRotationWithVariance(Entity $$0, float $$1, float $$2, float $$3, float $$4, float $$5) {
        float $$6 = -Mth.sin($$2 * (float) (Math.PI / 180.0)) * Mth.cos($$1 * (float) (Math.PI / 180.0));
        float $$7 = -Mth.sin(($$1 + $$3) * (float) (Math.PI / 180.0));
        float $$8 = Mth.cos($$2 * (float) (Math.PI / 180.0)) * Mth.cos($$1 * (float) (Math.PI / 180.0));
        this.shootWithVariance((double)$$6, (double)$$7, (double)$$8, $$4, $$5);
        Vec3 $$9 = $$0.getDeltaMovement();
        this.setDeltaMovement(this.getDeltaMovement().add($$9.x, $$0.onGround() ? 0.0 : $$9.y, $$9.z));
    }
}
