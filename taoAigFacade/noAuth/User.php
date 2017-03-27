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
namespace itis\taoAigFacade\noAuth;

use itis\taoAigFacade\models\exceptions\AlreadyExistsException;

require_once dirname ( __FILE__ ) . '/../includes/raw_start.php';
/**
 * noAuth class is an helper to avoid the TAO user authentication required by TAO REST API.
 * Outside the class is defined the access point (global scope).
 *
 * Class to manage users
 *
 * @author Olivier Pedretti
 *        
 */
class User {
	const PARAM_URL_REQUEST = 'urlrequest';
	/**
	 * Delegate the HTTP client call related to user management.
	 *
	 * @throws \common_exception_NotImplemented If the HTTP method is not yet supported
	 * @todo Supports all REST methods
	 */
	public static function parseRequest() {
		$method = $_SERVER ['REQUEST_METHOD'];
		switch ($method) {
			case 'POST' :
				self::addUser ();
				break;
			case 'GET' :
			// fall through
			case 'PUT' :
			// fall through
			case 'DELETE' :
			// fall through
			default :
				throw new \common_exception_NotImplemented ( 'HTTP method not implemented: ' . $method );
		}
	}
	/**
	 * Add a user
	 *
	 * @throws \common_exception_NotAcceptable if the request parameter is missing
	 * @throws AlreadyExistsException
	 */
	protected static function addUser() {
		// use of $_GET due to Apache RewriteRule
		if (! isset ( $_GET [self::PARAM_URL_REQUEST] )) {
			throw new \common_exception_NotAcceptable ( 'missing parameter: ' . self::PARAM_URL_REQUEST );
		}
		
		// get wanted credentials
		if (preg_match ( '/^\/(.+)\/pwd\/(.+)$/', $_GET [self::PARAM_URL_REQUEST], $matches ) != 1) {
			throw new \common_exception_NotAcceptable ( 'login or pwd is missing' );
		}
		$login = $matches [1];
		$pwd = $matches [2];
		
		// check availability
		$userService = \tao_models_classes_UserService::singleton ();
		if (! $userService->loginAvailable ( $login )) {
			throw new AlreadyExistsException ( 'login already exists' );
		}
		
		// add user and its properties
		$taoUserRole = new \core_kernel_classes_Resource ( INSTANCE_ROLE_DELIVERY );
		$taoUserClass = new \core_kernel_classes_Class ( TAO_SUBJECT_CLASS );
		$userRsc = $userService->addUser ( $login, $pwd, $taoUserRole, $taoUserClass );
		$userRsc->setLabel ( $login );
		
		$UiLgProp = new \core_kernel_classes_Property ( PROPERTY_USER_UILG );
		$userRsc->setPropertyValue ( $UiLgProp, 'http://www.tao.lu/Ontologies/TAO.rdf#Lang' . DEFAULT_LANG );
		
		$DataLgProp = new \core_kernel_classes_Property ( PROPERTY_USER_DEFLG );
		$userRsc->setPropertyValue ( $DataLgProp, 'http://www.tao.lu/Ontologies/TAO.rdf#Lang' . DEFAULT_LANG );
		
		// display result
		http_response_code ( 201 );
		echo "user Created";
	}
}

// entry point of the REST layer for user management (global scope)
try {
	User::parseRequest ();
} catch ( AlreadyExistsException $e ) {
	http_response_code ( 409 );
	echo 'Conflict' . ' - message: ' . $e->getMessage ();
} catch ( \common_exception_NotAcceptable $e ) {
	$msg = 'Not Acceptable' . ' - message: ' . $e->getMessage ();
	\common_Logger::w ( $msg );
	http_response_code ( 400 );
	echo $msg;
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

