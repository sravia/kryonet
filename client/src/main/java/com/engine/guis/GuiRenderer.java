package com.engine.guis;

import com.engine.models.RawModel;
import com.engine.rendering.Loader;
import com.engine.utils.Math;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import java.util.List;

public class GuiRenderer {

    private GuiShader shader;

    public GuiRenderer(Loader loader) {
        shader = new GuiShader();
    }

    public void render(List<GuiTexture> guis) {
        shader.start();
        for (GuiTexture gui : guis) {
            GL30.glBindVertexArray(gui.getRawModel().getVaoID());
            GL20.glEnableVertexAttribArray(0);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTexture());
            Matrix4f matrix = Math.createTransformationMatrix(gui.getPosition());
            shader.loadTransformation(matrix);
            shader.loadVisibility(gui.getVisibility());
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, gui.getRawModel().getVertexCount());
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_BLEND);
            GL20.glDisableVertexAttribArray(0);
            GL30.glBindVertexArray(0);
        }
        shader.stop();
    }

    public void cleanUp() {
        shader.cleanUp();
    }
}
