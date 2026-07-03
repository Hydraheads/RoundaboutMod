package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.access.IMob;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.KeyboardPilotInput;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.projectile.SoftAndWetBubbleEntity;
import net.hydra.jojomod.entity.projectile.SoftAndWetPlunderBubbleEntity;
import net.hydra.jojomod.entity.projectile.StrayCatAirBubble;
import net.hydra.jojomod.entity.projectile.ThrownObjectEntity;
import net.hydra.jojomod.entity.stand.KillerQueenEntity;
import net.hydra.jojomod.entity.stand.ManhattanTransferEntity;
import net.hydra.jojomod.entity.stand.StarPlatinumEntity;
import net.hydra.jojomod.entity.substand.SheerHeartAttackEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.visages.mobs.JotaroNPC;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.ModGamerules;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewPunchingStand;
import net.hydra.jojomod.entity.substand.BlockBombEntity;
import net.hydra.jojomod.util.config.Config;
import net.hydra.jojomod.util.config.ConfigManager;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.ExplosionUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.CameraType;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PowersKillerQueen extends NewPunchingStand {
	public PowersKillerQueen(LivingEntity self) {super(self);}

    @Override public boolean isStandEnabled(){ return ClientNetworking.getAppropriateConfig().killerQueenSettings.enableKillerQueen; }
    @Override public boolean isWip(){return true;}
    @Override public Component ifWipListDevStatus(){ return Component.translatable(  "roundabout.dev_status.active").withStyle(ChatFormatting.AQUA);}
    @Override public Component ifWipListDev(){ return Component.literal("DOGael Arts").withStyle(ChatFormatting.YELLOW);}
    @Override public StandPowers generateStandPowers(LivingEntity entity){ return new PowersKillerQueen(entity);}
    @Override public StandEntity getNewStandEntity(){ return ModEntities.KILLER_QUEEN.create(this.getSelf().level());}

	// TODO Air bubble redirect
	// TODO Make bomb item
	// TODO Bites The Dust
	
	// TODO Audio Translations

    // TODO sheer heart attack walk on wall
	
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
        BITES_THE_DUST_PLANT = 62,
        STRAY_CAT_ADD = 63,
        BUBBLE_BOMB = 63,
        IMPALE_NOISE = 105,

    // Bomb Status things
		BOMB_NONE=0,
		BOMB_BLOCK=1,
		BOMB_ITEM=2,
		BOMB_ENTITY=3,
		BOMB_BUBBLE=4,
        BLOCK_CONTACT = 5,
        BUBBLE_CONTACT = 6,

    // Sheer Heart Attack Status things
        SHA_NONE = 0,
        SHA_SEND = 1,
        SHA_RETREAT = 3;


	
	private byte currentBombStatus = BOMB_NONE;
    private byte currentShaStatus = SHA_NONE;
    private int bombConfig = 2;
	
	 public void syncBombStatus(byte status) {

         this.currentBombStatus = status;
         this.updatePowerInt(PowersKillerQueen.PLANTED, status);
         if (this.getSelf() instanceof Player) {
             S2CPacketUtil.sendIntPowerDataPacket((Player) this.getSelf(), PowersKillerQueen.PLANTED, status);
         }
    }

    public void syncShaStatus(byte status) {
        this.currentShaStatus = status;
        this.updatePowerInt(PowersKillerQueen.SHEER_HEART_ATTACK, status);
        if (this.getSelf() instanceof Player) {
            S2CPacketUtil.sendIntPowerDataPacket((Player) this.getSelf(), PowersKillerQueen.SHEER_HEART_ATTACK, status);
        }
    }

	public Entity bombEntity = null;
    private int bombEntityID = -1;

    public Entity getBombEntity(){
        if (this.bombEntity != null && !this.bombEntity.isRemoved()) {
            return this.bombEntity;
        }else {
            this.bombEntity = this.self.level().getEntity(this.bombEntityID);
        }
        return this.bombEntity;
    }

	public BlockBombEntity bombBlock = null;
    public ItemStack bombItemStack = null;
	public StrayCatAirBubble bombBubble = null;
    public int bombBubbleID = -1;
	public SheerHeartAttackEntity SHA = null;

    public int plantInventorySlot=1;

    @Override
    public void onPowerSwitch(){
        this.bombEntity = null;
        if (this.bombBlock != null) {
            this.bombBlock.discard();
            this.bombBlock = null;
        }
        if (this.SHA != null) {
            this.SHA.discard();
            this.SHA = null;
        }
        if (this.bombBubble != null) {
            this.bombBubble.discard();
        }

        super.onPowerSwitch();
    }

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

    public boolean canUseStrayCat() {

        return this.hasStrayCat || (this.getSelf() instanceof Player pl && ((!((StandUser) pl).roundabout$getStandDisc().isEmpty() &&
                ((StandUser) pl).roundabout$getStandDisc().getItem() instanceof MaxStandDiscItem) ||
                pl.isCreative()));
    }


    private boolean hasBitesTheDust = false;
    public boolean canBitesTheDust() {

        return this.hasBitesTheDust || (this.getSelf() instanceof Player pl && ((!((StandUser) pl).roundabout$getStandDisc().isEmpty() &&
                ((StandUser) pl).roundabout$getStandDisc().getItem() instanceof MaxStandDiscItem) ||
                pl.isCreative()));
    }

	public boolean wentForCharge = false;
	public int chargedFinal;
	public boolean holdDownClick = false;
	
	public static int maxKickTime = 25;
	private static final int blockPlantMaxTicks = 5;
    public int mobPlantTicks = 0;
    public int impaleTicks = 0;

    public static final float mobPlantRange = 3.5F;
    public static final float impaleRange = 3.5F;
    public static final float blockPlantRange = 3.5f;
    public static final float btdPlantRange = 3.0f;

    public int getMobPlantWindup() {
        return ClientNetworking.getAppropriateConfig().killerQueenSettings.mobPlantWindup;
    }

    public float getThrowAngle(){ return -0.5F;}
    public float getThrowAngle2(){
        return 0.8F;
    }
    public float getThrowAngle3(){
        return -3.0F;
    }

    @Override
    public int getMaxGuardPoints(){ return 15; }

    @Override public float getPickMiningSpeed() { return 12F;}
    @Override public float getAxeMiningSpeed() { return 8F;}
    @Override public float getSwordMiningSpeed() { return 8F;}
    @Override public float getShovelMiningSpeed() {return 8F;}

    public float getStrayCatAirBubbleSpeed() { return 0.2f;}

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

    public float getAirBubbleDamage(Entity entity){
        float damage = ClientNetworking.getAppropriateConfig().killerQueenSettings.StrayCatAirBubblesDamage;
        if (this.getReducedDamage(entity)){
            return levelupDamageMod(((float) ((float) damage * (ClientNetworking.getAppropriateConfig().
                    killerQueenSettings.killerQueenAttackMultOnPlayers*0.01))));
        } else {
            return levelupDamageMod(((float) ((float) damage * (ClientNetworking.getAppropriateConfig().
                    killerQueenSettings.killerQueenAttackMultOnMobs*0.01))));
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

    static final String strayCatTag = "hasStrayCat";
    static final String BitesTheDustTag = "hasBTD";

    @Override
    public void addAdditionalSaveData(CompoundTag $$0) {
        $$0.putBoolean(strayCatTag, this.hasStrayCat);
        $$0.putBoolean(BitesTheDustTag, this.hasBitesTheDust);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag $$0) {
        if ($$0.contains(strayCatTag)) {
            this.hasStrayCat = $$0.getBoolean(strayCatTag);
        }
        if ($$0.contains(BitesTheDustTag)) {
            this.hasBitesTheDust = $$0.getBoolean(BitesTheDustTag);
        }
    }

    // Level stufff
    @Override public byte getMaxLevel(){ return 7; }

    public int getImpaleLevel() { return 2;}
    public int getSheerHeartAttackLevel() { return 3; }
    public int getStrayCatLevel() { return 5; }
    public int getBitesTheDustLevel() { return 7; }

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
        List<Byte> l = Lists.newArrayList();
        l.add(PART_4);
        if (this.getSelf() instanceof Player PE) {
            byte Level = ((IPlayerEntity) PE).roundabout$getStandLevel();
            ItemStack goldDisc = ((StandUser) PE).roundabout$getStandDisc();
            boolean bypass = PE.isCreative() || (!goldDisc.isEmpty() && goldDisc.getItem() instanceof MaxStandDiscItem);
            if (Level > 1 || bypass){
                l.add(MANGA);
                l.add(GOGO);
                l.add(JOJOLION);
            }
            if (Level > 2 || bypass){
                l.add(ARTWORK);
                l.add(FINAL);
                l.add(YELLOW);
            }
            if (Level > 3 || bypass){
                l.add(CRACKED);
                l.add(DEADLY);
                l.add(CREEPER);
                l.add(TAMA);
            }
            if (Level > 4 || bypass){
                l.add(UMBRA);
                l.add(NIGHTMARE);
                l.add(LIMBUSMORTIS);
            }
            if (Level > 5 || bypass){
                l.add(MEMENTO);
                l.add(GUNPOWDER);
                l.add(MINESWEEPER);
            }
            if (Level > 6 || bypass){
                l.add(NOTW);
                l.add(STARDUST);
            }
             if (((IPlayerEntity)PE).roundabout$getUnlockedBonusSkin() || bypass){
                l.add(STRAY);
            }
        }

        return l;
    }

    @Override
    public void rollSkin(){
        StandUser user = getUserData(this.self);
        if (this.self instanceof Creeper) {
            user.roundabout$setStandSkin(KillerQueenEntity.CREEPER);
        } else if (this.self instanceof WanderingTrader) {
            user.roundabout$setStandSkin(KillerQueenEntity.GUNPOWDER);
        }else if (this.self instanceof AbstractVillager) {
            user.roundabout$setStandSkin(KillerQueenEntity.JOJOLION);
        }else if (this.self instanceof EnderMan || this.self instanceof EnderDragon) {
            user.roundabout$setStandSkin(KillerQueenEntity.UMBRA);
        }else if (this.self instanceof Raider) {
            user.roundabout$setStandSkin(KillerQueenEntity.DEADLY);
        }else if (this.self instanceof Slime) {
            user.roundabout$setStandSkin(KillerQueenEntity.GOGO);
        } else if (this.self instanceof Piglin) {
            user.roundabout$setStandSkin(KillerQueenEntity.FINAL);
        } else if (this.self instanceof Rabbit) {
            user.roundabout$setStandSkin(KillerQueenEntity.ARTWORK);
        } else if (this.self instanceof IronGolem) {
            user.roundabout$setStandSkin(KillerQueenEntity.LIMBUSMORTIS);
        } else if (this.self instanceof Warden) {
            user.roundabout$setStandSkin(KillerQueenEntity.STRAY);
        } else if (this.self instanceof Cat) {
            user.roundabout$setStandSkin(KillerQueenEntity.TAMA);
        }
    }

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {

    	if (inBitesTheDustMode()) {
            setSkillIcon(context, x, y, 1, StandIcons.KILLER_QUEEN_BTD_DAY, PowerIndex.SKILL_EXTRA);
        } else if (canAddStrayCatto()) {
            setSkillIcon(context, x, y, 1, StandIcons.KILLER_QUEEN_ADD_STRAY_CAT, PowerIndex.NO_CD);
    	} else if (isGuarding() && !this.isPiloting()) {
            setSkillIcon(context, x, y, 1, StandIcons.KILLER_QUEEN_BOMB_SETIINGS, PowerIndex.NO_CD);
        } else if (this.currentBombStatus != BOMB_NONE || this.isPiloting()) {
    		setSkillIcon(context, x, y, 1, StandIcons.KILLER_QUEEN_BOMB_DETONATE, PowerIndex.NO_CD);
    	} else if (isHoldingSneak()){
            setSkillIcon(context, x, y, 1, StandIcons.KILLER_QUEEN_PLANT_BOMB_ITEM, PowerIndex.SKILL_1_SNEAK);
        } else {
        	setSkillIcon(context, x, y, 1, StandIcons.KILLER_QUEEN_PLANT_BOMB_BLOCK, PowerIndex.SKILL_1);
        }
        
    	if (inBitesTheDustMode()) {
    		setSkillIcon(context, x, y, 2, StandIcons.KILLER_QUEEN_BTD_COMBAT, PowerIndex.SKILL_EXTRA_2);
        } else if (this.currentBombStatus == BOMB_BUBBLE && isGuarding() || this.isPiloting()) {
            setSkillIcon(context, x, y, 2, StandIcons.KILLER_QUEEN_BUBBLE_REDIRECT, PowerIndex.NO_CD);

        } else if (this.currentBombStatus != BOMB_NONE) {
        	setSkillIcon(context, x, y, 2, StandIcons.KILLER_QUEEN_BOMB_DEFUSE, PowerIndex.NO_CD);
        } else if (isGuarding()) {
            if (!canExecuteMoveWithLevel(getStrayCatLevel()) || !this.canUseStrayCat()) {
                setSkillIcon(context, x, y, 2, StandIcons.LOCKED, PowerIndex.NO_CD,true);
            } else {
        		 setSkillIcon(context, x, y, 2, StandIcons.KILLER_QUEEN_BUBBLE_LAUNCH, PowerIndex.SKILL_2_GUARD);
        	}
    	} else if (isHoldingSneak()){
            if (canExecuteMoveWithLevel(getImpaleLevel())) {
                setSkillIcon(context, x, y, 2, StandIcons.KILLER_QUEEN_IMPALE, PowerIndex.SKILL_2_SNEAK);
            } else {
                setSkillIcon(context, x, y, 2, StandIcons.LOCKED, PowerIndex.NO_CD,true);
            }
        } else {
        	setSkillIcon(context, x, y, 2, StandIcons.KILLER_QUEEN_PLANT_BOMB_MOB, PowerIndex.SKILL_2);
        }

        if (isHoldingSneak() && !(inBitesTheDustMode())){
            if (!canExecuteMoveWithLevel(getSheerHeartAttackLevel())) {
                setSkillIcon(context, x, y, 3, StandIcons.LOCKED, PowerIndex.NO_CD,true);
            } else if (this.currentShaStatus != SHA_NONE) {
                if (this.currentShaStatus == SHA_SEND) {
                    setSkillIcon(context, x, y, 3, StandIcons.KILLER_QUEEN_SHA_RETREAT, PowerIndex.NO_CD);
                }else {
                    setSkillIcon(context, x, y, 3, StandIcons.KILLER_QUEEN_SHA_RETREAT_CANCEL, PowerIndex.NO_CD);
                }
        	}else {
        		setSkillIcon(context, x, y, 3, StandIcons.KILLER_QUEEN_SHA_SUMMON, PowerIndex.SKILL_3);
        	}
        } else if (isGuarding() && !(inBitesTheDustMode())){
            if (!canExecuteMoveWithLevel(getSheerHeartAttackLevel())) {
                setSkillIcon(context, x, y, 3, StandIcons.LOCKED, PowerIndex.NO_CD,true);
            } else if (this.currentShaStatus != SHA_NONE) {
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

        if (!canExecuteMoveWithLevel(getBitesTheDustLevel())) {
            setSkillIcon(context, x, y, 4, StandIcons.LOCKED, PowerIndex.NO_CD,true);
        } else if (inBitesTheDustMode()) {
        	setSkillIcon(context, x, y, 4, StandIcons.KILLER_QUEEN_BTD_DEACTIVATE, PowerIndex.SKILL_4);
        }else {
        	setSkillIcon(context, x, y, 4, StandIcons.KILLER_QUEEN_BTD_ACTIVATE, PowerIndex.SKILL_4);
        }
    }
    
    @Override
    public boolean isAttackIneptVisually(byte activeP, int slot) {
    	if (slot == 1) {
            if (this.isPiloting()) {
                return false;
            } else if(inBitesTheDustMode()) {
    			return (!ClientNetworking.getAppropriateConfig().killerQueenSettings.enableBitesTheDustDayMode);
    		}else if (this.currentBombStatus == BOMB_NONE && !isGuarding() && !canAddStrayCatto()) {
    			if (isHoldingSneak()) {
    				return !canItemPlantBomb();
    			}else {
                    return !canBlockPlantBomb();
                }
    		}
    	}
    	if (slot == 2 && this.currentBombStatus == BOMB_NONE && !this.BitesTheDustMode && isGuarding()){
            if (this.isPiloting()) {
                return false;
            }
            return !canUseStrayCat();
    	}

        if (slot == 3) {
            return this.isPiloting();
        }

        if (slot == 4 && !this.BitesTheDustMode) {
            return !this.canBitesTheDustPlant() || this.isPiloting();
        }
    		
		return super.isAttackIneptVisually(activeP, slot);
    }
    
    @Override
    public void powerActivate(PowerContext context) {
    	
        switch (context)
        {
        	case SKILL_1_NORMAL -> {
        		if (!this.inBitesTheDustMode()) {
                    if (this.canAddStrayCatto() && !this.isPiloting()) {
                        addStrayCattoClient();
                    }else if (this.currentBombStatus == BOMB_NONE) {
	        			tryBlockPlantBomb();
	        		}else {
	        			detonateClient();
	        		}
        		}
        	}
        	case SKILL_1_CROUCH -> {
                if (this.currentBombStatus == BOMB_NONE) {
                    if (this.canItemPlantBomb()) {
                        //tryItemPlantBomb();
                    }
                }else {
                    detonateClient();
                }
        	}
        	case SKILL_1_GUARD, SKILL_1_CROUCH_GUARD -> {
        		if (this.isPiloting()) {
                    detonateClient();
                }else {
                    this.tryBombConfig();
                }
        	}
        	case SKILL_2_NORMAL -> {
        		if (!this.inBitesTheDustMode()) {
                    if (this.isPiloting()) {

                        //toggleControlModeClient();
                    }else if (this.currentBombStatus == BOMB_NONE) {
                        tryMobPlantBomb();
	        		}else {
	        			defuseClient();
	        		}
        		}
        	}
            case SKILL_2_CROUCH_GUARD, SKILL_2_GUARD -> {
                if (!this.inBitesTheDustMode()) {
                    if (this.currentBombStatus == BOMB_BUBBLE) {
                        airBubbleRedirectClient();
                        //toggleControlModeClient();
                    } else if (this.currentBombStatus == BOMB_NONE) {
                        tryBubbleSend();
                    } else {
                        defuseClient();
                    }
                }
            }
        	case SKILL_2_CROUCH -> {
                if (this.isPiloting()) {
                    toggleControlModeClient();
                }else if (!this.inBitesTheDustMode()) {
        			tryImpale();
        		}
        	}
        	case SKILL_3_NORMAL -> {
                if (!this.isPiloting()) {
                    tryToDashClient();
                }
        	}
            case SKILL_3_CROUCH -> {
                if (!this.isPiloting()) {
                    if (this.BitesTheDustMode) {
                        tryToDashClient();
                    } else {
                        tryToSendOrReturnSHA(false);
                    }
                }
            }
            case SKILL_3_GUARD, SKILL_3_CROUCH_GUARD -> {
                if (!this.isPiloting()) {
                    if (this.BitesTheDustMode) {
                        tryToDashClient();
                    } else {
                        tryToSendOrReturnSHA(true);
                    }
                }
        	}
        	case SKILL_4_NORMAL, SKILL_4_CROUCH, SKILL_4_GUARD, SKILL_4_CROUCH_GUARD -> {
                if (!this.isPiloting()) {

                }
        		//bitesTheDustModeToggleClient();
        	}
        }
    }
    // Can methods

    public boolean isBitesTheDustPlanted() {
        return this.getActivePower() == BITES_THE_DUST_COMBAT || this.getActivePower() == BITES_THE_DUST_DAY;
    }

    @Override
    public boolean isPiloting() {
        if (this.getSelf() instanceof Player PE) {
            IPlayerEntity ipe = ((IPlayerEntity) PE);
            int zint = ipe.roundabout$getControlling();
            Entity sde = ((StandUser)PE).roundabout$getStand();
            if (sde != null && zint == sde.getId()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canGuard(){
    	if (this.getActivePower() == DETONATE || isBitesTheDustPlanted()) {
    		return false;
    	}
        return super.canGuard();
    }
    
    @Override
    public boolean canAttack() {
    	if (this.getActivePower() == DETONATE || isBitesTheDustPlanted()) {
    		return false;
    	}
    	
    	return super.canAttack();
    }
    
    @Override
    public boolean canAttack2() {
    	if (isBitesTheDustPlanted()) {
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

    public boolean canAddStrayCatto() {
        Entity maybeStraycat = getTargetEntity(this.self, 3.5f);

        // for now, I am using parrot as placeholder
        if (maybeStraycat instanceof Parrot StrayCatForSure) {
            return StrayCatForSure.isTame() && StrayCatForSure.isOwnedBy(this.getSelf()) && !this.hasStrayCat;
        }

        return false;
    }

    public boolean canBlockPlantBomb() { 
    	StandEntity standEntity = ((StandUser) this.getSelf()).roundabout$getStand();
		
	    if (standEntity != null && standEntity.isAlive() && !standEntity.isRemoved() && this.currentBombStatus == BOMB_NONE) {
	    	
	    	Vec3 vec3d = this.getSelf().getEyePosition(0);
	        Vec3 vec3d2 = this.getSelf().getViewVector(0);
	        Vec3 vec3d3 = vec3d.add(vec3d2.x * blockPlantRange, vec3d2.y * blockPlantRange, vec3d2.z * blockPlantRange);
	        
	        BlockHitResult blockHit = this.getSelf().level().clip(new ClipContext(vec3d, vec3d3, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this.getSelf()));       
	        if (blockHit.getType() != HitResult.Type.BLOCK) { return false; }
            BlockPos pos = blockHit.getBlockPos();
	    	BlockState state = this.getSelf().level().getBlockState(pos);
	    	
	    	return (!ExplosionUtil.isBlockBlackListed(state));
	    }
	    return false;
    }


    public boolean canItemPlantBomb() {
        ItemStack handItem =  this.getSelf().getMainHandItem();

        return !handItem.isEmpty();
    }

    public boolean canBitesTheDustPlant() {
        Entity targetEntity = getTargetEntity(this.self, btdPlantRange);

        if (targetEntity == null) {
            return false;
        }else if (!targetEntity.isAlive() || targetEntity instanceof StandEntity) {
            return false;
        }
        if (targetEntity instanceof LivingEntity LE) {
            if (LE.isDeadOrDying()) {
                return false;
            }
        }else {
            return false;
        }

        return !(((StandUser)targetEntity).roundabout$hasAStand());
    }

    @Override
    public float inputSpeedModifiers(float basis){
        if (this.activePower == PowerIndex.POWER_2) {
            if (this.getSelf().isCrouching()){
                float f = Mth.clamp(0.3F + EnchantmentHelper.getSneakingSpeedBonus(this.getSelf()), 0.0F, 1.0F);
                float g = 1/f;
                basis *= g;
            }
            basis*=0.5f;
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
    /*
    @Override
    public boolean setPowerAttack(){
        if (this.getSelf() instanceof Player) {
            StandEntity standEntity = ((StandUser) this.getSelf()).roundabout$getStand();
            if (standEntity != null && standEntity.isAlive() && !standEntity.isRemoved()) {
                if (!standEntity.getHeldItem().isEmpty()) {
                    if (!this.getSelf().level().isClientSide) {
                        if (throwObject(standEntity.getHeldItem())) {
                            if (MainUtil.isThrownBlockItem(standEntity.getHeldItem().getItem())) {
                                animateStand(StandEntity.BLOCK_THROW);
                            } else {
                                animateStand(StandEntity.ITEM_THROW);
                            }

                            poseStand(OffsetIndex.FOLLOW);
                            standEntity.setHeldItem(ItemStack.EMPTY);
                            if (this.getSelf() instanceof Player) {
                                S2CPacketUtil.sendGenericIntToClientPacket(((ServerPlayer) this.getSelf()),
                                        PacketDataIndex.S2C_INT_ATD, -10);
                            }
                            this.setAttackTimeDuring(-10);

                            return true;
                        }
                    }
                    return false;
                }
            }
        }
        if ((isClient() && canAttack()) || hardBlocker < 1) { return super.setPowerAttack(); }

        return false;
    }*/

    public boolean throwObject(ItemStack item){
        //int cdr = ClientNetworking.getAppropriateConfig().generalStandSettings.objectThrowCooldown;
        //this.setCooldown(PowerIndex.SKILL_2, cdr);
        // /***/

        Vec3 pos = new Vec3(this.self.getX(), this.self.getEyeY()-0.1F, this.self.getZ());
        Direction gravD = ((IGravityEntity)this.self).roundabout$getGravityDirection();
        if (gravD != Direction.DOWN){
            pos = RotationUtil.vecPlayerToWorld(
                    new Vec3(0, this.self.getEyeHeight() - 0.1F, 0
                    ),gravD);
            pos = new Vec3(this.self.getX()+pos.x,this.self.getY()+pos.y,this.self.getZ()+pos.z);
        }

        return ThrownObjectEntity.throwAnObject(this.self, false,item, 0.5f, 0.5f, getThrowAngle(),
                getThrowAngle2(),getThrowAngle3(),false,ThrownObjectEntity.TWTHROW,this.self.getXRot(),this.self.getYRot(),
                new Vec3(pos.x,pos.y,pos.z),false,1, false);

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
                this.setAttackTimeDuring(-15);
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
            this.setAttackTimeDuring(-10);

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
                this.bombEntityID = entID;

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
        if (entity != null && entity.distanceTo(self) > getReach()+0.5f) {
            entity = null;
        }
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
         float shibapitch = 1F;
         float pitch = 1F;
         byte skin = this.getStandUserSelf().roundabout$getStandSkin();
         if (skin == DEADLY || skin == NIGHTMARE) {
             shibapitch = 0.6f;
         }


         if (entity != null) {
             //SE = getKickAttackSound();
        	 SE = ModSounds.FINAL_KICK_EVENT; // create Kick event
             pitch = 1.2F;
             shibapitch *= 1.2f;
         } else {
             SE = ModSounds.PUNCH_2_SOUND_EVENT;
         }


         if (!this.self.level().isClientSide()) {
             this.self.level().playSound(null, this.self.blockPosition(), SE, SoundSource.PLAYERS, 0.95F, pitch);
             //if (chargedFinal >= maxKickTime && entity instanceof LivingEntity) {
             this.self.level().playSound(null, this.self.blockPosition(), ModSounds.KILLER_QUEEN_SHIBA_EVENT, SoundSource.PLAYERS, 0.95F, shibapitch);
             //}
         }
    }
    
    
    
    @Override
    public boolean setPowerOther(int move, int lastMove) {

    	if (move == PowerIndex.POWER_1) {
            return this.blockPlantBomb();
        } else if (move == PowerIndex.POWER_1_SNEAK) {
            return this.itemPlantBomb();
    	} else if (move == PowerIndex.POWER_4) {
    		return switchModes();
    	} else if (move == PowersKillerQueen.DEFUSE) {
    		return defuseServer();
    	} else if (move == PowerIndex.POWER_2) {
    		return mobPlantBomb();
        } else if (move == PowerIndex.POWER_2_SNEAK) {
            return impale();
        } else if (move == PowerIndex.POWER_2_BLOCK) {
            return this.bubbleSend();
        } else if (move == PowerIndex.POWER_2_EXTRA) {
            return this.bubbleRedirect();
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
        } else if (move == STRAY_CAT_ADD){
            return this.addStrayCatto();
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
        } else if (move == PowerIndex.POWER_1_SNEAK) {
            this.plantInventorySlot = chargeTime;
        }

        return super.tryIntPower(move, forced, chargeTime);
    }

    @Override
    public boolean tryBlockPosPower(int move, boolean forced, BlockPos blockPos){
    	return super.tryBlockPosPower(move, forced, blockPos);
    }

    @Override
    public boolean tryPosPower(int move, boolean forced, Vec3 pos){
        savedPos = pos;
        return tryPower(move, forced);
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
                this.currentShaStatus = (byte)data;
            }
            case PowersKillerQueen.ENTITY_BOMB -> {
                this.bombEntityID = data;
                this.bombEntity = this.getSelf().level().getEntity(data);
            }
            case PowersKillerQueen.BUBBLE_BOMB -> {
                this.bombBubbleID = data;
                this.bombBubble = (StrayCatAirBubble) this.getSelf().level().getEntity(data);
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
        } else if (this.getActivePower() == DETONATE) {
            updateDetonate();
        }

    	super.updateUniqueMoves();
    }

    public void updateDetonate() {
        if (this.attackTimeDuring >= ClientNetworking.getAppropriateConfig().killerQueenSettings.explosionActivationCooldown) {
            this.setAttackTimeDuring(-10);
            this.explode();
        }
    }

    public void updateMobPlant(){
        if (this.attackTimeDuring > -1) {
            if (this.attackTimeDuring >= getMobPlantWindup()) {
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
        if (canExecuteMoveWithLevel(getSheerHeartAttackLevel())) {
            if (shaThrow) {
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_3_BLOCK, true);
                tryPowerPacket(PowerIndex.POWER_3_BLOCK);
            } else {
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_3, true);
                tryPowerPacket(PowerIndex.POWER_3);
            }
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
    	//if (!this.onCooldown(PowerIndex.SKILL_1)) {
        if (this.currentBombStatus != BOMB_NONE && this.canAttack() && this.canAttack2()) {

            ((StandUser) this.getSelf()).roundabout$tryPower(PowersKillerQueen.DETONATE, true);
            tryPowerPacket(PowersKillerQueen.DETONATE);
        }
    	//}
    }

    public void toggleControlModeClient() {

        if (isPiloting()) {
            if (this.self instanceof Player PE) {
                IPlayerEntity ipe = ((IPlayerEntity) PE);
                ipe.roundabout$setIsControlling(0);
            }
            //this.setSomeTicks(5);
            tryIntToServerPacket(PacketDataIndex.INT_UPDATE_PILOT, 0);
        } else {
            StandEntity entity = this.getStandEntity(this.self);
            int L = 0;
            if (entity != null) {
                L = entity.getId();
            }
            tryIntToServerPacket(PacketDataIndex.INT_UPDATE_PILOT, L);
        }

        //this.setCooldown(PowerIndex.SKILL_2_GUARD, 15);
    }

    public void tryImpale() {
        if (!this.onCooldown(PowerIndex.SKILL_2_SNEAK)) {
            if (canExecuteMoveWithLevel(getImpaleLevel())) {
                if (this.activePower == PowerIndex.POWER_2_SNEAK) {
                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                    tryPowerPacket(PowerIndex.NONE);
                } else {
                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_2_SNEAK, true);
                    tryPowerPacket(PowerIndex.POWER_2_SNEAK);
                }
            }
        }
    }

    public void tryBubbleSend() {
        if (!this.onCooldown(PowerIndex.SKILL_2_GUARD) && (this.getActivePower() == PowerIndex.NONE || this.getActivePower() == PowerIndex.GUARD)
            && this.canUseStrayCat()) {
            bombConfigPacket();
            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_2_BLOCK, true);
            tryPowerPacket(PowerIndex.POWER_2_BLOCK);
        }
    }

    public void addStrayCattoClient() {
        if (!this.hasStrayCat) {
            ((StandUser) this.getSelf()).roundabout$tryPower(STRAY_CAT_ADD, true);
            tryPowerPacket(STRAY_CAT_ADD);
        }
    }

    public void tryBlockPlantBomb() {
    	if (!this.onCooldown(PowerIndex.SKILL_1) && this.canAttack2()) {
            bombConfigPacket();

    		((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_1, true);
        	tryPowerPacket(PowerIndex.POWER_1);
    	}
    }

    public void tryItemPlantBomb() {
        if ((!this.onCooldown(PowerIndex.SKILL_1_SNEAK)) && this.canAttack2() && this.currentBombStatus == BOMB_NONE) {
            bombConfigPacket();
            ItemStack stack = this.getSelf().getMainHandItem();
            if (!stack.isEmpty()) {
                ((StandUser) this.getSelf()).roundabout$tryIntPower(PowerIndex.POWER_1_SNEAK, true,
                        ((Player) this.getSelf()).getInventory().selected);

                tryIntPowerPacket(PowerIndex.POWER_1_SNEAK,
                        ((Player) this.getSelf()).getInventory().selected);
            }
        }
    }

    public void airBubbleRedirectClient(){
        if (!this.onCooldown(PowerIndex.SKILL_EXTRA_2)) {
            Vec3 pos = MainUtil.getRaytracePointOnMobOrBlock(this.self, 30);
            this.tryPosPower(PowerIndex.POWER_2_EXTRA, true, pos);
            tryPosPowerPacket(PowerIndex.POWER_2_EXTRA, pos);
        }

    }

    public void tryMobPlantBomb() {
        if (!this.onCooldown(PowerIndex.SKILL_2) && this.canAttack2()) {
            bombConfigPacket();

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

    public boolean addStrayCatto() {
        if (!this.isClient()) {
            Entity maybeStraycat = getTargetEntity(this.self, 3.5f);

            // for now, I am using parrot as placeholder
            if (maybeStraycat instanceof Parrot StrayCatForSure) {
                if (StrayCatForSure.isTame() && StrayCatForSure.isOwnedBy(this.getSelf()) && !this.hasStrayCat) {
                    StrayCatForSure.discard();
                    this.hasStrayCat = true;
                    this.saveDiscAndSync();

                    return true;
                }
            }
        }
        
        return this.canAddStrayCatto();
    }

    public boolean defuseServer() {
    	if (!this.isClient()) {
            if (currentBombStatus == BOMB_BLOCK) {
                this.bombBlock.discard();
                this.bombBlock = null;
            }else if (currentBombStatus == BOMB_ENTITY) {
                this.bombEntity = null;
            }else if (currentBombStatus == BOMB_BUBBLE) {
                if (this.bombBubble != null) {
                    this.bombBubble.setHasTimeLimit(true);
                    this.bombBubble.setIsPlanted(false);
                }
                this.bombBubble = null;
            }
    	}
    	
    	this.syncBombStatus(BOMB_NONE);
    	
    	return true;
    }

    public void bubbleContacted(Entity ent) {
        if (this.bombConfig >= 2 || this.getActivePower() == DETONATE) {
            this.bombEntity = ent;
            syncBombStatus(BUBBLE_CONTACT);
            if (this.getActivePower() != DETONATE) {
                this.explode();
            }
        }else {
            syncBombStatus(NONE);
        }
    }

    public boolean bubbleSend() {
        if (!canUseStrayCat()) {
            return false;
        }
        if (!this.isClient()) {
            if (this.currentBombStatus == BOMB_NONE) {
                LivingEntity user = this.getSelf();
                StrayCatAirBubble bubble = ModEntities.STRAY_CAT_AIRBUBBLE.create(user.level());
                if (bubble != null) {

                    bubble.setSped(getStrayCatAirBubbleSpeed());

                    bubble.setOwner(user);
                    bubble.setSkin(this.getBubbleSkin());
                    bubble.setIsKQAirBubble(true);
                    bubble.setIsPlanted(true);
                    bubble.setHasTimeLimit(false);
                    bubble.setFollowOwnerView(true);

                    Vec3 addToPosition = new Vec3(0, user.getEyeHeight() * 0.85f, 0);
                    Direction direction = ((IGravityEntity) user).roundabout$getGravityDirection();
                    if (direction != Direction.DOWN) {
                        addToPosition = RotationUtil.vecPlayerToWorld(addToPosition, direction);
                    }
                    Vec3 pos = user.getPosition(1).add(addToPosition.x, addToPosition.y, addToPosition.z).add(user.getForward().scale(user.getBbWidth() * 1));
                    bubble.setPos(pos.x(), pos.y(), pos.z());
                    bubble.shootFromRotationDeltaAgnostic(user, user.getXRot(), user.getYRot(), 1.0F, getStrayCatAirBubbleSpeed(), 0);
                    //bubble.shootFromRotation(P, P.getXRot(), P.getYRot(), -0.5F, SPEED, 0.00f);

                    user.level().addFreshEntity(bubble);

                    this.bombBubble = bubble;

                    int entID = this.bombBubble.getId();
                    this.bombBubbleID = entID;

                    S2CPacketUtil.sendIntPowerDataPacket((Player)this.getSelf(),PowersKillerQueen.BUBBLE_BOMB, entID);

                    syncBombStatus(BOMB_BUBBLE);

                    return true;
                }else {
                    Roundabout.LOGGER.info("hey what?");
                }

            }
        }

        return false;
    }

    public boolean bubbleRedirect(){
        if (this.bombBubble != null){
            this.setCooldown(PowerIndex.SKILL_EXTRA_2, 3);

            if (!this.self.level().isClientSide()) {
                if (savedPos != null) {

                    StrayCatAirBubble value = this.bombBubble;
                    value.setFollowOwnerView(false);

                    Vec3 vector = new Vec3((savedPos.x() - value.getX()),
                            (savedPos.y() - value.getY()),
                            (savedPos.z() - value.getZ())).normalize().scale(value.getSped());

                    value.setDeltaMovement(vector);
                    value.hurtMarked = true;
                    value.hasImpulse = true;
                }
            } else {
                if (savedPos != null){
                    this.self.playSound(ModSounds.BUBBLE_HOVERED_OVER_EVENT, 0.2F, (float) (0.95F+Math.random()*0.1F));
                    this.self.level()
                            .addParticle(
                                    ModParticles.POINTER_SOFT,
                                    savedPos.x(),
                                    savedPos.y() + 0.5,
                                    savedPos.z(),
                                    0,
                                    0,
                                    0
                            );
                }
            }

        }
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
                if (blockHit.getType() != HitResult.Type.BLOCK) { return true; }

                BlockPos pos = blockHit.getBlockPos();
		    	BlockState state = this.getSelf().level().getBlockState(pos);

		    	if (!ExplosionUtil.isBlockBlackListed(state)) {
                    this.bombBlock = ModEntities.BLOCK_BOMB.create(this.getSelf().level());
		    		this.bombBlock.setUser(this.self);
                    this.bombBlock.setYRot(0);

		    		this.bombBlock.setBlockPos(pos);
		    		this.self.level().addFreshEntity(this.bombBlock);
		    		this.currentBombStatus = BOMB_BLOCK;
		    		
		    		this.syncBombStatus(BOMB_BLOCK);
		    		
		    		this.animateStand(KillerQueenEntity.BLOCK_PLANT);
		    		this.poseStand(OffsetIndex.ATTACK);
                    this.setAttackTimeDuring(-blockPlantMaxTicks);
                    this.setActivePower(PowerIndex.POWER_1);

                    return true;
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
            playSoundsIfNearby(IMPALE_NOISE, 27, false);
            double rand = Math.random();
            if (rand > 0.5) {
                this.animateStand(KillerQueenEntity.MOB_PLANT);
            }else {
                this.animateStand(KillerQueenEntity.MOB_PLANT_2);
            }
            this.poseStand(OffsetIndex.GUARD);

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
        if (!this.getSelf().level().isClientSide()) {
            /*StandEntity standEntity = ((StandUser) this.getSelf()).roundabout$getStand();
            if (standEntity != null && standEntity.isAlive() && !standEntity.isRemoved() &&*/
            if (this.getSelf().isAlive() && this.currentBombStatus == BOMB_NONE &&
                    this.getSelf() instanceof Player PL) {
                ItemStack stack = PL.getInventory().getItem(this.plantInventorySlot);
                /*if (stack.getItem() instanceof BlockItem BI) {
                    BlockState BS = BI.getBlock().defaultBlockState();
                    if (ExplosionUtil.isBlockBlackListed(BS)) { return false; }


                }*/
                if (!stack.isEmpty()) {
                    ItemStack bombItem = stack.copyWithCount(1);
                    CompoundTag tag = bombItem.getOrCreateTag().copy();
                    //stack.setCount(stack.getCount() - 1); // - serve for eviting duping, disabled for test reasons

                    tag.putString("Kq Bomb", this.getSelf().getName().getString());
                    bombItem.setTag(tag);

                    this.bombItemStack = bombItem;
                    syncBombStatus(BOMB_ITEM);

                    PL.addItem(bombItem);

                    //return true;
                }
            }
        }
        return true;
    }

    public boolean sendOrReturnSHA(boolean shaThrow) {
        if (canExecuteMoveWithLevel(getSheerHeartAttackLevel())) {
            if (this.currentShaStatus == SHA_NONE) {
                this.playSoundsIfNearby(SHEER_HEART_ATTACK, 27, true);
                if (shaThrow) {
                    this.animateStand(KillerQueenEntity.FIRST_PUNCH);
                } else {
                    this.animateStand(KillerQueenEntity.SHA_SEND);
                }

                this.poseStand(OffsetIndex.FOLLOW);
                this.setAttackTimeDuring(-15);
                this.setActivePower(PowerIndex.POWER_3);
            }

            if (!this.getSelf().level().isClientSide()) {
                if (SHA == null || SHA.isRemoved()) {
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
                            SHA.shoot(getRayBlock(this.self, 4f));
                        }
                        this.syncShaStatus(SHA_SEND);
                    }

                } else {
                    SHA.setHaveToReturn(!SHA.getHaveToReturn());
                    if (SHA.getHaveToReturn()) {
                        this.syncShaStatus(SHA_RETREAT);
                    } else {
                        this.syncShaStatus(SHA_SEND);
                    }
                }
            }
            return true;
        }
        return false;
    }

    public void detectIfShouldDefuse() {
        if (this.currentBombStatus == BOMB_BLOCK) {
            if (this.bombBlock == null) { this.defuseServer(); }
            else if (this.bombBlock.blockGotDestroyed()) { this.defuseServer(); }
            else if (this.bombBlock.level() != this.getSelf().level()) { this.defuseServer(); }
            else if (this.bombBlock.isRemoved()) {this.defuseServer();}
        }
        else if (this.currentBombStatus == BOMB_ENTITY) {
            if (this.bombEntity == null) { this.defuseServer(); }
            else if (!this.bombEntity.isAlive()) { this.defuseServer(); }
            else if (this.bombEntity instanceof LivingEntity LE && LE.isDeadOrDying()) {
                this.defuseServer();
            }
            else if (this.bombEntity.level() != this.getSelf().level()) {
                this.defuseServer();
            }
        }
        else if (this.currentBombStatus == BOMB_BUBBLE) {
            if (this.bombBubble == null) { this.defuseServer(); }
            else if (this.bombBubble.isRemoved()) { this.defuseServer(); }
        }

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
                if (this.currentShaStatus == SHA_NONE) {
                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_3, true);
                }
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
                    } else if ((this.getSelf() instanceof Piglin || upAiNow
                            || this.getSelf() instanceof AbstractIllager || this.getSelf() instanceof Raider) && dist <= 15 && dist >= 13) {
                        if (!onCooldown(PowerIndex.SKILL_3) && this.currentShaStatus == SHA_NONE) {
                            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_3_BLOCK, true);
                        }
                    } else if ((this.getSelf() instanceof Spider || this.getSelf() instanceof Slime
                            || this.getSelf() instanceof JotaroNPC || upAiNow
                            || this.getSelf() instanceof Rabbit || this.getSelf() instanceof AbstractVillager
                            || this.getSelf() instanceof Piglin || this.getSelf() instanceof Vindicator) &&
                            dist <= 19 && dist >= 5 && this.currentBombStatus == BOMB_NONE && !wentForCharge) {
                        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_2_BLOCK, true);

                    } else if (this.currentBombStatus == BOMB_NONE && !wentForCharge) {
                        double RNG = Math.random();
                        if (!onCooldown(PowerIndex.SKILL_2) && dist <= (mobPlantRange + 3.5)
                                && RNG >= 0.70) {
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
    }

    @Override
    public void setPiloting(int ID) {
        if (this.self instanceof Player PE) {
            IPlayerEntity ipe = ((IPlayerEntity) PE);
            Entity ent = this.self.level().getEntity(ID);
            if (ent != null && ent.is(this.getPilotingStand())) {
                this.bombBubble.setFollowOwnerView(false);
                ipe.roundabout$setIsControlling(ID);
            } else {
                ipe.roundabout$setIsControlling(ID);
            }
        }
    }

    @Override
    public void pilotStandControls(KeyboardPilotInput kpi, LivingEntity entity) {
        int $$13 = 0;

        if ((this.bombBubble != null) && this.currentBombStatus == BOMB_BUBBLE && !this.isClient()) {
            float flyingSpeed = getStrayCatAirBubbleSpeed();

            LivingEntity ent = getPilotingStand();
           // IEntityAndData entityAndData = ((IEntityAndData) ent);

            if (kpi.shiftKeyDown) { $$13--; }
            if (kpi.jumping) { $$13++; }

            //entity.xxa = kpi.leftImpulse;
            //entity.zza = kpi.forwardImpulse;
            Vec3 direction = this.getSelf().getViewVector(0);
            Vec3 directionFix = new Vec3(direction.x, $$13, direction.z).normalize();
            Vec3 axis = directionFix.normalize().yRot((float)(Math.PI / 2.0) * kpi.leftImpulse);;

            if (kpi.forwardImpulse < 0) {
                axis = axis.yRot((float)Math.PI);
            }



            if ((kpi.leftImpulse != 0 || kpi.forwardImpulse != 0 || $$13 != 0)) {
                Entity user = this.self;
                float rotY = user.getYRot() +(float)(Math.PI / 2.0) * kpi.leftImpulse;
                float rotX = user.getXRot() + (float)(Math.PI / 2.0) * $$13;
                if (kpi.forwardImpulse < 0) {
                    rotY += (float)(Math.PI);
                }

                this.bombBubble.shootFromRotationDeltaAgnostic2(user, rotX, rotY, 1.0F, flyingSpeed);
            }else {
                this.bombBubble.shoot(0.0f, 0.0f, 0.0f, 0, 0.0f);
            }

        }
    }

    @Override
    public void tickPower(){
        if (mobPlantTicks > 0){ mobPlantTicks--; }
        if (impaleTicks > 0){ impaleTicks--; }

        if (!isClient()) {
            this.detectIfShouldDefuse();

            if (this.SHA != null) {
                if ((this.SHA.shaIsNear() && this.SHA.getHaveToReturn()) || this.SHA.isRemoved() || this.inBitesTheDustMode()) {
                    this.SHA.discard();
                    this.syncShaStatus(SHA_NONE);
                    this.setCooldown(PowerIndex.SKILL_3, ClientNetworking.getAppropriateConfig().killerQueenSettings.sheerHeartAttackCooldown);
                }
            }else if (this.currentShaStatus != SHA_NONE){
                this.syncShaStatus(SHA_NONE);
            }

            byte activePower = this.getActivePower();

            if (this.bombConfig >= 2 && activePower != DETONATE) {
                if (this.currentBombStatus == BOMB_BLOCK) {
                    if(Objects.nonNull(this.bombBlock) && activePower != PowerIndex.POWER_1) {

                        Entity contact = this.bombBlock.detectContact();
                        if (contact != null) {
                            this.syncBombStatus(BLOCK_CONTACT);
                            this.bombBlock.discard();
                            this.bombEntity = contact;
                            this.detonate();
                        }
                    }
                }else if (this.currentBombStatus == BOMB_ENTITY) {
                    if(Objects.nonNull(this.getBombEntity()) && activePower != PowerIndex.POWER_2) {
                        Entity contact = this.isBombEntityContacting();
                        if (contact != null) {
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
                    entity.equals(this.getSelf()) || entity.equals(((StandUser)this.getStandUserSelf()).roundabout$getStand())) {
                continue;
            }
            Vec3 pos = new Vec3(this.bombEntity.getX(), this.bombEntity.getY(), this.bombEntity.getZ());
            Vec3 dir = pos.subtract(entity.getX(), entity.getY(), entity.getZ());

            double dist = Math.abs(dir.length());

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
       }else if (soundChoice == SHEER_HEART_ATTACK) {
           return ModSounds.KILLER_QUEEN_SHA_SUMMON_EVENT;
       }
       
        return super.getSoundFromByte(soundChoice);
    }

    // hightlights entity things :0
    public boolean highlightsEntity(Entity ent,Player player){
        if(this.currentBombStatus == BOMB_ENTITY) {
            if (this.getBombEntity() != null) {
                return this.getSelf().hasLineOfSight(ent) && ent == this.getBombEntity();
            }
        }

        return false;
    }

    @Override
    public int highlightsEntityColor(Entity ent, Player player){
        if(this.currentBombStatus == BOMB_ENTITY) {
            if (this.getBombEntity() != null && ent == this.getBombEntity()) {
                if (this.getSelf().hasLineOfSight(ent) && ent == this.getBombEntity()) {
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
        $$1.add(drawSingleGUIIcon(context,18,leftPos+39, topPos+99, getStrayCatLevel(), "ability.roundabout.kq_guard_bubble",
                "instruction.roundabout.hold_block", StandIcons.KILLER_QUEEN_GUARD_BUBBLES,0,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+39,topPos+118,0, "ability.roundabout.kq_bomb_block",
                "instruction.roundabout.press_skill", StandIcons.KILLER_QUEEN_PLANT_BOMB_BLOCK,1,level,bypas));

        $$1.add(drawSingleGUIIcon(context,18,leftPos+58,topPos+80, getBitesTheDustLevel(), "ability.roundabout.kq_btd_day",
                "instruction.roundabout.press_skill_btd_mode", StandIcons.KILLER_QUEEN_BTD_DAY,1,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+58,topPos+99, 0, "ability.roundabout.kq_bomb_item",
                "instruction.roundabout.press_skill_crouch", StandIcons.KILLER_QUEEN_PLANT_BOMB_ITEM,1,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+58,topPos+118, 0, "ability.roundabout.kq_config",
                "instruction.roundabout.press_skill_block", StandIcons.KILLER_QUEEN_BOMB_SETIINGS,1,level,bypas));

        $$1.add(drawSingleGUIIcon(context,18,leftPos+77,topPos+80,0, "ability.roundabout.kq_bomb_mob",
                "instruction.roundabout.press_skill", StandIcons.KILLER_QUEEN_PLANT_BOMB_MOB,2,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+77,topPos+99,getImpaleLevel(), "ability.roundabout.impale",
                "instruction.roundabout.press_skill_crouch", StandIcons.KILLER_QUEEN_IMPALE,2,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+77,topPos+118, getStrayCatLevel(), "ability.roundabout.kq_bomb_bubble",
                "instruction.roundabout.press_skill_block", StandIcons.KILLER_QUEEN_BUBBLE_LAUNCH,2,level,bypas));

        $$1.add(drawSingleGUIIcon(context,18,leftPos+96,topPos+80, getBitesTheDustLevel(), "ability.roundabout.kq_btd_combat",
                "instruction.roundabout.press_skill_shooting_mode", StandIcons.KILLER_QUEEN_BTD_COMBAT,2,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+96,topPos+99,0, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE,3,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+96,topPos+118,0, "ability.roundabout.fall_brace",
                "instruction.roundabout.press_skill_falling", StandIcons.KILLER_QUEEN_FALL_BRACE,3,level,bypas));

        $$1.add(drawSingleGUIIcon(context,18,leftPos+115,topPos+80,0, "ability.roundabout.vault",
                "instruction.roundabout.press_skill_air", StandIcons.KILLER_QUEEN_VAULT,3,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+115,topPos+99, getSheerHeartAttackLevel(), "ability.roundabout.kq_sha_summon",
                "instruction.roundabout.press_skill_crouch", StandIcons.KILLER_QUEEN_SHA_SUMMON,3,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+115,topPos+118,getSheerHeartAttackLevel(), "ability.roundabout.kq_sha_throw",
                "instruction.roundabout.press_skill_block", StandIcons.KILLER_QUEEN_SHA_THROW,3,level,bypas));

        $$1.add(drawSingleGUIIcon(context,18,leftPos+134,topPos+80, getBitesTheDustLevel(), "ability.roundabout.kq_btd_mode",
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

    public byte getBubbleSkin() {

        switch (this.getStandUserSelf().roundabout$getStandSkin())
        {
            case KillerQueenEntity.MANGA, KillerQueenEntity.NOTW,
                 KillerQueenEntity.GOGO, KillerQueenEntity.CREEPER -> {return 1;}
            case KillerQueenEntity.LIMBUSMORTIS, KillerQueenEntity.GUNPOWDER,
                 KillerQueenEntity.TAMA, KillerQueenEntity.STRAY-> {return 2;}
            case KillerQueenEntity.FINAL, KillerQueenEntity.YELLOW,
                 KillerQueenEntity.ARTWORK -> {return 3;}
            case KillerQueenEntity.MINESWEEPER -> {return 4;}
            
            default -> {return 0;}
        }
    }

    public boolean explode() {
        if (!this.isClient()) {
            /*
            if (this.currentBombStatus == BOMB_BLOCK || this.currentBombStatus == BLOCK_CONTACT) {
                this.setCooldown(PowerIndex.SKILL_1, ClientNetworking.getAppropriateConfig().killerQueenSettings.blockPlantCooldown);
            }else if (this.currentBombStatus == BOMB_ENTITY) {
                this.setCooldown(PowerIndex.SKILL_2, ClientNetworking.getAppropriateConfig().killerQueenSettings.mobPlantCooldown);
            }*/

            addEXP(5);

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
            } else if (bStatus == PowersKillerQueen.BOMB_ENTITY || bStatus == PowersKillerQueen.BLOCK_CONTACT || bStatus == PowersKillerQueen.BUBBLE_CONTACT) {
                target = getBombEntity();
                vPos = target.position();
                bPos = new BlockPos(target.getBlockX(), target.getBlockY(), target.getBlockZ());
                level = target.level();

                if (target instanceof LivingEntity LE) {
                    addEXP(bStatus == BOMB_ENTITY ? 10 : 5, LE);
                }

                this.bombEntity = null;
            }else if (bStatus == PowersKillerQueen.BOMB_BUBBLE) {
                vPos = this.bombBubble.position();
                bPos = new BlockPos(this.bombBubble.getBlockX(), this.bombBubble.getBlockY(), this.bombBubble.getBlockZ());
                level = this.bombBubble.level();
                this.bombBubble.discard();

            }

            if (canDestroyBlocks) {
                ExplosionUtil.explodeBlocksBase(bPos, level, 1.0f, true);
            }

            Config.KillerQueenSettings config = ClientNetworking.getAppropriateConfig().killerQueenSettings;

            float damage = bStatus == BOMB_BUBBLE ? config.StrayCatAirBubblesDamage : config.explosionDetonateMaxDamage;

            float dmgOnPlayers = (config.killerQueenAttackMultOnPlayers/100.0f);
            float dmgOnMobs = (config.killerQueenAttackMultOnMobs/100.0f);

            DamageSource dmg = ModDamageTypes.of(level, ModDamageTypes.EXPLOSIVE_STAND, this.getSelf());
            DamageSource sneakyDmg = ModDamageTypes.of(level, ModDamageTypes.EXPLOSIVE_STAND, null);
            ExplosionUtil.explosionHurtSneakyWithMulti(vPos, dmg, level,
                    damage,
                    0.4f, 1.5f, dmgOnMobs, dmgOnPlayers);

            if (target != null && bStatus == BOMB_ENTITY) {
                float hitPoints = config.mobPlantDesintegrationDamage;

                if (this.getReducedDamage(target)) {
                    hitPoints *= dmgOnPlayers;
                }else {
                    hitPoints *= dmgOnMobs;
                }

                boolean playersHitkill = config.mobPlantHitkillPlayers;
                boolean mobsHitkill = config.mobPlantHitkillMobs;

                boolean isBoss = MainUtil.isBossMob(target);

                if (isBoss) { hitPoints *= 0.70f; }

                if (target instanceof LivingEntity LE) {
                    if (LE.hasLineOfSight(this.getSelf()) && !isBoss){
                        dmg = sneakyDmg;
                    }
                }

                if (target instanceof Player pl) {
                    /*
                     * apparently, "die(dmg)" would result in the player been glitched
                     * and unable to respawn for some weird reason.
                    */
                    if (!pl.isCreative()) {
                        if (playersHitkill) { pl.hurt(dmg, 9999999999.0f); }
                        else { pl.hurt(dmg, hitPoints); }
                    }
                } else {
                    if (mobsHitkill && !isBoss) { target.hurt(dmg, 9999999999.0f);}
                    else {target.hurt(dmg, hitPoints);}
                }
            }

            if(target != null && !target.isAlive() && !MainUtil.isBossMob(target)){ target.discard(); }

            ExplosionUtil.explodeEffects(vPos, level, ModParticles.KILLER_QUEEN_EXPLOSION, 0.6f);
            this.getSelf().level().playSound(null, bPos, ModSounds.KILLER_QUEEN_EXPLOSION_EVENT, SoundSource.PLAYERS, 0.65F, 1.0f);
            //explosionSFX(vPos, 10);

        }
		this.syncBombStatus(BOMB_NONE);
		this.setPowerNone();
		
    	return true;
    }
    
    public boolean detonate() {
    	if (!this.isClient() && this.getActivePower() == PowerIndex.NONE) {
            this.playSoundsIfNearby(DETONATE, 27, true);

            int detonateWindup = ClientNetworking.getAppropriateConfig().killerQueenSettings.explosionActivationCooldown;

            if (this.currentBombStatus == BOMB_BLOCK || this.currentBombStatus == BLOCK_CONTACT) {
                this.setCooldown(PowerIndex.SKILL_1, ClientNetworking.getAppropriateConfig().killerQueenSettings.blockPlantCooldown + detonateWindup);
            }else if (this.currentBombStatus == BOMB_ENTITY) {
                this.setCooldown(PowerIndex.SKILL_2, ClientNetworking.getAppropriateConfig().killerQueenSettings.mobPlantCooldown  + detonateWindup);
            }else if (this.currentBombStatus == BUBBLE_CONTACT || this.currentBombStatus == BOMB_BUBBLE) {
                this.setCooldown(PowerIndex.SKILL_2_GUARD, ClientNetworking.getAppropriateConfig().killerQueenSettings.bubbleShootCooldown);
            }

            if (this.currentBombStatus == BOMB_ITEM) {
                StandEntity standEntity = ((StandUser) this.getSelf()).roundabout$getStand();
                if (standEntity != null && standEntity.isAlive() && !standEntity.isRemoved() && this.getSelf() instanceof Player PL && this.bombItemStack != null) {
                    if (!this.bombItemStack.isEmpty()) {
                        PL.getInventory().removeItem(this.bombItemStack);
                        //standEntity.setHeldItem(this.bombItemStack);
                        /*this.setAttackTimeDuring(0);
                        poseStand(OffsetIndex.FOLLOW_NOLEAN);
                        if (MainUtil.isThrownBlockItem(this.bombItemStack.getItem())) {
                            animateStand(StandEntity.BLOCK_GRAB);
                        } else {
                            animateStand(StandEntity.ITEM_GRAB);
                        }*/
                        if (throwObject(this.bombItemStack)) {
                            if (MainUtil.isThrownBlockItem(standEntity.getHeldItem().getItem())) {
                                animateStand(StandEntity.BLOCK_THROW);
                            } else {
                                animateStand(StandEntity.ITEM_THROW);
                            }

                            poseStand(OffsetIndex.FOLLOW);
                            //standEntity.setHeldItem(ItemStack.EMPTY);
                            if (this.getSelf() instanceof Player) {
                                S2CPacketUtil.sendGenericIntToClientPacket(((ServerPlayer) this.getSelf()),
                                        PacketDataIndex.S2C_INT_ATD, -10);
                            }
                            this.setAttackTimeDuring(-10);

                            return true;
                        }
                    }
                }
            }else {
                this.setAttackTimeDuring(0);
                this.poseStand(OffsetIndex.GUARD);
                this.animateStand(KillerQueenEntity.DETONATE);
                this.setActivePower(DETONATE);
            }
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
