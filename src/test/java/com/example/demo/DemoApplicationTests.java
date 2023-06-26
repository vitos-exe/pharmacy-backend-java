package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.SQLException;

@SpringBootTest
class DemoApplicationTests {
	@Autowired
	DataSource dataSource;

	@Test
	void testContextLoads() {

	}

	@Test
	void testDataSourceConnection() throws SQLException {
		assertTrue(dataSource.getConnection().isValid(0));
	}
}
