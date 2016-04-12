package com.server.game.skybox;

import com.server.Config;
import com.server.game.entities.Camera;
import com.server.game.shaders.ShaderProgram;
import com.server.game.toolbox.Maths;
import org.lwjgl.util.vector.Matrix4f;

public class SkyboxShader extends ShaderProgram {

    private static final String VERTEX_FILE = Config.GAME_PATH + "skybox/skyboxVertexShader.txt";
    private static final String FRAGMENT_FILE = Config.GAME_PATH + "skybox/skyboxFragmentShader.txt";

    private int location_projectionMatrix;
    private int location_viewMatrix;

    public SkyboxShader(){
        super(VERTEX_FILE,FRAGMENT_FILE);
    }

    public void loadProjectionMatrix(Matrix4f matrix){
        super.loadMatrix(location_projectionMatrix,matrix);
    }

    public void loadViewMatrix(Camera camera){
        Matrix4f matrix = Maths.createViewMatrix(camera);
        matrix.m30 = 0;
        matrix.m31 = 0;
        matrix.m32 = 0;
        super.loadMatrix(location_viewMatrix,matrix);
    }

    protected void getAllUniformLocations() {
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
    }

    protected void bindAttributes() {
        super.bindAttribute(0,"position");
    }
}
