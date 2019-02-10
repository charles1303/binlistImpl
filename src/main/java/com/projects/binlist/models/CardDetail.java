package com.projects.binlist.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.projects.binlist.utils.CardType;

@Entity
@Table(name="card_details")
public class CardDetail extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1633939315255867864L;

	@Column(name = "iin_start")
	private String iinStart;
	
	@Column(name = "iin_end")
	private String iinEnd;
	
	@Column(name = "number_length")
	private int numberLength;
	
	@Column(name = "number_luhn")
	private boolean numberLuhn;
	
	@ManyToOne
	private Scheme scheme;
	
	@ManyToOne
	private Brand brand;
	
	@Enumerated(EnumType.STRING)
	private CardType type;
	
	@ManyToOne
	private Country country;
	
	@ManyToOne
	private Bank bank;

	public String getIinStart() {
		return iinStart;
	}

	public void setIinStart(String iinStart) {
		this.iinStart = iinStart;
	}

	public String getIinEnd() {
		return iinEnd;
	}

	public void setIinEnd(String iinEnd) {
		this.iinEnd = iinEnd;
	}

	public int getNumberLength() {
		return numberLength;
	}

	public void setNumberLength(int numberLength) {
		this.numberLength = numberLength;
	}

	public boolean isNumberLuhn() {
		return numberLuhn;
	}

	public void setNumberLuhn(boolean numberLuhn) {
		this.numberLuhn = numberLuhn;
	}

	public Scheme getScheme() {
		return scheme;
	}

	public void setScheme(Scheme scheme) {
		this.scheme = scheme;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public CardType getType() {
		return type;
	}

	public void setType(CardType type) {
		this.type = type;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}
	
}
