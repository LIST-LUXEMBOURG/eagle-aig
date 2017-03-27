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
namespace itis\taoAigFacade\models;

use oat\taoQtiItem\model\qti\ImportService;
use oat\taoQtiItem\model\qti\interaction\ChoiceInteraction;
use oat\taoQtiItem\model\qti\Item;
use oat\taoQtiItem\model\qti\OutcomeDeclaration;
use oat\taoQtiItem\model\qti\response\Template;
use oat\taoQtiItem\model\qti\ResponseDeclaration;

/**
 *
 * @author pedretti
 *        
 */
class ItemBuilderService extends \taoItems_models_classes_ItemsService {
	public function __construct() {
		parent::__construct ();
	}
	/**
	 *
	 * @param array $items
	 *        	Items information
	 * @see taoAigItemBuilder_models_classes_ItemBuilderService::buildItem() For the item object format
	 * @throws \InvalidArgumentException If provided argument items is not of type 'array'
	 * @return \core_kernel_classes_Resource[] List of items
	 */
	public function buildItems($items = array()) {
		if (! is_array ( $items )) {
			throw new \InvalidArgumentException ( 'not an array' );
		}
		$itemsAsRscs = array ();
		foreach ( $items as $i ) {
			$itemAsRsc = $this->buildItem ( $i );
			array_push ( $itemsAsRscs, $itemAsRsc );
		}
		return $itemsAsRscs;
	}
	/**
	 *
	 * @param array $item
	 *        	{
	 *        	item informations
	 *        	@info string $title
	 *        	@info string $prompt The item prompt
	 *        	@info array[string] $choices List of distractors
	 *        	@info string $correct The correct response in choices
	 *        	}
	 * @throws \InvalidArgumentException
	 * @return \core_kernel_classes_Resource The resource that represents the generated item
	 */
	public function buildItem($item) {
		if (! is_array ( $item )) {
			throw new \InvalidArgumentException ( 'not an array' );
		}
		return $this->createItem ( $item );
	}
	/**
	 * Return a class, create one if doesn't exist
	 *
	 * @param string $destinationClass
	 *        	{
	 *        	@type null For root class
	 *        	@type string The uri of the class to which the item will be attach
	 *        	@type string If not an uri, the name of a class; will create a class if not already exists
	 *        	}
	 * @param \core_kernel_classes_Class $rootClass        	
	 * @return \core_kernel_classes_Class
	 */
	public static function defineDestinationClass($destinationClass = '', \core_kernel_classes_Class $rootClass) {
		if (\common_Utils::isUri ( $destinationClass )) {
			\common_Logger::t ( __METHOD__ . ' it is a class' );
			$destinationClass = new \core_kernel_classes_Class ( $destinationClass );
		} else if (! empty ( $destinationClass )) {
			$classes = $rootClass->getSubClasses ( false );
			$found = false;
			foreach ( $classes as $class ) {
				if (strcmp ( $class->getLabel (), $destinationClass ) == 0) {
					$destinationClass = $class;
					$found = true;
					break;
				}
			}
			if (! $found) {
				$destinationClass = $rootClass->createSubClass ( $destinationClass );
			}
		}
		if (empty ( $destinationClass )) {
			\common_Logger::t ( __METHOD__ . ' use root class' );
			$destinationClass = $rootClass;
		}
		return $destinationClass;
	}
	/**
	 *
	 * Import a QTI XML item in TAO (in DB for metadata and in FS for QTI XML file)
	 *
	 * @param string $qtiItem
	 *        	The item in QTI XML format
	 * @param mixed $parentClass
	 *        	{
	 *        	@type null Generated item will be attach to the Item root class
	 *        	@type string The uri of the class to which the item will be attach
	 *        	@type string If not an uri, the name of a class to which the item will be attach; will create a class if not already exists
	 *        	}
	 * @return \core_kernel_classes_Resource The resource that represents the generated item
	 */
	public function importQtiItem($qtiItem, $parentClass = '') {
		\common_Logger::t ( __METHOD__ . ' importing item' );
		\common_Logger::t ( __METHOD__ . 'ITEM: '. $qtiItem);
		$parentClass = self::defineDestinationClass ( $parentClass, $this->getRootClass () );
		$report = ImportService::singleton ()->importQTIFile ( $qtiItem, $parentClass, true );
		$itemRsc = $this->parseReport ( $report );
		\common_Logger::t ( __METHOD__ . ' item imported successfully' );
		return $itemRsc;
	}
	/**
	 * Create item in TAO (in DB for metadata and in FS for xml qti file)
	 *
	 * @param array $item
	 *        	{
	 *        	item informations
	 *        	@info string $title
	 *        	@info string $prompt The item prompt
	 *        	@info array[string] $choices List of distractors
	 *        	@info string $correct The correct response in choices
	 *        	}
	 * @return \core_kernel_classes_Resource The resource that represents the generated item
	 */
	protected function createItem($item) {
		$title = $item ['title'];
		$choices = $item ['choices'];
		$correct = $item ['correct'];
		$prompt = $item ['prompt'];
		\common_Logger::t ( __METHOD__ . ' ' . $title );
		$itemAttrs = array (
				'title' => $title 
		);
		// 'xml:lang' =>'en-US'
		
		$interactionAttrs = array (
				'shuffle' => true,
				'orientation' => 'vertical' 
		);
		$choiceAttrs = array (
				'fixed' => false,
				'showHide' => 'show' 
		);
		$responseDeclAttrs = array (
				'cardinality' => 'single',
				'baseType' => 'identifier' 
		);
		
		// $body = 'The body of the item';
		
		$myItem = new Item ( $itemAttrs );
		// $myItem->getBody()->edit($body);
		
		$myInteraction = new ChoiceInteraction ( $interactionAttrs );
		$myInteraction->getPrompt ()->edit ( $prompt );
		
		$correctResponse = null;
		foreach ( $choices as $choice ) {
			$aChoice = $myInteraction->createChoice ( $choiceAttrs, $choice );
			if (strcmp ( $correct, $choice ) == 0) {
				$correctResponse = $aChoice;
			}
		}
		
		$myItem->addInteraction ( $myInteraction, "{$myInteraction->getPlaceholder()}" );
		
		// set response
		$myResponse = new ResponseDeclaration ( $responseDeclAttrs );
		// $correctResponse = $myChoice2;
		$myResponse->setCorrectResponses ( $correctResponse->getIdentifier () );
		$myItem->addResponse ( $myResponse );
		
		// fix exception: force identifier generation
		$myResponse->getIdentifier ();
		
		// bind response to interaction
		$myInteraction->setResponse ( $myResponse );
		
		$template = new Template ( Template::MATCH_CORRECT );
		$myItem->setResponseProcessing ( $template );
		
		$outcomesAttrs = array (
				'identifier' => 'SCORE',
				'baseType' => 'float',
				'cardinality' => 'single' 
		);
		
		$outcomes = new OutcomeDeclaration ( $outcomesAttrs );
		$myItem->setOutcomes ( array (
				$outcomes 
		) );
		
		\common_Logger::t ( __METHOD__ . ' importing item' );
		$report = ImportService::singleton ()->importQTIFile ( $myItem->toXML (), $this->getRootClass (), true );
		$itemRsc = $this->parseReport ( $report );
		\common_Logger::t ( __METHOD__ . ' item imported successfully' );
		return $itemRsc;
	}
	/**
	 * Get Data of a TAO report
	 *
	 * @param \common_report_Report $report        	
	 * @throws \common_exception_Error if the report contains errors
	 * @return mixed The content of the report
	 */
	protected function parseReport(\common_report_Report $report) {
		if ($report->containsError ()) {
			throw new \common_exception_Error ( $report->getMessage () );
		}
		return $report->getData ();
	}
}