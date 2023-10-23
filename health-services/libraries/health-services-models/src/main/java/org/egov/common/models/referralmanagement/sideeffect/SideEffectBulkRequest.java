package org.egov.common.models.referralmanagement.sideeffect;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.request.RequestInfo;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SideEffectBulkRequest {
    @JsonProperty("RequestInfo")
    @NotNull
    @Valid
    private RequestInfo requestInfo;

    @JsonProperty("SideEffects")
    @NotNull
    @Valid
    @Size(min=1)
    private List<SideEffect> sideEffects = new ArrayList<>();

    public SideEffectBulkRequest addSideEffectItem(SideEffect sideEffectItem) {
        if(Objects.nonNull(sideEffectItem))
            this.sideEffects.add(sideEffectItem);
        return this;
    }

}
