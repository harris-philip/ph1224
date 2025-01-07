package com.rental.rental_app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.rental.rental_app.entity.Tool;
import com.rental.rental_app.entity.ToolRentalInfo;
import com.rental.rental_app.entity.ToolType;
import com.rental.rental_app.exceptions.InvalidDiscountRateException;
import com.rental.rental_app.exceptions.InvalidRentalDaysException;
import com.rental.rental_app.services.CheckoutService;
import com.rental.rental_app.services.ToolService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RentalAppApplicationTests {
	private Map<String, Tool> testTools;
	private Map<UUID, ToolRentalInfo> testToolRentalInfos;
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yy");

	@Mock
	ToolService toolService;

	@InjectMocks
	CheckoutService checkoutService;

	@BeforeEach
	void setupTestInformation() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test-data.json");
		TestDataDto testData = objectMapper.readValue(inputStream, TestDataDto.class);
		List<Tool> updatedToolsList = testData.getTools();
		updatedToolsList.forEach(tool -> {
			ToolType foundToolType = testData.getToolTypes().stream().filter(toolType -> toolType.getToolTypeId().equals(tool.getToolType().getToolTypeId())).findAny().get();
			tool.setToolType(foundToolType);
		});
		this.testTools = updatedToolsList.stream().collect(Collectors.toMap(Tool::getToolCode, tool -> tool));
		this.testToolRentalInfos = testData.getToolRentalInfos().stream().filter(Objects::nonNull)
				.collect(Collectors
						.toMap(rentalInfo -> rentalInfo.getToolType().getToolTypeId(), rentalInfo -> rentalInfo));
	}


	@Test
	void test1() {
		assertThrows(InvalidDiscountRateException.class,
				() -> checkoutService.checkout("JAKR", 5, 101, LocalDate.parse("9/3/15", formatter)));
	}

	@Test
	void test2() throws InvalidRentalDaysException, InvalidDiscountRateException {
		Tool toolToUse = testTools.get("LADW");
		when(toolService.findToolByCode(anyString())).thenReturn(toolToUse);
		when(toolService.findRentalInfoByToolType(any())).thenReturn(testToolRentalInfos.get(toolToUse.getToolType().getToolTypeId()));

		checkoutService.checkout("LADW", 3, 10, LocalDate.parse("7/2/20", formatter));
	}

	@Test
	void test3() throws InvalidRentalDaysException, InvalidDiscountRateException {
		Tool toolToUse = testTools.get("CHNS");
		when(toolService.findToolByCode(anyString())).thenReturn(toolToUse);
		when(toolService.findRentalInfoByToolType(any())).thenReturn(testToolRentalInfos.get(toolToUse.getToolType().getToolTypeId()));

		checkoutService.checkout("CHNS", 5, 25, LocalDate.parse("7/2/15", formatter));
	}

	@Test
	void test4() throws InvalidRentalDaysException, InvalidDiscountRateException {
		Tool toolToUse = testTools.get("JAKD");
		when(toolService.findToolByCode(anyString())).thenReturn(toolToUse);
		when(toolService.findRentalInfoByToolType(any())).thenReturn(testToolRentalInfos.get(toolToUse.getToolType().getToolTypeId()));

		checkoutService.checkout("JAKD", 6, 0, LocalDate.parse("9/3/15", formatter));
	}

	@Test
	void test5() throws InvalidRentalDaysException, InvalidDiscountRateException {
		Tool toolToUse = testTools.get("JAKR");
		when(toolService.findToolByCode(anyString())).thenReturn(toolToUse);
		when(toolService.findRentalInfoByToolType(any())).thenReturn(testToolRentalInfos.get(toolToUse.getToolType().getToolTypeId()));

		checkoutService.checkout("JAKR", 9, 0, LocalDate.parse("7/2/15", formatter));
	}

	@Test
	void test6() throws InvalidRentalDaysException, InvalidDiscountRateException {
		Tool toolToUse = testTools.get("JAKR");
		when(toolService.findToolByCode(anyString())).thenReturn(toolToUse);
		when(toolService.findRentalInfoByToolType(any())).thenReturn(testToolRentalInfos.get(toolToUse.getToolType().getToolTypeId()));

		checkoutService.checkout("JAKR", 4, 50, LocalDate.parse("7/2/20", formatter));
	}
}
