package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.projectile.SoftAndWetBubbleEntity;
import net.hydra.jojomod.entity.projectile.SoftAndWetExplosiveBubbleEntity;
import net.hydra.jojomod.entity.stand.KillerQueenEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.stand.StarPlatinumEntity;
import net.hydra.jojomod.entity.stand.TheWorldEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewPunchingStand;
import net.hydra.jojomod.util.config.ClientConfig;
import net.hydra.jojomod.util.config.ConfigManager;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.List;

public class PowersKillerQueen extends NewPunchingStand {
	
	// TODO Make Impale code
	// TODO Make air bubble bomb spawn and entity
	// TODO Make bomb entity
	// TODO Make bomb item
	// TODO Make bomb block
	// TODO Bites The Dust
	
	public enum BOMB_TYPE {
		NONE,
		BLOCK,
		ITEM,
		ENTITY,
		BUBBLE
	}
	
	public Entity bombEntity = null;
	public BlockPos bombBlock = null;
	public Entity bombBubble = null;
	private boolean destroyTerrain = false;
	private boolean explodeOnContact = false;
	private boolean BitesTheDustMode = false;
	
	public float standReach = 5;
    public PowersKillerQueen(LivingEntity self) {super(self);}
    
    @Override
    public StandPowers generateStandPowers(LivingEntity entity){
        return new PowersKillerQueen(entity);
    }
    
    @Override
    public StandEntity getNewStandEntity() {
        return ModEntities.KILLER_QUEEN.create(this.getSelf().level());
    }

    @Override
    public void powerActivate(PowerContext context) {
    	
        switch (context)
        {
        	case SKILL_2_CROUCH -> {
        		tryShootAirBubbleClient();
        	}
        	case SKILL_3_NORMAL, SKILL_3_GUARD -> {
        		tryToDashClient();
        	}
        	case SKILL_3_CROUCH -> {
        		if (this.BitesTheDustMode) {
        			tryToDashClient();
        		}
        	}
        	case SKILL_4_NORMAL, SKILL_4_CROUCH, SKILL_4_GUARD -> {
        		bitesTheDustModeToggleClient();
        	}
        	
        }
    }
    @Override
    public boolean setPowerOther(int move, int lastMove) {
    	
    	if (move == PowerIndex.POWER_4) {
    		return switchModes();
    	} else if (move == PowerIndex.POWER_2) {
    		return shootAirBubble();
    	}
    	
    	return super.setPowerOther(move,  lastMove);
    }
    @Override
    public List<Byte> getSkinList() {
        return Arrays.asList(
                KillerQueenEntity.PART_4
        );
    }
    

    @Override
    public int getMaxGuardPoints(){
        return 15;
    }
    @Override
    public void handleStandAttack(Player player, Entity target){
        super.handleStandAttack(player,target);
    }
    
    
    @Override
    public boolean tryPower(int move, boolean forced) {
        return super.tryPower(move, forced);
    }
    @Override
    public boolean tryIntPower(int move, boolean forced, int chargeTime) {
    	return super.tryIntPower(move, forced, chargeTime);
    }

    public boolean wentForCharge = false;
    
    public void tryToDashClient(){
        if (vaultOrFallBraceFails()){
            dash();
        }
    }
    
    public void tryShootAirBubbleClient() {
    	if (!this.BitesTheDustMode) {
            if (!this.onCooldown(PowerIndex.SKILL_2)) {

                int bubbleType = 1;
                /*ClientConfig clientConfig = ConfigManager.getClientConfig();
                if (clientConfig != null && clientConfig.dynamicSettings != null) {
                    bubbleType = clientConfig.dynamicSettings.SoftAndWetCurrentlySelectedBubble;
                }*/

                this.tryIntPower(PowerIndex.POWER_2, true, bubbleType);

                tryIntPowerPacket(PowerIndex.POWER_2,bubbleType);
                //this.setCooldown(PowerIndex.SKILL_1, ClientNetworking.getAppropriateConfig().cooldownsInTicks.magicianRedBindFailOrMiss);
            }
        } 
    }

    public void bitesTheDustModeToggleClient(){
        /*if (canExecuteMoveWithLevel(getShootingModeLevel())) {
            this.tryPower(PowerIndex.POWER_4, true);
            tryPowerPacket(PowerIndex.POWER_4);
            

            getStandUserSelf().roundabout$getStandPowers().tryPower(PowerIndex.NONE, true);
            tryPowerPacket(PowerIndex.NONE);
            //ClientUtil.stopDestroyingBlock();
        //}*/
    	
    	((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_4, true);
        tryPowerPacket(PowerIndex.POWER_4);
    }
    
    public boolean inBitesTheDustMode(){
        return this.BitesTheDustMode;
    }

    public boolean switchModes(){
    	this.BitesTheDustMode = !(this.BitesTheDustMode);
    	
        return true;
    }
    
    public boolean shootAirBubble(){
        this.setCooldown(PowerIndex.SKILL_2_SNEAK, 3);
        SoftAndWetExplosiveBubbleEntity bubble = getAirBubble();

        if (bubble != null){

            this.poseStand(OffsetIndex.FOLLOW);
            this.setAttackTimeDuring(-10);
            this.setActivePower(PowerIndex.POWER_2_SNEAK);
            shootAirBubbleSpeed(bubble,getAirBubbleSpeed());
            //bubbleListInit();
            //this.bubbleList.add(bubble);
            this.bombBubble = bubble;
            
            this.getSelf().level().addFreshEntity(bubble);

                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.EXPLOSIVE_BUBBLE_SHOT_EVENT, SoundSource.PLAYERS, 2F, (float) (0.98 + (Math.random() * 0.04)));

        }
        return true;
    }
    
    public float getAirBubbleSpeed(){
        return (float) (0.54F*(ClientNetworking.getAppropriateConfig().
                softAndWetSettings.explosiveBubbleShootSpeedMultiplier*0.01));
    }
    
    public void shootAirBubbleSpeed(SoftAndWetBubbleEntity ankh, float speed){
        ankh.setSped(speed);

        Vec3 addToPosition = new Vec3(0,this.self.getEyeHeight()*0.8F,0);
        Direction direction = ((IGravityEntity)this.self).roundabout$getGravityDirection();
        if (direction != Direction.DOWN){
            addToPosition = RotationUtil.vecPlayerToWorld(addToPosition,direction);
        }
        Vec3 pos = this.self.getPosition(1).add(addToPosition.x,addToPosition.y,addToPosition.z).add(this.self.getForward().scale(this.self.getBbWidth()*1));
        ankh.setPos(pos.x(), pos.y(), pos.z());
        ankh.shootFromRotationDeltaAgnostic(this.getSelf(), this.getSelf().getXRot(), this.getSelf().getYRot(), 1.0F, speed, 0);
    }
    
    public SoftAndWetExplosiveBubbleEntity getAirBubble(){
        SoftAndWetExplosiveBubbleEntity bubble = new SoftAndWetExplosiveBubbleEntity(this.self,this.self.level());
        bubble.absMoveTo(this.getSelf().getX(), this.getSelf().getY(), this.getSelf().getZ());
        bubble.setUser(this.self);
        bubble.setOwner(this.self);
        bubble.lifeSpan = 400;
        return bubble;
    }
    /**Punching stands only go for barrages when facing players, because barrages will be interrupted 100% of the time
     * otherwise.*/
    @Override
    public void tickMobAI(LivingEntity attackTarget){
        if (attackTarget != null && attackTarget.isAlive()){
            if ((this.getActivePower() == PowerIndex.ATTACK || this.getActivePower() == PowerIndex.BARRAGE)
                    || attackTarget.distanceTo(this.getSelf()) <= 5){
                rotateMobHead(attackTarget);
            }

            Entity targetEntity = getTargetEntity(this.self, -1);
            if (targetEntity != null && targetEntity.is(attackTarget)) {
                if (this.attackTimeDuring <= -1) {
                    double RNG = Math.random();
                    if (RNG < 0.35 && targetEntity instanceof Player && this.activePowerPhase <= 0 && !wentForCharge){
                        wentForCharge = true;
                        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.BARRAGE_CHARGE, true);
                    } else if (this.activePowerPhase < this.activePowerPhaseMax || this.attackTime >= this.attackTimeMax) {
                        wentForCharge = false;
                        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.ATTACK, true);
                    }
                }
            }
        }
    }
    @Override
    public boolean setPowerAttack(){
        if (this.activePowerPhase >= 3){
            this.activePowerPhase = 1;
        } else {
            this.activePowerPhase++;
            if (this.activePowerPhase == 3) {
                this.attackTimeMax= 37;
            } else {
                this.attackTimeMax= 27;
            }

        }

        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.ATTACK);
        this.setAttackTime(0);

        animateStand(this.activePowerPhase);
        poseStand(OffsetIndex.ATTACK);
        return true;
    }


    @Override
    public void punchImpact(Entity entity){
        this.setAttackTimeDuring(-10);
        if (entity != null) {
            float pow;
            float knockbackStrength;
            if (this.getActivePowerPhase() >= this.getActivePowerPhaseMax()) {
                /*The last hit in a string has more power and knockback if you commit to it*/
                pow = getHeavyPunchStrength(entity);
                knockbackStrength = 1F;
            } else {
                pow = getPunchStrength(entity);
                knockbackStrength = 0.2F;
            }
            if (StandDamageEntityAttack(entity, pow, 0, this.self)) {
                takeDeterminedKnockback(this.self, entity, knockbackStrength);
            } else {
                if (this.activePowerPhase >= this.activePowerPhaseMax) {
                    knockShield2(entity, 40);
                }
            }
        } else {
            // This is less accurate raycasting as it is server sided but it is important for particle effects
            float distMax = this.getDistanceOut(this.self, this.standReach, false);
            float halfReach = (float) (distMax * 0.5);
            Vec3 pointVec = DamageHandler.getRayPoint(self, halfReach);
            if (!this.self.level().isClientSide) {
                ((ServerLevel) this.self.level()).sendParticles(ParticleTypes.EXPLOSION, pointVec.x, pointVec.y, pointVec.z,
                        1, 0.0, 0.0, 0.0, 1);
            }
        }
    }

    @Override
    public boolean setPowerBarrageCharge() {
        animateStand(StandEntity.BARRAGE_CHARGE);
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.BARRAGE_CHARGE);
        this.poseStand(OffsetIndex.ATTACK);
        this.clashDone = false;
        playBarrageChargeSound();
        return true;
    }

    @Override
    public void setPowerBarrage() {
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.BARRAGE);
        this.poseStand(OffsetIndex.ATTACK);
        this.setAttackTimeMax(this.getBarrageRecoilTime());
        this.setActivePowerPhase(this.getActivePowerPhaseMax());
        animateStand(StandEntity.BARRAGE);
        playBarrageCrySound();
    }

    @Override
    public void updateMovesFromPacket(byte activePower){
        if (activePower == PowerIndex.BARRAGE){
            this.setActivePowerPhase(this.activePowerPhaseMax);
        }
        super.updateMovesFromPacket(activePower);
    }

    @Override
    public byte chooseBarrageSound(){
        return SoundIndex.BARRAGE_CRY_SOUND;
    }
    @Override
    protected Byte getSummonSound() {return SoundIndex.SUMMON_SOUND;
    }
    
    public SoundEvent getSoundFromByte(byte soundChoice){
       Roundabout.LOGGER.info(""+soundChoice);
        switch (soundChoice)
        {
            case SoundIndex.BARRAGE_CRY_SOUND -> {
                return ModSounds.KILLER_QUEEN_BARRAGE_EVENT;
            }
            case SoundIndex.SUMMON_SOUND -> {
                return ModSounds.KILLER_QUEEN_SUMMON_EVENT;
            }
        }
        return super.getSoundFromByte(soundChoice);
    }

    @Override
    public boolean tryBlockPosPower(int move, boolean forced, BlockPos blockPos){
    	return tryPower(move, forced);
    }


    @Override
    public void buttonInput2(boolean KeyIsDown, Options options) {

    }
    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {

    	
    	if (inBitesTheDustMode() == true) {
    		setSkillIcon(context, x, y, 1, StandIcons.KILLER_QUEEN_BTD_DAY, PowerIndex.SKILL_1);
    	} else if (isGuarding()) {
            setSkillIcon(context, x, y, 1, StandIcons.KILLER_QUEEN_BOMB_SETIINGS, PowerIndex.SKILL_1_SNEAK);
        } else if (this.getBomb() != BOMB_TYPE.NONE) {
    		setSkillIcon(context, x, y, 1, StandIcons.KILLER_QUEEN_BOMB_DETONATE, PowerIndex.NO_CD);
    	} else if (isHoldingSneak()){
            setSkillIcon(context, x, y, 1, StandIcons.KILLER_QUEEN_PLANT_BOMB_MOB, PowerIndex.SKILL_1_GUARD);
        } else {
        	setSkillIcon(context, x, y, 1, StandIcons.KILLER_QUEEN_PLANT_BOMB_BLOCK, PowerIndex.SKILL_1);
        }
        
    	if (inBitesTheDustMode()) {
    		setSkillIcon(context, x, y, 2, StandIcons.KILLER_QUEEN_BTD_COMBAT, PowerIndex.SKILL_2);
    	} else if (this.getBomb() != BOMB_TYPE.NONE && this.getBomb() != BOMB_TYPE.BUBBLE) {
        	setSkillIcon(context, x, y, 2, StandIcons.KILLER_QUEEN_BOMB_DEFUSE, PowerIndex.NO_CD);
        } else if (this.getBomb() == BOMB_TYPE.BUBBLE) {
    		setSkillIcon(context, x, y, 2, StandIcons.KILLER_QUEEN_BUBBLE_REDIRECT, PowerIndex.SKILL_2_GUARD);
    	} else if (isHoldingSneak()){
            setSkillIcon(context, x, y, 2, StandIcons.KILLER_QUEEN_BUBBLE_LAUNCH, PowerIndex.SKILL_2_SNEAK);
        } else {
        	setSkillIcon(context, x, y, 2, StandIcons.KILLER_QUEEN_PLANT_BOMB_ITEM, PowerIndex.SKILL_2);
        }
        
        if (isHoldingSneak() && !(inBitesTheDustMode())){
            setSkillIcon(context, x, y, 3, StandIcons.KILLER_QUEEN_SHA_SUMMON, PowerIndex.SKILL_3);
        } else {
            setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
        }
        
        if (inBitesTheDustMode()) {
        	setSkillIcon(context, x, y, 4, StandIcons.KILLER_QUEEN_BTD_DEACTIVATE, PowerIndex.SKILL_4);
        }else {
        	setSkillIcon(context, x, y, 4, StandIcons.KILLER_QUEEN_BTD_ACTIVATE, PowerIndex.SKILL_4);
        }
        	
        
    }
    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypas) {
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        return $$1;
    }
    @Override
    public void renderAttackHud(GuiGraphics context, Player playerEntity,
                                int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                float flashAlpha, float otherFlashAlpha) {
        StandUser standUser = ((StandUser) playerEntity);
        boolean standOn = PowerTypes.hasStandActive(self);
        int j = scaledHeight / 2 - 7 - 4;
        int k = scaledWidth / 2 - 8;

        float attackTimeDuring = standUser.roundabout$getAttackTimeDuring();
        if (standOn && standUser.roundabout$isClashing()) {
            int ClashTime = 15 - Math.round((attackTimeDuring / 60) * 15);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 30, ClashTime, 6);

        } else if (standOn && standUser.roundabout$getStandPowers().isBarrageAttacking() && attackTimeDuring > -1) {
            int ClashTime = 15 - Math.round((attackTimeDuring / standUser.roundabout$getStandPowers().getBarrageLength()) * 15);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 30, ClashTime, 6);

        } else if (standOn && standUser.roundabout$getStandPowers().isBarrageCharging()) {
            int ClashTime = Math.round((attackTimeDuring / standUser.roundabout$getStandPowers().getBarrageWindup()) * 15);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 30, ClashTime, 6);

        } else {
            int barTexture = 0;
            Entity TE = standUser.roundabout$getTargetEntity(playerEntity, -1);
            float attackTimeMax = standUser.roundabout$getAttackTimeMax();
            if (attackTimeMax > 0) {
                float attackTime = standUser.roundabout$getAttackTime();
                float finalATime = attackTime / attackTimeMax;
                if (finalATime <= 1) {


                    if (standUser.roundabout$getActivePowerPhase() == standUser.roundabout$getActivePowerPhaseMax()) {
                        barTexture = 24;
                    } else {
                        if (TE != null) {
                            barTexture = 12;
                        } else {
                            barTexture = 18;
                        }
                    }


                    context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
                    int finalATimeInt = Math.round(finalATime * 15);
                    context.blit(StandIcons.JOJO_ICONS, k, j, 193, barTexture, finalATimeInt, 6);


                }
            }
            if (standOn) {
                if (TE != null) {
                    if (barTexture == 0) {
                        context.blit(StandIcons.JOJO_ICONS, k, j, 193, 0, 15, 6);
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
        return Component.literal(  "Ashley").withStyle(ChatFormatting.YELLOW);
    }

    @Override public Component getSkinName(byte skinId) {
        switch (skinId)
        {
            case KillerQueenEntity.PART_4 -> {return Component.translatable("skins.roundabout.killer_queen.base");}
        }
        return Component.translatable("skins.roundabout.killer_queen.base");
    }
    
    public BOMB_TYPE getBomb() {
    	if (this.bombEntity != null) {
    		if (bombEntity instanceof ItemEntity) {
    			return BOMB_TYPE.ITEM;
    		} 
    		return BOMB_TYPE.ENTITY;
    	}
    	if (this.bombBubble != null) {
    		return BOMB_TYPE.BUBBLE;
    	}
    	if (this.bombBlock != null) {
    		return BOMB_TYPE.BLOCK;
    	}
    	
    	return BOMB_TYPE.NONE;
    }
 }
