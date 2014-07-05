package com.sample.pricedata.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sample.pricedata.dao.PriceDataDao;
import com.sample.pricedata.domain.TradeInstrumentPrice;
import com.sample.pricedata.domain.Vendor;
import com.sample.pricedata.exception.PriceDataCacheException;

@Transactional
@Component
public class PriceDataCacheServiceImpl implements PriceDataCacheService {
	
	protected final Logger logger = Logger.getLogger(PriceDataCacheServiceImpl.class);

	private PriceDataDao priceDao;

	public PriceDataDao getPriceDao() {
		return priceDao;
	}

	@Autowired
	public void setPriceDao(PriceDataDao priceDao) {
		this.priceDao = priceDao;
	}

	/**
	 * Get All prices for given Vendor
	 */
	public List<TradeInstrumentPrice> getAllPricesByVendor(String vendorName) throws PriceDataCacheException {

		logger.info("PriceDataCacheServiceImpl : getAllPricesByVendor called for " + vendorName);
		Vendor vendor = priceDao.getVendor(vendorName);
		if (null == vendor) {
			logger.error("PriceDataCacheServiceImpl :  No Vendor found with given name " + vendorName);
			throw new PriceDataCacheException("No Vendor found with given name " + vendorName);
		}

		return priceDao.getPricesForVendorAndInstrument(vendor, null);

	}
	
	/**
	 * Get Price for Single Instrument for list of Vendors.
	 */

	public List<TradeInstrumentPrice> getPriceForSingleInstrument(String instrument, List<String> vendors)
			throws PriceDataCacheException {

		logger.info("PriceDataCacheServiceImpl : getPriceForSingleInstrument invoked for " + instrument );
		
		List<TradeInstrumentPrice> instrumentPrices = new ArrayList<TradeInstrumentPrice>();

		for (String vendorName : vendors) {

			Vendor vendor = priceDao.getVendor(vendorName);
			if (null == vendor) {
				logger.error("PriceDataCacheServiceImpl :  No Vendor found with given name " + vendorName);
				throw new PriceDataCacheException("No Vendor found with given name " + vendorName);
			}

			instrumentPrices.addAll(priceDao.getPricesForVendorAndInstrument(vendor, instrument));
		}
		return instrumentPrices;

	}
	
	/**
	 * Add Price Data for vendor for given instrument and price 
	 */

	public void addPriceData(String vendorName, String instrumentName, Double price) throws PriceDataCacheException {

		logger.info("PriceDataCacheServiceImpl : addPriceData invoked for " + vendorName  + " " +  instrumentName + " " + price);
		
		TradeInstrumentPrice tradeInstrumentPrice = new TradeInstrumentPrice();
		tradeInstrumentPrice.setPrice(price);
		tradeInstrumentPrice.setTradeInstrument(instrumentName);

		Vendor vendor = priceDao.getVendor(vendorName);
		if (null == vendor) {
			logger.error("PriceDataCacheServiceImpl :  No Vendor found with given name " + vendorName);
			throw new PriceDataCacheException("Vendor doesn't exist in our system ,pleaes check name of the vendor "
					+ vendorName);
		}

		vendor.addPrice(tradeInstrumentPrice);
		priceDao.saveVendor(vendor);
	}

	/**
	 * Add New Vendors to the system if vendor is already there ignore it.
	 */
	public void onboardVendors(String[] vendors) {

		logger.info("PriceDataCacheServiceImpl : onboardVendors invoked ");
		for (String vendorName : vendors) {

			Vendor vendor = priceDao.getVendor(vendorName);
			if (null == vendor) {
				vendor = new Vendor();
				vendor.setVendorName(vendorName);
				priceDao.saveVendor(vendor);
			}
		}
	}

	/**
	 * Remove all the prices for a given vendor Name
	 */
	public void clearAllPrices(String vendorName) throws PriceDataCacheException {
		logger.info("PriceDataCacheServiceImpl : clearAllPrices invoked ");
		priceDao.removePrices(vendorName);

	}
}
