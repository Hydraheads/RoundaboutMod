package net.hydra.jojomod.stand.powers;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.RattEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.mixin.StandUserEntity;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
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
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec2;
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
    public StandEntity getNewStandEntity(){return ModEntities.RATT.create(this.getSelf().level());}

    public static float PlacementRange = 5.0F;

    public static final byte
            SHOULDER = 0,
            MOVING = 1,
            PLACED = 2,
            RETURNING = 3,

            UPDATE_POSITION = 4,
            UPDATE_STATE = 5,
            UPDATE_OFFSET_TYPE = 6,
            UPDATE_PLACEMENT = 7,
            NULL_PLACEMENT = 8,
            ENABLE_ACTIVE = 9,
            DISABLE_ACTIVE = 10,
            ROTATE = 11;


    public boolean active = false;
    @Override
    public boolean canSummonStandAsEntity() {
        return active;
    }

    public byte getRattState() {
        RattEntity RE = (RattEntity) getStandUserSelf().roundabout$getStand();
        if (RE != null) {
            return RE.MotionState;
        } else if (this.getStandUserSelf().roundabout$getActive()) {
            return SHOULDER;
        }
        return -1;
    }
    public void setRattState(byte b) {
        RattEntity RE = (RattEntity) getStandUserSelf().roundabout$getStand();
        if (RE != null) {
            RE.UpdateMotionState(b);
        }
    }
    public boolean isRattState(byte b) {
        RattEntity RE = (RattEntity) getStandUserSelf().roundabout$getStand();
        if (RE != null) {
            return RE.MotionState == b;
        } else if (b == SHOULDER) {
                return true;
        }
        return false;
    }


    public boolean isAuto() {
        return this.getStandUserSelf().roundabout$getUniqueStandModeToggle();
    }
    public void setAuto(boolean b) {
        this.getStandUserSelf().roundabout$setUniqueStandModeToggle(b);
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
        if (blockHit.getType() == HitResult.Type.BLOCK && blockHit.getDirection() == Direction.UP || cond){
            return blockHit;
        }
        return null;
    }




    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        ClientUtil.fx.roundabout$onGUI(context);

        switch(getRattState()) {
            case SHOULDER -> {
                ResourceLocation ScopeIcon = StandIcons.RATT_SCOPE_IN;
                if (scopeLevel == 1) {
                    ScopeIcon = StandIcons.RATT_SCOPE_OUT;
                }
                setSkillIcon(context, x, y, 1, ScopeIcon, PowerIndex.SKILL_1);
                setSkillIcon(context,x,y,2,StandIcons.RATT_PLACE,PowerIndex.SKILL_2);
            }
            case MOVING -> {
                setSkillIcon(context,x,y,1,StandIcons.NONE, PowerIndex.SKILL_1);
                setSkillIcon(context,x,y,2,StandIcons.RATT_RECALL,PowerIndex.SKILL_2);
            }
            case PLACED -> {
                if (!getSelf().isCrouching()) {
                    setSkillIcon(context,x,y,1,StandIcons.RATT_BURST, PowerIndex.SKILL_1);
                } else {
                    if (isAuto()) {
                        setSkillIcon(context, x, y, 1, StandIcons.RATT_AUTO, PowerIndex.SKILL_1);
                    } else {
                        setSkillIcon(context,x,y,1,StandIcons.RATT_UNAUTO,PowerIndex.SKILL_1);
                    }
                }
                setSkillIcon(context,x,y,2,StandIcons.RATT_RECALL,PowerIndex.SKILL_2);
            }
        }
        setSkillIcon(context,x,y,3,StandIcons.DODGE,PowerIndex.GLOBAL_DASH);
    }


    @Override
    public boolean tryPower(int move, boolean forced) {
        switch(move) {
            case NULL_PLACEMENT -> {
                ((RattEntity) this.getStandEntity(this.getSelf()) ).Placement = null;
            }
            case ENABLE_ACTIVE -> {
                active = true;
                if (!this.isClient()) {
                    this.getStandUserSelf().roundabout$summonStand(this.getSelf().level(), true, false);
                }
            }
            case DISABLE_ACTIVE -> {
                active = false;
            }
        }
        return super.tryPower(move, forced);
    }
    @Override
    public boolean tryPosPower(int move, boolean forced, Vec3 pos) {
        switch(move) {
            case UPDATE_POSITION -> {
                if (this.getStandEntity(this.getSelf()) != null) {
                    this.getStandEntity(this.getSelf()).setPos(pos);
                }
            }
            case UPDATE_PLACEMENT -> {
                if (this.getStandEntity(this.getSelf()) != null) {
                    ((RattEntity) this.getStandEntity(this.getSelf())).Placement = pos;
                }
            }
            case ROTATE -> {
                if (this.getStandEntity(this.getSelf()) != null) {
                    this.getStandEntity(this.getSelf()).setStandRotationY((float) pos.y);
                }
            }
        }
        return true;
    }

    @Override
    public boolean tryIntPower(int move, boolean forced, int chargeTime) {
        if (this.getStandEntity(this.getSelf()) != null) {
            switch (move) {
                case UPDATE_OFFSET_TYPE -> {
                    this.getStandEntity(this.getSelf()).setOffsetType((byte) chargeTime);
                }
                case UPDATE_STATE -> {
                    ((RattEntity)this.getStandEntity(this.getSelf())).MotionState = (byte) chargeTime;
                }

            }
        }
        return true;
    }


    @Override
    public void tickPower() {
        StandEntity SE = getStandEntity(this.getSelf());
        if ( SE != null && SE.getOffsetType() == OffsetIndex.FOLLOW) {
            if (active) {
                Deploy();
            } else {
                if (!this.getSelf().level().isClientSide()) {
                    SE.remove(Entity.RemovalReason.DISCARDED);
                }
            }
        }
        super.tickPower();
    }


    @Override
    public void powerActivate(PowerContext context) {

        switch (context)
        {
            case SKILL_1_NORMAL -> {
                if (isRattState(SHOULDER)) {
                    RattScope();
                } else if (isRattState(PLACED)) {
                        BurstFire();
                }
            }
            case SKILL_1_CROUCH -> {
                ToggleAuto();
            }
            case SKILL_2_NORMAL -> {
                if (isRattState(SHOULDER) ) {
                    if (getValidPlacement() != null) {
                        Deploy();
                    }
                } else {
                    Recall();
                }
            }

            case SKILL_3_NORMAL -> {
                dash();
            }
        }
    }

    public void RattScope() {
        /* will eventually add some sort of scope overlay  */
        int nl = scopeLevel + 1;
        if (nl == 2) {
            setScopeLevel(0);
        } else {
            setScopeLevel(nl);
            this.getSelf().playSound(ModSounds.STAR_PLATINUM_SCOPE_EVENT, 1.0F, (float) (0.98F + (Math.random() * 0.04F)));
        }

    }

    public void BurstFire() {
        Roundabout.LOGGER.info("BURST FIRE RAAAH");
    }

    public void ToggleAuto() {
        setAuto(!isAuto());
    }

    public void Deploy() {
        if (!this.onCooldown(PowerIndex.POWER_2)) {
            RattEntity RE = (RattEntity) this.getStandUserSelf().roundabout$getStand();
            if (RE != null) {
                if (getValidPlacement() != null) {
                    this.setCooldown(PowerIndex.SKILL_2, 70);
                    setRattState(MOVING);
                    if (getValidPlacement().getDirection() == Direction.UP) {
                        RE.UpdatePlacement(getValidPlacement().getLocation());
                    } else {
                        Vec3 pos2 = getValidPlacement().getLocation();
                        Vec3 pos;
                        pos = new Vec3(pos2.x(),((int)pos2.y())+1,pos2.z());
                        RE.UpdatePlacement(pos);
                    }
                }
            } else {
                tryPower(ENABLE_ACTIVE, true);
                tryPowerPacket(ENABLE_ACTIVE);
            }
        }
    }


    public void Recall() {
        if (!this.onCooldown(PowerIndex.POWER_2)) {
            RattEntity RE = (RattEntity) this.getStandUserSelf().roundabout$getStand();
            if (RE != null) {
                this.setCooldown(PowerIndex.SKILL_2, 40);
                RE.NullPlacement();
                tryPower(DISABLE_ACTIVE, true);
                tryPowerPacket(DISABLE_ACTIVE);
            }
        }
    }


    @Override
    public boolean setPowerNone() {
        if (isRattState(SHOULDER)) {
            return super.setPowerNone();
        }
        return true;
    }

    @Override
    public boolean isAttackIneptVisually(byte activeP, int slot) {
        switch (activeP) {
            case PowerIndex.SKILL_2 -> {
                return getValidPlacement() == null && isRattState(SHOULDER);
            }
        }
        return super.isAttackIneptVisually(activeP, slot);
    }

    /** if = -1, not melt dodging */
    public int meltDodgeTicks = -1;



    @Override
    public boolean canScope() {
        return getStandUserSelf().roundabout$getActive() && isRattState(SHOULDER);
    }

    @Override
    public List<Byte> getSkinList() {
        return Arrays.asList(
                RattEntity.ANIME_SKIN,
                RattEntity.MANGA_SKIN,
                RattEntity.MELON_SKIN,
                RattEntity.SAND_SKIN
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
    protected Byte getSummonSound() {return SoundIndex.SUMMON_SOUND;}
    public SoundEvent getSoundFromByte(byte soundChoice){
        byte bt = ((StandUser)this.getSelf()).roundabout$getStandSkin();
        if (soundChoice == SoundIndex.SUMMON_SOUND) {
            return ModSounds.SUMMON_SOUND_EVENT;
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