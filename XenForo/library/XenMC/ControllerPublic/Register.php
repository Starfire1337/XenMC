<?php
class XenMC_ControllerPublic_Register extends XFCP_XenMC_ControllerPublic_Register {

	public function actionRegister() {

		$db = XenForo_Application::get('db');

		$this->_assertPostOnly();
		$this->_assertRegistrationActive();

		$inputData = $this->_getRegistrationInputDataSafe();
		$data = $inputData['data'];
		$customFields = $inputData['customFields'];
		$errors = $inputData['errors'];

		$mc_array = json_decode(file_get_contents("https://api.mojang.com/users/profiles/minecraft/".$inputData["data"]["username"]));
		$uuid = substr(@$mc_array->id, 0, 8) . "-" . substr(@$mc_array->id, 8, 4) . "-" . substr(@$mc_array->id, 12, 4) . "-" . substr(@$mc_array->id, 16, 4) . "-" . substr(@$mc_array->id, 20);

		$result = $db->fetchAll("
			SELECT username
			FROM minecraft_users
			WHERE ip=?
			AND username=?
			AND uuid=?
		", array($_SERVER['REMOTE_ADDR'], $inputData['data']['username'], $uuid));

		if(count($result) == 0) {
			$errors[] = new XenForo_Phrase("xenmc_username_invalid");
		}

		if($errors) {
			$fields = $data;
			$fields['tos'] = $this->_input->filterSingle('agree', XenForo_Input::UINT);
			$fields['custom_fields'] = $customFields;
			return $this->_getRegisterFormResponse($fields, $errors);
		}

		return parent::actionRegister();
	}

}