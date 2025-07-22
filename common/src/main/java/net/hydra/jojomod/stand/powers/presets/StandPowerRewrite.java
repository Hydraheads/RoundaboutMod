package net.hydra.jojomod.stand.powers.presets;

import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.minecraft.client.Options;
import net.minecraft.world.entity.LivingEntity;

public class StandPowerRewrite extends StandPowers {
    public StandPowerRewrite(LivingEntity self) {
        super(self);
    }


    public void powerActivate(PowerContext context) {};

    private boolean held1 = false;
    private boolean held2 = false;
    private boolean held3 = false;
    private boolean held4 = false;

    @Override
    public void buttonInput1(boolean keyIsDown, Options options) {
        if (keyIsDown)
        {
            if (held1)
                return;
            held1 = true;

            if (!isHoldingSneak() && !isGuarding())
            {
                powerActivate(PowerContext.SKILL_1_NORMAL);
                return;
            }
            if (isHoldingSneak() && !isGuarding())
            {
                powerActivate(PowerContext.SKILL_1_CROUCH);
                return;
            }
            if (!isHoldingSneak() && isGuarding())
            {
                powerActivate(PowerContext.SKILL_1_GUARD);
                return;
            }
            if (isHoldingSneak() && isGuarding())
            {
                powerActivate(PowerContext.SKILL_1_CROUCH_GUARD);
                return;
            }
        }
        else
        {
            held1 = false;
        }

        super.buttonInput1(keyIsDown, options);
    }

    @Override
    public void buttonInput2(boolean keyIsDown, Options options) {
        if (keyIsDown)
        {
            if (held2)
                return;
            held2 = true;

            if (!isHoldingSneak() && !isGuarding())
            {
                powerActivate(PowerContext.SKILL_2_NORMAL);
                return;
            }
            if (isHoldingSneak() && !isGuarding())
            {
                powerActivate(PowerContext.SKILL_2_CROUCH);
                return;
            }
            if (!isHoldingSneak() && isGuarding())
            {
                powerActivate(PowerContext.SKILL_2_GUARD);
                return;
            }
            if (isHoldingSneak() && isGuarding())
            {
                powerActivate(PowerContext.SKILL_2_CROUCH_GUARD);
                return;
            }
        }
        else
        {
            held2 = false;
        }

        super.buttonInput2(keyIsDown, options);
    }

    @Override
    public void buttonInput3(boolean keyIsDown, Options options) {
        if (keyIsDown)
        {
            if (held3)
                return;
            held3 = true;

            if (!isHoldingSneak() && !isGuarding())
            {
                powerActivate(PowerContext.SKILL_3_NORMAL);
                return;
            }
            if (isHoldingSneak() && !isGuarding())
            {
                powerActivate(PowerContext.SKILL_3_CROUCH);
                return;
            }
            if (!isHoldingSneak() && isGuarding())
            {
                powerActivate(PowerContext.SKILL_3_GUARD);
                return;
            }
            if (isHoldingSneak() && isGuarding())
            {
                powerActivate(PowerContext.SKILL_3_CROUCH_GUARD);
                return;
            }
        }
        else
        {
            held3 = false;
        }

        super.buttonInput3(keyIsDown, options);
    }

    @Override
    public void buttonInput4(boolean keyIsDown, Options options) {
        if (keyIsDown)
        {
            if (held4)
                return;
            held4 = true;

            if (!isHoldingSneak() && !isGuarding())
            {
                powerActivate(PowerContext.SKILL_4_NORMAL);
                return;
            }
            if (isHoldingSneak() && !isGuarding())
            {
                powerActivate(PowerContext.SKILL_4_CROUCH);
                return;
            }
            if (!isHoldingSneak() && isGuarding())
            {
                powerActivate(PowerContext.SKILL_4_GUARD);
                return;
            }
            if (isHoldingSneak() && isGuarding())
            {
                powerActivate(PowerContext.SKILL_4_CROUCH_GUARD);
                return;
            }
        }
        else
        {
            held4 = false;
        }

        super.buttonInput4(keyIsDown, options);
    }
}
