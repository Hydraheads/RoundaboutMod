package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.*;
import net.hydra.jojomod.entity.substand.LifeTrackerEntity;
import net.hydra.jojomod.entity.substand.SheerHeartAttackEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.FancyLighterItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.mixin.StandUserEntity;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.hydra.jojomod.util.BlackSabbathPlayerInventory;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.animation.AnimationDefinition;
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
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.List;

public class PowersBlackSabbath extends NewDashPreset {
    public PowersBlackSabbath(LivingEntity self) {
        super(self);
    }

    static final byte
    CLIENT_SYNC = 100;

    public int moveMode = 0;

    @Override
    public void updatePowerInt(byte activePower, int data) {
        switch (activePower){
            case PowerIndex.POWER_1 -> {
                this.setCooldown(activePower,data);
            }
            case PowersBlackSabbath.CLIENT_SYNC -> {
                this.moveMode = data;
            }
        }
        super.updatePowerInt(activePower,data);

    }

    public void setNull(){
        moveMode = 0;
        if (this.getSelf() instanceof Player) {
            S2CPacketUtil.sendIntPowerDataPacket((Player) this.getSelf(),PowersBlackSabbath.CLIENT_SYNC, moveMode);
        }
    }


    @Override
    /**Override to add disable config*/
    public boolean isStandEnabled(){
        return ClientNetworking.getAppropriateConfig().blackSabbathSettings.enableBlackSabbath;
    }

    @Override
    public void onStandSummon(boolean desummon) {
        super.onStandSummon(desummon);

        if (!isClient()) {
            if (desummon) {

                if (active) {
                    active = false;
                    setTickDown(10);
                }

            }
            if(!desummon){
                moveMode = 0;
                if (this.getSelf() instanceof Player) {
                    S2CPacketUtil.sendIntPowerDataPacket((Player) this.getSelf(),PowersBlackSabbath.CLIENT_SYNC, moveMode);
                }
            }
        }
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersBlackSabbath(entity);
    }
    @Override
    public StandEntity getNewStandEntity(){
        byte skin = ((StandUser)this.getSelf()).roundabout$getStandSkin();

        return ModEntities.BLACK_SABBATH.create(this.getSelf().level());
    }

    public boolean isPlaced() {return this.getStandEntity(this.getSelf()) != null;}

    @Override
    public boolean canSummonStandAsEntity(){
        return false;
    }

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        setSkillIcon(context, x, y, 1, StandIcons.POLPO_INVENTORY, PowerIndex.SKILL_1);
        setSkillIcon(context, x, y, 2, StandIcons.POLPO_SELECTING_TARGET_MODE, PowerIndex.SKILL_2);
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
                    blackSelectClient();
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

    public void blackSelectClient(){
        this.setCooldown(PowerIndex.SKILL_1, 20);
        this.setCooldown(PowerIndex.SKILL_2, 20);
                tryPower(PowerIndex.POWER_2, true);
                tryPowerPacket(PowerIndex.POWER_2);
    }
    public void blackChestClient(){
        this.setCooldown(PowerIndex.SKILL_1, 20);
        this.setCooldown(PowerIndex.SKILL_2, 20);
            Vec3 blockHitResult = self.position();
            if (blockHitResult != null) {
                tryPosPower(PowerIndex.POWER_1, true, blockHitResult);
                tryPosPowerPacket(PowerIndex.POWER_1, blockHitResult);
            }
    }

    public int cooldownFinger = ClientNetworking.getAppropriateConfig().blackSabbathSettings.fingerBiteCooldown;

    @Override
    public boolean setPowerOther(int move, int lastMove) {
        switch (move)
        {
            case PowerIndex.POWER_2 -> {
                return toggleSelection();
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

    public boolean toggleSelection() {

        this.setActivePower(PowerIndex.POWER_2);
        Vec3 lvec = getLookAngleChest(self.getYRot(), self);
        Position pn = this.self.getEyePosition().add(lvec.scale(-1F));
        if (!this.getSelf().level().isClientSide()) {
            if (blackSelect == null || blackSelect.isRemoved()){
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.FIRE_WHOOSH_EVENT, SoundSource.PLAYERS, 1F, 0.8F);
                StandEntity stand = this.getNewStandEntity();
                if (stand != null) {
                    blackSelect = stand;
                    if (stand instanceof BlackSabbathEntity BE) {
                        Direction gravD = ((IGravityEntity)this.self).roundabout$getGravityDirection();
                        BE.absMoveTo(pn.x(), this.self.getY() + (this.self.getBbHeight() / 2.35F), pn.z());
                        BE.setMaster(this.self);
                        BE.setYRot((self.getYRot() % 360) - 180);
                        BE.setSkin(((StandUser) this.getSelf()).roundabout$getStandSkin());
                        this.getStandUserSelf().roundabout$standMount(BE);
                        BE.setShouldFloat(false);
                        BE.setShouldSelect(true);
                        BE.setDeltaMovement(Vec3.ZERO);
                        self.setDeltaMovement(Vec3.ZERO);
                        BE.incFadeOut((byte) 1);
                        this.self.level().addFreshEntity(BE);
                    }
                    moveMode = 2;
                    if (this.getSelf() instanceof Player) {
                        S2CPacketUtil.sendIntPowerDataPacket((Player) this.getSelf(),PowersBlackSabbath.CLIENT_SYNC, moveMode);
                    }
                }
            } else {
                blackSelect.forceDespawnSet = true;
                this.self.level().playSound(null, this.self.getX(), this.self.getY(),
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
                    ojiroSasame.level().playSound(null, ojiroSasame, SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 0.85F, 1.0F);
                }
                ItemEntity $$4 = new ItemEntity(ojiroSasame.level(), ojiroSasame.getX(),
                        ojiroSasame.getY() + ojiroSasame.getBbHeight() - 0.20, ojiroSasame.getZ(),
                        ModItems.FANCY_LIGHTER.getDefaultInstance());
                if($$4.getItem().getItem() instanceof FancyLighterItem FI && this.getSelf() instanceof ServerPlayer P){
                    FI.stuff($$4.getItem(), P);
                }
                $$4.setPickUpDelay(0);
                $$4.setDeltaMovement(Vec3.ZERO);
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
        if($$0.level().getBrightness(LightLayer.BLOCK, pos) < 11){
            if(isDay){
                 if ($$0.level().isRaining() || $$0.level().isThundering()){
                    return true;
                } else if ( $$0.level().getBrightness(LightLayer.SKY, atVec) < 12 ){
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
    public void tickStandRejection(MobEffectInstance effect) {

    }
    @Override
    public void tickMobAI(LivingEntity attackTarget){

    }
    @Override
    public boolean tryPower(int move, boolean forced) {
        switch (move) {
            case PowerIndex.POWER_1_BONUS -> {
                active = false;
                if (this.getStandEntity(this.getSelf()) != null) {
                    if (!this.getStandEntity(this.getSelf()).forceDespawnSet) {
                        this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.RATT_DEPLACE_EVENT, SoundSource.PLAYERS, 0.5F, 1F);
                    }
                    if(this.getStandEntity(self) instanceof BlackSabbathEntity b){
                        b.setTickDownSecond(10);
                    }
                }
                setTickDown(10);
            }
        }
        return super.tryPower(move, forced);
    }

    private BlockPos getValidPlacement(){

        Vec3 lvec = getLookAngleChest(self.getYRot(), self);
        Position pn = self.getEyePosition().add(lvec.scale(1));
        BlockPos bpos = BlockPos.containing(pn.x(), self.getY(), pn.z());
        BlockPos myPos = BlockPos.containing(self.getX(), self.getY(), self.getZ());
        BlockPos bposExtra =BlockPos.containing(pn.x(), self.getY() - 1, pn.z());

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
        if(slot == 2 && !this.checkIfYouAreInDark()) {
            return  true;
        }
        if(slot == 1 && (!this.checkIfYouAreInDark() || getValidPlacement() == null) && !self.isSwimming() ||slot == 1 && moveMode != 1 && moveMode != 0){
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

    @Override
    public void tickPower() {
        if(fingerEatingTick > 0){
            fingerEatingTick--;
        } if (fingerEatingTick == 1){
            if (self instanceof ServerPlayer pl){
                this.setAttackTimeDuring(0);
                ((IPlayerEntity)pl).roundabout$SetPoseEmote((byte) 0);
            }
        }

        if(this.getStandEntity(self) == null && moveMode != 0){
            moveMode = 0;
            if (this.getSelf() instanceof Player) {
                S2CPacketUtil.sendIntPowerDataPacket((Player) this.getSelf(),PowersBlackSabbath.CLIENT_SYNC, moveMode);
            }
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

        if(this.getStandEntity(self) == null){
            moveMode = 0;
            if (this.getSelf() instanceof Player) {
                S2CPacketUtil.sendIntPowerDataPacket((Player) this.getSelf(),PowersBlackSabbath.CLIENT_SYNC, moveMode);
            }
        } else {
            if(active && moveMode == 0){
                moveMode = 1;
                if (this.getSelf() instanceof Player) {
                    S2CPacketUtil.sendIntPowerDataPacket((Player) this.getSelf(),PowersBlackSabbath.CLIENT_SYNC, moveMode);
                }
            }
        }

        System.out.println(moveMode);

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
            }
        }

        getValidPlacement();

        super.tickPower();
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
            self.level().playSound(null, this.self.blockPosition(),
                    ModSounds.OPEN_BLACK_SABBATH_CHEST_EVENT, SoundSource.PLAYERS, 1F,
                    (float) (0.99f + Math.random() * 0.02f));
        }
        float evilY = shouldBSummonBot ? (float) self.getY() - 1 : (float) self.getY();
        if (stand instanceof BlackSabbathEntity BE) {
                BE.absMoveTo(pn.x(), evilY, pn.z());
                BE.setMaster(this.self);
                BE.setYRot((self.getYRot() % 360) - 180);
                BE.setSkin(((StandUser) this.getSelf()).roundabout$getStandSkin());
                this.getStandUserSelf().roundabout$standMount(BE);
                BE.setShouldFloat(true);
                BE.setShouldSelect(false);
                BE.setDeltaMovement(Vec3.ZERO);
                self.setDeltaMovement(Vec3.ZERO);
                BE.incFadeOut((byte) 1);
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

    @Override
    public float inputSpeedModifiers(float basis){
        if (isLarpingOjiroSasame() || active) {
            basis*=0.0f;
        }
        return super.inputSpeedModifiers(basis);
    }
    @Override
    public boolean cancelJump(){
        if (isLarpingOjiroSasame() || active) {
            return true;
        }
        return super.cancelJump();
    }

    @Override
    public boolean cancelSprintParticles(){
        if (isLarpingOjiroSasame() || active) {
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
            if (skinId == BlackSabbathEntity.PART_5_ANIME){
                return Component.translatable(  "skins.roundabout.black_sabbath.anime");
            } else if (skinId == BlackSabbathEntity.PART_5_MANGA){
                return Component.translatable(  "skins.roundabout.black_sabbath.manga");
            }else if (skinId == BlackSabbathEntity.BURNING) {
                return Component.translatable("skins.roundabout.black_sabbath.burning");
            }else if (skinId == BlackSabbathEntity.GIO_GIO){
                return Component.translatable(  "skins.roundabout.black_sabbath.giogio");
            } else if (skinId == BlackSabbathEntity.VERDANT){
                return Component.translatable(  "skins.roundabout.black_sabbath.verdant");
            } else if (skinId == BlackSabbathEntity.NIGHT){
                return Component.translatable(  "skins.roundabout.black_sabbath.night");
            } else if (skinId == BlackSabbathEntity.DEPARTURE){
                return Component.translatable(  "skins.roundabout.black_sabbath.departure");
            }else if (skinId == BlackSabbathEntity.PHANTOM){
                return Component.translatable(  "skins.roundabout.black_sabbath.phantom");
            }else if (skinId == BlackSabbathEntity.SWEET){
                return Component.translatable(  "skins.roundabout.black_sabbath.sweet");
            }else if(skinId == BlackSabbathEntity.SACTHOTH){
                return Component.translatable(  "skins.roundabout.black_sabbath.sacthoth");
            }
            return Component.translatable(  "skins.roundabout.black_sabbath.anime");
    }

    @Override
    public int getDisplayPowerInventoryScale() {
        return 35;
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
            PHANTOM_SKIN = 8,
            SWEET_SKIN = 9,
            SACTHOTH_SKIN = 10;

    @Override
    public List<Byte> getSkinList() {
        return Arrays.asList(
                ANIME_SKIN,
                MANGA_SKIN,
                BURNING_SKIN,
                GIO_GIO_SKIN,
                VERDANT_SABBATH_SKIN,
                NIGHT_SKIN,
                DEPARTURE_SKIN,
                PHANTOM_SKIN,
                SWEET_SKIN,
                SACTHOTH_SKIN
        );
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
            displayStand = ModEntities.BLACK_SABBATH.create(this.getSelf().level());
        } else if(displayStand instanceof BlackSabbathEntity BSE){
            BSE.coat_open.start(BSE.tickCount);
        }
        if (this.self instanceof Player PL && ((IPlayerEntity) PL).roundabout$getStandSkin() != displayStand.getSkin()) {
            displayStand = ModEntities.BLACK_SABBATH.create(this.getSelf().level());
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