package com.github.botn365.wootingmovment.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;


public class KeyBindings {

    protected static final String OPEN_INVENTORY = "key.wootingmovment:open_inventory";
    protected static final String MOVMENT_CATAGORY = "key.wootingmovment:category";
    protected static final KeyBinding openInventory = new KeyBinding(OPEN_INVENTORY, Keyboard.KEY_RBRACKET, MOVMENT_CATAGORY);

    public static void registerKeyBindfings() {
        ClientRegistry.registerKeyBinding(openInventory);
        FMLCommonHandler.instance().bus().register(new KeyBindings());
    }

    @SubscribeEvent
    public void keyPressEvent(InputEvent.KeyInputEvent event) {
        if (openInventory.isPressed()) {
            System.out.println("button pressed");
            Minecraft.getMinecraft().displayGuiScreen(new SettingsGui());
            //open gui
        }
    }
}
