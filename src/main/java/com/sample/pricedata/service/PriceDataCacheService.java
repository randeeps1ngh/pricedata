package com.sample.pricedata.service;

import java.util.List;

import com.sample.pricedata.domain.TradeInstrumentPrice;
import com.sample.pricedata.exception.PriceDataCacheException;

public interface PriceDataCacheService {

	List<TradeInstrumentPrice> getAllPricesByVendor(String vendor) throws PriceDataCacheException;

	List<TradeInstrumentPrice> getPriceForSingleInstrument(String instrument, List<String> vendors)
			throws PriceDataCacheException;

	void addPriceData(String vendorName, String instrumentName, Double price) throws PriceDataCacheException;
	
	void onboardVendors(String [] vendors);
	
	void clearAllPrices(String vendorName) throws PriceDataCacheException;
	

}
