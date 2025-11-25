package com.project;

/**
 * Observer interface following the Observer Pattern and Interface Segregation Principle (SOLID).
 * Classes implementing this interface will be notified of game events.
 */
public interface GameObserver {
    /**
     * Called when a game event occurs.
     */
    void update(GameEvent event);
}
