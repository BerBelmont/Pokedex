package br.unijui.pokedexjavafx;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class MainController {

    @FXML
    private TextField txtNome;

    @FXML
    private ImageView imgPokemon;

    @FXML
    private Label lblNome;

    @FXML
    private Label lblId;

    @FXML
    private Label lblTipos;

    @FXML
    private Label lblStatus;

    @FXML
    private Label lblAltura;

    @FXML
    private Label lblPeso;

    @FXML
    private Label lblBaseExp;

    @FXML
    private ProgressBar barHp;

    @FXML
    private ProgressBar barAtk;

    @FXML
    private ProgressBar barDef;

    @FXML
    private ProgressBar barSpd;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @FXML
    private void onBuscar() {
        String entrada = txtNome.getText().trim().toLowerCase();

        if (entrada.isEmpty()) {
            lblStatus.setText("Digite um nome ou ID.");
            return;
        }

        // Reseta UI
        lblStatus.setText("Buscando...");
        lblNome.setText("Nome: -");
        lblId.setText("ID: -");
        lblTipos.setText("Tipos: -");
        lblAltura.setText("Altura: -");
        lblPeso.setText("Peso: -");
        lblBaseExp.setText("Base EXP: -");
        imgPokemon.setImage(null);
        resetStatsBars();

        String url = "https://pokeapi.co/api/v2/pokemon/" + entrada;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        // Thread separada para não travar a UI
        new Thread(() -> {
            try {
                HttpResponse<String> response = httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

                if (response.statusCode() != 200) {
                    Platform.runLater(() ->
                            lblStatus.setText("Pokémon não encontrado.")
                    );
                    return;
                }

                JSONObject json = new JSONObject(response.body());

                String name = json.getString("name");
                int id = json.getInt("id");

                // Tipos
                JSONArray tiposArray = json.getJSONArray("types");
                StringBuilder tipos = new StringBuilder();
                String primaryType = "";
                for (int i = 0; i < tiposArray.length(); i++) {
                    JSONObject typeObj = tiposArray.getJSONObject(i)
                            .getJSONObject("type");
                    String tName = typeObj.getString("name");
                    if (i == 0) {
                        primaryType = tName;
                    }
                    if (i > 0) tipos.append(", ");
                    tipos.append(tName);
                }

                // Altura (decímetros -> metros) e peso (hectogramas -> kg)
                int alturaDecimetros = json.getInt("height");
                int pesoHectogramas = json.getInt("weight");
                double alturaMetros = alturaDecimetros / 10.0;
                double pesoKg = pesoHectogramas / 10.0;

                int baseExp = json.optInt("base_experience", 0);

                // Stats básicos
                int hp = 0, atk = 0, def = 0, spd = 0;
                JSONArray statsArray = json.getJSONArray("stats");
                for (int i = 0; i < statsArray.length(); i++) {
                    JSONObject statObj = statsArray.getJSONObject(i);
                    String statName = statObj.getJSONObject("stat")
                            .getString("name");
                    int baseStat = statObj.getInt("base_stat");

                    switch (statName) {
                        case "hp" -> hp = baseStat;
                        case "attack" -> atk = baseStat;
                        case "defense" -> def = baseStat;
                        case "speed" -> spd = baseStat;
                        default -> {
                        }
                    }
                }

                String imgUrl = json.getJSONObject("sprites")
                        .optString("front_default", null);

                String nomeFormatado = capitalize(name);
                double maxStat = 255.0; // valor máximo aproximado

                // Cor baseada no tipo principal
                String typeColor = getTypeColor(primaryType);
                String barStyle = String.format(
                        "-fx-accent: %s; -fx-control-inner-background: #003000;",
                        typeColor
                );

                double progHp = hp / maxStat;
                double progAtk = atk / maxStat;
                double progDef = def / maxStat;
                double progSpd = spd / maxStat;

                // Atualiza a interface na thread de UI
                Platform.runLater(() -> {
                    lblNome.setText("Nome: " + nomeFormatado);
                    lblId.setText("ID: " + id);
                    lblTipos.setText("Tipos: " + tipos);
                    lblStatus.setText("");

                    lblAltura.setText(String.format("Altura: %.1f m", alturaMetros));
                    lblPeso.setText(String.format("Peso: %.1f kg", pesoKg));
                    lblBaseExp.setText("Base EXP: " + baseExp);

                    // aplica estilo de cor nas barras
                    barHp.setStyle(barStyle);
                    barAtk.setStyle(barStyle);
                    barDef.setStyle(barStyle);
                    barSpd.setStyle(barStyle);

                    // anima barras enchendo
                    animateBar(barHp, progHp);
                    animateBar(barAtk, progAtk);
                    animateBar(barDef, progDef);
                    animateBar(barSpd, progSpd);

                    if (imgUrl != null && !imgUrl.equals("null")) {
                        imgPokemon.setImage(new Image(imgUrl, true));
                        animateImage();
                    } else {
                        imgPokemon.setImage(null);
                    }
                });

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                Platform.runLater(() ->
                        lblStatus.setText("Erro ao conectar à API.")
                );
            }
        }).start();
    }

    /** Reseta barras para 0 */
    private void resetStatsBars() {
        barHp.setProgress(0);
        barAtk.setProgress(0);
        barDef.setProgress(0);
        barSpd.setProgress(0);
    }

    /** Capitaliza primeira letra do nome */
    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    /** Mapeia tipo principal para uma cor bonitinha */
    private String getTypeColor(String type) {
        if (type == null) return "#ff0000";
        return switch (type) {
            case "fire" -> "#ff6b6b";
            case "water" -> "#4dabff";
            case "grass" -> "#63d471";
            case "electric" -> "#ffd93b";
            case "ice" -> "#9ad0f5";
            case "psychic" -> "#ff6bd5";
            case "dragon" -> "#8f7cf0";
            case "dark" -> "#705746";
            case "fairy" -> "#f4b6ff";
            case "poison" -> "#b35cf4";
            case "flying" -> "#a4b0f5";
            case "ground" -> "#d2b48c";
            case "rock" -> "#c7b78b";
            case "bug" -> "#a8b820";
            case "ghost" -> "#7b62a3";
            case "steel" -> "#b8b8d0";
            default -> "#ff0000";
        };
    }

    /** Anima uma barra enchendo de 0 até o valor alvo */
    private void animateBar(ProgressBar bar, double target) {
        target = Math.max(0, Math.min(1, target)); // clamp
        bar.setProgress(0);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(bar.progressProperty(), 0)),
                new KeyFrame(Duration.millis(600),
                        new KeyValue(bar.progressProperty(), target))
        );
        timeline.play();
    }

    /** Faz a imagem "aparecer" suave */
    private void animateImage() {
        imgPokemon.setOpacity(0);
        FadeTransition ft = new FadeTransition(Duration.millis(400), imgPokemon);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }
}





