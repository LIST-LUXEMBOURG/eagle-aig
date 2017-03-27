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

/**
 * Class managing the REST calls to create, query, and delete groups.

 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 1.0
 * @version 1.0.43
 */
class GroupRestService extends RestService{
	
	/**
	 * Method called upon receiving a REST request to handle a user service.
	 *
	 * @throws \common_exception_NotImplemented If the HTTP method is not yet supported
	 */
	public static function handleRequest() {
		$method = $_SERVER ['REQUEST_METHOD'];
		
		switch ($method) {
			case 'GET' :
				self::create ();
				break;		
			case 'DELETE' :				
				// $FALL-THROUGH$
			case 'POST' :				
				// $FALL-THROUGH$
			case 'PUT' :
				// $FALL-THROUGH$
			default :
				throw new \common_exception_NotImplemented ( 'HTTP method not implemented: ' . $method );
		}
	}
	
	private static function create() {
		$inputs = self::parseInput ();
		
		if (! array_key_exists ( 'name', $inputs )) {
			throw new \common_exception_NotAcceptable ( 'Parameter missing. Name, given as "name" is a required parameter and may not be omitted!' );
		}
		$login = array_key_exists('user', $inputs) ? $inputs['user'] : null;
		GroupService::createGroup($inputs['user'], $login);
	}
	
	private static function delete() {
		throw new \common_exception_NotImplemented ( 'Method not implemented: DeleteGroup()' );
	}
	
	private static function get() {
		throw new \common_exception_NotImplemented ( 'Method not implemented: GetGroup()' );
	}
}

// entry point of the REST layer for group management (global scope)
try {
	GroupRestService::handleRequest ();
} catch ( AlreadyExistsException $e ) {
	http_response_code ( 409 );
	echo 'Conflict' . ' - message: ' . $e->getMessage ();
} catch ( \common_exception_NotAcceptable $e ) {
	$msg = 'Not Acceptable' . ' - message: ' . $e->getMessage ();
	\common_Logger::w ( $msg );
	http_response_code ( 400 );
	echo $msg;
} catch ( NoSuchUserException $e ) {
	\common_Logger::w ( $e->getMessage () );
	http_response_code ( 409 );
	echo $e->getMessage ();
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