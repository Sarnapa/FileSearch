package Controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static Controllers.Helpers.ControllersConverter.getDateFormat;

public class ResultController
{
    @FXML
    private Label rootDirectoryLabel;
    @FXML
    private Label extensionFileLabel;
    @FXML
    private Label oldByteSeqLabel;
    @FXML
    private Label newByteSeqLabel;
    @FXML
    private Label startTimeLabel;
    @FXML
    private Label endTimeLabel;
    @FXML
    private ListView<String> filesListView;
    @FXML
    private TableView filesStatsTableView;
    @FXML
    private TableView errorsTableView;
    private Stage stage;

    private String rootDirectory;
    private String extensionFile;
    private String oldByteSeq;
    private String newByteSeq;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Map<String, Integer> filesStatsMap;
    private Map<String, Exception> errorsMap;

    public void setRootDirectory(String rootDirectory)
    {
        this.rootDirectory = rootDirectory;
    }

    public void setExtensionFile(String extensionFile)
    {
        this.extensionFile = extensionFile;
    }

    public void setOldByteSeq(String oldByteSeq)
    {
        this.oldByteSeq = oldByteSeq;
    }

    public void setNewByteSeq(String newByteSeq)
    {
        this.newByteSeq = newByteSeq;
    }

    public void setStartTime(LocalDateTime startTime)
    {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime)
    {
        this.endTime = endTime;
    }

    public void setFilesStatsMap(Map<String, Integer> filesStatsMap)
    {
        this.filesStatsMap = filesStatsMap;
    }

    public void setErrorsMap(Map<String, Exception> errorsMap)
    {
        this.errorsMap = errorsMap;
    }

    public ResultController()
    {}

    void init(Parent root)
    {
        // Ograniczenia dla okna z rezultatami
        final int MIN_WIDTH = 800;
        final int MIN_HEIGHT = 600;
        stage = new Stage();
        stage.setTitle("Wyniki");
        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);
        stage.setScene(new Scene(root, MIN_WIDTH, MIN_HEIGHT));
        setMainDataPart();
        setFilesPart();
        setFilesStatsPart();
        setErrorsPart();
    }

    public void showWindow()
    {
        stage.show();
    }

    private void setMainDataPart()
    {
        rootDirectoryLabel.setText(rootDirectory);
        extensionFileLabel.setText(extensionFile);
        oldByteSeqLabel.setText(oldByteSeq);
        newByteSeqLabel.setText(newByteSeq);
        startTimeLabel.setText(getDateFormat(startTime));
        endTimeLabel.setText(getDateFormat(endTime));
    }

    private void setFilesPart()
    {
        ObservableList<String> filenamesList = FXCollections.observableArrayList(filesStatsMap.keySet());
        filesListView.setItems(filenamesList);
    }

    private void setFilesStatsPart()
    {
        // Zeby nie bylo tego domyslnego napisu, gdy tabela pusta
        filesStatsTableView.setPlaceholder(new Label(""));
        List<TableColumn> columns = filesStatsTableView.getColumns();
        TableColumn filenameCol = columns.get(0);
        TableColumn modificationsCountCol = columns.get(1);
        filenameCol.setCellValueFactory((Callback<TableColumn.CellDataFeatures<Map.Entry<String, Integer>, String>, ObservableValue<String>>) p ->
                new SimpleStringProperty(p.getValue().getKey()));
        modificationsCountCol.setCellValueFactory((Callback<TableColumn.CellDataFeatures<Map.Entry<String, Integer>, String>, ObservableValue<String>>) p ->
                new SimpleStringProperty(p.getValue().getValue().toString()));
        filesStatsTableView.setItems(FXCollections.observableArrayList(filesStatsMap.entrySet()));
    }

    private void setErrorsPart()
    {
        // Zeby nie bylo tego domyslnego napisu, gdy tabela pusta
        errorsTableView.setPlaceholder(new Label(""));
        List<TableColumn> columns = errorsTableView.getColumns();
        TableColumn filenameCol = columns.get(0);
        TableColumn errorCol = columns.get(1);
        filenameCol.setCellValueFactory((Callback<TableColumn.CellDataFeatures<Map.Entry<String, Exception>, String>, ObservableValue<String>>) p ->
                new SimpleStringProperty(p.getValue().getKey()));
        errorCol.setCellValueFactory((Callback<TableColumn.CellDataFeatures<Map.Entry<String, Exception>, String>, ObservableValue<String>>) p ->
                new SimpleStringProperty(p.getValue().getValue().getMessage()));
        errorsTableView.setItems(FXCollections.observableArrayList(errorsMap.entrySet()));
    }

}
