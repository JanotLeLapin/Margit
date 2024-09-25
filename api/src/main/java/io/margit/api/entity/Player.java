package io.margit.api.entity;

public interface Player {
  void closeInventory();
  String getName();
  String getLocale();
  void sendMessage(String message);
}
