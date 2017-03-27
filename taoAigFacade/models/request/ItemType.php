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

class ItemType {
	/**
	 *
	 * @var string
	 */
	protected $dataSetFactor = null;
	/**
	 *
	 * @var string
	 */
	protected $type = null;
	/**
	 *
	 * @var string
	 */
	protected $value = null;
	/**
	 *
	 * @var string
	 */
	protected $amount = null;
	/**
	 *
	 * @var ItemCriteria[]
	 */
	protected $criteria = null;
	protected function __construct($type, $value, $amount, $criteria, $dataSetFactor) {
		$this->type = $type;
		$this->value = $value;
		$this->amount = $amount;
		$this->criteria = $criteria;
		$this->dataSetFactor = ($dataSetFactor !== null) ? $dataSetFactor : 1;
	}
	public static function fromArray($array) {
		if (! is_array ( $array )) {
			throw new \common_Exception ( 'not a json' );
		}
		
		$type = null;
		$elName = 'type';
		if (array_key_exists ( $elName, $array )) {
			$type = $array [$elName];
		}
		
		$value = null;
		$elName = 'value';
		if (array_key_exists ( $elName, $array )) {
			$value = $array [$elName];
		}
		
		$amount = null;
		$elName = 'amount';
		if (array_key_exists ( $elName, $array )) {
			$amount = $array [$elName];
		}
		
		$dataSetFactor = null;
		$elName = 'dataSetFactor';
		if (array_key_exists ( $elName, $array )) {
			$dataSetFactor = $array [$elName];
		}
		
		$criteria = array ();
		$elName = 'criteria';
		if (array_key_exists ( $elName, $array )) {
			$jsonCriteria = $array [$elName];
			foreach ( $jsonCriteria as $oneCriteria ) {
				array_push ( $criteria, ItemCriteria::fromArray ( $oneCriteria ) );
			}
		}
		
		self::checkRequiredData ( $type, $value, $amount, $dataSetFactor );
		
		return new ItemType ( $type, $value, $amount, $criteria, $dataSetFactor );
	}
	protected static function checkRequiredData($type, $value, $amount, $dataSetFactor) {
		if ($type === null) {
			throw new \common_exception_NotAcceptable ( 'invalid format: no type' );
		}
		if (! (new \core_kernel_classes_Property ( $type ))->isProperty ()) {
			throw new \common_exception_NotAcceptable ( 'not an item property: ' . $type );
		}
		if ($value === null) {
			throw new \common_exception_NotAcceptable ( 'invalid format: no value' );
		}
		if ($amount === null) {
			throw new \common_exception_NotAcceptable ( 'invalid format: no amount' );
		}
		if ($dataSetFactor !== null && ! is_numeric ( $dataSetFactor )) {
			throw new \common_exception_NotAcceptable ( 'invalid format: dataSetFactor not numeric' );
		}
	}
	/**
	 * Helper that generate 'ItemType' from XML
	 *
	 * @param \SimpleXMLElement $xmlEl
	 *        	That represents an itemType
	 * @throws \common_Exception
	 * @return ItemType
	 */
	public static function fromSimpleXMLElement(\SimpleXMLElement $xmlEl) {
		if (! ($xmlEl instanceof \SimpleXMLElement)) {
			throw new \common_Exception ( 'not instanceof ' . \SimpleXMLElement::class );
		}
		if ($xmlEl->getName () !== 'itemType') {
			throw new \common_Exception ( 'invalid xml' );
		}
		$type = null;
		$value = null;
		$amount = null;
		$dataSetFactor = null;
		foreach ( $xmlEl->attributes () as $attr ) {
			switch ($attr->getName ()) {
				case 'type' :
					$type = $attr->__toString ();
					break;
				case 'value' :
					$value = $attr->__toString ();
					break;
				case 'amount' :
					$amount = $attr->__toString ();
					break;
				case 'dataSetFactor' :
					$dataSetFactor = $attr->__toString ();
					break;
				default :
					\common_Logger::t ( __METHOD__ . ' unsupported attribute: ' . $attr->getName () );
					break;
			}
		}
		
		$criteria = array ();
		foreach ( $xmlEl as $child ) {
			switch ($child->getName ()) {
				case 'criteria' :
					array_push ( $criteria, ItemCriteria::fromSimpleXMLElement ( $child ) );
					break;
				default :
					\common_Logger::t ( __METHOD__ . ' unsupported child: ' . $child->getName () );
					break;
			}
		}
		
		self::checkRequiredData ( $type, $value, $amount, $dataSetFactor );
		
		return new ItemType ( $type, $value, $amount, $criteria, $dataSetFactor );
	}
	/**
	 *
	 * @return multitype:ItemCriteria
	 */
	public function getCriteria() {
		return $this->criteria;
	}
	/**
	 *
	 * @return string
	 */
	public function getType() {
		return $this->type;
	}
	/**
	 *
	 * @return string
	 */
	public function getValue() {
		return $this->value;
	}
	/**
	 *
	 * @return string
	 */
	public function getAmount() {
		return $this->amount;
	}
	/**
	 *
	 * @return string
	 */
	public function getDataSetFactor() {
		return $this->dataSetFactor;
	}
}