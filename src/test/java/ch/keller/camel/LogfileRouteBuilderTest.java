package ch.keller.camel;

import static org.junit.Assert.assertEquals;

import org.apache.camel.spring.boot.CamelAutoConfiguration;
import org.apache.camel.spring.boot.actuate.endpoint.CamelRoutesEndpointAutoConfiguration;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import ch.keller.xmlns.logfiles.GetLogfileResponseType.Result;
import ch.keller.xmlns.logfiles.GetLogfileType.InputParameter;
import ch.keller.xmlns.logfiles.LogfilesPortType;

@DirtiesContext
@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = WebEnvironment.RANDOM_PORT,
        classes = {
                CamelAutoConfiguration.class,
                CamelRoutesEndpointAutoConfiguration.class,
                RunApp.class
        }
)
public class LogfileRouteBuilderTest {

    @LocalServerPort
    private String port;

    @Test
    public void testInvokingServiceFromClient2() throws Exception {
        final JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(LogfilesPortType.class);
        factory.setAddress("http://localhost:" + port + AppConfiguration.PATH);

        final String filename = "50mb.log";

        final InputParameter inputParameter = new InputParameter();
        inputParameter.setFilename(filename);

        final LogfilesPortType logfilesService = factory.create(LogfilesPortType.class);
        final Result result = logfilesService.getLogfile(inputParameter);
        assertEquals(result.getName(), "50mb.log");
    }

}
