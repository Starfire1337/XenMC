package com.starfire1337.XenMC;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class XenMC extends JavaPlugin implements Listener{
	
	public static XenMC plugin;
	public MySQL sql = new MySQL();
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		saveDefaultConfig();
		plugin = this;
		getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			@Override
			public void run() {
				sql.connect();
			}
		});
	}
	
	@Override
	public void onDisable() {
		sql.close();
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if(p.getAddress().getAddress().getHostAddress() == null) {
			getLogger().info(p.getName() + "'s IP address was returned as null. MySQL query aborted.");
			return;
		}
		final String uuid = p.getUniqueId().toString();
		final String name = p.getName();
		final String ip = p.getAddress().getAddress().getHostAddress();
		getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			@Override
			public void run() {
				if(!sql.isConn()) {
					sql.connect();
				}
				try {
					PreparedStatement ps = sql.prepare("INSERT INTO " + getConfig().getString("MySQL.table") + " (uuid, username, ip) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE username=?,ip=?");
					ps.setString(1, uuid);
					ps.setString(2, name);
					ps.setString(3, ip);
					ps.setString(4, name);
					ps.setString(5, ip);
					sql.query(ps);
				} catch (SQLException e1) {
					getLogger().info("Failed to excecute async MySQL query.");
					getLogger().info(e1.getMessage());
				}
			}
		});
	}
}
