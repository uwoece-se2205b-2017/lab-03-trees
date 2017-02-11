package ca.uwo.eng.se2205b.lab03;


import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Simple AutoComplete example to utilize a Trie.
 */
public class AutoComplete extends Application {
    
    @FXML
    private ListView<String> options;

    @FXML
    private Button loadButton;

    @FXML
    private TextField input;

    @FXML
    private Spinner<Integer> resultCounter;

    @FXML
    private Label countLabel;

    /**
     * Trie of words
     */
    private Trie prefixTrie;

    /**
     * Load the {@link #prefixTrie} field with the values from {@code file}, the file has a single word per line.
     * @param file Non-null file to read from
     */
    private void loadTrie(@Nonnull File file) {
        // TODO SE2205B
    }

    /**
     * Read from the input box, and the spinner and calculate the auto complete and update the "countLabel"
     * with the number of results.
     */
    private void loadAutoComplete() {
        // TODO SE2205B
    }

    ////////////////////////////////////////
    // DO NOT CHANGE BELOW
    ////////////////////////////////////////

    @FXML
    protected void initialize() {

        input.setOnAction(ev -> {
            loadAutoComplete();
        });

        resultCounter.valueProperty().addListener((obs, oldValue, newValue) -> {
            loadAutoComplete();
        });

        countLabel.setText("0");
        loadButton.setOnAction(ev -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Choose Dictionary File");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            fc.setInitialDirectory(Paths.get(".").toFile());

            File file = fc.showOpenDialog(loadButton.getScene().getWindow());

            if (file != null) {
                loadTrie(file);

                input.setDisable(false);
                options.setDisable(false);
            }
        });
    }

    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/AutoComplete.fxml"));
    
        Scene scene = new Scene(root);
    
        stage.setTitle("Auto-complete Example");
        stage.setScene(scene);
        stage.show();
    }
    
    
}
