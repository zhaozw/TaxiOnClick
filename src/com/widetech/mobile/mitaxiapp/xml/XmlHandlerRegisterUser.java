package com.widetech.mobile.mitaxiapp.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import com.widetech.mobile.tools.GlobalConstants;

public class XmlHandlerRegisterUser extends DefaultHandler {

	String value = "";

	public XmlHandlerRegisterUser() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (localName.equalsIgnoreCase(GlobalConstants.RESULT)) {
			value = attributes.getValue("value");
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
		return this.value;
	}
}
