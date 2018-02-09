package ch.keller.camel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@SpringBootApplication
public class RunApp {

    public static void main(final String[] args) throws Exception {
        final SpringApplication app = new SpringApplication(RunApp.class);
        app.setWebEnvironment(true);
        app.run(args);
    }

}