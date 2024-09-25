package io.margit.api.event;

import io.margit.api.entity.Player;

public class ChatMessageEvent {
  private final Player player;
  private final String content;

  public ChatMessageEvent(Player player, String content) {
    this.player = player;
    this.content = content;
  }

  public Player getPlayer() {
  	return player;
  }

  public String getContent() {
  	return content;
  }
}
