package net.hydra.jojomod.event.powers.stand.presets;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.block.StandFireBlock;
import net.hydra.jojomod.block.StandFireBlockEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.JusticeEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.stand.StarPlatinumEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.PowersStarPlatinum;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static net.hydra.jojomod.event.index.PacketDataIndex.FLOAT_STAR_FINGER_SIZE;

public class PowersMagiciansRed extends PunchingStand {

    public int snapNumber;
    public int fireIDNumber;
    public PowersMagiciansRed(LivingEntity self) {
        super(self);
    }
    @Override
    public StandPowers generateStandPowers(LivingEntity entity){
        return new PowersMagiciansRed(entity);
    }
    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        if (this.isGuarding()){
            if (isHoldingSneak()) {
                setSkillIcon(context, x, y, 1, StandIcons.LIGHT_FIRE, PowerIndex.SKILL_1_SNEAK);
            } else {
                setSkillIcon(context, x, y, 1, StandIcons.RED_BIND, PowerIndex.NO_CD);
            }
            setSkillIcon(context, x, y, 3, StandIcons.PROJECTILE_BURN, PowerIndex.SKILL_EXTRA);
        } else {
            if (isHoldingSneak()) {
                setSkillIcon(context, x, y, 1, StandIcons.LIGHT_FIRE, PowerIndex.SKILL_1_SNEAK);
                setSkillIcon(context, x, y, 3, StandIcons.SNAP_ICON, PowerIndex.SKILL_3);
            } else {
                setSkillIcon(context, x, y, 1, StandIcons.RED_BIND, PowerIndex.NO_CD);
                setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.SKILL_3_SNEAK);
            }
        }
        setSkillIcon(context, x, y, 2, StandIcons.NONE, PowerIndex.NO_CD);
        setSkillIcon(context, x, y, 4, StandIcons.NONE, PowerIndex.NO_CD);
    }
    @Override
    public StandEntity getNewStandEntity(){
        return ModEntities.MAGICIANS_RED.create(this.getSelf().level());
    }
    @Override
    public Component getSkinName(byte skinId){
        return JusticeEntity.getSkinNameT(skinId);
    }

    @Override
    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }
    @Override
    public SoundEvent getSoundFromByte(byte soundChoice){
        if (soundChoice == SoundIndex.SUMMON_SOUND) {
            return ModSounds.SUMMON_MAGICIAN_EVENT;
        } else if (soundChoice == LAST_HIT_1_NOISE) {
            return ModSounds.FIRE_STRIKE_LAST_EVENT;
        }
        return super.getSoundFromByte(soundChoice);
    }

    public boolean hold3 = false;
    public boolean hold1 = false;

    public BlockPos getGrabPos(float range) {
        Vec3 vec3d = this.getSelf().getEyePosition(0);
        Vec3 vec3d2 = this.getSelf().getViewVector(0);
        Vec3 vec3d3 = vec3d.add(vec3d2.x * range, vec3d2.y * range, vec3d2.z * range);
        return new BlockPos((int) vec3d3.x, (int) vec3d3.y, (int) vec3d3.z);
    }
    public BlockPos getGrabBlock(){
        return getGrabBlock(5);
    }
    public BlockPos getGrabBlock(float range){

        Vec3 vec3d = this.getSelf().getEyePosition(0);
        Vec3 vec3d2 = this.getSelf().getViewVector(0);
        Vec3 vec3d3 = vec3d.add(vec3d2.x * range, vec3d2.y * range, vec3d2.z * range);
        BlockHitResult blockHit = this.getSelf().level().clip(new ClipContext(vec3d, vec3d3,
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.getSelf()));
        if (blockHit.getType() == HitResult.Type.BLOCK){
            grabBlock2 = blockHit.getBlockPos();
            return blockHit.getBlockPos().relative(blockHit.getDirection());
        }
        return null;
    }
    @Override
    public void buttonInput1(boolean keyIsDown, Options options) {
        if (keyIsDown) {
            if (!hold1) {
                hold1 = true;
                if (!isGuarding()) {
                    if (isHoldingSneak()) {
                        if (!this.onCooldown(PowerIndex.SKILL_1_SNEAK)) {
                            BlockPos HR = getGrabBlock();
                            if (HR != null) {
                                this.setCooldown(PowerIndex.SKILL_1_SNEAK, ClientNetworking.getAppropriateConfig().cooldownsInTicks.magicianIgniteFire);
                                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_1_SNEAK, true);
                                ModPacketHandler.PACKET_ACCESS.StandPosPowerPacket(PowerIndex.EXTRA, grabBlock2);
                                ModPacketHandler.PACKET_ACCESS.StandPosPowerPacket(PowerIndex.POWER_1_SNEAK, HR);
                            }
                        }
                    }
                }
            }
        } else {
            hold1 = false;
        }
    }
    public BlockPos grabBlock = null;
    public BlockPos grabBlock2 = null;
    public boolean tryPosPower(int move, boolean forced, BlockPos blockPos){
        if (move == PowerIndex.POWER_1_SNEAK) {
            this.grabBlock = blockPos;
            return tryPower(move, forced);
        } else if (move == PowerIndex.SPECIAL) {
            this.grabBlock2 = blockPos;
        } else if (move == PowerIndex.POWER_3_BLOCK) {
            this.grabBlock = blockPos;
            return tryPower(move, forced);
        }
        return false;
        /*Return false in an override if you don't want to sync cooldowns, if for example you want a simple data update*/
    }
    @Override
    public void buttonInput3(boolean keyIsDown, Options options) {
        if (!isPiloting()) {
            if (keyIsDown) {
                if (!inputDash) {
                    if (this.isGuarding()) {
                        if (!this.onCooldown(PowerIndex.SKILL_EXTRA)) {
                            this.setCooldown(PowerIndex.SKILL_EXTRA, 100);

                            BlockPos HR = getGrabPos(10);
                            if (HR != null) {
                                ModPacketHandler.PACKET_ACCESS.StandPosPowerPacket(PowerIndex.POWER_3_BLOCK, HR);
                            }
                        }
                        inputDash = true;
                    } else if (isHoldingSneak()) {
                        if (!this.onCooldown(PowerIndex.SKILL_3)) {
                            this.setCooldown(PowerIndex.SKILL_3, ClientNetworking.getAppropriateConfig().cooldownsInTicks.magicianSnapFireAway);
                            ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.POWER_3);
                        }
                        inputDash = true;
                    } else {
                        super.buttonInput3(keyIsDown, options);
                    }
                }
            } else {
                inputDash = false;
            }
        } else {
            if (keyIsDown) {
                if (!hold3) {
                    hold3 = true;
                }
            } else {
                if (hold3) {
                    hold3 = false;
                }
            }
        }
    }

    public static final byte LAST_HIT_1_NOISE = 120;

    @Override
    public boolean canLightFurnace(){
        return true;
    }
    @Override
    public boolean setPowerOther(int move, int lastMove) {
        if (move == PowerIndex.POWER_3) {
            return this.snap();
        } else if (move == PowerIndex.POWER_1_SNEAK) {
            return this.setFire();
        } else if (move == PowerIndex.POWER_3_BLOCK) {
            return this.fireBlast();
        }
        return super.setPowerOther(move,lastMove);
    }

    public boolean fireBlast(){
        if (!this.self.level().isClientSide()) {
            this.self.level().playSound(null, this.self.getX(), this.self.getY(),
                    this.self.getZ(), ModSounds.FIRE_BLAST_EVENT, this.self.getSoundSource(), 2.0F, 1F);
            StandEntity stand = this.getStandEntity(this.self);
            if (stand != null && grabBlock != null) {
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 90; j++) {
                        double spd = (1 - ((double) i / 6))*0.4;
                        double random = (Math.random() * 14) - 7;
                        double random2 = (Math.random() * 14) - 7;
                        double random3 = (Math.random() * 14) - 7;
                        ((ServerLevel) stand.level()).sendParticles(ModParticles.ORANGE_FLAME, stand.getX(),
                                stand.getY() + stand.getEyeHeight()*0.8, stand.getZ(),
                                0,
                                (-3 * (stand.getX() - grabBlock.getX()) + 0.5 + random) * spd,
                                (-3 * (stand.getY() - grabBlock.getY()) - 1 + random2) * spd,
                                (-3 * (stand.getZ() - grabBlock.getZ()) + 0.5 + random3) * spd,
                                0.15);
                    }
                }
                burnProjectiles(this.self,DamageHandler.genHitbox(this.self, grabBlock.getX(), grabBlock.getY(),
                        grabBlock.getZ(), 10, 10, 10), 20, 25);
            }
        }
        return true;
    }

    public List<Entity> burnProjectiles(LivingEntity User, List<Entity> entities, float maxDistance, float angle){
        List<Entity> hitEntities = new ArrayList<>(entities) {
        };
        for (Entity value : entities) {
            if (!value.isRemoved() && value instanceof Projectile && !(value instanceof Fireball)){
                if (angleDistance(getLookAtEntityYaw(User, value), (User.getYHeadRot()%360f)) <= angle && angleDistance(getLookAtEntityPitch(User, value), User.getXRot()) <= angle){
                    hitEntities.remove(value);
                    ((ServerLevel) this.self.level()).sendParticles(ParticleTypes.SMOKE, value.getX(),
                            value.getY(), value.getZ(),
                            20,
                            0.1,
                            0.1,
                            0.1,
                            0.03);
                    value.discard();
                }
            }
        }
        return hitEntities;
    }

    public boolean snap(){
        this.self.level().playSound(null, this.self.getX(), this.self.getY(),
                this.self.getZ(), ModSounds.SNAP_EVENT, this.self.getSoundSource(), 2.0F, 1F);
        this.setCooldown(PowerIndex.SKILL_3, ClientNetworking.getAppropriateConfig().cooldownsInTicks.magicianSnapFireAway);
        this.snapNumber++;
        return true;
    }

    public boolean setFire(){
        if (grabBlock != null && tryPlaceBlock(grabBlock)){
            this.self.level().playSound(null, this.self.getX(), this.self.getY(),
                    this.self.getZ(), ModSounds.FIRE_WHOOSH_EVENT, this.self.getSoundSource(), 2.0F, 2F);
            for (int j = 0; j < 10; j++) {
                double random = (Math.random() * 0.8) - 0.4;
                double random2 = (Math.random() * 0.8) - 0.4;
                double random3 = (Math.random() * 0.8) - 0.4;
                ((ServerLevel) this.self.level()).sendParticles(ModParticles.ORANGE_FLAME, this.self.getX(),
                        this.self.getY() + this.self.getEyeHeight()*0.7, this.self.getZ(),
                        0,
                        -1*(this.self.getX() - grabBlock.getX())+0.5 + random,
                        -1*(this.self.getY() - grabBlock.getY())-0.5 + random2,
                        -1*(this.self.getZ() - grabBlock.getZ())+0.5 + random3,
                        0.15);
            }
            createStandFire(grabBlock);
        }
        return true;
    }

    public int getNewFireId(){
        this.fireIDNumber++;
        return this.fireIDNumber;
    }

    public void createStandFire(BlockPos pos){
        this.fireIDNumber++;
        this.getSelf().level().setBlockAndUpdate(pos, ((StandFireBlock)ModBlocks.STAND_FIRE).getStateForPlacement(this.self.level(),pos));
        BlockEntity be = this.self.level().getBlockEntity(pos);
        if (be instanceof StandFireBlockEntity sfbe){
            sfbe.standUser = this.self;
            sfbe.snapNumber = this.snapNumber;
            sfbe.fireIDNumber = this.fireIDNumber;
            sfbe.fireColorType = getFireColor();
        }
    }

    public byte getFireColor(){
        return StandFireType.ORANGE.id;
    }

    @Override
    public float getReach(){
        return 7;
    }

    @Override
    public float getPunchStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod((float) ((float) 1.25* (ClientNetworking.getAppropriateConfig().
                    damageMultipliers.magicianAttackOnPlayers*0.01)));
        } else {
            return levelupDamageMod((float) ((float) 3.5* (ClientNetworking.getAppropriateConfig().
                    damageMultipliers.magicianAttackOnMobs*0.01)));
        }
    }
    @Override
    public float getHeavyPunchStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod((float) ((float) 1.75* (ClientNetworking.getAppropriateConfig().
                    damageMultipliers.magicianAttackOnPlayers*0.01)));
        } else {
            return levelupDamageMod((float) ((float) 4.5* (ClientNetworking.getAppropriateConfig().
                    damageMultipliers.magicianAttackOnMobs*0.01)));
        }
    }
    @Override
    public void updateAttack(){
        if (this.attackTimeDuring > -1) {
            if (this.attackTimeDuring > this.attackTimeMax) {
                this.attackTime = -1;
                this.attackTimeMax = 0;
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE,true);
            } else {
                if ((this.attackTimeDuring == 7 && this.activePowerPhase == 1)
                        || this.attackTimeDuring == 8) {
                    this.standPunch();
                }
            }
        }
    }
    @Override
    public void standPunch(){

        if (this.self instanceof Player){
            if (isPacketPlayer()){
                this.attackTimeDuring = -10;

                Entity targetEntity = getTargetEntity(this.self,-1);

                List<Entity> listE = getTargetEntityList(this.self,-1);
                int id = -1;
                if (targetEntity != null){
                    id = targetEntity.getId();
                }
                ModPacketHandler.PACKET_ACCESS.StandPunchPacket(id, this.activePowerPhase);
                if (!listE.isEmpty() && ClientNetworking.getAppropriateConfig().barrageHasAreaOfEffect){
                    for (int i = 0; i< listE.size(); i++){
                        if (!(targetEntity != null && listE.get(i).is(targetEntity))) {
                            if (!(listE.get(i) instanceof StandEntity) && listE.get(i).distanceTo(this.self) < 3.5) {
                                ModPacketHandler.PACKET_ACCESS.StandPunchPacket(listE.get(i).getId(), (byte) (this.activePowerPhase + 50));
                            }
                        }
                    }
                }
            }
        } else {
            /*Caps how far out the punch goes*/

            Entity targetEntity = getTargetEntity(this.self,-1);

            List<Entity> listE = getTargetEntityList(this.self,-1);
            punchImpact(targetEntity);
            if (!listE.isEmpty()){
                for (int i = 0; i< listE.size(); i++){
                    if (!(storeEnt != null && listE.get(i).is(storeEnt))) {
                        if (!(listE.get(i) instanceof StandEntity) && listE.get(i).distanceTo(this.self) < 3.5) {
                            this.setActivePowerPhase((byte) (this.getActivePowerPhase()+50));
                            punchImpact(listE.get(i));
                        }
                    }
                }
            }
        }

    }
    @Override
    public boolean setPowerAttack(){
        if (this.activePowerPhase >= 3){
            this.activePowerPhase = 1;
        } else {
            this.activePowerPhase++;
            if (this.activePowerPhase == 3) {
                this.attackTimeMax= ClientNetworking.getAppropriateConfig().cooldownsInTicks.magicianLastLashInString;
            } else {
                this.attackTimeMax= ClientNetworking.getAppropriateConfig().cooldownsInTicks.magicianLash;
            }

        }

        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.ATTACK);
        this.setAttackTime(0);

        animateStand(this.activePowerPhase);
        poseStand(OffsetIndex.ATTACK);
        return true;
    }
    boolean splash = false;
    @Override
    public void punchImpact(Entity entity){
        if (this.getActivePowerPhase() >= 50){
            this.setActivePowerPhase((byte) (this.getActivePowerPhase()-50));
            splash = true;
        } else {
            splash = false;
        }
        this.setAttackTimeDuring(-10);
        if (entity != null) {
            float pow;
            float knockbackStrength;
            boolean lasthit = false;
            if (this.getActivePowerPhase() >= this.getActivePowerPhaseMax()) {
                /*The last hit in a string has more power and knockback if you commit to it*/
                pow = getHeavyPunchStrength(entity);
                knockbackStrength = 0.2F;
                lasthit = true;
            } else {
                pow = getPunchStrength(entity);
                knockbackStrength = 0.14F;
            }

            if (splash){
                pow/=4;
            }
            if (StandDamageEntityAttack(entity, pow, 0, this.self)) {
                if (entity instanceof LivingEntity LE){

                    if (lasthit){addEXP(2,LE);} else {addEXP(1,LE);}
                }

                this.takeDeterminedKnockback(this.self, entity, knockbackStrength);
            } else {
                if (this.activePowerPhase >= this.activePowerPhaseMax) {
                    knockShield2(entity, 40);
                }
            }
        } else {
            // This is less accurate raycasting as it is server sided but it is important for particle effects
            float distMax = this.getDistanceOut(this.self, this.getReach(), false);
            float halfReach = (float) (distMax * 0.5);
            Vec3 pointVec = DamageHandler.getRayPoint(self, halfReach);
            if (!this.self.level().isClientSide) {
                ((ServerLevel) this.self.level()).sendParticles(ParticleTypes.SMOKE, pointVec.x, pointVec.y, pointVec.z,
                        10, 0.2, 0.2, 0.2, 0.1);
            }
        }

        if (!splash) {
            SoundEvent SE;
            float pitch = 1F;
            if (this.activePowerPhase >= this.activePowerPhaseMax) {

                if (!this.self.level().isClientSide()) {
                    Byte LastHitSound = this.getLastHitSound();
                    this.playStandUserOnlySoundsIfNearby(LastHitSound, 15, false,
                            true);
                }

                if (entity != null) {
                    SE = ModSounds.FIRE_STRIKE_LAST_EVENT;
                } else {
                    SE = ModSounds.FIRE_WHOOSH_EVENT;
                }
            } else {
                if (entity != null) {
                    SE = ModSounds.FIRE_STRIKE_EVENT;
                    pitch = 0.99F + 0.02F * activePowerPhase;
                } else {
                    SE = ModSounds.FIRE_WHOOSH_EVENT;
                }
            }

            if (!this.self.level().isClientSide()) {
                this.self.level().playSound(null, this.self.blockPosition(), SE, SoundSource.PLAYERS, 0.95F, pitch);
            }
        }
    }

}
