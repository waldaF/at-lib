package com.walder.at.lib.provider;

import lombok.experimental.UtilityClass;

import java.util.concurrent.TimeUnit;

@UtilityClass
public class RetryProvider {

	public static void sleepMs(final long sleepDuration) {
		try {
			TimeUnit.MILLISECONDS.sleep(sleepDuration);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}