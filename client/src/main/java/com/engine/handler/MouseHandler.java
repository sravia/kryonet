package com.engine.handler;

import com.engine.rendering.Loader;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import java.util.Arrays;

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
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f(1.0f,1.0f,1.0f,0.5f);
            GL11.glVertex2f(selectedArea[0].x,selectedArea[0].y);
            GL11.glVertex2f(selectedArea[0].x,selectedArea[1].y);
            GL11.glVertex2f(selectedArea[1].x,selectedArea[1].y);
            GL11.glVertex2f(selectedArea[1].x,selectedArea[0].y);
            System.out.println("--------------------------");
            System.out.println(selectedArea[0].x + " - "+selectedArea[0].y);
            System.out.println(selectedArea[0].x + " - "+selectedArea[1].y);
            System.out.println(selectedArea[1].x + " - "+selectedArea[1].y);
            System.out.println(selectedArea[1].x + " - "+selectedArea[0].y);
            System.out.println("--------------------------");

            GL11.glEnd();
            GL11.glDisable(GL11.GL_BLEND);
        }
        System.out.println(Arrays.toString(selectedArea));
    }
}
