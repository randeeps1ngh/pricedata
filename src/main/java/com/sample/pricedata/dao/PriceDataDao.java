package com.sample.pricedata.dao;

import java.util.List;

import com.sample.pricedata.domain.TradeInstrumentPrice;
import com.sample.pricedata.domain.Vendor;
import com.sample.pricedata.exception.PriceDataCacheException;

public interface PriceDataDao {

	public List<TradeInstrumentPrice> getPricesForVendorAndInstrument(Vendor vendor, String tradeInstrument);

	public Vendor getVendor(String vendorName);

	public void saveVendor(Vendor vendor);

	public void removePrices(String vendorName) throws PriceDataCacheException;

}
