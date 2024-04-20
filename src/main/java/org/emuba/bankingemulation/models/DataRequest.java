package org.emuba.bankingemulation.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.emuba.bankingemulation.dto.DataRequestDTO;
import org.emuba.bankingemulation.enums.DataType;

@Entity
@Data
@NoArgsConstructor
public class DataRequest {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private CustomClient client;
    private String clientName;
    @Enumerated(EnumType.STRING)
    private DataType dataType;

    private Long transactionId;

    private DataRequest(CustomClient client, DataType dataType, Long transactionId) {
        this.client = client;
        this.dataType = dataType;
        this.transactionId = transactionId;
        clientName = client.getName() + " " + client.getSurname();
    }

    public static DataRequest of(CustomClient client, DataType dataType, Long transactionId) {
        return new DataRequest(client, dataType, transactionId);
    }

    public DataRequestDTO toDTO() {
        return DataRequestDTO.of(id,clientName,dataType);
    }
}
