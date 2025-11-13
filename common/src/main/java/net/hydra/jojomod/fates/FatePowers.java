package net.hydra.jojomod.fates;

import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.fates.powers.AbilityScapeBasis;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.client.Options;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class FatePowers extends AbilityScapeBasis {

    public FatePowers(LivingEntity self) {
        super(self);
    }
    public FatePowers() {
        super(null);
    }
    public void tick(){
    }


    /**This is imporant, on every fate class, override this and do something like
     *     return new VampireFate(entity); */
    public FatePowers generateFatePowers(LivingEntity entity){
        return new FatePowers(entity);
    }




    @Override

    public void xTryPower(byte index, boolean forced){
        tryPower(index, forced);
        tryPowerStuff();
    }

    @Override
    public void syncActivePower(){
        if (!this.self.level().isClientSide && this.self instanceof ServerPlayer SP){
            S2CPacketUtil.sendActivePowerFatePacket(SP,activePower);
        }
    }
    public void preCheckButtonInputAttack(boolean keyIsDown, Options options) {
        if (!hasStandActive(this.getSelf()) && !this.isGuarding()) {
            buttonInputAttack(keyIsDown, options);
        }
    }

    @Override
    public void tryPowerPacket(byte packet){
        if (this.self.level().isClientSide()) {
            C2SPacketUtil.tryPowerPacketF(packet);
        }
    }
    @Override
    public void tryIntPowerPacket(byte packet, int integer){
        if (this.self.level().isClientSide()) {
            C2SPacketUtil.tryIntPowerPacketF(packet,integer);
        }
    }
    @Override
    public void tryTripleIntPacket(byte packet, int in1, int in2, int in3){
        if (this.self.level().isClientSide()) {
            C2SPacketUtil.tryTripleIntPacketF(packet, in1, in2, in3);
        }
    }
    @Override
    public void tryBlockPosPowerPacket(byte packet, BlockPos pos){
        if (this.self.level().isClientSide()) {
            C2SPacketUtil.tryBlockPosPowerFPacket(packet, pos);
        }
    }
    @Override
    public void tryBlockPosPowerPacket(byte packet, BlockPos pos, HitResult hitResult){
        if (this.self.level().isClientSide()) {
            C2SPacketUtil.tryBlockPosPowerFPacket(packet, pos, hitResult);
        }
    }
    @Override
    public void tryPosPowerPacket(byte packet, Vec3 pos){
        if (this.self.level().isClientSide()) {
            C2SPacketUtil.tryPosPowerFPacket(packet, pos);
        }
    }
}
