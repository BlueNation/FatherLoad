package com.github.BlueNation.FatherLoad;

import net.minecraft.util.EnumChatFormatting;

public final class Reference {
    //The values are filled by the ForgeGradle 1.2 replace method.
    //The location of this file must be defined in gradle.properties under project_reference
    public static final String MODID = "@MODID@";
    public static final String NAME = "@NAME@";
    public static final String VERSION = "@VERSION@";
    public static final int SIDEWAYS_LAND_WORLD_ID = 69;

    public static final String MARKING =
            EnumChatFormatting.BLUE + "Father" +
                    EnumChatFormatting.DARK_BLUE + "Load" +
                    EnumChatFormatting.BLUE + ": Industries";
    public static final String MARKING_DEBUG =
            EnumChatFormatting.BLUE + "Father" +
                    EnumChatFormatting.DARK_BLUE + "Load" +
                    EnumChatFormatting.BLUE + ": Outdustries";

    private Reference() {}
}
