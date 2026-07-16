package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.KingCrimsonEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.stand.StarPlatinumEntity;
import net.hydra.jojomod.entity.stand.WalkingHeartEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewPunchingStand;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
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

public class PowersKingCrimson extends NewPunchingStand {

    public PowersKingCrimson(LivingEntity self) {
        super(self);
    }

    @Override
    /**Override to add disable config*/
    public boolean isStandEnabled() {
        return ClientNetworking.getAppropriateConfig().theWorldSettings.enableTheWorld;
    }


    @Override
    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersKingCrimson(entity);
    }

    @Override
    public SoundEvent getSoundFromByte(byte soundChoice) {
        if (soundChoice == SoundIndex.SUMMON_SOUND) {
            return ModSounds.SUMMON_KING_CRIMSON_EVENT;
        }
        return super.getSoundFromByte(soundChoice);
    }
        @Override
    public StandEntity getNewStandEntity() {
        byte sk = ((StandUser) this.getSelf()).roundabout$getStandSkin();
        return ModEntities.KING_CRIMSON.create(this.getSelf().level());
    }
    @Override
    public Component getSkinName(byte skinId) {
        return getSkinNameT(skinId);
    }
    public static Component getSkinNameT(byte skinId){
        if (skinId == KingCrimsonEntity.MANGA_SKIN){
            return Component.translatable(  "skins.roundabout.king_crimson.manga");
        } if (skinId == KingCrimsonEntity.END){
            return Component.translatable(  "skins.roundabout.king_crimson.end");
        } if (skinId == KingCrimsonEntity.END_2){
            return Component.translatable(  "skins.roundabout.king_crimson.end_2");
        } if (skinId == KingCrimsonEntity.STARLESS){
            return Component.translatable(  "skins.roundabout.king_crimson.starless");
        } if (skinId == KingCrimsonEntity.HEAVEN){
            return Component.translatable(  "skins.roundabout.king_crimson.heaven");
        }if (skinId == KingCrimsonEntity.AGOGO){
            return Component.translatable(  "skins.roundabout.king_crimson.agogo");
        }if (skinId == KingCrimsonEntity.SPINE_ART){
            return Component.translatable(  "skins.roundabout.king_crimson.spine_art");
        }if (skinId == KingCrimsonEntity.GREEN){
            return Component.translatable(  "skins.roundabout.king_crimson.green");
        }if (skinId == KingCrimsonEntity.YELLOW){
            return Component.translatable(  "skins.roundabout.king_crimson.yellow");
        }if (skinId == KingCrimsonEntity.AQUA){
            return Component.translatable(  "skins.roundabout.king_crimson.aqua");
        }if (skinId == KingCrimsonEntity.BLACK){
            return Component.translatable(  "skins.roundabout.king_crimson.black");
        }if (skinId == KingCrimsonEntity.DARK){
            return Component.translatable(  "skins.roundabout.king_crimson.dark");
        }if (skinId == KingCrimsonEntity.BETA){
            return Component.translatable(  "skins.roundabout.king_crimson.beta");
        }if (skinId == KingCrimsonEntity.CONCEPT){
            return Component.translatable(  "skins.roundabout.king_crimson.concept");
        }if (skinId == KingCrimsonEntity.RED){
            return Component.translatable(  "skins.roundabout.king_crimson.red");
        }if (skinId == KingCrimsonEntity.BLUE){
            return Component.translatable(  "skins.roundabout.king_crimson.blue");
        }
        return Component.translatable(  "skins.roundabout.king_crimson.base");
    }

    @Override
    public List<Byte> getSkinList() {
        List<Byte> $$1 = Lists.newArrayList();
        $$1.add(KingCrimsonEntity.RED);
        if (this.getSelf() instanceof Player PE) {
            byte Level = ((IPlayerEntity) PE).roundabout$getStandLevel();
            ItemStack goldDisc = ((StandUser) PE).roundabout$getStandDisc();
            boolean bypass = PE.isCreative() || (!goldDisc.isEmpty() && goldDisc.getItem() instanceof MaxStandDiscItem);

            $$1.add(KingCrimsonEntity.PART_5_SKIN);
            $$1.add(KingCrimsonEntity.MANGA_SKIN);
            if (Level > 1 || bypass) {
                $$1.add(KingCrimsonEntity.SPINE_ART);
                $$1.add(KingCrimsonEntity.AGOGO);
            } if (Level > 2 || bypass) {
                $$1.add(KingCrimsonEntity.BLUE);
                $$1.add(KingCrimsonEntity.BLACK);
                $$1.add(KingCrimsonEntity.DARK);
            } if (Level > 3 || bypass) {
                $$1.add(KingCrimsonEntity.HEAVEN);
                $$1.add(KingCrimsonEntity.AQUA);
                $$1.add(KingCrimsonEntity.YELLOW);
                $$1.add(KingCrimsonEntity.GREEN);
            } if (Level > 4 || bypass) {
                $$1.add(KingCrimsonEntity.STARLESS);
                $$1.add(KingCrimsonEntity.CONCEPT);
                $$1.add(KingCrimsonEntity.BETA);
            } if (((IPlayerEntity)PE).roundabout$getUnlockedBonusSkin() || bypass){
                $$1.add(KingCrimsonEntity.END);
                $$1.add(KingCrimsonEntity.END_2);
            }
        }
        return $$1;
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
    public boolean isWip(){
        return true;
    }
    @Override
    public Component ifWipListDevStatus(){
        return Component.translatable(  "roundabout.dev_status.active").withStyle(ChatFormatting.AQUA);
    }
    @Override
    public Component ifWipListDev(){
        return Component.literal(  "Hydra").withStyle(ChatFormatting.GOLD);
    }
    @Override
    public void renderAttackHud(GuiGraphics context, Player playerEntity,
                                int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                float flashAlpha, float otherFlashAlpha) {
        StandUser standUser = ((StandUser) playerEntity);
        boolean standOn = PowerTypes.hasStandActive(playerEntity);
        int j = scaledHeight / 2 - 7 - 4;
        int k = scaledWidth / 2 - 8;
        if (this.getActivePower() == PowerIndex.POWER_1_SNEAK){
            Entity TE = this.getTargetEntity(playerEntity, impaleRange);
            if (TE != null) {
                context.blit(StandIcons.JOJO_ICONS, k, j, 193, 0, 15, 6);
            }
        } else if (standOn && this.getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE){
            int ClashTime = Math.min(15,Math.round(((float) attackTimeDuring / getMaxSuperHitTime()) * 15));
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 30, ClashTime, 6);
        } else {
            super.renderAttackHud(context,playerEntity,
                    scaledWidth,scaledHeight,ticks,vehicleHeartCount, flashAlpha, otherFlashAlpha);
        }
    }
    //hold input
    public boolean holdDownClick = false;
    public int impaleTicks = 0;
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
                        int atd = this.getAttackTimeDuring();
                        this.tryIntPower(PowerIndex.SNEAK_ATTACK, true, atd);
                        tryIntPowerPacket(PowerIndex.SNEAK_ATTACK,atd);
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
    /**Stand related things that slow you down or speed you up*/
    public float inputSpeedModifiers(float basis){
        if (this.activePower == PowerIndex.SNEAK_ATTACK_CHARGE) {
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
        return super.inputSpeedModifiers(basis);
    }
    @Override
    public void updateUniqueMoves() {
        /*Tick through Time Stop Charge*/
        if (this.getActivePower() == PowerIndex.POWER_1_SNEAK){
            updateImpale();
        } else if (this.getActivePower() == PowerIndex.SNEAK_ATTACK){
            updateFinalAttack();
        } else if (this.getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE){
            updateFinalAttackCharge();
        }
        super.updateUniqueMoves();
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
    public boolean setPowerOther(int move, int lastMove) {
        if (move == PowerIndex.VAULT){
            return this.vault();
        } else if (move == PowerIndex.POWER_1_SNEAK){
            return this.impale();
        } else if (move == PowerIndex.SNEAK_ATTACK_CHARGE){
            return this.setPowerFinalAttack();
        } else if (move == PowerIndex.SNEAK_ATTACK){
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
    @Override
    public void handleStandAttack(Player player, Entity target){
        if (this.getActivePower() == PowerIndex.POWER_1_SNEAK){
            impaleImpact(target);
        } else if (this.getActivePower() == PowerIndex.SNEAK_ATTACK){
            finalAttackImpact(target);
        }
    }
    public void animateFinalAttack(){
        animateStand(StandEntity.FINAL_ATTACK_WINDUP);
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
    public static final float impaleRange = 3.5F;
    public void standImpale(){
        /*By setting this to -10, there is a delay between the stand retracting*/

        if (this.self instanceof Player){
            if (isPacketPlayer()){
                this.setAttackTimeDuring(-20);
                impaleTicks = 15;
                tryIntToServerPacket(PacketDataIndex.INT_STAND_ATTACK,getTargetEntityId2(impaleRange));
            }
        } else {
            /*Caps how far out the punch goes*/
            Entity targetEntity = getTargetEntity(this.self,impaleRange);
            impaleImpact(targetEntity);
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

        this.setAttackTimeMax(ClientNetworking.getAppropriateConfig().generalStandSettings.finalPunchAndKickMinimumCooldown + chargedFinal);
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
            finalAttackImpact(targetEntity);
        }
    }

    public void finalAttackImpact(Entity entity){
        this.setAttackTimeDuring(-20);

        if (entity != null && entity.distanceTo(self) > 5.5F) {
            entity = null;
        }
        if (entity != null) {
            hitParticlesCenter(entity);
            float pow;
            float knockbackStrength;
            pow = getFinalPunchStrength(entity);
            knockbackStrength = getFinalAttackKnockback();
            if (StandDamageEntityAttack(entity, pow, 0, this.self)) {
                if (entity instanceof LivingEntity LE) {
                    if (chargedFinal >= getMaxSuperHitTime()) {
                        addEXP(5, LE);
                    }
                }
                takeDeterminedKnockbackWithY(this.self, entity, knockbackStrength);
            } else {
                if (chargedFinal >= getMaxSuperHitTime()) {
                    knockShield2(entity, getFinalAttackKnockShieldTime());
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
    public float getFinalAttackKnockback(){
        return (((float)this.chargedFinal /(float)getMaxSuperHitTime())*3);
    }
    public float getFinalPunchStrength(Entity entity){
        float punchD = this.getPunchStrength(entity)*2+this.getHeavyPunchStrength(entity);
        if (this.getReducedDamage(entity)){
            return (((float)this.chargedFinal/(float)getMaxSuperHitTime())*punchD);
        } else {
            return (((float)this.chargedFinal/(float)getMaxSuperHitTime())*punchD)+3;
        }
    }
    public int getFinalAttackKnockShieldTime(){
        return 20;
    }

    public int getMaxSuperHitTime(){
        return 25+(getMeltLevel()*2);
    }

    public void updateFinalAttackCharge(){
        if (this.attackTimeDuring > -1) {
            if (this.attackTimeDuring >= 80) {
                if (this.getSelf() instanceof Player && this.getSelf().level().isClientSide && this.isPacketPlayer()){
                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                    tryPowerPacket(PowerIndex.NONE);
                }
            } else if (this.attackTimeDuring >= getMaxSuperHitTime() && !(this.getSelf() instanceof Player)){
                ((StandUser) this.getSelf()).roundabout$tryIntPower(PowerIndex.SNEAK_ATTACK, true,getMaxSuperHitTime());
            }
        }
    }

}