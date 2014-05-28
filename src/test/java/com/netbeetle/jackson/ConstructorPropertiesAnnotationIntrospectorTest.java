package com.netbeetle.jackson;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import lombok.Data;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

public class ConstructorPropertiesAnnotationIntrospectorTest
{
    @Data
    private static class ImmutablePojo
    {
        private final String name;
        private final int value;
        
//        @ConstructorProperties({"name", "value"})
//        public ImmutablePojo(String name, int value) {
//        	this.name = name;
//        	this.value = value;
//        }
//        
//        public String getName() {
//			return name;
//		}
//        
//        public int getValue() {
//			return value;
//		}
    }

    private final ImmutablePojo instance = new ImmutablePojo("foobar", 42);

    @Test(expected = JsonMappingException.class)
    public void testJacksonUnableToDeserialize() throws JsonGenerationException,
        JsonMappingException, IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(instance);
        mapper.readValue(json, ImmutablePojo.class);
    }

    @Test
    public void testJacksonAbleToDeserialize() throws JsonGenerationException,
        JsonMappingException, IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        AnnotationIntrospector ai = AnnotationIntrospectorPair.create(new JacksonAnnotationIntrospector(), new ConstructorPropertiesAnnotationIntrospector());
        mapper.setAnnotationIntrospector(ai);
        
        String json = mapper.writeValueAsString(instance);
        ImmutablePojo output = mapper.readValue(json, ImmutablePojo.class);
        assertThat(output, equalTo(instance));
    }
}
