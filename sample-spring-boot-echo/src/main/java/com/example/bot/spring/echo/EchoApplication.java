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

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.bot.spring.entity.Village;
import com.example.bot.staticdata.MessageConst;
import com.example.bot.staticdata.VillageList;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
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

    // messageの取得
    String message = getMessage(userId, userMessage);

    // 返信用メッセージ
    TextMessage textMessage = new TextMessage(message);

    reply(event.getReplyToken(), Arrays.asList(textMessage, textMessage));
  }

  @EventMapping
  public void handleDefaultMessageEvent(Event event) {
    System.out.println("event: " + event);
    System.out.println("動いています");
  }

  private void reply(@NonNull String replyToken, @NonNull List<Message> messages) {
    try {
      lineMessagingClient
          .replyMessage(new ReplyMessage(replyToken, messages))
          .get();
    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    }
  }

  private String getMessage(String userId, String userMessage) {
    String message = MessageConst.DEFAILT_MESSAGE;

    Random random = new Random();

    try {
      int number = Integer.parseInt(userMessage.trim());

      if (number > 100) {
        // 村番号の場合
        return getMessageVillageNum(userId, number);
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

            // インサイダー位置設定
            int insiderNum = random.nextInt(number) + 1;
            VillageList.get(i).setInsiderNum(insiderNum);
            if (VillageList.get(i).getGmNum() == MessageConst.DEFAULT_GMNUM) {
              int gmNum = random.nextInt(number) + 1;
              while (gmNum == insiderNum) {
                gmNum = random.nextInt(number) + 1;
              }
              VillageList.get(i).setGmNum(gmNum);
            }

            message = VillageList.get(i).getVillageNum() + "村 の人数を『" + number
                + "人』に設定しました。"
                + "\n皆さんに村番号を伝えてください。"
                + "\n配布状況を確認したい場合は村番号を入力してください。";

            break;
          }
        }
      }

    } catch (Exception e) {
      if ("お題".equals(userMessage.trim()) || "題".equals(userMessage.trim())
          || "神".equals(userMessage.trim())) {
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

        Village newVillage = new Village();
        newVillage.setOwnerId(userId);
        newVillage.setVillageNum(villageNum);

        if ("神".equals(userMessage.trim())) {
          newVillage.setGmNum(MessageConst.DEFAULT_GMNUM);
        }

        VillageList.addVillage(newVillage);

        message = villageNum + "村 を新しく作成しました。\n" + MessageConst.OWNER_ODAIMESSAGE;

      } else {
        for (int i = VillageList.getVillageList().size() - 1; i >= 0; i--) {
          if (null == VillageList.get(i).getOdai()
              && userId.equals(VillageList.get(i).getOwnerId())) {
            VillageList.get(i).setOdai(userMessage);
            message = VillageList.get(i).getVillageNum() + "村 のお題を『" + userMessage + "』に設定しました。\n";
            if (VillageList.get(i).getGmNum() == MessageConst.DEFAULT_GMNUM) {
              message = message + MessageConst.GOD_NUMSETMESSAGE;
            } else {
              message = message + MessageConst.OWNER_NUMSETMESSAGE;
            }
            break;
          }
        }
      }

    }

    return message;
  }

  private String getMessageVillageNum(String userId, int number) {
    String message = MessageConst.DEFAILT_MESSAGE;

    Village village = VillageList.getVillage(number);

    if (userId.equals(village.getOwnerId())) {
      // オーナーの場合
      message = village.getMessageOwner();

    } else {

      // 参加書の場合
      String memberRole = village.getMemberRole(userId);
      if (memberRole == null) {

        if (village.getRoleList().size() >= village.getVillageSize()) {
          message = "村がいっぱいです。";
          return message;
        }
        // 配役の設定
        village.addRoleList(
            village.getInsiderNum() == village.getRoleList().size() + 1
                ? MessageConst.INSIDER_ROLE
                : village.getGmNum() == village.getRoleList().size() + 1
                    ? MessageConst.GAMEMASTER_ROLE
                    : MessageConst.VILLAGE_ROLE,
            userId);

        message = village.getRoleMessage(userId);

      } else {
        message = village.getRoleMessage(userId);
      }

    }

    return message;
  }

}
