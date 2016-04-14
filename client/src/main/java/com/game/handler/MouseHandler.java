package com.game.handler;

import com.game.guis.GuiTexture;
import com.game.models.RawModel;
import com.game.renderEngine.Loader;
import com.game.toolbox.Maths;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.Arrays;

import static com.game.engineTester.MainGameLoop.guiTextures;

public class MouseHandler {

    private Vector2f[] selectedArea = new Vector2f[2];
    private Loader loader;

    public MouseHandler(Loader loader){
        this.loader = loader;
    }

    public void start() {
        detectSelection();
    }

    private void detectSelection() {
        if (Mouse.isButtonDown(0)) {
            if (selectedArea[0] == null) {
                selectedArea[0] = new Vector2f(Mouse.getX(),Mouse.getY());
            }
        }

        if (!Mouse.isButtonDown(3)) {
            selectedArea[1] = new Vector2f(Mouse.getX(),Mouse.getY());
        }
        if(selectedArea[0] != null){

        }
        System.out.println(Arrays.toString(selectedArea));
    }
}
