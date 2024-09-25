package io.margit.api.event;

import io.margit.api.entity.Player;

public class PlayerJoinEvent {
  final Player player;

  public PlayerJoinEvent(Player player) {
    this.player = player;
  }

  public Player getPlayer() {
  	return player;
  }
}
