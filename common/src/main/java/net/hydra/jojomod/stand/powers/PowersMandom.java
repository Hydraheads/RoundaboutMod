package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.access.IMob;
import net.hydra.jojomod.block.MiningAlertBlock;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class PowersMandom extends NewDashPreset {
    public PowersMandom(LivingEntity self) {
        super(self);
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersMandom(entity);
    }


    public boolean dangerYapping = false;
    public boolean dangerYappingOn(){
        return dangerYapping;
    }
    public boolean canSummonStandAsEntity(){
        return false;
    }
    @Override
    public boolean rendersPlayer(){
        return true;
    }
    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        // code for advanced icons

        setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);

        super.renderIcons(context, x, y);
    }
    public void registerHUDIcons() {
        HashSet<GuiIcon> icons = new HashSet<>();

        // code for basic icons: the rest rely on criteria we have to manually implement

        GUI_ICON_REGISTRAR = icons;
    }

    @Override
    public void tick() {
    }

    @Override
    public void powerActivate(PowerContext context) {
        /**Making dash usable on both key presses*/
        switch (context)
        {
            case SKILL_3_NORMAL, SKILL_3_CROUCH -> {
                dash();
            }
        }
    }

    @Override
    public boolean setPowerOther(int move, int lastMove) {
        switch (move)
        {
            case PowerIndex.POWER_2 -> {
                return itsRewindTime();
            }
        }
        return super.setPowerOther(move,lastMove);
    }

    public static final byte
            ROTATE = 1;
    /**Let the client brunt the task of mass scanning blocks so it doesn't lag server TPS
     * also instill block limits so the packet count is sane*/
    public boolean itsRewindTime(){
        //this.setCooldown(PowerIndex.SKILL_2,ClientNetworking.getAppropriateConfig().heyYaSettings.oreDetectionCooldown);
        if (isClient()){
        }
        return true;
    }


    int ticksSinceLastYap = 0;
    int maxTicksSinceLastYap = 400;

    @Override
    public boolean tryPower(int move, boolean forced) {
        return super.tryPower(move, forced);
    }

    @Override
    public boolean isAttackIneptVisually(byte activeP, int slot) {
        return super.isAttackIneptVisually(activeP, slot);
    }

    /** if = -1, not melt dodging */
    public int meltDodgeTicks = -1;

    @Override
    public void tickPower() {
        /**Yap animation based on using power*/
        super.tickPower();
    }


    @Override
    public void updateUniqueMoves() {
        super.updateUniqueMoves();
    }



    public static final byte
            MANGA = 1;
    @Override
    public List<Byte> getSkinList() {
        return Arrays.asList(
                MANGA
        );
    }
    @Override public Component getSkinName(byte skinId) {
        return switch (skinId)
        {
            default -> Component.translatable("skins.roundabout.hey_ya.manga");
        };
    }

    @Override
    public boolean isSecondaryStand(){
        return true;
    }
    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }
    @Override
    public SoundEvent getSoundFromByte(byte soundChoice){
        switch (soundChoice)
        {
            case SoundIndex.SUMMON_SOUND -> {
                return ModSounds.HEY_YA_SUMMON_EVENT;
            }
        }
        return super.getSoundFromByte(soundChoice);
    }

    public byte worthinessType(){
        return HUMANOID_WORTHY;
    }
    public Component getPosName(byte posID) {
        if (posID == 1) {
            return Component.translatable("idle.roundabout.hey_ya_2");
        } else if (posID == 2) {
                return Component.translatable("idle.roundabout.mandom_back");
        } else {
            return Component.translatable("idle.roundabout.hey_ya_1");
        }
    }

    @Override
    public List<Byte> getPosList(){
        List<Byte> $$1 = Lists.newArrayList();
        $$1.add((byte) 0);
        $$1.add((byte) 1);
        $$1.add((byte) 2);
        return $$1;
    }
    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypass) {
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 118, 0, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE,3,level,bypass));
        return $$1;
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
        return Component.literal(  "Hydra").withStyle(ChatFormatting.YELLOW);
    }
}