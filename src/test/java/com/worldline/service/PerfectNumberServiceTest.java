package com.worldline.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.worldline.ToListArgumentConverter;
import com.worldline.service.api.PerfectNumberService;

/**
 * Like PerfectNumberControllerTest, performs some test cases to check outputs of PerfectNumberService against normal and invalid inputs. 
 * @author mohsen
 *
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PerfectNumberServiceTest {
	
	@Autowired
	PerfectNumberService perfectNumberService;

	/**
	 * Checks if correct outputs are returned by check when a non-zero positive long integer is given. The CSV source contains some long integers 
	 * as input along with a boolean as expected output. 
	 * @param number is a non-zero positive long integer to check if is perfect 
	 * @param expected true when we expect number is perfect otherwise, false 
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
				// none-perfect numbers
				"26, false", 
				"500, false", 
				"9000, false", 
				"98562364554, false"})
	public void check(Long number, Boolean expected) throws PerfectNumberException {
		assertEquals(expected, perfectNumberService.check(number));
	}
	
	/**
	 * Checks if PerfectNumberException is raised by check when an invalid input is given.
	 * @param invalidNumber is an invalid input including zero and a negative integer
	 */
	@ParameterizedTest
	@ValueSource(longs = {0, -1})
	public void faultyCheck(Long invalidNumber) {
		assertThrows(PerfectNumberException.class, () -> perfectNumberService.check(invalidNumber));
	}
	
	/**
	 * Checks if all perfect numbers are returned by findAllInRange when the lower and upper bounds of a range are given. The CSV source contains some 
 	 * lower and upper bounds as input along with a list of perfect numbers as expected output. 
	 * @param lowerBound is a non-zero positive long integer as the lower bound of the range
	 * @param upperBound is a non-zero positive long integer as the upper bound of the range
	 * @param expected is a list of perfect numbers as expected output
	 * @throws Exception
	 */
	@ParameterizedTest
	@CsvSource({"1, 100, [6 28]", "100, 10000, [496 8128]", "1 , 200000000000, [6 28 496 8128 33550336 8589869056 137438691328]"})
	public void findAllInRange(Long lowerBound, Long upperBound, @ConvertWith(ToListArgumentConverter.class) List<Long> expected) throws PerfectNumberException {
		assertEquals(expected, perfectNumberService.findAllInRange(lowerBound, upperBound));
	}
	
	/**
	 * Checks if PerfectNumberException is raised by findAllInRange when some invalid inputs are given.
	 * @param invalidLowerBound is zero, negative integers, or integers greater than the upper bound as invalid lower bound of the range
	 * @param invalidUpperBound is zero, negative integer, or integers less than the lower bound as invalid upper bound of the range
	 * @throws Exception
	 */
	@ParameterizedTest
	@CsvSource({"0, 10", "100, 50", "-1, 100", "-10, -1"})
	public void faultyFindAllInRange(Long invalidLowerBound, Long invalidUpperBound) {
		assertThrows(PerfectNumberException.class, () -> perfectNumberService.findAllInRange(invalidLowerBound, invalidUpperBound));
	}
	
}
