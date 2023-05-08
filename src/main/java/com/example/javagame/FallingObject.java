package com.example.javagame;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

public class FallingObject extends Sphere {
    private int value;
    private double speed;
    private PhongMaterial material;

    public FallingObject(double x, double y, double radius, Color color, int value, double speed) {
        super(radius);
        setTranslateX(x);
        setTranslateY(y);
        this.value = value;
        this.speed = speed;
        this.material = new PhongMaterial(color);
        setMaterial(material);
    }

    public int getValue() {
        return value;
    }

    public double getSpeed() {
        return speed;
    }
}
