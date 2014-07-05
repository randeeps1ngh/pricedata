package com.sample.pricedata.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "priceCache")
public class Vendor {

	private Long id;
	private String vendorName;
	private List<TradeInstrumentPrice> prices;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@OneToMany(orphanRemoval = true)
	@JoinColumn(name = "vendorId")
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	public List<TradeInstrumentPrice> getPrices() {
		return prices;
	}

	public void addPrice(TradeInstrumentPrice price) {
		if(null == prices){
			prices = new ArrayList<TradeInstrumentPrice>();
		}
		prices.add(price);
	}

	public void setPrices(List<TradeInstrumentPrice> prices) {
		this.prices = prices;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

}
