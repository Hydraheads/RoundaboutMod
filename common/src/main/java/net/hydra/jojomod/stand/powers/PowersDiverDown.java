package net.hydra.jojomod.stand.powers;


import com.google.common.collect.Lists;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.DiverDownEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;


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

        if (isHoldingSneak()){
            setSkillIcon(context, x, y, 1, StandIcons.NONE, PowerIndex.NONE);
        } else {
            setSkillIcon(context, x, y, 1, StandIcons.NONE, PowerIndex.NO_CD);

        }

        setSkillIcon(context, x, y, 2, StandIcons.NONE, PowerIndex.SKILL_2);

        if (isHoldingSneak()){
            setSkillIcon(context, x, y, 3, StandIcons.NONE, PowerIndex.NONE);
        } else {
            setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
        }

        setSkillIcon(context, x, y, 4, StandIcons.NONE, PowerIndex.SKILL_4);
    }
    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypas) {
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        $$1.add(drawSingleGUIIcon(context,18,leftPos+96,topPos+99,0, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE,3,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+115,topPos+99, 0,"ability.roundabout.vault",
                "instruction.roundabout.press_skill_air", StandIcons.DIVER_DOWN_VAULT,3,level,bypas));
        return $$1;

    }


        @Override
        public List<Byte> getSkinList () {
            return Arrays.asList(
                    DiverDownEntity.PART_6
            );
        }

        public float standReach = 5;

    public PowersDiverDown(LivingEntity self) {
            super(self);
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
        public void handleStandAttack (Player player, Entity target){
            super.handleStandAttack(player, target);
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

        @Override public Component getSkinName ( byte skinId){
            switch (skinId) {
                case DiverDownEntity.PART_6 -> {
                    return Component.translatable("skins.roundabout.diver_down.base");
                }
            }
            return Component.translatable("skins.roundabout.diver_down.base");
        }
    }

