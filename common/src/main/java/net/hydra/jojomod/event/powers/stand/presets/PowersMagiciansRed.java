package net.hydra.jojomod.event.powers.stand.presets;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.block.StandFireBlock;
import net.hydra.jojomod.block.StandFireBlockEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.UnburnableProjectile;
import net.hydra.jojomod.entity.projectile.CrossfireHurricaneEntity;
import net.hydra.jojomod.entity.stand.JusticeEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class PowersMagiciansRed extends PunchingStand {

    public CrossfireHurricaneEntity hurricane;
    public List<CrossfireHurricaneEntity> hurricaneSpecial = new ArrayList<>();
    public static int getChargingCrossfireSpecial(){
        return 26;
    }
    public static int getChargingCrossfireSpecialSize(){
        return 26;
    }
    public static int getChargingCrossfire(){
        return 60;
    }

    public static int getChargingCrossfireSize(){
        return 60;
    }
    public void tickPower() {
        super.tickPower();
        if (!this.self.level().isClientSide()) {
            if (ticksUntilHurricaneEnds > -1) {
                ticksUntilHurricaneEnds--;
                if (ticksUntilHurricaneEnds <= -1) {
                    this.self.level().playSound(null, this.self.blockPosition(), SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 2F, 0.8F);
                    ((ServerLevel) this.self.level()).sendParticles(getFlameParticle(), this.self.getX(),
                            this.self.getY()+(this.self.getBbHeight()*0.5), this.self.getZ(),
                            20,
                            1.2, 1.2, 1.2,
                            0.005);
                    clearAllHurricanes();

                    if (this.isChargingCrossfireSingle()){
                        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE,true);
                    }
                }
            }
        }
    }
    public void tickPowerEnd(){
        if (hurricaneSpecial != null && !hurricaneSpecial.isEmpty()){
            removeHurricanes();
            if (!this.self.level().isClientSide()) {
                hurricaneSpecialRotation();
            } else {
                lastSpinInt+= maxSpinint;
            }
        } else if (hurricane != null){
            removeHurricanes();
            if (!this.self.level().isClientSide()) {
                hurricaneRotation();
            }
        }
    }
    public void addHurricaneSpecial(CrossfireHurricaneEntity che){
        hurricaneInit();
        hurricaneSpecial.add(che);
    }
    public double spinint = 0;
    public double lastSpinInt = 0;
    public double maxSpinint = 4;

    public void hurricaneInit(){
        if (hurricaneSpecial == null) {
            hurricaneSpecial = new ArrayList<>();
        }
    }
    public void hurricaneRotation() {
        transformHurricane(this.hurricane, 1, this.self.getX(), this.self.getY(), this.self.getZ(),getChargingCrossfireSize());
    }
    public void hurricaneSpecialRotation() {
        hurricaneInit();
        List<CrossfireHurricaneEntity> hurricaneSpecial2 = new ArrayList<>(hurricaneSpecial) {
        };
        if (!hurricaneSpecial2.isEmpty()) {
            int totalnumber = hurricaneSpecial2.size();
            for (CrossfireHurricaneEntity value : hurricaneSpecial2) {
                transformHurricane(value, totalnumber, this.self.getX(), this.self.getY(), this.self.getZ(),value.getSize());
            }
        }
    }
    public void removeHurricanes(){
        hurricaneInit();
        List<CrossfireHurricaneEntity> hurricaneSpecial2 = new ArrayList<>(hurricaneSpecial) {
        };
        if (!hurricaneSpecial2.isEmpty()) {
            for (CrossfireHurricaneEntity value : hurricaneSpecial2) {
                if (value.isRemoved() || !value.isAlive() || value.getCrossNumber() <= 0) {
                    value.initialized = false;
                    hurricaneSpecial.remove(value);
                }
            }
        }
        if (hurricane != null && (hurricane.isRemoved() || !hurricane.isAlive() || hurricane.getCrossNumber() <= 0)){
            hurricane.initialized = false;
            hurricane = null;
        }
    }
    public float inputSpeedModifiers(float basis){
        if (this.hasHurricaneSingle()){
            basis *= 0.5f;
        }
        if (this.hasHurricane() || isChargingCrossfire()){
            basis *= 0.6f;
        }
        return super.inputSpeedModifiers(basis);
    }
    @Override
    public boolean cancelSprintJump(){
        if (this.hasHurricane() || isChargingCrossfire()){
            return true;
        }
        return super.cancelSprintJump();
    }
    public void transformHurricane(CrossfireHurricaneEntity value, int totalnumber, double entityX, double entityY, double entityZ, double rsize){
        if (value != null) {
            int size = value.getSize();
            double distanceUp = 0.3;
            if (size < value.getMaxSize()) {
                size += value.getAccrualRate();
                value.setSize(size);
            }
            distanceUp += ((double) rsize / 20);
            double offset = 0;
            int number = value.getCrossNumber();
            if (this.self.level().isClientSide()) {
                if (number == 1) {
                    offset = 0;
                } else if (number == 5) {
                    offset = this.self.getYRot() % 360;
                } else if (number == 2) {
                    offset = switch (totalnumber) {
                        case 3 -> 0;
                        case 4 -> 90;
                        default -> offset;
                    };
                } else if (number == 3) {
                    offset = switch (totalnumber) {
                        case 2 -> 0;
                        case 3 -> 120;
                        case 4 -> 180;
                        default -> offset;
                    };
                } else if (number == 4) {
                    offset = switch (totalnumber) {
                        case 1 -> 0;
                        case 2 -> 180;
                        case 3 -> 240;
                        case 4 -> 270;
                        default -> offset;
                    };
                }
                if (number != 5) {
                    offset += spinint;
                }
                if (offset > 360) {
                    offset -= 360;
                } else if (offset < 0) {
                    offset += 360;
                }
            } else {
                offset = this.self.getYRot() % 360;
            }
            double offset2 = offset;
            offset = (offset - 180) * Math.PI;
            double distanceOut = 2;
            if (number == 5) {
                distanceUp *= 0.25F;
            }
            double x1 = entityX - -1 * (distanceOut * (Math.sin(offset / 180)));
            double y1 = entityY + distanceUp;
            double z1 = entityZ - (distanceOut * (Math.cos(offset / 180)));
            if (!this.self.level().isClientSide()) {
                value.setOldPosAndRot();
                //Roundabout.LOGGER.info("bye");
            }
            value.actuallyTick();
            value.storeVec = new Vec3(x1, y1, z1);
            if (this.self.level().isClientSide()) {
                value.setYRot((float) offset2);
                value.yRotO = (float) offset2;
                value.xOld = x1;
                value.yOld = y1;
                value.zOld = z1;
                value.absMoveTo(x1, y1, z1);
            } else {
                value.setYRot((float) offset2);
                value.yRotO = (float) offset2;
                value.xOld = x1;
                value.yOld = y1;
                value.zOld = z1;
                value.setPos(x1, y1, z1);
            }
        }
    }
    public int snapNumber;
    public int fireIDNumber;
    public PowersMagiciansRed(LivingEntity self) {
        super(self);
    }
    @Override
    public StandPowers generateStandPowers(LivingEntity entity){
        return new PowersMagiciansRed(entity);
    }
    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        boolean secondSkillLocked =hasHurricaneSpecial() || this.isChargingCrossfire() || hasHurricaneSingle();
        if (this.isGuarding()){
            if (isHoldingSneak()) {
                setSkillIcon(context, x, y, 1, StandIcons.LIGHT_FIRE, PowerIndex.SKILL_1_SNEAK);
            } else {
                setSkillIcon(context, x, y, 1, StandIcons.RED_BIND, PowerIndex.NO_CD);
            }
            if (secondSkillLocked){
                setSkillIcon(context, x, y, 2, StandIcons.CROSSFIRE_HURRICANE_SHOT, PowerIndex.SKILL_2);
            } else {
                setSkillIcon(context, x, y, 2, StandIcons.NONE, PowerIndex.NO_CD);
            }
            setSkillIcon(context, x, y, 3, StandIcons.PROJECTILE_BURN, PowerIndex.SKILL_EXTRA);
        } else {
            if (isHoldingSneak()) {
                if (secondSkillLocked){
                    setSkillIcon(context, x, y, 2, StandIcons.CROSSFIRE_HURRICANE_SHOT, PowerIndex.NO_CD);
                } else {
                    setSkillIcon(context, x, y, 2, StandIcons.CROSSFIRE_HURRICANE_SPECIAL, PowerIndex.SKILL_2_SNEAK);
                }
                setSkillIcon(context, x, y, 1, StandIcons.LIGHT_FIRE, PowerIndex.SKILL_1_SNEAK);
                setSkillIcon(context, x, y, 3, StandIcons.SNAP_ICON, PowerIndex.SKILL_3);
            } else {
                if (secondSkillLocked){
                    setSkillIcon(context, x, y, 2, StandIcons.CROSSFIRE_HURRICANE_SHOT, PowerIndex.NO_CD);
                } else {
                    setSkillIcon(context, x, y, 2, StandIcons.CROSSFIRE_HURRICANE, PowerIndex.SKILL_2);
                }
                setSkillIcon(context, x, y, 1, StandIcons.RED_BIND, PowerIndex.NO_CD);
                setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.SKILL_3_SNEAK);
            }
        }
        setSkillIcon(context, x, y, 4, StandIcons.NONE, PowerIndex.NO_CD);
    }
    @Override
    public StandEntity getNewStandEntity(){
        return ModEntities.MAGICIANS_RED.create(this.getSelf().level());
    }
    @Override
    public Component getSkinName(byte skinId){
        return JusticeEntity.getSkinNameT(skinId);
    }

    @Override
    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }
    @Override
    public SoundEvent getSoundFromByte(byte soundChoice){
        if (soundChoice == SoundIndex.SUMMON_SOUND) {
            return ModSounds.SUMMON_MAGICIAN_EVENT;
        } else if (soundChoice == LAST_HIT_1_NOISE) {
            return ModSounds.FIRE_STRIKE_LAST_EVENT;
        } else if (soundChoice == CRY_1_NOISE) {
            return ModSounds.MAGICIANS_RED_CRY_EVENT;
        } else if (soundChoice == CRY_2_NOISE) {
            return ModSounds.MAGICIANS_RED_CRY_2_EVENT;
        }
        return super.getSoundFromByte(soundChoice);
    }

    public boolean hold1 = false;
    public boolean hold2 = false;
    public boolean hold3 = false;

    public BlockPos getGrabPos(float range) {
        Vec3 vec3d = this.getSelf().getEyePosition(0);
        Vec3 vec3d2 = this.getSelf().getViewVector(0);
        Vec3 vec3d3 = vec3d.add(vec3d2.x * range, vec3d2.y * range, vec3d2.z * range);
        return new BlockPos((int) vec3d3.x, (int) vec3d3.y, (int) vec3d3.z);
    }
    public BlockPos getGrabBlock(){
        return getGrabBlock(5);
    }
    public BlockPos getGrabBlock(float range){

        Vec3 vec3d = this.getSelf().getEyePosition(0);
        Vec3 vec3d2 = this.getSelf().getViewVector(0);
        Vec3 vec3d3 = vec3d.add(vec3d2.x * range, vec3d2.y * range, vec3d2.z * range);
        BlockHitResult blockHit = this.getSelf().level().clip(new ClipContext(vec3d, vec3d3,
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.getSelf()));
        if (blockHit.getType() == HitResult.Type.BLOCK){
            grabBlock2 = blockHit.getBlockPos();
            return blockHit.getBlockPos().relative(blockHit.getDirection());
        }
        return null;
    }

    public boolean hasHurricane(){
        return hasHurricaneSpecial() || hasHurricaneSingle();
    }

    public boolean hasHurricaneSingle(){
        return (this.hurricane != null && this.hurricane.isAlive());
    }
    public boolean hasHurricaneSpecial(){
        hurricaneInit();
        List<CrossfireHurricaneEntity> hurricaneSpecial2 = new ArrayList<>(hurricaneSpecial) {
        };
        if (!hurricaneSpecial2.isEmpty()) {
            return true;
        }
        return false;
    }
    public boolean isChargingCrossfire(){
        return (activePower == PowerIndex.POWER_2_SNEAK || activePower == PowerIndex.POWER_2);
    }
    public boolean isChargingCrossfireSpecial(){
        return (activePower == PowerIndex.POWER_2_SNEAK);
    }
    public boolean isChargingCrossfireSingle(){
        return (activePower == PowerIndex.POWER_2);
    }
    @Override
    public void buttonInput1(boolean keyIsDown, Options options) {
        if (keyIsDown) {
            if (!hold1) {
                hold1 = true;
                if (!isChargingCrossfire() && !hasHurricaneSingle()) {
                    if (!isGuarding()) {
                        if (isHoldingSneak()) {
                            if (!this.onCooldown(PowerIndex.SKILL_1_SNEAK)) {
                                BlockPos HR = getGrabBlock();
                                if (HR != null) {
                                    this.setCooldown(PowerIndex.SKILL_1_SNEAK, ClientNetworking.getAppropriateConfig().cooldownsInTicks.magicianIgniteFire);
                                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_1_SNEAK, true);
                                    ModPacketHandler.PACKET_ACCESS.StandPosPowerPacket(PowerIndex.EXTRA, grabBlock2);
                                    ModPacketHandler.PACKET_ACCESS.StandPosPowerPacket(PowerIndex.POWER_1_SNEAK, HR);
                                }
                            }
                        }
                    }
                }
            }
        } else {
            hold1 = false;
        }
    }
    @Override
    public void buttonInput2(boolean keyIsDown, Options options) {
        if (keyIsDown) {
            if (!hold2) {
                hold2 = true;
                if (hasHurricaneSpecial()) {
                    if (!isChargingCrossfire()) {
                        if (!this.onCooldown(PowerIndex.SKILL_EXTRA_2)) {
                            this.setCooldown(PowerIndex.SKILL_EXTRA_2, 4);
                            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_2_BONUS, true);
                            ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.POWER_2_BONUS);
                        }
                    }
                } else if (hasHurricaneSingle() || isChargingCrossfireSingle()){
                    this.setCooldown(PowerIndex.SKILL_2, 100);
                        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_2_BONUS, true);
                        ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.POWER_2_BONUS);
                } else {
                    if (!isGuarding()) {
                        if (isHoldingSneak()) {
                            if (!this.onCooldown(PowerIndex.SKILL_2_SNEAK)) {
                                this.setCooldown(PowerIndex.SKILL_2_SNEAK, 600);
                                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_2_SNEAK, true);
                                ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.POWER_2_SNEAK);
                            }
                        } else {
                            if (!this.onCooldown(PowerIndex.SKILL_2)) {
                                this.setCooldown(PowerIndex.SKILL_2, 40);
                                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_2, true);
                                ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.POWER_2);
                            }
                        }
                    }
                }
            }
        } else {
            hold2 = false;
        }
    }
    public BlockPos grabBlock = null;
    public BlockPos grabBlock2 = null;
    public boolean tryPosPower(int move, boolean forced, BlockPos blockPos){
        if (move == PowerIndex.POWER_1_SNEAK) {
            this.grabBlock = blockPos;
            return tryPower(move, forced);
        } else if (move == PowerIndex.SPECIAL) {
            this.grabBlock2 = blockPos;
        } else if (move == PowerIndex.POWER_3_BLOCK) {
            this.grabBlock = blockPos;
            return tryPower(move, forced);
        }
        return false;
        /*Return false in an override if you don't want to sync cooldowns, if for example you want a simple data update*/
    }
    @Override
    public void buttonInput3(boolean keyIsDown, Options options) {
        if (keyIsDown) {
            if (!inputDash) {
                if (!isChargingCrossfire() && !hasHurricaneSingle()) {
                    if (this.isGuarding()) {
                        if (!this.onCooldown(PowerIndex.SKILL_EXTRA)) {
                            this.setCooldown(PowerIndex.SKILL_EXTRA, 100);

                            BlockPos HR = getGrabPos(10);
                            if (HR != null) {
                                ModPacketHandler.PACKET_ACCESS.StandPosPowerPacket(PowerIndex.POWER_3_BLOCK, HR);
                            }
                        }
                        inputDash = true;
                    } else if (isHoldingSneak()) {
                        if (!this.onCooldown(PowerIndex.SKILL_3)) {
                            this.setCooldown(PowerIndex.SKILL_3, ClientNetworking.getAppropriateConfig().cooldownsInTicks.magicianSnapFireAway);
                            ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.POWER_3);
                        }
                        inputDash = true;
                    } else {
                        super.buttonInput3(keyIsDown, options);
                    }
                }
            }
        } else {
            inputDash = false;
        }
    }
    @Override
    public void buttonInput4(boolean keyIsDown, Options options) {
        if (!isChargingCrossfire() && !hasHurricaneSingle()) {

        }
    }
    @Override
    public boolean tryPower(int move, boolean forced) {

        if ((this.activePower == PowerIndex.POWER_2
        || this.activePower == PowerIndex.POWER_2_SNEAK)
        && move != PowerIndex.POWER_2_BONUS && move != PowerIndex.LEAD_IN) {
            if (hasHurricaneSingle() || hasHurricaneSpecial()) {
                this.clearAllHurricanes();
            }
        }
        if (this.getActivePower() == PowerIndex.POWER_2) {
                stopSoundsIfNearby(CRY_2_NOISE, 100, false);
        } else if (this.getActivePower() == PowerIndex.POWER_2_SNEAK) {
            stopSoundsIfNearby(CRY_1_NOISE, 100, false);
        }

        return super.tryPower(move,forced);
    }
    public static final byte LAST_HIT_1_NOISE = 120;
    public static final byte CRY_1_NOISE = 100;
    public static final byte CRY_2_NOISE = 101;

    @Override
    public boolean canLightFurnace(){
        return true;
    }
    @Override
    public boolean setPowerOther(int move, int lastMove) {
        if (move == PowerIndex.POWER_3) {
            return this.snap();
        } else if (move == PowerIndex.POWER_1_SNEAK) {
            return this.setFire();
        } else if (move == PowerIndex.POWER_3_BLOCK) {
            return this.fireBlast();
        } else if (move == PowerIndex.POWER_2) {
            return this.crossfire();
        } else if (move == PowerIndex.POWER_2_SNEAK) {
            return this.crossfireSpecial();
        } else if (move == PowerIndex.LEAD_IN) {
            if (hasHurricaneSpecial()) {
                ticksUntilHurricaneEnds = 160;
                return this.setPowerNone();
            } else {
                ticksUntilHurricaneEnds = 80;
                return false;
            }
        } else if (move == PowerIndex.POWER_2_BONUS) {
            return this.shootAnkhConfirm();
        }
        return super.setPowerOther(move,lastMove);
    }

    @Override
    public byte getSoundCancelingGroupByte(byte soundChoice) {
        if (soundChoice == CRY_2_NOISE) {
            return CRY_2_NOISE;
        }
        return super.getSoundCancelingGroupByte(soundChoice);
    }

    public int ticksUntilHurricaneEnds = -1;

    @Override
    public void updateUniqueMoves(){
        if (this.getActivePower() == PowerIndex.POWER_2_SNEAK) {
            this.updateCrossfireSpecial();
        } else if (this.getActivePower() == PowerIndex.POWER_2) {
            this.updateCrossfire();
        }
    }
    public void updateCrossfire(){
        if (!this.self.level().isClientSide()) {
            if (this.attackTimeDuring >= getChargingCrossfire()){
                if (!(this.self instanceof Player)){
                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.LEAD_IN,true);
                }
            }
        } else {
            if (this.attackTimeDuring == getChargingCrossfire()) {
                if (this.self instanceof Player) {
                    if (isPacketPlayer()) {
                        ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.LEAD_IN);
                    }
                }
            }
        }
    }
    public BlockPos blockPosForSpecial = BlockPos.ZERO;
    public void updateCrossfireSpecial(){
        if (!this.self.level().isClientSide()) {
            if (this.attackTimeDuring == 4) {
                createStandFire2(blockPosForSpecial.east().east());
                createStandFire2(blockPosForSpecial.west().west());
                createStandFire2(blockPosForSpecial.north().north());
                createStandFire2(blockPosForSpecial.south().south());
                createStandFire2(blockPosForSpecial.north().west());
                createStandFire2(blockPosForSpecial.north().east());
                createStandFire2(blockPosForSpecial.south().west());
                createStandFire2(blockPosForSpecial.south().east());
            } else if (this.attackTimeDuring == 5) {
                sendSpecialParticle(blockPosForSpecial.east().east().north());
                sendSpecialParticle(blockPosForSpecial.east().east().south());
                sendSpecialParticle(blockPosForSpecial.west().west().north());
                sendSpecialParticle(blockPosForSpecial.west().west().south());
                sendSpecialParticle(blockPosForSpecial.north().north().east());
                sendSpecialParticle(blockPosForSpecial.north().north().west());
                sendSpecialParticle(blockPosForSpecial.south().south().east());
                sendSpecialParticle(blockPosForSpecial.south().south().west());
            } else if (this.attackTimeDuring == 9) {
                createStandFire2(blockPosForSpecial.east().east().north());
                createStandFire2(blockPosForSpecial.east().east().south());
                createStandFire2(blockPosForSpecial.west().west().north());
                createStandFire2(blockPosForSpecial.west().west().south());
                createStandFire2(blockPosForSpecial.north().north().east());
                createStandFire2(blockPosForSpecial.north().north().west());
                createStandFire2(blockPosForSpecial.south().south().east());
                createStandFire2(blockPosForSpecial.south().south().west());
            } else if (this.attackTimeDuring >= getChargingCrossfireSpecial()){
                if (!(this.self instanceof Player)){
                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.LEAD_IN,true);
                }
            }
        } else {
            if (this.attackTimeDuring >= getChargingCrossfireSpecial()) {
                if (this.self instanceof Player) {
                    if (isPacketPlayer()) {
                        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.LEAD_IN, true);
                        ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.LEAD_IN);
                    }
                }
            }
        }
    }
    public SimpleParticleType getFlameParticle(){
        return ModParticles.ORANGE_FLAME;
    }
    public void sendSpecialParticle(BlockPos pos){
        if (!this.self.level().isClientSide()) {
            ((ServerLevel) this.self.level()).sendParticles(getFlameParticle(), pos.getX()+0.5,
                    pos.getY(), pos.getZ()+0.5,
                    0,
                    0, 1, 0,
                    0.16);
            ((ServerLevel) this.self.level()).sendParticles(getFlameParticle(), pos.getX()+0.25,
                    pos.getY(), pos.getZ()+0.25,
                    0,
                    0, 1, 0,
                    0.16);
            ((ServerLevel) this.self.level()).sendParticles(getFlameParticle(), pos.getX()+0.25,
                    pos.getY(), pos.getZ()+0.75,
                    0,
                    0, 1, 0,
                    0.16);
            ((ServerLevel) this.self.level()).sendParticles(getFlameParticle(), pos.getX()+0.75,
                    pos.getY(), pos.getZ()+0.25,
                    0,
                    0, 1, 0,
                    0.16);
            ((ServerLevel) this.self.level()).sendParticles(getFlameParticle(), pos.getX()+0.75,
                    pos.getY(), pos.getZ()+0.75,
                    0,
                    0, 1, 0,
                    0.16);
        }
    }
    public boolean crossfireSpecial(){
        if (!hasHurricaneSpecial()) {
            ticksUntilHurricaneEnds = -1;
            this.animateStand((byte)15);
            this.poseStand(OffsetIndex.GUARD_FURTHER_RIGHT);
            this.setAttackTimeDuring(0);
            this.setActivePower(PowerIndex.POWER_2_SNEAK);
            spinint = 0;
            if (!this.self.level().isClientSide()) {
                blockPosForSpecial = this.self.blockPosition();
                sendSpecialParticle(blockPosForSpecial.east().east());
                sendSpecialParticle(blockPosForSpecial.west().west());
                sendSpecialParticle(blockPosForSpecial.north().north());
                sendSpecialParticle(blockPosForSpecial.south().south());
                sendSpecialParticle(blockPosForSpecial.north().west());
                sendSpecialParticle(blockPosForSpecial.north().east());
                sendSpecialParticle(blockPosForSpecial.south().west());
                sendSpecialParticle(blockPosForSpecial.south().east());
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.FIRE_BLAST_EVENT, SoundSource.PLAYERS, 2F, 0.8F);
                generateCrossfire(1, getChargingCrossfireSpecialSize());
                generateCrossfire(2, getChargingCrossfireSpecialSize());
                generateCrossfire(3, getChargingCrossfireSpecialSize());
                generateCrossfire(4, getChargingCrossfireSpecialSize());
                playStandUserOnlySoundsIfNearby(CRY_1_NOISE, 27, false,true);

            }
        }
        return true;
    }

    public void generateCrossfire(int crossNumber, int maxSize){
        CrossfireHurricaneEntity cross = ModEntities.CROSSFIRE_HURRICANE.create(this.getSelf().level());
        if (cross != null){
            cross.absMoveTo(this.getSelf().getX(), this.getSelf().getY(), this.getSelf().getZ());
            cross.setUser(this.self);

            if (hurricaneSpecial == null) {hurricaneSpecial = new ArrayList<>();}
            cross.setCrossNumber(crossNumber);
            cross.setMaxSize(maxSize);
            hurricaneSpecial.add(cross);

            this.getSelf().level().addFreshEntity(cross);
        }
    }
    public void shootAnkh(CrossfireHurricaneEntity ankh){
        ankh.setPos(this.self.getX(), this.self.getEyeY(), this.self.getZ());
        ankh.setXRot(this.getSelf().getXRot()%360);
        ankh.shootFromRotationDeltaAgnostic(this.getSelf(), this.getSelf().getXRot(), this.getSelf().getYRot(), 1.0F, 1.1F, 0);
    }
    public boolean shootAnkhConfirm(){
        if (!this.self.level().isClientSide()) {
            if (hasHurricaneSpecial()){
                List<CrossfireHurricaneEntity> hurricaneSpecial2 = new ArrayList<>(hurricaneSpecial) {
                };
                if (!hurricaneSpecial2.isEmpty()) {
                    CrossfireHurricaneEntity cfh = hurricaneSpecial2.get(0);
                    if (cfh != null && !cfh.isRemoved() && cfh.isAlive()){
                        cfh.setCrossNumber(0);
                        hurricaneSpecial2.remove(0);
                        shootAnkh(cfh);
                        this.self.level().playSound(null, this.self.blockPosition(),  ModSounds.CROSSFIRE_SHOOT_EVENT,
                                SoundSource.PLAYERS, 2F, 1F);
                    }
                }
                hurricaneSpecial = hurricaneSpecial2;
                return false;
            } else if (hasHurricaneSingle()){
                CrossfireHurricaneEntity cfh = hurricane;
                if (cfh != null && !cfh.isRemoved() && cfh.isAlive()){
                    cfh.setCrossNumber(0);
                    shootAnkh(cfh);
                    hurricane = null;
                    this.self.level().playSound(null, this.self.blockPosition(),  ModSounds.CROSSFIRE_SHOOT_EVENT,
                            SoundSource.PLAYERS, 2F, 1F);
                }
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE,true);
            }
        }
        return true;
    }
    public boolean crossfire(){
        if (!hasHurricaneSingle()) {
            this.animateStand((byte) 15);
            this.poseStand(OffsetIndex.GUARD_FURTHER_RIGHT);
            this.setAttackTimeDuring(0);
            this.setActivePower(PowerIndex.POWER_2);
            if (!this.self.level().isClientSide()) {
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.FIRE_BLAST_EVENT, SoundSource.PLAYERS, 2F, 1.2F);
                playStandUserOnlySoundsIfNearby(CRY_2_NOISE, 27, false,true);
                ticksUntilHurricaneEnds = -1;
                CrossfireHurricaneEntity cross = ModEntities.CROSSFIRE_HURRICANE.create(this.getSelf().level());
                if (cross != null) {
                    cross.absMoveTo(this.getSelf().getX(), this.getSelf().getY(), this.getSelf().getZ());
                    cross.setUser(this.self);
                    cross.setCrossNumber(5);
                    cross.setMaxSize(getChargingCrossfireSize());
                    hurricane = cross;
                    this.getSelf().level().addFreshEntity(cross);
                }
            }
        }
        return true;
    }
    public boolean fireBlast() {
        if (!this.self.level().isClientSide()) {
            this.self.level().playSound(null, this.self.getX(), this.self.getY(),
                    this.self.getZ(), ModSounds.FIRE_BLAST_EVENT, this.self.getSoundSource(), 2.0F, 1F);
            StandEntity stand = this.getStandEntity(this.self);
            if (stand != null && grabBlock != null) {
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 90; j++) {
                        double spd = (1 - ((double) i / 6)) * 0.4;
                        double random = (Math.random() * 14) - 7;
                        double random2 = (Math.random() * 14) - 7;
                        double random3 = (Math.random() * 14) - 7;
                        ((ServerLevel) stand.level()).sendParticles(ModParticles.ORANGE_FLAME, stand.getX(),
                                stand.getY() + stand.getEyeHeight() * 0.8, stand.getZ(),
                                0,
                                (-3 * (stand.getX() - grabBlock.getX()) + 0.5 + random) * spd,
                                (-3 * (stand.getY() - grabBlock.getY()) - 1 + random2) * spd,
                                (-3 * (stand.getZ() - grabBlock.getZ()) + 0.5 + random3) * spd,
                                0.15);
                    }
                }
                burnProjectiles(this.self, DamageHandler.genHitbox(this.self, grabBlock.getX(), grabBlock.getY(),
                        grabBlock.getZ(), 10, 10, 10), 20, 25);
            }
        }
        return true;
    }

    public List<Entity> burnProjectiles(LivingEntity User, List<Entity> entities, float maxDistance, float angle){
        List<Entity> hitEntities = new ArrayList<>(entities) {
        };
        for (Entity value : entities) {
            if (!value.isRemoved() && value instanceof Projectile && !(value instanceof Fireball) && !(value instanceof UnburnableProjectile)){
                if (angleDistance(getLookAtEntityYaw(User, value), (User.getYHeadRot()%360f)) <= angle && angleDistance(getLookAtEntityPitch(User, value), User.getXRot()) <= angle){
                    hitEntities.remove(value);
                    ((ServerLevel) this.self.level()).sendParticles(ParticleTypes.SMOKE, value.getX(),
                            value.getY(), value.getZ(),
                            20,
                            0.1,
                            0.1,
                            0.1,
                            0.03);
                    value.discard();
                }
            }
        }
        return hitEntities;
    }

    public void clearAllHurricanes(){
        clearAllHurricanes(false);
    }
    public void clearAllHurricanes(boolean cancel){
        hurricaneInit();

        List<CrossfireHurricaneEntity> hurricaneSpecial2 = new ArrayList<>(hurricaneSpecial) {
        };
        if (!hurricaneSpecial2.isEmpty()) {
            int totalnumber = hurricaneSpecial2.size();
            for (CrossfireHurricaneEntity value : hurricaneSpecial2) {
                value.discard();
            }
        }
        if (!hurricaneSpecial.isEmpty()) {
            hurricaneSpecial.clear();
        }

        if (hurricane != null){
            hurricane.discard();
        }

        if (!cancel) {
            if (this.getSelf() instanceof ServerPlayer && this.isChargingCrossfireSingle()) {
                ModPacketHandler.PACKET_ACCESS.syncSkillCooldownPacket(((ServerPlayer) this.getSelf()),
                        PowerIndex.SKILL_2, 60);
            }
        }
    }
    public boolean snap(){
        this.self.level().playSound(null, this.self.getX(), this.self.getY(),
                this.self.getZ(), ModSounds.SNAP_EVENT, this.self.getSoundSource(), 2.0F, 1F);
        this.setCooldown(PowerIndex.SKILL_3, ClientNetworking.getAppropriateConfig().cooldownsInTicks.magicianSnapFireAway);
        this.snapNumber++;
        clearAllHurricanes();
        return true;
    }

    public boolean setFire(){
        if (grabBlock != null && tryPlaceBlock(grabBlock)){
            this.self.level().playSound(null, this.self.getX(), this.self.getY(),
                    this.self.getZ(), ModSounds.FIRE_WHOOSH_EVENT, this.self.getSoundSource(), 2.0F, 2F);
            for (int j = 0; j < 10; j++) {
                double random = (Math.random() * 0.8) - 0.4;
                double random2 = (Math.random() * 0.8) - 0.4;
                double random3 = (Math.random() * 0.8) - 0.4;
                ((ServerLevel) this.self.level()).sendParticles(ModParticles.ORANGE_FLAME, this.self.getX(),
                        this.self.getY() + this.self.getEyeHeight()*0.7, this.self.getZ(),
                        0,
                        -1*(this.self.getX() - grabBlock.getX())+0.5 + random,
                        -1*(this.self.getY() - grabBlock.getY())-0.5 + random2,
                        -1*(this.self.getZ() - grabBlock.getZ())+0.5 + random3,
                        0.15);
            }
            createStandFire(grabBlock);
        }
        return true;
    }
    public void createStandFire2(BlockPos pos){
        if (pos != null && tryPlaceBlock(pos)){
            createStandFire(pos);
        }
    }

    public int getNewFireId(){
        this.fireIDNumber++;
        return this.fireIDNumber;
    }

    public void createStandFire(BlockPos pos){
        this.fireIDNumber++;
        this.getSelf().level().setBlockAndUpdate(pos, ((StandFireBlock)ModBlocks.STAND_FIRE).getStateForPlacement(this.self.level(),pos));
        BlockEntity be = this.self.level().getBlockEntity(pos);
        if (be instanceof StandFireBlockEntity sfbe){
            sfbe.standUser = this.self;
            sfbe.snapNumber = this.snapNumber;
            sfbe.fireIDNumber = this.fireIDNumber;
            sfbe.fireColorType = getFireColor();
        }
    }

    public byte getFireColor(){
        return StandFireType.ORANGE.id;
    }

    @Override
    public float getReach(){
        return 7;
    }


    public float getHurricaneDirectDamage(Entity entity, CrossfireHurricaneEntity cfh, float size){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod((float) (0.5+((size/60)* 6) * (ClientNetworking.getAppropriateConfig().
                    damageMultipliers.magicianAttackOnPlayers*0.01)));
        } else {
            return levelupDamageMod((float) (1+(((size)/60)* 16) * (ClientNetworking.getAppropriateConfig().
                    damageMultipliers.magicianAttackOnMobs*0.01)));
        }
    }
    public float getHurricaneDamage(Entity entity, CrossfireHurricaneEntity cfh, float size){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod((float) (0.5+((size/60)* 3) * (ClientNetworking.getAppropriateConfig().
                    damageMultipliers.magicianAttackOnPlayers*0.01)));
        } else {
            return levelupDamageMod((float) (1+((size/60)* 9) * (ClientNetworking.getAppropriateConfig().
                    damageMultipliers.magicianAttackOnMobs*0.01)));
        }
    }


    @Override
    public float getPunchStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod((float) ((float) 1.25* (ClientNetworking.getAppropriateConfig().
                    damageMultipliers.magicianAttackOnPlayers*0.01)));
        } else {
            return levelupDamageMod((float) ((float) 3.5* (ClientNetworking.getAppropriateConfig().
                    damageMultipliers.magicianAttackOnMobs*0.01)));
        }
    }
    @Override
    public float getHeavyPunchStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod((float) ((float) 1.75* (ClientNetworking.getAppropriateConfig().
                    damageMultipliers.magicianAttackOnPlayers*0.01)));
        } else {
            return levelupDamageMod((float) ((float) 4.5* (ClientNetworking.getAppropriateConfig().
                    damageMultipliers.magicianAttackOnMobs*0.01)));
        }
    }
    @Override
    public void updateAttack(){
        if (this.attackTimeDuring > -1) {
            if (this.attackTimeDuring > this.attackTimeMax) {
                this.attackTime = -1;
                this.attackTimeMax = 0;
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE,true);
            } else {
                if ((this.attackTimeDuring == 7 && this.activePowerPhase == 1)
                        || this.attackTimeDuring == 8) {
                    this.standPunch();
                }
            }
        }
    }
    @Override
    public void standPunch(){

        if (this.self instanceof Player){
            if (isPacketPlayer()){
                this.attackTimeDuring = -10;

                Entity targetEntity = getTargetEntity(this.self,-1);

                List<Entity> listE = getTargetEntityList(this.self,-1);
                int id = -1;
                if (targetEntity != null){
                    id = targetEntity.getId();
                }
                ModPacketHandler.PACKET_ACCESS.StandPunchPacket(id, this.activePowerPhase);
                if (!listE.isEmpty() && ClientNetworking.getAppropriateConfig().barrageHasAreaOfEffect){
                    for (int i = 0; i< listE.size(); i++){
                        if (!(targetEntity != null && listE.get(i).is(targetEntity))) {
                            if (!(listE.get(i) instanceof StandEntity) && listE.get(i).distanceTo(this.self) < 3.5) {
                                ModPacketHandler.PACKET_ACCESS.StandPunchPacket(listE.get(i).getId(), (byte) (this.activePowerPhase + 50));
                            }
                        }
                    }
                }
            }
        } else {
            /*Caps how far out the punch goes*/

            Entity targetEntity = getTargetEntity(this.self,-1);

            List<Entity> listE = getTargetEntityList(this.self,-1);
            punchImpact(targetEntity);
            if (!listE.isEmpty()){
                for (int i = 0; i< listE.size(); i++){
                    if (!(storeEnt != null && listE.get(i).is(storeEnt))) {
                        if (!(listE.get(i) instanceof StandEntity) && listE.get(i).distanceTo(this.self) < 3.5) {
                            this.setActivePowerPhase((byte) (this.getActivePowerPhase()+50));
                            punchImpact(listE.get(i));
                        }
                    }
                }
            }
        }

    }
    @Override
    public boolean setPowerAttack(){
        if (this.activePowerPhase >= 3){
            this.activePowerPhase = 1;
        } else {
            this.activePowerPhase++;
            if (this.activePowerPhase == 3) {
                this.attackTimeMax= ClientNetworking.getAppropriateConfig().cooldownsInTicks.magicianLastLashInString;
            } else {
                this.attackTimeMax= ClientNetworking.getAppropriateConfig().cooldownsInTicks.magicianLash;
            }

        }

        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.ATTACK);
        this.setAttackTime(0);

        animateStand(this.activePowerPhase);
        poseStand(OffsetIndex.ATTACK);
        return true;
    }
    boolean splash = false;
    @Override
    public void punchImpact(Entity entity){
        if (this.getActivePowerPhase() >= 50){
            this.setActivePowerPhase((byte) (this.getActivePowerPhase()-50));
            splash = true;
        } else {
            splash = false;
        }
        this.setAttackTimeDuring(-10);
        if (entity != null) {
            float pow;
            float knockbackStrength;
            boolean lasthit = false;
            if (this.getActivePowerPhase() >= this.getActivePowerPhaseMax()) {
                /*The last hit in a string has more power and knockback if you commit to it*/
                pow = getHeavyPunchStrength(entity);
                knockbackStrength = 0.2F;
                lasthit = true;
            } else {
                pow = getPunchStrength(entity);
                knockbackStrength = 0.14F;
            }

            if (splash){
                pow/=4;
            }
            if (StandDamageEntityAttack(entity, pow, 0, this.self)) {
                if (entity instanceof LivingEntity LE){

                    if (lasthit){addEXP(2,LE);} else {addEXP(1,LE);}
                }

                this.takeDeterminedKnockback(this.self, entity, knockbackStrength);
            } else {
                if (this.activePowerPhase >= this.activePowerPhaseMax) {
                    knockShield2(entity, 40);
                }
            }
        } else {
            // This is less accurate raycasting as it is server sided but it is important for particle effects
            float distMax = this.getDistanceOut(this.self, this.getReach(), false);
            float halfReach = (float) (distMax * 0.5);
            Vec3 pointVec = DamageHandler.getRayPoint(self, halfReach);
            if (!this.self.level().isClientSide) {
                ((ServerLevel) this.self.level()).sendParticles(ParticleTypes.SMOKE, pointVec.x, pointVec.y, pointVec.z,
                        10, 0.2, 0.2, 0.2, 0.1);
            }
        }

        if (!splash) {
            SoundEvent SE;
            float pitch = 1F;
            if (this.activePowerPhase >= this.activePowerPhaseMax) {

                if (!this.self.level().isClientSide()) {
                    Byte LastHitSound = this.getLastHitSound();
                    this.playStandUserOnlySoundsIfNearby(LastHitSound, 15, false,
                            true);
                }

                if (entity != null) {
                    SE = ModSounds.FIRE_STRIKE_LAST_EVENT;
                } else {
                    SE = ModSounds.FIRE_WHOOSH_EVENT;
                }
            } else {
                if (entity != null) {
                    SE = ModSounds.FIRE_STRIKE_EVENT;
                    pitch = 0.99F + 0.02F * activePowerPhase;
                } else {
                    SE = ModSounds.FIRE_WHOOSH_EVENT;
                }
            }

            if (!this.self.level().isClientSide()) {
                this.self.level().playSound(null, this.self.blockPosition(), SE, SoundSource.PLAYERS, 0.95F, pitch);
            }
        }
    }
    @Override
    public boolean isAttackIneptVisually(byte activeP, int slot){
        if (this.isChargingCrossfireSpecial() || (slot != 2 && (isChargingCrossfireSingle() || hasHurricaneSingle()))){
            return true;
        }
        return super.isAttackIneptVisually(activeP,slot);
    }
}
