package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.D4CEntity;
import net.hydra.jojomod.entity.stand.JusticeEntity;
import net.hydra.jojomod.entity.stand.KingCrimsonEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewPunchingStand;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class PowersD4C extends NewPunchingStand {
    public PowersD4C(LivingEntity self) {
        super(self);
    }
    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersD4C(entity);
    }

    @Override
    /**Override to add disable config*/
    public boolean isStandEnabled() {
        return ClientNetworking.getAppropriateConfig().kingCrimsonSettings.enableKingCrimson;
    }
    @Override
    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }
    @Override
    public SoundEvent getSoundFromByte(byte soundChoice) {
        if (soundChoice == SoundIndex.SUMMON_SOUND) {
            return ModSounds.SUMMON_D4C_EVENT;
        }
        return super.getSoundFromByte(soundChoice);
    }

    @Override
    public void tickPower() {
        super.tickPower();
    }

    @Override
    public int getDisplayPowerInventoryScale(){
        return 25;
    }

    @Override
    public StandEntity getNewStandEntity() {
        return ModEntities.D4C.create(this.getSelf().level());
    }
    @Override
    public void powerActivate(PowerContext context) {
        switch (context)
        {
            case SKILL_3_NORMAL -> {
                dash();
            }
        }
    }

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
          setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
    }
    @Override
    public boolean cancelSprintJump(){
        if (this.getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE){
            return true;
        }
        return super.cancelSprintJump();
    }

    public boolean setPowerFinalAttack() {
        animateFinalAttack();
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.SNEAK_ATTACK_CHARGE);
        this.poseStand(OffsetIndex.GUARD);
        this.clashDone = false;
        return true;
    }
    public void animateFinalAttack(){
        animateStand(StandEntity.FINAL_ATTACK_WINDUP);
    }

    public int chargedFinal = 0;
    public float getChargedPercent(){
        return (((float)this.chargedFinal/(float)getMaxSuperHitTime()));
    }

    @Override
    public void updateUniqueMoves() {
        /*Tick through Time Stop Charge*/
        if (this.getActivePower() == PowerIndex.SNEAK_ATTACK) {
            updateFinalAttack();
        } else if (this.getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE) {
            updateFinalAttackCharge();
        }
        super.updateUniqueMoves();
    }
    public void updateFinalAttackCharge(){
        if (this.attackTimeDuring > -1) {
            if (this.attackTimeDuring >= 60) {
                if (this.getSelf() instanceof Player && this.getSelf().level().isClientSide && this.isPacketPlayer()){
                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                    tryPowerPacket(PowerIndex.NONE);
                }
            } else if (this.attackTimeDuring >= getMaxSuperHitTime() && !(this.getSelf() instanceof Player)){
                ((StandUser) this.getSelf()).roundabout$tryIntPower(PowerIndex.SNEAK_ATTACK, true,getMaxSuperHitTime());
            }
        }
    }
    @Override
    /**Stand related things that slow you down or speed you up*/
    public float inputSpeedModifiers(float basis){
        if (this.activePower == PowerIndex.SNEAK_ATTACK_CHARGE) {
            if (this.getSelf().isCrouching()) {
                float f = Mth.clamp(0.3F + EnchantmentHelper.getSneakingSpeedBonus(this.getSelf()), 0.0F, 1.0F);
                float g = 1 / f;
                basis *= g;
            }
            basis *= 0.3f;
        }
        return super.inputSpeedModifiers(basis);
    }

    //hold input
    public boolean holdDownClick = false;
    @Override
    public void buttonInputAttack(boolean keyIsDown, Options options) {
        if (!consumeClickInput) {
            if (impaleTicks > 0){
                return;
            }
            if (holdDownClick) {
                if (keyIsDown) {

                } else {
                    if (this.getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE) {
                        C2SPacketUtil.trySingleBytePacket(PacketDataIndex.SINGLE_STAND_TRIGGER_2);
                        int atd = this.getAttackTimeDuring();
                        this.tryIntPower(PowerIndex.SNEAK_ATTACK, true, atd);
                        tryIntPowerPacket(PowerIndex.SNEAK_ATTACK, atd);
                    }
                    holdDownClick = false;
                }
            } else {
                if (keyIsDown) {
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
        } else {
            if (!keyIsDown) {
                consumeClickInput = false;
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
    @Override
    public boolean tryIntPower(int move, boolean forced, int chargeTime){
        if (move == PowerIndex.SNEAK_ATTACK) {
            this.chargedFinal = chargeTime;
        }
        return super.tryIntPower(move, forced, chargeTime);
    }
    @Override
    public boolean setPowerOther(int move, int lastMove) {
        if (move == PowerIndex.SNEAK_ATTACK_CHARGE) {
            return this.setPowerFinalAttack();
        } else if (move == PowerIndex.SNEAK_ATTACK) {
            return this.setPowerSuperHit();
        }
        return super.setPowerOther(move,lastMove);
    }
    public boolean setPowerSuperHit() {
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.SNEAK_ATTACK);
        this.poseStand(OffsetIndex.ATTACK);
        chargedFinal = Math.min(this.chargedFinal,getMaxSuperHitTime());
        animateFinalAttackHit();
        //playBarrageCrySound();
        return true;
    }
    public void updateFinalAttack(){
        if (this.attackTimeDuring > -1) {
            if (this.attackTimeDuring == 5) {
                this.standFinalAttack();
            }
        }
    }

    public void standFinalAttack(){

        this.setAttackTimeMax(ClientNetworking.getAppropriateConfig().generalStandSettings.finalPunchAndKickMinimumCooldown + chargedFinal);
        this.setAttackTime(0);
        this.setActivePowerPhase(this.getActivePowerPhaseMax());

        if (this.self instanceof Player){
            if (isPacketPlayer()){
                this.attackTimeDuring = -10;
                tryIntToServerPacket(PacketDataIndex.INT_STAND_ATTACK,getTargetEntityId());
            }
        } else {
            /*Caps how far out the punch goes*/
            Entity targetEntity = getTargetEntity(this.self,-1);
            finalAttackImpact(targetEntity);
        }
    }

    public float getFinalAttackKnockback(){
        float charge = getChargedPercent();
        if (charge >= 1){
            return 0.7F;
        } else if (charge >= 0.5F){
            return (((float)this.chargedFinal /(float)getMaxSuperHitTime())*3);
        }
        return 0.1F;
    }
    public float getFinalPunchStrength(Entity entity){
        float punchD = this.getPunchStrength(entity)*2+this.getHeavyPunchStrength(entity);
        if (this.getReducedDamage(entity)){
            float ret = (getChargedPercent()*punchD);
            if (this.chargedFinal >= getMaxSuperHitTime()){
                ret +=0.5F;
            }
            return ret;
        } else {
            float ret = (getChargedPercent()*punchD)+3;
            if (this.chargedFinal >= getMaxSuperHitTime()){
                ret +=2;
            }
            return ret;
        }
    }

    public void finalAttackImpact(Entity entity){
        this.setAttackTimeDuring(-20);

        if (entity != null && entity.distanceTo(self) > 5.5F) {
            entity = null;
        }
        if (entity != null) {
            float charged = getChargedPercent();
            hitParticlesCenter(entity);
            float pow;
            float knockbackStrength;
            pow = getFinalPunchStrength(entity);
            knockbackStrength = getFinalAttackKnockback();
            if (StandDamageEntityAttack(entity, pow, 0, this.self)) {
                if (entity instanceof LivingEntity LE) {
                    if (charged >= 1) {
                        addEXP(5, LE);
                    } else if (charged > 0.5F){
                        addEXP(3, LE);
                    }
                }
                takeDeterminedKnockbackWithY(this.self, entity, knockbackStrength);
            } else {
                if (chargedFinal >= getMaxSuperHitTime()) {
                    if (charged >= 1) {
                        knockShield2(entity, 70);
                    } else if (charged > 0.5F){
                        knockShield2(entity, 50);
                    }
                }
            }
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
            SE = getFinalAttackSound();
            pitch = getFinalAttackPitch();
        } else {
            SE = ModSounds.PUNCH_2_SOUND_EVENT;
        }

        if (!this.self.level().isClientSide()) {
            this.self.level().playSound(null, this.self.blockPosition(), SE, SoundSource.PLAYERS, 0.95F, pitch);
        }
    }
    public SoundEvent getFinalAttackSound(){
        float charged = getChargedPercent();
        if (charged >= 1F){
            return ModSounds.KING_CRIMSON_PUNCH_4_EVENT;
        } else if (charged >= 0.5F){
            return ModSounds.KING_CRIMSON_PUNCH_5_EVENT;
        }
        return ModSounds.KING_CRIMSON_PUNCH_3_EVENT;
    }
    public float getFinalAttackPitch(){
        float charged = getChargedPercent();
        if (charged >= 1F){
            return 1;
        } else if (charged >= 0.5F){
            return 1;
        }
        return 1.2F;
    }

    public void animateFinalAttackHit(){
        float charged = getChargedPercent();
        if (charged >= 1F){
            animateStand(KingCrimsonEntity.FINAL_2);
            return;
        } else if (charged >= 0.5F){
            animateStand(KingCrimsonEntity.FINAL_1);
            return;
        }
        animateStand((byte) 86);
    }

    @Override
    public void renderAttackHud(GuiGraphics context, Player playerEntity,
                                int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                float flashAlpha, float otherFlashAlpha) {
        boolean standOn = PowerTypes.hasStandActive(playerEntity);
        int j = scaledHeight / 2 - 7 - 4;
        int k = scaledWidth / 2 - 8;
        if (standOn && this.getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE){
            float zamn = ((float) attackTimeDuring / getMaxSuperHitTime());
            int ClashTime = Math.min(15,Math.round(zamn * 15));
            context.blit(StandIcons.JOJO_ICONS, k, j, 213, 68, 15, 6);
            if (zamn >= 1){
                context.blit(StandIcons.JOJO_ICONS, k, j, 213, 89, ClashTime, 6);
            } else if (crossedThreshold2(zamn)){
                context.blit(StandIcons.JOJO_ICONS, k, j, 213, 75, ClashTime, 6);
            } else {
                context.blit(StandIcons.JOJO_ICONS, k, j, 213, 82, ClashTime, 6);
            }
        } else {
            super.renderAttackHud(context,playerEntity,
                    scaledWidth,scaledHeight,ticks,vehicleHeartCount, flashAlpha, otherFlashAlpha);
        }
    }
    public boolean crossedThreshold2(float zamn){
        return zamn >= 0.5F;
    }

    public int getMaxSuperHitTime(){
        return 30+(getMeltLevel()*2);
    }
    @Override public Component getSkinName(byte skinId) {
        return Component.translatable("skins.roundabout.d4c."+getSkinString(skinId));
    }
    public static Component getSkinNameStatic(byte skinId) {
        return Component.translatable("skins.roundabout.d4c."+getSkinString(skinId));
    }
    public static String getSkinString(byte skinId) {

        return switch (skinId)
        {
            case D4CEntity.BASE -> "base";
            case D4CEntity.COVER -> "cover";
            case D4CEntity.SPINE -> "spine";
            case D4CEntity.VELLER -> "veller";
            case D4CEntity.ROA -> "roa";
            case D4CEntity.DEPARTURE -> "departure";
            case D4CEntity.PROMO -> "promo";
            case D4CEntity.FOUNTAIN_BOY -> "fountain_boy";
            case D4CEntity.CONCEPT_ART -> "concept_art";
            case D4CEntity.EOH_BLUE -> "eoh_blue";
            case D4CEntity.EOH_PINK -> "eoh_pink";
            case D4CEntity.EOH_PURPLE -> "eoh_purple";
            case D4CEntity.EOH_YELLOW -> "eoh_yellow";
            case D4CEntity.GOLDEN -> "golden";
            case D4CEntity.GOLDEN_V2 -> "golden_v2";
            case D4CEntity.CURSED_CARD -> "cursed_card";
            case D4CEntity.ULTRA_BRAND -> "ultra_brand";
            case D4CEntity.KEYHOLDER -> "keyholder";
            case D4CEntity.GRAY -> "gray";
            case D4CEntity.BLACK_WHITE -> "black_white";
            case D4CEntity.REVERSE -> "reverse";
            case D4CEntity.CHOCOLATE -> "chocolate";
            case D4CEntity.INVERSE -> "inverse";
            default -> "base";
        };
    }
    @Override
    public List<Byte> getSkinList(){
        List<Byte> $$1 = Lists.newArrayList();
        $$1.add(D4CEntity.BASE);
        if (this.getSelf() instanceof Player PE){
            byte Level = ((IPlayerEntity)PE).roundabout$getStandLevel();
            ItemStack goldDisc = ((StandUser)PE).roundabout$getStandDisc();
            boolean bypass = PE.isCreative() || (!goldDisc.isEmpty() && goldDisc.getItem() instanceof MaxStandDiscItem);
            if (Level > 1 || bypass){
                $$1.add(D4CEntity.COVER);
                $$1.add(D4CEntity.SPINE);
                $$1.add(D4CEntity.CHOCOLATE);
            } if (Level > 2 || bypass){
                $$1.add(D4CEntity.VELLER);
                $$1.add(D4CEntity.ROA);
                $$1.add(D4CEntity.BLACK_WHITE);
                $$1.add(D4CEntity.REVERSE);
            } if (Level > 3 || bypass){
                $$1.add(D4CEntity.DEPARTURE);
                $$1.add(D4CEntity.PROMO);
                $$1.add(D4CEntity.KEYHOLDER);
                $$1.add(D4CEntity.GRAY);
            } if (Level > 4 || bypass){
                $$1.add(D4CEntity.EOH_BLUE);
                $$1.add(D4CEntity.EOH_PINK);
                $$1.add(D4CEntity.EOH_PURPLE);
                $$1.add(D4CEntity.EOH_YELLOW);
            } if (Level > 5 || bypass){
                $$1.add(D4CEntity.FOUNTAIN_BOY);
                $$1.add(D4CEntity.CONCEPT_ART);
                $$1.add(D4CEntity.CURSED_CARD);
                $$1.add(D4CEntity.ULTRA_BRAND);
            } if (Level > 6 || bypass){
                $$1.add(D4CEntity.INVERSE);
                $$1.add(D4CEntity.GOLDEN);
                $$1.add(D4CEntity.GOLDEN_V2);
            } if (((IPlayerEntity)PE).roundabout$getUnlockedBonusSkin() || bypass){
            }
        }
        return $$1;
    }
    public boolean isWip(){
        return true;
    }
    public Component ifWipListDevStatus(){
        return Component.translatable(  "roundabout.dev_status.active").withStyle(ChatFormatting.AQUA);
    }
    public Component ifWipListDev(){
        return Component.literal(  "Hydra").withStyle(ChatFormatting.GOLD);
    }
}
