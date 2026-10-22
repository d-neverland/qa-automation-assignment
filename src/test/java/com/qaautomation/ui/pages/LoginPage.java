package com.qaautomation.ui.pages;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

public class LoginPage {
  private final SelenideElement usernameInput = $("[data-test='username']");
  private final SelenideElement passwordInput = $("[data-test='password']");
  private final SelenideElement loginButton = $("[data-test='login-button']");
  private final SelenideElement errorMessage = $("[data-test='error']");

  public LoginPage open() {
    Selenide.open("/");
    usernameInput.shouldBe(visible);
    return this;
  }

  public ProductsPage loginAs(String username, String password) {
    submitCredentials(username, password);
    return new ProductsPage();
  }

  public LoginPage attemptLoginAs(String username, String password) {
    submitCredentials(username, password);
    return this;
  }

  public void shouldShowError(String expectedError) {
    errorMessage.shouldBe(visible).shouldHave(text(expectedError));
  }

  private void submitCredentials(String username, String password) {
    usernameInput.setValue(username);
    passwordInput.setValue(password);
    loginButton.click();
  }
}
