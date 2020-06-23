package hu.csapatnev.accentureonepre.dto;

import com.fasterxml.jackson.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Access.class, name = "access"),
        @JsonSubTypes.Type(value = Status.class, name = "status"),
})
@Schema(oneOf = {Access.class, Status.class})
public interface Payload {
}
