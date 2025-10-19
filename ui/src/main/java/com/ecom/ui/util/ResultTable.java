package com.ecom.ui.util;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class ResultTable {
    private List<List<String>> rows;

    public ResultTable(WebElement table){
        rows = new ArrayList<>();
        Waiters.sleep(3000);
        List<WebElement> rowsWE = table.findElements(By.tagName("tr"));

        for(int i = 0; i<rowsWE.size(); i++){
            Waiters.sleep(5000);
            List<WebElement> cellsWE = rowsWE.get(i).findElements(By.tagName("td"));
            List<String> rowStr = new ArrayList<>();

            for(int j = 0; j<cellsWE.size(); j++){
                rowStr.add(cellsWE.get(j).getText());
            }
            this.rows.add(rowStr);
        }
    }

    public void showTable(){
        for (List<String> row : this.rows) {
            for (String cell : row){
                System.out.print(cell + '\t');
            }
            System.out.println("");
        }
    }

    public int indexOfRecordInTable(String[] recordFields){
        for(int i = 0; i<this.rows.size(); i++){
            int index = 0;
            int count_flag = recordFields.length;
            for(int j = 0; j<this.rows.get(i).size()&&index<recordFields.length; j++) {
                System.out.println(j+"\t"+index+"\n");
                System.out.println(this.rows.get(i).get(j));
                System.out.println(recordFields[index]+"\n");
                System.out.println(count_flag);
                if (this.rows.get(i).get(j).equals(recordFields[index])) {
                    index++;
                    count_flag--;
                }
            }
            if(count_flag==0)
                return i;
        }

        return  -1;
    }
}
