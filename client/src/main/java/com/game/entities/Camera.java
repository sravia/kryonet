package com.game.entities;

import com.game.renderEngine.DisplayManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

    private float speed = 5f;
    private Vector3f position = new Vector3f(0,50,0);
    private float pitch = 60;
    private float yaw = 0;
    private float roll = 0;
    private int borderOffset = 50;

    public Camera(){}

    public void move(){
        handleKeyboard();
        handleMouse();

        int zoom = Mouse.getDWheel();
        if (zoom != 0){
            setZoom(zoom);
        }
    }

    private void handleMouse(){
        if(Mouse.isInsideWindow()){
            if(Mouse.getX() < borderOffset){
                position.x -= speed;
            }
            if(Mouse.getY() > DisplayManager.HEIGHT - borderOffset){
                position.z-=speed;
            }
            if(Mouse.getX() > DisplayManager.WIDTH - borderOffset){
                position.x+=speed;
            }
            if(Mouse.getY() < borderOffset){
                position.z+=speed;
            }
        }
    }

    private void handleKeyboard(){
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
