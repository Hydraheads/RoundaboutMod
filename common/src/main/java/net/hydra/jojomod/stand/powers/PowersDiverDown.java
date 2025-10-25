package net.hydra.jojomod.stand.powers;


import com.google.common.collect.Lists;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.DiverDownEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewPunchingStand;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import java.util.Arrays;
import java.util.List;

public class PowersDiverDown extends NewPunchingStand {
    public void tryToDashClient(){
        if (vaultOrFallBraceFails()){
            dash();
        }
    }
        @Override
        public void powerActivate (PowerContext context){
            switch (context) {
                case SKILL_3_NORMAL ->
                    tryToDashClient();

            }
        }
    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {

        setSkillIcon(context, x, y, 1, StandIcons.LOCKED, PowerIndex.SKILL_1);

        setSkillIcon(context, x, y, 2, StandIcons.LOCKED, PowerIndex.SKILL_2);

        if (isHoldingSneak()){
            setSkillIcon(context, x, y, 3, StandIcons.DIVER_DOWN_ZIP, PowerIndex.SKILL_3);
        } else {
            setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
        }
        if (canVault() ) {
            setSkillIcon(context, x, y, 3, StandIcons.DIVER_DOWN_VAULT, PowerIndex.GLOBAL_DASH);
        }

        setSkillIcon(context, x, y, 4, StandIcons.DIVER_SELECTION, PowerIndex.SKILL_4);
    }

    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypas) {
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        $$1.add(drawSingleGUIIcon(context,18,leftPos+96,topPos+99,0, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE,3,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+115,topPos+99, 0,"ability.roundabout.vault",
                "instruction.roundabout.press_skill_air", StandIcons.DIVER_DOWN_VAULT,3,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+134,topPos+99, 0,"ability.roundabout.diver_zip",
                "instruction.roundabout.press_skill_crouch", StandIcons.DIVER_DOWN_ZIP,3,level,bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 80, 0, "ability.roundabout.diver_selection",
                "instruction.roundabout.press_skill", StandIcons.DIVER_SELECTION,4,level,bypas));
        return $$1;
    }

    @Override public Component getSkinName(byte skinId) {
        switch (skinId)
        {
            case DiverDownEntity.PART_6 -> {return Component.translatable("skins.roundabout.diver_down.base");}
            case DiverDownEntity.LAVA_DIVER -> {return Component.translatable("skins.roundabout.diver_down.lavadiver");}
            case DiverDownEntity.RED_DIVER -> {return Component.translatable("skins.roundabout.diver_down.reddiver");}
            case DiverDownEntity.ORANGE_DIVER -> {return Component.translatable("skins.roundabout.diver_down.orangediver");}
            case DiverDownEntity.TREASURE_DIVER -> {return Component.translatable("skins.roundabout.diver_down.treasurediver");}
            case DiverDownEntity.BIRTHDAY_DIVER -> {return Component.translatable("skins.roundabout.diver_down.birthdaydiver");}
            case DiverDownEntity.FIRE_DIVER -> {return Component.translatable("skins.roundabout.diver_down.firediver");}
            default -> {return Component.translatable("skins.roundabout.diver_down.base");}
        }
    }

    @Override
    public List<Byte> getSkinList() {
        return Arrays.asList(
                DiverDownEntity.PART_6,
                DiverDownEntity.LAVA_DIVER,
                DiverDownEntity.RED_DIVER,
                DiverDownEntity.ORANGE_DIVER,
                DiverDownEntity.TREASURE_DIVER,
                DiverDownEntity.BIRTHDAY_DIVER,
                DiverDownEntity.FIRE_DIVER
        );
    }
        public float standReach = 5;

    public PowersDiverDown(LivingEntity self) {
            super(self);
        }

    @Override
    protected Byte getSummonSound() {return SoundIndex.SUMMON_SOUND;
    }

    @Override
    public SoundEvent getSoundFromByte(byte soundChoice){
        switch (soundChoice)
        {
            case SoundIndex.SUMMON_SOUND -> {
                return ModSounds.SUMMON_DIVER_DOWN_EVENT;
            }
        }
        return super.getSoundFromByte(soundChoice);
    }

        @Override
        public boolean canSummonStand () {
            return true;
        }

        @Override
        public boolean isMiningStand () {
            return true;
        }

        @Override
        public StandPowers generateStandPowers (LivingEntity entity){
            return new PowersDiverDown(entity);
        }

        @Override
        public int getMaxGuardPoints () {
            return 15;
        }



        @Override
        public boolean tryPower ( int move, boolean forced){
            return super.tryPower(move, forced);
        }

        @Override
        public StandEntity getNewStandEntity () {
            return ModEntities.DIVER_DOWN.create(this.getSelf().level());
        }


        @Override
        public boolean isWip () {
            return true;
        }

        @Override
        public Component ifWipListDevStatus () {
            return Component.translatable("roundabout.dev_status.active").withStyle(ChatFormatting.AQUA);
        }
        @Override
        public Component ifWipListDev () {
            return Component.literal("MrInkyTech").withStyle(ChatFormatting.YELLOW);
        }
    }

