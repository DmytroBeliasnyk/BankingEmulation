package org.emuba.bankingemulation.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.emuba.bankingemulation.dto.ClientRequestDTO;
import org.emuba.bankingemulation.enums.ClientRequestType;

@Data
@NoArgsConstructor
@Entity
public class ClientRequest {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private CustomClient client;
    private String clientName;
    @Enumerated(EnumType.STRING)
    private ClientRequestType clientRequestType;

    private Long transactionId;

    private ClientRequest(CustomClient client, ClientRequestType clientRequestType, Long transactionId) {
        this.client = client;
        this.clientRequestType = clientRequestType;
        this.transactionId = transactionId;
        clientName = client.getName() + " " + client.getSurname();
    }

    public static ClientRequest of(CustomClient client, ClientRequestType clientRequestType, Long transactionId) {
        return new ClientRequest(client, clientRequestType, transactionId);
    }

    public ClientRequestDTO toDTO() {
        return ClientRequestDTO.of(id, clientName, clientRequestType);
    }
}


