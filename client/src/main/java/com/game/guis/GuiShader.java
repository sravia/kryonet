package com.game.guis;

import com.Config;
import com.game.shaders.ShaderProgram;
import org.lwjgl.util.vector.Matrix4f;


public class GuiShader extends ShaderProgram {

    private static final String VERTEX_FILE = Config.GAME_PATH+"guis/guiVertexShader.txt";
    private static final String FRAGMENT_FILE = Config.GAME_PATH+"guis/guiFragmentShader.txt";

    private int location_transformationMatrix;

    public GuiShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    public void loadTransformation(Matrix4f matrix) {
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }


}
