package com.server.game;

import org.lwjgl.opengl.Display;

public class Game {

    public Game(){
        DisplayManager.createDisplay();

        while(!Display.isCloseRequested()){

            DisplayManager.updateDisplay();
        }

        DisplayManager.closeDisplay();
    }

}
