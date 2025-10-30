package net.hydra.jojomod.fates.powers;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IFatePlayer;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.KeyInputRegistry;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.*;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class AbilityScapeBasis {
    /**Note that self refers to the entity with the power*/
    public final LivingEntity self;

    public AbilityScapeBasis(LivingEntity self) {
        this.self = self;
    }

    // -----------------------------------------------------------------------------------------
    // UNDERSTANDING THE MAIN VARIABLES
    // -----------------------------------------------------------------------------------------

    /**The time that passed since using the last attack. It counts up, so that a visual meter can display cooldowns.
     * It is also used to */
    public int attackTime = -1;

    /**The time within an attack. This matters, because if you desummon a stand the attack time doesnt reset */
    public int attackTimeDuring = -1;

    /**The time until the generic ability cooldown passes.
     This exists so you have downtime that non-stand users can get it and attack you during.*/
    public int attackTimeMax = -1;

    /**The id of the move being used. Ex: 1 = punch*/
    public byte activePower = 0;

    /**The phase of the move being used, primarily to keep track of which punch you are on in a punch string.*/
    public byte activePowerPhase = 0;

    /**This is when the punch combo goes on cooldown. Default is 3 hit combo.*/
    public final byte activePowerPhaseMax = 3;

    /**This variable exists so that a client can begin displaying your attack hud info without ticking through it.
     * Basically, stand attacks are clientside, but they need the server's confirmation to kickstart so you
     * can't hit targets in frozen tps*/
    public boolean kickStarted = true;

    /**This plays automatically when a power is changed on the server to sync it with the client*/
    public void kickStartClient(){
        this.kickStarted = true;
    }


    public void updateMovesFromPacket(byte activePower){

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


    public boolean getReducedDamage(Entity entity){
        return (entity instanceof Player || entity instanceof StandEntity ||
                ((entity instanceof LivingEntity LE && !((StandUser)LE).roundabout$getStandDisc().isEmpty()) &&
                        ClientNetworking.getAppropriateConfig().generalStandUserMobSettings.standUserMobsTakePlayerDamageMultipliers)
        );
    }

    /**If your character is in a position to change abilities. By default, you are locked into clashing while clashing
     * unless you forfeit the clash by desummoning your stand*/
    public boolean canChangePower(int move, boolean forced){
        if ((this.activePower == PowerIndex.NONE || forced) &&
                (!this.isDazed(this.self))) {
            return true;
        }
        return false;
    }

    /** Tries to use an ability of your stand. If forced is true, the ability comes out no matter what.**/
    /** There is no reason for the function to be a boolean, that goes unused, so gradually we can convert this to
     * a void function*/
    public void tryPower(int move){
        tryPower(move,true);
    }
    public boolean tryPower(int move, boolean forced){
        if (canChangePower(move, forced)) {
            if (move == PowerIndex.NONE) {
                this.setPowerNone();
            } else {
                this.setPowerOther(move,this.getActivePower());
            }
        }
        return false;
    }

    /**Override this to set the special move*/
    public boolean setPowerOther(int move, int lastMove) {
        return false;
    }

    /**Sets your active power to nothing*/
    public boolean setPowerNone(){
        this.attackTimeDuring = -1;
        this.setActivePower(PowerIndex.NONE);
        return true;
    }


    public boolean tryPosPower(int move, boolean forced, Vec3 pos){
        tryPower(move, forced);
        /*Return false in an override if you don't want to sync cooldowns, if for example you want a simple data update*/
        return true;
    }
    public boolean tryBlockPosPower(int move, boolean forced, BlockPos blockPos){
        tryPower(move, forced);
        /*Return false in an override if you don't want to sync cooldowns, if for example you want a simple data update*/
        return true;
    }
    public boolean tryBlockPosPower(int move, boolean forced, BlockPos blockPos, BlockHitResult blockhit){
        tryPower(move, forced);
        /*Return false in an override if you don't want to sync cooldowns, if for example you want a simple data update*/
        return true;
    }

    public int storedInt = 0;
    public boolean tryIntPower(int move, boolean forced, int chargeTime){
        tryPower(move, forced);
        /*Return false in an override if you don't want to sync cooldowns, if for example you want a simple data update*/
        return true;
    }
    public boolean tryTripleIntPower(int move, boolean forced, int chargeTime, int move2, int move3){
        tryPower(move, forced);
        /*Return false in an override if you don't want to sync cooldowns, if for example you want a simple data update*/
        return true;
    }

    public void tryPowerPacket(byte packet){
        if (this.self.level().isClientSide()) {
            C2SPacketUtil.tryPowerPacket(packet);
        }
    }
    public void tryIntPowerPacket(byte packet, int integer){
        if (this.self.level().isClientSide()) {
            C2SPacketUtil.tryIntPowerPacket(packet,integer);
        }
    }
    /**This is different than int power packet only by virtue of what functions it passes through, and is useful
     * for calling something even if you are in a barrage clash or other conditions would otherwise interrupt your
     * packet. Very niche, but it exists, and isn't always used in essential ways*/
    public void tryIntToServerPacket(byte packet, int integer){
        if (this.self.level().isClientSide()) {
            C2SPacketUtil.intToServerPacket(packet,integer);
        }
    }

    public void tryTripleIntPacket(byte packet, int in1, int in2, int in3){
        if (this.self.level().isClientSide()) {
            C2SPacketUtil.tryTripleIntPacket(packet, in1, in2, in3);
        }
    }
    public void tryBlockPosPowerPacket(byte packet, BlockPos pos){
        if (this.self.level().isClientSide()) {
            C2SPacketUtil.tryBlockPosPowerPacket(packet, pos);
        }
    }
    public void tryBlockPosPowerPacket(byte packet, BlockPos pos, HitResult hitResult){
        if (this.self.level().isClientSide()) {
            C2SPacketUtil.tryBlockPosPowerPacket(packet, pos, hitResult);
        }
    }
    public void tryPosPowerPacket(byte packet, Vec3 pos){
        if (this.self.level().isClientSide()) {
            C2SPacketUtil.tryPosPowerPacket(packet, pos);
        }
    }
    public Vec3 savedPos;

    /**The most basic getters and setters*/
    public StandUser getStandUserSelf(){
        return ((StandUser)this.self);
    }

    public int getAttackTime(){
        return this.attackTime;
    }
    public int getAttackTimeDuring(){
        return this.attackTimeDuring;
    }
    public byte getActivePower(){
        return this.activePower;
    }
    public byte getActivePowerPhase(){
        return this.activePowerPhase;
    }
    public byte getActivePowerPhaseMax(){
        return this.activePowerPhaseMax;
    }

    public void setAttackTime(int attackTime){
        this.attackTime = attackTime;
    }
    public void setAttackTimeDuring(int attackTimeDuring){
        this.attackTimeDuring = attackTimeDuring;
    }
    public void setAttackTimeMax(int attackTimeMax){
        this.attackTimeMax = attackTimeMax;
    }
    public int getAttackTimeMax(){
        return this.attackTimeMax;
    }

    public void setMaxAttackTime(int attackTimeMax){
        this.attackTimeMax = attackTimeMax;
    }
    public void setActivePower(byte activeMove){
        this.activePower = activeMove;
    }
    public void setActivePowerPhase(byte activePowerPhase){
        this.activePowerPhase = activePowerPhase;
    }



    public void xTryPower(byte index, boolean forced){
    }
    public void tryPowerStuff(){
        syncActivePower();
        if (this.self.level().isClientSide) {
            kickStarted = false;
        }
    }

    /**Stuff that happens every tick while possessing the stand in general.
     * Remember to call the super when you override or some things might not function properly*/
    public void tickPower(){
        baseTickPower();
    }

    public void syncActivePower(){
        if (!this.self.level().isClientSide && this.self instanceof ServerPlayer SP){
            S2CPacketUtil.sendActivePowerPacket(SP,activePower);
        }
    }

    public void baseTickPower(){
        if (this.self.isAlive() && !this.self.isRemoved()) {
            if (this.self.level().isClientSide){
                if (!this.kickStarted && this.getAttackTimeDuring() <= -1){
                    this.kickStarted = true;
                }
            }
            if (!this.self.level().isClientSide || kickStarted) {
                if (this.attackTimeDuring != -1) {
                    this.attackTimeDuring++;
                    if (this.attackTimeDuring == -1) {
                        xTryPower(PowerIndex.NONE,true);
                    } else {
                        if (!this.isAttackInept(this.activePower)) {
                            if (this.activePower == PowerIndex.ATTACK) {
                                this.updateAttack();
                            } else {
                                this.updateUniqueMoves();
                            }
                        } else {
                            resetAttackState();
                        }
                    }
                }
                this.attackTime++;
                if (this.attackTime > this.attackTimeMax) {
                    this.setActivePowerPhase((byte) 0);
                }
                if (this.interruptCD > 0) {
                    this.interruptCD--;
                }
            }
            this.tickDash();
        }
        if (this.self.level().isClientSide) {
            tickSounds();
        }
        if (this.scopeLevel != 0 && !this.canScope()){
            setScopeLevel(0);
            this.scopeTime = -1;
        }
    }
    /**plays every tick that the active power is set to X move, the unique moves lets you do your own packets,
     * see examples*/
    public void updateUniqueMoves(){
    }

    /**same as above but for the standard attack packet*/
    public void updateAttack(){
    }

    /**Override this if you want to add or remove conditions that prevent moves from updating and shut
     * them down*/
    public boolean isAttackInept(byte activeP){
        return this.self.isUsingItem() || this.isDazed(this.self) || (((TimeStop)this.getSelf().level()).CanTimeStopEntity(this.getSelf()));
    }

    /**If doing something like eating, cancels attack state*/
    public void resetAttackState(){
        if (shouldReset(this.getActivePower())){
            this.interruptCD = 3;
            
            xTryPower(PowerIndex.NONE,true);
        }
    }

    /**If eating or using items in general shouldn't cancel certain abilties, put them as exceptions here*/
    public boolean shouldReset(byte activeP){
        return (this.self.isUsingItem() &&
                !(this.isDazed(this.self) || (((TimeStop)this.getSelf().level()).CanTimeStopEntity(this.getSelf()))));
    }
    public int interruptCD = 0;
    public boolean getInterruptCD(){
        return this.interruptCD <= 0;
    }
    public void setInterruptCD(int interruptCD){
        this.interruptCD = interruptCD;
    }



    public static final byte
            NONE = 0;

    /**Sound updates that play every tick*/
    public void tickSounds(){
        if (this.self.level().isClientSide) {
            ((StandUserClient) this.self).roundabout$clientPlaySound();
            ((StandUserClient) this.self).roundabout$clientSoundCancel();
        }
    }

    /**Does your stand let you zoom in a lot? Override this if it does*/
    public boolean canScope(){
        return false;
    }
    public int scopeTime = -1;
    public int scopeLevel = 0;
    public void setScopeLevel(int level){
        if (scopeLevel <= 0 && level > 0){
            if (this.getSelf().level().isClientSide()){
                C2SPacketUtil.trySingleBytePacket(PacketDataIndex.SINGLE_BYTE_SCOPE);
            }
        } else if (scopeLevel > 0 && level <= 0){
            if (this.getSelf().level().isClientSide()){
                C2SPacketUtil.trySingleBytePacket(PacketDataIndex.SINGLE_BYTE_SCOPE_OFF);
            }
        }
        scopeLevel=level;
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


    public ResourceLocation getIconYes(int slot){
        return StandIcons.SQUARE_ICON;
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
            context.blit(getIconYes(slot),x-3,y-3,0, 0, squareWidth, squareHeight, squareWidth, squareHeight);
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


    public void tickDash(){
        if (this.getSelf() instanceof Player) {

            if (((IPlayerEntity)this.getSelf()).roundabout$getDodgeTime() >= 0) {
                cancelConsumableItem(this.getSelf());
            }

            if (((IPlayerEntity)this.getSelf()).roundabout$getClientDodgeTime() >= 10){
                ((IPlayerEntity)this.getSelf()).roundabout$setClientDodgeTime(-1);
                if (!this.getSelf().level().isClientSide){
                    ((IPlayerEntity)this.getSelf()).roundabout$setDodgeTime(-1);
                    byte pos = ((IPlayerEntity)this.getSelf()).roundabout$GetPos();
                    if (pos == PlayerPosIndex.DODGE_FORWARD || pos == PlayerPosIndex.DODGE_BACKWARD) {
                        ((IPlayerEntity) this.getSelf()).roundabout$SetPos(PlayerPosIndex.NONE);
                    }
                }
            } else if (((IPlayerEntity)this.getSelf()).roundabout$getClientDodgeTime() >= 0){
                ((IPlayerEntity) this.getSelf()).roundabout$setClientDodgeTime(((IPlayerEntity) this.getSelf()).roundabout$getClientDodgeTime()+1);
            }

            if (((IPlayerEntity)this.getSelf()).roundabout$getDodgeTime() >= 10){

                ((IPlayerEntity)this.getSelf()).roundabout$setDodgeTime(-1);
                byte pos = ((IPlayerEntity)this.getSelf()).roundabout$GetPos();
                if (pos == PlayerPosIndex.DODGE_FORWARD || pos == PlayerPosIndex.DODGE_BACKWARD) {
                    ((IPlayerEntity) this.getSelf()).roundabout$SetPos(PlayerPosIndex.NONE);
                }
            } else if (((IPlayerEntity)this.getSelf()).roundabout$getDodgeTime() >= 0){
                if (this.getSelf().level().isClientSide){
                    ((IPlayerEntity) this.getSelf()).roundabout$setDodgeTime(((IPlayerEntity) this.getSelf()).roundabout$getDodgeTime()+1);
                }
            }
        }
    }


    /**Call this to make yourself stop using an item*/
    public void cancelConsumableItem(LivingEntity entity){
        ItemStack itemStack = entity.getUseItem();
        Item item = itemStack.getItem();
        if (item.isEdible() || item instanceof PotionItem) {
            entity.releaseUsingItem();
            if (entity instanceof Player) {
                entity.stopUsingItem();
            }
        }
    }








    /**returns if you are using stand guard*/
    public boolean isGuarding(){
        return this.activePower == PowerIndex.GUARD;
    }



    public void powerActivate(PowerContext context) {};

    private boolean held1 = false;
    private boolean held2 = false;
    private boolean held3 = false;
    private boolean held4 = false;

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
    }

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
    }

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
    }

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
    }


    public void preButtonInput4(boolean keyIsDown, Options options){
        if (!hasStandActive(this.getSelf())) {
            if (!((TimeStop)this.getSelf().level()).CanTimeStopEntity(this.getSelf())) {
                ((StandUser) this.getSelf()).roundabout$setIdleTime(0);
                buttonInput4(keyIsDown, options);
            }
        }
    }
    public void preButtonInput3(boolean keyIsDown, Options options){
        if (!hasStandActive(this.getSelf())) {
            if (!((TimeStop)this.getSelf().level()).CanTimeStopEntity(this.getSelf())) {
                ((StandUser) this.getSelf()).roundabout$setIdleTime(0);
                buttonInput3(keyIsDown, options);
            }
        }
    }

    public void preButtonInput2(boolean keyIsDown, Options options){
        if (!hasStandActive(this.getSelf())) {
            if (!((TimeStop)this.getSelf().level()).CanTimeStopEntity(this.getSelf())) {
                ((StandUser) this.getSelf()).roundabout$setIdleTime(0);
                buttonInput2(keyIsDown, options);
            }
        }
    }

    public void preButtonInput1(boolean keyIsDown, Options options){
        if (!hasStandActive(this.getSelf())) {
            if (!((TimeStop)this.getSelf().level()).CanTimeStopEntity(this.getSelf())) {
                ((StandUser) this.getSelf()).roundabout$setIdleTime(0);
                buttonInput1(keyIsDown, options);
            }
        }
    }
}
