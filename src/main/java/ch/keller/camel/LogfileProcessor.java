package ch.keller.camel;

import java.io.File;
import java.io.FileNotFoundException;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.xml.bind.JAXBElement;

import ch.keller.xmlns.logfiles.GetLogfileResponseType;
import ch.keller.xmlns.logfiles.GetLogfileType;
import ch.keller.xmlns.logfiles.ObjectFactory;

public class LogfileProcessor {

    // TODO only for tests
    private static final String DIR = "src/main/resources/log";

    private static final String DEFAULT_FILE = "50mb.log";

    public JAXBElement<GetLogfileResponseType> process(JAXBElement<GetLogfileType> getLogfileType) throws FileNotFoundException {
        final ObjectFactory objectFactory = new ObjectFactory();

        String fileName = getLogfileType.getValue().getInputParameter().getFilename();
        if (fileName == null || fileName.trim().length() == 0) {
            fileName = DEFAULT_FILE;
        }

        final File file = new File(DIR, fileName);
        System.out.printf("file %1$-15s = %2$15s%n", file.getName(), MemoryFormater.format(file.length()));

        final GetLogfileResponseType.Result result = objectFactory.createGetLogfileResponseTypeResult();
        result.setName(fileName);
        result.setContent(getDataHandler(file)); // TODO do not use in production...

        final GetLogfileResponseType response = objectFactory.createGetLogfileResponseType();
        response.setResult(result);

        return objectFactory.createGetLogfileResponse(response);
    }

    private DataHandler getDataHandler(File file) {
        final DataSource dataSource = new FileDataSource(file);
        return new DataHandler(dataSource);
    }

}
