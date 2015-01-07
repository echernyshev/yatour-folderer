/**
 *
 */
package ru.kindteam;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;

/**
 * @author echernyshev
 * @since Jan 6, 2015
 */
public class MusicFileCopier
{
    private static final int MAX_COUNT_IN_FOLDER = 99;

    private final MusicFileDataSource files;
    private final Path target;

    private int folderCounter = 0;
    private int fileCounter = MAX_COUNT_IN_FOLDER;

    public MusicFileCopier(Path source, Path target) throws IOException
    {
        files = MusicFileDataSource.create(source);
        this.target = target;
    }

    public void copyFiles() throws IOException
    {
        for (Path source : files)
        {
            copyFile(source);
        }
    }
    private void copyFile(Path source) throws IOException
    {
        ensureCurrentWriteFolder();
        Path currentFolder = getCurrentWriteFolder();
        String fileName = String.format("%02d %s", fileCounter + 1, source.getFileName().toString());
        Path newFilePath = currentFolder.resolve(fileName);
        Files.copy(source, newFilePath);
        System.out.println("File " + source + " copied to file " + newFilePath);
        fileCounter++;
    }

    private void ensureCurrentWriteFolder() throws IOException
    {
        if (fileCounter >= MAX_COUNT_IN_FOLDER)
        {
            fileCounter = 0;
            folderCounter++;
            Path newFolder = getCurrentWriteFolder();
            if (Files.exists(newFolder))
            {
                FileUtils.deleteDirectory(newFolder.toFile());
                System.out.println("Folder " + newFolder + " deleted");
            }
            Files.createDirectory(newFolder);
            System.out.println("Folder " + newFolder + " created");
        }
    }

    private Path getCurrentWriteFolder()
    {
        String folderName = String.format("CD%02d", folderCounter);
        return target.resolve(folderName);
    }
}
