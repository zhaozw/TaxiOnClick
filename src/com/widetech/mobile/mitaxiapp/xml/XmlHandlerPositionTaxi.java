package com.widetech.mobile.mitaxiapp.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import com.widetech.mobile.mitaxiapp.object.PositionMobile;

public class XmlHandlerPositionTaxi extends DefaultHandler {

	private static final String POSITION = "position";
	private PositionMobile posMobile;

	public PositionMobile getPosMobile() {
		return posMobile;
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		if (localName.equalsIgnoreCase(XmlHandlerPositionTaxi.POSITION)) {
			posMobile = new PositionMobile();
			posMobile
					.setLatitude(Double.parseDouble(attributes.getValue("lat")));
			posMobile.setLongitude(Double.parseDouble(attributes
					.getValue("lng")));
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		super.endElement(uri, localName, qName);
	}
}