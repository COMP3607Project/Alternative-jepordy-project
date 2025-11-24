package com.project;
import java.time.LocalDateTime;

public interface GameCommand {
    public void execute();

    public void undo();

    public LocalDateTime getTimestamp();
}
