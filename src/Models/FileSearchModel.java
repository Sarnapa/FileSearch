package Models;

import javafx.concurrent.Task;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Stack;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.*;

public class FileSearchModel extends Task<Void>
{

    private String rootDirectory;
    private String fileExtension;
    private byte[] oldByteSeq;
    private byte[] newByteSeq;
    private ArrayList<File> filesList = new ArrayList<>();

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
        if(!isCancelled())
            modifyFiles();
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
        printFilesList();
    }

    private void modifyFiles()
    {

    }

    private void getFilesAndDirs(File currentRoot, Stack<File> dirsList)
    {
        SuffixFileFilter extFilter = new SuffixFileFilter(fileExtension);
        filesList.addAll(FileUtils.listFiles(currentRoot, extFilter, null));
        File[] subDirs = currentRoot.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);
        if(subDirs != null)
            for(File subDir: subDirs)
            {
                dirsList.push(subDir);
            }
    }

    private void printFilesList()
    {
        System.out.println("Lista plików o podanym rozszerzeniu: ");
        for(File file: filesList)
            System.out.println(file.getAbsolutePath());
    }
}
