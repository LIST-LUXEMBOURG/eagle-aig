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

use itis\taoAigFacade\models\UserResultsService;

/**
 * Class managing the REST calls to retrieve results for a user.
 *
 * @author Olivier Pedretti [oliver.pedretti@list.lu]
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 1.0
 * @version 1.0.3
 */
class ResultRestService extends RestService {
	
	/**
	 * Method called upon receiving a REST request to handle a user service.
	 *
	 * @throws \common_exception_NotImplemented If the HTTP method is not yet supported
	 */
	public static function handleRequest() {
		$method = $_SERVER ['REQUEST_METHOD'];
		switch ($method) {
			case 'GET' :
				self::getResults ();
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
	protected static function getResults() {
		$inputs = self::parseInput ();
		$taoUserService = \tao_models_classes_UserService::singleton ();
		$resultService = UserResultsService::singleton ();
		
		if (! array_key_exists ( 'login', $inputs )) {
			throw new \common_exception_NotAcceptable ( 'Parameter missing. "login" is required to retrieve results for a user. Login needs to be followed by a valid login, e.g.: "login/validLogin"!' );
		}
		
		if ($taoUserService->loginAvailable ( $inputs ['login'] )) {
			throw new NoSuchUserException ( 'No such user exists!' );
		}
		
		$userResource = UserService::getUserByLogin ( $inputs ['login'] );
		
		$rawData = $resultService->getRawDataForUser ( $userResource );
		
		self::recursive_unset ( $rawData, 'user' );
		
		$deliveries = array ();
		foreach ( $rawData as $delivery ) {
			
			// Only ad best delivery to result.
			// if(isset($deliveries[$delivery ['deliveryUri']])) {
			// if($deliveries [$delivery ['deliveryUri']]['score'] > $delivery ['variables'] ['LtiOutcome']);
			// continue;
			// }
			
			$deliveries [$delivery ['deliveryResultUri']] = array (
					'testUri' => $delivery ['testUri'],
					'lastTestTaken' => $delivery ['lastTestTaken'],
					'deliveryUri' => $delivery ['deliveryUri'],
					'deliveryResultUri' => $delivery ['deliveryResultUri'],
					'deliveryResultLabel' => $delivery ['deliveryResultLabel'],
					'score' => $delivery ['variables'] ['LtiOutcome'],
					'numberOfItems' => \count ( $delivery ['items'] ) 
			);
		}
		
		$jsonData = array (
				'login' => $inputs ['login'],
				'lastTestTaken' => self::determineMostRecentTestDate ( $rawData )->format ( 'd-m-Y H:i:s T' ),
				'bestScoresByTest' => self::determineBestScoresByTest ( $rawData ),
				'deliveries' => $deliveries 
		);
		
		header ( 'Content-Type: application/json' );
		
		$json = json_encode ( $jsonData, JSON_FORCE_OBJECT | JSON_PRETTY_PRINT | JSON_UNESCAPED_SLASHES );
		echo $json;
	}
	private static function determineMostRecentTestDate(&$dataArray) {
		$date = new \DateTime ( '01-01-1000' );
		foreach ( $dataArray as $delivery ) {
			$deliveryDate = new \DateTime ( $delivery ['lastTestTaken'] );
			$date = $date > $deliveryDate ? $date : $deliveryDate;
		}
		return $date;
	}
	private static function determineBestScoresByTest(&$dataArray) {
		$score = array ();
		foreach ( $dataArray as $delivery ) {
			$bestScore = isset ( $score [$delivery ['testUri']] ) ? $score [$delivery ['testUri']] : 0;
			$currentScore = $delivery ['variables'] ['LtiOutcome'];
			
			if ($bestScore < $currentScore) {
				$score [$delivery ['testUri']] = $currentScore;
			}
		}
		return $score;
	}
	
	/**
	 * Taken from: http://stackoverflow.com/a/1708914/170781
	 *
	 * @param $array The
	 *        	array, passed as a reference, to unset the key from.
	 * @param $unwanted_key The
	 *        	key that is to be recursively removed.
	 */
	private static function recursive_unset(&$array, $unwanted_key) {
		unset ( $array [$unwanted_key] );
		foreach ( $array as &$value ) {
			if (is_array ( $value )) {
				self::recursive_unset ( $value, $unwanted_key );
			}
		}
	}
}

// entry point of the REST layer for user management (global scope)
try {
	ResultRestService::handleRequest ();
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
