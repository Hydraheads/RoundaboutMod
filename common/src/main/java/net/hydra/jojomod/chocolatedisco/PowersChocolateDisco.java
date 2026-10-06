package net.hydra.jojomod.chocolatedisco;

import com.google.common.collect.Lists;

import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.hydra.jojomod.chocolatedisco.network.ChocolateDiscoNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class PowersChocolateDisco extends NewDashPreset {

    public PowersChocolateDisco(LivingEntity self) {
        super(self);
    }

    @Override
    public boolean isWip() {
        return true;
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersChocolateDisco(entity);
    }

    @Override
    public boolean isSecondaryStand() {
        return true;
    }

    /*
     * Shown in the WIP tab's info panel as "Developer: ...".
     * Matches the plain Component.literal(...) convention
     * every other stand in the mod uses here.
     */
    @Override
    public Component ifWipListDev() {
        return Component.literal("Ladfromearth");
    }

    /*
     * Shown in the WIP tab's info panel as "Dev Status: ...".
     * "roundabout.dev_status.active" / ChatFormatting.AQUA is
     * the standard "actively being worked on" status used by
     * most other stands in the mod (the alternatives are
     * roundabout.dev_status.paused and
     * roundabout.dev_status.dropped).
     */
    @Override
    public Component ifWipListDevStatus() {
        return Component.translatable("roundabout.dev_status.active")
                .withStyle(ChatFormatting.AQUA);
    }

    @Override
    public boolean isServerControlledCooldown(byte num) {
        return num == PowerIndex.SKILL_4_SNEAK
                || super.isServerControlledCooldown(num);
    }

    @Override
    public StandEntity getNewStandEntity() {
        net.minecraft.world.entity.Entity entity =
                BuiltInRegistries.ENTITY_TYPE.get(new ResourceLocation("roundabout", "chocolate_disco"))
                        .create(this.getSelf().level());
        return entity instanceof StandEntity stand ? stand : null;
    }

    @Override
    public boolean canSummonStand() {
        return true;
    }

    private boolean standWasActive = false;

    @Override
    public void onStandSummon(boolean value) {
        super.onStandSummon(value);

        boolean nowActive = !value;
        boolean justBecameActive = nowActive && !this.standWasActive;
        this.standWasActive = nowActive;

        if (!justBecameActive) {
            return;
        }

        if (this.getSelf() instanceof ServerPlayer serverPlayer) {
            ChocolateDiscoNetworking.playDiscoSummonSound(serverPlayer);
        }
    }

    @Override
    public Component getStandName() {
        return Component.literal("Chocolate Disco");
    }

    @Override
    public Component getPosName(byte posID) {
        return Component.empty();
    }

    @Override
    public List<Byte> getPosList() {
        return Lists.newArrayList();
    }

    @Override
    public int getDisplayPowerInventoryYOffset() {
        return -10;
    }

    @Override
    public boolean rendersPlayer() {

        /*
         * In the library's PowerInventoryScreen.renderBg(), this
         * is an either/or switch: true means "render ONLY the
         * player", and the stand is skipped entirely - there is
         * no built-in "render both" mode. Keeping this false
         * always means the library always takes its normal
         * stand-rendering branch (which is what makes the
         * not-summoned "stand alone" preview work); the player
         * is then added alongside it, once actually summoned, by
         * ChocolateDiscoStandMenuPreviewMixin.
         */
        return false;
    }

    @Override
    public List<Byte> getSkinList() {
        return Lists.newArrayList(
                (byte) 0,
                (byte) 1,
                (byte) 2,
                (byte) 3,
                (byte) 4,
                (byte) 5,
                (byte) 6,
                (byte) 7,
                (byte) 8
        );
    }

    @Override
    public Component getSkinName(byte skinId) {
        if (skinId == 0) {
            return Component.literal("Default");
        }

        if (skinId == 1) {
            return Component.literal("Black & White");
        }

        if (skinId == 2) {
            return Component.literal("P-Cubed");
        }

        if (skinId == 3) {
            return Component.literal("Battleship");
        }

        if (skinId == 4) {
            return Component.literal("Disco Ball");
        }

        if (skinId == 5) {
            return Component.literal("Checkers");
        }

        if (skinId == 6) {
            return Component.literal("Tablet");
        }

        if (skinId == 7) {
            return Component.literal("Neopolitan");
        }

        if (skinId == 8) {
            return Component.literal("Chocolate");
        }

        return Component.empty();
    }


    @Override
    public void powerActivate(PowerContext context) {

        if (context == PowerContext.SKILL_1_NORMAL
                || context == PowerContext.SKILL_1_CROUCH
                || context == PowerContext.SKILL_4_CROUCH) {

            this.getSelf().stopUsingItem();
        }

        if (context == PowerContext.SKILL_1_NORMAL
                || context == PowerContext.SKILL_1_CROUCH) {

            System.out.println("CHOCOLATE DISCO ABILITY 1 PRESSED");

            animateStand((byte) 1);
        }

        super.powerActivate(context);
    }
}