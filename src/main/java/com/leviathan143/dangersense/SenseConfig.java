package com.leviathan143.dangersense;

import net.minecraftforge.common.config.Config;

@Config(modid = DangerSense.MOD_ID)
public class SenseConfig {
    private SenseConfig() {
        throw new UnsupportedOperationException();
    }

    @Config.RangeDouble(min = 1.0, max = 1024.0)
    @Config.Comment("The radius of monsters search")
    public static double searchRadius = 16.0;
}