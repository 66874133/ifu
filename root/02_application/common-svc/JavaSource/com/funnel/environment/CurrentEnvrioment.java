package com.funnel.environment;

@Deprecated
public final class CurrentEnvrioment {

    private CurrentEnvrioment() {
    }


	private static String environment;

	public static String getEnvironment() {
		return environment;
	}

	public static void setEnvironment(String environment) {
		CurrentEnvrioment.environment = environment;
	}
	

}
