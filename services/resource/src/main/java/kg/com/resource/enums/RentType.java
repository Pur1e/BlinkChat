package kg.com.resource.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RentType {
	
	HOURLY("HOURLY"),
	DAILY("DAILY"),
	MONTHLY("MONTHLY");
	
	private final String value;
	
}
