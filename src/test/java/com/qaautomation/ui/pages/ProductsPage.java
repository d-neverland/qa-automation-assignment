package com.qaautomation.ui.pages;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import java.math.BigDecimal;
import java.util.List;

public class ProductsPage {

  private static final String PRODUCT_CARD = "[data-test=inventory-item]";
  private static final String ADD_TO_CART_BUTTON = "[data-test^='add-to-cart']";

  private final SelenideElement title = $("[data-test='title']");
  private final ElementsCollection productNames = $$("[data-test='inventory-item-name']");
  private final ElementsCollection productPrices = $$("[data-test='inventory-item-price']");
  private final SelenideElement sortDropdown = $("[data-test='product-sort-container']");
  private final SelenideElement activeSortOption = $("[data-test='active-option']");
  private final SelenideElement cartLink = $("[data-test='shopping-cart-link']");
  private final SelenideElement cartBadge = $("[data-test='shopping-cart-badge']");

  public ProductsPage shouldBeOpened() {
    webdriver().shouldHave(urlContaining("/inventory.html"));
    title.shouldHave(exactText("Products"));
    return this;
  }

  public List<String> getProductNames() {
    productNames.shouldHave(sizeGreaterThan(0));
    return productNames.texts();
  }

  public List<BigDecimal> getProductPrices() {
    productPrices.shouldHave(sizeGreaterThan(0));
    return productPrices.texts().stream()
        .map(price -> new BigDecimal(price.replace("$", "")))
        .toList();
  }

  public ProductsPage sortBy(SortOption option) {
    sortDropdown.selectOptionByValue(option.value());
    activeSortOption.shouldHave(exactText(option.label()));
    return this;
  }

  public ProductsPage addToCart(String productName) {
    productNames.findBy(exactText(productName))
        .ancestor(PRODUCT_CARD)
        .$(ADD_TO_CART_BUTTON)
        .click();
    return this;
  }

  public ProductsPage shouldHaveCartBadge(int expectedCount) {
    cartBadge.shouldHave(exactText(String.valueOf(expectedCount)));
    return this;
  }

  public CartPage openCart() {
    cartLink.click();
    return new CartPage();
  }
}
