package ru.kindteam;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author echernyshev
 * @since Jan 6, 2015
 */
public class MusicFileDataSource implements Iterable<Path>
{
    private Path source;
    private SortedSet<Path> files = new TreeSet<>();

    private MusicFileDataSource(Path source)
    {
        this.source = requireNonNull(source, "path cannot be null");
    }

    public static MusicFileDataSource create(Path source) throws IOException
    {
        MusicFileDataSource result = new MusicFileDataSource(source);
        result.walkFiles();
        return result;
    }

    private void walkFiles() throws IOException
    {
        Files.walkFileTree(source, new SimpleFileVisitor<Path>()
                {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException
            {
                System.out.println("Processing directory " + dir.toString());
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
            {
                System.out.println("Processing file " + file.toString());
                if (file.toString().toLowerCase().endsWith(".mp3"))
                {
                    files.add(file);
                }
                return FileVisitResult.CONTINUE;
            }
                });
    }

    @Override
    public Iterator<Path> iterator()
    {
        return files.iterator();
    }
}
