package Controllers;

import Models.FileSearchModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import java.io.File;

public class MainController
{
    @FXML
    private TextField fileExtensionTextField;
    @FXML
    private TextField oldByteSeqTextField;
    @FXML
    private RadioButton binFormatForOldBytesSeqRadioButton;
    @FXML
    private RadioButton hexFormatForOldBytesSeqRadioButton;
    @FXML
    private TextField newByteSeqTextField;
    @FXML
    private RadioButton binFormatForNewBytesSeqRadioButton;
    @FXML
    private RadioButton hexFormatForNewBytesSeqRadioButton;
    @FXML
    private Button startOrSuspendButton;
    @FXML
    private HBox directoryChooserHBox;

    private Thread filesProcessingThread;
    // dla ulatwienia - zeby pozniej nie wydobywac tego z tekstu przycisku
    private String rootDirectory;

    public MainController()
    { }

    @FXML
    void initialize()
    {
        setDirectoryChooserButton();
        // ustawienie listenerow
        oldByteSeqTextField.focusedProperty().addListener((e, oldValue, newValue) ->
            validateTextField(oldByteSeqTextField, binFormatForOldBytesSeqRadioButton,
            hexFormatForOldBytesSeqRadioButton, newValue));
        newByteSeqTextField.focusedProperty().addListener((e, oldValue, newValue) ->
            validateTextField(newByteSeqTextField, binFormatForNewBytesSeqRadioButton,
            hexFormatForNewBytesSeqRadioButton, newValue));
        startOrSuspendButton.setOnAction(e -> startFilesProcessing());
    }

    // Wstawia przycisk do DirectoryChooser do odpowiedniego VBoxa
    // Nie ma możliwosci wstawienia DirectoryChooser poprzez SceneBuildera
    private void setDirectoryChooserButton()
    {
        Button btnOpenDirectoryChooser = new Button();
        btnOpenDirectoryChooser.setText("Wybierz katalog");
        btnOpenDirectoryChooser.setOnAction(event ->
        {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File selectedDirectory = directoryChooser.showDialog(directoryChooserHBox.getScene().getWindow());
            if(selectedDirectory == null)
            {
                rootDirectory = "";
                btnOpenDirectoryChooser.setText("Wybierz katalog");
            }
            else
            {
                rootDirectory = selectedDirectory.getAbsolutePath();
                btnOpenDirectoryChooser.setText("Wybrany katalog: " + rootDirectory);
            }
        });
        directoryChooserHBox.getChildren().add(btnOpenDirectoryChooser);
    }

    // Walidacja danych wprowadzanych do pol tekstowych zwiazanych z ciagami bajtow
    // Uzalezniona od wybranego formatu wprowadzanych danych
    private void validateTextField(TextField textField, RadioButton binRadioButton, RadioButton hexRadioButton, boolean newValue)
    {
        // kiedy utracono "focus"
        if(!newValue)
        {
            String bytesSeqText = textField.getText();
            if(binRadioButton.isSelected())
            {
                if (!bytesSeqText.matches("[01]+"))
                {
                    textField.setText("");
                }
            }
            else if(hexRadioButton.isSelected())
            {
                if(!bytesSeqText.matches("[A-F0-9]+"))
                {
                    textField.setText("");
                }
            }
        }
    }

    private void startFilesProcessing()
    {
        String fileExtension = fileExtensionTextField.getText();
        byte[] oldByteSeq = oldByteSeqTextField.getText().getBytes();
        byte[] newByteSeq = newByteSeqTextField.getText().getBytes();
        FileSearchModel fileSearchModel = new FileSearchModel(rootDirectory, fileExtension, oldByteSeq, newByteSeq);
        filesProcessingThread = new Thread(fileSearchModel);
        filesProcessingThread.start();
    }

}
