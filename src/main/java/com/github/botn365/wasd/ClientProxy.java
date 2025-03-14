package com.github.botn365.wasd;

import com.github.botn365.wasd.client.KeyBindings;
import cpw.mods.fml.common.event.*;

public class ClientProxy extends CommonProxy {

    public static KeyBindings keyBindings;

    // preInit "Run before anything else. Read your config, create blocks, items,
    // etc, and register them with the GameRegistry."
    public void preInit(FMLPreInitializationEvent event) 	{
        WASDInit.init();
        Config.syncronizeConfiguration(event.getSuggestedConfigurationFile());
    }

    // load "Do your mod setup. Build whatever data structures you care about. Register recipes."
    public void init(FMLInitializationEvent event) {
        super.init(event);
        keyBindings = new KeyBindings();
    }

    // postInit "Handle interaction with other mods, complete your setup based on this."
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

    public void serverAboutToStart(FMLServerAboutToStartEvent event) {
        super.serverAboutToStart(event);
    }

    // register server commands in this event handler
    public void serverStarting(FMLServerStartingEvent event) {
        super.serverStarting(event);
    }

    public void serverStarted(FMLServerStartedEvent event) {
        super.serverStarted(event);
    }

    public void serverStopping(FMLServerStoppingEvent event) {
        super.serverStopping(event);
    }

    public void serverStopped(FMLServerStoppedEvent event) {
        super.serverStopped(event);
    }
}