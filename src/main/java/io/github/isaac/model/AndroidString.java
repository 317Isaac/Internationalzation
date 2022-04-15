package io.github.isaac.model;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class AndroidString {
    private String key;

    private String value;

    private SupportedLanguages language;

    private static final String KEY_STRING = "</string>";

    private static final String SPLIT_KEY = "<string";

    private static final String KEY_START = "name=\"";

    private static final String KEY_END = "\">";

    private static final String VALUE_END = "</string>";

    public AndroidString(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public AndroidString(AndroidString androidString) {
        this.key = androidString.getKey();
        this.value = androidString.getValue();
        this.language = androidString.getLanguage();
    }

    public AndroidString() {}

    public String getKey() {
        return this.key;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public SupportedLanguages getLanguage() {
        return this.language;
    }

    public void setLanguage(SupportedLanguages language) {
        this.language = language;
    }

    public String toString() {
        return "<string name=\"" + this.key + "\">" + this.value + "</string>";
    }

    public static List<AndroidString> getAndroidStringsList(InputStream xml) {
        List<AndroidString> result = new ArrayList<>();
        try {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            XMLEventReader eventReader = inputFactory.createXMLEventReader(xml);
            AndroidString androidString = null;
            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    if (startElement.getName().getLocalPart().equals("string")) {
                        androidString = new AndroidString();
                        Iterator<Attribute> attributes = startElement.getAttributes();
                        while (attributes.hasNext()) {
                            Attribute attribute = attributes.next();
                            if (attribute.getName().toString().equals("name"))
                                androidString.setKey(attribute.getValue());
                        }
                        event = eventReader.nextEvent();
                        String value = event.asCharacters().getData().trim();
                        androidString.setValue(value);
                        continue;
                    }
                }
                if (event.isEndElement()) {
                    EndElement endElement = event.asEndElement();
                    if (endElement.getName().getLocalPart().equals("string"))
                        result.add(androidString);
                }
            }
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<AndroidString> getAndroidStringsList(byte[] xmlContentByte) {
        try {
            String fileContent = new String(xmlContentByte, "UTF-8");
            if (!fileContent.contains("</string>"))
                return null;
            String[] tokens = fileContent.split("<string");
            List<AndroidString> result = new ArrayList<>();
            for (int i = 0; i < tokens.length; i++) {
                if (tokens[i].contains("</string>")) {
                    int keyStartIndex = tokens[i].indexOf("name=\"") + "name=\"".length();
                    int keyEndIndex = tokens[i].indexOf("\">");
                    int valueEndIndex = tokens[i].indexOf("</string>");
                    if (keyStartIndex < tokens[i].length() && keyEndIndex < tokens[i]
                            .length() && keyEndIndex + "\">"
                            .length() < tokens[i].length() && valueEndIndex < tokens[i]
                            .length()) {
                        String key = tokens[i].substring(keyStartIndex, keyEndIndex).trim();
                        String value = tokens[i].substring(keyEndIndex + "\">".length(), valueEndIndex).trim();
                        result.add(new AndroidString(key, value));
                    }
                }
            }
            return result;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> getAndroidStringKeys(List<AndroidString> list) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++)
            result.add(((AndroidString)list.get(i)).getKey());
        return result;
    }

    public static List<String> getAndroidStringValues(List<AndroidString> list) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++)
            result.add(((AndroidString)list.get(i)).getValue());
        return result;
    }
}

