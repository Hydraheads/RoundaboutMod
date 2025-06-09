package net.hydra.jojomod.stand.powers;

import net.hydra.jojomod.event.powers.StandPowers;
import net.minecraft.client.Options;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.HashMap;

public abstract class StandPowerRewrite extends StandPowers {
    public StandPowerRewrite(LivingEntity self) {
        super(self);
    }

    public static HashMap<ResourceLocation, PowerContext> GUI_ICON_REGISTRAR = new HashMap<>();

    /** Register Power Contexts for GUI such as hud and stuff */
    public abstract void registerPowerContexts();
    public abstract void powerActivate(PowerContext context);
    /** Called per frame, use for particle FX and such */
    public abstract void tick();

    @Override
    public void buttonInput1(boolean keyIsDown, Options options) {
        // really hacky but it works lol
        this.tick();

        if (!getSelf().isCrouching() && !isGuarding())
        {
            powerActivate(PowerContext.SKILL_1_NORMAL);
            return;
        }
        if (getSelf().isCrouching() && !isGuarding())
        {
            powerActivate(PowerContext.SKILL_1_CROUCH);
            return;
        }
        if (!getSelf().isCrouching() && isGuarding())
        {
            powerActivate(PowerContext.SKILL_1_GUARD);
            return;
        }
        if (getSelf().isCrouching() && isGuarding())
        {
            powerActivate(PowerContext.SKILL_1_CROUCH_GUARD);
            return;
        }

        super.buttonInput1(keyIsDown, options);
    }

    @Override
    public void buttonInput2(boolean keyIsDown, Options options) {
        if (!getSelf().isCrouching() && !isGuarding())
        {
            powerActivate(PowerContext.SKILL_2_NORMAL);
            return;
        }
        if (getSelf().isCrouching() && !isGuarding())
        {
            powerActivate(PowerContext.SKILL_2_CROUCH);
            return;
        }
        if (!getSelf().isCrouching() && isGuarding())
        {
            powerActivate(PowerContext.SKILL_2_GUARD);
            return;
        }
        if (getSelf().isCrouching() && isGuarding())
        {
            powerActivate(PowerContext.SKILL_2_CROUCH_GUARD);
            return;
        }

        super.buttonInput2(keyIsDown, options);
    }

    @Override
    public void buttonInput3(boolean keyIsDown, Options options) {
        if (!getSelf().isCrouching() && !isGuarding())
        {
            powerActivate(PowerContext.SKILL_3_NORMAL);
            return;
        }
        if (getSelf().isCrouching() && !isGuarding())
        {
            powerActivate(PowerContext.SKILL_3_CROUCH);
            return;
        }
        if (!getSelf().isCrouching() && isGuarding())
        {
            powerActivate(PowerContext.SKILL_3_GUARD);
            return;
        }
        if (getSelf().isCrouching() && isGuarding())
        {
            powerActivate(PowerContext.SKILL_3_CROUCH_GUARD);
            return;
        }

        super.buttonInput3(keyIsDown, options);
    }

    @Override
    public void buttonInput4(boolean keyIsDown, Options options) {
        if (!getSelf().isCrouching() && !isGuarding())
        {
            powerActivate(PowerContext.SKILL_4_NORMAL);
            return;
        }
        if (getSelf().isCrouching() && !isGuarding())
        {
            powerActivate(PowerContext.SKILL_4_CROUCH);
            return;
        }
        if (!getSelf().isCrouching() && isGuarding())
        {
            powerActivate(PowerContext.SKILL_4_GUARD);
            return;
        }
        if (getSelf().isCrouching() && isGuarding())
        {
            powerActivate(PowerContext.SKILL_4_CROUCH_GUARD);
            return;
        }

        super.buttonInput4(keyIsDown, options);
    }
}
