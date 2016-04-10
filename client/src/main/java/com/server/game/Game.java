package com.server.game;

import com.server.game.entities.Camera;
import com.server.game.entities.Entity;
import com.server.game.entities.Light;
import com.server.game.model.RawModel;
import com.server.game.model.TexturedModel;
import com.server.game.rendering.MasterRenderer;
import com.server.game.rendering.loaders.OBJLoader;
import com.server.game.terrain.Terrain;
import com.server.game.textures.ModelTexture;
import com.server.game.textures.TerrainTexture;
import com.server.game.textures.TerrainTexturePack;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.lwjgl.opengl.GL11.glPolygonMode;

public class Game {

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;
    private static final int FPS_CAP = 120;

    public Game() {
        createDisplay();

        Loader loader = new Loader();

        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

        Terrain terrain = new Terrain(0, 0, loader, texturePack, blendMap, "heightmap");
        Terrain terrain2 = new Terrain(-1, 0, loader, texturePack, blendMap, "heightmap");

        RawModel model = OBJLoader.loadObjModel("tree", loader);

        TexturedModel staticModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("tree")));
        staticModel.getTexture().setHasTransparency(true);
        staticModel.getTexture().setUseFakeLighting(true);
        List<Entity> entities = new ArrayList<Entity>();
        Random random = new Random();
        for (int i = 0; i < 500; i++) {
            float x = random.nextFloat() * 300;
            float z = random.nextFloat() * 100;
            float y = terrain.getHeightOfTerrain(x,z);
            entities.add(new Entity(staticModel, new Vector3f(x, y, z), 0, random.nextFloat()*360, 0, 0.9f));
        }

        Light light = new Light(new Vector3f(20000, 20000, 2000), new Vector3f(1, 1, 1));


        Camera camera = new Camera();
        MasterRenderer renderer = new MasterRenderer();

        while (!Display.isCloseRequested()) {
            camera.move();

            renderer.processTerrain(terrain);
            renderer.processTerrain(terrain2);
            for (Entity entity : entities) {
                renderer.processEntity(entity);
            }
            renderer.render(light, camera);
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
