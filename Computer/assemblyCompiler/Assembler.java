import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;


public class Assembler {
	File file;
	File binFile;
	Parser parser;
	Code code;
	SymbolTable symbolTable;
	int freeVarAddress = 16;
	BufferedWriter writer;
	

	public Assembler(File file) throws FileNotFoundException {
		this.file = file;
		String fileName = file.getName();
		String binFilePath = file.getParent()+"\\"+fileName.substring(0, fileName.indexOf("."))+"cmp.hack";
		this.writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(binFilePath)));
		this.code = new Code();
		this.symbolTable = new SymbolTable();
	}
	
	
	public void firstScanFile() throws FileNotFoundException {
		this.parser = new Parser(this.file, new FileInputStream(this.file));
		do {
			parser.advance();
			CommandType commandType = parser.commandType();
			switch (commandType) {
			case L_COMMAND:
				String subL =  parser.currentCommand.substring(1, parser.currentCommand.length()-1);
				if(!parser.isDigit(subL)) {
					this.symbolTable.addEntry(subL, parser.address);
				}
				break;
			default:
				break;
			}
		}while(parser.hasMoreCommands());
	}
	
	
	public void secondScanFile() throws IOException {
		this.parser = new Parser(this.file, new FileInputStream(this.file));
		do {
			parser.advance();
			CommandType commandType = parser.commandType();
			switch (commandType) {
			case A_COMMAND:
				String subStr =  parser.currentCommand.substring(1, parser.currentCommand.length());
				if(parser.isDigit(subStr)) {
					System.out.println(parser.currentCommand+"\t\t"+parser.sysmbol());
					writer.write(parser.sysmbol()+"\n");
					break;
				}else {
					int address = 0;
					if(this.symbolTable.contains(subStr)) {
						address = this.symbolTable.getAddress(subStr);
					}else {
						this.symbolTable.addEntry(subStr, this.freeVarAddress);
						address = this.freeVarAddress;
						this.freeVarAddress++;
					}
					
					String addressStr = parser.paddingZero(Integer.toBinaryString(address));
					System.out.println(parser.currentCommand+"\t\t"+addressStr);
					writer.write(addressStr+"\n");
				}
				break;
			case C_COMMAND:
				System.out.print(parser.currentCommand+"\t\t");
				String dest = code.dest(parser.dest());
				String comp = code.comp(parser.comp());
				String jump = code.jump(parser.jump());
				String binCmd = "111"+comp+dest+jump;
				System.out.println(binCmd);
				writer.write(binCmd+"\n");
				break;
			case L_COMMAND:
				System.out.println(parser.currentCommand);
				break;
			default:
				break;
			}
		}while(parser.hasMoreCommands());
		this.writer.flush();
	}
	
	public static void main(String[] args) throws IOException {
		File file = new File("C:\\Users\\Thingcor\\Desktop\\Pong.asm");
		Assembler assembler = new Assembler(file);
		assembler.firstScanFile();
		assembler.secondScanFile();

	}
}
