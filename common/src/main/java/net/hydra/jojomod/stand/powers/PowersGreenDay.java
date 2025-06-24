package net.hydra.jojomod.stand.powers;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;

import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandPowers;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.mixin.StandUserEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;

import net.minecraft.network.chat.Component;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class PowersGreenDay extends NewPunchingStand{
    public PowersGreenDay(LivingEntity self) {super(self);}

    public boolean tryPower(int move, boolean forced) {
        return super.tryPower(move, forced);
    }

    @Override
    public int getMaxGuardPoints(){
        return ClientNetworking.getAppropriateConfig().guardPoints.d4cDefend;
    }

    @Override
    public StandEntity getNewStandEntity() {
        return ModEntities.GREEN_DAY.create(this.getSelf().level());
    }



    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersGreenDay(entity);
    }

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        ClientUtil.fx.roundabout$onGUI(context);

        super.renderIcons(context, x, y);
    }

    @Override
    public void registerHUDIcons() {
        HashSet<GuiIcon> icons = new HashSet<>();
        icons.add(new GuiIcon(PowerIndex.SKILL_1, StandIcons.GREEN_DAY_MOLD_PUNCH_LEFT));
        icons.add(new GuiIcon(PowerIndex.SKILL_1_GUARD, StandIcons.GREEN_DAY_MOLD_SPIN_LEFT));
        icons.add(new GuiIcon(PowerIndex.SKILL_1_CROUCH_GUARD, StandIcons.GREEN_DAY_ARM_RETURN_LEFT));
        icons.add(new GuiIcon(PowerIndex.SKILL_1_SNEAK, StandIcons.GREEN_DAY_ARM_RETURN_LEFT));

        // code for basic icons: the rest rely on criteria we have to manually implement
        icons.add(new GuiIcon(PowerIndex.SKILL_2, StandIcons.GREEN_DAY_MOLD_PUNCH_RIGHT));
        icons.add(new GuiIcon(PowerIndex.SKILL_2_GUARD, StandIcons.GREEN_DAY_MOLD_SPIN_RIGHT));
        icons.add(new GuiIcon(PowerIndex.SKILL_2_CROUCH_GUARD, StandIcons.GREEN_DAY_ARM_RETURN_RIGHT));
        icons.add(new GuiIcon(PowerIndex.SKILL_2_SNEAK, StandIcons.GREEN_DAY_ARM_RETURN_RIGHT));

        icons.add(new GuiIcon(PowerIndex.SKILL_3, StandIcons.DODGE));
        icons.add(new GuiIcon(PowerIndex.SKILL_3_GUARD, StandIcons.DODGE));
        icons.add(new GuiIcon(PowerIndex.SKILL_3_CROUCH_GUARD, StandIcons.GREEN_DAY_MOLD_LEAP));
        icons.add(new GuiIcon(PowerIndex.GLOBAL_DASH, StandIcons.GREEN_DAY_MOLD_LEAP));

        icons.add(new GuiIcon(PowerIndex.SKILL_4, StandIcons.GREEN_DAY_MOLD_FIELD));
        icons.add(new GuiIcon(PowerIndex.SKILL_4_GUARD, StandIcons.GREEN_DAY_MOLD_FIELD));
        icons.add(new GuiIcon(PowerIndex.SKILL_4_CROUCH_GUARD, StandIcons.GREEN_DAY_STITCH));
        icons.add(new GuiIcon(PowerIndex.SKILL_4_SNEAK, StandIcons.GREEN_DAY_STITCH));

        GUI_ICON_REGISTRAR = icons;
    }

    @Override
    public void tick() {
    }

    @Override
    public void powerActivate(PowerContext context) {
        /**Making dash usable on both key presses*/
        switch (context)
        {
            case SKILL_3_NORMAL, SKILL_3_CROUCH -> {
                Roundabout.LOGGER.info("dash");
                dash();
            }

            case SKILL_4_CROUCH, SKILL_4_CROUCH_GUARD -> {
                if (this.onCooldown(PowerIndex.SKILL_4_SNEAK))
                    return;
                Roundabout.LOGGER.info("Stitch");
                ((StandUser) this.getSelf()).roundabout$Stitch(1.0f);
                this.tryPower(PowerIndex.POWER_4, true);
                tryPowerPacket(PowerIndex.POWER_4);
                this.setCooldown(PowerIndex.SKILL_4_SNEAK, 80);
                this.setCooldown(PowerIndex.SKILL_4_CROUCH_GUARD, 80);

            }
        }
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
        return Component.literal(  "Fish").withStyle(ChatFormatting.YELLOW);
    }

    public static final byte
            PART_FOUR = 1;


    @Override
    public List<Byte> getSkinList() {
        return Arrays.asList(
                PART_FOUR

        );
    }



}
