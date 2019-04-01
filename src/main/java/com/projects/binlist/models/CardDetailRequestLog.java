package com.projects.binlist.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="card_details_request_logs")
public class CardDetailRequestLog extends BaseModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 450064507430370802L;
	
	@Column(name = "card_number")
	private String cardNumber;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "request_date")
	private Date requestDate = new Date();

	
	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public Date getRequestDate() {
		return requestDate;
	}
}
