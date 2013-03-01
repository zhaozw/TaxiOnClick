package com.widetech.mobile.mitaxiapp.xml;

import java.io.StringReader;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public class XmlFetcherMobile {

	String stream = "";
	String id = "";

	public XmlFetcherMobile(String stream, String id) {

		this.stream = stream;
		this.id = id;
	}

	public int getResultData() {

		int resultData = -10;

		try {

			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			XmlHandlerMobile handler = new XmlHandlerMobile(this.id);
			xr.setContentHandler(handler);
			InputSource inStream = new InputSource();
			inStream.setCharacterStream(new StringReader(this.stream));
			xr.parse(inStream);
			resultData = handler.getFoundMobiles();

		} catch (Exception e) {
			// TODO: handle exception
		}

		// Return raw json String
		return resultData;
	}

}
