/*
 * Copyright 2016 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.example.bot.staticdata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TabooConst {

  public static final boolean DISPLAY_NAME_FLG = true;
  public static final String IMAGE_01_URL = "https://raw.githubusercontent.com"
      + "/ITSVKANRIBU/LineBot_next/3.0/image/01.png";
  public static final String IMAGE_02_URL = "https://raw.githubusercontent.com"
      + "/ITSVKANRIBU/LineBot_next/3.0/image/02.png";
  public static final String IMAGE_03_URL = "https://raw.githubusercontent.com"
      + "/ITSVKANRIBU/LineBot_next/3.0/image/03.png";
  public static final String IMAGE_GOD_URL = "https://raw.githubusercontent.com"
      + "/ITSVKANRIBU/LineBot_next/3.0/image/GOD.png";
  private static ArrayList<String> TABOO_LIST;

  static {
    TABOO_LIST = new ArrayList<String>();

    String path = new File(".").getAbsoluteFile().getParent();
    File file = new File(path + "/sample-spring-boot-echo/src/main/resources/tabooList.txt");

    try {
      //文字コードUTF-8を指定してファイルを読み込む
      FileInputStream input = new FileInputStream(file);
      InputStreamReader stream = new InputStreamReader(input, "UTF-8");
      BufferedReader buffer = new BufferedReader(stream);

      String str;

      //ファイルの最終行まで読み込む
      while ((str = buffer.readLine()) != null) {
        System.out.println(str);
        TABOO_LIST.add(str);
      }

      buffer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static String getTabooList(int i) {

    if (TABOO_LIST.size() > i) {
      return TABOO_LIST.get(i);
    }

    return null;
  }

  public static int getTabooListSize() {
    return TABOO_LIST.size();
  }

}
