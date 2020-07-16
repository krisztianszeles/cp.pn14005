package com.cargopartner.education.pn14005.backend.converters;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonConverter {
	private final static Logger LOGGER = Logger.getLogger(JsonConverter.class);
	
	public String objectToJSON(Object object) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(object);
		} catch (IOException e) {
			LOGGER.error("Error converting to JSON", e);
		}
		return null;
	}	
	
	public <T> T getObjectFromJson(String json, Class<T> clazz) {
		try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            LOGGER.error("Error converting to/from JSON", e);
        }
        return null;
	}
}
