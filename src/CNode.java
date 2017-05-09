
public class CNode extends ENode {
	
	double value;
	
	private String strValue;
	
	public CNode(double value){
		super('C',null,null);
		this.value = value;
//		double factor = 1e7; // = 1 * 10^5 = 100000.
//		value = Math.round(value * factor) / factor;
		strValue = Double.toString(value);
		
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public String getStrValue() {
		return strValue;
	}

	public void setStrValue(String strValue) {
		this.strValue = strValue;
	}
	
	
	

}
