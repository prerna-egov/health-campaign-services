package org.egov.project.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
public class ProjectConfiguration {

    @Value("${project.staff.kafka.create.topic}")
    private String createProjectStaffTopic;

    @Value("${project.staff.kafka.update.topic}")
    private String updateProjectStaffTopic;

    @Value("${project.beneficiary.kafka.create.topic}")
    private String createProjectBeneficiaryTopic;

    @Value("${project.beneficiary.kafka.update.topic}")
    private String updateProjectBeneficiaryTopic;
}