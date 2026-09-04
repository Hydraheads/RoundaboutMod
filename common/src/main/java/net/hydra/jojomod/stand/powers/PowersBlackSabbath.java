package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.projectile.RoadRollerEntity;
import net.hydra.jojomod.entity.stand.*;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.FancyLighterItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.*;

public class PowersBlackSabbath extends NewDashPreset {
    public PowersBlackSabbath(LivingEntity self) {
        super(self);
    }

    static final byte
    CLIENT_SYNC = 100,
    CLIENT_SYNC_TARGET = 101,
    CLIENT_SYNC_TARGET_LIGHTER = 102,
    CLIENT_SYNC_REMOVE_TARGET = 103,
    CLIENT_SYNC_REMOVE_TARGET_LIST = 104,
    C2S_SYNC_DESTROY_LIST = 105;

    public int moveMode = 0;
    public int tickBeforeHunt = -1;
    public void setTickBeforeHunt(int tick){tickBeforeHunt = tick;}
    public List<LivingEntity> blackSabbathTargets = new ArrayList<>();

    public void cycleThroughBlackSabbathTargets(){
        if (blackSabbathTargets == null){
            blackSabbathTargets = new ArrayList<>();
        }
        List<LivingEntity> fogControlledEntities2 = new ArrayList<>(blackSabbathTargets) {};
        if (!fogControlledEntities2.isEmpty()){
            for (LivingEntity value : fogControlledEntities2) {
                if (value.isRemoved() || !value.isAlive()) {
                    removeTargetEntities(value);
                }
            }
        }
    }

    public boolean isHunting(){
        return moveMode == 3;
    }

    public List<LivingEntity> queryTargetEntities(){
        if (blackSabbathTargets == null){
            blackSabbathTargets = new ArrayList<>();
        }
        return blackSabbathTargets;
    }
    public List<LivingEntity> addTargetEntities(LivingEntity LE){
        if (blackSabbathTargets == null){
            blackSabbathTargets = new ArrayList<>();
        }
        blackSabbathTargets.add(LE);
        return blackSabbathTargets;
    }
    public List<LivingEntity> removeTargetEntities(LivingEntity LE){
        if (blackSabbathTargets == null){
            blackSabbathTargets = new ArrayList<>();
        }
        blackSabbathTargets.remove(LE);
        return blackSabbathTargets;
    }
    public List<LivingEntity> clearTargetEntities(){
        blackSabbathTargets = new ArrayList<>();
        setTickDown2(40);
        return blackSabbathTargets;
    }
    public List<LivingEntity> clearTargetEntitiesOnStandDeath(){
        blackSabbathTargets = new ArrayList<>();
        S2CPacketUtil.sendIntPowerDataPacket((Player) this.getSelf(),PowersBlackSabbath.C2S_SYNC_DESTROY_LIST, 0);
        return blackSabbathTargets;
    }

    @Override
    public void updatePowerInt(byte activePower, int data) {
        switch (activePower){
            case PowerIndex.POWER_1 -> {
                this.setCooldown(activePower,data);
            }
            case PowersBlackSabbath.CLIENT_SYNC -> {
                this.moveMode = data;
            }
            case PowersBlackSabbath.CLIENT_SYNC_TARGET_LIGHTER -> {
                this.EntityTargetOne = self.level().getEntity(data);
                if(self.level().getEntity(data) instanceof LivingEntity LE) {
                    this.addTargetEntities(LE);
                }
            }
            case PowersBlackSabbath.C2S_SYNC_DESTROY_LIST -> {
                this.clearTargetEntities();
            }
        }
        super.updatePowerInt(activePower,data);

    }

    public void setNull(){
        moveMode = 0;
        if (this.getSelf() instanceof Player) {
            S2CPacketUtil.sendIntPowerDataPacket((Player) this.getSelf(),PowersBlackSabbath.CLIENT_SYNC, 0);
        }
    }
    public void setOne(){
        moveMode = 1;
        if (this.getSelf() instanceof Player) {
            S2CPacketUtil.sendIntPowerDataPacket((Player) this.getSelf(),PowersBlackSabbath.CLIENT_SYNC, 1);
        }
    }
    public void setTwo(){
        moveMode = 2;
        if (this.getSelf() instanceof Player) {
            S2CPacketUtil.sendIntPowerDataPacket((Player) this.getSelf(),PowersBlackSabbath.CLIENT_SYNC, 2);
        }
    }
    public void setThree(){
        moveMode = 3;
        if (this.getSelf() instanceof Player) {
            S2CPacketUtil.sendIntPowerDataPacket((Player) this.getSelf(),PowersBlackSabbath.CLIENT_SYNC, 3);
        }
    }


    @Override
    /**Override to add disable config*/
    public boolean isStandEnabled(){
        return ClientNetworking.getAppropriateConfig().blackSabbathSettings.enableBlackSabbath;
    }

    @Override
    public void onStandSummon(boolean desummon) {
        if(this.getSelf() instanceof Player) {
            if (!isClient()) {
                if (desummon) {
                    if (active) {
                        active = false;
                        setTickDown(10);
                        blackSelect = null;
                    }
                    if (selecting) {
                        selecting = false;
                        blackSelect = null;
                    }
                    if (moveMode == 3) {
                        setNull();
                    }
                }
                if (!desummon) {
                    if (blackSabbathTargets.isEmpty()) {
                        setNull();
                        blackSelect = null;
                    } else {
                        blackSelectClient();
                    }
                }
            } else {
                if (desummon && moveMode == 2) {
                    this.setCooldown(PowerIndex.SKILL_1, 40);
                    this.setCooldown(PowerIndex.SKILL_2, 40);
                }
            }
        } else {
            if(desummon){
                setNull();
            }
        }
        super.onStandSummon(desummon);
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersBlackSabbath(entity);
    }
    @Override
    public StandEntity getNewStandEntity(){
        byte skin = ((StandUser)this.getSelf()).roundabout$getStandSkin();
        if (skin == BlackSabbathEntity.BEACH) {
            return ModEntities.BEACH_SABBATH.create(this.getSelf().level());
        } if(skin == BlackSabbathEntity.SANTA){
            return ModEntities.SANTA_SABBATH.create(this.getSelf().level());
        } if(skin == BlackSabbathEntity.COWBOY){
            return ModEntities.COWBOY_SABBATH.create(this.getSelf().level());
        }
        return ModEntities.BLACK_SABBATH.create(this.getSelf().level());
    }

    public boolean isPlaced() {return this.getStandEntity(this.getSelf()) != null;}

    @Override
    public boolean canSummonStandAsEntity(){
        return false;
    }

    public int securityTickDown = 20;
    void setSecurityTickDown(int i){securityTickDown = i;}

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        setSkillIcon(context, x, y, 1, StandIcons.POLPO_INVENTORY, PowerIndex.SKILL_1);

        if(moveMode == 2){
            if(!blackSabbathTargets.isEmpty()) {
                if(stupidTicksSon < 1) {
                    if (!isHoldingSneak()) {
                        setSkillIcon(context, x, y, 2, StandIcons.POLPO_SELECTING_TARGET_CONFIRM, PowerIndex.SKILL_2);
                    } else {
                        setSkillIcon(context, x, y, 2, StandIcons.POLPO_SELECTING_TARGET_NULL, PowerIndex.SKILL_2);
                    }
                } else {
                    setSkillIcon(context, x, y, 2, StandIcons.POLPO_SELECTING_TARGET_UNSELECTION, PowerIndex.SKILL_2);
                }
            } else {
                setSkillIcon(context, x, y, 2, StandIcons.POLPO_SELECTING_TARGET_MODE, PowerIndex.SKILL_2);
            }
        } else if (!blackSabbathTargets.isEmpty() && moveMode < 2){
            setSkillIcon(context, x, y, 2, StandIcons.POLPO_SELECTING_TARGET_MODE, PowerIndex.SKILL_2);
        } else if (moveMode == 1 || moveMode == 0){
            setSkillIcon(context, x, y, 2, StandIcons.POLPO_SELECTING_TARGET_MODE, PowerIndex.SKILL_2);
        } else {
            if(!isHoldingSneak()) {
                setSkillIcon(context, x, y, 2, StandIcons.POLPO_SELECTING_TARGET_UNSELECTION, PowerIndex.SKILL_2);
            } else {
                setSkillIcon(context, x, y, 2, StandIcons.POLPO_SELECTING_TARGET_NULL, PowerIndex.SKILL_2);
            }
        }

        setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
        setSkillIcon(context, x, y, 4, StandIcons.BITE_FINGERS_POLPO, PowerIndex.SKILL_4);

        super.renderIcons(context, x, y);
    }

    @Override
    public boolean tryPosPower(int move, boolean forced, Vec3 pos) {
        StandEntity SE = this.getStandEntity(this.getSelf());
        switch(move) {
            case PowerIndex.POWER_1 -> {
                this.active = true;
                this.setCooldown(PowerIndex.SKILL_1,20);
            }
        }
        return true;
    }

    @Override
    public void powerActivate(PowerContext context) {
        /**Making dash usable on both key presses*/
        switch (context)
        {
            case SKILL_1_NORMAL, SKILL_1_CROUCH ->{
                if(!onCooldown(PowerIndex.SKILL_1) && !isAttackIneptVisually(PowerIndex.SKILL_1, 1)) {
                    blackChestClient();
                }
            }
            case SKILL_2_NORMAL, SKILL_2_CROUCH -> {
                    if(!onCooldown(PowerIndex.SKILL_2) && !isAttackIneptVisually(PowerIndex.SKILL_2, 2)) {
                        if(moveMode != 3) {
                            if (moveMode == 2 && !blackSabbathTargets.isEmpty()) {
                                if (isHoldingSneak()) {
                                    killTargetListClient();
                                    this.setCooldown(PowerIndex.SKILL_2, 15);
                                } else {
                                    confirmListClient();
                                    this.setCooldown(PowerIndex.SKILL_2, 15);
                                }
                            } else {
                                blackSelectClient();
                            }
                        } else {
                            if(isHoldingSneak()){
                                killTargetListClient();
                                this.setCooldown(PowerIndex.SKILL_2, 15);
                            } else {
                                unselectEnemyClient();
                            }
                        }
                    }
            }
            case SKILL_3_NORMAL, SKILL_3_CROUCH -> {
                dash();
            }
            case SKILL_4_NORMAL, SKILL_4_CROUCH -> {
                if(!onCooldown(PowerIndex.SKILL_4)) {
                    biteFingersClient();
                }
            }
        }
    }

    public void unselectEnemyClient(){
        tryPower(PowerIndex.POWER_3_BONUS, true);
        tryPowerPacket(PowerIndex.POWER_3_BONUS);
    }
    public void blackSelectClient(){
        sharedChestSelectCooldown();
        setSecurityTickDown(40);
                tryPower(PowerIndex.POWER_2, true);
                tryPowerPacket(PowerIndex.POWER_2);
    }
    public void confirmListClient(){
        tryPower(PowerIndex.POWER_2_EXTRA, true);
        tryPowerPacket(PowerIndex.POWER_2_EXTRA);
    }
    public void setNullUniversal(){
        tryPower(PowerIndex.POWER_2_BONUS, true);
        tryPowerPacket(PowerIndex.POWER_2_BONUS);
    }
    public void blackChestClient(){
        sharedChestSelectCooldown();
        setSecurityTickDown(40);
            Vec3 blockHitResult = self.position();
            if (blockHitResult != null) {
                tryPosPower(PowerIndex.POWER_1, true, blockHitResult);
                tryPosPowerPacket(PowerIndex.POWER_1, blockHitResult);
            }
    }

    public void sharedChestSelectCooldown(){
        this.setCooldown(PowerIndex.SKILL_1, 40);
        this.setCooldown(PowerIndex.SKILL_2, 40);
    }

    public int cooldownFinger = ClientNetworking.getAppropriateConfig().blackSabbathSettings.fingerBiteCooldown;
private int stupidTicksSon = -1;
private void setStupidTicksSon(int ticks){stupidTicksSon = ticks;}

    @Override
    public boolean setPowerOther(int move, int lastMove) {
        switch (move)
        {
            case PowerIndex.POWER_2 -> {
                this.selecting = true;
                return two();
            }
            case PowerIndex.POWER_2_EXTRA -> {
                this.selecting = false;
                if(this.blackSelect != null) {
                    this.blackSelect.forceDespawnSet = true;
                    setTickBeforeHunt(20);
                }
                setStupidTicksSon(8);
            }
            case PowerIndex.POWER_2_BONUS -> {
                setNull();
            }
            case PowerIndex.POWER_3_BONUS -> {
                unselectClient();
            }
            case PowerIndex.POWER_4 -> {
                if(this.getSelf().getHealth() > 1) {
                    return biteFingers(this.self);
                }
            }
        }
        return super.setPowerOther(move,lastMove);
    }

    private void biteFingersClient(){
        if (!this.onCooldown(PowerIndex.SKILL_4) && !isAttackIneptVisually(PowerIndex.SKILL_4, 4)) {
            this.setCooldown(PowerIndex.SKILL_4, cooldownFinger);
            this.tryPower(PowerIndex.POWER_4, true);
            tryPowerPacket(PowerIndex.POWER_4);
        }
    }
    public StandEntity blackSelect = null;
    public boolean two(){
        Vec3 lvec = getLookAngleChest(self.getYRot(), self);
        Position pn = this.self.getEyePosition().add(lvec.scale(-0.75F));
        if(moveMode == 0) {
            if (!this.getSelf().level().isClientSide()) {
                if (blackSelect == null || blackSelect.isRemoved()){
                    playSoundIfPossible(self.level(),null, this.self.blockPosition(), ModSounds.FIRE_WHOOSH_EVENT, SoundSource.PLAYERS, 1F, 0.8F);
                    StandEntity stand = this.getNewStandEntity();
                    if (stand != null) {
                        blackSelect = stand;
                        if (stand instanceof BlackSabbathEntity BE) {
                            Direction gravD = ((IGravityEntity)this.self).roundabout$getGravityDirection();
                            BE.absMoveTo(pn.x(), this.self.getY() + (this.self.getBbHeight() / 2.45F), pn.z());
                            BE.setMaster(this.self);
                            BE.setYRot((self.getYRot() % 360) - 180);
                            BE.setSkin(((StandUser) this.getSelf()).roundabout$getStandSkin());
                            this.getStandUserSelf().roundabout$standMount(BE);
                            BE.setShouldFloat(false);
                            BE.setShouldSelect(true);
                            BE.setDeltaMovement(Vec3.ZERO);
                            self.setDeltaMovement(Vec3.ZERO);
                            BE.incFadeOut((byte) 1);
                            PowerTypes.copyPlaneOfExisting(self,BE);
                            this.self.level().addFreshEntity(BE);
                        }
                    }
                }
            }
        } else {
            if (!this.getSelf().level().isClientSide()) {
                blackSelect.forceDespawnSet = true;
                playSoundIfPossible(self.level(),null, this.self.getX(), this.self.getY(),
                        this.self.getZ(), ModSounds.SNAP_EVENT, this.self.getSoundSource(), 1F, 1.1F);
            }
        }
        return true;
    }
    private boolean biteFingers(LivingEntity ojiroSasame){
        if(this.self.isAlive()) {
            if (!isClient()) {
                if(ojiroSasame instanceof Player P && (!P.isCreative() || P.isSpectator())) {
                    ojiroSasame.hurt(ModDamageTypes.of(ojiroSasame.level(), DamageTypes.GENERIC_KILL), 1F);
                } if(ojiroSasame instanceof ServerPlayer P && (!P.isCreative() || P.isSpectator())){
                    this.eatFingerServer();
                    playSoundIfPossible(self.level(),ojiroSasame,SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 0.85F, 1.0F);
                }
                ItemEntity $$4 = new ItemEntity(ojiroSasame.level(), ojiroSasame.getX(),
                        ojiroSasame.getY() + ojiroSasame.getBbHeight() - 0.20, ojiroSasame.getZ(),
                        ModItems.FANCY_LIGHTER.getDefaultInstance());
                if($$4.getItem().getItem() instanceof FancyLighterItem FI && this.getSelf() instanceof ServerPlayer P){
                    FI.stuff($$4.getItem(), P);
                }
                $$4.setPickUpDelay(0);
                $$4.setDeltaMovement(Vec3.ZERO);
                PowerTypes.copyPlaneOfExisting(self,$$4);
                ojiroSasame.level().addFreshEntity($$4);
            }
        }
         return true;
    }
    public boolean checkIfYouAreInDark(){
        Entity $$0 = this.getSelf();
        BlockPos pos = $$0.blockPosition();
        long timeOfDay = $$0.level().getDayTime() % 24000L;
        Vec3 yes = $$0.getEyePosition();
        BlockPos atVec = BlockPos.containing(yes);
        boolean isDay = timeOfDay < 12555L || timeOfDay > 23470;
        if($$0.level().getBrightness(LightLayer.BLOCK, pos) < 12){
            if(isDay){
                 if ($$0.level().isRaining() || $$0.level().isThundering()){
                    return true;
                } else if ( $$0.level().getBrightness(LightLayer.SKY, atVec) < 14){
                    return true;
                }else {
                    return false;
                }
            } else if (!isDay){
                return true;
            } else {
                return false;
            }
        }
        return  false;
    }
    public boolean checkIfBposIsInDark(Vec3 bla){
        Entity $$0 = this.getSelf();
        BlockPos pos = new BlockPos((int)bla.x,(int)  bla.y,(int)  bla.z);
        long timeOfDay = $$0.level().getDayTime() % 24000L;
        BlockPos yes = new BlockPos((int)bla.x,(int)  bla.y + 1,(int)  bla.z);
        boolean isDay = timeOfDay < 12555L || timeOfDay > 23470;
        if($$0.level().getBrightness(LightLayer.BLOCK, pos) < 13){
            if(isDay){
                if ($$0.level().isRaining() || $$0.level().isThundering()){
                    return true;
                } else if ( $$0.level().getBrightness(LightLayer.SKY, yes) < 15){
                    return true;
                }else {
                    return false;
                }
            } else if (!isDay){
                return true;
            } else {
                return false;
            }
        }
        return  false;
    }
    public int fingerEatingTick = 0;
    private  void setFingerEatingTick(int tick){fingerEatingTick = tick;}
    public void eatFingerServer(){
            if (self instanceof ServerPlayer pl){
                setFingerEatingTick(16);
                ((IPlayerEntity)pl).roundabout$SetPoseEmote((byte) 37);
            }
    }
    @Override
    public void tickMobAI(LivingEntity attackTarget){
        if(attackTarget != null){
            if(!blackSabbathTargets.contains(attackTarget)) {
                if (blackSabbathTargets.isEmpty()) {
                    this.blackSabbathTargets.add(attackTarget);
                } else {
                    this.blackSabbathTargets.add(attackTarget);
                }

            }
            if(this.getStandEntity(self) == null && !blackSabbathTargets.isEmpty() && tickBeforeHunt < 1){
                this.setTickBeforeHunt(20);
                moveMode = 3;
            }
        }
        if(this.getSelf() instanceof AbstractIllager || this.getSelf() instanceof Raider || this.getSelf() instanceof Witch){
            List<Villager> lvent = this.self.level().getEntitiesOfClass(Villager.class, this.getSelf().getBoundingBox().inflate(40), (livingEntity) -> {
                return true;
            });
            if (lvent != null && !lvent.isEmpty()) {
                for (LivingEntity value : lvent) {
                    if (value.hasLineOfSight(this.getSelf())) {
                        if (!(value instanceof StandEntity || value instanceof RoadRollerEntity)) {
                            this.selectTargetSecond(value);
                        }
                    }
                }
            }
        }
    }
    @Override
    public boolean tryPower(int move, boolean forced) {
        switch (move) {
            case PowerIndex.POWER_1_BONUS -> {
                active = false;
                if (this.getStandEntity(this.getSelf()) != null) {
                    if (!this.getStandEntity(this.getSelf()).forceDespawnSet) {
                        playSoundIfPossible(self.level(),null, this.getSelf().blockPosition(), ModSounds.RATT_DEPLACE_EVENT, SoundSource.PLAYERS, 0.5F, 1F);
                    }
                    if(this.getStandEntity(self) instanceof BlackSabbathEntity b){
                        b.setTickDownSecond(10);
                    }
                }
                setTickDown(10);
            }
            case PowerIndex.POWER_2_BONUS -> {
                selecting = false;
                if (this.getStandEntity(this.getSelf()) != null) {
                    this.getStandEntity(self).forceDespawn(true);
                }
            }
        }
        return super.tryPower(move, forced);
    }

    private BlockPos getValidPlacement(){
        Vec3 lvec = getLookAngleChest(self.getYRot(), self);
        Position pn = self.getEyePosition().add(lvec.scale(1));
        BlockPos bpos = BlockPos.containing(pn.x(), Math.round(self.getY()), pn.z());
        BlockPos myPos = BlockPos.containing(self.getX(), self.getY(), self.getZ());
        BlockPos bposExtra =BlockPos.containing(pn.x(), Math.round(self.getY()) - 1, pn.z());
        if (self.onGround()) {
            if (this.self.level().getBlockState(bpos.below()).isSolid()) {
                setShouldBSummonBot(false);
                return bpos;
            } else if (this.self.level().getBlockState(bposExtra.below()).isSolid()) {
                setShouldBSummonBot(true);
                return bposExtra;
            }
        }
        var blockState = this.self.level().getBlockState(bpos);
        var blockState2 = this.self.level().getBlockState(bposExtra);
        if ((!blockState.canOcclude() || blockState.isAir()) && (!blockState2.canOcclude() || blockState2.isAir())) {
            return null;
        }
        return null;
    }
    @Override
    public boolean isAttackIneptVisually(byte activeP, int slot) {
        if(slot == 4 && this.getSelf().getHealth() <= 1) {
            return  true;
        }
        if (slot == 2 && ((!this.checkIfYouAreInDark() && !(moveMode == 3) || this.getStandEntity(self) != null && moveMode != 2 && moveMode != 3|| this.securityTickDown > 1 && this.blackSabbathTargets.isEmpty()))) {
            return true;
        }
        if(slot == 1 && (!this.checkIfYouAreInDark() || getValidPlacement() == null || self.isSwimming() || this.getStandEntity(self) != null || this.moveMode == 3 || this.securityTickDown > 1)){
            return true;
        }
        return super.isAttackIneptVisually(activeP, slot);
    }
    public boolean active = false;
    public boolean selecting = false;
    boolean shouldBSummonBot = false;
    void setShouldBSummonBot(boolean a){shouldBSummonBot = a;}
    public int tickDown = 10;
    void setTickDown(int t){tickDown = t;}
    public int tickDown2 = -10;
    public void setTickDown2(int ta){tickDown2 = ta;}
    public int visionTicks = 10;
    @Override
    public void tickPower() {
        if(fingerEatingTick > 0){
            fingerEatingTick--;
        } if (fingerEatingTick == 1) {
            if (self instanceof ServerPlayer pl) {
                this.setAttackTimeDuring(0);
                ((IPlayerEntity) pl).roundabout$SetPoseEmote((byte) 0);
            }
        }

        if(this.getStandEntity(self) == null && moveMode != 0 && moveMode != 3){
            setNull();
        }

        if(this.securityTickDown > 0){
            securityTickDown--;
        }

        if (this.getStandEntity(self) == null && this.getSelf().isAlive() && active) {
            if (PowerTypes.hasStandActive(self)) {
                if (!isClient()) {
                        if (this.getSelf().position() != null) {
                            placeBlackChest(this.getSelf().position());
                        }
                }
            }

        }

        if(tickBeforeHunt > 0){
            tickBeforeHunt--;
            if(tickBeforeHunt == 1){
                if(!this.isClient() && this.spawnTarget() != null && this.self.level() instanceof ServerLevel sl){
                    createTheMightyHunterOfTheShadows((findBlackSabbathSpawnPosition(sl,spawnTarget(), 25D)));
                }
            }
        }

        if(this.getStandEntity(self) != null){
        if(moveMode == 3 && this.blackSabbathTargets.isEmpty()){
            if(tickDown2 == -10) {
                if (this.getStandEntity(self) instanceof BlackSabbathEntity bs) {
                        bs.setHunting(false);
                        setTickDown2(40);
                    }
            }
            }
        }
        if(moveMode == 3){
            if(this.getStandEntity(self) instanceof BlackSabbathEntity BE && !this.isHunting()){
                BE.setHunting(false);
            }
        }
        if(tickDown2 >= -9){
            tickDown2--;
            if(tickDown2 == 35){
                if(this.getStandEntity(self) != null) {
                    this.getStandEntity(self).forceDespawnSet = true;
                }
            }
            if(tickDown2 == 15){
                if(this.getStandEntity(self) != null && this.getStandEntity(self).getHealth() <= 0) {
                    if(this.getSelf() instanceof Player) {
                        clearTargetEntitiesOnStandDeath();
                    } else {
                        clearTargetEntities();
                    }
                }
                if(!(this.getSelf() instanceof Player)){
                    moveMode = 0;
                }
                setNull();
            }
        }

        if(stupidTicksSon > 0){
            stupidTicksSon--;
            if (stupidTicksSon == 1){
                setThree();
                moveMode = 3;
            }
        }

        if (this.self.level().isClientSide()) {
            if (isPacketPlayer()) {
                if (moveMode == 2) {
                    if (visionTicks > -1) {
                        visionTicks--;
                    }
                } else {
                    if (visionTicks < 10) {
                        visionTicks++;
                    }
                }


                if (ClientNetworking.getAppropriateConfig().blackSabbathSettings.selectionModeUsesNightVision) {
                    if (moveMode == 2) {
                        if (!this.getSelf().hasEffect(MobEffects.NIGHT_VISION)) {
                            this.getSelf().addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, -1, 20, false, false), null);
                        }
                    } else {
                        MobEffectInstance ME = this.getSelf().getEffect(MobEffects.NIGHT_VISION);
                        if (ME != null && ME.isInfiniteDuration() && ME.getAmplifier() == 20) {
                            this.getSelf().removeEffect(MobEffects.NIGHT_VISION);
                        }
                    }
                } else {
                    MobEffectInstance ME = this.getSelf().getEffect(MobEffects.NIGHT_VISION);
                    if (ME != null && ME.isInfiniteDuration() && ME.getAmplifier() == 20) {
                        this.getSelf().removeEffect(MobEffects.NIGHT_VISION);
                    }
                }
            }
        }

        if(this.getStandEntity(self) == null){
            if(moveMode != 0 && moveMode != 3) {
                setNull();
                active = false;
            }
        } else {
            if(active && moveMode == 0){
                setOne();
            }
            if(selecting && moveMode == 0){
                setTwo();
            }
        }

        if(self instanceof Player PL) {
            if (self != null && this.getStandEntity(self) instanceof BlackSabbathEntity BSE) {
                if(active){
                    if(tickDown > 1){
                        tickDown--;
                        if (tickDown == 1){
                            BSE.openCustomInventoryScreen(PL);
                        }
                    }
                    if(!checkIfYouAreInDark()){
                        this.active = false;
                        this.RecallClient();
                    }
                }
                if(selecting){
                    if(!checkIfYouAreInDark()){
                        this.selecting = false;
                        BSE.forceDespawn(true);
                    }
                }
            }
        }

        String test = "";

        if(this.isClient()){
            test = "Level is Clientside";
        } else {
            test = "Level is Serverside";
        }

        //System.out.println(tickDown2 + ". " + test);
       // System.out.println(blackSabbathTargets + ". " + test);
       // System.out.println(moveMode + ". " + test);
        if(EntityTargetOne != null) {
            DimensionType T = this.getSelf().level().dimensionType();
            DimensionType t = this.EntityTargetOne.level().dimensionType();
            if (EntityTargetOne.isRemoved() || !EntityTargetOne.isAlive()) {
                EntityTargetOne = null;
                selectTargetNull();
            } else if(t != T){
                EntityTargetOne = null;
                selectTargetNull();
            }
        }

        if(this.getStandEntity(this.getSelf()) != null) {
            DimensionType t = this.getStandEntity(this.getSelf()).level().dimensionType();
            DimensionType T = this.getSelf().level().dimensionType();


            if (t != T) {
                ((StandUser) this.self).roundabout$setActive(false);
                this.getStandEntity(this.getSelf()).discard();
                setNull();
            }
        }

        getValidPlacement();
        cycleThroughBlackSabbathTargets();
        super.tickPower();
    }

    public void tickPowerEnd() {
        if (blackSelect != null && this.getStandEntity(self) != null) {
            if (!this.self.level().isClientSide()) {
                blackRotation((BlackSabbathEntity) this.getStandEntity(self));
            }
        }
    }

    @Nullable
    public Vec3 findBlackSabbathSpawnPosition(
            ServerLevel level,
            LivingEntity lent,
            double radius
    ) {
        int attempts = 100;
        double minDistance = 2.5D+ (0.5);
        for (int i = 0; i < attempts; i++) {
            double angle = Math.random() * Math.PI * 2.0D;
            double distance = minDistance
                    + Math.sqrt(Math.random()) * (radius - minDistance);
            double x = lent.getX() + Math.cos(angle) * distance;
            double z = lent.getZ() + Math.sin(angle) * distance;
            int baseY = Mth.floor(lent.getY());
            for (int yOffset = 0; yOffset <= 10; yOffset++) {
                double y = baseY + yOffset;
                Vec3 candidate = new Vec3(x, y, z);
                BlockPos bpos = BlockPos.containing(x, y - 0.1, z);
                var blockState = this.self.level().getBlockState(bpos);
                AABB yesbox = ModEntities.BLACK_SABBATH.getAABB(lent.getX(),lent.getY(),lent.getZ());
                AABB testBox = yesbox.move(
                        candidate.x - lent.getX(),
                        candidate.y - lent.getY(),
                        candidate.z - lent.getZ()
                );
                if (level.noCollision(lent, testBox) && !blockState.isAir() && checkIfBposIsInDark(candidate)) {
                    return candidate;
                } else {
                    for (int yOffset2 = -1; yOffset2 >= -8; yOffset2--) {
                        double y2 = baseY + yOffset2;
                        Vec3 candidate2 = new Vec3(x, y2, z);
                        BlockPos bpos2 = BlockPos.containing(x, y2 - 0.1, z);
                        var blockState2 = this.self.level().getBlockState(bpos2);
                        AABB yesbox2 = ModEntities.BLACK_SABBATH.getAABB(lent.getX(),lent.getY(),lent.getZ());
                        AABB testBox2 = yesbox2.move(
                                candidate2.x - lent.getX(),
                                candidate2.y - lent.getY(),
                                candidate2.z - lent.getZ()
                        );
                        if (level.noCollision(lent, testBox2) && !blockState2.isAir() && checkIfBposIsInDark(candidate)) {
                            return candidate2;
                        }
                    }
                }
            }
        }
        return null;
    }
    private LivingEntity spawnTarget(){
        if(!this.blackSabbathTargets.isEmpty()){
            LivingEntity lv = this.getSelf().level().getNearestEntity(blackSabbathTargets, MainUtil.OFFER_TARGER_CONTEXT, null,
                    this.self.getX(), this.self.getY(), this.self.getZ());
            return lv;
        }
        return null;
    }
    public void createTheMightyHunterOfTheShadows(Vec3 pos){
        if(isHunting()) {
            Random bandom = new Random();
            Integer randomInt = bandom.nextInt(361);
            if (!this.getSelf().level().isClientSide()) {
                if (this.getStandEntity(self) == null){
                    StandEntity stand = this.getNewStandEntity();
                    if (stand != null) {
                        if (stand instanceof BlackSabbathEntity BE) {
                            if(pos != null) {
                                BE.absMoveTo(pos.x(), pos.y(), pos.z());
                            } else {
                                BE.absMoveTo(self.getX(), self.getY(), self.getZ());
                            }
                            BE.setYRot(randomInt);
                            BE.setMaster(this.self);
                            BE.setSkin(((StandUser) this.getSelf()).roundabout$getStandSkin());
                            this.getStandUserSelf().roundabout$standMount(BE);
                            BE.setHunting(true);
                            BE.setShouldFloat(false);
                            BE.setShouldSelect(false);
                            PowerTypes.copyPlaneOfExisting(self,BE);
                            this.self.level().addFreshEntity(BE);
                        }
                    }
                }
            }
        }
    }
    public void blackRotation(BlackSabbathEntity blackSabbathEntity) {
        transformSabbath(blackSabbathEntity);
    }
    public void transformSabbath(BlackSabbathEntity value){
        if (value != null) {
            if(moveMode == 2) {
                if (!this.self.level().isClientSide()) {
                    value.setOldPosAndRot();
                }
                if (this.self.level().isClientSide()) {
                    value.setYRot(value.getUser().getYHeadRot() % 360);
                    value.setXRot(value.getUser().getXRot());
                    value.setYBodyRot(value.getUser().getYHeadRot() % 360);
                    value.setYHeadRot(value.getUser().getYHeadRot() % 360);
                } else {
                    value.setYRot(value.getUser().getYHeadRot() % 360);
                    value.setXRot(value.getUser().getXRot());
                    value.setYBodyRot(value.getUser().getYHeadRot() % 360);
                    value.setYHeadRot(value.getUser().getYHeadRot() % 360);
                }
            }
        }
    }

    @Override
    public boolean highlightsEntity(Entity entity,Player player){
          if(self != null) {
              if(moveMode == 2 || moveMode == 3 && this.blackSabbathTargets.contains(entity)) {
                  Entity TE = MainUtil.getTargetEntity(self, 100, 10);
                  if (TE != null && TE.is(entity) && !(TE instanceof StandEntity && !TE.isAttackable()) && !TE.isInvisible() && TE instanceof LivingEntity && !(TE instanceof RoadRollerEntity)) {
                      return true;
                  }
              }
              if(this.getStandEntity(self) != null && entity.is(this.getStandEntity(self))){
                  return true;
              }
          }
        return false;
    }

    public int highlightsEntityColor(Entity ent, Player player){
        return 14877186;
    }

    public void placeBlackChest(Vec3 pos) {
        this.setCooldown(PowerIndex.SKILL_1, 20);
        if (!isClient()) {
            if(self instanceof Player PL) {
                blipStand(pos, PL);
            }
        }
    }

    public final Vec3 getLookAngleChest(float $$0, Entity selfie) {
        return ((StandUser)selfie).roundabout$calculateViewVectorButICanUseIt(0, selfie.getYRot());
    }

    public void blipStand(Vec3 pos, Player PL) {
        StandEntity stand = getNewStandEntity();
        Vec3 lvec = getLookAngleChest(self.getYRot(), self);
        Position pn = self.getEyePosition().add(lvec.scale(1));
        if (self.level() instanceof ServerLevel sl){
            playSoundIfPossible(self.level(),null, this.self.blockPosition(),
                    ModSounds.OPEN_BLACK_SABBATH_CHEST_EVENT, SoundSource.PLAYERS, 1F,
                    (float) (0.99f + Math.random() * 0.02f));
        }
        float evilY = shouldBSummonBot ? (float) self.getY() - 1 : (float) self.getY();
        if (stand instanceof BlackSabbathEntity BE) {
                BE.absMoveTo(pn.x(), Math.round(evilY), pn.z());
                BE.setMaster(this.self);
                BE.setYRot((self.getYRot() % 360) - 180);
                BE.setSkin(((StandUser) this.getSelf()).roundabout$getStandSkin());
                this.getStandUserSelf().roundabout$standMount(BE);
                BE.setShouldFloat(true);
                BE.setShouldSelect(false);
                BE.setDeltaMovement(Vec3.ZERO);
                self.setDeltaMovement(Vec3.ZERO);
                BE.incFadeOut((byte) 1);
                PowerTypes.copyPlaneOfExisting(self,BE);
                this.self.level().addFreshEntity(BE);
        }
    }
    public void RecallClient() {
        tryPower(PowerIndex.POWER_1_BONUS,true);
        tryPowerPacket(PowerIndex.POWER_1_BONUS);
    }
    public boolean interceptAttack(){
        return this.moveMode == 2;
    }
    boolean holdAttack = false;
    public void buttonInputAttack(boolean keyIsDown, Options options) {
        if (keyIsDown) {
            if (!holdAttack) {
                holdAttack = true;
                if (moveMode == 2) {
                    if(this.isHoldingSneak()){
                        unselectClient();
                    } else {
                        selectTargetClient();
                    }
                }
            }
        } else if (holdAttack){
            holdAttack = false;
        }
    }
    public Entity EntityTargetOne = null;
    @Override
    public boolean tryIntPower(int move, boolean forced, int value) {
        switch (move) {

            case PowersBlackSabbath.CLIENT_SYNC_TARGET -> {
                this.setActivePower(PowersBlackSabbath.CLIENT_SYNC_TARGET);
                this.setAttackTime(0);
                Entity target = this.getSelf().level().getEntity(value);
                if(value != 0) {
                    if (target != null && target instanceof LivingEntity LE) {
                        if(!blackSabbathTargets.contains(LE)) {
                            this.addTargetEntities(LE);
                        }
                    }
                }
            }
            case PowersBlackSabbath.CLIENT_SYNC_REMOVE_TARGET -> {
                this.setActivePower(PowersBlackSabbath.CLIENT_SYNC_REMOVE_TARGET);
                this.setAttackTime(0);
                Entity target = this.getSelf().level().getEntity(value);
                if(value != 0) {
                    if (target != null && target instanceof LivingEntity LE) {
                        if(blackSabbathTargets.contains(LE)) {
                            this.removeTargetEntities(LE);
                        }
                    }
                }
            }
            case PowersBlackSabbath.CLIENT_SYNC_REMOVE_TARGET_LIST -> {
                this.setActivePower(PowersBlackSabbath.CLIENT_SYNC_REMOVE_TARGET_LIST);
                this.setAttackTime(0);
                setTickDown2(40);
                this.clearTargetEntities();
            }
        }
        return super.tryIntPower(move, forced, value);
    }
    private Entity getTarget() {
        Entity target = MainUtil.getTargetEntity(this.getSelf(),100,10);
        if (target instanceof LivingEntity LE) {
            if (LE.isInvisible() || LE instanceof RoadRollerEntity) {
                return null;
            }
        }
        return target;
    }
    public void selectTargetNull(){
        tryIntPower(PowersBlackSabbath.CLIENT_SYNC_TARGET, true, 0);
        tryIntPowerPacket(PowersBlackSabbath.CLIENT_SYNC_TARGET, 0);
    }
    public void unselectClient(){
        Entity TE = getTarget();
        if (TE != null) {
            if (TE instanceof LivingEntity LE) {
                if(blackSabbathTargets.contains(LE)) {
                    this.removeTargetEntities(LE);
                    tryIntPower(PowersBlackSabbath.CLIENT_SYNC_REMOVE_TARGET, true, LE.getId());
                    tryIntPowerPacket(PowersBlackSabbath.CLIENT_SYNC_REMOVE_TARGET, LE.getId());
                    this.self.playSound(ModSounds.CKB_NO_EVENT, 10F, 1F);
                }
            }
        }
    }
    public void killTargetListClient(){
        if(!blackSabbathTargets.isEmpty()) {
            this.clearTargetEntities();
            setTickDown2(40);
            tryIntPower(PowersBlackSabbath.CLIENT_SYNC_REMOVE_TARGET_LIST, true, 0);
            tryIntPowerPacket(PowersBlackSabbath.CLIENT_SYNC_REMOVE_TARGET_LIST, 0);
            this.self.playSound(ModSounds.CKB_NO_EVENT, 10F, 0.75F);
        }
    }
    public void selectTargetClient(){
        Entity TE = getTarget();
        if (TE != null) {
            if(TE instanceof LivingEntity LE) {
                if(!blackSabbathTargets.contains(LE)) {
                    this.addTargetEntities(LE);
                    int id = LE.getId();
                    tryIntPower(PowersBlackSabbath.CLIENT_SYNC_TARGET, true, id);
                    tryIntPowerPacket(PowersBlackSabbath.CLIENT_SYNC_TARGET, id);
                    this.self.playSound(ModSounds.CKB_YES_EVENT, 10F, 1F);
                }
            }
        }
    }
    public void selectTargetSecond(Entity ent){
        if(ent != null){
            if(EntityTargetOne == null) {
                int id = ent.getId();
                tryIntPower(PowersBlackSabbath.CLIENT_SYNC_TARGET, true, id);
                tryIntPowerPacket(PowersBlackSabbath.CLIENT_SYNC_TARGET, id);
                S2CPacketUtil.sendIntPowerDataPacket((Player) this.getSelf(),PowersBlackSabbath.CLIENT_SYNC_TARGET_LIGHTER, id);
            }
        }
    }
    @Override
    public float inputSpeedModifiers(float basis){
        if (isLarpingOjiroSasame() || moveMode == 1 || active) {
            basis*=0.0f;
        }
        return super.inputSpeedModifiers(basis);
    }
    @Override
    public boolean cancelJump(){
        if (isLarpingOjiroSasame() || moveMode == 1 || active) {
            return true;
        }
        return super.cancelJump();
    }
    @Override
    public boolean cancelSprintParticles(){
        if (isLarpingOjiroSasame() || moveMode == 1 || active) {
            return true;
        }
        return super.cancelSprintParticles();
    }
    public boolean isLarpingOjiroSasame(){
        return self instanceof Player pl && ((IPlayerEntity)pl).roundabout$GetPoseEmote() == 37;
    }
    @Override
    public void updateUniqueMoves() {
        super.updateUniqueMoves();
    }
    @Override public Component getSkinName(byte skinId) {
        if (skinId == BlackSabbathEntity.PART_5_ANIME) {
            return Component.translatable("skins.roundabout.black_sabbath.anime");
        } else if (skinId == BlackSabbathEntity.PART_5_MANGA) {
            return Component.translatable("skins.roundabout.black_sabbath.manga");
        } else if (skinId == BlackSabbathEntity.BURNING) {
            return Component.translatable("skins.roundabout.black_sabbath.burning");
        } else if (skinId == BlackSabbathEntity.GIO_GIO) {
            return Component.translatable("skins.roundabout.black_sabbath.giogio");
        } else if (skinId == BlackSabbathEntity.VERDANT) {
            return Component.translatable("skins.roundabout.black_sabbath.verdant");
        } else if (skinId == BlackSabbathEntity.NIGHT) {
            return Component.translatable("skins.roundabout.black_sabbath.night");
        } else if (skinId == BlackSabbathEntity.DEPARTURE) {
            return Component.translatable("skins.roundabout.black_sabbath.departure");
        } else if (skinId == BlackSabbathEntity.CHERRY){
        return Component.translatable("skins.roundabout.black_sabbath.cherry");
        } else if (skinId == BlackSabbathEntity.GRAPE){
            return Component.translatable("skins.roundabout.black_sabbath.grape");
        } else if (skinId == BlackSabbathEntity.MINT) {
            return Component.translatable("skins.roundabout.black_sabbath.mint");
        } else if (skinId == BlackSabbathEntity.TACO) {
            return Component.translatable("skins.roundabout.black_sabbath.taco");
        } else if (skinId == BlackSabbathEntity.WOOL) {
            return Component.translatable("skins.roundabout.black_sabbath.woven");
        } else if (skinId == BlackSabbathEntity.DAPPER){
            return Component.translatable("skins.roundabout.black_sabbath.dapper");
        } else if (skinId == BlackSabbathEntity.COPPER){
            return Component.translatable("skins.roundabout.black_sabbath.copper");
        } else if (skinId == BlackSabbathEntity.PHANTOM) {
            return Component.translatable("skins.roundabout.black_sabbath.phantom");
        } else if (skinId == BlackSabbathEntity.SWEET) {
            return Component.translatable("skins.roundabout.black_sabbath.sweet");
        } else if (skinId == BlackSabbathEntity.MAGMA) {
            return Component.translatable("skins.roundabout.black_sabbath.magma");
        }  else if (skinId == BlackSabbathEntity.OCULUS) {
            return Component.translatable("skins.roundabout.black_sabbath.oculus");
        } else if (skinId == BlackSabbathEntity.SACTHOTH) {
            return Component.translatable("skins.roundabout.black_sabbath.sacthoth");
        }else if (skinId == BlackSabbathEntity.COWBOY) {
            return Component.translatable("skins.roundabout.black_sabbath.cowboy");
        } else if (skinId == BlackSabbathEntity.BEACH){
            return Component.translatable("skins.roundabout.black_sabbath.beach");
        } else if (skinId == BlackSabbathEntity.SANTA) {
            return Component.translatable("skins.roundabout.black_sabbath.santa");
        }else if (skinId == BlackSabbathEntity.CRIMSON) {
            return Component.translatable("skins.roundabout.black_sabbath.crimson");
        }else if (skinId == BlackSabbathEntity.FUNGUS) {
            return Component.translatable("skins.roundabout.black_sabbath.mushroom");
        }
        return Component.translatable("skins.roundabout.black_sabbath.anime");
    }
    @Override
    public int getDisplayPowerInventoryScale() {
        byte skin = ((StandUser)this.getSelf()).roundabout$getStandSkin();
        if(skin == BlackSabbathEntity.SANTA){
            if(moveMode == 2){
                return 27;
            }
            return 29;
        }
        if(moveMode == 2){
            return 31;
        }
        return 33;
    }
    @Override
    public int getDisplayPowerInventoryYOffset() {
        return 0;
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
                return ModSounds.BLACK_SABBATH_SUMMON_EVENT;
            }

        }
        return super.getSoundFromByte(soundChoice);
    }
    public Component getPosName(byte posID){
        return Component.empty();
    }
    public List<Byte> getPosList(){
        List<Byte> $$1 = Lists.newArrayList();
        return $$1;
    }
    public static final byte
            ANIME_SKIN = 1,
            MANGA_SKIN = 2,
            BURNING_SKIN = 3,
            GIO_GIO_SKIN = 4,
            VERDANT_SABBATH_SKIN = 5,
            NIGHT_SKIN = 6,
            DEPARTURE_SKIN = 7,
            CHERRY = 8,
            GRAPE = 9,
            MINT = 10,
            TACO = 11,
            WOVEN = 12,
            FUNGUS = 13,
            DAPPER = 14,
            COPPER = 15,
            PHANTOM_SKIN = 16,
            SWEET_SKIN = 17,
            MAGMA = 18,
            OCULUS = 19,
            CRIMSON = 20,
            SACTHOTH_SKIN = 21,
            COWBOY = 22,
            BEACH = 23,
            SANTA = 24;
    @Override
    public List<Byte> getSkinList() {
        if (isPlaced()) {
            if (getStandEntity(this.getSelf()) instanceof BlackSabbathEntity BE) {
                List<Byte> list = Lists.newArrayList();
                list.add(BE.getSkin());
                return list;
            }
        } else {
            List<Byte> list = Lists.newArrayList();
            list.add(ANIME_SKIN);
            list.add(MANGA_SKIN);
            list.add(BURNING_SKIN);
            list.add(GIO_GIO_SKIN);
            list.add(VERDANT_SABBATH_SKIN);
            list.add(NIGHT_SKIN);
            list.add(DEPARTURE_SKIN);
            list.add(CHERRY);
            list.add(GRAPE);
            list.add(MINT);
            list.add(TACO);
            list.add(WOVEN);
            list.add(DAPPER);
            list.add(COPPER);
            list.add(FUNGUS);
            list.add(PHANTOM_SKIN);
            list.add(SWEET_SKIN);
            list.add(MAGMA);
            list.add(OCULUS);
            list.add(CRIMSON);
            list.add(SACTHOTH_SKIN);
            list.add(COWBOY);
            list.add(BEACH);
            list.add(SANTA);
            return list;
        }
        return null;
    }
    @Override
    public boolean returnFakeStandForHud(){
        if(this.self != null) {
            return !(this.getStandEntity(this.self) != null);
        }
        return false;
    }
    public StandEntity getStandForHUDIfFake(){
        if (displayStand == null){
            displayStand = this.getNewStandEntity();
        } else if(displayStand instanceof BlackSabbathEntity BSE){
            BSE.coat_open.start(BSE.tickCount);
        }
        if (this.self instanceof Player PL && ((IPlayerEntity) PL).roundabout$getStandSkin() != displayStand.getSkin()) {
            displayStand = this.getNewStandEntity();
            displayStand.setSkin(((IPlayerEntity) PL).roundabout$getStandSkin());
        }
        return displayStand;
    }
    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypass) {
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 80, 0, "ability.roundabout.danger_yap",
                "instruction.roundabout.press_skill", StandIcons.POLPO_INVENTORY, 1, level, bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 99, 0, "ability.roundabout.mining_yap",
                "instruction.roundabout.press_skill", StandIcons.POLPO_SELECTING_TARGET_MODE,2,level,bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 118, 0, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE,3,level,bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 80, 0, "ability.roundabout.yap_yap",
                "instruction.roundabout.press_skill", StandIcons.BITE_FINGERS_POLPO,4,level,bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 99, 0, "ability.roundabout.yap_yap",
                "instruction.roundabout.press_skill", StandIcons.POLPO_SELECTING_TARGET_NULL,4,level,bypass));
        return $$1;
    }
    @Override
    public boolean isWip(){
        return true;
    }
    @Override
    public Component ifWipListDevStatus(){
        return Component.translatable(  "roundabout.dev_status.active").withStyle(ChatFormatting.LIGHT_PURPLE);
    }
    @Override
    public Component ifWipListDev(){
        return Component.literal(  "14Kacper").withStyle(ChatFormatting.DARK_PURPLE);
    }
}