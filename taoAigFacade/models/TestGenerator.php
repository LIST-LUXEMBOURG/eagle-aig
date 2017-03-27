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

use itis\taoAigFacade\helpers\XmlHelper;
use itis\taoAigFacade\models\exceptions\InsufficientKnowledgeException;
use itis\taoAigFacade\models\request\ItemType;
use itis\taoAigFacade\models\request\TestRequest;

class TestGenerator {
	const TEST_VARIABLE_NB_ITEMS_PRESENTED = 'TestItemsPresented';
	const TEST_VARIABLE_GLOBAL_SCORE = 'TestScore';
	const TEST_VARIABLE_SECTION_SCORE_RADIX = 'score';
	const TEST_VARIABLE_SECTION_NB_ITEMS_PRESENTED = 'iPresented';
	const TEST_IDENTIFIER_SECTION_RADIX = 'section';
	const TEST_IDENTIFIER_ITEM_RADIX = 'i';
	protected $itemClass = null;
	public function __construct() {
		$this->itemClass = new \core_kernel_classes_Class ( TAO_AIG_FACADE_MODEL_ITEMCLASS );
	}
	public function generate(TestRequest $request) {
		$report = \common_report_Report::createSuccess ( __ ( 'Test Generation Successful' ) );
		
		$previousSession = null;
		$lang = $request->getLanguage ();
		if (! empty ( $lang )) {
			$previousSession = \common_session_SessionManager::getSession ();
			// create dummy session to handle language (replace the current one!)
			\common_session_SessionManager::startSession ( new Session ( $lang ) );
		}
		
		$itemListByTypes = [ ];
		// define wanted item properties
		$globalItemPropertyList = new KeyList ( $request->getUnicity () );
		$unicity = new Unicity ( $request->getUnicity () );
		$currentNbOfItems = 0;
		$maxNbOfItems = $request->getMaxNumberOfItems ();
		// search for items
		foreach ( $request->getItemTypes () as $itemType ) {
			$this->getItems ( $itemListByTypes, $itemType, $maxNbOfItems, $currentNbOfItems, $globalItemPropertyList, $unicity );
			if ($currentNbOfItems >= $maxNbOfItems) {
				break;
			}
		}
		// some checks
		$itemListByTypes = self::prepareItems ( $itemListByTypes );
		
		// generate test
		$testRsc = self::createEmptyTest ( $request->getTestName () );
		$doc = self::getTestContent ( $testRsc );
		$assessmentTest = self::configureMainAssessmentTest ( $doc, $request->getTestName (), $outcomeProcessing );
		$testPart = self::configureMainTestPart ( $assessmentTest, $mainSection );
		self::buildSectionIdentifiers ( $itemListByTypes );
		// add section for each item type
		foreach ( $itemListByTypes as list ( $itemType, $itemsForTest, $sectionId ) ) {
			$section = self::addEmptySection ( $mainSection, $sectionId );
			self::addItemsToSection ( $section, $itemsForTest, $report );
			self::addOutcomeProcessingsForSection ( $assessmentTest, $sectionId );
		}
		// set global outcomeprocessing
		$sectionIdentifiers = array_column ( $itemListByTypes, 2 );
		self::addOutcomeProcessingForSum ( $outcomeProcessing, self::TEST_VARIABLE_GLOBAL_SCORE, self::getSectionScoreVariableNames ( $sectionIdentifiers ) );
		self::addOutcomeProcessingForSum ( $outcomeProcessing, self::TEST_VARIABLE_NB_ITEMS_PRESENTED, self::getSectionNbItemsPresentedVariableNames ( $sectionIdentifiers ) );
		
		// generate report
		TestReport::includeReport ( $testPart, false, $request->getTestName (), $itemListByTypes );
		
		// save test
		self::setTestContent ( $testRsc, $doc );
		
		// add test as report data
		$report->setData ( $testRsc->getUri () );
		
		// restore previous session
		if ($previousSession) {
			\common_session_SessionManager::startSession ( $previousSession );
		}
		
		return $report;
	}
	/**
	 * Remove section if no items for wanted itemType(s)
	 *
	 * @param [\core_kernel_classes_Resource] $itemListByTypes        	
	 * @throws InsufficientKnowledgeException if not items founds
	 */
	protected static function prepareItems($itemListByTypes) {
		if (empty ( $itemListByTypes )) {
			throw new InsufficientKnowledgeException ( 'no items of this types found' );
		}
		// remove types of item where no item
		$newList = [ ];
		foreach ( $itemListByTypes as list ( $itemType, $itemsForTest ) ) {
			if (! empty ( $itemsForTest )) {
				$newList [] = [ 
						$itemType,
						$itemsForTest 
				];
			}
		}
		if (empty ( $newList )) {
			throw new InsufficientKnowledgeException ( 'no items of this types found' );
		}
		return $newList;
	}
	protected static function addEmptySection(\DOMElement $destination, $identifier) {
		$template = '<assessmentSection identifier="' . $identifier . '" required="true" fixed="false" title=" " visible="false" keepTogether="false"></assessmentSection>';
		return XmlHelper::addNodeFromXmlString ( $destination, $template, false );
	}
	protected static function buildSectionIdentifiers(&$itemListByTypes) {
		$count = 0;
		foreach ( $itemListByTypes as &$array ) {
			array_push ( $array, self::TEST_IDENTIFIER_SECTION_RADIX . ($count ++) );
		}
	}
	protected static function addOrdering(\DOMElement $destination, $shuffle = true) {
		$shuffleStr = $shuffle ? 'true' : 'false';
		$template = '<ordering shuffle="' . $shuffleStr . '" />';
		return XmlHelper::addNodeFromXmlString ( $destination, $template, false );
	}
	protected static function configureMainTestPart(\DOMElement $node, &$mainSection) {
		$testPart = $node->getElementsByTagName ( 'testPart' )->item ( 0 );
		$itemSessionControl = $testPart->getElementsByTagName ( 'itemSessionControl' )->item ( 0 );
		$itemSessionControl->setAttribute ( 'allowComment', 'false' );
		$itemSessionControl->setAttribute ( 'allowSkipping', 'false' );
		$itemSessionControl->setAttribute ( 'maxAttempts', '1' );
		$mainSection = $testPart->getElementsByTagName ( 'assessmentSection' )->item ( 0 );
		$mainSection->setAttribute ( 'keepTogether', 'false' );
		$mainSection->setAttribute ( 'title', ' ' );
		$mainSection->setAttribute ( 'fixed', 'true' );
		$mainSection->setAttribute ( 'required', 'true' );
		self::addOrdering ( $mainSection );
		return $testPart;
	}
	protected static function configureMainAssessmentTest(\DOMDocument $doc, $testName, &$outcomeProcessing) {
		$section = $doc->documentElement;
		$section->setAttribute ( 'identifier', preg_replace ( "/^[^A-Za-z_]|[^A-Za-z0-9-]/", '_', $testName ) );
		$section->setAttribute ( 'title', $testName );
		$outcomeProcessing = $doc->createElement ( 'outcomeProcessing' );
		$section->appendChild ( $outcomeProcessing );
		self::addOutcomeDeclaration ( $section, self::TEST_VARIABLE_GLOBAL_SCORE );
		self::addOutcomeDeclaration ( $section, self::TEST_VARIABLE_NB_ITEMS_PRESENTED );
		return $section;
	}
	protected static function addOutcomeDeclaration(\DOMElement $destination, $identifier) {
		$template = '<outcomeDeclaration baseType="float" cardinality="single" identifier="' . $identifier . '">
						<defaultValue><value>0</value></defaultValue>
					</outcomeDeclaration>';
		return XmlHelper::addNodeFromXmlString ( $destination, $template, false );
	}
	protected static function addAssessmentItemRef(\DOMElement $destination, $identifier, $uri) {
		$template = '<assessmentItemRef identifier="' . $identifier . '" required="true" fixed="false" href="' . $uri . '" />';
		return XmlHelper::addNodeFromXmlString ( $destination, $template, false );
	}
	protected static function addItemsToSection(\DOMElement $section, $itemsForTest, \common_report_Report &$report) {
		$sectionId = $section->getAttribute ( 'identifier' );
		$sectionReport = new \common_report_Report ( \common_report_Report::TYPE_INFO, __ ( 'Add section' ), [ 
				'id' => $sectionId 
		] );
		$report->add ( $sectionReport );
		
		$itemIdRadix = $sectionId . self::TEST_IDENTIFIER_ITEM_RADIX;
		$itemCounter = 0;
		foreach ( $itemsForTest as $itemForTest ) {
			$itemUri = $itemForTest->getUri ();
			$itemRef = self::addAssessmentItemRef ( $section, $itemIdRadix . ($itemCounter ++), $itemUri );
			$sectionReport->add ( new \common_report_Report ( \common_report_Report::TYPE_INFO, __ ( 'Add item' ), [ 
					'uri' => $itemUri 
			] ) );
		}
	}
	/**
	 *
	 * @return \core_kernel_classes_Resource
	 */
	protected static function getTestContent(\core_kernel_classes_Resource $testRsc) {
		$doc = new \DOMDocument ();
		$doc->formatOutput = true;
		$doc->preserveWhiteSpace = false;
		$testService = \taoQtiTest_models_classes_QtiTestService::singleton ();
		$doc->load ( $testService->getDocPath ( $testRsc ) );
		return $doc;
	}
	protected static function setTestContent(\core_kernel_classes_Resource $testRsc, \DOMDocument $doc) {
		$content = $doc->saveXML ();
		$testService = \taoQtiTest_models_classes_QtiTestService::singleton ();
		$filePathName = $testService->getDocPath ( $testRsc );
		file_put_contents ( $filePathName, $content );
	}
	protected static function createEmptyTest($testName) {
		$testService = \taoQtiTest_models_classes_QtiTestService::singleton ();
		$testClass = $testService->getRootClass ();
		// fix tao first char problem (TAO replaces invalid chars in identifiers with hyphen chars, including the first one that is not allowed for identifier)
		$testName = preg_replace ( "/^[^A-Za-z_]/", '', $testName );
		$testRsc = $testService->createInstance ( $testClass, $testName );
		// $report->add ( new \common_report_Report ( \common_report_Report::TYPE_INFO, "Test created: " . $testRsc->getLabel () ) );
		self::copyDefaultStyleSheet ( $testRsc );
		
		return $testRsc;
	}
	protected static function copyDefaultStyleSheet(\core_kernel_classes_Resource $testRsc) {
		$testService = \taoQtiTest_models_classes_QtiTestService::singleton ();
		$filename = 'rubrickBlock.css';
		$srcPath = DIR_VIEWS . 'css' . DIRECTORY_SEPARATOR . $filename;
		$destPath = $testService->getTestFile ( $testRsc )->getAbsolutePath () . DIRECTORY_SEPARATOR . $filename;
		if (! copy ( $srcPath, $destPath )) {
			throw new \common_exception_FileSystemError ( __METHOD__ . ' failed, src: ' . $srcPath . ' dest: ' . $destPath );
		}
	}
	protected static function getWantedProperties(ItemType $itemType, KeyList $globalItemPropertyList) {
		$itemPropertiesKeyList = clone $globalItemPropertyList;
		// add criteria to propertylist
		foreach ( $itemType->getCriteria () as $criteria ) {
			$itemPropertiesKeyList->addValueToList ( $criteria->getType () );
		}
		return $itemPropertiesKeyList->getListAsValues ();
	}
	protected function getItemPool($wantedPoolSize, ItemType $itemType, KeyList $globalItemPropertyList, Unicity $globalUnicity) {
		$wantedProps = self::getWantedProperties ( $itemType, $globalItemPropertyList );
		
		// define a temporary unicity that can be used for the pool
		$unicityForItemPool = clone $globalUnicity;
		
		$itemPool = [ ];
		$offset = 0;
		do {
			$dbLimitReached = false;
			// search more items because unicity and constraints will remove some items and another request will be performed
			$nbOfItemToSearch = $wantedPoolSize + 100;
			$dbItems = $this->searchItems ( $itemType->getType (), $itemType->getValue (), $offset, $nbOfItemToSearch, $dbLimitReached );
			$wantMore = true;
			foreach ( $dbItems as $dbItem ) {
				$itemForTest = self::getItemForTest ( $dbItem->getUri (), $dbItem->getPropertiesValues ( $wantedProps ) );
				if (! $this->verifyContraints ( $itemForTest, $itemType->getCriteria () )) {
					continue;
				}
				if (! $unicityForItemPool->addToList ( $itemForTest->getProperties () )) {
					continue;
				}
				$itemPool [] = $itemForTest;
				if (count ( $itemPool ) >= $wantedPoolSize) {
					$wantMore = false;
					break;
				}
			}
		} while ( $wantMore && ! $dbLimitReached );
		return $itemPool;
	}
	protected function getItems(&$itemListByTypes, ItemType $itemType, $totalMaxNbOfItems, &$currentGlobalNbOfItems, KeyList $globalItemPropertyList, Unicity &$globalUnicity) {
		$wantedNbOfItems = self::defineAmountOfItems ( $itemType->getAmount (), $totalMaxNbOfItems );
		if (($currentGlobalNbOfItems + $wantedNbOfItems) > $totalMaxNbOfItems) {
			\common_Logger::w ( 'the total nb of items exceeds the test limit, current count: ' . $currentGlobalNbOfItems . ' total limit: ' . $totalMaxNbOfItems . ' wanted: ' . $wantedNbOfItems . ' with this itemType: ' . $itemType->getType () . ' - ' . $itemType->getValue () );
			$wantedNbOfItems = $totalMaxNbOfItems - $currentGlobalNbOfItems;
		}
		
		$wantedPoolSize = self::definePoolSize ( $wantedNbOfItems, $itemType->getDataSetFactor () );
		$itemPool = $this->getItemPool ( $wantedPoolSize, $itemType, $globalItemPropertyList, $globalUnicity );
		
		// shuffle items
		shuffle ( $itemPool );
		
		// get wanted number of items
		$poolSize = count ( $itemPool );
		$nbOfItemsToPickUpInPool = $wantedNbOfItems;
		if ($poolSize < $wantedPoolSize) {
			\common_Logger::w ( 'selection on reduced pool, poolSize: ' . $poolSize . ' wanted: ' . $wantedPoolSize . ' with this itemType: ' . $itemType->getType () . ' - ' . $itemType->getValue () );
		}
		if ($poolSize < $wantedNbOfItems) {
			\common_Logger::w ( 'not enough items, poolSize: ' . $poolSize . ' wanted: ' . $wantedNbOfItems . ' with this itemType: ' . $itemType->getType () . ' - ' . $itemType->getValue () );
			$nbOfItemsToPickUpInPool = $poolSize;
		}
		$items = array_slice ( $itemPool, 0, $nbOfItemsToPickUpInPool );
		
		// update global values
		foreach ( $items as $itemForTest ) {
			$globalUnicity->addToList ( $itemForTest->getProperties () );
			$currentGlobalNbOfItems ++;
		}
		array_push ( $itemListByTypes, [ 
				$itemType,
				$items 
		] );
	}
	protected static function getItemForTest($uri, $wantedRscProperties) {
		$itemPropertiesValues = [ ];
		foreach ( $wantedRscProperties as $propName => $values ) {
			if (! empty ( $values )) {
				$oneProp = reset ( $values );
				$value = null;
				if ($oneProp instanceof \core_kernel_classes_Resource) {
					$value = $oneProp->getUri ();
				} else if ($oneProp instanceof \core_kernel_classes_Literal) {
					$value = $oneProp->literal;
				} else {
					\common_Logger::w ( 'unsupported class: ' . get_class ( $oneProp ) );
					continue;
				}
				$itemPropertiesValues [$propName] = $value;
			}
		}
		$itemForTest = new ItemForTest ( $uri, $itemPropertiesValues );
		return $itemForTest;
	}
	protected function searchItems($typeToSearch, $typeValue, &$offset, $limit, &$dbLimitReached) {
		// get more items that needed to avoid
		\common_Logger::d ( 'sql query offset: ' . $offset . ' limit: ' . $limit );
		$options = [ 
				'like' => false,
				'recursive' => true,
				'offset' => $offset,
				'limit' => $limit 
		];
		$dbItems = $this->itemClass->searchInstances ( array (
				$typeToSearch => $typeValue 
		), $options );
		$offset += $limit;
		$dbLimitReached = (count ( $dbItems ) != $limit) ? true : false;
		return $dbItems;
	}
	protected static function definePoolSize($nbOfWantedItems, $factor) {
		$poolSize = $nbOfWantedItems * intval ( $factor );
		if ($poolSize < $nbOfWantedItems) {
			\common_Logger::w ( 'bad factor value: ' . $factor );
			$poolSize = $nbOfWantedItems;
		}
		return $poolSize;
	}
	protected static function defineAmountOfItems($amount, $totalNb) {
		if (preg_match ( '/(.*)%/', $amount, $matches ) == 1) {
			return ceil ( floatval ( $matches [1] ) * $totalNb / 100 );
		} else {
			return intval ( $amount );
		}
	}
	protected function verifyContraints(ItemForTest $itemForTest, $criteria) {
		// FIXME TODO deal with request constraints before unicity
		return true;
	}
	public static function getSectionScoreVariableNames($sectionIdentifiers) {
		$names = [ ];
		foreach ( $sectionIdentifiers as $sectionIdentifier ) {
			$names [] = self::getSectionScoreVariableName ( $sectionIdentifier );
		}
		return $names;
	}
	public static function getSectionNbItemsPresentedVariableNames($sectionIdentifiers) {
		$names = [ ];
		foreach ( $sectionIdentifiers as $sectionIdentifier ) {
			$names [] = self::getSectionNbItemsPresentedVariableName ( $sectionIdentifier );
		}
		return $names;
	}
	public static function getSectionScoreVariableName($sectionIdentifier) {
		return $sectionIdentifier . self::TEST_VARIABLE_SECTION_SCORE_RADIX;
	}
	public static function getSectionNbItemsPresentedVariableName($sectionIdentifier) {
		return $sectionIdentifier . self::TEST_VARIABLE_SECTION_NB_ITEMS_PRESENTED;
	}
	protected static function addOutcomeProcessingForSum(\DOMElement $destination, $variableName, $identifiers) {
		$doc = $destination->ownerDocument;
		
		$setOutcomeValueNode = $doc->createElement ( 'setOutcomeValue' );
		$destination->appendChild ( $setOutcomeValueNode );
		$setOutcomeValueNode->setAttribute ( 'identifier', $variableName );
		
		$sumNode = $doc->createElement ( 'sum' );
		$setOutcomeValueNode->appendChild ( $sumNode );
		
		foreach ( $identifiers as $identifier ) {
			$varNode = $doc->createElement ( 'variable' );
			$sumNode->appendChild ( $varNode );
			$varNode->setAttribute ( 'identifier', $identifier );
		}
	}
	protected static function addOutcomeProcessingsForSection(\DOMElement $destination, $sectionIdentifier) {
		$outcomeProcessing = $destination->getElementsByTagName ( 'outcomeProcessing' )->item ( 0 );
		
		// add section score
		$scoreVariable = self::getSectionScoreVariableName ( $sectionIdentifier );
		self::addOutcomeDeclaration ( $destination, $scoreVariable );
		$template = '<setOutcomeValue identifier="' . $scoreVariable . '">
                        <sum>
                        	<testVariables variableIdentifier="SCORE" baseType="float" sectionIdentifier="' . $sectionIdentifier . '" />
                        </sum>
	                </setOutcomeValue>';
		XmlHelper::addNodeFromXmlString ( $outcomeProcessing, $template );
		
		// add number of items presented in section
		$nbItemsPresentedVariable = self::getSectionNbItemsPresentedVariableName ( $sectionIdentifier );
		self::addOutcomeDeclaration ( $destination, $nbItemsPresentedVariable );
		$template = '<setOutcomeValue identifier="' . $nbItemsPresentedVariable . '">
                		<numberPresented sectionIdentifier="' . $sectionIdentifier . '" />
                	</setOutcomeValue>';
		XmlHelper::addNodeFromXmlString ( $outcomeProcessing, $template );
	}
}