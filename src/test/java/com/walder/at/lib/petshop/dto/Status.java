package com.walder.at.lib.petshop.dto;

import com.google.gson.annotations.SerializedName;

public enum Status {
	@SerializedName("available")
	AVAILABLE,
	@SerializedName("pending")
	PENDING,
	@SerializedName("sold")
	SOLD,
	@SerializedName("unknown")
	UNKNOWN,
	;
}
