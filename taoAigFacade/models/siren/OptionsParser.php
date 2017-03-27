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
 * Helper to parse SIREN options
 *
 * @deprecated Not used
 *            
 * @author Olivier Pedretti
 */
namespace itis\taoAigFacade\models\siren;

use itis\taoAigFacade\models\exceptions\ParseException;

class OptionsParser {
	/**
	 *
	 * @param string $xmlString        	
	 * @return string[]
	 */
	public static function getDistractorsFromString($xmlString = '') {
		$xml = new \SimpleXMLElement ( $xmlString );
		return self::getDistractors ( $xml );
	}
	/**
	 *
	 * @param string $xmlString
	 *        	XML
	 * @return \SimpleXMLElement
	 */
	public static function parseXmlFromString($xmlString = '') {
		return new \SimpleXMLElement ( $xmlString );
	}
	/**
	 *
	 * @param \SimpleXMLElement $xml        	
	 * @throws ParseException
	 * @return string[]
	 */
	public static function getDistractors(\SimpleXMLElement $xml) {
		if ($xml->getName () !== 'options') {
			throw new ParseException ( 'unsupported xml format' );
		}
		
		$distratorsNode = self::getNode ( $xml, 'distractors' );
		$distratorNodes = self::getNode ( $distratorsNode, 'distractor' );
		
		$distrators = array ();
		foreach ( $distratorNodes as $distractorNode ) {
			$variableNode = self::getNode ( $distractorNode, 'variable' );
			$labelNode = self::getNode ( $variableNode, 'label' );
			array_push ( $distrators, $labelNode->__toString () );
		}
		return $distrators;
	}
	/**
	 *
	 * @param \SimpleXMLElement $xml        	
	 * @param string $name        	
	 * @throws ParseException
	 * @return \SimpleXMLElement
	 */
	private static function getNode(\SimpleXMLElement $xml, $name) {
		if (! property_exists ( $xml, $name )) {
			throw new ParseException ( 'variable not found for:' . $name );
		}
		return $xml->$name;
	}
}