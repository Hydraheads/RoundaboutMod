package net.hydra.jojomod.event.powers.stand.presets;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.ILivingEntityAccess;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.KeyInputs;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModGamerules;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.TimeStopInstance;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.StandUserClient;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Objects;

public class TWAndSPSharedPowers extends BlockGrabPreset{
    public TWAndSPSharedPowers(LivingEntity self) {
        super(self);
    }
    public boolean impactBrace = false;

    public int impactSlowdown = -1;
    public int impactAirTime = -1;
    public int bonusLeapCount = -1;
    public int spacedJumpTime = -1;

    /**Indicates the standard max TS Time, for setting up bar length*/
    @Override
    public int getMaxTSTime (){
        return 100;
    }

    /*Change this value actively to manipulate how long a ts charge can be in ticks*/
    public int maxChargeTSTime = 100;
    @Override
    public int getMaxChargeTSTime(){
        return maxChargeTSTime;
    }


    @Override
    public void onStandSwitch(){
        this.resumeTime();
    }

    @Override
    public float getPickMiningSpeed() {
        return 12F;
    }
    @Override
    public float getAxeMiningSpeed() {
        return 8F;
    }
    @Override
    public float getSwordMiningSpeed() {
        return 8F;
    }
    @Override
    public float getShovelMiningSpeed() {
        return 8F;
    }

    public boolean inputDash = false;
    /**Dodge ability*/
    @Override
    public void buttonInput3(boolean keyIsDown, Options options) {
        if (keyIsDown) {
            if (!inputDash){
            inputDash = true;
            if (this.getSelf().level().isClientSide && !this.isClashing() && this.getActivePower() != PowerIndex.POWER_2
                    && (this.getActivePower() != PowerIndex.POWER_2_EXTRA || this.getAttackTimeDuring() < 0) && !hasEntity()
                    && (this.getActivePower() != PowerIndex.POWER_2_SNEAK || this.getAttackTimeDuring() < 0) && !hasBlock()) {
                if (!((TimeStop) this.getSelf().level()).CanTimeStopEntity(this.getSelf())) {
                    if (!isHoldingSneak()) {
                        if (((StandUser) this.getSelf()).roundabout$getLeapTicks() > -1) {
                            /*Stand leap rebounds*/
                            standRebound();
                        } else {
                            if (this.getSelf().onGround() && !this.onCooldown(PowerIndex.SKILL_3_SNEAK)) {
                                byte forward = 0;
                                byte strafe = 0;
                                if (options.keyUp.isDown()) forward++;
                                if (options.keyDown.isDown()) forward--;
                                if (options.keyLeft.isDown()) strafe++;
                                if (options.keyRight.isDown()) strafe--;
                                int degrees = (int) (this.getSelf().getYRot() % 360);
                                int backwards = 0;

                                if (strafe > 0 && forward == 0) {
                                    degrees -= 90;
                                    degrees = degrees % 360;
                                    backwards = 1;
                                } else if (strafe > 0 && forward > 0) {
                                    degrees -= 45;
                                    degrees = degrees % 360;
                                    backwards = 2;
                                } else if (strafe > 0) {
                                    degrees -= 135;
                                    degrees = degrees % 360;
                                    backwards = -1;
                                } else if (strafe < 0 && forward == 0) {
                                    degrees += 90;
                                    degrees = degrees % 360;
                                    backwards = 3;
                                } else if (strafe < 0 && forward > 0) {
                                    degrees += 45;
                                    degrees = degrees % 360;
                                    backwards = 4;
                                } else if (strafe < 0) {
                                    degrees += 135;
                                    degrees = degrees % 360;
                                    backwards = -2;
                                } else if (forward < 0) {
                                    degrees += 180;
                                    degrees = degrees % 360;
                                    backwards = -3;
                                }


                                int cdTime = ClientNetworking.getAppropriateConfig().cooldownsInTicks.dash;
                                if (this.getSelf() instanceof Player) {
                                    ((IPlayerEntity) this.getSelf()).roundabout$setClientDodgeTime(0);
                                    if (options.keyJump.isDown()) {
                                        cdTime = ClientNetworking.getAppropriateConfig().cooldownsInTicks.jumpingDash;
                                    }
                                }
                                this.setCooldown(PowerIndex.SKILL_3_SNEAK, cdTime);
                                MainUtil.takeUnresistableKnockbackWithY(this.getSelf(), 0.91F,
                                        Mth.sin(degrees * ((float) Math.PI / 180)),
                                        Mth.sin(-20 * ((float) Math.PI / 180)),
                                        -Mth.cos(degrees * ((float) Math.PI / 180)));

                                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.MOVEMENT, true);
                                ModPacketHandler.PACKET_ACCESS.StandChargedPowerPacket(PowerIndex.MOVEMENT, backwards);
                            } else {
                                if (!doVault() && this.getSelf().fallDistance > 3) {
                                    if ((this.getActivePower() != PowerIndex.EXTRA || this.getAttackTimeDuring() == -1)) {

                                        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.EXTRA, true);
                                        ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.EXTRA);
                                    }
                                }
                            }
                        }
                    } else {
                        if (this.getSelf().onGround()) {
                            boolean jojoveinLikeKeys = !ClientNetworking.getAppropriateConfig().cooldownsInTicks.standJumpAndDashShareCooldown;
                            if ((jojoveinLikeKeys && !this.onCooldown(PowerIndex.SKILL_3)) ||
                                    (!jojoveinLikeKeys && !this.onCooldown(PowerIndex.SKILL_3_SNEAK))) {
                                if (canExecuteMoveWithLevel(getLeapLevel())) {
                                    if (jojoveinLikeKeys) {
                                        this.setCooldown(PowerIndex.SKILL_3, ClientNetworking.getAppropriateConfig().cooldownsInTicks.standJump);
                                    } else {
                                        this.setCooldown(PowerIndex.SKILL_3_SNEAK, ClientNetworking.getAppropriateConfig().cooldownsInTicks.standJump);
                                    }
                                    bonusLeapCount = 3;
                                    bigLeap(this.getSelf(), 20, 1);
                                    ((StandUser) this.getSelf()).roundabout$setLeapTicks(((StandUser) this.getSelf()).roundabout$getMaxLeapTicks());
                                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.SNEAK_MOVEMENT, true);
                                    ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.SNEAK_MOVEMENT);
                                }
                            }
                        } else {
                            if (((StandUser) this.getSelf()).roundabout$getLeapTicks() > -1) {
                                /*Stand leap rebounds*/
                                standRebound();
                            } else {
                                if ((!doVault()) && this.getSelf().fallDistance > 3 && impactSlowdown <= -1) {
                                    if ((this.getActivePower() != PowerIndex.EXTRA || this.getAttackTimeDuring() == -1)) {

                                        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.EXTRA, true);
                                        ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.EXTRA);
                                    }
                                }
                            }
                        }

                        }
                    }
                }
            }
        } else {
            inputDash = false;
        }
    }

    /**Phase Grab*/
    public void buttonInput2(boolean keyIsDown, Options options) {
        if ((this.getActivePower() != PowerIndex.POWER_2_BLOCK)) {
            if (keyIsDown) {
                if (((!this.isBarrageAttacking() && this.getActivePower() != PowerIndex.BARRAGE_2) ||
                        this.getAttackTimeDuring() < 0) && this.isGuarding()) {
                    if (this.getSelf().level().isClientSide && !this.isClashing() && this.getActivePower() != PowerIndex.POWER_2
                            && (this.getActivePower() != PowerIndex.POWER_2_EXTRA || this.getAttackTimeDuring() < 0) && !hasEntity()
                            && (this.getActivePower() != PowerIndex.POWER_2_SNEAK || this.getAttackTimeDuring() < 0) && !hasBlock()) {
                        if (!this.onCooldown(PowerIndex.SKILL_2)) {
                            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_2_BLOCK, true);
                            ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.POWER_2_BLOCK);
                        }
                    }
                    return;
                }
            }
            super.buttonInput2(keyIsDown, options);
        }
    }


    public float getFloatOutRange(){
        return 7F;
    }

    public Vec3 moveVec = Vec3.ZERO;
    @SuppressWarnings("deprecation")
    @Override
    public void tickPowerEnd() {

        super.tickPowerEnd();
        if (this.getSelf().isAlive() && !this.getSelf().isRemoved()){
            if (this.forwardBarrage && this.attackTimeDuring >= 0) {
                /**Tick Forward Barrage*/
                if (this.getActivePower() == PowerIndex.BARRAGE || this.getActivePower() == PowerIndex.BARRAGE_2) {
                    if (!this.getSelf().level().isClientSide()) {
                        StandEntity stand = getStandEntity(this.self);
                        if (Objects.nonNull(stand)) {
                            if (moveStarted) {
                                stand.setPos(stand.getPosition(0).add(stand.getForward().scale(0.12)));
                            } else {
                                stand.setPos(stand.getPosition(0).add(stand.getForward().scale(0.0075)));
                            }
                            if ((stand.isTechnicallyInWall() && this.getActivePower() != PowerIndex.POWER_1_BONUS) ||
                                    stand.position().distanceTo(this.getSelf().position()) > getFloatOutRange()) {
                                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                            }
                        }
                    }
                }
            } else if (this.getActivePower() == PowerIndex.POWER_2_BLOCK) {
                /**Tick Phase Grab*/
                if (!this.getSelf().level().isClientSide()) {
                    if (this.attackTimeDuring == 108) {
                        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                    } else if (this.attackTimeDuring >= 0) {
                        StandEntity stand = getStandEntity(this.self);
                        if (Objects.nonNull(stand)) {
                            AABB BB1 = stand.getBoundingBox();
                            Vec3 vec3d = this.getSelf().getEyePosition(0);
                            Vec3 vec3d2 = this.getSelf().getViewVector(0);
                            Vec3 vec3d3 = vec3d.add(vec3d2.x * 20, vec3d2.y * 20, vec3d2.z * 20);
                            double mag = 0.18F;

                            moveVec = moveVec.add(
                                    vec3d3.subtract(this.getSelf().position().add(moveVec)).normalize().scale(mag)
                            );
                            Vec3 yes = this.getSelf().position().add(moveVec);
                            double post = stand.position().distanceTo(vec3d3);
                            if (post< 1.5){
                                stand.setYRot(this.getSelf().getYHeadRot() % 360);
                                stand.setXRot(this.getSelf().getXRot());
                            } else {
                                stand.setYRot(getLookAtPlaceYaw(stand,vec3d3));
                                stand.setXRot(getLookAtPlacePitch(stand,vec3d3));
                            }
                            if (post < 0.4){
                                stand.setPos(vec3d3);
                            } else {
                                stand.setPos(yes);
                            }

                            if ((stand.isTechnicallyInImpassableWall() && this.getActivePower() != PowerIndex.POWER_1_BONUS) ||
                                    stand.position().distanceTo(this.getSelf().position()) > 15){
                                ModPacketHandler.PACKET_ACCESS.syncSkillCooldownPacket(((ServerPlayer) this.getSelf()), PowerIndex.SKILL_2, 7);
                                this.setCooldown(PowerIndex.SKILL_2, 5);
                                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                                return;
                            }



                            Vec3 vec3dST = stand.getEyePosition(0);
                            Vec3 vec3d2ST = stand.getViewVector(0);
                            Vec3 vec3d3ST = vec3dST.add(vec3d2ST.x * 3, vec3d2ST.y * 3, vec3d2ST.z * 3);

                            if (this.getSelf().level().getGameRules().getBoolean(ModGamerules.ROUNDABOUT_STAND_REDSTONE_INTERFERENCE))
                            {
                                BlockHitResult blockHit = stand.level().clip(new ClipContext(vec3dST, vec3d3ST,
                                        ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, stand));
                                BlockPos bpos = blockHit.getBlockPos();
                                BlockState state = stand.level().getBlockState(bpos);
                                if (!state.isAir()) {
                                    Block blk = state.getBlock();
                                    if (((blk instanceof LeverBlock) || (blk instanceof ButtonBlock)
                                            || (blk instanceof DoorBlock) || (blk instanceof TrapDoorBlock)
                                            || (blk instanceof FenceGateBlock))
                                            && this.getSelf() instanceof Player PE) {
                                        blk.use(state, this.getSelf().level(), bpos, PE, PE.getUsedItemHand(), blockHit);
                                        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                                        return;
                                    }
                                }
                            }

                            AABB BB2 = stand.getBoundingBox();
                            if (this.attackTimeDuring > 2) {
                                tryPhaseItemGrab(stand, BB1, BB2);
                            }
                        }
                    }
                }
            }
        }
    }
    public void tryPhaseItemGrab(StandEntity stand, AABB bb1, AABB bb2){
        bb1 = bb1.inflate(1.6F);
        bb2 = bb2.inflate(1.6F);

        AABB $$2 = bb1.minmax(bb2);
        List<Entity> $$3 = stand.level().getEntities(stand, $$2);
        if (!$$3.isEmpty()) {
            for (int $$4 = 0; $$4 < $$3.size(); $$4++) {
                Entity $$5 = $$3.get($$4);
                if ($$5 instanceof ItemEntity IE && stand.getSensing().hasLineOfSight($$5)){
                    if (!(IE.getItem().getItem() instanceof BlockItem BI && BI.getBlock() instanceof ShulkerBoxBlock)){
                        stand.canAcquireHeldItem = true;
                        stand.setHeldItem(IE.getItem().copyWithCount(1));
                        this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.BLOCK_GRAB_EVENT, SoundSource.PLAYERS, 1.7F, 1.3F);
                        this.setActivePower(PowerIndex.POWER_2_SNEAK);
                        this.setAttackTimeDuring(0);
                        poseStand(OffsetIndex.FOLLOW_NOLEAN);
                        if (MainUtil.isThrownBlockItem(IE.getItem().getItem())) {
                            animateStand((byte) 32);
                        } else {
                            animateStand((byte) 34);
                        }

                        IE.getItem().shrink(1);
                        itemNearby(IE.getId());
                        return;
                    }
                }
            }
        }
    }

    public final void itemNearby(int id) {
        if (!this.self.level().isClientSide) {
            ServerLevel serverWorld = ((ServerLevel) this.self.level());
            Vec3 userLocation = new Vec3(this.self.getX(),  this.self.getY(), this.self.getZ());
            for (int j = 0; j < serverWorld.players().size(); ++j) {
                ServerPlayer serverPlayerEntity = ((ServerLevel) this.self.level()).players().get(j);

                if (((ServerLevel) serverPlayerEntity.level()) != serverWorld) {
                    continue;
                }

                BlockPos blockPos = serverPlayerEntity.blockPosition();
                if (blockPos.closerToCenterThan(userLocation, 100)) {
                    ModPacketHandler.PACKET_ACCESS.sendIntPacket(serverPlayerEntity, PacketDataIndex.S2C_INT_GRAB_ITEM,id);
                }
            }
        }
    }
    public boolean holdDownClick = false;

    @Override
    public void buttonInputAttack(boolean keyIsDown, Options options) {
        if (!consumeClickInput) {
            if (holdDownClick) {
                if (keyIsDown) {

                } else {
                    if (this.getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE) {
                        int atd = this.getAttackTimeDuring();
                        this.tryChargedPower(PowerIndex.SNEAK_ATTACK, true, atd);
                        ModPacketHandler.PACKET_ACCESS.StandChargedPowerPacket(PowerIndex.SNEAK_ATTACK, atd);
                    }
                    holdDownClick = false;
                }
            } else {
                if (keyIsDown) {
                    Minecraft mc = Minecraft.getInstance();
                    if (!isHoldingSneak()) {
                        super.buttonInputAttack(keyIsDown, options);
                    } else {
                        if (this.canAttack()) {
                            this.tryPower(PowerIndex.SNEAK_ATTACK_CHARGE, true);
                            holdDownClick = true;
                            ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.SNEAK_ATTACK_CHARGE);
                        } else {
                            super.buttonInputAttack(keyIsDown, options);
                        }
                    }
                }
            }
        } else {
            if (!keyIsDown) {
                consumeClickInput = false;
            }
        }
    }

    /**Begin Charging Time Stop, also detects activation via release**/
    @Override
    public void buttonInput4(boolean keyIsDown, Options options) {
        if (this.getSelf().level().isClientSide) {
            if (!this.onCooldown(PowerIndex.SKILL_4) || (((Player)this.getSelf()).isCreative() && ClientNetworking.getAppropriateConfig().timeStopSettings.creativeModeInfiniteTimeStop)) {
                if ((((TimeStop)this.getSelf().level()).CanTimeStopEntity(this.getSelf()) || !this.isAttackInept(this.getActivePower()))) {
                    boolean exTS = canExecuteMoveWithLevel(getTSLevel());
                    boolean exImpTS = canExecuteMoveWithLevel(getImpulseTSLevel());
                    boolean sendPacket = false;
                    if (KeyInputs.roundaboutClickCount == 0) {
                        if (keyIsDown) {
                            if (this.isStoppingTime()) {
                                KeyInputs.roundaboutClickCount = 2;
                                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.SPECIAL_FINISH, true);
                                ModPacketHandler.PACKET_ACCESS.StandChargedPowerPacket(PowerIndex.SPECIAL_FINISH, this.getChargedTSTicks());
                            } else if (this.getActivePower() == PowerIndex.SPECIAL || (this.getSelf() instanceof Player && (((Player)this.getSelf()).isCreative() && ClientNetworking.getAppropriateConfig().timeStopSettings.creativeModeInfiniteTimeStop))) {
                                sendPacket = true;
                            } else {
                                KeyInputs.roundaboutClickCount = 2;
                                if (isHoldingSneak() || (!exTS &&
                                        exImpTS)) {
                                    if (exImpTS) {
                                        this.setChargedTSTicks(ClientNetworking.getAppropriateConfig().timeStopSettings.impulseTimeStopLength);
                                        this.setMaxChargeTSTime(ClientNetworking.getAppropriateConfig().timeStopSettings.impulseTimeStopLength);
                                        sendPacket = true;
                                    }
                                } else {
                                    if (this.getAttackTimeDuring() < 0) {
                                        if (exTS) {
                                            this.setMaxChargeTSTime(this.getMaxTSTime());
                                            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.SPECIAL, true);
                                            ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.SPECIAL);
                                            this.updateUniqueMoves();
                                        }
                                    }
                                }
                            }
                        }

                    } else {
                        if (keyIsDown) {
                            KeyInputs.roundaboutClickCount = 2;
                        }
                    }

                    if (sendPacket) {
                        KeyInputs.roundaboutClickCount = 2;
                        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.SPECIAL_CHARGED, true);
                        ModPacketHandler.PACKET_ACCESS.StandChargedPowerPacket(PowerIndex.SPECIAL_CHARGED, this.getChargedTSTicks());
                    }
                }
            }
        }
    }

    public int chargedFinal;

    public void updateImpale(){
        if (this.attackTimeDuring > -1) {
            if (this.attackTimeDuring > 24) {
               this.standImpale();
            } else {
                if (!this.getSelf().level().isClientSide()) {
                    if(this.attackTimeDuring%4==0) {
                        ((ServerLevel) this.getSelf().level()).sendParticles(ModParticles.MENACING,
                                this.getSelf().getX(), this.getSelf().getY() + 0.3, this.getSelf().getZ(),
                                1, 0.2, 0.2, 0.2, 0.05);
                    }
                }
            }
        }
    }

    @Override
    public void handleStandAttack(Player player, Entity target){
        if (this.getActivePower() == PowerIndex.POWER_1_SNEAK){
            impaleImpact(target);
        } else if (this.getActivePower() == PowerIndex.SNEAK_ATTACK){
            finalAttackImpact(target);
        }
    }

    public void standImpale(){
        /*By setting this to -10, there is a delay between the stand retracting*/

        if (this.self instanceof Player){
            if (isPacketPlayer()){
                this.setAttackTimeDuring(-20);
                ModPacketHandler.PACKET_ACCESS.intToServerPacket(getTargetEntityId2(3), PacketDataIndex.INT_STAND_ATTACK);
            }
        } else {
            /*Caps how far out the punch goes*/
            Entity targetEntity = getTargetEntity(this.self,3);
            impaleImpact(targetEntity);
        }

    }

    public float getImpalePunchStrength(Entity entity){
        return 0;
    }
    public float getImpaleKnockback(){
        return 1.3F;
    }
    public void impaleImpact(Entity entity){
        this.setAttackTimeDuring(-20);
        if (entity != null) {
            float pow;
            float knockbackStrength;
            pow = getImpalePunchStrength(entity);
            knockbackStrength = getImpaleKnockback();
            if (StandDamageEntityAttack(entity, pow, 0, this.self)) {
                if (entity instanceof LivingEntity LE) {
                    addEXP(5, LE);
                    if (MainUtil.getMobBleed(entity)) {
                        if ((((TimeStop)this.getSelf().level()).CanTimeStopEntity(entity))) {
                            MainUtil.makeBleed(entity, 2, 100, this.getSelf());
                        } else {
                            MainUtil.makeBleed(entity, 2, 200, this.getSelf());
                        }
                            MainUtil.makeMobBleed(entity);
                    }
                }
                this.takeDeterminedKnockback(this.self, entity, knockbackStrength);
            } else {
                knockShield2(entity, 100);
            }
        }

        if (this.getSelf() instanceof Player) {
            ModPacketHandler.PACKET_ACCESS.syncSkillCooldownPacket(((ServerPlayer) this.getSelf()), PowerIndex.SKILL_1_SNEAK, 40);
        }
        this.setCooldown(PowerIndex.SKILL_1_SNEAK, ClientNetworking.getAppropriateConfig().cooldownsInTicks.impaleAttack);
        SoundEvent SE;
        float pitch = 1F;
            if (entity != null) {
                SE = ModSounds.IMPALE_HIT_EVENT;
                pitch = 1.2F;
            } else {
                SE = ModSounds.PUNCH_2_SOUND_EVENT;
            }

        if (!this.self.level().isClientSide()) {
            this.self.level().playSound(null, this.self.blockPosition(), SE, SoundSource.PLAYERS, 0.95F, pitch);
        }
    }

    public SoundEvent getFinalAttackSound(){
        return null;
    }

    @Override
    public void updateMovesFromPacket(byte activePower){
        if (activePower == PowerIndex.BARRAGE_2){
            this.setActivePowerPhase(this.activePowerPhaseMax);
        }
        super.updateMovesFromPacket(activePower);
    }

    @Override
    public boolean buttonInputGuard(boolean keyIsDown, Options options) {
        if (this.activePower == PowerIndex.BARRAGE_CHARGE_2 || this.activePower == PowerIndex.BARRAGE_2 || this.activePower == PowerIndex.POWER_2_BLOCK) {
            return false;
        }
        return super.buttonInputGuard(keyIsDown,options);
    }

    @Override
    public byte getMaxLevel(){
        return 7;
    }

    /**Stand related things that slow you down or speed you up*/
    public float inputSpeedModifiers(float basis){
        if (this.activePower == PowerIndex.BARRAGE_CHARGE_2) {
            basis*=0.5f;
        } else if (this.activePower == PowerIndex.BARRAGE_2) {
            basis *= 0.3f;
        } else if (this.activePower == PowerIndex.SNEAK_ATTACK_CHARGE) {
            if (this.getSelf().isCrouching()) {
                float f = Mth.clamp(0.3F + EnchantmentHelper.getSneakingSpeedBonus(this.getSelf()), 0.0F, 1.0F);
                float g = 1 / f;
                basis *= g;
            }
            basis *= 0.3f;
        } else if (this.getActivePower()==PowerIndex.POWER_1_SNEAK){
            if (this.getSelf().isCrouching()){
                float f = Mth.clamp(0.3F + EnchantmentHelper.getSneakingSpeedBonus(this.getSelf()), 0.0F, 1.0F);
                float g = 1/f;
                basis *= g;
            }
        }

            StandUser standUser = ((StandUser) this.getSelf());
            if (standUser.roundabout$getTSJump()) {
                if (this.getSelf().isCrouching()) {
                    basis *= 1.1f;
                } else {
                    basis *= 0.85f;
                }
            } else if (this.getActivePower() == PowerIndex.SPECIAL) {
                basis *= 0.48f;
            } else if (impactSlowdown > -1) {
                basis = 0f;
            }
        return super.inputSpeedModifiers(basis);
    }



    @Override
    public boolean getIsTsCharging(){
        if (this.getActivePower() == PowerIndex.SPECIAL) {
            return true;
        }
        return false;
    }
    /**Tick through dash*/

    public byte getTimeResumeNoise(){
        return TIME_RESUME_NOISE;
    }
    public boolean resumeTime() {
        /*Time Resume*/
        if (!this.getSelf().level().isClientSide()) {
            if (((TimeStop) this.getSelf().level()).isTimeStoppingEntity(this.getSelf())) {
                //this.maxChargedTSTicks = this.getChargedTSTicks() + this.setCurrentMaxTSTime(this.getChargedTSTicks());
                /*Roundabout.LOGGER.info("maxChargedTSTicks: "+this.maxChargedTSTicks+" MaxChargedTSTime: "+this.maxChargeTSTime+
                        " chargedtsticks: "+this.getChargedTSTicks()+" maxtst"+this.getMaxTSTime());
                        */
                float tsTimeRemaining = (float) (ClientNetworking.getAppropriateConfig().cooldownsInTicks.timeStopMinimum+((this.maxChargedTSTicks-this.getChargedTSTicks())*5*(ClientNetworking.getAppropriateConfig().cooldownsInTicks.timeStopTimeUsedMultiplier*0.01)));
                if ((this.getActivePower() == PowerIndex.ATTACK || this.getActivePower() == PowerIndex.POWER_1_SNEAK ||
                        this.getActivePower() == PowerIndex.SNEAK_ATTACK ||
                        this.getActivePower() == PowerIndex.POWER_1) && this.getAttackTimeDuring() > -1){
                    this.hasActedInTS = true;
                }
                if (this.hasActedInTS){
                    tsTimeRemaining+=ClientNetworking.getAppropriateConfig().cooldownsInTicks.timeStopActionBonusTicks;
                    this.hasActedInTS = false;
                }

                int sendTSCooldown = Math.round(tsTimeRemaining);
                if (!(this.getSelf() instanceof Player && (((Player)this.getSelf()).isCreative() && ClientNetworking.getAppropriateConfig().timeStopSettings.creativeModeInfiniteTimeStop))) {
                    if (this.getSelf() instanceof Player) {
                        ModPacketHandler.PACKET_ACCESS.syncSkillCooldownPacket(((ServerPlayer) this.getSelf()), PowerIndex.SKILL_4, sendTSCooldown);
                    }
                    this.setCooldown(PowerIndex.SKILL_4, sendTSCooldown);
                }

                ((TimeStop) this.getSelf().level()).removeTimeStoppingEntity(this.getSelf());
                stopSoundsIfNearby(SoundIndex.TIME_SOUND_GROUP, 200,true);
                stopSoundsIfNearby(SoundIndex.TIME_SOUND_GROUP, 200,false);
                if (this.getSelf() instanceof Player) {
                    ModPacketHandler.PACKET_ACCESS.sendIntPowerPacket(((ServerPlayer) this.getSelf()), PowerIndex.SPECIAL_FINISH, 0);
                }

                if (!(((TimeStop)this.getSelf().level()).CanTimeStopEntity(this.getSelf()))) {
                    if (this.getMaxChargeTSTime() > 20) {
                        this.playSoundsIfNearby(getTimeResumeNoise(), 100, true);
                    }
                }
            }
        }
        this.setChargedTSTicks(0);
        if (this.isBarraging()) {
            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
        }
        return true;
    }
    @Override
    public boolean setPowerSpecial(int lastMove) {

        this.setMaxChargeTSTime(this.getMaxTSTime());
        this.setAttackTimeDuring(0);
        this.setChargedTSTicks(20);

        this.setActivePower(PowerIndex.SPECIAL);
        poseStand(OffsetIndex.GUARD);
        animateStand((byte) 30);
        if (!(((TimeStop)this.getSelf().level()).CanTimeStopEntity(this.getSelf())) && this.getMaxChargeTSTime() >= 100) {
            playSoundsIfNearby(getTSVoice(), 100, false);
        }
        playSoundsIfNearby(TIME_STOP_CHARGE, 100, true);
        return true;
    }



    @Override
    public boolean setPowerSneakMovement(int lastMove) {

        this.setAttackTimeDuring(-1);
        this.setActivePower(PowerIndex.NONE);
        poseStand(OffsetIndex.FOLLOW);
        animateStand((byte) 17);
        if (this.getSelf() instanceof Player) {
            cancelConsumableItem(this.getSelf());
        }
        if (!this.getSelf().level().isClientSide()) {
            if (Math.random() > 0.5){
                addEXP(1);
            }
            ((StandUser) this.getSelf()).roundabout$setLeapTicks(((StandUser) this.getSelf()).roundabout$getMaxLeapTicks());
            this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.STAND_LEAP_EVENT, SoundSource.PLAYERS, 2.3F, (float) (0.98 + (Math.random() * 0.04)));
        }
        return true;
    }
    public byte getTSVoice(){
        double rand = Math.random();
        if (rand > 0.6){
            return TIME_STOP_VOICE;
        } else {
            return TIME_STOP_VOICE_2;
        }
    }

    @Override
    public boolean isAttackInept(byte activeP){
        if (activeP == PowerIndex.SPECIAL){
            return false;
        }
        return super.isAttackInept(activeP);
    }




    @Override
    public void tickPower(){
        super.tickPower();
        if (this.getSelf().isAlive() && !this.getSelf().isRemoved()) {
            if (this.forwardBarrage && !(this.isBarrageAttacking() || this.getActivePower() == PowerIndex.BARRAGE_2)){
                this.forwardBarrage = false;
            }
            if (impactSlowdown > -1){
                impactSlowdown--;
            }
            if (freezeAttackInput > -1){
                freezeAttackInput--;
            }

            if (spacedJumpTime > -1){
                spacedJumpTime--;
            }

            if (this.getAnimation() == 18) {
                leapEndTicks++;
                if (leapEndTicks > 4) {
                    animateStand((byte) 0);
                    leapEndTicks = -1;
                }
            } else {
                leapEndTicks = -1;
            }

            if (impactBrace){
                if (((StandUser) this.getSelf()).roundabout$getActive()){
                    if (this.getSelf().onGround()) {
                        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.EXTRA_FINISH, true);
                        if (this.getSelf().level().isClientSide && this.isPacketPlayer()) {
                            ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.EXTRA_FINISH);
                        }
                    }else if (this.getSelf().isInWater() || this.getSelf().hasEffect(MobEffects.LEVITATION)){
                        impactSlowdown = -1;
                        impactBrace = false;
                        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                        if (this.getSelf().level().isClientSide && this.isPacketPlayer()) {
                            ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.NONE);
                        }
                    } else {
                        if (impactAirTime > -1){
                            impactAirTime--;
                        }
                        impactSlowdown = 15;
                        if (impactAirTime > -1 || this.getSelf().tickCount % 2 == 0){
                            this.getSelf().fallDistance -= 1;
                            if (this.getSelf().fallDistance < 0){
                                this.getSelf().fallDistance = 0;
                            }
                        }
                    }
                } else {
                    impactSlowdown = -1;
                    impactBrace = false;
                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                    if (this.getSelf().level().isClientSide && this.isPacketPlayer()) {
                        ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.NONE);
                    }
                }
            }

            if (this.getSelf().onGround()){
                if (((StandUser)this.getSelf()).roundabout$getLeapTicks() <= -1) {
                    if (this.getAnimation() == 17) {
                        animateStand((byte) 18);
                    }
                }
            }
        }
    }

    @Override
    public void preButtonInput4(boolean keyIsDown, Options options){
        if (hasStandActive(this.getSelf()) && !this.isClashing()) {
            buttonInput4(keyIsDown, options);
        }
    }


    /**Assault Ability*/
    public boolean hold1 = false;
    @Override
    public void buttonInput1(boolean keyIsDown, Options options) {
        if (this.isBarrageAttacking() || this.getActivePower() == PowerIndex.BARRAGE_2){
            if (keyIsDown && !forwardBarrage) {
                hold1 = true;
                forwardBarrage = true;
                ModPacketHandler.PACKET_ACCESS.singleByteToServerPacket(PacketDataIndex.SINGLE_BYTE_FORWARD_BARRAGE);
                return;
            }
        }

        if (this.getSelf().level().isClientSide && !this.isClashing() && this.getActivePower() != PowerIndex.POWER_2
                && (this.getActivePower() != PowerIndex.POWER_2_EXTRA || this.getAttackTimeDuring() < 0) && !hasEntity()
                && (this.getActivePower() != PowerIndex.POWER_2_SNEAK || this.getAttackTimeDuring() < 0) && !hasBlock()) {
                if (isHoldingSneak()) {
                    if (keyIsDown) {
                        if (!hold1) {
                            hold1 = true;
                            if (!this.onCooldown(PowerIndex.SKILL_1_SNEAK)) {
                                if (canExecuteMoveWithLevel(getImpaleLevel())) {
                                    if (this.activePower == PowerIndex.POWER_1_SNEAK) {
                                        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                                        ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.NONE);
                                    } else {
                                        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_1_SNEAK, true);
                                        ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.POWER_1_SNEAK);
                                    }
                                }
                            }
                        }
                    } else {
                        hold1 = false;
                    }
                }
        }
    }
    public int getImpaleLevel(){
        return 5;
    }
    public int getLeapLevel(){
        return 3;
    }
    public int getImpulseTSLevel(){
        return 6;
    }
    public int getTSLevel(){
        return 6;
    }

    public int getMaxTSFactorLevel(){
        return 7;
    }
    public boolean bounce() {
        this.setActivePower(PowerIndex.BOUNCE);
        this.setAttackTimeDuring(-7);
        this.getSelf().resetFallDistance();
        if (!this.getSelf().level().isClientSide()) {
            ((StandUser) this.getSelf()).roundabout$setLeapTicks(((StandUser) this.getSelf()).roundabout$getMaxLeapTicks());
            this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.DODGE_EVENT, SoundSource.PLAYERS, 1.5F, (float) (0.5 + (Math.random() * 0.04)));
        }
        return true;
    }

    public boolean fallBraceInit() {
        this.getSelf().fallDistance -= 20;
        if (this.getSelf().fallDistance < 0){
            this.getSelf().fallDistance = 0;
        }
        impactBrace = true;
        impactAirTime = 15;

        animateStand((byte) 10);
        this.setAttackTimeDuring(0);
        this.setActivePower(PowerIndex.EXTRA);
        this.poseStand(OffsetIndex.BENEATH);
        if (!this.getSelf().level().isClientSide()) {
            this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.STAND_LEAP_EVENT, SoundSource.PLAYERS, 2.3F, (float) (0.78 + (Math.random() * 0.04)));
        }
        return true;
    }
    public boolean vault() {
        animateStand((byte) 15);
        this.poseStand(OffsetIndex.GUARD);
        cancelConsumableItem(this.getSelf());
        this.setAttackTimeDuring(-7);
        this.setActivePower(PowerIndex.VAULT);
        this.getSelf().resetFallDistance();
        if (!this.getSelf().level().isClientSide()) {
            if (Math.random() > 0.85){
                addEXP(1);
            }
            this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.DODGE_EVENT, SoundSource.PLAYERS, 1.5F, (float) (0.8 + (Math.random() * 0.04)));

        }
        return true;
    }
    public boolean fallBrace() {
        if (this.getActivePower() == PowerIndex.EXTRA && this.attackTimeDuring >= 0) {
            impactBrace = false;

            cancelConsumableItem(this.getSelf());
            this.setAttackTimeDuring(-15);
            if (!this.getSelf().level().isClientSide()) {
                ((ServerLevel) this.getSelf().level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, this.getSelf().level().getBlockState(this.getSelf().getOnPos())),
                        this.getSelf().getX(), this.getSelf().getOnPos().getY() + 1.1, this.getSelf().getZ(),
                        50, 1.1, 0.05, 1.1, 0.4);
                ((ServerLevel) this.getSelf().level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, this.getSelf().level().getBlockState(this.getSelf().getOnPos())),
                        this.getSelf().getX(), this.getSelf().getOnPos().getY() + 1.1, this.getSelf().getZ(),
                        30, 1, 0.05, 1, 0.4);
                this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.FALL_BRACE_EVENT, SoundSource.PLAYERS, 1.0F, (float) (0.98 + (Math.random() * 0.04)));
                int degrees = (int) (this.getSelf().getYRot() % 360);
                MainUtil.takeUnresistableKnockbackWithY(this.getSelf(), 1.2F,
                        Mth.sin(degrees * ((float) Math.PI / 180)),
                        Mth.sin(-12 * ((float) Math.PI / 180)),
                        -Mth.cos(degrees * ((float) Math.PI / 180)));
            }
        }
        return true;
    }


    @Override
    public void updateUniqueMoves() {
        /*Tick through Time Stop Charge*/
        if (this.getActivePower() == PowerIndex.SPECIAL) {
            updateTSCharge();
        } else if (this.getActivePower() == PowerIndex.BARRAGE_CHARGE_2) {
                updateKickBarrageCharge();
        } else if (this.getActivePower() == PowerIndex.BARRAGE_2) {
            updateKickBarrage();
        } else if (this.getActivePower() == PowerIndex.POWER_1_SNEAK){
            updateImpale();
        } else if (this.getActivePower() == PowerIndex.SNEAK_ATTACK){
            updateFinalAttack();
        } else if (this.getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE){
            updateFinalAttackCharge();
        } else if (this.getActivePower() == PowerIndex.POWER_2_BLOCK){
        }
        super.updateUniqueMoves();
    }

    public void updateTSCharge(){
        int TSChargeSeconds = this.getChargedTSTicks();
        TSChargeSeconds += ClientNetworking.getAppropriateConfig().timeStopSettings.rateOfChargingTimeStop;
        if (TSChargeSeconds >= this.getMaxChargeTSTime()) {
            TSChargeSeconds = this.getMaxChargeTSTime();
            this.setChargedTSTicks(TSChargeSeconds);
            if (this.getSelf().level().isClientSide) {
                if (isPacketPlayer()) {
                    ModPacketHandler.PACKET_ACCESS.StandChargedPowerPacket(PowerIndex.SPECIAL_CHARGED, TSChargeSeconds);
                }
            } else {
                if (this.getSelf() instanceof ServerPlayer) {
                    ModPacketHandler.PACKET_ACCESS.sendIntPowerPacket(((ServerPlayer) this.getSelf()), PowerIndex.SPECIAL_CHARGED, TSChargeSeconds);
                }
            }
            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.SPECIAL_CHARGED, true);
        } else {
            this.setChargedTSTicks(TSChargeSeconds);
        }
    }


    public void updateFinalAttackCharge(){
        if (this.attackTimeDuring > -1) {
            if (this.attackTimeDuring >= 80) {
                if (this.getSelf() instanceof Player && this.getSelf().level().isClientSide && this.isPacketPlayer()){
                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                    ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.NONE);
                }
            } else if (this.attackTimeDuring >= maxSuperHitTime && !(this.getSelf() instanceof Player)){
                ((StandUser) this.getSelf()).roundabout$tryChargedPower(PowerIndex.SNEAK_ATTACK, true,maxSuperHitTime);
            }
        }
    }

    public void updateFinalAttack(){
        if (this.attackTimeDuring > -1) {
            if (this.attackTimeDuring == 5) {
                this.standFinalAttack();
            }
        }
    }

    public void standFinalAttack(){

        this.setAttackTimeMax(ClientNetworking.getAppropriateConfig().cooldownsInTicks.finalPunchAndKickMinimum + chargedFinal);
        this.setAttackTime(0);
        this.setActivePowerPhase(this.getActivePowerPhaseMax());

        if (this.self instanceof Player){
            if (isPacketPlayer()){
                //Roundabout.LOGGER.info("Time: "+this.self.getWorld().getTime()+" ATD: "+this.attackTimeDuring+" APP"+this.activePowerPhase);
                this.attackTimeDuring = -10;
                ModPacketHandler.PACKET_ACCESS.intToServerPacket(getTargetEntityId(), PacketDataIndex.INT_STAND_ATTACK);
            }
        } else {
            /*Caps how far out the punch goes*/
            Entity targetEntity = getTargetEntity(this.self,-1);
            finalAttackImpact(targetEntity);
        }
    }

    @Override
    public void standBarrageHit(){
        StandEntity stand = getStandEntity(this.self);
        if (this.self instanceof Player){
            if (isPacketPlayer()){
                if (forwardBarrage && Objects.nonNull(stand)) {
                    ModPacketHandler.PACKET_ACCESS.StandBarrageHitPacket(getTargetEntityId2(2.7F,stand,50), this.attackTimeDuring);
                } else {
                    List<Entity> listE = getTargetEntityList(this.self,-1);
                    int id = -1;
                    if (storeEnt != null){
                        id = storeEnt.getId();
                    }
                        ModPacketHandler.PACKET_ACCESS.StandBarrageHitPacket(id, this.attackTimeDuring);
                    if (!listE.isEmpty() && ClientNetworking.getAppropriateConfig().barrageHasAreaOfEffect){
                        for (int i = 0; i< listE.size(); i++){
                            if (!(storeEnt != null && listE.get(i).is(storeEnt))) {
                                if (!(listE.get(i) instanceof StandEntity) && listE.get(i).distanceTo(this.self) < 3.5) {
                                    ModPacketHandler.PACKET_ACCESS.StandBarrageHitPacket(listE.get(i).getId(), this.attackTimeDuring + 1000);
                                }
                            }
                        }
                    }
                }

                if (this.activePower == PowerIndex.BARRAGE_2 && this.attackTimeDuring == this.getKickBarrageLength()){
                    this.attackTimeDuring = -10;
                } else if (this.isBarraging() && this.attackTimeDuring == this.getBarrageLength()){
                    this.attackTimeDuring = -10;
                }
            }
        } else {
            /*Caps how far out the barrage hit goes*/
            Entity targetEntity;
            if (forwardBarrage && Objects.nonNull(stand)) {
                targetEntity = getTargetEntityGenerous(stand,2.7F,50);
                barrageImpact(targetEntity, this.attackTimeDuring);
            } else {
                List<Entity> listE = getTargetEntityList(this.self,-1);
                    barrageImpact(storeEnt, this.attackTimeDuring);
                if (!listE.isEmpty()){
                    for (int i = 0; i< listE.size(); i++){
                        if (!(storeEnt != null && listE.get(i).is(storeEnt))) {
                            if (!(listE.get(i) instanceof StandEntity) && listE.get(i).distanceTo(this.self) < 3.5) {
                                barrageImpact(listE.get(i), this.attackTimeDuring + 1000);
                            }
                        }
                    }
                }
            }
        }
    }
    @Override
    public void barrageImpact(Entity entity, int hitNumber){
        if (entity != null && moveStarted){
            moveStarted = false;

            StandEntity stand = getStandEntity(this.self);
            if (Objects.nonNull(stand)){
                stand.setXRot(getLookAtEntityPitch(stand, entity));
                stand.setYRot(getLookAtEntityYaw(stand, entity));
            }
        }

        if (this.activePower == PowerIndex.BARRAGE_2) {
            if (bonusBarrageConditions()) {
                boolean sideHit = false;
                if (hitNumber > 1000){
                    if (!(ClientNetworking.getAppropriateConfig().barrageHasAreaOfEffect)){
                        return;
                    }
                    hitNumber-=1000;
                    sideHit = true;
                }
                boolean lastHit = (hitNumber >= this.getKickBarrageLength());
                if (entity != null) {
                    if (entity instanceof LivingEntity && ((StandUser) entity).roundabout$isBarraging()
                            && ((StandUser) entity).roundabout$getAttackTimeDuring() > -1 && !(((TimeStop)this.getSelf().level()).CanTimeStopEntity(entity))) {
                        initiateClash(entity);
                    } else {
                        float pow;
                        float knockbackStrength = 0;
                        /**By saving the velocity before hitting, we can let people approach barraging foes
                         * through shields.*/
                        Vec3 prevVelocity = entity.getDeltaMovement();
                        if (lastHit) {
                            pow = this.getKickBarrageFinisherStrength(entity);
                            knockbackStrength = this.getKickBarrageFinisherKnockback();
                        } else {
                            pow = this.getKickBarrageHitStrength(entity);
                            float mn = this.getKickBarrageLength() - hitNumber;
                            if (mn == 0) {
                                mn = 0.015F;
                            } else {
                                mn = ((0.015F / (mn)));
                            }
                            knockbackStrength = 0.014F - mn;
                        }

                        if (sideHit){
                            pow/=3;
                            knockbackStrength/=3;
                        }

                        if (StandDamageEntityAttack(entity, pow, 0.0001F, this.self)) {
                            if (entity instanceof LivingEntity) {
                                if (lastHit) {
                                    if (entity instanceof Player PE){
                                        ((IPlayerEntity) PE).roundabout$setCameraHits(-1);
                                    }
                                    if (!sideHit) {
                                        playBarrageEndNoise(0, entity);
                                    }
                                } else {
                                    if (entity instanceof Player PE){
                                        ((IPlayerEntity) PE).roundabout$setCameraHits(2);
                                    }
                                    if (!sideHit) {
                                        playKickBarrageNoise(hitNumber, entity);
                                    }
                                }
                            }
                            kickBarrageImpact2(entity, lastHit, knockbackStrength);
                        } else {
                            if (lastHit) {

                                if (!sideHit) {
                                    playBarrageBlockEndNoise(0, entity);
                                }
                            } else {
                                entity.setDeltaMovement(prevVelocity);
                            }
                        }
                    }
                } else {

                    if (!sideHit) {
                        playBarrageMissNoise(hitNumber);
                    }
                }

                if (lastHit) {
                    animateStand((byte) 43);
                    this.attackTimeDuring = -10;
                }
            } else {
                ((StandUser) this.self).roundabout$tryPower(PowerIndex.NONE, true);
            }
        } else {
            super.barrageImpact(entity,hitNumber);
        }
    }


    public void kickBarrageImpact2(Entity entity, boolean lastHit, float knockbackStrength){
        if (entity instanceof LivingEntity){
            if (lastHit) {
                this.takeDeterminedKnockbackWithY(this.self, entity, knockbackStrength);
            } else {

                this.takeKnockbackWithY(entity, knockbackStrength,
                        Mth.sin(this.getSelf().getYRot() * ((float) Math.PI / 180)),
                        Mth.sin(-15 * ((float) Math.PI / 180)),
                        -Mth.cos(this.getSelf().getYRot() * ((float) Math.PI / 180)));
            }
        }
    }


    public float getKickBarrageFinisherStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return 2F;
        } else {
            return 7;
        }
    }
    private float getKickBarrageHitStrength(Entity entity){
        float barrageLength = this.getKickBarrageLength();
        float power;
        if (this.getReducedDamage(entity)){
            power = 7/barrageLength;
        } else {
            power = 15/barrageLength;
        }
        /*Barrage hits are incapable of killing their target until the last hit.*/
        if (entity instanceof LivingEntity){
            if (power >= ((LivingEntity) entity).getHealth()){
                if (entity instanceof Player) {
                    power = 0.00001F;
                } else {
                    power = 0F;
                }
            }
        }
        return power;
    }
    public float getKickBarrageFinisherKnockback(){
        return 1.4F;
    }

    public void playKickBarrageNoise(int hitNumber, Entity entity){
        if (!this.getSelf().level().isClientSide()) {
            if (!this.getSelf().level().isClientSide() && (((TimeStop)this.getSelf().level()).CanTimeStopEntity(entity))) {
                playBarrageBlockNoise();
            } else {
                if (hitNumber % 3 == 0) {
                    this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.STAND_BARRAGE_HIT2_EVENT, SoundSource.PLAYERS, 0.9F, (float) (0.9 + (Math.random() * 0.25)));
                }
            }
        }
    }
    @Override
    public void buttonInputBarrage(boolean keyIsDown, Options options){
        if (keyIsDown) {
            if (isHoldingSneak() && (this.getAttackTime() >= this.getAttackTimeMax() ||
                    (this.getActivePowerPhase() != this.getActivePowerPhaseMax()))) {
                this.tryPower(PowerIndex.BARRAGE_CHARGE_2, true);
                ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.BARRAGE_CHARGE_2);
            } else {
                super.buttonInputBarrage(keyIsDown, options);
            }
        }
    }
    private int leapEndTicks = -1;

    @Override
    public boolean canInterruptPower(){

        if (this.getActivePower() == PowerIndex.BARRAGE_CHARGE_2) {
            return true;
        } else if (this.getActivePower() == PowerIndex.SPECIAL) {
            int cdr = ClientNetworking.getAppropriateConfig().cooldownsInTicks.timeStopInterrupt;
            if (this.getSelf() instanceof Player) {
                ModPacketHandler.PACKET_ACCESS.syncSkillCooldownPacket(((ServerPlayer) this.getSelf()), PowerIndex.SKILL_4, cdr);
            }
            this.setCooldown(PowerIndex.SKILL_4, cdr);
            return true;
        } else if (this.getActivePower() == PowerIndex.POWER_1_SNEAK){
            int cdr = ClientNetworking.getAppropriateConfig().cooldownsInTicks.impaleAttack;
            if (this.getSelf() instanceof Player) {
                ModPacketHandler.PACKET_ACCESS.syncSkillCooldownPacket(((ServerPlayer) this.getSelf()), PowerIndex.SKILL_1_SNEAK, cdr);
            }
            this.setCooldown(PowerIndex.SKILL_1_SNEAK, cdr);
            return true;
        } else {
            return super.canInterruptPower();
        }
    }
    @Override
    public byte getSoundCancelingGroupByte(byte soundChoice) {
        if (soundChoice >= TIME_STOP_CHARGE && soundChoice <= TIME_STOP_VOICE_3) {
            return SoundIndex.TIME_CHARGE_SOUND_GROUP;
        } else if (soundChoice >= TIME_STOP_NOISE && soundChoice <= TIME_STOP_ENDING_NOISE_2) {
            return SoundIndex.TIME_SOUND_GROUP;
        } else if (soundChoice >= BARRAGE_NOISE && soundChoice <= BARRAGE_NOISE_8) {
            return SoundIndex.BARRAGE_SOUND_GROUP;
        } else if (soundChoice == SoundIndex.ALT_CHARGE_SOUND_1
                || soundChoice == KICK_BARRAGE_NOISE_2 || soundChoice == KICK_BARRAGE_NOISE
                || soundChoice == KICK_BARRAGE_NOISE_3|| soundChoice == KICK_BARRAGE_NOISE_4){
            return SoundIndex.BARRAGE_SOUND_GROUP;
        } else {
            return super.getSoundCancelingGroupByte(soundChoice);
        }
    }



    @Override
    public boolean cancelItemUse() {
        return (this.getActivePower() == PowerIndex.BARRAGE_CHARGE_2 || this.getActivePower() == PowerIndex.BARRAGE_2);
    }

    @Override
    public boolean clickRelease(){
        return (this.getActivePower() == PowerIndex.BARRAGE_CHARGE_2 || this.getActivePower() == PowerIndex.BARRAGE_2 || this.getActivePower() == PowerIndex.POWER_2_BLOCK);
    }
    @Override
    public boolean cancelSprintJump(){
        if (this.getActivePower() == PowerIndex.BARRAGE_CHARGE_2 || this.getActivePower() == PowerIndex.BARRAGE_2
                || this.getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE){
            return true;
        }
        return super.cancelSprintJump();
    }


    /**The version of the above function to call at the end of a timestop. Used to calculate additional TS seconds*/
    public int setCurrentMaxTSTime(int chargedTSSeconds){
        return 0;
    }

    public int maxChargedTSTicks = 20;

    /**Charge up Time Stop*/
    @Override
    public boolean tryChargedPower(int move, boolean forced, int chargeTime){
        if (this.canChangePower(move, forced) && this.getActivePower() != PowerIndex.POWER_2
                && (this.getActivePower() != PowerIndex.POWER_2_SNEAK || this.getAttackTimeDuring() < 0)  && !hasBlock()) {
            if (move == PowerIndex.SPECIAL_CHARGED) {
                if (this.getSelf().level().isClientSide() ||
                        !((TimeStop) this.getSelf().level()).isTimeStoppingEntity(this.getSelf())) {
                    this.setChargedTSTicks(chargeTime);
                }
                super.tryChargedPower(move, forced, chargeTime);
            } else if (move == PowerIndex.SPECIAL_FINISH) {
                /*If the server is behind on the client TS time, update it to lower*/
            }else if (move == PowerIndex.SNEAK_ATTACK) {
                this.chargedFinal = chargeTime;
            }
            return super.tryChargedPower(move, forced, chargeTime);
        }
        return false;
    }

    @Override
    public boolean setPowerOther(int move, int lastMove) {
        if (move == PowerIndex.SPECIAL_FINISH) {
            return this.resumeTime();
        } else if (move == PowerIndex.SPECIAL_CHARGED) {
            return this.stopTime();
        } else if (move == PowerIndex.BARRAGE_CHARGE_2) {
            return this.setPowerKickBarrageCharge();
        } else if (move == PowerIndex.EXTRA){
            return this.fallBraceInit();
        } else if (move == PowerIndex.EXTRA_FINISH){
            return this.fallBrace();
        } else if (move == PowerIndex.VAULT){
            return this.vault();
        } else if (move == PowerIndex.BOUNCE){
            return this.bounce();
        } else if (move == PowerIndex.POWER_1_SNEAK){
            return this.impale();
        } else if (move == PowerIndex.SNEAK_ATTACK_CHARGE){
            return this.setPowerFinalAttack();
        } else if (move == PowerIndex.SNEAK_ATTACK){
            return this.setPowerSuperHit();
        } else if (move == PowerIndex.BARRAGE_2) {
            return this.setPowerKickBarrage();
        } else if (move == PowerIndex.POWER_2_BLOCK) {
            return this.phaseGrab();
        }
        return super.setPowerOther(move,lastMove);
    }

    public void playKickBarrageChargeSound(){
        if (!timeStopStartedBarrage) {
            if (!this.self.level().isClientSide()) {
                SoundEvent barrageChargeSound = this.getBarrageChargeSound();
                if (barrageChargeSound != null) {
                    playSoundsIfNearby(SoundIndex.ALT_CHARGE_SOUND_1, 27, false);
                }
            }
        }
    }

    public boolean phaseGrab() {
        StandEntity stand = getStandEntity(this.self);
        if (Objects.nonNull(stand)) {
            animateStand((byte) 87);
            this.attackTimeDuring = 0;
            stand.setFadePercent(50);
            this.setActivePower(PowerIndex.POWER_2_BLOCK);
            this.poseStand(OffsetIndex.LOOSE);
            stand.setYRot(this.getSelf().getYHeadRot() % 360);
            stand.setXRot(this.getSelf().getXRot());
            moveVec = DamageHandler.getRotationVector(
                    this.getSelf().getXRot(), (float) (this.getSelf().getYRot())).scale(1.8).add(0, 0.25, 0);
            stand.setPos(this.getSelf().position().add(moveVec));
            return true;
        }
        return false;
    }
    public boolean setPowerKickBarrageCharge() {
        animateStand((byte) 42);
        this.attackTimeDuring = 0;
        playKickBarrageChargeSound();
        this.setActivePower(PowerIndex.BARRAGE_CHARGE_2);
        this.poseStand(OffsetIndex.ATTACK);
        return true;
    }

    @Override
    public void renderAttackHud(GuiGraphics context, Player playerEntity,
                                int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                float flashAlpha, float otherFlashAlpha) {
        StandUser standUser = ((StandUser) playerEntity);
        boolean standOn = standUser.roundabout$getActive();
        int j = scaledHeight / 2 - 7 - 4;
        int k = scaledWidth / 2 - 8;
        if (standOn && this.getActivePower() == PowerIndex.BARRAGE_2 && attackTimeDuring > -1) {
            int ClashTime = 15 - Math.round(((float) attackTimeDuring / this.getKickBarrageLength()) * 15);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 30, ClashTime, 6);
        } else if (this.getActivePower() == PowerIndex.POWER_1_SNEAK){
            Entity TE = this.getTargetEntity(playerEntity, 3F);
            if (TE != null) {
                context.blit(StandIcons.JOJO_ICONS, k, j, 193, 0, 15, 6);
            }
        } else if (standOn && this.getActivePower() == PowerIndex.BARRAGE_CHARGE_2) {
            int ClashTime = Math.round(((float) attackTimeDuring / this.getKickBarrageWindup()) * 15);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 30, ClashTime, 6);
        } else if (standOn && this.getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE){
            int ClashTime = Math.min(15,Math.round(((float) attackTimeDuring / maxSuperHitTime) * 15));
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 30, ClashTime, 6);
        } else {
            super.renderAttackHud(context,playerEntity,
                    scaledWidth,scaledHeight,ticks,vehicleHeartCount, flashAlpha, otherFlashAlpha);
        }
    }

    public int getKickBarrageRecoilTime(){
        return 35;
    }
    public void updateKickBarrage(){
        if (this.attackTimeDuring == -2 && this.getSelf() instanceof Player) {
            ((StandUser) this.self).roundabout$tryPower(PowerIndex.GUARD, true);
        } else {
            if (this.attackTimeDuring > this.getKickBarrageLength()) {
                this.attackTimeDuring = -20;
            } else {
                if (this.attackTimeDuring > 0) {
                    this.setAttackTime((getKickBarrageRecoilTime() - 1) -
                            Math.round(((float) this.attackTimeDuring / this.getKickBarrageLength())
                                    * (getKickBarrageRecoilTime() - 1)));

                    standBarrageHit();
                }
            }
        }
    }


    public static final byte KICK_BARRAGE_NOISE = 106;
    public static final byte KICK_BARRAGE_NOISE_2 = KICK_BARRAGE_NOISE+1;
    public static final byte KICK_BARRAGE_NOISE_3 = KICK_BARRAGE_NOISE+2;
    public static final byte KICK_BARRAGE_NOISE_4 = KICK_BARRAGE_NOISE+3;

    /**This is called first by the server, it chooses the sfx and sends packets to nearby players*/
    public void playKickBarrageCrySound(){
        if (!this.self.level().isClientSide()) {
            double rand = Math.random();
            byte barrageCrySound;
            if (rand > 0.5) {
                barrageCrySound = KICK_BARRAGE_NOISE;
            } else {
                barrageCrySound = KICK_BARRAGE_NOISE_2;
            }
            playStandUserOnlySoundsIfNearby(barrageCrySound, 32, false,true);
        }
    }
    public boolean setPowerKickBarrage() {
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.BARRAGE_2);
        this.poseStand(OffsetIndex.ATTACK);
        playKickBarrageCrySound();
        this.setAttackTimeMax(this.getKickBarrageRecoilTime());
        this.setActivePowerPhase(this.getActivePowerPhaseMax());
        animateStand((byte) 80);
        return true;
    }
    public int getKickBarrageLength(){
        return 50;
    }
    public void updateKickBarrageCharge(){
        if (this.attackTimeDuring >= this.getKickBarrageWindup()) {
            ((StandUser) this.self).roundabout$tryPower(PowerIndex.BARRAGE_2, true);
        }
    }
    @Override
    public int getKickBarrageWindup(){
        return ClientNetworking.getAppropriateConfig().kickBarrageWindup;
    }

    private float getKickBarrageChargePitch(){
        return 1/((float) this.getKickBarrageWindup() /20);
    }

    public boolean impale(){
        StandEntity stand = getStandEntity(this.self);
        if (Objects.nonNull(stand)){
            this.setAttackTimeDuring(0);
            this.setActivePower(PowerIndex.POWER_1_SNEAK);
            playSoundsIfNearby(IMPALE_NOISE, 27, false);
            this.animateStand((byte)81);
            this.poseStand(OffsetIndex.GUARD);

            return true;
        }
        return false;
    }

    /**If a client is behind a server on TS charging somehow, and the server finishes charging, this packet rounds
     * things out*/
    @Override
    public void updatePowerInt(byte activePower, int data){
        if (activePower == PowerIndex.SPECIAL) {
            if (this.getMaxChargeTSTime() < data) {
                this.setMaxChargeTSTime(data);
                this.setChargedTSTicks(data);
            }
        } else if (activePower == PowerIndex.SPECIAL_CHARGED){
            this.setChargedTSTicks(data);
        } else if (activePower == PowerIndex.SPECIAL_FINISH){
            if (this.isBarraging()) {
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
            }
        }
    }

    @Override
    public void timeTickStopPower(){
        if (!(this.getSelf() instanceof Player && (((Player)this.getSelf()).isCreative() && ClientNetworking.getAppropriateConfig().timeStopSettings.creativeModeInfiniteTimeStop))) {
            int TSChargeTicks = this.getChargedTSTicks();
            TSChargeTicks -= 1;

            boolean cannotBreathInTs = ClientNetworking.getAppropriateConfig().timeStopSettings.preventsBreathing;
            if (cannotBreathInTs){
                this.getSelf().setAirSupply(((ILivingEntityAccess) this.getSelf()).roundabout$DecreaseAirSupply(this.getSelf().getAirSupply()));
            }

            if (TSChargeTicks < 0 || (cannotBreathInTs && this.getSelf().getAirSupply() == -20)) {
                if (this.getSelf().getAirSupply() == -20) {
                    this.getSelf().setAirSupply(0);
                }
                TSChargeTicks = 0;
                this.setChargedTSTicks(TSChargeTicks);
                if (this.getSelf().level().isClientSide) {
                    ModPacketHandler.PACKET_ACCESS.StandChargedPowerPacket(PowerIndex.SPECIAL_FINISH,TSChargeTicks);
                }
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.SPECIAL_FINISH, true);
            } else {
                if (this.getSelf().level().isClientSide) {
                    /*If the server is behind on the client TS time, update it to lower*/
                    ModPacketHandler.PACKET_ACCESS.intToServerPacket(TSChargeTicks, PacketDataIndex.INT_TS_TIME);
                } else {
                    /** This code was for the time resume sfx creeping in, but it sounds very chaotic
                     * with all of the other TS sounds so I am opting out
                     if (this.getMaxChargeTSTime() >= 20 && !playedResumeSound) {
                     if (TSChargeTicks <= 25 && this.getMaxChargeTSTime() >= 65) {
                     playSoundsIfNearby(TIME_STOP_ENDING_NOISE,100);
                     playedResumeSound = true;
                     } else if (TSChargeTicks <= 20 && this.getMaxChargeTSTime() > 20){
                     playSoundsIfNearby(TIME_STOP_ENDING_NOISE_2,100);
                     playedResumeSound = true;
                     }
                     }*/
                }
                this.setChargedTSTicks(TSChargeTicks);
            }
        }
    }

    public void setMaxChargeTSTime(int chargedTSTicks){
        this.maxChargeTSTime = chargedTSTicks;
    }

    public void playSPandTWTSSounds(){
        playSoundsIfNearby(TIME_STOP_NOISE, 100, true);

    }

    public void setChargeTicksMult(){

    }

    /*Activate Time Stop**/

    public byte getTimeStopShortNoise(){
        return TIME_STOP_NOISE_2;
    }
    public boolean stopTime() {
        /*Time Stop*/
        if (this.getActivePower() == PowerIndex.SPECIAL || (this.getSelf() instanceof Player && (((Player)this.getSelf()).isCreative() && ClientNetworking.getAppropriateConfig().timeStopSettings.creativeModeInfiniteTimeStop)) ||
                this.getChargedTSTicks() <= ClientNetworking.getAppropriateConfig().timeStopSettings.impulseTimeStopLength) {
            if (!this.getSelf().level().isClientSide()) {
                if (!((TimeStop) this.getSelf().level()).isTimeStoppingEntity(this.getSelf())) {
                    boolean animate = false;
                    hasActedInTS = false;
                    this.maxChargedTSTicks = this.getChargedTSTicks() + this.setCurrentMaxTSTime(this.getChargedTSTicks());
                    setChargeTicksMult();
                    if (!(((TimeStop) this.getSelf().level()).CanTimeStopEntity(this.getSelf()))) {
                        if (this.getChargedTSTicks() > 20 || (this.getSelf() instanceof Player && (((Player)this.getSelf()).isCreative() && ClientNetworking.getAppropriateConfig().timeStopSettings.creativeModeInfiniteTimeStop))) {
                            /*Charged Sound*/
                            animate = true;
                            playSPandTWTSSounds();
                        } else {
                            /*No Charged Sound*/
                            playSoundsIfNearby(getTimeStopShortNoise(), 100, true);
                        }
                    }
                    addEXP(3+(Math.max(1,(int)(this.getChargedTSTicks()/10))));
                    ((TimeStop) this.getSelf().level()).addTimeStoppingEntity(this.getSelf());
                    if (this.getSelf() instanceof Player) {
                        ModPacketHandler.PACKET_ACCESS.sendIntPowerPacket(((ServerPlayer) this.getSelf()), PowerIndex.SPECIAL, maxChargeTSTime);
                    }
                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                    /**
                     if (animate){
                     animateStand((byte) 31);
                     }
                     */
                }
            } else {
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.LEAD_IN, true);
            }
        } else {
            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
        }
        return true;
    }

    @SuppressWarnings("deprecation")
    public boolean canStandRebound(){
        if (this.getSelf().level().getBlockState(this.getSelf().getOnPos().above().east()).isSolid() ||
                this.getSelf().level().getBlockState(this.getSelf().getOnPos().above().west()).isSolid() ||
                this.getSelf().level().getBlockState(this.getSelf().getOnPos().above().north()).isSolid() ||
                this.getSelf().level().getBlockState(this.getSelf().getOnPos().above().south()).isSolid() ||

                this.getSelf().level().getBlockState(this.getSelf().getOnPos().above().east().north()).isSolid() ||
                this.getSelf().level().getBlockState(this.getSelf().getOnPos().above().west().south()).isSolid() ||
                this.getSelf().level().getBlockState(this.getSelf().getOnPos().above().west().north()).isSolid() ||
                this.getSelf().level().getBlockState(this.getSelf().getOnPos().above().east().south()).isSolid() ||

                this.getSelf().level().getBlockState(this.getSelf().getOnPos().above().above().east().north()).isSolid() ||
                this.getSelf().level().getBlockState(this.getSelf().getOnPos().above().above().west().south()).isSolid() ||
                this.getSelf().level().getBlockState(this.getSelf().getOnPos().above().above().west().north()).isSolid() ||
                this.getSelf().level().getBlockState(this.getSelf().getOnPos().above().above().east().south()).isSolid() ||

                this.getSelf().level().getBlockState(this.getSelf().getOnPos().above().above().east()).isSolid() ||
                this.getSelf().level().getBlockState(this.getSelf().getOnPos().above().above().west()).isSolid() ||
                this.getSelf().level().getBlockState(this.getSelf().getOnPos().above().above().north()).isSolid() ||
                this.getSelf().level().getBlockState(this.getSelf().getOnPos().above().above().south()).isSolid()
        ){
            return true;
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    public boolean doVault(){
        Vec3 vec3d = this.getSelf().getEyePosition(0);
        Vec3 vec3d2 = this.getSelf().getViewVector(0);
        Vec3 vec3d3 = vec3d.add(vec3d2.x * 2, vec3d2.y * 2, vec3d2.z * 2);
        BlockHitResult blockHit = this.getSelf().level().clip(new ClipContext(vec3d, vec3d3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.getSelf()));
        if (this.getSelf().level().getBlockState(blockHit.getBlockPos()).isSolid() && (blockHit.getBlockPos().getY()+1) > this.getSelf().getY()
                && !this.getSelf().level().getBlockState(blockHit.getBlockPos().above()).isSolid()) {
            if (!this.onCooldown(PowerIndex.SKILL_3_SNEAK)) {
                    /*Stand vaulting*/
                    this.setCooldown(PowerIndex.SKILL_3_SNEAK, ClientNetworking.getAppropriateConfig().cooldownsInTicks.vaulting);
                    double mag = this.getSelf().getPosition(0).distanceTo(
                            new Vec3(blockHit.getLocation().x, blockHit.getLocation().y, blockHit.getLocation().z)) * 1.68 + 1;
                    MainUtil.takeUnresistableKnockbackWithY2(this.getSelf(),
                            (blockHit.getLocation().x - this.getSelf().getX()) / mag,
                            0.35 + Math.max((blockHit.getLocation().y - this.getSelf().getY()) / mag, 0),
                            (blockHit.getLocation().z - this.getSelf().getZ()) / mag
                    );
                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.VAULT, true);
                    ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.VAULT);
                return true;
            }
            return true;
        }
        return false;
    }


    public void standRebound(){

        if (!this.getSelf().onGround()) {
            if (bonusLeapCount > 0 && spacedJumpTime < 0 && !this.onCooldown(PowerIndex.EXTRA) && canStandRebound()) {
                spacedJumpTime = 5;

                bigLeap(this.getSelf(), 20F, (float) (0.17+(bonusLeapCount*0.17)));
                bonusLeapCount--;
                if (bonusLeapCount <=0){
                    this.setCooldown(PowerIndex.EXTRA, 100);
                }
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.BOUNCE,true);
                ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.BOUNCE);
            }
        }
    }




    public void bigLeap(LivingEntity entity,float range, float mult){
        Vec3 vec3d = entity.getEyePosition(0);
        Vec3 vec3d2 = entity.getViewVector(0);
        Vec3 vec3d3 = vec3d.add(vec3d2.x * range, vec3d2.y * range, vec3d2.z * range);
        BlockHitResult blockHit = entity.level().clip(new ClipContext(vec3d, vec3d3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity));

        double mag = this.getSelf().getPosition(0).distanceTo(
                new Vec3(blockHit.getLocation().x, blockHit.getLocation().y,blockHit.getLocation().z))*0.75+1;

        MainUtil.takeUnresistableKnockbackWithY2(this.getSelf(),
                ((blockHit.getLocation().x - this.getSelf().getX())/mag)*mult,
                (0.6+Math.max((blockHit.getLocation().y - this.getSelf().getY())/mag,0))*mult,
                ((blockHit.getLocation().z - this.getSelf().getZ())/mag)*mult
        );

    }


    @SuppressWarnings("deprecation")
    public boolean canVault(){
        Vec3 vec3d = this.getSelf().getEyePosition(0);
        Vec3 vec3d2 = this.getSelf().getViewVector(0);
        Vec3 vec3d3 = vec3d.add(vec3d2.x * 2, vec3d2.y * 2, vec3d2.z * 2);
        BlockHitResult blockHit = this.getSelf().level().clip(new ClipContext(vec3d, vec3d3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.getSelf()));
        if (this.getSelf().level().getBlockState(blockHit.getBlockPos()).isSolid() && (blockHit.getBlockPos().getY()+1) > this.getSelf().getY()
                && !this.getSelf().level().getBlockState(blockHit.getBlockPos().above()).isSolid()){
            return true;
        } else {
            return false;
        }
    }

    public boolean timeStopStartedBarrage = false;
    @Override
    public boolean bonusBarrageConditions(){
        if (this.getSelf() != null){
            boolean TSEntity = ((TimeStop)this.getSelf().level()).isTimeStoppingEntity(this.getSelf());
            if (TSEntity && !this.timeStopStartedBarrage){
                this.timeStopStartedBarrage = true;
                return true;
            } else if (!TSEntity && this.timeStopStartedBarrage){
                this.timeStopStartedBarrage = false;
                return false;
            }
        }
        return true;
    }

    public void animateFinalAttack(){
        animateStand((byte) 85);
    }

    public void animateFinalAttackHit(){
        animateStand((byte) 86);
    }

    public boolean setPowerFinalAttack() {
        animateFinalAttack();
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.SNEAK_ATTACK_CHARGE);
        this.poseStand(OffsetIndex.GUARD);
        this.clashDone = false;
        return true;
    }
    public static int maxSuperHitTime = 25;
    public boolean setPowerSuperHit() {
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.SNEAK_ATTACK);
        this.poseStand(OffsetIndex.ATTACK);
        chargedFinal = Math.min(this.chargedFinal,maxSuperHitTime);
        animateFinalAttackHit();
        //playBarrageCrySound();
        return true;
    }

    public float getFinalAttackKnockback(){
        Roundabout.LOGGER.info("CF"+this.chargedFinal);
        Roundabout.LOGGER.info("KB"+(float)((this.chargedFinal/maxSuperHitTime)*3));
        return (((float)this.chargedFinal /(float)maxSuperHitTime)*3);
    }
    public float getFinalPunchStrength(Entity entity){
        float punchD = this.getPunchStrength(entity)*2+this.getHeavyPunchStrength(entity);
        if (this.getReducedDamage(entity)){
            return (((float)this.chargedFinal/(float)maxSuperHitTime)*punchD);
        } else {
            return (((float)this.chargedFinal/(float)maxSuperHitTime)*punchD)+3;
        }
    }
    public int getFinalAttackKnockShieldTime(){
        return 20;
    }


    public void dspStuff(Entity ent){
        
    }

    public void finalAttackImpact(Entity entity){
        this.setAttackTimeDuring(-20);
        if (entity != null) {
            float pow;
            float knockbackStrength;
            pow = getFinalPunchStrength(entity);
            knockbackStrength = getFinalAttackKnockback();
            dspStuff(entity);
            if (StandDamageEntityAttack(entity, pow, 0, this.self)) {
                if (entity instanceof LivingEntity LE) {
                    if (chargedFinal >= maxSuperHitTime) {
                        addEXP(5, LE);
                    }
                }
                this.takeDeterminedKnockbackWithY(this.self, entity, knockbackStrength);
            } else {
                if (chargedFinal >= maxSuperHitTime) {
                    knockShield2(entity, getFinalAttackKnockShieldTime());
                }
            }
        } else {
            // This is less accurate raycasting as it is server sided but it is important for particle effects
            float distMax = this.getDistanceOut(this.self, this.standReach, false);
            float halfReach = (float) (distMax * 0.5);
            Vec3 pointVec = DamageHandler.getRayPoint(self, halfReach);
            if (!this.self.level().isClientSide) {
                ((ServerLevel) this.self.level()).sendParticles(ParticleTypes.EXPLOSION, pointVec.x, pointVec.y, pointVec.z,
                        1, 0.0, 0.0, 0.0, 1);
            }
        }

        SoundEvent SE;
        float pitch = 1F;
        if (entity != null) {
            SE = getFinalAttackSound();
            pitch = 1.2F;
        } else {
            SE = ModSounds.PUNCH_2_SOUND_EVENT;
        }

        if (!this.self.level().isClientSide()) {
            this.self.level().playSound(null, this.self.blockPosition(), SE, SoundSource.PLAYERS, 0.95F, pitch);
        }
    }


    /**Barrage During a time stop, and it will cancel when time resumes, but it will also skip the charge*/
    @Override
    public boolean setPowerBarrageCharge(){
        if (this.getSelf() != null && ((TimeStop)this.getSelf().level()).isTimeStoppingEntity(this.getSelf())){
            timeStopStartedBarrage = true;
        } else {
            timeStopStartedBarrage = false;
        }
        return super.setPowerBarrageCharge();
    }

    @Override
    public void playBarrageChargeSound(){
        if (!timeStopStartedBarrage){
            super.playBarrageChargeSound();
        }
    }

    @Override
    public void playBarrageNoise(int hitNumber, Entity entity){
        if (!this.getSelf().level().isClientSide()) {
            if (!this.getSelf().level().isClientSide() && (((TimeStop)this.getSelf().level()).CanTimeStopEntity(entity))) {
                playBarrageBlockNoise();
            } else {
                if (hitNumber % 2 == 0) {
                    this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.STAND_BARRAGE_HIT_EVENT, SoundSource.PLAYERS, 0.9F, (float) (0.9 + (Math.random() * 0.25)));
                }
            }
        }
    }
    @Override
    public void playBarrageNoise2(int hitNumber, Entity entity){
        if (!this.getSelf().level().isClientSide()) {
            if (!this.getSelf().level().isClientSide() && (((TimeStop)this.getSelf().level()).CanTimeStopEntity(entity))) {
                playBarrageBlockNoise();
            } else {
                if (hitNumber%2==0) {
                    this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.STAND_BARRAGE_HIT2_EVENT, SoundSource.PLAYERS, 0.95F, (float) (0.9 + (Math.random() * 0.25)));
                }
            }
        }
    }
    @Override
    public void playBarrageEndNoise(float mod, Entity entity){
        if (!this.getSelf().level().isClientSide()) {
            if (!this.getSelf().level().isClientSide() && (((TimeStop)this.getSelf().level()).CanTimeStopEntity(entity))) {
                if (entity instanceof  LivingEntity){
                    ((StandUser)entity).roundaboutSetTSHurtSound(3);
                }
                playBarrageBlockEndNoise(0,entity);
            } else {
                this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.STAND_BARRAGE_END_EVENT, SoundSource.PLAYERS, 0.95F+mod, 1f);
            }
        }
    }

    /**20 ticks in a second*/
    @Override
    public boolean isStoppingTime(){
        return (((TimeStop)this.getSelf().level()).isTimeStoppingEntity(this.getSelf()));
    }

    @Override
    public boolean isAttackIneptVisually(byte activeP, int slot){
        return this.isDazed(this.getSelf()) || (activeP != PowerIndex.SKILL_4 && (((TimeStop)this.getSelf().level()).CanTimeStopEntity(this.getSelf()))
                || ((this.getActivePower() == PowerIndex.POWER_2_SNEAK && this.getAttackTimeDuring() >= 0)) || hasBlock() || hasEntity());
    }

    @Override
    public void timeTick(){
        if (this.getActivePower() == PowerIndex.SPECIAL){
            this.updateUniqueMoves();
        }
        super.timeTick();
    }

    @Override
    public boolean tryPower(int move, boolean forced) {

        StandEntity stand = getStandEntity(this.self);
        if (move != PowerIndex.POWER_2_BLOCK && Objects.nonNull(stand)) {
            stand.setFadePercent(100);
        }
        if (moveStarted){
            moveStarted = false;
        }
        if (!this.self.level().isClientSide &&
                (this.getActivePower() == PowerIndex.BARRAGE_CHARGE_2 || this.getActivePower() == PowerIndex.BARRAGE_2)
                && (move != PowerIndex.BARRAGE_2)){
            this.stopSoundsIfNearby(SoundIndex.BARRAGE_SOUND_GROUP, 100,false);
        }
        if (!this.getSelf().level().isClientSide && move == PowerIndex.SNEAK_MOVEMENT && this.isClashing()){
            ((StandUser) this.getSelf()).roundabout$setLeapTicks(((StandUser) this.getSelf()).roundabout$getMaxLeapTicks());
        }
        if (!this.getSelf().level().isClientSide && this.getActivePower() == PowerIndex.SPECIAL) {
            this.stopSoundsIfNearby(SoundIndex.TIME_CHARGE_SOUND_GROUP, 100,true);
            this.stopSoundsIfNearby(SoundIndex.TIME_CHARGE_SOUND_GROUP, 100,false);
        }
        if (!this.getSelf().level().isClientSide && this.getActivePower() == PowerIndex.POWER_1_SNEAK) {
            this.stopSoundsIfNearby(IMPALE_NOISE, 100,true);
        }
        return super.tryPower(move,forced);
    }
    @Override
    public float getSoundPitchFromByte(byte soundChoice){
        if (soundChoice == TIME_STOP_NOISE_3) {
            return 1F;
        } else if (soundChoice == SoundIndex.ALT_CHARGE_SOUND_1){
            return this.getKickBarrageChargePitch();
        } else {
            return super.getSoundPitchFromByte(soundChoice);
        }
    }

    @Override
    public float getSoundVolumeFromByte(byte soundChoice){
        if (soundChoice == TIME_RESUME_NOISE) {
            return 0.8f;
        } else if (soundChoice == TIME_STOP_NOISE_3) {
            return 0.6f;
        } else if (soundChoice == TIME_RESUME_NOISE_2) {
            return 0.6f;
        }
        return super.getSoundVolumeFromByte(soundChoice);
    }

    @Override
    public int getBarrageWindup(){
        if (timeStopStartedBarrage) {
            return Math.min(13,ClientNetworking.getAppropriateConfig().barrageWindup);
        } else {
            return ClientNetworking.getAppropriateConfig().barrageWindup;
        }
    }

    @Override
    public void runExtraSoundCode(byte soundChoice) {
        if (soundChoice >= TIME_STOP_NOISE && soundChoice <= TIME_STOP_NOISE_9) {
            if (this.getSelf().level().isClientSide) {
                Minecraft mc = Minecraft.getInstance();
                mc.getSoundManager().stop();
                if (!((TimeStop)this.getSelf().level()).isTimeStoppingEntity(this.getSelf())){
                    TimeStopInstance tsi = ((TimeStop)this.getSelf().level()).getTimeStopperInstanceClient(this.getSelf().position());
                    if (tsi != null && tsi.maxDuration >= 170) {
                        ((StandUserClient)this.getSelf()).roundabout$clientPlaySoundIfNoneActive(TIME_STOP_TICKING);
                    }
                }
            }
        }
    }

    public static final byte BARRAGE_NOISE = 20;
    public static final byte BARRAGE_NOISE_2 = BARRAGE_NOISE+1;
    public static final byte BARRAGE_NOISE_3 = BARRAGE_NOISE+2;
    public static final byte BARRAGE_NOISE_4 = BARRAGE_NOISE+3;
    public static final byte BARRAGE_NOISE_5 = BARRAGE_NOISE+4;
    public static final byte BARRAGE_NOISE_6 = BARRAGE_NOISE+5;
    public static final byte BARRAGE_NOISE_7 = BARRAGE_NOISE+7;
    public static final byte BARRAGE_NOISE_8 = BARRAGE_NOISE+8;
    public static final byte TIME_STOP_CHARGE = 30;
    public static final byte TIME_STOP_VOICE = TIME_STOP_CHARGE+1;
    public static final byte TIME_STOP_VOICE_2 = TIME_STOP_CHARGE+2;
    public static final byte TIME_STOP_VOICE_3 = TIME_STOP_CHARGE+3;
    public static final byte TIME_STOP_ENDING_NOISE_2 = TIME_STOP_NOISE+10;
    public static final byte TIME_STOP_ENDING_NOISE = TIME_STOP_NOISE+11;
    public static final byte IMPALE_NOISE = 105;
}
