package utils.listeners;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

public class HttpRequest extends HttpRequestClientFactory {
    private String pathParam;
    private String pathParamValue;
    private String url;
    private Map<String, String> pathParameters;

    public HttpRequest() {
    }

    private static String GetMimeTypes(File file) {
        String var1 = FilenameUtils.getExtension(file.getName());
        byte var2 = -1;
        switch(var1.hashCode()) {
            case 3124:
                if (var1.equals("au")) {
                    var2 = 3;
                }
                break;
            case 98822:
                if (var1.equals("csv")) {
                    var2 = 4;
                }
                break;
            case 105441:
                if (var1.equals("jpg")) {
                    var2 = 0;
                }
                break;
            case 111145:
                if (var1.equals("png")) {
                    var2 = 1;
                }
                break;
            case 117484:
                if (var1.equals("wav")) {
                    var2 = 2;
                }
        }

        switch(var2) {
            case 0:
            case 1:
                return "image/png";
            case 2:
            case 3:
                return "audio/mpeg";
            case 4:
                return "text/csv";
            default:
                return null;
        }
    }

    public static void setBaseUrl(String url) {
        setAllUrl(url);
    }

    private RequestSpecification getCurrentReqSpec() {
        return HttpRequestClientFactory.getFilterReqSpec();
    }

    public Response sendGet(String url) {
        RequestSpecification httpRequest = (new HttpRequest.HttpRequestBuilder()).create();
        return this.doGet(url, httpRequest);
    }

    public Response sendGet(String url, String pathParamName, Object pathParamValue) {
        RequestSpecification httpRequest = (new HttpRequest.HttpRequestBuilder(pathParamName, pathParamValue)).create();
        return this.doGet(url, httpRequest);
    }

    public Response sendGet(String url, Map<String, Object> params) {
        RequestSpecification httpRequest = (new HttpRequest.HttpRequestBuilder(params)).create();
        return this.doGet(url, httpRequest);
    }

    public Response sendGetWithPathParams(String url, Map<String, String> pathParameters) {
        RequestSpecification httpRequest = (new HttpRequest.HttpRequestBuilder()).create().pathParams(pathParameters);
        return this.doGet(url, httpRequest);
    }

    public Response sendGetWithPathParams(String url, Map<String, String> pathParameters, Map<String, Object> params) {
        RequestSpecification httpRequest = (new HttpRequest.HttpRequestBuilder(params)).create().pathParams(pathParameters);
        return this.doGet(url, httpRequest);
    }

    public Response sendGet(String url, String pathParam, String pathParamValue) {
        RequestSpecification httpRequest = (new HttpRequest.HttpRequestBuilder(pathParam, pathParamValue)).create();
        return this.doGet(url, httpRequest);
    }

    public Response sendGet(String url, Map<String, Object> params, String pathParamName, Object pathParamValue) {
        RequestSpecification httpRequest = (new HttpRequest.HttpRequestBuilder(params, pathParamName, pathParamValue)).create();
        return this.doGet(url, httpRequest);
    }

    public Response sendGet(String url, Map<String, Object> params, Map<String, String> pathParameters) {
        RequestSpecification httpRequest = (new HttpRequest.HttpRequestBuilder(params, pathParameters)).create();
        return this.doGet(url, httpRequest);
    }

    public Response sendPost(String url, Object body) {
        RequestSpecification httpRequest = (new HttpRequest.HttpRequestBuilder(body)).create();
        return this.doPost(url, httpRequest);
    }

    public Response sendPost(String url, String body) {
        RequestSpecification httpRequest = (new HttpRequest.HttpRequestBuilder(body)).create();
        return this.doPost(url, httpRequest);
    }

    public Response sendPost(String url, String body, String pathParamName, Object pathParamValue) {
        RequestSpecification httpRequest = (new HttpRequest.HttpRequestBuilder(body, pathParamName, pathParamValue)).create();
        return this.doPost(url, httpRequest);
    }

    public Response sendPost(String url, Object body, String pathParamName, Object pathParamValue) {
        RequestSpecification httpRequest = (new HttpRequest.HttpRequestBuilder(body, pathParamName, pathParamValue)).create();
        return this.doPost(url, httpRequest);
    }

    public Response sendPost(String url, Object body, Map<String, String> pathParameters) {
        RequestSpecification httpRequest = (new HttpRequest.HttpRequestBuilder(body)).create().pathParams(pathParameters);
        return this.doPost(url, httpRequest);
    }

    public Response sendPostWithParams(String url, Object body, Map<String, Object> params) {
        RequestSpecification httpRequest = (new HttpRequest.HttpRequestBuilder(body)).create().queryParams(params);
        return this.doPost(url, httpRequest);
    }

    public Response sendPostWithParams(String url, Object body, String pathParamName, Object pathParamValue, Map<String, Object> params) {
        RequestSpecification httpRequest = (new HttpRequest.HttpRequestBuilder(body, pathParamName, pathParamValue)).create().queryParams(params);
        return this.doPost(url, httpRequest);
    }

    public Response sendPost(String url, Map<String, File> formData, String pathParamName, Object pathParamValue) {
        RequestSpecification req = RestAssured.given();
        Iterator var6 = formData.entrySet().iterator();

        while(var6.hasNext()) {
            Map.Entry<String, File> entry = (Map.Entry)var6.next();
            req.multiPart((String)entry.getKey(), (File)entry.getValue(), GetMimeTypes((File)entry.getValue()));
        }

        return (Response)((ValidatableResponse)((ValidatableResponse)((Response)req.spec(this.getCurrentReqSpec()).header("Content-Type", "multipart/form-data", new Object[0]).pathParam(pathParamName, pathParamValue).when().post(url, new Object[0])).then()).log().ifError()).extract().response();
    }

    public Response sendPost(String url, Map<String, File> formData) {
        RequestSpecification req = RestAssured.given();
        Iterator var4 = formData.entrySet().iterator();

        while(var4.hasNext()) {
            Map.Entry<String, File> entry = (Map.Entry)var4.next();
            req.multiPart((String)entry.getKey(), (File)entry.getValue(), GetMimeTypes((File)entry.getValue()));
        }

        return (Response)((ValidatableResponse)((ValidatableResponse)((Response)req.spec(this.getCurrentReqSpec()).header("Content-Type", "multipart/form-data", new Object[0]).when().post(url, new Object[0])).then()).log().ifError()).extract().response();
    }

    public Response sendPostImplicit(String url, Map<String, Object> formData) {
        RequestSpecification req = RestAssured.given();
        Iterator var4 = formData.entrySet().iterator();

        while(var4.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry)var4.next();
            if (entry.getValue() instanceof File) {
                req.multiPart((String)entry.getKey(), (File)entry.getValue(), GetMimeTypes((File)entry.getValue()));
            } else {
                req.multiPart((String)entry.getKey(), String.valueOf(entry.getValue()));
            }
        }

        Response res = (Response)((ValidatableResponse)((ValidatableResponse)((Response)req.spec(this.getCurrentReqSpec()).header("Content-Type", "multipart/form-data", new Object[0]).when().post(url, new Object[0])).then()).log().ifError()).extract().response();
        this.setResponse(res);
        return res;
    }

    public Response sendPostWithPathParams(String url, Map<String, String> pathParameters) {
        RequestSpecification httpRequest = (new HttpRequest.HttpRequestBuilder()).create().pathParams(pathParameters);
        return this.doPost(url, httpRequest);
    }

    public Response sendPost(String url) {
        RequestSpecification httpRequest = (new HttpRequest.HttpRequestBuilder()).create();
        return this.doPost(url, httpRequest);
    }

    public Response sendPut(String url) {
        RequestSpecification httpRequest = (new HttpRequest.HttpRequestBuilder()).create();
        return this.doPut(url, httpRequest);
    }

    public Response sendPut(String url, Object body) {
        RequestSpecification httpRequest = (new HttpRequest.HttpRequestBuilder(body)).create();
        return this.doPut(url, httpRequest);
    }

    public Response sendPut(String url, Map<String, Object> body, String pathParamName, Object pathParamValue) {
        RequestSpecification httpRequest = (new HttpRequest.HttpRequestBuilder(pathParamName, pathParamValue)).create().body(body);
        return this.doPut(url, httpRequest);
    }

    public Response sendPut(String url, Object body, String pathParamName, Object pathParamValue) {
        RequestSpecification httpRequest = (new HttpRequest.HttpRequestBuilder(body, pathParamName, pathParamValue)).create();
        return this.doPut(url, httpRequest);
    }

    public Response sendPut(String url, Object body, Map<String, String> pathParameters) {
        RequestSpecification httpRequest = (new HttpRequest.HttpRequestBuilder(body)).create().pathParams(pathParameters);
        return this.doPut(url, httpRequest);
    }

    public Response sendPutWithPathParams(String url, Map<String, String> pathParameters) {
        RequestSpecification httpRequest = (new HttpRequest.HttpRequestBuilder()).create().pathParams(pathParameters);
        return this.doPut(url, httpRequest);
    }

    public Response sendPut(String url, Object body, String pathParamName, Object pathParamValue, Map<String, Object> params) {
        RequestSpecification httpRequest = (new HttpRequest.HttpRequestBuilder(body, pathParamName, pathParamValue, params)).create();
        return this.doPut(url, httpRequest);
    }

    public Response sendPatch(String url) {
        RequestSpecification httpRequest = (new HttpRequest.HttpRequestBuilder()).create();
        return this.doPatch(url, httpRequest);
    }

    public Response sendPatch(String url, Object body) {
        RequestSpecification httpRequest = (new HttpRequest.HttpRequestBuilder(body)).create();
        return this.doPatch(url, httpRequest);
    }

    public Response sendPatch(String url, Map<String, Object> body, String pathParamName, Object pathParamValue) {
        RequestSpecification httpRequest = (new HttpRequest.HttpRequestBuilder(pathParamName, pathParamValue)).create().body(body);
        return this.doPatch(url, httpRequest);
    }

    public Response sendPatch(String url, Object body, String pathParamName, Object pathParamValue) {
        RequestSpecification httpRequest = (new HttpRequest.HttpRequestBuilder(body, pathParamName, pathParamValue)).create();
        return this.doPatch(url, httpRequest);
    }

    public Response sendPatch(String url, Object body, Map<String, String> pathParameters) {
        RequestSpecification httpRequest = (new HttpRequest.HttpRequestBuilder(body)).create().pathParams(pathParameters);
        return this.doPatch(url, httpRequest);
    }

    public Response sendPatch(String url, Object body, String pathParamName, Object pathParamValue, Map<String, Object> params) {
        RequestSpecification httpRequest = (new HttpRequest.HttpRequestBuilder(body, pathParamName, pathParamValue, params)).create();
        return this.doPatch(url, httpRequest);
    }

    public Response sendPatch(String url, Object body, Map<String, String> pathParameters, Map<String, Object> params) {
        RequestSpecification httpRequest = (new HttpRequest.HttpRequestBuilder(body, pathParameters, params)).create();
        return this.doPatch(url, httpRequest);
    }

    public Response sendDelete(String url) {
        RequestSpecification httpRequest = (new HttpRequest.HttpRequestBuilder()).create();
        return this.doDelete(url, httpRequest);
    }

    public Response sendDelete(String url, String pathParamName, Object pathParamValue) {
        RequestSpecification httpRequest = (new HttpRequest.HttpRequestBuilder(pathParamName, pathParamValue)).create();
        return this.doDelete(url, httpRequest);
    }

    public Response sendDelete(String url, Map<String, String> pathParameters) {
        RequestSpecification httpRequest = (new HttpRequest.HttpRequestBuilder()).create().pathParams(pathParameters);
        return this.doDelete(url, httpRequest);
    }

    public Response sendDelete(String url, String pathParamName, Object pathParamValue, Map<String, Object> params) {
        RequestSpecification httpRequest = (new HttpRequest.HttpRequestBuilder(pathParamName, pathParamValue)).create().params(params);
        return this.doDelete(url, httpRequest);
    }

    public Response sendDelete(String url, Map<String, String> pathParameters, Map<String, Object> params) {
        RequestSpecification httpRequest = (new HttpRequest.HttpRequestBuilder(params)).create().pathParams(pathParameters);
        return this.doDelete(url, httpRequest);
    }

    public Response sendDelete(String url, Object body, Map<String, String> pathParameters) {
        RequestSpecification httpRequest = (new HttpRequest.HttpRequestBuilder(body)).create().pathParams(pathParameters);
        return this.doDelete(url, httpRequest);
    }

    public Response sendDelete(String url, String body, Map<String, Object> params) {
        RequestSpecification httpRequest = (new HttpRequest.HttpRequestBuilder(body)).create().params(params);
        return this.doDelete(url, httpRequest);
    }

    @Step("SEND POST TO URL : {0}")
    private Response doPost(String url, RequestSpecification requestSpecification) {
        Response res = (Response)((ValidatableResponse)((ValidatableResponse)((Response)requestSpecification.contentType(ContentType.JSON).when().post(url, new Object[0])).then()).log().ifValidationFails()).extract().response();
        this.setResponse(res);
        return res;
    }

    @Step("SEND PUT TO URL : {0}")
    private Response doPut(String url, RequestSpecification requestSpecification) {
        Response res = (Response)((ValidatableResponse)((ValidatableResponse)((Response)requestSpecification.contentType(ContentType.JSON).when().put(url, new Object[0])).then()).log().ifValidationFails()).extract().response();
        this.setResponse(res);
        return res;
    }

    @Step("SEND PATCH TO URL : {0}")
    private Response doPatch(String url, RequestSpecification requestSpecification) {
        Response res = (Response)((ValidatableResponse)((ValidatableResponse)((Response)requestSpecification.contentType(ContentType.JSON).when().patch(url, new Object[0])).then()).log().ifValidationFails()).extract().response();
        this.setResponse(res);
        return res;
    }

    @Step("SEND GET TO URL : {0}")
    private Response doGet(String url, RequestSpecification requestSpecification) {
        Response res = (Response)((ValidatableResponse)((ValidatableResponse)((Response)requestSpecification.contentType(ContentType.JSON).when().get(url, new Object[0])).then()).log().ifValidationFails()).extract().response();
        this.setResponse(res);
        return res;
    }

    @Step("SEND DELETE TO URL : {0}")
    private Response doDelete(String url, RequestSpecification requestSpecification) {
        Response res = (Response)((ValidatableResponse)((ValidatableResponse)((Response)requestSpecification.contentType(ContentType.JSON).when().delete(url, new Object[0])).then()).log().ifValidationFails()).extract().response();
        this.setResponse(res);
        return res;
    }

    public void setPathParam(String pathParam) {
        this.pathParam = pathParam;
    }

    public void setPathParamValue(String pathParamValue) {
        this.pathParamValue = pathParamValue;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private class HttpRequestBuilder {
        private String pathParamName;
        private String pathParamValueString;
        private Object pathParamValue;
        private Map<String, Object> params;
        private Map<String, String> pathParameters;
        private Object body;
        private RequestSpecification requestSpecification;

        private HttpRequestBuilder(String pathParamName, Object pathParamValue) {
            this.pathParamName = null;
            this.pathParamValueString = null;
            this.pathParamValue = null;
            this.params = null;
            this.pathParameters = null;
            this.body = null;
            this.requestSpecification = RestAssured.given().spec(HttpRequestClientFactory.getFilterReqSpec());
            this.pathParamName = pathParamName;
            this.pathParamValue = pathParamValue;
        }

        private HttpRequestBuilder(Map<String, Object> params) {
            this.pathParamName = null;
            this.pathParamValueString = null;
            this.pathParamValue = null;
            this.params = null;
            this.pathParameters = null;
            this.body = null;
            this.requestSpecification = RestAssured.given().spec(HttpRequestClientFactory.getFilterReqSpec());
            this.params = params;
        }

        private HttpRequestBuilder(Object body) {
            this.pathParamName = null;
            this.pathParamValueString = null;
            this.pathParamValue = null;
            this.params = null;
            this.pathParameters = null;
            this.body = null;
            this.requestSpecification = RestAssured.given().spec(HttpRequestClientFactory.getFilterReqSpec());
            this.body = body;
        }

        private HttpRequestBuilder(String pathParam, String pathParamValue) {
            this.pathParamName = null;
            this.pathParamValueString = null;
            this.pathParamValue = null;
            this.params = null;
            this.pathParameters = null;
            this.body = null;
            this.requestSpecification = RestAssured.given().spec(HttpRequestClientFactory.getFilterReqSpec());
            this.pathParamName = pathParam;
            this.pathParamValueString = pathParamValue;
        }

        private HttpRequestBuilder(Map<String, Object> params, String pathParamName, Object pathParamValue) {
            this.pathParamName = null;
            this.pathParamValueString = null;
            this.pathParamValue = null;
            this.params = null;
            this.pathParameters = null;
            this.body = null;
            this.requestSpecification = RestAssured.given().spec(HttpRequestClientFactory.getFilterReqSpec());
            this.params = params;
            this.pathParamName = pathParamName;
            this.pathParamValue = pathParamValue;
        }

        private HttpRequestBuilder(Object body, String pathParamName, Object pathParamValue) {
            this.pathParamName = null;
            this.pathParamValueString = null;
            this.pathParamValue = null;
            this.params = null;
            this.pathParameters = null;
            this.body = null;
            this.requestSpecification = RestAssured.given().spec(HttpRequestClientFactory.getFilterReqSpec());
            this.body = body;
            this.pathParamName = pathParamName;
            this.pathParamValue = pathParamValue;
        }

        private HttpRequestBuilder(Map<String, Object> params, Map<String, String> pathParameters) {
            this.pathParamName = null;
            this.pathParamValueString = null;
            this.pathParamValue = null;
            this.params = null;
            this.pathParameters = null;
            this.body = null;
            this.requestSpecification = RestAssured.given().spec(HttpRequestClientFactory.getFilterReqSpec());
            this.params = params;
            this.pathParameters = pathParameters;
        }

        public HttpRequestBuilder() {
            this.pathParamName = null;
            this.pathParamValueString = null;
            this.pathParamValue = null;
            this.params = null;
            this.pathParameters = null;
            this.body = null;
            this.requestSpecification = RestAssured.given().spec(HttpRequestClientFactory.getFilterReqSpec());
        }

        public HttpRequestBuilder(Object body, String pathParamName, Object pathParamValue, Map<String, Object> params) {
            this.pathParamName = null;
            this.pathParamValueString = null;
            this.pathParamValue = null;
            this.params = null;
            this.pathParameters = null;
            this.body = null;
            this.requestSpecification = RestAssured.given().spec(HttpRequestClientFactory.getFilterReqSpec());
            this.body = body;
            this.pathParamName = pathParamName;
            this.pathParamValue = pathParamValue;
            this.params = params;
        }

        public HttpRequestBuilder(Object body, Map<String, String> pathParams, Map<String, Object> params) {
            this.pathParamName = null;
            this.pathParamValueString = null;
            this.pathParamValue = null;
            this.params = null;
            this.pathParameters = null;
            this.body = null;
            this.requestSpecification = RestAssured.given().spec(HttpRequestClientFactory.getFilterReqSpec());
            this.body = body;
            this.pathParameters = pathParams;
            this.params = params;
        }

        public RequestSpecification create() {
            if (this.pathParamValue != null) {
                this.requestSpecification.pathParam(this.pathParamName, this.pathParamValue);
            }

            if (this.pathParamValueString != null) {
                this.requestSpecification.pathParam(this.pathParamName, this.pathParamValueString);
            }

            if (this.params != null) {
                this.requestSpecification.params(this.params);
            }

            if (this.body != null) {
                this.requestSpecification.body(this.body);
            }

            if (this.pathParameters != null) {
                this.requestSpecification.pathParams(this.pathParameters);
            }

            return this.requestSpecification;
        }
    }
}

