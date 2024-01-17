package org.egov.transformer.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.egov.transformer.Constants;
import org.egov.transformer.config.TransformerProperties;
import org.egov.transformer.service.ProjectService;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.egov.transformer.Constants.HYPHEN;

@Slf4j
@Component
public class CommonUtils {

    private final TransformerProperties properties;
    private final ProjectService projectService;
    private final ObjectMapper objectMapper;
    private static Map<String, List<JsonNode>> boundaryLevelVsLabelCache = new ConcurrentHashMap<>();

    public CommonUtils(TransformerProperties properties, ObjectMapper objectMapper, ProjectService projectService) {
        this.properties = properties;
        this.projectService = projectService;
        this.objectMapper = objectMapper;
    }

    public String getTimeStampFromEpoch(long epochTime) {
        String timeStamp = "";
        String timeZone = properties.getTimeZone();
        try {
            Date date = new Date(epochTime);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            dateFormat.setTimeZone(java.util.TimeZone.getTimeZone(timeZone));
            timeStamp = dateFormat.format(date);
        } catch (Exception e) {
            log.error("EpochTime to be transformed :" + epochTime);
            log.error("Exception while transforming epochTime to timestamp: {}", ExceptionUtils.getStackTrace(e));
        }
        return timeStamp;
    }

    public List<Double> getGeoPoint(Object address) {
        if (address == null) {
            return null;
        }
        try {
            Class<?> addressClass = address.getClass();
            Method getLongitudeMethod = addressClass.getMethod("getLongitude");
            Method getLatitudeMethod = addressClass.getMethod("getLatitude");

            Double longitude = (Double) getLongitudeMethod.invoke(address);
            Double latitude = (Double) getLatitudeMethod.invoke(address);

            if (longitude == null || latitude == null) {
                return null;
            }
            List<Double> geoPoint = new ArrayList<>();
            geoPoint.add(longitude);
            geoPoint.add(latitude);
            return geoPoint;

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("ERROR_IN_GEO_POINT_EXTRACTION : " + e);
            return null;
        }
    }

    public Integer calculateAgeInMonthsFromDOB(Date dob) {
        Duration difference = Duration.between(dob.toInstant(), new Date().toInstant());
        long totalDays = difference.toDays();
        return (int) (totalDays / 30.42);
    }

    public JsonNode getBoundaryHierarchy(String tenantId, String projectTypeId, Map<String, String> boundaryLabelToNameMap) {
        List<JsonNode> boundaryLevelVsLabel = null;
        ObjectNode boundaryHierarchy = objectMapper.createObjectNode();
        try {
            if (projectTypeId != null) {
                String cacheKey = tenantId + HYPHEN + projectTypeId;
                if (boundaryLevelVsLabelCache.containsKey(cacheKey)) {
                    boundaryLevelVsLabel = boundaryLevelVsLabelCache.get(tenantId + "-" + projectTypeId);
                    log.info("Fetching boundaryLevelVsLabel from cache for projectTypeId: {}", projectTypeId);
                } else {
                    JsonNode mdmsBoundaryData = projectService.fetchBoundaryData(tenantId, null, projectTypeId);
                    boundaryLevelVsLabel = StreamSupport
                            .stream(mdmsBoundaryData.get(Constants.BOUNDARY_HIERARCHY).spliterator(), false).collect(Collectors.toList());
                    boundaryLevelVsLabelCache.put(cacheKey, boundaryLevelVsLabel);
                }
            } else {
                boundaryLevelVsLabel = loadPredefinedBoundaryLevels();
            }
        } catch (Exception e) {
            log.error("Error while fetching boundaryHierarchy for projectTypeId: {}", projectTypeId);
            log.info("RETURNING BOUNDARY_LABEL_TO_NAME_MAP as BOUNDARY_HIERARCHY: {}", boundaryLabelToNameMap.toString());
            boundaryLevelVsLabel = loadPredefinedBoundaryLevels();
        }
        boundaryLevelVsLabel.forEach(node -> {
            if (node.get(Constants.LEVEL).asInt() > 1) {
                boundaryHierarchy.put(node.get(Constants.INDEX_LABEL).asText(), boundaryLabelToNameMap.get(node.get(Constants.LABEL).asText()) == null ? null : boundaryLabelToNameMap.get(node.get(Constants.LABEL).asText()));
            }
        });
        return boundaryHierarchy;
    }

    private List<JsonNode> loadPredefinedBoundaryLevels() {
        String jsonData = "[\n" +
                "  {\n" +
                "    \"level\": 0,\n" +
                "    \"label\": null,\n" +
                "    \"indexLabel\": null\n" +
                "  },\n" +
                "  {\n" +
                "    \"level\": 1,\n" +
                "    \"label\": \"Country\",\n" +
                "    \"indexLabel\": \"country\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"level\": 2,\n" +
                "    \"label\": \"Provincia\",\n" +
                "    \"indexLabel\": \"province\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"level\": 3,\n" +
                "    \"label\": \"Distrito\",\n" +
                "    \"indexLabel\": \"district\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"level\": 4,\n" +
                "    \"label\": \"Posto Administrativo\",\n" +
                "    \"indexLabel\": \"administrativeProvince\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"level\": 5,\n" +
                "    \"label\": \"Localidade\",\n" +
                "    \"indexLabel\": \"locality\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"level\": 6,\n" +
                "    \"label\": \"Aldeia\",\n" +
                "    \"indexLabel\": \"village\"\n" +
                "  }\n" +
                "]";

        List<JsonNode> boundaryLevelVsLabel = null;

        try {
            boundaryLevelVsLabel = objectMapper.readValue(jsonData, objectMapper.getTypeFactory().constructCollectionType(List.class, JsonNode.class));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return boundaryLevelVsLabel;
    }


}
