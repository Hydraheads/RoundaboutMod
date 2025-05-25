package net.hydra.jojomod.event.powers.stand;

import com.google.common.collect.Lists;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.block.BubbleScaffoldBlockEntity;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.projectile.SoftAndWetBubbleEntity;
import net.hydra.jojomod.entity.projectile.SoftAndWetPlunderBubbleEntity;
import net.hydra.jojomod.entity.stand.SoftAndWetEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.presets.PunchingStand;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.ClientConfig;
import net.hydra.jojomod.util.ConfigManager;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class PowersSoftAndWet extends PunchingStand {
    public PowersSoftAndWet(LivingEntity self) {
        super(self);
    }

    public List<SoftAndWetBubbleEntity> bubbleList = new ArrayList<>();
    @Override
    public StandEntity getNewStandEntity(){
        byte sk = ((StandUser)this.getSelf()).roundabout$getStandSkin();
        if (sk == SoftAndWetEntity.KING_SKIN){
            return ModEntities.SOFT_AND_WET_KING.create(this.getSelf().level());
        } else if (sk == SoftAndWetEntity.DROWNED_SKIN
        || sk == SoftAndWetEntity.DROWNED_SKIN_2){
            return ModEntities.SOFT_AND_WET_DROWNED.create(this.getSelf().level());
        }
        return ModEntities.SOFT_AND_WET.create(this.getSelf().level());
    }
    @Override
    public boolean canSummonStand(){
        return true;
    }
    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersSoftAndWet(entity);
    }


    @Override
    public boolean isWip(){
        return true;
    }
    @Override
    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }


    @Override
    public SoundEvent getSoundFromByte(byte soundChoice) {
        byte bt = ((StandUser) this.getSelf()).roundabout$getStandSkin();
        if (soundChoice == SoundIndex.SUMMON_SOUND) {
            return ModSounds.SUMMON_SOFT_AND_WET_EVENT;
        }
        return super.getSoundFromByte(soundChoice);
    }



    public void bubbleListInit(){
        if (bubbleList == null) {
            bubbleList = new ArrayList<>();
        }
    }

    @Override
    public List<Byte> getSkinList() {
        List<Byte> $$1 = Lists.newArrayList();
        $$1.add(SoftAndWetEntity.LIGHT_SKIN);
        $$1.add(SoftAndWetEntity.MANGA_SKIN);
        $$1.add(SoftAndWetEntity.FIGURE_SKIN);
        $$1.add(SoftAndWetEntity.DROWNED_SKIN);
        $$1.add(SoftAndWetEntity.DROWNED_SKIN_2);
        $$1.add(SoftAndWetEntity.KING_SKIN);
        $$1.add(SoftAndWetEntity.BETA_SKIN);
        return $$1;
    }
    public boolean isAttackIneptVisually(byte activeP, int slot) {
        if (slot == 2 && !canDoBubbleRedirect() && isGuarding()) {
            return true;
        }
        if (slot == 1 && !canDoBubbleClusterRedirect() && isGuarding()) {
            return true;
        }
        return super.isAttackIneptVisually(activeP,slot);
    }

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {

        if (isGuarding()) {
            setSkillIcon(context, x, y, 1, StandIcons.PLUNDER_BUBBLE_FILL_CONTROL, PowerIndex.SKILL_EXTRA_2);
        } else if (isHoldingSneak()){
            if (canDoBubbleClusterPop()){
                setSkillIcon(context, x, y, 1, StandIcons.PLUNDER_BUBBLE_FILL_POP, PowerIndex.SKILL_2_SNEAK);
            } else {
                setSkillIcon(context, x, y, 1, StandIcons.PLUNDER_BUBBLE_FILL, PowerIndex.SKILL_1_SNEAK);
            }
        } else {
            setSkillIcon(context, x, y, 1, StandIcons.PLUNDER_SELECTION, PowerIndex.NO_CD);
        }


        if (isGuarding()){
            setSkillIcon(context, x, y, 2, StandIcons.PLUNDER_BUBBLE_CONTROL, PowerIndex.SKILL_EXTRA_2);
        } else if (isHoldingSneak()){
            setSkillIcon(context, x, y, 2, StandIcons.PLUNDER_BUBBLE_POP, PowerIndex.SKILL_2_SNEAK);
        } else {
            setSkillIcon(context, x, y, 2, StandIcons.PLUNDER_BUBBLE, PowerIndex.SKILL_2);
        }

        if (canVault()) {
            setSkillIcon(context, x, y, 3, StandIcons.SOFT_AND_WET_VAULT, PowerIndex.SKILL_3_SNEAK);
        } else if (canFallBrace()) {
            setSkillIcon(context, x, y, 3, StandIcons.SOFT_AND_WET_FALL_CATCH, PowerIndex.SKILL_EXTRA);
        } else if (isHoldingSneak()){
            setSkillIcon(context, x, y, 3, StandIcons.SOFT_AND_WET_BUBBLE_SCAFFOLD, PowerIndex.SKILL_3);
        } else {
            setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.SKILL_3_SNEAK);
        }

        setSkillIcon(context, x, y, 4, StandIcons.NONE, PowerIndex.SKILL_4);
    }

    /**For mob ai, change the bubbleType before trypower to set what kind of plunder it has*/
    public byte bubbleType = PlunderTypes.ITEM.id;


    @Override
    public boolean canGuard(){
        return super.canGuard();
    }

    public boolean hold1 = false;
    @Override
    public void buttonInput1(boolean keyIsDown, Options options) {
        if (this.getSelf().level().isClientSide) {
            if (isGuarding()) {
                if (keyIsDown) {
                    if (!hold1) {
                        if (!this.onCooldown(PowerIndex.SKILL_EXTRA_2)) {
                            hold1 = true;

                            this.tryPower(PowerIndex.POWER_1_BONUS, true);
                            ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.POWER_1_BONUS);
                        }
                    }
                } else {
                    hold1 = false;
                }
            } else if (isHoldingSneak()) {
                if (keyIsDown) {
                    if (!hold1) {
                        if (!this.onCooldown(PowerIndex.SKILL_1_SNEAK)) {
                            if (this.activePower != PowerIndex.POWER_1_SNEAK && !canDoBubbleClusterPop()) {
                                hold1 = true;

                                int bubbleType = 1;
                                ClientConfig clientConfig = ConfigManager.getClientConfig();
                                if (clientConfig != null && clientConfig.dynamicSettings != null) {
                                    bubbleType = clientConfig.dynamicSettings.SoftAndWetCurrentlySelectedBubble;
                                }

                                this.tryChargedPower(PowerIndex.POWER_1_SNEAK, true, bubbleType);
                                ModPacketHandler.PACKET_ACCESS.StandChargedPowerPacket(PowerIndex.POWER_1_SNEAK, bubbleType);
                            } else {
                                if (!this.onCooldown(PowerIndex.SKILL_EXTRA_2)) {
                                    hold1 = true;
                                    this.tryPower(PowerIndex.EXTRA_2, true);
                                    ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.EXTRA_2);
                                }
                            }
                        } else if (this.activePower == PowerIndex.POWER_1_SNEAK || this.canDoBubbleClusterRedirect()) {
                            if (!this.onCooldown(PowerIndex.SKILL_EXTRA_2)) {
                                hold1 = true;
                                this.tryPower(PowerIndex.EXTRA_2, true);
                                ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.EXTRA_2);
                            }
                        }
                    }
                } else {
                    hold1 = false;
                }
            } else {
                if (keyIsDown) {
                    if (!hold1) {
                        hold1 = true;
                        ClientUtil.openPlunderScreen();
                    }
                } else {
                    hold1 = false;
                }
            }
        }
        super.buttonInput1(keyIsDown, options);
    }
    public boolean hold2 = false;
    public SoftAndWetPlunderBubbleEntity getPlunderBubble(){
        SoftAndWetPlunderBubbleEntity bubble = new SoftAndWetPlunderBubbleEntity(this.self,this.self.level());
        bubble.absMoveTo(this.getSelf().getX(), this.getSelf().getY(), this.getSelf().getZ());
        bubble.setUser(this.self);
        bubble.lifeSpan = ClientNetworking.getAppropriateConfig().softAndWetSettings.primaryPlunderBubbleLifespanInTicks;
        return bubble;
    }
    public boolean bubbleShot(){
        SoftAndWetPlunderBubbleEntity bubble = getPlunderBubble();

        if (bubble != null){
            this.setCooldown(PowerIndex.SKILL_2, 20);

            this.poseStand(OffsetIndex.FOLLOW);
            this.setAttackTimeDuring(-10);
            this.setActivePower(PowerIndex.POWER_2);
            bubble.setPlunderType(bubbleType);
            bubble.setSingular(true);
            shootBubbleSpeed(bubble,getBubbleSpeed());
            bubbleListInit();
            this.bubbleList.add(bubble);
            this.getSelf().level().addFreshEntity(bubble);

            if (bubbleType != PlunderTypes.SOUND.id) {
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.BUBBLE_CREATE_EVENT, SoundSource.PLAYERS, 2F, (float) (0.98 + (Math.random() * 0.04)));
            }
        }
        return true;
    }
    public boolean canDoBubblePop(){
        bubbleListInit();

        List<SoftAndWetBubbleEntity> bubbleList2 = new ArrayList<>(bubbleList) {
        };
        if (!bubbleList2.isEmpty()) {
            int totalnumber = bubbleList2.size();
            for (SoftAndWetBubbleEntity value : bubbleList2) {
                if (!(value instanceof SoftAndWetPlunderBubbleEntity PBE && PBE.isPopPlunderBubbble())) {
                    return true;
                }
            }
        }
        return !bubbleList.isEmpty();
    }
    public boolean canDoBubbleClusterRedirect(){
        bubbleListInit();

        List<SoftAndWetBubbleEntity> bubbleList2 = new ArrayList<>(bubbleList) {
        };
        if (!bubbleList2.isEmpty()) {
            int totalnumber = bubbleList2.size();
            for (SoftAndWetBubbleEntity value : bubbleList2) {
                if (value instanceof SoftAndWetPlunderBubbleEntity PBE) {
                    if (!PBE.getSingular() && !PBE.getActivated() && !PBE.getFinished()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public boolean canDoBubbleClusterPop(){
        bubbleListInit();

        if (this.activePower == PowerIndex.POWER_1_SNEAK){
            return true;
        }

        List<SoftAndWetBubbleEntity> bubbleList2 = new ArrayList<>(bubbleList) {
        };
        if (!bubbleList2.isEmpty()) {
            int totalnumber = bubbleList2.size();
            for (SoftAndWetBubbleEntity value : bubbleList2) {
                if (value instanceof SoftAndWetPlunderBubbleEntity PBE && !PBE.isPopPlunderBubbble()) {
                    if (!PBE.getSingular() && !PBE.getFinished()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public boolean canDoBubbleRedirect(){

        bubbleListInit();

        List<SoftAndWetBubbleEntity> bubbleList2 = new ArrayList<>(bubbleList) {
        };
        if (!bubbleList2.isEmpty()) {
            int totalnumber = bubbleList2.size();
            for (SoftAndWetBubbleEntity value : bubbleList2) {
                if (value.getActivated() && !(value instanceof SoftAndWetPlunderBubbleEntity PBE && (PBE.getPlunderType()==PlunderTypes.SIGHT.id ||
                        PBE.getPlunderType()==PlunderTypes.FRICTION.id))) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean clusterBubblePop() {
        bubbleListInit();
        if (!bubbleList.isEmpty()) {
            this.setCooldown(PowerIndex.SKILL_2_SNEAK, 10);
            if (!this.self.level().isClientSide()) {
                List<SoftAndWetBubbleEntity> bubbleList2 = new ArrayList<>(bubbleList) {
                };
                if (!bubbleList2.isEmpty()) {
                    for (SoftAndWetBubbleEntity value : bubbleList2) {
                        if (value instanceof SoftAndWetPlunderBubbleEntity plunder && !plunder.isPopPlunderBubbble()){
                            if (!plunder.getFinished() && !plunder.getSingular()){
                                plunder.popBubble();
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    public boolean bubblePop() {
        bubbleListInit();
        if (!this.self.level().isClientSide()) {
            bubbleNumber++;
        }
        if (!bubbleList.isEmpty()) {
            this.setCooldown(PowerIndex.SKILL_2_SNEAK, 10);
            if (!this.self.level().isClientSide()) {
                List<SoftAndWetBubbleEntity> bubbleList2 = new ArrayList<>(bubbleList) {
                };
                if (!bubbleList2.isEmpty()) {
                    for (SoftAndWetBubbleEntity value : bubbleList2) {
                        if (value instanceof SoftAndWetPlunderBubbleEntity plunder && !plunder.isPopPlunderBubbble()){
                            if (!plunder.getFinished()){
                               plunder.popBubble();
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**If there are any cluster bubbles that have not yet entered the plunder state, redirect them*/
    public boolean bubbleClusterRedirect(){
        bubbleListInit();
        List<SoftAndWetBubbleEntity> bubbleList2 = new ArrayList<>(bubbleList) {
        };
        if (!bubbleList2.isEmpty()) {
            int totalnumber = bubbleList2.size();
            for (SoftAndWetBubbleEntity value : bubbleList2) {
                if (value instanceof SoftAndWetPlunderBubbleEntity PBE) {
                    if (!PBE.getSingular() && !PBE.getActivated() && !PBE.getFinished()) {
                        shootBubbleSpeed2(PBE,PBE.getSped()*0.65F);
                        PBE.setLaunched(true);
                    }
                }
            }
        }
        return false;
    }

    /**Redirects all bubbles that have entered the plunder state and are compatible*/
    public boolean bubbleRedirect(){
        bubbleListInit();
        if (!bubbleList.isEmpty()){

            if (canDoBubbleRedirect()) {
                this.setCooldown(PowerIndex.SKILL_EXTRA_2, 3);

                Vec3 vec3d = this.self.getEyePosition(0);
                Vec3 vec3d2 = this.self.getViewVector(0);
                Vec3 vec3d3 = vec3d.add(vec3d2.x * 30, vec3d2.y * 30, vec3d2.z * 30);
                BlockHitResult blockHit = this.self.level().clip(new ClipContext(vec3d, vec3d3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.self));

                if (!this.self.level().isClientSide()) {
                    List<SoftAndWetBubbleEntity> bubbleList3 = new ArrayList<>(bubbleList) {
                    };
                    if (!bubbleList3.isEmpty()) {
                        int totalnumber = bubbleList3.size();
                        for (SoftAndWetBubbleEntity value : bubbleList3) {
                            if (value.getActivated() && !(value instanceof SoftAndWetPlunderBubbleEntity PBE && (PBE.getPlunderType()==PlunderTypes.SIGHT.id ||
                                    PBE.getPlunderType()==PlunderTypes.FRICTION.id))) {
                                Vec3 vector = new Vec3((blockHit.getLocation().x() - value.getX()),
                                        (blockHit.getLocation().y() - value.getY()),
                                        (blockHit.getLocation().z() - value.getZ())).normalize().scale(value.getSped());
                                if (totalnumber > 1){
                                    vector = new Vec3(
                                            vector.x()+(((Math.random()-0.5)*totalnumber)*value.getSped()*0.03),
                                            vector.y(),
                                            vector.z()+(((Math.random()-0.5)*totalnumber)*value.getSped()*0.03)
                                    ).normalize().scale(value.getSped());
                                }
                                value.setDeltaMovement(vector);
                                value.hurtMarked = true;
                                value.hasImpulse = true;
                                if (!value.getLaunched()){
                                    value.setLaunched(true);
                                }
                            }
                        }
                    }
                } else {
                    this.self.playSound(ModSounds.BUBBLE_HOVERED_OVER_EVENT, 0.2F, (float) (0.95F+Math.random()*0.1F));
                    this.self.level()
                            .addParticle(
                                    ModParticles.POINTER_SOFT,
                                    blockHit.getLocation().x(),
                                    blockHit.getLocation().y() + 0.5,
                                    blockHit.getLocation().z(),
                                    0,
                                    0,
                                    0
                            );
                }
            }
            /**
            if (bubbleType != PlunderTypes.SOUND.id) {
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.BUBBLE_CREATE_EVENT, SoundSource.PLAYERS, 2F, (float) (0.98 + (Math.random() * 0.04)));
            }
             **/
        }
        return true;
    }

    public float getBubbleSpeed(){
        if (bubbleType == PlunderTypes.OXYGEN.id){
            return 0.6F;
        } else if (bubbleType == PlunderTypes.ITEM.id){
            return 0.5F;
        } else if (bubbleType == PlunderTypes.POTION_EFFECTS.id){
            return 0.25F;
        } else if (bubbleType == PlunderTypes.SOUND.id){
            return 0.3F;
        }else if (bubbleType == PlunderTypes.MOISTURE.id){
            return 0.3F;
        }
        return 0.17F;
    }

    public void shootBubble(SoftAndWetBubbleEntity ankh){
        shootBubbleSpeed(ankh, 1.01F);
    }
    public void shootBubbleSpeed(SoftAndWetBubbleEntity ankh, float speed){
        ankh.setSped(speed);
        ankh.setPos(this.self.getX(), this.self.getY()+(this.self.getEyeHeight()*0.62), this.self.getZ());
        ankh.shootFromRotationDeltaAgnostic(this.getSelf(), this.getSelf().getXRot(), this.getSelf().getYRot(), 1.0F, speed, 0);
    }
    public void shootBubbleSpeed2(SoftAndWetBubbleEntity ankh, float speed){
        ankh.shootFromRotationDeltaAgnostic3(this.getSelf(), this.getSelf().getXRot(), this.getSelf().getYRot(), 1.0F, speed);
    }
    public void shootBubbleRandomly(SoftAndWetBubbleEntity ankh, float speed){
        ankh.setSped(speed);
        ankh.setPos(this.self.getX(), this.self.getY()+(this.self.getEyeHeight()*0.2), this.self.getZ());
        ankh.shootFromRotationDeltaAgnostic(this.getSelf(),-1*(float)(Math.random()*50), (float)(Math.random()*360), 1.0F, 0.25F, 0);
    }
    @Override
    public boolean setPowerOther(int move, int lastMove) {
        if (move == PowerIndex.POWER_2) {
            return this.bubbleShot();
        } else if (move == PowerIndex.POWER_2_EXTRA) {
            return this.bubbleRedirect();
        } else if (move == PowerIndex.POWER_2_SNEAK) {
            return this.bubblePop();
        } else if (move == PowerIndex.EXTRA){
            return this.fallBraceInit();
        } else if (move == PowerIndex.FALL_BRACE_FINISH){
            return this.fallBrace();
        } else if (move == PowerIndex.VAULT){
            return this.vault();
        } else if (move == PowerIndex.POWER_3){
            return this.bubbleLadder();
        } else if (move == PowerIndex.POWER_3_EXTRA){
            return this.bubbleLadderPlace();
        } else if (move == PowerIndex.POWER_1_SNEAK) {
            return this.bubbleClusterStart();
        } else if (move == PowerIndex.POWER_1) {
            return this.spawnRandomBubble();
        } else if (move == PowerIndex.POWER_1_BONUS) {
            return this.bubbleClusterRedirect();
        } else if (move == PowerIndex.EXTRA_2) {
            return this.clusterBubblePop();
        }
        return super.setPowerOther(move,lastMove);
    }
    @Override
    public boolean tryChargedPower(int move, boolean forced, int chargeTime){
        if (move == PowerIndex.POWER_2 || move == PowerIndex.POWER_1_SNEAK) {
            bubbleType = (byte)chargeTime;
        }
        return super.tryChargedPower(move, forced, chargeTime);
    }
    @Override
    public boolean cancelSprintJump(){
       if (this.getActivePower() == PowerIndex.POWER_1_SNEAK){
            return true;
        }
        return super.cancelSprintJump();
    }

    @Override
    public float inputSpeedModifiers(float basis){
        if (this.activePower == PowerIndex.POWER_1_SNEAK){
            basis *= 0.2f;
        }
        return super.inputSpeedModifiers(basis);
    }

    public boolean bubbleClusterStart(){
        if (!this.self.level().isClientSide()) {
            clusterBubblePop();
        }
        setActivePower(PowerIndex.POWER_1_SNEAK);
        this.poseStand(OffsetIndex.FOLLOW);
        this.attackTimeDuring = 0;
        animateStand((byte) 0);
        return true;
    }
    @Override
    public boolean tryPosPower(int move, boolean forced, BlockPos blockPos) {

        if (move == PowerIndex.POWER_3_EXTRA) {
            if (blockPos.getX() > 45){
                buildingBubbleScaffoldPos = buildingBubbleScaffoldPos.below();
            } else if (blockPos.getX() < -45){
                buildingBubbleScaffoldPos = buildingBubbleScaffoldPos.above();
            } else {
                buildingBubbleScaffoldPos = buildingBubbleScaffoldPos.relative(Direction.fromYRot(blockPos.getY()));
            }
        }
        return tryPower(move, forced);
    }
    public int bubbleNumber = 0;

    /**Bubble Scaffolding, build a ladder overtime somewhat like the Builder blocks in Twilight*/
    public boolean bubbleLadderPlace(){
        if (!this.self.level().isClientSide()){
            if (MainUtil.tryPlaceBlock(this.self,buildingBubbleScaffoldPos,false)){
                this.self.level().setBlockAndUpdate(buildingBubbleScaffoldPos, ModBlocks.BUBBLE_SCAFFOLD.defaultBlockState());
                if (this.self.level().getBlockEntity(buildingBubbleScaffoldPos) instanceof BubbleScaffoldBlockEntity SBE) {
                    SBE.standuser = this.self;
                    SBE.bubbleNo = bubbleNumber;
                    this.self.level().playSound(null, this.self.blockPosition(), ModSounds.BUBBLE_CREATE_EVENT,
                            SoundSource.PLAYERS, 2F, (float) (0.9 + (Math.random() * 0.2)));
                    this.self.level().playSound(null, this.self.blockPosition(), ModSounds.BUBBLE_CREATE_EVENT,
                            SoundSource.PLAYERS, 2F, (float) (0.9 + (Math.random() * 0.2)));
                    this.self.level().playSound(null, this.self.blockPosition(), ModSounds.BUBBLE_CREATE_EVENT,
                            SoundSource.PLAYERS, 2F, (float) (0.9 + (Math.random() * 0.2)));
                    this.self.level().playSound(null, this.self.blockPosition(), ModSounds.BUBBLE_CREATE_EVENT,
                            SoundSource.PLAYERS, 2F, (float) (0.9 + (Math.random() * 0.2)));
                }
            }
        }
        return false;
    }
    public boolean bubbleLadder(){
        setActivePower(PowerIndex.POWER_3);
        this.poseStand(OffsetIndex.GUARD_FURTHER_RIGHT);
        this.attackTimeDuring = 0;
        bubbleScaffoldCount = 0;
        animateStand((byte) 1);
        buildingBubbleScaffoldPos = this.self.blockPosition();
        return true;
    }
    @Override
    public void updateUniqueMoves() {
        /*Tick through Time Stop Charge*/
        if (this.getActivePower() == PowerIndex.POWER_1_SNEAK) {
            this.updateBubbleCluster();
        } else if (this.getActivePower() == PowerIndex.POWER_3){
            this.updateBubbleScaffold();
        }
        super.updateUniqueMoves();
    }
    int bubbleScaffoldCount = 0;
    public BlockPos buildingBubbleScaffoldPos = BlockPos.ZERO;
    public void updateBubbleScaffold(){
        if (this.self instanceof Player PE && this.self.level().isClientSide()) {
            if (isPacketPlayer()) {
                if (this.attackTimeDuring % 6 == 2) {

                    /**The client is the only one with accurate rotation and timings so it should be deciding how to build the ladder*/
                    ModPacketHandler.PACKET_ACCESS.StandPosPowerPacket(PowerIndex.POWER_3_EXTRA,
                            new BlockPos(
                                    (int)this.self.getXRot(),
                                    (int)this.self.getYRot(),
                                    0
                                    ));
                    bubbleScaffoldCount++;
                    this.setCooldown(PowerIndex.SKILL_3, 240);
                    if (bubbleScaffoldCount >= 10){
                        this.tryPower(PowerIndex.NONE, true);
                        ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.NONE);
                    }
                }
            }
        }
    }
    public void updateBubbleCluster(){
        if (this.self instanceof Player){
            if (isPacketPlayer()){
                bubbleCheck(true);
            }
        } else {
            bubbleCheck(false);
        }
    }
    public void bubbleCheck(boolean packetPlayer){
        if (this.attackTimeDuring > -1) {
            if (this.attackTimeDuring > 37){
                this.tryPower(PowerIndex.NONE, true);
                if (packetPlayer) {
                    ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.NONE);
                }
            } else if (this.attackTimeDuring % 3 == 0){
                if (packetPlayer){
                    ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.POWER_1);
                } else {
                    spawnRandomBubble();
                }
            }
        }
    }

    public boolean spawnRandomBubble(){
        SoftAndWetPlunderBubbleEntity bubble = getPlunderBubble();

        if (bubble != null) {
            bubble.setPlunderType(bubbleType);
            bubble.setSingular(false);
            shootBubbleRandomly(bubble, getBubbleSpeed()); //0.025F
            bubbleListInit();
            this.bubbleList.add(bubble);
            this.getSelf().level().addFreshEntity(bubble);

            if (bubbleType != PlunderTypes.SOUND.id) {
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.BUBBLE_CREATE_EVENT, SoundSource.PLAYERS, 2F, (float) (0.98 + (Math.random() * 0.04)));
            }
        }

        return true;
    }

    public void unloadBubbles(){
        bubbleListInit();
        List<SoftAndWetBubbleEntity> bubbleList2 = new ArrayList<>(bubbleList) {
        };
        if (!bubbleList2.isEmpty()) {
            for (SoftAndWetBubbleEntity value : bubbleList2) {
                if (value.isRemoved() || !value.isAlive() || (this.self.level().isClientSide() && this.self.level().getEntity(value.getId()) == null)) {
                    bubbleList.remove(value);
                } else {
                }
            }
        }
    }
    @Override
    public void tickPower(){
        unloadBubbles();
        super.tickPower();
    }
    @Override
    public void buttonInput2(boolean keyIsDown, Options options) {
        if (this.getSelf().level().isClientSide) {
            if (isGuarding()) {

                if (keyIsDown) {
                    if (!hold2) {
                        if (!this.onCooldown(PowerIndex.SKILL_EXTRA_2)){
                            hold2 = true;


                            this.tryPower(PowerIndex.POWER_2_EXTRA, true);
                            ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.POWER_2_EXTRA);
                            //this.setCooldown(PowerIndex.SKILL_1, ClientNetworking.getAppropriateConfig().cooldownsInTicks.magicianRedBindFailOrMiss);
                        }
                    }
                } else {
                    hold2 = false;
                }

            } else if (isHoldingSneak()) {


                if (keyIsDown) {
                    if (!hold2) {
                        if (!this.onCooldown(PowerIndex.SKILL_2_SNEAK)){
                            hold2 = true;


                            this.tryPower(PowerIndex.POWER_2_SNEAK, true);
                            ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.POWER_2_SNEAK);
                            //this.setCooldown(PowerIndex.SKILL_1, ClientNetworking.getAppropriateConfig().cooldownsInTicks.magicianRedBindFailOrMiss);
                        }
                    }
                } else {
                    hold2 = false;
                }

            } else {
                if (keyIsDown) {
                    if (!hold2) {
                        if (!this.onCooldown(PowerIndex.SKILL_2)){
                            hold2 = true;

                            int bubbleType = 1;
                            ClientConfig clientConfig = ConfigManager.getClientConfig();
                            if (clientConfig != null && clientConfig.dynamicSettings != null) {
                                bubbleType = clientConfig.dynamicSettings.SoftAndWetCurrentlySelectedBubble;
                            }

                            this.tryChargedPower(PowerIndex.POWER_2, true, bubbleType);
                            ModPacketHandler.PACKET_ACCESS.StandChargedPowerPacket(PowerIndex.POWER_2, bubbleType);
                            //this.setCooldown(PowerIndex.SKILL_1, ClientNetworking.getAppropriateConfig().cooldownsInTicks.magicianRedBindFailOrMiss);
                        }
                    }
                } else {
                    hold2 = false;
                }
            }
        }
        super.buttonInput1(keyIsDown, options);
    }

    @Override
    public void playFallBraceImpactParticles(){
        ((ServerLevel) this.getSelf().level()).sendParticles(ModParticles.BUBBLE_POP,
                this.getSelf().getX(), this.getSelf().getOnPos().getY() + 1.1, this.getSelf().getZ(),
                50, 1.1, 0.05, 1.1, 0.4);
        ((ServerLevel) this.getSelf().level()).sendParticles(ModParticles.BUBBLE_POP,
                this.getSelf().getX(), this.getSelf().getOnPos().getY() + 1.1, this.getSelf().getZ(),
                30, 1, 0.05, 1, 0.4);
    }
    public boolean hold3 = false;

    @Override
    public void playFallBraceInitSound(){
        this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.SUMMON_SOFT_AND_WET_EVENT, SoundSource.PLAYERS, 2.3F, (float) (0.78 + (Math.random() * 0.04)));
    }
    @Override
    public void playFallBraceImpactSounds(){
        this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.BUBBLE_POP_EVENT, SoundSource.PLAYERS, 1.0F, (float) (0.9 + (Math.random() * 0.2)));
        this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.BUBBLE_POP_EVENT, SoundSource.PLAYERS, 1.0F, (float) (0.9 + (Math.random() * 0.2)));
        this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.BUBBLE_POP_EVENT, SoundSource.PLAYERS, 1.0F, (float) (0.9 + (Math.random() * 0.2)));
        this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.BUBBLE_POP_EVENT, SoundSource.PLAYERS, 1.0F, (float) (0.9 + (Math.random() * 0.2)));
        this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.BUBBLE_POP_EVENT, SoundSource.PLAYERS, 1.0F, (float) (0.9 + (Math.random() * 0.2)));
        this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.BUBBLE_POP_EVENT, SoundSource.PLAYERS, 1.0F, (float) (0.9 + (Math.random() * 0.2)));
        this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.BUBBLE_POP_EVENT, SoundSource.PLAYERS, 1.0F, (float) (0.9 + (Math.random() * 0.2)));
    }
    @Override
    public void buttonInput3(boolean keyIsDown, Options options) {
        if (this.getSelf().level().isClientSide) {
            if (!(keyIsDown && !hold3 && doVault())) {
                if (canFallBrace()) {
                    if (keyIsDown) {
                        if (!hold3){
                            hold3 = true;
                            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.EXTRA, true);
                            ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.EXTRA);
                        }
                    } else {
                        hold3 = false;
                    }
                } else if (isGuarding()) {
                    if (keyIsDown) {
                        if (!hold3) {
                            hold3 = true;
                        }
                    } else {
                        hold3 = false;
                    }
                } else if (isHoldingSneak()) {
                    if (keyIsDown) {
                        if (!this.onCooldown(PowerIndex.SKILL_3)) {
                            if (!hold3) {
                                hold3 = true;
                                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_3, true);
                                ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.POWER_3);
                            }
                        }
                    } else {
                        hold3 = false;
                    }
                } else {
                    if (!keyIsDown) {
                        hold3 = false;
                    }
                    super.buttonInput3(keyIsDown, options);
                }
            }
        }
    }
}

