package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.npcs.Aesthetician;
import net.hydra.jojomod.entity.projectile.CinderellaVisageDisplayEntity;
import net.hydra.jojomod.entity.stand.CinderellaEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.LuckyLipstickItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PowersCinderella extends NewDashPreset {

    public List<CinderellaVisageDisplayEntity> floatingVisages = new ArrayList<>();
    public PowersCinderella(LivingEntity self) {
        super(self);
    }
    @Override
    public StandEntity getNewStandEntity(){
        return ModEntities.CINDERELLA.create(this.getSelf().level());
    }
    @Override
    public boolean canSummonStand(){
        return true;
    }
    @Override
    public StandPowers generateStandPowers(LivingEntity entity){
        return new PowersCinderella(entity);
    }

    @Override
    public boolean isSecondaryStand(){
        return true;
    }
    @Override
    public List<Byte> getPosList(){
        List<Byte> $$1 = Lists.newArrayList();
        $$1.add((byte) 0);
        $$1.add((byte) 1);
        $$1.add((byte) 2);
        $$1.add((byte) 3);
        $$1.add((byte) 4);
        return $$1;
    }


    @Override
    public void powerActivate(PowerContext context) {
        switch (context)
        {
            case SKILL_1_NORMAL, SKILL_1_CROUCH-> {
                doUIClient();
            }
            case SKILL_2_NORMAL, SKILL_2_CROUCH-> {
                doDefaceClient();
            }
            case SKILL_3_NORMAL, SKILL_3_CROUCH -> {
                dash();
            }
        }
    }

    public void doDefaceClient(){
        if (!this.onCooldown(PowerIndex.SKILL_2)) {
            if (this.activePower == PowerIndex.POWER_2) {
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.NONE);
            } else {
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_2, true);
                ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.POWER_2);
            }
        }
    }

    public void doUIClient(){
        ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.POWER_1);
        ClientUtil.setCinderellaUI();
        hasUIOpen = true;
    }
    @Override
    public void tickPowerEnd() {
        if (floatingVisages != null && !floatingVisages.isEmpty()) {
            removeFloatingVisages();
            if (!this.self.level().isClientSide()) {
                floatingVisagesRotation();
            } else {
                lastSpinInt += maxSpinint;
            }
        }
    }
    public double spinint = 0;
    public double lastSpinInt = 0;
    public double maxSpinint = 4;

    public void floatingVisageInit(){
        if (floatingVisages == null) {
            floatingVisages = new ArrayList<>();
        }
    }
    public void floatingVisagesRotation() {
        floatingVisageInit();
        List<CinderellaVisageDisplayEntity> hurricaneSpecial2 = new ArrayList<>(floatingVisages) {
        };
        if (!hurricaneSpecial2.isEmpty()) {
            int totalnumber = hurricaneSpecial2.size();
            for (CinderellaVisageDisplayEntity value : hurricaneSpecial2) {
                transformFloatingVisages(value, totalnumber, this.self.getX(), this.self.getY(), this.self.getZ(),value.getSize());
            }
        }
    }


    @Override
    public Component getSkinName(byte skinId) {
        return getSkinNameT(skinId);
    }

    public static Component getSkinNameT(byte skinId){
        if (skinId == CinderellaEntity.MANGA_SKIN) {
            return Component.translatable("skins.roundabout.cinderella.manga");
        } if (skinId == CinderellaEntity.ZOMBIE_SKIN) {
            return Component.translatable("skins.roundabout.cinderella.zombie");
        } if (skinId == CinderellaEntity.JACK_SKIN) {
            return Component.translatable("skins.roundabout.cinderella.jack_in_the_box");
        } if (skinId == CinderellaEntity.BUSINESS_SKIN) {
            return Component.translatable("skins.roundabout.cinderella.business");
        }
        return Component.translatable("skins.roundabout.cinderella.base");
    }

    public void addFloatingVisage(CinderellaVisageDisplayEntity che){
        floatingVisageInit();
        floatingVisages.add(che);
    }
    public void transformFloatingVisages(CinderellaVisageDisplayEntity value, int totalnumber, double entityX, double entityY, double entityZ, double rsize){
        if (value != null) {
            int size = value.getSize();
            double distanceUp = 0.3;
            if (size < value.getMaxSize()) {
                size += value.getAccrualRate();
                value.setSize(size);
            }
            distanceUp += ((double) rsize / 20);
            double offset = 0;
            int number = value.getCrossNumber();
            if (this.self.level().isClientSide()) {
                if (number == 1) {
                    offset = 0;
                } else if (number == 2) {
                    offset = 90;
                } else if (number == 3) {
                    offset = 180;
                } else if (number == 4) {
                    offset = 270;
                } else if (number == 5) {
                    offset = 45;
                } else if (number == 6) {
                    offset = 135;
                } else if (number == 7) {
                    offset = 225;
                } else if (number == 8) {
                    offset = 315;
                }
                    offset += Mth.floor(spinint/2);

                if (offset > 360) {
                    offset -= 360;
                } else if (offset < 0) {
                    offset += 360;
                }
            } else {
                offset = this.self.getYRot() % 360;
            }
            double offset2 = offset;
            offset = (offset - 180) * Math.PI;
            double distanceOut = 3F;
            if (number >4) {
                distanceUp *= 0.5F;
            }
            double x1 = entityX - -1 * (distanceOut * (Math.sin(offset / 180)));
            double y1 = entityY + distanceUp;
            double z1 = entityZ - (distanceOut * (Math.cos(offset / 180)));
            if (!this.self.level().isClientSide()) {
                value.setOldPosAndRot();
                //Roundabout.LOGGER.info("bye");
            }
            value.actuallyTick();
            value.storeVec = new Vec3(x1, y1, z1);
            if (this.self.level().isClientSide()) {
                value.setYRot((float) offset2);
                value.yRotO = (float) offset2;
                value.xOld = x1;
                value.yOld = y1;
                value.zOld = z1;
                value.absMoveTo(x1, y1, z1);
            } else {
                value.setYRot((float) offset2);
                value.yRotO = (float) offset2;
                value.xOld = x1;
                value.yOld = y1;
                value.zOld = z1;
                value.setPos(x1, y1, z1);
            }
        }
    }

    public void removeFloatingVisages(){
        floatingVisageInit();
        List<CinderellaVisageDisplayEntity> hurricaneSpecial2 = new ArrayList<>(floatingVisages) {
        };
        if (!hurricaneSpecial2.isEmpty()) {
            for (CinderellaVisageDisplayEntity value : hurricaneSpecial2) {
                if (value.isRemoved() || !value.isAlive() || value.getCrossNumber() <= 0) {
                    value.initialized = false;
                    floatingVisages.remove(value);
                }
            }
        }
    }
    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        setSkillIcon(context, x, y, 1, StandIcons.CINDERELLA_MASK, PowerIndex.NO_CD);
        setSkillIcon(context, x, y, 2, StandIcons.CINDERELLA_SCALP, PowerIndex.SKILL_2);
        setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
    }

    @Override
    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }
    public SoundEvent getSoundFromByte(byte soundChoice){
        byte bt = ((StandUser)this.getSelf()).roundabout$getStandSkin();
        if (soundChoice == SoundIndex.SUMMON_SOUND) {
            return ModSounds.CINDERELLA_SUMMON_EVENT;
        } else if (soundChoice == IMPALE_NOISE) {
            return ModSounds.CINDERELLA_ATTACK_EVENT;
        } else if (soundChoice == VISAGE_NOISE) {
            return ModSounds.CINDERELLA_VISAGE_CREATION_EVENT;
        }
        return super.getSoundFromByte(soundChoice);
    }
    public void clearAllFloatingVisages() {
        floatingVisageInit();

        List<CinderellaVisageDisplayEntity> hurricaneSpecial2 = new ArrayList<>(floatingVisages) {
        };
        if (!hurricaneSpecial2.isEmpty()) {
            int totalnumber = hurricaneSpecial2.size();
            for (CinderellaVisageDisplayEntity value : hurricaneSpecial2) {
                value.discard();
            }
        }
    }
    @Override
    public boolean tryPower(int move, boolean forced) {
        if (!this.getSelf().level().isClientSide && this.getActivePower() == PowerIndex.POWER_2 && this.attackTimeDuring > -1) {
            this.stopSoundsIfNearby(IMPALE_NOISE, 100,true);
        }if (!this.getSelf().level().isClientSide && !(this.getActivePower() != PowerIndex.POWER_1 && move == PowerIndex.POWER_1)) {
            this.stopSoundsIfNearby(VISAGE_NOISE, 100,true);
        }
        if (!this.getSelf().level().isClientSide()){
            clearAllFloatingVisages();
        }
        return super.tryPower(move,forced);
    }

    public boolean hasUIOpen = false;


    public void tickPower() {
        if (this.self.level().isClientSide()) {
            if (hasUIOpen && !ClientUtil.hasCinderellaUI()){
                hasUIOpen = false;
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.NONE);
            }
        } else {
            if (this.getActivePower() == PowerIndex.POWER_1){

                ((ServerLevel) this.getSelf().level()).sendParticles(ModParticles.PINK_SMOKE,
                        this.getSelf().getX(), this.getSelf().getY() + 0.3, this.getSelf().getZ(),
                        1, 2.5, 2,2.5, 0.015);

                if (this.self instanceof Aesthetician aes){
                    if (this.getStandEntity(this.self) == null || this.getStandEntity(this.self).isRemoved()){
                        if (this.canSummonStand()){
                            ((StandUser)this.self).roundabout$summonStand(this.self.level(),true,false);
                        }
                    }
                    if (aes.interactingWith != null && aes.interactingWith.isEmpty()){
                        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                    }
                }
            } else {
                if (this.self instanceof Aesthetician aes){
                    if (aes.interactingWith != null && !aes.interactingWith.isEmpty()){
                        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_1, true);
                    }
                }
            }
        }
        super.tickPower();
    }

    @Override
    public void updateUniqueMoves() {
        if (this.getActivePower() == PowerIndex.POWER_2){
            updateDeface();
        }
        super.updateUniqueMoves();
    }
    @Override
    public boolean setPowerOther(int move, int lastMove) {
        if (move == PowerIndex.POWER_2) {
            return this.deface();
        }if (move == PowerIndex.POWER_1) {
            return this.visages();
        }
        return super.setPowerOther(move,lastMove);
    }
    public void generateFloatingMask(int crossNumber, int maxSize){
        ItemStack stack = ModItems.getVisageStore().get(
                Mth.floor(Math.random()* (ModItems.getVisageStore().size()-1))
        ).stack;
        if (stack.getItem() instanceof LuckyLipstickItem){
            stack = ModItems.BLANK_MASK.getDefaultInstance().copy();
        }
        CinderellaVisageDisplayEntity cross = new CinderellaVisageDisplayEntity(
                this.self,this.self.level(),stack
                );
        if (cross != null){
            cross.absMoveTo(this.getSelf().getX(), this.getSelf().getY(), this.getSelf().getZ());
            cross.setUser(this.self);

            if (floatingVisages == null) {floatingVisages = new ArrayList<>();}
            cross.setCrossNumber(crossNumber);
            cross.setMaxSize(maxSize);
            floatingVisages.add(cross);

            this.getSelf().level().addFreshEntity(cross);
        }
    }
    public static int getChargingCrossfireSpecialSize(){
        return 26;
    }
    public boolean visages(){
        StandEntity stand = getStandEntity(this.self);
        if (Objects.nonNull(stand)){
            this.setAttackTimeDuring(0);
            this.setActivePower(PowerIndex.POWER_1);
            this.animateStand(CinderellaEntity.VISAGES);
            playStandUserOnlySoundsIfNearby(VISAGE_NOISE, 27, false,false);
            this.poseStand(OffsetIndex.GUARD);

            generateFloatingMask(1, getChargingCrossfireSpecialSize());
            generateFloatingMask(2, getChargingCrossfireSpecialSize());
            generateFloatingMask(3, getChargingCrossfireSpecialSize());
            generateFloatingMask(4, getChargingCrossfireSpecialSize());
            generateFloatingMask(5, getChargingCrossfireSpecialSize());
            generateFloatingMask(6, getChargingCrossfireSpecialSize());
            generateFloatingMask(7, getChargingCrossfireSpecialSize());
            generateFloatingMask(8, getChargingCrossfireSpecialSize());
            return true;
        }
        return false;
    }
    public static final byte VISAGE_NOISE = 104;
    public static final byte IMPALE_NOISE = 105;
    public boolean deface(){
        StandEntity stand = getStandEntity(this.self);
        if (Objects.nonNull(stand)){
            this.setAttackTimeDuring(0);
            this.setActivePower(PowerIndex.POWER_2);
            playStandUserOnlySoundsIfNearby(IMPALE_NOISE, 27, false,false);
            this.animateStand(CinderellaEntity.DEFACE);
            this.poseStand(OffsetIndex.ATTACK);

            return true;
        }
        return false;
    }
    public void updateDeface(){
        if (this.attackTimeDuring > -1) {
            if (this.attackTimeDuring > 15) {
                this.standImpale();
            } else {
                if (!this.getSelf().level().isClientSide()) {
                    if(this.attackTimeDuring%4==0) {
                        ((ServerLevel) this.getSelf().level()).sendParticles(ModParticles.MENACING,
                                this.getSelf().getX(), this.getSelf().getY() + 0.3, this.getSelf().getZ(),
                                1, 0.2, 0.2, 0.2, 0.05);
                    }
                }
            }
        }
    }

    @Override
    public void tickMobAI(LivingEntity attackTarget){
        if (attackTarget != null && attackTarget.isAlive()){
            if ((this.getActivePower() != PowerIndex.NONE
                    || attackTarget.distanceTo(this.getSelf()) <= 5)){
                this.getSelf().setXRot(getLookAtEntityPitch(this.getSelf(), attackTarget));
                float yrot = getLookAtEntityYaw(this.getSelf(), attackTarget);
                this.getSelf().setYRot(yrot);
                this.getSelf().setYHeadRot(yrot);
            }

            Entity targetEntity = getTargetEntity(this.self, 5);
            if (targetEntity != null && targetEntity.is(attackTarget)) {
                if (this.getActivePower() == PowerIndex.NONE && (!this.onCooldown(PowerIndex.SKILL_2) ||
                        this.self instanceof IronGolem)) {
                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_2, true);
                }
            }
        }
    }
    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypass) {
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 80, 0, "ability.roundabout.visage_creation",
                "instruction.roundabout.press_skill", StandIcons.CINDERELLA_MASK, 1, level, bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 99, 0, "ability.roundabout.face_removal",
                "instruction.roundabout.press_skill", StandIcons.CINDERELLA_SCALP,2,level,bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 118, 0, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE,3,level,bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 80, 0, "ability.roundabout.visages",
                "instruction.roundabout.passive", StandIcons.CINDERELLA_VISAGES,0,level,bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 99, 0, "ability.roundabout.lucky_lipstick",
                "instruction.roundabout.passive", StandIcons.CINDERELLA_LIPSTICK,0,level,bypass));
        return $$1;
    }
    @Override
    public List<Byte> getSkinList() {
        List<Byte> $$1 = Lists.newArrayList();
        $$1.add(CinderellaEntity.PART_4_SKIN);
        $$1.add(CinderellaEntity.MANGA_SKIN);
        $$1.add(CinderellaEntity.ZOMBIE_SKIN);
        $$1.add(CinderellaEntity.JACK_SKIN);
        $$1.add(CinderellaEntity.BUSINESS_SKIN);
        return $$1;
    }

    public void standImpale(){
        if (this.self instanceof Player){
            if (isPacketPlayer()){
                this.setAttackTimeDuring(-15);
                ModPacketHandler.PACKET_ACCESS.intToServerPacket(getTargetEntityId2(5), PacketDataIndex.INT_STAND_ATTACK);
            }
        } else {
            /*Caps how far out the punch goes*/
            Entity targetEntity = getTargetEntity(this.self,5);
            defaceImpact(targetEntity);
        }
    }
    public float getDefaceStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod(((float) ((float) 3* (ClientNetworking.getAppropriateConfig().
                    cinderellaSettings.cinderellaAttackMultOnPlayers*0.01))));
        } else {
            return levelupDamageMod(((float) ((float) 9* (ClientNetworking.getAppropriateConfig().
                    cinderellaSettings.cinderellaAttackMultOnMobs*0.01))));
        }
    }
    public float getDefaceKnockback(){
        return 0.7F;
    }
    public void handleStandAttack(Player player, Entity target){
        defaceImpact(target);
    }
    public void defaceImpact(Entity entity){
        this.setAttackTimeDuring(-20);
        if (entity != null) {
            float pow;
            float knockbackStrength;
            pow = getDefaceStrength(entity);
            knockbackStrength = getDefaceKnockback();
            if (StandDamageEntityAttack(entity, pow, 0, this.self)) {
                if (entity instanceof LivingEntity LE) {
                    addEXP(5, LE);
                    if (MainUtil.getMobBleed(entity)) {
                        int bleedlevel = ((StandUser)LE).roundabout$getBleedLevel();
                        if (bleedlevel < 0){
                            MainUtil.makeFaceless(entity, 200, 0, this.getSelf());
                            MainUtil.makeBleed(entity, 0, 200, this.getSelf());
                        } else if (bleedlevel == 0){
                            MainUtil.makeFaceless(entity, 250, 1, this.getSelf());
                            MainUtil.makeBleed(entity, 1, 250, this.getSelf());
                        } else {
                            MainUtil.makeFaceless(entity, 300, 2, this.getSelf());
                            MainUtil.makeBleed(entity, 2, 300, this.getSelf());
                            MainUtil.makeMobBleed(entity);
                        }
                    } else {
                        MainUtil.makeFaceless(entity, 200, 0, this.getSelf());
                    }
                }
                this.takeDeterminedKnockback(this.self, entity, knockbackStrength);
            }
        }

        if (this.getSelf() instanceof Player) {
            ModPacketHandler.PACKET_ACCESS.syncSkillCooldownPacket(((ServerPlayer) this.getSelf()), PowerIndex.SKILL_2,  ClientNetworking.getAppropriateConfig().cinderellaSettings.defaceAttackCooldown);
        }
        this.setCooldown(PowerIndex.SKILL_2, ClientNetworking.getAppropriateConfig().cinderellaSettings.defaceAttackCooldown);
        SoundEvent SE;
        float pitch = 1F;
        if (entity != null) {
            SE = ModSounds.PUNCH_3_SOUND_EVENT;
            pitch = 1.2F;
        } else {
            SE = ModSounds.PUNCH_2_SOUND_EVENT;
        }

        if (!this.self.level().isClientSide()) {
            this.self.level().playSound(null, this.self.blockPosition(), SE, SoundSource.PLAYERS, 0.95F, pitch);
        }
    }

    @Override
    public void tickStandRejection(MobEffectInstance effect){
        if (!this.getSelf().level().isClientSide()) {
            if (effect.getDuration() == 15) {
                MainUtil.makeFaceless(this.self,800,0,this.self);
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.CINDERELLA_FAIL_EVENT,
                        SoundSource.PLAYERS, 1F, 1F);
            }
        }
    }
    @Override
    public void renderAttackHud(GuiGraphics context, Player playerEntity,
                                int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                float flashAlpha, float otherFlashAlpha) {;
        int j = scaledHeight / 2 - 7 - 4;
        int k = scaledWidth / 2 - 8;
        if (this.getActivePower() == PowerIndex.POWER_2) {
            Entity TE = this.getTargetEntity(playerEntity, 5F);
            if (TE != null) {
                context.blit(StandIcons.JOJO_ICONS, k, j, 193, 0, 15, 6);
            }
        }
    }
}
