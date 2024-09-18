package net.hydra.jojomod.event.powers.stand.presets;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.entity.projectile.*;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.item.HarpoonItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.Options;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class BlockGrabPreset extends PunchingStand{
    public BlockGrabPreset(LivingEntity self) {
        super(self);
    }
    public BlockPos grabBlock = null;
    public int grabEntity = -1;

    public int freezeAttackInput = -1;



    public void throwObject(ItemStack item){
        ModPacketHandler.PACKET_ACCESS.syncSkillCooldownPacket(((ServerPlayer) this.getSelf()), PowerIndex.SKILL_2, 30);
        this.setCooldown(PowerIndex.SKILL_2, 30);
        if (item.getItem() instanceof ThrowablePotionItem) {
            ThrownPotion $$4 = new ThrownPotion(this.getSelf().level(), this.getSelf());
            $$4.setItem(item);
            $$4.shootFromRotation(this.getSelf(), this.getSelf().getXRot(), this.getSelf().getYRot(), -3.0F, 1.4F, 0.5F);
            this.getSelf().level().addFreshEntity($$4);
            this.getSelf().level().playSound(null, $$4, ModSounds.BLOCK_THROW_EVENT, SoundSource.PLAYERS, 1.0F, 1.3F);
        } else if (item.getItem() instanceof SnowballItem){
            Snowball $$4 = new Snowball(this.getSelf().level(), this.getSelf());
            $$4.setItem(item);
            $$4.shootFromRotation(this.getSelf(), this.getSelf().getXRot(), this.getSelf().getYRot(), -3.0F, 2F, 0.5F);
            this.getSelf().level().addFreshEntity($$4);
            this.getSelf().level().playSound(null, $$4, ModSounds.BLOCK_THROW_EVENT, SoundSource.PLAYERS, 1.0F, 1.3F);
        } else if (item.is(ModItems.KNIFE)){
            KnifeEntity $$7 = new KnifeEntity(this.getSelf().level(), this.getSelf(), item);
            $$7.shootFromRotation(this.getSelf(), this.getSelf().getXRot(), this.getSelf().getYRot(), -3.0F, 2.4F, 0.5F);
            this.getSelf().level().addFreshEntity($$7);
            this.getSelf().level().playSound(null, $$7, ModSounds.BLOCK_THROW_EVENT, SoundSource.PLAYERS, 1.0F, 1.3F);
        } else if (item.is(ModItems.KNIFE_BUNDLE)){
            for (int i = 0; i< 4; i++) {
                KnifeEntity $$7 = new KnifeEntity(this.getSelf().level(), this.getSelf(), item);
                $$7.shootFromRotationWithVariance(this.getSelf(), this.getSelf().getXRot(), this.getSelf().getYRot(), -3.0F, 2.4F, 0.5F);
                this.getSelf().level().addFreshEntity($$7);
                if (i ==0) {
                    this.getSelf().level().playSound(null, $$7, ModSounds.BLOCK_THROW_EVENT, SoundSource.PLAYERS, 1.0F, 1.3F);
                }
            }
        } else if (item.is(ModItems.GASOLINE_CAN)){
            GasolineCanEntity $$7 = new GasolineCanEntity(this.getSelf(),this.getSelf().level());
            $$7.shootFromRotation(this.getSelf(), this.getSelf().getXRot(), this.getSelf().getYRot(), -3.0F, 1.5F, 0.5F);
            this.getSelf().level().addFreshEntity($$7);
            this.getSelf().level().playSound(null, $$7, ModSounds.BLOCK_THROW_EVENT, SoundSource.PLAYERS, 1.0F, 1.3F);
        } else if (item.is(ModItems.MATCH)){
            MatchEntity $$7 = new MatchEntity(this.getSelf(),this.getSelf().level());
            $$7.shootFromRotation(this.getSelf(), this.getSelf().getXRot(), this.getSelf().getYRot(), -3.0F, 2.5F, 0.5F);
            this.getSelf().level().addFreshEntity($$7);
            this.getSelf().level().playSound(null, $$7, ModSounds.BLOCK_THROW_EVENT, SoundSource.PLAYERS, 1.0F, 1.3F);
        } else if (item.is(ModItems.MATCH_BUNDLE)){
            for (int i = 0; i< 4; i++) {
                MatchEntity $$7 = new MatchEntity(this.getSelf(),this.getSelf().level());
                $$7.shootFromRotationWithVariance(this.getSelf(), this.getSelf().getXRot(), this.getSelf().getYRot(), -3.0F, 2.5F, 0.5F);
                this.getSelf().level().addFreshEntity($$7);
                if (i ==0) {
                    this.getSelf().level().playSound(null, $$7, ModSounds.BLOCK_THROW_EVENT, SoundSource.PLAYERS, 1.0F, 1.3F);
                }
            }
        } else if (item.getItem() instanceof EggItem){
            ThrownEgg $$4 = new ThrownEgg(this.getSelf().level(), this.getSelf());
            $$4.setItem(item);
            $$4.shootFromRotation(this.getSelf(), this.getSelf().getXRot(), this.getSelf().getYRot(), -3.0F, 2F, 0.5F);
            this.getSelf().level().addFreshEntity($$4);
            this.getSelf().level().playSound(null, $$4, ModSounds.BLOCK_THROW_EVENT, SoundSource.PLAYERS, 1.0F, 1.3F);
        } else if (item.getItem() instanceof ExperienceBottleItem){
            ThrownExperienceBottle $$4 = new ThrownExperienceBottle(this.getSelf().level(), this.getSelf());
            $$4.setItem(item);
            $$4.shootFromRotation(this.getSelf(), this.getSelf().getXRot(), this.getSelf().getYRot(), -3.0F, 2F, 0.5F);
            this.getSelf().level().addFreshEntity($$4);
            this.getSelf().level().playSound(null, $$4, ModSounds.BLOCK_THROW_EVENT, SoundSource.PLAYERS, 1.0F, 1.3F);
        } else if (item.getItem() instanceof EnderpearlItem){
            ThrownEnderpearl $$4 = new ThrownEnderpearl(this.getSelf().level(), this.getSelf());
            if (this.getSelf() instanceof Player PE && ((Player) this.getSelf()).getCooldowns().isOnCooldown(item.getItem())){
                StandEntity standEntity = ((StandUser) this.getSelf()).getStand();
                if (standEntity != null) {
                    addItemLight(standEntity);
                }
            } else {
                if (this.getSelf() instanceof Player PE) {
                    PE.getCooldowns().addCooldown(item.getItem(), 20);
                }
                $$4.shootFromRotation(this.getSelf(), this.getSelf().getXRot(), this.getSelf().getYRot(), -0F, 1.8F, 0.5F);
                this.getSelf().level().addFreshEntity($$4);
                this.getSelf().level().playSound(null, $$4, ModSounds.BLOCK_THROW_EVENT, SoundSource.PLAYERS, 1.0F, 1.3F);
            }
        } else if (item.getItem() instanceof ArrowItem){
            ArrowItem $$10 = (ArrowItem) item.getItem();
            AbstractArrow $$11 = $$10.createArrow(this.getSelf().level(), item, this.getSelf());
            $$11.shootFromRotation(this.getSelf(), this.getSelf().getXRot(), this.getSelf().getYRot(), 0.0F, 3.0F, 1.0F);
            $$11.setCritArrow(true);
            this.getSelf().level().addFreshEntity($$11);
            this.getSelf().level().playSound(null, $$11, ModSounds.BLOCK_THROW_EVENT, SoundSource.PLAYERS, 1.0F, 1.3F);
        } else if (item.getItem() instanceof TridentItem){
            if (!item.hurt(1,this.getSelf().level().getRandom(),null)){
                ThrownTrident $$7 = new ThrownTrident(this.getSelf().level(), this.getSelf(), item);
                $$7.shootFromRotation(this.getSelf(), this.getSelf().getXRot(), this.getSelf().getYRot(), 0.0F, 3.0F, 0.5F);
                this.getSelf().level().addFreshEntity($$7);
                this.getSelf().level().playSound(null, $$7, ModSounds.BLOCK_THROW_EVENT, SoundSource.PLAYERS, 1.0F, 1.3F);
            }
        } else if (item.getItem() instanceof HarpoonItem){
            if (!item.hurt(1,this.getSelf().level().getRandom(),null)){
                HarpoonEntity $$7 = new HarpoonEntity(this.getSelf().level(), this.getSelf(), item);
                $$7.shootFromRotation(this.getSelf(), this.getSelf().getXRot(), this.getSelf().getYRot(), 0.0F, 3.0F, 0.5F);
                this.getSelf().level().addFreshEntity($$7);
                this.getSelf().level().playSound(null, $$7, ModSounds.BLOCK_THROW_EVENT, SoundSource.PLAYERS, 1.0F, 1.3F);
            }
        } else {
            boolean canPlace = getCanPlace();
            ThrownObjectEntity thrownBlockOrItem = new ThrownObjectEntity(this.getSelf(), this.getSelf().level(), item, canPlace);
            thrownBlockOrItem.shootFromRotation(this.getSelf(), this.getSelf().getXRot(),
                    this.getSelf().getYRot(), -0.5F, 1.7F, 0.8F);
            this.getSelf().level().addFreshEntity(thrownBlockOrItem);
            if (item.is(Items.IRON_NUGGET) || item.is(Items.GOLD_NUGGET) || item.is(Items.DIAMOND)|| item.is(Items.FLINT)) {
                this.getSelf().level().playSound(null, thrownBlockOrItem, ModSounds.BALL_BEARING_SHOT_EVENT, SoundSource.PLAYERS, 1.0F, 1F);
            } else {
                this.getSelf().level().playSound(null, thrownBlockOrItem, ModSounds.BLOCK_THROW_EVENT, SoundSource.PLAYERS, 1.0F, 1.3F);
            }
        }
    }

    public boolean getCanPlace(){
        boolean canPlace = false;
        boolean acq = false;
        StandEntity standEntity = ((StandUser) this.getSelf()).getStand();
        if (standEntity != null && standEntity.canAcquireHeldItem) {
            acq = true;
        }
        if (acq && !(this.getSelf() instanceof Player && ((ServerPlayer) this.getSelf()).gameMode.getGameModeForPlayer() == GameType.SPECTATOR)
                && !(this.getSelf() instanceof Player && ((ServerPlayer) this.getSelf()).gameMode.getGameModeForPlayer() == GameType.ADVENTURE)) {
            canPlace = true;
        }
        return canPlace;
    }

    /**Charge up Time Stop*/
    public boolean tryPosPower(int move, boolean forced, BlockPos blockPos){
        if (this.canChangePower(move, forced)){
            if (move == PowerIndex.POWER_2 || move == PowerIndex.POWER_2_BONUS) {
                this.grabBlock = blockPos;
                return tryPower(move, forced);
            }
        }
        return false;
        /*Return false in an override if you don't want to sync cooldowns, if for example you want a simple data update*/
    }

    public boolean hasBlock(){
        if (((StandUser) this.getSelf()).getStand() != null){
            if (!((StandUser) this.getSelf()).getStand().getHeldItem().isEmpty()){
                return true;
            }
        }
        return false;
    }
    public boolean hasEntity(){
        if (((StandUser) this.getSelf()).getStand() != null){
            if ((((StandUser) this.getSelf()).getStand().getFirstPassenger() != null)){
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean setPowerGuard(){
        if (this.getSelf() instanceof Player) {
            StandEntity standEntity = ((StandUser) this.getSelf()).getStand();
            if (standEntity != null && standEntity.isAlive() && !standEntity.isRemoved()) {
                if (!standEntity.getHeldItem().isEmpty()) {
                    if (!this.getSelf().level().isClientSide) {
                        if (standEntity.canAcquireHeldItem) {
                            if ((this.getActivePower() == PowerIndex.POWER_2 || this.getActivePower() == PowerIndex.POWER_2_SNEAK || this.getActivePower() == PowerIndex.POWER_2_SNEAK_EXTRA)
                                    && this.getAttackTimeDuring() < 10) {
                                return false;
                            }
                            this.addItem(standEntity);
                        }

                        if (this.getAnimation() == 34) {
                            animateStand((byte) 37);
                        } else {
                            animateStand((byte) 36);
                        }

                        standEntity.setHeldItem(ItemStack.EMPTY);
                        this.setAttackTimeDuring(-10);
                        this.syncCooldowns();
                        return true;
                    }
                    return false;
                } else if (standEntity.getFirstPassenger() != null){
                    MainUtil.ejectInFront(standEntity);
                    animateStand((byte) 36);
                    this.setAttackTimeDuring(-10);
                    this.setCooldown(PowerIndex.SKILL_2, 10);
                    this.syncCooldowns();
                    return true;
                }
            }
        }
        return super.setPowerGuard();
    }

    private int retractEndTIcks = -1;
    @Override
    public void tickPower(){
        super.tickPower();
        if (this.getSelf().isAlive() && !this.getSelf().isRemoved()) {
            StandEntity standEntity = ((StandUser) this.getSelf()).getStand();
            if (!this.getSelf().level().isClientSide) {
                if (standEntity != null && this.getActivePower() == PowerIndex.POWER_2_EXTRA &&
                        standEntity.getFirstPassenger() == null && this.getAttackTimeDuring() > -1){
                    ((StandUser)this.getSelf()).tryPower(PowerIndex.NONE, true);
                    animateStand((byte) 36);
                } else if (standEntity != null &&
                        (this.getActivePower() == PowerIndex.POWER_2_EXTRA ||
                                this.getActivePower() == PowerIndex.POWER_2 ||
                                this.getActivePower() == PowerIndex.POWER_2_SNEAK ||
                                this.getActivePower() == PowerIndex.POWER_2_SNEAK_EXTRA ||
                                this.getActivePower() == PowerIndex.POWER_2_BONUS
                        ) && !hasEntity() && !hasBlock()
                ) {
                    ((StandUser)this.getSelf()).tryPower(PowerIndex.NONE, true);
                    animateStand((byte) 0);
                }
            }
            if (this.getAnimation() == 36 || this.getAnimation() == 37) {
                retractEndTIcks++;
                if (retractEndTIcks > 8) {
                    animateStand((byte) 0);
                    retractEndTIcks = -1;
                }
            } else {
                retractEndTIcks = -1;
            }
            if (standEntity != null && this.getActivePower() == PowerIndex.POWER_2_EXTRA &&
                    standEntity.getFirstPassenger() != null && !(standEntity.getFirstPassenger() instanceof Player)){
                //clampRotation(standEntity.getFirstPassenger());
            }
        }
    }


    private float getGrabThrowStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return 3;
        } else {
            return 7;
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean setPowerAttack(){
        if (this.getSelf() instanceof Player) {
            StandEntity standEntity = ((StandUser) this.getSelf()).getStand();
            if (standEntity != null && standEntity.isAlive() && !standEntity.isRemoved()) {
                if (!standEntity.getHeldItem().isEmpty()) {
                    if (!this.getSelf().level().isClientSide) {
                        throwObject(standEntity.getHeldItem());
                        if (MainUtil.isThrownBlockItem(standEntity.getHeldItem().getItem())){
                            animateStand((byte) 33);
                        } else {
                            animateStand((byte) 35);
                        }
                        poseStand(OffsetIndex.FOLLOW);
                        standEntity.setHeldItem(ItemStack.EMPTY);
                        this.setAttackTimeDuring(-10);

                        return true;
                    }
                    return false;
                } else if (standEntity.getFirstPassenger() != null){
                    if (!this.getSelf().level().isClientSide) {
                        ModPacketHandler.PACKET_ACCESS.syncSkillCooldownPacket(((ServerPlayer) this.getSelf()), PowerIndex.SKILL_2, 30);
                        this.setCooldown(PowerIndex.SKILL_2, 30);
                        Entity ent = standEntity.getFirstPassenger();

                        Vec3 vec3d = this.getSelf().getEyePosition(0);
                        Vec3 vec3d2 = this.getSelf().getViewVector(0);
                        double width = (this.getSelf().getBbWidth()*0.6)+ent.getBbWidth()*1.8F;
                        double y = vec3d2.y;
                        if (this.getSelf() instanceof Player pl && pl.isCrouching()){
                            y-= (ent.getEyeHeight()*0.3);
                        }
                        Vec3 vec3d3 = vec3d.add(vec3d2.x * width, y * width, vec3d2.z * width);
                        standEntity.ejectPassengers();
                        boolean candoit = true;
                        for (var i = 0; i< ent.getBbHeight(); i++){
                            if (this.getSelf().level().getBlockState(new BlockPos(
                                    (int) vec3d3.x(), (int) (vec3d3.y+i),
                                    (int) vec3d3.z)).isSolid()){
                                candoit = false;
                                break;
                            }
                        }
                        Vec3 qVec2 = Vec3.ZERO;
                        if (candoit){
                            qVec2 = new Vec3(vec3d3.x,vec3d3.y,vec3d3.z);
                            ent.dismountTo(vec3d3.x,vec3d3.y,vec3d3.z);
                        } else {
                            qVec2 = new Vec3(this.getSelf().getX(),this.getSelf().getY(),this.getSelf().getZ());
                            ent.dismountTo(this.getSelf().getX(),this.getSelf().getY(),this.getSelf().getZ());
                        }

                        if (ent instanceof Player){
                            ((StandUser)ent).roundabout$setQVec2Params(qVec2);
                        }


                        int degrees = (int) (this.getSelf().getYRot() % 360);
                        int degreesY = (int) this.getSelf().getXRot();
                        float strength = 2.8F;
                        if (ent instanceof Player){
                            strength = 2.3F;
                        } else if (ent instanceof Boat){
                            strength = 6F;
                        } else if (ent instanceof Minecart){
                            strength = 4F;
                        }

                        float ybias = (90F - Math.abs(degreesY)) /90F;
                        if (this.getSelf() instanceof Player pl && pl.isCrouching()){
                            if (ent instanceof Player){
                                strength *= 0.8F;
                            } else {
                                strength *= 0.6F;
                            }
                            if (DamageHandler.PenetratingStandDamageEntity(ent, getGrabThrowStrength(ent), this.getSelf())){
                                if (ent instanceof LivingEntity le && (ent instanceof Player || ((TimeStop) this.getSelf().level()).CanTimeStopEntity(le))) {
                                    ((StandUser)le).roundabout$setQVec(new Vec3(Mth.sin(((degrees * ((float) Math.PI / 180)))),
                                            Mth.sin(degreesY * ((float) Math.PI / 180)),
                                            -Mth.cos((degrees * ((float) Math.PI / 180)))));
                                    ((StandUser)le).roundabout$setQVecParams(new Vec3(strength * (0.75 + (ybias / 4)),
                                            ybias,
                                            0F));
                                } else {
                                    MainUtil.takeUnresistableKnockbackWithYBias(ent, strength * (0.75 + (ybias / 4)),
                                            Mth.sin(((degrees * ((float) Math.PI / 180)))),
                                            Mth.sin(degreesY * ((float) Math.PI / 180)),
                                            -Mth.cos((degrees * ((float) Math.PI / 180))),
                                            ybias);
                                }
                                animateStand((byte) 3);
                                poseStand(OffsetIndex.ATTACK);
                                this.setAttackTimeDuring(-15);
                                this.getSelf().level().playSound(null, ent, ModSounds.PUNCH_4_SOUND_EVENT, SoundSource.PLAYERS, 1.0F, 1.18F);
                            } else {
                                animateStand((byte) 33);
                                poseStand(OffsetIndex.FOLLOW);
                                this.setAttackTimeDuring(-10);
                            }
                        } else {
                            if (ent instanceof Mob && ent.getBbHeight() < 1 && ent.getPassengers().isEmpty()){
                                ((StandUser)ent).roundabout$setThrower(this.getSelf());
                                ((StandUser)ent).roundabout$startAutoSpinAttack(20);
                            }
                            if (ent instanceof NeutralMob NE && !(ent instanceof Animal)){
                                NE.setTarget(this.getSelf());
                            }
                            this.getSelf().level().playSound(null, ent, ModSounds.BLOCK_THROW_EVENT, SoundSource.PLAYERS, 1.0F, 1.3F);

                            if (ent instanceof LivingEntity le && (ent instanceof Player || ((TimeStop) this.getSelf().level()).CanTimeStopEntity(le))) {
                                ((StandUser)le).roundabout$setQVec(new Vec3(Mth.sin(((degrees * ((float) Math.PI / 180)))),
                                        Mth.sin(degreesY * ((float) Math.PI / 180)),
                                        -Mth.cos((degrees * ((float) Math.PI / 180)))));
                                ((StandUser)le).roundabout$setQVecParams(new Vec3(strength * (0.5 + (ybias / 2)),
                                        ybias,
                                        0F));

                            } else {
                                MainUtil.takeUnresistableKnockbackWithYBias(ent, strength * (0.5 + (ybias / 2)),
                                        Mth.sin(((degrees * ((float) Math.PI / 180)))),
                                        Mth.sin(degreesY * ((float) Math.PI / 180)),
                                        -Mth.cos((degrees * ((float) Math.PI / 180))),
                                        ybias);
                            }
                            animateStand((byte) 33);
                            poseStand(OffsetIndex.FOLLOW);
                            this.setAttackTimeDuring(-10);
                        }

                        return true;
                    }
                    return false;
                }
            }
        }
        return super.setPowerAttack();
    }
    @Override
    public boolean setPowerOther(int move, int lastMove) {
        if (move == PowerIndex.POWER_2){
            return this.grab();
        } else if (move == PowerIndex.POWER_2_EXTRA){
            return this.mobGrab();
        } else if (move == PowerIndex.POWER_2_SNEAK_EXTRA){
            return this.inventoryGrab();
        } else if (move == PowerIndex.POWER_2_BONUS){
            return this.placeBlock();
        }
        return super.setPowerOther(move,lastMove);
    }
    /**Charge up Time Stop*/
    @Override
    public boolean tryChargedPower(int move, boolean forced, int chargeTime){
            if (move == PowerIndex.POWER_2_EXTRA){
                this.grabEntity = chargeTime;
            } else if (move == PowerIndex.POWER_2_SNEAK_EXTRA) {
                this.grabInventorySlot = chargeTime;
            }
            return super.tryChargedPower(move, forced, chargeTime);
    }
    @Override
    public boolean buttonInputGuard(boolean keyIsDown, Options options) {
        StandEntity standEntity = ((StandUser) this.getSelf()).getStand();
        if (standEntity != null && standEntity.isAlive() && !standEntity.isRemoved()) {
            if (!standEntity.getHeldItem().isEmpty() &&
                    (this.getActivePower() == PowerIndex.POWER_2 || this.getActivePower() == PowerIndex.POWER_2_SNEAK || this.getActivePower() == PowerIndex.POWER_2_SNEAK_EXTRA)
                    && this.getAttackTimeDuring() < 10) {
                return false;
            } else if (standEntity.getFirstPassenger() != null &&
                    (this.getActivePower() == PowerIndex.POWER_2 || this.getActivePower() == PowerIndex.POWER_2_SNEAK || this.getActivePower() == PowerIndex.POWER_2_SNEAK_EXTRA
                            || this.getActivePower() == PowerIndex.POWER_2_EXTRA)
                    && this.getAttackTimeDuring() < 3) {
                return false;
            }
        }
        if (!this.isGuarding() && !this.isBarraging() && !this.isClashing()) {
            ((StandUser)this.getSelf()).tryPower(PowerIndex.GUARD,true);
            ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.GUARD);
            return true;
        }
        return false;
    }
    @Override
    public boolean canInterruptPower(){
        StandEntity standEntity = ((StandUser) this.getSelf()).getStand();
        if (standEntity != null) {
            if (!standEntity.getHeldItem().isEmpty()) {
                if (!this.getSelf().level().isClientSide) {
                    ModPacketHandler.PACKET_ACCESS.syncSkillCooldownPacket(((ServerPlayer) this.getSelf()), PowerIndex.SKILL_2, 20);
                    this.setCooldown(PowerIndex.SKILL_2, 20);
                }
                return true;
            }
        }
        return super.canInterruptPower();
    }
    public int inputSpeedModifiers(int sprintTrigger){
        if (this.getSelf().level().isClientSide) {
            LocalPlayer local = ((LocalPlayer) this.getSelf());
            StandUser standUser = ((StandUser) this.getSelf());

            StandEntity standEntity = ((StandUser) this.getSelf()).getStand();
            if (standEntity != null) {
                if (!standEntity.getHeldItem().isEmpty()) {
                    local.input.leftImpulse *= 0.7f;
                    local.input.forwardImpulse *= 0.7f;
                } else if (standEntity.getFirstPassenger() != null){
                    local.input.leftImpulse *= 0.85f;
                    local.input.forwardImpulse *= 0.85f;
                }
            }
        }
        return super.inputSpeedModifiers(sprintTrigger);
    }


    @Override
    public boolean shouldReset(byte activeP){

        StandEntity standEntity = ((StandUser) this.getSelf()).getStand();
        if (standEntity != null && standEntity.isAlive() && !standEntity.isRemoved()) {
            if (!standEntity.getHeldItem().isEmpty() &&
                    (this.getActivePower() == PowerIndex.POWER_2 || this.getActivePower() == PowerIndex.POWER_2_SNEAK || this.getActivePower() == PowerIndex.POWER_2_SNEAK_EXTRA)
                    && this.getAttackTimeDuring() < 10) {
                return false;
            }
        }

        return super.shouldReset(activeP);
    }

    @Override
    public void buttonInputAttack(boolean keyIsDown, Options options) {
        StandEntity standEntity = ((StandUser) this.getSelf()).getStand();
        if (freezeAttackInput > -1) {
            this.freezeAttackInput = 1;
        }
        if (freezeAttackInput < 0) {
            if ( hasBlock() || hasEntity()) {
                this.freezeAttackInput = 1;
            }
            if (this.canAttack() || ((standEntity != null && standEntity.isAlive() && !standEntity.isRemoved())
                    && !standEntity.getHeldItem().isEmpty()) || hasEntity()) {

                if (standEntity != null && standEntity.isAlive() && !standEntity.isRemoved()) {
                    if (!standEntity.getHeldItem().isEmpty() &&
                            (this.getActivePower() == PowerIndex.POWER_2 || this.getActivePower() == PowerIndex.POWER_2_SNEAK || this.getActivePower() == PowerIndex.POWER_2_SNEAK_EXTRA)
                            && this.getAttackTimeDuring() < 10) {
                        return;
                    } else if (standEntity.getFirstPassenger() != null &&
                            (this.getActivePower() == PowerIndex.POWER_2_EXTRA)
                            && this.getAttackTimeDuring() < 3) {
                        return;
                    }
                }

                this.tryPower(PowerIndex.ATTACK, true);
                ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.ATTACK);
            }
        }
    }
    @Override
    public boolean cancelCollision(Entity et) {
        if (((StandUser) this.getSelf()).getStand() != null){
            if ((((StandUser) this.getSelf()).getStand().getFirstPassenger() != null &&
                    (((StandUser) this.getSelf()).getStand().getFirstPassenger().is(et)))) {
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean setPowerNone(){
        this.setAttackTimeDuring(-1);
        this.setActivePower(PowerIndex.NONE);
        poseStand(OffsetIndex.FOLLOW);
        if (this.getAnimation() != 32 && this.getAnimation() != 34 && this.getAnimation() != 38) {
            animateStand((byte) 0);
        }
        return true;
    }

    public boolean hold3 = false;
    /**Grab ability*/
    @Override
    public void buttonInput2(boolean keyIsDown, Options options) {
        if (this.getSelf().level().isClientSide && !this.isClashing()) {
            if (keyIsDown) {
                if (!hold3) {
                    hold3 = true;
                    if (!((TimeStop) this.getSelf().level()).CanTimeStopEntity(this.getSelf())) {
                        if (this.getActivePower() != PowerIndex.POWER_2
                                && (this.getActivePower() != PowerIndex.POWER_2_EXTRA || this.getAttackTimeDuring() < 0) && !hasEntity()
                                && (this.getActivePower() != PowerIndex.POWER_2_SNEAK || this.getAttackTimeDuring() < 0) && !hasBlock()) {
                            if (!this.onCooldown(PowerIndex.SKILL_2)) {
                                if (!options.keyShift.isDown()) {
                                    Entity targetEntity = MainUtil.getTargetEntity(this.getSelf(),2.1F);
                                    if (targetEntity != null) {
                                        if (canGrab(targetEntity)) {
                                            ((StandUser) this.getSelf()).tryPower(PowerIndex.POWER_2_EXTRA, true);
                                            ModPacketHandler.PACKET_ACCESS.StandChargedPowerPacket(PowerIndex.POWER_2_EXTRA, targetEntity.getId());
                                        }
                                    } else {
                                        //ModPacketHandler.PACKET_ACCESS.StandPosPowerPacket(PowerIndex.POWER_2, backwards);
                                        BlockHitResult HR = getGrabBlock();
                                        if (HR != null) {
                                            ((StandUser) this.getSelf()).tryPower(PowerIndex.POWER_2, true);
                                            ModPacketHandler.PACKET_ACCESS.StandPosPowerPacket(PowerIndex.POWER_2, HR.getBlockPos());
                                        }
                                    }
                                } else {
                                    ItemStack stack = this.getSelf().getMainHandItem();
                                    if (!stack.isEmpty()) {
                                        ((StandUser) this.getSelf()).tryChargedPower(PowerIndex.POWER_2_SNEAK_EXTRA, true,
                                                ((Player) this.getSelf()).getInventory().selected);
                                        ModPacketHandler.PACKET_ACCESS.StandChargedPowerPacket(PowerIndex.POWER_2_SNEAK_EXTRA,
                                                ((Player) this.getSelf()).getInventory().selected);
                                    }
                                }
                            }
                        } else {
                            if (hasBlock() || hasEntity()) {
                                ((StandUser) this.getSelf()).tryPower(PowerIndex.POWER_2_BONUS, true);
                                ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.POWER_2_BONUS);
                            }
                        }
                    }
                }
            } else {
                hold3 = false;
            }
        }
    }

    /**Block Pos ability*/
    public BlockHitResult getGrabBlock(){

        Vec3 vec3d = this.getSelf().getEyePosition(0);
        Vec3 vec3d2 = this.getSelf().getViewVector(0);
        Vec3 vec3d3 = vec3d.add(vec3d2.x * 5, vec3d2.y * 5, vec3d2.z * 5);
        BlockHitResult blockHit = this.getSelf().level().clip(new ClipContext(vec3d, vec3d3,
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.getSelf()));
        if (blockHit.getType() == HitResult.Type.BLOCK){
            return blockHit;
        }
        return null;
    }
    @Override
    public boolean tryPower(int move, boolean forced) {
        if (super.tryPower(move,forced)) {
            if (!this.getSelf().level().isClientSide) {
                StandEntity standEntity = ((StandUser) this.getSelf()).getStand();
                if (standEntity != null) {
                    if (!standEntity.getHeldItem().isEmpty() && move != PowerIndex.POWER_2 && move != PowerIndex.POWER_2_SNEAK
                            && move != PowerIndex.POWER_2_SNEAK_EXTRA) {
                        if (!MainUtil.isThrownBlockItem(standEntity.getHeldItem().getItem())) {
                            animateStand((byte) 37);
                        } else {
                            animateStand((byte) 36);
                        }
                        if (standEntity.canAcquireHeldItem) {
                            if ((this.getActivePower() == PowerIndex.POWER_2 || this.getActivePower() == PowerIndex.POWER_2_SNEAK || this.getActivePower() == PowerIndex.POWER_2_SNEAK_EXTRA)
                                    && this.getAttackTimeDuring() < 10) {
                                return false;
                            }
                            this.addItem(standEntity);
                            standEntity.setHeldItem(ItemStack.EMPTY);
                        }
                    } else if (standEntity.getFirstPassenger() != null && move != PowerIndex.POWER_2 && move != PowerIndex.POWER_2_SNEAK
                            && move != PowerIndex.POWER_2_SNEAK_EXTRA && move != PowerIndex.POWER_2_EXTRA){
                        MainUtil.ejectInFront(standEntity);
                        animateStand((byte) 36);
                    }
                }
            }
            return true;
        }
        return false;
    }
    public void addItem(StandEntity standEntity){
        addItemLight(standEntity);

        ModPacketHandler.PACKET_ACCESS.syncSkillCooldownPacket(((ServerPlayer) this.getSelf()), PowerIndex.SKILL_2, 10);
        this.setCooldown(PowerIndex.SKILL_2, 10);
    }
    public void addItemLight(StandEntity standEntity){
        if (canAddItem(standEntity.getHeldItem(), ((Player) this.getSelf()).getInventory())) {
            ((Player) this.getSelf()).addItem(standEntity.getHeldItem());
        } else {
            ItemEntity $$4 = new ItemEntity(this.getSelf().level(), this.getSelf().getX(),
                    this.getSelf().getY() + this.getSelf().getEyeHeight(), this.getSelf().getZ(),
                    standEntity.getHeldItem());
            $$4.setPickUpDelay(40);
            $$4.setThrower(this.getSelf().getUUID());
            this.getSelf().level().addFreshEntity($$4);
        }
    }

    public boolean canAddItem(ItemStack itemStack, Inventory inventory) {
        boolean bl = false;
        for (ItemStack itemStack2 : inventory.items) {
            if (!itemStack2.isEmpty() && (!ItemStack.isSameItemSameTags(itemStack2, itemStack) || itemStack2.getCount() >= itemStack2.getMaxStackSize())) continue;
            bl = true;
            break;
        }
        return bl;
    }

    @SuppressWarnings("deprecation")
    public boolean placeBlock() {
        if (!this.getSelf().level().isClientSide()) {
            if (this.hasEntity()) {
                StandEntity standEntity = ((StandUser) this.getSelf()).getStand();
                if (standEntity != null && standEntity.isAlive() && !standEntity.isRemoved()) {
                    if (standEntity.getFirstPassenger() != null) {
                        MainUtil.ejectInFront(standEntity);
                        animateStand((byte) 36);
                        this.setAttackTimeDuring(-10);
                        this.setCooldown(PowerIndex.SKILL_2, 10);
                        this.syncCooldowns();
                        return true;
                    }
                }
            } else {
                if (this.hasBlock() && this.attackTimeDuring >= 10) {
                    StandEntity standEntity = ((StandUser) this.getSelf()).getStand();
                    if (standEntity != null && standEntity.isAlive() && !standEntity.isRemoved()) {
                        Vec3 vec3d = this.getSelf().getEyePosition(0);
                        Vec3 vec3d2 = this.getSelf().getViewVector(0);
                        Vec3 vec3d3 = vec3d.add(vec3d2.x * 5, vec3d2.y * 5, vec3d2.z * 5);
                        BlockHitResult $$0 = this.getSelf().level().clip(new ClipContext(vec3d, vec3d3,
                                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.getSelf()));
                        BlockPos pos = null;
                        if (this.getSelf().level().getBlockState($$0.getBlockPos()).isSolid()){
                            pos = $$0.getBlockPos().relative($$0.getDirection());
                        }
                        if (pos != null) {
                            BlockState state = this.getSelf().level().getBlockState(pos);
                            if (standEntity.getHeldItem().getItem() instanceof BlockItem) {
                                if (getCanPlace()) {
                                    if (tryHitBlock($$0, pos, state, standEntity)) {
                                        standEntity.setHeldItem(ItemStack.EMPTY);

                                        ((StandUser)this.getSelf()).tryPower(PowerIndex.NONE, true);
                                        animateStand((byte) 36);
                                        ModPacketHandler.PACKET_ACCESS.syncSkillCooldownPacket(((ServerPlayer) this.getSelf()), PowerIndex.SKILL_2, 10);
                                        this.setCooldown(PowerIndex.SKILL_2, 10);
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean tryHitBlock(BlockHitResult $$0, BlockPos pos, BlockState state, StandEntity standEntity){

        if ((state.isAir() || state.canBeReplaced()) && !((this.getSelf() instanceof Player &&
                (((Player) this.getSelf()).blockActionRestricted(this.getSelf().level(), pos, ((ServerPlayer)
                        this.getSelf()).gameMode.getGameModeForPlayer()))) ||
                !this.getSelf().level().mayInteract(((Player) this.getSelf()), pos))){

            if (standEntity.getHeldItem().getItem() instanceof BlockItem) {
                Direction direction = $$0.getDirection();
                if (direction.getAxis() == Direction.Axis.X){
                    direction = direction.getOpposite();
                }
                if (((BlockItem) standEntity.getHeldItem().getItem()).getBlock() instanceof RotatedPillarBlock){
                    direction = $$0.getDirection();
                }

                if (((BlockItem)standEntity.getHeldItem().getItem()).place(new DirectionalPlaceContext(this.getSelf().level(),
                        pos,
                        direction, standEntity.getHeldItem(),
                        direction)) != InteractionResult.FAIL){
                    return true;
                }
            }

        }
        return false;
    }

    public boolean mobGrab() {
        if (!this.getSelf().level().isClientSide() && !this.hasBlock()) {
            StandEntity standEntity = ((StandUser) this.getSelf()).getStand();
            if (standEntity != null && standEntity.isAlive() && !standEntity.isRemoved()) {
                Entity entity = this.getSelf().level().getEntity(this.grabEntity);
                if (entity != null && this.canGrab(entity)) {
                    if (entity.startRiding(standEntity)) {
                        this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.BLOCK_GRAB_EVENT, SoundSource.PLAYERS, 20.0F, 1.3F);
                        this.setActivePower(PowerIndex.POWER_2_EXTRA);
                        this.setAttackTimeDuring(0);
                        poseStand(OffsetIndex.FOLLOW_NOLEAN);
                        animateStand((byte) 38);
                        return true;
                    }
                }
            }
        } else {
            this.setAttackTimeDuring(0);
            this.setActivePower(PowerIndex.POWER_2);
            return true;
        }
        this.setPowerNone();
        return false;
    }

    public boolean canGrab(Entity entity){
        if (!(entity instanceof EnderDragon) && !(entity instanceof WitherBoss) && !(entity instanceof Warden)
                && !(entity instanceof LivingEntity ent && ent.getHealth() > this.getSelf().getMaxHealth())
                && !(entity instanceof Player pl && pl.isCreative())
                && !(entity instanceof StandEntity)){
            return true;
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    public boolean grab() {
        if (!this.getSelf().level().isClientSide() && !this.hasEntity()) {
            StandEntity standEntity = ((StandUser) this.getSelf()).getStand();
            if (standEntity != null && standEntity.isAlive() && !standEntity.isRemoved()) {
                BlockState state = this.getSelf().level().getBlockState(this.grabBlock);
                if (this.grabBlock != null &&
                        grabBlock.distSqr(this.getSelf().getOnPos()) <= ServerGamePacketListenerImpl.MAX_INTERACTION_DISTANCE
                        && state.getBlock().isCollisionShapeFullBlock(state, this.getSelf().level(), this.grabBlock)
                        && state.getBlock().defaultDestroyTime() >= 0 && state.getBlock() != Blocks.NETHERITE_BLOCK) {

                    if (this.getSelf().level().getBlockEntity(this.grabBlock) == null) {
                        if ((this.getSelf() instanceof Player &&
                                !((Player) this.getSelf()).blockActionRestricted(this.getSelf().level(), this.grabBlock, ((ServerPlayer) this.getSelf()).gameMode.getGameModeForPlayer()))
                                && this.getSelf().level().mayInteract(((Player) this.getSelf()), this.grabBlock)) {
                            /*This is the code where blocks that are removable are grabbed*/
                            boolean $$4 = this.getSelf().level().removeBlock(this.grabBlock, false);
                            if ($$4) {
                                standEntity.canAcquireHeldItem = true;
                            } else {
                                standEntity.canAcquireHeldItem = false;
                            }
                        } else {
                            standEntity.canAcquireHeldItem = false;
                        }
                        standEntity.setHeldItem(state.getBlock().asItem().getDefaultInstance());
                        this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.BLOCK_GRAB_EVENT, SoundSource.PLAYERS, 20.0F, 1.3F);
                        this.setActivePower(PowerIndex.POWER_2_SNEAK);
                        this.setAttackTimeDuring(0);
                        poseStand(OffsetIndex.FOLLOW_NOLEAN);
                        animateStand((byte) 32);
                        if (state.is(Blocks.GRASS_BLOCK) || (this.getSelf().level().getBlockState(this.grabBlock.above()).is(state.getBlock()) ||
                                this.getSelf().level().getBlockState(this.grabBlock.below()).is(state.getBlock()) ||
                                this.getSelf().level().getBlockState(this.grabBlock.north()).is(state.getBlock()) ||
                                this.getSelf().level().getBlockState(this.grabBlock.south()).is(state.getBlock()) ||
                                this.getSelf().level().getBlockState(this.grabBlock.east()).is(state.getBlock()) ||
                                this.getSelf().level().getBlockState(this.grabBlock.west()).is(state.getBlock()) && !state.is(Blocks.PUMPKIN)
                                && !(state.getBlock() instanceof StemGrownBlock)&& !(state.getBlock() instanceof GlassBlock))){
                            if (!this.getSelf().level().isClientSide) {

                                BlockState state1 = state;
                               if (state.is(Blocks.GRASS_BLOCK)){
                                    state1 = Blocks.DIRT.defaultBlockState();
                               }
                                ((ServerLevel) this.getSelf().level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK,
                                                state1),
                                        this.grabBlock.getX()+0.5, this.grabBlock.getY()+0.5, this.grabBlock.getZ()+0.5,
                                        90, 0.4, 0.4, 0.4, 0.5);
                                SoundEvent SE = state.getSoundType().getBreakSound();
                                this.getSelf().level().playSound(null, this.getSelf().blockPosition(), SE, SoundSource.BLOCKS, 4.0F, 0.7F);
                            }
                        }


                        return true;
                    }
                }
            }
        } else {
            this.setAttackTimeDuring(0);
            this.setActivePower(PowerIndex.POWER_2);
            return true;
        }
        this.setPowerNone();
        return false;
    }

    public int grabInventorySlot=1;

    public boolean inventoryGrab() {
        if (!this.getSelf().level().isClientSide()) {
            StandEntity standEntity = ((StandUser) this.getSelf()).getStand();
            if (standEntity != null && standEntity.isAlive() && !standEntity.isRemoved() &&
                    this.getSelf() instanceof Player) {
                ItemStack stack = ((Player)this.getSelf()).getInventory().getItem(this.grabInventorySlot);
                if (!stack.isEmpty() && !(stack.getItem() instanceof BlockItem &&
                        ((BlockItem)stack.getItem()).getBlock() instanceof ShulkerBoxBlock)) {
                    standEntity.canAcquireHeldItem = true;
                    standEntity.setHeldItem(stack.copyWithCount(1));
                    this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.BLOCK_GRAB_EVENT, SoundSource.PLAYERS, 20.0F, 1.3F);
                    this.setActivePower(PowerIndex.POWER_2_SNEAK);
                    this.setAttackTimeDuring(0);
                    poseStand(OffsetIndex.FOLLOW_NOLEAN);
                    if (MainUtil.isThrownBlockItem(stack.getItem())){
                        animateStand((byte) 32);
                    } else {
                        animateStand((byte) 34);
                    }
                    stack.shrink(1);
                    return true;
                }
            }
        } else {
            this.setAttackTimeDuring(0);
            this.setActivePower(PowerIndex.POWER_2);
            return true;
        }
        this.setPowerNone();
        return false;
    }
}
