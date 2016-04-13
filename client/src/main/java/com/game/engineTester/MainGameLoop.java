package com.game.engineTester;

import com.Config;
import com.game.objConverter.OBJFileLoader;
import com.game.particles.Particle;
import com.game.particles.ParticleMaster;
import com.game.particles.ParticleSystem;
import com.game.particles.ParticleTexture;
import com.game.renderEngine.Loader;
import com.game.textures.ModelTexture;
import com.game.entities.Camera;
import com.game.entities.Entity;
import com.game.entities.Light;
import com.game.entities.Player;
import com.game.fontMeshCreator.FontType;
import com.game.fontMeshCreator.GUIText;
import com.game.fontRendering.TextMaster;
import com.game.guis.GuiRenderer;
import com.game.guis.GuiTexture;
import com.game.models.RawModel;
import com.game.models.TexturedModel;
import com.game.normalMappingObjConverter.NormalMappedObjLoader;
import com.game.renderEngine.DisplayManager;
import com.game.renderEngine.MasterRenderer;
import com.game.renderEngine.OBJLoader;
import com.game.terrains.Terrain;
import com.game.textures.TerrainTexture;
import com.game.textures.TerrainTexturePack;
import com.game.toolbox.MousePicker;
import com.game.water.WaterFrameBuffers;
import com.game.water.WaterRenderer;
import com.game.water.WaterShader;
import com.game.water.WaterTile;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainGameLoop {

    public void run(){
        DisplayManager.createDisplay();
        Loader loader = new Loader();
        TextMaster.init(loader);

        RawModel bunnyModel = OBJLoader.loadObjModel("person", loader);
        TexturedModel stanfordBunny = new TexturedModel(bunnyModel, new ModelTexture(
                loader.loadTexture("playerTexture")));

        Player player = new Player(stanfordBunny, new Vector3f(75, 5, -75), 0, 100, 0, 0.6f);
        Camera camera = new Camera(player);

        MasterRenderer renderer = new MasterRenderer(loader,camera);
        ParticleMaster.init(loader,renderer.getProjectionMatrix());

        FontType font = new FontType(loader.loadTexture("candara"), new File(Config.RESOURCE_PATH+"candara.fnt"));
        GUIText text = new GUIText("This is some text!", 3f, font, new Vector2f(0.0f, 0.4f), 1f, true);
        text.setColour(1f,1f,1f);

        // *********TERRAIN TEXTURE STUFF**********

        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy2"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture,
                gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

        // *****************************************

        TexturedModel rocks = new TexturedModel(OBJFileLoader.loadOBJ("rocks", loader),
                new ModelTexture(loader.loadTexture("rocks")));

        ModelTexture fernTextureAtlas = new ModelTexture(loader.loadTexture("fern"));
        fernTextureAtlas.setNumberOfRows(2);

        TexturedModel fern = new TexturedModel(OBJFileLoader.loadOBJ("fern", loader),
                fernTextureAtlas);

        TexturedModel bobble = new TexturedModel(OBJFileLoader.loadOBJ("pine", loader),
                new ModelTexture(loader.loadTexture("pine")));
        bobble.getTexture().setHasTransparency(true);

        fern.getTexture().setHasTransparency(true);

        Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap);
        List<Terrain> terrains = new ArrayList<Terrain>();
        terrains.add(terrain);

        TexturedModel lamp = new TexturedModel(OBJLoader.loadObjModel("lamp", loader),
                new ModelTexture(loader.loadTexture("lamp")));
        lamp.getTexture().setUseFakeLighting(true);

        List<Entity> entities = new ArrayList<Entity>();
        List<Entity> normalMapEntities = new ArrayList<Entity>();

        //******************NORMAL MAP MODELS************************

        TexturedModel barrelModel = new TexturedModel(NormalMappedObjLoader.loadOBJ("barrel", loader),
                new ModelTexture(loader.loadTexture("barrel")));
        barrelModel.getTexture().setNormalMap(loader.loadTexture("barrelNormal"));
        barrelModel.getTexture().setShineDamper(10);
        barrelModel.getTexture().setReflectivity(0.5f);

        TexturedModel crateModel = new TexturedModel(NormalMappedObjLoader.loadOBJ("crate", loader),
                new ModelTexture(loader.loadTexture("crate")));
        crateModel.getTexture().setNormalMap(loader.loadTexture("crateNormal"));
        crateModel.getTexture().setShineDamper(10);
        crateModel.getTexture().setReflectivity(0.5f);

        TexturedModel boulderModel = new TexturedModel(NormalMappedObjLoader.loadOBJ("boulder", loader),
                new ModelTexture(loader.loadTexture("boulder")));
        boulderModel.getTexture().setNormalMap(loader.loadTexture("boulderNormal"));
        boulderModel.getTexture().setShineDamper(10);
        boulderModel.getTexture().setReflectivity(0.5f);


        //************ENTITIES*******************

        Entity entity = new Entity(barrelModel, new Vector3f(75, 10, -75), 0, 0, 0, 1f);
        Entity entity2 = new Entity(boulderModel, new Vector3f(85, 10, -75), 0, 0, 0, 1f);
        Entity entity3 = new Entity(crateModel, new Vector3f(65, 10, -75), 0, 0, 0, 0.04f);
        normalMapEntities.add(entity);
        normalMapEntities.add(entity2);
        normalMapEntities.add(entity3);

        Random random = new Random(5666778);
        for (int i = 0; i < 500; i++) {
            if (i % 3 == 0) {
                float x = random.nextFloat() * 800;
                float z = random.nextFloat() * -800;
                if ((x > 50 && x < 100) || (z < -50 && z > -100)) {
                } else {
                    float y = terrain.getHeightOfTerrain(x, z);

                    entities.add(new Entity(fern, 3, new Vector3f(x, y, z), 0,
                            random.nextFloat() * 360, 0, 0.9f));
                }
            }
            if (i % 2 == 0) {

                float x = random.nextFloat() * 800;
                float z = random.nextFloat() * -800;
                if ((x > 50 && x < 100) || (z < -50 && z > -100)) {

                } else {
                    float y = terrain.getHeightOfTerrain(x, z);
                    entities.add(new Entity(bobble, 1, new Vector3f(x, y, z), 0,
                            random.nextFloat() * 360, 0, random.nextFloat() * 0.6f + 0.8f));
                }
            }
        }
        entities.add(new Entity(rocks, new Vector3f(75, 4.6f, -75), 0, 0, 0, 75));

        entities.add(player);

        //*******************OTHER SETUP***************

        List<Light> lights = new ArrayList<Light>();
        Light sun = new Light(new Vector3f(1000000, 1500000, -1000000), new Vector3f(1.3f, 1.3f, 1.3f));
        lights.add(sun);



        List<GuiTexture> guiTextures = new ArrayList<GuiTexture>();

        //GuiTexture shadowMap = new GuiTexture(renderer.getShadowMapTexture(),new Vector2f(0.5f,0.5f),new Vector2f(0.5f,0.5f));
        //guiTextures.add(shadowMap);

        GuiRenderer guiRenderer = new GuiRenderer(loader);
        MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrain);

        //**********Water Renderer Set-up************************

        WaterFrameBuffers buffers = new WaterFrameBuffers();
        WaterShader waterShader = new WaterShader();
        WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(), buffers);
        List<WaterTile> waters = new ArrayList<WaterTile>();
        WaterTile water = new WaterTile(475, -475, 0);
        waters.add(water);


        ParticleTexture particleTexture = new ParticleTexture(loader.loadTexture("particleAtlas"),4,true);

        ParticleSystem system = new ParticleSystem(particleTexture,50,25,0.3f,4,1);
        system.randomizeRotation();
        system.setDirection(new Vector3f(0,1,0),0.1f);
        system.setLifeError(0.1f);
        system.setSpeedError(0.4f);
        system.setScaleError(0.8f);

        //****************Game Loop Below*********************

        while (!Display.isCloseRequested()) {
            player.move(terrain);
            camera.move();
            picker.update();
            system.generateParticles(new Vector3f(player.getPosition()));
            ParticleMaster.update(camera);

            renderer.renderShadowMap(entities,sun);

            entity.increaseRotation(0, 1, 0);
            entity2.increaseRotation(0, 1, 0);
            entity3.increaseRotation(0, 1, 0);
            GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

            //render reflection teture
            buffers.bindReflectionFrameBuffer();
            float distance = 2 * (camera.getPosition().y - water.getHeight());
            camera.getPosition().y -= distance;
            camera.invertPitch();
            renderer.renderScene(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, 1, 0, -water.getHeight()+1));
            camera.getPosition().y += distance;
            camera.invertPitch();

            //render refraction texture
            buffers.bindRefractionFrameBuffer();
            renderer.renderScene(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, -1, 0, water.getHeight()));

            //render to screen
            GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
            buffers.unbindCurrentFrameBuffer();
            renderer.renderScene(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, -1, 0, 100000));
            waterRenderer.render(waters, camera, sun);

            ParticleMaster.renderParticles(camera);

            guiRenderer.render(guiTextures);
            TextMaster.render();

            DisplayManager.updateDisplay();
        }

        //*********Clean Up Below**************

        ParticleMaster.cleanUp();
        TextMaster.cleanUp();
        buffers.cleanUp();
        waterShader.cleanUp();
        guiRenderer.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }


}
