package com.engine.entities;

import com.Config;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

    private static final int BORDER_OFFSET = 50;
    private static final float SPEED = 5f;
    private Vector3f position = new Vector3f(0, 50, 0);
    private float pitch = 60;
    private float yaw = 0;
    private float roll = 0;

    public Camera() {
    }

    public void move() {
        handleKeyboard();
        handleMouse();

        int zoom = Mouse.getDWheel();
        if (zoom != 0) {
            setZoom(zoom);
        }
    }

    private void handleMouse() {
        if (Mouse.isInsideWindow()) {
            if (Mouse.getX() < BORDER_OFFSET) {
                position.x -= SPEED;
            }
            if (Mouse.getY() > Config.HEIGHT - BORDER_OFFSET) {
                position.z -= SPEED;
            }
            if (Mouse.getX() > Config.WIDTH - BORDER_OFFSET) {
                position.x += SPEED;
            }
            if (Mouse.getY() < BORDER_OFFSET) {
                position.z += SPEED;
            }
        }
    }

    private void handleKeyboard() {
        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            position.z -= SPEED;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            position.z += SPEED;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            position.x += SPEED;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            position.x -= SPEED;
        }
    }

    public void setZoom(int zoom) {
        float formattedZoom = zoom * 0.01f;
        if (zoom > 0 && pitch < 90) {
            pitch += formattedZoom;
            position.setY(position.getY() + formattedZoom);
        } else if (zoom < 0 && pitch > 0) {
            pitch += formattedZoom;
            position.setY(position.getY() + formattedZoom);
        }
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }

}
