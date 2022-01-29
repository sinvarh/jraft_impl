import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

import java.io.File;

@Slf4j
public class RocksDBTest {
    @Test
    public void Test() {
        // a static method that loads the RocksDB C++ library.
        RocksDB.loadLibrary();

        // the Options class contains a set of configurable DB options
        // that determines the behaviour of the database.
        try (final Options options = new Options().setCreateIfMissing(true)) {

            // a factory method that returns a RocksDB instance
            String dbDir = "./rocksDB/testDB";
            File file = new File(dbDir);
            if (!file.exists()) {
                boolean res = file.mkdirs();
                if (res) {
                    log.info("creat file success");
                } else {
                    log.error("creat file error");
                    return;
                }
                log.info(file.getAbsolutePath());
            }

            try (final RocksDB db = RocksDB.open(options, dbDir)) {

                // do something

                byte[] key1;
                byte[] key2;
                // some initialization for key1 and key2
                key1 = "testKey1".getBytes();
                key2 = "testKey2".getBytes();
                try {
                    final byte[] value = db.get(key1);
                    if (value != null) {  // value == null if key1 does not exist in db.
                        log.info(new String(value));
                    } else {
                        db.put(key1, "value1".getBytes());
                        log.info("can not get value");
                    }
//                    db.delete(key1);
                } catch (RocksDBException e) {
                    // error handling
                    log.info("error", e);
                }
            }
        } catch (RocksDBException e) {
            // do some error handling
            log.error("error", e);
        }
    }
}
