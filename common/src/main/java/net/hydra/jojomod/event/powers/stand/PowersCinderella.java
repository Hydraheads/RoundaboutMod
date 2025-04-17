package net.hydra.jojomod.event.powers.stand;

import com.google.common.collect.Lists;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.JusticeEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.stand.TheWorldEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.event.powers.stand.presets.DashPreset;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;

import java.util.List;
import java.util.Objects;

public class PowersCinderella extends DashPreset {

    public PowersCinderella(LivingEntity self) {
        super(self);
    }
    @Override
    public StandEntity getNewStandEntity(){
        return ModEntities.CINDERELLA.create(this.getSelf().level());
    }
    @Override
    public boolean canSummonStand(){
        return true;
    }
    @Override
    public boolean isWip(){
        return true;
    }
    @Override
    public StandPowers generateStandPowers(LivingEntity entity){
        return new PowersCinderella(entity);
    }

    @Override
    public List<Byte> getPosList(){
        List<Byte> $$1 = Lists.newArrayList();
        $$1.add((byte) 0);
        $$1.add((byte) 1);
        $$1.add((byte) 2);
        $$1.add((byte) 3);
        $$1.add((byte) 4);
        return $$1;
    }
    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        setSkillIcon(context, x, y, 1, StandIcons.CINDERELLA_MASK, PowerIndex.NO_CD);
        setSkillIcon(context, x, y, 2, StandIcons.CINDERELLA_SCALP, PowerIndex.SKILL_2);
        setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.SKILL_3_SNEAK);
    }

    @Override
    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }
    public SoundEvent getSoundFromByte(byte soundChoice){
        byte bt = ((StandUser)this.getSelf()).roundabout$getStandSkin();
        if (soundChoice == SoundIndex.SUMMON_SOUND) {
            return ModSounds.CINDERELLA_SUMMON_EVENT;
        } else if (soundChoice == IMPALE_NOISE) {
            return ModSounds.CINDERELLA_ATTACK_EVENT;
        }
        return super.getSoundFromByte(soundChoice);
    }
    @Override
    public boolean tryPower(int move, boolean forced) {
        if (!this.getSelf().level().isClientSide && this.getActivePower() == PowerIndex.POWER_2 && this.attackTimeDuring > -1) {
            this.stopSoundsIfNearby(IMPALE_NOISE, 100,true);
        }
        return super.tryPower(move,forced);
    }
    public boolean hold1 = false;
    @Override
    public void buttonInput2(boolean keyIsDown, Options options) {
        if (keyIsDown) {
            if (!hold1) {
                hold1 = true;
                if (!this.onCooldown(PowerIndex.SKILL_2)) {
                    if (this.activePower == PowerIndex.POWER_2) {
                        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                        ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.NONE);
                    } else {
                        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_2, true);
                        ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.POWER_2);
                    }
                }
            }
        } else {
            hold1 = false;
        }
    }
    @Override
    public void updateUniqueMoves() {
        if (this.getActivePower() == PowerIndex.POWER_2){
            updateDeface();
        }
        super.updateUniqueMoves();
    }
    @Override
    public boolean setPowerOther(int move, int lastMove) {
        if (move == PowerIndex.POWER_2) {
            return this.deface();
        }
        return super.setPowerOther(move,lastMove);
    }
    public static final byte IMPALE_NOISE = 105;
    public boolean deface(){
        StandEntity stand = getStandEntity(this.self);
        if (Objects.nonNull(stand)){
            this.setAttackTimeDuring(0);
            this.setActivePower(PowerIndex.POWER_2);
            playSoundsIfNearby(IMPALE_NOISE, 27, false);
            this.animateStand((byte)81);
            this.poseStand(OffsetIndex.GUARD);

            return true;
        }
        return false;
    }
    public void updateDeface(){
        if (this.attackTimeDuring > -1) {
            if (this.attackTimeDuring > 15) {
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

    public void standImpale(){
        if (this.self instanceof Player){
            if (isPacketPlayer()){
                this.setAttackTimeDuring(-15);
                ModPacketHandler.PACKET_ACCESS.intToServerPacket(getTargetEntityId2(5), PacketDataIndex.INT_STAND_ATTACK);
            }
        } else {
            /*Caps how far out the punch goes*/
            Entity targetEntity = getTargetEntity(this.self,5);
            defaceImpact(targetEntity);
        }
    }
    public float getDefaceStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod(((float) ((float) 3* (ClientNetworking.getAppropriateConfig().
                    damageMultipliers.cinderellaAttackOnPlayers*0.01))));
        } else {
            return levelupDamageMod(((float) ((float) 9* (ClientNetworking.getAppropriateConfig().
                    damageMultipliers.cinderellaAttackOnMobs*0.01))));
        }
    }
    public float getDefaceKnockback(){
        return 0.7F;
    }
    public void handleStandAttack(Player player, Entity target){
        defaceImpact(target);
    }
    public void defaceImpact(Entity entity){
        this.setAttackTimeDuring(-20);
        if (entity != null) {
            float pow;
            float knockbackStrength;
            pow = getDefaceStrength(entity);
            knockbackStrength = getDefaceKnockback();
            if (StandDamageEntityAttack(entity, pow, 0, this.self)) {
                if (entity instanceof LivingEntity LE) {
                    addEXP(5, LE);
                    if (MainUtil.getMobBleed(entity)) {
                        int bleedlevel = ((StandUser)LE).roundabout$getBleedLevel();
                        if (bleedlevel < 0){
                            MainUtil.makeBleed(entity, 0, 200, this.getSelf());
                        } else if (bleedlevel == 1){
                            MainUtil.makeBleed(entity, 1, 200, this.getSelf());
                        } else if (bleedlevel == 2){
                            MainUtil.makeBleed(entity, 2, 200, this.getSelf());
                            MainUtil.makeMobBleed(entity);
                        }
                    }
                }
                this.takeDeterminedKnockback(this.self, entity, knockbackStrength);
            }
        }

        if (this.getSelf() instanceof Player) {
            ModPacketHandler.PACKET_ACCESS.syncSkillCooldownPacket(((ServerPlayer) this.getSelf()), PowerIndex.SKILL_2,  ClientNetworking.getAppropriateConfig().cooldownsInTicks.cinderellaDefaceAttack);
        }
        this.setCooldown(PowerIndex.SKILL_2_SNEAK, ClientNetworking.getAppropriateConfig().cooldownsInTicks.cinderellaDefaceAttack);
        SoundEvent SE;
        float pitch = 1F;
        if (entity != null) {
            SE = ModSounds.PUNCH_3_SOUND_EVENT;
            pitch = 1.2F;
        } else {
            SE = ModSounds.PUNCH_2_SOUND_EVENT;
        }

        if (!this.self.level().isClientSide()) {
            this.self.level().playSound(null, this.self.blockPosition(), SE, SoundSource.PLAYERS, 0.95F, pitch);
        }
    }
    @Override
    public void renderAttackHud(GuiGraphics context, Player playerEntity,
                                int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                float flashAlpha, float otherFlashAlpha) {;
        int j = scaledHeight / 2 - 7 - 4;
        int k = scaledWidth / 2 - 8;
        if (this.getActivePower() == PowerIndex.POWER_1) {
            Entity TE = this.getTargetEntity(playerEntity, 5F);
            if (TE != null) {
                context.blit(StandIcons.JOJO_ICONS, k, j, 193, 0, 15, 6);
            }
        }
    }
}
