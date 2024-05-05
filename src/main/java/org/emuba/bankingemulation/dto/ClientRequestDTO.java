package org.emuba.bankingemulation.dto;

import lombok.Data;
import org.emuba.bankingemulation.enums.ClientRequestType;

@Data
public class ClientRequestDTO {
    private Long id;
    private String clientName;
    private ClientRequestType clientRequestType;

    private ClientRequestDTO(Long id, String clientName, ClientRequestType clientRequestType) {
        this.id = id;
        this.clientName = clientName;
        this.clientRequestType = clientRequestType;
    }

    public static ClientRequestDTO of(Long id, String clientName, ClientRequestType clientRequestType) {
        return new ClientRequestDTO(id, clientName, clientRequestType);
    }
}
