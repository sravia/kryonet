package com.server.game;

import com.server.game.model.RawModel;
import com.server.game.model.TexturedModel;
import com.server.game.shaders.StaticShader;
import com.server.game.textures.ModelTexture;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;

public class Game {

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;
    private static final int FPS_CAP = 120;

    public Game() {
        createDisplay();

        Loader loader = new Loader();
        Renderer renderer = new Renderer();
        StaticShader shader = new StaticShader();


        float[] vertices = {
                -0.5f,0.5f,0,   //V0
                -0.5f,-0.5f,0,  //V1
                0.5f,-0.5f,0,   //V2
                0.5f,0.5f,0     //V3
        };

        int[] indices = {
                0,1,3,  //Top left triangle (V0,V1,V3)
                3,1,2   //Bottom right triangle (V3,V1,V2)
        };

        float[] textureCoords = {
            0,0,
                0,1,
                1,1,
                1,0
        };

        RawModel model = loader.loadToVAO(vertices,textureCoords,indices);
        ModelTexture texture = new ModelTexture(loader.loadTexture("image"));
        TexturedModel texturedModel = new TexturedModel(model,texture);

        while (!Display.isCloseRequested()) {
            renderer.prepare();
            shader.start();
            renderer.render(texturedModel);
            shader.stop();
            updateDisplay();
        }

        shader.cleanUp();
        loader.cleanUp();
        closeDisplay();
    }

    public void createDisplay() {
        ContextAttribs attributes = new ContextAttribs(3, 2).withForwardCompatible(true).withProfileCore(true);

        try {
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.create(new PixelFormat(), attributes);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        GL11.glViewport(0, 0, WIDTH, HEIGHT);
    }

    public void updateDisplay() {
        Display.sync(FPS_CAP);
        Display.update();
    }

    public void closeDisplay() {
        Display.destroy();
    }
}
