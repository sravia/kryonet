package com.server.game;

import com.server.game.entities.Camera;
import com.server.game.entities.Entity;
import com.server.game.entities.Light;
import com.server.game.gui.GuiRenderer;
import com.server.game.gui.GuiTexture;
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
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "heightmap");


        RawModel model = OBJLoader.loadObjModel("tree", loader);

        TexturedModel tree = new TexturedModel(model, new ModelTexture(loader.loadTexture("tree")));
        TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader), new ModelTexture(loader.loadTexture("grassTexture")));

        ModelTexture fernTexture = new ModelTexture(loader.loadTexture("fern"));
        fernTexture.setNumberOfRows(2);
        TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern", loader), fernTexture);

        TexturedModel bobble = new TexturedModel(OBJLoader.loadObjModel("lowPolyTree", loader), new ModelTexture(loader.loadTexture("lowPolyTree")));

        grass.getTexture().setHasTransparency(true);
        grass.getTexture().setUseFakeLighting(true);
        fern.getTexture().setHasTransparency(true);

        List<Entity> entities = new ArrayList<Entity>();
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            if (i % 7 == 0) {
                float x = random.nextFloat() * 400;
                float z = random.nextFloat() * 400-400;
                float y = terrain.getHeightOfTerrain(x, z);
                entities.add(new Entity(fern, random.nextInt(4), new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, 0.9f));

                x = random.nextFloat() * 400;
                z = random.nextFloat() * 400-400;
                y = terrain.getHeightOfTerrain(x, z);
                entities.add(new Entity(grass, new Vector3f(x, y, z), 0, 0, 0, 1.8f));
            }

            if (i % 3 == 0) {
                float x = random.nextFloat() * 400;
                float z = random.nextFloat() * 400-400;
                float y = terrain.getHeightOfTerrain(x, z);
                entities.add(new Entity(bobble, new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, random.nextFloat() * 0.1f + 0.6f));

                x = random.nextFloat() * 400;
                z = random.nextFloat() * 400 - 400;
                y = terrain.getHeightOfTerrain(x, z);
                entities.add(new Entity(tree, new Vector3f(x, y, z), 0, 0, 0, random.nextFloat() * 1 + 4));
            }

        }


        Light light = new Light(new Vector3f(20000, 20000, 2000), new Vector3f(1, 1, 1));

        Camera camera = new Camera();
        MasterRenderer renderer = new MasterRenderer();


        List<GuiTexture> guis = new ArrayList<GuiTexture>();
        GuiTexture health = new GuiTexture(loader.loadTexture("health"), new Vector2f(-0.74f, 0.925f), new Vector2f(0.25f, 0.25f));
        guis.add(health);
        GuiRenderer guiRenderer = new GuiRenderer(loader);

        while (!Display.isCloseRequested()) {
            camera.move();

            renderer.processTerrain(terrain);
            for (Entity entity : entities) {
                renderer.processEntity(entity);
            }
            renderer.render(light, camera);
            guiRenderer.render(guis);
            updateDisplay();
        }
        guiRenderer.cleanUp();
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
