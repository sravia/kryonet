package com.engine.guis;

import com.engine.models.RawModel;
import org.lwjgl.util.vector.Vector2f;

public class GuiTexture {

    private int texture;
    private Vector2f position;
    private RawModel rawModel;
    private float visibility;

    public GuiTexture(int texture, RawModel rawModel, Vector2f position, float visibility) {
        this.texture = texture;
        this.position = position;
        this.rawModel = rawModel;
        this.visibility = visibility;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public float getVisibility() {
        return visibility;
    }

    public int getTexture() {
        return texture;
    }

    public Vector2f getPosition() {
        return position;
    }

    public RawModel getRawModel() {
        return rawModel;
    }


}
