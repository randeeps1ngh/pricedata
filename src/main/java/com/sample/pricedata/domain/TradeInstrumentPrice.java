package com.sample.pricedata.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "priceCache")
public class TradeInstrumentPrice {

	private Long id;
	private String tradeInstrument;
	private Double price;
	private Long vendorId;

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTradeInstrument() {
		return tradeInstrument;
	}

	public void setTradeInstrument(String tradeInstrument) {
		this.tradeInstrument = tradeInstrument;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

}
