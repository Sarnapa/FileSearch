package Controllers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import static Controllers.Helpers.ControllersConverter.getDateFormat;

public class ResultController
{

    @FXML
    private ScrollPane mainDataScrollPane;
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
    private Label durationLabel;
    @FXML
    private ListView filesListView;
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
    private ArrayList<File> filesList;
    private HashMap<File, Integer> filesStatsMap;
    private HashMap<File, Exception> errorsMap;

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

    public void setFilesList(ArrayList<File> filesList)
    {
        this.filesList = filesList;
    }

    public void setFilesStatsMap(HashMap<File, Integer> filesStatsMap)
    {
        this.filesStatsMap = filesStatsMap;
    }

    public void setErrorsMap(HashMap<File, Exception> errorsMap)
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
        mainDataScrollPane.setFitToHeight(true);
    }

    private void setFilesPart()
    {

    }

    private void setFilesStatsPart()
    {

    }

    private void setErrorsPart()
    {

    }

}
