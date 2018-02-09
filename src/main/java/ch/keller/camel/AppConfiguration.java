package ch.keller.camel;

import org.apache.camel.component.cxf.CxfEndpoint;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.transport.common.gzip.GZIPFeature;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ch.keller.xmlns.logfiles.LogfilesPortType;

@Configuration
public class AppConfiguration {

    public static final String PATH = "/logfiles";

    public static final String BEAN_C3_LOGFILES_WS = "c3-logfiles-ws";

    @Bean
    public ServletRegistrationBean cxfServlet() {
        return new ServletRegistrationBean(new CXFServlet(), "/*");
    }

    @Bean(name = "cxf", destroyMethod = "shutdown")
    public SpringBus cxfBus() {
        final SpringBus bus = new SpringBus();
        bus.setId("cxf");

        // Following parameters have no influence
        // bus.setProperty("bus.io.CachedOutputStream.Threshold", cxfProperties.getStreamFileCacheBytes().toString());
        // bus.setProperty("bus.io.CachedOutputStream.MaxSize", "100000000");
        // bus.setProperty("org.apache.cxf.output.buffering", true);
        // bus.setProperty("org.apache.cxf.io.CachedOutputStream.OutputDirectory", "/tmp/");
        // bus.setProperty("org.apache.cxf.io.CachedOutputStream.Threshold", "400");
        // bus.setProperty("attachment-memory-threshold", "400");

        //final LoggingFeature loggingFeature = new LoggingFeature();
        //bus.getFeatures().add(loggingFeature);

        final GZIPFeature gzipFeature = new GZIPFeature();
        //gzipFeature.setThreshold(10_000_000);
        bus.getFeatures().add(gzipFeature);

        return bus;
    }

    @Bean(name = BEAN_C3_LOGFILES_WS)
    public CxfEndpoint logfiles() {
        final CxfEndpoint cxfEndpoint = new CxfEndpoint();
        cxfEndpoint.setMtomEnabled(true);
        cxfEndpoint.setBus(cxfBus());
        cxfEndpoint.setAddress(PATH);
        cxfEndpoint.setServiceClass(LogfilesPortType.class);
        cxfEndpoint.setWsdlURL("classpath:schema/logfiles.wsdl");
        return cxfEndpoint;
    }

}
