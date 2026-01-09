package com.project;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class StudentServiceApplicationTests {

	@Test
	void mainMethodExistsAndIsStatic() throws NoSuchMethodException {
		Class<?> cls = StudentServiceApplication.class;
		Method main = cls.getMethod("main", String[].class);
		assertNotNull(main, "main method should exist");
		assertTrue(Modifier.isStatic(main.getModifiers()), "main method should be static");
		assertEquals(void.class, main.getReturnType(), "main method should have void return type");
	}

	@Test
	void annotatedWithSpringBootApplication() {
		SpringBootApplication ann = StudentServiceApplication.class.getAnnotation(SpringBootApplication.class);
		assertNotNull(ann, "StudentServiceApplication should be annotated with @SpringBootApplication");
	}
}
