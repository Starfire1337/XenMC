package com.starfire1337.XenMC;


import java.sql.*;
import java.util.Properties;

public class MySQL {
	private Connection con;
	private ResultSet rs;
	private PreparedStatement ps;
	
	protected boolean loadDriver() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			return true;
		} catch(ClassNotFoundException ex) {
			ex.printStackTrace();
			XenMC.plugin.getLogger().info("Driver not found.");
		}
		return false;
	}
	
	protected Properties getProperties() {
		Properties prop = new Properties();
		prop.put("autoReconnect", "true");
		prop.put("user", XenMC.plugin.getConfig().get("MySQL.username"));
		prop.put("password", XenMC.plugin.getConfig().get("MySQL.password"));
		prop.put("useUnicode", "true");
		prop.put("characterEncoding", "utf8");
		return prop;
	}
	
	public boolean connect() {
		if(loadDriver()) {
			try {
				con = DriverManager.getConnection("jdbc:mysql://" + XenMC.plugin.getConfig().get("MySQL.host") + ":" + XenMC.plugin.getConfig().get("MySQL.port") + "/" + XenMC.plugin.getConfig().get("MySQL.database"), getProperties());
				generateTable();
				XenMC.plugin.getLogger().info("Connected to MySQL Database.");
				return true;
			} catch(SQLException ex) {
				XenMC.plugin.getLogger().info("Failed to connect to database. Please check your database details.");
				XenMC.plugin.getLogger().info(ex.getMessage());
				return false;
			}
		}
		return false;
	}
	
	public boolean isConn() {
		if(con != null) {
			try {
				if(con.isValid(1)) {
					return true;
				}
				return false;
			} catch(SQLException ex) {
				return false;
			}
		}
		return false;
	}
	
	public ResultSet query(PreparedStatement ps) throws SQLException {
		if(isConn()) {
			ps.execute();
			rs = ps.getResultSet();
			return rs;
		}
		return null;
	}
	
	public PreparedStatement prepare(String q) throws SQLException {
		if(isConn()) {
			ps = con.prepareStatement(q);
			return ps;
		}
		return null;
	}
	
	public boolean close() {
		if (isConn()) {
			try {
				this.con.close();
				return true;
			} catch(SQLException ex) {
				return false;
			}
		}
	return false;
	}
	
	public void generateTable() {
		try {
			PreparedStatement ps = prepare("CREATE TABLE IF NOT EXISTS `" + XenMC.plugin.getConfig().get("MySQL.table") + "`(`uuid` varchar(36) NOT NULL default '', `username` varchar(16) NOT NULL default '', `ip` varchar(15) NOT NULL default '', PRIMARY KEY(`uuid`));");
			query(ps);
		} catch (SQLException e) {
			XenMC.plugin.getLogger().info("Failed to create table.");
			e.printStackTrace();
		}
	}
	
}
