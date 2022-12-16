package com.github.botn365.wootingmovment.client;

import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import java.util.ArrayList;


public class GuiError extends GuiScreen {
    protected String[] errorMsg;
    ArrayList<GuiText> texts = new ArrayList<>();

    int color = 14737632;
    public GuiError(String[] errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public void initGui() {
        int xMid = this.width/2;
        int yMid = this.height/2 - (errorMsg.length/2)*20;

        texts.clear();
        texts.add(new GuiText("Wooting Analog Movment Error Gui",xMid,yMid));
        int offset = 1;
        for (val msg : errorMsg) {
            texts.add(new GuiText(msg,xMid,yMid+(20 * offset)));
            ++offset;
        }
    }

    @Override
    public void drawScreen(int x, int y, float z) {
        this.drawDefaultBackground();
        for (val txt : texts) {
            this.drawCenteredString(Minecraft.getMinecraft().fontRenderer, txt.text, txt.x, txt.y,color);
        }
    }

    @Override
    protected void keyTyped(char p_73869_1_, int key) {
        if (key == 1 || key == KeyBindings.openInventory.getKeyCode()) {
            this.mc.displayGuiScreen((GuiScreen)null);
            this.mc.setIngameFocus();
        }
    }
}
