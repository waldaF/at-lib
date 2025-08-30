package com.walder.at.lib.client.selenium.vo;

import org.openqa.selenium.By;

/**
 * Pro simple prvky minimalni retry...pro frameVo by to bylo napr 200ms 3 retry atp
 */
public record ElementVo(By value, Integer iterations, Integer sleep) implements ByAttribute {
	@Override
	public By get() {
		return value;
	}
	public ElementVo(By value) {
		this(value, 1, 100);
	}

	@Override
	public Integer getIterations() {
		return iterations;
	}

	@Override
	public Integer getSleepTime() {
		return sleep;
	}
}
