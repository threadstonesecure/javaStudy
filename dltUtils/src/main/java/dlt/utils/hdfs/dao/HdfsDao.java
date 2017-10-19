package dlt.utils.hdfs.dao;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.IOUtils;

import java.io.*;
import java.net.URI;

public class HdfsDao implements IHdfsDao {

	public HdfsDao() {
		Configuration conf = new Configuration();
		hdfsPath = FileSystem.getDefaultUri(conf);
		this.conf = conf;
	}

	public HdfsDao(String hdfs) {
		Configuration conf = new Configuration();
		hdfsPath = URI.create(hdfs);
		this.conf = conf;
	}

	public HdfsDao(Configuration conf) {
		hdfsPath = FileSystem.getDefaultUri(conf);
		this.conf = conf;
	}

	public HdfsDao(String hdfs, Configuration conf) {
		this.hdfsPath = URI.create(hdfs);
		this.conf = conf;
	}

	private URI hdfsPath;
	private Configuration conf;

	@Override
	public void mkdirs(String folder) throws IOException {
		Path path = new Path(folder);
		FileSystem fs = FileSystem.get(hdfsPath, conf);
		if (!fs.exists(path)) {
			fs.mkdirs(path);
			System.out.println("Create: " + folder);
		}
		fs.close();
	}

	@Override
	public void rm(String folder) throws IOException {
		Path path = new Path(folder);
		FileSystem fs = FileSystem.get(hdfsPath, conf);
		fs.deleteOnExit(path);
		System.out.println("Delete: " + folder);
		fs.close();
	}

	@Override
	public void rename(String src, String dst) throws IOException {
		Path name1 = new Path(src);
		Path name2 = new Path(dst);
		FileSystem fs = FileSystem.get(hdfsPath, conf);
		fs.rename(name1, name2);
		System.out.println("Rename: from " + src + " to " + dst);
		fs.close();
	}

	@Override
	public void ls(String folder) throws IOException {
		Path path = new Path(folder);
		FileSystem fs = FileSystem.get(hdfsPath, conf);
		FileStatus[] list = fs.listStatus(path);
		System.out.println("ls: " + folder);
		System.out
				.println("==========================================================");
		for (FileStatus f : list) {
			System.out.printf("name: %s, folder: %s, size: %d\n", f.getPath(),
					f.isDirectory(), f.getLen());
		}
		System.out
				.println("==========================================================");
		fs.close();
	}

	@Override
	public void createFile(String file, String content) throws IOException {
		FileSystem fs = FileSystem.get(hdfsPath, conf);
		byte[] buff = content.getBytes();
		FSDataOutputStream os = null;
		try {
			os = fs.create(new Path(file));
			os.write(buff, 0, buff.length);
			System.out.println("Create: " + file);
		} finally {
			if (os != null)
				os.close();
		}
		fs.close();
	}

	@Override
	public void copyFile(String local, String remote) throws IOException {
		FileSystem fs = FileSystem.get(hdfsPath, conf);
		fs.copyFromLocalFile(new Path(local), new Path(remote));
		System.out.println("copy from: " + local + " to " + remote);
		fs.close();
	}

	@Override
	public void download(String remote, String local) throws IOException {
		Path path = new Path(remote);
		FileSystem fs = FileSystem.get(hdfsPath, conf);
		fs.copyToLocalFile(path, new Path(local));
		System.out.println("download: from" + remote + " to " + local);
		fs.close();
	}

	@Override
	public void cat(String remoteFile) throws IOException {
		Path path = new Path(remoteFile);
		FileSystem fs = FileSystem.get(hdfsPath, conf);
		FSDataInputStream fsdis = null;
		System.out.println("cat: " + remoteFile);

		OutputStream baos = new ByteArrayOutputStream();
		String str = null;

		try {
			fsdis = fs.open(path);
			IOUtils.copyBytes(fsdis, baos, 4096, false);
			str = baos.toString();
		} finally {
			IOUtils.closeStream(fsdis);
			fs.close();
		}
		System.out.println(str);

	}

	@Override
	public int getLineNum(String remoteFile) throws IOException {
		int linenum = 0;
		Path path = new Path(remoteFile);
		FileSystem fs = FileSystem.get(hdfsPath, conf);
		FSDataInputStream fsdis = null;

		System.out.println("cat: " + remoteFile);

		try {
			fsdis = fs.open(path);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					fsdis));
			String line;
			line = reader.readLine();
			while (line != null) {
				linenum++;
				line = reader.readLine();
			}

		} finally {
			IOUtils.closeStream(fsdis);
			fs.close();
		}
		return linenum;
	}

	@Override
	public void location() throws IOException {
		// String folder = hdfsPath + "create/";
		// String file = "t2.txt";
		// FileSystem fs = FileSystem.get(URI.create(hdfsPath), new
		// Configuration());
		// FileStatus f = fs.getFileStatus(new Path(folder + file));
		// BlockLocation[] list = fs.getFileBlockLocations(f, 0, f.getLen());
		//
		// System.out.println("File Location: " + folder + file);
		// for (BlockLocation bl : list) {
		// String[] hosts = bl.getHosts();
		// for (String host : hosts) {
		// System.out.println("host:" + host);
		// }
		// }
		// fs.close();
	}


}