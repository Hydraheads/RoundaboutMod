package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.access.IAbstractArrowAccess;
import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.access.IPermaCasting;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.block.StandFireBlock;
import net.hydra.jojomod.block.StandFireBlockEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.UnburnableProjectile;
import net.hydra.jojomod.entity.pathfinding.GroundHurricaneEntity;
import net.hydra.jojomod.entity.projectile.*;
import net.hydra.jojomod.entity.stand.MagiciansRedEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.substand.LifeTrackerEntity;
import net.hydra.jojomod.entity.visages.mobs.AvdolNPC;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.ModGamerules;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.PermanentZoneCastInstance;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewPunchingStand;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PowersMagiciansRed extends NewPunchingStand {

    public CrossfireHurricaneEntity hurricane;

    public CrossfireHurricaneEntity bigAnkh;
    public List<CrossfireHurricaneEntity> hurricaneSpecial = new ArrayList<>();
    public static int getChargingCrossfireSpecial(){
        return 26;
    }
    public static int getChargingCrossfireSpecialSize(){
        return 26;
    }
    public static int getChargingCrossfire(){
        return 52;
    }

    public static int getChargingCrossfireSize(){
        return 52;
    }
    public static int getKamikazeSize(){
        return 120;
    }
    public static int getMassiveCrossfireSize(){
        return 500;
    }

    @Override
    public float getShovelMiningSpeed() {
        return 9F;
    }
    public float bumpDamage(float baseDamage, boolean isActive){
        if (isActive) {
            return baseDamage * 1.4F;
        }
        return baseDamage;
    }
    public int multiplyCooldown(int cooldown){
        if (isUsingFirestorm()){
            return (int)(cooldown*2);
        }
        return cooldown;
    }

    @Override
    public Component getSkinName(byte skinId) {
        return getSkinNameT(skinId);
    }
    public static Component getSkinNameT(byte skinId){
        if (skinId == MagiciansRedEntity.PART_3_SKIN){
            return Component.translatable(  "skins.roundabout.magicians_red.base");
        } else if (skinId == MagiciansRedEntity.BLUE_SKIN){
            return Component.translatable(  "skins.roundabout.magicians_red.blue");
        } else if (skinId == MagiciansRedEntity.PURPLE_SKIN){
            return Component.translatable(  "skins.roundabout.magicians_red.purple");
        } else if (skinId == MagiciansRedEntity.GREEN_SKIN){
            return Component.translatable(  "skins.roundabout.magicians_red.green");
        } else if (skinId == MagiciansRedEntity.DREAD_SKIN){
            return Component.translatable(  "skins.roundabout.magicians_red.dread");
        } else if (skinId == MagiciansRedEntity.BLUE_ACE_SKIN){
            return Component.translatable(  "skins.roundabout.magicians_red.blue_ace");
        } else if (skinId == MagiciansRedEntity.DREAD_BEAST_SKIN){
            return Component.translatable(  "skins.roundabout.magicians_red.chagaroth");
        } else if (skinId == MagiciansRedEntity.MAGMA_SKIN){
            return Component.translatable(  "skins.roundabout.magicians_red.magma");
        } else if (skinId == MagiciansRedEntity.LIGHTER_SKIN){
            return Component.translatable(  "skins.roundabout.magicians_red.lighter");
        } else if (skinId == MagiciansRedEntity.MANGA_SKIN){
            return Component.translatable(  "skins.roundabout.magicians_red.manga");
        } else if (skinId == MagiciansRedEntity.OVA_SKIN){
            return Component.translatable(  "skins.roundabout.magicians_red.ova");
        } else if (skinId == MagiciansRedEntity.ABLAZE){
            return Component.translatable(  "skins.roundabout.magicians_red.ablaze");
        } else if (skinId == MagiciansRedEntity.LIGHTER_ABLAZE){
            return Component.translatable(  "skins.roundabout.magicians_red.lighter_ablaze");
        } else if (skinId == MagiciansRedEntity.BLUE_ABLAZE){
            return Component.translatable(  "skins.roundabout.magicians_red.blue_ablaze");
        } else if (skinId == MagiciansRedEntity.PURPLE_ABLAZE){
            return Component.translatable(  "skins.roundabout.magicians_red.purple_ablaze");
        } else if (skinId == MagiciansRedEntity.GREEN_ABLAZE){
            return Component.translatable(  "skins.roundabout.magicians_red.green_ablaze");
        } else if (skinId == MagiciansRedEntity.DREAD_ABLAZE){
            return Component.translatable(  "skins.roundabout.magicians_red.dread_ablaze");
        } else if (skinId == MagiciansRedEntity.SIDEKICK){
            return Component.translatable(  "skins.roundabout.magicians_red.sidekick");
        } else if (skinId == MagiciansRedEntity.BETA){
            return Component.translatable(  "skins.roundabout.magicians_red.beta");
        } else if (skinId == MagiciansRedEntity.JOJONIUM){
            return Component.translatable(  "skins.roundabout.magicians_red.jojonium");
        } else if (skinId == MagiciansRedEntity.JOJONIUM_ABLAZE){
            return Component.translatable(  "skins.roundabout.magicians_red.jojonium_ablaze");
        } else if (skinId == MagiciansRedEntity.DEBUT_SKIN){
            return Component.translatable(  "skins.roundabout.magicians_red.debut");
        } else if (skinId == MagiciansRedEntity.SKELETAL){
            return Component.translatable(  "skins.roundabout.magicians_red.skeletal");
        }
        return Component.translatable(  "skins.roundabout.magicians_red.base");
    }

    @Override
    public int getMaxGuardPoints(){
        return ClientNetworking.getAppropriateConfig().magiciansRedSettings.magiciansRedGuardPoints;
    }
    public void tickPower() {
        if (!this.self.level().isClientSide()) {
            if (leaded != null) {
                if (!hasStandActive(this.self)){
                    clearLeaded();
                }
            }
        }
        super.tickPower();

        offloadLead();
        if (!this.self.level().isClientSide()) {
            if (this.activePower == PowerIndex.MINING){
                spewFlamesMining();
            }
            if (leaded != null) {
                if (((leaded instanceof Enemy) ||
                        (leaded instanceof Mob mb && mb.getTarget() != null && mb.getTarget().is(this.self))) ||
                        (leaded instanceof Player) ||
                        !((StandUser)leaded).roundabout$getStandDisc().isEmpty()){
                    lassoTime--;
                    if (lassoTime <= -1){
                        ((StandUser)leaded).roundabout$dropString();
                        leaded = null;
                        if (this.self instanceof ServerPlayer SP) {
                            S2CPacketUtil.sendCooldownSyncPacket(SP, PowerIndex.SKILL_1,
                                    getRedBindMissCooldown());
                        }
                    }
                }

                if (leaded != null){
                    if (drillTime > -1){
                        drillTime--;
                        if (drillTime <= -1){
                            clearLeaded();
                        } else {
                            redBindDrill();
                        }
                    }
                }


                if (leaded != null){
                    if (leaded.isInWaterOrRain() && this.self.isInWaterOrRain()){
                        clearLeaded();
                    }
                }

                if (leaded !=null) {
                    ItemStack useItem = this.self.getUseItem();
                    if (useItem.is(ModItems.NEW_LOCACACA)){
                        clearLeaded();
                    }
                }
            } else {
                drillTime = -1;
                drillT = false;
                lassoTime = -1;
            }


            if (ticksUntilHurricaneEnds > -1) {
                ticksUntilHurricaneEnds--;
                if (ticksUntilHurricaneEnds <= -1) {
                    if (hasHurricane()) {
                        this.self.level().playSound(null, this.self.blockPosition(), SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 2F, 0.8F);
                        ((ServerLevel) this.self.level()).sendParticles(getFlameParticle(), this.self.getX(),
                                this.self.getY() + (this.self.getBbHeight() * 0.5), this.self.getZ(),
                                20,
                                1.2, 1.2, 1.2,
                                0.005);
                    }
                    clearAllHurricanes();

                    if (this.isChargingCrossfireSingle()){
                        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE,true);
                    }
                }
            }

            if (isLockedByWater()){
                clearAllHurricanes();
                if (isChargingCrossfire()){
                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE,true);
                }
            }
            if ((isChargingCrossfireSingle() && (hurricane == null || hurricane.isRemoved()))
            || isChargingCrossfireSpecial() && (hurricaneSpecial == null || hurricaneSpecial.isEmpty())){
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE,true);
            }
        }
    }

    public int getRedBindMissCooldown(){
        return ClientNetworking.getAppropriateConfig().magiciansRedSettings.redBindFailOrMissCooldown;
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
        if (bigAnkh != null){
            if (bigAnkh.isRemoved()){
                bigAnkh =null;
            } else {
                transformGiantHurricane(bigAnkh);
            }

        } else {
            if (isUsingFirestorm()){
                if (!this.self.level().isClientSide()) {
                    removeFirestorm();
                }
            }
        }

        if (!this.self.level().isClientSide()) {
            if (this.isInRain() && !this.self.isUnderWater() && isUsingFirestorm()) {
                if (this.self.tickCount % 2 == 0) {
                    ((ServerLevel) this.self.level()).sendParticles(ParticleTypes.SMOKE, this.self.getX(),
                            this.self.getY() + this.self.getBbHeight(), this.self.getZ(),
                            2,
                            0.2,
                            0,
                            0.2,
                            0.01);
                }
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
                if (value.isRemoved() || !value.isAlive() || value.getCrossNumber() <= 0  || (this.self.level().isClientSide() && this.self.level().getEntity(value.getId()) == null)) {
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
        } else if (this.activePower == PowerIndex.SNEAK_ATTACK_CHARGE) {
            if (this.getSelf().isCrouching()) {
                float f = Mth.clamp(0.3F + EnchantmentHelper.getSneakingSpeedBonus(this.getSelf()), 0.0F, 1.0F);
                float g = 1 / f;
                basis *= g;
            }
            basis *= 0.3f;
        } else if (this.activePower == PowerIndex.RANGED_BARRAGE_2) {
            if (this.getSelf().isCrouching()) {
                float f = Mth.clamp(0.3F + EnchantmentHelper.getSneakingSpeedBonus(this.getSelf()), 0.0F, 1.0F);
                float g = 1 / f;
                basis *= g;
            }
            basis *= 0.75f;
        } else if (this.activePower == PowerIndex.RANGED_BARRAGE || this.activePower == PowerIndex.RANGED_BARRAGE_CHARGE){
            basis *= 0.5f;
        } else if (this.getActivePower()==PowerIndex.POWER_1){
            if (this.getSelf().isCrouching()){
                float f = Mth.clamp(0.3F + EnchantmentHelper.getSneakingSpeedBonus(this.getSelf()), 0.0F, 1.0F);
                float g = 1/f;
                basis *= g;
            }
            basis *= 0.85f;
        } else if (this.getActivePower()==PowerIndex.POWER_4_BONUS){
            basis *= 0.1f;
        }

        if (isUsingFirestorm()){
            basis *= 0.7f;
        }
        return super.inputSpeedModifiers(basis);
    }

    public SoundEvent getKickAttackSound(){
        return ModSounds.FINAL_KICK_EVENT;
    }
    @Override
    public boolean cancelSprintJump(){
        if (this.hasHurricane() || isChargingCrossfire() || this.getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE
                || this.getActivePower() == PowerIndex.RANGED_BARRAGE_2 || this.getActivePower() == PowerIndex.RANGED_BARRAGE_CHARGE_2
                || this.getActivePower() == PowerIndex.RANGED_BARRAGE || this.getActivePower() == PowerIndex.RANGED_BARRAGE_CHARGE
                || this.getActivePower() == PowerIndex.POWER_4_BONUS){
            return true;
        }
        return super.cancelSprintJump();
    }
    public void transformGiantHurricane(CrossfireHurricaneEntity value){
        int size = value.getSize();
        if (size < value.getMaxSize()) {
            size += value.getAccrualRate();
            value.setSize(size);
        }
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


            if (!this.self.level().isClientSide()) {
                value.setOldPosAndRot();
                //Roundabout.LOGGER.info("bye");
            }

            Vec3 finalOffset = new Vec3(
                    -(-1 * (distanceOut * (Math.sin(offset / 180)))),
                    distanceUp,
                    -(distanceOut * (Math.cos(offset / 180)))
            );

            Direction dir = ((IGravityEntity)self).roundabout$getGravityDirection();
            if (dir != Direction.DOWN){
                finalOffset = RotationUtil.vecPlayerToWorld(finalOffset,dir);
            }
            double x1 = entityX + finalOffset.x;
            double y1 = entityY + finalOffset.y;
            double z1 = entityZ + finalOffset.z;
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
    /**Override to add disable config*/
    public boolean isStandEnabled(){
        return ClientNetworking.getAppropriateConfig().magiciansRedSettings.enableMagiciansRed;
    }

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        boolean secondSkillLocked =hasHurricaneSpecial() || this.isChargingCrossfire() || hasHurricaneSingle();
        boolean hasSingle =isChargingCrossfireSingle() || hasHurricaneSingle();

            boolean candoNumber1 = true;
            if (isHoldingSneak()) {
                if (secondSkillLocked){
                    if (canShootConcealedCrossfire()){
                        setSkillIcon(context, x, y, 1, StandIcons.CONCEALED_HURRICANE, PowerIndex.NO_CD);
                        candoNumber1 = false;
                    }
                    setSkillIcon(context, x, y, 2, StandIcons.CROSSFIRE_HURRICANE_SHOT, PowerIndex.NO_CD);
                } else {
                    if (canExecuteMoveWithLevel(4)) {
                        setSkillIcon(context, x, y, 2, StandIcons.CROSSFIRE_HURRICANE_SPECIAL, PowerIndex.SKILL_2_SNEAK);
                    } else {
                        setSkillIcon(context, x, y, 2, StandIcons.LOCKED, PowerIndex.NO_CD,true);
                    }
                }
                if (candoNumber1) {

                    if (this.isGuarding()) {
                        if (canExecuteMoveWithLevel(5)) {
                            setSkillIcon(context, x, y, 1, StandIcons.LIFE_TRACKER, PowerIndex.SKILL_1);
                        } else {
                            setSkillIcon(context, x, y, 1, StandIcons.LOCKED, PowerIndex.NO_CD,true);
                        }
                    } else {
                        setSkillIcon(context, x, y, 1, StandIcons.LIGHT_FIRE, PowerIndex.SKILL_1_SNEAK);
                    }
                }

                if (!this.getSelf().onGround() && canVault()){
                    setSkillIcon(context, x, y, 3, StandIcons.MAGICIANS_RED_LEDGE_GRAB, PowerIndex.GLOBAL_DASH);
                } else if (this.isGuarding()) {
                    if (canExecuteMoveWithLevel(2)) {
                        setSkillIcon(context, x, y, 3, StandIcons.PROJECTILE_BURN, PowerIndex.SKILL_EXTRA);
                    } else {
                        setSkillIcon(context, x, y, 3, StandIcons.LOCKED, PowerIndex.NO_CD,true);
                    }
                } else if (hasSingle){
                    if (canExecuteMoveWithLevel(3)) {
                        setSkillIcon(context, x, y, 3, StandIcons.HIDDEN_HURRICANE, PowerIndex.SKILL_EXTRA);
                    } else {
                        setSkillIcon(context, x, y, 3, StandIcons.LOCKED, PowerIndex.NO_CD,true);
                    }
                } else {
                    setSkillIcon(context, x, y, 3, StandIcons.SNAP_ICON, PowerIndex.SKILL_3);
                }
                if (canExecuteMoveWithLevel(6)) {
                    setSkillIcon(context, x, y, 4, StandIcons.FIRE_SLAM, PowerIndex.SKILL_4);
                } else {
                    setSkillIcon(context, x, y, 4, StandIcons.LOCKED, PowerIndex.NO_CD,true);
                }
            } else {
                if (secondSkillLocked){
                    if (canShootConcealedCrossfire()){
                        setSkillIcon(context, x, y, 1, StandIcons.CONCEALED_HURRICANE, PowerIndex.NO_CD);
                        candoNumber1 = false;
                    }
                    setSkillIcon(context, x, y, 2, StandIcons.CROSSFIRE_HURRICANE_SHOT, PowerIndex.NO_CD);
                } else {
                    setSkillIcon(context, x, y, 2, StandIcons.CROSSFIRE_HURRICANE, PowerIndex.SKILL_2);
                }
                if (candoNumber1) {

                    if (this.isGuarding()) {
                        if (canExecuteMoveWithLevel(5)) {
                            setSkillIcon(context, x, y, 1, StandIcons.LIFE_TRACKER, PowerIndex.NO_CD);
                        } else {
                            setSkillIcon(context, x, y, 1, StandIcons.LOCKED, PowerIndex.NO_CD,true);
                        }
                    } else {
                        setSkillIcon(context, x, y, 1, StandIcons.RED_BIND, PowerIndex.SKILL_1);
                    }
                }

                if (!this.getSelf().onGround() && canVault()){
                    setSkillIcon(context, x, y, 3, StandIcons.MAGICIANS_RED_LEDGE_GRAB, PowerIndex.GLOBAL_DASH);
                } else if (this.isGuarding()){
                    if (canExecuteMoveWithLevel(2)) {
                        setSkillIcon(context, x, y, 3, StandIcons.PROJECTILE_BURN, PowerIndex.SKILL_EXTRA);
                    } else {
                        setSkillIcon(context, x, y, 3, StandIcons.LOCKED, PowerIndex.NO_CD,true);
                    }
                } else if (hasSingle){
                    if (canExecuteMoveWithLevel(3)) {
                        setSkillIcon(context, x, y, 3, StandIcons.HIDDEN_HURRICANE, PowerIndex.SKILL_EXTRA);
                    } else {
                        setSkillIcon(context, x, y, 3, StandIcons.LOCKED, PowerIndex.NO_CD,true);
                    }
                } else {
                    setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
                }

                if (canExecuteMoveWithLevel(7)) {
                    if (isUsingFirestorm()) {
                        setSkillIcon(context, x, y, 4, StandIcons.CROSSFIRE_FIRESTORM_END, PowerIndex.NO_CD);
                    } else {
                        setSkillIcon(context, x, y, 4, StandIcons.CROSSFIRE_FIRESTORM, PowerIndex.NO_CD);
                    }
                } else {
                    setSkillIcon(context, x, y, 4, StandIcons.LOCKED, PowerIndex.NO_CD,true);
                }
            }
    }

    public boolean isUsingFirestorm(){
        return ((IPermaCasting) this.getSelf().level()).roundabout$isPermaCastingEntity(this.self);
    }

    public boolean canShootConcealedCrossfire(){
        return hasHurricaneSingle() && !this.self.getMainHandItem().isEmpty() && MainUtil.isThrownBlockItem(this.self.getMainHandItem().getItem());
    }


    @Override
    public int getExpForLevelUp(int currentLevel){
        int amt;
        if (currentLevel == 1){
            amt = 100;
        } else {
            amt = (100+((currentLevel-1)*50));
        }
        amt= (int) (amt*getLevelMultiplier());
        return amt;
    }

    public void playFlamethrowerSound(){
        if (!this.self.level().isClientSide()) {
            byte barrageCrySound;
            barrageCrySound = FLAMETHROWER_NOISE;
            playStandUserOnlySoundsIfNearby(barrageCrySound, 32, false,true);
        }
    }
    public boolean isRangedBarrageCharging(){
        return (this.activePower == PowerIndex.RANGED_BARRAGE_CHARGE);
    }
    public boolean isRangedBarrage2Charging(){
        return (this.activePower == PowerIndex.RANGED_BARRAGE_CHARGE_2);
    }
    public boolean isRangedBarraging(){
        return (this.activePower == PowerIndex.RANGED_BARRAGE || this.activePower == PowerIndex.RANGED_BARRAGE_CHARGE);
    }
    public int getRangedBarrageWindup(){
        return ClientNetworking.getAppropriateConfig().magiciansRedSettings.magiciansRedFireballsWindup;
    }
    public int getRangedBarrageWindup2(){
        return ClientNetworking.getAppropriateConfig().magiciansRedSettings.magiciansRedFlamethrowerWindup;
    }

    public int getRangedBarrageLength(){
        return 18;
    }
    public boolean isReadyToShoot(){
        return (attackTimeDuring == 2 || attackTimeDuring == 6 || attackTimeDuring == 10
                || attackTimeDuring == 14 || attackTimeDuring == 18);
    }

    public int getRangedBarrage2Length(){
        return 60;
    }

    @Override
    public void renderAttackHud(GuiGraphics context, Player playerEntity,
                                int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                float flashAlpha, float otherFlashAlpha) {
        StandUser standUser = ((StandUser) playerEntity);
        StandPowers powers = standUser.roundabout$getStandPowers();
        boolean standOn = PowerTypes.hasStandActive(playerEntity);
        int j = scaledHeight / 2 - 7 - 4;
        int k = scaledWidth / 2 - 8;
        if (standOn && this.getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE) {
            int ClashTime = Math.min(15, Math.round(((float) attackTimeDuring / maxSuperHitTime) * 15));
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 30, ClashTime, 6);

        } else if (standOn && this.getActivePower() == PowerIndex.RANGED_BARRAGE_2 && attackTimeDuring > -1) {
                int ClashTime = 15 - Math.round(((float) attackTimeDuring / this.getRangedBarrage2Length()) * 15);
                context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
                context.blit(StandIcons.JOJO_ICONS, k, j, 193, 30, ClashTime, 6);
        } else if (standOn && this.getActivePower() == PowerIndex.RANGED_BARRAGE && attackTimeDuring > -1) {
            int ClashTime = 15 - Math.round(((float) attackTimeDuring / this.getRangedBarrageLength()) * 15);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 30, ClashTime, 6);
        } else if (standOn && isRangedBarrage2Charging()) {
            int ClashTime = Math.round(((float) attackTimeDuring / getRangedBarrageWindup2()) * 15);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 30, ClashTime, 6);

        } else if (standOn && isRangedBarrageCharging()) {
            int ClashTime = Math.round(((float) attackTimeDuring / getRangedBarrageWindup()) * 15);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 30, ClashTime, 6);
        } else if (this.getActivePower() == PowerIndex.POWER_1){
            Entity TE = this.getTargetEntity(playerEntity, 4F);
            if (TE != null) {
                context.blit(StandIcons.JOJO_ICONS, k, j, 193, 0, 15, 6);
            }
        } else {
            super.renderAttackHud(context,playerEntity,
                    scaledWidth,scaledHeight,ticks,vehicleHeartCount, flashAlpha, otherFlashAlpha);
        }
    }
    @Override
    public StandEntity getNewStandEntity(){
        if (((StandUser)this.getSelf()).roundabout$getStandSkin() == MagiciansRedEntity.OVA_SKIN){
            return ModEntities.MAGICIANS_RED_OVA.create(this.getSelf().level());
        }
        return ModEntities.MAGICIANS_RED.create(this.getSelf().level());
    }
    @Override
    public List<Byte> getSkinList(){
        List<Byte> $$1 = Lists.newArrayList();
        $$1.add(MagiciansRedEntity.PART_3_SKIN);
        $$1.add(MagiciansRedEntity.ABLAZE);
        if (this.getSelf() instanceof Player PE){
            byte Level = ((IPlayerEntity)PE).roundabout$getStandLevel();
            ItemStack goldDisc = ((StandUser)PE).roundabout$getStandDisc();
            boolean bypass = PE.isCreative() || (!goldDisc.isEmpty() && goldDisc.getItem() instanceof MaxStandDiscItem);
            if (Level > 1 || bypass){
                $$1.add(MagiciansRedEntity.MANGA_SKIN);
                $$1.add(MagiciansRedEntity.LIGHTER_SKIN);
                $$1.add(MagiciansRedEntity.LIGHTER_ABLAZE);
            } if (Level > 2 || bypass){
                $$1.add(MagiciansRedEntity.OVA_SKIN);
                $$1.add(MagiciansRedEntity.SIDEKICK);
            } if (Level > 3 || bypass){
                $$1.add(MagiciansRedEntity.PURPLE_SKIN);
                $$1.add(MagiciansRedEntity.PURPLE_ABLAZE);
            } if (Level > 4 || bypass){
                $$1.add(MagiciansRedEntity.GREEN_SKIN);
                $$1.add(MagiciansRedEntity.GREEN_ABLAZE);
            } if (Level > 5 || bypass){
                $$1.add(MagiciansRedEntity.BLUE_SKIN);
                $$1.add(MagiciansRedEntity.BLUE_ABLAZE);
                $$1.add(MagiciansRedEntity.BLUE_ACE_SKIN);
                $$1.add(MagiciansRedEntity.SKELETAL);
            } if (Level > 6 || bypass){
                $$1.add(MagiciansRedEntity.JOJONIUM);
                $$1.add(MagiciansRedEntity.JOJONIUM_ABLAZE);
                $$1.add(MagiciansRedEntity.MAGMA_SKIN);
                $$1.add(MagiciansRedEntity.DEBUT_SKIN);
                $$1.add(MagiciansRedEntity.BETA);
            } if (((IPlayerEntity)PE).roundabout$getUnlockedBonusSkin() || bypass){
                $$1.add(MagiciansRedEntity.DREAD_BEAST_SKIN);
                $$1.add(MagiciansRedEntity.DREAD_SKIN);
                $$1.add(MagiciansRedEntity.DREAD_ABLAZE);
            }
        }
        return $$1;
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
        } else if (soundChoice == CRY_3_NOISE) {
            return ModSounds.MAGICIANS_RED_CRY_3_EVENT;
        } else if (soundChoice == RANGED_CHARGE_1) {
            return ModSounds.MAGICIANS_RED_CHARGE_EVENT;
        } else if (soundChoice == RANGED_CHARGE_2) {
            return ModSounds.MAGICIANS_RED_CHARGE_EVENT;
        } else if (soundChoice == FLAMETHROWER_NOISE) {
            return ModSounds.FLAMETHROWER_EVENT;
        } else if (soundChoice == FIRESTORM) {
            return ModSounds.FIRESTORM_EVENT;
        }  else if (soundChoice == BIND_CHARGE_NOISE) {
            return ModSounds.IMPALE_CHARGE_EVENT;
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
            if (this.self.level().getBlockState(grabBlock2).is(Blocks.CAMPFIRE)){
                return blockHit.getBlockPos();
            } if (this.self.level().getBlockState(grabBlock2).is(Blocks.CANDLE)){
                return blockHit.getBlockPos();
            } if (this.self.level().getBlockState(grabBlock2).getBlock() instanceof TntBlock){
                return blockHit.getBlockPos();
            }
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

    public boolean isBusy(){
        return this.activePower == PowerIndex.POWER_4 || this.activePower == PowerIndex.POWER_4_BONUS;
    }

    @Override
    public void powerActivate(PowerContext context) {
        switch (context) {
            case SKILL_1_NORMAL -> {
                tryRedBindClient();
            }
            case SKILL_1_CROUCH -> {
                tryIgnitionClient();
            }
            case SKILL_1_GUARD, SKILL_1_CROUCH_GUARD -> {
                tryLifeTrackerClient();
            }

            case SKILL_2_NORMAL -> {
                hurricaneClient();
            }
            case SKILL_2_CROUCH -> {
                hurricaneSpecialClient();
            }

            case SKILL_3_NORMAL -> {
                tryToDashClient();
            }
            case SKILL_3_CROUCH -> {
                tryToSnapFireAway();
            }
            case SKILL_3_GUARD, SKILL_3_CROUCH_GUARD -> {
                destroyProjectilesClient();
            }

            case SKILL_4_NORMAL -> {
                bigHurricaneClient();
            }
            case SKILL_4_CROUCH -> {
                cawFireSlamClientCaw();
            }
        }
    }

    public boolean useHurricaneSpecialClient(){
        if (hasHurricaneSpecial()) {
            if (!isChargingCrossfire()) {
                if (!this.onCooldown(PowerIndex.SKILL_EXTRA_2)) {
                    this.setCooldown(PowerIndex.SKILL_EXTRA_2, 8);
                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_2_BONUS, true);
                    tryPowerPacket(PowerIndex.POWER_2_BONUS);
                    return true;
                }
            }
        }
        return false;
    }
    public boolean ankhShootClient(){
        if (hasHurricaneSingle() || isChargingCrossfireSingle()) {
            this.setCooldown(PowerIndex.SKILL_2, multiplyCooldown(ClientNetworking.getAppropriateConfig().magiciansRedSettings.ankhSuccessCooldown));
            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_2_BONUS, true);
            tryPowerPacket(PowerIndex.POWER_2_BONUS);
            return true;
        }
        return false;
    }
    public void hurricaneSpecialClient(){
        if (this.isChargingCrossfireSpecial())
            return;
        if (isLockedByWater())
            return;
        if (useHurricaneSpecialClient()){
            return;
        }
        if (ankhShootClient()){
            return;
        }
        if (!this.onCooldown(PowerIndex.SKILL_2_SNEAK) && canExecuteMoveWithLevel(4)) {
            this.setCooldown(PowerIndex.SKILL_2_SNEAK, multiplyCooldown(ClientNetworking.getAppropriateConfig().magiciansRedSettings.hurricaneSpecialCooldown));
            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_2_SNEAK, true);
            tryPowerPacket(PowerIndex.POWER_2_SNEAK);
        }
    }
    public void hurricaneClient(){
        if (this.isChargingCrossfireSpecial())
            return;
        if (isLockedByWater())
            return;
        if (useHurricaneSpecialClient()){
            return;
        }
        if (ankhShootClient()){
            return;
        }
        if (!this.onCooldown(PowerIndex.SKILL_2) && !hasHurricaneSpecial()) {
            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_2, true);
            tryPowerPacket(PowerIndex.POWER_2);
        }
    }

    public boolean getShootConcealedHurricane() {
        if (canShootConcealedCrossfire()) {
            if (!onCooldown(PowerIndex.SKILL_2)) {
                this.setCooldown(PowerIndex.SKILL_2, multiplyCooldown(ClientNetworking.getAppropriateConfig().magiciansRedSettings.ankhConcealedCooldown));
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_1_BONUS, true);
                tryPowerPacket(PowerIndex.POWER_1_BONUS);
            }
            return true;
        }
        return false;
    }
    public void tryRedBindClient(){
        if (isChargingCrossfireSpecial() || this.activePower == PowerIndex.POWER_1_BLOCK)
            return;
        if (isBusy() || isLockedByWater())
            return;
        if (getShootConcealedHurricane()) {
            return;
        }
        if (!isChargingCrossfire() && !hasHurricaneSingle()) {
            if (!this.onCooldown(PowerIndex.SKILL_1) && this.getActivePower() != PowerIndex.POWER_1) {
                if (leaded == null) {
                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_1, true);
                } else {
                    this.setCooldown(PowerIndex.SKILL_1, ClientNetworking.getAppropriateConfig().magiciansRedSettings.redBindManualReleaseCooldown);
                }
                tryPowerPacket(PowerIndex.POWER_1);
            }
        }
    }

    public void tryIgnitionClient() {
        if (isChargingCrossfireSpecial() || this.activePower == PowerIndex.POWER_1_BLOCK)
            return;
        if (isBusy() || isLockedByWater())
            return;
        if (getShootConcealedHurricane()) {
            return;
        }
        if (!isChargingCrossfire() && !hasHurricaneSingle()) {
            if (!this.onCooldown(PowerIndex.SKILL_1_SNEAK)) {
                BlockPos HR = getGrabBlock();
                if (HR != null) {
                    this.setCooldown(PowerIndex.SKILL_1_SNEAK,
                            multiplyCooldown(ClientNetworking.getAppropriateConfig().magiciansRedSettings.igniteFireCooldown)
                    );
                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_1_SNEAK, true);
                    tryBlockPosPowerPacket(PowerIndex.EXTRA, grabBlock2);
                    tryBlockPosPowerPacket(PowerIndex.POWER_1_SNEAK, HR);
                }
            }
        }
    }

    public void tryLifeTrackerClient() {
        if (isChargingCrossfireSpecial())
            return;
        if (isBusy() || isLockedByWater())
            return;
        if (getShootConcealedHurricane()) {
            return;
        }
        if (!isChargingCrossfire() && !hasHurricaneSingle()) {
            if (canExecuteMoveWithLevel(5)) {
                this.setCooldown(PowerIndex.SKILL_1, 20);
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_1_BLOCK, true);
                tryPowerPacket(PowerIndex.POWER_1_BLOCK);
            }
        }
    }

    public BlockPos grabBlock = null;
    public BlockPos grabBlock2 = null;
    public boolean tryBlockPosPower(int move, boolean forced, BlockPos blockPos){
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

    public boolean crossfireVariationClient(){
        boolean hasSingle = isChargingCrossfireSingle() || hasHurricaneSingle();
        if (hasSingle) {
            if (canExecuteMoveWithLevel(3)) {
                this.setCooldown(PowerIndex.SKILL_2, multiplyCooldown(ClientNetworking.getAppropriateConfig().magiciansRedSettings.ankhHiddenCooldown));
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_3_BONUS, true);
                tryPowerPacket(PowerIndex.POWER_3_BONUS);
            }
            return true;
        }
        return false;
    }
    public void tryToDashClient(){
        if (isChargingCrossfireSpecial())
            return;
        if (isBusy())
            return;
        if (crossfireVariationClient())
            return;
        if (!doVault()) {
            dash();
        }
    }
    public void destroyProjectilesClient(){
        if (isChargingCrossfireSpecial())
            return;
        if (isBusy() || isLockedByWater())
            return;
        if (crossfireVariationClient())
            return;
        if (!this.onCooldown(PowerIndex.SKILL_EXTRA) && canExecuteMoveWithLevel(2)) {
            this.setCooldown(PowerIndex.SKILL_EXTRA, multiplyCooldown(ClientNetworking.getAppropriateConfig().magiciansRedSettings.projectileBurnCooldown));

            BlockPos HR = getGrabPos(10);
            if (HR != null) {
                tryBlockPosPowerPacket(PowerIndex.POWER_3_BLOCK, HR);
            }
        }
    }
    public void tryToSnapFireAway(){
        if (this.isChargingCrossfireSpecial())
            return;
        if (isBusy())
            return;
        if (crossfireVariationClient())
            return;
        if (!this.onCooldown(PowerIndex.SKILL_3)) {
            this.setCooldown(PowerIndex.SKILL_3, ClientNetworking.getAppropriateConfig().magiciansRedSettings.snapFireAwayCooldown);
            tryPowerPacket(PowerIndex.POWER_3);
        }
    }

    public void bigHurricaneClient(){
        if (isChargingCrossfire() || hasHurricaneSingle())
            return;
        if (isBusy())
            return;
        if (canExecuteMoveWithLevel(7) && !hasHurricaneSpecial()) {
            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_4_SNEAK, true);
            tryPowerPacket(PowerIndex.POWER_4_SNEAK);
        }
    }
    public void cawFireSlamClientCaw(){
        if (isChargingCrossfire())
            return;
        if (isBusy() || isLockedByWater())
            return;
        if (!this.onCooldown(PowerIndex.SKILL_4) && canExecuteMoveWithLevel(6)) {
            hold4 = true;
            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_4_BONUS, true);
            tryPowerPacket(PowerIndex.POWER_4_BONUS);
            this.setCooldown(PowerIndex.SKILL_4, ClientNetworking.getAppropriateConfig().magiciansRedSettings.flameCrashCooldown);
        }
    }
    public boolean hold4 = false;
    @Override
    public boolean tryPower(int move, boolean forced) {
        if ((move == PowerIndex.GUARD || move == PowerIndex.NONE)){
            if (!this.self.level().isClientSide()){
                if (this.getActivePower()==PowerIndex.POWER_4) {
                    if (kamikaze != null) {
                        kamikaze.setCrossNumber(0);
                        kamikaze.discard();
                    }
                }
            }
            if (move == PowerIndex.GUARD ||
                    !PowerTypes.hasStandActive(self)) {
                clearLeaded();
            }
        }

        if (!this.getSelf().level().isClientSide && this.getActivePower() == PowerIndex.POWER_1 && move != PowerIndex.POWER_1) {
            this.stopSoundsIfNearby(BIND_CHARGE_NOISE, 100,true);
        }

        if (move == PowerIndex.SPECIAL_CHARGED){

            if (!this.self.level().isClientSide()){
                if (this.activePower==PowerIndex.RANGED_BARRAGE){
                    fireballSpitGo();
                }
            }
            return true;
        }

        if ((this.activePower == PowerIndex.POWER_2
        || this.activePower == PowerIndex.POWER_2_SNEAK)
        && move != PowerIndex.POWER_2_BONUS && move != PowerIndex.LEAD_IN && move != PowerIndex.POWER_1_BONUS) {
            if (hasHurricaneSingle() || hasHurricaneSpecial()) {
                this.clearAllHurricanes();
            }
        }
        if (this.getActivePower() == PowerIndex.POWER_2) {
                stopSoundsIfNearby(CRY_2_NOISE, 100, false);
        } else if (this.getActivePower() == PowerIndex.POWER_2_SNEAK) {
            stopSoundsIfNearby(CRY_1_NOISE, 100, false);
        }

        if (!this.self.level().isClientSide &&
                (this.getActivePower() == PowerIndex.RANGED_BARRAGE_CHARGE_2 || this.getActivePower() == PowerIndex.RANGED_BARRAGE_2)
                && (move != PowerIndex.RANGED_BARRAGE_2 && move != PowerIndex.RANGED_BARRAGE_CHARGE_2 && move != PowerIndex.GUARD)){
            this.stopSoundsIfNearby(SoundIndex.BARRAGE_SOUND_GROUP, 100,false);
        }
        if (!this.self.level().isClientSide &&
                (this.getActivePower() == PowerIndex.RANGED_BARRAGE_CHARGE || this.getActivePower() == PowerIndex.RANGED_BARRAGE)
                && (move != PowerIndex.RANGED_BARRAGE && move != PowerIndex.RANGED_BARRAGE_CHARGE && move != PowerIndex.GUARD)){
            this.stopSoundsIfNearby(SoundIndex.BARRAGE_SOUND_GROUP, 100,false);
        }

        return super.tryPower(move,forced);
    }
    public static final byte LAST_HIT_1_NOISE = 120;
    public static final byte CRY_1_NOISE = 100;
    public static final byte CRY_2_NOISE = 101;
    public static final byte RANGED_CHARGE_1 = 102;
    public static final byte RANGED_CHARGE_2 = 104;
    public static final byte FLAMETHROWER_NOISE = 105;
    public static final byte FIRESTORM = 106;
    public static final byte CRY_3_NOISE = 107;
    public static final byte BIND_CHARGE_NOISE = 108;

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
        } else if (move == PowerIndex.POWER_1_BONUS) {
            return this.crossfireBlock();
        } else if (move == PowerIndex.POWER_3_BLOCK) {
            return this.fireBlast();
        } else if (move == PowerIndex.VAULT){
            return this.vault();
        } else if (move == PowerIndex.POWER_2) {
            return this.crossfire();
        } else if (move == PowerIndex.POWER_2_SNEAK) {
            return this.crossfireSpecial();
        } else if (move == PowerIndex.LEAD_IN) {
            if (hasHurricaneSpecial()) {
                ticksUntilHurricaneEnds = 160;
                return this.setPowerNone();
            } else {
                ticksUntilHurricaneEnds = 30;
                return false;
            }
        } else if (move == PowerIndex.POWER_2_BONUS) {
            return this.shootAnkhConfirm();
        } else if (move == PowerIndex.POWER_3_BONUS){
            return this.buryCrossfire();
        } else if (move == PowerIndex.SNEAK_ATTACK_CHARGE){
            return this.setPowerKickAttack();
        } else if (move == PowerIndex.SNEAK_ATTACK){
            return this.setPowerSuperHit();
        } else if (move == PowerIndex.RANGED_BARRAGE_CHARGE){
            return this.setPowerRangedBarrageCharge();
        } else if (move == PowerIndex.RANGED_BARRAGE_CHARGE_2){
            return this.setPowerRangedBarrageCharge2();
        } else if (move == PowerIndex.RANGED_BARRAGE) {
            return this.setPowerRangedBarrage();
        } else if (move == PowerIndex.RANGED_BARRAGE_2) {
            return this.setPowerRangedBarrage2();
        } else if (move == PowerIndex.POWER_4_SNEAK){
            return this.toggleBigAnkh();
        } else if (move == PowerIndex.POWER_4){
            return this.kamikaze();
        } else if (move == PowerIndex.POWER_4_BONUS){
            return this.kamikazeCharge();
        } else if (move == PowerIndex.POWER_1_BLOCK){
            return this.toggleLifeTracker();
        } else if (move == PowerIndex.POWER_1){
            return this.redBindCharge();
        } else if (move == PowerIndex.EXTRA_2) {
            return this.doRedBindAttack();
        }
        return super.setPowerOther(move,lastMove);
    }

    public void offloadLead(){
        if (leaded != null && (!leaded.isAlive() || !(((StandUser)leaded).roundabout$isStringBound()) || !(((StandUser)leaded).roundabout$getBoundTo().is(this.self)))){
            leaded = null;
        }
    }

    public boolean redBindCharge() {
        offloadLead();
        if (leaded != null){
            ((StandUser)leaded).roundabout$dropString();
            leaded = null;
        } else {
            this.animateStand(MagiciansRedEntity.RED_BIND);
            this.poseStand(OffsetIndex.GUARD);
            this.setAttackTimeDuring(0);
            this.setActivePower(PowerIndex.POWER_1);
            if (!this.getSelf().level().isClientSide()) {
                playSoundsIfNearby(BIND_CHARGE_NOISE, 27, false);
            }
        }
        return true;
    }
    @Override
    public void playSummonEffects(boolean forced){
        if (!forced) {
            Level lv = this.getSelf().level();
            if ((this.getSelf()) instanceof Player PE){
                StandUser user = ((StandUser)PE);
                ItemStack stack = user.roundabout$getStandDisc();
                if (!stack.isEmpty() && stack.is(ModItems.STAND_DISC_MAGICIANS_RED)){
                    IPlayerEntity ipe = ((IPlayerEntity) PE);
                    if (!ipe.roundabout$getUnlockedBonusSkin() && MainUtil.isDreadBook(PE.getMainHandItem())){
                        if (!lv.isClientSide()) {
                            ipe.roundabout$setUnlockedBonusSkin(true);
                            lv.playSound(null, PE.getX(), PE.getY(),
                                    PE.getZ(), ModSounds.UNLOCK_SKIN_EVENT, PE.getSoundSource(), 2.0F, 1.0F);
                            lv.playSound(null, PE.getX(), PE.getY(),
                                    PE.getZ(), ModSounds.DREAD_SUMMON_EVENT, PE.getSoundSource(), 3.0F, 1.0F);
                            ((ServerLevel) lv).sendParticles(ModParticles.DREAD_FLAME, PE.getX(),
                                    PE.getY()+PE.getEyeHeight(), PE.getZ(),
                                    10, 0.5, 0.5, 0.5, 0.2);
                            user.roundabout$setStandSkin(MagiciansRedEntity.DREAD_BEAST_SKIN);
                            ((ServerPlayer) ipe).displayClientMessage(
                                    Component.translatable("unlock_skin.roundabout.magicians_red.chagaroth"), true);
                        }
                    }
                }
            }
        }
    }
    public boolean toggleLifeTracker() {
        this.animateStand(MagiciansRedEntity.LIFE_DETECTOR);
        this.poseStand(OffsetIndex.GUARD);
        this.setAttackTimeDuring(-15);
        this.setActivePower(PowerIndex.POWER_1_BLOCK);
        if (!this.getSelf().level().isClientSide()) {
            if (tracker == null || tracker.isRemoved()){
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.FIRE_WHOOSH_EVENT, SoundSource.PLAYERS, 1F, 0.8F);
                LifeTrackerEntity cross = ModEntities.LIFE_TRACKER.create(this.getSelf().level());
                if (cross != null) {
                    tracker = cross;

                    Vec3 bam = new Vec3(0,
                            (this.self.getBbHeight()/2),
                            0);
                    Direction gravD = ((IGravityEntity)this.self).roundabout$getGravityDirection();
                    if (gravD != Direction.DOWN){
                        bam = RotationUtil.vecPlayerToWorld(bam,gravD);
                    }
                    cross.absMoveTo(this.self.getX()+bam.x, this.self.getY()+bam.y, this.self.getZ()+bam.z);
                    cross.setUser(this.self);
                    cross.shootFromRotationDeltaAgnostic(this.getSelf(), this.getSelf().getXRot(), this.getSelf().getYRot(), 1.0F, 0.2f, 0);
                    this.self.level().addFreshEntity(cross);
                }
            } else {
                tracker.discard();
                this.self.level().playSound(null, this.self.getX(), this.self.getY(),
                        this.self.getZ(), ModSounds.SNAP_EVENT, this.self.getSoundSource(), 1F, 1.1F);
            }
        }
        return true;
    }
    public void sealFromKamikaze(){
        int sealTime = 400;
        StandUser user = ((StandUser) this.self);
        if (this.self instanceof Player PE && PE.isCreative() && sealTime > 20){
            sealTime = 20;
        }
        if (this.self.level().isClientSide()) {
            user.roundabout$setMaxSealedTicks(sealTime);
            user.roundabout$setSealedTicks(sealTime);
        }

        if (!this.self.level().isClientSide() && user instanceof Player PE){
            S2CPacketUtil.sendGenericIntToClientPacket(((ServerPlayer) PE),
                    PacketDataIndex.S2C_INT_SEAL, sealTime);
        }
        user.roundabout$setActive(false);
    }

    public LivingEntity leaded;
    public CrossfireHurricaneEntity kamikaze;
    public boolean kamikazeCharge(){
        this.setCooldown(PowerIndex.SKILL_4, ClientNetworking.getAppropriateConfig().magiciansRedSettings.flameCrashCooldown);
        this.animateStand(StandEntity.BROKEN_GUARD);
        this.poseStand(OffsetIndex.GUARD_FURTHER_RIGHT);
        this.setAttackTimeDuring(0);
        this.setActivePower(PowerIndex.POWER_4_BONUS);
        if (leaded != null){
            setDazed(leaded, (byte) 0);
            clearLeaded();
        }

        if (!this.self.level().isClientSide()) {

            playStandUserOnlySoundsIfNearby(CRY_3_NOISE, 27, false,true);

            StandEntity stand = this.getStandEntity(this.self);
            if (stand != null) {
                ((ServerLevel) stand.level()).sendParticles(getFlameParticle(), stand.getX(),
                        stand.getY(), stand.getZ(),
                        80,
                        0.5,
                        0.5,
                        0.5,
                        0.15);
            }
        }
        return true;
    }
    public boolean kamikaze(){
        this.animateStand(MagiciansRedEntity.FIRE_CRASH);
        this.poseStand(OffsetIndex.GUARD_FURTHER_RIGHT);
        this.setAttackTimeDuring(0);
        this.setActivePower(PowerIndex.POWER_4);
        if (!this.self.level().isClientSide()) {
            clearEverything();

            StandEntity stand = this.getStandEntity(this.self);
            if (stand != null) {
                CrossfireHurricaneEntity cross = new CrossfireHurricaneEntity(this.self,this.self.level());
                if (cross != null) {
                    cross.absMoveTo(this.getSelf().getX(), this.getSelf().getY(), this.getSelf().getZ());
                    cross.setUser(this.self);
                    cross.setOwner(this.self);
                    cross.setCrossNumber(7);
                    cross.setMaxSize(getKamikazeSize());
                    cross.setSize(getKamikazeSize());
                    kamikaze = cross;
                    shootAnkhSpeed(kamikaze,0.8F);
                    this.getSelf().level().addFreshEntity(cross);
                    this.poseStand(OffsetIndex.LOOSE);
                }
            }
        }
        return true;
    }
    @Override
    public byte getPermaCastContext(){
        return PermanentZoneCastInstance.FIRESTORM;
    }
    public boolean toggleBigAnkh() {
        if (!this.getSelf().level().isClientSide()) {
            IPermaCasting icast = ((IPermaCasting) this.getSelf().level());
            if (!icast.roundabout$isPermaCastingEntity(this.getSelf())) {
                icast.roundabout$addPermaCaster(this.getSelf());
                if (bigAnkh == null || bigAnkh.isRemoved()){
                    CrossfireHurricaneEntity cross = new CrossfireHurricaneEntity(this.self,this.self.level());;
                    playSoundsIfNearby(FIRESTORM, 27, false);
                    if (cross != null) {
                        cross.absMoveTo(this.getSelf().getX(), this.getSelf().getY(), this.getSelf().getZ());
                        cross.setUser(this.self);
                        cross.setOwner(this.self);
                        cross.setCrossNumber(6);
                        cross.setMaxSize(getMassiveCrossfireSize());
                        bigAnkh = cross;
                        this.getSelf().level().addFreshEntity(cross);
                    }
                }
            } else {
                removeFirestorm();
            }
        }
        return true;
    }

    public void removeFirestorm(){
        IPermaCasting icast = ((IPermaCasting) this.getSelf().level());
        this.stopSoundsIfNearby(FIRESTORM, 100,false);
        icast.roundabout$removePermaCastingEntity(this.getSelf());
    }
    @Override
    public void updateMovesFromPacket(byte activePower){
        if (activePower == PowerIndex.RANGED_BARRAGE ||
        activePower == PowerIndex.RANGED_BARRAGE_2){
            this.setActivePowerPhase(this.activePowerPhaseMax);
        }
        super.updateMovesFromPacket(activePower);
    }
    public boolean setPowerRangedBarrage() {
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.RANGED_BARRAGE);
        this.poseStand(OffsetIndex.GUARD);
        addEXP(2);
        this.setAttackTimeMax(this.getRangedBarrageRecoilTime());
        this.setActivePowerPhase(this.getActivePowerPhaseMax());
        animateStand(MagiciansRedEntity.FIREBALL_SHOOT);
        return true;
    }
    public boolean setPowerRangedBarrage2() {
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.RANGED_BARRAGE_2);
        this.poseStand(OffsetIndex.ATTACK);
        playFlamethrowerSound();
        this.setAttackTimeMax(this.getRangedBarrage2RecoilTime());
        this.setActivePowerPhase(this.getActivePowerPhaseMax());
        animateStand(MagiciansRedEntity.FLAMETHROWER_SHOOT);
        return true;
    }
    public int getRangedBarrageRecoilTime(){
        return 60;
    }
    public int getRangedBarrage2RecoilTime(){
        return 35;
    }
    public void updateRangedBarrageCharge(){
        if (this.attackTimeDuring >= this.getRangedBarrageWindup()) {
            ((StandUser) this.self).roundabout$tryPower(PowerIndex.RANGED_BARRAGE, true);
        }
    }

    public void updateRangedBarrageCharge2(){
        if (this.attackTimeDuring >= this.getRangedBarrageWindup2()) {
            ((StandUser) this.self).roundabout$tryPower(PowerIndex.RANGED_BARRAGE_2, true);
        }
    }
    @Override
    public int getKickBarrageWindup(){
        return ClientNetworking.getAppropriateConfig().generalStandSettings.kickBarrageWindup;
    }
    @Override
    public byte getSoundCancelingGroupByte(byte soundChoice) {
        if (soundChoice == CRY_2_NOISE) {
            return CRY_2_NOISE;
        } else if (soundChoice == RANGED_CHARGE_1 ||
        soundChoice == RANGED_CHARGE_2 ||
                soundChoice == FLAMETHROWER_NOISE) {
            return SoundIndex.BARRAGE_SOUND_GROUP;
        }
        return super.getSoundCancelingGroupByte(soundChoice);
    }

    public int ticksUntilHurricaneEnds = -1;


    public void updateKickAttackCharge(){
        if (this.attackTimeDuring > -1) {
            if (this.attackTimeDuring >= maxSuperHitTime &&
                    (!(this.getSelf() instanceof Player) || (this.self.level().isClientSide() && isPacketPlayer()))){
                int atd = this.getAttackTimeDuring();
                ((StandUser) this.getSelf()).roundabout$tryIntPower(PowerIndex.SNEAK_ATTACK, true,maxSuperHitTime);
                if (this.self.level().isClientSide()){
                    tryIntPowerPacket(PowerIndex.SNEAK_ATTACK, atd);
                }
            }
        }
    }

    @Override
    public void updateUniqueMoves(){
        if (this.getActivePower() == PowerIndex.POWER_2_SNEAK) {
            this.updateCrossfireSpecial();
        } else if (this.getActivePower() == PowerIndex.RANGED_BARRAGE_CHARGE) {
            updateRangedBarrageCharge();
        } else if (this.getActivePower() == PowerIndex.RANGED_BARRAGE_CHARGE_2) {
            updateRangedBarrageCharge2();
        } else if (this.getActivePower() == PowerIndex.POWER_2) {
            this.updateCrossfire();
        } else if (this.getActivePower() == PowerIndex.SNEAK_ATTACK){
            updateKickAttack();
        } else if (this.getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE){
            updateKickAttackCharge();
        } else if (this.getActivePower() == PowerIndex.RANGED_BARRAGE){
            updateRangedBarrage();
        } else if (this.getActivePower() == PowerIndex.RANGED_BARRAGE_2){
            updateRangedBarrage2();
        } else if (this.getActivePower() == PowerIndex.POWER_4_BONUS){

            if (this.attackTimeDuring >= 16) {
                if (this.self instanceof Player) {
                    if (isPacketPlayer()) {
                        ((StandUser) this.self).roundabout$tryPower(PowerIndex.POWER_4, true);
                        tryPowerPacket(PowerIndex.POWER_4);
                    }
                } else {
                    ((StandUser) this.self).roundabout$tryPower(PowerIndex.POWER_4, true);
                }
            }
        } else if (this.getActivePower() == PowerIndex.POWER_1){
            updateRedBindCharge();
        }
    }
    public void updateRedBindCharge(){
        if (this.attackTimeDuring > -1) {
            if (this.attackTimeDuring > 24) {
                this.lasso();
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




    public void lasso(){
        /*By setting this to -10, there is a delay between the stand retracting*/
        drillT = false;
        if (this.self instanceof Player){
            if (isPacketPlayer()){
                this.setAttackTimeDuring(-20);
                Entity ent = getTargetEntity(this.self, 4);
                if (ent instanceof LivingEntity LE) {
                    leaded = LE;
                    ((StandUser)LE).roundabout$setBoundTo(this.self);
                    tryIntToServerPacket(PacketDataIndex.INT_STAND_ATTACK, LE.getId());
                } else {
                    tryIntToServerPacket(PacketDataIndex.INT_STAND_ATTACK,-1);
                    this.setCooldown(PowerIndex.SKILL_1, getRedBindMissCooldown());
                }
            }
        } else {
            /*Caps how far out the punch goes*/
            Entity targetEntity = getTargetEntity(this.self,4);
            if (targetEntity == null){
                this.setCooldown(PowerIndex.SKILL_1, getRedBindMissCooldown());
            }
            lassoImpact(targetEntity);
        }
    }

    public int lassoTime= -1;
    public void lassoImpact(Entity entity){
        if (this.activePower == PowerIndex.POWER_1) {
            this.setAttackTimeDuring(-20);
            if (entity != null) {
                if (entity instanceof LivingEntity LE) {
                    ((StandUser) LE).roundabout$setBoundTo(this.self);
                    leaded = LE;
                    lassoTime = 200;
                }
                knockShield2(entity, 100);
            }

            this.setCooldown(PowerIndex.SKILL_1, getRedBindMissCooldown());
            SoundEvent SE;
            float pitch = 1F;
            if (entity != null) {
                SE = ModSounds.LASSO_EVENT;
            } else {
                SE = ModSounds.PUNCH_2_SOUND_EVENT;
            }

            if (!this.self.level().isClientSide()) {
                this.self.level().playSound(null, this.self.blockPosition(), SE, SoundSource.PLAYERS, 0.95F, pitch);
            }
        }
    }

    public void updateRangedBarrage(){

        if (this.attackTimeDuring == -2 && this.getSelf() instanceof Player) {
            ((StandUser) this.self).roundabout$tryPower(PowerIndex.GUARD, true);
        } else {
            if (this.attackTimeDuring > this.getRangedBarrageLength()) {
                this.attackTimeDuring = -20;
            } else {
                if (this.attackTimeDuring > 0) {
                    this.setAttackTime((getBarrageRecoilTime() - 1) -
                            Math.round(((float) this.attackTimeDuring / this.getRangedBarrageLength())
                                    * (getBarrageRecoilTime() - 1)));

                    fireballSpit();
                }
            }
        }
    }

    public void fireballSpit(){
        if (this.self instanceof Player){
            if (isPacketPlayer()){
                if (isReadyToShoot()){
                    tryPowerPacket(PowerIndex.SPECIAL_CHARGED);
                }

                if (this.attackTimeDuring == this.getRangedBarrageLength()){
                    this.attackTimeDuring = -10;
                }

            }
        } else {
            if (isReadyToShoot()){
                fireballSpitGo();
            }

            if (this.attackTimeDuring == this.getRangedBarrageLength()){
                this.attackTimeDuring = -10;
            }
        }
    }
    public void fireballSpitGo(){
        if (!this.self.level().isClientSide()) {
            StandFireballEntity fireball = new StandFireballEntity(this.self,this.self.level());
            this.self.level().playSound(null, this.self.blockPosition(), ModSounds.FIREBALL_SHOOT_EVENT, SoundSource.PLAYERS, 1F, (float)(0.9F + Math.random()*0.2));
            if (fireball != null) {
                Vec3 bam = new Vec3(0,
                        this.self.getEyeHeight()*0.25,
                        0);
                Direction gravD = ((IGravityEntity)this.self).roundabout$getGravityDirection();
                if (gravD != Direction.DOWN){
                    bam = RotationUtil.vecPlayerToWorld(bam,gravD);
                }
                Vec3 vec3dST = this.self.getEyePosition(0).subtract(bam.x,bam.y,bam.z);


                Vec3 vec3d2ST = this.self.getViewVector(0);

                fireball.setUser(this.self);
                fireball.setOwner(this.self);
                Vec3 vec3d3ST = vec3dST.add(vec3d2ST.x * 1.1, vec3d2ST.y * 1.1, vec3d2ST.z * 1.1);
                fireball.absMoveTo(vec3d3ST.x(), vec3d3ST.y(), vec3d3ST.z());

                fireball.setXRot(this.getSelf().getXRot() % 360);
                fireball.shootFromRotationDeltaAgnostic(this.getSelf(), this.getSelf().getXRot(), this.getSelf().getYRot(), 1.0F, 1.35F, 0);
                this.getSelf().level().addFreshEntity(fireball);
            }
        }
    }

    public void spewFlamesMining(){
        if (!this.self.level().isClientSide()){
            //particle spew code here
            StandEntity stand = this.getStandEntity(this.self);
            if (stand != null) {
                Vec3 vector = getRayBlock(this.self,this.getReach());
                if (vector != null) {
                    for (int i = 0; i < 2; i++) {
                        double spd = (1 - ((double) i / 7)) * 0.9;
                        double random = (Math.random() * 2.2) - 1.1;
                        double random2 = (Math.random() * 2.2) - 1.1;
                        double random3 = (Math.random() * 2.2) - 1.1;
                        ((ServerLevel) stand.level()).sendParticles(getFlameParticle(), stand.getX(),
                                stand.getY() + stand.getEyeHeight() * 0.8, stand.getZ(),
                                0,
                                (-3 * (stand.getX() - vector.x()) + 0.5 + random) * spd,
                                (-3 * ((stand.getY() + stand.getEyeHeight()) - vector.y())+ 0.5  + random2) * spd,
                                (-3 * (stand.getZ() - vector.z()) + 0.5 + random3) * spd,
                                0.15);
                    }
                }
            }
        }
    }
    public void spewFlames(){
        if (!this.self.level().isClientSide()){
            //particle spew code here
            StandEntity stand = this.getStandEntity(this.self);
            if (stand != null) {
                Vec3 vector = getRayBlock(this.self,this.getReach());
                if (vector != null) {
                    for (int i = 0; i < 6; i++) {
                        double spd = (1 - ((double) i / 7)) * 0.5;
                        double random = (Math.random() * 6) - 3;
                        double random2 = (Math.random() * 6) - 3;
                        double random3 = (Math.random() * 6) - 3;
                        ((ServerLevel) stand.level()).sendParticles(getFlameParticle(), stand.getX(),
                                stand.getY() + stand.getEyeHeight() * 0.8, stand.getZ(),
                                0,
                                (-3 * (stand.getX() - vector.x()) + 0.5 + random) * spd,
                                (-3 * (stand.getY() - vector.y()) - 1 + random2) * spd,
                                (-3 * (stand.getZ() - vector.z()) + 0.5 + random3) * spd,
                                0.15);
                    }
                }
            }
        }
    }
    public void updateRangedBarrage2(){

        if (this.attackTimeDuring == -2 && this.getSelf() instanceof Player) {
            ((StandUser) this.self).roundabout$tryPower(PowerIndex.GUARD, true);
        } else {
            if (this.attackTimeDuring > this.getRangedBarrage2Length()) {
                this.attackTimeDuring = -20;
            } else {
                if (this.attackTimeDuring > 0) {

                    spewFlames();
                    this.setAttackTime((getBarrageRecoilTime() - 1) -
                            Math.round(((float) this.attackTimeDuring / this.getRangedBarrage2Length())
                                    * (getBarrageRecoilTime() - 1)));

                    flamethrowerImpact();
                }
            }
        }
    }

    public void flamethrowerImpact(){
        boolean lastHit = (this.attackTimeDuring >= this.getRangedBarrage2Length());
        if (this.self instanceof Player){
            if (isPacketPlayer()){
                float distMax = (float) getBlockDistanceOut(this.self,this.getReach());

                List<Entity> listE = getTargetEntityList(this.self,distMax);
                if (!listE.isEmpty()){
                    for (int i = 0; i< listE.size(); i++){
                        if (!(listE.get(i) instanceof StandEntity SE && !SE.canBeHitByStands()) && listE.get(i).distanceTo(this.self) < distMax) {
                            if (lastHit){
                                tryIntToServerPacket(PacketDataIndex.INT_STAND_ATTACK_2,listE.get(i).getId());
                            } else {
                                tryIntToServerPacket(PacketDataIndex.INT_STAND_ATTACK,listE.get(i).getId());
                            }
                        }
                    }
                }
                if (this.attackTimeDuring == this.getRangedBarrage2Length()){
                    this.attackTimeDuring = -10;
                }

            }
        } else {
            /*Caps how far out the punch goes*/

            float distMax = (float) getBlockDistanceOut(this.self,this.getReach());
            Entity targetEntity = getTargetEntity(this.self,distMax);

            List<Entity> listE = getTargetEntityList(this.self,distMax);
            rangedBarrageImpact(targetEntity,lastHit);
            if (!listE.isEmpty()){
                for (int i = 0; i< listE.size(); i++){
                    if (!(storeEnt != null && listE.get(i).is(storeEnt))) {
                        if (!(listE.get(i) instanceof StandEntity) && listE.get(i).distanceTo(this.self) < distMax) {
                            if (lastHit) {
                                rangedBarrageImpact(listE.get(i), true);
                            } else {
                                rangedBarrageImpact(listE.get(i), false);
                            }
                        }
                    }
                }
            }

            if (this.attackTimeDuring == this.getRangedBarrage2Length()){
                this.attackTimeDuring = -10;
            }
        }
    }
    public void updateCrossfire(){
        if (!this.self.level().isClientSide()) {
            if (this.attackTimeDuring >= getChargingCrossfire()){
                if (this.attackTimeDuring == getChargingCrossfire()){
                    this.self.level().playSound(null, this.self.blockPosition(),  SoundEvents.FIRECHARGE_USE,
                            SoundSource.PLAYERS, 1F, 2F);

                    ((ServerLevel) this.self.level()).sendParticles(getFlameParticle(), this.self.getX(),
                            this.self.getY()+(this.self.getBbHeight()*0.5), this.self.getZ(),
                            20,
                            0.4, 0.4, 0.4,
                            0.01);
                }
                if (!(this.self instanceof Player)){
                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.LEAD_IN,true);
                }
            } else if (this.attackTimeDuring == getChargingCrossfireSpecialSize()){

                ((ServerLevel) this.self.level()).sendParticles(getFlameParticle(), this.self.getX(),
                        this.self.getY()+(this.self.getBbHeight()*0.5), this.self.getZ(),
                        10,
                        0.4, 0.4, 0.4,
                        0.01);
                this.self.level().playSound(null, this.self.blockPosition(),  SoundEvents.FIRECHARGE_USE,
                        SoundSource.PLAYERS, 1F, 1.5F);
            }
        } else {
            if (this.attackTimeDuring == getChargingCrossfire()) {
                if (this.self instanceof Player) {
                    if (isPacketPlayer()) {
                        tryPowerPacket(PowerIndex.LEAD_IN);
                    }
                }
            }
        }
    }
    public BlockPos blockPosForSpecial = BlockPos.ZERO;
    //This isn't in mainutils cause this isn't perfect (not even close), as is shown with me having to use this everywhere to ensure standfire doesn't appear
    private boolean canPlaceOnClaimPos(Player p,BlockPos pos){
        if(!p.level().getBlockState(pos).isAir() || p.level().getBlockState(pos.relative(Direction.DOWN)).isAir()){
            if(p.level().getBlockState(pos.relative(Direction.DOWN)).is(Blocks.BARRIER)){
                return false;
            }

            return true;
        }

        boolean can = MainUtil.canPlaceOnClaim(p,new BlockHitResult(new Vec3(p.getX(),p.getY(),p.getZ()), Direction.UP,pos.relative(Direction.DOWN),false));
        if(p.level().getBlockState(pos.relative(Direction.DOWN)).is(Blocks.BARRIER)){
            p.level().removeBlock(pos.relative(Direction.DOWN),false);
        }
        return can;
    }
    public void updateCrossfireSpecial(){
        if (!this.self.level().isClientSide()) {
            if(this.getSelf() instanceof Player p) {
                if (!canPlaceOnClaimPos(p,blockPosForSpecial.east().east()) ||
                        !canPlaceOnClaimPos(p,blockPosForSpecial.west().west()) ||
                        !canPlaceOnClaimPos(p,blockPosForSpecial.north().north()) ||
                        !canPlaceOnClaimPos(p,blockPosForSpecial.south().south()) ||
                        !canPlaceOnClaimPos(p,blockPosForSpecial.north().west()) ||
                        !canPlaceOnClaimPos(p,blockPosForSpecial.north().east()) ||
                        !canPlaceOnClaimPos(p,blockPosForSpecial.south().west()) ||
                        !canPlaceOnClaimPos(p,blockPosForSpecial.south().east()) ||
                        !canPlaceOnClaimPos(p,blockPosForSpecial.east().east().north()) ||
                        !canPlaceOnClaimPos(p,blockPosForSpecial.east().east().south()) ||
                        !canPlaceOnClaimPos(p,blockPosForSpecial.west().west().north()) ||
                        !canPlaceOnClaimPos(p,blockPosForSpecial.west().west().south()) ||
                        !canPlaceOnClaimPos(p,blockPosForSpecial.north().north().east()) ||
                        !canPlaceOnClaimPos(p,blockPosForSpecial.north().north().west()) ||
                        !canPlaceOnClaimPos(p,blockPosForSpecial.south().south().east()) ||
                        !canPlaceOnClaimPos(p,blockPosForSpecial.south().south().west())
                ){return;}
            }
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
                        tryPowerPacket(PowerIndex.LEAD_IN);
                    }
                }
            }
        }
    }
    public SimpleParticleType getFlameParticle(){
        byte skn = ((StandUser)this.getSelf()).roundabout$getStandSkin();

        return switch (skn) {
            case MagiciansRedEntity.BLUE_SKIN, MagiciansRedEntity.BLUE_ACE_SKIN, MagiciansRedEntity.BLUE_ABLAZE, MagiciansRedEntity.SKELETAL -> ModParticles.BLUE_FLAME;
            case MagiciansRedEntity.PURPLE_SKIN, MagiciansRedEntity.PURPLE_ABLAZE -> ModParticles.PURPLE_FLAME;
            case MagiciansRedEntity.GREEN_SKIN, MagiciansRedEntity.GREEN_ABLAZE -> ModParticles.GREEN_FLAME;
            case MagiciansRedEntity.DREAD_SKIN, MagiciansRedEntity.DREAD_ABLAZE, MagiciansRedEntity.DREAD_BEAST_SKIN -> ModParticles.DREAD_FLAME;
            case MagiciansRedEntity.JOJONIUM, MagiciansRedEntity.JOJONIUM_ABLAZE -> ModParticles.CREAM_FLAME;
            default -> ModParticles.ORANGE_FLAME;
        };
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
            addEXP(4);
            this.poseStand(OffsetIndex.GUARD_FURTHER_RIGHT);
            this.setAttackTimeDuring(0);
            this.setActivePower(PowerIndex.POWER_2_SNEAK);
            spinint = 0;
            if (!this.self.level().isClientSide()) {
                this.setCooldown(PowerIndex.SKILL_2_SNEAK, multiplyCooldown(ClientNetworking.getAppropriateConfig().magiciansRedSettings.hurricaneSpecialCooldown));
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

    public void animateFinalAttack(){
        animateStand(StandEntity.FINAL_ATTACK_WINDUP);
    }

    public void animateKickAttackHit(){
        if (chargedFinal >= maxSuperHitTime) {
            animateStand(StandEntity.FINAL_ATTACK);
        } else {
            animateStand(MagiciansRedEntity.CHARGED_PUNCH);
        }
    }

    @Override
    public boolean tryIntPower(int move, boolean forced, int chargeTime){
        if (move == PowerIndex.SNEAK_ATTACK) {
            this.chargedFinal = chargeTime;
        }
        return super.tryIntPower(move, forced, chargeTime);
    }
    public int chargedFinal;
    public boolean setPowerKickAttack() {
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
        animateKickAttackHit();
        //playBarrageCrySound();
        return true;
    }

    public float getKickAttackKnockback(){
        if (chargedFinal >= maxSuperHitTime) {
            return (((float)this.chargedFinal /(float)maxSuperHitTime)*3);
        } else {
            return (((float)this.chargedFinal/(float)maxSuperHitTime)*1.5F);
        }
    }
    public float getKickAttackStrength(Entity entity){
        float punchD = this.getPunchStrength(entity)*2+this.getHeavyPunchStrength(entity);
        if (this.getReducedDamage(entity)){
            return (((float)this.chargedFinal/(float)maxSuperHitTime)*punchD);
        } else {
            return (((float)this.chargedFinal/(float)maxSuperHitTime)*punchD)+1;
        }
    }
    public void kickAttackImpact(Entity entity){
        this.setAttackTimeDuring(-20);
        if (entity != null) {
            hitParticlesCenter(entity);
            float pow;
            float knockbackStrength;
            pow = getKickAttackStrength(entity);
            knockbackStrength = getKickAttackKnockback();
            if (StandDamageEntityAttack(entity, pow, 0, this.self)) {
                if (entity instanceof LivingEntity LE) {
                    if (chargedFinal >= maxSuperHitTime) {
                        addEXP(5, LE);
                    }
                }
                this.takeDeterminedKnockbackWithY(this.self, entity, knockbackStrength);
            } else {
                if (chargedFinal >= maxSuperHitTime) {
                    knockShield2(entity, getKickAttackKnockShieldTime());

                }
            }

            int fireCount = 50;
            float firespeed =0.05F;
            if (chargedFinal >= maxSuperHitTime){
                fireCount = 100;
                firespeed =0.1F;
            }
            ((ServerLevel) this.self.level()).sendParticles(getFlameParticle(), entity.getX(),
                    entity.getY() + (entity.getBbHeight() * 0.5), entity.getZ(),
                    fireCount,
                    0, 0, 0,
                    firespeed);
        } else {
            // This is less accurate raycasting as it is server sided but it is important for particle effects
            float distMax = this.getDistanceOut(this.self, this.getReach(), false);
            float halfReach = (float) (distMax * 0.5);
            Vec3 pointVec = DamageHandler.getRayPoint(self, halfReach);
            if (!this.self.level().isClientSide) {
                ((ServerLevel) this.self.level()).sendParticles(ModParticles.PUNCH_MISS, pointVec.x, pointVec.y, pointVec.z,
                        1, 0.0, 0.0, 0.0, 1);
            }
        }

        SoundEvent SE;
        float pitch = 1F;
        if (entity != null) {
            SE = getKickAttackSound();
            pitch = 1.2F;
        } else {
            SE = ModSounds.PUNCH_2_SOUND_EVENT;
        }

        if (!this.self.level().isClientSide()) {
            this.self.level().playSound(null, this.self.blockPosition(), SE, SoundSource.PLAYERS, 0.95F, pitch);
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
                        this.tryIntPower(PowerIndex.SNEAK_ATTACK, true, atd);
                        tryIntPowerPacket(PowerIndex.SNEAK_ATTACK, atd);
                    }
                    holdDownClick = false;
                }
            } else {
                if (keyIsDown) {
                    Minecraft mc = Minecraft.getInstance();

                    offloadLead();
                    if (leaded != null && !drillT) {
                        this.tryPower(PowerIndex.EXTRA_2, true);
                        tryPowerPacket(PowerIndex.EXTRA_2);
                        consumeClickInput = true;
                    } else if (!isHoldingSneak()) {
                        super.buttonInputAttack(keyIsDown, options);
                    } else {
                        if (this.canAttack()) {
                            this.tryPower(PowerIndex.SNEAK_ATTACK_CHARGE, true);
                            holdDownClick = true;
                            tryPowerPacket(PowerIndex.SNEAK_ATTACK_CHARGE);
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

    @Override
    public void handleStandAttack(Player player, Entity target){
        if (this.getActivePower() == PowerIndex.SNEAK_ATTACK){
            kickAttackImpact(target);
        } else if (this.getActivePower() == PowerIndex.RANGED_BARRAGE_2){
            rangedBarrageImpact(target,false);
        } else if (this.getActivePower() == PowerIndex.POWER_1){
            lassoImpact(target);
        }
    }
    @Override
    public void handleStandAttack2(Player player, Entity target){
        if (this.getActivePower() == PowerIndex.RANGED_BARRAGE_2){
            rangedBarrageImpact(target,true);
        }
    }

    public boolean crossfireDamageEntityAttack(Entity target, float pow, float knockbackStrength, Entity attacker){
        if (attacker instanceof TamableAnimal TA){
            if (target instanceof TamableAnimal TT && TT.getOwner() != null
                    && TA.getOwner() != null && TT.getOwner().is(TA.getOwner())){
                return false;
            }
        } else if (attacker instanceof AbstractVillager){
            if (target instanceof AbstractVillager){
                return false;
            }
        }
        if (DamageHandler.CrossfireDamageEntity(target,pow, attacker)){
            if (attacker instanceof LivingEntity LE){
                LE.setLastHurtMob(target);
            }
            if (target instanceof LivingEntity && knockbackStrength > 0) {
                ((LivingEntity) target).knockback(knockbackStrength * 0.5f, Mth.sin(attacker.getYRot() * ((float) Math.PI / 180)), -Mth.cos(attacker.getYRot() * ((float) Math.PI / 180)));
            }
            return true;
        }
        return false;
    }


    private float getRangedBarrage2HitStrength(Entity entity){

        float barrageLength = this.getRangedBarrage2Length();
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


        if (power > 0.005F) {
            if (getReducedDamage(entity)) {
                power = levelupDamageMod(multiplyPowerByStandConfigPlayers(power));
            } else {
                power = levelupDamageMod(multiplyPowerByStandConfigMobs(power));
            }
        }

        return power;
    }

    @Override
    public float multiplyPowerByStandConfigPlayers(float power){
        return (float) (power*(ClientNetworking.getAppropriateConfig().
                magiciansRedSettings.magicianAttackMultOnPlayers*0.01));
    }
    @Override
    public float multiplyPowerByStandConfigMobs(float power){
        return (float) (power*(ClientNetworking.getAppropriateConfig().
                magiciansRedSettings.magicianAttackMultOnMobs*0.01));
    }


    public void rangedBarrageImpact(Entity entity, boolean finalHit){
        if (entity != null && moveStarted){
            moveStarted = false;

            StandEntity stand = getStandEntity(this.self);
            if (Objects.nonNull(stand)){
                stand.setXRot(getLookAtEntityPitch(stand, entity));
                stand.setYRot(getLookAtEntityYaw(stand, entity));
            }
        }

        if (this.activePower == PowerIndex.RANGED_BARRAGE_2) {
            if (entity != null) {
                float pow;
                float knockbackStrength = 0;
                /**By saving the velocity before hitting, we can let people approach barraging foes
                 * through shields.*/
                Vec3 prevVelocity = entity.getDeltaMovement();
                    pow = this.getRangedBarrage2HitStrength(entity);
                    knockbackStrength = 0.0003F;


                if (StandRushDamageEntityAttack(entity, pow, 0.0001F, this.self)) {

                    if (entity instanceof LivingEntity LE) {
                        if (entity instanceof Player PE){
                            ((IPlayerEntity) PE).roundabout$setCameraHits(2);
                        }
                        int ticks = 0;
                        StandUser su = (StandUser) LE;
                        if (su.roundabout$getRemainingFireTicks() > -1){
                            ticks+=su.roundabout$getRemainingFireTicks();
                            ticks+=2;
                        } else {
                            ticks+=40;
                        }
                        su.roundabout$setOnStandFire(this.getFireColor(), this.self);
                        su.roundabout$setRemainingStandFireTicks(ticks);
                    }
                    entity.setDeltaMovement(prevVelocity);
                    if (Math.abs(this.lastHurtTick-this.self.tickCount) > 6) {
                        this.self.level().playSound(null, this.self.blockPosition(), ModSounds.STAND_FLAME_HIT_EVENT, SoundSource.PLAYERS, 1F, 1F);
                        this.lastHurtTick = this.self.tickCount;
                    }
                } else {
                   entity.setDeltaMovement(prevVelocity);
                    if (Math.abs(this.lastHurtTick-this.self.tickCount) > 6) {
                        this.self.level().playSound(null, this.self.blockPosition(), ModSounds.STAND_FLAME_HIT_EVENT, SoundSource.PLAYERS, 1F, 0.8F);
                        this.lastHurtTick = this.self.tickCount;
                    }
                }
            }

            if (finalHit) {
                this.attackTimeDuring = -10;
            }
        }
    }
    public int lastHurtTick = 0;

    public void updateKickAttack(){
        if (this.attackTimeDuring > -1) {
            if (this.attackTimeDuring == 5) {
                this.standFinalAttack();
            }
        }
    }

    public void standFinalAttack(){

        if (chargedFinal >= maxSuperHitTime) {
            this.setAttackTimeMax((int) (ClientNetworking.getAppropriateConfig().magiciansRedSettings.magicianKickMinimumCooldown + chargedFinal * 1.5));
        } else {
            this.setAttackTimeMax((int) (ClientNetworking.getAppropriateConfig().magiciansRedSettings.magicianKickMinimumCooldown + chargedFinal));
        }
        this.setAttackTime(0);
        this.setActivePowerPhase(this.getActivePowerPhaseMax());

        if (this.self instanceof Player){
            if (isPacketPlayer()){
                //Roundabout.LOGGER.info("Time: "+this.self.getWorld().getTime()+" ATD: "+this.attackTimeDuring+" APP"+this.activePowerPhase);
                this.attackTimeDuring = -10;
                tryIntToServerPacket(PacketDataIndex.INT_STAND_ATTACK,getTargetEntityId());
            }
        } else {
            /*Caps how far out the punch goes*/
            Entity targetEntity = getTargetEntity(this.self,-1);
            kickAttackImpact(targetEntity);
        }
    }
    public int getKickAttackKnockShieldTime(){
        return 100;
    }
    public void generateCrossfire(int crossNumber, int maxSize){
        CrossfireHurricaneEntity cross = new CrossfireHurricaneEntity(this.self,this.self.level());;
        if (cross != null){
            cross.absMoveTo(this.getSelf().getX(), this.getSelf().getY(), this.getSelf().getZ());
            cross.setUser(this.self);
            cross.setOwner(this.self);

            if (hurricaneSpecial == null) {hurricaneSpecial = new ArrayList<>();}
            cross.setCrossNumber(crossNumber);
            cross.setMaxSize(maxSize);
            cross.fireStormCreated = isUsingFirestorm();
            hurricaneSpecial.add(cross);

            this.getSelf().level().addFreshEntity(cross);
        }
    }
    @Override
    public void buttonInputBarrage(boolean keyIsDown, Options options){
        if (keyIsDown) {
            if (!isLockedByWater()) {
                if (this.getAttackTime() >= this.getAttackTimeMax() ||
                        (this.getActivePowerPhase() != this.getActivePowerPhaseMax())) {
                    if (isHoldingSneak()) {
                        this.tryPower(PowerIndex.RANGED_BARRAGE_CHARGE_2, true);
                        tryPowerPacket(PowerIndex.RANGED_BARRAGE_CHARGE_2);
                    } else {
                        this.tryPower(PowerIndex.RANGED_BARRAGE_CHARGE, true);
                        tryPowerPacket(PowerIndex.RANGED_BARRAGE_CHARGE);
                    }
                }
            }
        }
    }

    @Override
    public float getMiningMultiplier() {
        return (float) (1F*(ClientNetworking.getAppropriateConfig().
                        magiciansRedSettings.miningSpeedMultiplierMagiciansRed *0.01));
    }

    @Override
    public int getMiningLevel() {
        return ClientNetworking.getAppropriateConfig().magiciansRedSettings.getMiningTierMagiciansRed;
    }
    @Override
    public boolean clickRelease(){
        if (!canGuard()){
            return true;
        }
        return super.clickRelease();
    }
    @Override
    public boolean canGuard(){
        return this.activePower != PowerIndex.RANGED_BARRAGE_CHARGE && this.activePower != PowerIndex.RANGED_BARRAGE &&
                this.activePower != PowerIndex.RANGED_BARRAGE_CHARGE_2 && this.activePower != PowerIndex.RANGED_BARRAGE_2
                && this.activePower != PowerIndex.POWER_1_BLOCK;
    }

    @Override
    public boolean canInterruptPower() {
        if (this.getActivePower() == PowerIndex.RANGED_BARRAGE_CHARGE || this.getActivePower() == PowerIndex.RANGED_BARRAGE_CHARGE_2
                || this.getActivePower() == PowerIndex.RANGED_BARRAGE || this.getActivePower() == PowerIndex.POWER_4_BONUS) {
            return true;
        } else if (this.getActivePower() == PowerIndex.POWER_1) {
            int cdr = getRedBindMissCooldown();
            if (this.getSelf() instanceof Player) {
                S2CPacketUtil.sendCooldownSyncPacket(((ServerPlayer) this.getSelf()), PowerIndex.SKILL_1, cdr);
            }
            this.setCooldown(PowerIndex.SKILL_1, cdr);
            return true;
        }
            return super.canInterruptPower();
    }
    public void shootAnkh(CrossfireHurricaneEntity ankh){
        shootAnkhSpeed(ankh, 1.01F);
    }
    public void shootAnkhSpeed(CrossfireHurricaneEntity ankh, float speed){
        ankh.setPos(this.self.getEyePosition().x, this.self.getEyePosition().y, this.self.getEyePosition().z);
        ankh.setXRot(this.getSelf().getXRot()%360);
        ankh.shootFromRotationDeltaAgnostic(this.getSelf(), this.getSelf().getXRot(), this.getSelf().getYRot(), 1.0F, speed, 0);
    }

    public boolean shootAnkhConfirm(){
        if (!this.self.level().isClientSide()) {
            if (hasHurricaneSpecial()){

                this.setCooldown(PowerIndex.SKILL_EXTRA_2, 8);
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
                this.setCooldown(PowerIndex.SKILL_2, multiplyCooldown(ClientNetworking.getAppropriateConfig().magiciansRedSettings.ankhSuccessCooldown));
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

    public boolean buryCrossfire(){
        boolean hasSingle =isChargingCrossfireSingle() || hasHurricaneSingle();
        if (hasSingle) {
            this.animateStand(StandEntity.BROKEN_GUARD);
            this.poseStand(OffsetIndex.GUARD_FURTHER_RIGHT);
            this.setAttackTimeDuring(-15);
            this.setActivePower(PowerIndex.POWER_3_BONUS);
            if (!this.self.level().isClientSide()) {
                this.setCooldown(PowerIndex.SKILL_2, multiplyCooldown(ClientNetworking.getAppropriateConfig().magiciansRedSettings.ankhHiddenCooldown));
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.STAND_FLAME_HIT_EVENT, SoundSource.PLAYERS, 1F, 1.5F);
                GroundHurricaneEntity groundent = new GroundHurricaneEntity(this.getSelf().level(), this.self);
                groundent.setPos(this.self.position());
                groundent.fireStormCreated = isUsingFirestorm();
                if (this.hurricane != null){

                    groundent.setSize(this.hurricane.getSize());
                }
                this.getSelf().level().addFreshEntity(groundent);
                if (groundHurricane != null && !groundHurricane.isRemoved()){
                    groundHurricane.discard();
                }
                groundHurricane = groundent;
                clearAllHurricanes();
            }
        }
        return true;
    }

    public GroundHurricaneEntity groundHurricane = null;
    public LifeTrackerEntity tracker = null;

    public boolean crossfireBlock(){
        if (canShootConcealedCrossfire()){
            this.animateStand(StandEntity.BROKEN_GUARD);
            this.poseStand(OffsetIndex.GUARD_FURTHER_RIGHT);
            this.setAttackTimeDuring(-15);
            this.setActivePower(PowerIndex.POWER_1_BONUS);
            if (!this.self.level().isClientSide()) {
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.STAND_FLAME_HIT_EVENT, SoundSource.PLAYERS, 1F, 1.5F);
                ConcealedFlameObjectEntity thrownBlockOrItem = new ConcealedFlameObjectEntity(this.getSelf(), this.getSelf().level(), this.self.getMainHandItem().copy());
                thrownBlockOrItem.setPos(thrownBlockOrItem.position().subtract(0,this.self.getBbHeight()*0.3,0));
                thrownBlockOrItem.shootFromRotationDeltaAgnostic(this.getSelf(), this.getSelf().getXRot(),
                        this.getSelf().getYRot(), 0, 0.12F, 0);
                thrownBlockOrItem.fireStormCreated = isUsingFirestorm();
                if (this.hurricane != null){

                    thrownBlockOrItem.setSize(this.hurricane.getSize());
                }
                this.getSelf().level().addFreshEntity(thrownBlockOrItem);

                this.self.getMainHandItem().shrink(1);
                clearAllHurricanes();
            }
        }
        return true;
    }
    public boolean crossfire(){
        if (!hasHurricaneSingle()) {
            this.animateStand(StandEntity.BROKEN_GUARD);
            this.poseStand(OffsetIndex.GUARD_FURTHER_RIGHT);
            this.setAttackTimeDuring(0);
            this.setActivePower(PowerIndex.POWER_2);
            if (!this.self.level().isClientSide()) {
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.FIRE_BLAST_EVENT, SoundSource.PLAYERS, 2F, 1.2F);
                playStandUserOnlySoundsIfNearby(CRY_2_NOISE, 27, false,true);
                ticksUntilHurricaneEnds = -1;
                CrossfireHurricaneEntity cross = new CrossfireHurricaneEntity(this.self,this.self.level());
                if (cross != null) {
                    cross.absMoveTo(this.getSelf().getX(), this.getSelf().getY(), this.getSelf().getZ());
                    cross.setUser(this.self);
                    cross.setOwner(this.self);
                    cross.setCrossNumber(5);
                    cross.setMaxSize(getChargingCrossfireSize());
                    cross.fireStormCreated = isUsingFirestorm();
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
                        ((ServerLevel) stand.level()).sendParticles(getFlameParticle(), stand.getX(),
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
        Direction gravD = ((IGravityEntity)User).roundabout$getGravityDirection();
        for (Entity value : entities) {
            if (!value.isRemoved() && value instanceof Projectile && !(value instanceof Fireball) && !(value instanceof UnburnableProjectile)
                    && !(value instanceof AbstractArrow aa && ((IAbstractArrowAccess)aa).roundabout$GetPickupItem() != null &&
                    ((IAbstractArrowAccess)aa).roundabout$GetPickupItem().getItem().canBeDepleted()
                    )
            ){
                Vec2 lookVec = new Vec2(getLookAtEntityYaw(User, value), getLookAtEntityPitch(User, value));
                if (gravD != Direction.DOWN) {
                    lookVec = RotationUtil.rotPlayerToWorld(lookVec.x, lookVec.y, gravD);
                }
                if (angleDistance(lookVec.x, (User.getYHeadRot()%360f)) <= angle && angleDistance(lookVec.y, User.getXRot()) <= angle){
                    hitEntities.remove(value);
                    ((ServerLevel) this.self.level()).sendParticles(ParticleTypes.SMOKE, value.getX(),
                            value.getY(), value.getZ(),
                            20,
                            0.1,
                            0.1,
                            0.1,
                            0.03);

                    if (value instanceof GasolineSplatterEntity || value instanceof GasolineCanEntity){
                        ((ServerLevel) value.level()).sendParticles(ParticleTypes.FLAME, value.getX(), value.getY()+value.getEyeHeight(), value.getZ(),
                                40, 0.0, 0.2, 0.0, 0.2);
                        ((ServerLevel) value.level()).sendParticles(ParticleTypes.EXPLOSION, value.getX(), value.getY()+value.getEyeHeight(), value.getZ(),
                                1, 0.5, 0.5, 0.5, 0.2);
                        MainUtil.gasExplode(null, (ServerLevel) value.level(), value.getOnPos(), 0, 2, 4, MainUtil.gasDamageMultiplier()*10);
                    }

                    value.discard();
                }
            } else if (value instanceof LivingEntity le){
                MainUtil.removeFleshBud(le);
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
                S2CPacketUtil.sendCooldownSyncPacket(((ServerPlayer) this.getSelf()),
                        PowerIndex.SKILL_2,  multiplyCooldown(ClientNetworking.getAppropriateConfig().magiciansRedSettings.ankhFailCooldown));
            }
        }
    }
    public boolean snap(){
        this.self.level().playSound(null, this.self.getX(), this.self.getY(),
                this.self.getZ(), ModSounds.SNAP_EVENT, this.self.getSoundSource(), 2.0F, 1F);
        this.setCooldown(PowerIndex.SKILL_3,  multiplyCooldown(ClientNetworking.getAppropriateConfig().magiciansRedSettings.snapFireAwayCooldown));
        clearEverything();
        return true;
    }

    public void clearEverything(){
        this.snapNumber++;
        clearAllHurricanes();
        removeFirestorm();
        if (groundHurricane != null && !groundHurricane.isRemoved()){
            groundHurricane.discard();
        }
        if (tracker != null && !tracker.isRemoved()){
            tracker.discard();
        }
        clearLeaded();
    }

    public void clearLeaded(){
        if (leaded != null){
            this.setCooldown(PowerIndex.SKILL_1, ClientNetworking.getAppropriateConfig().magiciansRedSettings.redBindManualReleaseCooldown);
            ((StandUser)leaded).roundabout$dropString();
            leaded = null;
        }
    }
    private static void explode(Level $$0, BlockPos $$1, @Nullable LivingEntity $$2) {
        if (!$$0.isClientSide) {
            PrimedTnt $$3 = new PrimedTnt($$0, (double)$$1.getX() + 0.5, (double)$$1.getY(), (double)$$1.getZ() + 0.5, $$2);
            $$0.addFreshEntity($$3);
            $$0.playSound((Player)null, $$3.getX(), $$3.getY(), $$3.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
            $$0.gameEvent($$2, GameEvent.PRIME_FUSE, $$1);
        }
    }
    public boolean setFire(){
        if (grabBlock != null){
            BlockState state = this.self.level().getBlockState(grabBlock);
            if (state.is(Blocks.CAMPFIRE)) {
                if (!state.getValue(CampfireBlock.LIT) && this.getSelf().level().getGameRules().getBoolean(ModGamerules.ROUNDABOUT_STAND_GRIEFING) && !(this.self instanceof ServerPlayer SP && SP.gameMode.getGameModeForPlayer() == GameType.ADVENTURE)) {
                    this.getSelf().level().setBlockAndUpdate(grabBlock, state.setValue(CampfireBlock.LIT, true));
                }
            } else if (state.getBlock() instanceof CandleBlock){
                if (!state.getValue(CandleBlock.LIT) && this.getSelf().level().getGameRules().getBoolean(ModGamerules.ROUNDABOUT_STAND_GRIEFING) && !(this.self instanceof ServerPlayer SP && SP.gameMode.getGameModeForPlayer() == GameType.ADVENTURE)){
                    this.getSelf().level().setBlockAndUpdate(grabBlock, state.setValue(CandleBlock.LIT,true));
                }
            } else if (state.getBlock() instanceof TntBlock){
                if (this.getSelf().level().getGameRules().getBoolean(ModGamerules.ROUNDABOUT_STAND_GRIEFING) && !(this.self instanceof ServerPlayer SP && SP.gameMode.getGameModeForPlayer() == GameType.ADVENTURE)){
                    this.getSelf().level().setBlockAndUpdate(grabBlock, Blocks.AIR.defaultBlockState());
                    explode(this.self.level(),grabBlock,this.self);
                }
            } else if (tryPlaceBlock(grabBlock)) {
                this.self.level().playSound(null, this.self.getX(), this.self.getY(),
                        this.self.getZ(), ModSounds.FIRE_WHOOSH_EVENT, this.self.getSoundSource(), 2.0F, 2F);
                for (int j = 0; j < 10; j++) {
                    double random = (Math.random() * 0.8) - 0.4;
                    double random2 = (Math.random() * 0.8) - 0.4;
                    double random3 = (Math.random() * 0.8) - 0.4;
                    ((ServerLevel) this.self.level()).sendParticles(getFlameParticle(), this.self.getX(),
                            this.self.getY() + this.self.getEyeHeight() * 0.7, this.self.getZ(),
                            0,
                            -1 * (this.self.getX() - grabBlock.getX()) + 0.5 + random,
                            -1 * (this.self.getY() - grabBlock.getY()) - 0.5 + random2,
                            -1 * (this.self.getZ() - grabBlock.getZ()) + 0.5 + random3,
                            0.15);
                }
                createStandFire(grabBlock);
                addEXP(1);
            }
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
        BlockState state = ((StandFireBlock)ModBlocks.STAND_FIRE).getStateForPlacement(this.self.level(),pos).
                setValue(StandFireBlock.COLOR,(int)this.getFireColor());
        if(this.getSelf() instanceof Player p){
            if(!canPlaceOnClaimPos(p,pos) ||
            !canPlaceOnClaimPos(p,pos.east()) ||
            !canPlaceOnClaimPos(p,pos.west()) ||
            !canPlaceOnClaimPos(p,pos.north()) ||
            !canPlaceOnClaimPos(p,pos.south()) ||
            !canPlaceOnClaimPos(p,pos.north().east()) ||
            !canPlaceOnClaimPos(p,pos.north().west()) ||
            !canPlaceOnClaimPos(p,pos.south().east()) ||
            !canPlaceOnClaimPos(p,pos.south().west())
            ){
                return;
            }
            this.getSelf().level().setBlockAndUpdate(pos, state);
            BlockEntity be = this.self.level().getBlockEntity(pos);
            if (be instanceof StandFireBlockEntity sfbe){
                sfbe.standUser = this.self;
                sfbe.snapNumber = this.snapNumber;
                sfbe.fireIDNumber = this.fireIDNumber;
                sfbe.fireColorType = getFireColor();
            }

        } else {
            this.getSelf().level().setBlockAndUpdate(pos, state);
            BlockEntity be = this.self.level().getBlockEntity(pos);
            if (be instanceof StandFireBlockEntity sfbe) {
                sfbe.standUser = this.self;
                sfbe.snapNumber = this.snapNumber;
                sfbe.fireIDNumber = this.fireIDNumber;
                sfbe.fireColorType = getFireColor();
            }
        }
    }

    public byte getFireColor(){
        byte skn = ((StandUser)this.getSelf()).roundabout$getStandSkin();

        return switch (skn) {
            case MagiciansRedEntity.BLUE_SKIN, MagiciansRedEntity.BLUE_ACE_SKIN, MagiciansRedEntity.BLUE_ABLAZE, MagiciansRedEntity.SKELETAL -> StandFireType.BLUE.id;
            case MagiciansRedEntity.PURPLE_SKIN, MagiciansRedEntity.PURPLE_ABLAZE -> StandFireType.PURPLE.id;
            case MagiciansRedEntity.GREEN_SKIN, MagiciansRedEntity.GREEN_ABLAZE -> StandFireType.GREEN.id;
            case MagiciansRedEntity.DREAD_SKIN, MagiciansRedEntity.DREAD_ABLAZE, MagiciansRedEntity.DREAD_BEAST_SKIN -> StandFireType.DREAD.id;
            case MagiciansRedEntity.JOJONIUM, MagiciansRedEntity.JOJONIUM_ABLAZE -> StandFireType.CREAM.id;
            default -> StandFireType.ORANGE.id;
        };
    }

    public Block getFireColorBlock(){
        byte skn = ((StandUser)this.getSelf()).roundabout$getStandSkin();
        return switch (skn) {
            case MagiciansRedEntity.BLUE_SKIN, MagiciansRedEntity.BLUE_ACE_SKIN, MagiciansRedEntity.BLUE_ABLAZE, MagiciansRedEntity.SKELETAL -> ModBlocks.BLUE_FIRE;
            case MagiciansRedEntity.PURPLE_SKIN, MagiciansRedEntity.PURPLE_ABLAZE -> ModBlocks.PURPLE_FIRE;
            case MagiciansRedEntity.GREEN_SKIN, MagiciansRedEntity.GREEN_ABLAZE -> ModBlocks.GREEN_FIRE;
            case MagiciansRedEntity.DREAD_SKIN, MagiciansRedEntity.DREAD_ABLAZE, MagiciansRedEntity.DREAD_BEAST_SKIN -> ModBlocks.DREAD_FIRE;
            case MagiciansRedEntity.JOJONIUM, MagiciansRedEntity.JOJONIUM_ABLAZE -> ModBlocks.CREAM_FIRE;
            default -> ModBlocks.ORANGE_FIRE;
        };
    }

    @Override
    public float getReach(){
        if (this.isHoldingSneak()){
            return 5;
        }
        return 7;
    }

    @Override
    public void tickStandRejection(MobEffectInstance effect){
        if (!this.getSelf().level().isClientSide()) {
            if (effect.getDuration() == 15) {
                StandUser user = ((StandUser) this.self);
                user.roundabout$setSecondsOnStandFire(20);
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.MAGICIANS_RED_CRY_3_EVENT,
                        SoundSource.PLAYERS, 1F, 1F);
                user.roundabout$setOnStandFire((byte) 1, this.self);
                ((ServerLevel) this.self.level()).sendParticles(getFlameParticle(), this.self.getX(),
                        this.self.getY()+(this.self.getBbHeight()*0.5), this.self.getZ(),
                        10,
                        0.25, 0.25, 0.25,
                        0.005);
            }
        }
    }

    @Override
    public void levelUp(){
        if (!this.getSelf().level().isClientSide() && this.getSelf() instanceof Player PE){
            IPlayerEntity ipe = ((IPlayerEntity) PE);
            byte level = ipe.roundabout$getStandLevel();
            if (level == 7) {
                ((ServerPlayer) this.self).displayClientMessage(Component.translatable("leveling.roundabout.levelup.max.both").
                        withStyle(ChatFormatting.AQUA), true);
            } else {
                ((ServerPlayer) this.self).displayClientMessage(Component.translatable("leveling.roundabout.levelup.both").
                        withStyle(ChatFormatting.AQUA), true);
            }
        }
        super.levelUp();
    }

    @Override
    public byte getMaxLevel(){
        return 7;
    }

    public float getHurricaneDirectDamage(Entity entity, float size, boolean fireStorm){
        if (this.getReducedDamage(entity)){
            return bumpDamage(levelupDamageMod(multiplyPowerByStandConfigPlayers((float) (0.5+((size/60)* 5.5)))),fireStorm);
        } else {
            return bumpDamage(levelupDamageMod(multiplyPowerByStandConfigMobs(1+((size/60)* 16))),fireStorm);
        }
    }
    public float getHurricaneDamage(Entity entity,  float size, boolean fireStorm){
        if (size >=52){size=60;}
        if (this.getReducedDamage(entity)){
            return bumpDamage(levelupDamageMod(multiplyPowerByStandConfigPlayers((float) (0.5+((size/60)* 2.5)))),fireStorm);
        } else {
            return bumpDamage(levelupDamageMod(multiplyPowerByStandConfigMobs(1+((size/60)* 9))),fireStorm);
        }
    }
    public float getFireballDamage(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod(multiplyPowerByStandConfigPlayers(1.5F));
        } else {
            return levelupDamageMod(multiplyPowerByStandConfigMobs(4));
        }
    }


    @Override
    public float getPunchStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod(multiplyPowerByStandConfigPlayers(1.2F));
        } else {
            return levelupDamageMod(multiplyPowerByStandConfigMobs(3.5F));
        }
    }
    @Override
    public float getHeavyPunchStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod(multiplyPowerByStandConfigPlayers(1.55F));
        } else {
            return levelupDamageMod(multiplyPowerByStandConfigMobs(4.5F));
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

                float distMax = (float) getBlockDistanceOut(this.self,this.getReach());
                Entity targetEntity = getTargetEntity(this.self,distMax);

                List<Entity> listE = getTargetEntityList(this.self,distMax);
                int id = -1;
                if (targetEntity != null){
                    id = targetEntity.getId();
                }
                C2SPacketUtil.standPunchPacket(id, this.activePowerPhase);
                if (!listE.isEmpty()){
                    for (int i = 0; i< listE.size(); i++){
                        if (!(targetEntity != null && listE.get(i).is(targetEntity)) && listE.get(i).distanceTo(this.self) < distMax) {
                            if (!(listE.get(i) instanceof StandEntity)) {
                                C2SPacketUtil.standPunchPacket(listE.get(i).getId(), (byte) (this.activePowerPhase + 50));
                            }
                        }
                    }
                }
            }
        } else {
            /*Caps how far out the punch goes*/

            float distMax = (float) getBlockDistanceOut(this.self,this.getReach());
            Entity targetEntity = getTargetEntity(this.self,distMax);

            List<Entity> listE = getTargetEntityList(this.self,distMax);
            punchImpact(targetEntity);
            if (!listE.isEmpty()){
                for (int i = 0; i< listE.size(); i++){
                    if (!(storeEnt != null && listE.get(i).is(storeEnt) && listE.get(i).distanceTo(this.self) < distMax)) {
                        if (!(listE.get(i) instanceof StandEntity)) {
                            this.setActivePowerPhase((byte) (this.getActivePowerPhase()+50));
                            punchImpact(listE.get(i));
                        }
                    }
                }
            }
        }

    }

    @Override
    public boolean setPowerGuard() {
        return super.setPowerGuard();
    }

    public int drillTime = -1;
    public boolean drillT = false;
    public boolean doRedBindAttack(){
            drillT = true;
            this.setCooldown(PowerIndex.SKILL_1, ClientNetworking.getAppropriateConfig().magiciansRedSettings.redBindDazeAttackCooldown);
            if (!this.self.level().isClientSide()) {
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.FIRE_BLAST_EVENT, SoundSource.PLAYERS, 1F, 1F);
                drillTime = 80;
                addEXP(6, leaded);
                ((StandUser) leaded).roundabout$setRedBound(true);
                ((ServerLevel) this.self.level()).sendParticles(getFlameParticle(), leaded.getX(),
                        leaded.getY() + (leaded.getBbHeight() * 0.5), leaded.getZ(),
                        10,
                        0.25, 0.25, 0.25,
                        0.005);
            }
        return true;
    }

    public void redBindDrill(){
        offloadLead();
        if (leaded != null) {
            setDazed( leaded, (byte) 3);
            leaded.hurtMarked = true;
            leaded.setDeltaMovement(leaded.getDeltaMovement().x(),0.028,leaded.getDeltaMovement().z());
            ((ServerLevel) this.self.level()).sendParticles(getFlameParticle(), leaded.getX(),
                    leaded.getY()+(leaded.getBbHeight()*0.7), leaded.getZ(),
                    2,
                    0.25, 0.3, 0.25,
                    0.005);
            leaded.hasImpulse = true;
        }
    }


    /** Red Bind attack inserted here*/
    @Override
    public boolean setPowerAttack(){
            if (this.activePowerPhase >= 3) {
                this.activePowerPhase = 1;
            } else {
                this.activePowerPhase++;
                if (this.activePowerPhase == 3) {
                    this.attackTimeMax = ClientNetworking.getAppropriateConfig().magiciansRedSettings.lastLashInStringCooldown;
                } else {
                    this.attackTimeMax = ClientNetworking.getAppropriateConfig().magiciansRedSettings.lashCooldown;
                }

            }

            this.attackTimeDuring = 0;
            this.setActivePower(PowerIndex.ATTACK);
            this.setAttackTime(0);

            animateStand((byte) (40+this.activePowerPhase));
            poseStand(OffsetIndex.ATTACK);
        return true;
    }
    public boolean setPowerRangedBarrageCharge() {
        animateStand(MagiciansRedEntity.FIREBALL_CHARGE);
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.RANGED_BARRAGE_CHARGE);
        this.poseStand(OffsetIndex.ATTACK);
        this.clashDone = false;
        playRangedBarrageChargeSound();
        return true;
    }
    public boolean setPowerRangedBarrageCharge2() {
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.RANGED_BARRAGE_CHARGE_2);
        this.poseStand(OffsetIndex.ATTACK);
        this.clashDone = false;
        playRangedBarrageChargeSound2();
        animateStand(MagiciansRedEntity.FLAMETHROWER_CHARGE);
        return true;
    }
    public void playRangedBarrageChargeSound(){
        if (!this.self.level().isClientSide()) {
            SoundEvent barrageChargeSound = this.getBarrageChargeSound();
            if (barrageChargeSound != null) {
                playSoundsIfNearby(RANGED_CHARGE_1, 27, false);
            }
        }
    }
    public void playRangedBarrageChargeSound2(){
        if (!this.self.level().isClientSide()) {
            SoundEvent barrageChargeSound = this.getBarrageChargeSound();
            if (barrageChargeSound != null) {
                playSoundsIfNearby(RANGED_CHARGE_2, 27, false);
            }
        }
    }

    public float getSoundPitchFromByte(byte soundChoice){
        if (soundChoice == RANGED_CHARGE_1) {
            return 1 / ((float) this.getRangedBarrageWindup() / 20);
        } else if (soundChoice == RANGED_CHARGE_2){
                return 1/((float) this.getRangedBarrageWindup2() /20);
        } else {
            return super.getSoundPitchFromByte(soundChoice);
        }
    }



    public int anticipationticks = 0;
    @Override
    public void tickMobAI(LivingEntity attackTarget){
        if (attackTarget != null && attackTarget.isAlive() && !this.isDazed(this.getSelf())) {
            double dist = attackTarget.distanceTo(this.getSelf());
                boolean isCreeper = this.getSelf() instanceof Creeper;
                if (isCreeper) {
                    if (leaded != null) {
                        if (anticipationticks == 0){
                            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.EXTRA_2, true);
                            anticipationticks = -1;
                        } else {
                            anticipationticks--;
                        }
                    } else if (dist <= 5 && !this.onCooldown(PowerIndex.SKILL_1)) {
                        if (this.activePower == PowerIndex.NONE) {
                            anticipationticks = 4;
                            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_1, true);
                        }
                    }
                } else {

                    if ((this.self instanceof Ravager || this.self instanceof Hoglin || this.self instanceof WitherBoss ||
                            this.self instanceof EnderDragon || this.self instanceof Warden) && !isUsingFirestorm() &&
                            !this.self.isUnderWater()){
                        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_4_SNEAK, true);
                    }

                    if (this.getActivePower() != PowerIndex.NONE
                            || attackTarget.distanceTo(this.getSelf()) <= 7 || hasHurricane()){
                        this.getSelf().setXRot(getLookAtEntityPitch(this.getSelf(), attackTarget));
                        float yrot = getLookAtEntityYaw(this.getSelf(), attackTarget);
                        this.getSelf().setYRot(yrot);
                        this.getSelf().setYHeadRot(yrot);
                    }

                    boolean isBasicMob = (this.self instanceof Zombie || this.self instanceof Spider || this.self instanceof Skeleton);

                    if (!(isBasicMob)) {
                        if (dist <= 25 && !hasHurricane() && activePower == PowerIndex.NONE) {

                            if ((this.self instanceof Raider || this.self instanceof Villager || this.self instanceof AvdolNPC ||
                                    this.self instanceof Blaze || this.self instanceof Piglin || this.self instanceof EnderMan ||
                                    this.self instanceof Hoglin || this.self instanceof ZombifiedPiglin || this.self instanceof PiglinBrute)
                                    && !this.onCooldown(PowerIndex.SKILL_2_SNEAK)
                            ) {
                                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_2_SNEAK, true);
                            } else if (dist > 7 && !this.onCooldown(PowerIndex.SKILL_2) && !isLockedByWater()) {
                                double RNG = Math.random();
                                if (RNG < 0.3) {
                                    wentForCharge = true;
                                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.RANGED_BARRAGE_CHARGE, true);
                                } else {
                                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_2, true);
                                }
                            }
                        }
                    }


                    if (dist <= 7 && !hasHurricane() && (activePower == PowerIndex.NONE || activePower == PowerIndex.ATTACK)){
                        Entity targetEntity = getTargetEntity(this.self, -1);
                        if (targetEntity != null && targetEntity.is(attackTarget)) {
                            if (this.attackTimeDuring <= -1) {
                                double RNG = Math.random();
                                if (RNG < 0.35 && targetEntity instanceof Player && this.activePowerPhase <= 0 && !wentForCharge && !isLockedByWater() && !(isBasicMob)){
                                    wentForCharge = true;
                                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.RANGED_BARRAGE_CHARGE_2, true);
                                } else if ((this.activePowerPhase < this.activePowerPhaseMax || this.attackTime >= this.attackTimeMax) &&
                                        (this.activePower == PowerIndex.NONE || this.activePower == PowerIndex.ATTACK)) {
                                    if (RNG < 0.5 && (this.self instanceof IronGolem ||
                                            this.self instanceof Ravager || this.self instanceof Piglin || this.self instanceof AvdolNPC ||
                                            this.self instanceof ZombifiedPiglin ||this.self instanceof Hoglin ||
                                            this.self instanceof PiglinBrute)){
                                        wentForCharge = false;
                                        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.SNEAK_ATTACK_CHARGE, true);
                                    } else {
                                        wentForCharge = false;
                                        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.ATTACK, true);
                                    }
                                }
                            }
                        }
                    }

                    if (hasHurricaneSingle()){
                        if (hurricane.getSize() >= hurricane.getMaxSize()){
                            double RNG = Math.random();
                            if (RNG < 0.5 && (groundHurricane == null || groundHurricane.isRemoved())){
                                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_3_BONUS, true);
                            } else {
                                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_2_BONUS, true);
                            }
                        }
                    } else if (hasHurricaneSpecial() && activePower == PowerIndex.NONE){
                        if (!this.onCooldown(PowerIndex.SKILL_EXTRA_2)) {
                            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_2_BONUS, true);
                        }
                    }


                }
                /**
                 if (dist <= 8 && (hurricaneSpecial == null || hurricaneSpecial.isEmpty())) {
                 ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_2_SNEAK, true);
                 }
                 **/
        } else {
            if (hasHurricane()){
                clearAllHurricanes();
            }
        }
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
            if (!this.self.level().isClientSide) {
                ((ServerLevel) this.self.level()).sendParticles(ParticleTypes.SMOKE, entity.getEyePosition().x, entity.getEyePosition().y, entity.getEyePosition().z,
                        4, 0.1, 0.1, 0.1, 0.3);
            }
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

    public boolean isLockedByWater(){
        if (this.self.isUnderWater()){
            return true;
        }
        if (this.isInRain()){
            if (!isUsingFirestorm()){
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean isAttackIneptVisually(byte activeP, int slot){
        if ((this.self.isUnderWater() || this.isInRain()) && !(slot == 4 && !this.isHoldingSneak()) && !(slot == 3 && !this.isGuarding())){
            if (!isUsingFirestorm() || this.self.isUnderWater()){
                return true;
            }
        }
        if (this.isChargingCrossfireSpecial() || (slot != 2 && slot != 3 && (isChargingCrossfireSingle() || hasHurricaneSingle()))){
            if (canShootConcealedCrossfire() && slot == 1){
                return false;
            }
            return true;
        }
        if (activeP == PowerIndex.POWER_4){
            return true;
        }
        return super.isAttackIneptVisually(activeP,slot);
    }

    @Override
    public void onStandSwitchInto(){
        if (!(this.getSelf() instanceof Player && (((Player)this.getSelf()).isCreative()))) {
            if (this.getSelf() instanceof Player) {
                if (!isClient()) {
                    S2CPacketUtil.sendCooldownSyncPacket(((ServerPlayer) this.getSelf()), PowerIndex.SKILL_2_SNEAK, ClientNetworking.getAppropriateConfig().magiciansRedSettings.hurricaneSpecialCooldown);
                    S2CPacketUtil.sendCooldownSyncPacket(((ServerPlayer) this.getSelf()), PowerIndex.SKILL_4, ClientNetworking.getAppropriateConfig().magiciansRedSettings.flameCrashCooldown);
                }
            }
            this.setCooldown(PowerIndex.SKILL_2_SNEAK, ClientNetworking.getAppropriateConfig().magiciansRedSettings.hurricaneSpecialCooldown);
            this.setCooldown(PowerIndex.SKILL_4, ClientNetworking.getAppropriateConfig().magiciansRedSettings.flameCrashCooldown);
        }
        super.onStandSwitchInto();
    }

    public boolean isInRain() {
        BlockPos $$0 = this.self.blockPosition();
        return this.self.level().isRainingAt($$0)
                || this.self.level().isRainingAt(BlockPos.containing((double)$$0.getX(), this.self.getBoundingBox().maxY, (double)$$0.getZ()));
    }

    @Override
    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypas){
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        $$1.add(drawSingleGUIIcon(context,18,leftPos+20,topPos+80,0, "ability.roundabout.red_lash",
                "instruction.roundabout.press_attack", StandIcons.RED_LASH,0,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+20, topPos+99,0, "ability.roundabout.guard",
                "instruction.roundabout.hold_block", StandIcons.MAGICIANS_RED_GUARD,0,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+20,topPos+118,0, "ability.roundabout.flame_kick",
                "instruction.roundabout.hold_attack_crouch", StandIcons.MAGICIANS_FLAME_KICK,0,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+39,topPos+80,0, "ability.roundabout.fireball_barrage",
                "instruction.roundabout.barrage", StandIcons.FIREBALL_BARRAGE,0,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+39,topPos+99,0, "ability.roundabout.flamethrower",
                "instruction.roundabout.kick_barrage", StandIcons.FLAMETHROWER,0,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+39,topPos+118,0, "ability.roundabout.furnace",
                "instruction.roundabout.passive", StandIcons.FURNACE_ABILITY,1,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+58,topPos+80, 0, "ability.roundabout.red_bind",
                "instruction.roundabout.press_skill", StandIcons.RED_BIND,1,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+58,topPos+99, 0, "ability.roundabout.flame_creation",
                "instruction.roundabout.press_skill_crouch", StandIcons.LIGHT_FIRE,1,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+58,topPos+118,5, "ability.roundabout.life_detector",
                "instruction.roundabout.press_skill_block", StandIcons.LIFE_TRACKER,1,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+77,topPos+80,0, "ability.roundabout.crossfire",
                "instruction.roundabout.press_skill", StandIcons.CROSSFIRE_HURRICANE,2,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+77,topPos+99,0, "ability.roundabout.crossfire_block",
                "instruction.roundabout.press_skill", StandIcons.CONCEALED_HURRICANE,1,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+77,topPos+118,3, "ability.roundabout.crossfire_ground",
                "instruction.roundabout.press_skill", StandIcons.HIDDEN_HURRICANE,3,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+96,topPos+80,4, "ability.roundabout.crossfire_special",
                "instruction.roundabout.press_skill_crouch", StandIcons.CROSSFIRE_HURRICANE_SPECIAL,2,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+96,topPos+99,0, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE,3,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+96,topPos+118,0, "ability.roundabout.vault",
                "instruction.roundabout.press_skill_air", StandIcons.MAGICIANS_RED_LEDGE_GRAB,3,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+115,topPos+80,0, "ability.roundabout.flame_extinguish",
                "instruction.roundabout.press_skill_crouch", StandIcons.SNAP_ICON,3,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+115,topPos+99,2, "ability.roundabout.projectile_burn",
                "instruction.roundabout.press_skill_block", StandIcons.PROJECTILE_BURN,3,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+115,topPos+118,7, "ability.roundabout.crossfire_firestorm",
                "instruction.roundabout.press_skill", StandIcons.CROSSFIRE_FIRESTORM,4,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+134,topPos+80,6, "ability.roundabout.fire_slam",
                "instruction.roundabout.press_skill_crouch", StandIcons.FIRE_SLAM,4,level,bypas));
        return $$1;
    }
    //Level 7 = Firestorm
    //Level 6 = Fire Slam
    //Level 5 = Life Detector
    //Level 4 = Crossfire Special
    //Level 3 = Crossfire Ground
    //Level 2 = Projectile Burn
}
