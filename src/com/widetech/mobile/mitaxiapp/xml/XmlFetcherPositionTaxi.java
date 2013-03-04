package com.widetech.mobile.mitaxiapp.xml;

import java.io.StringReader;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import com.widetech.mobile.mitaxiapp.object.PositionMobile;

public class XmlFetcherPositionTaxi {

	private String stream = "";

	public XmlFetcherPositionTaxi(String stream) {

		this.stream = stream;
	}

	public PositionMobile getResultData() {

		PositionMobile resultData = null;

		try {

			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			XmlHandlerPositionTaxi handler = new XmlHandlerPositionTaxi();
			xr.setContentHandler(handler);
			InputSource inStream = new InputSource();
			inStream.setCharacterStream(new StringReader(this.stream));
			xr.parse(inStream);
			resultData = handler.getPosMobile();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		// Return raw json String
		return resultData;
	}

}