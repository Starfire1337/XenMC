<?php
class XenMC_Listener {
	public static function load_class_controller($class, array &$extend) {
		$extend[] = 'XenMC_ControllerPublic_Register';
	}
}