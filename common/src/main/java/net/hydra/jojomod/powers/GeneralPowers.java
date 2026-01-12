package net.hydra.jojomod.powers;

import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.event.index.PlayerPosIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.fates.powers.AbilityScapeBasis;
import net.hydra.jojomod.powers.power_types.VampireGeneralPowers;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.client.Options;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2i;

public class GeneralPowers extends AbilityScapeBasis {
    public GeneralPowers(LivingEntity self) {
        super(self);
    }
    public GeneralPowers() {
        super(null);
    }
    public GeneralPowers generatePowers(LivingEntity entity){
        return new GeneralPowers(entity);
    }

    @Override
    public void syncActivePower(){
        if (!this.self.level().isClientSide && this.self instanceof ServerPlayer SP){
            S2CPacketUtil.sendActivePowerPowersPacket(SP,activePower);
        }
    }
    /**The text name of the power*/
    public Component getPowerName(){
        return Component.empty();
    }
    public Component getPowerTagName(){
        return Component.empty();
    }
    public boolean isBrawling(){
        return false;
    }

    /**Adjust this function to enable the below minin functions, and intercept your mining when not holding
     * a mining tool*/
    public boolean isMining() {
        return false;
    }
    /**How fast does the block mine blocks that require pickaxes?*/
    public float getPickMiningSpeed() {
        return 7F;
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
    //The Power inventory icon coords
    public Vector2i getCoords(){
        return new Vector2i(0,222);
    }
    public ResourceLocation getSource(){
        return StandIcons.JOJO_ICONS_2;
    }

    @Override
    public void tryPowerPacket(byte packet){
        if (this.self.level().isClientSide()) {
            C2SPacketUtil.tryPowerPacketP(packet);
        }
    }
    @Override
    public void tryIntPowerPacket(byte packet, int integer){
        if (this.self.level().isClientSide()) {
            C2SPacketUtil.tryIntPowerPacketP(packet,integer);
        }
    }
    @Override
    public void tryTripleIntPacket(byte packet, int in1, int in2, int in3){
        if (this.self.level().isClientSide()) {
            C2SPacketUtil.tryTripleIntPacketP(packet, in1, in2, in3);
        }
    }
    @Override
    public void tryBlockPosPowerPacket(byte packet, BlockPos pos){
        if (this.self.level().isClientSide()) {
            C2SPacketUtil.tryBlockPosPowerPPacket(packet, pos);
        }
    }
    @Override
    public void tryBlockPosPowerPacket(byte packet, BlockPos pos, HitResult hitResult){
        if (this.self.level().isClientSide()) {
            C2SPacketUtil.tryBlockPosPowerPPacket(packet, pos, hitResult);
        }
    }
    @Override
    public void tryPosPowerPacket(byte packet, Vec3 pos){
        if (this.self.level().isClientSide()) {
            C2SPacketUtil.tryPosPowerPPacket(packet, pos);
        }
    }

    @Override
    public void preButtonInput4(boolean keyIsDown, Options options){
        if (hasActive(this.getSelf())) {
            if (!((TimeStop)this.getSelf().level()).CanTimeStopEntity(this.getSelf()) && !this.getStandUserSelf().roundabout$isPossessed()  ) {
                ((StandUser) this.getSelf()).roundabout$setIdleTime(0);
                buttonInput4(keyIsDown, options);
            }
        }
    }
    @Override
    public void preButtonInput3(boolean keyIsDown, Options options){
        if (hasActive(this.getSelf())) {
            if (!((TimeStop)this.getSelf().level()).CanTimeStopEntity(this.getSelf()) && !this.getStandUserSelf().roundabout$isPossessed()  ) {
                ((StandUser) this.getSelf()).roundabout$setIdleTime(0);
                buttonInput3(keyIsDown, options);
            }
        }
    }

    @Override
    public void preButtonInput2(boolean keyIsDown, Options options){
        if (hasActive(this.getSelf())) {
            if (!((TimeStop)this.getSelf().level()).CanTimeStopEntity(this.getSelf()) && !this.getStandUserSelf().roundabout$isPossessed()   ) {
                ((StandUser) this.getSelf()).roundabout$setIdleTime(0);
                buttonInput2(keyIsDown, options);
            }
        }
    }

    @Override
    public void preButtonInput1(boolean keyIsDown, Options options){
        if (hasActive(this.getSelf())) {
            if (!((TimeStop)this.getSelf().level()).CanTimeStopEntity(this.getSelf()) && !this.getStandUserSelf().roundabout$isPossessed()   ) {
                ((StandUser) this.getSelf()).roundabout$setIdleTime(0);
                buttonInput1(keyIsDown, options);
            }
        }
    }

    @Override
    public boolean tryPower(int move, boolean forced){
        if (!this.self.level().isClientSide && (this.isBarraging()) && (move != PowerIndex.BARRAGE && move != PowerIndex.BARRAGE_CLASH
                && move != PowerIndex.BARRAGE_CHARGE && move != PowerIndex.GUARD) && this.attackTimeDuring  > -1){
            this.stopSoundsIfNearby(SoundIndex.BARRAGE_SOUND_GROUP, 100,false);
        }

        if (canChangePower(move, forced)) {
            if (move == PowerIndex.NONE || move == PowerIndex.CLASH_CANCEL) {
                this.setPowerNone();
            } else if (move == PowerIndex.GUARD) {
                this.setPowerGuard();
            } else if (move == PowerIndex.MOVEMENT) {
                this.setPowerMovement(move);
            } else {
                this.setPowerOther(move, this.getActivePower());
            }

        }
        return false;
    }

    @Override
    public void tickPower() {
        if (!self.level().isClientSide()) {
            if (getActivePower() != PowerIndex.GUARD && getPlayerPos2() == PlayerPosIndex.GUARD) {
                setPlayerPos2(PlayerPosIndex.NONE);
            }
            if (getActivePower() != PowerIndex.BARRAGE_CHARGE && getPlayerPos2() == PlayerPosIndex.BARRAGE_CHARGE) {
                setPlayerPos2(PlayerPosIndex.NONE);
            }
            if (getActivePower() != PowerIndex.BARRAGE && getPlayerPos2() == PlayerPosIndex.BARRAGE) {
                setPlayerPos2(PlayerPosIndex.NONE);
            }
            if (getActivePower() != PowerIndex.SNEAK_ATTACK && getPlayerPos2() == PlayerPosIndex.SWEEP_KICK) {
                setPlayerPos2(PlayerPosIndex.NONE);
            }
            if (getActivePower() != VampireGeneralPowers.POWER_SPIKE && getPlayerPos2() == PlayerPosIndex.HAIR_SPIKE) {
                setPlayerPos2(PlayerPosIndex.NONE);
            }
            if (this.self instanceof Player PE && PE.isSpectator()) {
                ((StandUser) this.getSelf()).roundabout$setActive(false);
            }
        }
        super.tickPower();
    }

    /**Releasing right click normally stops guarding but that's something you can adjust*/
    public boolean clickRelease(){
        return false;
    }

    public boolean setPowerGuard() {
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.GUARD);
        return true;
    }

}
