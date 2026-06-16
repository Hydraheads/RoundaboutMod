package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IFatePlayer;
import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.access.IMob;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.KillerQueenEntity;
import net.hydra.jojomod.entity.substand.SheerHeartAttackEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.entity.visages.mobs.JotaroNPC;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.ModGamerules;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.fates.powers.ZombieFate;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.item.PlayerHandItem;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewPunchingStand;
import net.hydra.jojomod.entity.substand.BlockBombEntity;
import net.hydra.jojomod.util.config.ConfigManager;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.ExplosionUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PlayerHeadItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.PlayerHeadBlock;
import net.minecraft.world.level.block.PlayerWallHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
//import java.util.UUID;
//import java.util.Dictionary;
//import java.util.Hashtable;
import java.util.Objects;

public class PowersKillerQueen extends NewPunchingStand {
	public PowersKillerQueen(LivingEntity self) {super(self);}

    @Override
    /**Override to add disable config*/
    public boolean isStandEnabled(){
        return ClientNetworking.getAppropriateConfig().killerQueenSettings.enableKillerQueen;
    }

	// TODO Make air bubble bomb spawn and entity
	// TODO Make bomb item
	// TODO Bites The Dust
	
	// TODO Audio Translations

    // TODO fix Sheer Heart Attack code
    // TODO explosions ignore ores on shift
	
	private static final byte
		PLANTED=53,
		DETONATE=54,
		DEFUSE = 55,
		EXPLOSION = 56,
        SHEER_HEART_ATTACK = 57,
        ENTITY_BOMB = 58,
        BOMB_CONFIG = 59,
        BITES_THE_DUST_COMBAT = 60,
        BITES_THE_DUST_DAY = 61,
        IMPALE_NOISE = 105,

    // Bomb Status things
		BOMB_NONE=0,
		BOMB_BLOCK=1,
		BOMB_ITEM=2,
		BOMB_ENTITY=3,
		BOMB_BUBBLE=4,
        BOMB_CONTACT = 5,
    // Sheer Heart Attack Status things
        SHA_NONE = 0,
        SHA_SEND = 1,
        SHA_RETREAT = 3;


	
	private byte currentBombStatus = BOMB_NONE;
	private boolean shaReleased = false;
    private byte currentShaStatus = SHA_NONE;
    private int bombConfig = 2;
	
	 public void syncBombStatus(byte status) {
         this.currentBombStatus = status;
         this.updatePowerInt(PowersKillerQueen.PLANTED, status);
         S2CPacketUtil.sendIntPowerDataPacket((Player)this.getSelf(),PowersKillerQueen.PLANTED, status);

    }

    public void syncShaStatus(byte status) {
        this.shaReleased = status == 1;
        this.currentShaStatus = status;
        this.updatePowerInt(PowersKillerQueen.SHEER_HEART_ATTACK, status);
        S2CPacketUtil.sendIntPowerDataPacket((Player)this.getSelf(),PowersKillerQueen.SHEER_HEART_ATTACK, status);
    }

	public static ArrayList<String> blowableBlocksBlackList = Lists.newArrayList("minecraft:bedrock", "minecraft:obsidian");
	
	public boolean isBlockBlackListed(BlockState bs) {
		ResourceLocation rl = BuiltInRegistries.BLOCK.getKey(bs.getBlock());
		return (blowableBlocksBlackList != null && !blowableBlocksBlackList.isEmpty() && rl != null && blowableBlocksBlackList.contains(rl.toString()));
	}
	
	public Entity bombEntity = null;
	public BlockBombEntity bombBlock = null;
	public Entity bombBubble = null;
	public SheerHeartAttackEntity SHA = null;
	
	private boolean BitesTheDustMode = false;
	public boolean inBitesTheDustMode() {return this.BitesTheDustMode;}
	public boolean switchModes(){
    	this.BitesTheDustMode = !(this.BitesTheDustMode);

    	if (!this.isClient() && this.BitesTheDustMode) {
            this.defuseServer();
    	}
    	
        return true;
    }
	
	private boolean hasStrayCat = false;
	
	//public float standReach = 5;
	public boolean wentForCharge = false;
	public int chargedFinal;
	public boolean holdDownClick = false;
	
	public static int maxKickTime = 25;
	private static final int blockPlantMaxTicks = 5;
	private int ticksCount = -1;
    public int mobPlantTicks = 0;
    public int impaleTicks = 0;

    public static final float mobPlantRange = 3.5F;
    public static final float impaleRange = 3.5F;
    public static final float blockPlantRange = 3.5f;
    public static final float btdPlantRange = 3.0f;

    @Override
    public int getMaxGuardPoints(){ return 15; }

    @Override public float getPickMiningSpeed() { return 12F;}
    @Override public float getAxeMiningSpeed() { return 8F;}
    @Override public float getSwordMiningSpeed() { return 8F;}
    @Override public float getShovelMiningSpeed() {return 8F;}

    @Override public float getBarrageDamagePlayer(){ return 8; }
    @Override public float getBarrageDamageMob(){ return 18;}
    public float getImpaleKnockback(){
        return 1.3F;
    }

    public float getImpalePunchStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod(((float) ((float) 2F* (ClientNetworking.getAppropriateConfig().
                    killerQueenSettings.killerQueenAttackMultOnPlayers*0.01) * (ClientNetworking.getAppropriateConfig().
                    generalStandSettings.generalImpaleAttackMultiplier *0.01))));
        } else {
            return levelupDamageMod(((float) ((float) 12* (ClientNetworking.getAppropriateConfig().
                    killerQueenSettings.killerQueenAttackMultOnMobs*0.01) * (ClientNetworking.getAppropriateConfig().
                    generalStandSettings.generalImpaleAttackMultiplier *0.01))));
        }
    }

    @Override
    public float getBarrageHitStrength(Entity entity){
        float str = super.getBarrageHitStrength(entity);
        if (str > 0.005F) {
            if (getReducedDamage(entity)) {
                str *= levelupDamageMod((float) ((ClientNetworking.getAppropriateConfig().
                        killerQueenSettings.killerQueenAttackMultOnPlayers * 0.01)));
            } else {
                str *= levelupDamageMod((float) ((ClientNetworking.getAppropriateConfig().
                        killerQueenSettings.killerQueenAttackMultOnMobs * 0.01)));
            }
        }
        if (entity instanceof LivingEntity){
            if (str >= ((LivingEntity) entity).getHealth() && ClientNetworking.getAppropriateConfig().generalStandSettings.barragesOnlyKillOnLastHit){
                if (entity instanceof Player) { str = 0.00001F; }
                else { str = 0F; }
            }
        }
        return str;
    }
    public float getKickAttackKnockback(){ return (((float)this.chargedFinal/(float)maxKickTime)*1.3F); }
    public int getKickAttackKnockShieldTime(){ return 40; }

    public float getKickAttackStrength(Entity entity){
        float punchD = this.getPunchStrength(entity)*1.9F+this.getHeavyPunchStrength(entity);

        if (this.getReducedDamage(entity)){
            return (((float)this.chargedFinal/(float)maxKickTime)*punchD);
        } else {
            return (((float)this.chargedFinal/(float)maxKickTime)*punchD)+1;
        }
    }

    // Level stufff

    public int getImpaleLevel() {
        return 2;
    }


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
            setSkillIcon(context, x, y, 1, StandIcons.KILLER_QUEEN_PLANT_BOMB_ITEM, PowerIndex.SKILL_1_SNEAK);
        } else {
        	setSkillIcon(context, x, y, 1, StandIcons.KILLER_QUEEN_PLANT_BOMB_BLOCK, PowerIndex.SKILL_1);
        }
        
    	if (inBitesTheDustMode()) {
    		setSkillIcon(context, x, y, 2, StandIcons.KILLER_QUEEN_BTD_COMBAT, PowerIndex.SKILL_EXTRA_2);
    	} else if (this.currentBombStatus != BOMB_NONE) {
        	setSkillIcon(context, x, y, 2, StandIcons.KILLER_QUEEN_BOMB_DEFUSE, PowerIndex.NO_CD);
        } else if (isGuarding()) {
        	if (this.currentBombStatus == BOMB_BUBBLE) {
        		setSkillIcon(context, x, y, 2, StandIcons.KILLER_QUEEN_BUBBLE_REDIRECT, PowerIndex.NO_CD);
        	} else {
        		 setSkillIcon(context, x, y, 2, StandIcons.KILLER_QUEEN_BUBBLE_LAUNCH, PowerIndex.SKILL_2_GUARD);
        	}
    	} else if (isHoldingSneak()){
    		 setSkillIcon(context, x, y, 2, StandIcons.KILLER_QUEEN_IMPALE, PowerIndex.SKILL_2_SNEAK);
        } else {
        	setSkillIcon(context, x, y, 2, StandIcons.KILLER_QUEEN_PLANT_BOMB_MOB, PowerIndex.SKILL_2);
        }

        if (isHoldingSneak() && !(inBitesTheDustMode())){
        	if (this.currentShaStatus != SHA_NONE) {
                if (this.currentShaStatus == SHA_SEND) {
                    setSkillIcon(context, x, y, 3, StandIcons.KILLER_QUEEN_SHA_RETREAT, PowerIndex.NO_CD);
                }else {
                    setSkillIcon(context, x, y, 3, StandIcons.KILLER_QUEEN_SHA_RETREAT_CANCEL, PowerIndex.NO_CD);
                }
        	}else {
        		setSkillIcon(context, x, y, 3, StandIcons.KILLER_QUEEN_SHA_SUMMON, PowerIndex.SKILL_3);
        	}
        } else if (isGuarding() && !(inBitesTheDustMode())){
            if (this.currentShaStatus != SHA_NONE) {
                if (this.currentShaStatus == SHA_SEND) {
                    setSkillIcon(context, x, y, 3, StandIcons.KILLER_QUEEN_SHA_RETREAT, PowerIndex.NO_CD);
                }else {
                    setSkillIcon(context, x, y, 3, StandIcons.KILLER_QUEEN_SHA_RETREAT_CANCEL, PowerIndex.NO_CD);
                }
            }else {
                setSkillIcon(context, x, y, 3, StandIcons.KILLER_QUEEN_SHA_THROW, PowerIndex.SKILL_3);
            }
    	} else {

            if (canVault() ) {
                setSkillIcon(context, x, y, 3, StandIcons.KILLER_QUEEN_VAULT, PowerIndex.GLOBAL_DASH);
            } else if (canFallBrace()) {
                setSkillIcon(context, x, y, 3, StandIcons.KILLER_QUEEN_FALL_BRACE, PowerIndex.NONE);
            } else {
                setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
            }
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
    		}else if (this.currentBombStatus == BOMB_NONE && !isGuarding()) {
    			if (isHoldingSneak()) {
    				return true;
    			}else {
                    return !canBlockPlantBomb();
                }
    		}
    	}
    	if (slot == 2 && !this.BitesTheDustMode) {

            if (this.currentBombStatus == BOMB_NONE) {
                if(isGuarding()){
                    return !this.hasStrayCat;
                }
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
        	case SKILL_1_CROUCH -> {
        		// do item plant lol
        	}
        	case SKILL_1_GUARD, SKILL_1_CROUCH_GUARD -> {
        		this.tryBombConfig();
        	}
        	case SKILL_2_NORMAL -> {
        		if (!this.inBitesTheDustMode()) {
	        		if (this.currentBombStatus == BOMB_NONE) {
                        tryMobPlantBomb();
	        		}else {
	        			defuseClient();
	        		}
        		}
        	}
            case SKILL_2_CROUCH_GUARD, SKILL_2_GUARD -> {
                if (!this.inBitesTheDustMode()) {
                    if (this.currentBombStatus == BOMB_BUBBLE) {
                        // redirect
                    } else if (this.currentBombStatus == BOMB_NONE) {
                        // send bubble
                    } else {
                        defuseClient();
                    }
                }
            }
        	case SKILL_2_CROUCH -> {
        		if (!this.inBitesTheDustMode()) {
        			tryImpale();
        		}
        	}
        	case SKILL_3_NORMAL -> {
        		tryToDashClient();
        	}
            case SKILL_3_CROUCH -> {
                if (this.BitesTheDustMode) {
                    tryToDashClient();
                }else {
                    tryToSendOrReturnSHA(false);
                }
            }
            case SKILL_3_GUARD, SKILL_3_CROUCH_GUARD -> {
        		if (this.BitesTheDustMode) {
        			tryToDashClient();
        		}else {
        			tryToSendOrReturnSHA(true);
        		}
        	}
        	case SKILL_4_NORMAL, SKILL_4_CROUCH, SKILL_4_GUARD, SKILL_4_CROUCH_GUARD -> {
        		bitesTheDustModeToggleClient();
        	}
        }
    }
    // Can methods
    
    @Override
    public boolean canGuard(){
    	if (this.getActivePower() == DETONATE || this.getActivePower() == BITES_THE_DUST_COMBAT) {
    		return false;
    	}
        return super.canGuard();
    }
    
    @Override
    public boolean canAttack() {
    	if (this.getActivePower() == DETONATE || this.getActivePower() == BITES_THE_DUST_COMBAT) {
    		return false;
    	}
    	
    	return super.canAttack();
    }
    
    @Override
    public boolean canAttack2() {
    	if (this.getActivePower() == BITES_THE_DUST_COMBAT) {
    		return false;
    	}
    	
    	return super.canAttack2();
    }

    @Override
    public boolean canInterruptPower(DamageSource sauce, Entity interrupter){
        if (this.getActivePower() == PowerIndex.POWER_2) {
            int cdr = ClientNetworking.getAppropriateConfig().killerQueenSettings.mobPlantCooldown;
            if (this.getSelf() instanceof Player) {
                S2CPacketUtil.sendCooldownSyncPacket(((ServerPlayer) this.getSelf()), PowerIndex.SKILL_2, cdr);
            }
            this.setCooldown(PowerIndex.SKILL_2, cdr);
            return true;
        } else if (this.getActivePower() == PowerIndex.POWER_2_SNEAK) {
            int cdr = ClientNetworking.getAppropriateConfig().generalStandSettings.impaleAttackCooldown;
            if (this.getSelf() instanceof Player) {
                S2CPacketUtil.sendCooldownSyncPacket(((ServerPlayer) this.getSelf()), PowerIndex.SKILL_2_SNEAK, cdr);
            }
            this.setCooldown(PowerIndex.SKILL_2_SNEAK, cdr);
            return true;
        } else {
            return super.canInterruptPower(sauce,interrupter);
        }
    }

    public boolean canBlockPlantBomb() { 
    	StandEntity standEntity = ((StandUser) this.getSelf()).roundabout$getStand();
		
	    if (standEntity != null && standEntity.isAlive() && !standEntity.isRemoved() && this.currentBombStatus == BOMB_NONE) {
	    	
	    	Vec3 vec3d = this.getSelf().getEyePosition(0);
	        Vec3 vec3d2 = this.getSelf().getViewVector(0);
	        Vec3 vec3d3 = vec3d.add(vec3d2.x * blockPlantRange, vec3d2.y * blockPlantRange, vec3d2.z * blockPlantRange);
	        
	        BlockHitResult blockHit = this.getSelf().level().clip(new ClipContext(vec3d, vec3d3, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this.getSelf()));       
	        BlockPos pos = blockHit.getBlockPos();
	    	BlockState state = this.getSelf().level().getBlockState(pos);
	    	
	    	return (!isBlockBlackListed(state) && !state.isAir()); 
	    }
	    return false;
    }

    
    @Override
    public float inputSpeedModifiers(float basis){
        if (this.activePower == PowerIndex.POWER_2) {
            basis*=0.3f;
        } else if (this.getActivePower()==PowerIndex.POWER_2_SNEAK){
            if (this.getSelf().isCrouching()){
                float f = Mth.clamp(0.3F + EnchantmentHelper.getSneakingSpeedBonus(this.getSelf()), 0.0F, 1.0F);
                float g = 1/f;
                basis *= g;
            }
        } else if (this.activePower == PowerIndex.BARRAGE_CHARGE_2) {
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
            if (this.mobPlantTicks > 0 || this.impaleTicks > 0) {
                return;
            }

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
                    //if (!this.inBitesTheDustMode()){
                    if (!isHoldingSneak()) {
                        super.buttonInputAttack(keyIsDown, options);
                    } else {
                        if (this.canAttack()) {
                            this.tryPower(PowerIndex.SNEAK_ATTACK_CHARGE, true);
                            holdDownClick = true;
                            tryPowerPacket(PowerIndex.SNEAK_ATTACK_CHARGE);
                        } else {
                            super.buttonInputAttack(keyIsDown, options);
                        }
                    }
                    //}
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
        }else if (this.getActivePower() == PowerIndex.POWER_2) {
            mobPlantImpact(target);
        }else if (this.getActivePower() == PowerIndex.POWER_2_SNEAK) {
            impaleImpact(target);
        }

    	super.handleStandAttack(player,target);
    }
    

    public void standMobPlant(){
        /*By setting this to -10, there is a delay between the stand retracting*/

        if (this.self instanceof Player){
            if (isPacketPlayer()){
                this.setAttackTimeDuring(-20);
                this.mobPlantTicks = 15;
                tryIntToServerPacket(PacketDataIndex.INT_STAND_ATTACK,getTargetEntityId2(mobPlantRange));
            }
        } else {
            /*Caps how far out the punch goes*/
            Entity targetEntity = getTargetEntity(this.self,mobPlantRange);
            this.mobPlantImpact(targetEntity);
        }


    }

    public void standImpale(){
        /*By setting this to -10, there is a delay between the stand retracting*/

        if (this.self instanceof Player){
            if (isPacketPlayer()){
                this.setAttackTimeDuring(-20);
                this.mobPlantTicks = 15;
                tryIntToServerPacket(PacketDataIndex.INT_STAND_ATTACK,getTargetEntityId2(impaleRange));
            }
        } else {
            /*Caps how far out the punch goes*/
            Entity targetEntity = getTargetEntity(this.self,impaleRange);
            this.impaleImpact(targetEntity);
        }
    }


    public void impaleImpact(Entity entity) {
        if (activePower == PowerIndex.POWER_2_SNEAK){
            this.setAttackTimeDuring(-20);
            if (entity != null && entity.distanceTo(self) > impaleRange+0.75F) {
                entity = null;
            }
            if (entity != null) {
                hitParticlesCenter(entity);

                float pow;
                float knockbackStrength;
                pow = getImpalePunchStrength(entity);
                knockbackStrength = getImpaleKnockback();
                if (StandDamageEntityAttack(entity, pow, 0, this.self)) {
                    if (entity instanceof LivingEntity LE) {
                        addEXP(5, LE);
                        if (MainUtil.getMobBleed(entity)) {
                            if ((((TimeStop)this.getSelf().level()).CanTimeStopEntity(entity))) {
                                MainUtil.makeBleed(entity, 0, 200, this.getSelf());
                            } else {
                                MainUtil.makeBleed(entity, 2, 200, this.getSelf());
                            }
                            MainUtil.makeMobBleed(entity);
                        }
                    }
                    takeDeterminedKnockback(this.self, entity, knockbackStrength);
                } else {
                    knockShield2(entity, 100);
                }
            }

            if (this.getSelf() instanceof Player) {
                S2CPacketUtil.sendCooldownSyncPacket(((ServerPlayer) this.getSelf()), PowerIndex.SKILL_2_SNEAK, ClientNetworking.getAppropriateConfig().generalStandSettings.impaleAttackCooldown);
            }
            this.setCooldown(PowerIndex.SKILL_2_SNEAK, ClientNetworking.getAppropriateConfig().generalStandSettings.impaleAttackCooldown);
            SoundEvent SE;
            float pitch = 1F;
            if (entity != null) {
                //playImpaleConnectSoundExtra();
                SE = getImpaleSound();
                pitch = 1.2F;
            } else {
                SE = ModSounds.PUNCH_2_SOUND_EVENT;
            }

            if (!this.self.level().isClientSide()) {
                this.self.level().playSound(null, this.self.blockPosition(), SE, SoundSource.PLAYERS, 0.95F, pitch);
            }
        }
    }

    public SoundEvent getImpaleSound(){
        return ModSounds.IMPALE_HIT_EVENT;
    }

    public void mobPlantImpact(Entity entity) {
        if (activePower == PowerIndex.POWER_2){
            this.setAttackTimeDuring(-20);

            if (entity != null && entity.distanceTo(self) > mobPlantRange+0.75F) {
                entity = null;
            }
            if (entity instanceof StandEntity) {
                entity = null;
            }
            if (entity != null) {
                if (entity instanceof LivingEntity LE) {
                    addEXP(5, LE);
                }


                this.bombEntity = entity;
                this.currentBombStatus = BOMB_ENTITY;
                this.syncBombStatus(BOMB_ENTITY);
                int entID = entity.getId();

                S2CPacketUtil.sendIntPowerDataPacket((Player)this.getSelf(),PowersKillerQueen.ENTITY_BOMB, entID);
            }
            if (this.getSelf() instanceof Player) {
                S2CPacketUtil.sendCooldownSyncPacket(((ServerPlayer) this.getSelf()), PowerIndex.SKILL_2, ClientNetworking.getAppropriateConfig().killerQueenSettings.mobPlantCooldown);
            }
            this.setCooldown(PowerIndex.SKILL_2, ClientNetworking.getAppropriateConfig().killerQueenSettings.mobPlantCooldown);
        }
    }


    public void kickAttackImpact(Entity entity){
    	 this.setAttackTimeDuring(-20);
         if (entity != null) {
             if (chargedFinal < maxKickTime) {
            	 hitParticles(entity);
             }else {
                 hitParticlesCenter(entity);
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
    		return this.blockPlantBomb();
    	} else if (move == PowerIndex.POWER_4) {
    		return switchModes();
    	} else if (move == PowersKillerQueen.DEFUSE) {
    		return defuseServer();
    	} else if (move == PowerIndex.POWER_2) {
    		return mobPlantBomb();
        } else if (move == PowerIndex.POWER_2_SNEAK) {
            return impale();
    	} else if (move == PowerIndex.POWER_3) {
    		return this.sendOrReturnSHA(false);
        } else if (move == PowerIndex.POWER_3_BLOCK) {
            return this.sendOrReturnSHA(true);
    	} else if (move == PowersKillerQueen.DETONATE) {
    		return detonate();
    	} else if (move == PowerIndex.SNEAK_ATTACK_CHARGE){
            return this.setPowerKickWindup();
        } else if (move == PowerIndex.SNEAK_ATTACK){
            return this.setPowerKick();
        } else if (move == PowerIndex.EXTRA){
            return this.fallBraceInit();
        } else if (move == PowerIndex.FALL_BRACE_FINISH){
            return this.fallBrace();
        } else if (move == PowerIndex.VAULT){
            return this.vault();
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
        }else if (move == BOMB_CONFIG) {
            this.bombConfig = chargeTime;
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
                   this.bombEntity = null;
               }
            }
            case PowersKillerQueen.SHEER_HEART_ATTACK-> {
                this.shaReleased = data == 1;
                this.currentShaStatus = (byte)data;
            }
            case PowersKillerQueen.ENTITY_BOMB -> {
                this.bombEntity = this.getSelf().level().getEntity(data);
            }

        }
        super.updatePowerInt(activePower,data);
    }
    
   
    
    @Override
    public void updateUniqueMoves() {
    	if (this.getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE){
            updateKickAttackCharge();
        } else if (this.getActivePower() == PowerIndex.SNEAK_ATTACK){
            updateKickAttack();
        } else if (this.getActivePower() == PowerIndex.POWER_2) {
            updateMobPlant();
        } else if (this.getActivePower() == PowerIndex.POWER_2_SNEAK) {
            updateImpale();
        }

    	super.updateUniqueMoves();
    }

    public void updateMobPlant(){
        if (this.attackTimeDuring > -1) {
            if (this.attackTimeDuring > 68) {
                this.standMobPlant();
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
    public void updateImpale(){
        if (this.attackTimeDuring > -1) {
            if (this.attackTimeDuring > 24) {
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
    
    public void tryToSendOrReturnSHA(boolean shaThrow) {
        if (shaThrow) {
            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_3_BLOCK, true);
            tryPowerPacket(PowerIndex.POWER_3_BLOCK);
        }else{
            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_3, true);
            tryPowerPacket(PowerIndex.POWER_3);
        }
    }
    
    public void tryBombConfig() {
    	if (!this.BitesTheDustMode ) {
    		ClientUtil.openBombConfigScreen();
    	}
    }

    public void bombConfigPacket() {
        int status = ConfigManager.getClientConfig().dynamicSettings.KillerQueenCurrentBombConfig;
        this.bombConfig = status;
        this.tryIntPower(PowersKillerQueen.BOMB_CONFIG, true, status);
        tryIntPowerPacket(PowersKillerQueen.BOMB_CONFIG, status);

    }
    
    public void defuseClient() {
    	((StandUser) this.getSelf()).roundabout$tryPower(PowersKillerQueen.DEFUSE, true);
        tryPowerPacket(PowersKillerQueen.DEFUSE);
    	
        this.currentBombStatus = BOMB_NONE;

    }
    
    public void detonateClient() {
    	if (!this.onCooldown(PowerIndex.SKILL_1)) {
	    	if (currentBombStatus != BOMB_NONE && this.canAttack() && this.canAttack2()) {
	    		((StandUser) this.getSelf()).roundabout$tryPower(PowersKillerQueen.DETONATE, true);
	            tryPowerPacket(PowersKillerQueen.DETONATE);
	    	}
    	}
    }

    public void bitesTheDustModeToggleClient(){ 
    	((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_4, true);
        tryPowerPacket(PowerIndex.POWER_4);
    }

    public void tryImpale() {
        if (!this.onCooldown(PowerIndex.SKILL_2_SNEAK)) {
            if (this.activePower == PowerIndex.POWER_2_SNEAK) {
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                tryPowerPacket(PowerIndex.NONE);
            } else {
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_2_SNEAK, true);
                tryPowerPacket(PowerIndex.POWER_2_SNEAK);
            }
        }
    }

    public void tryBlockPlantBomb() {
    	if (!this.onCooldown(PowerIndex.SKILL_1) && this.canAttack2()) {
    		((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_1, true);
        	tryPowerPacket(PowerIndex.POWER_1);
    	}
    }

    public void tryMobPlantBomb() {
        if (!this.onCooldown(PowerIndex.SKILL_2) && this.canAttack2()) {


            if (this.activePower == PowerIndex.POWER_2) {
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                tryPowerPacket(PowerIndex.NONE);
            } else {
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_2, true);
                tryPowerPacket(PowerIndex.POWER_2);
            }
        }
    }

    // server side methods
    
    public boolean defuseServer() {
    	if (!this.isClient()) {
            if (currentBombStatus == BOMB_BLOCK) {
                this.bombBlock.discard();
                this.bombBlock = null;
            }else if (currentBombStatus == BOMB_ENTITY) {
                this.bombEntity = null;
            }
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
		        Vec3 vec3d3 = vec3d.add(vec3d2.x * blockPlantRange, vec3d2.y * blockPlantRange, vec3d2.z * blockPlantRange);
		        
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

    public boolean mobPlantBomb() {

        StandEntity stand = getStandEntity(this.self);
        if (Objects.nonNull(stand)){
            this.setAttackTimeDuring(0);
            this.setActivePower(PowerIndex.POWER_2);
            //playSoundsIfNearby(IMPALE_NOISE, 27, false);
            double rand = Math.random();
            if (rand > 0.5) {
                this.animateStand(KillerQueenEntity.MOB_PLANT);
            }else {
                this.animateStand(KillerQueenEntity.MOB_PLANT_2);
            }
            this.poseStand(OffsetIndex.ATTACK);

            return true;
        }
        return false;
    }

    public boolean impale() {

        StandEntity stand = getStandEntity(this.self);
        if (Objects.nonNull(stand)){
            this.setAttackTimeDuring(0);
            this.setActivePower(PowerIndex.POWER_2_SNEAK);
            playSoundsIfNearby(IMPALE_NOISE, 27, false);

            this.animateStand(KillerQueenEntity.IMPALE);

            this.poseStand(OffsetIndex.GUARD);

            return true;
        }
        return false;
    }

    public boolean itemPlantBomb() { 
    	                                 
    	return true;                     
    }                                 
    
    // Ticks things
    
    /**Punching stands only go for barrages when facing players, because barrages will be interrupted 100% of the time
     * otherwise.*/
    @Override
    public void tickMobAI(LivingEntity attackTarget){
        if (this.attackTimeDuring <= -1) {
            if (this.getSelf().fallDistance > 4 && !(this.self instanceof Blaze) && !(this.self instanceof FlyingMob) && !this.getSelf().isNoGravity()
                    && !(this.getSelf().noPhysics) && !(this.self instanceof EnderDragon) && !(this.self instanceof WitherBoss)) {
                /**Fall Brace AI*/
                ((StandUser) this.getSelf()).roundabout$summonStand(this.getSelf().level(),true,false);
                if (this.getSelf() instanceof Mob MB){
                    ((IMob)MB).roundabout$setRetractTicks(140);
                }
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.EXTRA, true);
                return;
            }
        }

        if (attackTarget != null && attackTarget.isAlive() && !this.isDazed(this.getSelf())) {
            boolean upAiNow = upAi(attackTarget);
            double dist = attackTarget.distanceTo(this.getSelf());
            boolean isCreeper = this.getSelf() instanceof Creeper;
            if (isCreeper) {
            } else {
                if ((this.getActivePower() != PowerIndex.NONE)
                        || dist <= 5){
                    rotateMobHead(attackTarget);
                }
                if (this.attackTimeDuring == -1 || (this.attackTimeDuring < -1 && this.activePower == PowerIndex.ATTACK)) {
                    Entity targetEntity = getTargetEntity(this.self, -1);
                    if (targetEntity != null && targetEntity.is(attackTarget)) {
                        double RNG = Math.random();
                        if (RNG < 0.3 && targetEntity instanceof Player && this.activePowerPhase <= 0 && !wentForCharge) {
                            wentForCharge = true;
                            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.BARRAGE_CHARGE, true);
                        }else if (this.activePowerPhase < this.activePowerPhaseMax || this.attackTime >= this.attackTimeMax) {
                            if ((RNG < 0.85 && (this.getSelf() instanceof Hoglin || this.getSelf() instanceof Ravager))) {
                                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.SNEAK_ATTACK_CHARGE, true);
                                wentForCharge = false;
                            } else {
                                if (!onCooldown(PowerIndex.SKILL_2_SNEAK) && RNG >= 0.85 && dist <= 3 && !wentForCharge) {
                                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_2_SNEAK, true);
                                    wentForCharge = true;
                                } else {
                                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.ATTACK, true);
                                    wentForCharge = false;
                                }
                            }
                        }
                    } else if ((this.getSelf() instanceof Piglin
                            || upAiNow
                            || this.getSelf() instanceof AbstractVillager) && dist <= 11 && dist >= 6) {
                        if (!onCooldown(PowerIndex.SKILL_3)) {
                            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_3_BLOCK, true);
                        }
                    } else if ((this.getSelf() instanceof Spider || this.getSelf() instanceof Slime
                            || this.getSelf() instanceof JotaroNPC || upAiNow
                            || this.getSelf() instanceof Rabbit || this.getSelf() instanceof AbstractVillager
                            || this.getSelf() instanceof Piglin || this.getSelf() instanceof Vindicator) &&
                            this.getSelf().onGround() && dist <= 19 && dist >= 5) {

                    } else if (this.currentBombStatus == BOMB_NONE) {
                        //double RNG = Math.random(); "&& RNG >= 0.90"
                        if (!onCooldown(PowerIndex.SKILL_2) && dist <= (mobPlantRange - 0.3) && !wentForCharge) {
                            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_2, true);
                        }
                    } else if (this.currentBombStatus != BOMB_BUBBLE) {
                        if (dist > 1.4 && !wentForCharge) {
                            ((StandUser) this.getSelf()).roundabout$tryPower(DETONATE, true);
                        }
                    }
                }
            }
        }

        /*
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
        }*/
    }
    
    @Override
    public void tickPower(){

        if (mobPlantTicks > 0){
            mobPlantTicks--;
        }
        if (impaleTicks > 0){
            impaleTicks--;
        }
        if (isClient()) {

        }else {
            if(Objects.nonNull(this.getStandEntity(this.self))) {
                byte currentAnim = this.getStandEntity(this.self).getAnimation();
                if ((currentAnim == KillerQueenEntity.DETONATE || currentAnim == KillerQueenEntity.BLOCK_PLANT) && this.ticksCount == 0) {
                    this.animateStand(StandEntity.IDLE);
                    this.poseStand(OffsetIndex.FOLLOW);
                }
            }

            if (this.currentBombStatus == BOMB_BLOCK) {
                if (this.bombBlock.blockGotDestroyed()) { this.defuseServer(); }
            }
            else if (this.currentBombStatus == BOMB_ENTITY) {
                if (this.bombEntity == null) { this.defuseServer(); }
                else if (!this.bombEntity.isAlive()) { this.defuseServer(); }
                else if (this.bombEntity instanceof LivingEntity LE) {
                    if (LE.isDeadOrDying()) {
                        this.defuseServer();
                    }
                }

            }
            if (this.getActivePower() == DETONATE && this.ticksCount == 0) {
                this.explode();
            }

            if (this.ticksCount >= 0) { this.ticksCount--;}

            if (this.SHA != null) {
                if ((this.SHA.shaIsNear() && this.SHA.getHaveToReturn()) || this.SHA.isRemoved() || this.inBitesTheDustMode()) {
                    this.SHA.discard();
                    this.syncShaStatus(SHA_NONE);
                    this.setCooldown(PowerIndex.SKILL_3, ClientNetworking.getAppropriateConfig().killerQueenSettings.sheerHeartAttackCooldown);
                }
            }else if (this.shaReleased){
                this.syncShaStatus(SHA_NONE);
            }

            if (this.bombConfig >= 2 && this.getActivePower() != DETONATE) {
                if (this.currentBombStatus == BOMB_BLOCK) {
                    if(Objects.nonNull(this.bombBlock)) {
                        Entity contact = this.bombBlock.detectContact();
                        if (contact != null) {
                            this.syncBombStatus(BOMB_CONTACT);
                            this.bombBlock.discard();
                            this.bombEntity = contact;
                            this.detonate();
                        }
                    }
                }else if (this.currentBombStatus == BOMB_ENTITY) {
                    if(Objects.nonNull(this.bombEntity)) {
                        Entity contact = this.isBombEntityContacting();
                        if (contact != null) {
                            //this.syncBombStatus(BOMB_CONTACT);
                            this.bombEntity = contact;
                            this.detonate();
                        }
                    }
                }
            }
        }

        super.tickPower();
    }

    public Entity isBombEntityContacting() {
        if (this.bombEntity instanceof LivingEntity LE) {
            LivingEntity lastAttacker = LE.getLastAttacker();
            int lastTime = LE.getLastHurtByMobTimestamp();
            Roundabout.LOGGER.info("Last damage taken: " + lastTime);
        }
        float margin = 0.2f;

        float hRad = this.bombEntity.getBbHeight() / 2.0f + margin;
        float wRad = this.bombEntity.getBbWidth() / 2.0f + margin;
        Level level = this.bombEntity.level();

        List<Entity> contact = MainUtil.genHitbox(level, this.bombEntity.getX(), this.bombEntity.getY() + hRad, this.bombEntity.getZ(),
                wRad, hRad, wRad);

        double distRecord = -1.0;
        Entity blowTarget = null;

        for (Entity entity : contact) {

            if (entity == this.bombEntity || entity instanceof StandEntity || !(entity instanceof LivingEntity) ||
                    entity.equals(this.getStandUserSelf()) || entity.equals(((StandUser)this.getStandUserSelf()).roundabout$getStand())) {
                continue;
            }
            double dist = MainUtil.cheapDistanceTo(
                    this.bombEntity.getX(),
                    this.bombEntity.getY(),
                    this.bombEntity.getZ(),
                    entity.getX(),
                    entity.getY(),
                    entity.getZ()
            );

            if (distRecord == -1 || dist < distRecord) {
                blowTarget = entity;
                distRecord = dist;
            }
        }

        return blowTarget;
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
       }else if (soundChoice == IMPALE_NOISE) {
           return ModSounds.IMPALE_CHARGE_EVENT;
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

    // hightlights entity things :0
    public boolean highlightsEntity(Entity ent,Player player){
        if(this.currentBombStatus == BOMB_ENTITY) {
            if (this.bombEntity != null) {
                return this.getSelf().hasLineOfSight(ent) && ent == this.bombEntity;
            }
        }
        return false;
    }

    @Override
    public int highlightsEntityColor(Entity ent, Player player){
        if(this.currentBombStatus == BOMB_ENTITY) {
            if (this.bombEntity != null && ent == this.bombEntity) {
                if (this.getSelf().hasLineOfSight(ent) && ent == this.bombEntity) {
                    return 16150472;
                }
            }
        }
        return super.highlightsEntityColor(ent, player);
    }

    // more GUI things


    @Override
    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypas){
        List<AbilityIconInstance> $$1 = Lists.newArrayList();

        $$1.add(drawSingleGUIIcon(context,18,leftPos+20,topPos+80,0, "ability.roundabout.punch",
                "instruction.roundabout.press_attack", StandIcons.KILLER_QUEEN_PUNCH,0,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+20, topPos+99,0, "ability.roundabout.guard",
                "instruction.roundabout.hold_block", StandIcons.KILLER_QUEEN_GUARD,0,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+20,topPos+118,0, "ability.roundabout.kq_fast_kick",
                "instruction.roundabout.hold_attack_crouch", StandIcons.KILLER_QUEEN_FAST_KICK,0,level,bypas));

        $$1.add(drawSingleGUIIcon(context,18,leftPos+39,topPos+80,0, "ability.roundabout.barrage",
                "instruction.roundabout.barrage", StandIcons.KILLER_QUEEN_BARRAGE,0,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+39, topPos+99,0, "ability.roundabout.kq_guard_bubble",
                "instruction.roundabout.hold_block", StandIcons.KILLER_QUEEN_GUARD_BUBBLES,0,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+39,topPos+118,0, "ability.roundabout.kq_bomb_block",
                "instruction.roundabout.press_skill", StandIcons.KILLER_QUEEN_PLANT_BOMB_BLOCK,1,level,bypas));

        $$1.add(drawSingleGUIIcon(context,18,leftPos+58,topPos+80, 0, "ability.roundabout.kq_btd_day",
                "instruction.roundabout.press_skill_btd_mode", StandIcons.KILLER_QUEEN_BTD_DAY,1,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+58,topPos+99, 0, "ability.roundabout.kq_bomb_item",
                "instruction.roundabout.press_skill_crouch", StandIcons.KILLER_QUEEN_PLANT_BOMB_ITEM,1,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+58,topPos+118, 0, "ability.roundabout.kq_config",
                "instruction.roundabout.press_skill_block", StandIcons.KILLER_QUEEN_BOMB_SETIINGS,1,level,bypas));

        $$1.add(drawSingleGUIIcon(context,18,leftPos+77,topPos+80,0, "ability.roundabout.kq_bomb_mob",
                "instruction.roundabout.press_skill", StandIcons.KILLER_QUEEN_PLANT_BOMB_MOB,2,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+77,topPos+99,0, "ability.roundabout.impale",
                "instruction.roundabout.press_skill_crouch", StandIcons.KILLER_QUEEN_IMPALE,2,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+77,topPos+118,0, "ability.roundabout.kq_bomb_bubble",
                "instruction.roundabout.press_skill_block", StandIcons.KILLER_QUEEN_BUBBLE_LAUNCH,2,level,bypas));

        $$1.add(drawSingleGUIIcon(context,18,leftPos+96,topPos+80, 0, "ability.roundabout.kq_btd_combat",
                "instruction.roundabout.press_skill_shooting_mode", StandIcons.KILLER_QUEEN_BTD_COMBAT,2,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+96,topPos+99,0, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE,3,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+96,topPos+118,0, "ability.roundabout.fall_brace",
                "instruction.roundabout.press_skill_falling", StandIcons.KILLER_QUEEN_FALL_BRACE,3,level,bypas));

        $$1.add(drawSingleGUIIcon(context,18,leftPos+115,topPos+80,0, "ability.roundabout.vault",
                "instruction.roundabout.press_skill_air", StandIcons.KILLER_QUEEN_VAULT,3,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+115,topPos+99, 0, "ability.roundabout.kq_sha_summon",
                "instruction.roundabout.press_skill_crouch", StandIcons.KILLER_QUEEN_SHA_SUMMON,3,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+115,topPos+118,0, "ability.roundabout.kq_sha_throw",
                "instruction.roundabout.press_skill_block", StandIcons.KILLER_QUEEN_SHA_THROW,3,level,bypas));

        $$1.add(drawSingleGUIIcon(context,18,leftPos+134,topPos+80, 0, "ability.roundabout.kq_btd_mode",
                "instruction.roundabout.press_skill", StandIcons.KILLER_QUEEN_BTD_ACTIVATE,4,level,bypas));

        return $$1;
    }

    @Override
    public void renderAttackHud(GuiGraphics context, Player playerEntity,
                                int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                float flashAlpha, float otherFlashAlpha) {

        StandUser standUser = ((StandUser) playerEntity);
        StandPowers powers = standUser.roundabout$getStandPowers();
        boolean standOn = PowerTypes.hasStandActive(playerEntity);;
        int j = scaledHeight / 2 - 7 - 4;
        int k = scaledWidth / 2 - 8;
        if (standOn && this.getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE) {
            int ClashTime = Math.min(15, Math.round(((float) attackTimeDuring / maxKickTime) * 15));
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 30, ClashTime, 6);
        } else if (this.getActivePower() == PowerIndex.POWER_2_SNEAK){
            Entity TE = this.getTargetEntity(playerEntity, impaleRange);
            if (TE != null) {
                context.blit(StandIcons.JOJO_ICONS, k, j, 193, 0, 15, 6);
            }
        } else if (this.getActivePower() == PowerIndex.POWER_2){
            Entity TE = this.getTargetEntity(playerEntity, mobPlantRange);
            if (TE != null) {
                context.blit(StandIcons.JOJO_ICONS, k, j, 193, 0, 15, 6);
            }
        }  else {
        	super.renderAttackHud(context, playerEntity,
                    scaledWidth, scaledHeight, ticks, vehicleHeartCount,
                    flashAlpha, otherFlashAlpha);
        }
    	
    	
        
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
            case KillerQueenEntity.MEMENTO -> {return Component.translatable("skins.roundabout.killer_queen.memento");}
            case KillerQueenEntity.STARDUST -> {return Component.translatable("skins.roundabout.killer_queen.stardust");}
        }
        return Component.translatable("skins.roundabout.killer_queen.anime");
    }

    public boolean explode() {


        if (!this.isClient()) {

            BlockPos bPos = BlockPos.ZERO;
            Vec3 vPos = Vec3.ZERO;
            Level level = this.getSelf().level();
            byte bStatus = this.currentBombStatus;

            boolean canDestroyBlocks = ((this.bombConfig % 2) == 1) &&
                    ClientNetworking.getAppropriateConfig().killerQueenSettings.blocksDestruction &&
                    level.getGameRules().getBoolean(ModGamerules.ROUNDABOUT_STAND_GRIEFING) &&
                    this.getSelf() instanceof Player;

            Entity target = null;

            if (bStatus == PowersKillerQueen.BOMB_BLOCK) {
                bPos = this.bombBlock.getBlockPos();
                vPos = bPos.getCenter();
                level = this.bombBlock.level();

                this.bombBlock.discard();
                this.bombBlock = null;
            } else if (bStatus == PowersKillerQueen.BOMB_ENTITY || bStatus == PowersKillerQueen.BOMB_CONTACT) {
                vPos = this.bombEntity.position();
                bPos = new BlockPos(this.bombEntity.getBlockX(), this.bombEntity.getBlockY(), this.bombEntity.getBlockZ());
                level = this.bombEntity.level();
                target = this.bombEntity;

                this.bombEntity = null;
            }

            if (canDestroyBlocks) {
                ExplosionUtil.explodeBlocks(bPos, level, 1.0f);
            }

            DamageSource dmg = ModDamageTypes.of(level, DamageTypes.PLAYER_EXPLOSION, this.getSelf());
            DamageSource sneakyDmg = ModDamageTypes.of(level, DamageTypes.EXPLOSION, null);
            ExplosionUtil.explosionHurtSneaky(vPos, dmg, level,
                    ClientNetworking.getAppropriateConfig().killerQueenSettings.explosionDetonateMaxDamage, 0.4f, 1.5f);

            if (target != null && bStatus == BOMB_ENTITY) {
                float hitPoints = ClientNetworking.getAppropriateConfig().killerQueenSettings.mobPlantDesintegrationDamage;

                boolean playersHitkill = ClientNetworking.getAppropriateConfig().killerQueenSettings.mobPlantHitkillPlayers;
                boolean mobsHitkill = ClientNetworking.getAppropriateConfig().killerQueenSettings.mobPlantHitkillMobs;

                boolean isBoss = MainUtil.isBossMob(target);

                if (!target.getControllingPassenger().hasLineOfSight(this.getSelf()) && !isBoss) {
                    dmg = sneakyDmg;
                }

                if (target instanceof Player pl) {
                    if (!pl.isCreative() && playersHitkill) { pl.die(dmg); }
                    else {
                        target.hurt(dmg, hitPoints
                            * (ClientNetworking.getAppropriateConfig().killerQueenSettings.killerQueenAttackMultOnPlayers/100.0f)
                        );
                    }
                } else {
                    if (mobsHitkill && !isBoss) { target.kill();}
                    else if (isBoss) {
                        target.hurt(dmg, hitPoints * 0.70f
                            * (ClientNetworking.getAppropriateConfig().killerQueenSettings.killerQueenAttackMultOnMobs / 100f)
                        );
                    } else {
                        target.hurt(dmg, hitPoints
                            * (ClientNetworking.getAppropriateConfig().killerQueenSettings.killerQueenAttackMultOnMobs / 100f)
                        );
                    }
                }

                if (target instanceof LivingEntity LE) {
                    if (LE.isDeadOrDying()) {
                        ItemStack stack = null;
                        byte type = -1;

                        if (LE instanceof Player pl) {
                            if (((IFatePlayer)pl).rdbt$getFatePowers() instanceof ZombieFate zp) {
                                type = 3;
                            } else {
                                type = 0;
                            }
                        }else if (LE instanceof JojoNPC){
                            type = 0;
                        } else if (LE instanceof AbstractIllager) {
                            type = 1;
                        } else if (LE instanceof AbstractVillager) {
                            type = 2;
                        } else {
                            if ((LE instanceof Zombie)) {
                                type = 3;
                            }
                        }
                        if (type != -1) {
                            switch (type) {
                                case 0 -> {
                                    stack = ModItems.HAND.getDefaultInstance().copy();
                                }
                                case 1 -> {
                                    stack = ModItems.ILLAGER_HAND.getDefaultInstance().copy();
                                }
                                case 2 -> {
                                    stack = ModItems.VILLAGER_HAND.getDefaultInstance().copy();
                                }
                                case 3 -> {
                                    stack = ModItems.ROTTEN_HAND.getDefaultInstance().copy();
                                }
                            }

                            if (LE instanceof Player PL) {
                                CompoundTag nbtData = new CompoundTag();
                                nbtData.putString(PlayerHandItem.TAG_HAND_OWNER, PL.getName().getString());
                                stack.setTag(nbtData);
                            }

                            ItemEntity drop = new ItemEntity(LE.level(),
                                    LE.getX(), LE.getY(), LE.getZ(),
                                    stack);
                            LE.level().addFreshEntity(drop);
                        }
                    }
                }
            }

            ExplosionUtil.explodeEffects(vPos, level, ModParticles.KILLER_QUEEN_EXPLOSION, 0.6f);

            explosionSFX(vPos, 10);

        }
		this.syncBombStatus(BOMB_NONE);
		this.setPowerNone();
		
    	return true;
    }
    
    public boolean detonate() {
    	if (!this.isClient() && this.getActivePower() == PowerIndex.NONE) {
    		 if (this.currentBombStatus == BOMB_BLOCK) {
            	this.setCooldown(PowerIndex.SKILL_1, ClientNetworking.getAppropriateConfig().killerQueenSettings.blockPlantCooldown);
            }
    		
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
    
    public boolean sendOrReturnSHA(boolean shaThrow) {
        if (shaThrow) {
            this.animateStand(KillerQueenEntity.FIRST_PUNCH);
        }else{
            this.animateStand(KillerQueenEntity.SHA_SEND);
        }

        this.poseStand(OffsetIndex.GUARD);
        this.setAttackTimeDuring(-15);
    	this.setActivePower(PowerIndex.POWER_3);

        if (!this.getSelf().level().isClientSide()) {
            if (SHA == null || SHA.isRemoved()){
            	SheerHeartAttackEntity sha = ModEntities.SHEER_HEART_ATTACK.create(this.getSelf().level());
            	if (sha != null) {

                    sha.setUser(this.self);
                    sha.setXRot(this.self.getXRot());
                    sha.setYRot(this.self.getYRot());
                    if (shaThrow) {
                        sha.setPos(this.self.getEyePosition());
                    } else {
                        sha.setPos(getRayBlock(this.self, 0.5f).add(0, -0.3, 0));
                    }
            		this.self.level().addFreshEntity(sha);
            		
            		SHA = sha;

                    if (shaThrow) {
                        SHA.shoot(getRayBlock(this.self,4f));
                    }

                    this.syncShaStatus(SHA_SEND);
            	}
            	
            } else {
                SHA.setHaveToReturn(!SHA.getHaveToReturn());
                this.syncShaStatus(SHA_RETREAT);
            }
        }
    	
    	
    	return true;
    }
   
 }
