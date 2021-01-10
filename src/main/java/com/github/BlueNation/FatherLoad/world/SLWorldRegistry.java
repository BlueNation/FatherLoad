package com.github.BlueNation.FatherLoad.world;

import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.registry.GameRegistry;

public class SLWorldRegistry {
    public static void mainRegistry() {
        initWorldGen();
    }

    public static void initWorldGen() {
        registerWorldGen(new SLWorldGenerator(), 100);
    }

    public static void registerWorldGen(IWorldGenerator worldGenClass, int weightedProbability) {
        GameRegistry.registerWorldGenerator(worldGenClass, weightedProbability);
    }
}
