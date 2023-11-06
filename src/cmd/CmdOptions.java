package cmd;

public class CmdOptions {

	public boolean online;
	public boolean multipleRace;
	public String path;
	public int verbosity;
	public String excludeList;

	public CmdOptions() {
		this.online = true;
		this.multipleRace = true;
		this.path = null;
		this.verbosity = 0;
		this.excludeList = null;
	}
	
	public String toString(){
		String str = "";
		str += "online			" + " = " + this.online					+ "\n";
		str += "multipleRace	" + " = " + this.multipleRace			+ "\n";	
		str += "path			" + " = " + this.path					+ "\n";
		str += "verbosity		" + " = " + this.verbosity				+ "\n";
		str += "excludeList		" + " = " + this.excludeList			+ "\n";
		return str;
	}

}
