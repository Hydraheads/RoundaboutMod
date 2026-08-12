package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.access.IPlayerEntityServer;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.gui.BlackSabbathPlayerInventoryMenu;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.index.ShapeShifts;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersBlackSabbath;
import net.hydra.jojomod.stand.powers.PowersCinderella;
import net.hydra.jojomod.util.BlackSabbathPlayerInventory;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.hydra.jojomod.util.config.ConfigManager;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class BlackSabbathEntity extends StandEntity implements HasCustomInventoryScreen {

    private MoveFunction positionUpdater;

    public BlackSabbathEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }

    public static final byte
            PART_5_ANIME = 1,
            PART_5_MANGA = 2,
            BURNING = 3,
            GIO_GIO = 4,
            VERDANT = 5,
            NIGHT = 6,
            DEPARTURE = 7,
            PHANTOM = 8,
            SWEET = 9,
            OCULUS = 10,
            SACTHOTH = 11,
            BEACH = 12;

    public final AnimationState coat_open = new AnimationState();
    public final AnimationState chest_open = new AnimationState();
    public final AnimationState chest_close = new AnimationState();

    public boolean shouldFloat = false;
    public void setShouldFloat(boolean bool){shouldFloat = bool;}
    public boolean shouldSelect = false;
    public void setShouldSelect(boolean bool){shouldSelect = bool;}
    public int tickDownSecond = 0;
    public void setTickDownSecond(int td){tickDownSecond = td;}

    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        if(this.getUser() != null){
            if (((StandUser)this.getUser()).roundabout$getStandPowers() instanceof PowersBlackSabbath pb){
                   switch (pb.moveMode) {
                      case 1 -> {
                          if (pb.active) {
                              this.coat_open.stop();
                              chest_close.stop();
                              this.chest_open.startIfStopped(this.tickCount);
                          } else {
                              this.chest_open.stop();
                              this.coat_open.stop();
                              this.chest_close.startIfStopped(this.tickCount);
                          }
                      }
                      case 2 -> {
                          this.chest_open.stop();
                          this.chest_close.stop();
                          this.coat_open.startIfStopped(this.tickCount);
                      }
                   }
            }
        } else {
            this.chest_open.stop();
            this.chest_close.stop();
            this.coat_open.startIfStopped(this.tickCount);
        }
    }

    @Override
    public boolean forceVisualRotation(){
        return true;
    }

    @Override
    public boolean lockPos(){
        if(this.getUser() != null && ((StandUser)this.getUser()).roundabout$getStandPowers() instanceof PowersBlackSabbath pb){
            return pb.moveMode == 2;
        }
        return false;
    }
    @Override
    public boolean hasNoPhysics(){
        if(this.getUser() != null && ((StandUser)this.getUser()).roundabout$getStandPowers() instanceof PowersBlackSabbath pb){
            return pb.moveMode == 2;
        }
        return false;
    }

    @Override
    public boolean isNoGravity() {
        if(this.getUser() != null && ((StandUser)this.getUser()).roundabout$getStandPowers() instanceof PowersBlackSabbath pb){
            return pb.moveMode == 2;
        }
        return false;
    }

    @Override
    public boolean standHasGravity() {
        if(this.getUser() != null && ((StandUser)this.getUser()).roundabout$getStandPowers() instanceof PowersBlackSabbath pb){
            return pb.moveMode != 2;
        }
        return true;
    }

    @Override
    public void tick(){
        validateUUID();
        float pitch = this.getXRot();
        float yaw = this.getYRot();


        if(shouldFloat && this.getUser() != null){
            if (!this.level().isClientSide()) {
                this.setXRot(pitch);
                this.setYRot(yaw);
                this.setYBodyRot(yaw);
                this.xRotO = pitch;
                this.yRotO = yaw;
            }
            if(((StandUser)this.getUser()).roundabout$getStandPowers() instanceof PowersBlackSabbath pb){
                if(tickDownSecond > 1){
                    tickDownSecond--;

                    if(tickDownSecond == 4){
                        this.forceDespawnSet = true;
                    }
                }
            }
        }
        super.tick();
        travelAhead(Entity::setPos);
    }
    public void travelAhead(Entity.MoveFunction positionUpdater) {
        if (this.getUser() != null) {
            if(((StandUser)this.getUser()).roundabout$getStandPowers() instanceof PowersBlackSabbath pb && pb.moveMode == 2) {
                Vec3 lvec = pb.getLookAngleChest(this.getUser().getYRot(), this.getUser());
                Position pn = this.getUser().getEyePosition().add(lvec.scale(-0.9F));
                positionUpdater.accept(this, pn.x(), this.getUser().getY() + (this.getUser().getBbHeight() / 2.35), pn.z());
            }
        }
    }

    @Override
    public boolean isAttackable() {return true;}
    @Override
    public boolean isPickable() {return true;}
    @Override
    public boolean skipAttackInteraction(Entity $$0) {return false;}
    @Override
    public void knockback(double $$0, double $$1, double $$2) {}
    public boolean isInvulnerable() {
        return false;
    }

    public void openCustomInventoryScreen(Player player) {
        if (!this.level().isClientSide) {
            ((IPlayerEntityServer)player).roundabout$openBlackSabbathInventory(this, player.getInventory());
        }
    }
}
