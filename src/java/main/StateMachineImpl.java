package main;


import com.alipay.sofa.rpc.common.json.JSON;
import lombok.extern.slf4j.Slf4j;
import main.model.log.LogEntry;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

import java.io.File;
import java.util.Arrays;

/**
 * 复制状态机interface
 *
 * @author sinvar
 */
@Slf4j
public class StateMachineImpl implements StateMachine {
    private RocksDB db;

    public StateMachineImpl(int port) {
        //初始化连接
        final Options options = new Options().setCreateIfMissing(true);
        String dbDir = "./rocksDB/kvDB/" + port;
        File file = new File(dbDir);
        if (!file.exists()) {
            boolean res = file.mkdirs();
            if (res) {
                log.info("create file success");
            } else {
                log.error("create file error");
                return;
            }
            log.info(file.getAbsolutePath());
        }
        try {
            db = RocksDB.open(options, dbDir);
        } catch (RocksDBException e) {
            log.error("init db error", e);
        }
    }

    @Override
    public void apply(LogEntry logEntry) {
        byte[] key = logEntry.getCommand().getKey().getBytes();
        try {
            db.put(key, logEntry.getCommand().getValue().getBytes());
        } catch (RocksDBException e) {
            log.error("error", e);
        }
    }

    @Override
    public String read(String key) {
        try {
            return new String(db.get(key.getBytes()));
        } catch (RocksDBException e) {
            log.error("read error",e);
        }
        return null;
    }
}
