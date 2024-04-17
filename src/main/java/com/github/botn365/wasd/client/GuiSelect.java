package com.github.botn365.wasd.client;

import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;


public class GuiSelect extends Gui {
    ArrayList<GuiText> texts = new ArrayList<>();

    ArrayList<Selection> selections = new ArrayList<>();

    Selection current;

    int x;
    int y;
    int w;
    int h;
    int color = 14737632;

    String title;

    boolean isSelecting = false;

    public GuiSelect(int xPos, int yPos, int width,String title) {
        this.x = xPos;
        this.y = yPos;
        this.w = width;
        this.title = title;
    }

    public void addSelection(Selection select) {
        if (current == null) current = select;
        selections.add(select);
    }

    public void setCurrent(Selection select) {
        current = select;
    }

    public void drawScreen(int x, int y, float z) {

        if (isSelecting) {
            int hight = 20;
            for (val sel : selections) {
                hight += sel.length()*10+5;
            }
            drawBackGround(15,hight);
            int len = 35;
            for (val sel : selections) {
                if (isHoveringOver(x,y,len-5,sel.length() * 10)) {
                    drawSelectRing(len,sel);
                    break;
                }
                len += sel.length()*10+5;
            }
        } else {
            int len = 25 + (current == null ? 0 : current.length() * 10);
            if (isHoveringOver(x,y,5,len)) {
                drawBackGround(this.y+5,len);
            }
        }

        drawText(title,this.y+10);
        int len = 30;
        if (isSelecting) {
            len += drawList(this.y+30);
        } else {
            len += drawCurrent(this.y+30);
        }
        h = len;
    }

    protected int drawCurrent(int y) {
        if (current != null) {
            return drawSelection(current,y);
        }
        return 0;
    }

    protected int drawList(int y) {
        int yOffset = 0;
        for ( val select : selections) {
            yOffset += drawSelection(select,yOffset+y) + 5;
        }
        return yOffset;
    }

    protected void drawBackGround(int y,int len) {
        //drawing square
        val tes = Tessellator.instance;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(0.1f,0.1f,0.1f,1f);
        tes.startDrawingQuads();
        tes.addVertex((double) x,(double) y+len,0);
        tes.addVertex((double) x+w,(double) y+len,0);
        tes.addVertex((double) x+w,(double) y,0);
        tes.addVertex((double) x,(double) y,0);
        tes.draw();

        //drawing lines
        GL11.glColor4f(0.7f,0.7f,0.7f,1);
        tes.startDrawing(GL11.GL_LINE_LOOP);
        tes.addVertex((double) x,(double) y+len,0);
        tes.addVertex((double) x+w,(double) y+len,0);
        tes.addVertex((double) x+w,(double) y,0);
        tes.addVertex((double) x,(double) y,0);
        tes.draw();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    protected void drawSelectRing(int y,Selection sel) {
        int len = sel.length()*10+5;
        val tes = Tessellator.instance;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(0.9f,0.9f,0.9f,1);
        tes.startDrawing(GL11.GL_LINE_LOOP);
        tes.addVertex((double) x,(double) y+len,0);
        tes.addVertex((double) x+w,(double) y+len,0);
        tes.addVertex((double) x+w,(double) y,0);
        tes.addVertex((double) x,(double) y,0);
        tes.draw();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    protected int drawSelection(Selection select, int y) {
        val texts = select.getText();
        int index = 0;
        for (val txt : texts) {
            drawText(txt,y+(index * 10));
            ++index;
        }
        return index * 10;
    }

    protected void drawText(String text,int y) {
        drawCenteredString(getFont(),text,x + (w/2),y,color);
    }

    protected final FontRenderer getFont() {
        return Minecraft.getMinecraft().fontRenderer;
    }

    protected boolean isHoveringOver(int x, int y,int yOffset, int len) {
        int yPos = this.y + yOffset;
        return x >= this.x && x <= this.x + w && y >= yPos && y<= yPos + len;
    }

    public void mouseClicked(int x1, int y1, int x2) {
        val xPos = normalizeX(x1);
        val yPos = normalizeY(y1);
        if (xPos < 0 || xPos > w || yPos < 0 || yPos > h) return;
        if (isSelecting) {
            int len = 30;
            for (val sel : selections) {
                if (isHoveringOver(x1,y1,len,sel.length() * 10)) {
                    if (sel.select()) {
                        current = sel;
                    }
                    break;
                }
                len += sel.length()*10+5;
            }
            isSelecting = false;
        } else {
            isSelecting = true;
        }
    }

    protected int normalizeX(int x) {
        return x - this.x;
    }

    protected int normalizeY(int y) {
        return y - this.y;
    }
}
