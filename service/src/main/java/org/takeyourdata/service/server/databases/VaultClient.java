package org.takeyourdata.service.server.databases;

import io.github.jopenlibs.vault.SslConfig;
import io.github.jopenlibs.vault.Vault;
import io.github.jopenlibs.vault.VaultConfig;
import io.github.jopenlibs.vault.VaultException;
import org.takeyourdata.service.server.ConfigProperties;

public class VaultClient {
    private final Vault vault;

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

    public Vault getVault() {
        return vault;
    }
}
