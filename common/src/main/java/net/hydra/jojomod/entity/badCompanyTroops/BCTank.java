package net.hydra.jojomod.entity.badCompanyTroops;

import net.hydra.jojomod.entity.stand.MultipleTypeStand;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public class BCTank extends BCTroop{
    public BCTank(EntityType<? extends BCTroop> entityType, Level world) {
        super(entityType, world);
    }

    public static AttributeSupplier.Builder createStandAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED,
                0.12F).add(Attributes.MAX_HEALTH, 12.0).add(Attributes.ATTACK_DAMAGE, 7.0);
    }

    private static final EntityDataAccessor<Byte> CURRENT_ANIM = SynchedEntityData.defineId(BCSoldier.class, EntityDataSerializers.BYTE);


    public static final byte
            IDLE = 0;

    public void setAnim(byte value) {
        this.entityData.set(CURRENT_ANIM, value);
    }
    public byte getAnim() {
        return this.entityData.get(CURRENT_ANIM);
    }

    public final static float width = 0.4f;
    public final static float height = 0.32f;

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CURRENT_ANIM, IDLE);
    }

    @Override
    public void setupAnimationStates() {
        //super.setupAnimationStates();
        if (this.getUser() != null) {

        }
    }

    @Override
    public void tick() {
        super.tick();

        /*float pitch = this.getXRot();
        float yaw = this.getYRot();

        this.setXRot(pitch);
        this.setYRot(yaw);
        this.setYBodyRot(yaw);
        this.xRotO = pitch;
        this.yRotO = yaw;*/
    }
}
