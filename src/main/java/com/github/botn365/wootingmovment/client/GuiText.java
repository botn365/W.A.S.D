package com.github.botn365.wootingmovment.client;

import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class GuiText extends Gui {
    protected int x;
    protected int y;
    protected String text;

    int color = 14737632;

    public GuiText(String text,int x, int y) {
        this.x = x;
        this.y = y;
        this.text = text;
    }

    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
        val fr = Minecraft.getMinecraft().fontRenderer;
        this.drawCenteredString(fr,text,x + (fr.getStringWidth(text)/2),y,color);
    }
}
