/**
 * @author semyvolos_h
 * @date 8/29/2023 3:08 PM
 */
package com.ecom.tests.support;

import static tests.paylink.api.stoplist.CreateItem.stopListId;

import com.ecom.core.config.ConfigStoplist;
import org.testng.annotations.DataProvider;

public class DataDrivenStoplist {
  @DataProvider(name = "createItemPan")
  public Object[][] createItemPanData() {

    Object[][] data = new Object[1][1];
    data[0] =
        new Object[] {
          ConfigStoplist.getProperty("stoplist.merchantId"),
          ConfigStoplist.getProperty("stoplist.terminalId"),
          ConfigStoplist.getProperty("stoplist.create.item.trackingId"),
          ConfigStoplist.getProperty("stoplist.create.item.type"),
          ConfigStoplist.getProperty("stoplist.create.item.type.value"),
          ConfigStoplist.getProperty("stoplist.create.item.expDate"),
          ConfigStoplist.getProperty("stoplist.create.item.createRemark"),
          ConfigStoplist.getProperty("stoplist.create.item.createDate")
        };
    return data;
  }

  @DataProvider(name = "getItemPan")
  public Object[][] getItemPanData() {

    Object[][] data = new Object[1][1];
    data[0] =
        new Object[] {
          ConfigStoplist.getProperty("stoplist.merchantId"),
          ConfigStoplist.getProperty("stoplist.terminalId"),
          ConfigStoplist.getProperty("stoplist.get.item.trackingId"),
          stopListId
        };
    return data;
  }

  @DataProvider(name = "updateItemPan")
  public Object[][] updateItemPanData() {

    Object[][] data = new Object[1][1];
    data[0] =
        new Object[] {
          ConfigStoplist.getProperty("stoplist.merchantId"),
          ConfigStoplist.getProperty("stoplist.terminalId"),
          ConfigStoplist.getProperty("stoplist.update.item.trackingId"),
          stopListId,
          ConfigStoplist.getProperty("stoplist.update.item.expDate"),
          ConfigStoplist.getProperty("stoplist.update.item.updateRemark")
        };
    return data;
  }

  @DataProvider(name = "findItemPan")
  public Object[][] findItemPanData() {

    Object[][] data = new Object[1][1];
    data[0] =
        new Object[] {
          ConfigStoplist.getProperty("stoplist.merchantId"),
          ConfigStoplist.getProperty("stoplist.terminalId"),
          ConfigStoplist.getProperty("stoplist.find.item.trackingId"),
          ConfigStoplist.getProperty("stoplist.find.item.type"),
          ConfigStoplist.getProperty("stoplist.find.item.type.value"),
        };
    return data;
  }

  @DataProvider(name = "deleteItemPan")
  public Object[][] deleteItemPanData() {

    Object[][] data = new Object[1][1];
    data[0] =
        new Object[] {
          ConfigStoplist.getProperty("stoplist.merchantId"),
          ConfigStoplist.getProperty("stoplist.terminalId"),
          ConfigStoplist.getProperty("stoplist.delete.item.trackingId"),
          stopListId
        };
    return data;
  }
}
