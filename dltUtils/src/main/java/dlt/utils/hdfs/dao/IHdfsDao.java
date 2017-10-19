package dlt.utils.hdfs.dao;

import java.io.IOException;

public interface IHdfsDao {
	public void mkdirs(String folder) throws IOException;

	public void rm(String folder) throws IOException;

	public void rename(String src, String dst) throws IOException;

	public void ls(String folder) throws IOException;

	public void createFile(String file, String content) throws IOException;

	public void copyFile(String local, String remote) throws IOException;

	public void download(String remote, String local) throws IOException;

	public void cat(String remoteFile) throws IOException;

	public int getLineNum(String remoteFile) throws IOException;

	public void location() throws IOException;
}
