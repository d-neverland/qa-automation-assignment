package com.qaautomation.ui.tests;

import static org.testng.Assert.assertEquals;

import com.qaautomation.config.TestConfig;
import com.qaautomation.ui.pages.LoginPage;
import com.qaautomation.ui.pages.ProductsPage;
import com.qaautomation.ui.pages.SortOption;
import java.math.BigDecimal;
import java.util.List;
import org.testng.annotations.Test;

public class ProductCartTest extends BaseUiTest {

  @Test(description = "Products are sorted by price (low to high) and the cheapest product can be added to the cart")
  public void shouldSortProductsByPriceAndAddCheapestToCart() {
    ProductsPage productsPage = new LoginPage()
        .open()
        .loginAs(TestConfig.getSauceDemoUsername(), TestConfig.getSauceDemoPassword())
        .shouldBeOpened();

    List<BigDecimal> displayedPrices = productsPage
        .sortBy(SortOption.PRICE_LOW_TO_HIGH)
        .getProductPrices();

    List<BigDecimal> expectedPrices = displayedPrices
        .stream()
        .sorted()
        .toList();
    assertEquals(displayedPrices, expectedPrices, "Products should be sorted by price from low to high");

    String cheapestProduct = productsPage.getProductNames().get(0);
    productsPage.addToCart(cheapestProduct)
        .shouldHaveCartBadge(1)
        .openCart()
        .shouldBeOpened()
        .shouldContainOnly(cheapestProduct);
  }
}
