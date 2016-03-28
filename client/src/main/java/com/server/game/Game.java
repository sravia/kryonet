package com.server.game;

import com.server.game.entities.Camera;
import com.server.game.entities.Entity;
import com.server.game.entities.Light;
import com.server.game.model.RawModel;
import com.server.game.model.TexturedModel;
import com.server.game.rendering.MasterRenderer;
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

        RawModel model = OBJLoader.loadObjModel("stall",loader);
        TexturedModel staticModel = new TexturedModel(model,new ModelTexture(loader.loadTexture("stallTexture")));
        ModelTexture texture = staticModel.getTexture();
        texture.setShineDamper(10);
        texture.setReflectivity(1);

        Entity entity = new Entity(staticModel, new Vector3f(0,0,-50),0,0,0,1);
        Light light = new Light(new Vector3f(0,0,-20),new Vector3f(1,1,1));
        Camera camera = new Camera();

        MasterRenderer renderer = new MasterRenderer();

        while (!Display.isCloseRequested()) {
            camera.move();

            renderer.processEntity(entity);

            renderer.render(light,camera);
            updateDisplay();
        }

        renderer.cleanUp();
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
