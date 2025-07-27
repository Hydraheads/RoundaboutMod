package net.hydra.jojomod.stand.powers;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.projectile.RattDartEntity;
import net.hydra.jojomod.entity.stand.RattEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.*;

import java.util.Arrays;
import java.util.List;

public class PowersRatt extends NewDashPreset {
    public PowersRatt(LivingEntity self) {
        super(self);
    }

    public static final int MinThreshold = 30;
    public static final int MaxThreshold = 90;
    public static final int BaseShootCooldown = 10;
    public static final int MaxShootCooldown = 30;
    public static final int[] ShotThresholds = {MinThreshold,50,MaxThreshold};
    public static final float[] ShotPowerFloats = {3,4.2F,5};
    public static final float[] ShotDamageTicks = {0.5F,0.5F,0.5F};

    public static final int[] ShotSuperthrowTicks = {4,10,15};


    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersRatt(entity);
    }


    @Override
    public StandEntity getNewStandEntity(){
        return ModEntities.RATT.create(this.getSelf().level());
    }

    public void setShotCooldown(int i) {
        shotcooldown = i;
        maxshotcooldown = i;
    }


    public static float PlacementRange = 5.0F;


    public static final byte

            UPDATE_POSITION = 0,
            ROTATE = 2,
            CHANGE_MODE = 5,
            AUTO = 7,
            SETPLACE = 8,
            SCOPE = 9,
            FIRE_DART =10,
            TOGGLE_BURSTING = 11,
            START_PLAYER_BURST = 13,
            PLAYER_BURST = 15,
            PLACE_BURST = 16;
    // skips 3, 6, 12, and 15 due to conflicts
    public static final byte
            NET_PLACE_BURST = 17,
            PLACE_BURST_FIRE = 16,
            NET_PLACE = 1,
            NET_RECALL = 2,
            NET_SCOPE = 3,
            UPDATE_CHARGE = 4;



    public Entity ShootTarget = null;
    public Entity getShootTarget() {return ShootTarget;}
    public void setShootTarget(Entity e) {ShootTarget = e;}



    boolean active = false;
    @Override
    public boolean canSummonStandAsEntity() {return false;}

    int chargeTime = 0;
    // max == 100
    public int getChargeTime() {return chargeTime;}
    public void updateChargeTime(int i ) {
        tryIntPower(PowersRatt.UPDATE_CHARGE,true,i);
        tryIntPowerPacket(PowersRatt.UPDATE_CHARGE,i);
    }

    int shotcooldown = 0;
    int maxshotcooldown = 0;

    public boolean isPlaced() {return this.getStandEntity(this.getSelf()) != null;}
    public boolean isAuto() {return this.getStandUserSelf().roundabout$getUniqueStandModeToggle();}

    public BlockHitResult getTargetPos() {
        Vec3 vec3d = this.getSelf().getEyePosition(0);
        Vec3 vec3d2 = this.getSelf().getViewVector(0);
        Vec3 vec3d3 = vec3d.add(vec3d2.x * 60, vec3d2.y * 60, vec3d2.z * 60);
        BlockHitResult blockHit = this.getSelf().level().clip(new ClipContext(vec3d, vec3d3,
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.getSelf()));
        return blockHit;
    }
    public static Entity CoolerrayCastEntity(Entity entityX, float reach){
        float tickDelta = 0;
        if (entityX.level().isClientSide()) {
            tickDelta = ClientUtil.getDelta();
        }
        Vec3 vec3d = entityX.getEyePosition(tickDelta);

        Vec3 vec3d2 = entityX.getViewVector(1.0f).multiply(-1,-1,-1);
        Vec3 vec3d3 = vec3d.add(vec3d2.x * reach, vec3d2.y * reach, vec3d2.z * reach);
        float f = 1.0f;
        AABB box = new AABB(vec3d.x+reach, vec3d.y+reach, vec3d.z+reach, vec3d.x-reach, vec3d.y-reach, vec3d.z-reach);

        EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(entityX, vec3d, vec3d3, box, entity -> !entity.isSpectator() && entity.isPickable() && !entity.isInvulnerable(), reach*reach);
        if (entityHitResult != null) {
            return entityHitResult.getEntity();
        } else {
            return null;
        }
    }

    private BlockHitResult getValidPlacement(){
        Vec3 vec3d = this.getSelf().getEyePosition(0);
        Vec3 vec3d2 = this.getSelf().getViewVector(0);
        Vec3 vec3d3 = vec3d.add(vec3d2.x * PlacementRange, vec3d2.y * PlacementRange, vec3d2.z * PlacementRange);
        BlockHitResult blockHit = this.getSelf().level().clip(new ClipContext(vec3d, vec3d3,
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.getSelf()));
        boolean cond = false;
        if (blockHit.getDirection() != Direction.UP && blockHit.getDirection() != Direction.DOWN) {
            BlockPos pos = blockHit.getBlockPos();
            if (this.getSelf().level().getBlockState(pos.above()).isAir()) {
                cond = true;
            }
        }
        if (blockHit.getType() == HitResult.Type.BLOCK && (blockHit.getDirection() == Direction.UP || cond) ) {
            return blockHit;
        }
        return null;
    }



    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        ClientUtil.fx.roundabout$onGUI(context);

        if (isPlaced()) {
            if (!getSelf().isCrouching()) {
                setSkillIcon(context, x, y, 1, StandIcons.RATT_BURST, PowersRatt.PLACE_BURST);
            } else {
                if (isAuto()) {
                    setSkillIcon(context, x, y, 1, StandIcons.RATT_AUTO, PowersRatt.CHANGE_MODE);
                } else {
                    setSkillIcon(context, x, y, 1, StandIcons.RATT_UNAUTO, PowersRatt.CHANGE_MODE);
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
            setSkillIcon(context, x, y, 1, ScopeIcon, PowersRatt.SCOPE);
            if (scopeLevel == 0) {
                setSkillIcon(context, x, y, 2, StandIcons.RATT_PLACE, PowersRatt.SETPLACE);
            } else {
                if (isAuto()) {
                    setSkillIcon(context, x, y, 2, StandIcons.RATT_BURST, PowersRatt.CHANGE_MODE);
                } else {
                    setSkillIcon(context, x, y, 2, StandIcons.RATT_SINGLE, PowersRatt.CHANGE_MODE);
                }
            }
        }
        setSkillIcon(context,x,y,3,StandIcons.DODGE,PowerIndex.GLOBAL_DASH);
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
                active = true;
                Placement = pos;
                this.setCooldown(PowersRatt.SETPLACE,80);
            }
            case ROTATE -> {

                if (this.getStandEntity(this.getSelf()) != null && this.getStandEntity(this.getSelf()) instanceof  RattEntity RE) {

                    RE.setHeadRotationX((float)pos.x);
                    RE.setStandRotationY((float)pos.y);
                }
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
  /*      ((ServerLevel) this.self.level()).sendParticles(ModParticles.FOG_CHAIN, pos.x(),
                pos.y(), pos.z(),
                14, 0.4, 0.2, 0.4, 0.35); */
    }

    public void blipStand(Vec3 pos) {
        StandEntity stand = getNewStandEntity();
        if (stand instanceof RattEntity RE) {
            StandUser user = getStandUserSelf();
            stand.setSkin(user.roundabout$getStandSkin());
            stand.setMaster(this.self);
            stand.absMoveTo(pos.x(),pos.y(),pos.z());
            this.getStandUserSelf().roundabout$standMount(stand);
            this.self.level().addFreshEntity(stand);
        }
    }


    Vec3 Placement = null;
    @Override
    public void tickPower() {
        super.tickPower();


        if (this.getSelf().level().isClientSide()) {
            if (this.getStandEntity(this.getSelf()) != null && this.getStandEntity(this.getSelf()) instanceof RattEntity RE) {
                Entity target = getShootTarget();
                Vec3 targetPos = getTargetPos().getLocation();
                if (target != null) {
                    targetPos = target.getEyePosition(0);
                }
                double x = (targetPos.x() - RE.getPosition(0).x());
                double z = (targetPos.z() - RE.getPosition(0).z());
                float rot = (float) (Math.atan2(z,x) - Math.PI/2) ;

                double hy = (targetPos.y() - (RE.getPosition(0).y() + 0.5));
                double hd = Math.sqrt(Math.pow(x,2)+Math.pow(z,2));
                float hrot = (float) (Math.atan2(hd,hy) + Math.PI/2); // flip the sign if you want it to be not armed

                tryPosPower(PowersRatt.ROTATE,true,new Vec3(hrot,rot,0));
                tryPosPowerPacket(PowersRatt.ROTATE,new Vec3(hrot,rot,0));
            }
        }









        if (shotcooldown != 0) {shotcooldown--;}
        if (shotcooldown == 0) {maxshotcooldown = 0;}

        if (scopeLevel == 0) {
            if (attackTime > 60 && this.getChargeTime() != 0) {
                chargeTime -= 2;
            }
        }


        StandEntity SE = this.getStandEntity(this.getSelf());

        if (isPlaced()) {

            if (this.isClient()) {
                Entity e = MainUtil.getTargetEntity(this.self, 30, 15);


                if (isAuto()) {
                 /*   Entity f = PowersRatt.CoolerrayCastEntity(SE,60);
                    if (f != null) {
                        if (!f.equals(this.getSelf())) {
                            BurstFire();
                        }
                    } */
                } else {
                    setShootTarget(null);
                    if (e instanceof LivingEntity) {
                        if (!e.equals(this.getSelf()) && !e.equals(this.getStandEntity(this.getSelf()))) {
                            setShootTarget(e);
                        }
                    }
                }


                if (e != null) {
                    if (e.distanceTo(SE) >= 30 ) {setShootTarget(null);}
                }

                setGoBeyondTarget(getShootTarget());
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
        if (this.getActivePower() == PowerIndex.GUARD) {
            updateChargeTime(Mth.clamp(getChargeTime()+3,0,100));

            if (getChargeTime() == 100) {this.setPowerNone();}
            if (scopeLevel == 0) {setPowerNone();}
        } else if (this.getActivePower() == PowersRatt.PLAYER_BURST) {
            if (isClient()) {
                if (this.attackTimeDuring%3 == 1) {
                    tryPower(PowersRatt.PLAYER_BURST,true);
                    tryPowerPacket(PowersRatt.PLAYER_BURST);
                }
                if (getChargeTime() < 30) {
                    this.updateChargeTime(0);
                    this.setShotCooldown(30);
                    tryPower(PowerIndex.NONE,true);
                    tryPowerPacket(PowerIndex.NONE);
                }
            }
        } else if (this.getActivePower() == PowersRatt.PLACE_BURST) {
            setShotCooldown(25);
            if (getAttackTimeDuring() >= 7) {
                setPowerNone();
                setAttackTimeDuring(-1);
            }
            if (this.attackTimeDuring%4 == 1) {
                tryPower(PowersRatt.PLACE_BURST_FIRE,true);
                tryPowerPacket(PowersRatt.PLACE_BURST_FIRE);
            }
        }
        super.updateUniqueMoves();
    }

    @Override
    public void powerActivate(PowerContext context) {

        switch (context)
        {
            case SKILL_1_NORMAL -> {
                if (!isPlaced()) {
                    RattScope();
                } else {
                    BurstFire();
                }
            }
            case SKILL_1_CROUCH -> {
                if (isPlaced()) {
                    if (!isAttackIneptVisually(PowersRatt.AUTO, 1)) {
                        ToggleAuto();
                    }
                }
            }
            case SKILL_2_NORMAL -> {
                if (scopeLevel != 0)  {
                    ToggleBursting();
                } else {
                    if (!isPlaced()) {
                        if (getValidPlacement() != null) {
                            DeployClient();
                        }
                    } else {
                        RecallClient();
                    }
                }
            }

            case SKILL_3_NORMAL, SKILL_3_CROUCH -> {
                dash();
            }
        }
    }

    public void RattScope() {
        if (!this.onCooldown(PowersRatt.SCOPE)) {
            int nl = scopeLevel + 1;
            tryIntPower(PowersRatt.NET_SCOPE, true,nl == 2 ? 0 : nl);
            tryIntPowerPacket(PowersRatt.NET_SCOPE,nl == 2 ? 0 : nl);
            if (nl != 2) {
                this.getSelf().playSound(ModSounds.RATT_SCOPE_EVENT, 1.0F, (float) (0.98F + (Math.random() * 0.04F)));
            }
        }

    }


    @Override
    public boolean tryIntPower(int move, boolean forced, int chargeTime) {
        switch (move) {
            case PowersRatt.NET_SCOPE -> {
                this.setCooldown(PowersRatt.SCOPE,10);
                this.setAttackTime(-1);
                setScopeLevel(chargeTime);
                this.getStandUserSelf().roundabout$setCombatMode(scopeLevel != 0);
            }
            case PowersRatt.UPDATE_CHARGE -> {
                this.chargeTime = chargeTime;
            }

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
        this.getStandUserSelf().roundabout$setUniqueStandModeToggle(!isAuto());
        tryPower(PowersRatt.TOGGLE_BURSTING,true);
        tryPowerPacket(PowersRatt.TOGGLE_BURSTING);
    }

    public void PlayerFire() {
        int i = getChargeTime();
        tryIntPower(PowersRatt.FIRE_DART,true,i);
        tryIntPowerPacket(PowersRatt.FIRE_DART,i);

    }


    public void BurstFire() {
        if (!isAttackIneptVisually(PowersRatt.PLACE_BURST,2)) {
            tryPower(PowersRatt.NET_PLACE_BURST, true);
            tryPowerPacket(PowersRatt.NET_PLACE_BURST);
        }
    }


    public void FireDart(int i, float acuracy) {
        float power = 0;
        for (int b=ShotThresholds.length-1;b>=0;b--) {
            if (i >= ShotThresholds[b]) {
                power = ShotPowerFloats[b];
                break;
            }
        }
        RattDartEntity e = new RattDartEntity(this.getSelf().level(),this.getSelf(),i);
        e.shootFromRotation(this.getSelf(), this.getSelf().getXRot(), this.getSelf().getYRot(), -0.5F, power, acuracy);
        e.EnableSuperThrow();
        this.getSelf().level().addFreshEntity(e);
    }

    public void ToggleAuto() {
        this.getStandUserSelf().roundabout$setUniqueStandModeToggle(!isAuto());
    }

    public void DeployClient() {
        if (!this.onCooldown(PowersRatt.SETPLACE)) {
            updateChargeTime(0);
            this.getSelf().playSound(ModSounds.RATT_SUMMON_EVENT, 1.0F, (float) (0.98F + (Math.random() * 0.04F)));
            BlockHitResult blockHitResult = getValidPlacement();
            if (blockHitResult != null) {
                Vec3 pos = blockHitResult.getLocation();
                if (blockHitResult.getDirection() != Direction.UP && blockHitResult.getDirection() != Direction.DOWN) {
                    pos = new Vec3(pos.x(), ((int) pos.y()) + 1, pos.z());
                }
                tryPosPower(PowersRatt.NET_PLACE, true, pos);
                tryPosPowerPacket(PowersRatt.NET_PLACE, pos);
            }
        }
    }


    public void RecallClient() {
        if (!this.onCooldown(PowersRatt.SETPLACE)) {
            tryPower(PowersRatt.NET_RECALL,true);
            tryPowerPacket(PowersRatt.NET_RECALL);
        }
    }

    @Override
    public boolean tryPower(int move, boolean forced) {
        switch (move) {
            case PowersRatt.NET_RECALL -> {
                active = false;
                this.getStandUserSelf().roundabout$setUniqueStandModeToggle(false);
                this.getStandEntity(this.getSelf()).remove(Entity.RemovalReason.DISCARDED);
                this.setCooldown(PowersRatt.SETPLACE,40);
            }
            case PowersRatt.FIRE_DART -> {
                this.setCooldown(PowersRatt.CHANGE_MODE,15);
            }
            case PowersRatt.START_PLAYER_BURST -> {
                this.setAttackTimeDuring(0);
                this.setActivePower(PowersRatt.PLAYER_BURST);
            }
            case PowersRatt.PLAYER_BURST -> {
                this.setCooldown(PowersRatt.SETPLACE,10);
                this.setActivePower(PowersRatt.PLAYER_BURST);
                chargeTime -= 30;
                if (!isClient()) {
                    FireDart(51,0.4F);
                }
            }
            case PowersRatt.TOGGLE_BURSTING -> {
                this.setCooldown(PowersRatt.CHANGE_MODE,10);
            }
            case PowersRatt.NET_PLACE_BURST -> {
                this.setAttackTimeDuring(0);
                this.setActivePower(PowersRatt.PLACE_BURST);
            }
            case PowersRatt.PLACE_BURST_FIRE -> {
                if (isClient()) {
                } else {

                    float power = 0;
                    for (int b=ShotThresholds.length-1;b>=0;b--) {
                        if (51 >= ShotThresholds[b]) {
                            power = ShotPowerFloats[b];
                            break;
                        }
                    }
                    if (this.getStandEntity(this.getSelf()) instanceof RattEntity RE) {
                        RattDartEntity e = new RattDartEntity(RE.level(), this.getSelf(), 51);
                        e.shootFromRotation(RE, RE.getHeadRotationX()*180/(float)Math.PI+180, RE.getStandRotationY()*180/(float)Math.PI, -0.5F, power, 0.1F);
                        e.EnableSuperThrow();
                        RE.level().addFreshEntity(e);
                    }

                }
            }
        }
        return super.tryPower(move, forced);
    }

    @Override
    public boolean setPowerNone() {
        if (!isPlaced()) {
            return super.setPowerNone();
        }
        return true;
    }

    @Override
    public float inputSpeedModifiers(float basis) {
        if (scopeLevel != 0){
            basis*=0.5f;
        }
        return super.inputSpeedModifiers(basis);
    }

    @Override
    public boolean isAttackIneptVisually(byte activeP, int slot) {
        switch (activeP) {
            case PowersRatt.AUTO -> {
                return getShootTarget() == null && this.getSelf().isCrouching() && !isAuto() && isPlaced();
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
                return shotcooldown != 0;
            }
            case PowersRatt.CHANGE_MODE -> {
                if (scopeLevel != 0) {
                    return getChargeTime() <= MinThreshold || shotcooldown != 0;
                } else if (isPlaced()) {
                    return !isAuto() && getShootTarget() == null;
                }
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
                        if (isAuto()) {
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
    public boolean interceptGuard() {return   scopeLevel != 0;}
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
    public void renderAttackHud(GuiGraphics context, Player playerEntity, int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount, float flashAlpha, float otherFlashAlpha) {
        int j = scaledHeight / 2 - 7 - 4;
        int k = scaledWidth / 2 - 8;


        if (shotcooldown != 0) {
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            float ratio = (float) shotcooldown /maxshotcooldown;
            int fifteen = 15-Math.round(ratio*15);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 12, fifteen, 6);

        } else if (getChargeTime() >= 10 || scopeLevel != 0) {
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6    , 15, 6);
            float amount = (float) getChargeTime() /100;
            int finalAmount = Math.round(amount*15);
            int bartexture = 30;
            if (getChargeTime() >= MaxThreshold) {bartexture -= 6;}
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, bartexture, finalAmount, 6);
        }
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
        return Arrays.asList(
                RattEntity.ANIME_SKIN,
                RattEntity.MANGA_SKIN,
                RattEntity.MELON_SKIN,
                RattEntity.SAND_SKIN,
                RattEntity.AZTEC_SKIN,
                RattEntity.TOWER_SKIN,
                RattEntity.SNOWY_SKIN
        );
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
            case RattEntity.SNOWY_SKIN -> {return Component.translatable("skins.roundabout.ratt.snowy");}
            default -> {return Component.translatable("skins.roundabout.ratt.anime");}
        }
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
        byte bt = ((StandUser)this.getSelf()).roundabout$getStandSkin();
        if (soundChoice == SoundIndex.SUMMON_SOUND) {
            return ModSounds.RATT_SUMMON_EVENT;
        }
        return super.getSoundFromByte(soundChoice);
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