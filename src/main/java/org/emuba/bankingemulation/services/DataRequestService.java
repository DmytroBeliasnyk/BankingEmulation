package org.emuba.bankingemulation.services;

import org.emuba.bankingemulation.dto.DataRequestDTO;
import org.emuba.bankingemulation.enums.DataType;
import org.emuba.bankingemulation.models.DataRequest;

import java.util.List;

public interface DataRequestService {
    void add(String login, DataType dataType, Long transactionId);

    void delete(Long id);

    List<DataRequestDTO> getAll();

    DataRequest find(Long id);
}
