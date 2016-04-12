package com.server.game.entities;


import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

    private float speed = 1f;
    private Vector3f position = new Vector3f(0, 5, 0); //50
    private float pitch = 10; // 40
    private float yaw = 0;
    private float roll = 0;

    public Camera() {
    }

    public void move() {
        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            position.z -= speed;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            position.z += speed;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            position.x += speed;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            position.x -= speed;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            position.y += speed;
        }

        //int zoom = Mouse.getDWheel();
        //if (zoom != 0) {
        //    setZoom(zoom);
        //}



        calculatePitch();
        calculateAngleAroundPlayer();
    }

    private void calculatePitch() {
        if (Mouse.isButtonDown(0)) {
            float pitchChange = Mouse.getDY() * 0.1f;
            pitch -= pitchChange;
        }
    }

    private void calculateAngleAroundPlayer() {
        if (Mouse.isButtonDown(1)) {
            float angleChange = Mouse.getDX() * 0.3f;
            yaw -= angleChange;
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