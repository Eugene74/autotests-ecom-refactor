/**
 * @author semyvolos_h
 * @date 8/29/2023 1:41 PM
 */
package tests.paylink.api.stoplist;

import tests.BaseTest;

public class BaseTestStoplist extends BaseTest {

    /** GlobalStopList Endpoints */
    protected static String urlCreateItem = envProperties.getProperty("URLtomee") + "/go/stop-list/create";
    protected static String urlGetItem = envProperties.getProperty("URLtomee") + "/go/stop-list/get";
    protected static String urlFindItem = envProperties.getProperty("URLtomee") + "/go/stop-list/find";
    protected static String urlUpdateItem = envProperties.getProperty("URLtomee") + "/go/stop-list/update";
    protected static String urlDeleteItem = envProperties.getProperty("URLtomee") + "/go/stop-list/delete";

    /** Templates */
    public static final String XML_TEMPLATES_STOPLIST_CREATE_ITEM_PATH = "template/xml/api/stoplist/createItem.xml";
    public static final String XML_TEMPLATES_STOPLIST_GET_ITEM_PATH = "template/xml/api/stoplist/getItem.xml";
    public static final String XML_TEMPLATES_STOPLIST_FIND_ITEM_PATH = "template/xml/api/stoplist/findItem.xml";
    public static final String XML_TEMPLATES_STOPLIST_UPDATE_ITEM_PATH = "template/xml/api/stoplist/updateItem.xml";
    public static final String XML_TEMPLATES_STOPLIST_DELETE_ITEM_PATH = "template/xml/api/stoplist/deleteItem.xml";
}