package ru.kindteam;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Утилита для упаковки файлов по папкам для yatour M-06. Данный класс - точка входа в приложение.
 *
 * @author echernyshev
 * @since Jan 2, 2015
 */
public class YatourFolderer
{
    private final static class CopyParameters
    {
        CopyParameters(Path source, Path target)
        {
            this.source = source;
            this.target = target;
        }

        Path source;
        Path target;
    }

    private static Options OPTIONS = new Options();

    static
    {
        OPTIONS.addOption("in", true, "Input folder, that contains music files to copy.");
        OPTIONS.addOption("out", true, "Output folder for copying music files.");
    }

    public static void main( String[] args )
    {
        CopyParameters params = parseArguments(args);
        System.out.println(OPTIONS.toString());
        System.out.println("Processing files");

        try
        {
            MusicFileCopier copier = MusicFileCopier.create(params.source, params.target);
            copier.copyFiles();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }


    private static CopyParameters parseArguments(String[] args)
    {
        CommandLineParser parser = new BasicParser();
        try
        {
            CommandLine line = parser.parse(OPTIONS, args);
            if (!line.hasOption("in"))
            {
                printUsage("Bad syntax. Argument \"-in\" is required.");
                System.exit(1);
            }
            if (!line.hasOption("out"))
            {
                printUsage("Bad syntax. Argument \"-out\" is required.");
                System.exit(1);
            }
            String in = line.getOptionValue("in");
            String out = line.getOptionValue("out");
            checkFolder(in);
            checkFolder(out);
            return new CopyParameters(Paths.get(in), Paths.get(out));
        }
        catch (ParseException e)
        {
            printUsage("Parse command error: " + e.getMessage());
            System.exit(1);
            return null;
        }
    }

    private static void printUsage(String error)
    {
        System.out.println(error);
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(120,
                "java -jar yatour-folderer.jar -in /path/to/your/music -out /path/to/yatour/device",
                "Arguments: ", OPTIONS, "");
    }

    private static void checkFolder(String path)
    {
        if (!new File(path).isDirectory())
        {
            printUsage(path + "\nNot a folder");
            System.exit(1);
        }
    }
}
