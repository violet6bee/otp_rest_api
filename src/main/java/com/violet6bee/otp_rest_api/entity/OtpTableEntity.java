package com.violet6bee.otp_rest_api.entity;

import com.violet6bee.otp_rest_api.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "otp_tables")
public class OtpTableEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "otp_table_id_seq")
    @SequenceGenerator(name = "otp_table_id_seq", sequenceName = "otp_table_id_seq", allocationSize = 1)
    private Long id;

    private LocalDateTime createDateTime;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @ManyToOne
    private UserEntity user;

    @Column(name = "operation_uuid", nullable = false)
    private UUID operationUuid;
}
