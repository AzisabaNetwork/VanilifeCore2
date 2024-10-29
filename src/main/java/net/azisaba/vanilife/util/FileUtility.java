package net.azisaba.vanilife.util;

import net.azisaba.vanilife.Vanilife;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

public class FileUtility
{
    public static boolean rmdir(File directory)
    {
        if (! directory.exists())
        {
            return false;
        }

        File[] files = directory.listFiles();

        if (files != null)
        {
            for (File file : files)
            {
                if (file.isDirectory())
                {
                    FileUtility.rmdir(file);
                }
                else
                {
                    file.delete();
                }
            }
        }

        return directory.delete();
    }

    public static void cpdir(Path from, Path to)
    {
        try (Stream<Path> stream = Files.walk(from))
        {
            stream.forEach(src -> {
                Path target = to.resolve(from.relativize(src));

                try
                {
                    Files.copy(src, target, StandardCopyOption.REPLACE_EXISTING);
                }
                catch (IOException e)
                {
                    Vanilife.sendExceptionReport(e);
                }
            });
        }
        catch (IOException ignored) {}
    }
}
