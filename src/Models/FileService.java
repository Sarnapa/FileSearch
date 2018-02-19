package Models;

import org.apache.commons.io.FilenameUtils;
import java.io.*;
import java.util.Arrays;

class FileService
{
    private File oldFile;
    private File newFile;
    private byte[] oldByteSeq;
    private byte[] newByteSeq;
    private byte[] readerBuffer;
    private InputStream inputStream;
    private OutputStream outputStream;
    private boolean failed = false;
    private int modificationCounter = 0;

    FileService(File file, byte[] oldByteSeq, byte[] newByteSeq) throws Exception
    {
        this.oldFile = file;
        setNewFile();
        this.oldByteSeq = oldByteSeq;
        this.newByteSeq = newByteSeq;
        readerBuffer = new byte[oldByteSeq.length];
    }

    // utworzenie odpowiednich strumieni
    void open() throws IOException
    {
        try
        {
            inputStream = new BufferedInputStream(new FileInputStream(oldFile));
            outputStream = new BufferedOutputStream(new FileOutputStream(newFile));
        }
        catch (IOException e)
        {
            failed = true;
            throw new IOException(e);
        }
    }

    // zamkniecie strumieni
    int close() throws Exception
    {
        if(failed)
            deleteNewFile();
        else
            renameNewFile();
        inputStream.close();
        outputStream.close();
        return modificationCounter;
    }

    void suspendService()
    {
        failed = true;
    }

    int readFromFile() throws IOException
    {
        try
        {
            readerBuffer = new byte[oldByteSeq.length];
            return inputStream.read(readerBuffer);
        }
        catch (IOException e)
        {
            failed = true;
            throw new IOException(e);
        }
    }

    void writeToFile() throws IOException
    {
        try
        {
            //System.out.println(Arrays.toString(readerBuffer));
            if (Arrays.equals(readerBuffer, oldByteSeq))
            {
                modificationCounter++;
                outputStream.write(newByteSeq);
            }
            else
                outputStream.write(readerBuffer);
        }
        catch (IOException e)
        {
            failed = true;
            throw new IOException(e);
        }
    }

    private void setNewFile() throws Exception
    {
        String fileExtension = FilenameUtils.getExtension(oldFile.getName());
        newFile = oldFile;
        int i = 0;
        while(newFile.exists())
        {
            if (fileExtension.isEmpty())
                newFile = new File(oldFile.getAbsolutePath() + "Tmp" + i);
            else
                newFile = new File(FilenameUtils.removeExtension(oldFile.getAbsolutePath()) + "Tmp" + i + "." + fileExtension);
            i++;
        }
        try
        {
            boolean success = newFile.createNewFile();
            if(!success)
            {
                failed = true;
                throw new Exception("Internal error associated with creating file service.");
            }
        }
        catch (IOException e)
        {
            failed = true;
            throw new IOException(e);
        }
    }

    private void renameNewFile() throws Exception
    {
        boolean success = newFile.renameTo(oldFile);
        if(!success)
        {
            failed = true;
            throw new Exception("Internal error associated with closing file service.");
        }
    }

    private void deleteNewFile() throws Exception
    {
        boolean success = newFile.delete();
        if(!success)
        {
            failed = true;
            throw new Exception("Internal error associated with closing file service.");
        }
    }

}
