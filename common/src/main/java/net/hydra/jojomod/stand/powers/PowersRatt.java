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
import net.hydra.jojomod.mixin.StandUserEntity;
import net.hydra.jojomod.sound.ModSounds;
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
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

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
    public static final int[] ShotSuperthrowTicks = {4,10,15};


    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersRatt(entity);
    }


    @Override
    public StandEntity getNewStandEntity(){
        byte sk = ((StandUser)this.getSelf()).roundabout$getStandSkin();
        switch(sk) {
            case RattEntity.REDD_SKIN -> {return ModEntities.EYEBROW_RATT.create(this.getSelf().level());}
            default -> {return ModEntities.RATT.create(this.getSelf().level());}
        }
    }

    public void setCooldown(int i) {
        shotcooldown = i;
        maxshotcooldown = i;
    }


    public static float PlacementRange = 5.0F;

    public static final byte
            UPDATE_POSITION = 0,
            ROTATE = 1,
            UPDATE_SCOPE = 2,
            UPDATE_CHARGE = 3;

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
                setSkillIcon(context, x, y, 1, StandIcons.RATT_BURST, PowerIndex.SKILL_1_GUARD);
            } else {
                if (isAuto()) {
                    setSkillIcon(context, x, y, 1, StandIcons.RATT_AUTO, PowerIndex.SKILL_1_SNEAK);
                } else {
                    setSkillIcon(context, x, y, 1, StandIcons.RATT_UNAUTO, PowerIndex.SKILL_1_SNEAK);
                }
            }
            if (scopeLevel == 0) {
                setSkillIcon(context, x, y, 2, StandIcons.RATT_RECALL, PowerIndex.SKILL_2);
            }
        } else {
            ResourceLocation ScopeIcon = StandIcons.RATT_SCOPE_IN;
            if (scopeLevel == 1) {
                ScopeIcon = StandIcons.RATT_SCOPE_OUT;
            }
            setSkillIcon(context, x, y, 1, ScopeIcon, PowerIndex.SKILL_1);
            if (scopeLevel == 0) {
                setSkillIcon(context, x, y, 2, StandIcons.RATT_PLACE, PowerIndex.SKILL_2);
            } else {
                if (isAuto()) {
                    setSkillIcon(context, x, y, 2, StandIcons.RATT_BURST, PowerIndex.SKILL_1_SNEAK);
                } else {
                    setSkillIcon(context, x, y, 2, StandIcons.RATT_SINGLE, PowerIndex.SKILL_1_SNEAK);
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
            case ROTATE -> {
                if (SE != null) {
                    SE.setYBodyRot((float) pos.y);
                }
            }
            case PowerIndex.POWER_2 -> {
                this.getStandUserSelf().roundabout$setUniqueStandModeToggle(false);
                active = true;
                Placement = pos;
                this.setCooldown(PowerIndex.SKILL_2,80);
            }
        }
        return true;
    }

    public void placeRatt(Vec3 pos) {
        int cooldown = 60;
        this.setCooldown(PowerIndex.POWER_2, cooldown);
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

        Roundabout.LOGGER.info("A: {}, B: {}", this.getActivePower(), this.getAttackTimeDuring());

        if (shotcooldown != 0) {shotcooldown--;}
        if (shotcooldown == 0) {maxshotcooldown = 0;}

        if (scopeLevel == 0) {
            if (attackTime > 20 && this.getChargeTime() != 0) {
                chargeTime -= 2;
            }
        }


        StandEntity SE = this.getStandEntity(this.getSelf());

        if (isPlaced()) {

            if (this.isClient()) {
                Entity e = MainUtil.getTargetEntity(this.self, 30, 15);


                if (isAuto()) {
                    if (e != null) {
                        if (e instanceof LivingEntity T) {
                            if (!T.equals(SE)) {
                                setShootTarget(e);
                            }
                        }
                    }
                } else {
                    if (e != null) {
                        if (e instanceof LivingEntity T) {
                            if (!T.equals(SE)) {
                                setShootTarget(e);
                            }
                        }
                    } else {
                        setShootTarget(e);
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

            if (getChargeTime() == 100) {this.setActivePower(PowerIndex.NONE);}
            if (scopeLevel == 0) {setActivePower(PowerIndex.NONE);}
        } else if (this.getActivePower() == PowerIndex.POWER_1_BLOCK) {
            if (isClient()) {
                if (this.attackTimeDuring%8 == 4) {
                    Roundabout.LOGGER.info("FIIIRE");
                    tryPower(PowerIndex.POWER_1_BLOCK,true);
                    tryPowerPacket(PowerIndex.POWER_1_BLOCK);
                }
                if (this.chargeTime < 30) {
                    this.updateChargeTime(0);
                    this.setCooldown(30);
                    this.setActivePower(PowerIndex.NONE);
                }
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
                    if (!isAttackIneptVisually(PowerIndex.SKILL_1, 1)) {
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

            case SKILL_3_NORMAL -> {
                dash();
            }
        }
    }

    public void RattScope() {
        if (!this.onCooldown(PowerIndex.SKILL_1)) {
            tryPower(PowerIndex.POWER_1, true);
            tryPowerPacket(PowerIndex.POWER_1);
            int nl = scopeLevel + 1;
            if (nl == 2) {
                UpdateScope(0);
            } else {
                UpdateScope(nl);
                this.getSelf().playSound(ModSounds.RATT_SCOPE_EVENT, 1.0F, (float) (0.98F + (Math.random() * 0.04F)));
            }
        }

    }


    private void UpdateScope(int scope) {
        tryIntPower(PowersRatt.UPDATE_SCOPE,true,scope);
        tryIntPowerPacket(PowersRatt.UPDATE_SCOPE,scope);
    }

    @Override
    public boolean tryIntPower(int move, boolean forced, int chargeTime) {
        switch (move) {
            case PowersRatt.UPDATE_SCOPE -> {
                setScopeLevel(chargeTime);
                this.getStandUserSelf().roundabout$setCombatMode(scopeLevel != 0);
            }
            case PowersRatt.UPDATE_CHARGE -> {
                this.chargeTime = chargeTime;
            }

            case PowerIndex.POWER_1_SNEAK -> {
                setCooldown(chargeTime >= MaxThreshold ? MaxShootCooldown : BaseShootCooldown);
                this.setCooldown(PowerIndex.SKILL_2,30);
                if (!isClient()) {
                    FireDart(chargeTime);
                }  else {
                    updateChargeTime(0);
                }

            }


        }
        return super.tryIntPower(move, forced, chargeTime);
    }

    public void ToggleBursting() {
        this.getStandUserSelf().roundabout$setUniqueStandModeToggle(!isAuto());
        tryPower(PowerIndex.POWER_1_BONUS,true);
        tryPowerPacket(PowerIndex.POWER_1_BONUS);
        this.setCooldown(PowerIndex.SKILL_1_SNEAK,10);
    }

    public void PlayerFire() {
        int i = getChargeTime();
        tryIntPower(PowerIndex.POWER_1_SNEAK,true,i);
        tryIntPowerPacket(PowerIndex.POWER_1_SNEAK,i);

    }
    public void PlayerBurstFire() {
        tryPower(PowerIndex.SNEAK_ATTACK,true);
        tryPowerPacket(PowerIndex.SNEAK_ATTACK);

    }


    public void BurstFire() {
        Roundabout.LOGGER.info("BURST FIRE RAAAH");
    }


    public void FireDart(int i) {
        float power = 0;
        for (int b=ShotThresholds.length-1;b>=0;b--) {
            if (i >= ShotThresholds[b]) {
                power = ShotPowerFloats[b];
                break;
            }
        }
        RattDartEntity e = new RattDartEntity(this.getSelf().level(),this.getSelf(),i);
        e.shootFromRotation(this.getSelf(), this.getSelf().getXRot(), this.getSelf().getYRot(), -0.5F, power, 0.2F);
        e.EnableSuperThrow();
        this.getSelf().level().addFreshEntity(e);
    }

    public void ToggleAuto() {
        this.getStandUserSelf().roundabout$setUniqueStandModeToggle(!isAuto());
    }

    public void DeployClient() {
        if (!this.onCooldown(PowerIndex.SKILL_2)) {
            updateChargeTime(0);
            this.getSelf().playSound(ModSounds.RATT_SUMMON_EVENT, 1.0F, (float) (0.98F + (Math.random() * 0.04F)));
            BlockHitResult blockHitResult = getValidPlacement();
            if (blockHitResult != null) {
                Vec3 pos = blockHitResult.getLocation();
                if (blockHitResult.getDirection() != Direction.UP && blockHitResult.getDirection() != Direction.DOWN) {
                    pos = new Vec3(pos.x(), ((int) pos.y()) + 1, pos.z());
                }
                tryPosPower(PowerIndex.POWER_2, true, pos);
                tryPosPowerPacket(PowerIndex.POWER_2, pos);
            }
        }
    }


    public void RecallClient() {
        if (!this.onCooldown(PowerIndex.SKILL_2)) {
            tryPower(PowerIndex.POWER_2_SNEAK,true);
            tryPowerPacket(PowerIndex.POWER_2_SNEAK);
        }
    }

    @Override
    public boolean tryPower(int move, boolean forced) {
        switch (move) {
            case PowerIndex.POWER_2_SNEAK -> {
                active = false;
                this.getStandUserSelf().roundabout$setUniqueStandModeToggle(false);
                this.getStandEntity(this.getSelf()).remove(Entity.RemovalReason.DISCARDED);
                this.setCooldown(PowerIndex.SKILL_2,40);
            }
            case PowerIndex.POWER_1 -> {
                this.setCooldown(PowerIndex.SKILL_1,10);
                this.setAttackTime(-1);
            }
            case PowerIndex.POWER_1_SNEAK -> {
                this.setCooldown(PowerIndex.SKILL_1_SNEAK,15);
            }
            case PowerIndex.SNEAK_ATTACK -> {
                this.setAttackTimeDuring(0);
                this.setActivePower(PowerIndex.POWER_1_BLOCK);
            }
            case PowerIndex.POWER_1_BLOCK -> {
                this.setCooldown(PowerIndex.SKILL_2,30);
                this.setActivePower(PowerIndex.POWER_1_BLOCK);
                if (!isClient()) {
                    FireDart(49);
                }  else {
                    updateChargeTime(getChargeTime()-30);
                }
            }
            case PowerIndex.POWER_1_BONUS -> {
                this.setCooldown(PowerIndex.SKILL_1_SNEAK,30);
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
            case PowerIndex.SKILL_1 -> {
                return getShootTarget() == null && this.getSelf().isCrouching() && !isAuto() && isPlaced();
            }
            case PowerIndex.SKILL_2 -> {
                if (isPlaced()) {
                    return false;
                } else {
                    if (scopeLevel == 0) {
                        return getValidPlacement() == null && !isPlaced();
                    }
                }
            }
            case PowerIndex.SKILL_1_SNEAK -> {
                if (scopeLevel != 0) {
                    return getChargeTime() <= MinThreshold || shotcooldown != 0;
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
            if (this.getActivePower() == PowerIndex.NONE) {
                if (!isAttackIneptVisually(PowerIndex.SKILL_1_SNEAK, 2)) {
                    if (isAuto()) {
                        PlayerBurstFire();
                    } else {
                        PlayerFire();
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
                RattEntity.REDD_SKIN,
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
            case RattEntity.REDD_SKIN -> {return Component.translatable("skins.roundabout.ratt.redd");}
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