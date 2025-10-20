package org.takeyourdata.service.server.databases;

import io.github.jopenlibs.vault.SslConfig;
import io.github.jopenlibs.vault.Vault;
import io.github.jopenlibs.vault.VaultConfig;
import io.github.jopenlibs.vault.VaultException;
import org.takeyourdata.service.server.ConfigProperties;

import java.util.Base64;
import java.util.Map;
import java.util.Properties;

public class VaultClient {
    private final Vault vault;
    private final Properties config = new ConfigProperties().get();

    public VaultClient() throws VaultException {
        ConfigProperties config = new ConfigProperties();
        String address = config.get().getProperty("database.vault.address");
        String token = config.get().getProperty("database.vault.token");

        VaultConfig vaultConfig =
                new VaultConfig()
                        .address(address)
                        .token(token)
                        .openTimeout(5)
                        .readTimeout(30)
                        .sslConfig(new SslConfig().build())
                        .build();

        this.vault = Vault.create(vaultConfig);
    }

    public byte[] getAuthKey(int userId, byte[] authId) throws VaultException {
        Map<String, String> data = vault.logical()
                .read(config.getProperty("database.vault.path") + "/users/"
                        + userId + "/" + Base64.getEncoder().withoutPadding().encodeToString(authId))
                .getData();
        return Base64.getDecoder().decode(data.get("key"));
    }

    public Vault getVault() {
        return vault;
    }
}
