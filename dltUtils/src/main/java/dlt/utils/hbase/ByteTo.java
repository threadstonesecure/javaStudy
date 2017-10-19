package dlt.utils.hbase;

import org.apache.hadoop.hbase.util.Bytes;

public interface ByteTo<T> { // interface成员变量都默认是public static final
	

	ByteTo<byte[]> ByteArray = new ByteTo<byte[]>() {
		@Override
		public byte[] to(byte[] bs) {
			return bs;
		}
	};
	
	ByteTo<String> String = new ByteTo<String>() {
		@Override
		public String to(byte[] bs) {

			return Bytes.toString(bs);
		}


	};

	ByteTo<Long> Long = new ByteTo<Long>() {
		@Override
		public Long to(byte[] bs) {
			return Bytes.toLong(bs);
		}
	};
	ByteTo<Integer> Integer = new ByteTo<Integer>() {
		@Override
		public Integer to(byte[] bs) {
			return Bytes.toInt(bs);
		}
	};


	ByteTo<Float> Float = new ByteTo<Float>() {
		@Override
		public Float to(byte[] bs) {
			return Bytes.toFloat(bs);
		}
	};

	ByteTo<Double> Double = new ByteTo<Double>() {
		@Override
		public Double to(byte[] bs) {
			return Bytes.toDouble(bs);
		}
	};

	ByteTo<Boolean> Boolean = new ByteTo<Boolean>() {
		@Override
		public Boolean to(byte[] bs) {
			return Bytes.toBoolean(bs);
		}
	};

	ByteTo<Short> Short = new ByteTo<Short>() {
		@Override
		public Short to(byte[] bs) {
			return Bytes.toShort(bs);
		}
	};
	
	T to(byte[] bs);
}
