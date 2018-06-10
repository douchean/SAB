package student;

import java.math.BigDecimal;

public class dd140526_Pair<A, B> implements operations.PackageOperations.Pair<A, B> {

	private A a;
	private B b;

	public dd140526_Pair(A a, B b) {
		super();
		this.a = a;
		this.b = b;
	}

	
	@Override
	public A getFirstParam() {
		return a;
	}

	@Override
	public B getSecondParam() {
		return b;
	}

}
