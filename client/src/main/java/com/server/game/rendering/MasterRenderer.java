package com.server.game.rendering;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.server.game.entities.Camera;
import com.server.game.entities.Entity;
import com.server.game.entities.Light;
import com.server.game.model.TexturedModel;
import com.server.game.shaders.StaticShader;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;


public class MasterRenderer {

    private StaticShader shader = new StaticShader();
    private Renderer renderer = new Renderer(shader);

    private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();

    public void render(Light sun, Camera camera){
        renderer.prepare();
        shader.start();
        shader.loadLight(sun);
        shader.loadViewMatrix(camera);
        renderer.render(entities);
        shader.stop();
        entities.clear();
    }

    public void processEntity(Entity entity){
        TexturedModel entityModel = entity.getModel();
        List<Entity> batch = entities.get(entityModel);
        if(batch != null){
            batch.add(entity);
        }else{
            List<Entity> newBatch = new ArrayList<Entity>();
            newBatch.add(entity);
            entities.put(entityModel,newBatch);
        }
    }

    public void cleanUp(){
        shader.cleanUp();
    }


}
