package com.cabapp.pro.service;

import com.cabapp.pro.entity.Complaint;

import java.util.List;

public interface IComplaintService {
    String createComplaint(Complaint complaint) throws Exception ;
    String updateComplaint(Complaint complaint) throws Exception;
    Complaint getByCompalintId(Integer complaintId) throws Exception;
    List<Complaint> getAllComplaints() throws Exception;
    List<Complaint> complaintByUser(int id) throws Exception;
    List<Complaint> complaintOnDriver(int id) throws Exception;

    List<Complaint> complaintByStatus(String status) throws Exception;
}
