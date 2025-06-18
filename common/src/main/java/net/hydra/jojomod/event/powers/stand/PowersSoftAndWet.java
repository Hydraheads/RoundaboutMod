package net.hydra.jojomod.event.powers.stand;

import com.google.common.collect.Lists;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IAbstractArrowAccess;
import net.hydra.jojomod.access.IMob;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.block.BubbleScaffoldBlockEntity;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.pathfinding.GroundBubbleEntity;
import net.hydra.jojomod.entity.projectile.*;
import net.hydra.jojomod.entity.stand.*;
import net.hydra.jojomod.entity.substand.EncasementBubbleEntity;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.entity.visages.mobs.AvdolNPC;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.presets.PunchingStand;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.config.ClientConfig;
import net.hydra.jojomod.util.config.ConfigManager;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class PowersSoftAndWet extends PunchingStand {
    public PowersSoftAndWet(LivingEntity self) {
        super(self);
    }

    public List<SoftAndWetBubbleEntity> bubbleList = new ArrayList<>();
    @Override
    public StandEntity getNewStandEntity(){
        byte sk = ((StandUser)this.getSelf()).roundabout$getStandSkin();
        if (sk == SoftAndWetEntity.KING_SKIN) {
            return ModEntities.SOFT_AND_WET_KING.create(this.getSelf().level());
        } else if (sk == SoftAndWetEntity.DEBUT){
                return ModEntities.SOFT_AND_WET_DEBUT.create(this.getSelf().level());
        } else if (sk == SoftAndWetEntity.DROWNED_SKIN
        || sk == SoftAndWetEntity.DROWNED_SKIN_2){
            return ModEntities.SOFT_AND_WET_DROWNED.create(this.getSelf().level());
        } else if (sk == SoftAndWetEntity.KIRA){
            return ModEntities.SOFT_AND_WET_KILLER_QUEEN.create(this.getSelf().level());
        }
        return ModEntities.SOFT_AND_WET.create(this.getSelf().level());
    }

    @Override
    public float getMiningMultiplier() {
        return (float) (1F*(ClientNetworking.getAppropriateConfig().
                miningSettings.speedMultiplierSoftAndWet*0.01));
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

    @Override
    public int getMiningLevel() {
        return ClientNetworking.getAppropriateConfig().miningSettings.getMiningTierSoftAndWet;
    }
    @Override
    public boolean canSummonStand(){
        if (this.getSelf() instanceof Creeper){
            return false;
        }
        return true;
    }
    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersSoftAndWet(entity);
    }


    @Override
    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }
    @Override
    public byte getSoundCancelingGroupByte(byte soundChoice) {
        if (soundChoice == SoundIndex.ALT_CHARGE_SOUND_1){
            return SoundIndex.BARRAGE_SOUND_GROUP;
        } else if (soundChoice >= BARRAGE_NOISE && soundChoice <= BARRAGE_NOISE_2) {
            return SoundIndex.BARRAGE_SOUND_GROUP;
        }
        return super.getSoundCancelingGroupByte(soundChoice);
    }

    @Override
    public void playBarrageClashSound(){
        if (!this.self.level().isClientSide()) {
            byte skn = ((StandUser)this.getSelf()).roundabout$getStandSkin();
            if (skn == SoftAndWetEntity.KIRA){
            } else {
                playStandUserOnlySoundsIfNearby(BARRAGE_NOISE, 27, false, true);
            }
        }
    }
    @Override
    public SoundEvent getSoundFromByte(byte soundChoice) {
        byte bt = ((StandUser) this.getSelf()).roundabout$getStandSkin();
        if (soundChoice == SoundIndex.SUMMON_SOUND) {
            return ModSounds.SUMMON_SOFT_AND_WET_EVENT;
        } else if (soundChoice == BARRAGE_NOISE) {
            return ModSounds.SOFT_AND_WET_BARRAGE_EVENT;
        } else if (soundChoice == SoundIndex.ALT_CHARGE_SOUND_1) {
            return ModSounds.STAND_BARRAGE_WINDUP_EVENT;
        } else if (soundChoice == BARRAGE_NOISE_2) {
            return ModSounds.SOFT_AND_WET_BARRAGE_2_EVENT;
        }
        return super.getSoundFromByte(soundChoice);
    }



    public void bubbleListInit(){
        if (bubbleList == null) {
            bubbleList = new ArrayList<>();
        }
    }

    @Override
    public List<Byte> getSkinList(){
        List<Byte> $$1 = Lists.newArrayList();
        $$1.add(SoftAndWetEntity.LIGHT_SKIN);
        if (this.getSelf() instanceof Player PE){
            byte Level = ((IPlayerEntity)PE).roundabout$getStandLevel();
            ItemStack goldDisc = ((StandUser)PE).roundabout$getStandDisc();
            boolean bypass = PE.isCreative() || (!goldDisc.isEmpty() && goldDisc.getItem() instanceof MaxStandDiscItem);
            if (Level > 1 || bypass){
                $$1.add(SoftAndWetEntity.MANGA_SKIN);
            } if (Level > 2 || bypass){
                $$1.add(SoftAndWetEntity.DEBUT);
            } if (Level > 3 || bypass){
                $$1.add(SoftAndWetEntity.STRIPED);
            } if (Level > 4 || bypass){
                $$1.add(SoftAndWetEntity.FIGURE_SKIN);
            } if (Level > 5 || bypass){
                $$1.add(SoftAndWetEntity.DROWNED_SKIN);
                $$1.add(SoftAndWetEntity.DROWNED_SKIN_2);
            } if (Level > 6 || bypass){
                $$1.add(SoftAndWetEntity.KING_SKIN);
                $$1.add(SoftAndWetEntity.BETA_SKIN);
            } if (((IPlayerEntity)PE).roundabout$getUnlockedBonusSkin() || bypass){
                $$1.add(SoftAndWetEntity.KIRA);
            }
        }
        return $$1;
    }

    public boolean goBeyondCharged(){
        return getInExplosiveSpinMode() || (this.self instanceof Player PE && PE.isCreative());
    }
    public boolean isAttackIneptVisually(byte activeP, int slot) {
        if (inShootingMode()){
            if (slot == 1 && (!goBeyondCharged() || getGoBeyondTarget() == null)){
                return true;
            }
            if (slot == 2 && !canDoBubbleItemLaunch() && !isHoldingSneak()){
                return true;
            }
        } else {
            if (slot == 1 && (!canDoBubbleClusterRedirect() && isGuarding())) {
                return true;
            }

            if (slot == 2 && ((!canDoBubbleRedirect() && isGuarding()))) {
                return true;
            }

            if (slot == 4 && isHoldingSneak()){
                if (!canUseWaterShield()){
                    return true;
                }
            }
        }

        if (slot == 3 && (!canVault() && !canFallBrace() && !isGuarding() && isHoldingSneak()) && !canBridge()){
            return true;
        }
        if (slot == 3 && (!canVault() && !canFallBrace() && isGuarding() && !canBigBubble())){
            return false;
        }

        return super.isAttackIneptVisually(activeP,slot);
    }

    public int waterShieldTicks = 0;
    public boolean hasWaterShield(){
        return waterShieldTicks > 0;
    }
    public void setWaterShieldTicks(int ticks){
        waterShieldTicks = ticks;
    }
    public int getWaterShieldTicks(){
        return waterShieldTicks;
    }
    public void tickWaterShield(){
        if (waterShieldTicks > 0){
            waterShieldTicks--;
        }
    }

    @Override
    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypas){
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        $$1.add(drawSingleGUIIcon(context,18,leftPos+20,topPos+80,0, "ability.roundabout.punch",
                "instruction.roundabout.press_attack", StandIcons.SOFT_AND_WET_PUNCH,0,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+20, topPos+99,0, "ability.roundabout.guard",
                "instruction.roundabout.hold_block", StandIcons.SOFT_AND_WET_GUARD,0,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+20,topPos+118,0, "ability.roundabout.encasement_strike",
                "instruction.roundabout.hold_attack_crouch", StandIcons.ENCASEMENT_STRIKE,0,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+39,topPos+80,0, "ability.roundabout.barrage",
                "instruction.roundabout.barrage", StandIcons.SOFT_AND_WET_BARRAGE,0,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+39,topPos+99,getShootingModeLevel(), "ability.roundabout.bubble_barrage",
                "instruction.roundabout.shooting_barrage", StandIcons.BUBBLE_BARRAGE,0,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+39,topPos+118,0, "ability.roundabout.bubble_selection",
                "instruction.roundabout.press_skill", StandIcons.PLUNDER_SELECTION,1,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+58,topPos+80, getGoBeyondLevel(), "ability.roundabout.go_beyond",
                "instruction.roundabout.press_skill_explosive_spin_mode", StandIcons.GO_BEYOND,1,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+58,topPos+99, getSpreadLevel(), "ability.roundabout.bubble_spread",
                "instruction.roundabout.press_skill_crouch", StandIcons.PLUNDER_BUBBLE_FILL,1,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+58,topPos+118,getSpreadLevel(), "ability.roundabout.bubble_spread_redirect",
                "instruction.roundabout.press_skill_block", StandIcons.PLUNDER_BUBBLE_FILL_CONTROL,1,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+77,topPos+80,0, "ability.roundabout.plunder_bubble",
                "instruction.roundabout.press_skill", StandIcons.PLUNDER_BUBBLE,2,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+77,topPos+99,0, "ability.roundabout.bubble_pop",
                "instruction.roundabout.press_skill_crouch", StandIcons.PLUNDER_BUBBLE_POP,2,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+77,topPos+118,0, "ability.roundabout.bubble_redirect",
                "instruction.roundabout.press_skill_block", StandIcons.PLUNDER_BUBBLE_CONTROL,2,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+96,topPos+80,getItemShootingLevel(), "ability.roundabout.item_launching_bubble",
                "instruction.roundabout.press_skill_shooting_mode", StandIcons.ITEM_BUBBLE,2,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+96,topPos+99,0, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE,3,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+96,topPos+118,0, "ability.roundabout.fall_brace",
                "instruction.roundabout.press_skill_falling", StandIcons.SOFT_AND_WET_FALL_CATCH,3,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+115,topPos+80,0, "ability.roundabout.vault",
                "instruction.roundabout.press_skill_air", StandIcons.SOFT_AND_WET_VAULT,3,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+115,topPos+99,getScaffoldLevel(), "ability.roundabout.bubble_scaffold",
                "instruction.roundabout.press_skill_crouch", StandIcons.SOFT_AND_WET_BUBBLE_SCAFFOLD,3,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+115,topPos+118,0, "ability.roundabout.encasement_bubble",
                "instruction.roundabout.press_skill_block", StandIcons.SOFT_AND_WET_BUBBLE_ENCASEMENT,3,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+134,topPos+80,getShootingModeLevel(), "ability.roundabout.shooting_mode",
                "instruction.roundabout.press_skill", StandIcons.SOFT_SHOOTING_MODE,4,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+134,topPos+99,getWaterShieldLevel(), "ability.roundabout.water_shield",
                "instruction.roundabout.press_skill_crouch", StandIcons.WATER_SHIELD,4,level,bypas));
        return $$1;
    }

    public int getGoBeyondLevel(){
        return 7;
    }
    public int getWaterShieldLevel(){
        return 6;
    }
    public int getItemShootingLevel(){
        return 5;
    }
    public int getShootingModeLevel(){
        return 4;
    }
    public int getScaffoldLevel(){
        return 3;
    }
    public int getSpreadLevel(){
        return 2;
    }
    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {

        if (inShootingMode()) {
            if (canExecuteMoveWithLevel(getGoBeyondLevel())) {
                setSkillIcon(context, x, y, 1, StandIcons.GO_BEYOND, PowerIndex.SKILL_EXTRA_2);
            } else {
                setSkillIcon(context, x, y, 1, StandIcons.LOCKED, PowerIndex.NO_CD,true);
            }
        } else if (isGuarding()) {
            if (canExecuteMoveWithLevel(getSpreadLevel())) {
                setSkillIcon(context, x, y, 1, StandIcons.PLUNDER_BUBBLE_FILL_CONTROL, PowerIndex.SKILL_EXTRA_2);
            } else {
                setSkillIcon(context, x, y, 1, StandIcons.LOCKED, PowerIndex.NO_CD,true);
            }
        } else if (isHoldingSneak()){
            if (canExecuteMoveWithLevel(getSpreadLevel())){
                if (canDoBubbleClusterPop()){
                    setSkillIcon(context, x, y, 1, StandIcons.PLUNDER_BUBBLE_FILL_POP, PowerIndex.SKILL_2_SNEAK);
                } else {
                    setSkillIcon(context, x, y, 1, StandIcons.PLUNDER_BUBBLE_FILL, PowerIndex.SKILL_1_SNEAK);
                }
            } else {
                setSkillIcon(context, x, y, 1, StandIcons.LOCKED, PowerIndex.NO_CD,true);
            }
        } else {
            setSkillIcon(context, x, y, 1, StandIcons.PLUNDER_SELECTION, PowerIndex.NO_CD);
        }


        if (inShootingMode()) {
            if (isHoldingSneak()){
                setSkillIcon(context, x, y, 2, StandIcons.PLUNDER_BUBBLE_POP, PowerIndex.SKILL_2_SNEAK);
            } else {
                if (canExecuteMoveWithLevel(getItemShootingLevel())) {
                    setSkillIcon(context, x, y, 2, StandIcons.ITEM_BUBBLE, PowerIndex.SKILL_2);
                } else {
                    setSkillIcon(context, x, y, 2, StandIcons.LOCKED, PowerIndex.NO_CD,true);
                }
            }
        } else if (isGuarding()){
            setSkillIcon(context, x, y, 2, StandIcons.PLUNDER_BUBBLE_CONTROL, PowerIndex.SKILL_EXTRA_2);
        } else if (isHoldingSneak()){
            setSkillIcon(context, x, y, 2, StandIcons.PLUNDER_BUBBLE_POP, PowerIndex.SKILL_2_SNEAK);
        } else {
            setSkillIcon(context, x, y, 2, StandIcons.PLUNDER_BUBBLE, PowerIndex.SKILL_2);
        }

        if (canVault()) {
            setSkillIcon(context, x, y, 3, StandIcons.SOFT_AND_WET_VAULT, PowerIndex.SKILL_3_SNEAK);
        } else if (canFallBrace()) {
            setSkillIcon(context, x, y, 3, StandIcons.SOFT_AND_WET_FALL_CATCH, PowerIndex.NONE);
        } else if (isGuarding()) {
            setSkillIcon(context, x, y, 3, StandIcons.SOFT_AND_WET_BUBBLE_ENCASEMENT, PowerIndex.SKILL_EXTRA);
        } else if (isHoldingSneak()){
            if (canExecuteMoveWithLevel(getScaffoldLevel())) {
                setSkillIcon(context, x, y, 3, StandIcons.SOFT_AND_WET_BUBBLE_SCAFFOLD, PowerIndex.SKILL_3);
            } else {
                setSkillIcon(context, x, y, 3, StandIcons.LOCKED, PowerIndex.NO_CD,true);
            }
        } else {
            setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.SKILL_3_SNEAK);
        }

        if (inShootingMode()) {
            setSkillIcon(context, x, y, 4, StandIcons.SOFT_SHOOTING_MODE_EXIT, PowerIndex.SKILL_4);
        } else {
            if (isHoldingSneak()) {
                if (canExecuteMoveWithLevel(getWaterShieldLevel())) {
                    setSkillIcon(context, x, y, 4, StandIcons.WATER_SHIELD, PowerIndex.SKILL_4_SNEAK);
                } else {
                    setSkillIcon(context, x, y, 4, StandIcons.LOCKED, PowerIndex.NO_CD,true);
                }
            } else {
                if (canExecuteMoveWithLevel(getShootingModeLevel())) {
                    setSkillIcon(context, x, y, 4, StandIcons.SOFT_SHOOTING_MODE, PowerIndex.SKILL_4);
                } else {
                    setSkillIcon(context, x, y, 4, StandIcons.LOCKED, PowerIndex.NO_CD,true);
                }
            }
        }

    }

    @Override
    public byte getMaxLevel(){
        return 7;
    }
    @Override
    public int getExpForLevelUp(int currentLevel){
        int amt;
        if (currentLevel == 1){
            amt = 100;
        } else {
            amt = (100+((currentLevel-1)*55));
        }
        amt= (int) (amt*(ClientNetworking.getAppropriateConfig().standExperienceNeededForLevelupMultiplier *0.01));
        return amt;
    }
    public boolean canUseWaterShield(){
        ItemStack stack = this.getSelf().getMainHandItem();
        ItemStack stack2 = this.getSelf().getOffhandItem();
        return ((!stack.isEmpty() && stack.getItem() instanceof PotionItem PI && PotionUtils.getPotion(stack) == Potions.WATER)
        || (!stack2.isEmpty() && stack2.getItem() instanceof PotionItem PI2 && PotionUtils.getPotion(stack2) == Potions.WATER));
    }

    /**For mob ai, change the bubbleType before trypower to set what kind of plunder it has*/
    public byte bubbleType = PlunderTypes.ITEM.id;


    @Override
    public boolean canGuard(){
        return super.canGuard();
    }

    public boolean hold1 = false;
    public boolean holdDownClick = false;

    public int getUseTicks(){
        return ClientNetworking.getAppropriateConfig().softAndWetSettings.heatGainedPerShot;
    }
    public int getGoBeyondUseTicks(){
        return ClientNetworking.getAppropriateConfig().softAndWetSettings.explosiveSpinMeterGainedPerShot;

}
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
                    if (inShootingMode()){
                        if (!holdDownClick){
                            if (!this.onCooldown(PowerIndex.SKILL_4) && ((getActivePower() == PowerIndex.NONE)
                            || getActivePower() == PowerIndex.POWER_4_EXTRA)) {
                                if (getInExplosiveSpinMode() || confirmShot(getUseTicks())) {
                                    if (this.self instanceof Player PE){
                                        IPlayerEntity ipe = ((IPlayerEntity)PE);
                                        ipe.roundabout$getBubbleShotAim().stop();
                                        ipe.roundabout$setBubbleShotAimPoints(10);
                                    }
                                    this.tryPower(PowerIndex.POWER_4_EXTRA, true);
                                    if (getInExplosiveSpinMode()){
                                        tryPowerPacket(PowerIndex.POWER_4_BONUS);
                                    } else {
                                        tryPowerPacket(PowerIndex.POWER_4_EXTRA);
                                    }
                                }
                            }
                        }
                    } else {
                        Minecraft mc = Minecraft.getInstance();

                        if (!isHoldingSneak()) {
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
            }
        } else {
            if (!keyIsDown) {
                consumeClickInput = false;
            }
        }
    }

    @Override
    public void buttonInputBarrage(boolean keyIsDown, Options options){
        if (keyIsDown) {
            if (!inShootingMode()) {
                super.buttonInputBarrage(keyIsDown, options);
            } else {
                if (this.getAttackTime() >= this.getAttackTimeMax() ||
                        (this.getActivePowerPhase() != this.getActivePowerPhaseMax())) {
                    this.tryPower(PowerIndex.BARRAGE_CHARGE_2, true);
                    ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.BARRAGE_CHARGE_2);
                }
            }
        }
    }

    public int getBubbleBarrageWindup(){
        return ClientNetworking.getAppropriateConfig().chargeSettings.barrageWindup;
    }

    public float getBubbleBarrageChargePitch(){
        return 1/((float) this.getBubbleBarrageWindup() /20);
    }
    public float getSoundPitchFromByte(byte soundChoice){
        if (soundChoice == SoundIndex.ALT_CHARGE_SOUND_1){
            return this.getBubbleBarrageChargePitch();
        } else {
            return super.getSoundPitchFromByte(soundChoice);
        }
    }
    public static int maxSuperHitTime = 25;


    public int getBubbleBarrageRecoilTime(){
        return ClientNetworking.getAppropriateConfig().
                cooldownsInTicks.bubbleBarrageRecoil;
    }
    public void updateBubbleBarrage(){
        if (this.attackTimeDuring == -2 && this.getSelf() instanceof Player) {
            ((StandUser) this.self).roundabout$tryPower(PowerIndex.GUARD, true);
        } else {
            if (this.attackTimeDuring > this.getBubbleBarrageLength()) {
                this.attackTimeDuring = -20;
            } else {
                if (this.attackTimeDuring > 0) {
                    this.setAttackTime((getBubbleBarrageRecoilTime() - 1) -
                            Math.round(((float) this.attackTimeDuring / this.getBubbleBarrageLength())
                                    * (getBubbleBarrageRecoilTime() - 1)));
                    bubbleBarrageTick();

                }
            }
        }
    }


    public void bubbleBarrageTick(){
        if (!this.self.level().isClientSide()) {
            playBarrageMissNoise(this.attackTimeDuring);

            if (this.attackTimeDuring % 5 == 1){
                generateGroundBubble();
            }

            if (this.activePower == PowerIndex.BARRAGE_2 && this.attackTimeDuring == this.getBubbleBarrageLength()) {
                this.attackTimeDuring = -10;
                animateStand(StandEntity.BARRAGE_FINISHER);
            }
            findDeflectables();
        }
    }
    public int getBubbleBarrageLength(){
        return 20;
    }
    @Override
    public void renderAttackHud(GuiGraphics context, Player playerEntity,
                                int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                float flashAlpha, float otherFlashAlpha) {


        StandUser standUser = ((StandUser) playerEntity);
        StandPowers powers = standUser.roundabout$getStandPowers();
        boolean standOn = standUser.roundabout$getActive();
        int j = scaledHeight / 2 - 7 - 4;
        int k = scaledWidth / 2 - 8;
        if (standOn && this.getActivePower() == PowerIndex.BARRAGE_2 && attackTimeDuring > -1) {
            int ClashTime = 15 - Math.round(((float) attackTimeDuring / this.getBubbleBarrageLength()) * 15);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 30, ClashTime, 6);
        } else if (standOn && this.getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE) {
            int ClashTime = Math.min(15, Math.round(((float) attackTimeDuring / maxSuperHitTime) * 15));
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 30, ClashTime, 6);
        } else if (standOn && this.getActivePower() == PowerIndex.BARRAGE_CHARGE_2) {
            int ClashTime = Math.round(((float) attackTimeDuring / this.getBubbleBarrageWindup()) * 15);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 30, ClashTime, 6);
        } else {
            super.renderAttackHud(context,playerEntity,
                    scaledWidth,scaledHeight,ticks,vehicleHeartCount, flashAlpha, otherFlashAlpha);
        }
    }

    @Override
    public boolean cancelItemUse() {
        return (this.getActivePower() == PowerIndex.BARRAGE_CHARGE_2 || this.getActivePower() == PowerIndex.BARRAGE_2);
    }
    @Override
    public boolean canInterruptPower(){
        if (this.getActivePower() == PowerIndex.BARRAGE_CHARGE_2) {
            return true;
        } else {
            return super.canInterruptPower();
        }
    }
    @Override
    public boolean clickRelease(){
        return (this.getActivePower() == PowerIndex.BARRAGE_CHARGE_2 || this.getActivePower() == PowerIndex.BARRAGE_2 || this.getActivePower() == PowerIndex.POWER_2_BLOCK);
    }


    @Override
    public void buttonInput1(boolean keyIsDown, Options options) {
        if (this.getSelf().level().isClientSide) {
            if (inShootingMode()){
                if (keyIsDown) {
                    if (!hold1) {
                        if (canExecuteMoveWithLevel(getGoBeyondLevel())) {
                            if (goBeyondCharged() && getGoBeyondTarget() != null) {
                                hold1 = true;
                                this.tryIntPower(PowerIndex.SPECIAL_TRACKER, true, getGoBeyondTarget().getId());
                                tryIntPowerPacket(PowerIndex.SPECIAL_TRACKER, getGoBeyondTarget().getId());
                                this.setGoBeyondTarget(null);
                                this.setGoBeyondChargeTicks(0);
                                this.setShootTicks(0);
                            }
                        }
                    }
                } else {
                    hold1 = false;
                }
            } else if (isGuarding()) {
                if (keyIsDown) {
                    if (!hold1) {
                        if (canExecuteMoveWithLevel(getSpreadLevel())) {
                            if (!this.onCooldown(PowerIndex.SKILL_EXTRA_2)) {
                                hold1 = true;

                                this.tryPower(PowerIndex.POWER_1_BONUS, true);

                                tryPowerPacket(PowerIndex.POWER_1_BONUS);
                            }
                        }
                    }
                } else {
                    hold1 = false;
                }
            } else if (isHoldingSneak()) {
                if (keyIsDown) {
                    if (!hold1) {
                        if (canExecuteMoveWithLevel(getSpreadLevel())) {
                            if (!this.onCooldown(PowerIndex.SKILL_1_SNEAK)) {
                                if (this.activePower != PowerIndex.POWER_1_SNEAK && !canDoBubbleClusterPop()) {
                                    hold1 = true;

                                    int bubbleType = 1;
                                    ClientConfig clientConfig = ConfigManager.getClientConfig();
                                    if (clientConfig != null && clientConfig.dynamicSettings != null) {
                                        bubbleType = clientConfig.dynamicSettings.SoftAndWetCurrentlySelectedBubble;
                                    }

                                    this.tryIntPower(PowerIndex.POWER_1_SNEAK, true, bubbleType);
                                    tryIntPowerPacket(PowerIndex.POWER_1_SNEAK, bubbleType);

                                } else {
                                    if (!this.onCooldown(PowerIndex.SKILL_EXTRA_2)) {
                                        hold1 = true;
                                        this.tryPower(PowerIndex.EXTRA_2, true);
                                        tryPowerPacket(PowerIndex.EXTRA_2);
                                    }
                                }
                            } else if (this.activePower == PowerIndex.POWER_1_SNEAK || this.canDoBubbleClusterRedirect()) {
                                if (!this.onCooldown(PowerIndex.SKILL_EXTRA_2)) {
                                    hold1 = true;
                                    this.tryPower(PowerIndex.EXTRA_2, true);
                                    tryPowerPacket(PowerIndex.EXTRA_2);
                                }
                            }
                        }
                    }
                } else {
                    hold1 = false;
                }
            } else {
                if (keyIsDown) {
                    if (!hold1) {
                        hold1 = true;
                        ClientUtil.openPlunderScreen();
                    }
                } else {
                    hold1 = false;
                }
            }
        }
        super.buttonInput1(keyIsDown, options);
    }

    @Override
    public void tickStandRejection(MobEffectInstance effect){
        if (!this.getSelf().level().isClientSide()) {
            if (effect.getDuration() == 50) {
                    StandUser SE = ((StandUser) this.self);
                    if (!SE.roundabout$isLaunchBubbleEncased()) {
                        SE.roundabout$setStoredVelocity(
                                new Vec3(0, 0.1, 0)
                        );
                        if (this.self.isAlive()) {
                            SE.roundabout$setBubbleLaunchEncased();
                        }

                        if (SE instanceof Player && !this.self.level().isClientSide) {
                            ((ServerPlayer) SE).displayClientMessage(Component.translatable("text.roundabout.launch_bubble_encased"), true);
                        }
                        Vec3 storedVec = SE.roundabout$getStoredVelocity();
                        this.self.level().playSound(null, this.self.blockPosition(), ModSounds.WATER_ENCASE_EVENT, SoundSource.PLAYERS, 1F, 1);
                        MainUtil.takeLiteralUnresistableKnockbackWithY(this.self, storedVec.x, storedVec.y, storedVec.z);
                    }
            }
        }
    }
    public void creeperSpawnBubble(){
        bubbleType = PlunderTypes.SOUND.id;
        SoftAndWetPlunderBubbleEntity bubble = getPlunderBubble();
        if (!onCooldown(PowerIndex.SKILL_2)) {
            if (bubble != null) {
                this.setCooldown(PowerIndex.SKILL_2, ClientNetworking.getAppropriateConfig().cooldownsInTicks.softAndWetBasicBubbleShot);

                this.poseStand(OffsetIndex.FOLLOW);
                this.setAttackTimeDuring(-10);
                this.setActivePower(PowerIndex.POWER_2);
                bubble.setPlunderType(bubbleType);
                bubble.setSingular(true);
                shootBubbleSpeed(bubble, 0);
                bubbleListInit();
                this.bubbleList.add(bubble);
                this.getSelf().level().addFreshEntity(bubble);
                bubble.setBlockPos(this.self.blockPosition());
                bubble.setFloating();
            }
        }
    }

    int bubbleMax = 0;
    int bubbleCd = 20;
    @Override
    public void tickMobAI(LivingEntity attackTarget){

        if (this.attackTimeDuring <= -1) {
            if (this.getSelf().fallDistance > 4 && !(this.self instanceof FlyingMob) && !this.getSelf().isNoGravity()
                    && !(this.getSelf().noPhysics) && !(this.self instanceof EnderDragon) && !(this.self instanceof WitherBoss)) {
                /**Fall Brace AI*/
                if (!((StandUser) this.getSelf()).roundabout$isBubbleEncased()) {
                    if (!this.onCooldown(PowerIndex.SKILL_EXTRA)) {
                        this.self.level().playSound(null, this.self.blockPosition(), ModSounds.BIG_BUBBLE_CREATE_EVENT, SoundSource.PLAYERS, 2F, (float) (0.98 + (Math.random() * 0.04)));
                        ((StandUser) this.getSelf()).roundabout$setBubbleEncased((byte) 1);
                        this.setCooldown(PowerIndex.SKILL_EXTRA, ClientNetworking.getAppropriateConfig().cooldownsInTicks.softAndWetEncasementBubbleCreate);
                        return;
                    }
                }
            }
        }
        if (attackTarget != null && attackTarget.isAlive() && !this.isDazed(this.getSelf())) {

            double dist = attackTarget.distanceTo(this.getSelf());
            boolean isCreeper = this.getSelf() instanceof Creeper;
            if (isCreeper) {
            } else {
                boolean isBasicMob = (this.self instanceof Zombie || this.self instanceof Spider || this.self instanceof Skeleton);


                if (this.self instanceof JojoNPC || this.self instanceof Villager || this.self instanceof Raider){
                    if (Math.random() > 0.5F){
                        bubbleType = PlunderTypes.SIGHT.id;
                    } else {
                        bubbleType = PlunderTypes.FRICTION.id;
                    }
                } else {
                    if (isBasicMob){
                        bubbleType = PlunderTypes.SIGHT.id;
                    } else {
                        bubbleType = PlunderTypes.FRICTION.id;
                    }
                }
                if (bubbleMax < 10) {
                    if (dist <= 20 && (activePower == PowerIndex.NONE)) {
                        if (!this.onCooldown(PowerIndex.SKILL_2)) {
                            ((StandUser) this.getSelf()).roundabout$tryIntPower(PowerIndex.POWER_2, true, bubbleType);
                            bubbleMax++;
                            bubbleCd = 300;
                        }
                    }
                } else {
                    bubbleCd--;
                    if (bubbleCd <= 0){
                        bubbleMax = 0;
                    }
                }


                if (dist <= 6 &&  (activePower == PowerIndex.NONE || activePower == PowerIndex.ATTACK)){
                    Entity targetEntity = getTargetEntity(this.self, -1);
                    if (targetEntity != null && targetEntity.is(attackTarget)) {
                        if (this.attackTimeDuring <= -1) {
                            double RNG = Math.random();
                            if ((this.activePowerPhase < this.activePowerPhaseMax || this.attackTime >= this.attackTimeMax) &&
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

            }
            /**
             if (dist <= 8 && (hurricaneSpecial == null || hurricaneSpecial.isEmpty())) {
             ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_2_SNEAK, true);
             }
             **/
        }
    }
    public boolean hold2 = false;
    public SoftAndWetPlunderBubbleEntity getPlunderBubble(){
        SoftAndWetPlunderBubbleEntity bubble = new SoftAndWetPlunderBubbleEntity(this.self,this.self.level());
        bubble.absMoveTo(this.getSelf().getX(), this.getSelf().getY(), this.getSelf().getZ());
        bubble.setUser(this.self);
        bubble.setOwner(this.self);
        bubble.lifeSpan = ClientNetworking.getAppropriateConfig().softAndWetSettings.primaryPlunderBubbleLifespanInTicks;
        return bubble;
    }
    public SoftAndWetItemLaunchingBubbleEntity getItemLaunchingBubble(){
        SoftAndWetItemLaunchingBubbleEntity bubble = new SoftAndWetItemLaunchingBubbleEntity(this.self,this.self.level());
        bubble.absMoveTo(this.getSelf().getX(), this.getSelf().getY(), this.getSelf().getZ());
        bubble.setUser(this.self);
        bubble.setOwner(this.self);
        bubble.lifeSpan = ClientNetworking.getAppropriateConfig().softAndWetSettings.primaryPlunderBubbleLifespanInTicks;
        return bubble;
    }
    public SoftAndWetExplosiveBubbleEntity getExplosiveBubble(){
        SoftAndWetExplosiveBubbleEntity bubble = new SoftAndWetExplosiveBubbleEntity(this.self,this.self.level());
        bubble.absMoveTo(this.getSelf().getX(), this.getSelf().getY(), this.getSelf().getZ());
        bubble.setUser(this.self);
        bubble.setOwner(this.self);
        bubble.lifeSpan = ClientNetworking.getAppropriateConfig().softAndWetSettings.explosiveBubbleLifespanInTicks;
        return bubble;
    }
    public GoBeyondEntity getGoBeyondBubble(){
        GoBeyondEntity bubble = new GoBeyondEntity(this.self,this.self.level());
        bubble.absMoveTo(this.getSelf().getX(), this.getSelf().getY(), this.getSelf().getZ());
        bubble.setUser(this.self);
        bubble.setOwner(this.self);
        bubble.lifeSpan = ClientNetworking.getAppropriateConfig().softAndWetSettings.goBeyondLifespanInTicks;
        return bubble;
    }

    public boolean canShootExplosive(int useTicks){
        if ((shootTicks+useTicks) <= getMaxShootTicks()){
            return true;
        }
        return false;
    }

    public int pauseTicks(){
        return ClientNetworking.getAppropriateConfig().softAndWetSettings.heatTickDownPauseLength;
    }
    public int getPauseGrowthTicks(){
        return pauseGrowthTicks;
    }
    public boolean confirmShot(int useTicks){
        if (canShootExplosive(useTicks)){
            pauseGrowthTicks = pauseTicks();
            setGoBeyondChargeTicks(goBeyondChargeTicks+getGoBeyondUseTicks());
            setShootTicks((shootTicks+useTicks));
            return true;
        }
        return false;
    }

    public int shootTicks = 0;
    public int getShootTicks(){
        return shootTicks;
    }
    public void setShootTicks(int shootTicks){
        this.shootTicks = Mth.clamp(shootTicks,0,getMaxShootTicks());

    }
    public int goBeyondChargeTicks = 0;
    public boolean inExplosiveSpinMode = false;
    public boolean getInExplosiveSpinMode(){
        return inExplosiveSpinMode;
    }
    public void setInExplosiveSpinMode(boolean mode){
        this.inExplosiveSpinMode = mode;
    }
    public int getGoBeyondCharge(){
        return goBeyondChargeTicks;
    }
    public void setGoBeyondChargeTicks(int goBeyondChargeTicks){
        this.goBeyondChargeTicks = Mth.clamp(goBeyondChargeTicks,0,getMaxGoBeyondChargeTicks());

        if (getInExplosiveSpinMode() && getGoBeyondCharge() == 0){
            setInExplosiveSpinMode(false);
        }
        if (!getInExplosiveSpinMode() && getGoBeyondCharge() >= getMaxGoBeyondChargeTicks()){
            if (this.self.level().isClientSide()){
                this.self.playSound(ModSounds.EXPLOSIVE_SPIN_MODE_EVENT, 1F, 1.0F);
            }
            setInExplosiveSpinMode(true);
        }
    }

    /**Similar to Justice selecting of mobs**/
    @Override
    public void updateGoBeyondTarget(){
        if (canExecuteMoveWithLevel(getGoBeyondLevel())) {
            if (inShootingMode() && goBeyondCharged()) {
                Entity TE = MainUtil.getTargetEntity(this.self, 30, 15);
                if (TE != null && !TE.is(this.self) && !(TE instanceof StandEntity && !TE.isAttackable()) && MainUtil.isActuallyALivingEntityNoCap(TE)) {
                    this.setGoBeyondTarget(TE);
                }
            } else {
                this.setGoBeyondTarget(null);
            }
        } else {
            this.setGoBeyondTarget(null);
        }
    }
    public int getMaxShootTicks(){
        return 10000;
    }
    public int getMaxGoBeyondChargeTicks(){
        return 10000;
    }

    public float getExplosiveSpeed(){
        if (getInExplosiveSpinMode()){
            return (float) (0.8F*(ClientNetworking.getAppropriateConfig().
                                softAndWetSettings.explosiveBubbleShootSpeedMultiplier*0.01));
        }
        return (float) (0.54F*(ClientNetworking.getAppropriateConfig().
                softAndWetSettings.explosiveBubbleShootSpeedMultiplier*0.01));
    }

    public boolean inShootingMode(){
        return getStandUserSelf().roundabout$getCombatMode();
    }
    public boolean shootExplosiveBubble(){
        this.setCooldown(PowerIndex.SKILL_4, 3);
        SoftAndWetExplosiveBubbleEntity bubble = getExplosiveBubble();

        if (bubble != null){

            this.poseStand(OffsetIndex.FOLLOW);
            this.setAttackTimeDuring(-10);
            this.setActivePower(PowerIndex.POWER_4_EXTRA);
            shootExplosiveBubbleSpeed(bubble,getExplosiveSpeed());
            bubbleListInit();
            this.bubbleList.add(bubble);
            this.getSelf().level().addFreshEntity(bubble);

                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.EXPLOSIVE_BUBBLE_SHOT_EVENT, SoundSource.PLAYERS, 2F, (float) (0.98 + (Math.random() * 0.04)));

        }
        return true;
    }
    public boolean useWaterShield(){
        if (this.self instanceof Player PL && !PL.level().isClientSide()) {
            ItemStack stack = this.getSelf().getMainHandItem();
            if ((!stack.isEmpty() && stack.getItem() instanceof PotionItem PI && PotionUtils.getPotion(stack) == Potions.WATER)) {
                if (!PL.getAbilities().instabuild) {
                    stack.shrink(1);
                    PL.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
                }
                splashWaterShield();
                return true;
            }
            ItemStack stack2 = this.getSelf().getOffhandItem();
            if ((!stack2.isEmpty() && stack2.getItem() instanceof PotionItem PI2 && PotionUtils.getPotion(stack2) == Potions.WATER)) {
                if (!PL.getAbilities().instabuild) {
                    stack2.shrink(1);
                    PL.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
                }
                splashWaterShield();
            }
        }

        this.setCooldown(PowerIndex.SKILL_4_SNEAK, ClientNetworking.getAppropriateConfig().cooldownsInTicks.softAndWetWaterShieldCD);

        return true;
    }
    public void splashWaterShield(){
        float width = self.getBbWidth()*0.5F;
        float height = self.getBbHeight()*0.5F;
        if (((StandUser) self).roundabout$isOnStandFire()){
            ((StandUser) self).roundabout$setRemainingStandFireTicks(0);
        }
        ((StandUser) self).roundabout$setGasolineTime(-1);
        self.extinguishFire();
        this.self.level().playSound(null, this.self.blockPosition(), ModSounds.WATER_ENCASE_EVENT, SoundSource.PLAYERS, 1F, (float) (1.5 + (Math.random() * 0.04)));
        ((ServerLevel) this.self.level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK,
                        Blocks.WATER.defaultBlockState()),
                this.self.getX(),
                this.self.getY() +(this.self.getBbHeight()*0.5),
                this.self.getZ(),
                120,width, height, width, 0.4);
        this.setWaterShieldTicks(ClientNetworking.getAppropriateConfig().softAndWetSettings.waterShieldDurationInTicks);
    }
    public boolean switchModes(){
        if (getStandUserSelf().roundabout$getCombatMode()){
            getStandUserSelf().roundabout$setCombatMode(false);
            if (this.self.level().isClientSide()){
                this.self.playSound(ModSounds.EXPLOSIVE_BUBBLE_SWITCH_OFF_EVENT, 1F, 1.0F);
            }
        } else {
            getStandUserSelf().roundabout$setCombatMode(true);
            if (this.self.level().isClientSide()){
                this.self.playSound(ModSounds.EXPLOSIVE_BUBBLE_SWITCH_EVENT, 1F, 1.0F);
            }
        }

        return true;
    }
    public boolean goBeyond(){
            //Vec3 vector = this.self.

        if (!this.self.level().isClientSide()){
            if (goBeyondActiveTarget != null) {
                GoBeyondEntity bubble = getGoBeyondBubble();
                if (bubble != null) {
                    this.setCooldown(PowerIndex.SKILL_2, 20);

                    this.poseStand(OffsetIndex.FOLLOW);
                    this.setAttackTimeDuring(-10);
                    this.setActivePower(PowerIndex.POWER_2);
                    bubble.setChasing(goBeyondActiveTarget);
                    shootBubbleSpeed(bubble, 0.165F);
                    bubbleListInit();
                    this.bubbleList.add(bubble);
                    this.getSelf().level().addFreshEntity(bubble);

                    if (bubbleType != PlunderTypes.SOUND.id) {
                        this.self.level().playSound(bubble, bubble.blockPosition(), ModSounds.GO_BEYOND_LAUNCH_EVENT, SoundSource.PLAYERS, 2F, (float) (0.98 + (Math.random() * 0.04)));
                    }

                    Vec3 vector = Vec3.directionFromRotation(new Vec2(-52, this.self.yBodyRot - 90));

                    for (int i = 0; i < 10; ++i) {
                        double randomX = (Math.random() * 0.5) - 0.25;
                        double randomY = (Math.random() * 0.5) - 0.25;
                        double randomZ = (Math.random() * 0.5) - 0.25;
                        Vec3 xvec = vector.add(randomX, randomY, randomZ);
                        byte sk = ((StandUser)this.getSelf()).roundabout$getStandSkin();
                        if (sk == SoftAndWetEntity.KIRA) {
                            ((ServerLevel) this.getSelf().level()).sendParticles(ModParticles.HEART_ATTACK_MINI,
                                    this.getSelf().getX(), this.getSelf().getY() + this.self.getEyeHeight() * 0.7F, this.getSelf().getZ(),
                                    0, xvec.x, xvec.y, xvec.z, 0.12);
                        } else {
                            ((ServerLevel) this.getSelf().level()).sendParticles(ModParticles.PURPLE_STAR,
                                    this.getSelf().getX(), this.getSelf().getY() + this.self.getEyeHeight() * 0.7F, this.getSelf().getZ(),
                                    0, xvec.x, xvec.y, xvec.z, 0.12);
                        }
                    }
                }
            }
        }
        return true;
    }

    public int grabInventorySlot=1;
    /**Explosive Item Bubble Shooting*/
    public boolean itemBubbleShot() {
        ItemStack stack = ((Player) this.getSelf()).getInventory().getItem(this.grabInventorySlot);
        if (!stack.isEmpty() && !(stack.getItem() instanceof BlockItem &&
                ((BlockItem) stack.getItem()).getBlock() instanceof ShulkerBoxBlock)) {
            this.setCooldown(PowerIndex.SKILL_2, ClientNetworking.getAppropriateConfig().cooldownsInTicks.softAndWetItemBubbleShot);
            if (!this.self.level().isClientSide()) {

                SoftAndWetItemLaunchingBubbleEntity bubble = getItemLaunchingBubble();

                if (bubble != null) {

                    bubble.setHeldItem(stack.copyWithCount(1));
                    stack.shrink(1);
                    this.poseStand(OffsetIndex.FOLLOW);
                    this.setAttackTimeDuring(-10);
                    this.setActivePower(PowerIndex.POWER_2_BONUS);
                    addEXP(1);
                    shootExplosiveItemBubbleSpeed(bubble, getExplosiveItemBubbleSpeed());
                    bubbleListInit();
                    this.bubbleList.add(bubble);
                    this.getSelf().level().addFreshEntity(bubble);
                    this.self.level().playSound(null, this.self.blockPosition(), ModSounds.EXPLOSIVE_BUBBLE_SHOT_EVENT, SoundSource.PLAYERS, 0.7F, (float) (1.2 + (Math.random() * 0.04)));
                }
            }
        }
        return true;
    }
    public boolean bubbleShot(){
        SoftAndWetPlunderBubbleEntity bubble = getPlunderBubble();

        if (bubble != null){
            this.setCooldown(PowerIndex.SKILL_2, ClientNetworking.getAppropriateConfig().cooldownsInTicks.softAndWetBasicBubbleShot);

            this.poseStand(OffsetIndex.FOLLOW);
            this.setAttackTimeDuring(-10);
            this.setActivePower(PowerIndex.POWER_2);
            bubble.setPlunderType(bubbleType);
            bubble.setSingular(true);
            shootBubbleSpeed(bubble,getBubbleSpeed());
            bubbleListInit();
            this.bubbleList.add(bubble);
            this.getSelf().level().addFreshEntity(bubble);

            if (bubbleType != PlunderTypes.SOUND.id) {
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.BUBBLE_CREATE_EVENT, SoundSource.PLAYERS, 2F, (float) (0.98 + (Math.random() * 0.04)));
            }
        }
        return true;
    }
    public boolean canDoBubblePop(){
        bubbleListInit();

        List<SoftAndWetBubbleEntity> bubbleList2 = new ArrayList<>(bubbleList) {
        };
        if (!bubbleList2.isEmpty()) {
            int totalnumber = bubbleList2.size();
            for (SoftAndWetBubbleEntity value : bubbleList2) {
                if (!(value instanceof SoftAndWetPlunderBubbleEntity PBE && PBE.isPopPlunderBubbble())) {
                    return true;
                }
            }
        }
        return !bubbleList.isEmpty();
    }
    public boolean canDoBubbleClusterRedirect(){
        bubbleListInit();

        List<SoftAndWetBubbleEntity> bubbleList2 = new ArrayList<>(bubbleList) {
        };
        if (!bubbleList2.isEmpty()) {
            int totalnumber = bubbleList2.size();
            for (SoftAndWetBubbleEntity value : bubbleList2) {
                if (value instanceof SoftAndWetPlunderBubbleEntity PBE) {
                    if (!PBE.getSingular() && !PBE.getActivated() && !PBE.getFinished()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public boolean canDoBubbleClusterPop(){
        bubbleListInit();

        if (this.activePower == PowerIndex.POWER_1_SNEAK){
            return true;
        }

        List<SoftAndWetBubbleEntity> bubbleList2 = new ArrayList<>(bubbleList) {
        };
        if (!bubbleList2.isEmpty()) {
            int totalnumber = bubbleList2.size();
            for (SoftAndWetBubbleEntity value : bubbleList2) {
                if (value instanceof SoftAndWetPlunderBubbleEntity PBE && !PBE.isPopPlunderBubbble()) {
                    if (!PBE.getSingular() && !PBE.getFinished()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public boolean canDoBubbleRedirect(){

        bubbleListInit();

        List<SoftAndWetBubbleEntity> bubbleList2 = new ArrayList<>(bubbleList) {
        };
        if (!bubbleList2.isEmpty()) {
            int totalnumber = bubbleList2.size();
            for (SoftAndWetBubbleEntity value : bubbleList2) {
                if (value.getActivated() && !(value instanceof SoftAndWetPlunderBubbleEntity PBE && (PBE.getPlunderType()==PlunderTypes.SIGHT.id ||
                        PBE.getPlunderType()==PlunderTypes.FRICTION.id))) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean clusterBubblePop() {
        bubbleListInit();
        if (!bubbleList.isEmpty()) {
            this.setCooldown(PowerIndex.SKILL_2_SNEAK, 10);
            if (!this.self.level().isClientSide()) {
                List<SoftAndWetBubbleEntity> bubbleList2 = new ArrayList<>(bubbleList) {
                };
                if (!bubbleList2.isEmpty()) {
                    for (SoftAndWetBubbleEntity value : bubbleList2) {
                        if (value instanceof SoftAndWetPlunderBubbleEntity plunder && !plunder.isPopPlunderBubbble()){
                            if (!plunder.getFinished() && !plunder.getSingular()){
                                if (plunder.getPlunderType() == PlunderTypes.ITEM.id){
                                    plunder.forceDropItem();
                                }
                                plunder.popBubble();
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    public boolean bubblePop() {
        bubbleListInit();
        if (!this.self.level().isClientSide()) {
            bubbleNumber++;
        }
        if (!bubbleList.isEmpty()) {
            this.setCooldown(PowerIndex.SKILL_2_SNEAK, 20);
            if (!this.self.level().isClientSide()) {
                List<SoftAndWetBubbleEntity> bubbleList2 = new ArrayList<>(bubbleList) {
                };
                if (!bubbleList2.isEmpty()) {
                    for (SoftAndWetBubbleEntity value : bubbleList2) {
                        if (value instanceof SoftAndWetPlunderBubbleEntity plunder && !plunder.isPopPlunderBubbble()){
                            if (!plunder.getFinished()){
                                if (plunder.getPlunderType() == PlunderTypes.ITEM.id){
                                    plunder.forceDropItem();
                                }
                               plunder.popBubble();
                            }
                        } else if (value instanceof SoftAndWetExplosiveBubbleEntity SBE){
                            SBE.popWithForce();
                        } else if (value instanceof SoftAndWetItemLaunchingBubbleEntity GBE){
                            GBE.popWithForce(savedPos);
                        }
                    }
                }
            }
        }
        return false;
    }

    /**If there are any cluster bubbles that have not yet entered the plunder state, redirect them*/
    public boolean bubbleClusterRedirect(){
        bubbleListInit();
        List<SoftAndWetBubbleEntity> bubbleList2 = new ArrayList<>(bubbleList) {
        };
        if (!bubbleList2.isEmpty()) {
            int totalnumber = bubbleList2.size();
            for (SoftAndWetBubbleEntity value : bubbleList2) {
                if (value instanceof SoftAndWetPlunderBubbleEntity PBE) {
                    if (!PBE.getSingular() && !PBE.getActivated() && !PBE.getFinished()) {
                        shootBubbleSpeed2(PBE,PBE.getSped()*0.65F);
                        PBE.setLaunched(true);
                    }
                }
            }
        }
        return false;
    }

    /**Redirects all bubbles that have entered the plunder state and are compatible*/
    public boolean bubbleRedirect(){
        bubbleListInit();
        if (!bubbleList.isEmpty()){

            if (canDoBubbleRedirect()) {
                this.setCooldown(PowerIndex.SKILL_EXTRA_2, 3);

                if (!this.self.level().isClientSide()) {
                    if (savedPos != null) {
                        List<SoftAndWetBubbleEntity> bubbleList3 = new ArrayList<>(bubbleList) {
                        };
                        if (!bubbleList3.isEmpty()) {
                            int totalnumber = bubbleList3.size();
                            for (SoftAndWetBubbleEntity value : bubbleList3) {
                                if (value.getActivated() && !(value instanceof SoftAndWetPlunderBubbleEntity PBE && (PBE.getPlunderType() == PlunderTypes.SIGHT.id ||
                                        PBE.getPlunderType() == PlunderTypes.FRICTION.id))) {
                                    Vec3 vector = new Vec3((savedPos.x() - value.getX()),
                                            (savedPos.y() - value.getY()),
                                            (savedPos.z() - value.getZ())).normalize().scale(value.getSped());
                                    if (totalnumber > 1) {
                                        vector = new Vec3(
                                                vector.x() + (((Math.random() - 0.5) * totalnumber) * value.getSped() * 0.03),
                                                vector.y(),
                                                vector.z() + (((Math.random() - 0.5) * totalnumber) * value.getSped() * 0.03)
                                        ).normalize().scale(value.getSped());
                                    }
                                    value.setDeltaMovement(vector);
                                    value.hurtMarked = true;
                                    value.hasImpulse = true;
                                    if (!value.getLaunched()) {
                                        value.setLaunched(true);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if (savedPos != null){
                        this.self.playSound(ModSounds.BUBBLE_HOVERED_OVER_EVENT, 0.2F, (float) (0.95F+Math.random()*0.1F));
                        this.self.level()
                                .addParticle(
                                        ModParticles.POINTER_SOFT,
                                        savedPos.x(),
                                        savedPos.y() + 0.5,
                                        savedPos.z(),
                                        0,
                                        0,
                                        0
                                );
                    }
                }
            }
            /**
            if (bubbleType != PlunderTypes.SOUND.id) {
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.BUBBLE_CREATE_EVENT, SoundSource.PLAYERS, 2F, (float) (0.98 + (Math.random() * 0.04)));
            }
             **/
        }
        return true;
    }

    @Override
    public boolean hasShootingModeVisually(){
        return true;
    }
    public float getBubbleSpeed(){
        if (bubbleType == PlunderTypes.OXYGEN.id){
            return 0.6F;
        } else if (bubbleType == PlunderTypes.ITEM.id){
            return 0.5F;
        } else if (bubbleType == PlunderTypes.POTION_EFFECTS.id){
            return 0.25F;
        } else if (bubbleType == PlunderTypes.SOUND.id){
            return 0.3F;
        }else if (bubbleType == PlunderTypes.MOISTURE.id){
            return 0.3F;
        }
        return 0.17F;
    }
    public float getExplosiveItemBubbleSpeed(){
        return 0.25F;
    }

    public void generateGroundBubble(){
        GroundBubbleEntity groundent = new GroundBubbleEntity(this.getSelf().level(), this.self);
        groundent.bubbleNo = bubbleNumber;
        Vec3 pos = MainUtil.getRaytracePointOnMobOrBlock(this.self,2.5F);
        groundent.setPos(pos);
        groundent.setLifeSpan(120);
        float rando = (float) (Math.random()* 0.05F)-0.025F;
        groundent.setSpeed(groundent.getSpeed()+rando);
        this.getSelf().level().addFreshEntity(groundent);
    }


    public void shootBubble(SoftAndWetBubbleEntity ankh){
        shootBubbleSpeed(ankh, 1.01F);
    }
    public void shootExplosiveBubbleSpeed(SoftAndWetBubbleEntity ankh, float speed){
        ankh.setSped(speed);
        Vec3 pos = this.self.getPosition(1).add(0,this.self.getEyeHeight()*0.8F,0).add(this.self.getForward().scale(this.self.getBbWidth()*1));
        ankh.setPos(pos.x(), pos.y(), pos.z());
        ankh.shootFromRotationDeltaAgnostic(this.getSelf(), this.getSelf().getXRot(), this.getSelf().getYRot(), 1.0F, speed, 0);
    }
    public void shootExplosiveItemBubbleSpeed(SoftAndWetBubbleEntity ankh, float speed){
        ankh.setSped(speed);
        Vec3 pos = this.self.getPosition(1).add(0,this.self.getEyeHeight()*0.8F,0).add(this.self.getForward().scale(this.self.getBbWidth()*1));
        ankh.setPos(pos.x(), pos.y(), pos.z());
        ankh.shootFromRotationDeltaAgnostic(this.getSelf(), this.getSelf().getXRot(), this.getSelf().getYRot(), 1.0F, speed, 0);
    }
    public void shootBubbleSpeed(SoftAndWetBubbleEntity ankh, float speed){
        ankh.setSped(speed);
        ankh.setPos(this.self.getX(), this.self.getY()+(this.self.getEyeHeight()*0.71), this.self.getZ());
        ankh.shootFromRotationDeltaAgnostic(this.getSelf(), this.getSelf().getXRot(), this.getSelf().getYRot(), 1.0F, speed, 0);
    }
    public void shootBubbleSpeed2(SoftAndWetBubbleEntity ankh, float speed){
        ankh.shootFromRotationDeltaAgnostic3(this.getSelf(), this.getSelf().getXRot(), this.getSelf().getYRot(), 1.0F, speed);
    }
    public void shootBubbleRandomly(SoftAndWetBubbleEntity ankh, float speed){
        ankh.setSped(speed);
        ankh.setPos(this.self.getX(), this.self.getY()+(this.self.getEyeHeight()*0.2), this.self.getZ());
        ankh.shootFromRotationDeltaAgnostic(this.getSelf(),-1*(float)(Math.random()*50), (float)(Math.random()*360), 1.0F, 0.25F, 0);
    }

    public boolean setPowerBubbleBarrage() {
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.BARRAGE_2);
        this.poseStand(OffsetIndex.ATTACK);
        this.setAttackTimeMax(this.getBubbleBarrageRecoilTime());
        this.setActivePowerPhase(this.getActivePowerPhaseMax());
        animateStand(StandEntity.BARRAGE);
        return true;
    }
    @Override
    public boolean setPowerOther(int move, int lastMove) {
        if (move == PowerIndex.POWER_2) {
            return this.bubbleShot();
        } else if (move == PowerIndex.POWER_2_BONUS) {
            return this.itemBubbleShot();
        } else if (move == PowerIndex.BARRAGE_CHARGE_2) {
            return this.setPowerBubbleBarrageCharge();
        } else if (move == PowerIndex.POWER_2_EXTRA) {
            return this.bubbleRedirect();
        } else if (move == PowerIndex.POWER_2_SNEAK) {
            return this.bubblePop();
        } else if (move == PowerIndex.BARRAGE_2) {
            return this.setPowerBubbleBarrage();
        } else if (move == PowerIndex.EXTRA){
            return this.fallBraceInit();
        } else if (move == PowerIndex.FALL_BRACE_FINISH){
            return this.fallBrace();
        } else if (move == PowerIndex.VAULT){
            return this.vault();
        } else if (move == PowerIndex.POWER_4){
            return this.switchModes();
        } else if (move == PowerIndex.POWER_4_SNEAK){
            return this.useWaterShield();
        } else if (move == PowerIndex.POWER_4_EXTRA){
            if (!this.self.level().isClientSide()) {
                this.setInExplosiveSpinMode(false);
            }
            return this.shootExplosiveBubble();
        } else if (move == PowerIndex.POWER_4_BONUS){
            if (!this.self.level().isClientSide()) {
                this.setInExplosiveSpinMode(true);
            }
            return this.shootExplosiveBubble();
        } else if (move == PowerIndex.POWER_3){
            return this.bubbleLadder();
        } else if (move == PowerIndex.POWER_3_EXTRA){
            return this.bubbleLadderPlace();
        } else if (move == PowerIndex.POWER_3_BONUS){
            return this.bigEncasementBubbleCreate();
        } else if (move == PowerIndex.POWER_1_SNEAK) {
            return this.bubbleClusterStart();
        } else if (move == PowerIndex.POWER_1) {
            return this.spawnRandomBubble();
        } else if (move == PowerIndex.POWER_1_BONUS) {
            return this.bubbleClusterRedirect();
        } else if (move == PowerIndex.SNEAK_ATTACK_CHARGE){
            return this.setPowerKickAttack();
        } else if (move == PowerIndex.SNEAK_ATTACK){
            return this.setPowerSuperHit();
        } else if (move == PowerIndex.EXTRA_2) {
            return this.clusterBubblePop();
        } else if (move == PowerIndex.SPECIAL_TRACKER){
            return this.goBeyond();
        }
        return super.setPowerOther(move,lastMove);
    }
    public int chargedFinal;
    public boolean setPowerSuperHit() {
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.SNEAK_ATTACK);
        this.poseStand(OffsetIndex.ATTACK);
        chargedFinal = Math.min(this.chargedFinal,maxSuperHitTime);
        if (chargedFinal >= maxSuperHitTime){
            this.animateStand(SoftAndWetEntity.ENCASEMENT_STRIKE);
        } else {
            this.animateStand(SoftAndWetEntity.KICK);
        }
        //playBarrageCrySound();
        return true;
    }
    public boolean setPowerKickAttack() {
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.SNEAK_ATTACK_CHARGE);
        this.animateStand(SoftAndWetEntity.KICK_CHARGE);
        this.poseStand(OffsetIndex.GUARD);
        return true;
    }

    public static final byte BARRAGE_NOISE = 20;
    public static final byte BARRAGE_NOISE_2 = BARRAGE_NOISE+1;
    public static final byte BARRAGE_NOISE_3 = BARRAGE_NOISE+2;
    @Override
    public byte chooseBarrageSound(){
        byte skn = ((StandUser)this.getSelf()).roundabout$getStandSkin();
        if (skn == SoftAndWetEntity.KIRA){
            return 0;
        } else {
            double rand = Math.random();
            if (rand > 0.5) {
                return BARRAGE_NOISE;
            } else {
                return BARRAGE_NOISE_2;
            }
        }
    }

    Entity goBeyondActiveTarget = null;
    @Override
    public boolean tryIntPower(int move, boolean forced, int chargeTime){
        if (move == PowerIndex.POWER_2 || move == PowerIndex.POWER_1_SNEAK) {
            bubbleType = (byte)chargeTime;
        } else if (move == PowerIndex.SNEAK_ATTACK) {
            this.chargedFinal = chargeTime;
        } else if (move == PowerIndex.SPECIAL_TRACKER){
            goBeyondActiveTarget = this.self.level().getEntity(chargeTime);
        } else if (move == PowerIndex.POWER_2_BONUS){
            this.grabInventorySlot = chargeTime;
        }
        return super.tryIntPower(move, forced, chargeTime);
    }
    @Override
    public boolean cancelSprintJump(){
       if (this.getActivePower() == PowerIndex.POWER_1_SNEAK){
            return true;
       } else if (this.getActivePower() == PowerIndex.BARRAGE_CHARGE_2 || this.getActivePower() == PowerIndex.BARRAGE_2
                || this.getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE){
            return true;
       }
        return super.cancelSprintJump();
    }

public void unlockSkin(){
    Level lv = this.getSelf().level();
    if ((this.getSelf()) instanceof Player PE){
        StandUser user = ((StandUser)PE);
        ItemStack stack = user.roundabout$getStandDisc();
        if (!stack.isEmpty() && stack.is(ModItems.STAND_DISC_SOFT_AND_WET)){
            IPlayerEntity ipe = ((IPlayerEntity) PE);
            if (!ipe.roundabout$getUnlockedBonusSkin()){
                if (!lv.isClientSide()) {
                    ipe.roundabout$setUnlockedBonusSkin(true);
                    lv.playSound(null, PE.getX(), PE.getY(),
                            PE.getZ(), ModSounds.UNLOCK_SKIN_EVENT, PE.getSoundSource(), 2.0F, 1.0F);
                    ((ServerLevel) lv).sendParticles(ModParticles.HEART_ATTACK_MINI, PE.getX(),
                            PE.getY()+PE.getEyeHeight(), PE.getZ(),
                            10, 0.5, 0.5, 0.5, 0.2);
                    user.roundabout$setStandSkin(SoftAndWetEntity.KIRA);
                    user.roundabout$summonStand(this.getSelf().level(), true, false);
                    ((ServerPlayer) ipe).displayClientMessage(
                            Component.translatable("unlock_skin.roundabout.soft_and_wet.kira"), true);
                }
            }
        }
    }
}
    public void playBubbleBarrageChargeSound(){
        if (!this.self.level().isClientSide()) {
            SoundEvent barrageChargeSound = this.getBarrageChargeSound();
            if (barrageChargeSound != null) {
                playSoundsIfNearby(SoundIndex.ALT_CHARGE_SOUND_1, 27, false);
            }
        }
    }
    public boolean setPowerBubbleBarrageCharge() {
        animateStand(StandEntity.BARRAGE_CHARGE);
        this.attackTimeDuring = 0;
        playBubbleBarrageChargeSound();
        this.setActivePower(PowerIndex.BARRAGE_CHARGE_2);
        this.poseStand(OffsetIndex.ATTACK);
        return true;
    }
    @Override
    public float inputSpeedModifiers(float basis){
        if (this.activePower == PowerIndex.BARRAGE_CHARGE_2) {
            basis*=0.5f;
        } else if (this.activePower == PowerIndex.POWER_1_SNEAK){
            basis *= 0.2f;
        } else if (this.activePower == PowerIndex.SNEAK_ATTACK_CHARGE){
            if (this.getSelf().isCrouching()) {
                float f = Mth.clamp(0.3F + EnchantmentHelper.getSneakingSpeedBonus(this.getSelf()), 0.0F, 1.0F);
                float g = 1 / f;
                basis *= g;
            }
            basis *= 0.6f;
        }
        return super.inputSpeedModifiers(basis);
    }

    public boolean bubbleClusterStart(){
        if (!this.self.level().isClientSide()) {
            clusterBubblePop();
        }
        setActivePower(PowerIndex.POWER_1_SNEAK);
        this.poseStand(OffsetIndex.FOLLOW);
        this.attackTimeDuring = 0;
        animateStand(StandEntity.IDLE);
        return true;
    }
    @Override
    public int getMaxGuardPoints(){
        return ClientNetworking.getAppropriateConfig().guardPoints.softAndWetDefend;
    }
    @Override
    public boolean tryBlockPosPower(int move, boolean forced, BlockPos blockPos) {
        return tryPower(move, forced);
    }
    @Override
    public boolean tryPosPower(int move, boolean forced, Vec3 pos) {
        savedPos = pos;
        return tryPower(move, forced);
    }
    public int bubbleNumber = 0;

    @Override
    public boolean tryPower(int move, boolean forced) {
        if (!this.self.level().isClientSide &&
                (this.getActivePower() == PowerIndex.BARRAGE_CHARGE_2 || this.getActivePower() == PowerIndex.BARRAGE_2)
                && (move != PowerIndex.BARRAGE_2 && move != PowerIndex.BARRAGE_CHARGE_2 && move != PowerIndex.GUARD)){
            this.stopSoundsIfNearby(SoundIndex.BARRAGE_SOUND_GROUP, 100,false);
        }
        return super.tryPower(move,forced);
    }
    public boolean bigEncasementBubbleCreate() {
        this.setCooldown(PowerIndex.SKILL_EXTRA, ClientNetworking.getAppropriateConfig().cooldownsInTicks.softAndWetEncasementBubbleCreate);
        if (!this.self.level().isClientSide()) {
            EncasementBubbleEntity encasement = ModEntities.ENCASEMENT_BUBBLE.create(this.getSelf().level());
            if (encasement != null){

                encasement.bubbleNo = bubbleNumber;
                Vec3 movevec = this.self.getPosition(0).add(0,(this.self.getEyeHeight()*0.65F),0).add(this.self.getForward().normalize().scale(0.72));
                encasement.absMoveTo(movevec.x(), movevec.y(), movevec.z());
                encasement.setUser(this.self);
                encasement.lifeSpan = ClientNetworking.getAppropriateConfig().softAndWetSettings.encasementBubbleFloatingLifespanInTicks;
                this.getSelf().level().addFreshEntity(encasement);
                addEXP(1);

                //((StandUser)this.self).roundabout$setAdjustedGravity(30);
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.BIG_BUBBLE_CREATE_EVENT, SoundSource.PLAYERS, 2F, (float) (0.98 + (Math.random() * 0.04)));
            }
        }
        return false;
    }

    /**Bubble Scaffolding, build a ladder overtime somewhat like the Builder blocks in Twilight*/
    public boolean bubbleLadderPlace(){
        if (!this.self.level().isClientSide()){

            if (this.self.getXRot() > 35){
                buildingBubbleScaffoldPos = buildingBubbleScaffoldPos.below();
            } else if (this.self.getXRot() < -35){
                buildingBubbleScaffoldPos = buildingBubbleScaffoldPos.above();
            } else {
                buildingBubbleScaffoldPos = buildingBubbleScaffoldPos.relative(Direction.fromYRot(this.self.getYHeadRot()));
            }
            if (MainUtil.tryPlaceBlock(this.self,buildingBubbleScaffoldPos,false)){
                boolean heartAttackState = ((StandUser)this.getSelf()).roundabout$getStandSkin() == SoftAndWetEntity.KIRA;
                this.self.level().setBlockAndUpdate(buildingBubbleScaffoldPos, ModBlocks.BUBBLE_SCAFFOLD.defaultBlockState().setValue(BlockStateProperties.TRIGGERED,heartAttackState));
                if (this.self.level().getBlockEntity(buildingBubbleScaffoldPos) instanceof BubbleScaffoldBlockEntity SBE) {
                    SBE.standuser = this.self;
                    SBE.bubbleNo = bubbleNumber;
                    this.self.level().playSound(null, this.self.blockPosition(), ModSounds.BUBBLE_CREATE_EVENT,
                            SoundSource.PLAYERS, 2F, (float) (0.9 + (Math.random() * 0.2)));
                    this.self.level().playSound(null, this.self.blockPosition(), ModSounds.BUBBLE_CREATE_EVENT,
                            SoundSource.PLAYERS, 2F, (float) (0.9 + (Math.random() * 0.2)));
                    this.self.level().playSound(null, this.self.blockPosition(), ModSounds.BUBBLE_CREATE_EVENT,
                            SoundSource.PLAYERS, 2F, (float) (0.9 + (Math.random() * 0.2)));
                    this.self.level().playSound(null, this.self.blockPosition(), ModSounds.BUBBLE_CREATE_EVENT,
                            SoundSource.PLAYERS, 2F, (float) (0.9 + (Math.random() * 0.2)));
                }
            }
        }
        return false;
    }
    public boolean bubbleLadder(){
        setActivePower(PowerIndex.POWER_3);
        this.poseStand(OffsetIndex.GUARD_FURTHER_RIGHT);
        addEXP(1);
        this.attackTimeDuring = 0;
        bubbleScaffoldCount = 0;
        animateStand((byte) StandEntity.FIRST_PUNCH);
        buildingBubbleScaffoldPos = this.self.blockPosition();
        return true;
    }
    @Override
    public void updateUniqueMoves() {
        /*Tick through Time Stop Charge*/
        if (this.getActivePower() == PowerIndex.POWER_1_SNEAK) {
            this.updateBubbleCluster();
        } else if (this.getActivePower() == PowerIndex.BARRAGE_2) {
            updateBubbleBarrage();
        } else if (this.getActivePower() == PowerIndex.BARRAGE_CHARGE_2) {
            updateBubbleBarrageCharge();
        } else if (this.getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE){
            updateKickAttackCharge();
        } else if (this.getActivePower() == PowerIndex.SNEAK_ATTACK){
            updateKickAttack();
        } else if (this.getActivePower() == PowerIndex.POWER_3){
            this.updateBubbleScaffold();
        }
        super.updateUniqueMoves();
    }

    public void updateBubbleBarrageCharge(){
        if (this.attackTimeDuring >= this.getBubbleBarrageWindup()) {
            ((StandUser) this.self).roundabout$tryPower(PowerIndex.BARRAGE_2, true);
        }
    }
    public void updateKickAttack(){
        if (this.attackTimeDuring > -1) {
            if (this.attackTimeDuring == 5) {
                this.encasementKick();
            }
        }
    }

    @Override
    public float multiplyPowerByStandConfigPlayers(float power){
        return (float) (power*(ClientNetworking.getAppropriateConfig().
                        damageMultipliers.softAndWetAttacksOnPlayers*0.01));
    }

    @Override
    public float multiplyPowerByStandConfigMobs(float power){
        return (float) (power*(ClientNetworking.getAppropriateConfig().
                damageMultipliers.softAndWetAttacksOnMobs*0.01));
    }

    public float multiplyPowerByStandConfigShooting(float power){
        return (float) (power*(ClientNetworking.getAppropriateConfig().
                damageMultipliers.softAndWetShootingModePower*0.01));
    }
    public float multiplyPowerByStandConfigGoBeyond(float power){
        return (float) (power*(ClientNetworking.getAppropriateConfig().
                damageMultipliers.softAndWetGoBeyondPower*0.01));
    }
    @Override
    public float getPunchStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod(multiplyPowerByStandConfigPlayers(1.65F));
        } else {
            return levelupDamageMod(multiplyPowerByStandConfigMobs(4.7F));
        }
    }

    @Override
    public float getHeavyPunchStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod(multiplyPowerByStandConfigPlayers(2.35F));
        } else {
            return levelupDamageMod(multiplyPowerByStandConfigMobs(5.7F));
        }
    }


    public float getExplosiveBubbleStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod(multiplyPowerByStandConfigShooting(multiplyPowerByStandConfigPlayers(1.1F)));
        } else {
            return levelupDamageMod(multiplyPowerByStandConfigShooting(multiplyPowerByStandConfigMobs(3F)));
        }
    }
    public float getGoBeyondStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod(multiplyPowerByStandConfigGoBeyond(multiplyPowerByStandConfigPlayers(11F)));
        } else {
            return levelupDamageMod(multiplyPowerByStandConfigGoBeyond(multiplyPowerByStandConfigMobs(40F)));
        }
    }

    public float getKickAttackKnockback(){
        return (((float)this.chargedFinal/(float)maxSuperHitTime)*2.2F);
    }
    public float getKickAttackStrength(Entity entity){
        float punchD = this.getPunchStrength(entity)*1.8F+this.getHeavyPunchStrength(entity);
        /**Full charge does much less damage because it's more for moving mobs*/

        if (this.chargedFinal >= maxSuperHitTime){
            punchD*=0.5F;
        }
        if (this.getReducedDamage(entity)){
            return (((float)this.chargedFinal/(float)maxSuperHitTime)*punchD);
        } else {
            return (((float)this.chargedFinal/(float)maxSuperHitTime)*punchD)+1;
        }
    }

    public void kickAttackImpact(Entity entity){
        this.setAttackTimeDuring(-20);
        if (entity != null) {
            float pow;
            float knockbackStrength;
            pow = getKickAttackStrength(entity);
            knockbackStrength = getKickAttackKnockback();
            if (StandDamageEntityAttack(entity, pow, 0, this.self)) {
                if (entity instanceof LivingEntity LE) {
                    if (chargedFinal >= maxSuperHitTime) {
                        addEXP(5, LE);
                    } else {
                        addEXP(1, LE);
                    }
                }
                this.takeDeterminedKnockbackWithY(this.self, entity, knockbackStrength);
            } else {
                if (chargedFinal >= maxSuperHitTime) {
                    knockShield2(entity, getKickAttackKnockShieldTime());

                }
            }

            if (entity instanceof LivingEntity LE) {
                if (chargedFinal >= maxSuperHitTime) {
                    StandUser SE = ((StandUser) LE);
                    if (!SE.roundabout$isLaunchBubbleEncased()) {
                        float xRot = this.self.getXRot();
                        SE.roundabout$setStoredVelocity(
                                this.self.getForward().normalize().scale(0.13).add(0,0.033f,0)
                        );
                        if (LE.isAlive()) {
                            SE.roundabout$setBubbleLaunchEncased();
                        }

                        if (!this.self.level().isClientSide()) {
                            Vec3 $$2 = LE.getDeltaMovement();
                            float $$4 = (float) Mth.floor(LE.getY());
                            for (int $$8 = 0; (float) $$8 < 1.0F + LE.getBbWidth() * 20.0F; $$8++) {
                                double $$9 = (LE.level().random.nextDouble() * 2.0 - 1.0) * (double) LE.getBbWidth();
                                double $$10 = (LE.level().random.nextDouble() * 2.0 - 1.0) * (double) LE.getBbWidth();

                                ((ServerLevel) this.getSelf().level()).sendParticles(ParticleTypes.SPLASH,
                                        LE.getX() + $$9, (double) ($$4 + 1.0F), LE.getZ() + $$10,
                                        30, $$2.x, $$2.y, $$2.z, 0.4);
                            }
                        }

                        if (SE instanceof Player && !this.self.level().isClientSide) {
                            ((ServerPlayer) SE).displayClientMessage(Component.translatable("text.roundabout.launch_bubble_encased"), true);
                        }
                        Vec3 storedVec = SE.roundabout$getStoredVelocity();
                        MainUtil.takeLiteralUnresistableKnockbackWithY(LE, storedVec.x, storedVec.y, storedVec.z);
                    }
                }
            }

            int fireCount = 50;
            float firespeed =0.05F;
            if (chargedFinal >= maxSuperHitTime){
                fireCount = 100;
                firespeed =0.1F;
            }
        } else {
            // This is less accurate raycasting as it is server sided but it is important for particle effects
            float distMax = this.getDistanceOut(this.self, this.getReach(), false);
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
            SE = getKickAttackSound();
            pitch = 1.2F;
        } else {
            SE = ModSounds.PUNCH_2_SOUND_EVENT;
        }

        if (!this.self.level().isClientSide()) {
            this.self.level().playSound(null, this.self.blockPosition(), SE, SoundSource.PLAYERS, 0.95F, pitch);
            if (chargedFinal >= maxSuperHitTime && entity instanceof LivingEntity) {
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.WATER_ENCASE_EVENT, SoundSource.PLAYERS, 1F, pitch);
            }
        }
    }

    @Override
    public void handleStandAttack(Player player, Entity target){
        if (this.getActivePower() == PowerIndex.SNEAK_ATTACK){
            kickAttackImpact(target);
        }
    }
    public SoundEvent getKickAttackSound(){
        return ModSounds.SOFT_AND_WET_KICK_EVENT;
    }
    public int getKickAttackKnockShieldTime(){
        return 40;
    }
    public void encasementKick(){

        if (chargedFinal >= maxSuperHitTime) {
            this.setAttackTimeMax((int) (ClientNetworking.getAppropriateConfig().cooldownsInTicks.softAndWetKickMinimum + chargedFinal * 1.5));
        } else {
            this.setAttackTimeMax((int) (ClientNetworking.getAppropriateConfig().cooldownsInTicks.softAndWetKickMinimum + chargedFinal));
        }
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
            kickAttackImpact(targetEntity);
        }
    }
    public void updateKickAttackCharge(){
        if (this.attackTimeDuring > -1) {
            if (this.attackTimeDuring >= maxSuperHitTime &&
                    (!(this.getSelf() instanceof Player) || (this.self.level().isClientSide() && isPacketPlayer()))){
                int atd = this.getAttackTimeDuring();
                ((StandUser) this.getSelf()).roundabout$tryIntPower(PowerIndex.SNEAK_ATTACK, true,maxSuperHitTime);
                if (this.self.level().isClientSide()){
                    tryIntPowerPacket(PowerIndex.SNEAK_ATTACK,atd);
                }
            }
        }
    }
    int bubbleScaffoldCount = 0;

    public int pauseGrowthTicks = 0;
    public BlockPos buildingBubbleScaffoldPos = BlockPos.ZERO;
    public void updateBubbleScaffold(){
        if (this.self instanceof Player PE && this.self.level().isClientSide()) {
            if (isPacketPlayer()) {
                if (this.attackTimeDuring % 6 == 2) {

                    tryPowerPacket(PowerIndex.POWER_3_EXTRA);
                    bubbleScaffoldCount++;
                    this.setCooldown(PowerIndex.SKILL_3, ClientNetworking.getAppropriateConfig().cooldownsInTicks.softAndWetBubbleScaffolding);
                    if (bubbleScaffoldCount >= 10){
                        this.tryPower(PowerIndex.NONE, true);
                        tryPowerPacket(PowerIndex.NONE);
                    }
                }
            }
        }
    }
    public void updateBubbleCluster(){
        if (this.self instanceof Player){
            if (isPacketPlayer()){
                bubbleCheck(true);
            }
        } else {
            bubbleCheck(false);
        }
    }
    public void bubbleCheck(boolean packetPlayer){
        if (this.attackTimeDuring > -1) {
            if (this.attackTimeDuring > 37){
                this.tryPower(PowerIndex.NONE, true);
                if (packetPlayer) {
                    tryPowerPacket(PowerIndex.NONE);
                }
            } else if (this.attackTimeDuring % 3 == 0){
                if (packetPlayer){
                    tryPowerPacket(PowerIndex.POWER_1);
                } else {
                    spawnRandomBubble();
                }
            }
        }
    }

    public boolean spawnRandomBubble(){
        SoftAndWetPlunderBubbleEntity bubble = getPlunderBubble();

        if (bubble != null) {
            bubble.setPlunderType(bubbleType);
            bubble.setSingular(false);
            shootBubbleRandomly(bubble, getBubbleSpeed()); //0.025F
            bubbleListInit();
            this.bubbleList.add(bubble);
            this.getSelf().level().addFreshEntity(bubble);

            if (bubbleType != PlunderTypes.SOUND.id) {
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.BUBBLE_CREATE_EVENT, SoundSource.PLAYERS, 2F, (float) (0.98 + (Math.random() * 0.04)));
            }
        }

        return true;
    }

    public void unloadBubbles(){
        bubbleListInit();
        List<SoftAndWetBubbleEntity> bubbleList2 = new ArrayList<>(bubbleList) {
        };
        if (!bubbleList2.isEmpty()) {
            for (SoftAndWetBubbleEntity value : bubbleList2) {
                if (value.isRemoved() || !value.isAlive() || (this.self.level().isClientSide() && this.self.level().getEntity(value.getId()) == null)) {
                    bubbleList.remove(value);
                } else {
                }
            }
        }
    }

    public int getLowerTicks(){
        return ClientNetworking.getAppropriateConfig().softAndWetSettings.heatTickDownRate;
    }
    public int getLowerGoBeyondTicks(){
        return ClientNetworking.getAppropriateConfig().softAndWetSettings.explosiveSpinMeterTickDownRate;
    }
    public int getLowerExplosiveSpinTicks(){
        return ClientNetworking.getAppropriateConfig().softAndWetSettings.explosiveSpinModeTickDownRate;
    }
    @Override
    public void tickPower(){
        unloadBubbles();
        /**Burn through ticks*/

        tickWaterShield();

        /**tick down the shooting animtation*/
        if (this.self.level().isClientSide()){
            if (this.self instanceof Player PE) {
                IPlayerEntity ipe = ((IPlayerEntity) PE);
                int pt = ipe.roundabout$getBubbleShotAimPoints();
                if (pt > 0){
                    pt--;
                    ipe.roundabout$setBubbleShotAimPoints(pt);
                }
            }
        }

        if (this.self instanceof Player PE && PE.isCreative()) {
            setShootTicks(0);
        } else if (getPauseGrowthTicks() > 0){
            pauseGrowthTicks-=1;
        } else {
            if (getShootTicks() > 0) {
               setShootTicks(getShootTicks() - getLowerTicks());
            }
        }

        if (this.self instanceof Player PE && PE.isCreative()) {
            if (getGoBeyondCharge()>0) {
                setGoBeyondChargeTicks(0);
            }
        } else {
            if (getGoBeyondCharge()>0){
                if (getInExplosiveSpinMode()){
                    setGoBeyondChargeTicks(getGoBeyondCharge()-getLowerExplosiveSpinTicks());
                } else {
                    setGoBeyondChargeTicks(getGoBeyondCharge()-getLowerGoBeyondTicks());
                }
            }
        }

        super.tickPower();
    }

    public Vec3 BubbleRandomPos(){

        float r1 = (float) (Math.random()*1-0.5F);
        float r2= (float) (Math.random()*0.4-0.2F);
        float r3 = (float) (Math.random()*1-0.5F);
        return this.self.getEyePosition().add(r1,r2,r3);
    }

    @Override
    public boolean dealWithProjectile(Entity ent, HitResult res){
        if (!ent.level().isClientSide()) {
            if (hasWaterShield()) {
                boolean success = false;
                if (ent instanceof AbstractArrow AA) {
                    ItemStack ii = ((IAbstractArrowAccess)ent).roundabout$GetPickupItem();
                    if (!ii.isEmpty() && !ii.isDamageableItem()) {
                        SoftAndWetItemLaunchingBubbleEntity bubble = getItemLaunchingBubble();
                        if (bubble != null){

                            success = true;
                            if (!AA.pickup.equals(AbstractArrow.Pickup.ALLOWED)) {
                                bubble.canGiveYouItem = false;
                            }
                        //SE.setHeldItem(ii.copyAndClear());
                            bubble.setHeldItem(ii.copyAndClear());
                            bubble.setPos(BubbleRandomPos());
                            bubbleListInit();
                            this.bubbleList.add(bubble);
                            this.getSelf().level().addFreshEntity(bubble);
                        }
                    }
                } else if (ent instanceof ThrownObjectEntity TO) {
                    ItemStack ii = TO.getItem();
                    if (!ii.isEmpty()) {
                        SoftAndWetItemLaunchingBubbleEntity bubble = getItemLaunchingBubble();
                        if (bubble != null) {
                            success = true;
                            if (!TO.places) {
                                bubble.canGiveYouItem = false;
                            }
                            bubble.setHeldItem(ii.copyAndClear());
                            bubble.setPos(BubbleRandomPos());
                            bubbleListInit();
                            this.bubbleList.add(bubble);
                            this.getSelf().level().addFreshEntity(bubble);
                        }
                    }
                } else if (ent instanceof ThrownPotion TP) {
                    ItemStack ii = TP.getItem();
                    if (!ii.isEmpty()) {
                        SoftAndWetItemLaunchingBubbleEntity bubble = getItemLaunchingBubble();
                        if (bubble != null) {
                            success = true;
                            if (!(TP.getOwner() == null || TP.getOwner() instanceof Player)) {
                                bubble.canGiveYouItem = false;
                            }
                            //SE.setHeldItem(ii.copyAndClear());
                            bubble.setHeldItem(ii.copyAndClear());
                            bubble.setPos(BubbleRandomPos());
                            bubbleListInit();
                            this.bubbleList.add(bubble);
                            this.getSelf().level().addFreshEntity(bubble);
                        }
                    }
                }

                if (success){
                    this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.BUBBLE_PLUNDER_EVENT, SoundSource.PLAYERS, 1.7F, 1.8F);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean waterShieldBlockProjectile(Projectile projectile)
    {
        return true;
    }

    @Override
    public void buttonInput2(boolean keyIsDown, Options options) {
        if (this.getSelf().level().isClientSide) {
            if (isGuarding() && !inShootingMode()) {

                if (keyIsDown) {
                    if (!hold2) {
                        if (!this.onCooldown(PowerIndex.SKILL_EXTRA_2)){
                            hold2 = true;



                            Vec3 pos = MainUtil.getRaytracePointOnMobOrBlock(this.self,30);

                            this.tryPosPower(PowerIndex.POWER_2_EXTRA, true, pos);

                            tryPosPowerPacket(PowerIndex.POWER_2_EXTRA,pos);

                            //this.setCooldown(PowerIndex.SKILL_1, ClientNetworking.getAppropriateConfig().cooldownsInTicks.magicianRedBindFailOrMiss);
                        }
                    }
                } else {
                    hold2 = false;
                }

            } else if (isHoldingSneak()) {


                if (keyIsDown) {
                    if (!hold2) {
                        if (!this.onCooldown(PowerIndex.SKILL_2_SNEAK)){
                            hold2 = true;

                            Vec3 pos = MainUtil.getRaytracePointOnMobOrBlock(this.self,30);

                            this.tryPosPower(PowerIndex.POWER_2_SNEAK, true, pos);
                            tryPosPowerPacket(PowerIndex.POWER_2_SNEAK,pos);
                            //this.setCooldown(PowerIndex.SKILL_1, ClientNetworking.getAppropriateConfig().cooldownsInTicks.magicianRedBindFailOrMiss);
                        }
                    }
                } else {
                    hold2 = false;
                }

            } else {
                if (keyIsDown) {
                    if (!hold2) {
                        hold2 = true;
                        if (!inShootingMode()) {
                            if (!this.onCooldown(PowerIndex.SKILL_2)) {

                                int bubbleType = 1;
                                ClientConfig clientConfig = ConfigManager.getClientConfig();
                                if (clientConfig != null && clientConfig.dynamicSettings != null) {
                                    bubbleType = clientConfig.dynamicSettings.SoftAndWetCurrentlySelectedBubble;
                                }

                                this.tryIntPower(PowerIndex.POWER_2, true, bubbleType);

                                tryIntPowerPacket(PowerIndex.POWER_2,bubbleType);
                                //this.setCooldown(PowerIndex.SKILL_1, ClientNetworking.getAppropriateConfig().cooldownsInTicks.magicianRedBindFailOrMiss);
                            }
                        } else {
                            if (canExecuteMoveWithLevel(getItemShootingLevel())) {
                                if (!this.onCooldown(PowerIndex.SKILL_2)) {
                                    if (canDoBubbleItemLaunch()) {

                                        this.tryIntPower(PowerIndex.POWER_2_BONUS, true, ((Player) this.getSelf()).getInventory().selected);

                                        tryIntPowerPacket(PowerIndex.POWER_2_BONUS, ((Player) this.getSelf()).getInventory().selected);
                                        //this.setCooldown(PowerIndex.SKILL_1, ClientNetworking.getAppropriateConfig().cooldownsInTicks.magicianRedBindFailOrMiss);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    hold2 = false;
                }
            }
        }
        super.buttonInput1(keyIsDown, options);
    }

    public boolean canDoBubbleItemLaunch(){
        ItemStack stack = this.getSelf().getMainHandItem();
        return !stack.isEmpty();
    }

    @Override
    public void playFallBraceImpactParticles(){
        ((ServerLevel) this.getSelf().level()).sendParticles(ModParticles.BUBBLE_POP,
                this.getSelf().getX(), this.getSelf().getOnPos().getY() + 1.1, this.getSelf().getZ(),
                50, 1.1, 0.05, 1.1, 0.4);
        ((ServerLevel) this.getSelf().level()).sendParticles(ModParticles.BUBBLE_POP,
                this.getSelf().getX(), this.getSelf().getOnPos().getY() + 1.1, this.getSelf().getZ(),
                30, 1, 0.05, 1, 0.4);
    }

    @Override
    public void playFallBraceInitSound(){
        this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.SUMMON_SOFT_AND_WET_EVENT, SoundSource.PLAYERS, 2.3F, (float) (0.78 + (Math.random() * 0.04)));
    }
    @Override
    public void playFallBraceImpactSounds(){
        this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.BUBBLE_POP_EVENT, SoundSource.PLAYERS, 1.0F, (float) (0.9 + (Math.random() * 0.2)));
        this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.BUBBLE_POP_EVENT, SoundSource.PLAYERS, 1.0F, (float) (0.9 + (Math.random() * 0.2)));
        this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.BUBBLE_POP_EVENT, SoundSource.PLAYERS, 1.0F, (float) (0.9 + (Math.random() * 0.2)));
        this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.BUBBLE_POP_EVENT, SoundSource.PLAYERS, 1.0F, (float) (0.9 + (Math.random() * 0.2)));
        this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.BUBBLE_POP_EVENT, SoundSource.PLAYERS, 1.0F, (float) (0.9 + (Math.random() * 0.2)));
        this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.BUBBLE_POP_EVENT, SoundSource.PLAYERS, 1.0F, (float) (0.9 + (Math.random() * 0.2)));
        this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.BUBBLE_POP_EVENT, SoundSource.PLAYERS, 1.0F, (float) (0.9 + (Math.random() * 0.2)));
    }
    public boolean hold3 = false;
    @Override
    public void buttonInput3(boolean keyIsDown, Options options) {
        if (this.getSelf().level().isClientSide) {
            if (!(keyIsDown && !hold3 && doVault())) {
                if (canFallBrace()) {
                    if (keyIsDown) {
                        if (!hold3){
                            hold3 = true;
                            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.EXTRA, true);
                            tryPowerPacket(PowerIndex.EXTRA);
                        }
                    } else {
                        hold3 = false;
                    }
                } else if (isGuarding()) {
                    if (keyIsDown) {
                        if (!hold3){
                            hold3 = true;
                            if (!this.onCooldown(PowerIndex.SKILL_EXTRA) && canBigBubble()) {
                                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_3_BONUS, true);
                                tryPowerPacket(PowerIndex.POWER_3_BONUS);
                            }
                        }
                    } else {
                        hold3 = false;
                    }
                } else if (isHoldingSneak()) {
                    if (keyIsDown) {
                        if (canExecuteMoveWithLevel(getScaffoldLevel())) {
                            if (!this.onCooldown(PowerIndex.SKILL_3)) {
                                if (!hold3) {
                                    if (canBridge()) {
                                        hold3 = true;
                                        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_3, true);
                                        tryPowerPacket(PowerIndex.POWER_3);
                                    }
                                }
                            }
                        }
                    } else {
                        hold3 = false;
                    }
                } else {
                    if (!keyIsDown) {
                        hold3 = false;
                    }
                    super.buttonInput3(keyIsDown, options);
                }
            }
        }
    }

    public boolean hold4 = false;
    @Override
    public void buttonInput4(boolean keyIsDown, Options options) {
        if (this.getSelf().level().isClientSide) {
            if (isGuarding()) {
                if (keyIsDown) {
                    if (!hold4){
                        hold4 = true;
                    }
                } else {
                    hold4 = false;
                }
            } else if (isHoldingSneak() && !inShootingMode()) {
                if (keyIsDown) {
                    if (!this.onCooldown(PowerIndex.SKILL_4_SNEAK)) {
                        if (canExecuteMoveWithLevel(getWaterShieldLevel())) {
                            if (!hold4) {
                                hold4 = true;
                                if (canUseWaterShield()) {
                                    this.tryPower(PowerIndex.POWER_4_SNEAK, true);
                                    tryPowerPacket(PowerIndex.POWER_4_SNEAK);
                                }
                            }
                        }
                    }
                } else {
                    hold4 = false;
                }
            } else {
                if (keyIsDown) {
                    if (canExecuteMoveWithLevel(getShootingModeLevel())) {
                        if (!hold4) {
                            hold4 = true;

                            this.tryPower(PowerIndex.POWER_4, true);
                            tryPowerPacket(PowerIndex.POWER_4);

                            getStandUserSelf().roundabout$getStandPowers().tryPower(PowerIndex.NONE, true);
                            tryPowerPacket(PowerIndex.NONE);
                            ClientUtil.stopDestroyingBlock();
                        }
                    }
                } else {
                    hold4 = false;
                }
            }
        }
    }

    public boolean canBridge(){
        return ((this.self.onGround() && !this.self.isInWater()) || this.self.onClimbable() || (this.self instanceof Player PE && PE.isCreative()));
    }


    public boolean canBigBubble(){
        return ((this.self.onGround() || this.self.isInWater()) || this.self.onClimbable() || (this.self instanceof Player PE && PE.isCreative()));
    }
}

