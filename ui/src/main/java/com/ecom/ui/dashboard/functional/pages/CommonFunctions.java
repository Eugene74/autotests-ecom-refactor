package com.ecom.ui.dashboard.functional.pages;

import com.ecom.ui.util.ResultTable;
import com.ecom.ui.util.Waiters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class CommonFunctions {

    public static void chooseFromList(WebElement list, String item){
        List<WebElement> items = list.findElements(By.tagName("li"));
        for (int i = 0; i<items.size(); ++i){
            String itemLi = items.get(i).getText();
            Waiters.sleep(4000);
            if(itemLi.equals(item)){
                Waiters.sleep(2000);
                items.get(i).click();
                break;
            }
        }
    }

    public static String applyMask(String card){
        return new String(card.substring(0, 6)+"*"+card.substring(card.length()-4, card.length()));
    }

    public static boolean findRecordInResultTable(String checkField, WebElement resultTable){
        Waiters.sleep(4000);
        List<WebElement> results = resultTable.findElements(By.tagName("td"));
        boolean result = false;
        if(!results.isEmpty()){
            for(int i = 0; i<results.size(); ++i){
                if(results.get(i).getText().equals(checkField)){ //getAttribute("innerHTML")
                    Waiters.sleep(1500);
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    public static int findRecordInResultTable(String[] dataToCheck, WebElement resultTable){
        ResultTable table = new ResultTable(resultTable);
        table.showTable();
        return table.indexOfRecordInTable(dataToCheck);
    }

    public static boolean clickOnTran(String checkField, WebElement resultTable){
        Waiters.sleep(2000);
        List<WebElement> results = resultTable.findElements(By.tagName("tr"));
        Waiters.sleep(2000);
        if(!results.isEmpty()){
            for(int i = 0; i<results.size(); ++i){//WebElement result : results
                Waiters.sleep(1500);
                if(results.get(i).getText().contains(checkField)){
                    System.out.println(results.get(i).getText());
                    Waiters.sleep(1500);
                    results.get(i).click();
                    Waiters.sleep(1500);
                    return true;
                }
            }
        }
        return false;
    }
}