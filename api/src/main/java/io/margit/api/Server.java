package io.margit.api;

public interface Server {
  void broadcastMessage(String message);
  EventManager getEventmManager();
}
