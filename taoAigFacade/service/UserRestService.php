<?php

/**
 * Copyright (c) 2016-2017  Luxembourg Institute of Science and Technology (LIST).
 * 
 * This software is licensed under the Apache License, Version 2.0 (the "License") ; you
 * may not use this file except in compliance with the License. You may obtain a copy of the License
 * at : http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 * 
 * for more information about the software, please contact info@list.lu
 */
namespace itis\taoAigFacade\service;

include_once 'RestService.php';

use itis\taoAigFacade\models\exceptions\AlreadyExistsException;
use itis\taoAigFacade\models\exceptions\NoSuchUserException;
use itis\taoAigFacade\helpers\PropertyHelper;

/**
 * Class managing the REST calls to register, update, and delete users.
 *
 * @author Olivier Pedretti [oliver.pedretti@list.lu]
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 1.0
 * @version 1.0.44
 */
class UserRestService extends RestService {
	
	/**
	 * Method called upon receiving a REST request to handle a user service.
	 *
	 * @throws \common_exception_NotImplemented If the HTTP method is not yet supported
	 */
	public static function handleRequest() {
		$method = $_SERVER ['REQUEST_METHOD'];
		switch ($method) {
			case 'POST' :
				self::createUser ();
				break;
			case 'PUT' :
				self::updateUser ();
				break;
			case 'DELETE' :
				self::deleteUser ();
				break;
			case 'GET' :
				self::getUser ();
				break;
			default :
				throw new \common_exception_NotImplemented ( 'HTTP method not implemented: ' . $method );
		}
	}
	
	/**
	 * Function called to create and add a user to TAO.
	 * The function will read the input by parsing the provided URL
	 * and splitting all folders into key-value pairs. As such their order does not matter. The function checks if a
	 * login is available before creating the user.
	 *
	 * @throws AlreadyExistsException Thrown when the creation of a user is unsuccessful due to the login already existing.
	 * @return A response code of 201 will be returned if the user is created successfully.
	 */
	protected static function createUser() {
		$inputs = self::parseInput ();
		$taoUserService = \tao_models_classes_UserService::singleton ();
		
		if (! array_key_exists ( 'name', $inputs ) || ! array_key_exists ( 'label', $inputs ) || ! array_key_exists ( 'password', $_GET )) {
			throw new \common_exception_NotAcceptable ( 'Parameter missing. Name, label, and password are required to create a user. Additional parameters are optional!' );
		}
		
		if (! $taoUserService->loginAvailable ( $inputs ['name'] )) {
			throw new AlreadyExistsException ( 'Login empty or already exists!' );
		}
		
		$taoUserRole = new \core_kernel_classes_Resource ( INSTANCE_ROLE_DELIVERY );
		$taoUserClass = new \core_kernel_classes_Class ( TAO_SUBJECT_CLASS );
		$userResource = $taoUserService->addUser ( $inputs ['name'], $_GET ['password'], $taoUserRole, $taoUserClass );
		$userResource->setLabel ( $inputs ['label'] );
		if (array_key_exists ( 'mail', $inputs )) {
			$userResource->setPropertyValue ( new \core_kernel_classes_Property ( PROPERTY_USER_MAIL ), $inputs ['mail'] );
		}
		if (array_key_exists ( 'firstName', $inputs )) {
			$userResource->setPropertyValue ( new \core_kernel_classes_Property ( PROPERTY_USER_FIRSTNAME ), $inputs ['firstName'] );
		}
		if (array_key_exists ( 'lastName', $inputs )) {
			$userResource->setPropertyValue ( new \core_kernel_classes_Property ( PROPERTY_USER_LASTNAME ), $inputs ['lastName'] );
		}
		
		// TODO Add checks on language validity
		$uiLanguageProperty = new \core_kernel_classes_Property ( PROPERTY_USER_UILG );
		$language = empty ( $inputs ['language'] ) ? DEFAULT_LANG : $inputs ['language'];
		$userResource->setPropertyValue ( $uiLanguageProperty, 'http://www.tao.lu/Ontologies/TAO.rdf#Lang' . $language );
		$dataLangaugeProperty = new \core_kernel_classes_Property ( PROPERTY_USER_DEFLG );
		$userResource->setPropertyValue ( $dataLangaugeProperty, 'http://www.tao.lu/Ontologies/TAO.rdf#Lang' . $language );
		
		$taoUserService->attachRole ( $userResource, new \core_kernel_classes_Resource ( INSTANCE_ROLE_GLOBALMANAGER ) );
		
		if (array_key_exists ( 'group', $inputs )) {
			GroupService::createGroup ( $inputs ['group'], $inputs ['name'] );
		}
		
		\common_Logger::t ( 'user created, label: ' . $userResource->getLabel () . ' login: ' . $inputs ['name'] . (array_key_exists ( 'group', $inputs ) ? ' group:' . $inputs ['group'] : ' nogroup') );
		
		// display result
		http_response_code ( 201 );
		echo "User Created";
	}
	protected static function getUser() {
		$inputs = self::parseInput ();
		$taoUserService = \tao_models_classes_UserService::singleton ();
		
		if (! array_key_exists ( 'name', $inputs )) {
			throw new \common_exception_NotAcceptable ( 'Parameter missing. Name is required to update a user. Additional parameters are optional!' );
		}
		
		if ($taoUserService->loginAvailable ( $inputs ['name'] )) {
			throw new NoSuchUserException ( 'No such user exists: ' . $inputs ['name'] );
		}
		
		$userResource = UserService::getUserByLogin ( $inputs ['name'] );
		
		$userLogin = empty ( $userResource->getPropertyValues ( new \core_kernel_classes_Property ( PROPERTY_USER_LOGIN ) ) ) ? 'None' : $userResource->getPropertyValues ( new \core_kernel_classes_Property ( PROPERTY_USER_LOGIN ) ) [0];
		$userFirstName = empty ( $userResource->getPropertyValues ( new \core_kernel_classes_Property ( PROPERTY_USER_FIRSTNAME ) ) ) ? 'None' : $userResource->getPropertyValues ( new \core_kernel_classes_Property ( PROPERTY_USER_FIRSTNAME ) ) [0];
		$userLastName = empty ( $userResource->getPropertyValues ( new \core_kernel_classes_Property ( PROPERTY_USER_LASTNAME ) ) ) ? 'None' : $userResource->getPropertyValues ( new \core_kernel_classes_Property ( PROPERTY_USER_LASTNAME ) ) [0];
		$userMail = empty ( $userResource->getPropertyValues ( new \core_kernel_classes_Property ( PROPERTY_USER_MAIL ) ) ) ? 'None' : $userResource->getPropertyValues ( new \core_kernel_classes_Property ( PROPERTY_USER_MAIL ) ) [0];
		$userDataLanguage = empty ( $userResource->getPropertyValues ( new \core_kernel_classes_Property ( PROPERTY_USER_DEFLG ) ) ) ? 'None' : $userResource->getPropertyValues ( new \core_kernel_classes_Property ( PROPERTY_USER_DEFLG ) ) [0];
		$userUiLanguage = empty ( $userResource->getPropertyValues ( new \core_kernel_classes_Property ( PROPERTY_USER_UILG ) ) ) ? 'None' : $userResource->getPropertyValues ( new \core_kernel_classes_Property ( PROPERTY_USER_UILG ) ) [0];
		
		$jsonUserData = array (
				'login' => $userLogin,
				'firstName' => $userFirstName,
				'lastName' => $userLastName,
				'mail' => $userMail,
				'userDataLanguage' => $userDataLanguage,
				'userUiLanguage' => $userUiLanguage 
		);
		header ( 'Content-Type: application/json' );
		echo json_encode ( $jsonUserData );
	}
	
	/**
	 * Method used to delete a user for the parameters passed with the request.
	 *
	 * @throws \common_exception_NotAcceptable Thrown if one or more of the required parameters are missing.
	 * @throws AlreadyExistsException Thrown if a user with a given login already exists.
	 */
	protected static function deleteUser() {
		$inputs = self::parseInput ();
		
		if (! array_key_exists ( 'name', $inputs )) {
			throw new \common_exception_NotAcceptable ( 'Parameter missing. Name is required to update a user. Additional parameters are optional!' );
		}
		
		$taoUserService = \tao_models_classes_UserService::singleton ();
		
		if (! $taoUserService->loginAvailable ( $inputs ['name'] )) {
			$userResource = UserService::getUserByLogin ( $inputs ['name'] );
			$userResource->delete ( true );
		} else {
			throw new NoSuchUserException ( 'No such user exists: ' . $inputs ['name'] );
		}
		
		/**
		 * http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html
		 * A successful response SHOULD be 200 (OK) if the response includes an entity describing the status,
		 * 202 (Accepted) if the action has not yet been enacted, or 204 (No Content) if the action has been
		 * enacted but the response does not include an entity.
		 */
		http_response_code ( 204 );
		echo "User Deleted";
	}
	
	/**
	 * Method used to update the data stored for a user.
	 * The following properties can be updates:<br>
	 * <ul>
	 * <li>password: The new password to set for the user!</li>
	 * <li>language: The language to set the UI and data language to. The passed code must be of the format en_US as specified by ISO 3166 and ISO 639-1.</li>
	 * <li>mail: The new email address for the user.</li>
	 * <li>firstname: The new firstname for the user.</li>
	 * <li>lastname: The new lastname for the user.</li>
	 * </ul>
	 *
	 * @throws NoSuchUserException Thrown when no user for the given name was found.
	 */
	protected static function updateUser() {
		$inputs = self::parseInput ();
		$taoUserService = \tao_models_classes_UserService::singleton ();
		
		if (! array_key_exists ( 'name', $inputs )) {
			throw new \common_exception_NotAcceptable ( 'Parameter missing. Name is required to update a user. Additional parameters are optional!' );
		}
		
		if ($taoUserService->loginAvailable ( $inputs ['name'] )) {
			throw new NoSuchUserException ( 'No such user exists: ' . $inputs ['name'] );
		}
		
		$userResource = UserService::getUserByLogin ( $inputs ['name'] );
		
		if (array_key_exists ( 'password', $_GET )) {
			/**
			 * The password is set by the user service to encrypt it!
			 */
			$taoUserService = \tao_models_classes_UserService::singleton ();
			$taoUserService->setPassword ( $userResource, $_GET ['password'] );
		}
		if (array_key_exists ( 'language', $inputs )) {
			// TODO Add checks on language validity
			$language = empty ( $inputs ['language'] ) ? DEFAULT_LANG : $inputs ['language'];
			$uiLanguageProperty = new \core_kernel_classes_Property ( PROPERTY_USER_UILG );
			$dataLanguageProperty = new \core_kernel_classes_Property ( PROPERTY_USER_DEFLG );
			PropertyHelper::updatePropertyValue ( $userResource, $uiLanguageProperty, 'http://www.tao.lu/Ontologies/TAO.rdf#Lang' . $language );
			PropertyHelper::updatePropertyValue ( $userResource, $dataLanguageProperty, 'http://www.tao.lu/Ontologies/TAO.rdf#Lang' . $language );
		}
		if (array_key_exists ( 'mail', $inputs )) {
			PropertyHelper::updatePropertyValue ( $userResource, new \core_kernel_classes_Property ( PROPERTY_USER_MAIL ), $inputs ['mail'] );
		}
		if (array_key_exists ( 'firstName', $inputs )) {
			PropertyHelper::updatePropertyValue ( $userResource, new \core_kernel_classes_Property ( PROPERTY_USER_FIRSTNAME ), $inputs ['firstName'] );
		}
		if (array_key_exists ( 'lastName', $inputs )) {
			PropertyHelper::updatePropertyValue ( $userResource, new \core_kernel_classes_Property ( PROPERTY_USER_LASTNAME ), $inputs ['lastName'] );
		}
		
		http_response_code ( 204 );
		echo "User Updated";
	}
}

// entry point of the REST layer for user management (global scope)
try {
	UserRestService::handleRequest ();
} catch ( AlreadyExistsException $e ) {
	http_response_code ( 409 );
	echo 'Conflict' . ' - message: ' . $e->getMessage ();
} catch ( \common_exception_NotAcceptable $e ) {
	$msg = 'Not Acceptable' . ' - message: ' . $e->getMessage ();
	\common_Logger::w ( $msg );
	http_response_code ( 400 );
	echo $msg;
} catch ( NoSuchUserException $e ) {
	\common_Logger::t ( $e->getMessage () );
	http_response_code ( 409 );
} catch ( \common_exception_NotImplemented $e ) {
	$msg = 'Not Implemented' . ' - message: ' . $e->getMessage ();
	\common_Logger::w ( $msg );
	http_response_code ( 501 );
	echo $msg;
} catch ( \common_exception_UserReadableException $e ) {
	http_response_code ( 500 );
	\common_Logger::e ( $e->getFile () . ' ' . $e->getLine () . ' ' . $e->getMessage () . ' ' . $e->getTraceAsString () );
	echo $e->getUserMessage ();
} catch ( \Exception $e ) {
	http_response_code ( 500 );
	\common_Logger::e ( $e->getFile () . ' ' . $e->getLine () . ' ' . $e->getMessage () . ' ' . $e->getTraceAsString () );
	echo 'Internal Server Error' . ' - message: ' . $e->getMessage ();
}
