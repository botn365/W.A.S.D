package com.github.botn365.wootingmovment.client;

import com.github.botn365.wootingmovment.WootingInit;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;


public class KeyBindings {

    protected static final String OPEN_INVENTORY = "key.wootingmovment:open_inventory";
    protected static final String MOVMENT_CATAGORY = "key.wootingmovment:category";
    protected static final KeyBinding openInventory = new KeyBinding(OPEN_INVENTORY, Keyboard.KEY_LBRACKET, MOVMENT_CATAGORY);

    public static void registerKeyBindings() {
        ClientRegistry.registerKeyBinding(openInventory);
        FMLCommonHandler.instance().bus().register(new KeyBindings());
    }

    @SubscribeEvent
    public void keyPressEvent(InputEvent.KeyInputEvent event) {
        if (openInventory.isPressed()) {
            if (Minecraft.getMinecraft().currentScreen == null) {
                val value = WootingInit.error.value;
                if (value <= -1990 && value >= -1997) {
                    //show error messege that game needs restart
                    Minecraft.getMinecraft().displayGuiScreen(new GuiError(new String[]{
                            "Critical Error Game needs restart after fixed",
                            "Error message: "+WootingInit.error.name()
                    }));
                } else if (value < 1) {
                    Minecraft.getMinecraft().displayGuiScreen(new GuiError(new String[]{
                            "Non Critical Error Re-open gui to after fix",
                            "Error message: "+WootingInit.error.name()
                    }));
                } else {
                    if (!WootingInit.isInit()) WootingInit.init();
                    if (!WootingInit.isInit()) {
                        Minecraft.getMinecraft().displayGuiScreen(new GuiError(new String[]{
                                "Not initialised but was initialised",
                                "But got uninitialised And cant re-initialised",
                                "Error message: "+WootingInit.error.name()
                        }));
                    } else {
                        Minecraft.getMinecraft().displayGuiScreen(new SettingsGui());
                    }
                }
            }
        }
    }
}
