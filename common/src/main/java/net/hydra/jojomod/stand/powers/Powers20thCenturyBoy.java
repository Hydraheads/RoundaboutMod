package net.hydra.jojomod.stand.powers;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;

import java.util.Arrays;
import java.util.List;

/*
things to do before release:
take him off wip tab

 */
public class Powers20thCenturyBoy extends NewDashPreset {
    public Powers20thCenturyBoy(LivingEntity self){super(self);}

    public boolean invincibleState = false;
    public int mode = 1;

    /** general definition stuff **/
    @Override
    public boolean isSecondaryStand() {
        return true;
    }

    @Override
    public boolean canSummonStandAsEntity() {
        return false;
    }

    @Override
    public boolean rendersPlayer() {
        return true;
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new Powers20thCenturyBoy(entity);
    }

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        switch (mode) {
            case 1 ->{
                setSkillIcon(context, x, y, 1, StandIcons.GROUND_IMPLANT_OUT, PowerIndex.SKILL_1);
            }
            case 2 ->{
                setSkillIcon(context, x, y, 1, StandIcons.WATER_SHIELD, PowerIndex.SKILL_1);
            }
            case 3 ->{
                setSkillIcon(context, x, y, 1, StandIcons.RED_LASH, PowerIndex.SKILL_1);
            }
            case 4 ->{
                setSkillIcon(context, x, y, 1, StandIcons.RATT_LEAP, PowerIndex.SKILL_1);
            }
        }
        setSkillIcon(context,x,y,2, StandIcons.MUSCLE, PowerIndex.SKILL_2);
        setSkillIcon(context,x,y,3,StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
        super.renderIcons(context, x, y);
    }

    /** wip stuffz **/
    public boolean isWip(){return true;}
    public Component ifWipListDevStatus(){
        return Component.translatable(  "roundabout.dev_status.active").withStyle(ChatFormatting.AQUA);
    }
    public Component ifWipListDev(){
        return Component.literal(  "Waiter").withStyle(ChatFormatting.YELLOW);
    }

    @Override
    public boolean isStandEnabled() {return ClientNetworking.getAppropriateConfig().centuryBoySettings.enableCenturyBoy;}

    /** skins stuff **/
    public static final byte
        MANGA = 1,
        BEDROCK = 2,
        BEE = 3,
        BIKER = 4,
        RETRO = 5,
        GOLD = 6,
        OLD_CENTURY_BOY = 7,
        SHULKER = 8;
    @Override
    public List<Byte> getSkinList() {
        return Arrays.asList(
                MANGA,
                BEDROCK,
                BEE,
                BIKER,
                RETRO,
                GOLD,
                OLD_CENTURY_BOY,
                SHULKER

        );
    }

    @Override
    public Component getSkinName(byte skinId) {
        return switch (skinId){
            case Powers20thCenturyBoy.BEDROCK -> Component.translatable("skins.roundabout.20_centuryboy.bedrock");
            case Powers20thCenturyBoy.BEE -> Component.translatable("skins.roundabout.20_centuryboy.bee");
            case Powers20thCenturyBoy.BIKER -> Component.translatable("skins.roundabout.20_centuryboy.biker");
            case Powers20thCenturyBoy.RETRO -> Component.translatable("skins.roundabout.20_centuryboy.retro");
            case Powers20thCenturyBoy.GOLD -> Component.translatable("skins.roundabout.20_centuryboy.gold");
            case Powers20thCenturyBoy.OLD_CENTURY_BOY -> Component.translatable("skins.roundabout.20_centuryboy.19th_century_boy");
            case Powers20thCenturyBoy.SHULKER -> Component.translatable("skins.roundabout.20_centuryboy.shulker");
            default -> Component.translatable("skins.roundabout.20_centuryboy.manga");
        };
    }

    /** now to the fun stuff**/


    @Override
    public boolean setPowerOther(int move, int lastMove) {
        switch (move){
            case PowerIndex.POWER_1, PowerIndex.POWER_1_SNEAK -> {
                switchMode();
            }
            case PowerIndex.POWER_2, PowerIndex.POWER_2_SNEAK -> {
                toggleInvincibility();
            }
        }

        return super.setPowerOther(move, lastMove);
    }

    @Override
    public void powerActivate(PowerContext context) {
        this.tryPower(PowerIndex.POWER_1, true);
            tryPowerPacket(PowerIndex.POWER_1);

        if (!this.onCooldown(PowerIndex.SKILL_2)) {
            this.tryPower(PowerIndex.POWER_2, true);
            tryPowerPacket(PowerIndex.POWER_2);
        }

    }


    public void switchMode(){
        if (mode < 4){
            mode += 1;
        } else{
            mode = 1;
        }
    }

    public void toggleInvincibility(){

        if (!invincibleState) {
            invincibleState = true;
            ClientUtil.stopDestroyingBlock();
            switch (mode) {
                case 1 -> {
                    Roundabout.LOGGER.info("i'll cook something later guys don't worry1");
                }
                case 2 -> {

                }
                case 3 -> {
                    Roundabout.LOGGER.info("i'll cook something later guys don't worry3");
                }
                case 4 -> {
                    Roundabout.LOGGER.info("i'll cook something later guys don't worry4");
                }
            }
        } else {
            invincibleState = false;
        }
    }



    @Override
    public boolean interceptDamageEvent(DamageSource damageSource, float amount) {

        if(invincibleState){
            Roundabout.LOGGER.info("aAAAAAAAAA");
            /** ps: don't forget to put TA4 shot when it gets added **/
            if (damageSource.is(DamageTypes.FELL_OUT_OF_WORLD) ||
                    damageSource.is(DamageTypes.WITHER) ||
                    damageSource.is(DamageTypes.DRAGON_BREATH) ||
                    damageSource.is(ModDamageTypes.GO_BEYOND)
            ) {
                return false;
            } else {
                return true;
            }
        } else {
            return super.interceptDamageEvent(damageSource, amount);
        }
    }
}
