package com.github.botn365.wootingmovment.client;

import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.util.ArrayList;

import static com.github.botn365.wootingmovment.Settings.*;

public class SettingsGui extends GuiScreen {
    public static final String[] curveNames = {
            "Global Default",
            "Movement",
            "X",
            "Y",
            "Forward",
            "Backward",
            "Left",
            "Right",
            "Flight",
            "Up",
            "Down"
    };
    ResponseGraphGui curve;
    ArrayList<GuiText> texts = new ArrayList<>();
    GuiButton movementButton;
    GuiButton flightButton;

    GuiButton increaseButton;
    GuiButton decreaseButton;
    /*
        0 = Global
        1 - 7 = movement
        8 - 10 = flight
      */
    int curveSelection = 0;
    GuiScreen child = null;


    @Override
    public void initGui() {
        super.initGui();
        val mc = Minecraft.getMinecraft();
        buttonList.add(new GuiButtonState(1,10,20,60,20,movementEnabled?ENABLED:DISABLED,movementEnabled));
        buttonList.add(new GuiButtonState(2,10,45,60,20,fleightEnabled?ENABLED:DISABLED,fleightEnabled));
        texts.add(new GuiText("Analog movment.",80,27));
        texts.add(new GuiText("Analog flight.",80,52));
        initGraphsSetting(70);
    }

    @Override
    public void drawScreen(int x, int y, float z) {
        this.drawDefaultBackground();
        super.drawScreen(x,y,z);
        drawText();
        curve.drawScreen(x,y,z);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 1:
                switchButtonState(button);
                movementEnabled = button.enabled;
                break;
            case 2:
                switchButtonState(button);
                fleightEnabled = button.enabled;
                break;
            case 3:
                if (stateGlobal == State.Unified) {
                    stateGlobal = State.Split;
                    decreaseButton.visible = true;
                    increaseButton.visible = true;
                    curveSelection = 1;
                    setNewSelection();
                    otherButtonState(true);
                } else {
                    stateGlobal = State.Unified;
                    decreaseButton.visible = false;
                    increaseButton.visible = false;
                    setCurveValues(0);
                    otherButtonState(false);
                }
                button.displayString = stateGlobal.name();
                break;
            case 4:
                if (stateMovement == State.Unified) {
                    stateMovement = State.XY;
                } else if (stateMovement == State.XY) {
                    stateMovement = State.Different;
                } else {
                    stateMovement = State.Unified;
                }
                setNewSelection();
                movementButton.displayString = stateMovement.name();
                break;
            case 5:
                if (stateFlight == State.Unified) {
                    stateFlight = State.UpDown;
                } else {
                    stateFlight = State.Unified;
                }
                setNewSelection();
                flightButton.displayString = stateFlight.name();
                break;
            case 6:
                decreaseSelection();
                break;
            case 7:
                increaseSelection();
                break;
            case 8:
                resetCurve();
                break;
            case 9:
                resetAllCurves();
                break;
        }

    }

    protected void initGraphsSetting(int y) {
        texts.add(new GuiText("Response Curve Settings",10,y+10));
        texts.add(new GuiText("Global Curve",80,y+30));
        texts.add(new GuiText("Movement Curve",80,y+55));
        texts.add(new GuiText("Flight Curve",80,y+80));
        buttonList.add(new GuiButton(3,10,y + 25,60,20,stateGlobal.name()));
        movementButton = new GuiButton(4,10,y + 50,60,20,stateMovement.name());
        flightButton = new GuiButton(5,10,y + 75,60,20,stateFlight.name());
        decreaseButton = new GuiButton(6,168,y+50,20,20,"<");
        increaseButton = new GuiButton(7,292,y+50,20,20,">");
        curve = new ResponseGraphGui(defaultCurve,190,y + 25,100,70,10,"Global Default");
        buttonList.add(new GuiButton(8,188,y + 100,40,13,"RESRET"));
        buttonList.add(new GuiButton(9,233,y + 100,60,13,"RESRET ALL"));
        if (stateGlobal == State.Unified) {
            otherButtonState(false);
            decreaseButton.visible = false;
            increaseButton.visible = false;
        } else {
            curveSelection = 1;
            curve.setResponseCurve(movementCurves.get(0));
        }
        buttonList.add(decreaseButton);
        buttonList.add(increaseButton);
        buttonList.add(movementButton);
        buttonList.add(flightButton);
        setNewSelection();
    }

    protected void resetCurve() {
        curve.resetCurve();
    }

    protected void resetAllCurves() {
        resetPointsToDefault(defaultCurve);
        for (val curve : movementCurves) {
            resetPointsToDefault(curve);
        }
        for (val curve : flightCurves) {
            resetPointsToDefault(curve);
        }
    }

    protected void setNewSelection() {
        if (curveSelection == 0) {
            if (stateGlobal == State.Split) {
                curveSelection = 1;
                increaseToMovement();
            }
        } else if  (curveSelection < 8) {
            increaseToMovement();
        } else {
            increaseToFlight();
        }
        setCurveValues(curveSelection);
    }

    protected void decreaseSelection() {
        curveSelection--;
        if (curveSelection<0) curveSelection = curveNames.length-1;
        if (curveSelection == 0) {
            if (stateGlobal == State.Split) {
                curveSelection = curveNames.length-1;
                decreaseToFlight();
            }
        } else if (curveSelection < 8) {
            decreaseToMovement();
        } else {
            decreaseToFlight();
        }
        setCurveValues(curveSelection);
    }

    protected void increaseSelection() {
        curveSelection++;
        curveSelection %= curveNames.length;
        if (curveSelection == 0) {
            if (stateGlobal == State.Split) {
                curveSelection = 1;
                increaseToMovement();
            }
        } else if (curveSelection < 8) {
            increaseToMovement();
        } else {
            increaseToFlight();
        }
        setCurveValues(curveSelection);
    }

    protected void decreaseToMovement() {
        if (stateMovement == State.Unified) {
            curveSelection = 1;
        } else if (stateMovement == State.XY) {
            if (curveSelection < 2) {
                if (stateGlobal == State.Unified) curveSelection = 0;
                else {
                    curveSelection = curveNames.length-1;
                    decreaseToFlight();
                }
            } else if (curveSelection > 3) {
                curveSelection = 3;
            }
        } else {
            if (curveSelection < 4) {
                if (stateGlobal == State.Unified) curveSelection = 0;
                else {
                    curveSelection = curveNames.length-1;
                    decreaseToFlight();
                }
            }
        }
    }

    protected  void increaseToMovement() {
        if (stateMovement == State.Unified) {
            if (curveSelection > 1) {
                curveSelection = 8;
                increaseToFlight();
            }
        } else if (stateMovement == State.XY) {
            if (curveSelection < 2) curveSelection = 2;
            else if (curveSelection > 3) {
                curveSelection = 8;
                increaseToFlight();
            }
        } else {
            if (curveSelection < 4) curveSelection = 4;
        }
    }

    protected void decreaseToFlight() {
        if (stateFlight == State.Unified) {
            curveSelection = 8;
        } else {
            if (curveSelection < 9) {
                curveSelection = 7;
                decreaseToMovement();
            }
        }
    }

    protected  void increaseToFlight() {
        if (stateFlight == State.Unified) {
            if (curveSelection > 8) {
                if (stateGlobal == State.Unified) curveSelection = 0;
                else {
                    curveSelection = 1;
                    increaseToMovement();
                }
            }
        } else {
            if (curveSelection < 9) curveSelection = 9;
        }
    }

    protected void setCurveValues(int value) {
        if (value == 0) {
            curve.setResponseCurve(defaultCurve);
        } else if (value < 8) {
            curve.setResponseCurve(movementCurves.get(value-1));
        } else {
            curve.setResponseCurve(flightCurves.get(value-8));
        }
        curve.setName(curveNames[value]);
    }

    protected void otherButtonState(boolean state) {
        if (movementButton ==  null) return;
        movementButton.enabled = state;
        flightButton.enabled = state;
    }

    public static final String ENABLED = "Enabled";
    public static final String DISABLED = "Disabled";

    protected void switchButtonState(GuiButton button) {
        button.enabled = !button.enabled;
        if (button.enabled) {
            button.displayString = ENABLED;
        } else {
            button.displayString = DISABLED;
        }
    }


    @Override
    protected void mouseClicked(int x1, int y1, int type) {
        super.mouseClicked(x1,y1,type);
        curve.mouseClicked(x1,y1,type);
    }

    @Override
    protected void keyTyped(char p_73869_1_, int key) {
        if (key == 1 || key == KeyBindings.openInventory.getKeyCode()) {
            if (child == null) {
                this.mc.displayGuiScreen((GuiScreen)null);
                this.mc.setIngameFocus();
            } else {
                this.mc.displayGuiScreen(this);
            }
        }
    }

    @Override
    protected void mouseClickMove(int x, int y, int p_146273_3_, long p_146273_4_) {
        curve.mouseClickMove(x,y,p_146273_3_,p_146273_4_);
    }

    protected void drawText() {
        for (val text : texts) {
            text.drawScreen(0,0,0);
        }
    }

}
