package com.widetech.mobile.mitaxiapp.facade;

import java.util.ArrayList;
import java.util.Vector;

import com.widetech.mobile.mitaxiapp.app.Application;
import com.widetech.mobile.mitaxiapp.dataacesss.DataAccessMobile;
import com.widetech.mobile.mitaxiapp.dataacesss.Factory;
import com.widetech.mobile.mitaxiapp.dataacesss.Factory.DAC;
import com.widetech.mobile.mitaxiapp.object.Mobile;

public class FacadeMobile {

	protected static DataAccessMobile DacMobile = (DataAccessMobile) Factory
			.getInstanceFactory().getDataAccess(DAC.DAC_MOBILE,
					Application.getContext());

	public static long createMobile(Mobile mobile) {
		return DacMobile.create(mobile);
	}

	public static Mobile read(int id) {
		return DacMobile.read(id);
	}

	public static Vector<Mobile> readAll() {
		return DacMobile.readAll();
	}

	public static ArrayList<Mobile> readMobilesForServiceId(int serviceId) {
		return DacMobile.readMobilesForIdService(serviceId);
	}

	public static long updateMobile(Mobile mobile) {
		return DacMobile.update(mobile);
	}
}
