package net.hydra.jojomod.chocolatedisco.mixin.client;

import com.google.common.collect.Lists;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.fates.powers.AbilityScapeBasis;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.hydra.jojomod.chocolatedisco.PowersChocolateDisco;

import java.util.List;

@Mixin(AbilityScapeBasis.class)
public class ChocolateDiscoStandMenuMixin {

    private static final ResourceLocation CHOCOLATE_DISCO_TELEPORT =
            new ResourceLocation(
                    "roundabout",
                    "textures/gui/icons/chocolate_disco_teleport.png"
            );

    private static final ResourceLocation CHOCOLATE_DISCO_HIGH_TELEPORT =
            new ResourceLocation(
                    "roundabout",
                    "textures/gui/icons/chocolate_disco_highteleport.png"
            );

    private static final ResourceLocation CHOCOLATE_DISCO_SELECT =
            new ResourceLocation(
                    "roundabout",
                    "textures/gui/icons/chocolate_disco_select.png"
            );

    private static final ResourceLocation CHOCOLATE_DISCO_GRID_LOCK =
            new ResourceLocation(
                    "roundabout",
                    "textures/gui/icons/chocolate_disco_gridlock.png"
            );

    private static final ResourceLocation CHOCOLATE_DISCO_GRID_UNLOCK =
            new ResourceLocation(
                    "roundabout",
                    "textures/gui/icons/chocolate_disco_gridunlock.png"
            );

    private static final ResourceLocation CHOCOLATE_DISCO_QUEUE =
            new ResourceLocation(
                    "roundabout",
                    "textures/gui/icons/chocolate_disco_queue.png"
            );

    private static final ResourceLocation CHOCOLATE_DISCO_SELF_DISCO =
            new ResourceLocation(
                    "roundabout",
                    "textures/gui/icons/chocolate_disco_selfdisco.png"
            );

    private static final ResourceLocation CHOCOLATE_DISCO_BUILD =
            new ResourceLocation(
                    "roundabout",
                    "textures/gui/icons/chocolate_disco_build.png"
            );

    private static final ResourceLocation CHOCOLATE_DISCO_PASSIVE =
            new ResourceLocation(
                    "roundabout",
                    "textures/gui/icons/chocolate_disco_passive.png"
            );

    @Inject(
            method = "drawGUIIcons",
            at = @At("RETURN"),
            cancellable = true,
            remap = false
    )
    private void chocolateDisco$addStandMenuIcons(
            GuiGraphics context,
            float delta,
            int mouseX,
            int mouseY,
            int leftPos,
            int topPos,
            byte level,
            boolean bypass,
            CallbackInfoReturnable<List<AbilityIconInstance>> cir
    ) {
        if (!((Object) this instanceof PowersChocolateDisco powers)) {
            return;
        }

        List<AbilityIconInstance> icons = cir.getReturnValue();

        if (icons == null) {
            icons = Lists.newArrayList();
        }

        /*
         * COLUMN 1
         *
         * Passive = DISCO
         * Skill 1 = Teleport
         * Skill 1 crouching = High Teleport
         */

        icons.add(powers.drawSingleGUIIcon(
                context,
                18,
                leftPos + 20,
                topPos + 80,
                0,
                "ability.roundabout.chocolate_disco_disco",
                "instruction.roundabout.passive",
                CHOCOLATE_DISCO_PASSIVE,
                0,
                level,
                bypass
        ));

        icons.add(powers.drawSingleGUIIcon(
                context,
                18,
                leftPos + 20,
                topPos + 99,
                0,
                "ability.roundabout.chocolate_disco_teleport",
                "instruction.roundabout.press_skill",
                CHOCOLATE_DISCO_TELEPORT,
                1,
                level,
                bypass
        ));

        icons.add(powers.drawSingleGUIIcon(
                context,
                18,
                leftPos + 20,
                topPos + 118,
                0,
                "ability.roundabout.chocolate_disco_highteleport",
                "instruction.roundabout.press_skill_crouch",
                CHOCOLATE_DISCO_HIGH_TELEPORT,
                1,
                level,
                bypass
        ));

        /*
         * COLUMN 2
         *
         * Skill 2 = Select
         * Skill 2 crouching = Build
         * Skill 3 = Dash
         */

        icons.add(powers.drawSingleGUIIcon(
                context,
                18,
                leftPos + 39,
                topPos + 80,
                0,
                "ability.roundabout.chocolate_disco_select",
                "instruction.roundabout.press_skill",
                CHOCOLATE_DISCO_SELECT,
                2,
                level,
                bypass
        ));

        icons.add(powers.drawSingleGUIIcon(
                context,
                18,
                leftPos + 39,
                topPos + 99,
                0,
                "ability.roundabout.chocolate_disco_build",
                "instruction.roundabout.press_skill_crouch",
                CHOCOLATE_DISCO_BUILD,
                2,
                level,
                bypass
        ));

        icons.add(powers.drawSingleGUIIcon(
                context,
                18,
                leftPos + 39,
                topPos + 118,
                0,
                "ability.roundabout.dodge",
                "instruction.roundabout.press_skill",
                StandIcons.DODGE,
                3,
                level,
                bypass
        ));

        /*
         * COLUMN 3
         *
         * Skill 3 crouching = Grid Lock
         * Skill 4 = Queue
         * Skill 4 crouching = Self Disco
         */

        icons.add(powers.drawSingleGUIIcon(
                context,
                18,
                leftPos + 58,
                topPos + 80,
                0,
                "ability.roundabout.chocolate_disco_gridlock",
                "instruction.roundabout.press_skill_crouch",
                CHOCOLATE_DISCO_GRID_LOCK,
                3,
                level,
                bypass
        ));

        icons.add(powers.drawSingleGUIIcon(
                context,
                18,
                leftPos + 58,
                topPos + 99,
                0,
                "ability.roundabout.chocolate_disco_queue",
                "instruction.roundabout.press_skill",
                CHOCOLATE_DISCO_QUEUE,
                4,
                level,
                bypass
        ));

        icons.add(powers.drawSingleGUIIcon(
                context,
                18,
                leftPos + 58,
                topPos + 118,
                0,
                "ability.roundabout.chocolate_disco_selfdisco",
                "instruction.roundabout.press_skill_crouch",
                CHOCOLATE_DISCO_SELF_DISCO,
                4,
                level,
                bypass
        ));

        cir.setReturnValue(icons);
    }
}