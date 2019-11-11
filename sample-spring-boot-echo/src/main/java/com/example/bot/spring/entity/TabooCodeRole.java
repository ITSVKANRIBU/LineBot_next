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

package com.example.bot.spring.entity;

import com.example.bot.staticdata.TabooConst;

public class TabooCodeRole implements Role {

  private String userId;
  private String roleName;
  private String userName;
  private int tabooCode;

  @Override
  public String getUserId() {
    return userId;
  }

  @Override
  public void setUserId(String userId) {
    this.userId = userId;
  }

  @Override
  public String getRoleName() {
    return roleName;
  }

  @Override
  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

  @Override
  public String getUserName() {
    return userName;
  }

  @Override
  public void setUserName(String userName) {
    this.userName = userName;
  }

  public int getTabooCode() {
    return tabooCode;
  }

  public void setTabooCode(int tabooCode) {
    this.tabooCode = tabooCode;
  }

  public String getMessage() {
    String message = "『" + userName + "』のタブーコードは、\n" + TabooConst.getTabooList(tabooCode);

    return message;
  }

}
