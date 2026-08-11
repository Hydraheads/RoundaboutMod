package net.hydra.jojomod.entity.stand;

import com.google.common.collect.Lists;
import net.hydra.jojomod.access.ILivingEntityAccess;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.KingCrimsonCloneEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

import java.util.List;

public class D4CEntity extends FollowingStandEntity {
    public D4CEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }
    public static final byte
            BASE = 1,
            COVER = 2,
            SPINE = 3,
            VELLER = 4,
            ROA = 5,
            DEPARTURE = 6,
            PROMO = 7,
            FOUNTAIN_BOY = 8,
            CONCEPT_ART = 9,
            EOH_BLUE = 10,
            EOH_PINK = 11,
            EOH_PURPLE = 12,
            EOH_YELLOW = 13,
            GOLDEN = 14,
            GOLDEN_V2 = 15,
            CURSED_CARD = 16,
            ULTRA_BRAND = 17,
            KEYHOLDER = 18,
            GRAY = 19,
            BLACK_WHITE = 20,
            REVERSE = 21,
            CHOCOLATE = 22,
            INVERSE = 23;

    public final AnimationState finalPunch = new AnimationState();
    public final AnimationState finalPunch2 = new AnimationState();
    public final AnimationState finalPunch3 = new AnimationState();
    public final AnimationState finalPunchWindup = new AnimationState();
    public final AnimationState chop = new AnimationState();
    public final AnimationState impale2 = new AnimationState();

    public final AnimationState hideFists = new AnimationState();

    public static final byte
            FINAL_1 = 82,
            FINAL_2 = 83,
            IMPALE_2 = 50,
            CHOP = 51;
    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        if (this.getAnimation() != BARRAGE) {
            this.hideFists.startIfStopped(this.tickCount);
        } else {
            this.hideFists.stop();
        }
        if (this.getAnimation() == FINAL_ATTACK_WINDUP) {
            this.finalPunchWindup.startIfStopped(this.tickCount);
        } else {
            this.finalPunchWindup.stop();
        }
        if (this.getAnimation() == FINAL_ATTACK) {
            this.finalPunch.startIfStopped(this.tickCount);
        } else {
            this.finalPunch.stop();
        }
        if (this.getAnimation() == FINAL_1) {
            this.finalPunch2.startIfStopped(this.tickCount);
        } else {
            this.finalPunch2.stop();
        }
        if (this.getAnimation() == FINAL_2) {
            this.finalPunch3.startIfStopped(this.tickCount);
        } else {
            this.finalPunch3.stop();
        }
        if (this.getAnimation() == IMPALE_2) {
            this.impale2.startIfStopped(this.tickCount);
        } else {
            this.impale2.stop();
        }
        if (this.getAnimation() == CHOP) {
            this.chop.startIfStopped(this.tickCount);
        } else {
            this.chop.stop();
        }
    }


    @Override
    public void tick(){
        super.tick();
    }

}
