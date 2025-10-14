package org.takeyourdata.service.server.handlers;

import org.takeyourdata.service.server.ProcessedPacket;

public interface Handler {
    void handle(ProcessedPacket packet) throws Exception;
}
