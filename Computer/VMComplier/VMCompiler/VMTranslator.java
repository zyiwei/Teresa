import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class VMTranslator {
	private Parser parser;
	private CodeWriter codeWriter;
	
	
	public VMTranslator(File file) throws IOException {
		this.codeWriter = new CodeWriter(file);
	}
	
	public Parser getParser() {
		return parser;
	}
	
	public CodeWriter getCodeWriter() {
		return codeWriter;
	}
	
	
	public void doCompile(File file) throws IOException {
		if(file.isFile()) {
			this.parser = new Parser(file, new FileInputStream(file));
		}else {
			this.parseDirectory(file);
		}
	}
	
	
	private void parseDirectory(File fileDirectory) throws IOException {
		File[] files = fileDirectory.listFiles();
		ArrayList<File> VMFileList = new ArrayList<>();
		
		for(File i : files) {
			String fileName = i.getName();
			String laterName = fileName.substring(fileName.lastIndexOf(".")+1, fileName.length());
			if(laterName.equals("vm")) {
				VMFileList.add(i);
			}else {
				continue;
			}
		}
		
		for(File i : VMFileList) {
			if(i.getName().equals("Sys.vm")) {
				this.parseFile(i);
				VMFileList.remove(i);
				break;
			}
		}
		
		for(File i : VMFileList) {
			this.parseFile(i);
		}
	}
	
	
	private void parseFile(File file) throws IOException {
		this.parser = new Parser(file, new FileInputStream(file));
		do {
			this.doWrite();
		}while(parser.hasMoreCommands());
	}

	
	private void doWrite() throws IOException {
		this.parser.advance();
		CommandType commandType = this.parser.commandType();
		if(commandType.equals(CommandType.C_ARITHMETIC)) {
			codeWriter.writeArithmetic(this.parser);
		}else if(commandType.equals(CommandType.C_PUSH) || commandType.equals(CommandType.C_POP)) {
			codeWriter.writePushPop(this.parser);
		}else if(commandType.equals(CommandType.C_CALL)) {
			codeWriter.writeCall(this.parser);
		}else if(commandType.equals(CommandType.C_FUNCTION)) {
			codeWriter.writeFunction(this.parser);
		}else if(commandType.equals(CommandType.C_GOTO)) {
			codeWriter.writeGoto(this.parser);
		}else if(commandType.equals(CommandType.C_IF)) {
			codeWriter.writeIf(this.parser);
		}else if(commandType.equals(CommandType.C_LABEL)) {
			codeWriter.writeLabel(this.parser);
		}else if(commandType.equals(CommandType.C_RETURN)) {
			codeWriter.writeReturn(this.parser);
		}else {
			System.out.println("unknown command type!");
		}
		this.codeWriter.getWriter().flush();
	}
	
	public static void main(String[] args) throws IOException {
		File file = new File("D:\\leaning_source\\nand2tetris\\projects\\08\\FunctionCalls\\StaticsTest");
		VMTranslator vmTranslator = new VMTranslator(file);
		vmTranslator.doCompile(file);
	}
}
