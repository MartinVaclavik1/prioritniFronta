package com.example.prioritnifronta.UI;

import com.example.prioritnifronta.Obec;
import com.example.prioritnifronta.abstrHeap.AbstrHeap;
import com.example.prioritnifronta.abstrHeap.AbstrHeapException;
import com.example.prioritnifronta.abstrHeap.IAbstrHeap;
import com.example.prioritnifronta.abstrHeap.eTypProhl;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.Iterator;
import java.util.Optional;
import java.util.Random;

public class ProgAbstrHeap extends Application {
    private final ObservableList<String> observableList = FXCollections.observableArrayList();
    private final ListView<String> listView = new ListView<>(observableList);
    private final String nazevSouboru = "zaloha.bin";
    private IAbstrHeap prioritniFronta = new AbstrHeap();
    private final Pane pane = new Pane();
    private final ChoiceBox<eTypProhl> choiceBox = new ChoiceBox<>();
    private final Label typRazeni = new Label("Typ řazení: počet obyvatel");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        choiceBox.getItems().addAll(eTypProhl.DO_HLOUBKY, eTypProhl.DO_SIRKY);
        choiceBox.setPrefWidth(150);
        choiceBox.getSelectionModel().selectFirst();

        choiceBox.setOnAction(actionEvent -> {
            if (choiceBox.getSelectionModel().getSelectedItem() != null) {
                aktualizujListView();
            }
        });

        pane.setPrefSize(1500, 700);
        listView.setFocusTraversable(false);
        BorderPane root = new BorderPane();
        VBox vBox = new VBox();
        root.setCenter(listView);
        root.setRight(vBox);
        vBox.setPadding(new Insets(10, 10, 0, 10));
        vBox.setSpacing(5);

        vBox.getChildren().add(newButton("importuj data", importujData()));
        vBox.getChildren().add(newButton("vlož obec", vlozObec()));
        vBox.getChildren().add(newButton("ulož", uloz()));
        vBox.getChildren().add(newButton("načti", nacti()));
        vBox.getChildren().add(newButton("vygeneruj", vygeneruj()));
        vBox.getChildren().add(newButton("zpřístupní max", zpristupniMax()));
        vBox.getChildren().add(newButton("odeber max", odeberMax()));
        vBox.getChildren().add(newButton("reorganizace", reorganizace()));
        vBox.getChildren().add(newButton("zruš", zrus()));
//        vBox.getChildren().add(newButton("aktualizuj", aktualizuj()));
        vBox.getChildren().add(choiceBox);
        vBox.getChildren().add(typRazeni);

        Scene scene = new Scene(root);
        stage.setTitle("Václavík - AbstrTable");
        stage.setScene(scene);
        stage.setHeight(500);
        stage.setWidth(800);
        stage.show();
    }

//    private EventHandler<ActionEvent> aktualizuj() {
//        return EventHandler -> aktualizujListView();
//    }

    private EventHandler<ActionEvent> zrus() {
        return EventHandler -> {
            prioritniFronta.zrus();
            aktualizujListView();
        };
    }

    private EventHandler<ActionEvent> reorganizace() {
        return EventHandler -> {
            try {
                prioritniFronta.reorganizace();

                if (typRazeni.getText().equals("Typ řazení: počet obyvatel")) {
                    typRazeni.setText("Typ řazení: název obce");
                } else {
                    typRazeni.setText("Typ řazení: počet obyvatel");
                }

            } catch (AbstrHeapException x) {
                chybovaHlaska(x.getMessage());
            }

            aktualizujListView();
        };
    }

    private EventHandler<ActionEvent> odeberMax() {
        return EventHandler -> {
            try {


                Obec obec = prioritniFronta.odeberMax();
                aktualizujListView();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(obec.toString());
                alert.showAndWait();
            } catch (AbstrHeapException x) {
                chybovaHlaska(x.getMessage());
            }
        };
    }

    private EventHandler<ActionEvent> zpristupniMax() {
        return EventHandler -> {
            try {
                Obec obec = prioritniFronta.zpristupniMax();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(obec.toString());
                alert.showAndWait();
            } catch (AbstrHeapException x) {
                chybovaHlaska(x.getMessage());
            }
        };
    }

    private EventHandler<ActionEvent> vygeneruj() {
        return EventHandler -> {
            try {
                Random nahoda = new Random();
                int pocet = nahoda.nextInt(29) + 1;

                for (int i = 0; i < pocet; i++) {
                    int psc = nahoda.nextInt(89999) + 10000;
                    StringBuilder nazev = new StringBuilder();
                    for (int j = 0; j < nahoda.nextInt(5) + 5; j++) {
                        nazev.append((char) ('a' + nahoda.nextInt(25)));
                    }
                    int pocetM = nahoda.nextInt(5000) + 1;
                    int pocetZ = nahoda.nextInt(5000) + 1;
                    prioritniFronta.vloz(new Obec(psc, nazev.toString(), pocetM, pocetZ));
                }
                aktualizujListView();
            } catch (AbstrHeapException e) {
                chybovaHlaska(e.getMessage());
            }
        };
    }

    private EventHandler<ActionEvent> nacti() {
        return EventHandler -> {
            try {
                int pocet = 0;
                ObjectInputStream vstup =
                        new ObjectInputStream(
                                new FileInputStream(nazevSouboru));

                //TODO mazat zbytek pole, nebo nechat a přičíst? - pro nechání smazat následující řádek
                prioritniFronta = new AbstrHeap();

                int konec = vstup.readInt();

                while (konec != -1) {
                    Obec obec = (Obec) vstup.readObject();
                    //System.out.println(obec);
                    if (obec != null) {
                        prioritniFronta.vloz(obec);
                        pocet++;
                    }

                    konec = vstup.readInt();

                }
                vstup.close();
                aktualizujListView();
                System.out.println("Úspěšně načteno " + pocet + " obcí");

            } catch (Exception x) {
                chybovaHlaska("Chyba při načítání dat");
            }
        };
    }

    private EventHandler<ActionEvent> uloz() {
        return EventHandler -> {
            try {

                ObjectOutputStream vystup =
                        new ObjectOutputStream(
                                new FileOutputStream(nazevSouboru));

                Iterator<Obec> it = prioritniFronta.vytvorIterator(eTypProhl.DO_SIRKY);

                while (it.hasNext()) {
                    vystup.writeInt(1);
                    vystup.writeObject(it.next());
                }

                vystup.writeInt(-1);

                vystup.close();
                System.out.println("Úspěšně uloženo");
            } catch (Exception x) {
                System.out.println(x.getMessage());
                chybovaHlaska("Chyba při ukládání souboru");
            }
        };
    }

    private EventHandler<ActionEvent> vlozObec() {
        return EventHandler -> {
            try {
                Obec obec = dialogObec();
                if (obec == null) {
                    System.out.println("obec nebyla zadána");
                    return;
                }
                prioritniFronta.vloz(obec);

                aktualizujListView();
            } catch (AbstrHeapException x) {
                chybovaHlaska(x.getMessage());
            }
        };
    }

    private Obec dialogObec() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 10, 10, 10));
        gridPane.add(new Label("PSČ:"), 0, 2);
        gridPane.add(new Label("Název obce:"), 0, 3);
        gridPane.add(new Label("Počet mužů:"), 0, 4);
        gridPane.add(new Label("Počet žen:"), 0, 5);

        TextField pscTXT = new TextField();
        TextField nazevTXT = new TextField();
        TextField pocetMTXT = new TextField();
        TextField pocetZTXT = new TextField();

        gridPane.add(pscTXT, 1, 2);
        gridPane.add(nazevTXT, 1, 3);
        gridPane.add(pocetMTXT, 1, 4);
        gridPane.add(pocetZTXT, 1, 5);

        Dialog<Obec> dialog = new Dialog<>();


        dialog.getDialogPane().setContent(gridPane);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    int psc = Integer.parseInt(pscTXT.getText());
                    int pocetM = Integer.parseInt(pocetMTXT.getText());
                    int pocetZ = Integer.parseInt(pocetZTXT.getText());
                    return new Obec(psc, nazevTXT.getText(), pocetM, pocetZ);
                } catch (Exception x) {
                    chybovaHlaska("Chyba v zadávání hodnot. Zadávejte celá čísla");
                }
            }
            return null;
        });

        Optional<Obec> obec = dialog.showAndWait();
        return obec.orElse(null);
    }

    private void aktualizujListView() {

        observableList.clear();

        Iterator<Obec> iterator = prioritniFronta.vytvorIterator(choiceBox.getValue());
        while (iterator.hasNext()) {
            observableList.addAll(iterator.next().toString());
        }
    }

    private EventHandler<ActionEvent> importujData() {
        return EventHandler -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Text Files", "*.csv"));
            File soubor = fileChooser.showOpenDialog(new Stage());
            if (soubor != null) {
                nactiData(String.valueOf(soubor));
            }
            aktualizujListView();
        };
    }

    private void nactiData(String soubor) {
        try (BufferedReader nactenySoubor = new BufferedReader(new FileReader((soubor)))) {
            String radek = nactenySoubor.readLine();
            while (radek != null) {
                //0 = id kraje, 1 = ENUM kraj, 2 = PSČ, 3 = obec, 4 = pocet muzu, 5 = pocet zen, 6 = pocet celkem
                String[] rozdelenyRadek = radek.split(";");

                //int idKraje = Integer.parseInt(rozdelenyRadek[0]);
                int psc = Integer.parseInt(rozdelenyRadek[2]);
                String nazevObce = rozdelenyRadek[3];
                int pocetMuzu = Integer.parseInt(rozdelenyRadek[4]);
                int pocetZen = Integer.parseInt(rozdelenyRadek[5]);


                prioritniFronta.vloz(new Obec(psc, nazevObce, pocetMuzu, pocetZen));
                radek = nactenySoubor.readLine();
            }

        } catch (IOException x) {
            chybovaHlaska("Chyba v načítání souboru");
        } catch (AbstrHeapException x) {
            chybovaHlaska(x.getMessage());
        }
    }

    private Button newButton(String nazev, EventHandler<ActionEvent> handler) {
        Button button = new Button(nazev);
        button.setOnAction(handler);
        button.setPrefWidth(150);
        return button;
    }

    private void chybovaHlaska(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(message);
        alert.showAndWait();
    }
}
