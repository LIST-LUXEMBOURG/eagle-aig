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

use itis\taoAigFacade\models\UserResultsService;

/**
 *
 * @author Olivier Pedretti
 *        
 */
class DeliveryResults extends \tao_actions_SaSModule {
	const REQUEST_TIME_LIMIT = 3600;
	public function __construct() {
		parent::__construct ();
		$this->resultService = UserResultsService::singleton ();
		$this->defaultData ();
	}
	/**
	 * display the delivery results UI
	 *
	 * {@inheritDoc}
	 *
	 * @see tao_actions_RdfController::index()
	 */
	public function index() {
		$delivery = $this->getCurrentClass ();
		
		$this->setData ( 'filter', $this->getRequestParameter ( 'filter' ) );
		$this->setData ( 'classUri', $this->getRequestParameter ( 'classUri' ) );
		$this->setData ( 'uri', $delivery->getUri () );
		$this->setData ( 'deliveryName', $delivery->getLabel () );
		
		$this->setView ( 'DeliveryResults.tpl' );
	}
	/**
	 * Write an Excel file in output HTTP stream for the wanted delivery
	 *
	 * @return void Write an Excel file in output HTTP stream
	 */
	public function getExcelFile() {
		set_time_limit ( self::REQUEST_TIME_LIMIT );
		
		$delivery = $this->getCurrentInstance ();
		
		$label = $delivery->getLabel ();
		
		\common_Logger::d ( __FUNCTION__ . ' uri: ' . $delivery->getUri () . ' label: ' . $label );
		
		if (empty ( $label )) {
			$label = 'Delivery-' . date ( "Y-m-d H\hi\ms" );
		}
		
		$d_results = $this->resultService->getRawDataForDelivery ( $delivery );
		
		//$excel = $this->resultService->resultsProcessing ( $d_results );
		
		$outputCharset = 'UTF-8';
		header ( 'Set-Cookie: fileDownload=true' );
		setcookie ( "fileDownload", "true", 0, "/" );
		header ( 'Content-type: text/plain; charset=' . $outputCharset );
		header ( 'Content-Encoding: ' . $outputCharset );
		header ( 'Content-Disposition: attachment; filename="results of ' . $label . '.xlsx"' );
		//$this->resultService->saveExcel ( $excel, 'php://output' );
	}
}


