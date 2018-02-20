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
    private RandomAccessFile inputStream;
    private OutputStream outputStream;
    private int offset = 0;
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
            inputStream = new RandomAccessFile(oldFile, "r");
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
            inputStream.seek(offset);
            int readBytes = inputStream.read(readerBuffer);
            if(readBytes == -1)
                return -1;
            return 0;
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
            System.out.println(Arrays.toString(readerBuffer));
            if (Arrays.equals(readerBuffer, oldByteSeq))
            {
                modificationCounter++;
                offset += oldByteSeq.length;
                outputStream.write(newByteSeq);
            }
            else
            {
                int bytesToWrite = getOffset();
                outputStream.write(readerBuffer, 0, bytesToWrite);
            }
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

    // tylko dla przypadku, gdy przeczytany ciag bajtow nie zgadza sie z zadanym ciagiem bajtow
    // jesli w przeczytanym ciagu bajtow znajdziemy podciag, bedacy poczatkiem poszukiwanego ciagu to ustawiamy odpowiednio offset
    // zwraca ile trzeba zapisac bajtow z readerBuffer do nowego pliku
    private int getOffset()
    {
        int oldByteSeqIdx = 0;
        int readerBufferIdx;
        int firstByteIdx = -1;
        for(readerBufferIdx = 1; readerBufferIdx < readerBuffer.length; ++readerBufferIdx)
        {
            if(readerBuffer[readerBufferIdx] == oldByteSeq[oldByteSeqIdx])
            {
                firstByteIdx = readerBufferIdx;
                oldByteSeqIdx++;
                readerBufferIdx++;
                while(readerBufferIdx < readerBuffer.length)
                {
                    if(readerBuffer[readerBufferIdx] != oldByteSeq[oldByteSeqIdx])
                        break;
                    oldByteSeqIdx++;
                    readerBufferIdx++;
                }
                break;
            }
        }
        if(readerBufferIdx == readerBuffer.length && firstByteIdx != -1)
        {
            offset += firstByteIdx;
            return firstByteIdx;
        }
        else
            {
            offset += readerBuffer.length;
            return readerBuffer.length;
        }
    }

}
