package com.example.javagame;


import javafx.scene.shape.Sphere;

public class FallingObject extends Sphere {
    private int value;
    private double speed;

    public FallingObject(double x, double y, double z, double radius, int value, double speed) {
        super(radius);
        setTranslateX(x);
        setTranslateY(y);
        setTranslateZ(z);
        this.value = value;
        this.speed = speed;

    }

    public int getValue() {
        return value;
    }

    public double getSpeed() {
        return speed;
    }

}
