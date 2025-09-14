package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.access.IMob;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.SavedSecond;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.CooldownInstance;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.networking.ServerToClientPackets;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.zetalasis.networking.message.api.ModMessageEvents;

import java.util.Arrays;
import java.util.List;

public class PowersMandom extends NewDashPreset {
    public PowersMandom(LivingEntity self) {
        super(self);
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersMandom(entity);
    }
    @Override
    /**Override to add disable config*/
    public boolean isStandEnabled(){
        return ClientNetworking.getAppropriateConfig().mandomSettings.enableMandom;
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
        // code for advanced icons

        if (activatedPastVision())
            setSkillIcon(context, x, y, 1, StandIcons.MANDOM_VISION_ON, PowerIndex.NO_CD);
        else
            setSkillIcon(context, x, y, 1, StandIcons.MANDOM_VISION_OFF, PowerIndex.NO_CD);

        if (timeHasBeenAltered > -1 && ClientNetworking.getAppropriateConfig().mandomSettings.timeRewindCooldownExtraCondition > 0)
            setSkillIcon(context, x, y, 2, StandIcons.REWIND_PENALTY, PowerIndex.SKILL_2);
        else
            setSkillIcon(context, x, y, 2, StandIcons.REWIND, PowerIndex.SKILL_2);


        if (!isHoldingSneak())
            setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
        else
            setSkillIcon(context, x, y, 3, StandIcons.WATCH, PowerIndex.NO_CD);

        renderClock(context,x,y,4);

        super.renderIcons(context, x, y);
    }
    public void renderClock(GuiGraphics context, int x, int y, int slot){
        RenderSystem.enableBlend();
        context.setColor(1f, 1f, 1f, 1f);
        CooldownInstance cd = null;
        x += slot * 25;
        y-=1;
        RenderSystem.enableBlend();
        context.blit(StandIcons.NOVELTY_ICON,x-3,y-3,0, 0, squareWidth, squareHeight, squareWidth, squareHeight);

        RenderSystem.enableBlend();
        ItemStack clock = new ItemStack(Items.CLOCK);
        context.renderItem(clock, x+1, y+1);  // Draw the item itself
    }

    public byte getWatchStyle(){
        if (this.self instanceof Player PL){
            return ((IPlayerEntity)PL).roundabout$getWatchStyle();
        }
        return WATCHLESS;
    }
    public void swapWatchStyle(){
        byte style = getWatchStyle();
        style++;
        if (style > ROLEX){
            style = WATCHLESS;
        }
        if (this.self instanceof Player PL){
            ((IPlayerEntity)PL).roundabout$setWatchStyle(style);
        }
    }
    public boolean activatedPastVision(){
        return getStandUserSelf().roundabout$getUniqueStandModeToggle();
    }

    @Override
    public void powerActivate(PowerContext context) {
        /**Making dash usable on both key presses*/
        switch (context)
        {

            case SKILL_1_NORMAL, SKILL_1_CROUCH -> {
                swapVisionModeClient();
            }
            case SKILL_2_NORMAL, SKILL_2_CROUCH -> {
                rewindTimeClient();
            }
            case SKILL_3_NORMAL-> {
                dash();
            }
            case SKILL_3_CROUCH-> {
                switchWatchClient();
            }
        }
    }
    public void switchWatchClient(){
        this.tryPower(PowerIndex.POWER_4, true);
        tryPowerPacket(PowerIndex.POWER_4);
    }
    public boolean switchWatch(){
        swapWatchStyle();
        if (!isClient() && this.self instanceof ServerPlayer PE) {
            switch (getWatchStyle())
            {
                case WATCHLESS -> {
                    PE.displayClientMessage(Component.translatable("text.roundabout.mandom.watch_off").withStyle(ChatFormatting.GOLD), true);
                }
                case MAIN -> {
                    PE.displayClientMessage(Component.translatable("text.roundabout.mandom.watch_on").withStyle(ChatFormatting.GOLD), true);
                }
                case ROLEX -> {
                    PE.displayClientMessage(Component.translatable("text.roundabout.mandom.watch_on_rolex").withStyle(ChatFormatting.GOLD), true);
                }
            }
        }
        return true;
    }
    public void rewindTimeClient(){
        if (!this.onCooldown(PowerIndex.SKILL_2)) {
            this.tryPower(PowerIndex.POWER_2, true);
            tryPowerPacket(PowerIndex.POWER_2);
        }
    }
    public void swapVisionModeClient(){
        this.tryPower(PowerIndex.POWER_1, true);
        tryPowerPacket(PowerIndex.POWER_1);
    }
    @Override
    public boolean setPowerOther(int move, int lastMove) {
        switch (move)
        {
            case PowerIndex.POWER_1 -> {
                return toggleVision();
            }
            case PowerIndex.POWER_2 -> {
                return itsRewindTime();
            }
            case PowerIndex.POWER_4 -> {
                return switchWatch();
            }
        }
        return super.setPowerOther(move,lastMove);
    }

    public static final byte
            ROTATE = 1;

    public boolean toggleVision(){
        getStandUserSelf().roundabout$setUniqueStandModeToggle(!activatedPastVision());
        if (!isClient()) {
            if (activatedPastVision()) {
                ((ServerPlayer) this.self).displayClientMessage(Component.translatable("text.roundabout.mandom.vision_on").withStyle(ChatFormatting.DARK_PURPLE), true);
            } else {
                ((ServerPlayer) this.self).displayClientMessage(Component.translatable("text.roundabout.mandom.vision_off").withStyle(ChatFormatting.DARK_PURPLE), true);
            }
        }
        return true;
    }



    public boolean itsRewindTime(){
        if (isClient() || (!this.onCooldown(PowerIndex.SKILL_2) || !ClientNetworking.getAppropriateConfig().mandomSettings.timeRewindCooldownUsesServerLatency)) {
            int cooldown = ClientNetworking.getAppropriateConfig().mandomSettings.timeRewindCooldownv2;
            if (timeHasBeenAltered > -1)
                cooldown += ClientNetworking.getAppropriateConfig().mandomSettings.timeRewindCooldownExtraCondition;
            this.setCooldown(PowerIndex.SKILL_2, cooldown);
            setTimeHasBeenAltered(-1);
            if (isClient()) {
                this.self.playSound(ModSounds.MANDOM_REWIND_EVENT, 200F, 1.0F);
            } else {
                rewindTimeActivation();
            }
        }
        return true;
    }


    @Override
    public void onPlaceBlock(ServerPlayer $$0, BlockPos $$1, ItemStack $$2){
        /**Can't really cancel this one*/
        if (!$$0.isCreative()) {
            setTimeHasBeenAltered(140);
        }
        super.onPlaceBlock($$0,$$1,$$2);
    }
    @Override
    public void onDestroyBlock(Level $$0, Player $$1, BlockPos $$2, BlockState $$3, BlockEntity $$4, ItemStack $$5){
        if (!$$3.is(BlockTags.SAPLINGS) && !$$3.is(BlockTags.FLOWERS) && !$$3.is(BlockTags.REPLACEABLE)){
            setTimeHasBeenAltered(140);
        }
        super.onDestroyBlock($$0,$$1,$$2,$$3,$$4,$$5);
    }

    @Override
    public boolean onKilledEntity(ServerLevel $$0, LivingEntity $$1){
        if (!(this.self instanceof Player PL && PL.isCreative())) {
            setTimeHasBeenAltered(140);
        }
        return super.onKilledEntity($$0,$$1);
    }
    public int timeHasBeenAltered = -1;

    public void tickTimeAlteration(){
        if (!this.self.level().isClientSide()) {
            if (timeHasBeenAltered > -1){
                setTimeHasBeenAltered(timeHasBeenAltered-1);
            }
        }
    }

    public void setTimeHasBeenAltered(int altered){
        if (timeHasBeenAltered != altered && ClientNetworking.getAppropriateConfig().mandomSettings.timeRewindCooldownExtraCondition > 0){
            // send packet here
            timeHasBeenAltered = altered;
            if (!this.self.level().isClientSide() && this.self instanceof ServerPlayer SP) {
                ModMessageEvents.sendToPlayer(SP, "mandom_penalty",altered);
            }
        }
    }

    int ticksSinceLastYap = 0;
    int maxTicksSinceLastYap = 400;

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

    public SimpleParticleType getParticle(Entity ent){
        if (ent instanceof Monster || (ent instanceof Mob mb && mb.isAggressive()))
            return ModParticles.RED_CLOCK;
        if (ent !=null && ent.is(this.self))
            return ModParticles.CLOCK;
        if (ent instanceof Player)
            return ModParticles.BLUE_CLOCK;
        if (ent instanceof Projectile || ent instanceof ItemEntity)
            return ModParticles.ORANGE_CLOCK;
        return ModParticles.GREEN_CLOCK;
    }
    public static final byte
            WATCHLESS = 0,
            MAIN = 1,
            ROLEX = 2;
    public byte getWatchStyle = WATCHLESS;

    public void rewindTimeActivation(){
        int rewindPacketRange = ClientNetworking.getAppropriateConfig().mandomSettings.timeRewindRange;
        int rewindCooldown = ClientNetworking.getAppropriateConfig().mandomSettings.timeRewindCooldownv2;
        spreadRadialClientPacket(rewindPacketRange,false, ServerToClientPackets.S2CPackets.MESSAGES.Rewind.value);
        List<Entity> mobsInRange = MainUtil.getEntitiesInRange(this.self.level(), this.self.blockPosition(),
                rewindPacketRange);
        if (!mobsInRange.isEmpty()) {
            for (Entity ent : mobsInRange) {
                if (MainUtil.canRewindInTime(ent, this.self)) {
                    IEntityAndData iData = (IEntityAndData) ent;
                    SavedSecond lastSecond = iData.roundabout$getLastSavedSecond();
                    if (lastSecond != null) {
                        lastSecond.loadTime(ent);
                    }

                    if (!ent.is(this.self)){
                        if (ent instanceof LivingEntity LE){
                            StandUser user = ((StandUser)LE);
                            StandPowers powers = user.roundabout$getStandPowers();
                            if (powers instanceof PowersMandom PM && !(PM.onCooldown(PowerIndex.SKILL_2) && PM.getCooldown(PowerIndex.SKILL_2).time > rewindCooldown)){
                                PM.setCooldown(PowerIndex.SKILL_2,rewindCooldown);
                            }
                        }
                    }
                }
            }
            unskipInterp = 1;
        }
        if (this.self instanceof Player PE){
            IPlayerEntity ipe = ((IPlayerEntity) PE);
            if (ipe.roundabout$getWatchStyle() > WATCHLESS){
                ipe.roundabout$SetPoseEmote((byte) 10);
            }
        }
    }
    public int unskipInterp = -1;

    public boolean canUseStillStandingRecharge(byte bt){
        if (bt == PowerIndex.SKILL_2)
            return false;
        return super.canUseStillStandingRecharge(bt);
    }


    @Override
    public void tickPower() {
        super.tickPower();
        tickTimeAlteration();
        if (unskipInterp > -1){
            unskipInterp--;
            if (unskipInterp <= -1){
                int rewindPacketRange = ClientNetworking.getAppropriateConfig().mandomSettings.timeRewindRange;
                spreadRadialClientPacket(rewindPacketRange+50,false, "unskip_interpolation");
            }
        }

        /**Grabs nearby entities pretty regularly to see if they can be rendered*/
        if (!this.self.level().isClientSide()) {
            if (activatedPastVision() && this.self.tickCount % 3 == 0) {
                List<Entity> mobsInRange = MainUtil.getEntitiesInRange(this.self.level(), this.self.blockPosition(),
                        ClientNetworking.getAppropriateConfig().mandomSettings.chronoVisionRange);
                if (!mobsInRange.isEmpty()) {
                    for (Entity ent : mobsInRange) {
                        if (MainUtil.canRewindInTime(ent,this.self)) {
                            IEntityAndData iData = (IEntityAndData) ent;
                            SavedSecond lastSecond = iData.roundabout$getLastSavedSecond();
                            if (lastSecond != null){
                                if (!lastSecond.hasHadParticle){
                                    lastSecond.hasHadParticle = true;
                                    lastSecond.isTickingParticles = this.self;
                                    if (ent instanceof Player && this.self instanceof Player PE){
                                        spreadRadialClientPacket(
                                                ClientNetworking.getAppropriateConfig().mandomSettings.chronoVisionRange,
                                                false,
                                                "chrono_vision_player",
                                                ent.getId(),lastSecond.position.x,lastSecond.position.y,lastSecond.position.z);
                                    } else {
                                        ((ServerLevel) this.self.level()).sendParticles(getParticle(ent),
                                                lastSecond.position.x, lastSecond.position.y+ent.getEyeHeight()*0.8, lastSecond.position.z,
                                                0, 0, 0, 0, 0.015);
                                    }
                                }
                                if (!(ent instanceof Projectile) && !(ent instanceof ItemEntity)) {
                                    if (lastSecond.isTickingParticles != null && lastSecond.isTickingParticles.is(this.self)) {
                                        Vec3 forward = Vec3.directionFromRotation(new Vec2(lastSecond.rotationVec.x,lastSecond.headYRotation));
                                        ((ServerLevel) this.self.level()).sendParticles(ModParticles.TIME_EMBER,
                                                lastSecond.position.x, lastSecond.position.y + ent.getEyeHeight() * 0.8, lastSecond.position.z,
                                                0,
                                                forward.x,
                                                forward.y,
                                                forward.z,
                                                0.25);
                                    }
                                }
                            }
                        }
                    }
                    /**
                    ((ServerLevel) this.level()).sendParticles(ModParticles.BUBBLE_TRAIL,
                            this.getX(), this.getY() + this.getBbHeight() / 2, this.getZ(),
                            0, 0, 0, 0, 0.015);**/
                }
            }
        } else {

        }
    }


    @Override
    public void updateUniqueMoves() {
        super.updateUniqueMoves();
    }


    public static final byte
            MANGA = 1,
            PURPLE = 2,
            SKY = 3,
            SQUID = 4,
            GLOW_SQUID = 5,
            ROSE = 6,
            NAUTILUS = 7,
            ALIEN = 8,
            DARK = 9,
            JELLYFISH = 10,
            HAPPY = 11,
            EYE = 12,
            MELON = 13,
            ESIDISI = 14;
    @Override
    public List<Byte> getSkinList() {
        return Arrays.asList(
                MANGA,
                PURPLE,
                SKY,
                ALIEN,
                DARK,
                SQUID,
                GLOW_SQUID,
                ROSE,
                NAUTILUS,
                JELLYFISH,
                HAPPY,
                EYE,
                MELON,
                ESIDISI
        );
    }
    @Override public Component getSkinName(byte skinId) {
        return switch (skinId)
        {
            case PowersMandom.PURPLE -> Component.translatable("skins.roundabout.mandom.purple");
            case PowersMandom.SKY -> Component.translatable("skins.roundabout.mandom.sky");
            case PowersMandom.SQUID -> Component.translatable("skins.roundabout.mandom.squid");
            case PowersMandom.ALIEN -> Component.translatable("skins.roundabout.mandom.alien");
            case PowersMandom.DARK -> Component.translatable("skins.roundabout.mandom.dark");
            case PowersMandom.GLOW_SQUID -> Component.translatable("skins.roundabout.mandom.glow_squid");
            case PowersMandom.ROSE -> Component.translatable("skins.roundabout.mandom.rose");
            case PowersMandom.NAUTILUS -> Component.translatable("skins.roundabout.mandom.nautilus");
            case PowersMandom.JELLYFISH -> Component.translatable("skins.roundabout.mandom.jellyfish");
            case PowersMandom.HAPPY -> Component.translatable("skins.roundabout.mandom.happy");
            case PowersMandom.EYE -> Component.translatable("skins.roundabout.mandom.eye");
            case PowersMandom.MELON -> Component.translatable("skins.roundabout.mandom.melon");
            case PowersMandom.ESIDISI -> Component.translatable("skins.roundabout.mandom.esidisi");
            default -> Component.translatable("skins.roundabout.mandom.manga");
        };
    }

    @Override
    public boolean isSecondaryStand(){
        return true;
    }
    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }
    @Override
    public SoundEvent getSoundFromByte(byte soundChoice){
        switch (soundChoice)
        {
            case SoundIndex.SUMMON_SOUND -> {
                return ModSounds.SUMMON_MANDOM_EVENT;
            }
        }
        return super.getSoundFromByte(soundChoice);
    }


    boolean thisistheend=false;
    @Override
    public void tickStandRejection(MobEffectInstance effect) {
        if (!this.getSelf().level().isClientSide() && !thisistheend) {
            thisistheend = true;
                itsRewindTime();
        }
    }

    public float lastRewindHealth = -1;
    @Override
    public void tickMobAI(LivingEntity attackTarget){
        if (lastRewindHealth < 0){
            lastRewindHealth = this.self.getHealth();
        }
        if (this.self.getHealth() < lastRewindHealth && !onCooldown(PowerIndex.SKILL_2)){
            itsRewindTime();
            if (!hasStandActive(this.self)){
                ((IMob)this.self).roundabout$setRetractTicks(500);
                getStandUserSelf().roundabout$summonStand(this.self.level(),true,false);
            }
        }
        lastRewindHealth = this.self.getHealth();
    }

    public byte worthinessType(){
        return HUMANOID_WORTHY;
    }
    public Component getPosName(byte posID) {
        if (posID == 1) {
            return Component.translatable("idle.roundabout.hey_ya_2");
        } else if (posID == 2) {
                return Component.translatable("idle.roundabout.mandom_back");
        } else {
            return Component.translatable("idle.roundabout.hey_ya_1");
        }
    }

    @Override
    public List<Byte> getPosList(){
        List<Byte> $$1 = Lists.newArrayList();
        $$1.add((byte) 0);
        $$1.add((byte) 1);
        $$1.add((byte) 2);
        return $$1;
    }
    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypass) {
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 80, 0, "ability.roundabout.chrono_vision",
                "instruction.roundabout.press_skill", StandIcons.MANDOM_VISION_ON, 1, level, bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 99, 0, "ability.roundabout.rewind_time",
                "instruction.roundabout.press_skill", StandIcons.REWIND,2,level,bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 118, 0, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE,3,level,bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 80, 0, "ability.roundabout.watch_switch",
                "instruction.roundabout.press_skill_crouch", StandIcons.WATCH,3,level,bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 99, 0, "ability.roundabout.clock",
                "instruction.roundabout.passive", StandIcons.CLOCK,4,level,bypass));
        return $$1;
    }


    public boolean isServerControlledCooldown(CooldownInstance ci, byte num){
        if (num == PowerIndex.SKILL_2 && ClientNetworking.getAppropriateConfig().mandomSettings.timeRewindCooldownUsesServerLatency) {
            return true;
        }
        return super.isServerControlledCooldown(ci, num);
    }

    /**
    public boolean isWip(){
        return true;
    }
    public Component ifWipListDevStatus(){
        return Component.translatable(  "roundabout.dev_status.active").withStyle(ChatFormatting.AQUA);
    }
    public Component ifWipListDev(){
        return Component.literal(  "Hydra").withStyle(ChatFormatting.YELLOW);
    }
     */
}