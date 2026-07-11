package net.hydra.jojomod.stand.powers;

import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.event.index.PlayerPosIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class PowersOasis extends NewDashPreset {
    public PowersOasis(LivingEntity self) {
        super(self);
    }

    @Override
    public boolean isStandEnabled(){
        return ClientNetworking.getAppropriateConfig().oasisSettings.enableOasis;
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersOasis(entity);
    }

    @Override
    public boolean canSummonStandAsEntity(){
        return false;
    }

    @Override
    public boolean rendersPlayer(){
        return true;
    }

    @Override
    public boolean isBrawling(){
        return fistsOut;
    }
    @Override
    public boolean interceptAttack(){
        return fistsOut;
    }
    @Override
    public boolean interceptGuard(){
        return fistsOut;
    }

    public boolean fistsOut = false;

    @Override
    public boolean isWip() {
        return true;
    }
    @Override
    public Component ifWipListDevStatus(){
        return Component.translatable(  "roundabout.dev_status.active").withStyle(ChatFormatting.WHITE);
    }
    @Override
    public Component ifWipListDev(){
        return Component.literal(  "kepich").withStyle(ChatFormatting.WHITE);
    }


    public boolean renderSuit(){
        return (self instanceof Player pl || MainUtil.isHumanoid2(self)) && PowerTypes.hasStandActive(self);
    }

    // stand fading for first person
    public static float getOasisAmt(Entity entity, float partialTicks){
        float heyFull = 0;
        if (entity instanceof LivingEntity LE) {
            StandUser user = ((StandUser) LE);
            boolean hasOasisOut = user.roundabout$getStandPowers() instanceof PowersOasis po && po.renderSuit();
            int oasisTicks = user.roundabout$getOasisVanishTicks();
            if (hasOasisOut || oasisTicks > 0) {
                byte skin = user.roundabout$getStandSkin();
                if (user.roundabout$getLastStandSkin() != skin) {
                    user.roundabout$setLastStandSkin(skin);
                    oasisTicks = 0;
                    user.roundabout$setOasisVanishTicks(0);
                }

                float partialTicks2 = partialTicks % 1;
                if (hasOasisOut) {
                    heyFull = oasisTicks + partialTicks2;
                    heyFull = Math.min(heyFull / 10, 1f);
                } else {
                    heyFull = oasisTicks - partialTicks2;
                    heyFull = Math.max(heyFull / 10, 0);
                }
            }
        }
        return heyFull;
    }


    public void toggleFistsClient() {
        if (self instanceof Player pl){
            pl.resetAttackStrengthTicker();
        }
        tryPowerPacket(PowerIndex.POWER_1);
    }

    @Override
    public void onStandSummon(boolean desummon){
        if (self instanceof Player pl && fistsOut){
            pl.resetAttackStrengthTicker();
        }
    }

    public void toggleFists() {
        if (!this.self.level().isClientSide()){
            fistsOut = !fistsOut;
            saveDiscAndSync();
        }
    }


    public void renderAttackHud(GuiGraphics context, Player playerEntity,
                                int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                float flashAlpha, float otherFlashAlpha) {
        boolean powerOn = PowerTypes.hasStandActive(playerEntity);
        int j = scaledHeight / 2 - 7 - 4;
        int k = scaledWidth / 2 - 8;

        float attackTimeDuring = getAttackTimeDuring();
        if (powerOn && isBarrageAttacking() && attackTimeDuring > -1) {
            int ClashTime = 15 - Math.round((attackTimeDuring / getBarrageLength()) * 15);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 30, ClashTime, 6);
        } else if (powerOn && isBarrageCharging()) {
            int ClashTime = Math.round((attackTimeDuring / getBarrageWindup()) * 15);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 30, ClashTime, 6);
        } else {
            int barTexture = 0;
            Entity TE = getTargetEntity(playerEntity, 3, getBrawlPunchAngle());
            float attackTimeMax = getAttackTimeMax();
            if (attackTimeMax > 0) {
                float attackTime = getAttackTime();
                float finalATime = attackTime / attackTimeMax;
                if (finalATime <= 1) {

                    if (getActivePowerPhase() == getActivePowerPhaseMax()) {
                        barTexture = 24;
                    } else if (TE != null && isBrawling()) {
                        barTexture = 12;
                    } else {
                        barTexture = 18;
                    }


                    context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
                    int finalATimeInt = Math.round(finalATime * 15);
                    context.blit(StandIcons.JOJO_ICONS, k, j, 193, barTexture, finalATimeInt, 6);

                }
            }
            if (powerOn && isBrawling()) {
                if (TE != null) {
                    if (barTexture == 0) {
                        context.blit(StandIcons.JOJO_ICONS, k, j, 193, 0, 15, 6);
                    }
                }
            }
        }
    }


    @Override
    public void powerActivate(PowerContext context) {
        switch (context) {

            case SKILL_1_NORMAL -> {
                toggleFistsClient();
            }

        }
    }

    @Override
    public boolean setPowerOther(int move, int lastMove) {
        switch (move) {

            case PowerIndex.POWER_1 -> {
                toggleFists();
            }

        }

        return super.setPowerOther(move,lastMove);
    }



    @Override
    public void tickPower() {

        super.tickPower();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag $$0) {
        super.addAdditionalSaveData($$0);
        $$0.putBoolean("fistsOut",fistsOut);
    }
    @Override
    public void readAdditionalSaveData(CompoundTag $$0) {
        super.readAdditionalSaveData($$0);
        if ($$0.contains("fistsOut")) {
            fistsOut = $$0.getBoolean("fistsOut");
        }
    }

    @Override
    public boolean setPowerAttack(){
        setAttack();
        return false;
    }

    @Override
    public boolean setPowerGuard(){
        if (!self.level().isClientSide()) {
            if (getPlayerPos2() != PlayerPosIndex.GUARD) {
                setPlayerPos2(PlayerPosIndex.GUARD);
            }
        }
        return super.setPowerGuard();
    }

    /*
    @Override
    public void updateUniqueMoves(){
        super.updateUniqueMoves();
    }
     */

    @Override
    public void buttonInputAttack(boolean keyIsDown, Options options) {
        if (self instanceof Player pl &&  ((IPlayerEntity)pl).roundabout$getAttackStrengthTicker() < 5) {
            return;
        }
        if (keyIsDown) {
            if (activePowerPhase == 0) {
                if (isBrawling() && !isBarraging()) {
                    if (!isHoldingSneak()) {
                        this.tryPower(PowerIndex.ATTACK);
                    } else if (self.onGround()) {

                    }
                }
            }
        }
    }

    @Override
    public boolean buttonInputGuard(boolean keyIsDown, Options options) {
        if (!this.isGuarding() && this.isBrawling()) {
            this.tryPower(PowerIndex.GUARD, true);
            tryPowerPacket(PowerIndex.GUARD);
            return true;
        }
        return false;
    }

    @Override
    public void buttonInputBarrage(boolean keyIsDown, Options options) {
        if (keyIsDown) {
            if (activePowerPhase == 0 || this.getAttackTime() >= this.getAttackTimeMax()){
                if (isBrawling() && !isBarraging()) {
                    this.tryPower(PowerIndex.BARRAGE_CHARGE, true);
                    tryPowerPacket(PowerIndex.BARRAGE_CHARGE);
                }
            }
        }
    }


    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {

        if (fistsOut) {
            setSkillIcon(context, x, y, 1, StandIcons.SUIT_COMBAT_2, PowerIndex.SKILL_1);
        } else {
            setSkillIcon(context, x, y, 1, StandIcons.SUIT_COMBAT, PowerIndex.SKILL_1);
        }

    }



    @Override
    public Component getSkinName(byte skinId) {
        return Component.translatable("skins.roundabout.oasis."+getSkinString(skinId));
    }

    public static String getSkinString(byte skinId) {
        return switch (skinId)
        {
            default -> "base";
        };
    }

    @Override
    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }

    @Override
    public SoundEvent getSoundFromByte(byte soundChoice) {
        switch(soundChoice) {
            case SoundIndex.SUMMON_SOUND -> {
                return ModSounds.SUMMON_OASIS_EVENT;
            }
        }
        return super.getSoundFromByte(soundChoice);
    }

}
