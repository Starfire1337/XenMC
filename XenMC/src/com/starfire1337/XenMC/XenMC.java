package com.starfire1337.XenMC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
				try {
					PreparedStatement ps = sql.prepare("SELECT ip FROM " + getConfig().getString("MySQL.table") + " WHERE uuid=?");
					ps.setString(1, uuid);
					ResultSet rs = sql.query(ps);
					int i = 0;
					while(rs.next()) {
						i++;
					}
					if(i == 0) {
						ps = sql.prepare("INSERT INTO " + getConfig().getString("MySQL.table") + " (uuid, username, ip) VALUES (?, ?, ?)");
						ps.setString(1, uuid);
						ps.setString(2, name);
						ps.setString(3, ip);
						sql.query(ps);
					} else {
						ps = sql.prepare("UPDATE " + getConfig().getString("MySQL.table") + " SET uuid=?, username=?, ip=?");
						ps.setString(1, uuid);
						ps.setString(2, name);
						ps.setString(3, ip);
						sql.query(ps);
					}
				} catch (SQLException e1) {
					getLogger().info("Failed to excecute async MySQL query.");
					getLogger().info(e1.getMessage());
				}
			}
		});
	}
}
