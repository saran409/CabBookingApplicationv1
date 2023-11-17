package com.cabapp.pro.entity;

 
import java.util.Objects;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
 
 

@Entity
@DiscriminatorValue("admin")
public class Admin extends AbstractUser {
	
	
	 
	private int adminId;


	public Admin() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Admin(int userId, String username, String password, String mobileNumber, String email) {
		super(userId, username, password, mobileNumber, email);
		// TODO Auto-generated constructor stub
	}
	

	public Admin(int adminId) {
		super();
		this.adminId = adminId;
	}

	public int getAdminId() {
		return adminId;
	}

	public void setAdminId(int adminId) {
		this.adminId = adminId;
	}

	@Override
	public String toString() {
		return "Admin [adminId=" + adminId + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(adminId);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Admin other = (Admin) obj;
		return adminId == other.adminId;
	}
	
	

}
