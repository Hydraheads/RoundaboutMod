package net.hydra.jojomod.entity.npcs;

import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.entity.visages.StandUsingNPC;
import net.hydra.jojomod.entity.visages.mobs.AyaNPC;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.index.ShapeShifts;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.PowersCinderella;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.item.StandDiscItem;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class Aesthetician extends StandUsingNPC {
    public Aesthetician(EntityType<? extends JojoNPC> p_35384_, Level p_35385_) {
        super(p_35384_, p_35385_);
    }

    @Override
    public StandDiscItem getDisc(){
        return ((StandDiscItem) ModItems.STAND_DISC_CINDERELLA);
    }

    @Override
    public boolean hidesInGeneral(){
        return true;
    }
    @Override
    public boolean runsIfLow(){
        return true;
    }

    public List<Player> interactingWith = new ArrayList<>();

    public void initInteractCheck(){
        if (interactingWith == null) {interactingWith = new ArrayList<>();}
    }

    public void addPlayerToList(Player pl){
        initInteractCheck();
        if (!interactingWith.contains(pl)){
            interactingWith.add(pl);
        }
    }

    public boolean canSummonStandThroughFightOrFlightActive(){
        return true;
    }
    public void removePlayerFromList(Player pl){
        initInteractCheck();
        interactingWith.remove(pl);
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor $$0, DifficultyInstance $$1, MobSpawnType $$2, @Nullable SpawnGroupData $$3, @Nullable CompoundTag $$4) {
        rollStand();
        if (!(this instanceof AyaNPC)) {
            RandomSource $$5 = $$0.getRandom();
            if ($$5.nextFloat() < 0.2F) {
                setSkinNumber(2);
            } else if ($$5.nextFloat() < 0.4F) {
                setSkinNumber(3);
            } else if ($$5.nextFloat() < 0.6F) {
                setSkinNumber(4);
            } else if ($$5.nextFloat() < 0.8F) {
                setSkinNumber(5);
            }
        }
        return super.finalizeSpawn($$0,$$1,$$2,$$3,$$4);
    }

    public InteractionResult mobInteract(Player $$0, InteractionHand $$1) {
        ItemStack $$2 = $$0.getItemInHand($$1);
        if ($$0.level().isClientSide() && ((StandUser)this).roundabout$getStandPowers() instanceof PowersCinderella) {

            IPlayerEntity ple = ((IPlayerEntity) $$0);
            byte shape = ple.roundabout$getShapeShift();
            ShapeShifts shift = ShapeShifts.getShiftFromByte(shape);
            if (ShapeShifts.isZombie(shift) || ShapeShifts.isSkeleton(shift)) {
                return (InteractionResult.sidedSuccess(this.level().isClientSide));
            }
            ClientUtil.setCinderellaUI(true,this.getId());
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        } else {
            return super.mobInteract($$0, $$1);
        }
    }

    @Override
    public boolean isUsingBrain(){
        initInteractCheck();
        if (this.getTarget() == null){
            if (!interactingWith.isEmpty()) {
                return false;
            }
        } else {
            if (!interactingWith.isEmpty()) {

                List<Player> iteratable2 = new ArrayList<>(interactingWith);
                for (Player value : iteratable2) {
                    if (!this.level().isClientSide() && value instanceof ServerPlayer PE && value.isAlive()) {
                        ModPacketHandler.PACKET_ACCESS.sendSimpleByte(PE, PacketDataIndex.S2C_SIMPLE_CLOSE_THE_RELLA);
                    }
                }
                interactingWith = new ArrayList<>();
            }
        }
        return true;
    }
    @Override
    public void useNotBrain(){
        initInteractCheck();
        if (!interactingWith.isEmpty()){
            List<Player> iteratable = new ArrayList<>(interactingWith);
            List<Player> iteratable2 = new ArrayList<>(interactingWith);
            for (Player value : iteratable) {
                if (value == null || value.isRemoved() || !value.isAlive() || value.distanceTo(this) > 15){
                    if (!this.level().isClientSide() && value instanceof ServerPlayer PE && value.isAlive()){
                        ModPacketHandler.PACKET_ACCESS.sendSimpleByte(PE, PacketDataIndex.S2C_SIMPLE_CLOSE_THE_RELLA);
                    }
                    iteratable2.remove(value);
                    interactingWith =  iteratable2;
                }
            }
            List<Player> iteratable3 = new ArrayList<>(interactingWith);
            if (!iteratable3.isEmpty()){
                Player plrot = iteratable3.get(0);
                if (plrot != null){
                    this.setXRot(MainUtil.getLookAtEntityPitch(this, plrot));
                    float yrot = MainUtil.getLookAtEntityYaw(this, plrot);
                    this.setYRot(yrot);
                    this.setYHeadRot(yrot);
                }
                this.navigation.stop();
            }
        }
    }
}
