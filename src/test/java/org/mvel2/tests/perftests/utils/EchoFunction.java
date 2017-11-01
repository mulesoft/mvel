package org.mvel2.tests.perftests.utils;

public class EchoFunction {
	public static String ECHO = "Echo: ";
	public String echo(String echo) {
	    return ECHO + echo;
	}
}