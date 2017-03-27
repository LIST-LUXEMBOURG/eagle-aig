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

use oat\taoOutcomeRds\model\RdsResultStorage;
use oat\taoOutcomeUi\model\ResultsService;

/**
 *
 * @author Olivier Pedretti
 *        
 */
class UserResultsService extends ResultsService {
	/**
	 *
	 * @var string If no value is set
	 */
	const EXCEL_CELL_DEFAULT = '-';
	public function __construct() {
		parent::__construct ();
		//include_once DIR_MODELS . 'PHPExcel' . DIRECTORY_SEPARATOR . 'PHPExcel.php';
	}
	protected static function setEncoding() {
		setlocale ( LC_COLLATE, 'en_US.utf8' );
		mb_internal_encoding ( 'UTF-8' );
	}
	/**
	 *
	 * @param \core_kernel_classes_Resource $delivery        	
	 * @throws \Exception
	 * @return mixed data
	 */
	public function getRawDataForDelivery(\core_kernel_classes_Resource $delivery) {
		\common_Logger::d ( __FUNCTION__ . ' Memory usage before getRawDataForDelivery: ' . (memory_get_usage ( true ) / 1024 / 1024) . 'MB peak: ' . (memory_get_peak_usage ( true ) / 1024 / 1024) . 'MB' );
		self::setEncoding ();
		
		$implementation = $this->getReadableImplementation ( $delivery );
		$this->setImplementation ( $implementation );
		
		$results = $this->getImplementation ()->getResultByDelivery ( array (
				$delivery->getUri () 
		), null );
		
		$data = [ ];
		
		$nbOfResults = count ( $results );
		$resultCounter = 0;
		$startTime = time ();
		foreach ( $results as $result ) {
			$resultCounter ++;
			if ($resultCounter <= 1) {
				\common_Logger::d ( 'result(' . $resultCounter . '/' . $nbOfResults . ') ' );
			} else {
				$exportDurationEstimation = (time () - $startTime) / ($resultCounter - 1) * $nbOfResults;
				$exportEndTimeEstimation = date ( "Y-m-d H:i:s", $startTime + $exportDurationEstimation );
				\common_Logger::d ( 'result(' . $resultCounter . '/' . $nbOfResults . ') Export duration: ' . (new \DateTime ( '@' . round ( $exportDurationEstimation ) ))->format ( 'H:i:s' ) . ' End time: ' . $exportEndTimeEstimation );
			}
			$deliveryExecution = new \taoDelivery_models_classes_execution_OntologyDeliveryExecution ( $result ['deliveryResultIdentifier'] );
			$data [] = $this->getRawDataForDeliveryExecution ( $deliveryExecution );
		}
		\common_Logger::d ( __FUNCTION__ . ' Memory usage after getRawDataForDelivery: ' . (memory_get_usage ( true ) / 1024 / 1024) . 'MB peak: ' . (memory_get_peak_usage ( true ) / 1024 / 1024) . 'MB' );
		return $data;
	}
	/**
	 *
	 * @param \core_kernel_classes_Resource $user        	
	 * @return mixed data
	 */
	public function getRawDataForUser(\core_kernel_classes_Resource $user) {
		// force the storage (no auto detection)
		$resultStorageImplementation = new RdsResultStorage ();
		$this->setImplementation ( $resultStorageImplementation );
		// return [['deliveryResultIdentifier' , 'testTakerIdentifier']]
		$testTakerResultPairs = $this->getImplementation ()->getAllTestTakerIds ();
		$userUri = $user->getUri ();
		$results = [];
		foreach ( $testTakerResultPairs as &$testTakerResultPair ) {
			if (strcmp ( $userUri, $testTakerResultPair ['testTakerIdentifier'] ) === 0) {
				$deliveryExecution = new \taoDelivery_models_classes_execution_OntologyDeliveryExecution ( $testTakerResultPair ['deliveryResultIdentifier'] );
				if (! $deliveryExecution->exists ()) {
					\common_Logger::w ( 'delivery result not found: ' . $deliveryExecution->getUri () );
					continue;
				}
				$results [] = $this->getRawDataForDeliveryExecution ( $deliveryExecution );
			}
		}
		return $results;
	}
	/**
	 *
	 * @param \taoDelivery_models_classes_execution_DeliveryExecution $deliveryExecution        	
	 */
	protected function getRawDataForDeliveryExecution(\taoDelivery_models_classes_execution_DeliveryExecution $deliveryExecution) {
		
		// getTestTaker
		$testTaker = $this->getTestTaker ( $deliveryExecution );
		$label = '';
		$login = '';
		$lastname = '';
		$firstname = '';
		$mail = '';
		if (get_class ( $testTaker ) != 'core_kernel_classes_Literal') {
			$propValues = $testTaker->getPropertiesValues ( array (
					RDFS_LABEL,
					PROPERTY_USER_LOGIN,
					PROPERTY_USER_FIRSTNAME,
					PROPERTY_USER_LASTNAME,
					PROPERTY_USER_MAIL 
			) );
			$label = (! empty ( $propValues [RDFS_LABEL] )) ? array_pop ( $propValues [RDFS_LABEL] )->literal : '';
			$login = (! empty ( $propValues [PROPERTY_USER_LOGIN] )) ? array_pop ( $propValues [PROPERTY_USER_LOGIN] )->literal : '';
			$lastname = (! empty ( $propValues [PROPERTY_USER_LASTNAME] )) ? array_pop ( $propValues [PROPERTY_USER_LASTNAME] )->literal : '';
			$firstname = (! empty ( $propValues [PROPERTY_USER_FIRSTNAME] )) ? array_pop ( $propValues [PROPERTY_USER_FIRSTNAME] )->literal : '';
			$mail = (! empty ( $propValues [PROPERTY_USER_MAIL] )) ? array_pop ( $propValues [PROPERTY_USER_MAIL] )->literal : '';
		}
		$user = [ 
				'uri' => $testTaker->getUri (),
				'label' => $label,
				'login' => $login,
				'lastname' => $lastname,
				'firstname' => $firstname,
				'mail' => $mail 
		];
		// get test variables
		$testVariables = [ ];
		$testTracesVariables = [ ];
		$testResults = $this->getTestsFromDeliveryResult ( $deliveryExecution );
		
		// \common_Logger::d('result(' . $resultCounter . '/' . $nbOfResults . ')testVariable(' . count($testResults) . ')');
		foreach ( $testResults as $testResult ) {
			$testResultVariables = $this->getVariablesFromObjectResult ( $testResult );
			foreach ( $testResultVariables as $testVar ) {
				$theVar = reset ( $testVar );
				$testUri = $theVar->test;
				$varClass = $theVar->class;
				switch ($varClass) {
					case \taoResultServer_models_classes_OutcomeVariable::class :
						$var = $theVar->variable;
						$testVariables [$var->getIdentifier ()] = $var->getValue ();
						break;
					case \taoResultServer_models_classes_TraceVariable::class :
						$var = $theVar->variable;
						$testTracesVariables [$var->getIdentifier ()] = $var->getTrace ();
						\common_Logger::i ( 'trace variable: ' . $var->getTrace () );
						break;
					default :
						\common_Logger::w ( 'not supported test variable type: ' . $varClass );
						break;
				}
			}
		}		
		
		// get items
		$items = [ ];
		$itemResults = $this->getItemResultsFromDeliveryResult ( $deliveryExecution );
		$nbOfItems = count ( $itemResults );
		$itemCounter = 0;
		$dateLastTestTaken = new \DateTime('01-01-1000');
		// \common_Logger::d('result(' . $resultCounter . '/' . $nbOfResults . ')items(' . $nbOfItems . ')');
		foreach ( $itemResults as $itemResult ) {
			$itemCounter ++;
			// \common_Logger::d('result(' . $resultCounter . '/' . $nbOfResults . ')item(' . $itemCounter . '/' . $nbOfItems . ')');
			$itemResultVariables = $this->getVariablesFromObjectResult ( $itemResult );
			if (! empty ( $itemResultVariables )) {
				$outcomeVariables = [ ];
				$responseVariables = [ ];
				$itemUri = null;
				$itemLabel = null;
				$itemRsc = null;
				$testItem = null;
				$itemEpoch = 0;
				foreach ( $itemResultVariables as $itemVar ) {
					$theVar = reset ( $itemVar );
					$varClass = $theVar->class;
					$itemUri = $theVar->item;
					// len + 1 (for the "." char)
					$testItem = substr ( $theVar->callIdItem, strlen ( $theVar->deliveryResultIdentifier ) + 1 );
					if (empty ( $itemRsc )) {
						$itemRsc = new \core_kernel_classes_Resource ( $itemUri );
						$itemLabel = $itemRsc->getLabel ();
					}
					switch ($varClass) {
						case \taoResultServer_models_classes_ResponseVariable::class :
							$var = $theVar->variable;
							$itemEpoch = (empty ( $itemEpoch )) ? self::getDateFromVar ( $var ) : $itemEpoch;
							$responseVariables [$var->getIdentifier ()] = $var->getValue ();
							break;
						case \taoResultServer_models_classes_OutcomeVariable::class :
							$var = $theVar->variable;
							$itemEpoch = (empty ( $itemEpoch )) ? self::getDateFromVar ( $var ) : $itemEpoch;
							$outcomeVariables [$var->getIdentifier ()] = $var->getValue ();
							break;
						default :
							\common_Logger::w ( 'not supported item variable type: ' . $varClass );
							break;
					}				
					
					$dateLastTestTaken = self::mostRecentDate($dateLastTestTaken, $itemEpoch);
				}
				$items [$testItem] = [ 
						'date' => $itemEpoch,
						'testItem' => $testItem,
						'uri' => $itemUri,
						'label' => $itemLabel,
						'responseVariables' => $responseVariables,
						'outcomeVariables' => $outcomeVariables 
				];
			}
		}
		$delivery = $this->getDelivery ( $deliveryExecution );
		$deliveryExists = $delivery->exists () ? '1' : '0';
		return [ 
				'testUri' => $testUri,
				// The date formatting is wrong. The date given by the database is in UTC. This format says its CEST as by the generis.conf setting.
				'lastTestTaken' => $dateLastTestTaken->format('d-m-Y H:i:s T'),
				'deliveryUri' => $delivery->getUri (),
				'deliveryExists' => $deliveryExists,
				'deliveryResultUri' => $deliveryExecution->getIdentifier (),
				'deliveryResultLabel' => $deliveryExecution->getLabel (),
				'user' => $user,
				'variables' => $testVariables,
				'items' => $items 
		];
	}
	protected static function getDateFromVar(\taoResultServer_models_classes_Variable $var) {
		return \tao_helpers_Date::displayeDate ( \tao_helpers_Date::getTimeStamp ( $var->getEpoch () ) );
	}
	
	private function mostRecentDate(\DateTime $currentlyMostRecent, $possibleNewMostRecent) {
		$possibleNew = \DateTime::createFromFormat('d/m/Y H:i:s', $possibleNewMostRecent);
		
		return $currentlyMostRecent > $possibleNew ? $currentlyMostRecent : $possibleNew;
	}

	
	/**
	 *
	 * @param mixed $data        	
	 * @param string $pathFileName        	
	 * @throws \common_exception_FileSystemError
	 * @return void
	 */
	public static function saveToJsonFile($data, $pathFileName) {
		self::setEncoding ();
		\common_Logger::d ( __FUNCTION__ . ' Memory usage before json save: ' . (memory_get_usage ( true ) / 1024 / 1024) . 'MB peak: ' . (memory_get_peak_usage ( true ) / 1024 / 1024) . 'MB' );
		$jsonString = json_encode ( $data, JSON_PRETTY_PRINT | JSON_UNESCAPED_UNICODE );
		\common_Logger::d ( __FUNCTION__ . ' writing to: ' . $pathFileName );
		if (file_put_contents ( $pathFileName, $jsonString ) === false) {
			throw new \common_exception_FileSystemError ( 'write error for: ' . $pathFileName );
		}
		\common_Logger::d ( __FUNCTION__ . ' Memory usage after json save: ' . (memory_get_usage ( true ) / 1024 / 1024) . 'MB peak: ' . (memory_get_peak_usage ( true ) / 1024 / 1024) . 'MB' );
	}
// 	/**
// 	 *
// 	 * @param \PHPExcel $excel        	
// 	 * @param string $pathFileName        	
// 	 * @return void
// 	 */
// 	public static function saveExcel(\PHPExcel $excel, $pathFileName) {
// 		self::setEncoding ();
// 		\common_Logger::d ( __FUNCTION__ . ' Memory usage before excel save: ' . (memory_get_usage ( true ) / 1024 / 1024) . 'MB peak: ' . (memory_get_peak_usage ( true ) / 1024 / 1024) . 'MB' );
// 		$xls = \PHPExcel_IOFactory::createWriter ( $excel, 'Excel2007' );
// 		\common_Logger::d ( __FUNCTION__ . ' writing to: ' . $pathFileName );
// 		$xls->save ( $pathFileName );
// 		\common_Logger::d ( __FUNCTION__ . ' Memory usage after excel save: ' . (memory_get_usage ( true ) / 1024 / 1024) . 'MB peak: ' . (memory_get_peak_usage ( true ) / 1024 / 1024) . 'MB' );
// 	}
// 	/**
// 	 *
// 	 * @param \PHPExcel_Worksheet $sheet        	
// 	 * @param integer $col        	
// 	 * @param integer $row        	
// 	 * @param mixed $value        	
// 	 */
// 	protected static function setCell(\PHPExcel_Worksheet $sheet, $col, $row, $value) {
// 		$cell = $sheet->setCellValueByColumnAndRow ( $col, $row, $value, true );
// 		$sheet->getColumnDimension ( $cell->getColumn () )->setAutoSize ( true );
// 	}
	/**
	 *
	 * @param string $prop        	
	 * @param array $properties        	
	 */
	protected static function getOnePropertyValue($prop, $properties) {
		if (array_key_exists ( $prop, $properties )) {
			$oneProp = reset ( $properties [$prop] );
			if ($oneProp instanceof \core_kernel_classes_Resource) {
				return $oneProp->getUri ();
			} else if ($oneProp instanceof \core_kernel_classes_Literal) {
				return $oneProp->literal;
			} else {
				\common_Logger::w ( 'unsupported class: ' . get_class ( $oneProp ) );
			}
		}
		return '';
	}
// 	/**
// 	 * Make some sorts, try to identify some pattern like skillcards
// 	 *
// 	 * @param mixed $data        	
// 	 * @return PHPExcel
// 	 */
// 	public function resultsProcessing(&$d_results) {
// 		\common_Logger::d ( __FUNCTION__ . ' Memory usage before data processing: ' . (memory_get_usage ( true ) / 1024 / 1024) . 'MB peak: ' . (memory_get_peak_usage ( true ) / 1024 / 1024) . 'MB' );
// 		self::setEncoding ();
		
// 		// build excel document
// 		$excel = self::getEmptyExcel ();
// 		$sheet = self::newSheet ( $excel, 'results' );
		
// 		// set header
// 		$row = 1;
// 		$col = 0;
// 		$sheet->getStyle ( $row . ':' . $row )->getFont ()->setBold ( true );
// 		self::setCell ( $sheet, $col ++, $row, 'user login' );
// 		self::setCell ( $sheet, $col ++, $row, 'test name' );
// 		self::setCell ( $sheet, $col ++, $row, 'test id' );
// 		self::setCell ( $sheet, $col ++, $row, 'test status' );
// 		self::setCell ( $sheet, $col ++, $row, 'test attempt id' );
// 		self::setCell ( $sheet, $col ++, $row, 'item status' );
// 		self::setCell ( $sheet, $col ++, $row, 'item id' );
// 		self::setCell ( $sheet, $col ++, $row, 'item lang' );
// 		self::setCell ( $sheet, $col ++, $row, 'item stem' );
// 		self::setCell ( $sheet, $col ++, $row, 'item date' );
// 		self::setCell ( $sheet, $col ++, $row, 'item is correct' );
// 		self::setCell ( $sheet, $col ++, $row, 'item user response' );
// 		self::setCell ( $sheet, $col ++, $row, 'item title' );
// 		self::setCell ( $sheet, $col ++, $row, 'item category' );
// 		self::setCell ( $sheet, $col ++, $row, 'item template' );
		
// 		// set rows
// 		foreach ( $d_results as &$d_result ) {
// 			$testName = $d_result ['deliveryResultLabel'];
			
// 			$testUri = $d_result ['deliveryUri'];
// 			$testId = substr ( $testUri, strrpos ( $testUri, '#' ) + 1 );
			
// 			$testStatus = (strcmp ( $d_result ['deliveryExists'], '1' ) === 0) ? 'exists' : 'deleted';
			
// 			$attemptUri = $d_result ['deliveryResultUri'];
// 			$attemptId = substr ( $attemptUri, strrpos ( $attemptUri, '#' ) + 1 );
			
// 			$userLogin = $d_result ['user'] ['login'];
			
// 			foreach ( $d_result ['items'] as $itemRef => &$d_item ) {
// 				$row ++;
// 				$col = 0;
// 				$itemUri = $d_item ['uri'];
// 				$itemRsc = new \core_kernel_classes_Resource ( $itemUri );
// 				$itemExists = $itemRsc->exists ();
// 				$itemStatus = ($itemExists) ? 'exists' : 'deleted';
// 				$itemId = substr ( $itemUri, strrpos ( $itemUri, '#' ) + 1 );
// 				$itemDate = $d_item ['date'];
				
// 				$outcomeVariables = $d_item ['outcomeVariables'];
// 				$itemIsCorrect = '-';
// 				$varName = 'SCORE';
// 				if (array_key_exists ( $varName, $outcomeVariables )) {
// 					$score = $outcomeVariables [$varName];
// 					$itemIsCorrect = (floatval ( $score ) > 0) ? 'correct' : 'incorrect';
// 				}
				
// 				$itemCategory = '(not defined)';
// 				$itemTemplate = '(not defined)';
// 				$itemTitle = '(not defined)';
// 				if ($itemExists) {
// 					$itemProperties = $itemRsc->getPropertiesValues ( [ 
// 							new \core_kernel_classes_Property ( TAO_AIG_FACADE_MODEL_PROPERTY_CATEGORY ),
// 							new \core_kernel_classes_Property ( TAO_AIG_FACADE_MODEL_PROPERTY_SOURCETEMPLATE ),
// 							new \core_kernel_classes_Property ( TAO_AIG_FACADE_MODEL_PROPERTY_ITEMTITLE ) 
// 					] );
// 					$itemCategory = self::getOnePropertyValue ( TAO_AIG_FACADE_MODEL_PROPERTY_CATEGORY, $itemProperties );
// 					$itemTemplate = self::getOnePropertyValue ( TAO_AIG_FACADE_MODEL_PROPERTY_SOURCETEMPLATE, $itemProperties );
// 					$itemTitle = self::getOnePropertyValue ( TAO_AIG_FACADE_MODEL_PROPERTY_ITEMTITLE, $itemProperties );
// 				}
				
// 				$responseVariables = $d_item ['responseVariables'];
// 				$userResponse = '-';
				
// 				$varName = 'RESPONSE';
// 				if (array_key_exists ( $varName, $responseVariables )) {
// 					$responseId = $responseVariables [$varName];
// 					$userResponse = 'responseId: ' . $responseId;
// 					self::addResponseMapToItem ( $d_item );
// 					$responseMap = $d_item ['responseMap'];
// 					if (array_key_exists ( $responseId, $responseMap )) {
// 						$userResponse = $responseMap [$responseId];
// 					}
// 				}
				
// 				$itemLang = '(not defined)';
// 				if ($itemExists) {
// 					$sessionDataLg = \common_session_SessionManager::getSession ()->getDataLanguage ();
// 					if (\taoItems_models_classes_ItemsService::singleton ()->hasItemContent ( $itemRsc, $sessionDataLg )) {
// 						$itemLang = $sessionDataLg;
// 					} else if (\taoItems_models_classes_ItemsService::singleton ()->hasItemContent ( $itemRsc, DEFAULT_LANG )) {
// 						$itemLang = DEFAULT_LANG;
// 						\common_Logger::i ( 'found content in default language: ' . DEFAULT_LANG . ' not in: ' . $sessionDataLg );
// 					} else {
// 						\common_Logger::w ( 'no content found for language: ' . $sessionDataLg . ' or default one: ' . DEFAULT_LANG );
// 					}
// 				}
				
// 				$itemStem = '(not defined)';
// 				if ($itemExists) {
// 					$itemStem = trim ( self::getPrompt ( $itemRsc ) );
// 					$limit = 128;
// 					$endOfShortenedString = '(…)';
// 					if (mb_strlen ( $itemStem ) > $limit) {
// 						$itemStem = mb_substr ( $itemStem, 0, $limit - mb_strlen ( $endOfShortenedString ) ) . $endOfShortenedString;
// 					}
// 				}
				
// 				self::setCell ( $sheet, $col ++, $row, $userLogin );
// 				self::setCell ( $sheet, $col ++, $row, $testName );
// 				self::setCell ( $sheet, $col ++, $row, $testId );
// 				self::setCell ( $sheet, $col ++, $row, $testStatus );
// 				self::setCell ( $sheet, $col ++, $row, $attemptId );
// 				self::setCell ( $sheet, $col ++, $row, $itemStatus );
// 				self::setCell ( $sheet, $col ++, $row, $itemId );
// 				self::setCell ( $sheet, $col ++, $row, $itemLang );
// 				self::setCell ( $sheet, $col ++, $row, $itemStem );
// 				self::setCell ( $sheet, $col ++, $row, $itemDate );
// 				self::setCell ( $sheet, $col ++, $row, $itemIsCorrect );
// 				self::setCell ( $sheet, $col ++, $row, $userResponse );
// 				self::setCell ( $sheet, $col ++, $row, $itemTitle );
// 				self::setCell ( $sheet, $col ++, $row, $itemCategory );
// 				self::setCell ( $sheet, $col ++, $row, $itemTemplate );
// 			}
// 		}
// 		\common_Logger::d ( __FUNCTION__ . ' Memory usage after data processing: ' . (memory_get_usage ( true ) / 1024 / 1024) . 'MB peak: ' . (memory_get_peak_usage ( true ) / 1024 / 1024) . 'MB' );
// 		return $excel;
// 	}
	/**
	 * Get prompt from QTI xml file
	 *
	 * @param \core_kernel_classes_Resource $rsc        	
	 * @return string or null if not found
	 */
	protected static function getPrompt(\core_kernel_classes_Resource $rsc) {
		$prompt = null;
		\common_Logger::w ( var_export ( $rsc->getUri (), true ) );
		if ($rsc->exists ()) {
			$xmlStr = \taoItems_models_classes_ItemsService::singleton ()->getItemContent ( $rsc );
			if (! empty ( $xmlStr )) {
				$doc = new \DOMDocument ();
				$doc->loadXML ( $xmlStr );
				$choices = $doc->getElementsByTagName ( 'prompt' );
				if ($choices->length > 0) {
					$prompt = $choices->item ( 0 )->nodeValue;
				}
			}
		}
		return $prompt;
	}
// 	protected static function addOneSheetForCompetencyMaxScore(\PHPExcel $excel, $results, $itemLevelsModel, $competencyLabel) {
// 		$compentencyTestItems = self::getTestItemsForACompetency ( $itemLevelsModel );
// 		// add the max score worksheet
// 		$varSheet = self::newSheet ( $excel, $competencyLabel, 'CompMaxScore' );
// 		$row = 1;
// 		$col = 0;
// 		// set user header
// 		$cell = $varSheet->setCellValueByColumnAndRow ( $col ++, $row, 'login', true );
// 		$varSheet->getColumnDimension ( $cell->getColumn () )->setAutoSize ( true );
// 		// set var header
// 		$cell = $varSheet->setCellValueByColumnAndRow ( $col ++, $row, $competencyLabel . ' - ' . 'CompMaxScore', true );
// 		$varSheet->getColumnDimension ( $cell->getColumn () )->setAutoSize ( true );
// 		// access each result
// 		$row ++;
// 		foreach ( $results as $result ) {
// 			$col = 0;
// 			$userLogin = $result ['user'] ['login'];
// 			$varSheet->setCellValueByColumnAndRow ( $col ++, $row, $userLogin );
// 			$userItems = $result ['items'];
// 			// search max score
// 			$maxScore = 0;
// 			foreach ( $compentencyTestItems as $testItem ) {
// 				// search in results
// 				if (array_key_exists ( $testItem, $userItems )) {
// 					$item = $userItems [$testItem];
// 					$outcomeVariables = $item ['outcomeVariables'];
// 					$wantedVar = 'SCORE';
// 					if (array_key_exists ( $wantedVar, $outcomeVariables )) {
// 						$score = floatval ( $outcomeVariables [$wantedVar] );
// 						if ($score > $maxScore) {
// 							$maxScore = $score;
// 						}
// 					}
// 				}
// 			}
// 			$varSheet->setCellValueByColumnAndRow ( $col ++, $row, $maxScore );
// 			$row ++;
// 		}
// 	}
	protected static function getTestItemsForACompetency($levels) {
		$testItems = [ ];
		foreach ( $levels as $level => $items ) {
			foreach ( $items as $item ) {
				$testItems [] = $item ['testItem'];
			}
		}
		return $testItems;
	}
// 	/**
// 	 * Remove default worksheet
// 	 *
// 	 * @return \PHPExcel
// 	 */
// 	protected static function getEmptyExcel() {
// 		\PHPExcel_Shared_Font::setAutoSizeMethod ( \PHPExcel_Shared_Font::AUTOSIZE_METHOD_EXACT );
// 		$excel = new \PHPExcel ();
// 		$excel->removeSheetByIndex ( 0 );
// 		return $excel;
// 	}
	/**
	 * Prepare the value based on its name
	 *
	 * @param string $name        	
	 * @param string $value        	
	 * @return string
	 */
	protected static function prepareValue($name, $value, $itemModel) {
		$prepared = $value;
		if ((strpos ( $name, 'RESPONSE' ) !== false)) {
			if (preg_match ( '/^\[\'(.*)\'\]$/', $value, $matches ) == 1) {
				$identifier = $matches [1];
				$responseMap = $itemModel ['responseMap'];
				if (array_key_exists ( $identifier, $responseMap )) {
					$prepared = $responseMap [$identifier];
				}
			}
		}
		switch ($name) {
			case 'duration' :
				$computed = self::intervalToSeconds ( $value );
				if ($computed !== null) {
					$prepared = $computed;
				}
				break;
			default :
				break;
		}
		return $prepared;
	}
// 	/**
// 	 *
// 	 * @param \PHPExcel $excel        	
// 	 * @param mixed $itemModel        	
// 	 * @param mixed[] $results        	
// 	 * @param string $typeOfVar
// 	 *        	e.g: outcomeVariables, responseVariables
// 	 */
// 	protected static function addOneSheetPerVariableInList(\PHPExcel &$excel, $itemModel, $results, $typeOfVar) {
// 		$wantedTestItem = $itemModel ['testItem'];
// 		foreach ( $itemModel [$typeOfVar] as $varName => $varValue ) {
// 			$varSheet = self::newSheet ( $excel, $itemModel ['label'], $varName );
// 			$row = 1;
// 			$col = 0;
// 			// set user header
// 			$cell = $varSheet->setCellValueByColumnAndRow ( $col ++, $row, 'login', true );
// 			$varSheet->getColumnDimension ( $cell->getColumn () )->setAutoSize ( true );
// 			// set var header
// 			$cell = $varSheet->setCellValueByColumnAndRow ( $col ++, $row, $itemModel ['label'] . ' - ' . $varName, true );
// 			$varSheet->getColumnDimension ( $cell->getColumn () )->setAutoSize ( true );
// 			// access each result
// 			$row ++;
// 			foreach ( $results as $result ) {
// 				$col = 0;
// 				$userLogin = $result ['user'] ['login'];
// 				$varSheet->setCellValueByColumnAndRow ( $col ++, $row, $userLogin );
// 				$userItems = $result ['items'];
// 				$theVal = self::EXCEL_CELL_DEFAULT;
// 				if (array_key_exists ( $wantedTestItem, $userItems )) {
// 					if (array_key_exists ( $varName, $userItems [$wantedTestItem] [$typeOfVar] )) {
// 						$theVal = self::prepareValue ( $varName, $userItems [$wantedTestItem] [$typeOfVar] [$varName], $itemModel );
// 					} else {
// 						\common_Logger::w ( 'variable NOT found: ' . $varName . ' userLogin: ' . $userLogin . ' testItem: ' . $wantedTestItem . ' itemUri: ' . $itemModel ['uri'] . ' itemLabel: ' . $itemModel ['label'] );
// 					}
// 				}
// 				$varSheet->setCellValueByColumnAndRow ( $col ++, $row, $theVal );
// 				$row ++;
// 			}
// 		}
// 	}
// 	/**
// 	 *
// 	 * @param \PHPExcel $excel        	
// 	 * @param string $title        	
// 	 * @return bool
// 	 */
// 	protected static function hasTitleInWorkBook(\PHPExcel $excel, $title) {
// 		$sheets = $excel->getAllSheets ();
// 		foreach ( $sheets as $sheet ) {
// 			if (strcasecmp ( $sheet->getTitle (), $title ) == 0) {
// 				return true;
// 				break;
// 			}
// 		}
// 		return false;
// 	}
	/**
	 *
	 * @param string $firstPart        	
	 * @param string $secondPart        	
	 * @param string $id        	
	 * @throws \common_Exception
	 * @return string
	 */
	protected static function defineTitleForWorkSheet($title, $id = null) {
		$idStr = ($id !== null) ? ' ' . $id : '';
		$newTitle = mb_substr ( $title, 0, 24 ) . $idStr;
		if (mb_strlen ( $newTitle ) > 31) {
			throw new \common_Exception ( 'too many title duplicates: ' . $newTitle );
		}
		return $newTitle;
	}
// 	/**
// 	 *
// 	 * @param \PHPExcel $excel        	
// 	 * @param string $firstTitlePart        	
// 	 * @param string $secondTitlePart        	
// 	 * @param string $id        	
// 	 * @return string
// 	 */
// 	protected static function getUniqueTitle(\PHPExcel $excel, $title, $id = null) {
// 		$title = self::defineTitleForWorkSheet ( $title, $id );
// 		if (self::hasTitleInWorkBook ( $excel, $title )) {
// 			$title = self::getUniqueTitle ( $excel, $title, (($id === null) ? 2 : ++ $id) );
// 		}
// 		return $title;
// 	}
// 	/**
// 	 *
// 	 * @param \PHPExcel $excel        	
// 	 * @param string $firstTitlePart        	
// 	 * @param string $secondTitlePart        	
// 	 * @return \PHPExcel_Worksheet
// 	 */
// 	protected static function newSheet(\PHPExcel $excel, $title) {
// 		$sheet = new \PHPExcel_Worksheet ();
// 		$sheet->setTitle ( self::getUniqueTitle ( $excel, $title ) );
// 		$excel->addSheet ( $sheet );
// 		return $sheet;
// 	}
	/**
	 *
	 * @param
	 *        	array<string, mixed> $array
	 * @return void
	 */
	protected static function sortKeysAlphabetical(&$array) {
		uksort ( $array, function ($a, $b) {
			return strcoll ( mb_strtolower ( $a ), mb_strtolower ( $b ) );
		} );
	}
	/**
	 *
	 * @param
	 *        	array<string, mixed> $competencyLabels
	 * @param string $label        	
	 * @param string $lvl        	
	 * @param mixed $item        	
	 */
	protected static function addCompetencyItemAsLabel(&$competencyLabels, $label, $lvl, $item) {
		if (! array_key_exists ( $label, $competencyLabels )) {
			// define an array for levels
			$competencyLabels [$label] = [ ];
		}
		$levels = &$competencyLabels [$label];
		
		if (! array_key_exists ( $lvl, $levels )) {
			// define an array to avoid same item label
			$levels [$lvl] = [ ];
		}
		$levels [$lvl] [] = $item;
	}
	/**
	 *
	 * @param mixed[] $normalItems        	
	 * @param string $key        	
	 * @return void
	 */
	protected static function sortValuesByValueKeyAlphabetical(&$normalItems, $key) {
		usort ( $normalItems, function ($a, $b) use($key) {
			return strcoll ( mb_strtolower ( $a [$key] ), mb_strtolower ( $b [$key] ) );
		} );
	}
	protected static function addInteractionChoice(\DOMDocument $doc, &$target, $typeOfChoice) {
		$choices = $doc->getElementsByTagName ( $typeOfChoice );
		foreach ( $choices as $choice ) {
			$target [$choice->getAttribute ( 'identifier' )] = $choice->nodeValue;
		}
	}
	protected static function addResponseMapToItem(&$item) {
		$itemContentProperty = new \core_kernel_classes_Property ( TAO_ITEM_CONTENT_PROPERTY );
		$responseMap = [ ];
		$rsc = new \core_kernel_classes_Resource ( $item ['uri'] );
		if ($rsc->exists () && \taoItems_models_classes_ItemsService::singleton ()->hasItemContent ( $rsc )) {
			$xmlStr = \taoItems_models_classes_ItemsService::singleton ()->getItemContent ( $rsc );
			if (! empty ( $xmlStr )) {
				$doc = new \DOMDocument ();
				$doc->loadXML ( $xmlStr );
				$typesOfChoice = [ 
						'simpleAssociableChoice',
						'associableChoice',
						'inlineChoice',
						'simpleChoice' 
				];
				foreach ( $typesOfChoice as $typeOfChoice ) {
					self::addInteractionChoice ( $doc, $responseMap, $typeOfChoice );
				}
			}
		}
		$item ['responseMap'] = $responseMap;
	}
	/**
	 *
	 * @param mixed $data        	
	 * @return array{ @type string competencies @type string classics}
	 */
	protected static function buildSkillcardTestItems(&$data) {
		// get test items
		$testItems = self::getTestItems ( $data );
		
		$competencies = [ ];
		$normalItems = [ ];
		foreach ( $testItems as $testItemUri => &$item ) {
			$label = $item ['label'];
			self::addResponseMapToItem ( $item );
			if (preg_match ( '/^lvl(\d+)_(.*)$/', $label, $matches ) == 1) {
				// a competency item
				$lvl = $matches [1];
				$shortLabel = $matches [2];
				self::addCompetencyItemAsLabel ( $competencies, $shortLabel, $lvl, $item );
			} else {
				$normalItems [] = $item;
			}
		}
		// sort classics
		self::sortValuesByValueKeyAlphabetical ( $normalItems, 'label' );
		// sort competencies
		self::sortKeysAlphabetical ( $competencies );
		// sort levels in competencies
		self::sortLevels ( $competencies );
		return [ 
				'competencies' => $competencies,
				'classics' => $normalItems 
		];
	}
	/**
	 *
	 * @param
	 *        	array<string, mixed> $competencies Key is level
	 * @return void
	 */
	protected static function sortLevels(&$competencies) {
		foreach ( $competencies as &$levels ) {
			uksort ( $levels, function ($a, $b) {
				return strnatcmp ( $a, $b );
			} );
		}
	}
	protected static function getTestItems($data) {
		$collected = [ ];
		foreach ( $data ['results'] as $result ) {
			$collected = array_merge ( $collected, $result ['items'] );
		}
		return $collected;
	}
	protected static function collectTestVariables($data) {
		$vars = [ ];
		foreach ( $data ['results'] as $result ) {
			$vars = array_merge ( $vars, $result ['variables'] );
		}
		return $vars;
	}
	
	/**
	 *
	 * @param string $interval
	 *        	ISO 8601 string
	 * @return int|null Return null if not parsed as an ISO 8601 string
	 */
	protected static function intervalToSeconds($interval) {
		$dateInverval = null;
		try {
			$dateInverval = new \DateInterval ( $interval );
		} catch ( \Exception $e ) {
			\common_Logger::w ( 'unsupported DateInterval format: ' . $interval . ' message: ' . $e->getMessage () );
			return null;
		}
		return date_create ( '@0' )->add ( $dateInverval )->getTimestamp ();
	}
	protected static function sortTestVariables(&$data) {
		uksort ( $data, function ($a, $b) {
			return strcoll ( mb_strtolower ( $a ), mb_strtolower ( $b ) );
		} );
	}
	protected static function sortByLogin(&$data) {
		usort ( $data, function ($a, $b) {
			return strnatcasecmp ( $a ['user'] ['label'], $b ['user'] ['label'] );
		} );
	}
}