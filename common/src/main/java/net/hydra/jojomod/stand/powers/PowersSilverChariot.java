package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewPunchingStand;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class PowersSilverChariot extends NewPunchingStand {
	public PowersSilverChariot(LivingEntity self) {
        super(self);
    }

    @Override
    public StandEntity getNewStandEntity() {
        return ModEntities.SILVER_CHARIOT.create(this.getSelf().level());
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersSilverChariot(entity);
    }

    @Override
    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypas) {
        List<AbilityIconInstance> $$1 = Lists.newArrayList();

        // dodge
        $$1.add(drawSingleGUIIcon(context,18,leftPos+77,topPos+80,0, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE,3,level,bypas));

        return $$1;
    }

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {

        setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
    }

    @Override
    public void powerActivate(PowerContext context) {
        /**Making dash usable on both key presses*/
        switch (context)
        {
            case SKILL_1_NORMAL -> {

            }

            case SKILL_1_CROUCH -> {

            }

            case SKILL_1_GUARD -> {

            }
            case SKILL_2_NORMAL -> {

            }

            case SKILL_2_CROUCH -> {

            }

            case SKILL_2_GUARD -> {

            }
            case SKILL_3_NORMAL -> {
                tryToDashClient();
            }
            case SKILL_3_CROUCH -> {

            }
            case SKILL_4_CROUCH, SKILL_4_CROUCH_GUARD -> {

            }
            case SKILL_4_NORMAL -> {
            }
            case SKILL_4_GUARD -> {

            }

        }
    }

    public void tryToDashClient(){
        // if (!doVault()){
        //    dash();
        // }
        dash();
    }
}