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

use itis\taoAigFacade\models\FacadeService;
use itis\taoAigFacade\models\request\TestRequest;
use itis\taoAigFacade\models\Session;

require_once dirname ( __FILE__ ) . '/../includes/raw_start.php';
/**
 * noAuth class is an helper to avoid the TAO user authentication required by TAO REST API.
 * Outside the class is defined the access point (global scope).
 *
 * Class to manage Tests
 *
 * @author Olivier Pedretti
 *        
 */
class Test {
	const PARAM_REQUEST = 'request';
	const PARAM_URL_REQUEST = 'urlrequest';
	
	/**
	 * Delegate the HTTP client call related to test management.
	 *
	 * @throws \common_exception_NotImplemented If the HTTP method is not yet supported
	 * @todo Supports all REST methods
	 */
	public static function parseRequest() {
		switch ($_SERVER ['REQUEST_METHOD']) {
			case 'POST' :
				self::onPost ();
				break;
			case 'GET' :
			// fall through
			case 'PUT' :
			// fall through
			case 'DELETE' :
			// fall through
			default :
				throw new \common_exception_NotImplemented ( 'HTTP method not implemented: ' . $_SERVER ['REQUEST_METHOD'] );
		}
	}
	protected static function onPost() {
		// use of $_GET due to Apache RewriteRule
		if (isset ( $_GET [self::PARAM_URL_REQUEST] )) {
			if (preg_match ( '/^\/itemClass\/(.+)$/', $_GET [self::PARAM_URL_REQUEST], $matches ) == 1) {
				self::createTestWithItemClass ( $matches [1] );
				return;
			}
		}
		if (isset ( $_POST [self::PARAM_REQUEST] )) {
			self::createTestWithRequest ( $_POST [self::PARAM_REQUEST] );
			return;
		}
		throw new \common_exception_PreConditionFailure ( 'request details is missing' );
	}
	protected static function createTestWithRequest($requestParam) {
		$requestJson = $requestParam;
		
		// fix html encoding
		$cleanJson = htmlspecialchars_decode ( $requestJson );
		\common_Logger::d ( 'Request: ' . $cleanJson );
		$request = TestRequest::fromJson ( $cleanJson );
		
		$facadeService = FacadeService::singleton ();
		$report = $facadeService->createTestWithTestRequest ( $request );
		
		// prepare report
		if (strcmp ( $report->getType (), \common_report_Report::TYPE_ERROR ) === 0) {
			http_response_code ( 500 );
		} elseif ($report->containsError ()) {
			$report->setType ( \common_report_Report::TYPE_ERROR );
			$report->setTitle ( __ ( 'Generation completed with errors' ) );
			http_response_code ( 201 );
		} elseif ($report->contains ( \common_report_Report::TYPE_WARNING )) {
			$report->setType ( \common_report_Report::TYPE_WARNING );
			$report->setTitle ( __ ( 'Generation completed with warnings' ) );
			http_response_code ( 201 );
		} else {
			http_response_code ( 201 );
		}
		
		$response = json_encode ( $report, JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES );
		// log response
		\common_Logger::d ( 'Response: ' . $response );
		echo $response;
	}
	protected static function createTestWithItemClass($partOfRequest) {
		$uri = base64_decode ( $partOfRequest );
		
		// isInScope
		if (! (\common_Utils::isUri ( $uri ))) {
			throw new \common_exception_NotAcceptable ( 'not an identifier: ' . $uri );
		}
		$rsc = new \core_kernel_classes_Resource ( $uri );
		if (! $rsc->isClass ()) {
			throw new \common_exception_NotAcceptable ( 'not a class: ' . $uri );
		}
		$class = new \core_kernel_classes_Class ( $uri );
		
		// call service
		$facadeService = FacadeService::singleton ();
		$rsc = $facadeService->createTestWithItemClass ( $class );
		
		// result
		http_response_code ( 201 );
		echo json_encode ( array (
				'test' => array (
						'uri' => $rsc->getUri () 
				) 
		), JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES );
	}
	/**
	 * FIXME TODO migrate this function in dedicated frontend API
	 * Create a test (items+test+delivery+group)
	 * Return at HTTP level informations on generated delivery
	 *
	 * @throws \common_exception_NotAcceptable
	 *
	 */
	protected static function createDeliveryWithRequest() {
		$requestJson = $_POST [self::PARAM_REQUEST];
		
		// fix html encoding
		$cleanJson = htmlspecialchars_decode ( $requestJson );
		$request = DeliveryRequest::fromJson ( $cleanJson );
		$facadeService = FacadeService::singleton ();
		$delivRsc = $facadeService->createDelivery ( $request, $createdTestRsc, $createdItemRscs, $createdGroupRsc );
		
		http_response_code ( 201 );
		echo json_encode ( array (
				'test' => array (
						'uri' => $delivRsc->getUri () 
				) 
		), JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES );
	}
}

// entry point of the REST layer for user management (global scope)
try {
	Test::parseRequest ();
} catch ( \common_exception_NotAcceptable $e ) {
	$msg = 'Bad Request' . ' - message: ' . $e->getMessage ();
	\common_Logger::w ( $msg );
	http_response_code ( 400 );
	echo $msg;
} catch ( \common_exception_NoContent $e ) {
	$msg = 'No content' . ' - message: ' . $e->getMessage ();
	\common_Logger::w ( $msg );
	http_response_code ( 204 );
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

					