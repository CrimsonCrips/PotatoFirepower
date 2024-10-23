package com.crimsoncrips.potatofirepower.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public final class PPConfigHolder {

    public static final PPConfigList POTATO_POWER;

    public static final ForgeConfigSpec POTATO_POWER_SPEC;

    static {
        {
            final Pair<PPConfigList, ForgeConfigSpec> interact = new ForgeConfigSpec.Builder().configure(PPConfigList::new);
            POTATO_POWER = interact.getLeft();
            POTATO_POWER_SPEC = interact.getRight();
        }
    }
}