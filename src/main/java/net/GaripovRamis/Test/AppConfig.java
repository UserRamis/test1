package net.GaripovRamis.Test;

import net.GaripovRamis.Test.storage.SqlStorageImpl;
import net.GaripovRamis.Test.storage.Storage;

import java.io.InputStream;
import java.util.Properties;

public class AppConfig {

    private static final AppConfig INSTANCE = new AppConfig();

    private Storage storage;

    private Properties appProps;

    public static AppConfig get() {
        return INSTANCE;
    }

    public Storage getStorage() {
        return storage;
    }

    private AppConfig() {
        try (InputStream dbProp = getClass().getClassLoader().getResourceAsStream("db.properties")) {
            appProps = new Properties();
            appProps.load(dbProp);
            storage = new SqlStorageImpl(
                    appProps.getProperty("db.url"),
                    appProps.getProperty("db.user"),
                    appProps.getProperty("db.password"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
