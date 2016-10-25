package com.aion.axframe.utils.log;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * Created by Aion on 16/9/20.
 */
public class XmlLog {

    public static void printXml(int type, String tag, String xml, String headString, boolean isShowHeadInfo, boolean isShowThreadInfo, int methodCount, int methodOffset) {
        if (xml != null) {
            xml = XmlLog.formatXML(xml);
            xml = headString + "\n" + xml;
        } else {
            xml = headString + ALog.NULL_TIPS;
        }

        LineUtilsLog.printLine(type, tag, LineUtilsLog.TOP);
        if (isShowHeadInfo) {
            LineUtilsLog.logHeaderContent(type, tag, isShowThreadInfo, methodCount, methodOffset);
        }
        String[] lines = xml.split(ALog.LINE_SEPARATOR);
        for (String line : lines) {
            if (!LineUtilsLog.isEmpty(line)) {
                LineUtilsLog.typeLog(type, tag, "║ " + line);
            }
        }
        LineUtilsLog.printLine(type, tag, LineUtilsLog.BOM);
    }

    public static void printXml(String tag, String xml, String headString) {
        printXml(ALog.D, tag, xml, headString, false, false, 0, 0);
    }

    public static String formatXML(String inputXML) {
        try {
            Source xmlInput = new StreamSource(new StringReader(inputXML));
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(xmlInput, xmlOutput);
            return xmlOutput.getWriter().toString().replaceFirst(">", ">\n");
        } catch (Exception e) {
            e.printStackTrace();
            return inputXML;
        }
    }
}
