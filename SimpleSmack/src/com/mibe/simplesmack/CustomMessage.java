package com.mibe.simplesmack;

import java.util.ArrayList;
import java.util.List;

public class CustomMessage extends org.jivesoftware.smack.packet.Message {
	
	List<CustomStanza> csList = new ArrayList<CustomStanza>();
	
	public CustomMessage() {
		super();
	}
	
	public void addStanza(String tagName, String text){
		
		csList.add(new CustomStanza(tagName, text));
	}

	@Override
	public String toXML() {
		String XMLMessage = super.toXML();
		String XMLMessage1 = XMLMessage.substring(0, XMLMessage.indexOf(">"));
		String XMLMessage2 = XMLMessage.substring(XMLMessage.indexOf(">"));

		XMLMessage1 = XMLMessage1.concat(">");
		XMLMessage2 = XMLMessage2.substring(1);
		
		String customText = "";
		int len = csList.size();
		
		for(int i = 0; i < len; i++){
			CustomStanza stanza = csList.get(i);
			String tagName = stanza.tagName;
			String text = stanza.text;
			
			customText = customText.concat("<").concat(tagName).concat(">").concat(text).concat("</").concat(tagName).concat(">");
		}
		
		return XMLMessage1.concat(customText).concat(XMLMessage2);
		//return XMLMessage1.concat("<hoge>hoge5</hoge>").concat(XMLMessage2);
		//return XMLMessage1.concat(XMLMessage2);
	}

	private class CustomStanza{
		
		public String tagName;
		public String text;
		
		public CustomStanza(String tagName, String text){
			
			this.tagName = tagName;
			this.text = text;
		}
	}
}