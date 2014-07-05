package com.sample.pricedata.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sample.pricedata.domain.TradeInstrumentPrice;
import com.sample.pricedata.domain.Vendor;
import com.sample.pricedata.exception.PriceDataCacheException;

@Repository("priceDataDao")
@Transactional(propagation = Propagation.REQUIRED)
public class PriceDataDaoImpl implements PriceDataDao {

	@PersistenceContext
	private EntityManager entityManager;

	private static final String HIBERNATE_CACHEABLE = "org.hibernate.cacheable";
	
	/**
	 * Retrieve Prices for a given vendor and trade Instrument
	 */
	public List<TradeInstrumentPrice> getPricesForVendorAndInstrument(Vendor vendor, String tradeInstrument) {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<TradeInstrumentPrice> criteria = cb.createQuery(TradeInstrumentPrice.class);
		Root<TradeInstrumentPrice> root = criteria.from(TradeInstrumentPrice.class);
		criteria.select(root);

		Predicate vendorIdPred = cb.equal(root.get("vendorId"), vendor.getId());
		criteria.where(vendorIdPred);

		Predicate tradeInstrumentPred = null;

		if (null != tradeInstrument) {
			tradeInstrumentPred = cb.equal(root.get("tradeInstrument"), tradeInstrument);
			criteria.where(tradeInstrumentPred);
			cb.and(vendorIdPred, tradeInstrumentPred);
		} else {
			cb.and(vendorIdPred);
		}

		TypedQuery<TradeInstrumentPrice> jpaQuery = entityManager.createQuery(criteria);

		jpaQuery.setHint(HIBERNATE_CACHEABLE, Boolean.TRUE);
		List<TradeInstrumentPrice> prices = jpaQuery.getResultList();
		return prices;
	}
	/**
	 * Retrieve Vendor for a given Vendor Name
	 */

	public Vendor getVendor(String vendorName) {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Vendor> criteria = cb.createQuery(Vendor.class);
		Root<Vendor> root = criteria.from(Vendor.class);
		criteria.select(root);

		Predicate vendorNamePred = cb.equal(root.get("vendorName"), vendorName);
		criteria.where(vendorNamePred);
		TypedQuery<Vendor> jpaQuery = entityManager.createQuery(criteria);
		jpaQuery.setHint(HIBERNATE_CACHEABLE, Boolean.TRUE);

		List<Vendor> vendors = jpaQuery.getResultList();

		if (null == vendors || vendors.size() == 0) {

			return null;
		}

		return vendors.get(0);
	}

	/**
	 * Save New Vendor Information 
	 */
	public void saveVendor(Vendor vendor) {
		entityManager.persist(vendor);
	}

	public void removePrices(String vendorName) throws PriceDataCacheException {

		Vendor vendor = getVendor(vendorName);
		if (null == vendor) {
			throw new PriceDataCacheException("No Vendor found with given name " + vendorName);
		}
		entityManager.createNativeQuery("delete from tradeinstrumentprice where vendorId =" + vendor.getId())
				.executeUpdate();
	}

}
