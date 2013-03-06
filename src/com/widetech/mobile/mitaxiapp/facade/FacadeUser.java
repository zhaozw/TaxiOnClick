package com.widetech.mobile.mitaxiapp.facade;

import com.widetech.mobile.mitaxiapp.app.Application;
import com.widetech.mobile.mitaxiapp.dataacesss.DataAccessUser;
import com.widetech.mobile.mitaxiapp.dataacesss.Factory;
import com.widetech.mobile.mitaxiapp.dataacesss.Factory.DAC;
import com.widetech.mobile.mitaxiapp.object.User;

public class FacadeUser {

	protected static DataAccessUser DacUSer = (DataAccessUser) Factory
			.getInstanceFactory().getDataAccess(DAC.DAC_USER,
					Application.getContext());

	public static long create(User user) {
		return DacUSer.create(user);
	}

	public static User read() {
		return DacUSer.read();
	}

	public static void truncateUser() {
		DacUSer.truncateTableUser();
	}
}
