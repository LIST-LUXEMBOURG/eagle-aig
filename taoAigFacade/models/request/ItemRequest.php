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
 * Represents the item request
 *
 * @author Olivier Pedretti
 */
namespace itis\taoAigFacade\models\request;

use itis\taoAigFacade\helpers\LanguageHelper;

class ItemRequest {
	const DEFAULT_LANGUAGE = 'fr-FR';
	/**
	 *
	 * @var string
	 */
	protected $itemTemplate = null;
	/**
	 *
	 * @var int
	 */
	protected $conceptsPerItem = null;
	/**
	 *
	 * @var string
	 */
	protected $itemCategory = null;
	/**
	 *
	 * @var int
	 */
	protected $itemsPerConcept = null;
	/**
	 *
	 * @var string
	 */
	protected $language = null;
	/**
	 *
	 * @var string
	 */
	protected $referrer = null;
	/**
	 *
	 * @var Word[]
	 */
	protected $words = null;
	/**
	 *
	 * @var string
	 */
	protected $parentClass = null;
	/**
	 *
	 * @var bool
	 */
	protected $analyseFeatures = false;
	/**
	 *
	 * @param Word[] $words        	
	 * @param string $language        	
	 * @param string $referrer        	
	 * @param string $parentClass        	
	 * @param string $itemTemplate        	
	 * @param string $itemCategory        	
	 * @param string $itemsPerConcept        	
	 * @param bool $analyseFeatures        	
	 */
	protected function __construct($words, $language, $referrer, $parentClass, $itemsPerConcept, $itemTemplate, $itemCategory, $conceptsPerItem, $analyseFeatures) {
		$this->words = $words;
		$this->language = $language;
		$this->referrer = $referrer;
		$this->parentClass = $parentClass;
		$this->itemsPerConcept = intval ( $itemsPerConcept );
		$this->itemTemplate = $itemTemplate;
		$this->itemCategory = $itemCategory;
		$this->conceptsPerItem = intval ( $conceptsPerItem );
		$this->analyseFeatures = boolval ( $analyseFeatures );
	}
	/**
	 *
	 * @param string $jsonString
	 *        	JSON
	 * @see restItemGeneration.json file
	 * @throws \common_Exception
	 * @return ItemRequest
	 */
	public static function fromJson($jsonString) {
		$jsonObj = json_decode ( $jsonString, true );
		if (! is_array ( $jsonObj )) {
			throw new \common_Exception ( 'not a json' );
		}
		$elName = 'itemRequest';
		if (! array_key_exists ( $elName, $jsonObj )) {
			throw new \common_Exception ( 'invalid format: missing: ' . $elName );
		}
		$request = $jsonObj [$elName];
		
		$itemTemplate = self::getKeyIfExists ( 'itemTemplate', $request );
		$conceptsPerItem = self::getKeyIfExists ( 'conceptsPerItem', $request );
		$itemCategory = self::getKeyIfExists ( 'itemCategory', $request );
		$analyseFeatures = self::getKeyIfExists ( 'featureAnalysis', $request );
		$itemsPerConcept = self::getKeyIfExists ( 'itemsPerConcept', $request );
		$language = self::getKeyIfExists ( 'language', $request );
		$referrer = self::getKeyIfExists ( 'referrer', $request );
		$parentClass = self::getKeyIfExists ( 'parentClass', $request );
		
		if (! array_key_exists ( 'word', $request )) {
			throw new \common_Exception ( 'invalid format: missing: word' );
		}
		$jsonWords = $request ['word'];
		$words = array ();
		foreach ( $jsonWords as $word ) {
			array_push ( $words, Word::fromArray ( $word ) );
		}
		
		self::checkRequiredData ( $itemTemplate, $words, $language, $itemsPerConcept, $itemCategory, $conceptsPerItem );
		
		return new ItemRequest ( $words, $language, $referrer, $parentClass, $itemsPerConcept, $itemTemplate, $itemCategory, $conceptsPerItem, $analyseFeatures );
	}
	protected static function getKeyIfExists($key, $array, $default = null) {
		if (array_key_exists ( $key, $array )) {
			return $array [$key];
		}
		return $default;
	}
	/**
	 * FIXME TODO use simplexml_load_string() and json_encode(, true)
	 *
	 * @param string $xmlString
	 *        	XML @see restItemGeneration.xml
	 *        	
	 * @throws \common_Exception
	 * @return ItemRequest
	 */
	public static function fromXml($xmlString) {
		$xmlObj = new \SimpleXMLElement ( $xmlString );
		if ($xmlObj->getName () !== 'itemRequest') {
			throw new \common_Exception ( 'invalid xml' );
		}
		
		$itemTemplate = null;
		$conceptsPerItem = null;
		$analyseFeatures = null;
		$itemCategory = null;
		$itemsPerConcept = null;
		$language = null;
		$referrer = null;
		$parentClass = null;
		foreach ( $xmlObj->attributes () as $attr ) {
			switch ($attr->getName ()) {
				case 'itemTemplate' :
					$itemTemplate = $attr->__toString ();
					break;
				case 'conceptsPerItem' :
					$conceptsPerItem = $attr->__toString ();
					break;
				case 'featureAnalysis' :
					$analyseFeatures = $attr->__toString ();
					break;
				case 'itemCategory' :
					$itemCategory = $attr->__toString ();
					break;
				case 'itemsPerConcept' :
					$itemsPerConcept = $attr->__toString ();
					break;
				case 'language' :
					$language = $attr->__toString ();
					break;
				case 'referrer' :
					$referrer = $attr->__toString ();
					break;
				case 'parentClass' :
					$parentClass = $attr->__toString ();
					break;
				default :
					\common_Logger::t ( __METHOD__ . ' unsupported attribute: ' . $attr->getName () );
					break;
			}
		}
		$words = array ();
		foreach ( $xmlObj as $child ) {
			switch ($child->getName ()) {
				case 'word' :
					array_push ( $words, Word::fromSimpleXMLElement ( $child ) );
					break;
				default :
					\common_Logger::t ( __METHOD__ . ' unsupported child: ' . $child->getName () );
					break;
			}
		}
		
		self::checkRequiredData ( $itemTemplate, $words, $language, $itemsPerConcept, $itemCategory, $conceptsPerItem );
		
		return new ItemRequest ( $words, $language, $referrer, $parentClass, $itemsPerConcept, $itemTemplate, $itemCategory, $conceptsPerItem, $analyseFeatures );
	}
	protected static function checkRequiredData($itemTemplate, $words, $language, $itemsPerConcept, $itemCategory, $conceptsPerItem) {
		if (empty ( $itemTemplate )) {
			throw new \common_Exception ( 'invalid format: no itemTemplate' );
		}
		if (empty ( $words )) {
			throw new \common_Exception ( 'invalid format: 0 word' );
		}
		if (empty ( $language )) {
			throw new \common_Exception ( 'invalid format: no language' );
		}
		if (! LanguageHelper::checkDataLanguage ( $language )) {
			throw new \common_Exception ( 'invalid format: bad language format' );
		}
		if (! is_numeric ( $itemsPerConcept )) {
			throw new \common_Exception ( 'invalid format: itemsPerConcept not numeric' );
		}
		if (! is_numeric ( $conceptsPerItem )) {
			throw new \common_Exception ( 'invalid format: conceptsPerItem not numeric' );
		}
	}
	/**
	 *
	 * @return multitype:Word
	 */
	public function getWords() {
		return $this->words;
	}
	/**
	 *
	 * @return string
	 */
	public function getParentClass() {
		return $this->parentClass;
	}
	/**
	 *
	 * @return string
	 */
	public function getLanguage() {
		return $this->language;
	}
	/**
	 *
	 * @return number
	 */
	public function getItemsPerConcept() {
		return $this->itemsPerConcept;
	}
	/**
	 *
	 * @return number
	 */
	public function getConceptsPerItem() {
		return $this->conceptsPerItem;
	}
	/**
	 *
	 * @return bool
	 */
	public function hasToAnalyseFeatures() {
		return $this->analyseFeatures;
	}
	
	/**
	 *
	 * @return string
	 */
	public function getItemTemplate() {
		return $this->itemTemplate;
	}
	/**
	 *
	 * @return string
	 */
	public function getItemCategory() {
		return $this->itemCategory;
	}
}