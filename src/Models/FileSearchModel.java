package Models;

import javafx.concurrent.Task;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.*;

public class FileSearchModel extends Task<Void>
{
    private String rootDirectory;
    private String fileExtension;
    private byte[] oldByteSeq;
    private byte[] newByteSeq;
    private ArrayList<File> filesList = new ArrayList<>();
    // Zawiera informacje, ile modyfikacji nastapilo w danym pliku
    private HashMap<File, Integer> filesStatsMap = new HashMap<>();
    // Zawiera informacje o wszystkich bledach jakie wystapily
    private HashMap<File, Exception> errorsMap = new HashMap<>();

    public ArrayList<File> getFilesList()
    {
        return filesList;
    }

    public HashMap<File, Integer> getFilesStatsMap()
    {
        return filesStatsMap;
    }

    public HashMap<File, Exception> getErrorsMap()
    {
        return errorsMap;
    }

    public FileSearchModel(String rootDirectory, String fileExtension, byte[] oldByteSeq, byte[] newByteSeq)
    {
        this.rootDirectory = rootDirectory;
        this.fileExtension = fileExtension;
        this.oldByteSeq = oldByteSeq;
        this.newByteSeq = newByteSeq;
    }

    @Override
    protected Void call()
    {
        searchFiles();
        printFilesList();
        if(!isCancelled())
            modifyFiles();
        printFilesStatsMap();
        printErrorsMap();
        return null;
    }

    private void searchFiles()
    {
        Stack<File> dirsList = new Stack<>();
        dirsList.push(new File(rootDirectory));
        while(!dirsList.empty())
        {
            File currentRoot = dirsList.pop();
            getFilesAndDirs(currentRoot, dirsList);
            if(isCancelled())
                break;
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                if(isCancelled())
                    break;
            }
        }
    }

    private void modifyFiles()
    {
        FileService fileService;
        for(File file: filesList)
        {
            try
            {
                fileService = new FileService(file, oldByteSeq, newByteSeq);
                fileService.open();
                while(fileService.readFromFile() != -1)
                {
                    fileService.writeToFile();
                    if(isCancelled())
                    {
                        fileService.suspendService();
                        break;
                    }
                    try
                    {
                        Thread.sleep(100);
                    }
                    catch (InterruptedException e)
                    {
                        if(isCancelled())
                        {
                            fileService.suspendService();
                            break;
                        }
                    }
                }
                int modificationCount = fileService.close();
                filesStatsMap.put(file, modificationCount);
            }
            catch(Exception e)
            {
                errorsMap.put(file, new Exception(e));
            }
        }
    }

    private void getFilesAndDirs(File currentRoot, Stack<File> dirsList)
    {
        List<File> allFiles = (List<File>)FileUtils.listFiles(currentRoot, TrueFileFilter.INSTANCE, null);
        for(File file: allFiles)
        {
            //System.out.println(file.getAbsolutePath());
            if(FilenameUtils.getExtension(file.getName()).equals(fileExtension))
                filesList.add(file);
        }
        File[] subDirs = currentRoot.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);
        if(subDirs != null)
            for(File subDir: subDirs)
            {
                dirsList.push(subDir);
            }
    }

    private void printFilesList()
    {
        System.out.println("Lista plików o podanym rozszerzeniu:");
        for(File file: filesList)
            System.out.println(file.getAbsolutePath());
    }

    private void printFilesStatsMap()
    {
        System.out.println("Statystyki zmian w plikach(Plik - Ilość zmian):");
        for(File file: filesStatsMap.keySet())
        {
            System.out.println(file + " - " + filesStatsMap.get(file));
        }
    }

    private void printErrorsMap()
    {
        System.out.println("Błędy dotyczące modyfikacji plików(Plik - Informacja o błędzie):");
        for(File file: errorsMap.keySet())
        {
            System.out.println(file + " - " + errorsMap.get(file).getMessage());
        }
    }
}
