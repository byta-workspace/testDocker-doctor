 /*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bytatech.ayoos.service.dto;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.bytatech.ayoos.domain.ContactInfo;
import com.bytatech.ayoos.domain.DoctorSettings;
import com.bytatech.ayoos.domain.PaymentSettings;
import com.bytatech.ayoos.domain.Qualification;
import com.bytatech.ayoos.domain.ReservedSlot;
import com.bytatech.ayoos.domain.Review;
import com.bytatech.ayoos.domain.UserRating;
import com.bytatech.ayoos.domain.WorkPlace;

/**
 * TODO Provide a detailed description here 
 * @author MayaSanjeev
 * mayabytatech, maya.k.k@lxisoft.com
 */
public class DoctorAggregateDTO {
	
	    private Long id;

	    @Lob
	    private byte[] image;


	    private String imageContentType;


	    private String doctorId;


	    private String specialization;


	    private String registerNumber;


	    private LocalDate practiceSince;

	  
	    private Double totalRating;

	    private String firstName;

	    private String email;

	    private Long phoneNumber;

	
	    private ContactInfo contactInfo;

	
	    private PaymentSettings paymentSettings;

	    private DoctorSettings doctorSettings;

	
	    /*private List<WorkPlace> workPlaces =  new ArrayList<>();

	    private List<Qualification> qualifications = new ArrayList<>();
	  
	    private List<Review> reviews =new ArrayList<>();

	    private List<UserRating> userRatings = new ArrayList<>();
	  
	    private List<ReservedSlot> reservedSlots =new ArrayList<>();*/
		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public byte[] getImage() {
			return image;
		}

		public void setImage(byte[] image) {
			this.image = image;
		}

		public String getImageContentType() {
			return imageContentType;
		}

		public void setImageContentType(String imageContentType) {
			this.imageContentType = imageContentType;
		}

		public String getDoctorId() {
			return doctorId;
		}

		public void setDoctorId(String doctorId) {
			this.doctorId = doctorId;
		}

		public String getSpecialization() {
			return specialization;
		}

		public void setSpecialization(String specialization) {
			this.specialization = specialization;
		}

		public String getRegisterNumber() {
			return registerNumber;
		}

		public void setRegisterNumber(String registerNumber) {
			this.registerNumber = registerNumber;
		}

		public LocalDate getPracticeSince() {
			return practiceSince;
		}

		public void setPracticeSince(LocalDate practiceSince) {
			this.practiceSince = practiceSince;
		}

		public Double getTotalRating() {
			return totalRating;
		}

		public void setTotalRating(Double totalRating) {
			this.totalRating = totalRating;
		}

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public Long getPhoneNumber() {
			return phoneNumber;
		}

		public void setPhoneNumber(Long phoneNumber) {
			this.phoneNumber = phoneNumber;
		}
/*
		public List<Qualification> getQualifications() {
			return qualifications;
		}

		public void setQualifications(List<Qualification> qualifications) {
			this.qualifications = qualifications;
		}

		public List<Review> getReviews() {
			return reviews;
		}

		public void setReviews(List<Review> reviews) {
			this.reviews = reviews;
		}

		public List<UserRating> getUserRatings() {
			return userRatings;
		}

		public void setUserRatings(List<UserRating> userRatings) {
			this.userRatings = userRatings;
		}

		public List<ReservedSlot> getReservedSlots() {
			return reservedSlots;
		}

		public void setReservedSlots(List<ReservedSlot> reservedSlots) {
			this.reservedSlots = reservedSlots;
		}*/

		public ContactInfo getContactInfo() {
			return contactInfo;
		}

		public void setContactInfo(ContactInfo contactInfo) {
			this.contactInfo = contactInfo;
		}

		public PaymentSettings getPaymentSettings() {
			return paymentSettings;
		}

		public void setPaymentSettings(PaymentSettings paymentSettings) {
			this.paymentSettings = paymentSettings;
		}

		public DoctorSettings getDoctorSettings() {
			return doctorSettings;
		}

		public void setDoctorSettings(DoctorSettings doctorSettings) {
			this.doctorSettings = doctorSettings;
		}

		/*public List<WorkPlace> getWorkPlaces() {
			return workPlaces;
		}

		public void setWorkPlaces(List<WorkPlace> workPlaces) {
			this.workPlaces = workPlaces;
		}
*/
		

		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((contactInfo == null) ? 0 : contactInfo.hashCode());
			result = prime * result + ((doctorId == null) ? 0 : doctorId.hashCode());
			result = prime * result + ((doctorSettings == null) ? 0 : doctorSettings.hashCode());
			result = prime * result + ((email == null) ? 0 : email.hashCode());
			result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			result = prime * result + Arrays.hashCode(image);
			result = prime * result + ((imageContentType == null) ? 0 : imageContentType.hashCode());
			result = prime * result + ((paymentSettings == null) ? 0 : paymentSettings.hashCode());
			result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
			result = prime * result + ((practiceSince == null) ? 0 : practiceSince.hashCode());
			result = prime * result + ((registerNumber == null) ? 0 : registerNumber.hashCode());
			result = prime * result + ((specialization == null) ? 0 : specialization.hashCode());
			result = prime * result + ((totalRating == null) ? 0 : totalRating.hashCode());
			return result;
		}

		@Override
		public String toString() {
			return "DoctorAggregateDTO [id=" + id + ", image=" + Arrays.toString(image) + ", imageContentType="
					+ imageContentType + ", doctorId=" + doctorId + ", specialization=" + specialization
					+ ", registerNumber=" + registerNumber + ", practiceSince=" + practiceSince + ", totalRating="
					+ totalRating + ", firstName=" + firstName + ", email=" + email + ", phoneNumber=" + phoneNumber
					+ ", contactInfo=" + contactInfo + ", paymentSettings=" + paymentSettings + ", doctorSettings="
					+ doctorSettings + "]";
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			DoctorAggregateDTO other = (DoctorAggregateDTO) obj;
			if (contactInfo == null) {
				if (other.contactInfo != null)
					return false;
			} else if (!contactInfo.equals(other.contactInfo))
				return false;
			if (doctorId == null) {
				if (other.doctorId != null)
					return false;
			} else if (!doctorId.equals(other.doctorId))
				return false;
			if (doctorSettings == null) {
				if (other.doctorSettings != null)
					return false;
			} else if (!doctorSettings.equals(other.doctorSettings))
				return false;
			if (email == null) {
				if (other.email != null)
					return false;
			} else if (!email.equals(other.email))
				return false;
			if (firstName == null) {
				if (other.firstName != null)
					return false;
			} else if (!firstName.equals(other.firstName))
				return false;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			if (!Arrays.equals(image, other.image))
				return false;
			if (imageContentType == null) {
				if (other.imageContentType != null)
					return false;
			} else if (!imageContentType.equals(other.imageContentType))
				return false;
			if (paymentSettings == null) {
				if (other.paymentSettings != null)
					return false;
			} else if (!paymentSettings.equals(other.paymentSettings))
				return false;
			if (phoneNumber == null) {
				if (other.phoneNumber != null)
					return false;
			} else if (!phoneNumber.equals(other.phoneNumber))
				return false;
			if (practiceSince == null) {
				if (other.practiceSince != null)
					return false;
			} else if (!practiceSince.equals(other.practiceSince))
				return false;
			if (registerNumber == null) {
				if (other.registerNumber != null)
					return false;
			} else if (!registerNumber.equals(other.registerNumber))
				return false;
			if (specialization == null) {
				if (other.specialization != null)
					return false;
			} else if (!specialization.equals(other.specialization))
				return false;
			if (totalRating == null) {
				if (other.totalRating != null)
					return false;
			} else if (!totalRating.equals(other.totalRating))
				return false;
			return true;
		}
	    
	    
	    
}
