package com.crimsoncrips.potatofirepower.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class PPConfigList {


//    public final ForgeConfigSpec.BooleanValue BLOOD_DAMAGE_DIFFERENCE_ENABLED;

    public final ForgeConfigSpec.IntValue FIRE_DELAY;
    



    public PPConfigList(final ForgeConfigSpec.Builder builder) {
        this.FIRE_DELAY = buildInt(builder, "FIRE_DELAY", " ", 10, 0, Integer.MAX_VALUE, "lower = faster, higher = slower , 0 = no delay");
//        this.STRADDLER_VENGEANCE_ENABLED = buildBoolean(builder, "STRADDLER_VENGEANCE_ENABLED", " ", true, "Straddlers have ammo,once exhausted it requires time to reload");

    }

    private static ForgeConfigSpec.BooleanValue buildBoolean(ForgeConfigSpec.Builder builder, String name, String catagory, boolean defaultValue, String comment){
        return builder.comment(comment).translation(name).define(name, defaultValue);
    }

    private static ForgeConfigSpec.IntValue buildInt(ForgeConfigSpec.Builder builder, String name, String catagory, int defaultValue, int min, int max, String comment){
        return builder.comment(comment).translation(name).defineInRange(name, defaultValue, min, max);
    }
}
