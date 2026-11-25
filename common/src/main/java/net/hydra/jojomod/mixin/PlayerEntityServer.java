package net.hydra.jojomod.mixin;

import com.mojang.authlib.GameProfile;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.access.IPlayerEntityServer;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.gui.BlackSabbathPlayerInventoryMenu;
import net.hydra.jojomod.client.gui.D4CCloneInventoryMenu;
import net.hydra.jojomod.entity.D4CCloneEntity;
import net.hydra.jojomod.entity.stand.BlackSabbathEntity;
import net.hydra.jojomod.entity.stand.FollowingStandEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.ModGamerules;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.platform.Services;
import net.hydra.jojomod.stand.powers.PowersBlackSabbath;
import net.hydra.jojomod.stand.powers.PowersDiverDown;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(ServerPlayer.class)
public abstract class PlayerEntityServer extends Player implements IPlayerEntityServer {

    public PlayerEntityServer(Level world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Shadow
    private void initMenu(AbstractContainerMenu $$0) {
    }
    @Shadow
    private void nextContainerCounter() {
    }

    @Override
    @Unique
    public int roundabout$getCounter(){
        return containerCounter;
    }
    @Override
    @Unique
    public void roundabout$nextContainerCounter(){
        nextContainerCounter();
    }
    @Override
    @Unique
    public void roundabout$initMenu(AbstractContainerMenu $$0){
        initMenu($$0);
    }
    @Unique
    private boolean roundabout$initializeDataOnClient = false;
    @Shadow
    public ServerGamePacketListenerImpl connection;
    @Shadow private int containerCounter;
    @Shadow private int spawnInvulnerableTime;
    @Unique private int roundabout$invincibleTicks = 0;
    @Unique private int roundabout$gerScenarioTicks = 0;
    @Unique private boolean roundabout$gerScenarioStarted = false;
    @Unique private final List<Entity> roundabout$gerTempEntities = new ArrayList<>();

    @Override
    public void roundabout$setInvincibleTicks(int ticks){
        roundabout$invincibleTicks = ticks;
    }
    @Inject(method = "onEffectRemoved", at = @At(value = "HEAD"))
    public void roundabout$oneffectRemoved(MobEffectInstance $$0, CallbackInfo ci) {

    }
    @Inject(method = "isChangingDimension()Z", at = @At(value = "HEAD"), cancellable = true)
    public void roundabout$changeDimensions(CallbackInfoReturnable<Boolean> cir) {
        if (roundabout$invincibleTicks > 0){
            cir.setReturnValue(true);
        }
    }
    @Inject(method = "tick", at = @At(value = "HEAD"))
    public void roundabout$tick(CallbackInfo ci) {
        if (!this.level().isClientSide() && !roundabout$initializeDataOnClient && connection !=null && connection.isAcceptingMessages()){
            IPlayerEntity ipe = ((IPlayerEntity)this);
            S2CPacketUtil.sendPowerInventorySettings(
                    ((ServerPlayer)((Player)(Object)this)), ipe.roundabout$getAnchorPlace(),
                    ipe.roundabout$getDistanceOut(),
                    ipe.roundabout$getSizePercent(),
                    ipe.roundabout$getIdleRotation(),
                    ipe.roundabout$getIdleYOffset(),
                    ipe.roundabout$getAnchorPlaceAttack());
            roundabout$initializeDataOnClient = true;
        }
        if (!this.level().isClientSide) {
            if (roundabout$invincibleTicks > 0){
                roundabout$invincibleTicks--;
            }
            ((IPlayerEntity) this).roundabout$getMaskInventory().update();
            ((IPlayerEntity) this).roundabout$getBlckSabbathPlayerInventory().update();
            IPlayerEntity ipe = (IPlayerEntity) this;
            int loops = ipe.rdbt$getGerDeathLoops();

            if (loops > 0 && this.isAlive()) {
                if (PowerTypes.getPlaneOfExisting((Entity) this) != 13) {
                    PowerTypes.setPlaneOfExisting((Entity) this, (byte) 13);
                }
                ((StandUser) this).roundabout$sealStand(1300);

                if (!roundabout$gerScenarioStarted) {
                    roundabout$gerScenarioStarted = true;
                    roundabout$gerScenarioTicks = 0;
                    roundabout$startGerDeathScenario(10 - loops);
                }

                roundabout$gerScenarioTicks++;
                roundabout$tickGerDeathScenario(10 - loops, roundabout$gerScenarioTicks);
            }
        }
    }


    @Inject(method = "teleportTo(Lnet/minecraft/server/level/ServerLevel;DDDFF)V", at = @At(value = "TAIL"))
    public void roundabout$teleportTo(ServerLevel p_9000_, double p_9001_, double p_9002_, double p_9003_, float p_9004_, float p_9005_, CallbackInfo info) {
        if (p_9000_ == this.level()) {
            ((StandUser) this).roundabout$getFollowers().forEach($$0 -> {
                for (StandEntity $$1 : ((StandUser) this).roundabout$getFollowers()) {
                    if ($$1.level().dimension() == this.level().dimension()) {
                        $$1.moveTo(this.getX(), this.getY(), this.getZ());
                    }
                }
            });
            StandUser standUserData = ((StandUser) this);
            if (standUserData.roundabout$hasStandOut()  && standUserData.roundabout$getStand() instanceof FollowingStandEntity fe) {
                if (fe.level().dimension() == this.level().dimension()) {
                    standUserData.roundabout$updateStandOutPosition(fe, Entity::moveTo);
                }
            }
        }
    }

    /**Make NBT save on death*/
    /** respawn */
    @Inject(method = "restoreFrom(Lnet/minecraft/server/level/ServerPlayer;Z)V", at = @At(value = "TAIL"))
    public void roundabout$respawn(ServerPlayer $$0, boolean $$1, CallbackInfo info) {
        if ($$0.level().getGameRules().getBoolean(ModGamerules.ROUNDABOUT_KEEP_STANDS_ON_DEATH)){
            ((StandUser)this).roundabout$setStandDisc(MainUtil.saveToDiscData($$0,((StandUser)$$0).roundabout$getStandDisc()));
        } else {
            if (ClientNetworking.getAppropriateConfig().itemSettings.standDiscsDropWithKeepGameRuleOff){
                ItemStack disc = MainUtil.saveToDiscData($$0,((StandUser)$$0).roundabout$getStandDisc());
                $$0.drop(disc,true,false);
            }
            ((StandUser)this).roundabout$setStandDisc(ItemStack.EMPTY);
        }
        IPlayerEntity ipe = ((IPlayerEntity)this);
        ipe.roundabout$setMaskInventory(((IPlayerEntity)$$0).roundabout$getMaskInventory());
        ipe.roundabout$setBlckSabbathPlayerInventory(((IPlayerEntity)$$0).roundabout$getBlckSabbathPlayerInventory());
        if (!this.level().isClientSide) {
            ((IPlayerEntity) this).roundabout$setMaskSlot(((IPlayerEntity) $$0).roundabout$getMaskSlot());
            ((IPlayerEntity) this).roundabout$setMaskVoiceSlot(((IPlayerEntity) $$0).roundabout$getMaskVoiceSlot());

            int anchorPlace = ((IPlayerEntity) $$0).roundabout$getAnchorPlace();
            int anchorPlaceAttack = ((IPlayerEntity) $$0).roundabout$getAnchorPlaceAttack();
            float distanceOut = ((IPlayerEntity) $$0).roundabout$getDistanceOut();
            float size = ((IPlayerEntity) $$0).roundabout$getSizePercent();
            float rotat = ((IPlayerEntity) $$0).roundabout$getIdleRotation();
            float yOffset = ((IPlayerEntity) $$0).roundabout$getIdleYOffset();
            byte teamColor = ((IPlayerEntity) $$0).roundabout$getTeamColor();
            ipe.roundabout$setAnchorPlace(anchorPlace);
            ipe.roundabout$setDistanceOut(distanceOut);
            ipe.roundabout$setSizePercent(size);
            ipe.roundabout$setIdleRotation(rotat);
            ipe.roundabout$setIdleYOffset(yOffset);
            ipe.roundabout$setTeamColor(teamColor);
            ipe.roundabout$setAnchorPlaceAttack(anchorPlaceAttack);

            ipe.rdbt$setHairColorX(((IPlayerEntity) $$0).rdbt$getHairColorX());
            ipe.rdbt$setHairColorY(((IPlayerEntity) $$0).rdbt$getHairColorY());
            ipe.rdbt$setHairColorZ(((IPlayerEntity) $$0).rdbt$getHairColorZ());
            ipe.rdbt$setZombieFish(((IPlayerEntity) $$0).rdbt$getZombieFish());

            S2CPacketUtil.sendPowerInventorySettings(
                        ((ServerPlayer)((Player)(Object)this)), anchorPlace,distanceOut,
                    ipe.roundabout$getSizePercent(),
                    ipe.roundabout$getIdleRotation(),
                    ipe.roundabout$getIdleYOffset(),
                    anchorPlaceAttack);

            if (!this.level().getGameRules().getBoolean(ModGamerules.ROUNDABOUT_LOSE_FATE_ON_DEATH)) {
                byte strat = ((IPlayerEntity) $$0).rdbt$getRespawnStrategy();
                if (strat < 1) {
                    ipe.roundabout$setFate(((IPlayerEntity) $$0).roundabout$getFate());
                }
            }

            ipe.rdbt$setVampireData(((IPlayerEntity) $$0).rdbt$getVampireData());
            ipe.roundabout$setPower(((IPlayerEntity) $$0).roundabout$getPower());

            IPlayerEntity oldIpe = (IPlayerEntity) $$0;
            int loops = oldIpe.rdbt$getGerDeathLoops();
            ListTag savedInv = oldIpe.rdbt$getGerSavedInventory();

            if (!$$1 && loops > 0) {
                ipe.rdbt$setGerDeathLoops(loops);
                // stash the inventory IF they have one
                if (savedInv == null && !this.getInventory().isEmpty()) {
                    savedInv = this.getInventory().save(new ListTag());
                }
                ipe.rdbt$setGerSavedInventory(savedInv);
                this.getInventory().clearContent();
            } else if (!$$1 && savedInv != null) {
                this.getInventory().load(savedInv);
                ipe.rdbt$setGerSavedInventory(null);
            }
        }
    }

    public void roundabout$openBlackSabbathInventory(BlackSabbathEntity $$0, Container $$1) {
        ServerPlayer player = (ServerPlayer) (Object) this;
        if(this.containerMenu != this.inventoryMenu){
            this.closeContainer();
        }
        this.nextContainerCounter();
        Services.PACKET_HELPER.sendBlackSabbathChestOpenPacket((ServerPlayer) (Object) this, $$1);
        this.containerMenu = new BlackSabbathPlayerInventoryMenu(this.getInventory(), this, this.containerCounter);
        this.initMenu(this.containerMenu);
    }
    public void roundabout$openD4CCloneInventory(D4CCloneEntity $$0, Container $$1) {
        ServerPlayer player = (ServerPlayer) (Object) this;
        if(this.containerMenu != this.inventoryMenu){
            this.closeContainer();
        }
        this.nextContainerCounter();
        Services.PACKET_HELPER.sendD4CCloneInventoryOpenPacket((ServerPlayer) (Object) this, $$0, $$1);
        this.containerMenu = new D4CCloneInventoryMenu(this.containerCounter, this.getInventory(), $$0, $$0.inventory);
        this.initMenu(this.containerMenu);
    }

    @Inject(method = "die", at = @At("HEAD"))
    public void roundabout$onServerPlayerDie(DamageSource $$0, CallbackInfo ci) {
        if (((StandUser) this).roundabout$getStandPowers() instanceof PowersDiverDown dd
                && dd.isTransferringDamage && dd.submergedTarget != null) {
            DamageSource customSource = ModDamageTypes.of(
                    this.level(),
                    ModDamageTypes.DIVER_REDIRECTION,
                    dd.submergedTarget
            );
            this.getCombatTracker().recordDamage(customSource, 0);
        }
        IPlayerEntity ipe = (IPlayerEntity) this;
        int loops = ipe.rdbt$getGerDeathLoops();
        if (loops > 0) {
            roundabout$cleanupGerScenario();
            ipe.rdbt$setGerDeathLoops(loops - 1);
            roundabout$gerScenarioStarted = false;
            roundabout$gerScenarioTicks = 0;
        }
    }

    @Unique
    private void roundabout$startGerDeathScenario(int index) {
        if (!(this.level() instanceof ServerLevel sl)) return;
        this.spawnInvulnerableTime = 0;
        this.invulnerableTime = 0;
        BlockPos base = this.blockPosition();

        switch (index) {
            // falling anvils
            case 0 -> {
                for (int i = 0; i < 4; i++) {
                    FallingBlockEntity anvil = FallingBlockEntity.fall(sl, base.above(30 + i * 2), Blocks.ANVIL.defaultBlockState());
                    anvil.setHurtsEntities(12.0F, 40);
                    anvil.dropItem = false;
                    PowerTypes.setPlaneOfExisting(anvil, (byte) 13);
                    roundabout$gerTempEntities.add(anvil);
                }
            }
            // fall damage
            case 1 -> {
                int surfaceY = sl.getHeight(Heightmap.Types.MOTION_BLOCKING, base.getX(), base.getZ());
                double dropY = Math.max(this.getY() + 50.0D, surfaceY + 45.0D);
                this.fallDistance = 0.0F;
                this.teleportTo(this.getX(), dropY, this.getZ());
            }
            // baby zombie (philza jumpscare)
            case 2 -> {
                Zombie brute = EntityType.ZOMBIE.create(sl);
                if (brute != null) {
                    brute.setBaby(true);
                    brute.moveTo(this.getX() + 1.5, this.getY(), this.getZ());
                    brute.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(15.0);
                    brute.setTarget(this);
                    PowerTypes.setPlaneOfExisting(brute, (byte) 13);
                    sl.addFreshEntity(brute);
                    roundabout$gerTempEntities.add(brute);
                }
            }
            // blaze jumpscare
            case 3 -> {
                this.setSecondsOnFire(10);
                for (int i = 0; i < 3; i++) {
                    double angle = (Math.PI * 2.0 / 3.0) * i;
                    Blaze blaze = EntityType.BLAZE.create(sl);
                    if (blaze != null) {
                        blaze.moveTo(this.getX() + Math.cos(angle) * 2.5, this.getY() + 1.0, this.getZ() + Math.sin(angle) * 2.5);
                        blaze.setTarget(this);
                        PowerTypes.setPlaneOfExisting(blaze, (byte) 13);
                        sl.addFreshEntity(blaze);
                        roundabout$gerTempEntities.add(blaze);
                    }
                }
            }
            // creeper jumpscare
            case 4 -> {
                for (int i = 0; i < 3; i++) {
                    double angle = (Math.PI * 2.0 / 3.0) * i;
                    Creeper creeper = new Creeper(EntityType.CREEPER, sl) {
                        @Override
                        protected boolean shouldDropLoot() {
                            return false;
                        }
                        @Override
                        public boolean shouldDropExperience() {
                            return false;
                        }
                    };
                    creeper.moveTo(this.getX() + Math.cos(angle) * 1.8, this.getY(), this.getZ() + Math.sin(angle) * 1.8);
                    creeper.ignite();
                    PowerTypes.setPlaneOfExisting(creeper, (byte) 13);
                    sl.addFreshEntity(creeper);
                    roundabout$gerTempEntities.add(creeper);
                }
            }
            // firing squad
            case 5 -> {
                for (int i = 0; i < 4; i++) {
                    double angle = (Math.PI / 2.0) * i;
                    Skeleton skel = EntityType.SKELETON.create(sl);
                    if (skel != null) {
                        skel.moveTo(this.getX() + Math.cos(angle) * 3.0, this.getY(), this.getZ() + Math.sin(angle) * 3.0);
                        ItemStack powerBow = new ItemStack(Items.BOW);
                        powerBow.enchant(Enchantments.POWER_ARROWS, 5);
                        skel.setItemSlot(EquipmentSlot.MAINHAND, powerBow);
                        PowerTypes.setPlaneOfExisting(skel, (byte) 13);
                        sl.addFreshEntity(skel);
                        roundabout$gerTempEntities.add(skel);
                    }
                }
            }
            // suffocation
            case 6 -> {
                // look at GravityScreenRenderer, it's how the overlay gets applied
                this.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 120, 0));
                this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 120, 5));
            }
            // explosion
            case 7 -> {
                this.playNotifySound(SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            // void
            case 8 -> {
                double belowBedrockY = sl.getMinBuildHeight() - 2.0D;
                this.teleportTo(this.getX(), belowBedrockY, this.getZ());
            }
            // lightning storm (in tick)
            case 9 -> {
                if (this.connection != null) {
                    this.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.START_RAINING, 0.0F));
                    this.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.RAIN_LEVEL_CHANGE, 1.0F));
                    this.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.THUNDER_LEVEL_CHANGE, 1.0F));
                }
            }
        }
    }

    @Unique
    private void roundabout$tickGerDeathScenario(int index, int ticks) {
        if (!(this.level() instanceof ServerLevel sl)) return;
        if (this.spawnInvulnerableTime > 0) {
            this.spawnInvulnerableTime = 0;
        }

        for (Entity e : new ArrayList<>(roundabout$gerTempEntities)) {
            if (!this.isAlive()) return;
            if (e == null || !e.isAlive()) {
                roundabout$gerTempEntities.remove(e);
                continue;
            }

            if (e.isOnFire()) {
                e.clearFire();
            }

            if (e instanceof FallingBlockEntity anvil) {
                double nextY = anvil.getY() + anvil.getDeltaMovement().y;
                BlockPos belowPos = BlockPos.containing(anvil.getX(), nextY - 0.05D, anvil.getZ());
                boolean hitsPlayer = anvil.getBoundingBox().expandTowards(0, anvil.getDeltaMovement().y, 0)
                        .intersects(this.getBoundingBox());
                boolean hitsGround = !sl.getBlockState(belowPos).getCollisionShape(sl, belowPos).isEmpty();

                if (hitsPlayer || hitsGround) {
                    sl.playSound(null, anvil.blockPosition(), SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 1.0F, 1.0F);
                    anvil.discard();
                    roundabout$gerTempEntities.remove(e);
                    if (hitsPlayer) {
                        this.invulnerableTime = 0;
                        this.hurt(this.damageSources().anvil(anvil), 12.0F);
                    }
                    continue;
                }
            }

            if (e instanceof Zombie brute && brute.distanceToSqr(this) <= 2.25D && ticks % 10 == 0) {
                brute.swing(InteractionHand.MAIN_HAND);
                this.invulnerableTime = 0;
                brute.doHurtTarget(this);
                continue;
            }

            if (e instanceof Skeleton skel && ticks % 12 == 0) {
                double dx = this.getX() - skel.getX();
                double dy = this.getY(0.5D) - skel.getEyeY();
                double dz = this.getZ() - skel.getZ();
                double horizDist = Math.sqrt(dx * dx + dz * dz);
                Arrow arrow = new Arrow(sl, skel) {
                    @Override
                    protected void onHitBlock(BlockHitResult result) {
                        this.discard();
                    }

                    @Override
                    protected void onHitEntity(EntityHitResult result) {
                        super.onHitEntity(result);
                        this.discard();
                    }
                };
                arrow.pickup = AbstractArrow.Pickup.DISALLOWED;
                arrow.setBaseDamage(8.0D);
                arrow.setEnchantmentEffectsFromEntity(skel, 1.0F);
                arrow.shoot(dx, dy + horizDist * 0.12D, dz, 2.2F, 0.0F);
                PowerTypes.setPlaneOfExisting(arrow, (byte) 13);
                skel.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F);
                sl.addFreshEntity(arrow);
            }

            if (e instanceof Blaze blaze && ticks % 8 == 0) {
                double dx = this.getX() - blaze.getX();
                double dy = this.getY(0.5D) - blaze.getY(0.5D);
                double dz = this.getZ() - blaze.getZ();

                SmallFireball fireball = new SmallFireball(sl, blaze, dx, dy, dz) {
                    @Override
                    protected void onHitBlock(BlockHitResult result) {
                        this.discard();
                    }

                    @Override
                    protected void onHitEntity(EntityHitResult result) {
                        if (result.getEntity() == PlayerEntityServer.this) {
                            PlayerEntityServer.this.invulnerableTime = 0;
                            PlayerEntityServer.this.hurt(this.damageSources().fireball(this, this.getOwner()), 4.0F);
                        }
                        this.discard();
                    }
                };
                fireball.setPos(blaze.getX(), blaze.getY(0.5D) + 0.2D, blaze.getZ());
                PowerTypes.setPlaneOfExisting(fireball, (byte) 13);
                blaze.playSound(SoundEvents.BLAZE_SHOOT, 1.0F, 1.0F);
                sl.addFreshEntity(fireball);
            }

            // creeper anti grief
            if (e instanceof Creeper creeper && creeper.getSwelling(0.0F) >= 0.9F) {
                double x = creeper.getX(), y = creeper.getY(), z = creeper.getZ();
                creeper.discard();
                roundabout$gerTempEntities.remove(e);
                sl.explode(null, x, y, z, 3.5F, Level.ExplosionInteraction.NONE);
                this.invulnerableTime = 0;
                this.hurt(this.damageSources().explosion(null, null), 30.0F);
            }
        }

        if (!this.isAlive()) return;

        if (index == 3) {
            if (this.isInWater()) {
                this.setSecondsOnFire(10);
                if (ticks % 10 == 0) {
                    this.invulnerableTime = 0;
                    this.hurt(this.damageSources().lava(), 6.0F);
                }
            }

            List<SmallFireball> fireballs = sl.getEntitiesOfClass(
                    SmallFireball.class, this.getBoundingBox().inflate(24.0D)
            );
            for (SmallFireball fireball : fireballs) {
                if (fireball.getClass() == SmallFireball.class
                        && fireball.getOwner() != null
                        && roundabout$gerTempEntities.contains(fireball.getOwner())) {
                    fireball.discard();
                }
            }
        }

        if (index == 6 && ticks % 10 == 0) {
            this.invulnerableTime = 0;
            this.hurt(this.damageSources().inWall(), 4.0F);
        }

        if (index == 7 && ticks == 50) {
            sl.explode(null, this.getX(), this.getY() - 0.5, this.getZ(), 6.0F, Level.ExplosionInteraction.NONE);
            this.invulnerableTime = 0;
            this.hurt(this.damageSources().explosion(null, null), 100.0F);
        }

        if (index == 9 && ticks % 10 == 1 && ticks > 100) {
            LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(sl);
            if (bolt != null) {
                bolt.moveTo(this.getX(), this.getY(), this.getZ());
                bolt.setVisualOnly(true);
                PowerTypes.setPlaneOfExisting(bolt, (byte) 13);
                sl.addFreshEntity(bolt);
                this.invulnerableTime = 0;
                this.hurt(this.damageSources().lightningBolt(), 12.0F);
            }
        }

        // make them FEEL the incoming heart attack
        if (ticks == 340) {
            this.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 80, 0, false, false));
            this.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 80, 0, false, false));
            sl.playSound(null, this.blockPosition(), SoundEvents.WARDEN_HEARTBEAT, SoundSource.PLAYERS, 1.5F, 1.2F);
        }
        // dead
        if (ticks >= 400) {
            sl.playSound(null, this.blockPosition(), SoundEvents.WARDEN_HEARTBEAT, SoundSource.PLAYERS, 2.0F, 0.6F);
            this.invulnerableTime = 0;
            this.hurt(this.damageSources().genericKill(), Float.MAX_VALUE);
        }
    }

    @Unique
    private void roundabout$cleanupGerScenario() {
        if (this.connection != null && this.level() instanceof ServerLevel sl) {
            this.connection.send(new ClientboundGameEventPacket(
                    sl.isRaining() ? ClientboundGameEventPacket.START_RAINING : ClientboundGameEventPacket.STOP_RAINING,
                    0.0F
            ));
            this.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.RAIN_LEVEL_CHANGE, sl.getRainLevel(1.0F)));
            this.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.THUNDER_LEVEL_CHANGE, sl.getThunderLevel(1.0F)));
        }
        for (Entity e : roundabout$gerTempEntities) {
            if (e != null && !e.isRemoved()) {
                e.discard();
            }
        }
        roundabout$gerTempEntities.clear();
    }
}