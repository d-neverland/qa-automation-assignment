package com.qaautomation.ui.pages;

import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

public class CartPage {

  private final SelenideElement title = $("[data-test='title']");
  private final ElementsCollection cartItemNames = $$("[data-test='cart-list'] [data-test='inventory-item-name']");

  public CartPage shouldBeOpened() {
    webdriver().shouldHave(urlContaining("/cart.html"));
    title.shouldHave(exactText("Your Cart"));
    return this;
  }

  public void shouldContainOnly(String productName) {
    cartItemNames.shouldHave(exactTexts(productName));
  }
}
