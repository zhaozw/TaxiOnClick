/*
 * Factory.java
 *
 * Created on December 19, 2012
 */

/**
 * @author  Felipe Calderon Barragan
 * @since  1.0
 * @company WideTech S.A
 */
package com.widetech.mobile.mitaxiapp.dataacesss;

import android.content.Context;

public class Factory {

	private static Factory instance = new Factory();

	public enum DAC {
		DAC_MOBILE, DAC_USER, DAC_ADDRESS
	}

	private Factory() {

	}

	public static Factory getInstanceFactory() {

		return instance;
	}

	public DataAccess getDataAccess(DAC Dac, Context context) {

		switch (Dac) {
		case DAC_MOBILE:
			return new DataAccessMobile(context);
		case DAC_USER:
			return new DataAccessUser(context);
		case DAC_ADDRESS:
			return new DataAccessAddress(context);
		default:
			return null;
		}
	}
}
