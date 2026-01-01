package net.hydra.jojomod.event.powers;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.access.IProjectileAccess;
import net.hydra.jojomod.client.*;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.projectile.KnifeEntity;
import net.hydra.jojomod.entity.projectile.RoundaboutBulletEntity;
import net.hydra.jojomod.entity.projectile.ThrownObjectEntity;
import net.hydra.jojomod.entity.stand.FollowingStandEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModGamerules;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.fates.powers.AbilityScapeBasis;
import net.hydra.jojomod.item.HarpoonItem;
import net.hydra.jojomod.item.KnifeItem;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.hydra.jojomod.item.StandDiscItem;
import net.hydra.jojomod.stand.powers.presets.TWAndSPSharedPowers;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.IceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.zetalasis.networking.message.api.ModMessageEvents;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StandPowers extends AbilityScapeBasis {

    /**StandPowers is a class that every stand has a variation of, override it
     * to define and tick through stand abilities and cooldowns.
     * Note that most generic STAND USER code is in a mixin to the livingentity class.*/


    // -----------------------------------------------------------------------------------------
    // OVERRIDE THIS
    // -----------------------------------------------------------------------------------------
    /**This is imporant, on every stand class, override this and do something like
     *     return new PowersTheWorld(entity); */
    public StandPowers generateStandPowers(LivingEntity entity){
        return null;
    }




    public boolean hasPassiveCombatMode(){
        return false;
    }

    // -----------------------------------------------------------------------------------------
    // FUNCTIONS TO OVERRIDE (Excluding hud stuff, see next section for that)
    // -----------------------------------------------------------------------------------------

    /**Is your stand rendered on the player model like hey ya or hermit purple? If so, override
     * worthinessType() to return HUMANOID_WORTHY instead so only mobs it can render on are worthy*/
    public static final byte
            ALL_WORTHY = 1,
            HUMANOID_WORTHY = 2;
    public byte worthinessType(){
        return ALL_WORTHY;
    }

    /**Is your stand a main stand, or a secondary stand?*/
    public boolean isSecondaryStand(){
        return false;
    }

    /**If your stand has one singular entity that comes out when you summon it*/
    public boolean canSummonStandAsEntity(){
        return true;
    }
    /**Override if the above is true, and return the stand entity*/
    public StandEntity getNewStandEntity(){
        return null;
    }
    /**If you are not currently supposed to be able to activate your stand, override for sealing reasons*/
    public boolean canSummonStand(){
        return false;
    }



    /**If the standard right click input should usually be canceled while your stand is active*/
    public boolean interceptGuard(){
        return false;
    }
    /**The above, but for canceling all right click interactions like villager interactions etc*/
    public boolean interceptAllInteractions(){
        return false;
    }
    public boolean buttonInputGuard(boolean keyIsDown, Options options) {
        return false;
    }



    /**Override this if you need ultra specific timing on tickpower after other entity functions are called,
     * this works the best for subtle movement tricks*/
    public void tickPowerEnd(){
    }

    /**The AI for a stand User Mob, runs every tick. AttackTarget may be null*/
    public void tickMobAI(LivingEntity attackTarget){
    }
    /**If you want mobs to stop doing their own attacks as well during certain abilities override this*/
    public boolean disableMobAiAttack(){
        return ((this.activePower == PowerIndex.BARRAGE_CLASH && this.attackTimeDuring > -1) || this.isBarraging());
    }

    /**Edit this to apply special effect when stand virus is ravaging a mob with this stand.
     * Use the instance to time the effect appropriately*/
    public void tickStandRejection(MobEffectInstance effect){
    }

    /** Called per client tick, use for particle FX and such */
    public void visualFrameTick() {};

    /**Override this to determine how many points of damage your stand's guard can take before it breaks,
     * generally hooks into config settings.*/
    public int getMaxGuardPoints(){
        return 10;
    }

    /**Runs this code while switching out of your stand with a disc*/
    public void onStandSwitch(){
        getStandUserSelf().roundabout$setUniqueStandModeToggle(false);
    }
    /**Runs this code while pressing R to activate powers*/
    public void onStandSummon(boolean desummon){

    }
    /**Runs this code while switching into stand with a disc*/
    public void onStandSwitchInto(){
    }

    /**Holds one arm out with the player model, override if you are using a stand like soft and wet or emperor that
     * should make the player hold their arm out in 3d person*/
    public boolean hasShootingModeVisually(){
        return false;
    }

    /**If your stand is using a perma cast, which basically boils down to casting an ability over an area.
     * Think casting a giant MR ankh, Green Day's mold field, etc.
     * These are data structures that automatically sync across server and client so they
     * are advisable to use for gigantic sweeeping abilities (not simple hitboxes or hitbox zones).*/
    public float getPermaCastRange(){
        return 100;
    }
    public byte getPermaCastContext(){
        return -1;
    }
    public void tickPermaCast(){}

    /**Override if your stand can see through justice's fog, consider letting scoping moves see through it*/
    public boolean canSeeThroughFog(){return false;}

    /**Override to add disable config*/
    public boolean isStandEnabled(){
        return true;
    }

    @Override
    /**Stand related things that slow you down or speed you up, override and call super to make
     * any stand ability slow you down*/
    public float inputSpeedModifiers(float basis){
        StandUser standUser = ((StandUser) this.getSelf());
        if (standUser.roundabout$isDazed()) {
            basis = 0;
        } else if (!(this.getSelf().getVehicle() != null && this.getSelf().getControlledVehicle() == null) &&
                (standUser.roundabout$isGuarding() && this.getSelf().getVehicle() == null)) {
            basis*=0.3f;
        } else if (this.isBarrageAttacking() || standUser.roundabout$isClashing()) {
            basis*=0.2f;
        } else if (this.isBarrageCharging()) {
            basis*=0.5f;
        }
        if (impactSlowdown > -1) {
            basis = 0f;
        }
        return basis;
    }
    @Override
    /**Similar to the above function, but prevents the additional velocity carried over from
     * sprint jumping if made to return true, override and call super*/
    public boolean cancelSprintJump(){
        return this.isBarraging();
    }

    public int getJumpHeightAddon() {return 0;}

    /**If a power can be interrupted, that means you can hit the person using the power to cancel it,
     * like when someone charging a barrage gets their barrage canceled to damage*/
    public boolean canInterruptPower(){
        return false;
    }

    /**Probably will only apply to magician's red but leaving it in here just in case*/
    public boolean canLightFurnace(){
        return false;
    }

    /**This value prevents you from resummoning/blocking to cheese the 3 hit combo's last hit faster*/

    public int getMobRecoilTime(){
        return -30;
    }


    /**Guard + Attack to use a barrage*/
    public void buttonInputBarrage(boolean keyIsDown, Options options){
        if (keyIsDown) {
            if (this.getAttackTime() >= this.getAttackTimeMax() ||
                    (this.getActivePowerPhase() != this.getActivePowerPhaseMax())) {
                this.tryPower(PowerIndex.BARRAGE_CHARGE, true);
                tryPowerPacket(PowerIndex.BARRAGE_CHARGE);
            }
        }
    }

    /**This gets set to true when you begin using a forward barrage, not many stands will use this mechanic likely*/
    public boolean forwardBarrage = false;

    /**If the stand has you in a state where you cannot collide with entities at all, mark this*/
    public boolean cancelCollision(Entity et) {
        return false;
    }

    /**The Guard Variation is prioritized over this for most stands but it may find niche uses*/
    public void buttonInputUse(boolean keyIsDown, Options options) {
        if (keyIsDown) {
        }
    }




    /**Returns if the stand is in control/pilot mode right now*/
    public boolean isPiloting(){
        return false;
    }
    /**Returns the stand entity that is going to be controlled, you most likely will not need to override this
     * unless you are doing a multi stand type like bad company*/
    public StandEntity getPilotingStand(){
        return getStandEntity(this.self);
    }
    /**If the passed in entity id matches that of getPilotingStand (consider passing in that function directly),
     * sets you to pilot the stand. Pass in 0, -1, etc to cancel pilot mode.*/
    public void setPiloting(int ID){
    }
    /**Check the justice override, this is good for matching the camera up well to the piloting entity clientside*/
    public void synchToCamera(){
    }
    /**Override to add controls to your pilot mode*/
    public void pilotStandControls(KeyboardPilotInput kpi, LivingEntity entity){
    }
    /**What happens when you left click while in pilot mode, exists clientside like poweractivate*/
    public void pilotInputAttack(){
    }
    /**What happens when you left click while in pilot mode, exists clientside like poweractivate*/
    public boolean pilotInputInteract(){
        return false;
    }
    /**return 1 for hard distance calcs, return 2 for soft/performant cubelike distance calcs*/
    public int getPilotMode(){
        return 2;
    }
    /**How far does pilot mode travel*/
    public int getMaxPilotRange(){
        return 100;
    }


    /**How much bonus oxygen does the stand provide? Might be useful for stands that are more water focused,
     * if it makes sense. Currently only applies to The World.*/
    public void setAirAmount(int airAmount){}
    public int getAirAmount(){
        return -1;
    }
    public int getMaxAirAmount(){
        return ClientNetworking.getAppropriateConfig().theWorldSettings.oxygenTankAdditionalTicks;
    }




    /**The manner in which your powers tick when you are being time stopped. Override this if the stand acts differently.
     * By technicality, you should still tick sounds.*/
    public void timeTick(){
        if (this.getSelf().level().isClientSide) {
            this.tickSounds();
        } else {

            softenTicks++;
        }
    }
    // Soften ticks are a mechanic to make damage dealt after TS from
    // certain sources less bad
    public int softenTicks = 0;

    /**A generic function which sends a float corresponding with an active power via packets to the client from the
     *  server or vice versa. An example of its usage is sending the time left on TS in the world stand via
     *  overriding this method and sending a packet*/
    public void updatePowerFloat(byte activePower, float data){
    }

    /**A generic function which sends an int corresponding with an active power via packets to the client from the
     *  server or vice versa. An example of its usage is sending the time left on TS in the world stand via
     *  overriding this method and sending a packet*/
    public void updatePowerInt(byte activePower, int data){
    }



    public void updateIntMove(int in){
    }

    /**make anything apply suspendguard to make it cancel guard, override if you want
     * some other conditions to suspend your stand guard*/
    public boolean suspendGuard = false;
    public void updateGuard(boolean yeet){
        if (suspendGuard) {
            if (!yeet) {
                suspendGuard = false;
            }
        }
    }

    /**A specific packet makes this happen*/
    public void updateMove(float flot){
    }

    /**If you want something to happen when you spawn a projectile, this is your place.
     * For instance, you could make every shot out arrow be super thrown, or create a penalty for throwing
     * a knife*/
    public boolean onCreateProjectile(Projectile proj){
        return false;
    }

    /**When you deal damage, intercept or run code based off of it, or potentially cancel it*/
    public boolean interceptDamageDealtEvent(DamageSource $$0, float $$1, LivingEntity target){
        return false;
    }
    /**Same as above but happens later in the damage code after a hit is already confirmed*/
    public boolean interceptSuccessfulDamageDealtEvent(DamageSource $$0, float $$1, LivingEntity target){
        return false;
    }
    /**Similar to above but less strict on damage source and doesn't outright cancel*/
    public void onActuallyHurt(DamageSource $$0, float $$1){
    }
    /**When damage is dealt to you, intercept or run code based off of it, or potentially cancel it*/
    public boolean interceptDamageEvent(DamageSource $$0, float $$1){
        return false;
    }
    /**When you eat food, intercept or run code based off of it*/
    public void eatEffectIntercept(ItemStack $$0, Level $$1, LivingEntity $$2){
    }
    /**When you are about to be hit by a projectile, intercept or run code based off of it, or potentially cancel it
     * Currently it supports abstract arrows but this can be expanded*/
    public boolean dealWithProjectile(Entity ent, HitResult res){
        return false;
    }

    /**When your stand is summoned, if you want to do anything fancy particle wise or otherwise, override this*/
    public void playSummonEffects(boolean forced){
    }

    /**Stands that react to mobs setting aggro to them like hey ya and wonder of u will override this*/
    public void reactToAggro(Mob mob){}
    /**Can't really cancel this one, but happens when you place a block*/
    public void onPlaceBlock(ServerPlayer $$0, BlockPos $$1, ItemStack $$2){}
    /**Can't really cancel this one, but happens when you destroy a block, also it only applies to survival mode*/
    public void onDestroyBlock(Level $$0, Player $$1, BlockPos $$2, BlockState $$3, BlockEntity $$4, ItemStack $$5){
    }
    /**return true to cancel the onkill event*/
    public boolean onKilledEntity(ServerLevel $$0, LivingEntity $$1){
        return false;
    }

    public void deflectArrowsAndBullets(Entity ent){
        if (ent instanceof Projectile PE){
            IProjectileAccess ipa = (IProjectileAccess) PE;
            if (!ipa.roundabout$getIsDeflected()){
                if (PE instanceof RoundaboutBulletEntity) {
                    ((RoundaboutBulletEntity) PE).setSuperThrown(false);
                    PE.level().playSound(null, PE.blockPosition(), ModSounds.BULLET_RICOCHET_EVENT, SoundSource.PLAYERS, 1.0F, 1.0F);
                }
                ipa.roundabout$setIsDeflected(true);
                ent.setDeltaMovement(ent.getDeltaMovement().scale(-0.4));
                ent.setYRot(ent.getYRot() + 180.0F);
                ent.yRotO += 180.0F;
            }
        }
    }

    //((ServerWorld) this.self.getWorld()).spawnParticles(ParticleTypes.EXPLOSION,pointVec.x, pointVec.y, pointVec.z,
    //        1,0.0, 0.0, 0.0,1);


    /**Override and use this to integrate stand general damage configs easily*/
    public float multiplyPowerByStandConfigPlayers(float power){
        return power;
    }
    public float multiplyPowerByStandConfigMobs(float power){
        return power;
    }

    /**Override these methods to fine tune the attack strength of the stand*/
    public float getPunchStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return 1.75F;
        } else {
            return 5;
        }
    } public float getHeavyPunchStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return 2.5F;
        } else {
            return 6;
        }
    } public float getBarrageFinisherStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return 3;
        } else {
            return 8;
        }
    }
    public float getBarrageFinisherKnockback(){
        return 2.8F;
    }

    private float getClashBreakStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return 4;
        } else {
            return 10;
        }
    }

    public float getBarrageDamagePlayer(){
        return 9;
    }

    public float getBarrageDamageMob(){
        return 20;
    }
    public float getBarrageHitStrength(Entity entity){
        float barrageLength = this.getBarrageLength();
        float power;
        if (this.getReducedDamage(entity)){
            power = getBarrageDamagePlayer()/barrageLength;
        } else {
            power = getBarrageDamageMob()/barrageLength;
        }
        /*Barrage hits are incapable of killing their target until the last hit.*/
        if (entity instanceof LivingEntity){
            if (power >= ((LivingEntity) entity).getHealth() && ClientNetworking.getAppropriateConfig().generalStandSettings.barragesOnlyKillOnLastHit){
                if (entity instanceof Player) {
                    power = 0.00001F;
                } else {
                    power = 0F;
                }
            }
        }
        return power;
    }

    /***The distance above you the stand floats*/
    private float getYOffSet(LivingEntity stand){
        float yy = 0.1F;
        if (stand.isSwimming() || stand.isVisuallyCrawling() || stand.isFallFlying()) {
            yy -= 1;
        }
        return yy;
    }

    /**Releasing right click normally stops guarding but that's something you can adjust*/
    public boolean clickRelease(){
        return false;
    }

    /**The impact of a punch*/
    public void punchImpact(Entity entity){
    }

    /**If you need to temporarily save a boolean for an attack state use this*/
    public boolean moveStarted = false;

    /**If your stand is in a position to change abilities. By default, you are locked into clashing while clashing
     * unless you forfeit the clash by desummoning your stand*/
    public boolean canChangePower(int move, boolean forced){
        if (!this.isClashing() || move == PowerIndex.CLASH_CANCEL) {
            if ((this.activePower == PowerIndex.NONE || forced) &&
                    (!this.isDazed(this.self) || move == PowerIndex.BARRAGE_CLASH)) {
                return true;
            }
        }
        return false;
    }


    /** Tries to use an ability of your stand. If forced is true, the ability comes out no matter what.**/
    /** There is no reason for the function to be a boolean, that goes unused, so gradually we can convert this to
     * a void function*/

    @Override
    public boolean tryPower(int move, boolean forced){
        if (move != PowerIndex.NONE && this.self instanceof Mob && !hasStandEntity(this.self)){
            if (canSummonStand()) {
                ((StandUser) this.self).roundabout$setActive(true);
                ((StandUser) this.self).roundabout$summonStand(this.self.level(), true, false);
            }
        }

        if (!this.self.level().isClientSide && (this.isBarraging() || this.isClashing()) && (move != PowerIndex.BARRAGE && move != PowerIndex.BARRAGE_CLASH
        && move != PowerIndex.BARRAGE_CHARGE && move != PowerIndex.GUARD) && this.attackTimeDuring  > -1){
            this.stopSoundsIfNearby(SoundIndex.BARRAGE_SOUND_GROUP, 100,false);
        }

        if (canChangePower(move, forced)) {
                if (move == PowerIndex.NONE || move == PowerIndex.CLASH_CANCEL) {
                    this.setPowerNone();
                } else if (move == PowerIndex.ATTACK) {
                    this.setPowerAttack();
                } else if (move == PowerIndex.GUARD) {
                    this.setPowerGuard();
                } else if (move == PowerIndex.BARRAGE_CHARGE) {
                    this.setPowerBarrageCharge();
                } else if (move == PowerIndex.BARRAGE) {
                    this.setPowerBarrage();
                } else if (move == PowerIndex.BARRAGE_CLASH) {
                    this.setPowerClash();
                } else if (move == PowerIndex.SPECIAL) {
                    this.setPowerSpecial(move);
                } else if (move == PowerIndex.MOVEMENT) {
                    this.setPowerMovement(move);
                } else if (move == PowerIndex.SNEAK_MOVEMENT) {
                    this.setPowerSneakMovement(move);
                } else if (move == PowerIndex.MINING) {
                    this.setPowerMining(move);
                } else {
                    this.setPowerOther(move, this.getActivePower());
                }

        }
        return false;
    }


    public void handleStandAttack(Player player, Entity target){
    }

    public void handleStandAttack2(Player player, Entity target){
    }




    public boolean isUsingShield(LivingEntity entity) {
        if (entity.isUsingItem()) {
            InteractionHand hand = entity.getUsedItemHand();
            ItemStack item = entity.getItemInHand(hand);
            if (item.getItem() instanceof ShieldItem) {
                return true;
            }
        }
        return false;
    }

    @Override
    /**If eating or using items in general shouldn't cancel certain abilties, put them as exceptions here*/
    public boolean shouldReset(byte activeP){
        return ((this.self.isUsingItem() &&
                !(this.getActivePower() == PowerIndex.BARRAGE_CLASH)) || this.isDazed(this.self) || (((TimeStop)this.getSelf().level()).CanTimeStopEntity(this.getSelf())));
    }

    /** Items that combat mode can use. NOTE: maybe move to AbilityScapeBasis if needed
        Note that this doesn't necessarily apply to  rendering items. */
    public boolean canCombatModeUse(ItemStack stack) {
        return canCombatModeUse(stack.getItem());
    }
    public boolean canCombatModeUse(Item item) {
        return item.isEdible() || item instanceof HarpoonItem || item instanceof TridentItem
                || item instanceof KnifeItem;
    }



    public void xTryPower(byte index, boolean forced){
        ((StandUser) this.self).roundabout$tryPower(index,true);
    }


    public boolean setPowerGuard() {
        if (((StandUser)this.self).roundabout$getGuardBroken()) {
            animateStand(StandEntity.BROKEN_GUARD);
        } else {
            animateStand(StandEntity.BLOCK);
        }
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.GUARD);
        this.poseStand(OffsetIndex.GUARD);
        return true;
    }


    public boolean setPowerBarrageCharge() {
        return true;
    }

    public void setPowerBarrage() {
    }

    public int clashStarter = 0;




    /**For humanoid stands that have their own mining*/

    /**Adjust this function to enable the below minin functions, and intercept your mining when not holding
     * a mining tool*/
    public boolean isMiningStand() {
        return false;
    }

    /**Can you currently use the stand to mine if the above is true?*/
    public boolean canUseMiningStand() {
        return (isMiningStand() && (!(this.getSelf().getMainHandItem().getItem() instanceof DiggerItem ||
                this.getSelf().getMainHandItem().getItem() instanceof ShearsItem) || (this.getActivePower() == PowerIndex.MINING
                && !(this.getSelf().getMainHandItem().getItem() instanceof ShearsItem))
        ));
    }

    /**How fast does the block mine blocks that require pickaxes?*/
    public float getPickMiningSpeed() {
        return 5F;
    }
    /**How fast does the block mine blocks that require axes?*/
    public float getAxeMiningSpeed() {
        return 5F;
    }
    /**How fast does the block mine blocks that require swords like cobwebs?*/
    public float getSwordMiningSpeed() {
        return 5F;
    }
    /**How fast does the block mine blocks that require shovels?*/
    public float getShovelMiningSpeed() {
        return 5F;
    }
    /**A general multiplier to apply across the board*/
    public float getMiningMultiplier() {
        return 1F;
    }
    /**Override with config options for your stand to be able to mine blocks*/
    public int getMiningLevel() {
        return 0;
    }
    /**The amount of exp you gain from mining blocks*/
    public void gainExpFromSpecialMining(BlockState $$1, BlockPos $$2) {
        if (!($$1.getBlock() instanceof IceBlock) && !$$1.is(Blocks.PACKED_ICE) &&
                !($$1.getDestroySpeed(this.self.level(),$$2) < 0.1)) {
            if (Math.random() > 0.62) {
                addEXP(1);
            }
        }
    }
    /**Sets your current ability to stand mining*/
    public boolean setPowerMining(int lastMove) {
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.MINING);
        this.poseStand(OffsetIndex.FIXED_STYLE);
        animateStand(StandEntity.MINING_BARRAGE);
        return true;
    }


    /**exp from standard tool mining*/
    public void gainExpFromStandardMining(BlockState $$1, BlockPos $$2) {
    }


    /**Override to decide how much experience is needed to levelup in general*/
    public int getExpForLevelUp(int currentLevel){
        return 100;
    }
    /**The maximum level of this particular stand, use your own discretion it can be any number*/
    public byte getMaxLevel(){
        return 1;
    }
    /**Override this in general with leveling stands so you can display generic messages of what each level unlocks*/
    public void levelUp(){
        if (!this.getSelf().level().isClientSide()){
            ((ServerLevel) this.self.level()).sendParticles(ParticleTypes.END_ROD,
                    this.getSelf().getEyePosition().x, this.getSelf().getEyePosition().y, this.getSelf().getEyePosition().z,
                    20, 0.4, 0.4, 0.4, 0.4);
            this.self.level().playSound(null, this.self.blockPosition(), ModSounds.LEVELUP_EVENT, SoundSource.PLAYERS, 0.95F, (float) (0.8 + (Math.random() * 0.4)));
        }
    }

    public int getBarrageRecoilTime(){
        return ClientNetworking.getAppropriateConfig().
                generalStandSettings.barrageRecoilCooldown;
    }

    /**returns if you are using stand guard*/
    public boolean isGuarding(){
        return this.activePower == PowerIndex.GUARD;
    }

    public int getKickBarrageWindup(){
        return ClientNetworking.getAppropriateConfig().generalStandSettings.kickBarrageWindup;
    }
    public int getBarrageWindup(){
        return ClientNetworking.getAppropriateConfig().generalStandSettings.barrageWindup;
    }
    public int getBarrageLength(){
        return 60;
    }

    public boolean setPowerAttack(){
        return false;
    }






























    // -----------------------------------------------------------------------------------------
    // SOUND FUNCTIONS TO USE AND OVERRIDE
    // Sounds are represented by a byte, you get to define that byte within parameters
    // In effect we can cancel started sounds and play some sounds to stand users only
    // -----------------------------------------------------------------------------------------



    /**Barrage sound playing and canceling involve sending a byte in a packet, then reading it from here on
     * the client level. You can define your own sound bytes, try to start with id 70 onwards up to the byte limit.
     * (otherwise it may be the same byte as one of the below?)*/
    public SoundEvent getSoundFromByte(byte soundChoice){
        if (soundChoice == SoundIndex.BARRAGE_CHARGE_SOUND) {
            return this.getBarrageChargeSound();
        } else if (soundChoice == SoundIndex.GLAIVE_CHARGE) {
            return ModSounds.GLAIVE_CHARGE_EVENT;
        } else if (soundChoice == SoundIndex.REVOLVER_RELOAD) {
            return ModSounds.SNUBNOSE_RELOAD_EVENT;
        } else if (soundChoice == SoundIndex.SNIPER_RELOAD) {
            return ModSounds.JACKAL_RELOAD_EVENT;
        } else if (soundChoice == SoundIndex.COLT_RELOAD) {
            return ModSounds.COLT_RELOAD_EVENT;
        } else if (soundChoice == SoundIndex.TOMMY_RELOAD) {
            return ModSounds.TOMMY_RELOAD_EVENT;
        } else if (soundChoice == TIME_STOP_NOISE) {
            return ModSounds.TIME_STOP_STAR_PLATINUM_EVENT;
        } else if (soundChoice == TIME_STOP_NOISE_4) {
            return ModSounds.TIME_STOP_THE_WORLD_EVENT;
        } else if (soundChoice == TIME_STOP_NOISE_5) {
            return ModSounds.TWAU_TIMESTOP_EVENT;
        } else if (soundChoice == TIME_STOP_NOISE_7) {
            return ModSounds.OVA_LONG_TS_EVENT;
        } else if (soundChoice == TIME_STOP_NOISE_8) {
            return ModSounds.OVA_SP_TS_EVENT;
        } else if (soundChoice == TIME_STOP_NOISE_9) {
            return ModSounds.OVA_SHORT_TS_EVENT;
        } else if (soundChoice == TIME_STOP_NOISE_10) {
            return ModSounds.ARCADE_SHORT_TS_EVENT;
        } else if (soundChoice == TIME_STOP_NOISE_11) {
            return ModSounds.ARCADE_TIMESTOP_EVENT;
        } else if (soundChoice == TIME_STOP_NOISE_12) {
            return ModSounds.ARCADE_STAR_PLATINUM_SHORT_TS_EVENT;
        } else if (soundChoice == TIME_RESUME_NOISE){
            return ModSounds.TIME_RESUME_EVENT;
        } else if (soundChoice == TIME_STOP_NOISE_2) {
            return ModSounds.TIME_STOP_THE_WORLD2_EVENT;
        } else if (soundChoice == TIME_STOP_NOISE_3) {
            return ModSounds.TIME_STOP_THE_WORLD3_EVENT;
        } else if (soundChoice == SoundIndex.SPECIAL_MOVE_SOUND_2) {
            return ModSounds.TIME_RESUME_EVENT;
        } else if (soundChoice == TIME_RESUME_NOISE_2) {
            return ModSounds.OVA_TIME_RESUME_EVENT;
        } else if (soundChoice == TIME_RESUME_NOISE_3) {
            return ModSounds.ARCADE_TIME_RESUME_EVENT;
        } else if (soundChoice == SoundIndex.STAND_ARROW_CHARGE) {
            return ModSounds.STAND_ARROW_CHARGE_EVENT;
        } else if (soundChoice == SoundIndex.CACKLE) {
            return ModSounds.CACKLE_EVENT;
        } else if (soundChoice == SoundIndex.BLOOD_REGEN) {
            return ModSounds.BLOOD_REGEN_EVENT;
        }
        return null;
    }

    /**Some standard bytes for sound noises, stay clear of 40-62 as they exist universally*/
    public static final byte TIME_STOP_NOISE = 40;
    public static final byte TIME_STOP_NOISE_2 = TIME_STOP_NOISE+1;
    public static final byte TIME_STOP_NOISE_3 = TIME_STOP_NOISE+2;
    public static final byte TIME_STOP_NOISE_4 = TIME_STOP_NOISE+3;
    public static final byte TIME_STOP_NOISE_5 = TIME_STOP_NOISE+4;
    public static final byte TIME_STOP_NOISE_6 = TIME_STOP_NOISE+5;
    public static final byte TIME_STOP_NOISE_7 = TIME_STOP_NOISE+6;
    public static final byte TIME_STOP_NOISE_8 = TIME_STOP_NOISE+7;
    public static final byte TIME_STOP_NOISE_9 = TIME_STOP_NOISE+8;
    public static final byte TIME_STOP_NOISE_10 = TIME_STOP_NOISE+9;
    public static final byte TIME_STOP_NOISE_11 = TIME_STOP_NOISE+10;
    public static final byte TIME_STOP_NOISE_12 = TIME_STOP_NOISE+11;
    public static final byte TIME_STOP_TICKING = TIME_STOP_NOISE+16;
    public static final byte TIME_RESUME_NOISE = 60;
    public static final byte TIME_RESUME_NOISE_2 = 61;
    public static final byte TIME_RESUME_NOISE_3 = 62;

    public void playBarrageMissNoise(int hitNumber){
        if (!this.self.level().isClientSide()) {
            if (hitNumber%2==0) {
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.STAND_BARRAGE_MISS_EVENT, SoundSource.PLAYERS, 0.95F, (float) (0.8 + (Math.random() * 0.4)));
            }
        }
    }
    public void playBarrageNoise(int hitNumber, Entity entity){
        if (!this.self.level().isClientSide()) {
            if (hitNumber % 2 == 0) {
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.STAND_BARRAGE_HIT_EVENT, SoundSource.PLAYERS, 0.9F, (float) (0.9 + (Math.random() * 0.25)));
            }
        }
    }

    public void playBarrageEndNoise(float mod, Entity entity){
        if (!this.self.level().isClientSide()) {
            this.self.level().playSound(null, this.self.blockPosition(), ModSounds.STAND_BARRAGE_END_EVENT, SoundSource.PLAYERS, 0.95F+mod, 1f);
        }
    }
    public void playBarrageBlockEndNoise(float mod, Entity entity){
        if (!this.self.level().isClientSide()) {
            this.self.level().playSound(null, this.self.blockPosition(), ModSounds.STAND_BARRAGE_END_BLOCK_EVENT, SoundSource.PLAYERS, 0.88F+mod, 1.7f);
        }
    }
    public void playBarrageBlockNoise(){
        if (!this.self.level().isClientSide()) {
            this.self.level().playSound(null, this.self.blockPosition(), ModSounds.STAND_BARRAGE_BLOCK_EVENT, SoundSource.PLAYERS, 0.95F, (float) (0.8 + (Math.random() * 0.4)));
        }
    }

    public void runExtraSoundCode(byte soundChoice) {
    }

    public byte getSoundCancelingGroupByte(byte soundChoice) {
        if (soundChoice == SoundIndex.BARRAGE_CHARGE_SOUND) {
            return SoundIndex.BARRAGE_SOUND_GROUP;
        } else if (soundChoice <= SoundIndex.REVOLVER_RELOAD) {
            return SoundIndex.ITEM_GROUP;
        } else if (soundChoice <= SoundIndex.SNIPER_RELOAD) {
            return SoundIndex.ITEM_GROUP;
        } else if (soundChoice <= SoundIndex.COLT_RELOAD) {
            return SoundIndex.ITEM_GROUP;
        } else if (soundChoice <= SoundIndex.TOMMY_RELOAD) {
            return SoundIndex.ITEM_GROUP;
        } else if (soundChoice <= SoundIndex.GLAIVE_CHARGE) {
            return SoundIndex.ITEM_GROUP;
        }

        return soundChoice;
    }
    public float getSoundPitchFromByte(byte soundChoice){
        if (soundChoice == SoundIndex.BARRAGE_CHARGE_SOUND){
            return this.getBarrageChargePitch();
        } else {
            return 1F;
        }
    }
    public float getSoundVolumeFromByte(byte soundChoice){
        if (soundChoice == TIME_STOP_NOISE) {
            return 0.7f;
        } else if (soundChoice == SoundIndex.CACKLE) {
            return 120f;
        } else if (soundChoice == TIME_STOP_NOISE_4 || soundChoice == TIME_STOP_NOISE_5
                || soundChoice == TIME_STOP_NOISE_7
                || soundChoice == TIME_STOP_NOISE_8
                || soundChoice == TIME_STOP_NOISE_9) {
            return 0.7f;
        }
        return 1F;
    }
    protected Byte getSummonSound() {
        return -1;
    }

    public void playSummonSound() {
        if (this.self.isCrouching()){
            return;
        }
        playStandUserOnlySoundsIfNearby(this.getSummonSound(), 10, false,false);
    } //Plays the Summon sound. Happens when stand is summoned with summon key.

    public float getBarrageChargePitch(){
        return 1/((float) this.getBarrageWindup() /20);
    }

    /**Realistically, you only need to override this if you're canceling sounds*/
    public ResourceLocation getBarrageCryID(){
        return ModSounds.STAND_THEWORLD_MUDA1_SOUND_ID;
    }
    public SoundEvent getBarrageChargeSound(){
        return ModSounds.STAND_BARRAGE_WINDUP_EVENT;
    }

    public ResourceLocation getBarrageChargeID(){
        return ModSounds.STAND_BARRAGE_WINDUP_ID;
    }
    public Byte getLastHitSound(){
        return SoundIndex.NO_SOUND;
    }

    public ResourceLocation getLastHitID(){
        return ModSounds.STAND_THEWORLD_MUDA3_SOUND_ID;
    }

    public ResourceLocation getSoundID(byte soundNumber){
        if (soundNumber == SoundIndex.BARRAGE_CRY_SOUND) {
            return getBarrageCryID();
        } else if (soundNumber == SoundIndex.BARRAGE_CHARGE_SOUND) {
            return getBarrageChargeID();
        }
        return null;
    }



    public void playBarrageClashSound(){
        if (!this.self.level().isClientSide()) {
        }
    }
    public void playBarrageChargeSound(){
        if (!this.self.level().isClientSide()) {
            SoundEvent barrageChargeSound = this.getBarrageChargeSound();
            if (barrageChargeSound != null) {
                playSoundsIfNearby(SoundIndex.BARRAGE_CHARGE_SOUND, 27, false);
            }
        }
    }

































    // -----------------------------------------------------------------------------------------
    // UI/CLIENT, + SKIN FUNCTIONS TO OVERRIDE
    // Be conscious of where you use client functions and arguments since this class can't accidentally
    // call client functions on a server without crashing
    // -----------------------------------------------------------------------------------------


    /**The list of skins for your stand to cycle through, override, see examples, the server iterates through
     * this as well*/
    public List<Byte> getSkinList(){
        List<Byte> $$1 = Lists.newArrayList();
        $$1.add((byte) 0);
        return $$1;
    }

    /**The name for your stand's skins to display in the power inventory HUD*/
    public Component getSkinName(byte skinId){
        return Component.empty();
    }

    /**Override this to decide which skins the stand rolls for on an entity, this is done server sided*/
    public void rollSkin(){}

    /**The idle pose title for your stand to display in the power inventory HUD*/
    public List<Byte> getPosList(){
        List<Byte> $$1 = Lists.newArrayList();
        $$1.add((byte) 0);
        $$1.add((byte) 1);
        return $$1;
    }
    /**The name for your stand's poses to display in the power inventory HUD*/
    public Component getPosName(byte posID){
        if (posID == 1){
            return Component.translatable(  "idle.roundabout.battle");
        } else if (posID == 2){
            return Component.translatable(  "idle.roundabout.floaty");
        } else if (posID == 3){
            return Component.translatable(  "idle.roundabout.star_platinum");
        } else if (posID == 4){
            return Component.translatable(  "idle.roundabout.arms_only");
        } else {
            return Component.translatable(  "idle.roundabout.passive");
        }
    }


    /**In the power inventory, the stand that displays is the one that exists while your powers are active.
     * But if no stand is out, then it can generate a fake stand. Override to return true like survivor for
     * a fake stand to render.*/
    public boolean returnFakeStandForHud(){
        return !hasStandActive(self);
    }

    /**if the above is true, override this to actually create a fake stand for the power inventory display.*/
    public StandEntity getStandForHUDIfFake(){
        if (displayStand == null){
            displayStand = getNewStandEntity();
        }
        if (displayStand != null) {
            if (this.self instanceof Player PL && ((IPlayerEntity) PL).roundabout$getStandSkin() != displayStand.getSkin()) {
                displayStand = getNewStandEntity();
            }
        }
        if (displayStand != null) {
            displayStand.setSkin(((StandUser) self).roundabout$getStandSkin());
            displayStand.setAnimation(((StandUser) self).roundabout$getStandAnimation());
            displayStand.setIdleAnimation(((StandUser) self).roundabout$getIdlePos());
            displayStand.tickCount = self.tickCount;
            displayStand.setUser(self);
            displayStand.setupAnimationStates();
        }
        return displayStand;
    }
    public StandEntity displayStand = null;

    /**If the powers inventory should render the player instead*/
    public boolean rendersPlayer(){
        return false;
    }

    /**Not meant to be overidden necessarily. use the above functions to make a different stand appear*/
    public StandEntity getStandForHUD(){
        if (returnFakeStandForHud())
            return getStandForHUDIfFake();
        return getStandUserSelf().roundabout$getStand();
    }

    /**how big your stand is in the power inventory*/
    public int getDisplayPowerInventoryScale(){
        return 30;
    }
    /**how high up your stand is in the power inventory, negative = higher, positive = lower*/
    public int getDisplayPowerInventoryYOffset(){
        return 0;
    }

    /**Override if you are in the middle of making a stand, check other examples of overrides.
     * Basically this makes the stand disc display that the stand is WIP and the current
     * dev working on it*/
    public Component ifWipListDevStatus(){
        return null;
    }
    public Component ifWipListDev(){
        return null;
    }
    public boolean isWip(){
        return false;
    }






























    // -----------------------------------------------------------------------------------------
    // Functions you will NOT need to override
    // Ones you might actually use edition
    // -----------------------------------------------------------------------------------------

    /**Is the stand user self in creative mode?*/
    public boolean getCreative(){
        return this.self instanceof Player PE && PE.isCreative();
    }

    /**changes the pose of the stand, as in the position offset. For instance, if a stand is floating by you, or
     * in front of you to block, or simply loose (on its own). Synchs if changed on the server.*/
    public void poseStand(byte r){
        StandEntity stand = getStandEntity(this.self);
        if (stand instanceof FollowingStandEntity FE){
            FE.setOffsetType(r);
        }
    }
    /**changes the animation of the stand, define your animations on the standentity and model/renderer.
     *  Synchs if changed on the server.*/
    public void animateStand(byte r){
        StandEntity stand = getStandEntity(this.self);
        if (Objects.nonNull(stand)){
            stand.setAnimation(r);
        }
    }
    /**returns the current animation.*/
    public byte getAnimation(){
        StandEntity stand = getStandEntity(this.self);
        if (Objects.nonNull(stand)){
            return stand.getAnimation();
        }
        return -1;
    }


    /**Sets your active power to nothing*/
    @Override
    public boolean setPowerNone(){
        this.attackTimeDuring = -1;
        this.setActivePower(PowerIndex.NONE);
        poseStand(OffsetIndex.FOLLOW);
        animateStand(StandEntity.IDLE);
        return true;
    }



    /**Call this to verify your stand is leveled enough to use a move*/
    public boolean canExecuteMoveWithLevel(int minLevel){
        if (!ClientNetworking.getAppropriateConfig().standLevelingSettings.enableStandLeveling) {
            return true;
        }

        if (this.getSelf() instanceof Player pl){
            if (((IPlayerEntity)pl).roundabout$getStandLevel() >= minLevel || hasGoldenDisc() ||
                    pl.isCreative()){
                return true;
            }
            return false;
        }
        return true;
    }






    /**This function ensures the client sending attack packets is ONLY the player using the attack, prevents double attacking*/
    public boolean isPacketPlayer(){
        if (this.self.level().isClientSide) {
            Minecraft mc = Minecraft.getInstance();
            return mc.player != null && mc.player.getId() == this.self.getId();
        }
        return false;
    }


    /**Functions to use to make your abilities grant stand exp*/
    public void addEXP(int amt, LivingEntity ent){
        if (!((StandUser)ent).roundabout$getStandDisc().isEmpty() && (ent instanceof Monster ||
                ent instanceof NeutralMob)){
            if (ent.getMaxHealth() >= 100){
                amt = (int)( amt*0.5);
            }
            addEXP(amt*5);
        } else {
            if (ent.getMaxHealth() >= 100){
                amt = (int)( amt*0.5);
            }
            addEXP(amt);
        }
    }
    public void addEXP(int amt){
        if (this.getSelf() instanceof Player PE){
            ItemStack stack = ((StandUser) PE).roundabout$getStandDisc();
            if (!stack.isEmpty() && !(hasGoldenDisc())){
                IPlayerEntity ipe = ((IPlayerEntity) PE);
                ipe.roundabout$addStandExp(amt);
            }
        }
    }

    /**Attempts to safely place a block in the world, adheres to gamerules, adventure mode, and claims potentially*/
    @SuppressWarnings("deprecation")
    public boolean tryPlaceBlock(BlockPos pos){
        if (!this.self.level().isClientSide()) {
            BlockState state = this.getSelf().level().getBlockState(pos);
            if (state.isAir() || (state.canBeReplaced() && getIsGamemodeApproriateForGrief() && !state.liquid() &&
                    this.getSelf().level().getGameRules().getBoolean(ModGamerules.ROUNDABOUT_STAND_GRIEFING) &&
                    !((this.getSelf() instanceof Player &&
                            (((Player) this.getSelf()).blockActionRestricted(this.getSelf().level(), pos, ((ServerPlayer)
                                    this.getSelf()).gameMode.getGameModeForPlayer()))) ||
                            (this.getSelf() instanceof Player && !this.getSelf().level().mayInteract(((Player) this.getSelf()), pos))))) {
                if(this.self instanceof Player p){
                    if(MainUtil.canPlaceOnClaim(p, new BlockHitResult(new Vec3(pos.relative(Direction.DOWN).getX(),pos.relative(Direction.DOWN).getY(),pos.relative(Direction.DOWN).getZ()), Direction.UP,pos.relative(Direction.DOWN),false))){
                        return true;
                    }
                    else{
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
    /**The checks in question, has a mainutil counterpart*/
    public boolean getIsGamemodeApproriateForGrief(){
        if ((!(this.getSelf() instanceof Player) || (((ServerPlayer) this.getSelf()).gameMode.getGameModeForPlayer() != GameType.SPECTATOR
                && ((ServerPlayer) this.getSelf()).gameMode.getGameModeForPlayer() != GameType.ADVENTURE))
                && this.getSelf().level().getGameRules().getBoolean(ModGamerules.ROUNDABOUT_STAND_GRIEFING)) {
            return true;
        }
        return false;
    }


    /**Inflict knockback*/
    public void takeKnockbackWithY(Entity entity, double strength, double x, double y, double z) {

        if (entity instanceof LivingEntity && (strength *= (float) (1.0 - ((LivingEntity)entity).getAttributeValue(Attributes.KNOCKBACK_RESISTANCE))) <= 0.0) {
            return;
        }
        if (MainUtil.isKnockbackImmune(entity)){
            return;
        }
        entity.hurtMarked = true;
        Vec3 vec3d2 = new Vec3(x, y, z).normalize().scale(strength);
        entity.setDeltaMovement(- vec3d2.x,
                -vec3d2.y,
                - vec3d2.z);
        entity.hasImpulse = true;
    }


    /**Inflict knockback with push upwards*/
    public void takeKnockbackUp(Entity entity, double strength) {
        if (entity instanceof LivingEntity && (strength *= (float) (1.0 - ((LivingEntity)entity).getAttributeValue(Attributes.KNOCKBACK_RESISTANCE))) <= 0.0) {
            return;
        }
        if (MainUtil.isKnockbackImmune(entity)){
            return;
        }
        entity.hasImpulse = true;

        Vec3 vec3d2 = new Vec3(0, strength, 0).normalize().scale(strength);
        entity.setDeltaMovement(vec3d2.x,
                vec3d2.y,
                vec3d2.z);
    }

    /**Look at where these are called for context*/
    public void takeDeterminedKnockbackWithY(LivingEntity user, Entity target, float knockbackStrength){
        float xRot; if (!target.onGround()){xRot=user.getXRot();} else {xRot = -15;}
        this.takeKnockbackWithY(target, knockbackStrength,
                Mth.sin(user.getYRot() * ((float) Math.PI / 180)),
                Mth.sin(xRot * ((float) Math.PI / 180)),
                -Mth.cos(user.getYRot() * ((float) Math.PI / 180)));

    }

    public Vec3 defaultKnockbackAngle(LivingEntity user,Entity target,float knockbackStrength) {
        Vec3 vec3d2 = new Vec3(Mth.sin(
                user.getYRot() * ((float) Math.PI / 180)),
                0,
                -Mth.cos(user.getYRot() * ((float) Math.PI / 180))).normalize().scale(knockbackStrength);
        vec3d2 = new Vec3(-vec3d2.x,
                target.onGround() ? 0.28 : 0,
                -vec3d2.z);
        return vec3d2;
    }

    public void takeDeterminedKnockback(LivingEntity user, Entity target, float knockbackStrength){

        if (target instanceof LivingEntity && (knockbackStrength *= (float) (1.0 - ((LivingEntity)target).getAttributeValue(Attributes.KNOCKBACK_RESISTANCE))) <= 0.0) {
            return;
        }

        if (MainUtil.isKnockbackImmune(target)){
            return;
        }
        Vec3 vec3d2 = defaultKnockbackAngle(user,target,knockbackStrength);
        target.setDeltaMovement(vec3d2);
        target.hasImpulse = true;
    }



    /**Multiply damage by this to add compatibility for stand levelup config*/
    public float levelupDamageMod(float damage){
        int percent = ClientNetworking.getAppropriateConfig().
                standLevelingSettings.bonusStandDmgByMaxLevel;

        if (percent > 0  && this.self instanceof Player PE && this.getMaxLevel() >= 1){
            int maxlevel = getMaxLevel();
            if (maxlevel > 1) {
                int level = ((IPlayerEntity) PE).roundabout$getStandLevel();
                ItemStack sdisc = ((StandUser)PE).roundabout$getStandDisc();
                if (hasGoldenDisc()){
                    level =maxlevel;
                }
                damage *= (float) (1 +
                        ((((maxlevel - 1) - ((float) ((maxlevel - 1) - (level - 1)))) / (maxlevel - 1) *
                                (0.01 * percent))));
            }
        }
        return damage;
    }



    @Override
    public ResourceLocation getIconYes(int slot){
        return StandIcons.SQUARE_ICON;
    }

    public BlockHitResult getAheadVec(float distOut){
        Vec3 vec3d = this.self.getEyePosition(1);
        Vec3 vec3d2 = this.self.getViewVector(1);
        return this.getSelf().level().clip(new ClipContext(vec3d, vec3d.add(vec3d2.x * distOut,
                vec3d2.y * distOut, vec3d2.z * distOut), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE,
                this.getSelf()));
    }





    /**Use this to multiply the exp needed to levelup for the config option*/
    public float getLevelMultiplier(){
        return (float) (ClientNetworking.getAppropriateConfig().standLevelingSettings.standExperienceNeededForLevelupMultiplier *0.01);
    }

    /**Send a packet to nearby players, the string is the packet identifier, s2c*/
    public final void spreadRadialClientPacket(double range, boolean skipSelf, String packet) {
        if (!this.self.level().isClientSide) {
            ServerLevel serverWorld = ((ServerLevel) this.self.level());
            Vec3 userLocation = new Vec3(this.self.getX(),  this.self.getY(), this.self.getZ());
            for (int j = 0; j < serverWorld.players().size(); ++j) {
                ServerPlayer serverPlayerEntity = ((ServerLevel) this.self.level()).players().get(j);

                if (((ServerLevel) serverPlayerEntity.level()) != serverWorld) {
                    continue;
                }
                if (skipSelf && this.self.is(serverPlayerEntity)) {
                    continue;
                }

                BlockPos blockPos = serverPlayerEntity.blockPosition();
                if (blockPos.closerToCenterThan(userLocation, range)) {
                    ModMessageEvents.sendToPlayer((ServerPlayer)serverPlayerEntity, packet);
                }
            }
        }
    }
    public final void spreadRadialClientPacket(double range, boolean skipSelf, String packet, Object... vargs) {
        if (!this.self.level().isClientSide) {
            ServerLevel serverWorld = ((ServerLevel) this.self.level());
            Vec3 userLocation = new Vec3(this.self.getX(),  this.self.getY(), this.self.getZ());
            for (int j = 0; j < serverWorld.players().size(); ++j) {
                ServerPlayer serverPlayerEntity = ((ServerLevel) this.self.level()).players().get(j);

                if (((ServerLevel) serverPlayerEntity.level()) != serverWorld) {
                    continue;
                }
                if (skipSelf && this.self.is(serverPlayerEntity)) {
                    continue;
                }

                BlockPos blockPos = serverPlayerEntity.blockPosition();
                if (blockPos.closerToCenterThan(userLocation, range)) {
                    ModMessageEvents.sendToPlayer((ServerPlayer)serverPlayerEntity, packet,vargs);
                }
            }
        }
    }




























    // -----------------------------------------------------------------------------------------
    // Functions you will NOT need to override
    // And will never be needed by you
    // BASICALLY YOU CAN IGNORE EVERYTHING HERE
    // -----------------------------------------------------------------------------------------

    public boolean isWorthinessType(LivingEntity LE){
        if (worthinessType() == HUMANOID_WORTHY){
            return MainUtil.isHumanoid(LE);
        }
        return true;
    }



    /**Preloads guard points*/
    public StandPowers generateStandPowersPre(LivingEntity entity){
        ((StandUser)entity).roundabout$setGuardPoints(getMaxGuardPoints());
        StandPowers powers = generateStandPowers(entity);

        // If the stand powers are enabled, return them. Otherwise, return empty powers.
        return (powers.isStandEnabled()) ? powers : new StandPowers(entity);
    }


    /**Sets the active power to clashing*/
    public boolean setPowerClash() {
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.BARRAGE_CLASH);
        this.poseStand(OffsetIndex.LOOSE);
        this.setClashProgress(0f);
        this.clashIncrement = 0;
        this.clashMod = (int) (Math.round(Math.random()*8));
        animateStand(StandEntity.BARRAGE);

        if (this.self instanceof Player && !this.self.level().isClientSide) {
            ((ServerPlayer) this.self).displayClientMessage(Component.translatable("text.roundabout.barrage_clash"), true);
        }
        return true;
        //playBarrageGuardSound();
    }

    /**Initiates a stand barrage clash. This code should probably not be overridden, it is a very mutual event*/
    public void initiateClash(Entity entity){
        ((StandUser) entity).roundabout$getStandPowers().setClashOp(this.self);
        ((StandUser) this.self).roundabout$getStandPowers().setClashOp((LivingEntity) entity);
        this.clashStarter = 0;
        ((StandUser) entity).roundabout$getStandPowers().clashStarter = 1;

        ((StandUser) entity).roundabout$tryPower(PowerIndex.BARRAGE_CLASH, true);
        ((StandUser) self).roundabout$tryPower(PowerIndex.BARRAGE_CLASH, true);

        LivingEntity standEntity = ((StandUser) entity).roundabout$getStand();
        LivingEntity standSelf = ((StandUser) self).roundabout$getStand();

        if (standEntity != null && standSelf != null){
            ((StandUser) entity).roundabout$getStandPowers().playBarrageClashSound();
            ((StandUser) this.self).roundabout$getStandPowers().playBarrageClashSound();
            Vec3 CenterPoint = entity.position().add(self.position()).scale(0.5);

            Vec3 entityPoint = offsetBarrageVector(
                    CenterPoint.subtract(((CenterPoint.subtract(entity.position())).normalize()).scale(0.95)),
                    getLookAtEntityYaw(entity,self));


            Vec3 selfPoint = offsetBarrageVector(
                    CenterPoint.subtract(((CenterPoint.subtract(self.position())).normalize()).scale(0.95)),
                    getLookAtEntityYaw(self,entity));

            standEntity.setPosRaw(entityPoint.x(),entityPoint.y()+getYOffSet(standEntity),entityPoint.z());

            Direction gdir = ((IGravityEntity)this.self).roundabout$getGravityDirection();
            Vec2 grot = new Vec2(getLookAtEntityYaw(standEntity,standSelf),
                    getLookAtEntityPitch(standEntity,standSelf)
            );
            grot =  RotationUtil.rotWorldToPlayer(grot,gdir);

            standEntity.setXRot(grot.y);
            standEntity.setYRot(grot.x);

            grot = new Vec2(getLookAtEntityYaw(standSelf,standEntity),
                    getLookAtEntityPitch(standSelf,standEntity)
            );
            grot =  RotationUtil.rotWorldToPlayer(grot,gdir);

            standSelf.setPosRaw(selfPoint.x(),selfPoint.y()+getYOffSet(standSelf),selfPoint.z());
            standSelf.setXRot(grot.y);
            standSelf.setYRot(grot.x);

        }
    }

    /**The relative position of a stand compared to yours and your opponent's in a barrage clash*/
    private Vec3 offsetBarrageVector(Vec3 vec3d, float yaw){
        Vec3 vec3d2 = DamageHandler.getRotationVector(0, yaw+ 90);
        return vec3d.add(vec3d2.x*0.3, 0, vec3d2.z*0.3);
    }

    /**Stand barrage results*/
    public void breakClash(LivingEntity winner, LivingEntity loser){
        if (StandDamageEntityAttack(loser, this.getClashBreakStrength(loser), 0.0001F, winner)) {
            ((StandUser)winner).roundabout$getStandPowers().stopSoundsIfNearby(SoundIndex.BARRAGE_SOUND_GROUP, 100,false);
            ((StandUser)loser).roundabout$getStandPowers().stopSoundsIfNearby(SoundIndex.BARRAGE_SOUND_GROUP, 100,false);
            ((StandUser)winner).roundabout$getStandPowers().playBarrageEndNoise(0, loser);
            this.takeDeterminedKnockbackWithY(winner, loser, this.getBarrageFinisherKnockback());
            ((StandUser)winner).roundabout$getStandPowers().animateStand(StandEntity.BARRAGE_FINISHER);
            ((StandUser)loser).roundabout$tryPower(PowerIndex.NONE,true);
        }
    }
    public void TieClash(LivingEntity user1, LivingEntity user2){
        ((StandUser)user1).roundabout$getStandPowers().stopSoundsIfNearby(SoundIndex.BARRAGE_SOUND_GROUP, 100,false);
        ((StandUser)user2).roundabout$getStandPowers().stopSoundsIfNearby(SoundIndex.BARRAGE_SOUND_GROUP, 100,false);
        ((StandUser)user1).roundabout$getStandPowers().playBarrageEndNoise(0F,user2);
        ((StandUser)user2).roundabout$getStandPowers().playBarrageEndNoise(-0.05F,user1);

        user1.hurtMarked = true;
        user2.hurtMarked = true;
        user1.knockback(0.55f,user2.getX()-user1.getX(), user2.getZ()-user1.getZ());
        user2.knockback(0.55f,user1.getX()-user2.getX(), user1.getZ()-user2.getZ());
    }

    public void updateClashing(){
        if (this.getStandEntity(this.self) != null) {
            //Roundabout.LOGGER.info("3 " + this.getStandEntity(this.self).getPitch() + " " + this.getStandEntity(this.self).getYaw());
        }
        LivingEntity entity = this.getClashOp();
        if (entity != null && entity.isAlive() && this.self.isAlive()) {
            if (this.attackTimeDuring <= 60) {

                /*Rotation has to be set actively by both client and server,
                 * because serverPitch and serverYaw are inconsistent, client overwrites stand stuff sometimes*/
                LivingEntity standEntity = ((StandUser) entity).roundabout$getStand();
                LivingEntity standSelf = ((StandUser) self).roundabout$getStand();
                if (standSelf != null && standEntity != null) {
                    if (!this.self.level().isClientSide) {
                        standSelf.setXRot(getLookAtEntityPitch(standSelf, standEntity));
                        standSelf.setYRot(getLookAtEntityYaw(standSelf, standEntity));
                        standEntity.setXRot(getLookAtEntityPitch(standEntity, standSelf));
                        standEntity.setYRot(getLookAtEntityYaw(standEntity, standSelf));
                    }
                }


                if (!(this.self instanceof Player)) {
                    this.RoundaboutEnemyClash();
                }
                if (!this.self.level().isClientSide) {

                    if ((this.getClashDone() && ((StandUser) entity).roundabout$getStandPowers().getClashDone())
                            || !PowerTypes.hasStandActive(self) || !PowerTypes.hasStandActive(entity)) {
                        this.updateClashing2();
                    } else {
                        playBarrageNoise(this.attackTimeDuring+ clashStarter, entity);
                    }
                }
            } else {
                if (!this.self.level().isClientSide) {
                    this.updateClashing2();
                }
            }
        } else {
            endClash();
        }
    }

    public void endClash(){
        if (!this.self.level().isClientSide) {
            ((StandUser) this.self).roundabout$tryPower(PowerIndex.CLASH_CANCEL, true);
            LivingEntity entity = this.getClashOp();
            if (entity != null){
                ((StandUser) this.self).roundabout$tryPower(PowerIndex.CLASH_CANCEL, true);
            }
        }
    }


    private void updateClashing2(){
        if (this.getClashOp() != null) {
            boolean thisActive = PowerTypes.hasStandActive(self);
            boolean opActive = PowerTypes.hasStandActive(this.getClashOp());
            if (thisActive && !opActive){
                breakClash(this.self, this.getClashOp());
            } else if (!thisActive && opActive){
                breakClash(this.getClashOp(), this.self);
            } else if (thisActive && opActive){
                if ((this.getClashProgress() == ((StandUser) this.getClashOp()).roundabout$getStandPowers().getClashProgress())) {
                    TieClash(this.self, this.getClashOp());
                } else if (this.getClashProgress() > ((StandUser) this.getClashOp()).roundabout$getStandPowers().getClashProgress()) {
                    breakClash(this.self, this.getClashOp());
                } else {
                    breakClash(this.getClashOp(), this.self);
                }
            }
            ((StandUser) this.self).roundabout$setAttackTimeDuring(-10);
            ((StandUser) this.getClashOp()).roundabout$setAttackTimeDuring(-10);
            ((StandUser) this.self).roundabout$getStandPowers().syncActivePower();
            ((StandUser) this.getClashOp()).roundabout$getStandPowers().syncActivePower();
        }
    }

    /**ClashDone is a value that makes you lock in your barrage when you are done barraging**/
    public boolean clashDone = false;
    public boolean getClashDone(){
        return this.clashDone;
    } public void setClashDone(boolean clashDone){
        this.clashDone = clashDone;
    }
    public float clashProgress = 0.0f;
    private float clashOpProgress = 0.0f;
    /**Clash Op is the opponent you are clashing with*/
    @Nullable
    private LivingEntity clashOp;
    public @Nullable LivingEntity getClashOp() {
        return this.clashOp;
    }
    public void setClashOp(@Nullable LivingEntity clashOp) {
        this.clashOp = clashOp;
    }
    public float getClashOpProgress(){
        return this.clashOpProgress;
    }
    public void setClashOpProgress(float clashOpProgress1) {
        this.clashOpProgress = clashOpProgress1;
    }
    public float getClashProgress(){
        return this.clashProgress;
    }
    public void setClashProgress(float clashProgress1){
        this.clashProgress = clashProgress1;
        if (!this.self.level().isClientSide && this.clashOp != null && this.clashOp instanceof ServerPlayer SP){
            S2CPacketUtil.updateBarrageClashS2C(SP, this.self.getId(), this.clashProgress);
        }
    }

    public boolean isClashing(){
        return this.activePower == PowerIndex.BARRAGE_CLASH && this.attackTimeDuring > -1;
    }

    public boolean isBarrageCharging(){
        return (this.activePower == PowerIndex.BARRAGE_CHARGE);
    }
    public boolean isBarraging(){
        return (this.activePower == PowerIndex.BARRAGE || this.activePower == PowerIndex.BARRAGE_CHARGE);
    }
    public boolean isBarrageAttacking(){
        return this.activePower == PowerIndex.BARRAGE;
    }

    public void updateBarrageCharge(){
        if (this.attackTimeDuring >= this.getBarrageWindup()) {
            ((StandUser) this.self).roundabout$tryPower(PowerIndex.BARRAGE, true);
        }
    }
    public void updateBarrage(){
        if (this.attackTimeDuring == -2 && this.getSelf() instanceof Player) {
            ((StandUser) this.self).roundabout$tryPower(PowerIndex.GUARD, true);
        } else {
            if (this.attackTimeDuring > this.getBarrageLength()) {
                this.attackTimeDuring = -20;
            } else {
                if (this.attackTimeDuring > 0) {
                    this.setAttackTime((getBarrageRecoilTime() - 1) -
                            Math.round(((float) this.attackTimeDuring / this.getBarrageLength())
                                    * (getBarrageRecoilTime() - 1)));

                    standBarrageHit();
                }
            }
        }
    }

    /**Enemies randomize their clash power, up to on occasion the maximum for some forced at best ties*/
    private void RoundaboutEnemyClash(){
        if (this.isClashing()) {
            if (this.clashIncrement < 0) {
                ++this.clashIncrement;
                if (this.clashIncrement == 0) {
                    this.setClashProgress(0.0f);
                }
            }
            ++this.clashIncrement;
            if (this.clashIncrement < (6 + this.clashMod)){
                this.setClashProgress(this.clashIncrement < 10 ?
                        (float) this.clashIncrement * 0.1f : 0.8f + 2.0f / (float) (this.clashIncrement - 9) * 0.1f);
            } else {
                this.setClashDone(true);
            }

        }
    }

    private int clashIncrement =0;
    private int clashMod =0;


    /**While you can override this, it might be more sensible to just edit this base function,
     * also veeery conditional use canInterruptPower instead*/
    public boolean preCanInterruptPower(DamageSource sauce, Entity interrupter, boolean isStandDamage){
        if (ClientNetworking.getAppropriateConfig().generalStandSettings.spiritOutInterruption){
            if (sauce != null){
                if (interrupter instanceof LivingEntity LE){
                    StandUser user = ((StandUser) LE);
                    if (PowerTypes.hasStandActivelyEquipped(LE)){
                        if (!PowerTypes.hasStandActive(LE)){
                            return false;
                        }
                    }
                }
            }
        }

        boolean interrupt = false;
        if (interrupter != null){
            if (this.isBarraging() && ClientNetworking.getAppropriateConfig().generalStandSettings.barragesAreAlwaysInterruptable) {
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                return true;
            } else if (isStandDamage && ClientNetworking.getAppropriateConfig().generalStandSettings.standsInterruptSomeStandAttacks){
                interrupt = true;
            } else if (this instanceof TWAndSPSharedPowers && this.getActivePower() == PowerIndex.SPECIAL &&
                    ClientNetworking.getAppropriateConfig().timeStopSettings.timeStopIsAlwaysInterruptable){
                interrupt = true;
            } else if (interrupter instanceof Player && ClientNetworking.getAppropriateConfig().generalStandSettings.playersInterruptSomeStandAttacks){
                interrupt = true;
            } else if (interrupter instanceof Mob && ClientNetworking.getAppropriateConfig().generalStandSettings.mobsInterruptSomeStandAttacks){
                interrupt = true;
            }
        } else {
            interrupt = true;
        }

        if (interrupt){
            return canInterruptPower();
        } else {
            return false;
        }
    }

    @Override
    public void preButtonInput4(boolean keyIsDown, Options options){
        if (hasStandActive(this.getSelf()) && !this.isClashing()) {
            if (!((TimeStop)this.getSelf().level()).CanTimeStopEntity(this.getSelf())  && !this.getStandUserSelf().roundabout$isPossessed()   ) {
                ((StandUser) this.getSelf()).roundabout$setIdleTime(0);
                buttonInput4(keyIsDown, options);
            }
        }
    }
    @Override
    public void preButtonInput3(boolean keyIsDown, Options options){
        if (hasStandActive(this.getSelf()) && !this.isClashing()) {
            if (!((TimeStop)this.getSelf().level()).CanTimeStopEntity(this.getSelf())  && !this.getStandUserSelf().roundabout$isPossessed()   ) {
                ((StandUser) this.getSelf()).roundabout$setIdleTime(0);
                buttonInput3(keyIsDown, options);
            }
        }
    }

    @Override
    public void preButtonInput2(boolean keyIsDown, Options options){
        if (hasStandActive(this.getSelf()) && !this.isClashing()) {
            if (!((TimeStop)this.getSelf().level()).CanTimeStopEntity(this.getSelf())  && !this.getStandUserSelf().roundabout$isPossessed()   ) {
                ((StandUser) this.getSelf()).roundabout$setIdleTime(0);
                buttonInput2(keyIsDown, options);
            }
        }
    }

    @Override
    public void preButtonInput1(boolean keyIsDown, Options options){
        if (hasStandActive(this.getSelf()) && !this.isClashing()) {
            if (!((TimeStop)this.getSelf().level()).CanTimeStopEntity(this.getSelf())  && !this.getStandUserSelf().roundabout$isPossessed()   ) {
                ((StandUser) this.getSelf()).roundabout$setIdleTime(0);
                buttonInput1(keyIsDown, options);
            }
        }
    }




    public static final int zenith = 10;



    public void preCheckButtonInputAttack(boolean keyIsDown, Options options) {
        if (hasStandActive(this.getSelf()) && !this.isGuarding()) {
            buttonInputAttack(keyIsDown, options);
        }
    }
    public void preCheckButtonInputUse(boolean keyIsDown, Options options) {
        if (hasStandActive(this.getSelf())) {
            buttonInputUse(keyIsDown, options);
        }
    }
    public void preCheckButtonInputBarrage(boolean keyIsDown, Options options) {
        if (hasStandActive(this.getSelf())) {
            buttonInputBarrage(keyIsDown, options);
        }
    }
    public boolean preCheckButtonInputGuard(boolean keyIsDown, Options options) {
        if (hasStandActive(this.getSelf())) {
            return buttonInputGuard(keyIsDown, options);
        }
        return false;
    }



    /// called when clickRelease() cancels something
    public boolean onClickRelease() {return false;}

    /**Ticks through the overlays on your screen such as mandom's time rewind function*/
    public void tickOverlayTicks(){
        if (timeRewindOverlayTicks > -1) {
            timeRewindOverlayTicks++;
            if (timeRewindOverlayTicks >= (zenith*2)) {
                timeRewindOverlayTicks = -1;
            }
        }
    }
    public int timeRewindOverlayTicks = -1;

    public static final float maxOverlay = 0.45f;

    public float getOverlayFromOverlayTicks(float delta) {
        // Interpolated tick value with partial tick (delta)
        float ticks = timeRewindOverlayTicks + delta;

        // Compute how far from the peak (5) we are
        float distanceFromPeak = Math.abs(ticks - ((float)zenith));

        // Normalize (distance from 5 goes from 0 to 5)
        float normalized = 1.0f - (distanceFromPeak / ((float)zenith));

        // Clamp and scale to maxOverlay
        return Math.max(0.0f, Math.min(1.0f, normalized)) * maxOverlay;
    }



    public Vec3 getRandPos(Entity ent){
        Vec3 funnyVec = new Vec3(0,(ent.getBbHeight()*0.65),0);
        Direction gd = ((IGravityEntity)ent).roundabout$getGravityDirection();
        if (gd != Direction.DOWN){
            funnyVec = RotationUtil.vecPlayerToWorld(funnyVec,gd);
        }
        return new Vec3(
                ent.getRandomX(1)+funnyVec.x,
                getRandomY(ent,0.33)+funnyVec.y,
                ent.getRandomZ(1)+funnyVec.z
        );
    }

    public double getRandomY(Entity ent, double $$0) {
        return ent.getY((2.0 * Math.random() - 1.0) * $$0);
    }


    public SimpleParticleType getImpactParticle(){
        SimpleParticleType punchpart;
        float random = (float) (Math.random()*3);
        if (random > 2){
            punchpart = ModParticles.PUNCH_IMPACT_A;
        } else if (random > 1){
            punchpart = ModParticles.PUNCH_IMPACT_B;
        } else {
            punchpart = ModParticles.PUNCH_IMPACT_C;
        }
        return punchpart;
    }

    public void hitParticles(Entity entity){
        Vec3 vec = getRandPos(entity);
        ((ServerLevel) this.self.level()).sendParticles(
                getImpactParticle(),
                vec.x,vec.y,vec.z,
                1, 0.0, 0.0, 0.0, 1);
    }
    public void hitParticlesCenter(Entity entity){
        Vec3 funnyVec = new Vec3(0,(entity.getBbHeight()*0.65),0);
        Direction gd = ((IGravityEntity)entity).roundabout$getGravityDirection();
        if (gd != Direction.DOWN){
            funnyVec = RotationUtil.vecPlayerToWorld(funnyVec,gd);
        }
        ((ServerLevel) this.self.level()).sendParticles(
                getImpactParticle(),
                entity.getX()+funnyVec.x,entity.getY()+funnyVec.y,entity.getZ()+funnyVec.z,
                1, 0.0, 0.0, 0.0, 1);
    }

    /**If you override this for any reason, you should probably call the super(). Although SP and TW override
     * this, you can probably do better*/
    public void barrageImpact(Entity entity, int hitNumber){
        if (this.isBarrageAttacking()) {
            if (bonusBarrageConditions()) {
                boolean sideHit = false;
                if (hitNumber > 1000){
                    if (!(ClientNetworking.getAppropriateConfig().generalStandSettings.barrageHasAreaOfEffect)){
                        return;
                    }
                    hitNumber-=1000;
                    sideHit = true;
                }
                boolean lastHit = (hitNumber >= this.getBarrageLength());
                if (entity != null) {
                    if (entity instanceof LivingEntity && ((StandUser) entity).roundabout$isBarraging()
                            && ((StandUser) entity).roundabout$getAttackTimeDuring() > -1 && !(((TimeStop)this.getSelf().level()).CanTimeStopEntity(entity))  && !this.getStandUserSelf().roundabout$isPossessed()   ) {
                        initiateClash(entity);
                    } else {
                        hitParticles(entity);

                        float pow;
                        float knockbackStrength = 0;
                        /**By saving the velocity before hitting, we can let people approach barraging foes
                         * through shields.*/
                        Vec3 prevVelocity = entity.getDeltaMovement();
                        if (lastHit) {
                            pow = this.getBarrageFinisherStrength(entity);
                            knockbackStrength = this.getBarrageFinisherKnockback();
                        } else {
                            pow = this.getBarrageHitStrength(entity);
                            float mn = this.getBarrageLength() - hitNumber;
                            if (mn == 0) {
                                mn = 0.015F;
                            } else {
                                mn = ((0.015F / (mn)));
                            }
                            knockbackStrength = 0.014F - mn;
                        }

                        if (sideHit){
                            pow/=4;
                            knockbackStrength/=6;
                        }

                        if (StandRushDamageEntityAttack(entity, pow, 0.0001F, this.self)) {
                            if (entity instanceof LivingEntity LE) {
                                if (lastHit) {
                                    setDazed((LivingEntity) entity, (byte) 0);

                                    if (!sideHit) {
                                        ((StandUser)LE).roundabout$setDestructionTrailTicks(80);
                                        addEXP(8,LE);
                                        playBarrageEndNoise(0, entity);
                                    }
                                } else {
                                    setDazed((LivingEntity) entity, (byte) 3);
                                    if (!sideHit) {
                                        playBarrageNoise(hitNumber, entity);
                                    }
                                }
                            }
                            barrageImpact2(entity, lastHit, knockbackStrength);
                        } else {
                            if (lastHit) {
                                knockShield2(entity, 200);
                                if (!sideHit) {
                                    playBarrageBlockEndNoise(0, entity);
                                }
                            } else {
                                entity.setDeltaMovement(prevVelocity);
                                playBarrageBlockNoise();
                            }
                        }
                    }
                } else {
                    if (!sideHit) {
                        playBarrageMissNoise(hitNumber);
                    }
                }

                if (lastHit) {
                    animateStand(StandEntity.BARRAGE_FINISHER);
                    this.attackTimeDuring = -10;
                }
            } else {
                ((StandUser) this.self).roundabout$tryPower(PowerIndex.NONE, true);
            }
        } else {
            ((StandUser) this.self).roundabout$tryPower(PowerIndex.NONE, true);
        }
    }
    public void barrageImpact2(Entity entity, boolean lastHit, float knockbackStrength){
        if (entity instanceof LivingEntity){
            if (lastHit) {
                this.takeDeterminedKnockbackWithY(this.self, entity, knockbackStrength);
            } else {
                this.takeKnockbackUp(entity,knockbackStrength);
            }
        }
    }
    /**This happens every time a stand barrage hits, generally you dont want to override this unless
     * your stand's barrage operates very differently*/
    public void standBarrageHit(){
        if (this.self instanceof Player){
            if (isPacketPlayer()){
                List<Entity> listE = getTargetEntityList(this.self,-1);
                int id = -1;
                if (storeEnt != null){
                    id = storeEnt.getId();
                }
                C2SPacketUtil.standBarrageHitPacket(id, this.attackTimeDuring);
                if (!listE.isEmpty() && ClientNetworking.getAppropriateConfig().generalStandSettings.barrageHasAreaOfEffect){
                    for (int i = 0; i< listE.size(); i++){
                        if (!(storeEnt != null && listE.get(i).is(storeEnt))) {
                            if (!(listE.get(i) instanceof StandEntity) && listE.get(i).distanceTo(this.self) < 3.5) {
                                C2SPacketUtil.standBarrageHitPacket(listE.get(i).getId(), this.attackTimeDuring + 1000);
                            }
                        }
                    }
                }

                if (this.attackTimeDuring == this.getBarrageLength()){
                    this.attackTimeDuring = -10;
                }
            }
        } else {
            /*Caps how far out the barrage hit goes*/
            Entity targetEntity = getTargetEntity(this.self,-1);

            List<Entity> listE = getTargetEntityList(this.self,-1);
            barrageImpact(storeEnt, this.attackTimeDuring);
            if (!listE.isEmpty()){
                for (int i = 0; i< listE.size(); i++){
                    if (!(storeEnt != null && listE.get(i).is(storeEnt))) {
                        if (!(listE.get(i) instanceof StandEntity) && listE.get(i).distanceTo(this.self) < 3.5) {
                            barrageImpact(listE.get(i), this.attackTimeDuring + 1000);
                        }
                    }
                }
            }

        }

        findDeflectables();
    }
    public StandPowers(LivingEntity self) {
        super(self);
    }

    @Override
    public void baseTickPower(){
        if (this.self.level().isClientSide()){
            if (this.self instanceof Player) {
                tickOverlayTicks();
            }

            if (displayStand != null){
                if (displayStand.getFadeOut() < displayStand.MaxFade) {
                    displayStand.incFadeOut((byte) 1);
                }
            }
        } else {
            if (softenTicks > 0) {
                softenTicks-= 2;
            }
        }

        if (this.self instanceof Player PE && PE.isSpectator()) {
            ((StandUser) this.getSelf()).roundabout$setActive(false);
        }
        if (this.self.isAlive() && !this.self.isRemoved()) {
            if (this.self.level().isClientSide){
                updateGoBeyondTarget();
                if (!this.kickStarted && this.getAttackTimeDuring() <= -1){
                    this.kickStarted = true;
                }
            }
            if (this.isClashing()) {
                if (this.attackTimeDuring != -1) {
                    this.attackTimeDuring++;
                    this.updateClashing();
                }
            } else if (!this.self.level().isClientSide || kickStarted) {
                if (this.attackTimeDuring != -1) {
                    this.attackTimeDuring++;
                    if (this.attackTimeDuring == -1) {
                        ((StandUser) this.self).roundabout$tryPower(PowerIndex.NONE,true);
                    } else {
                        if (!this.isAttackInept(this.activePower)) {
                            if (this.activePower == PowerIndex.ATTACK) {
                                this.updateAttack();
                            } else if (this.isBarraging()) {

                                if (bonusBarrageConditions()) {
                                    if (this.isBarrageCharging()) {
                                        this.updateBarrageCharge();
                                    } else {
                                        this.updateBarrage();
                                    }
                                } else {
                                    ((StandUser) this.self).roundabout$tryPower(PowerIndex.NONE, true);
                                }
                            } else {
                                this.updateUniqueMoves();
                            }
                        } else {
                            resetAttackState();
                        }
                    }
                }
                this.attackTime++;
                if (this.attackTime > this.attackTimeMax) {
                    this.setActivePowerPhase((byte) 0);
                }
                if (this.interruptCD > 0) {
                    this.interruptCD--;
                }
            }
            this.tickDash();
        } else {
            StandUser user = ((StandUser)this.getSelf());
            StandEntity stnd = user.roundabout$getStand();
            if (stnd != null){
                user.roundabout$setStand(null);
            }
        }
        if (this.self.level().isClientSide) {
            tickSounds();
        }
        if (this.scopeLevel != 0 && !this.canScope()){
            setScopeLevel(0);
            this.scopeTime = -1;
        }
        if (((StandUser)this.self).roundabout$getStandDisc().isEmpty()){
            ((StandUser)this.self).roundabout$setStandPowers(new StandPowers(this.self));
        }
        if (!hasStandActive(this.self)) {
            getStandUserSelf().roundabout$setStandAnimation(NONE);
        }
    }
    /**Iteration through skins in the power inventory*/
    public void getSkinInDirection(boolean right, boolean sealed){
        StandUser SE = ((StandUser)this.getSelf());
        byte currentSkin = ((StandUser)this.getSelf()).roundabout$getStandSkin();
        List<Byte> skins = getSkinList();
        if (!skins.isEmpty() && skins.size() > 1) {
            int skinind = 0;
            for (int i = 0; i<skins.size(); i++){
                if (skins.get(i) == currentSkin){
                    skinind = i;
                }
            }
            if (right) {
                skinind+=1;
                if (skinind >= skins.size()){
                    skinind =0;
                }
                SE.roundabout$setStandSkin((skins.get(skinind)));
            } else {
                skinind-=1;
                if (skinind < 0){
                    skinind =skins.size()-1;
                }
                SE.roundabout$setStandSkin((skins.get(skinind)));
            }
            if (!sealed) {
                if (hasStandActive(self)) {
                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                    SE.roundabout$summonStand(this.getSelf().level(), true, false);
                }
            }
        }
    }
    /**Iteration through poses in the power inventory*/
    public void getPoseInDirection(boolean right){
        StandUser SE = ((StandUser)this.getSelf());
        byte currentSkin = ((StandUser)this.getSelf()).roundabout$getIdlePos();
        List<Byte> poses = getPosList();
        if (!poses.isEmpty() && poses.size() > 1) {
            int skinind = 0;
            for (int i = 0; i<poses.size(); i++){
                if (poses.get(i) == currentSkin){
                    skinind = i;
                }
            }
            if (right) {
                skinind+=1;
                if (skinind >= poses.size()){
                    skinind =0;
                }
                SE.roundabout$setIdlePosX(poses.get(skinind));
            } else {
                skinind-=1;
                if (skinind < 0){
                    skinind =poses.size()-1;
                }
                SE.roundabout$setIdlePosX(poses.get(skinind));
            }
        }
    }


    /**Only star platinum or the world would ever need to override these*/
    public float getTimestopRange(){
        return ClientNetworking.getAppropriateConfig().timeStopSettings.blockRangeNegativeOneIsInfinite;
    }
    public boolean fullTSChargeBonus(){return false;}
    /**Ticks through your own timestop. This value exists in the general stand powers in case you switch stands.*/
    public void timeTickStopPower(){
    }
    /**Name straightforward*/
    public boolean isStoppingTime(){
        return false;
    }
    public int getChargedTSTicks(){
        return this.chargedTSTicks;
    }
    public void setChargedTSTicks(int chargedTSSeconds){
        this.chargedTSTicks = chargedTSSeconds;
    }
    public int getMaxTSTime (){
        return 0;
    }
    public int getMaxChargeTSTime(){
        return 0;
    }
    public boolean getIsTsCharging(){
        return false;
    }
    private int chargedTSTicks = 0;
    public boolean hasActedInTS = false;

    /**This is not in powerssoftandwet because I believe if someone is using paisley or other stands they may be able
     * to redirect it in the future*/
    public Entity goBeyondTarget = null;
    public Entity getGoBeyondTarget(){
        return this.goBeyondTarget;
    }
    public void setGoBeyondTarget(Entity goBeyondTarget){
        this.goBeyondTarget = goBeyondTarget;
    }
    public void updateGoBeyondTarget(){
        goBeyondTarget = null;
    }

    /**If they have a max disc, very niche*/
    public boolean hasGoldenDisc(){
        ItemStack stack = ((StandUser)this.getSelf()).roundabout$getStandDisc();
        return !stack.isEmpty() && stack.getItem() instanceof MaxStandDiscItem;
    }

    /**Whether or not the stand has more than one skin*/
    public boolean hasMoreThanOneSkin(){
        List<Byte> skinList = getSkinList();
        return (skinList != null && !skinList.isEmpty() && skinList.size() > 1);
    }
    /**Whether or not the stand has more than one pose*/
    public boolean hasMoreThanOnePos(){
        List<Byte> posList = getPosList();
        return (posList != null && !posList.isEmpty() && posList.size() > 1);
    }

    /**gets projectiles for barrage to deflect*/
    public void findDeflectables(){
        float halfReach = (float) (getReach()*0.5);
        Vec3 pointVec = DamageHandler.getRayPoint(this.self, halfReach);
        List<Entity> arrows = arrowGrabHitbox(this.self,DamageHandler.genHitbox(this.self, pointVec.x, pointVec.y,
                pointVec.z, halfReach, halfReach, halfReach), getReach());
        if (!arrows.isEmpty() && ClientNetworking.getAppropriateConfig().generalStandSettings.barrageDeflectsArrows) {
            for (int i = 0; i < arrows.size(); i++) {
                deflectArrowsAndBullets(arrows.get(i));
            }
        }
    }

    //Stands sending simple message
    public void serverQueried(){
    }
    public void serverQueried2(){
    }


    //Stands sending simple message
    public void clientIntUpdated(int integer){
    }


    /**The stand is named on the disc so we just use that*/
    public Component getStandName(){
        ItemStack disc = ((StandUser)this.getSelf()).roundabout$getStandDisc();
        if (!disc.isEmpty() && disc.getItem() instanceof StandDiscItem SDI){
            return SDI.getDisplayName2();
        }
        return Component.empty();
    }



    /**You don't really need this*/
    public boolean setPowerSpecial(int lastMove) {return false;}
    public boolean setPowerMovement(int lastMove) {return false;}
    public boolean setPowerSneakMovement(int lastMove) {return false;}
    /**The cooldown for summoning. It is mostly clientside and doesn't have to be synced*/
    public int summonCD = 0;
    /**Just a sanity prevention for summoning too fast*/
    public boolean getSummonCD(){
        return this.summonCD <= 0;
    } public void setSummonCD(int summonCD){
        this.summonCD = summonCD;
    } public int getSummonCD2(){
        return this.summonCD;
    }
}
