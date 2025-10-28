package net.hydra.jojomod.fates.powers;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.KeyInputRegistry;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.CooldownInstance;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class AbilityScapeBasis {
    /**Note that self refers to the entity with the power*/
    public final LivingEntity self;

    public AbilityScapeBasis(LivingEntity self) {
        this.self = self;
    }
    /**If the cooldown slot is to be controlled by the server, return true. Consider using this if
     * bad TPS makes a stand ability actually overpowered for the client to handle the recharging of.*/
    public boolean isServerControlledCooldown(CooldownInstance ci, byte num){
        return false;
    }
    /**If you stand still enough, abilities recharge faster. But this could be overpowered for some abilties, so
     * use discretion and override this to return false on abilities where this might be op.*/
    public boolean canUseStillStandingRecharge(byte bt){
        return true;
    }


    // -----------------------------------------------------------------------------------------
    // UI/CLIENT, + SKIN FUNCTIONS TO OVERRIDE
    // Be conscious of where you use client functions and arguments since this class can't accidentally
    // call client functions on a server without crashing
    // -----------------------------------------------------------------------------------------
    /**Override this to render stand icons, see examples from other stands*/
    public void renderIcons(GuiGraphics context, int x, int y) {
    }

    /**Use this to draw HUD elements, it is primarily for the middle HUD (attack cooldown bar that is
     * blue, white, and orange), see punchingstand for examples*/
    public void renderAttackHud(GuiGraphics context,  Player playerEntity,
                                int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                float flashAlpha, float otherFlashAlpha){
    }

    /**A basic function called to draw stand icons*/
    public void setSkillIcon(GuiGraphics context, int x, int y, int slot, ResourceLocation rl, byte CDI){
        setSkillIcon(context,x,y,slot,rl,CDI,false);
    }

    public static final int squareHeight = 24;
    public static final int squareWidth = 24;
    public void setSkillIcon(GuiGraphics context, int x, int y, int slot, ResourceLocation rl, byte CDI, boolean locked){
        RenderSystem.enableBlend();
        context.setColor(1f, 1f, 1f, 1f);
        CooldownInstance cd = null;
        if (CDI >= 0 && !getPowerCooldowns().isEmpty() && getPowerCooldowns().size() >= CDI){
            cd = getPowerCooldowns().get(CDI);
        }
        x += slot * 25;
        y-=1;

        if (locked){
            RenderSystem.enableBlend();
            context.blit(StandIcons.LOCKED_SQUARE_ICON,x-3,y-3,0, 0, squareWidth, squareHeight, squareWidth, squareHeight);
        } else {
            RenderSystem.enableBlend();
            context.blit(StandIcons.SQUARE_ICON,x-3,y-3,0, 0, squareWidth, squareHeight, squareWidth, squareHeight);
            Font renderer = Minecraft.getInstance().font;
            if (slot==4){
                Component special4Key = KeyInputRegistry.abilityFourKey.getTranslatedKeyMessage();
                special4Key = fixKey(special4Key);
                context.drawString(renderer, special4Key,x-1,y+11,0xffffff,true);
            }
            else if (slot==3){
                Component special3Key = KeyInputRegistry.abilityThreeKey.getTranslatedKeyMessage();
                special3Key = fixKey(special3Key);
                context.drawString(renderer, special3Key,x-1,y+11,0xffffff,true);
            }
            else if (slot==2){
                Component special2Key = KeyInputRegistry.abilityTwoKey.getTranslatedKeyMessage();
                special2Key = fixKey(special2Key);
                context.drawString(renderer, special2Key,x-1,y+11,0xffffff,true);
            }
            else if (slot==1){
                Component special1Key = KeyInputRegistry.abilityOneKey.getTranslatedKeyMessage();
                special1Key = fixKey(special1Key);
                context.drawString(renderer, special1Key,x-1,y+11,0xffffff,true);
            }
            Component special1Key = KeyInputRegistry.abilityOneKey.getTranslatedKeyMessage();
        }


        if ((cd != null && (cd.time >= 0)) || isAttackIneptVisually(CDI,slot)){
            RenderSystem.enableBlend();
            context.setColor(0.62f, 0.62f, 0.62f, 0.8f);
            context.blit(rl, x, y, 0, 0, 18, 18, 18, 18);
            if ((cd != null && (cd.time >= 0))) {
                float blit = (20*(1-((float) (1+cd.time) /(1+cd.maxTime))));
                int b = (int) Math.round(blit);
                RenderSystem.enableBlend();
                context.setColor(1f, 1f, 1f, 1f);

                ResourceLocation COOLDOWN_TEX = StandIcons.COOLDOWN_ICON;

                if (cd.isFrozen())
                    COOLDOWN_TEX = StandIcons.FROZEN_COOLDOWN_ICON;

                context.blit(COOLDOWN_TEX, x - 1, y - 1 + b, 0, b, 20, 20-b, 20, 20);
                int num = ((int)(Math.floor((double) cd.time /20)+1));
                int offset = x+3;
                if (num <=9){
                    offset = x+7;
                }

                if (!cd.isFrozen())
                    context.drawString(Minecraft.getInstance().font, ""+num,offset,y,0xffffff,true);

            }
            context.setColor(1f, 1f, 1f, 0.9f);
        } else {
            RenderSystem.enableBlend();
            context.blit(rl, x, y, 0, 0, 18, 18, 18, 18);
        }
    }


    /**This function grays out icons for moves you can't currently use. Slot is the icon slot from 1-4,
     * activeP is your currently active power*/
    public boolean isAttackIneptVisually(byte activeP, int slot){
        return this.isDazed(this.self) || (((TimeStop)this.getSelf().level()).CanTimeStopEntity(this.getSelf()));
    }




    // -----------------------------------------------------------------------------------------
    // Functions you will NOT need to override
    // And will never be needed by you
    // BASICALLY YOU CAN IGNORE EVERYTHING HERE
    // -----------------------------------------------------------------------------------------

    public AbilityIconInstance drawSingleGUIIcon(GuiGraphics context, int size, int startingLeft, int startingTop, int levelToUnlock,
                                                 String nameSTR, String instructionStr, ResourceLocation draw, int extra, byte level, boolean bypass){
        Component name;
        if (level < levelToUnlock && !bypass) {
            context.blit(StandIcons.LOCKED_SQUARE_ICON, startingLeft, startingTop, 0, 0,size, size, size, size);
            context.blit(StandIcons.LOCKED, startingLeft+2, startingTop+2, 0, 0,size-4, size-4, size-4, size-4);
            name = Component.translatable("ability.roundabout.locked").withStyle(ChatFormatting.BOLD).
                    withStyle(ChatFormatting.DARK_GRAY);
        } else {
            context.blit(StandIcons.SQUARE_ICON, startingLeft, startingTop, 0, 0,size, size, size, size);
            context.blit(draw, startingLeft+2, startingTop+2, 0, 0,size-4, size-4, size-4, size-4);
            name = Component.translatable(nameSTR).withStyle(ChatFormatting.BOLD).
                    withStyle(ChatFormatting.DARK_PURPLE);
        }
        Component instruction;
        if (level < levelToUnlock && !bypass){
            instruction = Component.translatable("ability.roundabout.locked.ctrl").
                    withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.RED);
        } else {
            if (extra <= 0) {
                instruction = Component.translatable(instructionStr).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.BLUE);
            } else {
                instruction = Component.translatable(instructionStr, "" + extra).withStyle(ChatFormatting.ITALIC).
                        withStyle(ChatFormatting.BLUE);

            }
        }
        Component description;
        if (level < levelToUnlock && !bypass){
            description = Component.translatable("ability.roundabout.locked.desc", "" + levelToUnlock).
                    withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.RED);
        } else {
            description = Component.translatable(nameSTR+".desc");
        }
        return new AbilityIconInstance(size,startingLeft,startingTop,levelToUnlock,
                name,instruction,description,extra);
    }

    /**Code for the button that switches your ability row*/
    public boolean heldDownSwitch = false;
    public void switchRowsKey(boolean keyIsDown, Options options) {
        if (!heldDownSwitch) {
            if (keyIsDown) {
                heldDownSwitch = true;
                if (isHoldingSneakToggle) {
                    isHoldingSneakToggle = false;
                } else {
                    isHoldingSneakToggle = true;
                }
            }
        } else {
            if (!keyIsDown) {
                heldDownSwitch = false;
            }
        }
    }
    /**Related code to the above*/
    public boolean isHoldingSneakToggle = false;
    public boolean isHoldingSneak(){
        if (this.self.level().isClientSide) {
            Minecraft mc = Minecraft.getInstance();
            return ((mc.options.keyShift.isDown() && !isHoldingSneakToggle) || (isHoldingSneakToggle && !mc.options.keyShift.isDown()));
        }
        return this.self.isCrouching();
    }

    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypas){
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        return $$1;
    }


    public static Component fixKey(Component textIn){

        String X = textIn.getString();
        if (X.length() > 1){
            String[] split = X.split("\\s");
            if (split.length > 1){
                return Component.nullToEmpty(""+split[0].charAt(0)+split[1].charAt(0));
            } else {
                if (split[0].length() > 1){
                    return Component.nullToEmpty(""+split[0].charAt(0)+split[0].charAt(1));
                } else {
                    return Component.nullToEmpty(""+split[0].charAt(0));
                }
            }
        } else {
            return textIn;
        }
    }
    public LivingEntity getSelf(){
        return this.self;
    }

    /**Does the casting to stand user for you, will always work because this class needs a livingentity to exist*/
    public StandUser getUserData(LivingEntity User){
        return ((StandUser) User);
    }

    /**What side are we on?*/
    public boolean isClient(){
        return this.getSelf().level().isClientSide();
    }

    /**Just time stop barrage canceling when the time stop expires*/
    public boolean bonusBarrageConditions(){
        return true;
    }

    /**Your stand's generalized cooldowns*/
    public List<CooldownInstance> getPowerCooldowns(){
        if (this.self != null){
            return getUserData(this.self).rdbt$getPowerCooldowns();
        }
        return new ArrayList<>();
    }

    public void setPowerCooldowns(List<CooldownInstance> powerCooldowns){
        if (this.self != null){
            getUserData(this.self).rdbt$setPowerCooldowns(powerCooldowns);
        }
    }

    public void syncAllCooldowns(){
        try {
            if (this.self instanceof ServerPlayer sp) {
                byte cin = -1;
                for (CooldownInstance ci : getPowerCooldowns()){
                    cin++;
                    S2CPacketUtil.sendMaxCooldownSyncPacket(sp, cin, ci.time, ci.maxTime);
                }
            }
        } catch (Exception e){
            //I very much doubt this will error
            Roundabout.LOGGER.info("???");
        }
    }



    /**Functions to check/set barrage daze*/
    public boolean isDazed(LivingEntity entity){
        return this.getUserData(entity).roundabout$isDazed();
    }
    public void setDazed(LivingEntity entity, byte dazeTime){
        if ((1.0 - entity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE)) <= 0.0) {
            /*Warden, iron golems, and anything else knockback immmune can't be dazed**/
            return;
        } else if (MainUtil.isBossMob(entity)){
            /*Bosses can't be dazed**/
            return;
        }

        /**Stand Drops item when user is dazed*/
        StandEntity stand = getStandEntity(entity);
        if (stand != null && !stand.getHeldItem().isEmpty()){
            double $$3 = stand.getEyeY() - 0.3F;
            ItemEntity $$4 = new ItemEntity(stand.level(), stand.getX(), $$3, stand.getZ(), stand.getHeldItem());
            $$4.setPickUpDelay(40);
            $$4.setThrower(stand.getUUID());
            stand.level().addFreshEntity($$4);
            stand.setHeldItem(ItemStack.EMPTY);
        }
        if (dazeTime > 0){
            ((StandUser) entity).roundabout$tryPower(PowerIndex.NONE,true);
            ((StandUser) entity).roundabout$getStandPowers().animateStand(StandEntity.HURT_BY_BARRAGE);
        } else {
            ((StandUser) entity).roundabout$getStandPowers().animateStand(StandEntity.IDLE);
        }
        this.getUserData(entity).roundabout$setDazed(dazeTime);
    }
    public void setDazedSafely(LivingEntity entity, byte dazeTime){
        if (dazeTime > 0){
            ((StandUser) entity).roundabout$tryPower(PowerIndex.NONE,true);
            ((StandUser) entity).roundabout$getStandPowers().animateStand(StandEntity.HURT_BY_BARRAGE);
        } else {
            ((StandUser) entity).roundabout$getStandPowers().animateStand(StandEntity.IDLE);
        }
        this.getUserData(entity).roundabout$setDazed(dazeTime);
    }


    /**If you have a stand entity summoned, get that*/
    public StandEntity getStandEntity(LivingEntity User){
        return this.getUserData(User).roundabout$getStand();
    } public boolean hasStandEntity(LivingEntity User){
        return this.getUserData(User).roundabout$hasStandOut();
    } public boolean hasStandActive(LivingEntity User){
        return this.getUserData(User).roundabout$getActive();
    }

    /**set an ability on cooldown*/
    public void setCooldown(byte power, int cooldown){
        if (!getPowerCooldowns().isEmpty() && getPowerCooldowns().size() >= power){
            getPowerCooldowns().get(power).time = cooldown;
            getPowerCooldowns().get(power).maxTime = cooldown;
        }
    }
    /**set an ability on cooldown, and change the max cooldown*/
    public void setCooldownMax(byte power, int cooldown, int maxCooldown){
        if (!getPowerCooldowns().isEmpty() && getPowerCooldowns().size() >= power){
            getPowerCooldowns().get(power).time = cooldown;
            getPowerCooldowns().get(power).maxTime = maxCooldown;
        }
    }

    /**Returns the cooldown for an ability*/
    public CooldownInstance getCooldown(byte power){
        if (!getPowerCooldowns().isEmpty() && getPowerCooldowns().size() >= power){
            return getPowerCooldowns().get(power);
        }
        return null;
    }

    /**Checks if an ability is currently on cooldown*/
    public boolean onCooldown(byte power){
        if (!getPowerCooldowns().isEmpty() && getPowerCooldowns().size() >= power){
            return (getPowerCooldowns().get(power).time >= 0);
        }
        return false;
    }

    public boolean hasCooldowns(){
        List<CooldownInstance> CDCopy = new ArrayList<>(getPowerCooldowns()) {
        };
        for (byte i = 0; i < CDCopy.size(); i++){
            CooldownInstance ci = CDCopy.get(i);
            if (ci.time >= 0){
                return true;
            }
        }
        return false;
    }

    public void refreshCooldowns(){
        List<CooldownInstance> CDCopy = new ArrayList<>(getPowerCooldowns()) {
        };
        for (byte i = 0; i < CDCopy.size(); i++){
            CooldownInstance ci = CDCopy.get(i);
            ci.time = -1;
        }
        setPowerCooldowns(CDCopy);
    }
}
