package net.hydra.jojomod.stand.powers;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.block.D4CLightBlockEntity;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.D4CCloneEntity;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.D4CEntity;
import net.hydra.jojomod.entity.stand.SoftAndWetEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.InterdimensionalKeyItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.zetalasis.world.DynamicWorld;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PowersHeyYa extends NewDashPreset {
    public PowersHeyYa(LivingEntity self) {
        super(self);
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersHeyYa(entity);
    }


    public boolean dangerYappingOn(){
        return false;
    }
    public boolean canSummonStandAsEntity(){
        return false;
    }
    @Override
    public boolean rendersPlayer(){
        return true;
    }
    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        ClientUtil.fx.roundabout$onGUI(context);

        // code for advanced icons

        if (dangerYappingOn())
            setSkillIcon(context, x, y, 1, StandIcons.DANGER_YAP_DISABLE, PowerIndex.SKILL_1);
        else
            setSkillIcon(context, x, y, 1, StandIcons.DANGER_YAP, PowerIndex.SKILL_1);

        /**It is sneak because all stands share this cooldown and SP/TW
         * shared it between dash and stand leap*/
        setSkillIcon(context, x, y, 2, StandIcons.MINING_YAP, PowerIndex.SKILL_2);
        setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.SKILL_3_SNEAK);
        setSkillIcon(context, x, y, 4, StandIcons.YAP_YAP, PowerIndex.SKILL_4);

        super.renderIcons(context, x, y);
    }
    public void registerHUDIcons() {
        HashSet<GuiIcon> icons = new HashSet<>();

        // code for basic icons: the rest rely on criteria we have to manually implement

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
        }
    }

    @Override
    public boolean tryPower(int move, boolean forced) {
        return super.tryPower(move, forced);
    }

    @Override
    public boolean isAttackIneptVisually(byte activeP, int slot) {
        return super.isAttackIneptVisually(activeP, slot);
    }

    /** if = -1, not melt dodging */
    public int meltDodgeTicks = -1;

    @Override
    public void tickPower() {
        super.tickPower();
    }



    @Override
    public boolean setPowerOther(int move, int lastMove) {

        return super.setPowerOther(move, lastMove);
    }

    @Override
    public void updateIntMove(int in) {

        super.updateIntMove(in);
    }

    @Override
    public void updateUniqueMoves() {
        super.updateUniqueMoves();
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
        return Component.literal(  "Hydra").withStyle(ChatFormatting.YELLOW);
    }

    public static final byte
            MANGA = 1,
            GOTHIC = 2;

    @Override
    public List<Byte> getSkinList() {
        return Arrays.asList(
                MANGA,
                GOTHIC
        );
    }
    @Override public Component getSkinName(byte skinId) {
        return switch (skinId)
        {
            case GOTHIC -> Component.translatable("skins.roundabout.hey_ya.gothic");
            default -> Component.translatable("skins.roundabout.hey_ya.manga");
        };
    }
}