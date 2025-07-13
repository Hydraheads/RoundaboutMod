package net.hydra.jojomod.stand.powers;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.RattEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
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

    public static float PlacementRange = 5.0F;

    public static final byte
            UPDATE_POSITION = 0,
            ROTATE = 1;

    public Entity ShootTarget = null;
    public Entity getShootTarget() {return ShootTarget;}
    public void setShootTarget(Entity e) {ShootTarget = e;}

    boolean active = false;
    @Override
    public boolean canSummonStandAsEntity() {return false;}


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
                setSkillIcon(context, x, y, 1, StandIcons.RATT_BURST, PowerIndex.SKILL_1);
            } else {
                if (isAuto()) {
                    setSkillIcon(context, x, y, 1, StandIcons.RATT_AUTO, PowerIndex.SKILL_1);
                } else {
                    setSkillIcon(context, x, y, 1, StandIcons.RATT_UNAUTO, PowerIndex.SKILL_1);
                }
            }
            setSkillIcon(context, x, y, 2, StandIcons.RATT_RECALL, PowerIndex.SKILL_2);
        } else {
            ResourceLocation ScopeIcon = StandIcons.RATT_SCOPE_IN;
            if (scopeLevel == 1) {
                ScopeIcon = StandIcons.RATT_SCOPE_OUT;
            }
            setSkillIcon(context, x, y, 1, ScopeIcon, PowerIndex.SKILL_1);
            setSkillIcon(context, x, y, 2, StandIcons.RATT_PLACE, PowerIndex.SKILL_2);
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
                active = true;
                Placement = pos;
                this.setCooldown(PowerIndex.POWER_2,80);
            }
        }
        return true;
    }

    public void placeRatt(Vec3 pos) {
        int cooldown = 60;
        this.setCooldown(PowerIndex.SKILL_2, cooldown);
        if (!isClient()) {
            blipStand(pos);
        }
  /*      ((ServerLevel) this.self.level()).sendParticles(ModParticles.FOG_CHAIN, pos.x(),
                pos.y(), pos.z(),
                14, 0.4, 0.2, 0.4, 0.35); */
    }

    public void blipStand(Vec3 pos) {
        StandEntity stand = ModEntities.RATT.create(this.getSelf().level());
        if (stand instanceof RattEntity RE) {
            StandUser user = getStandUserSelf();
            stand.setSkin(user.roundabout$getStandSkin());
            stand.setMaster(this.self);
            stand.absMoveTo(pos.x(),pos.y(),pos.z());
            stand.setFadeOut((byte)2);
            this.getStandUserSelf().roundabout$standMount(stand);
            this.self.level().addFreshEntity(stand);
        }
    }


    Vec3 Placement = null;
    @Override
    public void tickPower() {
        super.tickPower();



        StandEntity SE = this.getStandEntity(this.getSelf());

        if (isPlaced()) {

            if (this.isClient()) {
                Entity e = MainUtil.getTargetEntity(this.self, 30, 15);


                if (!isAuto()) {
                    if (e instanceof LivingEntity && !e.equals(SE) ) {
                        setShootTarget(e);
                    }
                }
                if (e != null) {
                    if (e.distanceTo(SE) >= 30 ) {setShootTarget(null);}
                }

                setGoBeyondTarget(getShootTarget());
            }
        } else if (active) {
            if (!isClient()) {
                if (Placement != null) {
                    placeRatt(Placement);
                }
            }

        }
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
                if (!isPlaced()) {
                    if (getValidPlacement() != null) {
                        DeployClient();
                    }
                } else {
                    RecallClient();
                }
            }

            case SKILL_3_NORMAL -> {
                dash();
            }
        }
    }

    public void RattScope() {
        int nl = scopeLevel + 1;
        if (nl == 2) {
            setScopeLevel(0);
        } else {
            setScopeLevel(nl);
            this.getSelf().playSound(ModSounds.RATT_SCOPE_EVENT, 1.0F, (float) (0.98F + (Math.random() * 0.04F)));
        }

    }

    public void BurstFire() {
        Roundabout.LOGGER.info("BURST FIRE RAAAH");
    }

    public void ToggleAuto() {
        this.getStandUserSelf().roundabout$setUniqueStandModeToggle(!isAuto());
    }

    public void DeployClient() {
        if (!this.onCooldown(PowerIndex.POWER_2)) {
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
        if (!this.onCooldown(PowerIndex.POWER_2)) {
            tryPower(PowerIndex.POWER_2_SNEAK,true);
            tryPowerPacket(PowerIndex.POWER_2_SNEAK);
        }
    }

    @Override
    public boolean tryPower(int move, boolean forced) {
        switch (move) {
            case PowerIndex.POWER_2_SNEAK -> {
                active = false;
                this.getStandEntity(this.getSelf()).remove(Entity.RemovalReason.DISCARDED);
                this.setCooldown(PowerIndex.POWER_2,40);
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
    public boolean isAttackIneptVisually(byte activeP, int slot) {
        switch (activeP) {
            case PowerIndex.SKILL_2 -> {
                return getValidPlacement() == null && !isPlaced();
            }
            case PowerIndex.SKILL_1 -> {
                return getShootTarget() == null && this.getSelf().isCrouching() && !isAuto() && isPlaced();
            }
        }
        return super.isAttackIneptVisually(activeP, slot);
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