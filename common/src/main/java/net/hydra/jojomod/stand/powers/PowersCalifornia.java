package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.npcs.Aesthetician;
import net.hydra.jojomod.entity.projectile.CinderellaVisageDisplayEntity;
import net.hydra.jojomod.entity.stand.CaliforniaKingBedEntity;
import net.hydra.jojomod.entity.stand.CinderellaEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.LuckyLipstickItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PowersCalifornia extends NewDashPreset {

    public PowersCalifornia(LivingEntity self) {
        super(self);
    }


    @Override
    /**Override to add disable config*/
    public boolean isStandEnabled(){
        return ClientNetworking.getAppropriateConfig().cinderellaSettings.enableCinderella;
    }

    @Override
    public StandEntity getNewStandEntity(){
        return ModEntities.CALIFORNIA_KING_BED.create(this.getSelf().level());
    }
    @Override
    public boolean canSummonStand(){
        return true;
    }
    @Override
    public StandPowers generateStandPowers(LivingEntity entity){
        return new PowersCalifornia(entity);
    }

    @Override
    public boolean isSecondaryStand(){
        return true;
    }
    @Override
    public List<Byte> getPosList(){
        List<Byte> $$1 = Lists.newArrayList();
        $$1.add((byte) 0);
        $$1.add((byte) 1);
        return $$1;
    }


    @Override
    public void powerActivate(PowerContext context) {
        switch (context)
        {
            case SKILL_3_NORMAL, SKILL_3_CROUCH -> {
                dash();
            }
        }
    }

    public void doDefaceClient(){
        if (!this.onCooldown(PowerIndex.SKILL_2)) {
            if (this.activePower == PowerIndex.POWER_2) {
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                tryPowerPacket(PowerIndex.NONE);
            } else {
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_2, true);
                tryPowerPacket(PowerIndex.POWER_2);
            }
        }
    }
    @Override
    public void tickPowerEnd() {
        super.tickPowerEnd();
    }


    @Override
    public Component getSkinName(byte skinId) {
        return getSkinNameT(skinId);
    }

    public static Component getSkinNameT(byte skinId){
        if (skinId == CaliforniaKingBedEntity.PART_8_SKIN) {
            return Component.translatable("skins.roundabout.california_king_bed.base");
        }
        return Component.translatable("skins.roundabout.california_king_bed.base");
    }

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        setSkillIcon(context, x, y, 1, StandIcons.CINDERELLA_MASK, PowerIndex.NO_CD);
        setSkillIcon(context, x, y, 2, StandIcons.CINDERELLA_SCALP, PowerIndex.SKILL_2);
        setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
    }

    @Override
    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }
    public SoundEvent getSoundFromByte(byte soundChoice){
        byte bt = ((StandUser)this.getSelf()).roundabout$getStandSkin();
        if (soundChoice == SoundIndex.SUMMON_SOUND) {
            return ModSounds.CINDERELLA_SUMMON_EVENT;
        }
        return super.getSoundFromByte(soundChoice);
    }

    @Override
    public boolean tryPower(int move, boolean forced) {
        return super.tryPower(move,forced);
    }

    public void tickPower() {
        super.tickPower();
    }

    @Override
    public void updateUniqueMoves() {
        super.updateUniqueMoves();
    }
    @Override
    public boolean setPowerOther(int move, int lastMove) {
        return super.setPowerOther(move,lastMove);
    }


    @Override
    public void tickMobAI(LivingEntity attackTarget){
    }
    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypass) {
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 118, 0, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE,3,level,bypass));
       return $$1;
    }
    @Override
    public List<Byte> getSkinList() {
        List<Byte> $$1 = Lists.newArrayList();
        $$1.add(CaliforniaKingBedEntity.PART_8_SKIN);
        return $$1;
    }

    @Override
    public void tickStandRejection(MobEffectInstance effect){
    }
    @Override
    public void renderAttackHud(GuiGraphics context, Player playerEntity,
                                int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                float flashAlpha, float otherFlashAlpha) {
    }


    @Override
    public boolean isWip() {
        return true;
    }

    @Override
    public Component ifWipListDev() {
        return Component.literal("Hydra");
    }

    @Override
    public Component ifWipListDevStatus() {
        return Component.translatable("roundabout.dev_status.active");
    }
}
