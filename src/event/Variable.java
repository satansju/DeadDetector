package event;

public class Variable extends Decoration {

	public static int variableCountTracker = 0;

	public Variable() {
		this.id = variableCountTracker;
		variableCountTracker++;
		this.name = "__variable::" + this.id + "__";
	}

	public Variable(String sname) {
		this.id = variableCountTracker;
		variableCountTracker++;
		this.name = sname;
	}

}
