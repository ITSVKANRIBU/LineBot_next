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

public class MessageConst {

  public static final String DEFAILT_MESSAGE = "村を作成したい場合は「タブー」を、\n"
      + "村の確認をしたい場合は村番号（数字4桁）を入力してください。";

  public static final String OWNER_NUMSETMESSAGE = "自分を含む人数を入力してください。";

  public static final String ERR_NUMSETMESSAGE = "村の人数は2人以上に設定してください。\n"
      + "もう一度村の人数を設定してください。";
  public static final String OWNER_CONFMESSAGE = "配布状況を確認したい場合は"
      + "村番号を入力してください。";
  public static final String INSIDER_ROLE = "インサイダー";
  public static final String VILLAGE_ROLE = "村人";
  public static final String GAMEMASTER_ROLE = "ＧＭ";
  public static final int DEFAULT_GMNUM = 999;

}
