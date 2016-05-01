package com.engine.handler;

import com.Config;
import com.engine.Engine;
import com.engine.guis.GuiTexture;
import com.engine.rendering.Loader;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import java.util.Arrays;

public class MouseHandler {

    private Vector2f[] selectedArea = new Vector2f[2];
    private Loader loader;
    private GuiTexture guiTexture;

    public MouseHandler(Loader loader){
        this.loader = loader;

        float[] vertices = {
                0.0f,  0.5f, 0.0f,
                -0.5f, 0.5f, 0.0f,
                -0.5f, 0.0f, 0.0f,
                0.0f,0.0f,0.0f

        };
        guiTexture = new GuiTexture(loader.loadTexture("my/black"), loader.loadToVAO(vertices,3),new Vector2f(0.1f,0.1f),0.5f);
        Engine.guiTextures.add(guiTexture);
    }

    public void start() {
        detectSelection();
    }

    private void detectSelection() {
        float x = (float) (-1.0 + 2.0 * Mouse.getX() / Config.WIDTH);
        float y = (float) -(1.0 - 2.0 * Mouse.getY() / Config.HEIGHT);

        if (Mouse.isButtonDown(0)) {
            if (selectedArea[0] == null) {
                selectedArea[0] = new Vector2f(x,y);
            }
            selectedArea[1] = new Vector2f(x,y);
        }else{
            selectedArea[0] = null;
            selectedArea[1] = null;
        }



        if(selectedArea[0] != null && selectedArea[1] != null){
            //guiTexture.setPosition(new Vector2f((selectedArea[0].y - selectedArea[1].y) / 2,(selectedArea[0].y - selectedArea[1].y) / 2));

        }
        System.out.println(Arrays.toString(selectedArea));
    }
}
