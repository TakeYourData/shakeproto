package org.takeyourdata.service.server;

public record Session(
        int userId,
        String clientId,
        String hardwareId,
        String location
) {
}
