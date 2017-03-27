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
 *
 * @author Olivier Pedretti
 *        
 */
namespace itis\taoAigFacade\models;

use itis\itemFeatureAnalysis\models\ItemFeatureAnalysisService;
use itis\taoAigFacade\models\exceptions\ConnectFailedException;
use itis\taoAigFacade\models\exceptions\ParseException;
use itis\taoAigFacade\models\request\ItemRequest;
use itis\taoAigFacade\models\siren\ItemsParser;
use itis\taoAigFacade\models\siren\Siren;

class ItemGenerator {
	protected $itemBuilder = null;
	protected $itemClass = null;
	public function __construct() {
		$this->itemBuilder = ItemBuilderService::singleton ();
		$this->itemClass = new \core_kernel_classes_Class ( TAO_AIG_FACADE_MODEL_ITEMCLASS );
	}
	public function generate(ItemRequest $request) {
		$report = \common_report_Report::createSuccess ( __ ( 'Item Generation Successful' ) );
		
		$previousSession = null;
		$lang = $request->getLanguage ();
		if (! empty ( $lang )) {
			$previousSession = \common_session_SessionManager::getSession ();
			// create dummy session to handle language (replace the current one!)
			\common_session_SessionManager::startSession ( new Session ( $lang ) );
		}
		
		$conceptList = $this->prepareGeneration ( $request );
		$nbOfConcepts = count ( $conceptList );
		$nbConceptsPerItem = $request->getConceptsPerItem ();
		$maxNbOfConcepts = $nbOfConcepts - $nbOfConcepts % $nbConceptsPerItem;
		$discardedConceptsString = '';
		for($conceptIndex = 0; $conceptIndex < $maxNbOfConcepts; $conceptIndex += $nbConceptsPerItem) {
			$itemRscs = [ ];
			$concepts = [ ];
			for($i = 0; $i < $nbConceptsPerItem; $i ++) {
				$concepts [] = $conceptList [$conceptIndex + $i];
			}
			try {
				$itemRscs = $this->generateItemsForConcepts ( $concepts );
			} catch ( \common_exception_Error $e ) {
				$conceptUris = '';
				foreach ( $concepts as $concept ) {
					$conceptUris .= $concept->getWord ()->getConceptURI () . ' ; ';
				}
				\common_Logger::w ( __METHOD__ . ' discarded concepts: ' . $conceptUris );
				$report->add ( new \common_report_Report ( \common_report_Report::TYPE_WARNING, 'discarded concept', $conceptUris ) );
				continue;
			}
			foreach ( $itemRscs as $rsc ) {
				$conceptUris = [ ];
				foreach ( $concepts as $concept ) {
					$conceptUris [] = $concept->getWord ()->getConceptURI ();
				}
				$report->add ( new \common_report_Report ( \common_report_Report::TYPE_INFO, __ ( 'Item generated' ), [ 
						'concepts' => $conceptUris,
						'uri' => $rsc->getUri () 
				] ) );
			}
		}
		
		// build list of items
		$itemRscs = $this->getListOfItemUris ( $conceptList );
		
		// analyse features?
		if ($request->hasToAnalyseFeatures ()) {
			$extensionManager = \common_ext_ExtensionsManager::singleton ();
			if ($extensionManager->isEnabled ( TAO_AIG_FACADE_ITEMFEATUREANALYSIS_EXTENSTION_NAME )) {
				$extension = $extensionManager->getExtensionById ( TAO_AIG_FACADE_ITEMFEATUREANALYSIS_EXTENSTION_NAME );
				$extension->load ();
				$itemFeatureAnalysisService = ItemFeatureAnalysisService::singleton ();
				$itemFeatureAnalyseReport = $itemFeatureAnalysisService->analyse ( $itemRscs, true, $request->getLanguage () );
				$report->add ( $itemFeatureAnalyseReport );
			}
		}

		$report->setData ( $itemRscs );
		
		// restore previous session
		if ($previousSession) {
			\common_session_SessionManager::startSession ( $previousSession );
		}
		
		return $report;
	}
	
	/**
	 * Get items as resources
	 *
	 * @param Concept[] $itemList        	
	 * @return \core_kernel_classes_Resource[] Items
	 */
	protected function getListOfItemUris($conceptList) {
		$return = [ ];
		foreach ( $conceptList as $concept ) {
			$rscs = $concept->getRscs ();
			foreach ( $rscs as $rsc ) {
				$return [] = $rsc->getUri ();
			}
		}
		return $return;
	}
	protected function importQtiItems($itemXmls = array(), $parentClass, $concepts) {
		$rscs = [ ];
		foreach ( $itemXmls as $itemXml ) {
			$itemRsc = $this->itemBuilder->importQtiItem ( ItemsParser::simpleXmlToXmlString ( $itemXml ), $parentClass );
			$rscs [] = $itemRsc;
			// store metadata
			$this->setItemProperties ( $itemRsc, self::buildItemProperties ( $concepts, $itemXml ) );
		}
		return $rscs;
	}
	protected static function buildItemProperties($concepts, \SimpleXMLElement $itemXml) {
		$oneConcept = reset ( $concepts );
		$relatedConstructList = [ ];
		foreach ( $concepts as $concept ) {
			$relatedConstructList [] = $concept->getWord ()->getConceptURI ();
		}
		$relatedConstructListSorted = usort ( $relatedConstructList, function ($a, $b) {
			return strcoll ( mb_strtolower ( $a ), mb_strtolower ( $b ) );
		} );
		// reduce array depth
		if (count ( $relatedConstructList ) == 1) {
			$relatedConstructList = reset ( $relatedConstructList );
		}
		$relatedConstruct = json_encode ( $relatedConstructList, JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES );
		return [ 
				TAO_AIG_FACADE_MODEL_PROPERTY_CREATIONDATE => gmdate ( "Y-m-d H:i:s" ),
				TAO_AIG_FACADE_MODEL_PROPERTY_SOURCETEMPLATE => $oneConcept->getTemplate (),
				TAO_AIG_FACADE_MODEL_PROPERTY_RELATEDCONSTRUCT => $relatedConstruct,
				TAO_AIG_FACADE_MODEL_PROPERTY_ITEMTITLE => self::getItemTitle ( $itemXml ),
				TAO_AIG_FACADE_MODEL_PROPERTY_CATEGORY => $concept->getCategory (),
				TAO_AIG_FACADE_MODEL_PROPERTY_CORRECTRESPONSE => self::getCorrestResponse ( $itemXml ) 
		];
	}
	protected function setItemProperties(\core_kernel_classes_Resource $item, $itemProperties) {
		if (! $item->isInstanceOf ( $this->itemClass )) {
			throw new \common_exception_PreConditionFailure ( 'item is not an instance of: ' . $this->itemClass->getLabel () . ' itemLabel: ' . $item->getLabel () . ' itemUri: ' . $item->getUri () );
		}
		foreach ( $itemProperties as $prop => $propValue ) {
			$this->setItemProperty ( $item, $prop, $propValue );
		}
	}
	protected function setItemProperty(\core_kernel_classes_Resource $item, $propertyName, $value) {
		$success = $item->setPropertyValue ( new \core_kernel_classes_Property ( $propertyName ), $value );
		if (! $success) {
			throw new \common_exception_Error ( 'failed to set property: ' . $propertyName );
		}
	}
	protected function generateItemsForConcepts(&$concepts) {
		$oneConcept = reset ( $concepts );
		$xmlResponseStr = $this->sendRequestForItems ( $concepts );
		$itemXmls = ItemsParser::getNFirstItemsFromString ( $xmlResponseStr, $oneConcept->getNbOfItemsWanted () );
		self::defineTitles ( $itemXmls );
		$parentClass = $this->itemBuilder->defineDestinationClass ( $oneConcept->getParentClass (), $this->itemClass );
		$rscs = $this->importQtiItems ( $itemXmls, $parentClass->getUri (), $concepts );
		$oneConcept->setRscs ( $rscs );
		return $rscs;
	}
	protected function prepareGeneration(ItemRequest $request) {
		$list = array ();
		foreach ( $request->getWords () as $word ) {
			array_push ( $list, new Concept ( $word, $request->getItemTemplate (), $request->getItemCategory (), $request->getParentClass (), $request->getItemsPerConcept () ) );
		}
		return $list;
	}
	protected function sendRequestForItems($concepts) {
		$responseData = $this->sendRequest ( Siren::getRequestUrl ( $concepts ) );
		return $responseData;
	}
	/**
	 * Set title in Items according to the position of an item in the item array
	 *
	 * @param \SimpleXMLElement[] $xmls        	
	 */
	protected static function defineTitles($xmls) {
		$count = 1;
		foreach ( $xmls as $xml ) {
			self::setItemTitle ( $xml, self::generateItemTitle ( self::getItemTitle ( $xml ), $count ++ ) );
		}
	}
	protected static function addInteractionChoice(\DOMElement $el, &$target, $typeOfChoice) {
		$choices = $el->getElementsByTagName ( $typeOfChoice );
		foreach ( $choices as $choice ) {
			$target [$choice->getAttribute ( 'identifier' )] = $choice->nodeValue;
		}
	}
	protected static function getCorrestResponse(\SimpleXMLElement $xml) {
		$correctResponsesJson = '';
		if ($xml->getName () !== 'assessmentItem') {
			throw new ParseException ( 'unsupported xml format' );
		}
		$doc = dom_import_simplexml ( $xml );
		$typesOfChoice = [ 
				'simpleAssociableChoice',
				'associableChoice',
				'inlineChoice',
				'simpleChoice' 
		];
		// build response map
		$responseMap = [ ];
		foreach ( $typesOfChoice as $typeOfChoice ) {
			self::addInteractionChoice ( $doc, $responseMap, $typeOfChoice );
		}
		
		$correctResponseEls = $doc->getElementsByTagName ( 'correctResponse' );
		$firstCorrectResponseEl = $correctResponseEls->item ( 0 );
		$correctResponsesIds = [ ];
		if (! empty ( $firstCorrectResponseEl )) {
			$valueEls = $firstCorrectResponseEl->getElementsByTagName ( 'value' );
			foreach ( $valueEls as $valueEl ) {
				$correctResponsesIds [] = explode ( ' ', $valueEl->nodeValue );
			}
		}
		$correctResponsesObj = self::buildCorrectResponseArray ( $correctResponsesIds, $responseMap );
		
		// reduce array depth
		if (count ( $correctResponsesObj ) == 1) {
			$correctResponsesObj = reset ( $correctResponsesObj );
			if (count ( $correctResponsesObj ) == 1) {
				$correctResponsesObj = reset ( $correctResponsesObj );
			}
		}
		
		$correctResponsesJson = json_encode ( $correctResponsesObj, JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES );
		return $correctResponsesJson;
	}
	protected static function buildCorrectResponseArray($correctResponsesIds, $responseMap) {
		$responses = [ ];
		foreach ( $correctResponsesIds as $correctResponseIds ) {
			$response = [ ];
			foreach ( $correctResponseIds as $correctResponseId ) {
				$response [] = (isset ( $responseMap [$correctResponseId] )) ? $responseMap [$correctResponseId] : $correctResponseId;
			}
			$responses [] = $response;
		}
		return $responses;
	}
	/**
	 *
	 * @param string $base        	
	 * @param int $nb        	
	 * @return string
	 */
	protected static function generateItemTitle($base, $nb) {
		$title = $base . '_' . $nb;
		return $title;
	}
	/**
	 *
	 * @param \SimpleXMLElement $xml        	
	 * @param string $title        	
	 */
	protected static function setItemTitle($xml, $title) {
		$xml ['title'] = $title;
	}
	/**
	 *
	 * @param \SimpleXMLElement $xml        	
	 * @throws ParseException
	 * @return string
	 */
	protected static function getItemTitle(\SimpleXMLElement $xml) {
		$title = '';
		if ($xml->getName () !== 'assessmentItem') {
			throw new ParseException ( 'unsupported xml format' );
		}
		$titleXml = $xml ['title'];
		if (! empty ( $titleXml )) {
			$title = $titleXml->__toString ();
		}
		return $title;
	}
	/**
	 * Make an HTTP request
	 *
	 * @param string $requestUrl        	
	 * @throws \common_exception_Error
	 * @throws ConnectFailedException
	 * @return mixed
	 */
	protected function sendRequest($requestUrl) {
		\common_Logger::t ( __METHOD__ . ' ' . $requestUrl );
		$session = curl_init ( $requestUrl );
		if ($session === false) {
			throw new \common_exception_Error ( 'session not set' );
		}
		
		$returnValue = curl_setopt_array ( $session, array (
				
				// post method
				CURLOPT_HTTPGET => 1,
				
				CURLOPT_HTTPHEADER => array (
						"Accept: text/xml" 
				),
				
				// return the transfer as a string of the return value of curl_exec() instead of outputting it out directly.
				CURLOPT_RETURNTRANSFER => 1 
		) );
		if ($returnValue === false) {
			throw new \common_exception_Error ( 'cannot set curl options' );
		}
		
		// send request to server
		$returnedData = curl_exec ( $session );
		if (curl_errno ( $session )) {
			throw new ConnectFailedException ( 'session error: ' . curl_errno ( $session ) . ':' . curl_error ( $session ) );
		}
		
		// check the http code returned
		$httpCode = curl_getinfo ( $session, CURLINFO_HTTP_CODE );
		curl_close ( $session );
		if ($httpCode !== 200) {
			throw new \common_exception_Error ( 'unexpected http code from server:' . $httpCode . ' request:' . $requestUrl . ' data: ' . $returnedData );
		}
		return $returnedData;
	}
}
