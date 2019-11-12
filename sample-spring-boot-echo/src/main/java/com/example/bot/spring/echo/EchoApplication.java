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

package com.example.bot.spring.echo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.bot.spring.entity.Role;
import com.example.bot.spring.entity.TabooCodeRole;
import com.example.bot.spring.entity.TabooCodeVillage;
import com.example.bot.spring.entity.Village;
import com.example.bot.staticdata.MessageConst;
import com.example.bot.staticdata.TabooConst;
import com.example.bot.staticdata.VillageList;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import lombok.NonNull;

@SpringBootApplication
@LineMessageHandler
public class EchoApplication {
  @Autowired
  private LineMessagingClient lineMessagingClient;

  public static void main(String[] args) {
    SpringApplication.run(EchoApplication.class, args);
  }

  @EventMapping
  public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
    System.out.println("event: " + event);
    System.out.println("動いています通常");

    String userId = event.getSource().getUserId();
    String userMessage = event.getMessage().getText();
    try {
      UserProfileResponse userProfile = lineMessagingClient.getProfile(userId).get();
      System.out.println("このユーザーは" + userProfile.getDisplayName());
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }

    // messageの取得
    replyMessage(event.getReplyToken(), userId, userMessage);
  }

  @EventMapping
  public void handleDefaultMessageEvent(Event event) {
    System.out.println("event: " + event);
    System.out.println("動いています");
  }

  private void reply(@NonNull String replyToken, @NonNull Message message) {
    reply(replyToken, Collections.singletonList(message));
  }

  private void reply(@NonNull String replyToken, @NonNull List<Message> messages) {
    try {
      lineMessagingClient
          .replyMessage(new ReplyMessage(replyToken, messages))
          .get();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
  }

  private void replyMessage(String replyToken, String userId, String userMessage) {
    String message = MessageConst.DEFAILT_MESSAGE;

    Random random = new Random();

    try {
      int number = Integer.parseInt(userMessage.trim());

      if (number > 100) {
        // 村番号の場合
        replyMessageVillageNum(replyToken, userId, number);
        return;
      } else {

        // 人数が0のものを探す
        for (int i = VillageList.getVillageList().size() - 1; i >= 0; i--) {
          if (0 == VillageList.get(i).getVillageSize()
              && userId.equals(VillageList.get(i).getOwnerId())) {
            if (number <= 1) {
              message = MessageConst.ERR_NUMSETMESSAGE;
              break;
            }

            // 村人数設定
            VillageList.get(i).setVillageSize(number);
            int tabooCode = random.nextInt(TabooConst.getTabooListSize());

            TabooCodeRole masterRole = new TabooCodeRole();
            masterRole.setUserName("1番目の入室者");
            masterRole.setUserId(userId);
            masterRole.setTabooCode(tabooCode);
            ArrayList<Role> roleList = new ArrayList<Role>();
            roleList.add(masterRole);

            for (int j = 1; j < number; j++) {
              int tabooCodeSub = random.nextInt(TabooConst.getTabooListSize());
              TabooCodeRole role = new TabooCodeRole();
              role.setUserName((j + 1) + "番目の入室者");
              role.setTabooCode(tabooCodeSub);
              roleList.add(role);
            }
            VillageList.get(i).setRoleList(roleList);

            message = VillageList.get(i).getVillageNum() + "村 の人数を『" + number
                + "人』に設定しました。"
                + "\n皆さんに村番号を伝えてください。"
                + "\n自分のお題、状況を確認したい場合は村番号を入力してください。";

            break;
          }
        }
      }

    } catch (Exception e) {
      if (!NumberFormatException.class.isInstance(e)) {
        e.printStackTrace();
      }
      if ("タブー".equals(userMessage.trim())) {
        int villageNum = random.nextInt(8999) + 1000;

        // 重複しない番号取得（防止のため、100回まで）
        for (int i = 0; i < 100; i++) {
          boolean breakFlg = true;
          for (Village dao : VillageList.getVillageList()) {
            if (villageNum == dao.getVillageNum()) {
              villageNum = random.nextInt(8999) + 1000;
              breakFlg = false;
              break;
            }
          }
          if (breakFlg) {
            break;
          }
        }

        Village newVillage = new TabooCodeVillage();
        newVillage.setOwnerId(userId);
        newVillage.setVillageNum(villageNum);

        VillageList.addVillage(newVillage);

        message = villageNum + "村 を新しく作成しました。\n" + MessageConst.OWNER_NUMSETMESSAGE;

      } else if ("ルール".equals(userMessage.trim())) {
        List<Message> messages = new ArrayList<Message>();
        messages.add(new TextMessage(MessageConst.RULE_FIRST));
        messages.add(new TextMessage(MessageConst.RULE_SECOND));
        messages.add(new TextMessage(MessageConst.RULE_THIRD));
        messages.add(new TextMessage(MessageConst.RULE_HINT));
        reply(replyToken, messages);
      }
    }
    // リプライ
    reply(replyToken, new TextMessage(message));
  }

  private void replyMessageVillageNum(String replyToken, String userId, int number) {
    Village village = VillageList.getVillage(number);

    if (village == null) {
      String message = MessageConst.DEFAILT_MESSAGE;
      reply(replyToken, new TextMessage(message));
      return;
    }

    Role role = village.getMemberRole(userId);
    if (role == null) {
      long nonEntryNum = village.getRoleList().stream()
          .filter(obj -> obj.getUserId() == null).count();

      if (nonEntryNum < 1L) {
        reply(replyToken, new TextMessage("村がいっぱいです。"));
        return;
      }

      // 新たな入室
      Role getRole = village.getRoleList().stream()
          .filter(obj -> obj.getUserId() == null).findFirst().orElse(null);

      getRole.setUserId(userId);

    }
    ArrayList<String> messageList = village.getRoleMessages(userId);

    List<Message> rplyMessageList = new ArrayList<Message>();
    for (String mes : messageList) {
      rplyMessageList.add(new TextMessage(mes));
    }

    reply(replyToken, rplyMessageList);
  }

}
