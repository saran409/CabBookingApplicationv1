package com.cabapp.pro.entity;

    import java.time.Instant;
import java.util.Date;
import java.util.Objects;

import javax.persistence.*;

import io.swagger.annotations.ApiModel;
import lombok.*;

    @Entity
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @ApiModel(description = "Details about Complaint Bean")
    public class Complaint {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer complaintId;
        private Instant dateOnRegister;
        private String comment;
        private String status;
        private String email;
        private String customerId;
        private String driverId;
		
    }
