package appu26j.utils;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUtil
{
    public static void unzip(File input, File output)
    {
        try
        {
            if (!output.exists())
            {
                output.mkdirs();
            }

            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(input));
            ZipEntry zipEntry = zipInputStream.getNextEntry();

            while (zipEntry != null)
            {
                File file = new File(output, zipEntry.getName());

                if (!zipEntry.isDirectory())
                {
                    extractFile(zipInputStream, file);
                }

                else
                {
                    file.mkdirs();
                }

                zipInputStream.closeEntry();
                zipEntry = zipInputStream.getNextEntry();
            }

            zipInputStream.close();
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static void extractFile(ZipInputStream zipInputStream, File file)
    {
        try
        {
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            byte[] bytes = new byte[4096];
            int read = 0;

            while ((read = zipInputStream.read(bytes)) != -1)
            {
                bufferedOutputStream.write(bytes, 0, read);
            }

            bufferedOutputStream.close();
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static class FileTransferable implements Transferable
    {
        private final ArrayList<File> files;

        public FileTransferable(ArrayList<String> files, boolean unused)
        {
            this.files = new ArrayList<>();

            for (String filePath : files)
            {
                this.files.add(new File(filePath));
            }
        }

        public FileTransferable(ArrayList<File> files)
        {
            this.files = new ArrayList<>(files);
        }

        @Override
        public DataFlavor[] getTransferDataFlavors()
        {
            return new DataFlavor[] {DataFlavor.javaFileListFlavor};
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor)
        {
            return DataFlavor.javaFileListFlavor.equals(flavor);
        }

        @Override
        public Object getTransferData(DataFlavor flavor)
        {
            return this.files;
        }
    }
}
