package com.widetech.mobile.mitaxiapp.xml;

import java.io.StringReader;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public class XmlFetcherCancelTaxi {

	String stream = "";

	public XmlFetcherCancelTaxi(String stream) {

		this.stream = stream;

	}

	public String getResultData() {

		String result = null;

		try {

			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			XmlHandlerCancelTaxi handler = new XmlHandlerCancelTaxi();
			xr.setContentHandler(handler);
			InputSource inStream = new InputSource();
			inStream.setCharacterStream(new StringReader(this.stream));
			xr.parse(inStream);
			result = handler.getData();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return result;
	}

}
