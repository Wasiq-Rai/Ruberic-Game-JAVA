package  com.example.javagame;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

public class HelloApplication extends Application {

    private static final int NUM_OBJECTS = 30;
    private static final double WIDTH = 1000;
    private static final double HEIGHT = 650;
    private  AnimationTimer gameLoop;
    private int score = 0;
    private int objectsClicked = 0;

    private int objectsFell =0;
    private Text scoreText;
    private List<FallingObject> objects;
    private List<Text> texts;
    private Random random;
    private List<Integer> scores = new ArrayList<>() ;
    public static void main(String[] args) {
        launch(args);
    }
    @Override

    public void start(Stage primaryStage) {
        Pane root = new Pane();
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        scene.setFill(Color.LIGHTBLUE);
        root.setStyle("-fx-background-color: lightblue;");


        setScore(0);
        setObjectsClicked(0);
        setObjectsFell(0);

       setupScoreAndValues(root);
        objects = new ArrayList<>();
        texts = new ArrayList<>();
        random = new Random();

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (objectsClicked < NUM_OBJECTS ) {
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
        Button restartButton = new Button("Restart Game");
        restartButton.setLayoutX(10);
        restartButton.setLayoutY(HEIGHT - 50);
        restartButton.setOnAction(event -> restartGame(root));
        restartButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 8px 12px;");
        root.getChildren().add(restartButton);
        Button pauseButton = new Button("Pause");
        pauseButton.setLayoutX(10);
        pauseButton.setLayoutY(HEIGHT / 2);
        pauseButton.setStyle("-fx-background-color: #FF8C00; -fx-border-color: #000000; -fx-border-radius: 5; -fx-text-fill: #FFFFFF; -fx-font-size: 16px;");
        pauseButton.setOnAction(event -> gameLoop.stop());
        root.getChildren().add(pauseButton);

        Button playButton = new Button("Play");
        playButton.setLayoutX(10);
        playButton.setLayoutY(HEIGHT / 2 + 50);
        playButton.setStyle("-fx-background-color: #008000; -fx-border-color: #000000; -fx-border-radius: 5; -fx-text-fill: #FFFFFF; -fx-font-size: 16px;");
        playButton.setOnAction(event -> gameLoop.start());
        root.getChildren().add(playButton);

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
            text.setText(String.valueOf(value));
            text.setFill(Color.WHITE);
            text.setFont(Font.font("Arial",FontWeight.BOLD,12));
            text.setTranslateX(x); // set x offset
            text.setTranslateY(object.getTranslateY());// set y offset

            objects.add(object);
            texts.add(text);
            root.getChildren().add(object);
            root.getChildren().add(text);
        }
        else if((objectsFell==NUM_OBJECTS) && (objects.isEmpty()) || (objectsClicked==NUM_OBJECTS)){
            gameLoop.stop();
            scores.add(score);
            gameOverAnimation(root);
            showScores(root);
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

    }
    private void setupScoreAndValues(Pane root) {
        setScore(0);
        setObjectsClicked(0);
        setObjectsFell(0);

        scoreText = new Text("Score: " + score);
        scoreText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        scoreText.setFill(Color.BLACK);
        scoreText.setEffect(new DropShadow(10, Color.GRAY));
        scoreText.setLayoutX(10);
        scoreText.setLayoutY(30);
        root.getChildren().add(scoreText);

        Text valuesText = new Text("Object Values\n\n"
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
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(2), gameOverText);
        scaleTransition.setToX(1);
        scaleTransition.setToY(1);

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), gameOverText);
        fadeTransition.setToValue(0);

        ParallelTransition parallelTransition = new ParallelTransition(scaleTransition, fadeTransition);
        parallelTransition.setOnFinished(event -> {
            root.getChildren().remove(gameOverText);
            showScores(root);
        });

        parallelTransition.play();
    }
    private void showScores(Pane root) {

        // Calculate the highest score
        int highestScore = 0;
        for (int score : scores) {
            if (score > highestScore) {
                highestScore = score;
            }
        }
        Collections.sort(scores, Collections.reverseOrder());


        // Display the highest score
        Text highestScoreText = new Text("Highest score: " + highestScore);
        highestScoreText.setFont(Font.font("Calibri", FontWeight.BOLD, 24));
        highestScoreText.setFill(Color.TEAL);
        highestScoreText.setLayoutX(WIDTH / 2 - 150);
        highestScoreText.setLayoutY(20);
        root.getChildren().add(highestScoreText);

        // Display the 5 most recent scores
        Text recentScoresText = new Text("Recent scores");
        recentScoresText.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        recentScoresText.setFill(Color.GREEN);
        recentScoresText.setLayoutX(WIDTH - 150);
        recentScoresText.setLayoutY(HEIGHT/2 -50);
        root.getChildren().add(recentScoresText);

        int numRecentScores = 5;
        int numScoresDisplayed = 0;

        for (int i = 0; numScoresDisplayed < numRecentScores && (i < scores.size()); i++) {
            int score = scores.get(i);
            Text scoreText = new Text("" + score);
            scoreText.setFont(Font.font("Arial", 18));
            scoreText.setFill(Color.BLACK);
            scoreText.setLayoutX(WIDTH -50);
            scoreText.setLayoutY(HEIGHT/2+i*50);
            root.getChildren().add(scoreText);
            numScoresDisplayed++;
        }

    }
    private void restartGame(Pane root) {
        objects.clear();
        texts.clear();
        root.getChildren().removeIf(node -> node instanceof FallingObject || node instanceof Text);
        setScore(0);
        setObjectsClicked(0);
        setObjectsFell(0);

        setupScoreAndValues(root);

        gameLoop.start();
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setObjectsClicked(int objectsClicked) {
        this.objectsClicked = objectsClicked;
    }

    public void setObjectsFell(int objectsFell) {
        this.objectsFell = objectsFell;
    }
}
