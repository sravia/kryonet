package com.engine;

import com.Config;
import com.engine.entities.Camera;
import com.engine.entities.Entity;
import com.engine.entities.Light;
import com.engine.fonts.FontRenderer;
import com.engine.fonts.TextMaster;
import com.engine.fonts.mesh.FontType;
import com.engine.fonts.mesh.GUIText;
import com.engine.guis.GuiRenderer;
import com.engine.guis.GuiTexture;
import com.engine.handler.MouseHandler;
import com.engine.models.RawModel;
import com.engine.models.TexturedModel;
import com.engine.rendering.Loader;
import com.engine.rendering.MasterRenderer;
import com.engine.rendering.OBJLoader;
import com.engine.terrains.Terrain;
import com.engine.textures.ModelTexture;
import com.engine.textures.TerrainTexture;
import com.engine.textures.TerrainTexturePack;
import com.engine.utils.MousePicker;
import com.engine.water.WaterFrameBuffers;
import com.engine.water.WaterRenderer;
import com.engine.water.WaterShader;
import com.engine.water.WaterTile;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.Config.ANTIALIASING_COUNT;
import static com.Config.HEIGHT;
import static com.Config.WIDTH;

public class Engine {

    private static final int FPS_CAP = 60;
    private static long lastFrameTime;
    private static float delta;

    public static List<GuiTexture> guiTextures = new ArrayList<GuiTexture>();
    public static List<Entity> entities = new ArrayList<Entity>();
    public static List<Light> lights = new ArrayList<Light>();
    public static Loader loader = new Loader();
    public static List<Terrain> terrains = new ArrayList<Terrain>();
    public static List<WaterTile> waters = new ArrayList<WaterTile>();

    public Engine(){
        createDisplay();
    }

    public void run() {
        Camera camera = new Camera();
        MasterRenderer renderer = new MasterRenderer(loader, camera);
        MouseHandler mouseHandler = new MouseHandler(loader);
        GuiRenderer guiRenderer = new GuiRenderer(loader);
        TextMaster.init(loader);

        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy2"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture,
                gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

        Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "heightMap");
        terrains.add(terrain);

        MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrain);

        RawModel bunnyModel = OBJLoader.loadObjModel("person", loader);
        TexturedModel stanfordBunny = new TexturedModel(bunnyModel, new ModelTexture(
                loader.loadTexture("playerTexture")));


        Entity player = new Entity(stanfordBunny, new Vector3f(75, 5, -75), 0, 100, 0, 0.6f);
        entities.add(player);


        Light sun = new Light(new Vector3f(1000000, 1500000, -1000000), new Vector3f(1.3f, 1.3f, 1.3f));
        lights.add(sun);


        WaterFrameBuffers buffers = new WaterFrameBuffers();
        WaterShader waterShader = new WaterShader();
        WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(), buffers);
        WaterTile water = new WaterTile(-200, 0, 2);
        waters.add(water);


        GuiTexture shadowMap = new GuiTexture(loader.loadTexture("my/black"), new Vector2f(0.1f,0.1f),new Vector2f(0.5f,0.1f),0.5f);
        guiTextures.add(shadowMap);

        GL11.glOrtho(0, 800, 0, 600, 1, -1);

        FontType font = new FontType(loader.loadTexture("candara"), new File(Config.RESOURCE_PATH+"candara.fnt"));
        GUIText mousePositionText = new GUIText("Mouse: " + Mouse.getX() + " - " + Mouse.getY(), 1f, font, new Vector2f(0.3f, 0.0f), 1f, true);
        TextMaster.loadText(mousePositionText);

        while (!Display.isCloseRequested()) {
            camera.move();
            picker.update();


            renderer.renderShadowMap(entities, sun);
            renderer.renderScene(entities, terrains, lights, camera, new Vector4f(0, -1, 0, 100000));
            waterRenderer.render(waters, camera, sun);
            guiRenderer.render(guiTextures);
            mousePositionText.setTextString("Mouse: " + Mouse.getX() + " - " + Mouse.getY());
            TextMaster.render();
            mouseHandler.start();

            updateDisplay();
        }

        guiRenderer.cleanUp();
        TextMaster.cleanUp();
        buffers.cleanUp();
        waterShader.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        closeDisplay();
    }

    private static void createDisplay() {
        //ContextAttribs contextAttribs = new ContextAttribs(3, 3).withForwardCompatible(true).withProfileCore(true);

        try {
            setDisplayMode(WIDTH, HEIGHT,false);
            //Display.create(new PixelFormat().withSamples(ANTIALIASING_COUNT), contextAttribs);
            Display.create(new PixelFormat().withSamples(ANTIALIASING_COUNT));
            Display.setTitle("V1");
            GL11.glEnable(GL13.GL_MULTISAMPLE);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        GL11.glViewport(0, 0, WIDTH, HEIGHT);
        lastFrameTime = getCurrentTime();
    }

    public static void updateDisplay() {
        Display.sync(FPS_CAP);
        Display.update();
        long currentFrameTime = getCurrentTime();
        delta = (currentFrameTime - lastFrameTime) / 1000f;
        lastFrameTime = currentFrameTime;
    }

    public static float getFrameTimeSeconds() {
        return delta;
    }

    public static void closeDisplay() {
        Display.destroy();
    }

    private static long getCurrentTime() {
        return Sys.getTime() * 1000 / Sys.getTimerResolution();
    }

    private static void setDisplayMode(int width, int height, boolean fullscreen) {
        if ((Display.getDisplayMode().getWidth() == width) &&
                (Display.getDisplayMode().getHeight() == height) &&
                (Display.isFullscreen() == fullscreen)) {
            return;
        }

        try {
            DisplayMode targetDisplayMode = null;

            if (fullscreen) {
                DisplayMode[] modes = Display.getAvailableDisplayModes();
                int freq = 0;

                for (DisplayMode current : modes) {
                    if ((current.getWidth() == width) && (current.getHeight() == height)) {
                        if ((targetDisplayMode == null) || (current.getFrequency() >= freq)) {
                            if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) {
                                targetDisplayMode = current;
                                freq = targetDisplayMode.getFrequency();
                            }
                        }

                        if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel()) &&
                                (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency())) {
                            targetDisplayMode = current;
                            break;
                        }
                    }
                }
            } else {
                targetDisplayMode = new DisplayMode(width,height);
            }

            if (targetDisplayMode == null) {
                System.out.println("Failed to find value mode: "+width+"x"+height+" fs="+fullscreen);
                return;
            }

            Display.setDisplayMode(targetDisplayMode);
            Display.setFullscreen(fullscreen);
        } catch (LWJGLException e) {
            System.out.println("Unable to setup mode "+width+"x"+height+" fullscreen="+fullscreen + e);
        }
    }


}
