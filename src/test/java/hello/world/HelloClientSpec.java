package hello.world;

import io.micronaut.test.annotation.MicronautTest;
import javax.inject.Inject;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

// Code from https://docs.micronaut.io/latest/guide/index.html
@MicronautTest
public class HelloClientSpec {

    @Inject
    HelloClient client;

    @Test
    public void testHelloWorldResponse(){
        assertEquals("Hello World", client.hello().blockingGet());
    }
}