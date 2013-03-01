package com.widetech.mobile.mitaxiapp.xml;

import java.io.StringReader;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public class XmlFetcherIdService {

	String stream = "";

	public XmlFetcherIdService(String stream) {

		this.stream = stream;
	}

	public String getResultData() {

		String resultData = null;

		try {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			XmlHandlerIdService handler = new XmlHandlerIdService();
			xr.setContentHandler(handler);
			InputSource inStream = new InputSource();
			inStream.setCharacterStream(new StringReader(this.stream));
			xr.parse(inStream);
			resultData = handler.getData();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		// Return raw String
		return resultData;
	}
}