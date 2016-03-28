package com.server.game;

import com.server.game.entities.Camera;
import com.server.game.entities.Entity;
import com.server.game.entities.Light;
import com.server.game.model.RawModel;
import com.server.game.model.TexturedModel;
import com.server.game.rendering.OBJLoader;
import com.server.game.rendering.Renderer;
import com.server.game.shaders.StaticShader;
import com.server.game.textures.ModelTexture;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector3f;

public class Game {

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;
    private static final int FPS_CAP = 120;

    public Game() {
        createDisplay();

        Loader loader = new Loader();
        StaticShader shader = new StaticShader();
        Renderer renderer = new Renderer(shader);

        RawModel model = OBJLoader.loadObjModel("dragon",loader);

        TexturedModel staticModel = new TexturedModel(model,new ModelTexture(loader.loadTexture("stallTexture")));
        ModelTexture texture = staticModel.getTexture();
        texture.setShineDamper(10);
        texture.setReflectivity(1);
        Entity entity = new Entity(staticModel, new Vector3f(0,0,-50),0,0,0,1);
        Light light = new Light(new Vector3f(0,0,-20),new Vector3f(1,1,1));
        Camera camera = new Camera();

        while (!Display.isCloseRequested()) {
            entity.increaseRotation(0, 1, 0);
            camera.move();
            renderer.prepare();
            shader.start();
            shader.loadLight(light);
            shader.loadViewMatrix(camera);
            renderer.render(entity,shader);
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
