package com.walder.at.lib.common.constant;

import com.walder.at.lib.provider.KeyProvider;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Constant {
	public static final String LINE_SEPARATOR = KeyProvider.loadProperty("line.separator");
}
