package org.emuba.bankingemulation.dto;

import lombok.Data;
import org.emuba.bankingemulation.enums.DataType;

@Data
public class DataRequestDTO {
    private Long id;
    private String clientName;
    private DataType dataType;

    private DataRequestDTO(Long id, String clientName, DataType dataType) {
        this.id = id;
        this.clientName = clientName;
        this.dataType = dataType;
    }

    public static DataRequestDTO of(Long id, String clientName, DataType dataType) {
        return new DataRequestDTO(id, clientName, dataType);
    }
}
