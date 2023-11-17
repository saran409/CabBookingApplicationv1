package com.cabapp.pro.dto;

 
 
import lombok.Data;

import java.time.Instant;

 

@Data
public class ComplaintRequestDto {
	
	private Integer complaintId;
    private Instant dateOnRegister;
    private String comment;
    private String status;
    private String email;
    private String customerId;
    private String driverId;
   
}
