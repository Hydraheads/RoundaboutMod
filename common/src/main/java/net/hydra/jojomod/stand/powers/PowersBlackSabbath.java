package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.BlackSabbathEntity;
import net.hydra.jojomod.entity.stand.ManhattanTransferEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.substand.SheerHeartAttackEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.index.Poses;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.FancyLighterItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.hydra.jojomod.util.BlackSabbathPlayerInventory;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.List;

public class PowersBlackSabbath extends NewDashPreset {
    public PowersBlackSabbath(LivingEntity self) {
        super(self);
    }

    public BlackSabbathEntity BSE = null;
    private byte currentBlackStatus = BS_NONE;

    private static final byte
            ENTITY_BLACK_SABBATH = 53,
            BLACK_SABBATH = 54,
        BS_NONE = 0,
        BS_ACTIVE = 1;

    @Override
    /**Override to add disable config*/
    public boolean isStandEnabled(){
        return ClientNetworking.getAppropriateConfig().blackSabbathSettings.enableBlackSabbath;
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersBlackSabbath(entity);
    }

    public void syncBlackStatus(byte status) {
        this.currentBlackStatus = status;
        this.updatePowerInt(PowersBlackSabbath.BLACK_SABBATH, status);
        if (this.getSelf() instanceof Player) {
            S2CPacketUtil.sendIntPowerDataPacket((Player) this.getSelf(), PowersBlackSabbath.BLACK_SABBATH, status);
        }
    }

    public boolean canSummonStandAsEntity(){
        return false;
    }

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        setSkillIcon(context, x, y, 1, StandIcons.POLPO_INVENTORY, PowerIndex.SKILL_1);
       // setSkillIcon(context, x, y, 2, StandIcons.MINING_YAP, PowerIndex.SKILL_2);
        setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
        setSkillIcon(context, x, y, 4, StandIcons.BITE_FINGERS_POLPO, PowerIndex.SKILL_4);

        super.renderIcons(context, x, y);
    }

    @Override
    public void powerActivate(PowerContext context) {
        /**Making dash usable on both key presses*/
        switch (context)
        {
            case SKILL_1_NORMAL, SKILL_1_CROUCH ->{
                if(!onCooldown(PowerIndex.SKILL_1) && !isAttackIneptVisually(PowerIndex.SKILL_1, 1)) {

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

    public int cooldownFinger = ClientNetworking.getAppropriateConfig().blackSabbathSettings.fingerBiteCooldown;

    @Override
    public boolean setPowerOther(int move, int lastMove) {
        switch (move)
        {
            case PowerIndex.POWER_1 -> {
        //        return summonBlack();
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

    private boolean summonBlack(){
        if (!this.getSelf().level().isClientSide()) {
            if (BSE == null || BSE.isRemoved()) {
                BlackSabbathEntity sha = ModEntities.BLACK_SABBATH.create(this.getSelf().level());
                if (sha != null) {
                    sha.setUser(this.self);
                    sha.setXRot(this.self.getXRot());
                    sha.setYRot(this.self.getYRot());
                    if(this.self instanceof Player PL) {
                        displayStand.setSkin(((IPlayerEntity) PL).roundabout$getStandSkin());
                    }
                    this.self.level().addFreshEntity(sha);

                    BSE = sha;

                    this.syncBlackStatus(BS_ACTIVE);

                    S2CPacketUtil.sendIntPowerDataPacket((Player)this.getSelf(),PowersBlackSabbath.ENTITY_BLACK_SABBATH, this.BSE.getId());

                }

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
        boolean isDay = timeOfDay < 12555L || timeOfDay > 23470;
        if($$0.level().getBrightness(LightLayer.BLOCK, pos) < 11){
            if(isDay){
                if($$0.level().getBrightness(LightLayer.SKY, $$0.blockPosition()) < 11 || $$0.level().isRaining() || $$0.level().isThundering()){
                    return true;
                }
            } else if (!isDay){
                return true;
            } else {
                return false;
            }
        }

        return  false;
       // return $$0.level().getBrightness(LightLayer.BLOCK, pos) < 11 && ((isDay && !($$0.level().canSeeSky(BlockPos.containing($$0.getEyePosition())) && $$0.level().canSeeSky(BlockPos.containing($$0.position())) || !isDay)));
    }

    public void openPolpoInventory(){
        if(this.self instanceof Player player){
            if(player.level() != null) {
                this.self.playSound(SoundEvents.ENDER_CHEST_OPEN);
            }
            C2SPacketUtil.trySingleBytePacket(PacketDataIndex.SINGLE_BYTE_OPEN_BLACK_SABBATH_INVENTORY);
        }
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
        return super.tryPower(move, forced);
    }

    @Override
    public boolean isAttackIneptVisually(byte activeP, int slot) {
        if(slot == 4 && this.getSelf().getHealth() <= 1) {
            return  true;
        }
        if(slot == 1 && !this.checkIfYouAreInDark()){
            return true;
        }
        return super.isAttackIneptVisually(activeP, slot);
    }

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
        super.tickPower();
    }

    @Override
    public float inputSpeedModifiers(float basis){
        if (isLarpingOjiroSasame()) {
            basis*=0.0f;
        }
        return super.inputSpeedModifiers(basis);
    }
    @Override
    public boolean cancelJump(){
        if (isLarpingOjiroSasame()) {
            return true;
        }
        return super.cancelJump();
    }

    @Override
    public boolean cancelSprintParticles(){
        if (isLarpingOjiroSasame()) {
            return true;
        }
        return super.cancelSprintParticles();
    }

    public boolean isLarpingOjiroSasame(){
        return self instanceof Player pl && ((IPlayerEntity)pl).roundabout$GetPoseEmote() == 37;
    }


    @Override
    public void updateIntMove(int in) {

        super.updateIntMove(in);
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
            SWEET_SKIN = 9;

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
                SWEET_SKIN
        );
    }

    @Override
    public boolean returnFakeStandForHud(){
        return true;
    }

    public StandEntity getStandForHUDIfFake(){
        if (displayStand == null){
            displayStand = ModEntities.BLACK_SABBATH.create(this.getSelf().level());
        }
        if (this.self instanceof Player PL && ((IPlayerEntity) PL).roundabout$getStandSkin() != displayStand.getSkin()) {
            displayStand = ModEntities.BLACK_SABBATH.create(this.getSelf().level());
            displayStand.setSkin(((IPlayerEntity) PL).roundabout$getStandSkin());
            if(displayStand instanceof BlackSabbathEntity BSE){
                BSE.coat_open.start(BSE.tickCount);
            }
        }
        return displayStand;
    }

    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypass) {
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 80, 0, "ability.roundabout.danger_yap",
                "instruction.roundabout.press_skill", StandIcons.POLPO_INVENTORY, 1, level, bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 99, 0, "ability.roundabout.mining_yap",
                "instruction.roundabout.press_skill", StandIcons.PLUNDER_SELECTION,2,level,bypass));
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