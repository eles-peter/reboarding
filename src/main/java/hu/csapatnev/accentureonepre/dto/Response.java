package hu.csapatnev.accentureonepre.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Response {
    @JsonProperty("query")
    Query requestData;
    @JsonProperty("result")
    Payload payload;

    public Response(Query requestData, Payload payload) {
        this.requestData = requestData;
        this.payload = payload;
    }

    public Query getRequestData() {
        return requestData;
    }

    public void setRequestData(Query requestData) {
        this.requestData = requestData;
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }
}
