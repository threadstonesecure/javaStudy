package watchhdp;

import com.google.common.base.Joiner;
import com.yuntai.hdp.access.RequestPack;
import com.yuntai.hdp.access.ResultPack;
import org.rocksdb.*;
import org.rocksdb.util.SizeUnit;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 仅统计业务量
 */
public class StatsDataHandler extends DataHandler {

    private RocksDB hdpDB;
    private Options options;
    private boolean open = false;


    public StatsDataHandler(String name) throws RocksDBException {

        options = new Options().setCreateIfMissing(true)
                .setComparator(BuiltinComparator.BYTEWISE_COMPARATOR)
                .setWriteBufferSize(8 * SizeUnit.KB);
        hdpDB = RocksDB.open(options, name);
        open = true;
    }


    @Override
    protected ResultPack deal(Object msg) {
        if (open && msg instanceof RequestPack) {
            RequestPack requestPack = (RequestPack) msg;
            increase(getStatKey(requestPack.getHosId(), requestPack.getCmd()));
        }
        return null;
    }

    private synchronized void increase(String aKey) {
        try {
            byte[] key = aKey.getBytes(StandardCharsets.UTF_8);
            byte[] value = hdpDB.get(key);
            if (value == null)
                hdpDB.put(key, serialize(1));
            else {
                Integer temp = deserialize(value) + 1;
                WriteOptions wOpts = new WriteOptions().setDisableWAL(false).setSync(false);
                hdpDB.put(wOpts, key, serialize(temp));
            }
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }
    }

    private String getStatKey(String hosId, String cmd) {
        String hour = hourDataFormat.format(new Date());
        return Joiner.on("_").join(hosId, hour, cmd);
    }

    public synchronized void close() {
        if (!open) return;
        open = false;
        options.close();
        hdpDB.close();

        options = null;
        hdpDB = null;
    }

    private Integer deserialize(byte[] data) {
        if (data == null) {
            return null;
        } else if (data.length != 4) {
            throw new RuntimeException("Size of data received by IntegerDeserializer is not 4");
        } else {
            int value = 0;
            byte[] arr$ = data;
            int len$ = data.length;

            for (int i$ = 0; i$ < len$; ++i$) {
                byte b = arr$[i$];
                value <<= 8;
                value |= b & 255;
            }
            return value;
        }
    }

    public byte[] serialize(Integer data) {
        return data == null ? null : new byte[]{(byte) (data >>> 24), (byte) (data >>> 16), (byte) (data >>> 8), data.byteValue()};
    }

    @Override
    protected void finalize() throws Throwable {
        close();
    }

    private SimpleDateFormat hourDataFormat = new SimpleDateFormat("yyyyMMddHH");

    public static void main(String[] args) {

    }
}
