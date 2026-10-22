package com.qaautomation.ui.tests;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;

public abstract class BaseUiTest {

  private static final List<Logger> SILENCED_SELENIUM_LOGGERS = List.of(
      Logger.getLogger("org.openqa.selenium.devtools"),
      Logger.getLogger("org.openqa.selenium.chromium"));

  @BeforeSuite(alwaysRun = true)
  public void silenceSeleniumWarnings() {
    SILENCED_SELENIUM_LOGGERS.forEach(logger -> logger.setLevel(Level.SEVERE));
  }

  @BeforeSuite(alwaysRun = true)
  public void addAllureListener() {
    SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
  }

  @AfterMethod(alwaysRun = true)
  public void closeBrowser() {
    Selenide.closeWebDriver();
  }
}