package net.hydra.jojomod.stand.powers;

import com.mojang.blaze3d.systems.RenderSystem;
import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.PurpleHazeEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.CooldownInstance;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewPunchingStand;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.config.ConfigManager;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.List;

public class PowersPurpleHaze extends NewPunchingStand {
    public PowersPurpleHaze(LivingEntity self) {super(self);}

    @Override public Component getSkinName(byte skinId) {
        switch (skinId)
        {
            case PurpleHazeEntity.DEFAULT_PURPLE_HAZE -> {return Component.translatable("skins.roundabout.purple_haze.default_purple_haze");}
            case PurpleHazeEntity.MIG_PLAGUE -> {return Component.translatable("skins.roundabout.purple_haze.mig_plague");}
            default -> {return Component.translatable("skins.roundabout.purple_haze.default_purple_haze");}
        }
    }

    public int getLeapLevel(){return 3;}
    public int bonusLeapCount = -2;
    public int spacedJumpTime = -1;

    @Override
    public List<Byte> getSkinList() {
        return Arrays.asList(
                PurpleHazeEntity.DEFAULT_PURPLE_HAZE,
                PurpleHazeEntity.MIG_PLAGUE
        );
    }

    @Override
    public boolean canSummonStand () {
        return true;
    }

    @Override
    public boolean isMiningStand () {
        return true;
    }

    @Override
    public StandPowers generateStandPowers (LivingEntity entity){
        return new PowersPurpleHaze(entity);
    }

    @Override
    public int getMaxGuardPoints () {
        return 15;
    }

    @Override
    public boolean tryPower ( int move, boolean forced){
        StandUser SU = (StandUser) this.getSelf();
        switch (move) {
            case PowerIndex.SNEAK_MOVEMENT -> {
                this.setAttackTimeDuring(0);
                this.setActivePower(PowerIndex.SNEAK_MOVEMENT);
                this.setCooldown(PowerIndex.GLOBAL_DASH, 60);

                if (this.getSelf() instanceof Player P) {
                    P.getAbilities().flying = false;
                }

                Vec3 look = getSelf().getLookAngle().multiply(1, 0, 1).normalize();
                SU.roundabout$setLeapTicks(((StandUser) this.getSelf()).roundabout$getMaxLeapTicks());
                SU.roundabout$setLeapIntentionally(true);

                if (isPacketPlayer()) {
                    float strength = 1F;
                    if (this.getSelf().onGround()) {
                        MainUtil.takeUnresistableKnockbackWithY(this.getSelf(), strength, look.x, 1, look.z);
                    } else {
                        if (Math.abs(look.x) + Math.abs(look.z) == 0) {
                            strength *= 0.6F;
                        }
                    }
                    MainUtil.takeUnresistableKnockbackWithY(this.getSelf(), strength, look.x * 1, -1 * (this.getSelf().onGround() ? 1 : 0.8), look.z * 1);
                }
            }
        }
        return super.tryPower(move, forced);
    }


    @Override
    public StandEntity getNewStandEntity () {
        return ModEntities.PURPLE_HAZE.create(this.getSelf().level());
    }

    public void tryToDashClient(){
        if (vaultOrFallBraceFails()){
            dash();
        }
    }


    public void tryToStandLeapClient() {
        if (!onCooldown(PowerIndex.GLOBAL_DASH)) {
            tryPower(PowerIndex.SNEAK_MOVEMENT,true);
            tryPowerPacket(PowerIndex.SNEAK_MOVEMENT);
        }
    }

    @Override
    public void powerActivate (PowerContext context){
        switch (context) {
            case SKILL_3_NORMAL ->
                    tryToDashClient();
            case SKILL_3_CROUCH ->
                    tryToStandLeapClient();
        }
    }


    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {

        setSkillIcon(context, x, y, 1, StandIcons.LOCKED, PowerIndex.SKILL_1);

        setSkillIcon(context, x, y, 2, StandIcons.LOCKED, PowerIndex.SKILL_2);

        if (isHoldingSneak()){
            setSkillIcon(context, x, y, 3, StandIcons.LOCKED, PowerIndex.NONE);
        } else {
            setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
        }
        if (isHoldingSneak()){
            setSkillIcon(context, x, y, 3, StandIcons.STAND_LEAP_PURPLE_HAZE, PowerIndex.SKILL_3);
        } else {
            if (canVault() ) {
                setSkillIcon(context, x, y, 3, StandIcons.PURPLE_HAZE_LEDGE_GRAB, PowerIndex.GLOBAL_DASH);
            } else {
                setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
            }
        }

        renderPodStock(context, x, y, 4);
    }


    public void renderPodStock(GuiGraphics context, int x, int y, int slot){
        RenderSystem.enableBlend();
        context.setColor(1f, 1f, 1f, 1f);
        CooldownInstance cd = null;
        x += slot * 25;
        y-=1;
        RenderSystem.enableBlend();
        context.blit(StandIcons.PODS_STOCKS,x-3,y-3,0, 0, squareWidth, squareHeight, squareWidth, squareHeight);
    }



    public void bigLeap(LivingEntity entity,float range, float mult){
        Vec3 vec3d = entity.getEyePosition(1);
        Vec3 vec3d2 = entity.getViewVector(1);
        Vec3 vec3d3 = vec3d.add(vec3d2.x * range, vec3d2.y * range, vec3d2.z * range);
        BlockHitResult blockHit = entity.level().clip(new ClipContext(vec3d, vec3d3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity));

        double mag = this.getSelf().getPosition(1).distanceTo(
                new Vec3(blockHit.getLocation().x, blockHit.getLocation().y,blockHit.getLocation().z))*0.75+1;
        Vec3 vec3 = new Vec3(
                (blockHit.getLocation().x - this.getSelf().getX())/mag,
                (blockHit.getLocation().y - this.getSelf().getY())/mag,
                (blockHit.getLocation().z - this.getSelf().getZ())/mag
        );
        Direction gravD = ((IGravityEntity)this.self).roundabout$getGravityDirection();
        if (gravD != Direction.DOWN){
            vec3 = RotationUtil.vecWorldToPlayer(vec3,gravD);
        }
        vec3= new Vec3(
                vec3.x*mult,
                0.6+Math.max(vec3.y,0)*mult,
                vec3.z*mult
        );

        MainUtil.takeUnresistableKnockbackWithY2(this.getSelf(),
                vec3.x,
                vec3.y,
                vec3.z
        );

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
        return Component.literal(  "Feu_Ghost").withStyle(ChatFormatting.YELLOW);
    }


}
