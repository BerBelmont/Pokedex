package br.unijui.pokedexjavafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Objects;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // carrega a fonte customizada
        Font.loadFont(
                Objects.requireNonNull(
                        getClass().getResourceAsStream("/fonts/PokemonGb-RAeo.ttf")
                ),
                14
        );

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/view/main-view.fxml")
        );
        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("Pokédex JavaFX");
        stage.setScene(scene);
        stage.setWidth(1000);
        stage.setHeight(650);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
