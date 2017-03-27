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

class TestRequest {
	/**
	 *
	 * @var string
	 */
	protected $language = null;
	/**
	 *
	 * @var int
	 */
	protected $maxNumberOfItems = null;
	/**
	 *
	 * @var string
	 */
	protected $testName = null;
	/**
	 *
	 * @var ItemType[]
	 */
	protected $itemTypes = null;
	/**
	 *
	 * @var string[]
	 */
	protected $unicity = null;
	protected function __construct($language, $testName, $itemTypes, $maxNumberOfItems, $unicity) {
		$this->itemTypes = $itemTypes;
		$this->maxNumberOfItems = intval ( $maxNumberOfItems );
		$this->unicity = $unicity;
		$this->testName = $testName;
		$this->language = $language;
	}
	protected static function checkRequiredData($language, $testName, $itemTypes, $maxNumberOfItems) {
		if ($testName === null) {
			throw new \common_Exception ( 'invalid format: no testName' );
		}
		if (empty ( $itemTypes )) {
			throw new \common_Exception ( 'invalid format: no itemTypes' );
		}
		if (! is_numeric ( $maxNumberOfItems )) {
			throw new \common_Exception ( 'invalid format: maxNumberOfItems not numeric' );
		}
		if (empty ( $language )) {
			throw new \common_Exception ( 'invalid format: no language' );
		}
		if (! LanguageHelper::checkDataLanguage ( $language )) {
			throw new \common_Exception ( 'invalid format: bad language format' );
		}
	}
	/**
	 *
	 * @param string $jsonString
	 *        	JSON
	 * @see restTestGeneration.json file
	 * @throws \common_Exception
	 * @return TestRequest
	 */
	public static function fromJson($jsonString) {
		$jsonObj = json_decode ( $jsonString, true );
		if (! is_array ( $jsonObj )) {
			throw new \common_Exception ( 'not a json' );
		}
		$elName = 'testRequest';
		if (! array_key_exists ( $elName, $jsonObj )) {
			throw new \common_Exception ( 'invalid format: missing: ' . $elName );
		}
		$request = $jsonObj [$elName];
		
		$maxNumberOfItems = null;
		$elName = 'maxNumberOfItems';
		if (array_key_exists ( $elName, $request )) {
			$maxNumberOfItems = $request [$elName];
		}
		
		$testName = null;
		$elName = 'testName';
		if (array_key_exists ( $elName, $request )) {
			$testName = $request [$elName];
		}
		
		$language = null;
		if (array_key_exists ( 'language', $request )) {
			$language = $request ['language'];
		}
		
		$elName = 'itemType';
		if (! array_key_exists ( $elName, $request )) {
			throw new \common_Exception ( 'invalid format: missing: ' . $elName );
		}
		$jsonItemTypes = $request [$elName];
		$itemTypes = array ();
		foreach ( $jsonItemTypes as $itemType ) {
			array_push ( $itemTypes, ItemType::fromArray ( $itemType ) );
		}
		
		$elName = 'unicity';
		$unicity = array ();
		if (array_key_exists ( $elName, $request )) {
			$jsonUnicity = $request [$elName];
			foreach ( $jsonUnicity as $oneUnicity ) {
				array_push ( $unicity, $oneUnicity );
			}
		}
		
		self::checkRequiredData ( $language, $testName, $itemTypes, $maxNumberOfItems );
		
		return new TestRequest ( $language, $testName, $itemTypes, $maxNumberOfItems, $unicity );
	}
	/**
	 * FIXME TODO use simplexml_load_string() and json_encode(, true)
	 *
	 * @param string $xmlString
	 *        	XML @see restItemGeneration.xml
	 *        	
	 * @throws \common_Exception
	 * @return TestRequest
	 */
	public static function fromXml($xmlString) {
		$xmlObj = new \SimpleXMLElement ( $xmlString );
		if ($xmlObj->getName () !== 'testRequest') {
			throw new \common_Exception ( 'invalid xml' );
		}
		$maxNumberOfItems = null;
		$testName = null;
		$language = null;
		foreach ( $xmlObj->attributes () as $attr ) {
			switch ($attr->getName ()) {
				case 'maxNumberOfItems' :
					$maxNumberOfItems = $attr->__toString ();
					break;
				case 'testName' :
					$testName = $attr->__toString ();
					break;
				case 'language' :
					$language = $attr->__toString ();
					break;
				default :
					\common_Logger::t ( __METHOD__ . ' unsupported attribute: ' . $attr->getName () );
					break;
			}
		}
		$itemTypes = array ();
		$unicity = array ();
		foreach ( $xmlObj as $child ) {
			switch ($child->getName ()) {
				case 'itemType' :
					array_push ( $itemTypes, ItemType::fromSimpleXMLElement ( $child ) );
					break;
				case 'unicity' :
					array_push ( $unicity, $child->__toString () );
					break;
				default :
					\common_Logger::t ( __METHOD__ . ' unsupported child: ' . $child->getName () );
					break;
			}
		}
		
		self::checkRequiredData ( $language, $testName, $itemTypes, $maxNumberOfItems );
		
		return new TestRequest ( $language, $testName, $itemTypes, $maxNumberOfItems, $unicity );
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
	 * @return multitype:ItemType
	 */
	public function getItemTypes() {
		return $this->itemTypes;
	}
	/**
	 *
	 * @return string[]
	 */
	public function getUnicity() {
		return $this->unicity;
	}
	/**
	 *
	 * @return string
	 */
	public function getTestName() {
		return $this->testName;
	}
	/**
	 *
	 * @return number
	 */
	public function getMaxNumberOfItems() {
		return $this->maxNumberOfItems;
	}
}