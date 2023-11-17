package com.cabapp.pro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cabapp.pro.entity.Complaint;

@Repository
public interface IComplaintRepository  extends JpaRepository<Complaint,Integer> {

    public Complaint findByComplaintId(Integer complaintId);

    public List<Complaint> findByCustomerId(int id);

    public List<Complaint> findByDriverId(int id);

    public List<Complaint> findByStatus(String status);
}
