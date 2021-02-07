package com.accenture.interview;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.accenture.interview.entities.Client;
import com.accenture.interview.entities.Product;
import com.accenture.interview.entities.Purchase;
import com.accenture.interview.exceptions.BaseException;
import com.accenture.interview.exceptions.UpdatedPurchaseCostLessException;
import com.accenture.interview.purchase.impl.PurchaseService;
import com.accenture.interview.repositories.ClientRepository;
import com.accenture.interview.repositories.ProductRepository;
import com.accenture.interview.repositories.PurchaseRepository;
import com.accenture.interview.util.ResponseCode;
import com.accenture.interview.util.SystemProperties;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
class PurchaseServiceImplTest {

	private static final Long USER_ONE_ID = 1L;
	private static final Long PRODUCT_ONE_ID = 1L;
	private static final Long PRODUCT_TWO_ID = 2L;
	private static final Long PURCHASE_ONE_ID = 1L;

	@InjectMocks
	private PurchaseService purchaseService;

	@Mock
	private ClientRepository clientRepository;
	@Mock
	private ProductRepository productRepository;
	@Mock
	private PurchaseRepository purchaseRepository;
	@Mock
	private SystemProperties systemProperties;

	private Client client;
	private List<Product> products;
	private Purchase purchase;

	@BeforeEach
	public void setUp() {
		// Products
		products = new ArrayList<>();
		String[] names = { "Product1", "Product2" };
		int[] prices = { 1000, 2000 };

		for (int i = 0; i < names.length; i++) {
			products.add(new Product(names[i], new BigDecimal(prices[i])));
		}

		// client
		client = new Client("1005206910", "Juan Manuel G", "Mz J # 177");

		// purchase
		purchase = new Purchase(PURCHASE_ONE_ID, new Date(), new BigDecimal(3000), new BigDecimal(0),
				new BigDecimal(5000), new BigDecimal(8000), true, products, client);
		// when
		Mockito.when(clientRepository.findById(USER_ONE_ID)).thenReturn(Optional.of(client));
		Mockito.when(productRepository.findById(PRODUCT_ONE_ID)).thenReturn(Optional.of(products.get(0)));
		Mockito.when(productRepository.findById(PRODUCT_TWO_ID)).thenReturn(Optional.of(products.get(1)));
		Mockito.when(systemProperties.getIvaPercentage()).thenReturn(new BigDecimal(19));
		Mockito.when(systemProperties.getDeliveryPrice()).thenReturn(new BigDecimal(5000));
		Mockito.when(systemProperties.getDeleteHourTimeLimit()).thenReturn(12);
		Mockito.when(purchaseRepository.save(any(Purchase.class))).thenReturn(purchase);
	}

	@Test
	@DisplayName("Should create a new purchase")
	public void shouldAddPurchaseService() throws Exception {

		List<Long> productsId = new ArrayList<>();

		productsId.add(PRODUCT_ONE_ID);
		productsId.add(PRODUCT_TWO_ID);

		purchaseService.addPurchase(USER_ONE_ID, productsId);
	}

	@Test
	@DisplayName("Should update a purchase")
	public void shouldUpdatePurchaseService() throws Exception {

		Mockito.when(purchaseRepository.findById(PURCHASE_ONE_ID)).thenReturn(Optional.of(purchase));

		List<Long> productsId = new ArrayList<>();

		productsId.add(PRODUCT_ONE_ID);
		productsId.add(PRODUCT_TWO_ID);

		purchaseService.updatePurchase(PURCHASE_ONE_ID, productsId);
	}

	@Test
	@DisplayName("Should not update purchase, new products cost less than the old ones")
	public void shouldNotUpdatePurchaseService() throws Exception {

		Mockito.when(purchaseRepository.findById(PURCHASE_ONE_ID)).thenReturn(Optional.of(purchase));

		List<Long> productsId = new ArrayList<>();

		productsId.add(PRODUCT_ONE_ID);

		BaseException exception = assertThrows(UpdatedPurchaseCostLessException.class, () -> {
			purchaseService.updatePurchase(PURCHASE_ONE_ID, productsId);
		});

		String errorCode = exception.getErrorCode();

		assertEquals(ResponseCode.UPDATED_PURCHASE_COST_LESS, errorCode);
	}

	@Test
	@DisplayName("Should delete a purchase")
	public void shouldDeletePurchase() throws Exception {

		Mockito.when(purchaseRepository.findById(PURCHASE_ONE_ID)).thenReturn(Optional.of(purchase));
		purchaseService.deletePurchase(PURCHASE_ONE_ID);
	}

}
