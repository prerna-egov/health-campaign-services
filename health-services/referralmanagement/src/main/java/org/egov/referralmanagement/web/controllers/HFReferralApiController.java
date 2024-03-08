package org.egov.referralmanagement.web.controllers;

import io.swagger.annotations.ApiParam;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.common.models.referralmanagement.hfreferral.HFReferral;
import org.egov.common.models.referralmanagement.hfreferral.HFReferralBulkRequest;
import org.egov.common.models.referralmanagement.hfreferral.HFReferralBulkResponse;
import org.egov.common.models.referralmanagement.hfreferral.HFReferralRequest;
import org.egov.common.models.referralmanagement.hfreferral.HFReferralResponse;
import org.egov.common.models.referralmanagement.hfreferral.HFReferralSearchRequest;
import org.egov.common.producer.Producer;
import org.egov.common.utils.ResponseInfoFactory;
import org.egov.referralmanagement.config.ReferralManagementConfiguration;
import org.egov.referralmanagement.service.HFReferralService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * HF Referral Management Api Controller
 */
@Controller
@RequestMapping("/hf-referral")
@Validated
public class HFReferralApiController {
    private final HttpServletRequest httpServletRequest;

    private final HFReferralService hfReferralService;

    private final Producer producer;

    private final ReferralManagementConfiguration referralManagementConfiguration;

    public HFReferralApiController(
            HttpServletRequest httpServletRequest, 
            HFReferralService hfReferralService, 
            Producer producer,
            ReferralManagementConfiguration referralManagementConfiguration
    ) {
        this.httpServletRequest = httpServletRequest;
        this.hfReferralService = hfReferralService;
        this.producer = producer;
        this.referralManagementConfiguration = referralManagementConfiguration;
    }

    /**
     * @
     * @param request
     * @return
     */
    @RequestMapping(value = "/v1/_create", method = RequestMethod.POST)
    public ResponseEntity<HFReferralResponse> referralV1CreatePost(@ApiParam(value = "Capture details of HFReferral", required = true) @Valid @RequestBody HFReferralRequest request) {

        HFReferral hfReferral = hfReferralService.create(request);
        HFReferralResponse response = HFReferralResponse.builder()
                .hfReferral(hfReferral)
                .responseInfo(ResponseInfoFactory
                        .createResponseInfo(request.getRequestInfo(), true))
                .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }


    /**
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/v1/bulk/_create", method = RequestMethod.POST)
    public ResponseEntity<ResponseInfo> referralBulkV1CreatePost(@ApiParam(value = "Capture details of HFReferral", required = true) @Valid @RequestBody HFReferralBulkRequest request) {
        request.getRequestInfo().setApiId(httpServletRequest.getRequestURI());
        hfReferralService.putInCache(request.getHfReferrals());
        producer.push(referralManagementConfiguration.getCreateHFReferralBulkTopic(), request);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(ResponseInfoFactory
                .createResponseInfo(request.getRequestInfo(), true));
    }

    /**
     *
     * @param request
     * @param limit
     * @param offset
     * @param tenantId
     * @param lastChangedSince
     * @param includeDeleted
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/v1/_search", method = RequestMethod.POST)
    public ResponseEntity<HFReferralBulkResponse> referralV1SearchPost(@ApiParam(value = "HFReferral Search.", required = true) @Valid @RequestBody HFReferralSearchRequest request,
                                                                       @NotNull @Min(0) @Max(1000) @ApiParam(value = "Pagination - limit records in response", required = true) @Valid @RequestParam(value = "limit", required = true) Integer limit,
                                                                       @NotNull @Min(0) @ApiParam(value = "Pagination - offset from which records should be returned in response", required = true) @Valid @RequestParam(value = "offset", required = true) Integer offset,
                                                                       @NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @Valid @RequestParam(value = "tenantId", required = true) String tenantId,
                                                                       @ApiParam(value = "epoch of the time since when the changes on the object should be picked up. Search results from this parameter should include both newly created objects since this time as well as any modified objects since this time. This criterion is included to help polling clients to get the changes in system since a last time they synchronized with the platform. ") @Valid @RequestParam(value = "lastChangedSince", required = false) Long lastChangedSince,
                                                                       @ApiParam(value = "Used in search APIs to specify if (soft) deleted records should be included in search results.", defaultValue = "false") @Valid @RequestParam(value = "includeDeleted", required = false, defaultValue = "false") Boolean includeDeleted) throws Exception {

        List<HFReferral> hfReferrals = hfReferralService.search(request, limit, offset, tenantId, lastChangedSince, includeDeleted);
        HFReferralBulkResponse response = HFReferralBulkResponse.builder().responseInfo(ResponseInfoFactory
                .createResponseInfo(request.getRequestInfo(), true)).hfReferrals(hfReferrals).build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/v1/_update", method = RequestMethod.POST)
    public ResponseEntity<HFReferralResponse> referralV1UpdatePost(@ApiParam(value = "Capture details of Existing HFReferral", required = true) @Valid @RequestBody HFReferralRequest request) {
        HFReferral hfReferral = hfReferralService.update(request);

        HFReferralResponse response = HFReferralResponse.builder()
                .hfReferral(hfReferral)
                .responseInfo(ResponseInfoFactory
                        .createResponseInfo(request.getRequestInfo(), true))
                .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);

    }

    /**
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/v1/bulk/_update", method = RequestMethod.POST)
    public ResponseEntity<ResponseInfo> referralV1BulkUpdatePost(@ApiParam(value = "Capture details of Existing HFReferral", required = true) @Valid @RequestBody HFReferralBulkRequest request) {
        request.getRequestInfo().setApiId(httpServletRequest.getRequestURI());
        producer.push(referralManagementConfiguration.getUpdateHFReferralBulkTopic(), request);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(ResponseInfoFactory
                .createResponseInfo(request.getRequestInfo(), true));
    }

    @RequestMapping(value = "/v1/_delete", method = RequestMethod.POST)
    public ResponseEntity<HFReferralResponse> referralV1DeletePost(@ApiParam(value = "Capture details of Existing HFReferral", required = true) @Valid @RequestBody HFReferralRequest request) {
        HFReferral hfReferral = hfReferralService.delete(request);

        HFReferralResponse response = HFReferralResponse.builder()
                .hfReferral(hfReferral)
                .responseInfo(ResponseInfoFactory
                        .createResponseInfo(request.getRequestInfo(), true))
                .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);

    }

    @RequestMapping(value = "/v1/bulk/_delete", method = RequestMethod.POST)
    public ResponseEntity<ResponseInfo> referralV1BulkDeletePost(@ApiParam(value = "Capture details of Existing HFReferral", required = true) @Valid @RequestBody HFReferralBulkRequest request) {
        request.getRequestInfo().setApiId(httpServletRequest.getRequestURI());
        producer.push(referralManagementConfiguration.getDeleteHFReferralBulkTopic(), request);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(ResponseInfoFactory
                .createResponseInfo(request.getRequestInfo(), true));
    }

}
