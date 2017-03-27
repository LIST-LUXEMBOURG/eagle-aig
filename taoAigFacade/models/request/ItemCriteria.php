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
 * Represents the an item criteria
 *
 * @author Olivier Pedretti
 */
namespace itis\taoAigFacade\models\request;

class ItemCriteria {
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
	protected $operator = null;
	protected function __construct($type, $value, $operator) {
		$this->type = $type;
		$this->value = $value;
		$this->operator = $operator;
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
		
		$operator = null;
		$elName = 'operator';
		if (array_key_exists ( $elName, $array )) {
			$operator = $array [$elName];
		}
		
		self::checkRequiredData ( $type, $value, $operator );
		
		return new ItemCriteria ( $type, $value, $operator );
	}
	protected static function checkRequiredData($type, $value, $operator) {
		if ($type === null) {
			throw new \common_Exception ( 'invalid format: no type' );
		}
		if ($value === null) {
			throw new \common_Exception ( 'invalid format: no value' );
		}
		if ($operator === null) {
			throw new \common_Exception ( 'invalid format: no operator' );
		}
	}
	/**
	 * Helper that generate 'ItemCriteria' from XML
	 *
	 * @param \SimpleXMLElement $xmlEl
	 *        	That represents an itemType
	 * @throws \common_Exception
	 * @return ItemCriteria
	 */
	public static function fromSimpleXMLElement(\SimpleXMLElement $xmlEl) {
		if (! ($xmlEl instanceof \SimpleXMLElement)) {
			throw new \common_Exception ( 'not instanceof ' . \SimpleXMLElement::class );
		}
		if ($xmlEl->getName () !== 'criteria') {
			throw new \common_Exception ( 'invalid xml' );
		}
		$type = null;
		$value = null;
		$operator = null;
		foreach ( $xmlEl->attributes () as $attr ) {
			switch ($attr->getName ()) {
				case 'type' :
					$type = $attr->__toString ();
					break;
				case 'value' :
					$value = $attr->__toString ();
					break;
				case 'operator' :
					$operator = $attr->__toString ();
					break;
				default :
					\common_Logger::t ( __METHOD__ . ' unsupported attribute: ' . $attr->getName () );
					break;
			}
		}
		self::checkRequiredData ( $type, $value, $operator );
		
		return new ItemCriteria ( $type, $value, $operator );
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
	public function getOperator() {
		return $this->operator;
	}
}