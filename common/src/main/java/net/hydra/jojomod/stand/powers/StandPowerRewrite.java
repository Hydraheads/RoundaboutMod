package net.hydra.jojomod.stand.powers;

import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class StandPowerRewrite extends StandPowers {
    public StandPowerRewrite(LivingEntity self) {
        super(self);

        registerHUDIcons();
    }

    public static HashSet<GuiIcon> GUI_ICON_REGISTRAR = new HashSet<>();

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        super.renderIcons(context, x, y);
        GUI_ICON_REGISTRAR.forEach((icon) -> {
            switch (icon.index)
            {
                case PowerIndex.SKILL_1 -> {
                    if (!getSelf().isCrouching() && !isGuarding())
                        setSkillIcon(context, x, y, 1, icon.iconLocation, icon.index);
                }
                case PowerIndex.SKILL_1_SNEAK -> {
                    if (getSelf().isCrouching() && !isGuarding())
                        setSkillIcon(context, x, y, 1, icon.iconLocation, icon.index);
                }
                case PowerIndex.SKILL_1_GUARD -> {
                    if (!getSelf().isCrouching() && isGuarding())
                        setSkillIcon(context, x, y, 1, icon.iconLocation, icon.index);
                }
                case PowerIndex.SKILL_1_CROUCH_GUARD -> {
                    if (getSelf().isCrouching() && isGuarding())
                        setSkillIcon(context, x, y, 1, icon.iconLocation, icon.index);
                }

                case PowerIndex.SKILL_2 -> {
                    if (!getSelf().isCrouching() && !isGuarding())
                        setSkillIcon(context, x, y, 2, icon.iconLocation, icon.index);
                }
                case PowerIndex.SKILL_2_SNEAK -> {
                    if (getSelf().isCrouching() && !isGuarding())
                        setSkillIcon(context, x, y, 2, icon.iconLocation, icon.index);
                }
                case PowerIndex.SKILL_2_GUARD -> {
                    if (!getSelf().isCrouching() && isGuarding())
                        setSkillIcon(context, x, y, 2, icon.iconLocation, icon.index);
                }
                case PowerIndex.SKILL_2_CROUCH_GUARD -> {
                    if (getSelf().isCrouching() && isGuarding())
                        setSkillIcon(context, x, y, 2, icon.iconLocation, icon.index);
                }

                case PowerIndex.SKILL_3 -> {
                    if (!getSelf().isCrouching() && !isGuarding())
                        setSkillIcon(context, x, y, 3, icon.iconLocation, icon.index);
                }
                case PowerIndex.SKILL_3_SNEAK -> {
                    if (getSelf().isCrouching() && !isGuarding())
                        setSkillIcon(context, x, y, 3, icon.iconLocation, icon.index);
                }
                case PowerIndex.SKILL_3_GUARD -> {
                    if (!getSelf().isCrouching() && isGuarding())
                        setSkillIcon(context, x, y, 3, icon.iconLocation, icon.index);
                }
                case PowerIndex.SKILL_3_CROUCH_GUARD -> {
                    if (getSelf().isCrouching() && isGuarding())
                        setSkillIcon(context, x, y, 3, icon.iconLocation, icon.index);
                }

                case PowerIndex.SKILL_4 -> {
                    if (!getSelf().isCrouching() && !isGuarding())
                        setSkillIcon(context, x, y, 4, icon.iconLocation, icon.index);
                }
                case PowerIndex.SKILL_4_SNEAK -> {
                    if (getSelf().isCrouching() && !isGuarding())
                        setSkillIcon(context, x, y, 4, icon.iconLocation, icon.index);
                }
                case PowerIndex.SKILL_4_GUARD -> {
                    if (!getSelf().isCrouching() && isGuarding())
                        setSkillIcon(context, x, y, 4, icon.iconLocation, icon.index);
                }
                case PowerIndex.SKILL_4_CROUCH_GUARD -> {
                    if (getSelf().isCrouching() && isGuarding())
                        setSkillIcon(context, x, y, 4, icon.iconLocation, icon.index);
                }
            }
        });
    }

    /** Register Power Contexts for GUI such as hud and stuff */
    public void registerHUDIcons() { return; };
    public void powerActivate(PowerContext context) {};
    /** Called per frame, use for particle FX and such */
    public void tick() {};

    private boolean held1 = false;
    private boolean held2 = false;
    private boolean held3 = false;
    private boolean held4 = false;

    @Override
    public void buttonInput1(boolean keyIsDown, Options options) {
        // really hacky but it works lol
        this.tick();

        if (keyIsDown)
        {
            if (held1)
                return;
            held1 = true;

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
        }
        else
        {
            held4 = false;
        }

        super.buttonInput4(keyIsDown, options);
    }
}
