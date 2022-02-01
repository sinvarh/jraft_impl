package main;

import com.alipay.sofa.rpc.common.json.JSON;

import lombok.extern.slf4j.Slf4j;
import main.model.log.LogEntry;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

import java.io.File;
import java.util.List;

@Slf4j
//todo 这里的写会有并发问题
public class DefaultLogModule implements LogModule {
    private RocksDB db;
    private static final String lastIndexKey = "LAST_INDEX";

    public DefaultLogModule(int port) {
        //初始化连接
        final Options options = new Options().setCreateIfMissing(true);
        String dbDir = "./rocksDB/logDB/" + port;
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
    public void appendEntries(List<LogEntry> entries) {
        for (LogEntry entry : entries) {
            write(entry);
        }
    }

    @Override
    public LogEntry getLast() {
        try {
            byte[] res = db.get(lastIndexKey.getBytes());
            if (res == null) {
                return null;
            } else {
                return read(Utils.bytesToLong(res));
            }
        } catch (Exception e) {
            log.error("get last", e);
        }
        return null;
    }

    @Override
    public long getLastIndex() {
        LogEntry logEntry = getLast();
        if (logEntry == null) {
            return 0;
        } else {
            return logEntry.getIndex();
        }
    }

    @Override
    public LogEntry read(Long index) {
        try {
            byte[] res = db.get(Utils.longToBytes(index));
            if (res == null) {
                return null;
            }
            return JSON.parseObject(new String(res), LogEntry.class);
        } catch (Exception e) {
            log.error("read error", e);
        }
        return null;
    }

    @Override
    public boolean removeFromStartIndex(Long startIndex) {
        //todo 怎么删除
        return false;
    }

    @Override
    public void write(LogEntry logEntry) {
        byte[] key = Utils.longToBytes(logEntry.getIndex());
        byte[] lastIndexKeyByte = lastIndexKey.getBytes();
        try {
            db.put(key, JSON.toJSONString(logEntry).getBytes());
            db.put(lastIndexKeyByte, Utils.longToBytes(logEntry.getIndex()));
        } catch (RocksDBException e) {
            log.error("error", e);
        }
    }
}
