package com.game.guis;

import org.lwjgl.util.vector.Vector2f;

public class GuiTexture {

    private int texture;
    private Vector2f position;
    private Vector2f scale;
    private float visibility;

    public GuiTexture(int texture, Vector2f position, Vector2f scale, float visibility) {
        this.texture = texture;
        this.position = position;
        this.scale = scale;
        this.visibility = visibility;
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

    public Vector2f getScale() {
        return scale;
    }


}
