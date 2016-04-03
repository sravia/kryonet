package com.server.game.entities;

import com.server.game.toolbox.Maths;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;
public class Camera {

    private float speed = 1f;
    private Vector3f position = new Vector3f(0,5,0); //50
    private float pitch = 10; // 40
    private float yaw = 0;
    private float roll = 0;

    public Camera(){}

    public void move(){
        if(Keyboard.isKeyDown(Keyboard.KEY_UP)){
            position.z-=speed;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
            position.z+=speed;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
            position.x+=speed;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
            position.x-=speed;
        }

        int zoom = Mouse.getDWheel();
        if (zoom != 0){
            setZoom(zoom);
        }
    }

    public void setZoom(int zoom){
        float formattedZoom = zoom*0.01f;
        if(zoom > 0 && pitch < 90){
            pitch += formattedZoom;
            position.setY(position.getY()+formattedZoom);
        }else if(zoom < 0 && pitch > 0){
            pitch += formattedZoom;
            position.setY(position.getY()+formattedZoom);
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