package com.recipe.myrecipe.util;

import java.util.Iterator;
import java.util.List;

public class ListToString {
    public static String convertToDelimSeperatedString(List<String> list, String delim){
        String result = "";

        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            String item = iterator.next();
            result += item;
            if (iterator.hasNext()) { // 다음 아이템이 있는지 확인
                result += delim;
            }
        }
        return result;
    }
}
