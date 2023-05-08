package com.example.javagame;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class HelloApplication extends Application {

    private static final int NUM_OBJECTS = 30;
    private static final double WIDTH = 1000;
    private static final double HEIGHT = 650;

    private int score = 0;
    private int objectsClicked = 0;
    private Text scoreText;
    private List<FallingObject> objects;
    private Random random;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        scoreText = new Text("Score: 0");
        scoreText.setLayoutX(10);
        scoreText.setLayoutY(20);
        root.getChildren().add(scoreText);

        Text valuesText = new Text("Object Values:\n\n"
                + "Yellow: 3\n"
                + "Green: 2\n"
                + "Blue: 1\n"
                + "Red: 4");
        valuesText.setLayoutX(WIDTH - 150);
        valuesText.setLayoutY(20);
        root.getChildren().add(valuesText);

        objects = new ArrayList<>();
        random = new Random();

        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (objectsClicked < NUM_OBJECTS) {
                    spawnObjects(root);
                    updateObjects();
                }
            }
        };

        scene.setOnMouseClicked(event -> {
            for (FallingObject object : objects) {
                if (object.contains(event.getX(), event.getY())) {
                    score += object.getValue();
                    scoreText.setText("Score: " + score);
                    objectsClicked++;
                    root.getChildren().remove(object);
                    break;
                }
            }
        });

        primaryStage.setTitle("Speed Click Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        gameLoop.start();
    }


    private void spawnObjects(Pane root) {
        if (random.nextDouble() < 0.01 * (1 + objectsClicked)) {
            double radius = 10 + random.nextDouble() * 20;
            double x = radius + random.nextDouble() * (WIDTH - 2 * radius);
            double speed = 1 + 0.5 * objectsClicked;
            int value = 1 + random.nextInt(4);

            Color color = switch (value) {
                case 1 -> Color.BLUE;
                case 2 -> Color.GREEN;
                case 3 -> Color.YELLOW;
                case 4 -> Color.RED;
                default -> Color.BLACK;
            };

            FallingObject object = new FallingObject(x, 0, radius, color, value, speed);
            objects.add(object);

            // Create text to display object value on the object
            Text valueText = new Text(String.valueOf(value));
            valueText.setFill(Color.WHITE);
            valueText.setFont(new Font(18));
            valueText.setX(x - radius / 2);
            valueText.setY(radius / 2);

            Circle objectShape = object;
            objectShape.setMaterial(object.getMaterial());
            objectShape.getChildren().add(valueText);
            root.getChildren().add(objectShape);
        }
    }

    private void updateObjects() {
        Iterator<FallingObject> iterator = objects.iterator();
        while (iterator.hasNext()) {
            FallingObject object = iterator.next();
            object.setCenterY(object.getCenterY() + object.getSpeed());

            if (object.getCenterY() - object.getRadius() > HEIGHT) {
                iterator.remove();
                root.getChildren().remove(object);
            }
            else {
                Circle objectShape = object.getShape();
                PhongMaterial material = new PhongMaterial(objectShape.getFill());
                objectShape.setMaterial(material);
                objectShape.setTranslateZ(-1 * object.getRadius());
            }
        }
    }



    private void updateObjects() {
        Iterator<FallingObject> iterator = objects.iterator();
        while (iterator.hasNext()) {
            FallingObject object = iterator.next();
            object.setCenterY(object.getCenterY() + object.getSpeed());

            if (object.getCenterY() - object.getRadius() > HEIGHT) {
                iterator.remove();
            }
        }

        if (objectsClicked == NUM_OBJECTS) {
            endGame();
        }
    }
    private void endGame() {
        gameLoop.stop();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText("Final Score: " + score);
        alert.showAndWait();
    }}

