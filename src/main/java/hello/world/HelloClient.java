package hello.world;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import io.reactivex.Single;

// Code from https://docs.micronaut.io/latest/guide/index.html
@Client("/hello")
public interface HelloClient {

    @Get(consumes = MediaType.TEXT_PLAIN)
    Single<String> hello();
}