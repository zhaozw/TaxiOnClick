package com.widetech.mobile.mitaxiapp.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import com.widetech.mobile.log.WidetechLogger;
import com.widetech.mobile.mitaxiapp.facade.FacadeMobile;
import com.widetech.mobile.mitaxiapp.object.Mobile;

public class XmlHandlerMobile extends DefaultHandler {

	private static final String RESULT = "result";
	private static final String SERVICE = "servicio";

	private int foundMobiles = -1;
	private String totalrows;
	private String status;
	private String id;

	public XmlHandlerMobile(String id) {
		// TODO Auto-generated constructor stub
		this.id = id;
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub

		if (localName.equalsIgnoreCase(XmlHandlerMobile.RESULT)) {
			totalrows = attributes.getValue("totalrows");
			status = attributes.getValue("status");

			WidetechLogger.d("totalrows: " + totalrows + " " + "status: "
					+ status);

			if ((totalrows.compareTo("0") == 0) && (status.compareTo("0") == 0)) {
				foundMobiles = -1;
			} else if ((status.compareTo("1") == 0)
					&& (totalrows.compareTo("0") == 0)) {
				foundMobiles = 1;
			}
		}
		if (localName.equalsIgnoreCase(XmlHandlerMobile.SERVICE)) {
			foundMobiles = 0;
			Mobile mobile = new Mobile();
			mobile.setMobile(attributes.getValue("movil"));
			mobile.setPlate(attributes.getValue("placa"));
			mobile.setId_position(attributes.getValue("id"));
			mobile.setId_service(Integer.parseInt(id));
			mobile.setStatus(0);
			try {
				FacadeMobile.createMobile(mobile);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		super.endElement(uri, localName, qName);
	}

	public int getFoundMobiles() {
		return foundMobiles;
	}
}