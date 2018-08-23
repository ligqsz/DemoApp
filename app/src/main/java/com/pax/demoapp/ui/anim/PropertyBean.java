package com.pax.demoapp.ui.anim;

/**
 * @author ligq
 * @date 2018/8/23
 */

public class PropertyBean {
    private int backgroundColor;
    private float size;
    private float rotationX;

    public PropertyBean(int backgroundColor, float size, float rotationX) {
        this.backgroundColor = backgroundColor;
        this.size = size;
        this.rotationX = rotationX;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public float getRotationX() {
        return rotationX;
    }

    public void setRotationX(float rotationX) {
        this.rotationX = rotationX;
    }

    @Override
    public String toString() {
        return "PropertyBean{" +
                "backgroundColor=" + backgroundColor +
                ", size=" + size +
                ", rotationX=" + rotationX +
                '}';
    }
}
