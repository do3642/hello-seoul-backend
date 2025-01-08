package com.helloseoul.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddressParser {
	private static Pattern GU_NAME_PATTERN = Pattern.compile("([가-힣]+구)");
	
	public static String extractGuName(String address) {
		if (address == null || address.isEmpty()) {
			return null;
		}
		
		Matcher matcher = GU_NAME_PATTERN.matcher(address);
		if (matcher.find()) {
			return matcher.group(1);
		}
		
		return null ;
	}
}
