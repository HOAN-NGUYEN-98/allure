package utils.listeners;

import io.restassured.RestAssured;
import io.restassured.config.HeaderConfig;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.RequestSpecification;

public class HttpRequestClientFactory {
    private static final LogHelper log = LogHelper.getInstance();
    private static ThreadLocal<Response> response = new ThreadLocal();
    private static String baseUrl;
    private static ThreadLocal<FilterableRequestSpecification> filterReqSpec = new ThreadLocal();

    public HttpRequestClientFactory() {
    }

    public Response getResponse() {
        return (Response)response.get();
    }

    public void setResponse(Response res) {
        response.set(res);
    }


    public static FilterableRequestSpecification getFilterReqSpec() {
        return (FilterableRequestSpecification)filterReqSpec.get();
    }

    public static void setAllUrl(String url) {
        getFilterReqSpec().given().baseUri(url);
    }

    private static RestAssuredConfig getRestAssuredConfig() {
        return RestAssuredConfig.newConfig().headerConfig(HeaderConfig.headerConfig().overwriteHeadersWithName("access-token", new String[]{"Accept", "Content-Type"})).httpClient(HttpClientConfig.httpClientConfig()).httpClient(HttpClientConfig.httpClientConfig().setParam("http.connection.timeout", 60000).setParam("http.socket.timeout", 60000));
    }


}
