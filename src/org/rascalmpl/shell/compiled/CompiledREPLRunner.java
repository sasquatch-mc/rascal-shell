package org.rascalmpl.shell.compiled;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.NoSuchRascalFunction;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl.CommandExecutor;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl.CompiledRascalREPL;
import org.rascalmpl.shell.ShellRunner;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl.debug.DebugREPLFrameObserver;
import org.rascalmpl.library.util.PathConfig;

import jline.TerminalFactory;

public class CompiledREPLRunner extends CompiledRascalREPL  implements ShellRunner {
	
	private final DebugREPLFrameObserver debugObserver;
	
	public CompiledREPLRunner(PathConfig pcfg, InputStream stdin, OutputStream stdout) throws IOException, URISyntaxException {
		super(pcfg, stdin, stdout, true, true, getHistoryFile(), TerminalFactory.get());
		debugObserver = new DebugREPLFrameObserver(pcfg, reader.getInput(), stdout, true, true, getHistoryFile(), TerminalFactory.get());
		executor.setDebugObserver(debugObserver);
		setMeasureCommandTime(true);
	}

	private static File getHistoryFile() throws IOException {
		File home = new File(System.getProperty("user.home"));
		File rascal = new File(home, ".rascal");
		if (!rascal.exists()) {
			rascal.mkdirs();
		}
		File historyFile = new File(rascal, ".repl-history-rascal-terminal");
		if (!historyFile.exists()) {
			historyFile.createNewFile();
		}
		return historyFile;
	}

	@Override
	protected CommandExecutor constructCommandExecutor(PathConfig pcfg, PrintWriter stdout, PrintWriter stderr) throws IOException, NoSuchRascalFunction, URISyntaxException {
		return new CommandExecutor(pcfg, stdout, stderr);
	}

	@Override
	public void run(String[] args) throws IOException {
		// there are no args for now
		run();
	}
}