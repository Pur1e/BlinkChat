package kg.com.resource.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Currency {
	USD("USD"),
	KGS("KGS"),
	RUB("RUB");
	
	private final String value;
	
}
