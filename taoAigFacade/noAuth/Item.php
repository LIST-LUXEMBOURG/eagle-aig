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
/**
 * noAuth class is an helper to avoid the TAO user authentication required by TAO REST API.
 * Outside the class is defined the access point (global scope).
 *
 * Manage items
 *
 * @author Olivier Pedretti
 */
namespace itis\taoAigFacade\noAuth;

use itis\taoAigFacade\models\FacadeService;
use itis\taoAigFacade\models\request\ItemRequest;

require_once dirname ( __FILE__ ) . '/../includes/raw_start.php';
class Item {
	const PARAM_REQUEST = 'request';
	const PARAM_URL_REQUEST = 'urlrequest';
	/**
	 * Parse HTTP method and address the corresponding endpoint
	 *
	 * @throws \common_exception_NotImplemented
	 * @return void Write output in HTTP output stream
	 */
	public static function parseRequest() {
		$requestMethod = $_SERVER ['REQUEST_METHOD'];
		switch ($requestMethod) {
			case 'POST' :
				self::createItems ();
				break;
			case 'GET' :
			// fall through
			case 'PUT' :
			// fall through
			case 'DELETE' :
				self::deleteItems ();
				break;
			default :
				throw new \common_exception_NotImplemented ( 'HTTP method not implemented: ' . $requestMethod );
		}
	}
	/**
	 * Create items
	 *
	 * @throws \common_exception_NotAcceptable
	 * @throws \common_exception_Error
	 * @return void Write output in HTTP output stream
	 */
	protected static function createItems() {
		$requestParam = $_POST [self::PARAM_REQUEST];
		if (! isset ( $requestParam )) {
			throw new \common_exception_NotAcceptable ( 'missing parameter: ' . self::PARAM_REQUEST );
		}
		$requestJson = $requestParam;
		
		// fix html encoding
		$cleanJson = htmlspecialchars_decode ( $requestJson );
		
		// TODO check if XML or JSON input
		
		// log request
		\common_Logger::d ( 'Request: ' . $cleanJson );
		$request = ItemRequest::fromJson ( $cleanJson );
		
		// generate items
		$facadeService = FacadeService::singleton ();
		$report = $facadeService->generateItems ( $request );
		
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
		
		$response = json_encode ( $report );
		// log response
		\common_Logger::d ( 'Response: ' . $response );
		echo $response;
	}
	/**
	 * Delete the content of a class of Items or a single item
	 *
	 * @throws \common_exception_NotAcceptable
	 * @return void Write output in HTTP output stream
	 */
	protected static function deleteItems() {
		// use of $_GET due to Apache RewriteRule
		if (! isset ( $_GET [self::PARAM_URL_REQUEST] )) {
			throw new \common_exception_NotAcceptable ( 'missing parameter: ' . self::PARAM_URL_REQUEST );
		}
		
		if (preg_match ( '/^\/(.+)$/', $_GET [self::PARAM_URL_REQUEST], $matches ) != 1) {
			throw new \common_exception_NotAcceptable ( 'item identifier is missing' );
		}
		$uri = base64_decode ( $matches [1] );
		
		// isInScope
		if (! (\common_Utils::isUri ( $uri ))) {
			throw new \common_exception_NotAcceptable ( 'not an identifier: ' . $uri );
		}
		$baseClass = new \core_kernel_classes_Class ( TAO_ITEM_CLASS );
		$rsc = new \core_kernel_classes_Resource ( $uri );
		if ($rsc->isClass ()) {
			$class = new \core_kernel_classes_Class ( $uri );
			if (! $class->isSubClassOf ( $baseClass )) {
				throw new \common_exception_NotAcceptable ( 'not an item class: ' . $uri );
			}
		} else {
			if (! $rsc->isInstanceOf ( $baseClass )) {
				throw new \common_exception_NotAcceptable ( 'not an item: ' . $uri );
			}
		}
		
		self::deleteItem ( $rsc );
		
		http_response_code ( 200 );
	}
	/**
	 *
	 * @param \core_kernel_classes_Resource $rsc        	
	 */
	protected static function deleteItem(\core_kernel_classes_Resource $rsc) {
		$itemService = \taoItems_models_classes_ItemsService::singleton ();
		if ($isClass = $rsc->isClass ()) {
			$class = new \core_kernel_classes_Class ( $rsc->getUri () );
			
			$rscs = $class->getInstances ( false );
			foreach ( $rscs as $r ) {
				self::deleteItem ( $r );
			}
			
			$classes = $class->getSubClasses ( false );
			foreach ( $classes as $c ) {
				self::deleteItem ( $c );
			}
			\common_Logger::t ( "deleteItem-class: " . $rsc->getLabel () . ' with uri: ' . $rsc->getUri () );
			$itemService->deleteItemClass ( $class );
		} else {
			\common_Logger::t ( "deleteItem: " . $rsc->getLabel () . ' with uri: ' . $rsc->getUri () );
			$itemService->deleteItem ( $rsc );
		}
	}
}

// entry point of the REST layer for user management (global scope)
try {
	Item::parseRequest ();
} catch ( \common_exception_NotAcceptable $e ) {
	$msg = 'Bad Request' . ' - message: ' . $e->getMessage ();
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

					
