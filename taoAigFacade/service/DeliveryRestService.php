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
use itis\taoAigFacade\models\UserResultsService;

/**
 * Class managing the REST calls to retrieve deliveries.
 *
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 1.0
 * @version 1.0.41
 */
class DeliveryRestService extends RestService {
	private static $inputs;
	
	/**
	 * Method called upon receiving a REST request to handle a user service.
	 *
	 * @throws \common_exception_NotImplemented If the HTTP method is not yet supported
	 */
	public static function handleRequest() {
		$method = $_SERVER ['REQUEST_METHOD'];
		
		self::$inputs = self::parseInput ();
		
		switch ($method) {
			case 'GET' :
				if (array_key_exists ( 'sessionId', self::$inputs )) {
					self::getDelivery ( self::$inputs ['sessionId'] );
				} else {
					throw new \common_exception_NotAcceptable ( 'A session ID must be specified using the "sessionID parameter"' );
				}
				break;
			case 'DELETE' :
			case 'PUT' :
			case 'POST' :
			/**
			 * $FALL-THROUGH$
			 */
			default :
				throw new \common_exception_NotImplemented ( 'HTTP method not implemented: ' . $method );
		}
	}
	private function getDelivery($sessionID) {
		$resultService = UserResultsService::singleton ();
		$deliveryClass = new \core_kernel_classes_Class ( CLASS_COMPILEDDELIVERY );
		
		$options = [ 
				'recursive' => true,
				'like' => true 
		];
		
		$filters = array (
				TAO_AIG_FACADE_MODEL_PROPERTY_SESSION => $sessionID 
		);
		
		$deliveriesForSession = $deliveryClass->searchInstances ( $filters, $options );
		
		$deliveries = array ();
		foreach ( $deliveriesForSession as $delivery ) {
			$deliveries [] = $delivery->getUri ();
		}
		
		$json = json_encode ( $deliveries, JSON_PRETTY_PRINT | JSON_UNESCAPED_SLASHES );
		\common_Logger::t($json);
		
		header ( 'Content-Type: ' . 'application/json' . '; charset=' . 'UTF-8', true );
		
		echo $json;
	}
}

// entry point of the REST layer for user management (global scope)
try {
	DeliveryRestService::handleRequest ();
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
