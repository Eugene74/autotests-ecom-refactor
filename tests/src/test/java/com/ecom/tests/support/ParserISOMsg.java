package com.ecom.tests.support;

import static com.ecom.tests.support.DocumentTools.convertStringToXmlDocument;

import com.ecom.db.JDBCMethods;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.w3c.dom.Document;

public class ParserISOMsg {
  static final String SENDED_ISO_MSG_TITLE = "Send ISO message :";
  static final String RECEIVED_ISO_MSG_TITLE = "Receive ISO message :";
  static final String ISO_MSG_TAG_CLOSE = "</isomsg>";
  static final String F104_MSG_TITLE = "F104 :";
  static final String F104_TAG_CLOSE = "}";

  public static String getISOMessage(Integer tran) {
    String messageISO = JDBCMethods.getMessageISO(tran);
    return messageISO;
  }

  public static Document getOutgoingMsg(String msg) {
    int beginIndexOfSendedIsoMsg =
        msg.indexOf(SENDED_ISO_MSG_TITLE) + SENDED_ISO_MSG_TITLE.length();
    int endIndexOfSendedIsoMsg = msg.indexOf(ISO_MSG_TAG_CLOSE) + ISO_MSG_TAG_CLOSE.length();

    String sendedIsoMsg = msg.substring(beginIndexOfSendedIsoMsg, endIndexOfSendedIsoMsg);
    return convertStringToXmlDocument(sendedIsoMsg);
  }

  public static void getOutgoinMsgDataSets(String msg) {
    msg.substring(0, msg.indexOf(RECEIVED_ISO_MSG_TITLE));

    int beginIndexOfF104Msg = msg.indexOf(F104_MSG_TITLE) + F104_MSG_TITLE.length();
    int endIndexOfF104Msg = msg.lastIndexOf(F104_TAG_CLOSE) + F104_TAG_CLOSE.length();

    String f104Msg = msg.substring(beginIndexOfF104Msg, endIndexOfF104Msg).trim();
    System.out.println(f104Msg);
    JsonParser parser = new JsonParser();
    // get json object
    JsonObject jsonObject = (JsonObject) parser.parse(f104Msg);
    System.out.println(jsonObject.toString());
  }
}
