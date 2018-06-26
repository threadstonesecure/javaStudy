package com.yuntai.hdp.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import com.yuntai.hdp.server.updata.Command;
import com.yuntai.hdp.server.updata.Registry;
import org.springframework.stereotype.Service;

/**
 * @Description 获取数据库中的HDP配置信息
 * @author denglt
 *
 */
@Service
public class HdpDao {

	@Resource
	private DataSource dataSource;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public List<Registry> getRegistry() throws Exception {
		List<Registry> regs = new ArrayList<Registry>();
		String sql = "select registry_id,protocol,address,port,username,password,groupname,memo"
				   + "  from hdp_dubbo_registry where enable = 1 ";
		Connection conn = dataSource.getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Registry registry = new Registry();
				registry.setRegistryId(rs.getInt("registry_id"));
				registry.setProtocol(rs.getString("protocol"));
				registry.setAddress(rs.getString("address"));
				rs.getInt("port");
				if (rs.wasNull())
					registry.setPort(null);
				else
					registry.setPort(rs.getInt("port"));
				registry.setUserName(rs.getString("username"));
				registry.setPassword(rs.getString("password"));
                registry.setGroupname(rs.getString("groupname"));
				regs.add(registry);
			}
			rs.close();
		} finally {
			if (stmt != null)
				stmt.close();
			conn.close();
		}
		return regs;
	}

	public List<Command> getCommand() throws Exception {
		List<Command> cmds = new ArrayList<Command>();
		String sql = "select command ,interfacename,groupname,registry_id,memo"
				+ "   from hdp_updatahandler   where enable = 1";
		Connection conn = dataSource.getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Command cmd = new Command();
				cmd.setCode(rs.getString("command"));
				cmd.setInterfaceName(rs.getString("interfacename"));
				cmd.setGroupName(rs.getString("groupname"));
				cmd.setRegistryId(rs.getInt("registry_id"));
				cmd.setMemo(rs.getString("memo"));
				cmds.add(cmd);
			}
			rs.close();
		} finally {
			if (stmt != null)
				stmt.close();
			conn.close();
		}
		return cmds;
	}

}
