package com.github.botn365.wootingmovment.client;

import com.github.botn365.wootingmovment.Settings;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;

public class ResponseGraphGui extends Gui {
    int lx;
    int ly;
    int lw;
    int lh;
    float pointSize;
    String name;
    int color = 14737632;
    ResponseCurve response;


    //TODO reset button

    public ResponseGraphGui(ResponseCurve response, int lx, int ly, int lw, int lh, float pointSize, String name) {
        this.response = response;
        this.lx = lx;
        this.ly = ly + 10;
        this.lw = lw;
        this.lh = lh - 10;
        this.pointSize = pointSize;
        this.name = name;
    }


    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
        drawBackground(lx,ly,lx + lw,ly + lh,0.1f,0.1f,0.1f,1);
        drawGrid();
        drawBottom();
        val points = response.getPoints();
        for (int i = 0; i < points.size()-1; i++) {
            val pointA = points.get(i);
            val pointB = points.get(i+1);
            drawLine(normalizeX(pointA.x) , normalizeY(pointA.y), normalizeX(pointB.x),normalizeY(pointB.y),10);
        }
        this.drawCenteredString(Minecraft.getMinecraft().fontRenderer, name, lx + (lw/2),ly-10, color);
    }

    public void setResponseCurve(ResponseCurve response) {
        this.response = response;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected int held;

    public void mouseClicked(int x1, int y1, int x2) {
        held = isOverAPoint(x1,y1);
    }

    public void mouseClickMove(int x, int y, int p_146273_3_, long p_146273_4_) {
        if (held > -1) {
            val rez = response.getPoints();
            int maxSize = rez.size()-1;
            int xMax = held == maxSize ? lx + lw : normalizeX(rez.get(held+1).x);
            int xMin = held == 0 ? lx : normalizeX(rez.get(held-1).x);
            int yMax = held == 0 ? ly + lh : normalizeY(rez.get(held-1).y);
            int yMin = held == maxSize ? ly : normalizeY(rez.get(held+1).y);

            x = Math.min(xMax,Math.max(x,xMin));
            y = Math.min(yMax,Math.max(y,yMin));
            float xDiff = x - lx;
            float yDiff = ly + lh - y;
            rez.get(held).x = xDiff/lw;
            rez.get(held).y = yDiff/lh;
        }
    }

    protected int normalizeX(float x) {
        return (int) (lx + lw * x);
    }

    protected int normalizeY(float y) {
        return (int) (ly + lh - lh * y);
    }

    protected int isOverAPoint(int x, int y) {
        val resp = response.getPoints();
        for (int i = 0; i < resp.size(); i++) {
            if (isOverPoint(x,y,resp.get(i))) {
                return i;
            }
        }
        return -1;
    }

    protected boolean isOverPoint(int x, int y, ResponseCurve.Point point) {
        float pointXPos = normalizeX(point.x);
        float pointYPos = normalizeY(point.y);
        float rad = pointSize/2;
        return pointXPos - rad <= x && pointXPos + rad >= x && pointYPos - rad <= y && pointYPos + rad >= y;
    }


    public void drawLine(float x1, float y1, float x2, float y2, float thickness) {
        val tes = Tessellator.instance;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(1f,0.5f,0f,1f);
        GL11.glLineWidth(4);
        tes.startDrawing(GL11.GL_LINES);
        tes.addVertex((double) x1,(double) y1,0.0D);
        tes.addVertex((double) x2,(double) y2,0.0D);
        tes.draw();
        GL11.glPointSize(10);
        tes.startDrawing(GL11.GL_POINTS);
        tes.addVertex((double) x1,(double) y1,0.0D);
        tes.addVertex((double) x2,(double) y2,0.0D);
        tes.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public void drawBackground(int x1, int y1, int x2, int y2,float r,float g, float b, float a) {
        val tes = Tessellator.instance;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(r,g,b,a);
        tes.startDrawingQuads();
        tes.addVertex((double) x1,(double) y2,0);
        tes.addVertex((double) x2,(double) y2,0);
        tes.addVertex((double) x2,(double) y1,0);
        tes.addVertex((double) x1,(double) y1,0);
        tes.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public void drawGrid() {
        val tes = Tessellator.instance;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(1,1,1,1);
        GL11.glLineWidth(10);
        tes.startDrawing(GL11.GL_LINE_LOOP);
        tes.addVertex(lx,ly,0);
        tes.addVertex(lx,ly+lh,0);
        tes.addVertex(lx+lw,ly+lh,0);
        tes.addVertex(lx+lw,ly,0);
        tes.draw();
        GL11.glLineWidth(4);
        tes.startDrawing(GL11.GL_LINES);
        tes.addVertex(lx,ly+(lh/3),0);
        tes.addVertex(lx+lw,ly+(lh/3),0);
        tes.addVertex(lx,ly+((lh/3)*2),0);
        tes.addVertex(lx+lw,ly+((lh/3)*2),0);

        tes.addVertex(lx + (lw/3),ly,0);
        tes.addVertex(lx + (lw/3),ly+lh,0);
        tes.addVertex(lx + ((lw/3)*2),ly,0);
        tes.addVertex(lx + ((lw/3)*2),ly+lh,0);
        tes.draw();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public void drawBottom() {
        val tes = Tessellator.instance;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(0.9f,0.9f,0,1);
        tes.startDrawingQuads();
        val rez = response.getPoints();
        for (int i = 0; i < rez.size()-1; i++) {
            addBottomDraw(rez.get(i),rez.get(i+1));
        }
        tes.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public void addBottomDraw(ResponseCurve.Point A, ResponseCurve.Point B) {
        val tes = Tessellator.instance;
        tes.addVertex((double) normalizeX(A.x),(double) ly + lh,0);
        tes.addVertex((double) normalizeX(B.x),(double) ly + lh,0);
        tes.addVertex((double) normalizeX(B.x),(double) normalizeY(B.y),0);
        tes.addVertex((double) normalizeX(A.x),(double) normalizeY(A.y),0);
    }
}
