package hu.csapatnev.accentureonepre.dto;

import com.fasterxml.jackson.annotation.*;

@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Access.class, name = "access"),
        @JsonSubTypes.Type(value = Status.class, name = "status"),
})
public interface Payload {
}
