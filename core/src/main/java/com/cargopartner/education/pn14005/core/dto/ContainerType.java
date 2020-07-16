package com.cargopartner.education.pn14005.core.dto;

public enum ContainerType {
    STANDARD,
    REFRIGERATOR;
	
	public static ContainerType convertToEnum(String type) {
		return type.equals("STANDARD") ? STANDARD : REFRIGERATOR;
	}
}