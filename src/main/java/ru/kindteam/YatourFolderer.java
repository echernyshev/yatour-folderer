package ru.kindteam;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Утилита для упаковки файлов по папкам для yatour M-06. Данный класс - точка входа в приложение.
 *
 * @author echernyshev
 * @since Jan 2, 2015
 */
public class YatourFolderer
{
    private static final String HELP = "Использование команды:\n"
            + "java -jar yatour-folderer.jar /path/to/input/files /path/to/out";

    public static void main( String[] args )
    {
        checkArguments(args);
        System.out.println("Processing files");

        try
        {
            MusicFileCopier copier = MusicFileCopier.create(Paths.get(args[0]), Paths.get(args[1]));
            copier.copyFiles();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }


    private static void checkArguments(String[] args)
    {
        if (args.length != 2)
        {
            printHelp("Количество агргументов должно равняться 2");
            System.exit(1);
        }
        checkFolder(args[0]);
        checkFolder(args[1]);
    }

    private static void printHelp(String error)
    {
        System.out.println(error);
        System.out.println(HELP);
    }

    private static void checkFolder(String path)
    {
        if (!new File(path).isDirectory())
        {
            printHelp(path + "\nНе является папкой!");
            System.exit(1);
        }
    }
}
