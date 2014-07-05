package pricedata;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import com.sample.pricedata.domain.TradeInstrumentPrice;
import com.sample.pricedata.exception.PriceDataCacheException;
import com.sample.pricedata.service.PriceDataCacheService;

public class PriceDataCacheServiceTest {

	private static PriceDataCacheService priceDataService;

	static {

		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		priceDataService = (PriceDataCacheService) applicationContext.getBean("priceDataCacheServiceImpl");
	}

	@Test
	@Transactional
	public void getInstrumentPricesforVendor() throws PriceDataCacheException {

		priceDataService.onboardVendors(new String[] { "bloomberg", "murex", "reuters" });

		priceDataService.clearAllPrices("bloomberg");
		priceDataService.clearAllPrices("murex");
		priceDataService.clearAllPrices("reuters");

		TradeInstrumentPrice[] tradeInstrumentPrices = getTestTradeInstrumentData();

		try {
			priceDataService.addPriceData("bloomberg", tradeInstrumentPrices[0].getTradeInstrument(),
					tradeInstrumentPrices[0].getPrice());
			priceDataService.addPriceData("bloomberg", tradeInstrumentPrices[1].getTradeInstrument(),
					tradeInstrumentPrices[1].getPrice());
			priceDataService.addPriceData("bloomberg", tradeInstrumentPrices[2].getTradeInstrument(),
					tradeInstrumentPrices[2].getPrice());
			priceDataService.addPriceData("bloomberg", tradeInstrumentPrices[3].getTradeInstrument(),
					tradeInstrumentPrices[3].getPrice());

			priceDataService.addPriceData("murex", "bond", 1.2);
			priceDataService.addPriceData("murex", "swaps", 1.3);
			priceDataService.addPriceData("murex", "equity", 1.4);
			priceDataService.addPriceData("murex", "fxo", 1.5);

			List<TradeInstrumentPrice> prices = priceDataService.getAllPricesByVendor("bloomberg");

			Assert.assertEquals(tradeInstrumentPrices.length, prices.size());
			Assert.assertEquals(tradeInstrumentPrices[0].getTradeInstrument(), prices.get(0).getTradeInstrument());
			Assert.assertEquals(tradeInstrumentPrices[0].getPrice(), prices.get(0).getPrice());

			Assert.assertEquals(tradeInstrumentPrices[1].getTradeInstrument(), prices.get(1).getTradeInstrument());
			Assert.assertEquals(tradeInstrumentPrices[1].getPrice(), prices.get(1).getPrice());

			Assert.assertEquals(tradeInstrumentPrices[2].getTradeInstrument(), prices.get(2).getTradeInstrument());
			Assert.assertEquals(tradeInstrumentPrices[2].getPrice(), prices.get(2).getPrice());

			Assert.assertEquals(tradeInstrumentPrices[3].getTradeInstrument(), prices.get(3).getTradeInstrument());
			Assert.assertEquals(tradeInstrumentPrices[3].getPrice(), prices.get(3).getPrice());

		} catch (PriceDataCacheException e) {

		}

	}

	@Test
	@Transactional
	public void getInstrumentPriceforSingleInstrumentForVendorList() throws PriceDataCacheException {

		try {
			priceDataService.onboardVendors(new String[] { "bloomberg", "murex", "reuters" });

			priceDataService.clearAllPrices("bloomberg");
			priceDataService.clearAllPrices("murex");
			priceDataService.clearAllPrices("reuters");

			TradeInstrumentPrice[] tradeInstrumentPrices = getTestTradeInstrumentData();

			priceDataService.addPriceData("bloomberg", tradeInstrumentPrices[0].getTradeInstrument(),
					tradeInstrumentPrices[0].getPrice());
			priceDataService.addPriceData("bloomberg", tradeInstrumentPrices[1].getTradeInstrument(),
					tradeInstrumentPrices[1].getPrice());
			priceDataService.addPriceData("bloomberg", tradeInstrumentPrices[2].getTradeInstrument(),
					tradeInstrumentPrices[2].getPrice());
			priceDataService.addPriceData("bloomberg", tradeInstrumentPrices[3].getTradeInstrument(),
					tradeInstrumentPrices[3].getPrice());

			priceDataService.addPriceData("murex", tradeInstrumentPrices[0].getTradeInstrument(),
					tradeInstrumentPrices[0].getPrice());
			priceDataService.addPriceData("murex", tradeInstrumentPrices[1].getTradeInstrument(),
					tradeInstrumentPrices[1].getPrice());
			priceDataService.addPriceData("murex", tradeInstrumentPrices[2].getTradeInstrument(),
					tradeInstrumentPrices[2].getPrice());
			priceDataService.addPriceData("murex", tradeInstrumentPrices[3].getTradeInstrument(),
					tradeInstrumentPrices[3].getPrice());

			List<TradeInstrumentPrice> prices = priceDataService.getPriceForSingleInstrument("bond",
					Arrays.asList("bloomberg", "murex"));

			Assert.assertEquals(tradeInstrumentPrices[0].getTradeInstrument(), prices.get(0).getTradeInstrument());
			Assert.assertEquals(tradeInstrumentPrices[0].getPrice(), prices.get(0).getPrice());

			Assert.assertEquals(tradeInstrumentPrices[0].getTradeInstrument(), prices.get(1).getTradeInstrument());
			Assert.assertEquals(tradeInstrumentPrices[0].getPrice(), prices.get(1).getPrice());

		} catch (PriceDataCacheException e) {

		}

	}

	@Test(expected = PriceDataCacheException.class)
	public void testDataCacheException() throws PriceDataCacheException {

		priceDataService.onboardVendors(new String[] { "bloomberg", "murex", "reuters" });

		priceDataService.clearAllPrices("bloomberg");
		priceDataService.clearAllPrices("murex");
		priceDataService.clearAllPrices("reuters");

		priceDataService.addPriceData("abc", "bond", 1.2);

	}

	/**
	 * Method to Return Trade Instrument Test Data
	 * 
	 * @return
	 */
	private TradeInstrumentPrice[] getTestTradeInstrumentData() {

		TradeInstrumentPrice bond = new TradeInstrumentPrice();
		bond.setTradeInstrument("bond");
		bond.setPrice(1.2);

		TradeInstrumentPrice swaps = new TradeInstrumentPrice();
		swaps.setTradeInstrument("swaps");
		swaps.setPrice(1.3);

		TradeInstrumentPrice equity = new TradeInstrumentPrice();
		equity.setTradeInstrument("equity");
		equity.setPrice(1.4);

		TradeInstrumentPrice fxo = new TradeInstrumentPrice();
		fxo.setTradeInstrument("fxo");
		fxo.setPrice(1.4);

		TradeInstrumentPrice tradeInstrumentPrices[] = new TradeInstrumentPrice[] { bond, swaps, equity, fxo };

		return tradeInstrumentPrices;

	}

}
