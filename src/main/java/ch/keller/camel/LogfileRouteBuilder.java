package ch.keller.camel;

import java.lang.management.ManagementFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.spi.DataFormat;
import org.springframework.stereotype.Component;

import ch.keller.xmlns.logfiles.GetLogfileResponseType;
import ch.keller.xmlns.logfiles.GetLogfileType;
import ch.keller.xmlns.logfiles.ObjectFactory;


@Component
public class LogfileRouteBuilder extends RouteBuilder {

    public static final String ENDPOINT = "cxf:bean:" + AppConfiguration.BEAN_C3_LOGFILES_WS+"?dataFormat=PAYLOAD";

    @Override
    public void configure() throws Exception {
        from(ENDPOINT)
            .noStreamCaching()
            .process(new GcProcessor())
            .wireTap("direct:printHeap")
            //.setProperty(Message.MTOM_ENABLED, constant(true))
            //.log("${body}")
            .unmarshal(jaxbDataFormatGetlogFileRequest())
            .bean(new LogfileProcessor())
            .marshal(jaxbDataFormatGetlogFileResponse())
            .wireTap("direct:printHeap");

        from("direct:printHeap")
            .process(ex -> {
                final long heap = getCurrentlyUsedMemory();
                System.out.printf("free heap            = %15s%n", MemoryFormater.format(heap));
             });
    }

    private static DataFormat jaxbDataFormatGetlogFileRequest() throws JAXBException {
        // TODO leads to javax.xml.bind.UnmarshalException
        //final JAXBContext jaxbContext = JAXBContext.newInstance(GetLogfileType.class);
        //final JaxbDataFormat jaxbDataFormat = new JaxbDataFormat(jaxbContext);

        final ObjectFactory objectFactory = new ObjectFactory();
        final JaxbDataFormat jaxbDataFormat = new JaxbDataFormat();
        jaxbDataFormat.setContextPath(GetLogfileType.class.getPackage().getName());
        jaxbDataFormat.setPartClass(GetLogfileType.class.getName());
        jaxbDataFormat.setPartNamespace(objectFactory.createGetLogfile(new GetLogfileType()).getName());

        jaxbDataFormat.setIgnoreJAXBElement(false); // see http://camel.apache.org/jaxb.html#JAXB-WorkingwiththeObjectFactory
        return jaxbDataFormat;
    }

    private static DataFormat jaxbDataFormatGetlogFileResponse() throws JAXBException {
        final JAXBContext jaxbContext = JAXBContext.newInstance(GetLogfileResponseType.class);
        final JaxbDataFormat jaxbDataFormat = new JaxbDataFormat(jaxbContext);
        jaxbDataFormat.setIgnoreJAXBElement(false); // see http://camel.apache.org/jaxb.html#JAXB-WorkingwiththeObjectFactory
        return jaxbDataFormat;
    }

    private static class GcProcessor implements Processor {

        @Override
        public void process(Exchange exchange) throws Exception {
            for (int i = 0; i < 10; i++) {
                System.gc();
            }
        }

    }

    // https://cruftex.net/2017/03/28/The-6-Memory-Metrics-You-Should-Track-in-Your-Java-Benchmarks.html
    private static long getCurrentlyUsedMemory() {
        return
          ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed() +
          ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getUsed();
      }

}
