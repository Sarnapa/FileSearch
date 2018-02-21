package Controllers;

import Models.FileSearchModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import static Controllers.Helpers.ControllersConverter.binStringToByteArray;
import static Controllers.Helpers.ControllersConverter.hexStringToByteArray;

public class MainController
{
    private Button btnOpenDirectoryChooser;
    @FXML
    private TextField fileExtensionTextField;
    @FXML
    private TextField oldByteSeqTextField;
    @FXML
    private RadioButton binFormatForOldByteSeqRadioButton;
    @FXML
    private RadioButton hexFormatForOldByteSeqRadioButton;
    @FXML
    private TextField newByteSeqTextField;
    @FXML
    private RadioButton binFormatForNewByteSeqRadioButton;
    @FXML
    private RadioButton hexFormatForNewByteSeqRadioButton;
    @FXML
    private Button startOrSuspendButton;
    @FXML
    private HBox directoryChooserHBox;

    private FileSearchModel fileSearchModelTask;
    // dla ulatwienia - zeby pozniej nie wydobywac tego z tekstu przycisku
    private String rootDirectory;
    // pola do sprawdzania, czy do danych pol zostaly wprowadzone poprawne wartosci
    // uznajemy, ze to pole moze byc puste i szukamy plikow bez rozszerzenia
    private boolean isFileExtensionTextFieldValid = true;
    private boolean isOldByteSeqTextFieldValid = false;
    // uznajemy ze to pole moze byc puste i po prostu usuwamy wskazane bajty
    private boolean isNewByteSeqTextFieldValid = true;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public MainController()
    { }

    @FXML
    void initialize()
    {
        setDirectoryChooserButton();
        // ustawienie listenerow
        fileExtensionTextField.focusedProperty().addListener((e, oldValue, newValue) ->
        {
            validateFileExtensionTextField(newValue);
            setStartOrSuspendButtonDisable();
        });
        oldByteSeqTextField.focusedProperty().addListener((e, oldValue, newValue) ->
        {
            validateByteSeqTextField(oldByteSeqTextField, binFormatForOldByteSeqRadioButton,
                hexFormatForOldByteSeqRadioButton, newValue);
            setStartOrSuspendButtonDisable();
        });
        binFormatForOldByteSeqRadioButton.setOnAction(e ->
        {
            validateByteSeqTextField(oldByteSeqTextField, binFormatForOldByteSeqRadioButton,
                hexFormatForOldByteSeqRadioButton, false);
            setStartOrSuspendButtonDisable();
        });
        hexFormatForOldByteSeqRadioButton.setOnAction(e ->
        {
            validateByteSeqTextField(oldByteSeqTextField, binFormatForOldByteSeqRadioButton,
                    hexFormatForOldByteSeqRadioButton, false);
            setStartOrSuspendButtonDisable();
        });
        newByteSeqTextField.focusedProperty().addListener((e, oldValue, newValue) ->
        {
            validateByteSeqTextField(newByteSeqTextField, binFormatForNewByteSeqRadioButton,
                hexFormatForNewByteSeqRadioButton, newValue);
            setStartOrSuspendButtonDisable();
        });
        binFormatForNewByteSeqRadioButton.setOnAction(e ->
        {
            validateByteSeqTextField(newByteSeqTextField, binFormatForNewByteSeqRadioButton,
                    hexFormatForNewByteSeqRadioButton, false);
            setStartOrSuspendButtonDisable();
        });
        hexFormatForNewByteSeqRadioButton.setOnAction(e ->
        {
            validateByteSeqTextField(newByteSeqTextField, binFormatForNewByteSeqRadioButton,
                    hexFormatForNewByteSeqRadioButton, false);
            setStartOrSuspendButtonDisable();
        });
        startOrSuspendButton.setDisable(true);
        startOrSuspendButton.setOnAction(e -> startFilesProcessing());
    }

    // Wstawia przycisk do DirectoryChooser do odpowiedniego VBoxa
    // Nie ma możliwosci wstawienia DirectoryChooser poprzez SceneBuildera
    private void setDirectoryChooserButton()
    {
        btnOpenDirectoryChooser = new Button();
        btnOpenDirectoryChooser.setText("Wybierz katalog");
        btnOpenDirectoryChooser.setOnAction(event ->
        {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File selectedDirectory = directoryChooser.showDialog(directoryChooserHBox.getScene().getWindow());
            if(selectedDirectory == null)
            {
                rootDirectory = null;
                btnOpenDirectoryChooser.setText("Wybierz katalog");
            }
            else
            {
                rootDirectory = selectedDirectory.getAbsolutePath();
                btnOpenDirectoryChooser.setText("Wybrany katalog: " + rootDirectory);
            }
            setStartOrSuspendButtonDisable();
        });
        directoryChooserHBox.getChildren().add(btnOpenDirectoryChooser);
    }

    private void validateFileExtensionTextField(Boolean newValue)
    {
        if(!newValue)
        {
            //Tooltip tooltip = new Tooltip();
            String fileExtensionText = fileExtensionTextField.getText();
            if(fileExtensionText.isEmpty())
            {
                //setNotValidTemplateForTextField(fileExtensionTextField, tooltip, "Pole nie może być puste!");
                setValidTemplateForTextField(fileExtensionTextField);
                isFileExtensionTextFieldValid = true;
            }
            else
            {
                setValidTemplateForTextField(fileExtensionTextField);
                isFileExtensionTextFieldValid = true;
            }
        }
    }

    // Walidacja danych wprowadzanych do pol tekstowych zwiazanych z ciagami bajtow
    // Uzalezniona od wybranego formatu wprowadzanych danych
    private void validateByteSeqTextField(TextField textField, RadioButton binRadioButton, RadioButton hexRadioButton, boolean newValue)
    {
        // kiedy utracono "focus"
        if(!newValue)
        {
            Tooltip tooltip = new Tooltip();
            String bytesSeqText = textField.getText();
            if(bytesSeqText.isEmpty())
            {
                if(textField == oldByteSeqTextField)
                {
                    setNotValidTemplateForTextField(textField, tooltip, "Pole nie może być puste!");
                    setIsByteSeqTextFieldValid(textField, false);
                }
                else if(textField == newByteSeqTextField)
                {
                    setValidTemplateForTextField(textField);
                    setIsByteSeqTextFieldValid(textField, true);
                }
            }
            else if(binRadioButton.isSelected())
            {
                if (!bytesSeqText.matches("[01]+"))
                {
                    setNotValidTemplateForTextField(textField, tooltip, "Wprowadzona wartość nie jest zgodna z formatem!");
                    setIsByteSeqTextFieldValid(textField, false);
                }
                else if(bytesSeqText.length() % 8 != 0)
                {
                    setNotValidTemplateForTextField(textField, tooltip, "Nieprawidłowa ilość podanych bitów!");
                    setIsByteSeqTextFieldValid(textField, false);
                }
                else
                {
                    setValidTemplateForTextField(textField);
                    setIsByteSeqTextFieldValid(textField, true);
                }
            }
            else if(hexRadioButton.isSelected())
            {
                if(!bytesSeqText.matches("[A-F0-9]+"))
                {
                    setNotValidTemplateForTextField(textField, tooltip, "Wprowadzona wartość nie jest zgodna z formatem!");
                    setIsByteSeqTextFieldValid(textField, false);
                }
                else if(bytesSeqText.length() % 2 != 0)
                {
                    setNotValidTemplateForTextField(textField, tooltip, "Nieprawidłowa ilość podanych bitów!");
                    setIsByteSeqTextFieldValid(textField, false);
                }
                else
                {
                    setValidTemplateForTextField(textField);
                    setIsByteSeqTextFieldValid(textField, true);
                }
            }
        }
    }

    // Uruchomienie taska do wykonywania programu
    private void startFilesProcessing()
    {
        String fileExtension = fileExtensionTextField.getText();
        byte[] oldByteSeq = getByteArray(binFormatForOldByteSeqRadioButton, hexFormatForOldByteSeqRadioButton,
                oldByteSeqTextField.getText());
        byte[] newByteSeq = getByteArray(binFormatForNewByteSeqRadioButton, hexFormatForNewByteSeqRadioButton,
                newByteSeqTextField.getText());
        fileSearchModelTask = new FileSearchModel(rootDirectory, fileExtension, oldByteSeq, newByteSeq);
        fileSearchModelTask.setOnSucceeded(e ->
        {
            setInputFieldsDisable(false);
            startOrSuspendButton.setText("Start");
            startOrSuspendButton.setOnAction(e1 -> startFilesProcessing());
            endTime = LocalDateTime.now();
            showResultView();
        });
        Thread filesProcessingThread = new Thread(fileSearchModelTask);
        filesProcessingThread.start();
        startTime = LocalDateTime.now();
        setInputFieldsDisable(true);
        startOrSuspendButton.setText("Stop");
        startOrSuspendButton.setOnAction(e -> suspendFilesProcessing());
    }

    // Zatrzymanie taska
    private void suspendFilesProcessing()
    {
        fileSearchModelTask.cancel();
        setInputFieldsDisable(false);
        startOrSuspendButton.setText("Start");
        startOrSuspendButton.setOnAction(e -> startFilesProcessing());
        endTime = LocalDateTime.now();
        showResultView();
    }

    // Metody pomocnicze

    private void setValidTemplateForTextField(TextField textField)
    {
        textField.getStyleClass().removeIf(style -> style.equals("invalidTextField"));
        textField.setTooltip(null);
    }

    private void setNotValidTemplateForTextField(TextField textField, Tooltip tooltip, String msg)
    {
        tooltip.setText(msg);
        textField.setTooltip(tooltip);
        textField.getStyleClass().add("invalidTextField");
    }

    private void setIsByteSeqTextFieldValid(TextField textField, boolean value)
    {
        if(textField == oldByteSeqTextField)
            isOldByteSeqTextFieldValid = value;
        else
            isNewByteSeqTextFieldValid = value;
    }

    // Metoda do wylaczania/wlaczania przycisku, w zaleznosci od poprawnosci wprowadzonych danych
    private void setStartOrSuspendButtonDisable()
    {
        if(rootDirectory != null && isFileExtensionTextFieldValid && isOldByteSeqTextFieldValid && isNewByteSeqTextFieldValid)
            startOrSuspendButton.setDisable(false);
        else
            startOrSuspendButton.setDisable(true);
    }

    private void setInputFieldsDisable(boolean value)
    {
        btnOpenDirectoryChooser.setDisable(value);
        fileExtensionTextField.setDisable(value);
        oldByteSeqTextField.setDisable(value);
        binFormatForOldByteSeqRadioButton.setDisable(value);
        hexFormatForOldByteSeqRadioButton.setDisable(value);
        newByteSeqTextField.setDisable(value);
        binFormatForNewByteSeqRadioButton.setDisable(value);
        hexFormatForNewByteSeqRadioButton.setDisable(value);
    }

    private static byte[] getByteArray(RadioButton binFormatForByteSeqRadioButton, RadioButton hexFormatForByteSeqRadioButton,
                                       String value)
    {
        if(binFormatForByteSeqRadioButton.isSelected())
            return binStringToByteArray(value);
        else if(hexFormatForByteSeqRadioButton.isSelected())
            return hexStringToByteArray(value);
        // w przypadku bledu
        else return null;
    }

    private void showResultView()
    {
        FXMLLoader ResultWindowLoader = new FXMLLoader(getClass().getResource("../Views/ResultView.fxml"));
        Parent root = null;
        try
        {
            root = ResultWindowLoader.load();
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
        ResultController resultController = ResultWindowLoader.getController();
        resultController.setRootDirectory(rootDirectory);
        resultController.setExtensionFile(fileExtensionTextField.getText());
        resultController.setOldByteSeq(oldByteSeqTextField.getText());
        resultController.setNewByteSeq(newByteSeqTextField.getText());
        resultController.setStartTime(startTime);
        resultController.setEndTime(endTime);
        resultController.setFilesList(fileSearchModelTask.getFilesList());
        resultController.setFilesStatsMap(fileSearchModelTask.getFilesStatsMap());
        resultController.setErrorsMap(fileSearchModelTask.getErrorsMap());
        resultController.init(root);
        resultController.showWindow();
    }

}
