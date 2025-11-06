package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;

import java.util.Arrays;
import java.util.List;

/** basic af right now (if you see this don't spoil the fun :P ) */
public class PowersAnubis extends NewDashPreset {
    public PowersAnubis(LivingEntity self) {
        super(self);
    }


    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersAnubis(entity);
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
        setSkillIcon(context, x, y, 1, StandIcons.NONE, PowerIndex.SKILL_1);
        setSkillIcon(context, x, y, 2, StandIcons.NONE, PowerIndex.SKILL_2);
        setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
        setSkillIcon(context, x, y, 4, StandIcons.NONE, PowerIndex.SKILL_4);

        super.renderIcons(context, x, y);
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
        switch (move) {
        }
        return super.setPowerOther(move, lastMove);
    }



    @Override
    public void tickPower() {
       /*  if (!isClient()) {
            if (isYapping()) {
                getStandUserSelf().roundabout$setStandAnimation(YAP);
            } else {
                getStandUserSelf().roundabout$setStandAnimation(NONE);
            }
        } */
        super.tickPower();
    }

    @Override
    public boolean interceptAttack(){
        return true;
    }
    @Override
    public boolean interceptGuard(){
        return true;
    }


    public Component getPosName(byte posID){
        return Component.empty();
    }
    public List<Byte> getPosList(){
        List<Byte> $$1 = Lists.newArrayList();
        return $$1;
    }

    public static final byte
            ANIME = 1;

    @Override
    public List<Byte> getSkinList() {
        return Arrays.asList(
                ANIME
        );
    }

    @Override public Component getSkinName(byte skinId) {
        return switch (skinId)
        {
            default -> Component.translatable("skins.roundabout.anubis.anime");
        };
    }

    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }

    public byte worthinessType(){
        return HUMANOID_WORTHY;
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
        return Component.literal(  "Prisma").withStyle(ChatFormatting.YELLOW);
    }

}