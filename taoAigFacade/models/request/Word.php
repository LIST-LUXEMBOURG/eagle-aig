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
 * Word element of the request
 *
 * @author Olivier Pedretti
 *        
 */
namespace itis\taoAigFacade\models\request;

class Word {
	/**
	 *
	 * @var string
	 */
	protected $conceptURI = null;
	/**
	 *
	 * @var string
	 */
	protected $DBpediaURI = null;
	/**
	 *
	 * @var string
	 */
	protected $learningRsc = null;
	protected function __construct($conceptURI, $learningRsc, $DBpediaURI) {
		$this->conceptURI = $conceptURI;
		$this->learningRsc = $learningRsc;
		$this->DBpediaURI = $DBpediaURI;
		if (! empty ( $this->DBpediaURI )) {
			\common_Logger::t ( 'found DBpediaURI: ' . $this->DBpediaURI );
		}
		if (! empty ( $this->learningRsc )) {
			\common_Logger::t ( 'found learningRsc: ' . $this->learningRsc );
		}
	}
	/**
	 * Helper that generate 'Word' from XML
	 *
	 * @param \SimpleXMLElement $xmlEl
	 *        	That represents a word
	 * @throws \common_Exception
	 * @return Word
	 */
	public static function fromSimpleXMLElement(\SimpleXMLElement $xmlEl) {
		if (! ($xmlEl instanceof \SimpleXMLElement)) {
			throw new \common_Exception ( 'not instanceof ' . \SimpleXMLElement::class );
		}
		if ($xmlEl->getName () !== 'word') {
			throw new \common_Exception ( 'invalid xml' );
		}
		$conceptURI = null;
		$learningRsc = null;
		$DBpediaURI = null;
		foreach ( $xmlEl->attributes () as $attr ) {
			switch ($attr->getName ()) {
				case 'uri' :
					$conceptURI = $attr->__toString ();
					break;
				case 'learningRsc' :
					$learningRsc = $attr->__toString ();
					break;
				case 'DBpediaURI' :
					$DBpediaURI = $attr->__toString ();
					break;
				default :
					\common_Logger::t ( __METHOD__ . ' unsupported attribute: ' . $attr->getName () );
					break;
			}
		}
		
		self::checkRequiredData ( $conceptURI );
		
		return new Word ( $conceptURI, $learningRsc, $DBpediaURI );
	}
	protected static function checkRequiredData($conceptURI) {
		if ($conceptURI === null) {
			throw new \common_Exception ( 'invalid format: no conceptURI' );
		}
	}
	/**
	 * Generate 'Word' from an array of parameters
	 *
	 * @param array $array
	 *        	{
	 *        	word information
	 *        	@info string $label
	 *        	@info string $uri
	 *        	@info string $learningRsc optional
	 *        	@info string $DBpediaURI optional
	 *        	}
	 * @throws \common_Exception
	 * @return Word
	 */
	public static function fromArray($array) {
		if (! is_array ( $array )) {
			throw new \common_Exception ( 'not an array' );
		}
		if (array_key_exists ( 'uri', $array )) {
			$conceptURI = $array ['uri'];
		}
		
		$learningRsc = array_key_exists ( 'learningRsc', $array ) ? $array ['learningRsc'] : null;
		$DBpediaURI = array_key_exists ( 'DBpediaURI', $array ) ? $array ['DBpediaURI'] : null;
		
		self::checkRequiredData ( $conceptURI );
		
		return new Word ( $conceptURI, $learningRsc, $DBpediaURI );
	}
	/**
	 *
	 * @return string
	 */
	public function getConceptURI() {
		return $this->conceptURI;
	}
	/**
	 *
	 * @return string
	 */
	public function getDBpediaURI() {
		return $this->DBpediaURI;
	}
	/**
	 *
	 * @return string
	 */
	public function getLearningRsc() {
		return $this->learningRsc;
	}
}
