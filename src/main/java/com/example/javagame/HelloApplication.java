package  com.example.javagame;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class HelloApplication extends Application {

    private static final int NUM_OBJECTS = 5;
    private static final double WIDTH = 1000;
    private static final double HEIGHT = 650;

    private int score = 0;
    private int objectsClicked = 0;

    private int objectsFell =0;
    private Text scoreText;
    private List<FallingObject> objects;
    private List<Text> texts;
    private Random random;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        scoreText = new Text("Score: " + score);
        scoreText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        scoreText.setFill(Color.BLACK);
        scoreText.setEffect(new DropShadow(10, Color.GRAY));
        scoreText.setLayoutX(10);
        scoreText.setLayoutY(30);
        root.getChildren().add(scoreText);




        Text valuesText = new Text("Object Values:\n\n"
                + "Yellow: 3\n"
                + "Green: 2\n"
                + "Blue: 1\n"
                + "Red: 4");
        valuesText.setLayoutX(WIDTH - 150);
        valuesText.setLayoutY(20);
        valuesText.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        valuesText.setFill(Color.BLACK);
        valuesText.setEffect(new DropShadow(10, Color.GRAY));
        root.getChildren().add(valuesText);
        objects = new ArrayList<>();
        texts = new ArrayList<>();
        random = new Random();

        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (objectsClicked < NUM_OBJECTS) {
                    spawnObjects(root);
                    updateObjects(root);
                }
                else {

                    stop();
                }
            }
        };


        primaryStage.setTitle("Speed Click Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        gameLoop.start();
    }

    private void spawnObjects(Pane root) {
        if (random.nextDouble() < 0.01 * (1 + objectsClicked ) && objectsFell <NUM_OBJECTS) {
            double radius = 10 + random.nextDouble() * 20;
            double x = radius + random.nextDouble() * (WIDTH - 2 * radius);
            double speed = 1 + 0.5 * objectsClicked;
            double z = 2;
            int value = 1 + random.nextInt(4);

            PhongMaterial material = new PhongMaterial();
            switch (value) {
                case 1 -> {
                    material.setDiffuseColor(Color.BLUE);
                    material.setSpecularColor(Color.BLUE);
                }
                case 2 -> {
                    material.setDiffuseColor(Color.GREEN);
                    material.setSpecularColor(Color.GREEN);
                }
                case 3 -> {
                    material.setDiffuseColor(Color.YELLOW);
                    material.setSpecularColor(Color.YELLOW);
                }
                case 4 -> {
                    material.setDiffuseColor(Color.RED);
                    material.setSpecularColor(Color.RED);
                }
                default -> {
                    material.setDiffuseColor(Color.BLACK);
                    material.setSpecularColor(Color.BLACK);
                }
            }

            FallingObject object = new FallingObject(x, 0, z, radius, value, speed);
            object.setMaterial(material);
            objectsFell++;

            object.setOnMouseClicked(event -> handleClick((FallingObject) event.getTarget(), root));

            Text text = new Text();
            text.setText(getTextByValue(value));
            text.setFill(Color.WHITE);
            text.setFont(Font.font(12));
            text.setTranslateX(x); // set x offset
            text.setTranslateY(object.getTranslateY());// set y offset

            objects.add(object);
            texts.add(text);
            root.getChildren().add(object);
            root.getChildren().add(text);
        }
    }

    private String getTextByValue(int value) {
        switch (value) {
            case 1:
                return "1";
            case 2:
                return "2";
            case 3:
                return "3";
            case 4:
                return "4";
            default:
                return "";
        }
    }




    private void handleClick(FallingObject target,Pane root) {
        Iterator<FallingObject> it = objects.iterator();
        Iterator<Text> text = texts.iterator();

        while (it.hasNext()) {
            FallingObject object = it.next();
            Text number = text.next();
            if (object == target) {
                score += object.getValue();
                scoreText.setText("Score: " + score);
                objectsClicked++;
                root.getChildren().remove(object);
                root.getChildren().remove(number);
                text.remove();
                it.remove();
                break;
            }
        }
    }


    private void updateObjects(Pane root) {
        Iterator<FallingObject> it = objects.iterator();
        Iterator<Text> text = texts.iterator();
        while (it.hasNext()) {
            FallingObject node = it.next();
            Text number = text.next();


                node.setTranslateY(node.getTranslateY() + (1 + 0.5 * objectsClicked));
                number.setTranslateY(node.getTranslateY());
                if (node.getTranslateY() - node.getRadius() > HEIGHT) {
                    // remove the text node from root Pane
                    text.remove();
                    it.remove();
                    root.getChildren().remove(number);
                    root.getChildren().remove(node);
                }


        }
        if(objectsFell==NUM_OBJECTS && objects.isEmpty()){
            gameOverAnimation(root);


        }
    }
    private void gameOverAnimation(Pane root) {
        Text gameOverText = new Text("GAME OVER");
        gameOverText.setFill(Color.RED);
        gameOverText.setFont(Font.font(40));

        // Set the initial position of the text to the center of the screen
        gameOverText.setLayoutX(WIDTH / 2 - gameOverText.getLayoutBounds().getWidth() / 2);
        gameOverText.setLayoutY(HEIGHT / 2 - gameOverText.getLayoutBounds().getHeight() / 2);

        // Add the text to the root pane
        root.getChildren().add(gameOverText);

        // Animate the text to grow and fade out
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(1), gameOverText);
        scaleTransition.setToX(2);
        scaleTransition.setToY(2);

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), gameOverText);
        fadeTransition.setToValue(0);

        ParallelTransition parallelTransition = new ParallelTransition(scaleTransition, fadeTransition);
        parallelTransition.setOnFinished(event -> {
            root.getChildren().remove(gameOverText);

        });

        parallelTransition.play();
    }




}
