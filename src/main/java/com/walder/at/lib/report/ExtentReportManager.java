package com.walder.at.lib.report;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.model.Test;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.ExtentSparkReporterConfig;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.walder.at.lib.provider.KeyProvider;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

@UtilityClass
@Slf4j
public class ExtentReportManager {
	private static final String DIR = "ExtentReportResults.html";
	private static final Path PATH = Paths.get(System.getProperty("user.dir"));
	private static final ExtentReports extent = init();
	private static final ThreadLocal<ExtentTest> threadLocal = new ThreadLocal<>();

	public static ExtentTest createTest(final String name, final String description) {
		final ExtentTest extentTest = extent.createTest(name, description);
		threadLocal.set(extentTest);
		return extentTest;
	}

	public static ExtentTest createNodeTest(final ExtentTest parentTest) {
		final Test actualTest = parentTest.getModel();
		final String name = actualTest.getName();
		final ExtentTest extentTest = parentTest.createNode(name, null);
		threadLocal.set(extentTest);
		return extentTest;
	}

	public static ExtentTest getTest() {
		final ExtentTest extentTest = threadLocal.get();
		if (extentTest == null) {
			return createTest("Static initialization", "Some initialization in static content was failed.");
		}
		return extentTest;
	}
	public static Status getStatus() {
		final ExtentTest extentTest = threadLocal.get();
		return (extentTest != null) ? extentTest.getStatus() : null;
	}
	public static void flush() {
		extent.flush();
	}

	private static ExtentReports init() {

		final ExtentSparkReporter htmlReporter = new ExtentSparkReporter(PATH.resolve(DIR).toString());
		final ExtentSparkReporterConfig configuration = htmlReporter.config();
		configuration.setTheme(Theme.DARK);
		configuration.setDocumentTitle("Java REST API E2E");
		configuration.setEncoding(StandardCharsets.UTF_8.name());
		configuration.setReportName("E2E");

		final ExtentReports extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		extent.setSystemInfo("Host Name", resolveHostName());
		extent.setAnalysisStrategy(AnalysisStrategy.TEST);

		return extent;
	}

	private static String resolveHostName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			final String msg = "Unable to resolve host";
			log.error(msg, e);
			return msg;
		}
	}
}
