package com.example.demo.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class TagUtil {
  final static String regex = "@\\w+";

  public static List<String> getIds(String text) {
    List<String> ids = new ArrayList<>();
    String[] textArr = text.split(" ");
    for(String s: textArr) {
      if (Pattern.matches(regex, s)) {
        ids.add(s);
      }
    }
    return ids;
  }
}
