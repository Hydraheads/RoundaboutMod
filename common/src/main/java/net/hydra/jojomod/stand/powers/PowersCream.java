package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.*;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.*;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewPunchingStand;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class PowersCream extends NewPunchingStand {
    public PowersCream(LivingEntity self) {
        super(self);
    }

    @Override
    public StandEntity getNewStandEntity(){
        return ModEntities.CREAM.create(this.getSelf().level());
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity){
        return new PowersCream(entity);
    }

    @Override
    /**Override to add disable config*/
    public boolean isStandEnabled(){
        return ClientNetworking.getAppropriateConfig().creamSettings.enableCream;
    }

    @Override
    public void playSummonSound() {
        if (this.self.isCrouching()){
            return;
        }

        playStandUserOnlySoundsIfNearby(this.getSummonSound(), 10, false,false);
    }

    @Override
    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }

    public SoundEvent getSoundFromByte(byte soundChoice){
        byte bt = ((StandUser)this.getSelf()).roundabout$getStandSkin();
        if (soundChoice == SoundIndex.SUMMON_SOUND) {
            return ModSounds.CREAM_SUMMON_EVENT;
        }
        if (soundChoice == CREAM_VOID_ATTACK) {
            return ModSounds.CREAM_VOID_ATTACK_EVENT;
        }
        return super.getSoundFromByte(soundChoice);
    }

    public static final byte CREAM_VOID_ATTACK = 103;

    public void playVoidAttackEnterSound(){
        if (!this.self.level().isClientSide()) {
            byte skn = ((StandUser)this.getSelf()).roundabout$getStandSkin();
            playStandUserOnlySoundsIfNearby(CREAM_VOID_ATTACK, 27, false,true);
        }
    }

    @Override
    public int getMaxGuardPoints(){
        return ClientNetworking.getAppropriateConfig().creamSettings.creamGuardPoints;
    }

    @Override
    public float getMiningMultiplier() {
        return (float) (1F*(ClientNetworking.getAppropriateConfig().
                creamSettings.miningSpeedMultiplierCream *0.01));
    }

    @Override
    public int getMiningLevel() {
        return ClientNetworking.getAppropriateConfig().creamSettings.getMiningTierCream;
    }

    public int transformDirection = 0;

    public int insideVoidInt = 0;

    public int getVoidTime() {
        return insideVoidInt;
    }

    public void setVoidTime(int vt) {
        insideVoidInt = vt;
        if (!self.level().isClientSide && self instanceof Player player) {
            S2CPacketUtil.creamUpdateTimer(player, vt);
        }
    }

    public int transformTimer = 0;
    public boolean isTransforming = false;

    public int getTransformTimer() {
        return transformTimer;
    }

    public void setTransformTimer(int vt) {
        transformTimer = vt;
        if (!self.level().isClientSide && self instanceof Player player) {
            S2CPacketUtil.creamUpdateTransformTimer(player, vt);
        }
    }

    public int getTransformDirection() {
        return transformDirection;
    }

    public void setTransformDirection(int transformDirectionValue) {
        transformDirection = transformDirectionValue;
        if (!self.level().isClientSide && self instanceof Player player) {
            S2CPacketUtil.creamUpdateTransformDirection(player, transformDirectionValue);
        }
    }

    public boolean insideVoid = false;

    public Boolean previousMayFly;
    public Boolean previousFlying;
    public Boolean previousInvulnerable;
    public GameType gameMode;

    public Abilities abilities;

    @Override
    public void powerActivate(PowerContext context) {
        switch (context)
        {
            case SKILL_1_NORMAL, SKILL_1_CROUCH-> {
                enableVoidAttackClient();
            }
            case SKILL_2_NORMAL, SKILL_2_CROUCH-> {

            }
            case SKILL_3_NORMAL, SKILL_3_CROUCH -> {

            }
            case SKILL_4_NORMAL, SKILL_4_CROUCH -> {

            }
        }
    }

    public static final byte POWER_CREAM_VOID_ATTACK = PowerIndex.POWER_1;

    @Override
    public boolean setPowerOther(int move, int lastMove) {
        switch (move)
        {
            case VOID_ATTACK_ENABLE -> {
                enterVoidModeAttack();
            }
        }
        return super.setPowerOther(move,lastMove);
    }

    public static final byte VOID_ATTACK_ENABLE = 52;

    public void enableVoidAttackClient(){
        if (!this.onCooldown(PowerIndex.SKILL_2)) {
            this.tryPower(VOID_ATTACK_ENABLE, true);
            tryPowerPacket(VOID_ATTACK_ENABLE);
        }
    }

    public void enterVoidModeAttack() {
        if (!isTransforming && !insideVoid) {
            playVoidAttackEnterSound();
            setTransformDirection(1);
            setTransformTimer(0);
            isTransforming = true;
            if (self instanceof Player player) {
                if (!self.level().isClientSide) {
                    previousMayFly = ((Player) self).getAbilities().mayfly;
                    previousFlying = ((Player) self).getAbilities().flying;
                    previousInvulnerable = ((Player) self).getAbilities().invulnerable;
                    abilities = player.getAbilities();
                    gameMode = ((ServerPlayer) self).gameMode.getGameModeForPlayer();
                    if (((Player) self).isCreative()) {
                        ((Player) self).getAbilities().mayfly = true;
                        ((Player) self).getAbilities().invulnerable = true;
                    } else if (self.isSpectator()) {
                        ((Player) self).getAbilities().mayfly = true;
                        ((Player) self).getAbilities().invulnerable = true;
                    } else {
                        ((Player) self).getAbilities().mayfly = true;
                        ((Player) self).getAbilities().invulnerable = true;
                    }
                    Roundabout.LOGGER.info("Previous may fly:"+previousMayFly);
                    ((Player) self).onUpdateAbilities();
                }
                this.animateStand(CreamEntity.CREAM_EAT_VOID);
            }
        }
    }

    public void exitVoidModeAttack() {
        insideVoid = false;
        setTransformDirection(2);
        setTransformTimer(0);
        setVoidTime(0);
        isTransforming = true;
        if (!self.level().isClientSide) {
            GameType currentMode = ((ServerPlayer) self).gameMode.getGameModeForPlayer();
            boolean gameModeSwitched = false;

            if (currentMode != gameMode) {
                gameMode = currentMode;
                gameModeSwitched = true;
            }

            switch (gameMode) {
                case CREATIVE, SPECTATOR -> {
                    ((Player) self).getAbilities().mayfly = true;
                    ((Player) self).getAbilities().flying = previousFlying;
                    ((Player) self).getAbilities().invulnerable = true;
                }
                default -> {
                    if (!gameModeSwitched) {
                        ((Player) self).getAbilities().mayfly = previousMayFly;
                        ((Player) self).getAbilities().invulnerable = previousInvulnerable;
                    }
                    ((Player) self).getAbilities().flying = false;
                }
            }

            ((Player) self).onUpdateAbilities();
        }
        this.animateStand(CreamEntity.CREAM_UN_EAT);
    }

    public int creamAnimationIntForUnEat = 0;

    public void creamIdleAnimationReset() {
        this.animateStand(CreamEntity.IDLE);
    }

    public void creamBallAnimation() {
        this.animateStand(CreamEntity.CREAM_VOID_ATTACK_BALL);
    }

    public void actualVoidAttackDestruction() {
        if (!self.level().isClientSide && insideVoid) {
            int radius = 2;
            BlockPos playerPos = self.blockPosition().above();

            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        BlockPos pos = playerPos.offset(x, y, z);
                        if (!self.level().isEmptyBlock(pos)) {
                            BlockState state = self.level().getBlockState(pos);
                            float hardness = state.getDestroySpeed(self.level(), pos);
                            if (state.is(Blocks.WATER)) {
                                self.level().setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                            }
                            if (hardness >= 0 && hardness < 50) {
                                self.level().destroyBlock(pos, true, self);
                            }
                        }
                    }
                }
            }
        }
    }

    public void radialDamage() {
        if (!self.level().isClientSide()) {
            List<Entity> entityList = DamageHandler.genHitbox(self, self.getX(), self.getY(), self.getZ(),2.5,2.5,2.5);

            if (!entityList.isEmpty()) {
                for (Entity entity : entityList) {
                    if (entity != self && entity.isPickable() && entity.isAlive()) {
                        DamageSource src = ModDamageTypes.of(
                                self.level(),
                                ModDamageTypes.CREAM_VOID_BALL,
                                self,
                                self
                        );
                        entity.hurt(src, getVoidDamage(self));
                    }
                }
            }
        }
    }

    public float getVoidDamage(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod((float) ((float) 2.7* (ClientNetworking.getAppropriateConfig().
                    creamSettings.creamAttackMultOnPlayers*0.01)));
        } else {
            return levelupDamageMod((float) ((float) 10* (ClientNetworking.getAppropriateConfig().
                    creamSettings.creamAttackMultOnMobs*0.01)));
        }
    }

    @Override
    public void onStandSummon(boolean desummon){
        if (desummon) {
            if (insideVoidInt > 0 || transformTimer > 0) {
                exitVoidModeAttack();
            }
        }
    }

    public void tickPower() {
        super.tickPower();

        if (!self.level().isClientSide) {
            if (transformDirection == 2 && transformTimer > -1) {
                if (creamAnimationIntForUnEat >= 20) {
                    creamAnimationIntForUnEat = 0;
                    creamIdleAnimationReset();
                }
            }
        }

        if (!self.level().isClientSide) {
            if (insideVoidInt > 0) {
                radialDamage();
            }
        }

        if (!self.level().isClientSide) {
            if (insideVoidInt > 0) {
                if (self instanceof Player player) {
                    if (((Player) self).getAbilities().flying == false) {
                        ((Player) self).getAbilities().flying = true;
                        ((Player) self).onUpdateAbilities();
                    }
                }
            }
        }

        if (isTransforming) {
            if (getTransformDirection() == 1) {
                if (getTransformTimer() < 100) {
                    setTransformTimer(getTransformTimer() + 1);
                } else {
                    setTransformTimer(0);
                    insideVoid = true;
                    isTransforming = false;
                }
            } else if (getTransformDirection() == 2) {
                if (getTransformTimer() < 20) {
                    creamAnimationIntForUnEat++;
                    setTransformTimer(getTransformTimer() + 1);
                } else {
                    setTransformTimer(0);
                    setVoidTime(0);
                    insideVoid = false;
                    isTransforming = false;
                    setTransformDirection(0);
                }
            }
        }

        if (!self.level().isClientSide) {
            if (insideVoid) {
                actualVoidAttackDestruction();
                if (getVoidTime() < 400) {
                    creamBallAnimation();
                    setVoidTime(getVoidTime() + 1);
                } else if (getVoidTime() >= 400) {
                    exitVoidModeAttack();
                }
            }
        }
    }

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        setSkillIcon(context, x, y, 1, StandIcons.CREAM_VOID_ATTACK, PowerIndex.SKILL_1);
    }

    @Override
    public List<Byte> getSkinList(){
        List<Byte> $$1 = Lists.newArrayList();
        $$1.add(CreamEntity.PART_3_SKIN);
        if (this.getSelf() instanceof Player PE){
            byte Level = ((IPlayerEntity)PE).roundabout$getStandLevel();
            ItemStack goldDisc = ((StandUser)PE).roundabout$getStandDisc();
            boolean bypass = PE.isCreative() || (!goldDisc.isEmpty() && goldDisc.getItem() instanceof MaxStandDiscItem);
            if (Level > 1 || bypass){

            } if (Level > 2 || bypass){

            } if (Level > 3 || bypass){

            } if (Level > 4 || bypass){

            } if (Level > 5 || bypass){

            } if (Level > 6 || bypass){

            } if (((IPlayerEntity)PE).roundabout$getUnlockedBonusSkin() || bypass){

            }
        }
        return $$1;
    }

    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypass) {
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 80, 0, "ability.roundabout.void_attack",
                "instruction.roundabout.press_skill", StandIcons.CREAM_VOID_ATTACK, 1, level, bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 99, 0, "ability.roundabout.heel_plant",
                "instruction.roundabout.press_skill", StandIcons.GROUND_IMPLANT, 2, level, bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 118, 0, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE, 3, level, bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 80, 0, "ability.roundabout.wall_walk_move",
                "instruction.roundabout.press_skill", StandIcons.WALL_WALK, 3, level, bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 99, 0, "ability.roundabout.firm_swing",
                "instruction.roundabout.passive", StandIcons.FIRM_SWING, 0, level, bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 118, 0, "ability.roundabout.fall_disperse",
                "instruction.roundabout.passive", StandIcons.FALL_ABSORB, 0, level, bypass));
        return $$1;
    }

    @Override
    public Component getSkinName(byte skinId) {
        return getSkinNameT(skinId);
    }
    public static Component getSkinNameT(byte skinId){
        if (skinId == CreamEntity.PART_3_SKIN){
            return Component.translatable(  "skins.roundabout.the_world.base");
        }
        return Component.translatable(  "skins.roundabout.the_world.base");
    }

    @Override
    public void levelUp(){
        if (!this.getSelf().level().isClientSide() && this.getSelf() instanceof Player PE){
            IPlayerEntity ipe = ((IPlayerEntity) PE);
            byte level = ipe.roundabout$getStandLevel();
            if (level == 7){
                ((ServerPlayer) this.self).displayClientMessage(Component.translatable("leveling.roundabout.levelup.max.skins").
                        withStyle(ChatFormatting.AQUA), true);
            } else if (level == 2 || level == 3 || level == 4 || level == 6 || level == 5){
                ((ServerPlayer) this.self).displayClientMessage(Component.translatable("leveling.roundabout.levelup.both").
                        withStyle(ChatFormatting.AQUA), true);
            }
        }
        super.levelUp();
    }
}
