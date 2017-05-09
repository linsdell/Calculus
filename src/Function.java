
public class Function {

	String vars = "xy";
	private String variable = "x";
	char var = 'x';

	private double x;

	private String[] functions = { "abs", "acos", "asin", "atan", "cos", "cot", "csc", "ln", "log", "sin", "sqrt",
			"sqr", "tan", "sec" };

	private String digits = ".0123456789";
	private String constants = "pe";

	private String original, y;

	private String f;

	ENode yTree, dyTree, d2yTree;

	private int c; // cursor

	private int errorCode;



	static String errors[] = { "All good", "Division by zero", "Domain Error",
			"Invalid number of terms, please use number between 2 and 10" };
	static double EPSILON = 1E-6;

	private boolean isEqual(double a, double b) {
		return Math.abs(a - b) < EPSILON;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

	static String display(ENode node) {
		return node == null ? "" : display(node.getLeft()) + node.getType() + '\n' + display(node.getRight());

	}
	
//	static ENode prune(ENode node){
//		if(node.getLeft().getType()=='O'&&node.getLeft().getType()=='C'&&node.getRight().getType()=='C')
//			node.setLeft(evaluate(node.getLeft()));
//		else
//			prune(node.getLeft());
//			prune(node.getRight());
//	}

	ENode base;

	/**
	 * Function constructor that takes a base function as well as the number of
	 * terms for a Maclaurin polynomial approximation
	 * 
	 * @param f
	 *            the base function
	 * @param n
	 *            the number of terms (2-10)
	 */
	public Function(String f, int n) {
		y = preProcess(f);
		base = parse();
	

		// error code which stops function if n is not between 2 and 10
		if (n > 10 || n < 2) {
			this.setErrorCode(3);
			System.out.println(errors[getErrorCode()]);
			return;
		}

		// sets up the String definition of the Maclaurin polynomial
		y = "(" + derivative(0) + ")";
		for (int i = 1; i <= n; i++) {
			String term = "+((" + derivative(i) + "/" + factorial(i) + ")*" + xMultiple(i) + ")";
			y = y.concat(term);
		}

		// converts the String definition to an expression tree
		yTree = parse();

		System.out.println(f);

	}

	/**
	 * return the nth derivative of a base function at 0
	 * 
	 * @param n
	 *            the derivative of the base we are finding
	 * @return - a double value for the derivative at 0
	 */
	private double derivative(int n) {
		ENode temp = duplicate(base);
		for (int i = 0; i < n; i++) {
			temp = derive(temp);
		}
		this.x = 0.0;
		return evaluate(temp);
	}

	/**
	 * returns the factorial of the passed number (n*(n-1)*(n-2)...)
	 * 
	 * @param n
	 *            the starting number
	 * @return - the value of the calculation
	 */
	private double factorial(int n) {
		double product = n;
		n--;
		while (n > 0) {
			product = product * n;
			n--;
		}
		return product;
	}

	/**
	 * returns a string of the variable x to a passed power
	 * 
	 * @param n
	 *            the exponent x is be raised to
	 * @return a - string which can be put into the parser
	 */
	private String xMultiple(int n) {
		return "x^" + n;
	}

	public Function(String def) {
		//original = def;
		//y = preProcess(def);

		// yTree = new ONode('+', new ONode('*', new CNode(2.0), new
		// VNode(var)), new CNode(1.0));
		// yTree = new ONode('-', new VNode(var), new ONode('^', new
		// VNode(var),new CNode(2)));

		// yTree = new FNode(8,new ONode('*',new CNode(100),new FNode(9, new
		// VNode(var))));

		//yTree = parse();
		// System.out.println(display(yTree));
		//ENode copy = duplicate(yTree);
		// System.out.printf("Duplicate of %s is
		// %s%n",reportStructure(0),reportHelper(copy));

		//dyTree = derive(yTree);
		// System.out.println(display(dyTree));
		// dyTree = prune(dyTree);
		//System.out.println("dyTree Structure: " + reportStructure(1));

		//d2yTree = derive(dyTree);
		//System.out.println("dyTree Structure: " + reportStructure(2));

		//toString();

		//System.out.println(y);
	}

	private ENode duplicate(ENode node) {
		if (node != null) {
			switch (node.getType()) {
			case 'C':
				return new CNode(((CNode) node).getValue());
			case 'V':
				return new VNode(((VNode) node).getVar());
			case 'O':
				ENode leftDupe = duplicate(node.getLeft());
				ENode rightDupe = duplicate(node.getRight());
				return new ONode(((ONode) node).getOp(), leftDupe, rightDupe);
			case 'F':
				leftDupe = duplicate(node.getLeft());
				return new FNode(((FNode) node).getWhich(), leftDupe);
			}
		}
		return null;
	}

	private ENode derive(ENode node) {
		if (node != null) {
			switch (node.getType()) {
			case 'C':
				return new CNode(0.0);
			case 'V':
				return new CNode(1.0);
			case 'F':
				switch (((FNode) node).getWhich()) {
				/* abs */ case 0:
					return new ONode('*', new ONode('/', new FNode(0, node.getLeft()), node.getLeft()),
							derive(node.getLeft()));
				/* acos */ case 1:
					return new ONode('*',
							new ONode('/', new CNode(-1),
									new FNode(10, new ONode('-', new CNode(1), new FNode(11, node.getLeft())))),
							derive(node.getLeft()));
				/* asin */ case 2:
					return new ONode('*',
							new ONode('/', new CNode(1),
									new FNode(10, new ONode('-', new CNode(1), new FNode(11, node.getLeft())))),
							derive(node.getLeft()));
				/* atan */ case 3:
					return new ONode('*', derive(node.getLeft()), new ONode('/', new CNode(1),
							new ONode('+', new CNode(1), new ONode('^', node.getLeft(), new CNode(2)))));
				/* cos */ case 4:
					return new ONode('*', new ONode('*', new CNode(-1), new FNode(9, node.getLeft())),
							derive(node.getLeft()));
				/* cot */ case 5:
					return new ONode('*',
							new ONode('*', new CNode(-1), new ONode('^', new FNode(6, node.getLeft()), new CNode(2))),
							derive(node.getLeft()));
				/* csc */ case 6:
					return new ONode('*', new ONode('*', new ONode('*', new CNode(-1), new FNode(5, node.getLeft())),
							new FNode(6, node.getLeft())), derive(node.getLeft()));
				/* ln */ case 7:
					return new ONode('*', new ONode('/', new CNode(1), node.getLeft()), derive(node.getLeft()));
				/* log */ case 8:
					return new ONode('*',
							new ONode('/', new CNode(1), new ONode('*', node.getLeft(), new FNode(7, new CNode(10)))),
							derive(node.getLeft()));
				/* sin */ case 9:
					return new ONode('*', new FNode(4, node.getLeft()), derive(node.getLeft()));
				/* sqrt */ case 10:
					return new ONode('*',
							new ONode('/', new CNode(1), new ONode('*', new CNode(2), new FNode(10, node.getLeft()))),
							derive(node.getLeft()));
				/* sqr */ case 11:
					return new ONode('*', new ONode('*', new CNode(2), node.getLeft()), derive(node.getLeft()));
				/* tan */ case 12:
					return new ONode('*', new ONode('^', new FNode(13, node.getLeft()), new CNode(2)),
							derive(node.getLeft()));
				/* sec */ case 13:
					return new ONode('*', new ONode('*', new FNode(12, node.getLeft()), new FNode(13, node.getLeft())),
							derive(node.getLeft()));

				}
			case 'O':
				switch (((ONode) node).getOp()) {
				case '+':
					return sumRule(node);
				case '-':
					return sumRule(node);
				case '*':
					return productRule(node);
				case '/':
					return quotientRule(node);
				case '^':
					return powerRule(node);
				}
			}
		}
		return null;
	}

	private ENode sumRule(ENode node) {
		return new ONode(((ONode) node).getOp(), derive(node.getLeft()), derive(node.getRight()));
	}

	private ENode productRule(ENode node) {
		ENode leftTree = new ONode('*', derive(node.getLeft()), duplicate(node.getRight()));
		ENode rightTree = new ONode('*', duplicate(node.getLeft()), derive(node.getRight()));
		return new ONode('+', leftTree, rightTree);
	}

	private ENode quotientRule(ENode node) {
		ENode leftLeftTree = new ONode('*', derive(node.getLeft()), duplicate(node.getRight()));
		ENode leftRightTree = new ONode('*', duplicate(node.getLeft()), derive(node.getRight()));
		ENode leftTree = new ONode('-', leftLeftTree, leftRightTree);
		ENode rightTree = new ONode('*', duplicate(node.getRight()), duplicate(node.getRight()));
		return new ONode('/', leftTree, rightTree);
	}

	private ENode powerRule(ENode node) {
		if (node.getRight().getType() == 'C') {
			ENode leftTree = new ONode('*', duplicate(node.getRight()),
					new ONode('^', duplicate(node.getLeft()), new CNode(((CNode) node.getRight()).getValue() - 1)));
			return new ONode('*', leftTree, derive(node.getLeft()));
		} else {
			ENode leftTree = duplicate(node);
			ENode rightLeftTree = new ONode('*', derive(node.getRight()), new FNode(7, duplicate(node.getLeft())));
			ENode rightRightTree = new ONode('*',
					new ONode('*', duplicate(node.getRight()), new ONode('/', new CNode(1.0), node.getLeft())),
					derive(node.getLeft()));
			ENode rightTree = new ONode('+', rightLeftTree, rightRightTree);
			return new ONode('*', leftTree, rightTree);
		}
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ PARSER
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private ENode parse() {
		c = 0;
		return expression();
	}

	// recursive descent parser.....
	private ENode expression() {
		ENode ref = term();
		while (hasMore() && (y.charAt(c) == '+' || y.charAt(c) == '-'))
			ref = new ONode(y.charAt(c++), ref, term());
		return ref;
	}

	private ENode term() {
		ENode ref = factor();
		if (hasMore() && (y.charAt(c) == '*' || y.charAt(c) == '/')) {
			ref = new ONode(y.charAt(c++), ref, factor());
		}
		return ref;

	}

	private ENode factor() {
		ENode ref = power();
		if (hasMore() && y.charAt(c) == '^')
			ref = new ONode(y.charAt(c++), ref, power());
		return ref;

	}

	private ENode power() {
		ENode ref = primitive();
		if (hasMore() && y.charAt(c) == '^')
			ref = new ONode(y.charAt(c++), ref, primitive());
		return ref;
	}

	private ENode primitive() {
		if (y.charAt(c) == 'e') {
			c++;
			return new CNode(Math.E);
		}
		if (y.charAt(c) == 'p') {
			c++;
			System.out.println("P!");
			return new CNode(Math.PI);
		}
		if (isDigit()) {
			int save = c++;
			while (hasMore() && isDigit())
				c++;
			double value = Double.parseDouble(y.substring(save, c));
			return new CNode(value);
		}
		if (isVariable()) {
			return new VNode(y.charAt(c++));
		}
		if (y.charAt(c) == '(') {
			c++;
			ENode node = expression();
			c++;
			return node;
		}

		int save = c;
		while (isAlpha()) {
			c++;
		}
		String function = "";
		function = y.substring(save, c);
		for (int i = 0; i < functions.length; i++) {
			if (function.equals(functions[i]))
				return new FNode(i, primitive());
		}

		return null;
	}

	private boolean isAlpha() {
		return (int) y.charAt(c) >= 97 && (int) y.charAt(c) <= 122;
	}

	private boolean isVariable() {
		return vars.indexOf(y.charAt(c)) >= 0;
	}

	private boolean isDigit() {
		return digits.indexOf(y.charAt(c)) >= 0;
	}

	private boolean hasMore() {
		return c < y.length();
	}

	public String reportStructure(int which) {
		switch (which) {
		case 0:
			return reportHelper(yTree);
		case 1:
			return reportHelper(dyTree);
		case 2:
			return reportHelper(d2yTree);
		case 3:
			return reportHelper(base);
		default:
			return "";
		}

	}

	private String reportHelper(ENode node) {
		if (node != null) {
			return reportHelper(node.getLeft()) + node.getType() + reportHelper(node.getRight());
		}
		return "";
	}

	public double f(int which, double x) {
		this.x = x;
		switch (which) {
		case 0:
			return evaluate(yTree);
		case 1:
			return evaluate(dyTree);
		case 2:
			return evaluate(d2yTree);
		default:
			return 0.0;
		}
	}

	private double evaluate(ENode node) {
		double left, right;
		// if(node instanceof CNode)
		if (node != null) {
			switch (node.getType()) {
			case 'C':
				return ((CNode) node).getValue();
			case 'V':
				return x;
			case 'O':
				left = evaluate(node.getLeft());
				right = evaluate(node.getRight());
				switch (((ONode) node).getOp()) {
				case '+':
					return left + right;
				case '-':
					return left - right;
				case '*':
					return left * right;
				case '/':
					if (isEqual(right, 0)) {
						this.setErrorCode(1);
						return 1;
					}
					return left / right;
				case '^':
					return Math.pow(left, right);
				default:
					return 0.0;
				}
			case 'F':
				left = evaluate(node.getLeft());
				switch (((FNode) node).getWhich()) {
				/* abs */ case 0:
					return Math.abs(left);
				/* acos */ case 1:
					return Math.acos(left);
				/* asin */ case 2:
					return Math.asin(left);
				/* atan */ case 3:
					return Math.atan(left);
				/* cos */ case 4:
					return Math.cos(left);
				/* cot */ case 5:
					return (1 / Math.tan(left));
				/* csc */ case 6:
					return (1 / Math.sin(left));
				/* ln */ case 7:
					return Math.log(left);
				/* log */ case 8:
					return Math.log10(left);
				/* sin */ case 9:
					return Math.sin(left);
				/* sqrt */ case 10:
					return Math.sqrt(left);
				/* sqr */ case 11:
					return Math.pow(left, 2);
				/* tan */ case 12:
					return Math.tan(left);
				/* sec */ case 13:
					return (1 / Math.cos(left));
				default:
					return 0.0;
				}
			default:
				return 0.0;
			}
		}
		return 0.0;
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Pre-Process ~~~~~~~~~~~~~~~~~
	private String preProcess(String fx) {

		fx = fx.toLowerCase();

		// checks for digits or variable multiplying any functions or variable
		for (int i = 0; i < digits.length(); i++) {
			for (int j = 0; j < functions.length; j++) {
				for (int k = 0; k < constants.length(); k++) {
					for (int l = constants.length() - 1; l >= 0; l--) {
						// digits * functions
						fx = fx.replace("" + digits.charAt(i) + functions[j] + "",
								"" + digits.charAt(i) + "*" + functions[j] + "");
						fx = fx.replace("" + functions[j] + digits.charAt(i) + "",
								"" + functions[j] + "*" + digits.charAt(i) + "");
						// variable * functions
						fx = fx.replace("" + functions[j] + variable + "", "" + functions[j] + "*" + variable + "");
						fx = fx.replace("" + variable + functions[j] + "", "" + variable + "*" + functions[j] + "");
						// variable * digits
						fx = fx.replace("" + digits.charAt(i) + variable + "",
								"" + digits.charAt(i) + "*" + variable + "");
						fx = fx.replace("" + variable + digits.charAt(i) + "",
								"" + variable + "*" + digits.charAt(i) + "");
						// digits * bracketed expression
						fx = fx.replace("" + digits.charAt(i) + "(", "" + digits.charAt(i) + "*(");
						// variable * bracketed expression
						fx = fx.replace("" + variable + "(", "" + variable + "*(");
						// bracketed expression * bracketed expression
						fx = fx.replace(")(", ")*(");

						// deal with negative numbers
						fx = fx.replace("(-" + digits.charAt(i) + "", "(0-" + digits.charAt(i) + "");
						if (fx.charAt(0) == '-')
							fx = "0" + fx;

						// constant * constant
						fx = fx.replace("" + constants.charAt(k) + constants.charAt(l) + "",
								"" + constants.charAt(k) + "*" + constants.charAt(l) + "");
						// constant * number
						fx = fx.replace("" + constants.charAt(k) + digits.charAt(i) + "",
								"" + constants.charAt(k) + "*" + digits.charAt(i) + "");
						fx = fx.replace("" + digits.charAt(i) + constants.charAt(k) + "",
								"" + digits.charAt(i) + "*" + constants.charAt(k) + "");
						// constant * variable
						fx = fx.replace("" + constants.charAt(k) + variable + "",
								"" + constants.charAt(k) + "*" + variable + "");
						fx = fx.replace("" + variable + constants.charAt(k) + "",
								"" + variable + "*" + constants.charAt(k) + "");

					}
				}

			}
		}
		return fx;

	}

	public String toString() {
		String master = "";
		master = "" + y + "";
		return master;
	}

}
