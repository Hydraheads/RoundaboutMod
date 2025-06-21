package net.hydra.jojomod.stand.powers;

import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;

import java.util.*;

public class PowersHeyYa extends NewDashPreset {
    public PowersHeyYa(LivingEntity self) {
        super(self);
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersHeyYa(entity);
    }


    public boolean dangerYappingOn(){
        return false;
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
        ClientUtil.fx.roundabout$onGUI(context);

        // code for advanced icons

        if (dangerYappingOn())
            setSkillIcon(context, x, y, 1, StandIcons.DANGER_YAP_DISABLE, PowerIndex.SKILL_1);
        else
            setSkillIcon(context, x, y, 1, StandIcons.DANGER_YAP, PowerIndex.SKILL_1);

        /**It is sneak because all stands share this cooldown and SP/TW
         * shared it between dash and stand leap*/
        setSkillIcon(context, x, y, 2, StandIcons.MINING_YAP, PowerIndex.SKILL_2);
        setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.SKILL_3_SNEAK);
        setSkillIcon(context, x, y, 4, StandIcons.YAP_YAP, PowerIndex.SKILL_4);

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
            case SKILL_1_NORMAL, SKILL_1_CROUCH -> {
                toggleDangerYapClient();
            }
            case SKILL_2_NORMAL, SKILL_2_CROUCH -> {
                miningYapClient();
            }
            case SKILL_3_NORMAL, SKILL_3_CROUCH -> {
                dash();
            }
            case SKILL_4_NORMAL, SKILL_4_CROUCH -> {
                yapClient();
            }
        }
    }

    public void toggleDangerYapClient(){
    }
    public void miningYapClient(){

    }
    public void yapClient(){
        if (!this.onCooldown(PowerIndex.SKILL_4)) {
            this.tryPower(PowerIndex.POWER_4, true);
            tryPowerPacket(PowerIndex.POWER_4);
        }
    }

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
        super.tickPower();
    }



    @Override
    public boolean setPowerOther(int move, int lastMove) {

        return super.setPowerOther(move, lastMove);
    }

    @Override
    public void updateIntMove(int in) {

        super.updateIntMove(in);
    }

    @Override
    public void updateUniqueMoves() {
        super.updateUniqueMoves();
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

    public static final byte
            MANGA = 1,
            GOTHIC = 2,
            VOLUME_2 = 3,
            CHAPTER_24 = 4;

    @Override
    public List<Byte> getSkinList() {
        return Arrays.asList(
                MANGA,
                GOTHIC,
                VOLUME_2,
                CHAPTER_24
        );
    }
    @Override public Component getSkinName(byte skinId) {
        return switch (skinId)
        {
            case GOTHIC -> Component.translatable("skins.roundabout.hey_ya.gothic");
            case VOLUME_2 -> Component.translatable("skins.roundabout.hey_ya.volume_2");
            case CHAPTER_24 -> Component.translatable("skins.roundabout.hey_ya.chapter_24");
            default -> Component.translatable("skins.roundabout.hey_ya.manga");
        };
    }

    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }
    @Override
    public SoundEvent getSoundFromByte(byte soundChoice){
        if (soundChoice == SoundIndex.SUMMON_SOUND) {
            return ModSounds.HEY_YA_SUMMON_EVENT;
        }
        return super.getSoundFromByte(soundChoice);
    }
}