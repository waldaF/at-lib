package com.walder.at.lib.whalebone;

import com.aventstack.extentreports.ExtentTest;
import com.walder.at.lib.SuiteGroup;
import com.walder.at.lib.client.selenium.SeleniumClient;
import com.walder.at.lib.client.selenium.vo.ElementVo;
import com.walder.at.lib.listener.TestNgListener;
import com.walder.at.lib.report.ExtentReportManager;
import com.walder.at.lib.utils.StringUtils;
import com.walder.at.lib.validation.Validator;
import org.junit.jupiter.api.AfterAll;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

@Test(groups = {SuiteGroup.Whalebone.WHALEBONE_E2E})
@Listeners(TestNgListener.class)
public class WhaleboneSeleniumIT {
	private static final List<SeleniumClient> clients = new ArrayList<>();
	private static final boolean LOCALHOST = true;
	private static final By TITLE = By.id("title");
	private static final By LOAD_DELAY_HREF = By.xpath("//a[text()='Load Delay']");
	private static final By PROGRESS_BAR_HREF = By.xpath("//a[text()='Progress Bar']");

	private static final By LOAD_DELAY_BUTTON = By.xpath("//button[text()='Button Appearing After Delay']");
	private static final By PROGRESS_BAR_START_BUTTON = By.xpath("//h4[text()='Playground']/parent::*//button[text()='Start']");
	private static final By PROGRESS_BAR_STOP_BUTTON = By.id("stopButton");
	private static final By PROGRESS_BAR = By.id("progressBar");

	private static final int SLEEP_MS_PROGRESS_BAR = 20;
	private static final int WHILE_TIMEOUT_MS = 20000;

	@AfterAll
	public static void cleanUp() {
		clients.forEach(SeleniumClient::quit);
	}

	@Test(description =
			"<ul>" +
					"<li>http://uitestingplayground.com/  </li>" +
					"<li> Click on the Load Delay and verify the page will get loaded in reasonable time eg 500ms otherwise fail</li>" +
					"</ul>")
	public void whaleboneLoadDelayUi() {
		final ExtentTest extentTest = ExtentReportManager.getTest();
		final SeleniumClient client = SeleniumClient.createClient(extentTest, LOCALHOST);
		clients.add(client);
		client.loadMainPage("http://uitestingplayground.com/", new ElementVo(TITLE));
		client.takeScreenshot("Load main page");
		client.clickAndWaitForRedirectUnderThreshold(
				new ElementVo(LOAD_DELAY_HREF),
				LOAD_DELAY_BUTTON,
				500
		);
	}

	@Test(description =
			"<ul>" +
					"<li>http://uitestingplayground.com/  </li>" +
					"<li>From the Home page, navigate to the Progress Bar page</li>" +
					"<li>Loading have to take max 400 ms</li>" +
					"<li>Test executes start button and wait till required value in progress bar</li>" +
					"<li>Test validates that progress bar value corresponds to expected value in range +-1</li>" +
					"</ul>",
			dataProvider = "progressBarDataset")
	public void whaleboneProgressBarUi(final Progress progress) {
		final ExtentTest extentTest = ExtentReportManager.getTest();
		final int progressValue = progress.value;
		extentTest.info(String.format("progress bar test for %s", progressValue));
		final SeleniumClient client = SeleniumClient.createClient(extentTest, LOCALHOST);
		clients.add(client);
		client.loadMainPage("http://uitestingplayground.com/", new ElementVo(TITLE));
		client.clickAndWaitForRedirectUnderThreshold(
				new ElementVo(PROGRESS_BAR_HREF),
				PROGRESS_BAR_START_BUTTON,
				410
		);
		client.click(new ElementVo(PROGRESS_BAR_START_BUTTON));
		client.waitUntil(ExpectedConditions.presenceOfElementLocated(PROGRESS_BAR));

		int lastSeenValue = progressingBar(extentTest, client, progress);
		extentTest.info("Progress bar stopped at " + lastSeenValue + "%");

		final Validator validator = new Validator("Progress bar", extentTest);
		final var minExpected = progressValue - 1;
		final var maxExpected = progressValue == 100 ? 100 : progressValue + 1;
		validator.validateInRange(lastSeenValue, minExpected, maxExpected, "progress bar");
		validator.logToReporterAndMarkFailedIfError();
	}

	public int progressingBar(final ExtentTest extentTest,
							  final SeleniumClient client,
							  final Progress progress) {
		final long startTime = System.currentTimeMillis();
		boolean stopClicked = false;
		int lastSeenValue = 0;

		while (!stopClicked) {
			long elapsedTime = System.currentTimeMillis() - startTime;
			if (elapsedTime > WHILE_TIMEOUT_MS) {
				extentTest.fail("Timeout: Progress bar did not reach target value within " + WHILE_TIMEOUT_MS + "ms");
				break;
			}
			final var progressBarElement = client.findBy(new ElementVo(PROGRESS_BAR));
			final String valueStr = progressBarElement.getAttribute("aria-valuenow");
			final int safeValueInt = StringUtils.isEmpty(valueStr) ? lastSeenValue : Integer.parseInt(valueStr);
			lastSeenValue = safeValueInt;

			if (safeValueInt >= progress.value) {
				client.click(new ElementVo(PROGRESS_BAR_STOP_BUTTON));
				client.captureScreen();
				stopClicked = true;
			}

			try {
				Thread.sleep(SLEEP_MS_PROGRESS_BAR);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				break;
			}
		}
		return lastSeenValue;
	}

	@DataProvider(parallel = true)
	public static Object[][] progressBarDataset() {
		return new Object[][]{
				{new Progress(100)},
				{new Progress(75)},
				{new Progress(50)},
				{new Progress(25)},
		};
	}

	public record Progress(int value) {
	}
}

