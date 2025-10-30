package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.projectile.RattDartEntity;
import net.hydra.jojomod.entity.stand.RattEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.MaxStandDiscItem;
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
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.*;
import org.joml.Vector3f;

import java.util.List;
import java.util.Optional;

public class PowersRatt extends NewDashPreset {
    public PowersRatt(LivingEntity self) {
        super(self);
    }

    public static final int MinThreshold = 30;
    public static final int MaxThreshold = 90;
    public static final int BaseShootCooldown = 10;
    public static final int PlaceDelay = 10;
    public static final int PlaceShootCooldown = 30;
    public static final int MaxShootCooldown = 20;
    public static final int[] ShotThresholds = {MinThreshold,50,MaxThreshold};
    public static final float[] ShotPowerFloats = {3.55F,3.5F,4F};
    public static final int[] ShotSuperthrowTicks = {4,10,15};



    @Override
    public int getMaxPilotRange() {return ClientNetworking.getAppropriateConfig().rattSettings.rattMaxDespawnRange;}

    public static final byte

            UPDATE_POSITION = 52,
            AUTO = 54,
            FIRE_DART = 57,
            TOGGLE_BURSTING = 58,
            START_PLAYER_BURST = 59,
            PLAYER_BURST = 60,
            START_PLACE_BURST = 61,
            NET_PLACE = 63,
            NET_RECALL = 64,
            NET_SCOPE = 65,
            UPDATE_CHARGE = 66,
            START_CHARGE = 67,
            CHECK_AUTO = 68,

    PLACE_BURST = 69,
            CHANGE_MODE = 7,
            SETPLACE = 8,
            SCOPE = 9,
            RATT_LEAP = 5;




    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersRatt(entity);
    }
    @Override
    public StandEntity getNewStandEntity(){
        byte skin = ((StandUser)this.getSelf()).roundabout$getStandSkin();
        if (skin == RattEntity.REDD_SKIN){
            return ModEntities.REDD.create(this.getSelf().level());
        } else if (skin >= RattEntity.CHAIR_RAT_SKIN) {
            return ModEntities.CHAIR_RATT.create(this.getSelf().level());
        }

        return ModEntities.RATT.create(this.getSelf().level());
    }

    int shotcooldown = 0;
    int maxshotcooldown = 0;
    public void setShotCooldown(int i) {
        shotcooldown = i;
        maxshotcooldown = i;}



    public LivingEntity getShootTarget() {
        if (this.getStandEntity(this.getSelf()) instanceof RattEntity RE) {
            return RE.getTarget();
        }
        return null;
    }
    public void setShootTarget(LivingEntity l) {
        if (this.getStandEntity(this.getSelf()) instanceof RattEntity RE) {
            RE.setTarget(l);
        }
    }

    public boolean active = false;
    @Override
    public boolean canSummonStandAsEntity() {return false;}

    int chargeTime = 0; // max = 100
    public int getChargeTime() {return chargeTime;}
    public void updateChargeTime(int i ) {
        tryIntPower(PowersRatt.UPDATE_CHARGE,true,i);
        tryIntPowerPacket(PowersRatt.UPDATE_CHARGE,i);
    }

    @Override
    public void updatePowerInt(byte activePower, int data) {
        if (activePower == PowersRatt.UPDATE_CHARGE) {
            if (data < chargeTime) {
                ((StandUser) this.getSelf()).roundabout$setCombatMode(false);
                this.scopeLevel = 0;
                this.setCooldown(PowersRatt.SCOPE, 35);
            }
            chargeTime = data;
            updateChargeTime(data);
        }
        if (activePower == PowersRatt.NET_PLACE) {
            this.setCooldown(activePower,data);
        }
    }


    public boolean isPlaced() {return this.getStandEntity(this.getSelf()) != null;}
    public boolean isAuto() {return this.getStandUserSelf().roundabout$getUniqueStandModeToggle();}

    public BlockHitResult getTargetPos() {
        Vec3 vec3d = this.getSelf().getEyePosition(0);
        Vec3 vec3d2 = this.getSelf().getViewVector(0);
        Vec3 vec3d3 = vec3d.add(vec3d2.x * 60, vec3d2.y * 60, vec3d2.z * 60);
        return this.getSelf().level().clip(new ClipContext(vec3d, vec3d3,
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.getSelf()));

    }
    public Entity CoolerrayCastEntity(Level world, LivingEntity ratt, double maxDistance) {

        // yoinked from mainutil


        Vec3 vars = this.getRotations(this.getShootTarget());

        Vec3 eyePos = ratt.getEyePosition(1.0F);
        Vec3 lookVec = new Vec3(
                Math.cos(vars.y+Math.PI/2),
                Math.sin(vars.x),
                Math.sin(vars.y+Math.PI/2)
        );
        Vec3 reachVec = eyePos.add(lookVec.scale(maxDistance));

        ClipContext blockContext = new ClipContext(
                eyePos,
                reachVec,
                ClipContext.Block.OUTLINE,
                ClipContext.Fluid.NONE,
                ratt
        );

        BlockHitResult blockHit = world.clip(blockContext);
        double blockHitDistance = blockHit.getLocation().distanceTo(eyePos);

        // Search for potential target entities in bounding box
        AABB box = ratt.getBoundingBox().expandTowards(lookVec.scale(maxDistance)).inflate(1.0);
        List<Entity> candidates = world.getEntities(ratt, box,
                (e) -> e instanceof Entity && e.isPickable() && e.isAlive());

        Entity closest = null;
        double closestDistance = blockHitDistance;

        for (Entity entity : candidates) {
            AABB aabb = entity.getBoundingBox().inflate(0.3); // widen the target hit box a bit
            Optional<Vec3> hitOptional = aabb.clip(eyePos, reachVec);

            if (hitOptional.isPresent()) {
                double hitDistance = eyePos.distanceTo(hitOptional.get());
                if (hitDistance < closestDistance && !entity.isSpectator() && MainUtil.isStandPickable(entity) && !entity.isInvulnerable()
                        && !entity.hasPassenger(ratt)) {
                    closestDistance = hitDistance;
                    closest = entity;
                }
            }
        }

        return closest; // null if no valid hit
    }

    private Vec3 getValidPlacement(){
        Vec3 vec3d = this.getSelf().getEyePosition(0);
        Vec3 vec3d2 = this.getSelf().getViewVector(0);
        Vec3 vec3d3 = vec3d.add(vec3d2.x * 9, vec3d2.y * 9, vec3d2.z * 9);
        BlockHitResult blockHit = this.getSelf().level().clip(new ClipContext(vec3d, vec3d3,
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.getSelf()));
        Vec3 location = blockHit.getLocation();
        if (blockHit.getBlockPos() instanceof BlockPos.MutableBlockPos) {
            if (blockHit.getDirection() == Direction.UP) {
                return blockHit.getLocation();
            } else if (blockHit.getDirection() == Direction.DOWN) {
                if (this.getSelf().level().getBlockState(blockHit.getBlockPos().above(1)).isAir()) {
                    return new Vec3(location.x(), ((int) location.y()) + 1, location.z());
                }
            } else {
                for (int oy = 0; oy < 3; oy++) {
                    if (this.getSelf().level().getBlockState(blockHit.getBlockPos().above(oy)).isAir()) {
                        return new Vec3(location.x(), ((int) location.y()) + oy, location.z());
                    }
                }

            }
        }

        return null;
    }



    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        ClientUtil.fx.roundabout$onGUI(context);

        if (isPlaced()) {
            if (!isHoldingSneak()) {
                setSkillIcon(context, x, y, 1, LockedOrNot(StandIcons.RATT_BURST,0), PowersRatt.PLACE_BURST);
            } else {
                if (isAuto()) {
                    setSkillIcon(context, x, y, 1, LockedOrNot(StandIcons.RATT_AUTO, 1), PowersRatt.CHANGE_MODE);
                } else {
                    setSkillIcon(context, x, y, 1, LockedOrNot(StandIcons.RATT_UNAUTO,1), PowersRatt.CHANGE_MODE);
                }
            }
            if (scopeLevel == 0) {
                setSkillIcon(context, x, y, 2, StandIcons.RATT_RECALL, PowersRatt.SETPLACE);
            }
        } else {
            ResourceLocation ScopeIcon = StandIcons.RATT_SCOPE_IN;
            if (scopeLevel == 1) {
                ScopeIcon = StandIcons.RATT_SCOPE_OUT;
            }
            setSkillIcon(context, x, y, 1, LockedOrNot(ScopeIcon,2), PowersRatt.SCOPE);
            if (scopeLevel == 0) {
                setSkillIcon(context, x, y, 2, StandIcons.RATT_PLACE, PowersRatt.SETPLACE);
            } else {
                if (!isAuto()) {
                    setSkillIcon(context, x, y, 2, LockedOrNot(StandIcons.RATT_BURST,3), PowersRatt.CHANGE_MODE);
                } else {
                    setSkillIcon(context, x, y, 2, LockedOrNot(StandIcons.RATT_SINGLE,3), PowersRatt.CHANGE_MODE);
                }
            }
        }
        setSkillIcon(context,x,y,3,StandIcons.DODGE,PowerIndex.GLOBAL_DASH);

        setSkillIcon(context, x, y, 4, LockedOrNot(StandIcons.RATT_LEAP,4), PowersRatt.RATT_LEAP);

    }


    @Override
    public boolean tryPosPower(int move, boolean forced, Vec3 pos) {
        StandEntity SE = this.getStandEntity(this.getSelf());
        switch(move) {
            case UPDATE_POSITION -> {
                if (SE != null) {
                    SE.setPos(pos);
                }
            }
            case PowersRatt.NET_PLACE -> {
                this.getStandUserSelf().roundabout$setUniqueStandModeToggle(false);
                this.active = true;
                this.Placement = pos;
                this.setCooldown(PowersRatt.SETPLACE,80);
            }
        }
        return true;
    }

    public void placeRatt(Vec3 pos) {
        int cooldown = 60;
        this.setCooldown(PowersRatt.SETPLACE, cooldown);
        if (!isClient()) {
            blipStand(pos);
        }
    }

    public void blipStand(Vec3 pos) {
        StandEntity stand = getNewStandEntity();
        if (stand instanceof RattEntity RE) {
            StandUser user = getStandUserSelf();
            RE.setMaster(this.self);
            RE.absMoveTo(pos.x(),pos.y(),pos.z());
            RE.setSavedSkin( ((StandUser)this.getSelf()).roundabout$getStandSkin() );
            this.getStandUserSelf().roundabout$standMount(RE);
            this.self.level().addFreshEntity(RE);
            this.getSelf().level().playSound(this.getSelf(),this.getSelf().blockPosition(),ModSounds.RATT_PLACE_EVENT,SoundSource.PLAYERS,1F,1F);


        }
    }


    public Vec3 getRotations(Entity target) {

        RattEntity RE = (RattEntity) this.getStandEntity(this.getSelf());


        if (RE != null) {
            Vec3 targetPos = getTargetPos().getLocation();
            if (target != null) {
                targetPos = target.getEyePosition(1);
                if (isAuto()) {
                    double dist = targetPos.distanceTo(RE.getPosition(1));
                    double time = dist / ShotPowerFloats[1];
                    time *= 1.4;
                    Vec3 vec = target.getDeltaMovement();
                    if (target instanceof Player) {
                        if(Math.abs(vec.y) < 3 ) {vec = new Vec3(vec.x,0,vec.z);}
                    }
                    targetPos = targetPos.add(vec.multiply(time, time, time));
                }
            }
            double x = (targetPos.x() - RE.getPosition(0).x());
            double z = (targetPos.z() - RE.getPosition(0).z());
            float rot = (float) (Math.atan2(z, x) - Math.PI / 2);

            double hy = (targetPos.y() - (RE.getEyeP(0).y() ));
            double hd = Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2));
            double bonus = Math.PI/2;

            float hrot = (float) (Math.atan2(hd, hy) + Math.PI/2); // flip the sign if you want it to be not armed



            double percent = (double) RE.getFadeOut() / RE.getMaxFade();
            if (percent != 1 && RE.getSavedSkin() < RattEntity.CHAIR_RAT_SKIN) {
                hrot = (float) (Mth.lerp(percent, 0, hrot));
            }

            // HEAD ROTATION X = VERTICAL ROTATION OF HEAD
            // STAND ROTATION Y = HORIZONTAL ROTATION OF WHOLE
            return new Vec3(hrot, rot, 0);
        }
        return new Vec3(0,0,0);

    }


    Vec3 Placement = null;
    int shieldDelay = 0;
    @Override
    public void tickPower() {
        super.tickPower();


        if (isPlaced()) {
            DimensionType t = this.getStandEntity(this.getSelf()).level().dimensionType();
            DimensionType T = this.getSelf().level().dimensionType();
            if (t != T) {
                RecallClient(true);
            }

            if (this.getSelf().distanceTo(this.getStandEntity(this.getSelf())) > this.getMaxPilotRange() && !this.getStandEntity(this.getSelf()).forceDespawnSet) {
                RecallClient(true);
            }
        }



        if (this.getActivePower() == PowersRatt.START_CHARGE && this.isClient()) {
            int amount = ClientNetworking.getAppropriateConfig().rattSettings.rattManualChargeRate;
            updateChargeTime(Mth.clamp(getChargeTime()+(this.getAttackTime()%2 == 0 ? amount : amount+1),0,100));

            if (getChargeTime() == 100 || scopeLevel == 0) {this.setPowerNone();}

        }
        if (this.getStandEntity(this.getSelf()) != null) {
            if (this.getStandEntity(this.getSelf()).forceDespawnSet) {
                setPowerNone();
            }
        }

        if (shotcooldown != 0) {shotcooldown--;}
        if (shotcooldown == 0) {
            maxshotcooldown = 0;
            if(!this.isUsingShield(this.getSelf())) {
                shieldDelay = 0;
            }
        }


        StandEntity SE = this.getStandEntity(this.getSelf());


        if (isPlaced() && !(this.getSelf() instanceof Mob)) {


            Entity f = MainUtil.getTargetEntity(this.getSelf(),40);

            if (!this.isClient()) {

                if (!SE.onGround()) {
                    ((ServerLevel) this.getSelf().level()).sendParticles(new DustParticleOptions(new Vector3f(0.86F, 0.28F, 0.48F
                            ), 1f),
                            SE.getX(), ((RattEntity)SE).getEyeP(0F).y(), SE.getZ(),
                            0, 0, 0, 0, 0);
                }



                Entity e = MainUtil.getTargetEntity(this.getSelf(),40);

                if (e instanceof LivingEntity L) {
                    if (isAuto()) {
                        if (getShootTarget() != null) {
                            if (getShootTarget().getHealth() == 0) {
                                setShootTarget(null);
                            }
                        }

                    } else if (!L.equals(this.getSelf()) && !L.equals(SE)) {
                        if (!MainUtil.getEntityIsTrulyInvisible(e) && L.getEffect(MobEffects.INVISIBILITY) == null) {
                            if (!(L instanceof StandEntity)) {
                                setShootTarget(L);
                            } // this might have to be changed eventually
                        }
                    }

                    if (e.distanceTo(this.getStandEntity(this.getSelf())) >= 40 ) {
                        setShootTarget(null);
                    }
                    if (getShootTarget() != null) {
                        if (MainUtil.getEntityIsTrulyInvisible(getShootTarget()) || ((LivingEntity)getShootTarget()).getEffect(MobEffects.INVISIBILITY) != null) {
                            setShootTarget(null);
                        }
                    }

                    if(isAuto() && getShootTarget() == null) {
                        this.getStandUserSelf().roundabout$setUniqueStandModeToggle(false);
                    }


                } else {
                    if (e == null) {
                        if(!isAuto()) {
                            setShootTarget(null);
                        }
                    }
                }
            }
            if (isPacketPlayer()) {
                if (isAuto()) {
                    if (isClient()) {
                        if (true) {
                            BurstFire();
                        }
                    }
                    if (getShootTarget() != null) {
                        if (getShootTarget().getHealth() == 0) {
                            this.setShootTarget(null);
                        }
                    } else if (isAuto()) {
                        this.getStandUserSelf().roundabout$setUniqueStandModeToggle(false);
                    }
                }
            }
        } else if (active) {
            if (this.getStandUserSelf().roundabout$getActive()) {
                if (!isClient()) {
                    if (Placement != null) {
                        placeRatt(Placement);
                    }
                }
            }

        }


    }


    @Override
    public void updateUniqueMoves() {
        if (this.getActivePower() == PowersRatt.PLAYER_BURST) {
            if (isClient()) {
                if (this.attackTimeDuring%2 == 1) {
                    tryPower(PowersRatt.PLAYER_BURST,true);
                    tryPowerPacket(PowersRatt.PLAYER_BURST);
                }
                if (getChargeTime() < 30) {
                    this.updateChargeTime(0);
                    this.setShotCooldown(MaxShootCooldown);
                    tryPowerPacket(PowerIndex.NONE);
                    setPowerNone();
                }
            }
        } else if (this.getActivePower() == PowersRatt.PLACE_BURST) {
            setShotCooldown(PlaceShootCooldown+shieldDelay);
            if (getAttackTimeDuring() == PlaceDelay) {
                this.setAttackTimeDuring(PlaceDelay+5);
                setPowerNone();
                if (isUsingShield(this.getSelf())) {
                    shieldDelay += 8;
                   /* if (shieldDelay == 0) {shieldDelay=1;} else {
                        shieldDelay += 9;
                    } */
                }


                if (!this.isClient()) {
                    placeBurst();
                }


            }
        }
        super.updateUniqueMoves();
    }

    public void placeBurst() {
        this.animateStand(RattEntity.FIRE);
        this.setPowerNone();
        this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.RATT_FIRING_EVENT, SoundSource.PLAYERS, 1.7F, 0.9F+(float)Math.random()*0.2F);
        for (int i = 0; i < 3; i++) {
            if (this.getStandEntity(this.getSelf()) instanceof RattEntity RE) {
                RattDartEntity e = new RattDartEntity(RE.level(), this.getSelf(), 1, 0.1F);
                Vec3 v = this.getRotations(this.getShootTarget());
                e.shootFromRotation(RE, (float) v.x * 180 / (float) Math.PI + 180, (float) v.y * 180 / (float) Math.PI, -0.5F, ShotPowerFloats[1], 0.84F);
                e.EnableSuperThrow();
                e.setParticleTrails(true);
                RE.level().addFreshEntity(e);
            }
        }

    }

    @Override
    public void powerActivate(PowerContext context) {

        switch (context)
        {
            case SKILL_1_NORMAL -> {
                if (!isPlaced()) {
                    if (canExecuteMoveWithLevel(2)) {
                        RattScope();
                    }
                } else {
                    BurstFire();
                }
            }
            case SKILL_1_CROUCH -> {
                if (isPlaced()) {
                    if (!isAttackIneptVisually(PowersRatt.AUTO, 1)) {
                        if (canExecuteMoveWithLevel(1)) {
                            ToggleAuto();
                        }
                    }
                } else {
                    if (canExecuteMoveWithLevel(2)) {
                        RattScope();
                    }
                }
            }
            case SKILL_2_NORMAL, SKILL_2_CROUCH -> {
                if (scopeLevel != 0)  {
                    if(canExecuteMoveWithLevel(3)) {
                        ToggleBursting();
                    }
                } else {
                    if (!isPlaced()) {
                        if (getValidPlacement() != null) {
                            DeployClient();
                        }
                    } else {
                        RecallClient(false);
                    }
                }
            }
            case SKILL_3_NORMAL, SKILL_3_CROUCH -> dash();

            case SKILL_4_NORMAL, SKILL_4_CROUCH -> RattLeap();
        }
    }

    public void RattLeap() {
        if (!onCooldown(PowersRatt.RATT_LEAP) && !isAttackIneptVisually(PowersRatt.RATT_LEAP,4)) {
            tryPower(PowersRatt.RATT_LEAP);
            tryPowerPacket(PowersRatt.RATT_LEAP);
        }
    }
    public void RattScope() {
        if (!this.onCooldown(PowersRatt.SCOPE)) {
            int nl = scopeLevel + 1;
            tryIntPower(PowersRatt.NET_SCOPE, true,nl == 2 ? 0 : nl);
            tryIntPowerPacket(PowersRatt.NET_SCOPE,nl == 2 ? 0 : nl);
            this.getSelf().playSound(nl != 2 ? ModSounds.RATT_SCOPE_EVENT : ModSounds.RATT_DESCOPE_EVENT, 1.0F, (float) (0.98F + (Math.random() * 0.04F)));
        }

    }


    @Override
    public boolean tryIntPower(int move, boolean forced, int chargeTime) {
        switch (move) {
            case PowersRatt.NET_SCOPE -> {
                this.setCooldown(PowersRatt.SCOPE,5);
                this.setAttackTime(-1);
                setScopeLevel(chargeTime);
                this.getStandUserSelf().roundabout$setCombatMode(scopeLevel != 0);
            }
            case PowersRatt.UPDATE_CHARGE -> this.chargeTime = chargeTime;

            case PowersRatt.FIRE_DART -> {
                setShotCooldown(chargeTime >= MaxThreshold ? MaxShootCooldown : BaseShootCooldown);
                this.setCooldown(PowersRatt.SETPLACE,30);
                if (!isClient()) {
                    FireDart(chargeTime,0.2F);
                }  else {
                    updateChargeTime(0);
                }

            }


        }
        return super.tryIntPower(move, forced, chargeTime);
    }

    public void ToggleBursting() {
        if (!this.onCooldown(PowersRatt.CHANGE_MODE)) {
            this.getStandUserSelf().roundabout$setUniqueStandModeToggle(!isAuto());
            tryPower(PowersRatt.TOGGLE_BURSTING, true);
            tryPowerPacket(PowersRatt.TOGGLE_BURSTING);
        }
    }

    public void PlayerFire() {
        int i = getChargeTime();
        tryIntPower(PowersRatt.FIRE_DART,true,i);
        tryIntPowerPacket(PowersRatt.FIRE_DART,i);

    }


    public void BurstFire() {
        if (!isAttackIneptVisually(PowersRatt.PLACE_BURST,2)) {
            tryPower(PowersRatt.START_PLACE_BURST, true);
            tryPowerPacket(PowersRatt.START_PLACE_BURST);
        }
    }



    // -1 == 51 but with no melt
    public void FireDart(int i, float acuracy) {
        float power = 0;
        for (int b=ShotThresholds.length-1;b>=0;b--) {
            if ( (i == -1 ? 51 : i) >= ShotThresholds[b]) {
                power = ShotPowerFloats[b];
                break;
            }
        }
        RattDartEntity e = new RattDartEntity(this.getSelf().level(),this.getSelf(),i);
        e.shootFromRotation(this.getSelf(), this.getSelf().getXRot(), this.getSelf().getYRot(), -0.5F, power, acuracy);
        e.EnableSuperThrow();
        e.setParticleTrails(true);
        this.getSelf().level().addFreshEntity(e);

    }

    public void ToggleAuto() {
        tryPower(PowersRatt.TOGGLE_BURSTING,true);
        tryPowerPacket(PowersRatt.TOGGLE_BURSTING);
    }

    public void DeployClient() {
        if (!this.onCooldown(PowersRatt.SETPLACE)) {
            this.getSelf().playSound(ModSounds.RATT_PLACE_EVENT, 1.0F, (float) (0.98F + (Math.random() * 0.04F)));
            Vec3 blockHitResult = getValidPlacement();
            if (blockHitResult != null) {
                tryPosPower(PowersRatt.NET_PLACE, true, blockHitResult);
                tryPosPowerPacket(PowersRatt.NET_PLACE, blockHitResult);
            }
        }
    }


    public void RecallClient(boolean forced) {
        Roundabout.LOGGER.info("??"+this.getStandEntity(this.getSelf()).forceDespawnSet);
        if (!this.onCooldown(PowersRatt.SETPLACE) || forced && this.getStandEntity(this.getSelf()) != null) {
            tryPower(PowersRatt.NET_RECALL,true);
            tryPowerPacket(PowersRatt.NET_RECALL);
        }
    }

    @Override
    public boolean tryPower(int move, boolean forced) {
        switch (move) {
            case PowersRatt.CHECK_AUTO -> {
                this.setShootTarget(null);
            }
            case PowersRatt.NET_RECALL -> {
                active = false;
                this.getStandUserSelf().roundabout$setUniqueStandModeToggle(false);
                if (this.getStandEntity(this.getSelf()) != null) {
                    this.getStandEntity(this.getSelf()).forceDespawnSet = true;
                }
                this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.RATT_DEPLACE_EVENT, SoundSource.PLAYERS, 0.5F, 1F);
                this.setCooldown(PowersRatt.SETPLACE,40);
            }
            case PowersRatt.FIRE_DART -> this.setCooldown(PowersRatt.CHANGE_MODE,15);
            case PowersRatt.START_PLAYER_BURST -> {
                this.setAttackTimeDuring(0);
                this.setActivePower(PowersRatt.PLAYER_BURST);
            }
            case PowersRatt.PLAYER_BURST -> {
                this.setCooldown(PowersRatt.SETPLACE,10);
                this.setActivePower(PowersRatt.PLAYER_BURST);
                chargeTime -= 30;
                if (!isClient()) {
                    FireDart(61,0.4F);
                }
            }
            case PowersRatt.TOGGLE_BURSTING -> {
                if (isPlaced()) {
                    // I might add an auto noise idk I think it's obvious enough
                } else {
                    this.getSelf().level().playSound(null,this.getSelf().blockPosition(),ModSounds.RATT_MODE_CHANGE_EVENT,SoundSource.PLAYERS,1F,(float)(0.9+Math.random()*0.2));
                }
                this.getStandUserSelf().roundabout$setUniqueStandModeToggle(!isAuto());
                this.setCooldown(PowersRatt.CHANGE_MODE,10);
            }
            case PowersRatt.START_PLACE_BURST -> {
                this.setShotCooldown(PlaceShootCooldown);
                this.setAttackTimeDuring(0);
                this.setAttackTime(-1);
                if (this.getActivePower() != PowersRatt.PLACE_BURST) {
                    this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.RATT_LOADING_EVENT, SoundSource.PLAYERS);
                }
                this.setActivePower(PowersRatt.PLACE_BURST);
                if (this.getSelf() instanceof Player) {
                    S2CPacketUtil.sendActivePowerPacket((Player) this.getSelf(), this.getActivePower());
                }

                if (!isClient()) {
                    this.animateStand(RattEntity.LOADING);
                }
            }

            case PowersRatt.RATT_LEAP -> {
                this.setCooldown(PowersRatt.RATT_LEAP,150);
                Vec3 dir = this.getSelf().getViewVector(1);
                if (this.getStandEntity(this.getSelf()) != null) {
                    dir = dir.scale(1.12);
                    Vec3 vec3 = new Vec3(dir.x, Mth.clamp(dir.y+0.2F,0.1,100), dir.z);
                    this.getStandEntity(this.getSelf()).setDeltaMovement(vec3);
                }
                this.getSelf().level().playSound(null,this.getSelf().blockPosition(),ModSounds.RATT_LEAP_EVENT, SoundSource.PLAYERS, 1F,1.2F);
            }
        }
        return super.tryPower(move, forced);
    }



    @Override
    public float inputSpeedModifiers(float basis) {
        if (scopeLevel != 0){
            basis*=0.7f;
        }
        return super.inputSpeedModifiers(basis);
    }

    @Override
    public boolean isAttackInept(byte activeP) {
        if (this.getSelf().isUsingItem()) { return false;}
        return super.isAttackInept(activeP);
    }

    @Override
    public boolean isAttackIneptVisually(byte activeP, int slot) {
        switch (activeP) {
            case PowersRatt.AUTO -> {
                return getShootTarget() == null && isHoldingSneak() && !isAuto() && isPlaced();
            }
            case PowersRatt.SETPLACE -> {
                if (isPlaced()) {
                    return false;
                } else {
                    if (scopeLevel == 0) {
                        return getValidPlacement() == null && !isPlaced();
                    }
                }
            }
            case PowersRatt.PLACE_BURST -> {
                return shotcooldown != 0 || shieldDelay >= 20;
            }
            case PowersRatt.CHANGE_MODE -> {
                if (scopeLevel != 0) {
                    return getChargeTime() <= MinThreshold || shotcooldown != 0;
                } else if (isPlaced()) {
                    return !isAuto() && getShootTarget() == null;
                }
            }

            case PowersRatt.RATT_LEAP -> {
                if (!canExecuteMoveWithLevel(4)) {return true;}
                if (isPlaced()) {
                    if(this.getStandEntity(this.getSelf()).onGround()) {
                        return false;
                    }
                }
                return true;
            }

        }
        return super.isAttackIneptVisually(activeP, slot);
    }

    @Override
    public boolean interceptAttack() {return  scopeLevel != 0;}
    @Override
    public void buttonInputAttack(boolean keyIsDown, Options options) {
        if (keyIsDown) {
            if (scopeLevel != 0 && this.chargeTime >= PowersRatt.MinThreshold) {
                if (this.getActivePower() == PowerIndex.NONE) {
                    if (!isAttackIneptVisually(PowersRatt.CHANGE_MODE, 2)) {
                        this.getSelf().playSound(ModSounds.RATT_FIRING_EVENT, 1.0F, (float) (0.98F + (Math.random() * 0.04F)));
                        if (!isAuto()) {
                            tryPower(PowersRatt.START_PLAYER_BURST, true);
                            tryPowerPacket(PowersRatt.START_PLAYER_BURST);
                        } else {
                            PlayerFire();
                        }
                    }
                }

            }
        }

    }

    @Override
    public boolean interceptGuard() {return scopeLevel != 0;}
    @Override
    public boolean buttonInputGuard(boolean keyIsDown, Options options) {
        if (this.getActivePower() == PowerIndex.NONE) {
            if (getChargeTime() != 100 && shotcooldown == 0) {
                tryPower(PowerIndex.GUARD, true);
                tryPowerPacket(PowerIndex.GUARD);
            }
        }
        return true;
    }


    @Override
    public boolean clickRelease() {
        if (scopeLevel != 0 && this.getActivePower() == PowersRatt.START_CHARGE) {
            this.setPowerNone();
        }
        return false;
    }

    @Override
    public boolean setPowerGuard() {
        this.setActivePower(PowersRatt.START_CHARGE);
        return true;
    }

    @Override
    public void tickStandRejection(MobEffectInstance effect) {
        if (!isClient()) {
            if (effect.getDuration()%15 == 0) {
                MobEffectInstance effec = this.getSelf().getEffect(ModEffects.MELTING);
                int stack = effec != null ?  effec.getAmplifier() : 0;
                this.getSelf().addEffect(new MobEffectInstance(ModEffects.MELTING,600,stack+2));
                float degrees = (float) Math.random()*360;
                MainUtil.takeUnresistableKnockbackWithY(this.getSelf(), 0.27F,
                        Mth.sin(degrees * ((float) Math.PI / 180)),
                        Mth.sin(-20 * ((float) Math.PI / 180)),
                        -Mth.cos(degrees * ((float) Math.PI / 180)));
            }
        }
    }

    @Override
    public void onActuallyHurt(DamageSource $$0, float $$1) {
        if ($$0.is(DamageTypes.PLAYER_ATTACK) || $$0.is(DamageTypes.MOB_ATTACK) || $$0.is(ModDamageTypes.STAND) || $$0.is(ModDamageTypes.STAND_RUSH) || $$0.is(ModDamageTypes.PENETRATING_STAND)) {
            if ($$0.getEntity().getPosition(1).distanceTo(this.getSelf().getPosition(1)) < 6.0 ) {
                if (this.getSelf() instanceof Player P ) {
                    if (this.getStandUserSelf().roundabout$getCombatMode()) {
                        int nc = Math.max(this.getChargeTime()-30,0);
                        this.getSelf().level().playSound(null, this.getSelf().blockPosition(), SoundEvents.ITEM_BREAK, SoundSource.PLAYERS, 1F, 1F);
                        this.updatePowerInt(PowersRatt.UPDATE_CHARGE,nc);
                        S2CPacketUtil.sendIntPowerDataPacket(P,PowersRatt.UPDATE_CHARGE,nc);
                    }
                }
            }
        }
    }

    @Override
    public boolean shouldReset(byte activeP) {
        //    if (this.getSelf().isUsingItem()) {Roundabout.LOGGER.info("CANCELLED");return false;}
        return super.shouldReset(activeP);
    }

    @Override
    public void tickMobAI(LivingEntity attackTarget) {
        if (attackTarget != null) {
            this.setShootTarget(attackTarget);
            // this.getStandUserSelf().roundabout$setCombatMode(true);
            //    double dist = attackTarget.getPosition(0).distanceTo(this.getSelf().getPosition(0));
            if (isPlaced()) {
                if (this.shotcooldown == 0) {
                    this.shotcooldown = PlaceShootCooldown;
                    ((StandUser) this.getSelf()).roundabout$tryPower(PowersRatt.START_PLACE_BURST, true);
                }
            } else {
                if (!onCooldown(PowersRatt.SETPLACE)){
                    Vec3 vec3 = this.getSelf().getPosition(0);
                    blipStand(new Vec3(vec3.x, Math.floor(vec3.y), vec3.z));
                }
            }
        }
    }


    @Override
    public void renderAttackHud(GuiGraphics context, Player playerEntity, int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount, float flashAlpha, float otherFlashAlpha) {
        int j = scaledHeight / 2 - 7 - 4;
        int k = scaledWidth / 2 - 8;

        if (getValidPlacement() != null && !isPlaced() || shotcooldown != 0 || scopeLevel != 0) {
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
        }
        if (getChargeTime() > 10 && ((IPlayerEntity)playerEntity).roundabout$getStandLevel() > 1 ) {
            float amount = (float) getChargeTime() / 100;
            int finalAmount = Math.round(amount * 15);
            int bartexture = 30;
            if (getChargeTime() >= MaxThreshold) {
                bartexture -= 6;
            }
            //file, end x, endy, x, y, width, height
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, bartexture, finalAmount, 6);
        }
        if (shotcooldown != 0) {
            float ratio = (float) shotcooldown /maxshotcooldown;
            int fifteen = 15-Math.round(ratio*15);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 12, fifteen, 6);
        }

        super.renderAttackHud(context, playerEntity, scaledWidth, scaledHeight, ticks, vehicleHeartCount, flashAlpha, otherFlashAlpha);
    }


    @Override
    public boolean canScope() {
        return getStandUserSelf().roundabout$getActive() && !isPlaced();
    }

    @Override
    public boolean rendersPlayer() {
        return !isPlaced();
    }

    @Override
    public List<Byte> getSkinList() {
        List<Byte> list = Lists.newArrayList();
        list.add(RattEntity.ANIME_SKIN);
        list.add(RattEntity.MANGA_SKIN);
        if (this.getSelf() instanceof Player PE) {
            byte Level = ((IPlayerEntity)PE).roundabout$getStandLevel();
            ItemStack goldDisc = ((StandUser)PE).roundabout$getStandDisc();
            boolean bypass = PE.isCreative() || (!goldDisc.isEmpty() && goldDisc.getItem() instanceof MaxStandDiscItem);

            if (Level >= 2 || bypass) {
                list.add(RattEntity.MELON_SKIN);
                list.add(RattEntity.AZTEC_SKIN);
                list.add(RattEntity.TOWER_SKIN);
            }
            if (Level >= 3 || bypass) {
                list.add(RattEntity.GUARDIAN_SKIN);
                list.add(RattEntity.ELDER_GUARDIAN_SKIN);
                list.add(RattEntity.REDD_SKIN);
            }
            if (Level >= 4 || bypass) {
                list.add(RattEntity.SAND_SKIN);
                list.add(RattEntity.SNOWY_SKIN);
                list.add(RattEntity.NETHER_SKIN);
            }
            if (((IPlayerEntity)PE).roundabout$getUnlockedBonusSkin() || bypass) {
                list.add(RattEntity.CHAIR_RAT_SKIN);
                list.add(RattEntity.KING_RAT_SKIN);
                list.add(RattEntity.MECH_RAT_SKIN);
            }
        }
        return list;
    }

    @Override
    public Component getSkinName(byte skinId) {
        return getSkinNameT(skinId);
    }

    public static Component getSkinNameT(byte skinId){
        switch (skinId) {
            case RattEntity.MANGA_SKIN -> {return Component.translatable("skins.roundabout.ratt.manga");}
            case RattEntity.MELON_SKIN -> {return Component.translatable("skins.roundabout.ratt.melon");}
            case RattEntity.SAND_SKIN -> {return Component.translatable("skins.roundabout.ratt.sand");}
            case RattEntity.AZTEC_SKIN -> {return Component.translatable("skins.roundabout.ratt.aztec");}
            case RattEntity.TOWER_SKIN -> {return Component.translatable("skins.roundabout.ratt.tower");}
            case RattEntity.REDD_SKIN -> {return Component.translatable("skins.roundabout.ratt.redd");}
            case RattEntity.SNOWY_SKIN -> {return Component.translatable("skins.roundabout.ratt.snowy");}
            case RattEntity.NETHER_SKIN -> {return Component.translatable("skins.roundabout.ratt.nether");}
            case RattEntity.GUARDIAN_SKIN -> {return Component.translatable("skins.roundabout.ratt.guardian");}
            case RattEntity.ELDER_GUARDIAN_SKIN -> {return Component.translatable("skins.roundabout.ratt.elder_guardian");}
            case RattEntity.CHAIR_RAT_SKIN -> {return Component.translatable("skins.roundabout.ratt.chair_rat");}
            case RattEntity.KING_RAT_SKIN -> {return Component.translatable("skins.roundabout.ratt.king_rat");}
            case RattEntity.MECH_RAT_SKIN -> {return Component.translatable("skins.roundabout.ratt.mech_rat");}
            default -> {return Component.translatable("skins.roundabout.ratt.anime");}
        }
    }

    public void unlockSkin(){
        Level lv = this.getSelf().level();
        if ((this.getSelf()) instanceof Player PE){
            StandUser user = ((StandUser)PE);
            ItemStack stack = user.roundabout$getStandDisc();
            if (!stack.isEmpty() && stack.is(ModItems.STAND_DISC_RATT)){
                IPlayerEntity ipe = ((IPlayerEntity) PE);
                if (!ipe.roundabout$getUnlockedBonusSkin()){
                    if (!lv.isClientSide()) {
                        ipe.roundabout$setUnlockedBonusSkin(true);
                        lv.playSound(null, PE.getX(), PE.getY(),
                                PE.getZ(), ModSounds.UNLOCK_SKIN_EVENT, PE.getSoundSource(), 2.0F, 1.0F);
                        ((ServerLevel) lv).sendParticles(ParticleTypes.END_ROD, PE.getX(),
                                PE.getY()+PE.getEyeHeight(), PE.getZ(),
                                10, 0.5, 0.5, 0.5, 0.2);
                        user.roundabout$setStandSkin(RattEntity.CHAIR_RAT_SKIN);
                        user.roundabout$summonStand(this.getSelf().level(), true, false);
                        ((ServerPlayer) ipe).displayClientMessage(
                                Component.translatable("unlock_skin.roundabout.ratt.rat"), true);
                    }
                }
            }
        }
    }


    @Override
    public void levelUp() {
        if (!this.getSelf().level().isClientSide() && this.getSelf() instanceof Player PE){
            IPlayerEntity ipe = ((IPlayerEntity) PE);
            byte level = ipe.roundabout$getStandLevel();
            MutableComponent text = Component.translatable("leveling.roundabout.levelup.both");
            if (level == 4) {
                text = Component.translatable("leveling.roundabout.levelup.max.both");
            }

            ((ServerPlayer) this.self).displayClientMessage( (text).withStyle(ChatFormatting.AQUA), true);
        }
        super.levelUp();
    }

    public Component getPosName(byte posID) {
        if (posID == 1) {
            return Component.translatable("idle.roundabout.ratt_1");
        } else {
            return Component.translatable("idle.roundabout.ratt_2");
        }
    }

    @Override
    public SoundEvent getSoundFromByte(byte soundChoice){
        if (soundChoice == SoundIndex.SUMMON_SOUND) {
            return ModSounds.RATT_SUMMON_EVENT;
        }
        return super.getSoundFromByte(soundChoice);
    }
    @Override
    protected Byte getSummonSound() {return SoundIndex.SUMMON_SOUND;}

    @Override
    public byte getMaxLevel() {return 4;}

    public ResourceLocation LockedOrNot(ResourceLocation img, int level) {
        return canExecuteMoveWithLevel(level) ? img : StandIcons.LOCKED;
    }

    @Override
    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypas){
        List<AbilityIconInstance> $$1 = Lists.newArrayList();

        // manual scope
        $$1.add(drawSingleGUIIcon(context,18,leftPos+20,topPos+80,2, "ability.roundabout.ratt_scope",
                "instruction.roundabout.press_skill", StandIcons.RATT_SCOPE_IN,1,level,bypas));
        // charge fire
        $$1.add(drawSingleGUIIcon(context,18,leftPos+20, topPos+118,3, "ability.roundabout.ratt_single",
                "instruction.roundabout.hold_block", StandIcons.RATT_SINGLE,0,level,bypas));
        // burst fire
        $$1.add(drawSingleGUIIcon(context,18,leftPos+20,topPos+99,2, "ability.roundabout.ratt_burst",
                "instruction.roundabout.press_skill", StandIcons.RATT_BURST,2,level,bypas));
        // place ratt
        $$1.add(drawSingleGUIIcon(context,18,leftPos+39,topPos+80,0, "ability.roundabout.ratt_place",
                "instruction.roundabout.press_skill", StandIcons.RATT_PLACE,2,level,bypas));
        // place burst
        $$1.add(drawSingleGUIIcon(context,18,leftPos+39,topPos+99,0, "ability.roundabout.ratt_place_burst",
                "instruction.roundabout.press_skill", StandIcons.RATT_BURST,1,level,bypas));
        // place auto
        $$1.add(drawSingleGUIIcon(context,18,leftPos+39,topPos+118,1, "ability.roundabout.ratt_auto",
                "instruction.roundabout.press_skill_crouch", StandIcons.RATT_AUTO,1,level,bypas));
        // ratt leap
        $$1.add(drawSingleGUIIcon(context,18,leftPos+58,topPos+80,4, "ability.roundabout.ratt_leap",
                "instruction.roundabout.press_skill", StandIcons.RATT_LEAP,4,level,bypas));
        // passive
        $$1.add(drawSingleGUIIcon(context,18,leftPos+58,topPos+99,0, "ability.roundabout.ratt_flesh",
                "instruction.roundabout.passive", StandIcons.RATT_BLOB,3,level,bypas));
        // dodge
        $$1.add(drawSingleGUIIcon(context,18,leftPos+58,topPos+118,0, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE,3,level,bypas));
        return $$1;
    }

    @Override
    public boolean isStandEnabled() {
        return ClientNetworking.getAppropriateConfig().rattSettings.enableRatt;
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
        return Component.literal(  "Prisma").withStyle(ChatFormatting.YELLOW);
    }
}