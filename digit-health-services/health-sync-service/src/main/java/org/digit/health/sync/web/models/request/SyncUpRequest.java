package org.digit.health.sync.web.models.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.digit.health.sync.web.models.FileDetails;
import org.digit.health.sync.web.models.ReferenceId;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Validated
public class SyncUpRequest {
    @JsonProperty("requestInfo")
    private RequestInfo requestInfo;

    @Valid
    @JsonProperty("fileDetails")
    private FileDetails fileDetails;

    @JsonProperty("referenceId")
    private ReferenceId referenceId;
}