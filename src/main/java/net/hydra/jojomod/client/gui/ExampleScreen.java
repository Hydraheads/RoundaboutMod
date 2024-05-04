package net.hydra.jojomod.client.gui;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ExampleScreen extends CottonClientScreen {
    public ExampleScreen(GuiDescription description) {
        super(description);
    }
}
