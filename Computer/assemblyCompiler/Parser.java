import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;


public class Parser {
	File file;
	Scanner scanner;
	String currentCommand;
	int address = 0;
	
	
	public Parser(File file, FileInputStream fiStream) {
		this.file=file;
		this.scanner = new Scanner(fiStream);
	}
	
	
	public boolean hasMoreCommands() {
		if(this.scanner.hasNextLine()) {
			return true;
		}else {
			return false;
		}
	}
	
	
	public void advance() {
		if(hasMoreCommands()) {
			do{
				this.currentCommand = this.scanner.nextLine();
			}while(!getCommand());
			if(this.commandType().equals(CommandType.A_COMMAND) || this.commandType().equals(CommandType.C_COMMAND)) {
				this.address++;
			}
		}else {
			this.scanner.close();
		}
	}
	
	
	private boolean getCommand(){
		String stringLine = this.currentCommand;
		if(stringLine.equals("")) {
			return false;
		}
		String head = stringLine.substring(0, 2);
		String headChar = stringLine.trim().substring(0,2); 
		if(head.equals("//") || headChar.equals("//")) {
			return false;
		}
		if(stringLine.contains("//")) {
			String subStr = stringLine.substring(0, stringLine.indexOf("//"));
			this.currentCommand = subStr.trim();
			return true;
		}
		this.currentCommand = stringLine.trim();
		return true;
	}
	
	
	public CommandType commandType() {
		if(this.currentCommand.substring(0, 1).equals("@")) {
			return CommandType.A_COMMAND;
		}else if(this.currentCommand.substring(0, 1).equals("(")) {
			return CommandType.L_COMMAND;
		}else if(this.currentCommand.contains(";") || this.currentCommand.contains("=")) {
			return CommandType.C_COMMAND;
		}else {
			return CommandType.UNKNOW;
		}
	}
	
	
	public boolean isDigit(String symbol) {
		for(int i = 0;i<symbol.length();i++) {
			if(!Character.isDigit(symbol.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	
	public String paddingZero(String str) {
		while(str.length()<=15) {
			str="0"+str;
		}
		return str;
	}
	
	
	public String sysmbol() {
		if (commandType().equals(CommandType.A_COMMAND)) {
			String command = this.currentCommand;
			String subStr =  command.substring(1, command.length());
			if(isDigit(subStr)) {
				Long address = Long.parseLong(subStr);
				String addressStr = Long.toBinaryString(address);
				return paddingZero(addressStr);
			}else {
				return subStr;
			}
		}
		if(commandType().equals(CommandType.L_COMMAND)) {
			String command = this.currentCommand;
			String subStr = command.substring(1, command.length()-1);
			if(isDigit(subStr)) {
				Long address = Long.parseLong(subStr);
				String addressStr = Long.toBinaryString(address);
				return paddingZero(addressStr);
			}else {
				return subStr;
			}
		}
		return null;
	}
	
	
	public String dest() {
		if(commandType().equals(CommandType.C_COMMAND)) {
			String command = this.currentCommand;
			if(command.contains("=")) {
				return command.substring(0, command.indexOf("="));
			}else {
				return "";
			}
		}
		return null;
	}
	
	
	public String comp() {
		if(commandType().equals(CommandType.C_COMMAND)) {
			String command = this.currentCommand;
			if(command.contains("=") && command.contains(";")) {
				return command.substring(command.indexOf("=")+1, command.indexOf(";"));
			}else if (command.contains(";")) {
				return command.substring(0, command.indexOf(";"));
			}else if (command.contains("=")) {
				return command.substring(command.indexOf("=")+1, command.length());
			}else {
				return "";
			}
		}
		return null;
	}

	
	public String jump() {
		if(commandType().equals(CommandType.C_COMMAND)) {
			String command = this.currentCommand;
			if(command.contains(";")) {
				return command.substring(command.indexOf(";")+1, command.length());
			}else {
				return "";
			}
		}
		return null;
	}
}
