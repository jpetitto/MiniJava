/*
 * 
 * A small program to test the parsing of an expression according to the
 * binary operator precedence levels.
 * 
 * From lowest to highest:
 * &&
 * <
 * +, -
 * *
 * [], .
 * 
 */

class BinopExp {
	public static void main(String[] args) {
		// should parenthesize as (args[(1-2+2)]<5) && ((4+(5*3))<(args.length))
		System.out.println(args[(1-1+2)] < 5 && 4 + 5 * 3 < args.length);
	}
}