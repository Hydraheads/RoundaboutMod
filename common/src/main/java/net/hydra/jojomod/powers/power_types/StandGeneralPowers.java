package net.hydra.jojomod.powers.power_types;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.powers.GeneralPowers;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector2i;

import java.util.List;

public class StandGeneralPowers extends GeneralPowers {
    public StandGeneralPowers(LivingEntity self) {
        super(self);
    }
    public StandGeneralPowers() {
        super(null);
    }
    public GeneralPowers generatePowers(LivingEntity entity){
        return new StandGeneralPowers(entity);
    }

    /**The text name of the fate*/
    public Component getPowerName(){
        return Component.translatable("text.roundabout.powers.stand");
    }
    public Component getPowerTagName(){
        return Component.translatable("text.roundabout.powers.stand_select");
    }
    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypass) {
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 80, 0, "ability.roundabout.stand_summon",
                "instruction.roundabout.passive", StandIcons.STAND_SUMMON, 1, level, bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 99, 0, "ability.roundabout.stand_vision",
                "instruction.roundabout.passive", StandIcons.STAND_VISION,2,level,bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 118, 0, "ability.roundabout.stand_passives",
                "instruction.roundabout.passive", StandIcons.STAND_PASSIVES,2,level,bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 80, 0, "ability.roundabout.disc_imprint",
                "instruction.roundabout.use_item", StandIcons.DISC_IMPRINT,2,level,bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 99, 0, "ability.roundabout.disc_swap",
                "instruction.roundabout.use_item", StandIcons.DISC_SWAP,2,level,bypass));
        return $$1;
    }

    @Override

    public void drawOtherGUIElements(Font font, GuiGraphics context, float delta, int mouseX, int mouseY, int i, int j, ResourceLocation rl){
        ItemStack disc = ((StandUser)self).roundabout$getStandDisc();
        if (disc != null && !disc.isEmpty()) {
            RenderSystem.enableBlend();
            context.renderItem(disc, i +82, j +40);
            if (isSurelyHovering(i +82, j +40,16,16,mouseX,mouseY)){
                context.renderTooltip(font,disc,mouseX,mouseY);
            }
        }
    }

    @Override
    public Vector2i getCoords(){
        return new Vector2i(0,239);
    }
}
