package com.example.jjf.registry;

import com.example.jjf.stand.powers.PowersJumpinJackFlash;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.hydra.jojomod.item.StandDiscItem;
import net.minecraft.world.item.Item;

public final class ModItems {

    public static StandDiscItem JJF_STAND_DISC;
    public static MaxStandDiscItem JJF_STAND_DISC_MAX;

    private ModItems() {
    }

    public static StandDiscItem createStandDisc() {
        return new StandDiscItem(
                new Item.Properties()
                        .stacksTo(1),
                new PowersJumpinJackFlash(
                        null
                )
        );
    }

    public static MaxStandDiscItem createMaxStandDisc(
            StandDiscItem baseDisc
    ) {
        return new MaxStandDiscItem(
                new Item.Properties()
                        .stacksTo(1),
                new PowersJumpinJackFlash(
                        null
                ),
                baseDisc
        );
    }
}
