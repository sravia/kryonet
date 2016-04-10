package com.server.game.gui;

import com.server.Config;
import com.server.game.shaders.ShaderProgram;
import org.lwjgl.util.vector.Matrix4f;


public class GuiShader extends ShaderProgram {

    private static final String VERTEX_FILE = Config.GAME_PATH+"gui/guiVertexShader.txt";
    private static final String FRAGMENT_FILE = Config.GAME_PATH+"gui/guiFragmentShader.txt";

    private int location_transformationMatrix;

    public GuiShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    public void loadTransformation(Matrix4f matrix) {
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
    }

    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }

}