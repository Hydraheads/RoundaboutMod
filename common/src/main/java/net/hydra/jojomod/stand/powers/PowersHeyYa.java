package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.block.MiningAlertBlock;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.WaterFluid;

import java.util.*;

public class PowersHeyYa extends NewDashPreset {
    public PowersHeyYa(LivingEntity self) {
        super(self);
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersHeyYa(entity);
    }


    public boolean dangerYapping = false;
    public boolean dangerYappingOn(){
        return dangerYapping;
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
        setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
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
            case SKILL_1_NORMAL, SKILL_1_CROUCH -> {
                toggleDangerYapClient();
            }
            case SKILL_2_NORMAL, SKILL_2_CROUCH -> {
                miningYapClient();
            }
            case SKILL_3_NORMAL, SKILL_3_CROUCH -> {
                dash();
            }
            case SKILL_4_NORMAL, SKILL_4_CROUCH -> {
                yapClient();
            }
        }
    }

    @Override
    public boolean setPowerOther(int move, int lastMove) {
        switch (move)
        {
            case PowerIndex.POWER_1 -> {
                return switchDangerMode();
            }
            case PowerIndex.POWER_4 -> {
                return doYap();
            }
            case PowerIndex.POWER_2 -> {
                return scoutForOresOnClient();
            }
        }
        return super.setPowerOther(move,lastMove);
    }

    public void toggleDangerYapClient(){
        this.tryPower(PowerIndex.POWER_1, true);
        tryPowerPacket(PowerIndex.POWER_1);
    }
    public void miningYapClient(){
        if (!this.onCooldown(PowerIndex.SKILL_2)) {
            this.tryPower(PowerIndex.POWER_2, true);
            tryPowerPacket(PowerIndex.POWER_2);
        }
    }
    public void yapClient(){
        if (!this.onCooldown(PowerIndex.SKILL_4)) {
            this.tryPower(PowerIndex.POWER_4, true);
            tryPowerPacket(PowerIndex.POWER_4);
        }
    }
    /**Let the client brunt the task of mass scanning blocks so it doesn't lag server TPS
     * also instill block limits so the packet count is sane*/
    public boolean scoutForOresOnClient(){
        this.setCooldown(PowerIndex.SKILL_2,ClientNetworking.getAppropriateConfig().heyYaSettings.oreDetectionCooldown);
        if (isClient()){
            int range = ClientNetworking.getAppropriateConfig().heyYaSettings.oreDetectionRadius;
            int oremaxout = 0;
            int oremaxoutMax = ClientNetworking.getAppropriateConfig().heyYaSettings.oreDetectionMaximum;
            for (int x = -range; x < range; x++){
                for (int y = -range; y < range; y++){
                    for (int z = -range; z < range; z++){
                        BlockPos pos = this.self.blockPosition().offset(new BlockPos(x,y,z));
                        BlockState blk = this.self.level().getBlockState(pos);
                        if (blk.is(ModPacketHandler.PLATFORM_ACCESS.getOreTag())){
                            if (confirmSpace(pos.below()) || confirmSpace(pos.east()) ||
                                    confirmSpace(pos.west()) || confirmSpace(pos.north()) ||
                                    confirmSpace(pos.south()) || confirmSpace(pos.above())){
                                oremaxout++;
                                if (oremaxout >= oremaxoutMax){
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        } else {
            yapSounds();
                if (isEvilYapper()){
                    ((ServerPlayer) this.self).displayClientMessage(Component.translatable("text.roundabout.hey_ya_messaging.mining.evil.no_"+(Mth.floor(Math.random() * ClientNetworking.getAppropriateConfig().heyYaSettings.numberOfEvilMiningYapLines)+1)).withStyle(ChatFormatting.GOLD), true);
                } else {
                    ((ServerPlayer) this.self).displayClientMessage(Component.translatable("text.roundabout.hey_ya_messaging.mining.no_"+(Mth.floor(Math.random() * ClientNetworking.getAppropriateConfig().heyYaSettings.numberOfMiningYapLines)+1)).withStyle(ChatFormatting.GOLD), true);
                }
        }
        return true;
    }

    public void yapSounds(){

        if (!isYapping()){
            setYapTime(40);
            getStandUserSelf().roundabout$setStandAnimation(YAP);
            playStandUserOnlySoundsIfNearby((byte) (61 + Mth.floor(Math.random() * 7)), 100, false, true);
        }
    }

    /**if the block is legal to replace, send packet to server which will confirm if it is*/
    public boolean confirmSpace(BlockPos pos){
        BlockState state = this.self.level().getBlockState(pos);
        if (state.isAir() || state.is(Blocks.WATER)
        ){
            tryBlockPosPowerPacket(PowerIndex.POWER_4_BONUS,pos);

            return true;
        }
        return false;
    }


    public void tryHighlightOre(BlockPos pos){
        BlockState state = this.self.level().getBlockState(pos);
        if (state.isAir()
        ){
            this.self.level().setBlockAndUpdate(pos, ModBlocks.MINING_ALERT_BLOCK.defaultBlockState().setValue(MiningAlertBlock.WATERLOGGED, Boolean.valueOf(false)));
        } else if ( state.is(Blocks.WATER)){
            this.self.level().setBlockAndUpdate(pos, ModBlocks.MINING_ALERT_BLOCK.defaultBlockState().setValue(MiningAlertBlock.WATERLOGGED, Boolean.valueOf(true)));
        }
    }

    public boolean switchDangerMode(){
        dangerYapping = !dangerYapping;
        return true;
    }
    public boolean doYap(){
        this.setCooldown(PowerIndex.SKILL_4,ClientNetworking.getAppropriateConfig().heyYaSettings.yapCooldown);
        if (!isClient()){
            yapSounds();
            if (isEvilYapper()){
                ((ServerPlayer) this.self).displayClientMessage(Component.translatable("text.roundabout.hey_ya_messaging.evil.no_"+(Mth.floor(Math.random() * ClientNetworking.getAppropriateConfig().heyYaSettings.numberOfEvilYapLines)+1)).withStyle(ChatFormatting.GOLD), true);
            } else {
                ((ServerPlayer) this.self).displayClientMessage(Component.translatable("text.roundabout.hey_ya_messaging.no_"+(Mth.floor(Math.random() * ClientNetworking.getAppropriateConfig().heyYaSettings.numberOfYapLines)+1)).withStyle(ChatFormatting.GOLD), true);
            }
        }
        return true;
    }

    @Override
    public boolean tryBlockPosPower(int move, boolean forced,  BlockPos Pos) {
        switch (move) {
            case PowerIndex.POWER_4_BONUS -> {
                /**The server accepts the block pos of the ore the client detected*/
                tryHighlightOre(Pos);
                return true;
            }
        }
        return super.tryPower(move, forced);
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
        if (!isClient()) {
            if (isYapping()) {
                getStandUserSelf().roundabout$setStandAnimation(YAP);
            } else {
                getStandUserSelf().roundabout$setStandAnimation(NONE);
            }
            tickYapping();
        }
        /**Yap animation based on using power*/
        super.tickPower();
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
    public void reactToAggro(Mob mob){
        if (dangerYappingOn()) {
            /**If a mob tries to set its attack target to you again, does not repeat yapping*/
            if (!(mob.getTarget() != null && mob.getTarget().is(this.self)) && !(mob.getLastHurtByMob() != null && mob.getLastHurtByMob().is(this.self))) {
                /**This function assures the aggro isn't passive mob aggro like animals running*/
                if (MainUtil.getIfMobIsAttacking(mob)) {
                    yapSounds();
                    if (isEvilYapper()){
                        ((ServerPlayer) this.self).displayClientMessage(Component.translatable("text.roundabout.hey_ya_messaging.danger.evil.no_"+(Mth.floor(Math.random() * ClientNetworking.getAppropriateConfig().heyYaSettings.numberOfEvilDangerYapLines)+1), mob.getDisplayName()).withStyle(ChatFormatting.RED), true);
                    } else {
                        ((ServerPlayer) this.self).displayClientMessage(Component.translatable("text.roundabout.hey_ya_messaging.danger.no_"+(Mth.floor(Math.random() * ClientNetworking.getAppropriateConfig().heyYaSettings.numberOfDangerYapLines)+1), mob.getDisplayName()).withStyle(ChatFormatting.RED), true);
                    }
                }
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
        return Component.literal(  "Hydra").withStyle(ChatFormatting.YELLOW);
    }

    public int yapTime = 0;
    public boolean isYapping(){
        return yapTime > 0;
    }
    public int getYapTime(){
        return yapTime;
    }
    public void setYapTime(int yapTime){
        this.yapTime = yapTime;
    }
    public void tickYapping(){
        if (this.yapTime > 0){

            this.yapTime--;
        }
    }

    public static final byte
            YAP = 1;

    public static final byte
            MANGA = 1,
            GOTHIC = 2,
            VOLUME_2 = 3,
            CHAPTER_24 = 4,
            GREENER = 5,
            WORLD = 6,
            FIRE_AND_ICE = 7,
            WARDEN = 8,
            ICE_COLD = 9,
            VILLAGER = 10,
            GEEZER = 11,
            SKELETON = 12,
            WITHER = 13,
            TUSK = 14,
            DEVIL = 15,
            HELL_NAH = 16,
            ALIEN = 17,
            AMERICA = 18,
            ZOMBIE = 19;

    public static final byte
            YAP_1 = 61,
            YAP_2 = 62,
            YAP_3 = 63,
            YAP_4 = 64,
            YAP_5 = 65,
            YAP_6 = 66,
            YAP_7 = 67;
    @Override
    public List<Byte> getSkinList() {
        return Arrays.asList(
                MANGA,
                VOLUME_2,
                CHAPTER_24,
                GOTHIC,
                GREENER,
                FIRE_AND_ICE,
                ICE_COLD,
                GEEZER,
                ALIEN,
                AMERICA,
                WORLD,
                TUSK,
                VILLAGER,
                ZOMBIE,
                SKELETON,
                WITHER,
                WARDEN,
                DEVIL,
                HELL_NAH
        );
    }

    public boolean isEvilYapper(){
        switch (getStandUserSelf().roundabout$getStandSkin())
        {
            case PowersHeyYa.DEVIL,PowersHeyYa.HELL_NAH,
                    PowersHeyYa.WORLD, PowersHeyYa.WARDEN -> {return true;}
            default -> {return false;}
        }
    }
    @Override public Component getSkinName(byte skinId) {
        return switch (skinId)
        {
            case GOTHIC -> Component.translatable("skins.roundabout.hey_ya.gothic");
            case VOLUME_2 -> Component.translatable("skins.roundabout.hey_ya.volume_2");
            case CHAPTER_24 -> Component.translatable("skins.roundabout.hey_ya.chapter_24");
            case GREENER -> Component.translatable("skins.roundabout.hey_ya.greener");
            case WORLD -> Component.translatable("skins.roundabout.hey_ya.world");
            case WARDEN -> Component.translatable("skins.roundabout.hey_ya.warden");
            case FIRE_AND_ICE -> Component.translatable("skins.roundabout.hey_ya.fire_and_ice");
            case ICE_COLD -> Component.translatable("skins.roundabout.hey_ya.ice_cold");
            case VILLAGER -> Component.translatable("skins.roundabout.hey_ya.villager");
            case GEEZER -> Component.translatable("skins.roundabout.hey_ya.geezer");
            case DEVIL -> Component.translatable("skins.roundabout.hey_ya.devil");
            case TUSK -> Component.translatable("skins.roundabout.hey_ya.tusk");
            case SKELETON -> Component.translatable("skins.roundabout.hey_ya.skeleton");
            case WITHER -> Component.translatable("skins.roundabout.hey_ya.wither");
            case HELL_NAH -> Component.translatable("skins.roundabout.hey_ya.hell_nah");
            case ALIEN -> Component.translatable("skins.roundabout.hey_ya.alien");
            case AMERICA -> Component.translatable("skins.roundabout.hey_ya.america");
            case ZOMBIE -> Component.translatable("skins.roundabout.hey_ya.zombie");
            default -> Component.translatable("skins.roundabout.hey_ya.manga");
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
                return ModSounds.HEY_YA_SUMMON_EVENT;
            }
            case YAP_1 -> {
                return ModSounds.HEY_YA_1_EVENT;
            }
            case YAP_2 -> {
                return ModSounds.HEY_YA_2_EVENT;
            }
            case YAP_3 -> {
                return ModSounds.HEY_YA_3_EVENT;
            }
            case YAP_4 -> {
                return ModSounds.HEY_YA_4_EVENT;
            }
            case YAP_5 -> {
                return ModSounds.HEY_YA_5_EVENT;
            }
            case YAP_6 -> {
                return ModSounds.HEY_YA_6_EVENT;
            }
            case YAP_7 -> {
                return ModSounds.HEY_YA_7_EVENT;
            }
        }
        return super.getSoundFromByte(soundChoice);
    }

    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypass) {
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 80, 0, "ability.roundabout.danger_yap",
                "instruction.roundabout.press_skill", StandIcons.DANGER_YAP, 1, level, bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 99, 0, "ability.roundabout.mining_yap",
                "instruction.roundabout.press_skill", StandIcons.MINING_YAP,2,level,bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 118, 0, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE,3,level,bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 80, 0, "ability.roundabout.yap_yap",
                "instruction.roundabout.press_skill", StandIcons.YAP_YAP,4,level,bypass));
        return $$1;
    }
}