public class VirtualMemory {
	
	int[] memory;
	
	public VirtualMemory() {
		this.memory = new int[24577];
	}
	
	
	public int getBasicAddress(String symbol) {
		switch (symbol) {
		case "SP":
			return this.memory[0];
		case "local":
			return this.memory[1];
		case "argument":
			return this.memory[2];
		case "this": case "pointer":
			return this.memory[3];
		case "that":
			return this.memory[4];
		default:
			break;
		}
		return -1;
	}
	
	
	public int getValue(int address) {
		return this.memory[address];
	}
	
	
	public int getValue(String symbol) {
		switch (symbol) {
		case "temp":
			return this.memory[5];
		default:
			break;
		}
		return -1;
	}
	
	
	public void setValue(int address) {
		
	}
	
	
	public void setValue(String symbol) {
		
	}
	
}
