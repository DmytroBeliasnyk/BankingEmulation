package org.emuba.bankingemulation.repositories;

import org.emuba.bankingemulation.models.DataRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataRepository extends JpaRepository<DataRequest, Long> {
}
