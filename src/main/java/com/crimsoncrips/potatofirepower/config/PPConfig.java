package com.crimsoncrips.potatofirepower.config;

import static com.mojang.text2speech.Narrator.LOGGER;

public class PPConfig {

    public static int FIRE_DELAY;


    public static void bake() {
        try {

            FIRE_DELAY = PPConfigHolder.POTATO_POWER.FIRE_DELAY.get();



        } catch (Exception e) {
            LOGGER.warn("An exception was caused trying to load the config for Potato Firepower.");
            e.printStackTrace();
        }
    }

}
