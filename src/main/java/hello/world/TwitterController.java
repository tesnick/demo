package hello.world;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.core.MainResponse;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import javax.inject.Inject;
import java.io.IOException;

@Controller("/twitter")
public class TwitterController {

    @Inject
    org.elasticsearch.client.RestHighLevelClient highLevelClient;

    @Inject
    org.elasticsearch.client.RestClient client;

    private String INDEX_NAME = "twitter";

    @Get(produces = MediaType.TEXT_PLAIN)
    public String index() {

        try {
            // Health check
            MainResponse response = highLevelClient.info(RequestOptions.DEFAULT);

            // Check if index exists
            GetIndexRequest getIndexRequest = new GetIndexRequest(INDEX_NAME);
            boolean exists = highLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);

            if(!exists) {
                // Create index
                CreateIndexRequest createIndexRequest = new CreateIndexRequest(INDEX_NAME);
                CreateIndexResponse createIndexResponse = highLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
                System.out.println("CREATE INDEX ACK -> " + createIndexResponse.isAcknowledged());

                // Put mappings
                PutMappingRequest putMappingRequest = new PutMappingRequest(INDEX_NAME);
                putMappingRequest.source(
                        "{\n" +
                                " \"properties\" : {\n" +
                                "        \"message\" : {\n" +
                                "          \"type\" : \"text\"\n" +
                                "        },\n" +
                                "        \"postDate\" : {\n" +
                                "          \"type\" : \"date\"\n" +
                                "        },\n" +
                                "        \"user\" : {\n" +
                                "          \"type\" : \"keyword\"\n" +
                                "        }\n" +
                                "      }\n" +
                                "}",
                        XContentType.JSON);
                AcknowledgedResponse putMappingResponse = highLevelClient.indices().putMapping(putMappingRequest, RequestOptions.DEFAULT);
                System.out.println("MAPPING ACK -> " + putMappingResponse.isAcknowledged());
            }


            // Index 1 doc
            IndexRequest indexRequest = new IndexRequest(INDEX_NAME);
            //indexRequest.id("1");
            indexRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
            String jsonString = "{" +
                    "\"user\":\"kimchy\"," +
                    "\"postDate\":\"2013-01-30\"," +
                    "\"message\":\"trying out Elasticsearch\"" +
                    "}";
            indexRequest.source(jsonString, XContentType.JSON);
            IndexResponse indexResponse = highLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            System.out.println("INDEX status -> " + indexResponse.status().getStatus());
            System.out.println("INDEX id -> " + indexResponse.getId());

            // Query
            SearchRequest searchRequest = new SearchRequest(INDEX_NAME);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.matchAllQuery());
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = highLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            return "Elastic version -> " + response.getVersion().getNumber() + " and " + searchResponse.getHits().getHits().length + " hits.";

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Something was wrong";
    }

    @Delete(produces = MediaType.TEXT_PLAIN)
    public HttpResponse<String> deleteIndex(HttpRequest<?> request) {

        try {
            DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(INDEX_NAME);
            AcknowledgedResponse deleteIndexResponse = highLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);

            return HttpResponse.ok("Is aknowledge -> " + deleteIndexResponse.isAcknowledged());

        } catch (IOException e) {
            e.printStackTrace();
            return HttpResponse.serverError(e.getMessage());
        }
    }
}