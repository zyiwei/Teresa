import java.util.Hashtable;

public class SymbolTable {
	Hashtable<String, Integer> table;
	
	
	public SymbolTable() {
		this.table = new Hashtable<>();
		this.addEntry("SP", 0);
		this.addEntry("LCL", 1);
		this.addEntry("ARG", 2);
		this.addEntry("THIS", 3);
		this.addEntry("THAT", 4);
		for(int i=0;i<16;i++) {
			this.addEntry("R"+i, i);
		}
		this.addEntry("SCREEN", 16384);
		this.addEntry("KBD", 24576);
	}
	
	
	public void addEntry(String symbol, Integer address) {
		this.table.put(symbol, address);
	}
	
	
	public boolean contains(String symbol) {
		if(this.table.get(symbol) != null) {
			return true;
		};
		return false;
	}
	
	
	public int getAddress(String symbol) {
		if(contains(symbol)) {
			return this.table.get(symbol);
		}
		return -1;
	}
}
