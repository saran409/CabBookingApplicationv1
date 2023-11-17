package com.cabapp.pro.service;

import com.cabapp.pro.dto.EmailDetails;
import com.cabapp.pro.entity.Complaint;
import com.cabapp.pro.repository.IComplaintRepository;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CompalintServiceImpl  implements IComplaintService{
    @Autowired
    private IComplaintMailService mailService;

    @Autowired
    private IComplaintRepository complaintRepository;

    @Override
    public String createComplaint(Complaint complaint) throws Exception {
    	Date date = new Date();
    	

    	complaint.setDateOnRegister(date.toInstant());
    	complaint.setStatus("Pending");
        Complaint complaintResponse =  complaintRepository.save(complaint);
        if (complaintResponse.getComplaintId() != 0) {
            String body = "Dear User,"  + "<br><br>"
                    + "A new Complaint as been registered as per you mentioned, please find the complaint id"
                    + complaint.getComplaintId();
            EmailDetails email = new EmailDetails(complaint.getEmail(), body, "Received Your Complaint ");

            mailService.sendSimpleMail(email);

            return "Successfully Created Complaint with the id : " + complaintResponse.getComplaintId();
        }
        else {
            throw new Exception("complaint not saved");

        }


    }



    @Override
    public String updateComplaint(Complaint complaint) throws Exception {
        
            Complaint complaintResponse = getByCompalintId(complaint.getComplaintId());
            complaintResponse.setComment(complaint.getComment());
            complaintResponse.setStatus(complaint.getStatus());
            if (complaintResponse.getComplaintId() != 0) {
                String body = "Dear User, " + "<br><br>"
                        + "There is an update on the complaint you have raised "
                        + complaint.getComplaintId();
                EmailDetails email = new EmailDetails(complaint.getEmail(), body, "Received Your Complaint ");

                mailService.sendSimpleMail(email);

                return "Successfully  Complaint updated for the id : " + complaintResponse.getComplaintId();
            }
            else {
                throw new Exception("complaint not updated");

            }
       
    }

    @Override
    public Complaint getByCompalintId(Integer complaintId) throws Exception {
        try {
            Complaint complaint = complaintRepository.findByComplaintId(complaintId);
            if (ObjectUtils.isEmpty(complaint)) {
                throw new Exception("complaint id not found");
            }
            return complaint;
        } catch (Exception exception) {
            throw exception;
        }
    }

    @Override
    public List<Complaint> getAllComplaints() throws Exception {
       
            List<Complaint> allCompalints =	complaintRepository.findAll();
            if (ObjectUtils.isEmpty(allCompalints)) {
                throw new Exception("complaints not found");
            }
            return allCompalints;
        } 
    

    @Override
    public List<Complaint> complaintByUser(int id) throws Exception {
       
            List<Complaint> complaintsByUser = complaintRepository.findByCustomerId(id);
            if (ObjectUtils.isEmpty(complaintsByUser)) {
                throw new Exception("complaints not found for user");
            }
            return complaintsByUser;
        
    }

    @Override
    public List<Complaint> complaintOnDriver(int id) throws Exception {
       
            List<Complaint> complaintsForDriver = complaintRepository.findByDriverId(id);
            if (ObjectUtils.isEmpty(complaintsForDriver)) {
                throw new Exception("complaints not found for user");
            }
            return complaintsForDriver;
        } 

    @Override
    public List<Complaint> complaintByStatus(String status) throws Exception {
        try {
            List<Complaint> complaintsByStatus = complaintRepository.findByStatus(status);
            if (ObjectUtils.isEmpty(complaintsByStatus)) {
                throw new Exception("complaints not found for Status");
            }
            return complaintsByStatus;
        } catch (Exception exception) {
            throw exception;
        }
    }



	
}
