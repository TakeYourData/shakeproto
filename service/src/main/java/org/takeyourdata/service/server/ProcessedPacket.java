package org.takeyourdata.service.server;

import org.takeyourdata.protocol.packets.Packet;

public interface ProcessedPacket {
    void process(Packet result) throws Exception;
}
