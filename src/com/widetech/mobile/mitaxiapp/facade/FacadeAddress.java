package com.widetech.mobile.mitaxiapp.facade;

import java.util.ArrayList;
import com.widetech.mobile.mitaxiapp.app.Application;
import com.widetech.mobile.mitaxiapp.dataacesss.DataAccessAddress;
import com.widetech.mobile.mitaxiapp.dataacesss.Factory;
import com.widetech.mobile.mitaxiapp.dataacesss.Factory.DAC;
import com.widetech.mobile.mitaxiapp.object.Address;
import com.widetech.mobile.mitaxiapp.object.Mobile;

public class FacadeAddress {

	protected static DataAccessAddress DacAddress = (DataAccessAddress) Factory
			.getInstanceFactory().getDataAccess(DAC.DAC_ADDRESS,
					Application.getContext());

	public static long create(Address address) {
		return DacAddress.create(address);
	}

	public static Mobile read(int id) {
		return DacAddress.read(id);
	}

	public static ArrayList<Address> readAllAddress() {
		return DacAddress.readAllAddress();
	}

	public static Address findDataForAddress(String actualAddress) {
		return DacAddress.findAddress(actualAddress);
	}
}
