package net.hydra.jojomod.event.powers.stand;

import com.google.common.collect.Lists;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.JusticeEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.event.powers.stand.presets.DashPreset;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;

import java.util.List;

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
        setSkillIcon(context, x, y, 2, StandIcons.CINDERELLA_SCALP, PowerIndex.SKILL_1);
        setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.SKILL_3_SNEAK);
    }

    public boolean hold1 = false;
    @Override
    public void buttonInput1(boolean keyIsDown, Options options) {
        if (keyIsDown) {
            if (!hold1) {
                hold1 = true;
                if (!this.onCooldown(PowerIndex.SKILL_1_SNEAK)) {
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
    public float getDefaceStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod(((float) ((float) 3* (ClientNetworking.getAppropriateConfig().
                    damageMultipliers.cinderellaAttackOnPlayers*0.01))));
        } else {
            return levelupDamageMod(((float) ((float) 17* (ClientNetworking.getAppropriateConfig().
                    damageMultipliers.cinderellaAttackOnMobs*0.01))));
        }
    }
    public float getDefaceKnockback(){
        return 1.3F;
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
            ModPacketHandler.PACKET_ACCESS.syncSkillCooldownPacket(((ServerPlayer) this.getSelf()), PowerIndex.SKILL_2,  ClientNetworking.getAppropriateConfig().cooldownsInTicks.cinderellaDefaceAttack);
        }
        this.setCooldown(PowerIndex.SKILL_2_SNEAK, ClientNetworking.getAppropriateConfig().cooldownsInTicks.cinderellaDefaceAttack);
        SoundEvent SE;
        float pitch = 1F;
        if (entity != null) {
            SE = ModSounds.PUNCH_1_SOUND_EVENT;
            pitch = 1.2F;
        } else {
            SE = ModSounds.PUNCH_2_SOUND_EVENT;
        }

        if (!this.self.level().isClientSide()) {
            this.self.level().playSound(null, this.self.blockPosition(), SE, SoundSource.PLAYERS, 0.95F, pitch);
        }
    }
}
