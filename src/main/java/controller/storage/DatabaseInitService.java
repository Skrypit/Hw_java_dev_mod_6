package controller.storage;

import org.flywaydb.core.Flyway;

public class DatabaseInitService {

    public void initDb() {

        Flyway flyway = Flyway
                .configure()
                .dataSource(StorageConst.CONNECTION_URL, StorageConst.DB_LOGIN, StorageConst.DB_PASSWORD)
                .load();
        flyway.migrate();
    }
}
