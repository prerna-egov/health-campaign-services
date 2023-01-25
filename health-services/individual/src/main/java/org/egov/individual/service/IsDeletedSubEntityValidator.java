package org.egov.individual.service;

import org.egov.common.models.Error;
import org.egov.common.utils.Validator;
import org.egov.individual.web.models.Address;
import org.egov.individual.web.models.Identifier;
import org.egov.individual.web.models.Individual;
import org.egov.individual.web.models.IndividualBulkRequest;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.egov.common.utils.CommonUtils.populateErrorDetails;
import static org.egov.common.utils.ValidatorUtils.getErrorForIsDeleteSubEntity;

@Component
@Order(2)
public class IsDeletedSubEntityValidator  implements Validator<IndividualBulkRequest, Individual> {

    @Override
    public Map<Individual, List<Error>> validate(IndividualBulkRequest request) {
        HashMap<Individual, List<Error>> errorDetailsMap = new HashMap<>();
        List<Individual> validIndividuals = request.getIndividuals();
        for (Individual individual : validIndividuals) {
            individual.getIdentifiers().stream().filter(Identifier::getIsDeleted)
                    .forEach(identifier -> {
                        Error error = getErrorForIsDeleteSubEntity();
                        populateErrorDetails(individual, error, errorDetailsMap);
                    });
            individual.getAddress().stream().filter(Address::getIsDeleted)
                    .forEach(address -> {
                        Error error = getErrorForIsDeleteSubEntity();
                        populateErrorDetails(individual, error, errorDetailsMap);
                    });
        }
        return errorDetailsMap;
    }
}