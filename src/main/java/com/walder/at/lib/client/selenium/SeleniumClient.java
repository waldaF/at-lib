package com.walder.at.lib.client.selenium;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.walder.at.lib.client.selenium.vo.ByAttribute;
import com.walder.at.lib.exceptions.SeleniumUriException;
import com.walder.at.lib.provider.RetryProvider;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Objects;

/**
 * TODO zkusit selenoid je to nadstavba
 * Idealne by client mel mit jen zakladni veci a extentTest a featurky maji byt ve wraperu
 */
public class SeleniumClient {

	private static final String INCOGNITO_MODE = "--incognito";
	private static final String ALLOW_ORIGINS = "--remote-allow-origins=*";
	private static final String REMOTE_URL = "your-remote-company-url";
	private static final String SUPPORT_HTTP = "--ignore-certificate-errors";
	private static final Integer WAIT_TIME = 30;

	private final ChromeOptions chromeOptions;
	private final WebDriver driver;
	private final ExtentTest extentTest;
	private final boolean localhost;

	public static SeleniumClient createClient(final ExtentTest extentTest, final boolean localhost) {
		var selenium = new SeleniumClient(extentTest, localhost);
		selenium.deleteCookies();
		selenium.maximizeSize();
		return selenium;
	}

	private SeleniumClient(final ExtentTest extentTest, final boolean localhost) {
		this.extentTest = extentTest;
		this.chromeOptions = initOptions(INCOGNITO_MODE, ALLOW_ORIGINS, SUPPORT_HTTP);
		this.localhost = localhost;
		this.driver = driverInit();
	}

	private URL asURL() {
		try {
			return new URL(REMOTE_URL);
		} catch (MalformedURLException e) {
			throw new SeleniumUriException("Unable to get URL from" + REMOTE_URL, e);
		}
	}

	private WebDriver driverInit() {
		if (!localhost) {
			return new RemoteWebDriver(asURL(), chromeOptions);
		}
		return WebDriverManager.chromedriver().capabilities(chromeOptions).create();
	}

	private ChromeOptions initOptions(final String... data) {
		final ChromeOptions options = new ChromeOptions();
		options.addArguments(data);
		return options;
	}

	public void maximizeSize() {
		driver.manage().window().maximize();
	}

	public void deleteCookies() {
		this.driver.manage().deleteAllCookies();
	}

	public void quit() {
		this.driver.quit();
	}


	public void takeErrorScreenShot(final String message) {
		extentTest.fail(message,
				MediaEntityBuilder.createScreenCaptureFromBase64String(captureScreen()).build());
	}

	public void takeScreenshot(final String message) {
		extentTest.info(message,
				MediaEntityBuilder.createScreenCaptureFromBase64String(captureScreen()).build());

	}

	public void get(final String url) {
		this.driver.get(url);
	}

	public String captureScreen() {
		TakesScreenshot newScreen = (TakesScreenshot) driver;
		String scnShot = newScreen.getScreenshotAs(OutputType.BASE64);
		return "data:image/jpg;base64, " + scnShot;
	}

	public void click(final ByAttribute attribute) {
		var element = findBy(attribute);
		element.click();
	}

	public void waitUntil(final ExpectedCondition<WebElement> condition) {
		final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIME));
		wait.until(condition);
	}

	public void clickAndWaitForRedirectUnderThreshold(final ByAttribute field,
													  final By tillElementPresent,
													  final int thresholdInMs) {
		try {
			var element = findBy(field);
			long startTime = System.currentTimeMillis();

			element.click();

			final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
			waitTillJsLoaded();
			wait.until(ExpectedConditions.presenceOfElementLocated(tillElementPresent));

			long endTime = System.currentTimeMillis();
			long totalTime = endTime - startTime;

			if (totalTime > thresholdInMs) {
				String errMessage = String.format("Click on %s took %d thresold is %d ms to find element %s",
						field.get().toString(), totalTime, thresholdInMs, tillElementPresent.toString());
				takeErrorScreenShot(errMessage);
				return;
			}

			extentTest.log(Status.INFO, String.format(
					"Click on %s took %d ms",
					field.get().toString(),
					totalTime
			));
		} catch (TimeoutException te) {
			extentTest.fail(String.format("Timeout: Waiting for %s or final element failed", tillElementPresent.toString()));
			takeErrorScreenShot(te.getMessage());
			this.quit();
		} catch (Exception e) {
			extentTest.fail(String.format("Click on %s and wait for element %s failed",
					field.get().toString(), tillElementPresent.toString()));
			takeErrorScreenShot(e.getMessage());
			this.quit();
		}
	}

	public void loadMainPage(final String url, final ByAttribute waitTillElementEnabled) {
		get(url);
		waitTillJsLoaded();
		retryIsEnabled(waitTillElementEnabled);
	}

	public WebElement findBy(ByAttribute by) {
		retryIsEnabled(by);
		return driver.findElement(by.get());
	}

	private void retryIsEnabled(final ByAttribute attribute) {
		if (isNull(attribute.getIterations()) || isNull(attribute.getSleepTime())) {
			return;
		}
		int retries = 0;
		while (retries < attribute.getIterations()) {
			retries++;
			if (driver.findElements(attribute.get()).isEmpty()) {
				RetryProvider.sleepMs(attribute.getSleepTime());
			} else if (!driver.findElement(attribute.get()).isEnabled()) {
				RetryProvider.sleepMs(attribute.getSleepTime());
			} else {
				return;
			}
		}
		var errorMessage = String.format("Unable to locate element %s with retry %s times and sleep %s [ms]",
				attribute.get().toString(),
				attribute.getIterations(),
				attribute.getSleepTime());
		takeErrorScreenShot(errorMessage);
		this.quit();
		throw new RuntimeException("Problem to find element");
	}


	private void waitTillJsLoaded() {
		new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIME)).until(
				webDriver -> ((JavascriptExecutor) webDriver)
						.executeScript("return document.readyState").equals("complete"));
	}

	private boolean isNull(Integer value) {
		return Objects.isNull(value);
	}
}
