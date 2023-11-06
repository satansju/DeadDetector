package util.vectorclock;

import java.util.Vector;

public class VectorClock {

	private int dim;
	private Vector<Integer> clock;

	public VectorClock(int d) {
		this.dim = d;
		this.clock = new Vector<>(dim);
		for (int ind = 0; ind < this.dim; ind++) {
			this.clock.addElement(0);
		}
	}

	public VectorClock(int d, int val) {
		this.dim = d;
		this.clock = new Vector<>(dim);
		for (int ind = 0; ind < this.dim; ind++) {
			this.clock.addElement(val);
		}
	}

	public VectorClock(VectorClock fromVectorClock) {
		this.dim = fromVectorClock.getDim();
		this.clock = new Vector<>(dim);
		Vector<Integer> fromClock = fromVectorClock.getClock();
		for (int ind = 0; ind < fromVectorClock.getDim(); ind++) {
			this.clock.addElement(fromClock.get(ind));
		}
	}

	public int getDim() {
		if(!(this.dim == this.clock.size())){
			throw new IllegalArgumentException("Mismatch in dim and clock size"); 
		}
		return this.dim;
	}

	public Vector<Integer> getClock() {
		return this.clock;
	}

	public String toString() {
		return this.clock.toString();
	}

	/* public void inc(int ind) {
		if(! ((ind < this.dim) && (ind >= 0)) ){
			throw new IllegalArgumentException("You are attempting to access the vector clock with an illegal index");
		}
		int new_clock_val = this.clock.get(ind) + 1;
		this.clock.set(ind, (Integer) new_clock_val);
	} */

	public boolean isZero() {
		boolean itIsZero = true;
		for (int ind = 0; ind < this.dim; ind++) {
			int thisVal = this.clock.get(ind);
			if (thisVal != 0) {
				itIsZero = false;
				break;
			}
		}
		return itIsZero;
	}

	public boolean isEqual(VectorClock vc) {
		if(! (this.dim == vc.getDim()) ){
			throw new IllegalArgumentException("Mismatch in this.dim and argument.dim"); 
		}
		boolean itIsEqual = true;
		Vector<Integer> vcClock = vc.getClock();
		for (int ind = 0; ind < this.dim; ind++) {
			int thisVal = this.clock.get(ind);
			int vcVal = vcClock.get(ind);
			// System.out.println("Comparing: " + thisVal + " | " + vcVal);
			if (thisVal != vcVal) {
				itIsEqual = false;
				break;
			}
		}
		return itIsEqual;
	}

	public boolean isLessThanOrEqual(VectorClock vc) {
		if(! (this.dim == vc.getDim()) ){
			throw new IllegalArgumentException("Mismatch in this.dim and argument.dim"); 
		}
		boolean lessThanOrEqual = true;
		Vector<Integer> vcClock = vc.getClock();
		for (int ind = 0; ind < this.dim; ind++) {
			int thisVal = this.clock.get(ind);
			int vcVal = vcClock.get(ind);
			if (!(thisVal <= vcVal)) {
				lessThanOrEqual = false;
				break;
			}
		}
		return lessThanOrEqual;
	}

	public void setToZero() {
		for (int ind = 0; ind < this.dim; ind++) {
			this.clock.set(ind, 0);
		}
	}
	
	public void copyFrom(VectorClock vc) {
		if(! (this.dim == vc.getDim()) ){
			throw new IllegalArgumentException("Mismatch in this.dim and argument.dim"); 
		}
		for (int ind = 0; ind < this.dim; ind++) {
			this.clock.set(ind, vc.clock.get(ind));
		}
	}	
	
	private void updateMax2(VectorClock vc) {
		if(! (this.dim == vc.getDim()) ){
			throw new IllegalArgumentException("Mismatch in this.dim and argument.dim"); 
		}
		for (int ind = 0; ind < this.dim; ind++) {
			int this_c = this.clock.get(ind);
			int vc_c = vc.clock.get(ind);
			int max_c = Math.max(this_c, vc_c);
			this.clock.set(ind, max_c);
		}
	}
	
	public void updateWithMax(VectorClock... vcList) {
		if(! (vcList.length >= 1) ){
			throw new IllegalArgumentException("Insuffiecient number of arguments provided"); 
		}
		for (int i = 1; i < vcList.length; i++) {
			if (vcList[i].equals(this)) throw new IllegalArgumentException("If \'this\' is one of the arguments, then it must be the first");
		}

		//this.setToZero();
		//this.copyFrom(vcList[0]);
		for (int i = 0; i < vcList.length; i++) {
			VectorClock vc = vcList[i];
			if (! (this.dim == vc.getDim()) ) {
				throw new IllegalArgumentException("Mismatch in maxVC.dim and vc.dim");
			}
			this.updateMax2(vc);
		}
	}
	
	private void updateMin2(VectorClock vc) {
		if(! (this.dim == vc.getDim()) ){
			throw new IllegalArgumentException("Mismatch in this.dim and argument.dim"); 
		}
		for (int ind = 0; ind < this.dim; ind++) {
			int this_c = this.clock.get(ind);
			int vc_c = vc.clock.get(ind);
			int max_c = Math.max(this_c, vc_c);
			//Todo Check Max function
			this.clock.set(ind, max_c);
		}
	}
	
	public void updateWithMin(VectorClock... vcList) {
		if(! (vcList.length >= 1) ){
			throw new IllegalArgumentException("Insuffiecient number of arguments provided"); 
		}
		for (int i = 1; i < vcList.length; i++) {
			if (vcList[i].equals(this)) throw new IllegalArgumentException("If \'this\' is one of the arguments, then it must be the first");
		}

		//this.setToZero();
		this.copyFrom(vcList[0]);
		for (int i = 1; i < vcList.length; i++) {
			VectorClock vc = vcList[i];
			if (! (this.dim == vc.getDim()) ) {
				throw new IllegalArgumentException("Mismatch in maxVC.dim and vc.dim");
			}
			this.updateMin2(vc);
		}
	}
	
	public int getClockIndex(int tIndex){
		return this.clock.get(tIndex);
	}
	
	public void setClockIndex(int tIndex, int tValue){
		this.clock.set(tIndex, tValue);
	}
}