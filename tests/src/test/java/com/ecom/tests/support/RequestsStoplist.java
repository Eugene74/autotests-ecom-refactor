/**
 * @author semyvolos_h
 * @date 8/31/2023 2:59 PM
 */
package com.ecom.tests.support;
import org.w3c.dom.Document;
import com.ecom.tests.base.BaseTestStopList;
import static com.ecom.tests.support.DocumentTools.*;


public class RequestsStoplist {

    public static Document createItemPanDocument(String merchantId, String terminalId, String trackingId, String type, String pan, String expDate, String createRemark, String createDate){
        Document requestDoc = DocumentTools.readXMLFile(BaseTestStopList.XML_TEMPLATES_STOPLIST_CREATE_ITEM_PATH);
        fillCreateItemPan(requestDoc, merchantId, terminalId, trackingId, type, pan, expDate, createRemark, createDate);
        return requestDoc;
    }

    public static Document getItemPanDocument(String merchantId, String terminalId, String trackingId, String stopListId){
        Document requestDoc = DocumentTools.readXMLFile(BaseTestStopList.XML_TEMPLATES_STOPLIST_GET_ITEM_PATH);
        fillGetItemPan(requestDoc, merchantId, terminalId, trackingId, stopListId);
        return requestDoc;
    }

    public static Document updateItemPanDocument(String merchantId, String terminalId, String trackingId, String stopListId, String expDate, String updateRemark){
        Document requestDoc = DocumentTools.readXMLFile(BaseTestStopList.XML_TEMPLATES_STOPLIST_UPDATE_ITEM_PATH);
        fillUpdateItemPan(requestDoc, merchantId, terminalId, trackingId, stopListId, expDate, updateRemark);
        return requestDoc;
    }

    public static Document findItemPanDocument(String merchantId, String terminalId, String trackingId, String type, String pan){
        Document requestDoc = DocumentTools.readXMLFile(BaseTestStopList.XML_TEMPLATES_STOPLIST_FIND_ITEM_PATH);
        fillFindItemPan(requestDoc, merchantId, terminalId, trackingId, type, pan);
        return requestDoc;
    }

    public static Document deleteItemPanDocument(String merchantId, String terminalId, String trackingId, String stopListId){
        Document requestDoc = DocumentTools.readXMLFile(BaseTestStopList.XML_TEMPLATES_STOPLIST_DELETE_ITEM_PATH);
        fillDeleteItemPan(requestDoc, merchantId, terminalId, trackingId, stopListId);
        return requestDoc;
    }
}
