import javax.swing.JOptionPane;

/**************************************************
 * Calculus Project: ENode and reportStructure() *
 **************************************************/
public class Calculus {

	static String[] list = { "2X^2-3x+1", "xsin(x)", "x^2ln(3x)", "(2x-1)(1-4x^2)", "x^(3E)", "-1.5abs(2x)",
			"4sqrt(5-3.1x)", "(3x^4-4x^3)/(2-cos(1/(2x)))", "tan(2x)/3x", "e^atan(px)", "3ePE" };

	static char prime = '\u2032';
	static char doublePrime = '\u2033';

	static Function f;

	public static void main(String[] args) {
		// //String def = "2x+1";
		// //String def = "x-x^2";
		// //String def = "log(100sin(x))";
		//
		//// String input = JOptionPane.showInputDialog("Enter an
		// expression","1-2x;-4,4");
		//// String details[] = input.split(",");
		//// String def = details[0];
		//// double a = Double.parseDouble(details[1]);
		//// double b = Double.parseDouble(details[2]);
		//// double step = 0.1;
		//
		//
		// String def = "3ePE";
		// Function f = new Function(def);
		// double x = 1.0;
		//
		//// for(double x1 =a ;x1<=b;x1+=step){
		////
		//// }
		//
		//
		//
		//
		//
		// System.out.printf("f(x)=%s: %s\n",def,f.reportStructure(0));
		// System.out.printf("f(x)=%s: %s\n",def,f.reportStructure(1));
		// System.out.printf("f(x)=%s: %s\n",def,f.reportStructure(2));
		//
		//// f.setErrorCode(0);
		//// double result = f.f(0, x);
		//// System.out.println(f.errors[f.getErrorCode()]);
		//
		// System.out.printf("f(%3.1f)=%3.2f\n", x,f.f(0,x));
		//
		// System.out.printf("f%c(%3.1f)=%3.2f\n",prime, x,f.f(1,x));
		// System.out.printf("f%c(%3.1f)=%3.2f\n", doublePrime,x,f.f(2,x));
		//

		/*
		 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~ EXAM CODE~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		 */
		String[] macLaurin = { "sin(x)", "cos(2x)", "e^x" };

		// using terms 9-10 GC overhead limit exceeded, I believe I need to use
		// prune to make the derivative expression trees smaller

		for (int i = 0; i < macLaurin.length; i++) {
			f = new Function(macLaurin[i], 8);
			System.out.println("   x\t  Builtin\t MacLaurin");
			for (double domain = -0.5; domain < 0.5; domain += 0.1) {
				System.out.printf("%5.1f\t", domain);
				switch (i) {
				case 0:
					System.out.printf("%10.7f\t", Math.sin(domain));
					break;
				case 1:
					System.out.printf("%-9.8f\t", Math.cos(2 * domain));
					break;
				case 2:
					System.out.printf("%-9.8f\t", Math.exp(domain));
					break;
				}
				System.out.printf("%10.7f\n", f.f(0, domain));
			}
		}

	}

}