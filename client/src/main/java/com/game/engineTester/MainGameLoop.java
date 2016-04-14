package com.game.engineTester;

import com.game.entities.Camera;
import com.game.entities.Entity;
import com.game.entities.Light;
import com.game.entities.Player;
import com.game.fontRendering.TextMaster;
import com.game.guis.GuiRenderer;
import com.game.guis.GuiTexture;
import com.game.handler.MouseHandler;
import com.game.models.RawModel;
import com.game.models.TexturedModel;
import com.game.particles.ParticleMaster;
import com.game.renderEngine.DisplayManager;
import com.game.renderEngine.Loader;
import com.game.renderEngine.MasterRenderer;
import com.game.renderEngine.OBJLoader;
import com.game.terrains.Terrain;
import com.game.textures.ModelTexture;
import com.game.textures.TerrainTexture;
import com.game.textures.TerrainTexturePack;
import com.game.toolbox.MousePicker;
import com.game.water.WaterFrameBuffers;
import com.game.water.WaterRenderer;
import com.game.water.WaterShader;
import com.game.water.WaterTile;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class MainGameLoop {

    public static List<GuiTexture> guiTextures = new ArrayList<GuiTexture>();


    public void run() {
        List<Entity> entities = new ArrayList<Entity>();
        List<Entity> normalMapEntities = new ArrayList<Entity>();

        DisplayManager.createDisplay();
        Loader loader = new Loader();
        TextMaster.init(loader);

        RawModel bunnyModel = OBJLoader.loadObjModel("person", loader);
        TexturedModel stanfordBunny = new TexturedModel(bunnyModel, new ModelTexture(
                loader.loadTexture("playerTexture")));


        Player player = new Player(stanfordBunny, new Vector3f(75, 5, -75), 0, 100, 0, 0.6f);
        entities.add(player);
        Camera camera = new Camera();

        MasterRenderer renderer = new MasterRenderer(loader, camera);

        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy2"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture,
                gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

        Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "heightMap");
        List<Terrain> terrains = new ArrayList<Terrain>();
        terrains.add(terrain);




        List<Light> lights = new ArrayList<Light>();
        Light sun = new Light(new Vector3f(1000000, 1500000, -1000000), new Vector3f(1.3f, 1.3f, 1.3f));
        lights.add(sun);

        //**********Water Renderer Set-up************************

        WaterFrameBuffers buffers = new WaterFrameBuffers();
        WaterShader waterShader = new WaterShader();
        WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(), buffers);
        List<WaterTile> waters = new ArrayList<WaterTile>();
        WaterTile water = new WaterTile(-200, 0, 2);
        waters.add(water);

        MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrain);
        MouseHandler mouseHandler = new MouseHandler(loader);
        GuiRenderer guiRenderer = new GuiRenderer(loader);

        GuiTexture shadowMap = new GuiTexture(loader.loadTexture("my/black"),new Vector2f(0.1f,0.1f),new Vector2f(0.5f,0.1f),0.5f);
        guiTextures.add(shadowMap);

        while (!Display.isCloseRequested()) {
            //player.move(terrain);
            camera.move();
            picker.update();

            mouseHandler.start();



            renderer.renderShadowMap(entities, sun);

            GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
            buffers.unbindCurrentFrameBuffer();
            renderer.renderScene(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, -1, 0, 100000));
            waterRenderer.render(waters, camera, sun);
            guiRenderer.render(guiTextures);

            DisplayManager.updateDisplay();
        }

        guiRenderer.cleanUp();
        buffers.cleanUp();
        waterShader.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }


}
