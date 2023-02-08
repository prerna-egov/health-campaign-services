package org.egov.stock.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * StockReconciliationRequest
 */
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2023-02-08T11:49:06.320+05:30")

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockReconciliationRequest {
    @JsonProperty("RequestInfo")
    @NotNull
    @Valid
    private org.egov.common.contract.request.RequestInfo requestInfo = null;

    @JsonProperty("StockReconciliation")
    @NotNull
    @Valid
    @Size(min = 1)
    private List<StockReconciliation> stockReconciliation = new ArrayList<>();


    public StockReconciliationRequest addStockReconciliationItem(StockReconciliation stockReconciliationItem) {
        this.stockReconciliation.add(stockReconciliationItem);
        return this;
    }

}
