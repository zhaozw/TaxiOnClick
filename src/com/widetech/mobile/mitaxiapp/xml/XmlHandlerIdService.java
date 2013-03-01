package com.widetech.mobile.mitaxiapp.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class XmlHandlerIdService extends DefaultHandler {

	private String data;
	private static final String ID = "id";

	public XmlHandlerIdService() {
		// TODO Auto-generated constructor stub
		data = "-1";
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (localName.equalsIgnoreCase(XmlHandlerIdService.ID)) {
			data = attributes.getValue("valor");
		}
	}

	@Override
	public void error(SAXParseException e) throws SAXException {

	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub

	}

	public String getData() {
		return this.data;
	}
}