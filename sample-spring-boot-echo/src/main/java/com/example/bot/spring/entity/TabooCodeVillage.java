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

import java.util.ArrayList;

import com.example.bot.staticdata.MessageConst;

public class TabooCodeVillage implements Village {

  private int villageNum;
  private String ownerId;
  private ArrayList<Role> roleList;
  private int villageSize;

  @Override
  public int getVillageNum() {
    return villageNum;
  }

  @Override
  public void setVillageNum(int villageNum) {
    this.villageNum = villageNum;
  }

  @Override
  public String getOwnerId() {
    return ownerId;
  }

  @Override
  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }

  @Override
  public ArrayList<Role> getRoleList() {
    return roleList;
  }

  @Override
  public void setRoleList(ArrayList<Role> roleList) {
    this.roleList = roleList;
  }

  @Override
  public void addRoleList(Role role) {
    this.roleList.add(role);
  }

  @Override
  public boolean hasOwner(String userId) {
    return userId.equals(ownerId) ? true : false;
  }

  @Override
  public int getVillageSize() {
    return villageSize;
  }

  @Override
  public void setVillageSize(int villageSize) {
    this.villageSize = villageSize;
  }

  @Override
  public Role getMemberRole(String userId) {
    return roleList.stream()
        .filter(obj -> userId.equals(obj.getUserId())).findFirst().orElse(null);
  }

  @Override
  public String getRoleMessage(String userId) {
    return null;
  }

  @Override
  public ArrayList<String> getRoleMessages(String userId) {
    ArrayList<String> rtnMessageList = new ArrayList<String>();

    Role role = getMemberRole(userId);
    if (role == null) {
      rtnMessageList.add(MessageConst.DEFAILT_MESSAGE);
    } else {
      rtnMessageList.add("" + getMemberRole(userId).getUserName() + "さん頑張ってください。");
      int diviceNum = (villageSize - 1) / 5;
      for (int i = 0; i < roleList.size(); i++) {
        if (!userId.equals(roleList.get(i).getUserId())) {
          String message = roleList.get(i).getMessage();
          for (int j = 0; j < diviceNum; j++) {
            i++;
            if (i >= roleList.size()) {
              break;
            }
            message = message.concat("\n\n" + roleList.get(i).getMessage());
          }
          rtnMessageList.add(message);
        }
      }
    }

    return rtnMessageList;
  }
}
