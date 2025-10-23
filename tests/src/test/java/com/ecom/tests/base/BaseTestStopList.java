/**
 * @author semyvolos_h
 * @date 8/29/2023 1:41 PM
 */
package com.ecom.tests.base;

import static com.ecom.core.config.EnvData.URL_TOMEE;

public class BaseTestStopList {

  /** GlobalStopList Endpoints */
  protected static String urlCreateItem = URL_TOMEE + "/go/stop-list/create";

  protected static String urlGetItem = URL_TOMEE + "/go/stop-list/get";
  protected static String urlFindItem = URL_TOMEE + "/go/stop-list/find";
  protected static String urlUpdateItem = URL_TOMEE + "/go/stop-list/update";
  protected static String urlDeleteItem = URL_TOMEE + "/go/stop-list/delete";

  /** Templates */
  public static final String XML_TEMPLATES_STOPLIST_CREATE_ITEM_PATH =
      "template/xml/api/stoplist/createItem.xml";

  public static final String XML_TEMPLATES_STOPLIST_GET_ITEM_PATH =
      "template/xml/api/stoplist/getItem.xml";
  public static final String XML_TEMPLATES_STOPLIST_FIND_ITEM_PATH =
      "template/xml/api/stoplist/findItem.xml";
  public static final String XML_TEMPLATES_STOPLIST_UPDATE_ITEM_PATH =
      "template/xml/api/stoplist/updateItem.xml";
  public static final String XML_TEMPLATES_STOPLIST_DELETE_ITEM_PATH =
      "template/xml/api/stoplist/deleteItem.xml";
}
