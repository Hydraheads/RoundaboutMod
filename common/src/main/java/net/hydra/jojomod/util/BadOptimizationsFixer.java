package net.hydra.jojomod.util;

import java.util.function.BooleanSupplier;

public class BadOptimizationsFixer implements BooleanSupplier {

    public BadOptimizationsFixer() {}

    @Override
    public boolean getAsBoolean() {
        return true;
    }
}
