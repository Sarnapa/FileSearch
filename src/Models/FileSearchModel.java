package Models;

import javafx.concurrent.Task;
import java.util.ArrayList;
import java.util.Arrays;

public class FileSearchModel extends Task<Void>
{

    private String rootDirectory;
    private String fileExtension;
    private byte[] oldByteSeq;
    private byte[] newByteSeq;
    private ArrayList<String> filesList = new ArrayList<>();

    public FileSearchModel(String rootDirectory, String fileExtension, byte[] oldByteSeq, byte[] newByteSeq)
    {
        this.rootDirectory = rootDirectory;
        this.fileExtension = fileExtension;
        this.oldByteSeq = oldByteSeq;
        this.newByteSeq = newByteSeq;
    }

    @Override
    protected Void call() throws Exception
    {
        for(int i = 0; i < 10000000; ++i)
        {
            System.out.println("iteration number: " + i);
            System.out.println("rootDirectory: " + rootDirectory);
            System.out.println("fileExtension: " + fileExtension);
            System.out.println("oldBytes: " + Arrays.toString(oldByteSeq));
            System.out.println("newBytes: " + Arrays.toString(newByteSeq));
        }
        return null;
    }
}
