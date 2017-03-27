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
namespace itis\taoAigFacade\controller;

use oat\taoTestTaker\models\TestTakerService;
use itis\taoAigFacade\models\UserResultsService;
use itis\taoAigFacade\helpers\TestTakerHelper;

/**
 *
 * @author Olivier Pedretti
 *        
 */
class UserResults extends \tao_actions_SaSModule {
	const REQUEST_TIME_LIMIT = 3600;
	public function __construct() {
		parent::__construct ();
		$this->service = TestTakerService::singleton ();
		$this->resultService = UserResultsService::singleton ();
		$this->defaultData ();
	}
	public function index() {
	}
	/**
	 * Display the results UI for a TestTaker/User
	 */
	public function editTestTaker() {
		$user = $this->getCurrentInstance ();
		$login = TestTakerHelper::getLogin ( $user );
		
		$this->setData ( 'filter', $this->getRequestParameter ( 'filter' ) );
		$this->setData ( 'classUri', $this->getRequestParameter ( 'classUri' ) );
		$this->setData ( 'uri', $user->getUri () );
		$this->setData ( 'userName', $login );
		
		$this->setView ( 'UserResults.tpl' );
	}
	/**
	 * Do nothing
	 */
	public function editTestTakerClass() {
	}
	/**
	 * Write an Excel file related to the test-taker results in output HTTP stream
	 *
	 * @return void Write in output HTTP stream
	 */
	public function getExcelFile() {
		set_time_limit ( self::REQUEST_TIME_LIMIT );
		
		$user = $this->getCurrentInstance ();
		
		$login = TestTakerHelper::getLogin ( $user );
		if ($login === null) {
			$login = 'userLogin';
		}
		
		\common_Logger::d ( __FUNCTION__ . ' uri: ' . $user->getUri () . ' login: ' . $login . ' label: ' . $user->getLabel () );
		
		$d_results = $this->resultService->getRawDataForUser ( $user );
		
// 		$excel = $this->resultService->resultsProcessing ( $d_results );
		
		$outputCharset = 'UTF-8';
		header ( 'Set-Cookie: fileDownload=true' );
		setcookie ( "fileDownload", "true", 0, "/" );
		header ( 'Content-type: text/plain; charset=' . $outputCharset );
		header ( 'Content-Encoding: ' . $outputCharset );
		header ( 'Content-Disposition: attachment; filename="results of ' . $login . '.xlsx"' );
// 		$this->resultService->saveExcel ( $excel, 'php://output' );
	}
}


