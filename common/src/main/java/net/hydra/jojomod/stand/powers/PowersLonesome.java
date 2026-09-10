package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Arrays;
import java.util.List;

import static net.hydra.jojomod.util.MainUtil.raytraceEntity;

public class PowersLonesome extends NewDashPreset {
    public PowersLonesome(LivingEntity self) {
        super(self);
    }

    @Override
    /**Override to add disable config*/
    public boolean isStandEnabled(){
        return ClientNetworking.getAppropriateConfig().ohLonesomeMeSettings.enableOhLonesomeMe;
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersLonesome(entity);
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
        setSkillIcon(context, x, y, 1, StandIcons.LONESOME_ARM_LAUNCH, PowerIndex.SKILL_1);

        /**It is sneak because all stands share this cooldown and SP/TW
         * shared it between dash and stand leap*/
        setSkillIcon(context, x, y, 2, StandIcons.LONESOME_LASSO, PowerIndex.SKILL_2);
        if(!isHoldingSneak()) {
            setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
        } else {
            setSkillIcon(context, x, y, 3, StandIcons.LONESOME_ZIPLINE, PowerIndex.SKILL_3);
        }
        setSkillIcon(context, x, y, 4, StandIcons.LONESOME_CRAWL, PowerIndex.SKILL_4);

        super.renderIcons(context, x, y);
    }

    @Override
    public void powerActivate(PowerContext context) {
        /**Making dash usable on both key presses*/
        switch (context)
        {
            case SKILL_1_NORMAL, SKILL_1_CROUCH -> {
                armLaunchClient();
            }
            case SKILL_2_NORMAL, SKILL_2_CROUCH -> {
                lassoClient();
            }
            case SKILL_3_NORMAL -> {
                dash();
            }
            case SKILL_3_CROUCH -> {
                ziplineClient();
            }
            case SKILL_4_NORMAL, SKILL_4_CROUCH -> {
                switchCrawlModeClient();
            }
        }
    }

    @Override
    public boolean setPowerOther(int move, int lastMove) {
        switch (move)
        {
            case PowerIndex.POWER_1 -> {
                return armLaunch();
            }

            case PowerIndex.POWER_2 -> {
                //return scoutForOresOnClient();
            }

            case PowerIndex.POWER_3_SNEAK -> {
                //return scoutForOresOnClient();
            }

            case PowerIndex.POWER_4 -> {
                return switchCrawlMode();
            }
        }
        return super.setPowerOther(move,lastMove);
    }

    public void armLaunchClient() {
        if (!this.onCooldown(PowerIndex.SKILL_1)) {
            this.tryPower(PowerIndex.POWER_1, true);
            tryPowerPacket(PowerIndex.POWER_1);
        }
    }

    public void lassoClient(){
        if (!this.onCooldown(PowerIndex.SKILL_2)) {
            this.tryPower(PowerIndex.POWER_2, true);
            tryPowerPacket(PowerIndex.POWER_2);
        }
    }

    public void ziplineClient(){
        if (!this.onCooldown(PowerIndex.SKILL_2_SNEAK)) {
            this.tryPower(PowerIndex.POWER_2_SNEAK, true);
            tryPowerPacket(PowerIndex.POWER_2_SNEAK);
        }
    }

    public void switchCrawlModeClient(){
        if (!this.onCooldown(PowerIndex.SKILL_4)) {
            this.tryPower(PowerIndex.POWER_4, true);
            tryPowerPacket(PowerIndex.POWER_4);
        }
    }

    public List rayCastBlockOrEntity(LivingEntity player){ // I KNOW THAT EVERYONE AND THEIR MOTHER HAS MADE ONE OF THESE, BUT IT'S GOOD PRACTICE!
        BlockHitResult rayBlock = getRayBlockHit(player, 20f);
        Entity rayEntity = raytraceEntity(player.level(), player, 2000f);
        return Arrays.asList(rayBlock, rayEntity); //i HATE java, WHY do i have to smuggle out my variables :sob:
    }

    public boolean crawlingOn(){
        return getStandUserSelf().roundabout$getUniqueStandModeToggle();
    }

    public boolean switchCrawlMode(){
        getStandUserSelf().roundabout$setUniqueStandModeToggle(!crawlingOn());
        LivingEntity player = this.self;
        return true;
    }

    public boolean armLaunch(){
        if(!this.onCooldown(PowerIndex.SKILL_1)) {
            LivingEntity player = this.self;
            List rayCast = rayCastBlockOrEntity(player);
            BlockHitResult rayBlock = (BlockHitResult) rayCast.get(0);
            Entity rayEntity = (Entity) rayCast.get(1);
            float entityDistance = 400;
            if (rayEntity != null) {
                entityDistance = rayEntity.distanceTo(player);
            }

            float blockDistance = 400;
            if (rayBlock != null) { // I HATE NULLCHECKS
                blockDistance = (float) rayBlock.distanceTo(player);
            }
            System.out.println(blockDistance);
            System.out.println(entityDistance);

            if ( blockDistance <= entityDistance ) { //if the block is closer than the entity
                System.out.println(rayBlock.getBlockPos());
                System.out.println(rayBlock.getType());
            } else {
                System.out.println(rayEntity);
            }
        }
        return true;
    }


    public boolean crawl(){
        LivingEntity player = this.self;
        if (crawlingOn()){
            ((StandUser) player).rdbt$SetCrawlTicks(1);
        }
        return true;
    }

    @Override
    public void tickStandRejection(MobEffectInstance effect) {
    }
    @Override
    public void tickMobAI(LivingEntity attackTarget){
    }


    @Override
    public boolean tryPower(int move, boolean forced) {
        return super.tryPower(move, forced);
    }

    @Override
    public boolean isAttackIneptVisually(byte activeP, int slot) {
        return super.isAttackIneptVisually(activeP, slot);
    }


    @Override
    public void tickPower() {
        crawl();
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

    public static final byte
            YAP = 1;

    public static final byte
            MANGA = 1;

    @Override
    public List<Byte> getSkinList() {
        return Arrays.asList(
                MANGA
        );
    }

    @Override public Component getSkinName(byte skinId) {
        return switch (skinId)
        {
            //case GOTHIC -> Component.translatable("skins.roundabout.hey_ya.gothic");
            default -> Component.translatable("skins.roundabout.Lonesome.manga");
        };
    }

    @Override
    public boolean isSecondaryStand(){
        return true;
    } // </3
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
            //case YAP_1 -> {
            //    return ModSounds.HEY_YA_1_EVENT;
            //}
        }
        return super.getSoundFromByte(soundChoice);
    }

    @Override
    public boolean isWip(){
        return true;
    }

    @Override
    public Component ifWipListDevStatus(){
        return Component.translatable(  "roundabout.dev_status.active").withStyle(ChatFormatting.RED);
    }

    @Override
    public Component ifWipListDev(){
        return Component.literal(  "BinaryCell").withStyle(ChatFormatting.DARK_RED);
    }



    public byte worthinessType(){
        return HUMANOID_WORTHY;
    }
    public Component getPosName(byte posID) {
        if (posID == 1) {
            return Component.translatable("idle.roundabout.hey_ya_2");
        } else {
            return Component.translatable("idle.roundabout.hey_ya_1");
        }
    }

    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypass) {
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 80, 0, "ability.roundabout.danger_yap",
                "instruction.roundabout.press_skill", StandIcons.LONESOME_ARM_LAUNCH, 1, level, bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 99, 0, "ability.roundabout.mining_yap",
                "instruction.roundabout.press_skill", StandIcons.LONESOME_LASSO,2,level,bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 118, 0, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE,3,level,bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 80, 0, "ability.roundabout.yap_yap",
                "instruction.roundabout.press_skill", StandIcons.LONESOME_CRAWL,4,level,bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 99, 0, "ability.roundabout.yap_yap",
                "instruction.roundabout.press_skill", StandIcons.LONESOME_ZIPLINE,5,level,bypass));
        return $$1;
    }
}