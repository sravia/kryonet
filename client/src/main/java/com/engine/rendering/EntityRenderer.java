package com.engine.rendering;

import com.engine.models.RawModel;
import com.engine.models.TexturedModel;
import com.engine.shaders.StaticShader;
import com.engine.textures.ModelTexture;
import com.engine.utils.Math;
import com.engine.entities.Entity;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import java.util.List;
import java.util.Map;


public class EntityRenderer {

    private final ThreadLocal<StaticShader> shader = new ThreadLocal<StaticShader>();

    public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix) {
        this.shader.set(shader);
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.connectTextureUnits();
        shader.stop();
    }

    public void render(Map<TexturedModel, List<Entity>> entities,Matrix4f toShadowSpace) {
        shader.get().loadToShadowSpaceMatrix(toShadowSpace);
        for (TexturedModel model : entities.keySet()) {
            prepareTexturedModel(model);
            List<Entity> batch = entities.get(model);
            for (Entity entity : batch) {
                prepareInstance(entity);
                GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(),
                        GL11.GL_UNSIGNED_INT, 0);//GL_LINE_STRIP
            }
            unbindTexturedModel();
        }
        shader.get().stop();
    }

    private void prepareTexturedModel(TexturedModel model) {
        RawModel rawModel = model.getRawModel();
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        ModelTexture texture = model.getTexture();
        shader.get().loadNumberOfRows(texture.getNumberOfRows());
        if (texture.isHasTransparency()) {
            MasterRenderer.disableCulling();
        }
        shader.get().loadFakeLightingVariable(texture.isUseFakeLighting());
        shader.get().loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());

    }

    private void unbindTexturedModel() {
        MasterRenderer.enableCulling();
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    private void prepareInstance(Entity entity) {
        Matrix4f transformationMatrix = Math.createTransformationMatrix(entity.getPosition(),
                entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
        shader.get().loadTransformationMatrix(transformationMatrix);
        shader.get().loadOffset(entity.getTextureXOffset(), entity.getTextureYOffset());
    }

}
