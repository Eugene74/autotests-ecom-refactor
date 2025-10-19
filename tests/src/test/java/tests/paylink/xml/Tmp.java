package tests.paylink.xml;

import com.ecom.db.JDBCMethods;

import java.sql.SQLException;


public class Tmp {

    static final String SENDED_ISO_MSG_TITLE = "Send ISO message :";
    static final String RECEIVED_ISO_MSG_TITLE = "Receive ISO message :";
    static final String ISO_MSG_TAG_CLOSE = "</isomsg>";
    static final String F104_MSG_TITLE = "F104 :";
    static final String F104_TAG_CLOSE = "}";

    public void test() throws SQLException {
        String messageISO = JDBCMethods.getMessageISO(123);
        System.out.println("messageISO: ");
        System.out.println(messageISO);
        System.out.println("--------------------------------------------------------------------------------------------\n");

        String remaindedMsgPart = messageISO;

        int beginIndexOfSendedIsoMsg = remaindedMsgPart.indexOf(SENDED_ISO_MSG_TITLE) + SENDED_ISO_MSG_TITLE.length();
        int endIndexOfSendedIsoMsg = remaindedMsgPart.indexOf(ISO_MSG_TAG_CLOSE) + ISO_MSG_TAG_CLOSE.length();

        String sendedIsoMsg = remaindedMsgPart.substring(beginIndexOfSendedIsoMsg, endIndexOfSendedIsoMsg);
        System.out.println("sendedIsoMsg: ");
        System.out.println(sendedIsoMsg);
        System.out.println("--------------------------------------------------------------------------------------------\n");
        remaindedMsgPart = remaindedMsgPart.substring(endIndexOfSendedIsoMsg);

        int beginIndexOfF104Msg = remaindedMsgPart.indexOf(F104_MSG_TITLE) + F104_MSG_TITLE.length();
        int endIndexOfF104Msg = remaindedMsgPart.lastIndexOf(F104_TAG_CLOSE) + F104_TAG_CLOSE.length();
        String f104Msg = remaindedMsgPart.substring(beginIndexOfF104Msg, endIndexOfF104Msg).trim();
        System.out.println("F104Msg: ");
        System.out.println(f104Msg);
        System.out.println("--------------------------------------------------------------------------------------------\n");
        remaindedMsgPart = remaindedMsgPart.substring(endIndexOfF104Msg);

        int beginIndexOfReceivedIsoMsg = remaindedMsgPart.indexOf(RECEIVED_ISO_MSG_TITLE) + RECEIVED_ISO_MSG_TITLE.length();
        int endIndexOfReceivedIsoMsg = remaindedMsgPart.indexOf(ISO_MSG_TAG_CLOSE) + ISO_MSG_TAG_CLOSE.length();
        String receivedIsoMsg = remaindedMsgPart.substring(beginIndexOfReceivedIsoMsg, endIndexOfReceivedIsoMsg);
        System.out.println("receivedIsoMsg: ");
        System.out.println(receivedIsoMsg);
        System.out.println("--------------------------------------------------------------------------------------------\n");
    }

    public void test2() throws SQLException {
//        String msg = ParserISOMsg.getISOMessage(123);
//        Document sendedISOMsg = ParserISOMsg.getOutgoingMsg(msg);
//        System.out.println(sendedISOMsg.getElementsByTagName("field").item(0).getAttributes().item(1).getTextContent());
//        ParserISOMsg.getOutgoinMsgDataSets(msg);
        String s = "https://moja.tatrabanka.sk/3dsecure/3d-secure?acsTransId=2/863303cc-b3d0-4c2e-8260-f73c3d40a5e1/";
        s=s.substring(s.length()-37, s.length()-1);
        System.out.println(s);
    }
}