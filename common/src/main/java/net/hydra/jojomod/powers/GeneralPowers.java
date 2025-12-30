package net.hydra.jojomod.powers;

import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.fates.powers.AbilityScapeBasis;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
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
}
