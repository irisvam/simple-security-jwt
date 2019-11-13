package com.pattern.spring.converter;

import org.springframework.core.convert.converter.Converter;

import com.pattern.spring.enums.RoleName;

public class StringToEnumConverter implements Converter<String, RoleName> {

	@Override
	public RoleName convert(final String param) {

		return RoleName.valueOf(param.toUpperCase());
	}

}
