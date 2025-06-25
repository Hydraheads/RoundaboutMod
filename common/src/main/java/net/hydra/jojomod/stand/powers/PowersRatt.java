package net.hydra.jojomod.stand.powers;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.D4CEntity;
import net.hydra.jojomod.entity.stand.RattEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.mixin.InputEvents;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class PowersRatt extends NewDashPreset {
    public PowersRatt(LivingEntity self) {
        super(self);
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersRatt(entity);
    }

    public static float PlacementRange = 5.0F;
    public static float PilotRange = 3.0F;
    public static float ActiveRange = 10.0F;
    public static float DormantRange = 12.5F;



    public boolean isRattState(int state) {
        StandEntity  re = getStandEntity(this.self);
        if (re != null) {
            return ((RattEntity) re).MotionState == state;
        }
        return false;
    }


    private BlockHitResult getValidPlacement(){
        Vec3 vec3d = this.getSelf().getEyePosition(0);
        Vec3 vec3d2 = this.getSelf().getViewVector(0);
        Vec3 vec3d3 = vec3d.add(vec3d2.x * PlacementRange, vec3d2.y * PlacementRange, vec3d2.z * PlacementRange);
        BlockHitResult blockHit = this.getSelf().level().clip(new ClipContext(vec3d, vec3d3,
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.getSelf()));
        if (blockHit.getType() == HitResult.Type.BLOCK && blockHit.getDirection() == Direction.UP){
            return blockHit;
        }
        return null;
    }


    @Override
    public boolean canSummonStandAsEntity() {return false;}

    @Override
    public StandEntity getNewStandEntity(){return ModEntities.RATT.create(this.getSelf().level());}

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        ClientUtil.fx.roundabout$onGUI(context);

        RattEntity RE = ((RattEntity) getStandEntity(this.self) );

        if (isRattState(0)) {

            ResourceLocation ScopeIcon = StandIcons.STAR_PLATINUM_SCOPE;
            if (scopeLevel == 1) {
                ScopeIcon = StandIcons.STAR_PLATINUM_SCOPE_1;
            }
            if (scopeLevel == 2) {
                ScopeIcon = StandIcons.STAR_PLATINUM_SCOPE_2;
            }
            setSkillIcon(context, x, y, 1, ScopeIcon, PowerIndex.SKILL_1);

            setSkillIcon(context, x, y, 2, StandIcons.JUSTICE_PILOT, PowerIndex.SKILL_2);
        } else if (isRattState(1)) {
            setSkillIcon(context, x, y, 1, StandIcons.NONE, PowerIndex.SKILL_1);
            setSkillIcon(context, x, y, 2, StandIcons.NONE, PowerIndex.SKILL_2);
        } else if (isRattState(2)) {
            setSkillIcon(context, x, y, 1, StandIcons.NONE, PowerIndex.SKILL_1);
            setSkillIcon(context, x, y, 2, StandIcons.JUSTICE_PILOT_EXIT, PowerIndex.SKILL_2);
        } else if (isRattState(3)) {
            // nothing yet, will have piloting
        }

        if (!isRattState(3)) {
            setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
        }
        super.renderIcons(context, x, y);
    }

    @Override
    public void tick() {
    }

    @Override
    public void powerActivate(PowerContext context) {


        switch (context)
        {
            case SKILL_1_NORMAL -> {
                if (isRattState(0)) {
                    RattScope();
                }
            }
            case SKILL_2_NORMAL -> {
                if (isRattState(0) ) {
                    Deploy();
                } else {
                    Recall();
                }
            }

            case SKILL_3_NORMAL -> {
                dash();
            }
        }
    }

    public void Deploy() {
        RattEntity RE = ((RattEntity) getStandEntity(this.self) );
        Vec3 p = this.getRayBlock(RE.getUser(),PlacementRange);

        if (RE != null) {
            if (getValidPlacement() != null) {
                RE.setPlacement(p);
                RE.setMotionState(1);
                /* highlight Ratt for user ? */
            }
        }
    }

    public void Recall() {
        RattEntity RE = ((RattEntity) getStandEntity(this.self));
        if (RE != null) {
            RE.setPlacement(null);
            RE.setMotionState(1);
        }
            this.setCooldown(PowerIndex.SKILL_2, 100);
    }

    public void RattScope() {
        /* will eventually add some sort of scope effect  */
        int nl = scopeLevel + 1;
        if (nl == 2) {
            setScopeLevel(0);
        } else {
            setScopeLevel(nl);
            this.getSelf().playSound(ModSounds.STAR_PLATINUM_SCOPE_EVENT, 1.0F, (float) (0.98F + (Math.random() * 0.04F)));
        }

    }




    @Override
    public boolean tryPower(int move, boolean forced) {
        return super.tryPower(move, forced);
    }

    @Override
    public boolean isAttackIneptVisually(byte activeP, int slot) {
        switch (activeP) {
            case PowerIndex.SKILL_2 -> {
                return getValidPlacement() == null && isRattState(0);
            }
        }
        return super.isAttackIneptVisually(activeP, slot);
    }

    /** if = -1, not melt dodging */
    public int meltDodgeTicks = -1;

    @Override
    public void tickPower() {
        super.tickPower();
    }


    @Override
    public boolean canScope() {
        return true;
    }

    @Override
    public List<Byte> getSkinList() {
        return Arrays.asList(
                RattEntity.ANIME_SKIN
        );
    }

    @Override
    public boolean setPowerOther(int move, int lastMove) {

        return super.setPowerOther(move, lastMove);
    }

    @Override
    public void updateIntMove(int in) {

        super.updateIntMove(in);
    }

    @Override
    public void updateUniqueMoves() {
        super.updateUniqueMoves();
    }



    public Component getPosName(byte posID) {
        if (posID == 1) {
            return Component.translatable("idle.roundabout.ratt_1");
        } else {
            return Component.translatable("idle.roundabout.ratt_2");
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
        return Component.literal(  "Prisma").withStyle(ChatFormatting.YELLOW);
    }
}