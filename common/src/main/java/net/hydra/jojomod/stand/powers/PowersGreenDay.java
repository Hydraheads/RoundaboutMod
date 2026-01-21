package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPermaCasting;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;

import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.substand.SeperatedLegsEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.PermanentZoneCastInstance;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandPowers;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.mixin.StandUserEntity;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewPunchingStand;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;

import net.minecraft.core.UUIDUtil;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnderpearlItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.levelgen.WorldDimensions;
import net.minecraft.world.level.storage.WorldData;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PowersGreenDay extends NewPunchingStand {
    public PowersGreenDay(LivingEntity self) {super(self);}




    public boolean tryPower(int move, boolean forced) {
        return super.tryPower(move, forced);
    }

    @Override
    public int getMaxGuardPoints(){
        return 20;
    }

    @Override
    public StandEntity getNewStandEntity() {
        return ModEntities.GREEN_DAY.create(this.getSelf().level());
    }



    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersGreenDay(entity);
    }

    @Override
    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypas) {

        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        $$1.add(drawSingleGUIIcon(context,18,leftPos+20,topPos+80,2, "ability.roundabout.punch",
                "instruction.roundabout.press_attack", StandIcons.GREEN_DAY_PUNCH,1,level,bypas));
        // charge fire
        $$1.add(drawSingleGUIIcon(context,18,leftPos+20, topPos+118,0, "ability.roundabout.guard",
                "instruction.roundabout.hold_block", StandIcons.GREEN_DAY_GUARD,0,level,bypas));
        // burst fire
        $$1.add(drawSingleGUIIcon(context,18,leftPos+20,topPos+99,0, "ability.roundabout.barrage",
                "instruction.roundabout.barrage", StandIcons.GREEN_DAY_BARRAGE,2,level,bypas));
        // manual scope
        $$1.add(drawSingleGUIIcon(context,18,leftPos+39,topPos+80,0, "ability.roundabout.gd_punch_left",
                "instruction.roundabout.press_skill", StandIcons.GREEN_DAY_MOLD_PUNCH_LEFT,1,level,bypas));
        // charge fire
        $$1.add(drawSingleGUIIcon(context,18,leftPos+39, topPos+118,0, "ability.roundabout.gd_return_left",
                "instruction.roundabout.press_skill_crouch", StandIcons.GREEN_DAY_ARM_RETURN_LEFT,0,level,bypas));
        // burst fire
        $$1.add(drawSingleGUIIcon(context,18,leftPos+39,topPos+99,0, "ability.roundabout.gd_spin_left",
                "instruction.roundabout.press_skill_block", StandIcons.GREEN_DAY_MOLD_SPIN_LEFT,2,level,bypas));
        // place ratt
        $$1.add(drawSingleGUIIcon(context,18,leftPos+58,topPos+80,0, "ability.roundabout.gd_punch_right",
                "instruction.roundabout.press_skill", StandIcons.GREEN_DAY_MOLD_PUNCH_RIGHT,2,level,bypas));
        // place burst
        $$1.add(drawSingleGUIIcon(context,18,leftPos+58,topPos+118,0, "ability.roundabout.gd_spin_right",
                "instruction.roundabout.press_skill_crouch", StandIcons.GREEN_DAY_ARM_RETURN_RIGHT,1,level,bypas));
        // place auto
        $$1.add(drawSingleGUIIcon(context,18,leftPos+58,topPos+99,0, "ability.roundabout.gd_return_right",
                "instruction.roundabout.press_skill_block", StandIcons.GREEN_DAY_MOLD_SPIN_RIGHT,1,level,bypas));
        // dodge
        $$1.add(drawSingleGUIIcon(context,18,leftPos+77,topPos+80,0, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE,3,level,bypas));
        // passive
        $$1.add(drawSingleGUIIcon(context,18,leftPos+77,topPos+99,0, "ability.roundabout.gd_mold_leap",
                "instruction.roundabout.press_skill_crouch", StandIcons.GREEN_DAY_MOLD_LEAP,3,level,bypas));
        // bucket passive
        $$1.add(drawSingleGUIIcon(context,18,leftPos+77,topPos+118,0, "ability.roundabout.gd_mold_field",
                "instruction.roundabout.press_skill", StandIcons.GREEN_DAY_MOLD_FIELD,3,level,bypas));
        // ratt leap
        $$1.add(drawSingleGUIIcon(context,18,leftPos+96,topPos+80,0, "ability.roundabout.gd_stitch",
                "instruction.roundabout.press_skill_crouch", StandIcons.GREEN_DAY_STITCH,4,level,bypas));

        $$1.add(drawSingleGUIIcon(context,18,leftPos+96,topPos+99,0, "ability.roundabout.gd_pardon",
                "instruction.roundabout.press_skill_block", StandIcons.GREEN_DAY_PARDON,4,level,bypas));

        return $$1;
    }

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        ClientUtil.fx.roundabout$onGUI(context);

        if (isHoldingSneak())
            setSkillIcon(context, x, y, 4, StandIcons.GREEN_DAY_STITCH, PowerIndex.SKILL_4_SNEAK);
        else if (isGuarding())
            setSkillIcon(context, x, y, 4, StandIcons.GREEN_DAY_PARDON, PowerIndex.SKILL_4_GUARD);
        else
            if(isMoldFieldOn())
                setSkillIcon(context, x, y, 4, StandIcons.GREEN_DAY_MOLD_FIELD_OFF, PowerIndex.SKILL_4);
            else
                setSkillIcon(context, x, y, 4, StandIcons.GREEN_DAY_MOLD_FIELD, PowerIndex.SKILL_4);

        if (isHoldingSneak())
            setSkillIcon(context, x, y, 2, StandIcons.GREEN_DAY_ARM_RETURN_RIGHT, PowerIndex.SKILL_2_SNEAK);
        else
        if(isGuarding())
            setSkillIcon(context, x, y, 2, StandIcons.GREEN_DAY_MOLD_SPIN_RIGHT, PowerIndex.SKILL_2_GUARD);
        else
            setSkillIcon(context, x, y, 2, StandIcons.GREEN_DAY_MOLD_PUNCH_RIGHT, PowerIndex.SKILL_2);

        if (isHoldingSneak())
            setSkillIcon(context, x, y, 3, StandIcons.GREEN_DAY_MOLD_LEAP, PowerIndex.SNEAK_MOVEMENT);
        else
        if(isGuarding())
            setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
        else
            setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);

        if (isHoldingSneak())
            setSkillIcon(context, x, y, 1, StandIcons.GREEN_DAY_ARM_RETURN_LEFT, PowerIndex.SKILL_1);
        else
        if(isGuarding())
            setSkillIcon(context, x, y, 1, StandIcons.GREEN_DAY_MOLD_SPIN_LEFT, PowerIndex.SKILL_1);
        else
            setSkillIcon(context, x, y, 1, StandIcons.GREEN_DAY_MOLD_PUNCH_LEFT, PowerIndex.SKILL_1);



        super.renderIcons(context, x, y);
    }

    @Override
    public void powerActivate(PowerContext context) {
        /**Making dash usable on both key presses*/
        switch (context)
        {
            case SKILL_3_NORMAL -> {
                Roundabout.LOGGER.info("dash");
                dash();
            }
            case SKILL_3_CROUCH -> {
                Roundabout.LOGGER.info("Separation_Leap");
                tryToStandLeapClient();
                setcrawlserver(this.self);


            }
            case SKILL_4_CROUCH, SKILL_4_CROUCH_GUARD -> {
                Stitch();
            }
            case SKILL_4_NORMAL -> {
                toggleMold();
            }
            case SKILL_4_GUARD -> {
                selectAllyClient();
            }
            case SKILL_1_NORMAL -> {
              setcrawlserver(this.self);
            }
        }
    }
    public boolean IsMoldFieldActive = false;

    @Override
    public boolean setPowerOther(int move, int lastMove) {
        switch (move)
        {
            case PowerIndex.POWER_4_SNEAK -> {
                return StitchHeal(1.0f, this.self);
                //return this.setcrawlserver(this.self);
                
            }
            case PowerIndex.POWER_4 -> {
                return toggleMoldField();
            }
            case PowerIndex.POWER_4_BLOCK -> {
                return selectAllyServer();
            }
            case PowerIndex.POWER_3_EXTRA -> {
                return moldLeapServer();
            }
        }
        return super.setPowerOther(move,lastMove);
    }

    public boolean hasCheckedAllies = false;


    @Override
    public void tickPower() {
        moldShenanigans();
        if(legGoneTicks>0) {
            if (!this.self.level().isClientSide()) {
                for (int i = 0; i < 2; i = i + 1) {
                    double randX = Roundabout.RANDOM.nextDouble(-0.4, 0.4);
                    double randY = Roundabout.RANDOM.nextDouble(-0.4, 0.4);
                    double randZ = Roundabout.RANDOM.nextDouble(-0.4, 0.4);
                    ((ServerLevel) this.getSelf().level()).sendParticles(ModParticles.MOLD,
                            this.getSelf().getX() + randX,
                            this.getSelf().getY()+0.2 + randY,
                            this.getSelf().getZ() + randZ,
                            0, 0, 0, 0, 0);
                }

            }
            legGoneTicks = legGoneTicks - 1;
        }
        if(!(currentlegs == null)) {
            if(!(legGoneTicks>0)) {
                if (!this.self.level().isClientSide()) {
                    currentlegs.discard();
                }
            }else{
                if (!this.self.level().isClientSide()) {
                    if(MainUtil.cheapDistanceTo(this.self.getX(),this.self.getY(),this.self.getZ(),currentlegs.getX(),currentlegs.getY(),currentlegs.getZ())<1 && currentlegs.StartupTicks == 0) {
                        legGoneTicks = 0;
                        ((StandUser) this.self).rdbt$SetCrawlTicks(0);
                        setActivePower(PowerIndex.POWER_3_BONUS);
                        this.updatePowerInt(PowerIndex.POWER_3_BONUS,0);
                        S2CPacketUtil.sendIntPowerDataPacket((Player)this.getSelf(),PowerIndex.POWER_3_BONUS,0);

                        double Xangle = Math.toRadians(this.self.getLookAngle().x);
                        double Pitch = Math.toRadians(this.self.getLookAngle().y);
                        double Zangle = Math.toRadians(this.self.getLookAngle().z);
                        double diameter = 0.4d;
                        for (int i = 0; i < 11; i = i + 1) {
                            ((ServerLevel) this.getSelf().level()).sendParticles(ModParticles.STITCH,
                                    this.getSelf().getX() + (diameter * Math.sin(i * 4)) * Math.cos(Xangle),
                                    this.getSelf().getY() + 0.5,
                                    this.getSelf().getZ() + (diameter * Math.cos(i * 4)) * Math.cos(Zangle),
                                    0, 0, 0, 0, 0);
                        }
                    }
                }else{
                    if(MainUtil.cheapDistanceTo(this.self.getX(),this.self.getY(),this.self.getZ(),currentlegs.getX(),currentlegs.getY(),currentlegs.getZ())<1.5 && currentlegs.StartupTicks == 0 ) {
                        legGoneTicks = 0;
                        ((StandUser) this.self).rdbt$SetCrawlTicks(0);
                    }
                }

            }
        }
        if(!hasCheckedAllies) {
            generateAllyList();
            hasCheckedAllies=true;
        }
        super.tickPower();


    }

    @Override
    public void updatePowerInt(byte activePower, int data) {
        switch (activePower) {
            case PowerIndex.POWER_3_BONUS -> {
                ((StandUser)this.self).rdbt$SetCrawlTicks(data);
                legGoneTicks = data;
            }

        }
        super.updatePowerInt(activePower,data);
    }


    public boolean selectAllyServer(){
        listInit();
        if(!(((StandUser) this.self).roundabout$getTargetEntity(this.self,16)==null)) {
            Entity targetEnt = ((StandUser) this.self).roundabout$getTargetEntity(this.self, 16);
            if(!allies.contains(targetEnt.getStringUUID())) {
                allies.add(targetEnt.getStringUUID());
            }
            else{
                allies.remove(targetEnt.getStringUUID());
            }


        }
        return true;

    }

    public void selectAllyClient(){
        listInit();
        if(!(((StandUser) this.self).roundabout$getTargetEntity(this.self,16)==null)) {
            Entity targetEnt = ((StandUser) this.self).roundabout$getTargetEntity(this.self, 16);
            if(!allies.contains(targetEnt.getStringUUID())) {
                allies.add(targetEnt.getStringUUID());
            }
            else{
                allies.remove(targetEnt.getStringUUID());
            }

            tryPowerPacket(PowerIndex.POWER_4_BLOCK);
            Roundabout.LOGGER.info(listToString(allies));
        }
        saveAllies();

    }

    public List<String> allies = new ArrayList<>();

    public static List<String> ConvertToString(){
        String cf = ConfigManager.getClientConfig().greenDayAllyList.getFromMemory();
        return allyListParser(cf);
    }

    public void generateAllyList(){
        listInit();
        allies = ConvertToString();
    }

    public void saveAllies(){
        String cf = listToString(allies);
        ConfigManager.getClientConfig().greenDayAllyList.saveToMemory(cf);
        ConfigManager.saveClientConfig();
    }

    public static List<String> allyListParser(String string){
        List<String> allies = new ArrayList<>();
        String temporary = "";

        for (int i = 0; i < string.length(); i++) {
            char current = string.charAt(i);
            if (!(current == ',')){
                temporary += current;
            }else{
                allies.add(temporary);
                temporary = "";
            }
        }

        return allies;
    }

    public String listToString(List<String> allies){
        String allyString = "";

        for (int i = 0; i < allies.size(); i++) {
            String current = allies.get(i);
            allyString += current;
            allyString += ",";
        }

        return allyString;
    }


    public void listInit(){
        if (allies == null) {
            allies = new ArrayList<>();
        }
    }

    @Override
    public boolean highlightsEntity(Entity ent, Player player) {
        if(((StandUser)player).roundabout$isGuarding()) {
            if (allies.contains(ent.getStringUUID()) && player.hasLineOfSight(ent)){
                return true;
            } else if (!(((StandUser) player).roundabout$getTargetEntity(player, 16) == null)) {
                if (((StandUser) player).roundabout$getTargetEntity(player, 16).equals(ent)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int highlightsEntityColor(Entity ent, Player player) {
        listInit();
        if(allies.contains(ent.getStringUUID())){
            if(!(((StandUser) player).roundabout$getTargetEntity(player,16)== null)) {
                if (((StandUser) player).roundabout$getTargetEntity(player, 16).equals(ent)) {
                    return 12379456;
                }
            }
            return 10349135;
        }
        return 15526430;
    }

    public void Stitch(){
        if (!this.onCooldown(PowerIndex.SKILL_4_SNEAK)) {
            this.setCooldown(PowerIndex.SKILL_4_SNEAK, 400);
            this.setCooldown(PowerIndex.SKILL_4_CROUCH_GUARD, 400);
            this.tryPower(PowerIndex.POWER_4_SNEAK, true);
            tryPowerPacket(PowerIndex.POWER_4_SNEAK);
        }
    }

    public void toggleMold(){
        if (!this.onCooldown(PowerIndex.SKILL_4)) {
            this.setCooldown(PowerIndex.SKILL_4, 0);
            this.setCooldown(PowerIndex.SKILL_4_GUARD, 0);
            this.tryPower(PowerIndex.POWER_4, true);
            tryPowerPacket(PowerIndex.POWER_4);
        }

    }
    public int getLeapLevel(){
        return 3;
    }
    public int bonusLeapCount = -1;
    public void bigLeap(LivingEntity entity,float range, float mult){

        Vec3 vec3d = entity.getEyePosition(0);
        Vec3 vec3d2 = entity.getViewVector(0);
        Vec3 vec3d3 = vec3d.add(vec3d2.x * range, vec3d2.y * range, vec3d2.z * range);
        BlockHitResult blockHit = entity.level().clip(new ClipContext(vec3d, vec3d3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity));

        double mag = this.getSelf().getPosition(1).distanceTo(
                new Vec3(blockHit.getLocation().x, blockHit.getLocation().y,blockHit.getLocation().z))*0.75+1;

        MainUtil.takeUnresistableKnockbackWithY2(this.getSelf(),
                ((blockHit.getLocation().x - this.getSelf().getX())/mag)*mult*1.7,
                (0.3+ Math.max((blockHit.getLocation().y - this.getSelf().getY())/mag,0))*mult*1.7,
                ((blockHit.getLocation().z - this.getSelf().getZ())/mag)*mult*1.7
        );

    }

    public void tryToStandLeapClient() {

        if (vaultOrFallBraceFails()) {
            if (this.getSelf().onGround()) {
                boolean jojoveinLikeKeys = !ClientNetworking.getAppropriateConfig().generalStandSettings.standJumpAndDashShareCooldown;
                if ((jojoveinLikeKeys && !this.onCooldown(PowerIndex.SKILL_3)) ||
                        (!jojoveinLikeKeys && !this.onCooldown(PowerIndex.GLOBAL_DASH))) {
                    if (canExecuteMoveWithLevel(getLeapLevel())) {
                        if (jojoveinLikeKeys) {
                            this.setCooldown(PowerIndex.SKILL_3, ClientNetworking.getAppropriateConfig().generalStandSettings.standJumpCooldown);
                        } else {
                            this.setCooldown(PowerIndex.GLOBAL_DASH, ClientNetworking.getAppropriateConfig().generalStandSettings.standJumpCooldown);
                            this.setCooldown(PowerIndex.SNEAK_MOVEMENT, ClientNetworking.getAppropriateConfig().generalStandSettings.standJumpCooldown);
                        }

                        legGoneTicks = 240;
                        ((StandUser)this.self).rdbt$SetCrawlTicks(240);
                        tryPowerPacket(PowerIndex.POWER_3_EXTRA);

                        bonusLeapCount = 3;
                        bigLeap(this.getSelf(), 20, 1);
                        ((StandUser) this.getSelf()).roundabout$setLeapTicks(((StandUser) this.getSelf()).roundabout$getMaxLeapTicks());
                        ((StandUser) this.getSelf()).roundabout$setLeapIntentionally(true);
                        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.SNEAK_MOVEMENT, true);
                        tryPowerPacket(PowerIndex.SNEAK_MOVEMENT);
                    }
                }
            }
        }
    }
    public int legGoneTicks = 0;
    public SeperatedLegsEntity currentlegs;
    public boolean moldLeapServer() {
        legGoneTicks = 240;
        for(int i = 0; i < 11; i = i + 1) {
            double randX = Roundabout.RANDOM.nextDouble(-0.5, 0.5);
            double randY = Roundabout.RANDOM.nextDouble(-0.2, 0.2);
            double randZ = Roundabout.RANDOM.nextDouble(-0.5, 0.5);
            ((ServerLevel) this.getSelf().level()).sendParticles(ModParticles.MOLD,
                    this.getSelf().getX(),
                    this.getSelf().getY() + 1 ,
                    this.getSelf().getZ(),
                    1,randX,randY,randZ,0.12);
        }
        if(!(currentlegs == null)){
            currentlegs.discard();
        }

        ((StandUser) this.getSelf()).roundabout$setLeapTicks(((StandUser) this.getSelf()).roundabout$getMaxLeapTicks());
        ((StandUser) this.getSelf()).roundabout$setLeapIntentionally(false);
        setcrawlserver(this.self);
        SpawnLegs();
        return true;
    }

    public boolean setcrawlserver(LivingEntity entity) {
        if(!this.self.level().isClientSide()) {
            ((StandUser) entity).rdbt$SetCrawlTicks(240);
        }
        return true;
    }

    public void SpawnLegs(){
        SeperatedLegsEntity SLE = ModEntities.SEPERATED_LEGS.create(this.self.level());
        if(SLE != null) {
            SLE.setUser(this.self);
            SLE.setXRot(this.self.getXRot());
            SLE.setYRot(this.self.getYRot());
            SLE.setPos(this.self.getPosition(1).add(0,0.2,0));
            this.self.level().addFreshEntity(SLE);
            currentlegs = SLE;
        }
    }
    public boolean StitchHeal(float hp, LivingEntity entity) {
        if(!isClient()) {
            float maxhp = entity.getMaxHealth();
            float currenthp = entity.getHealth();

            if (currenthp < maxhp) {
                entity.setHealth(currenthp + 1.0f);
            }
            if (entity.hasEffect(ModEffects.BLEED)) {
                int level = entity.getEffect(ModEffects.BLEED).getAmplifier();
                int duration = entity.getEffect(ModEffects.BLEED).getDuration();
                entity.removeEffect(entity.getEffect(ModEffects.BLEED).getEffect());
                if (level > 0) {
                    entity.addEffect(new MobEffectInstance(ModEffects.BLEED, duration, level - 1));
                }
            }

            double Xangle = Math.toRadians(this.self.getLookAngle().x);
            double Pitch = Math.toRadians(this.self.getLookAngle().y);
            double Zangle = Math.toRadians(this.self.getLookAngle().z);
            double diameter = 0.6d;
            for(int i = 0; i < 11; i = i + 1) {
                ((ServerLevel) this.getSelf().level()).sendParticles(ModParticles.STITCH,
                        this.getSelf().getX() + (diameter * Math.sin(i*4)) * Math.cos(Xangle),
                        this.getSelf().getY() + (this.getSelf().getEyeHeight()*0.7),
                        this.getSelf().getZ() + (diameter * Math.cos(i*4)) * Math.cos(Zangle),
                        0,0,0,0,0);
            }


        }
        return true;
    }

    @Override
    public byte getPermaCastContext() {
        return PermanentZoneCastInstance.MOLD_FIELD;
    }

        public boolean toggleMoldField() {
        if (!this.getSelf().level().isClientSide()) {
            IPermaCasting icast = ((IPermaCasting) this.getSelf().level());
            if (!icast.roundabout$isPermaCastingEntity(this.getSelf())) {
                icast.roundabout$addPermaCaster(this.getSelf());

            } else {
                removeMold();
            }
        }
        return true;
    }

    public void removeMold() {
        IPermaCasting icast = ((IPermaCasting) this.getSelf().level());
        icast.roundabout$removePermaCastingEntity(this.getSelf());
    }

    public void moldShenanigans() {
        if (!this.getSelf().level().isClientSide()) {
            if(isMoldFieldOn()) {
                for(int i = 0; i < 84; i = i + 1) {
                    double randX = Roundabout.RANDOM.nextDouble(-50, 50);
                    double randY = Roundabout.RANDOM.nextDouble(-50, 50);
                    double randZ = Roundabout.RANDOM.nextDouble(-50, 50);
                    ((ServerLevel) this.getSelf().level()).sendParticles(ModParticles.MOLD_DUST,
                            this.getSelf().getX() + randX,
                            this.getSelf().getY() + randY,
                            this.getSelf().getZ() + randZ,
                            0,1,-1,1,0.12);

                }
            }
        }
    }

    public boolean isMoldFieldOn() {
        return((IPermaCasting) this.getSelf().level()).roundabout$isPermaCastingEntity(this.self);
    };

    @Override
    public void playSummonEffects(boolean forced) {
        if (!this.getSelf().level().isClientSide()) {
            for(int i = 0; i < 23; i = i + 1) {
                double randX = Roundabout.RANDOM.nextDouble(-1, 1);
                double randY = Roundabout.RANDOM.nextDouble(-1, 2);
                double randZ = Roundabout.RANDOM.nextDouble(-1, 1);
                ((ServerLevel) this.getSelf().level()).sendParticles(new DustParticleOptions(new Vector3f(0.76F, 1.0F, 0.9F
                ), 2f),
                        this.getSelf().getX() + randX,
                        this.getSelf().getY() + randY,
                        this.getSelf().getZ() + randZ,
                        0,0,0.2,0,0);

            }
        }
    }

    @Override
    public float getPunchStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod(multiplyPowerByStandConfigPlayers(1.5F));
        } else {
            return levelupDamageMod(multiplyPowerByStandConfigMobs(4.2F));
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
        return Component.literal(  "Fish").withStyle(ChatFormatting.YELLOW);
    }

    public static final byte
            PART_FOUR = 1;


    @Override
    public List<Byte> getSkinList() {
        return Arrays.asList(
                PART_FOUR

        );
    }



}
