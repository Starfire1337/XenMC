<?php
class XenMC_Install {
	public static function install() {
		$db = XenForo_Application::get('db');
		$db->query("CREATE TABLE IF NOT EXISTS `minecraft_users`(`uuid` varchar(36) NOT NULL default '', `username` varchar(16) NOT NULL default '', `ip` varchar(15) NOT NULL default '', PRIMARY KEY(`uuid`));");
	}
}