package ch.keller.camel;

import java.io.File;
import java.io.FileNotFoundException;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;

import ch.keller.xmlns.logfiles.GetLogfileResponseType;
import ch.keller.xmlns.logfiles.GetLogfileType;

public class LogfileProcessor {
    private static final String DIR = "src/main/resources/log";

    private static final String DEFAULT_FILE = "50mb.log";

    public GetLogfileResponseType.Result process(GetLogfileType.InputParameter getLogfileType) throws FileNotFoundException {
        String fileName = getLogfileType.getFilename();
        if (fileName == null || fileName.trim().length() == 0) {
            fileName = DEFAULT_FILE;
        }

        final File file = new File(DIR, fileName);
        System.out.printf("file %1$-15s = %2$15s%n", file.getName(), MemoryFormater.format(file.length()));

        final GetLogfileResponseType.Result result = new GetLogfileResponseType.Result();
        result.setName(fileName);
        result.setContent(getDataHandler(file)); // Good to use in production...

        return result;
    }

    private DataHandler getDataHandler(File file) {
        final DataSource dataSource = new FileDataSource(file);
        return new DataHandler(dataSource);
    }

}
