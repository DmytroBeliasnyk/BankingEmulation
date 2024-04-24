package org.emuba.bankingemulation.services;

import org.emuba.bankingemulation.dto.DataRequestDTO;
import org.emuba.bankingemulation.enums.DataType;
import org.emuba.bankingemulation.models.DataRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DataRequestService {
    void add(String login, DataType dataType, Long transactionId);

    void delete(Long id);

    List<DataRequestDTO> getAll(Pageable pageable);

    DataRequest find(Long id);

    long count();
}
