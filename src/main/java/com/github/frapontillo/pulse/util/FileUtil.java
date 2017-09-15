package com.github.frapontillo.pulse.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Utility class for reading {@link File}s.
 *
 * @author Francesco Pontillo
 */
public class FileUtil {

    /**
     * Read a {@link File} from a given path or resource name. The path will be checked first, if
     * it there is not file then the {@code fileName} will be considered a resource file.
     *
     * @param fileName           The path of the file or the name of the resource to read (in this
     *                           order).
     * @param resourceClassOwner The class owner of the resource.
     *
     * @return An {@link InputStream} to read the file contents from.
     * @throws FileNotFoundException If the file can't be found in the path or among the resources.
     */
    public static InputStream readFileFromPathOrResource(String fileName, Class resourceClassOwner)
            throws FileNotFoundException {

        InputStream inputStream;
        try {
            return new FileInputStream(fileName);
        } catch (FileNotFoundException ignored) {
        }

        inputStream = resourceClassOwner.getClassLoader().getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new FileNotFoundException();
        }
        return inputStream;

    }

}
