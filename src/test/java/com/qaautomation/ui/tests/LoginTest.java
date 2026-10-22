package com.qaautomation.ui.tests;

import com.qaautomation.config.TestConfig;
import com.qaautomation.ui.pages.LoginPage;
import com.qaautomation.ui.pages.ProductsPage;
import java.math.BigDecimal;
import java.util.List;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class LoginTest extends BaseUiTest {

  @Test(description = "Standard user logs in and lands on a Products page")
  public void shouldLogInAndSeeProducts() {
    ProductsPage productsPage = new LoginPage()
        .open()
        .loginAs(TestConfig.getSauceDemoUsername(), TestConfig.getSauceDemoPassword())
        .shouldBeOpened();

    List<String> names = productsPage.getProductNames();
    List<BigDecimal> prices = productsPage.getProductPrices();

    SoftAssert softAssert = new SoftAssert();
    softAssert.assertFalse(names.isEmpty(), "Products page should list at least one product");
    softAssert.assertEquals(prices.size(), names.size(), "Every product should have a price");
    names.forEach(name -> softAssert.assertFalse(name.isBlank(), "Every product should have a name"));
    prices.forEach(price -> softAssert.assertTrue(
        price.compareTo(BigDecimal.ZERO) > 0,
        "Price should be positive but was " + price
    ));
    softAssert.assertAll();
  }

  @Test(description = "Login is rejected with a clear error message", dataProvider = "invalidLogins")
  public void shouldRejectInvalidLogin(String testCaseName, String username, String password, String expectedError) {
    Reporter.log("Scenario: " + testCaseName);
    new LoginPage()
        .open()
        .attemptLoginAs(username, password)
        .shouldShowError(expectedError);
  }

  @DataProvider
  public Object[][] invalidLogins() {
    return new Object[][] {
        {
            "locked out user",
            "locked_out_user",
            TestConfig.getSauceDemoPassword(),
            "Sorry, this user has been locked out."
        },
        {
            "wrong password",
            TestConfig.getSauceDemoUsername(),
            "wrong_password",
            "Username and password do not match any user in this service"
        }
    };
  }
}
