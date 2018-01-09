package com.duplex.frame;

public class ByteArrayTransfer {

	public enum Mode {
		UPPER_TO_LOW, LOW_TO_UPPER, NONE
	}

	public static Mode reverse(Mode mode) {
		switch (mode) {
		case UPPER_TO_LOW:
			return Mode.LOW_TO_UPPER;
		case LOW_TO_UPPER:
			return Mode.UPPER_TO_LOW;
		default:
			break;
		}
		return mode;
	}
}
