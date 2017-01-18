package ui;

import lts.*;

/**
 * An implementation of LTSOutput which is convinient for command-line
 * execution.
 */
public class StandardOutput implements LTSOutput {
	public void out(String s) {
		System.out.print(s);
	}
	public void outln(String s) {
		System.out.println(s);
	}
	public void clearOutput() {	}
}
