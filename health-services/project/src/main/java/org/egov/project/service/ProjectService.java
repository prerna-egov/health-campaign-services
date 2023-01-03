package org.egov.project.service;

import lombok.extern.slf4j.Slf4j;
import org.egov.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(
            ProjectRepository projectRepository
    ) {
        this.projectRepository = projectRepository;
    }

    public List<String> validateProjectIds(List<String> productIds) {
        return projectRepository.validateProjectIds(productIds);
    }

}