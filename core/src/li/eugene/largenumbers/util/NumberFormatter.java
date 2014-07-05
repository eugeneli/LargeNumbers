package li.eugene.largenumbers.util;

import java.text.DecimalFormat;

public class NumberFormatter
{
	private static DecimalFormat formatter = new DecimalFormat("#,###");
	private static String[] suffix = new String[]{"k","m","b","t","quadrillion","quintillion"};
	
	public static String addCommas(long num) { return formatter.format(num); }
	
	public static String shorten(Long num)
	{
		long number = num.longValue();
	    int size = (number != 0) ? (int) Math.log10(number) : 0;
	    if (size >= 3){
	        while (size % 3 != 0) {
	            size = size - 1;
	        }
	    }
	    double notation = Math.pow(10, size);
	    String result = (size >= 3) ? + (Math.round((number / notation) * 100) / 100.0d)+suffix[(size/3) - 1] : + number + "";
	    return result;
	}
}
