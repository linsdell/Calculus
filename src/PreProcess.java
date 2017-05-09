/**
 * Calculus Project.
 * This client drives the initial version of your Function class that inserts
 * the explicit multiplication operator (*) wherever necessary as well as any other 
 * 'conditioning' the function definition may require prior to parsing.     
 */

public class PreProcess {

  static String[] list = {
    "2X^2-3x+1", "xsin(x)", "x^2ln(3x)", "(2x-1)(1-4x^2)",
    "x^(3E)", "-1.5abs(2x)", "4sqrt(5-3.1x)", "(3x^4-4x^3)/(2-cos(1/(2x)))",
    "tan(2x)/3x", "e^atan(px)","3ePE"};
	
  public static void main(String[] args) {
    for (String def:list) {
      Function f = new Function(def);
      System.out.println(def+" preprocessed yields "+f);
    } 
  }
}