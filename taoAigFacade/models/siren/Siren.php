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
 * Helper to address SIREN
 *
 * @author Olivier Pedretti
 */
namespace itis\taoAigFacade\models\siren;

use itis\taoAigFacade\models\Concept;
use itis\taoAigFacade\models\FacadeService;
use itis\taoAigFacade\models\ItemToGenerate;

class Siren {
	
	/**
	 * Build the request URL for SIREN
	 *
	 * @param Concept[] $concepts        	
	 * @return string SIREN request URL
	 */
	public static function getRequestUrl($concepts) {
		$json = json_encode ( self::getInputParameter ( $concepts ) );
		$oneConcept = reset ( $concepts );
		$templateName = $oneConcept->getTemplate ();
		$input = rawurlencode ( $json );
		$requestUrl = 'http://' . TAO_AIG_FACADE_SIREN_HOST . ':' . TAO_AIG_FACADE_SIREN_PORT . '/SIREN/templates/mcq/generate?name=' . $templateName . '&input=' . $input;
		return $requestUrl;
	}
	
	/**
	 * Get the url part needed to address SIREN for a particular type of item
	 *
	 * @param ItemToGenerate $item        	
	 * @throws \common_exception_PreConditionFailure
	 * @return string
	 */
	protected static function getItemTypeForRequest(ItemToGenerate $item) {
		$itemType = $item->getType ();
		switch ($itemType) {
			case FacadeService::ITEM_TYPE_VOC_TR_MCQ :
			// fall through
			case FacadeService::ITEM_TYPE_VOC_DEF_MCQ :
				return 'mcq';
			case FacadeService::ITEM_TYPE_VOC_TR_MATCH :
				return 'match';
			default :
				throw new \common_exception_PreConditionFailure ( __METHOD__ . ' not a supported type: ' . $itemType );
		}
	}
	/**
	 *
	 * @param ItemToGenerate $item        	
	 * @throws \common_exception_PreConditionFailure
	 */
	protected static function getAppropriateConceptUri(ItemToGenerate $item) {
		$itemType = $item->getType ();
		$word = $item->getWord ();
		switch ($itemType) {
			case FacadeService::ITEM_TYPE_VOC_TR_MCQ :
			// fall through
			case FacadeService::ITEM_TYPE_VOC_TR_MATCH :
				return $word->getConceptURI ();
			case FacadeService::ITEM_TYPE_VOC_DEF_MCQ :
				return $word->getConceptURI ();
			default :
				throw new \common_exception_PreConditionFailure ( __METHOD__ . ' not a supported type: ' . $itemType );
		}
	}
	/**
	 * Input parameter for SIREN request url
	 *
	 * @param Concept[] $concepts        	
	 * @return string JSON input parameter
	 */
	protected static function getInputParameter($concepts) {
		$input = [ ];
		$nbOfConcepts = count ( $concepts );
		for($conceptIndex = 0; $conceptIndex < $nbOfConcepts; $conceptIndex ++) {
			$conceptNb = $conceptIndex + 1;
			$concept = $concepts [$conceptIndex];
			$word = $concept->getWord ();
			$input ['conceptURI' . (($conceptNb === 1) ? '' : $conceptNb)] = $word->getConceptURI ();
		}
		$oneConcept = reset ( $concepts );
		$learningRsc = $oneConcept->getWord ()->getLearningRsc ();
		if ($learningRsc !== null) {
			$input ['learningRsc'] = $learningRsc;
		}
		return $input;
	}
	
	public static function getContentExtractionUrl($contentUrl) {
		$url = rawurlencode($contentUrl);
		$requestUrl = 'http://' . TAO_AIG_FACADE_SIREN_HOST . ':' . TAO_AIG_FACADE_SIREN_PORT . '/SIREN/templates/definition/generate?wikiUrl=' . $url;
		return $requestUrl;
	}
}