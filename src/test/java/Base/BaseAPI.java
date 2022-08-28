package Base;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.http.Headers;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath.CompatibilityMode;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import utils.listeners.*;


public class BaseAPI extends HttpRequest {
   public BaseAPI(){}

    public Response send(RequestMethod requestMethod, RequestSpecification spec, String url, String payload) {
        Response res = null;
        if (requestMethod.equals(RequestMethod.GET)) {
            res = (Response)((RequestSpecification)RestAssured.given().spec(spec).log().ifValidationFails()).get(url, new Object[0]);
        }

        if (requestMethod.equals(RequestMethod.POST)) {
            res = (Response)((RequestSpecification)RestAssured.given().spec(spec).log().ifValidationFails()).body(payload).post(url, new Object[0]);
        }

        return res;
    }

    public BaseAPI validateResponse(int statusCode) {
        ((ValidatableResponse)((ValidatableResponse)((ValidatableResponse)((ValidatableResponse)((ValidatableResponse)this.getResponse().then()).log().ifValidationFails()).statusCode(statusCode)).and()).body("", Matchers.allOf(Matchers.notNullValue(), Matchers.not("")), new Object[0])).extract().response();
        return this;
    }

    public BaseAPI validateStatusCode(int statusCode) {
        ((ValidatableResponse)((ValidatableResponse)this.getResponse().then()).log().ifValidationFails()).statusCode(statusCode);
        return this;
    }

    public String getJsonAsString() {
        return this.getResponse().body().asString();
    }

    public <T> T getJsonAsObject(Class<T> cls) {
        return this.getResponse().body().as(cls, ObjectMapperType.GSON);
    }

    public JsonPath getJsonPath(Response r) {
        String json = r.asString();
        return new JsonPath(json);
    }

    public JsonArray getJsonArray(Response r) {
        String json = r.asString();
        return (new JsonParser()).parse(json).getAsJsonArray();
    }

    public JsonObject getJsonObject(Response r) {
        String json = r.asString();
        return (new JsonParser()).parse(json).getAsJsonObject();
    }

    public JsonElement getJsonElement(String locator) {
        JsonObject object = this.getJsonObject(this.getResponse());
        return object.get(locator);
    }

    public String getHtmlValueByPath(String htmlPath) {
        String value = (String)this.getResponse().xmlPath(CompatibilityMode.HTML).get(htmlPath);
        return value;
    }

    public <T> T getJsonValueByGPath(String gpathLocator) {
            T result = this.getResponse().path(gpathLocator, new String[0]);
            return result;
    }

    public String getJsonValue(String jsonLocator) {
            String jsonValue = this.getJsonPath(this.getResponse()).get(jsonLocator).toString();
            return jsonValue;

    }

    public int getJsonArraySize(String jsonLocator) {
        JsonPath jsonValue = this.getJsonPath(this.getResponse());
        return jsonValue.getList(jsonLocator).size();
    }

    public void setHeader(String headerName, Object headerValue) {
        HttpRequestClientFactory.getFilterReqSpec().header(headerName, headerValue, new Object[0]);
    }

    public void setContentType(ContentType contentType) {
        HttpRequestClientFactory.getFilterReqSpec().contentType(contentType);
    }

    public void removeHeaders() {
        HttpRequestClientFactory.getFilterReqSpec().removeHeaders();
    }

    public Cookies getCookies() {
        return this.getResponse().getDetailedCookies();
    }

    public void removeHeader(String headerName) {
            HttpRequestClientFactory.getFilterReqSpec().removeHeader(headerName);
    }

    public String getHeader(String headerName) {
        return this.getResponse().header(headerName);
    }

    public void setHeaders(Headers obj) {
        HttpRequestClientFactory.getFilterReqSpec().headers(obj);
    }

    public Headers getHeaders() {
        return HttpRequestClientFactory.getFilterReqSpec().getHeaders();
    }

    public <T> Object saveResponseObject(Class<T> c) {
        try {
            return ObjectMappingUtils.parseJsonToModel(this.getJsonAsString(), c);
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public <T> Object returnObjectIfSuccess(Class<T> c) {
        try {
            if (this.getResponse().statusCode() == 200) {
                return ObjectMappingUtils.parseJsonToModel(this.getJsonAsString(), c);
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return null;
    }
}

