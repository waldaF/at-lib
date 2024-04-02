package com.walder.at.lib.listener;


import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.walder.at.lib.common.MutexMap;
import com.walder.at.lib.report.ExtentReportManager;
import com.walder.at.lib.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;

import java.util.List;


/**
 * Listener for all tests. When test start parent test is created. If parent(father) test has some dataset new one as
 * node (child) test is added. All tests are created via ExtentReporter which handle AT result as html output.
 */
@Slf4j
public class TestNgListener implements ITestListener, ISuiteListener, IReporter, IInvokedMethodListener {
	private static final MutexMap<String, ExtentTest> mutexMap = new MutexMap<>();

	@Override
	public void generateReport(List<XmlSuite> list,
							   List<ISuite> list1, String s) {
		ExtentReportManager.flush();
	}

	@Override
	public void onTestStart(ITestResult result) {
		final String className = result.getTestClass().getRealClass().getSimpleName();
		final ITestNGMethod method = result.getMethod();
		final String description = method.getDescription();
		final String methodName = method.getMethodName();
		final String key = String.format("%s.%s", className, methodName);

		if (mutexMap.containsKey(key)) {
			final ExtentTest parentTest = mutexMap.get(key);
			ExtentReportManager.createNodeTest(parentTest);
		} else {
			final String classNameDescription = className + "<br>";
			final String wholeDescription = StringUtils.isEmpty(description)
					? classNameDescription
					: classNameDescription + description;
			final ExtentTest extentTestFather = ExtentReportManager.createTest(methodName, wholeDescription);
			mutexMap.put(key, extentTestFather);
		}
	}

	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
		final Status status = ExtentReportManager.getStatus();
		if (status != null && status != Status.PASS) {
			testResult.setStatus(ITestResult.FAILURE);
			ExtentReportManager.getTest().fail("Test failed");
		}
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		ExtentReportManager.getTest().pass("Test complete");
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		ExtentReportManager.getTest().skip(result.getThrowable());
	}

	@Override
	public void onStart(ISuite suite) {
		log.info(String.format("START suite : %s", suite.getXmlSuite().getName()));
	}

	@Override
	public void onFinish(ISuite suite) {
		log.info(String.format("END suite : %s", suite.getXmlSuite().getName()));
	}

}