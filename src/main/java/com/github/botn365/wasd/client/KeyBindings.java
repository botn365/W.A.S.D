package com.github.botn365.wasd.client;

import com.github.botn365.wasd.WASDInit;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;


public class KeyBindings {

    protected static final String OPEN_INVENTORY = "WASD Open Inventory";
    protected static final String MOVMENT_CATAGORY = "key.wasd:category";
    protected final KeyBinding openInventory = new KeyBinding(OPEN_INVENTORY, Keyboard.KEY_LBRACKET, MOVMENT_CATAGORY);

    public KeyBindings() {
        ClientRegistry.registerKeyBinding(openInventory);
        FMLCommonHandler.instance().bus().register(this);
    }

    @SubscribeEvent
    public void keyPressEvent(InputEvent.KeyInputEvent event) {
        if (openInventory.isPressed()) {
            if (Minecraft.getMinecraft().currentScreen == null) {
                val value = WASDInit.error.value;
                if (value <= -1990 && value >= -1997) {
                    //show error messege that game needs restart
                    Minecraft.getMinecraft().displayGuiScreen(new GuiError(new String[]{
                            "Critical Error Game needs restart after fixed",
                            "Error message: "+ WASDInit.error.name()
                    }));
                } else if (value < 1) {
                    Minecraft.getMinecraft().displayGuiScreen(new GuiError(new String[]{
                            "Non Critical Error Re-open gui to after fix",
                            "Error message: "+ WASDInit.error.name()
                    }));
                } else {
                    if (!WASDInit.isInit()) WASDInit.init();
                    if (!WASDInit.isInit()) {
                        Minecraft.getMinecraft().displayGuiScreen(new GuiError(new String[]{
                                "Not initialised but was initialised",
                                "But got uninitialised And cant re-initialised",
                                "Error message: "+ WASDInit.error.name()
                        }));
                    } else {
                        Minecraft.getMinecraft().displayGuiScreen(new SettingsGui());
                    }
                }
            }
        }
    }
}
