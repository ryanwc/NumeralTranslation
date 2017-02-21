package app;

/**
 * Translate values from one unit system to another.
 * 
 * @author ryanwilliamconnor
 *
 */
public class Translator {

	/*
	 * Roman numerals are based on seven symbols:
	 * Symbol	I	V	X	L	C	D	M
	 * Value	1	5	10	50	100	500	1,000
	 * 
	 * Numbers are formed by combining symbols together and adding the values. 
	 * For example, MMVI is 1000 + 1000 + 5 + 1 = 2006. 
	 * Generally, symbols are placed in order of value, starting with the 
	 * largest values. When smaller values precede larger values, 
	 * the smaller values are subtracted from the larger values, and the 
	 * result is added to the total. 
	 * For example MCMXLIV = 1000 + (1000 - 100) + (50 - 10) + (5 - 1) = 1944.
	 * 
	 * Rules:
	 * 1. The symbols "I", "X", "C", and "M" can be repeated three times in 
	 * succession, but no more. (They may appear four times if the third and 
	 * fourth are separated by a smaller value, such as XXXIX.) "D", "L", and 
	 * "V" can never be repeated.
	 * 2. "I" can be subtracted from "V" and "X" only. "X" can be subtracted 
	 * from "L" and "C" only. "C" can be subtracted from "D" and "M" only. "V", 
	 * "L", and "D" can never be subtracted.
	 * 3. Only one small-value symbol may be subtracted from any 
	 * large-value symbol.
	 * 4. A number written in Arabic numerals can be broken into digits. 
	 * For example, 1903 is composed of 1, 9, 0, and 3. To write the Roman 
	 * numeral, each of the non-zero digits should be treated separately. 
	 * In the above example, 1,000 = M, 900 = CM, and 3 = III. 
	 * Therefore, 1903 = MCMIII.
	 */
}
