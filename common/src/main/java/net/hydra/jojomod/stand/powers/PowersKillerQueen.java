package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.projectile.SoftAndWetBubbleEntity;
import net.hydra.jojomod.entity.projectile.SoftAndWetExplosiveBubbleEntity;
import net.hydra.jojomod.entity.stand.GreenDayEntity;
import net.hydra.jojomod.entity.stand.KillerQueenEntity;
import net.hydra.jojomod.entity.stand.SoftAndWetEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
//import net.hydra.jojomod.entity.stand.StarPlatinumEntity;
//import net.hydra.jojomod.entity.stand.TheWorldEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.CooldownInstance;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewPunchingStand;
import net.hydra.jojomod.entity.substand.BlockBombEntity;
import net.hydra.jojomod.entity.visages.mobs.AvdolNPC;
import net.hydra.jojomod.util.config.ClientConfig;
import net.hydra.jojomod.util.config.ConfigManager;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
//import java.util.UUID;
//import java.util.Dictionary;
//import java.util.Hashtable;
import java.util.Objects;

import org.joml.Vector3f;



public class PowersKillerQueen extends NewPunchingStand {
	public PowersKillerQueen(LivingEntity self) {super(self);}
	
	// TODO Make Impale code
	// TODO Make air bubble bomb spawn and entity
	// TODO Make bomb entity
	// TODO Make bomb item
	// TODO Make bomb block (FINISHED) -- Adjust damage
	// TODO Bites The Dust
	
	// TODO Audio Translations
	
	// TODO-FIX pls someone make the block bomb rotation fixed
	
	private static final byte
		PLANTED=52,
		DETONATE=54,
		DEFUSE = 57,
		EXPLOSION = 58,
	
		BOMB_NONE=0,
		BOMB_BLOCK=1,
		BOMB_ITEM=2,
		BOMB_ENTITY=3,
		BOMB_BUBBLE=4;
	
	private byte currentBombStatus = BOMB_NONE;
	
	public static ArrayList<String> blowableBlocksBlackList = Lists.newArrayList("minecraft:bedrock", "minecraft:obsidian");
	
	public boolean isBlockBlackListed(BlockState bs) {
		ResourceLocation rl = BuiltInRegistries.BLOCK.getKey(bs.getBlock());
		return (blowableBlocksBlackList != null && !blowableBlocksBlackList.isEmpty() && rl != null && blowableBlocksBlackList.contains(rl.toString()));
	}
	
	public Entity bombEntity = null;
	public BlockBombEntity bombBlock = null;
	public Entity bombBubble = null;
	
	private boolean BitesTheDustMode = false;
	public boolean inBitesTheDustMode() {return this.BitesTheDustMode;}
	public boolean switchModes(){
    	this.BitesTheDustMode = !(this.BitesTheDustMode);
        return true;
    }
	
	private boolean hasStrayCat = false;
	
	public float standReach = 5;
	public boolean wentForCharge = false;
	public int chargedFinal;
	public boolean holdDownClick = false;
	
	public static int maxKickTime = 25;
	private static int blockPlantMaxTicks = 5;
	private int ticksCount = -1;
    
    @Override
    public int getMaxGuardPoints(){ return 15; }
    
    public static final byte
	    PART_4 = 0,
		MANGA = 1,
		UMBRA = 2,
		GOGO = 3,
		ARTWORK = 4,
		CRACKED = 5,
		CREEPER = 6,
		STRAY = 7,
		NIGHTMARE = 8,
		LIMBUSMORTIS = 9,
		JOJOLION = 10,
		GUNPOWDER = 11,
		FINAL = 12,
		DEADLY = 13,
		YELLOW = 14,
		TAMA = 15,
		MINESWEEPER = 16,
		NOTW = 17,
		MEMENTO = 18,
		STARDUST = 19;
    
    @Override
    public List<Byte> getSkinList() {
        return Arrays.asList(
        		PART_4 ,
        		MANGA,
        		UMBRA,
        		GOGO,
        		ARTWORK,
        		CRACKED,
        		CREEPER,
        		STRAY,
        		NIGHTMARE,
        		LIMBUSMORTIS,
        		JOJOLION,
        		GUNPOWDER,
        		FINAL,
        		DEADLY,
        		YELLOW,
        		TAMA,
        		MINESWEEPER,
        		NOTW,
        		MEMENTO,
        		STARDUST
        );
    }
    
    @Override public boolean isWip(){return true;}
    @Override public Component ifWipListDevStatus(){ return Component.translatable(  "roundabout.dev_status.active").withStyle(ChatFormatting.AQUA);}
    @Override public Component ifWipListDev(){ return Component.literal("DOGael Arts").withStyle(ChatFormatting.YELLOW);}
    @Override public StandPowers generateStandPowers(LivingEntity entity){ return new PowersKillerQueen(entity);}
    @Override public StandEntity getNewStandEntity(){ return ModEntities.KILLER_QUEEN.create(this.getSelf().level());}

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {

    	if (inBitesTheDustMode() == true) {
    		setSkillIcon(context, x, y, 1, StandIcons.KILLER_QUEEN_BTD_DAY, PowerIndex.SKILL_EXTRA);
    	} else if (isGuarding()) {
            setSkillIcon(context, x, y, 1, StandIcons.KILLER_QUEEN_BOMB_SETIINGS, PowerIndex.NO_CD);
        } else if (this.currentBombStatus != BOMB_NONE) {
        	
    		setSkillIcon(context, x, y, 1, StandIcons.KILLER_QUEEN_BOMB_DETONATE, PowerIndex.NO_CD);
    	} else if (isHoldingSneak()){
            setSkillIcon(context, x, y, 1, StandIcons.KILLER_QUEEN_PLANT_BOMB_MOB, PowerIndex.SKILL_1_SNEAK);
        } else {
        	setSkillIcon(context, x, y, 1, StandIcons.KILLER_QUEEN_PLANT_BOMB_BLOCK, PowerIndex.SKILL_1);
        }
        
    	if (inBitesTheDustMode()) {
    		setSkillIcon(context, x, y, 2, StandIcons.KILLER_QUEEN_BTD_COMBAT, PowerIndex.SKILL_EXTRA_2);
    	} else if (this.currentBombStatus != BOMB_NONE && this.currentBombStatus != BOMB_BUBBLE) {
        	setSkillIcon(context, x, y, 2, StandIcons.KILLER_QUEEN_BOMB_DEFUSE, PowerIndex.NO_CD);
        } else if (this.currentBombStatus == BOMB_BUBBLE) {
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
    
    @Override
    public boolean isAttackIneptVisually(byte activeP, int slot) {
    	if (slot == 1) {
    		if(inBitesTheDustMode()) {
    			return !ClientNetworking.getAppropriateConfig().killerQueenSettings.enableBitesTheDustDayMode;
    		}
    	}
    	if (slot == 2 && !this.BitesTheDustMode) {
    		if (this.self.isCrouching() && this.currentBombStatus == BOMB_NONE){
    			return !this.hasStrayCat;
    		} 
    	}
    		
		return super.isAttackIneptVisually(activeP, slot);
    }
    
    @Override
    public void powerActivate(PowerContext context) {
    	
        switch (context)
        {
        	case SKILL_1_NORMAL -> {
        		if (!this.inBitesTheDustMode()) {
	        		if (this.currentBombStatus == BOMB_NONE) {
	        			tryBlockPlantBomb();
	        		}else {
	        			detonateClient();
	        		}
        		}
        	}
        	case SKILL_1_GUARD -> {
        		this.tryBombConfig();
        	}
        	case SKILL_2_NORMAL -> {
        		if (!this.inBitesTheDustMode()) {
        			
	        		if (this.currentBombStatus == BOMB_NONE) {
	        			// item plant method
	        		}else {
	        			defuseClient();
	        		}
        		}
        	}
        	case SKILL_2_CROUCH -> {
        		if (!this.inBitesTheDustMode()) {
        			//tryShootAirBubbleClient();
        		}
        	}
        	case SKILL_3_NORMAL, SKILL_3_GUARD -> {
        		tryToDashClient();
        	}
        	case SKILL_3_CROUCH, SKILL_3_CROUCH_GUARD -> {
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
    public boolean canGuard(){
    	if (this.getActivePower() == DETONATE) {
    		return false;
    	}
        return super.canGuard();
    }
    
    @Override
    public void tickPower(){
    	
    	if (currentBombStatus == BOMB_BLOCK) {
    		//Vec3 iconPos = new Vec3(this.bombBlock.getX(), this.bombBlock.getY(), this.bombBlock.getZ());
    		
    		if (isClient()) {
    			//this.getSelf().level().addAlwaysVisibleParticle(getImpactParticle(), iconPos.x, iconPos.y, iconPos.z, iconPos.x, iconPos.y, iconPos.z);
    		}else {
    			if (this.currentBombStatus == BOMB_BLOCK) {
    				if (this.bombBlock.blockGotDestroyed()) { this.defuseServer(); }
    				
    				if(Objects.nonNull(this.getStandEntity(this.self))) {
    		    		byte currentAnim = this.getStandEntity(this.self).getAnimation();
    		            if ((currentAnim == KillerQueenEntity.DETONATE || currentAnim == KillerQueenEntity.BLOCK_PLANT) && this.ticksCount == 0) {
    		                this.animateStand(StandEntity.IDLE);
    		                this.poseStand(OffsetIndex.FOLLOW);
    		    			
    		            }
    		        }
    				
    				if (this.getActivePower() == DETONATE && this.ticksCount == 0) {
    					this.explode();
    				}
    			}
    			
    			if (this.ticksCount >= 0) { this.ticksCount--;}
    			
    			ClientConfig clientConfig = ConfigManager.getClientConfig();
    	    	int bombConf = clientConfig.dynamicSettings.KillerQueenCurrentBombConfig;
    	    	
    			
    			if (bombConf >= 2) {
    				if (this.currentBombStatus == BOMB_BLOCK) {
    					if(Objects.nonNull(this.bombBlock)) {
    						if (this.bombBlock.detectContact()) {
    							this.detonate();
    						}
    					}
    				}
    			}
    			
    		}
    	}
    	
        super.tickPower();
    }
    
    @Override
    public float inputSpeedModifiers(float basis){
        if (this.activePower == PowerIndex.BARRAGE_CHARGE_2) {
            basis*=0.5f;
        } else if (this.activePower == PowerIndex.SNEAK_ATTACK_CHARGE){
            if (this.getSelf().isCrouching()) {
                float f = Mth.clamp(0.3F + EnchantmentHelper.getSneakingSpeedBonus(this.getSelf()), 0.0F, 1.0F);
                float g = 1 / f;
                basis *= g;
            }
            basis *= 0.6f;
        }
        
        return super.inputSpeedModifiers(basis);
    }
    
    
    

    @Override
    public void buttonInputAttack(boolean keyIsDown, Options options) {
        if (!consumeClickInput) {
            if (holdDownClick) {
                if (!keyIsDown) {
                    if (this.getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE) {
                        int atd = this.getAttackTimeDuring();
                        this.tryIntPower(PowerIndex.SNEAK_ATTACK, true, atd);
                        tryIntPowerPacket(PowerIndex.SNEAK_ATTACK, atd);
                    }
                    holdDownClick = false;
                }
            } else {
                if (keyIsDown) {
                    if (!this.inBitesTheDustMode()){
                        if (!isHoldingSneak()) {
                            super.buttonInputAttack(keyIsDown, options);
                        } else {
                            if (this.canAttack()) {
                            	//this.tryPower(PowerIndex.SNEAK_ATTACK, true);
                                //tryPowerPacket(PowerIndex.SNEAK_ATTACK);
                            	this.tryPower(PowerIndex.SNEAK_ATTACK_CHARGE, true);
                                holdDownClick = true;
                                tryPowerPacket(PowerIndex.SNEAK_ATTACK_CHARGE);
                            } else {
                                super.buttonInputAttack(keyIsDown, options);
                            }
                        }
                    }
                }
            }
        } else {
            if (!keyIsDown) {
                consumeClickInput = false;
            }
        }
    }
    
    @Override
    public void handleStandAttack(Player player, Entity target){
    	if (this.getActivePower() == PowerIndex.SNEAK_ATTACK){
            kickAttackImpact(target);
        }
    	super.handleStandAttack(player,target);
    }
    
    public float getKickAttackStrength(Entity entity){
        float punchD = this.getPunchStrength(entity)*1.9F+this.getHeavyPunchStrength(entity);

        if (this.getReducedDamage(entity)){
            return (((float)this.chargedFinal/(float)maxKickTime)*punchD);
        } else {
            return (((float)this.chargedFinal/(float)maxKickTime)*punchD)+1;
        }
    }
    
    public float getKickAttackKnockback(){ return (((float)this.chargedFinal/(float)maxKickTime)*1.8F); }
    public int getKickAttackKnockShieldTime(){ return 40; }
    
    public void kickAttackImpact(Entity entity){
    	 this.setAttackTimeDuring(-20);
         if (entity != null) {
             if (chargedFinal < maxKickTime) {
                 //hitParticlesCenter(entity);
            	 hitParticles(entity);
             }

             float pow;
             float knockbackStrength;
             pow = getKickAttackStrength(entity);
             knockbackStrength = getKickAttackKnockback();
             
             if (StandDamageEntityAttack(entity, pow, 0, this.self)) {
                 if (entity instanceof LivingEntity LE) {
                     if (chargedFinal >= maxKickTime) {
                         addEXP(5, LE);
                     } else {
                         addEXP(1, LE);
                     }
                 }
                 takeDeterminedKnockbackWithY(this.self, entity, knockbackStrength);
             } else {
                 if (chargedFinal >= maxKickTime) {
                     knockShield2(entity, getKickAttackKnockShieldTime());

                 }
             }

             if (entity instanceof LivingEntity LE && !(LE instanceof Player PE && PE.isCreative())) {
                 if (chargedFinal >= maxKickTime) {
                     StandUser SE = ((StandUser) LE);
                     
                     SE.roundabout$setStoredVelocity(
                          this.self.getForward().normalize().scale(3.14).add(0,0.033f,0)
                     );
                     

                     if (!this.self.level().isClientSide()) {
                         /*Vec3 $$2 = LE.getDeltaMovement();
                         float $$4 = (float) Mth.floor(LE.getY());*/
                         for (int $$8 = 0; (float) $$8 < 1.0F + LE.getBbWidth() * 20.0F; $$8++) {
                             double $$9 = (LE.level().random.nextDouble() * 2.0 - 1.0) * (double) LE.getBbWidth();
                             double $$10 = (LE.level().random.nextDouble() * 2.0 - 1.0) * (double) LE.getBbWidth();

                             Vec3 vec3 = new Vec3($$9,1.0F,$$10);
                             Direction direction = ((IGravityEntity)this.self).roundabout$getGravityDirection();
                             if (direction != Direction.DOWN){
                                 vec3 = RotationUtil.vecPlayerToWorld(vec3,direction);
                             }

                             
                         }
                     }

                     
                     Vec3 storedVec = SE.roundabout$getStoredVelocity();
                     MainUtil.takeLiteralUnresistableKnockbackWithY(LE, storedVec.x, storedVec.y, storedVec.z);
                     
                 }
             }

         } else {
             // This is less accurate raycasting as it is server sided but it is important for particle effects
             float distMax = this.getDistanceOut(this.self, this.getReach(), false);
             float halfReach = (float) (distMax * 0.5);
             Vec3 pointVec = DamageHandler.getRayPoint(self, halfReach);
             if (!this.self.level().isClientSide) {
                 ((ServerLevel) this.self.level()).sendParticles(ModParticles.PUNCH_MISS, pointVec.x, pointVec.y, pointVec.z,
                         1, 0.0, 0.0, 0.0, 1);
             }
         }

         SoundEvent SE;
         float pitch = 1F;
         if (entity != null) {
             //SE = getKickAttackSound();
        	 SE = ModSounds.PUNCH_2_SOUND_EVENT; // create Kick event
             pitch = 1.2F;
         } else {
             SE = ModSounds.PUNCH_2_SOUND_EVENT;
         }
         /*
         if (!this.self.level().isClientSide()) {
             this.self.level().playSound(null, this.self.blockPosition(), SE, SoundSource.PLAYERS, 0.95F, pitch);
             if (chargedFinal >= maxKickTime && entity instanceof LivingEntity) {
                 this.self.level().playSound(null, this.self.blockPosition(), ModSounds.WATER_ENCASE_EVENT, SoundSource.PLAYERS, 1F, pitch);
             }
         }*/
    }
    
    
    
    @Override
    public boolean setPowerOther(int move, int lastMove) {

    	if (move == PowerIndex.POWER_1) {
    		this.blockPlantBomb();
    	} else if (move == PowerIndex.POWER_4) {
    		return switchModes();
    	} else if (move == PowersKillerQueen.DEFUSE) {
    		return defuseServer();
    	} else if (move == PowerIndex.POWER_2) {
    		//return shootAirBubble();
    	} else if (move == PowerIndex.POWER_2_SNEAK) {
    		//return shootAirBubble();
    	} else if (move == PowersKillerQueen.DETONATE) {
    		return detonate();
    	} else if (move == PowerIndex.SNEAK_ATTACK_CHARGE){
            return this.setPowerKickWindup();
        } else if (move == PowerIndex.SNEAK_ATTACK){
            return this.setPowerKick();
        }
    	
    	return super.setPowerOther(move,  lastMove);
    }
    
    // Client2Server Receiver Methods
    
    @Override
    public boolean tryPower(int move, boolean forced) {
        return super.tryPower(move, forced);
    }
    @Override
    public boolean tryIntPower(int move, boolean forced, int chargeTime) {
    	if (move == PowerIndex.SNEAK_ATTACK) {
            this.chargedFinal = chargeTime;
        }
    	
    	return super.tryIntPower(move, forced, chargeTime);
    }

    @Override
    public boolean tryBlockPosPower(int move, boolean forced, BlockPos blockPos){
    	return super.tryBlockPosPower(move, forced, blockPos);
    }
    
    // Server2Client Receiver Methods

    @Override
    public void updatePowerInt(byte activePower, int data) {
        switch (activePower) {
            case PowersKillerQueen.PLANTED-> {
               this.currentBombStatus = (byte)data;
               if (data == BOMB_NONE) {
            	   this.setPowerNone();
               }
            }

        }
        super.updatePowerInt(activePower,data);
    }
    
    public void syncBombStatus(byte status) {
    	this.currentBombStatus = status;
    	this.updatePowerInt(PowersKillerQueen.PLANTED, status);
        S2CPacketUtil.sendIntPowerDataPacket((Player)this.getSelf(),PowersKillerQueen.PLANTED, status);
    }
    
    @Override
    public void updateUniqueMoves() {
    	if (this.getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE){
            updateKickAttackCharge();
        } else if (this.getActivePower() == PowerIndex.SNEAK_ATTACK){
            updateKickAttack();
        }
    	super.updateUniqueMoves();
    }
    
    public void updateKickAttackCharge(){
        if (this.attackTimeDuring > -1) {
            if (this.attackTimeDuring >= maxKickTime &&
                    (!(this.getSelf() instanceof Player) || (this.self.level().isClientSide() && isPacketPlayer()))){
                int atd = this.getAttackTimeDuring();
                ((StandUser) this.getSelf()).roundabout$tryIntPower(PowerIndex.SNEAK_ATTACK, true, maxKickTime);
                if (this.self.level().isClientSide()){
                    tryIntPowerPacket(PowerIndex.SNEAK_ATTACK,atd);
                }
            }
        }
    }
    public void updateKickAttack(){
        if (this.attackTimeDuring > -1) {
            if (this.attackTimeDuring == 5) {
            	if (chargedFinal >= maxKickTime) {
                    this.setAttackTimeMax((int) (ClientNetworking.getAppropriateConfig().killerQueenSettings.kickMinimumCooldown + chargedFinal * 1.5));
                } else {
                    this.setAttackTimeMax((int) (ClientNetworking.getAppropriateConfig().killerQueenSettings.kickMinimumCooldown + chargedFinal));
                }
                this.setAttackTime(0);
                this.setActivePowerPhase(this.getActivePowerPhaseMax());

                if (this.self instanceof Player){
                    if (isPacketPlayer()){
                        //Roundabout.LOGGER.info("Time: "+this.self.getWorld().getTime()+" ATD: "+this.attackTimeDuring+" APP"+this.activePowerPhase);
                        this.attackTimeDuring = -10;
                        C2SPacketUtil.intToServerPacket(PacketDataIndex.INT_STAND_ATTACK,getTargetEntityId());
                    }
                } else {
                    /*Caps how far out the punch goes*/
                    Entity targetEntity = getTargetEntity(this.self,-1);
                    kickAttackImpact(targetEntity);
                }
            }
        }
    }
    
    
    // Clients Move sender/try:
    
    public void tryToDashClient(){
        if (vaultOrFallBraceFails()){
            dash();
        }
    }
    
    public void tryBombConfig() {
    	if (!this.BitesTheDustMode ) {
    		ClientUtil.openBombConfigScreen();
    	}
    }
    
    
    public void tryFastKick() {
    	if (!this.BitesTheDustMode ) {
    		((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.SNEAK_ATTACK, true);
            tryPowerPacket(PowerIndex.SNEAK_ATTACK);
    	}
    }
    
    public void defuseClient() {   	
    	((StandUser) this.getSelf()).roundabout$tryPower(PowersKillerQueen.DEFUSE, true);
        tryPowerPacket(PowersKillerQueen.DEFUSE);
    	
        this.currentBombStatus = BOMB_NONE;

    }
    
    public void detonateClient() {
    	if (!this.onCooldown(PowerIndex.SKILL_1)) {
	    	if (currentBombStatus != BOMB_NONE) {
	    		((StandUser) this.getSelf()).roundabout$tryPower(PowersKillerQueen.DETONATE, true);
	            tryPowerPacket(PowersKillerQueen.DETONATE);
	            
	            if (currentBombStatus == BOMB_BLOCK) {
	            	this.setCooldown(PowerIndex.SKILL_1, ClientNetworking.getAppropriateConfig().killerQueenSettings.blockPlantCooldown);
	            }
	            
	    	}
	    	
	    	this.currentBombStatus = BOMB_NONE;
    	}
    }

    public void bitesTheDustModeToggleClient(){ 
    	((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_4, true);
        tryPowerPacket(PowerIndex.POWER_4);
    }
    
    public void tryBlockPlantBomb() {
    	if (!this.onCooldown(PowerIndex.SKILL_1)) {
    		((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_1, true);
        	tryPowerPacket(PowerIndex.POWER_1);
    	}
    }
    // server side methods
    
    public boolean defuseServer() {
    	if (!this.isClient() && currentBombStatus == BOMB_BLOCK) {
    		this.bombBlock.discard();
    		this.bombBlock = null;

    	}
    	
    	this.syncBombStatus(BOMB_NONE);
    	
    	return true;
    }
     
    public boolean blockPlantBomb() { 
    	if (!this.isClient()) {
			StandEntity standEntity = ((StandUser) this.getSelf()).roundabout$getStand();
			
		    if (standEntity != null && standEntity.isAlive() && !standEntity.isRemoved() && this.currentBombStatus == BOMB_NONE) {
		    	
		    	Vec3 vec3d = this.getSelf().getEyePosition(0);
		        Vec3 vec3d2 = this.getSelf().getViewVector(0);
		        Vec3 vec3d3 = vec3d.add(vec3d2.x * 3.5, vec3d2.y * 3.5, vec3d2.z * 3.5);
		        
		        BlockHitResult blockHit = this.getSelf().level().clip(new ClipContext(vec3d, vec3d3, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this.getSelf()));       
		        BlockPos pos = blockHit.getBlockPos();
		    	BlockState state = this.getSelf().level().getBlockState(pos);
		    	
		    	if (!isBlockBlackListed(state) && !state.isAir()) {
		    		this.bombBlock = ModEntities.BLOCK_BOMB.create(this.getSelf().level());
		    		this.bombBlock.setUser(this.self);
		    		this.bombBlock.setBlockPos(pos);
		    		this.self.level().addFreshEntity(this.bombBlock);
		    		this.currentBombStatus = BOMB_BLOCK;
		    		
		    		this.syncBombStatus(BOMB_BLOCK);
		    		
		    		this.animateStand(KillerQueenEntity.BLOCK_PLANT);
		    		this.poseStand(OffsetIndex.ATTACK);
		    		this.ticksCount = blockPlantMaxTicks;
		    		
		    	}
		    }
    	}
    	return true;                     
    }           
    
    public boolean itemPlantBomb() { 
    	                                 
    	return true;                     
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

    // sound related stuff
    
    @Override
    public byte chooseBarrageSound(){ return SoundIndex.BARRAGE_CRY_SOUND;}
    
    @Override
    protected Byte getSummonSound() { return SoundIndex.SUMMON_SOUND;}
    
    @Override
    public SoundEvent getSoundFromByte(byte soundChoice){
       if (soundChoice == SoundIndex.BARRAGE_CRY_SOUND) {
    	   return ModSounds.KILLER_QUEEN_BARRAGE_EVENT;
       }else if (soundChoice == SoundIndex.SUMMON_SOUND) {
    	   return ModSounds.KILLER_QUEEN_SUMMON_EVENT;
       }else if (soundChoice == PowersKillerQueen.DETONATE) {
    	   return ModSounds.KILLER_QUEEN_DETONATE_EVENT;
       }else if (soundChoice == PowersKillerQueen.EXPLOSION) {
    	   return ModSounds.KILLER_QUEEN_EXPLOSION_EVENT;
       }
       
        return super.getSoundFromByte(soundChoice);
    }

    public void explosionSFX(Vec3 pos, float range) {
    	if (!this.self.level().isClientSide) {
            ServerLevel serverWorld = ((ServerLevel) this.self.level());

            for (int j = 0; j < serverWorld.players().size(); ++j) {
                ServerPlayer serverPlayerEntity = ((ServerLevel) this.self.level()).players().get(j);

                if (((ServerLevel) serverPlayerEntity.level()) != serverWorld) {
                    continue;
                }

                BlockPos blockPos = serverPlayerEntity.blockPosition();
                if (blockPos.closerToCenterThan(pos, range)) {
                
                    S2CPacketUtil.sendPlaySoundPacket(serverPlayerEntity, serverPlayerEntity.getId(), EXPLOSION);
                   
                }
            }
        }
    }
    
    // more GUI things
    

    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypas) {
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        return $$1;
    }
    @Override
    public void renderAttackHud(GuiGraphics context, Player playerEntity,
                                int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                float flashAlpha, float otherFlashAlpha) {
    	super.renderAttackHud(context, playerEntity,
                scaledWidth, scaledHeight, ticks, vehicleHeartCount,
                flashAlpha, otherFlashAlpha);
        
    }
   
    @Override public Component getSkinName(byte skinId) {
    	
    	switch (skinId)
        {
            case KillerQueenEntity.PART_4 -> {return Component.translatable("skins.roundabout.killer_queen.anime");}
            case KillerQueenEntity.GOGO -> {return Component.translatable("skins.roundabout.killer_queen.agogo");}
            case KillerQueenEntity.MANGA -> {return Component.translatable("skins.roundabout.killer_queen.manga");}
            case KillerQueenEntity.UMBRA -> {return Component.translatable("skins.roundabout.killer_queen.umbra");}
            case KillerQueenEntity.ARTWORK -> {return Component.translatable("skins.roundabout.killer_queen.artwork");}
            case KillerQueenEntity.CRACKED -> {return Component.translatable("skins.roundabout.killer_queen.cracked");}
            case KillerQueenEntity.CREEPER -> {return Component.translatable("skins.roundabout.killer_queen.creeper");}
            case KillerQueenEntity.STRAY -> {return Component.translatable("skins.roundabout.killer_queen.stray");}
            case KillerQueenEntity.NIGHTMARE -> {return Component.translatable("skins.roundabout.killer_queen.nightmare");}
            case KillerQueenEntity.LIMBUSMORTIS -> {return Component.translatable("skins.roundabout.killer_queen.mortis");}
            case KillerQueenEntity.JOJOLION -> {return Component.translatable("skins.roundabout.killer_queen.jojolion");}
            case KillerQueenEntity.GUNPOWDER -> {return Component.translatable("skins.roundabout.killer_queen.gunpowder");}
            case KillerQueenEntity.FINAL -> {return Component.translatable("skins.roundabout.killer_queen.final");}
            case KillerQueenEntity.DEADLY -> {return Component.translatable("skins.roundabout.killer_queen.deadly");}
            case KillerQueenEntity.YELLOW -> {return Component.translatable("skins.roundabout.killer_queen.yellow");}
            case KillerQueenEntity.TAMA -> {return Component.translatable("skins.roundabout.killer_queen.tama");}
            case KillerQueenEntity.MINESWEEPER -> {return Component.translatable("skins.roundabout.killer_queen.minesweeper");}
            case KillerQueenEntity.NOTW -> {return Component.translatable("skins.roundabout.killer_queen.notw");}
            case KillerQueenEntity.MEMENTO -> {return Component.translatable("skins.roundabout.killer_queen.final");}
            case KillerQueenEntity.STARDUST -> {return Component.translatable("skins.roundabout.killer_queen.stardust");}
        }
        return Component.translatable("skins.roundabout.killer_queen.anime");
    }
    
    // Explosion related stuff
    
    public void explodeEffects(Vec3 pos) {
    	float range = 0.6f;
    	
    	((ServerLevel) this.getSelf().level()).sendParticles(ModParticles.KILLER_QUEEN_EXPLOSION,
                pos.x,
                pos.y+1.0f,
                pos.z,
                18, range, range+0.3f, range, 1.0);
    	
    	((ServerLevel) this.getSelf().level()).sendParticles(new DustParticleOptions(new Vector3f(0.02F, 0.02F, 0.04F), 2f),
    			pos.x,
                pos.y+1.0f,
                pos.z,
                12, range, range+1.2f, range, 0.001);
    	
    	// Sound Emitter:
    	
    	 explosionSFX(pos, 10);
    	
    }
    
    
    
    
    public void explosionHurt(Vec3 pos) {
    	DamageSource dmg = ModDamageTypes.of(this.getSelf().level(), DamageTypes.PLAYER_EXPLOSION, this.getSelf());;
    	float range = 1.5f;
    	List<Entity> damages = MainUtil.genHitbox(this.getSelf().level(), pos.x(), pos.y(), pos.z(), range, range, range);
    	
    	for(int j = 0;j<damages.size();j++) {
            Entity entity = damages.get(j);
            double dist = entity.distanceToSqr(pos);
            float perc = 1.0f - (((float)dist/ (range * range * range))*0.75f); 
            
            entity.hurt(dmg, perc*ClientNetworking.getAppropriateConfig().killerQueenSettings.explosionDetonateMaxDamage);
            
            Vec3 knockback = new Vec3(entity.getX() - pos.x(), entity.getY() - pos.y(), entity.getZ() - pos.z());
            knockback.normalize();
            knockback.multiply(perc*3.0f, perc*3.0f, perc*3.0f);
            
            MainUtil.takeLiteralUnresistableKnockbackWithY(entity, knockback.x, knockback.y, knockback.z);
            
        }
    	
    }
    
    public void explodeBlocks(BlockPos location) {
    	Vec3 center = new Vec3(location.getX(), location.getY(), location.getZ());
    	
    	for (BlockPos pos : BlockPos.betweenClosed(location.offset(1, 1, 1), location.offset(-1, -1, -1))) {
			BlockState info = this.getSelf().level().getBlockState(pos);
			if (isBlockBlackListed(info)) {continue;}
			
			
			
			Double explosionDistance = 2.0 + ((double) this.getSelf().getRandom().nextIntBetweenInclusive(-2, 2) / 10.0);
			
			Double dist2 = center.distanceToSqr(pos.getX(), pos.getY(), pos.getZ());
			
			if (dist2 <= explosionDistance) {
				boolean shouldDrop = !info.requiresCorrectToolForDrops();
				this.getSelf().level().destroyBlock(pos, shouldDrop);
			}
		}
    }
    
    public boolean explode() {
    	ClientConfig clientConfig = ConfigManager.getClientConfig();
    	int bombConf = clientConfig.dynamicSettings.KillerQueenCurrentBombConfig;
    	
		if (!this.isClient() && this.currentBombStatus == PowersKillerQueen.BOMB_BLOCK) {
			BlockPos pos = this.bombBlock.getBlockPos();
			if (!isClient()) {
				if (bombConf % 2 == 1 && ClientNetworking.getAppropriateConfig().killerQueenSettings.blocksDestruction) {explodeBlocks(pos);}
				this.explosionHurt(pos.getCenter());
				this.explodeEffects(pos.getCenter());
			}
    		this.bombBlock.discard();
    		this.bombBlock = null;
    	}
    	
		this.syncBombStatus(BOMB_NONE);
		this.setPowerNone();
		
    	return true;
    }
    
    public boolean detonate() {
    	if (!this.isClient() && this.getActivePower() == PowerIndex.NONE) {
    		this.playSoundsIfNearby(DETONATE, 27, true);
    		this.animateStand(KillerQueenEntity.DETONATE);
    		this.poseStand(OffsetIndex.ATTACK);
    		
    		this.setActivePower(DETONATE);
    		
    		this.ticksCount = ClientNetworking.getAppropriateConfig().killerQueenSettings.explosionActivationCooldown;
    	}
    	
    	return true;
    }
    
    
    // animations:
    public boolean setPowerKick() {
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.SNEAK_ATTACK);
        this.poseStand(OffsetIndex.ATTACK);
        chargedFinal = Math.min(this.chargedFinal,maxKickTime);
        if (chargedFinal >= maxKickTime){
            this.animateStand(KillerQueenEntity.HEAVY_STRIKE);
        } else {
            this.animateStand(KillerQueenEntity.KICK);
        }
        //playBarrageCrySound();
        return true;
    }
    public boolean setPowerKickWindup() {
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.SNEAK_ATTACK_CHARGE);
        this.animateStand(KillerQueenEntity.KICK_CHARGE);
        this.poseStand(OffsetIndex.GUARD);
        return true;
    }
   
 }
