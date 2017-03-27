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
 * Represents the user request
 *
 * @author Olivier Pedretti
 */
namespace itis\taoAigFacade\models\request;

class DeliveryRequest {
	const DEFAULT_LANGUAGE = 'fr-FR';
	/**
	 *
	 * @var string
	 */
	protected $itemTemplate = null;
	/**
	 *
	 * @var string
	 */
	protected $itemCategory = null;
	protected $itemsPerConcept = 1;
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
	 * @var User
	 */
	protected $user = null;
	/**
	 *
	 * @var string
	 */
	protected $parentClass = null;
	/**
	 *
	 * @param Word[] $words        	
	 * @param User $user        	
	 * @param string $language        	
	 * @param string $referrer        	
	 * @param string $parentClass        	
	 * @param string $itemTemplate        	
	 * @param string $itemCategory        	
	 * @param string $itemsPerConcept        	
	 */
	protected function __construct($words = array(), User $user = null, $language = self::DEFAULT_LANGUAGE, $referrer = null, $parentClass = null, $itemsPerConcept = 1, $itemTemplate, $itemCategory) {
		$this->words = $words;
		$this->language = $language;
		$this->referrer = $referrer;
		$this->user = $user;
		$this->parentClass = $parentClass;
		$this->itemsPerConcept = $itemsPerConcept;
		$this->itemTemplate = $itemTemplate;
		$this->itemCategory = $itemCategory;
	}
	/**
	 *
	 * @param string $jsonString
	 *        	JSON
	 * @see restTest.json file
	 * @throws \common_Exception
	 * @return DeliveryRequest
	 */
	public static function fromJson($jsonString) {
		$jsonObj = json_decode ( $jsonString, true );
		if (! is_array ( $jsonObj )) {
			throw new \common_Exception ( 'not a json' );
		}
		$elName = 'deliveryRequest';
		if (! array_key_exists ( $elName, $jsonObj )) {
			throw new \common_Exception ( 'invalid format: missing: ' . $elName );
		}
		$request = $jsonObj [$elName];
		
		$itemTemplate = null;
		if (array_key_exists ( 'itemTemplate', $request )) {
			$itemTemplate = $request ['itemTemplate'];
		}
		$itemCategory = null;
		if (array_key_exists ( 'itemCategory', $request )) {
			$itemCategory = $request ['itemCategory'];
		}
		$itemsPerConcept = null;
		if (array_key_exists ( 'itemsPerConcept', $request )) {
			$itemsPerConcept = $request ['itemsPerConcept'];
		}
		$language = null;
		if (array_key_exists ( 'language', $request )) {
			$language = $request ['language'];
		}
		$referrer = null;
		if (array_key_exists ( 'referrer', $request )) {
			$referrer = $request ['referrer'];
		}
		$parentClass = null;
		if (array_key_exists ( 'parentClass', $request )) {
			$parentClass = $request ['parentClass'];
		}
		$user = null;
		if (array_key_exists ( 'user', $request )) {
			$jsonUser = $request ['user'];
			$user = User::fromArray ( $jsonUser );
		}
		
		if (! array_key_exists ( 'words', $request )) {
			throw new \common_Exception ( 'invalid format: missing: words' );
		}
		$jsonWords = $request ['words'];
		$words = array ();
		foreach ( $jsonWords as $word ) {
			array_push ( $words, Word::fromArray ( $word ) );
		}
		if (empty ( $words )) {
			throw new \common_Exception ( 'invalid json format: 0 word' );
		}
		return new DeliveryRequest ( $words, $user, $language, $referrer, $parentClass, $itemsPerConcept, $itemTemplate, $itemCategory );
	}
	/**
	 *
	 * @param string $xmlString
	 *        	XML @see restTest.xml
	 *        	
	 * @throws \common_Exception
	 * @return DeliveryRequest
	 */
	public static function fromXml($xmlString) {
		$xmlObj = new \SimpleXMLElement ( $xmlString );
		if ($xmlObj->getName () !== 'deliveryRequest') {
			throw new \common_Exception ( 'invalid xml' );
		}
		$itemTemplate = null;
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
		$user = null;
		foreach ( $xmlObj as $child ) {
			switch ($child->getName ()) {
				case 'word' :
					array_push ( $words, Word::fromSimpleXMLElement ( $child ) );
					break;
				case 'user' :
					$user = User::fromSimpleXMLElement ( $child );
					break;
				default :
					\common_Logger::t ( __METHOD__ . ' unsupported child: ' . $child->getName () );
					break;
			}
		}
		if (empty ( $words )) {
			throw new \common_Exception ( 'invalid format: no word' );
		}
		return new DeliveryRequest ( $words, $user, $language, $referrer, $parentClass, $itemsPerConcept, $itemTemplate, $itemCategory );
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
	 * @return User
	 */
	public function getUser() {
		return $this->user;
	}
	public function getItemsPerConcept() {
		return $this->itemsPerConcept;
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