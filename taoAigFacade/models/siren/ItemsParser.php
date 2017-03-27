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
 * Helper to parse item list returned by SIREN
 *
 * @author Olivier Pedretti
 */
namespace itis\taoAigFacade\models\siren;

use itis\taoAigFacade\models\exceptions\ParseException;

class ItemsParser {
	const XML_HEADER = '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>';
	/**
	 *
	 * @param string $xmlString
	 *        	XML
	 * @return string Item in XML format
	 */
	public static function getAnItemFromString($xmlString = '') {
		$xml = new \SimpleXMLElement ( $xmlString );
		return self::getAnItem ( $xml );
	}
	/**
	 *
	 * @param string $xmlString        	
	 * @param int $numberOfItems        	
	 * @return \SimpleXMLElement[]
	 */
	public static function getNFirstItemsFromString($xmlString = '', $numberOfItems) {
		$xml = new \SimpleXMLElement ( $xmlString );
		return self::getNFirstItems ( $xml, $numberOfItems );
	}
	/**
	 *
	 * @param \SimpleXMLElement $xml        	
	 * @param int $numberOfItems        	
	 * @return \SimpleXMLElement[]
	 * @throws ParseException
	 */
	public static function getNFirstItems(\SimpleXMLElement $xml, $numberOfItems) {
		if ($xml->getName () !== 'layers') {
			throw new ParseException ( 'unsupported xml format' );
		}
		$layerNodes = self::getNode ( $xml, 'layer' );
		
		$layers = [ ];
		foreach ( $layerNodes as $layerNode ) {
			$layers [] = $layerNode;
		}
		return array_slice ( $layers, 0, $numberOfItems );
	}
	public static function simpleXmlToXmlString(\SimpleXMLElement $xml) {
		return self::XML_HEADER . $xml->asXML ();
	}
	/**
	 *
	 * @param string $xmlString        	
	 * @return \SimpleXMLElement
	 */
	public static function parseXmlFromString($xmlString) {
		return new \SimpleXMLElement ( $xmlString );
	}
	/**
	 *
	 * @param \SimpleXMLElement $xml        	
	 * @throws ParseException
	 * @return \SimpleXMLElement
	 */
	public static function getItem(\SimpleXMLElement $xml) {
		if ($xml->getName () !== 'layer') {
			throw new ParseException ( __ ( 'unsupported xml format' ) );
		}
		$assessmentItem = self::getNode ( $xml, 'assessmentItem' );
		return $assessmentItem [0];
	}
	/**
	 * Get a random item
	 *
	 * @param \SimpleXMLElement $xml        	
	 * @throws ParseException
	 * @return string XML
	 */
	public static function getAnItem(\SimpleXMLElement $xml) {
		if ($xml->getName () !== 'items') {
			throw new ParseException ( __ ( 'unsupported xml format' ) );
		}
		
		$itemNodes = self::getNode ( $xml, 'assessmentItem' );
		
		$items = array ();
		foreach ( $itemNodes as $itemNode ) {
			array_push ( $items, $itemNode->asXML () );
		}
		$randomItem = self::XML_HEADER . $items [array_rand ( $items )];
		
		return $randomItem;
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
			throw new ParseException ( __ ( 'variable not found for: ' ) . $name );
		}
		return $xml->$name;
	}
}