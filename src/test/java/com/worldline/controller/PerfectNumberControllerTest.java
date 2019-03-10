package com.worldline.controller;

import org.json.JSONException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import com.worldline.PerfectNumberApplication;
import com.worldline.ToListArgumentConverter;

import net.minidev.json.JSONArray;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Performs some test cases to check outputs of PerfectNumberController against normal and invalid inputs. 
 * Also, checks idempotency of PerfectNumberController operations against several identical invocations. 
 * @author mohsen
 *
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = PerfectNumberApplication.class)
@AutoConfigureMockMvc
public class PerfectNumberControllerTest {
	
	@Autowired
    private MockMvc mockMvc;
	
	/**
	 * Checks if correct outputs are produced by /rest/perfectNumber/{number} when a non-zero positive long integer is given. 
	 * The CSV source contains some long integers as input along with a boolean as expected output. 
	 * @param numberString is a non-zero positive long integer to check if is perfect 
	 * @param expected true when we expect numberString is perfect otherwise, false 
	 * @throws Exception
	 */
	@ParameterizedTest
	@CsvSource({
				// perfect numbers
				"6, true", 
				"28, true", 
				"496, true", 
				"8128, true", 
				"33550336, true", 
				"8589869056, true", 
				"137438691328, true", 
				"2305843008139952128, true",
				// non-perfect numbers
				"26, false", 
				"500, false", 
				"9000, false", 
				"98562364554, false"})
	public void check(String numberString, Boolean expected) throws Exception {
		mockMvc.perform(get("/rest/perfectNumber/" + numberString))
		.andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("responseCode", is("OK")))
        .andExpect(jsonPath("message", is(expected)));
	}
	
	/**
	 * Checks if REQUEST_ERROR is returned as responseCode by /rest/perfectNumber/{number} when an invalid input is given.
	 * @param invalidNumberString is a string that represents an invalid input including zero, a negative integer, and non-numerical strings
	 * @throws Exception
	 */
	@ParameterizedTest
	@ValueSource(strings = {"0", "-1", "a", "*"})
	public void faultyCheck(String invalidNumberString) throws Exception {
		mockMvc.perform(get("/rest/perfectNumber/" + invalidNumberString))
		.andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("responseCode", is("REQUEST_ERROR")));
	}
	
	/**
	 * Checks idempotency of /rest/perfectNumber/{number} against several identical invocations. The CSV source contains some long integers 
	 * as input number along with a boolean as expected output and the number of invocations.
	 * @param numberString is a non-zero positive long integer to check if is perfect
	 * @param expected true when we expect numberString is perfect otherwise, false 
	 * @param iterationsNumber is the number of invocations
	 * @throws Exception
	 */
	@ParameterizedTest
	@CsvSource({"137438691328, true, 2","98562364554, false, 2", "137438691328, true, 4","98562364554, false, 4"
		, "137438691328, true, 8","98562364554, false, 8"})
	public void idempotentCheck(String numberString, Boolean expected, int invocationsNumber) throws Exception {
		for (int i = 0; i < invocationsNumber; i++) {
			mockMvc.perform(get("/rest/perfectNumber/" + numberString))
			.andExpect(status().isOk())
	        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        .andExpect(jsonPath("responseCode", is("OK")))
	        .andExpect(jsonPath("message", is(expected)));
		}
	}
	
	/**
	 * Checks if all perfect numbers are returned by /rest/perfectNumbersInRange/{lowerBound}/{upperBound} when the lower and upper bounds 
	 * of a range are given. The CSV source contains some lower and upper bounds as input along with a list of perfect numbers as expected output. 
	 * @param lowerBoundString is a non-zero positive long integer as the lower bound of the range
	 * @param upperBoundString is a non-zero positive long integer as the upper bound of the range
	 * @param expected is a list of perfect numbers as expected output
	 * @throws Exception
	 */
	@ParameterizedTest
	@CsvSource({"1, 100, [6 28]", "100, 10000, [496 8128]", "1 , 200000000000, [6 28 496 8128 33550336 8589869056 137438691328]"})
	public void findAllInRange(String lowerBoundString, String upperBoundString, 
			@ConvertWith(ToListArgumentConverter.class) List<Long> expected) throws Exception {
    	JSONArray jsonArray = toJSONArray(expected);
		mockMvc.perform(get("/rest/perfectNumbersInRange/" + lowerBoundString + "/" + upperBoundString))
		.andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("responseCode", is("OK")))
        .andExpect(jsonPath("message", is(jsonArray)));
	}

	private JSONArray toJSONArray(List<Long> expected) throws JSONException {
		JSONArray jsonArray = new JSONArray();
    	org.json.JSONArray jsonArrayTemp = new org.json.JSONArray(expected.toString());
    	for (int i = 0; i < jsonArrayTemp.length(); i++) {
    		jsonArray.add(jsonArrayTemp.get(i));
		}
		return jsonArray;
	}
	
	/**
	 * Checks if REQUEST_ERROR is returned by /rest/perfectNumbersInRange/{lowerBound}/{upperBound} as responseCode when some invalid inputs 
	 * are given.
	 * @param invalidLowerBoundString is zero, negative integer, or non-numerical strings as invalid lower bound of the range
	 * @param invalidUpperBoundString is zero, negative integer, or non-numerical strings as invalid upper bound of the range
	 * @throws Exception
	 */
	@ParameterizedTest
	@CsvSource({"0, 10", "100, 50", "-1, 100", "-10, -1", "a, b", "1, a", "a, 1"})
	public void faultyFindAllInRange(String invalidLowerBoundString, String invalidUpperBoundString) throws Exception {
		mockMvc.perform(get("/rest/perfectNumbersInRange/" + invalidLowerBoundString + "/" + invalidUpperBoundString))
		.andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("responseCode", is("REQUEST_ERROR")));
	}
	
	/**
	 * Checks idempotency of /rest/perfectNumbersInRange/{lowerBound}/{upperBound} against several identical invocations. The CSV source 
	 * contains some lower and upper bounds as input along with a list of perfect numbers as expected output and the number of invocations.
	 * @param lowerBoundString is a non-zero positive long integer as the lower bound of the range
	 * @param upperBoundString is a non-zero positive long integer as the upper bound of the range
	 * @param expected is a list of perfect numbers as expected output
	 * @param iterationsNumber is the number of invocations
	 * @throws Exception
	 */
	@ParameterizedTest
	@CsvSource({"1 , 200000000000, [6 28 496 8128 33550336 8589869056 137438691328], 2", 
		"1 , 200000000000, [6 28 496 8128 33550336 8589869056 137438691328], 4",
		"1 , 200000000000, [6 28 496 8128 33550336 8589869056 137438691328], 8"})
	public void idempotentFindAllInRange(String lowerBoundString, String upperBoundString, 
			@ConvertWith(ToListArgumentConverter.class) List<Long> expected, int iterationsNumber) throws Exception {
		for (int i = 0; i < iterationsNumber; i++) {
			JSONArray jsonArray = toJSONArray(expected);
			mockMvc.perform(get("/rest/perfectNumbersInRange/" + lowerBoundString + "/" + upperBoundString))
			.andExpect(status().isOk())
	        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        .andExpect(jsonPath("responseCode", is("OK")))
	        .andExpect(jsonPath("message", is(jsonArray)));
		}
	}
    
}
