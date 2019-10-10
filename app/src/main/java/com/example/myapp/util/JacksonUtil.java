package com.example.myapp.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class JacksonUtil {
	public static ObjectMapper objectMapper;

	public static <T> T readValue(String jsonStr, Class<T> valueType) {
		if (objectMapper == null) {
			objectMapper = new ObjectMapper();
		}

		try {
			return objectMapper.readValue(jsonStr, valueType);
		} catch (Exception arg2) {
			arg2.printStackTrace();
			return null;
		}
	}

	public static <T> T readValue(String jsonStr, TypeReference<T> valueTypeRef) {
		if (objectMapper == null) {
			objectMapper = new ObjectMapper();
		}

		try {
			return objectMapper.readValue(jsonStr, valueTypeRef);
		} catch (Exception arg2) {
			arg2.printStackTrace();
			return null;
		}
	}

	public static JsonNode readValue(String jsonStr) {
		if (objectMapper == null) {
			objectMapper = new ObjectMapper();
		}

		try {
			return objectMapper.readTree(jsonStr);
		} catch (Exception arg1) {
			arg1.printStackTrace();
			return null;
		}
	}

	public static String toJSon(Object object) {
		if (objectMapper == null) {
			objectMapper = new ObjectMapper();
		}

		try {
			return objectMapper.writeValueAsString(object);
		} catch (Exception arg1) {
			arg1.printStackTrace();
			return null;
		}
	}
}