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

require_once dirname ( __FILE__ ) . '/../includes/raw_start.php';

/**
 * Base class for all services that implement REST calls on the Facade.
 *
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 1.0
 * @version 1.0.44
 */
abstract class RestService {
	/**
	 * Constant used to identify the redirected parameter bits of the initial REST-format request that was redirected.
	 */
	const PARAM_URL_REQUEST = 'request';
	public abstract static function handleRequest();
	
	/**
	 * Function used to break the URL bit after the REST base call into key-value pairs.
	 *
	 * @return an array of said key-value pairs.
	 */
	protected static function parseInput() {
		if (! isset ( $_GET [self::PARAM_URL_REQUEST] )) {
			throw new \common_exception_NotAcceptable ( 'Missing parameter!' );
		}
		$input = $_GET [self::PARAM_URL_REQUEST];
		preg_match_all ( '/([^\/]+)\/([^\/]*)/', $input, $matches );
		return array_combine ( $matches [1], $matches [2] );
	}
}
