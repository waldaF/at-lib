package com.walder.at.lib.client.selenium.vo;

import java.time.LocalDateTime;

public record SeleniumMessageVo(String screen, String message, LocalDateTime createdAt) {
}
