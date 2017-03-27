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
namespace itis\taoTestGeneration\controller;

use itis\taoAigFacade\models\DeliveryBuilderService;
use itis\taoAigFacade\models\FacadeService;
use itis\taoAigFacade\models\ItemBuilderService;
use itis\taoAigFacade\models\siren\ItemsParser;
use itis\taoAigFacade\models\TestBuilderService;
use itis\taoAigFacade\service\GroupService;
use itis\taoTestGeneration\models\exceptions\PreConditionFailureException;
use itis\taoTestGeneration\models\form\AutomatedTestGenerator as Form;

/**
 *
 * @author Eric Tobias [eric.tobias@list.lu]
 * @author Olivier Pedretti [olivier.pedretti@list.lu]
 * @since 1.0
 * @version 1.0.2
 */
class AutomatedTestGenerator extends \tao_actions_SaSModule {
	private $facadeService = null;
	private $itemClass = null;
	private $parameters = null;
	public function __construct() {
		parent::__construct ();
		include_once BASE_PATH . 'helpers' . DIRECTORY_SEPARATOR . '3rdparty' . DIRECTORY_SEPARATOR . 'Parsedown' . DIRECTORY_SEPARATOR . 'Parsedown.php';

		$this->service = \taoTests_models_classes_TestsService::singleton ();
		$this->defaultData ();
		$this->facadeService = FacadeService::singleton ();
		$this->itemClass = new \core_kernel_classes_Class ( TAO_AIG_FACADE_MODEL_ITEMCLASS );

		$this->parameters = $this->getRequestParameters ();

		parse_str ( parse_url ( $_SERVER ['HTTP_REFERER'], PHP_URL_QUERY ), $refererParameters );

		$this->parameters = array_merge ( $this->parameters, $refererParameters );

		\common_Logger::i ( var_export ( $this->parameters, true ) );

		// Perform sanity checks on request parameters.
		if (! array_key_exists ( TAO_AIG_TEST_GENERATOR_SESSION_ID, $this->parameters )) {
			throw new PreConditionFailureException ( __ ( "The required parameter sessionId was missing!" ) );
		}
		if (! isset ( $this->parameters [TAO_AIG_TEST_GENERATOR_SESSION_ID] )) {
			throw new PreConditionFailureException ( __ ( "A valid, non-null value must be provided for parameter: " ) . TAO_AIG_TEST_GENERATOR_SESSION_ID );
		}

		if (! array_key_exists ( TAO_AIG_TEST_GENERATOR_GROUP, $this->parameters )) {
			throw new PreConditionFailureException ( __ ( "The required parameter group was missing!" ) );
		}
		if (! isset ( $this->parameters [TAO_AIG_TEST_GENERATOR_GROUP] )) {
			throw new PreConditionFailureException ( __ ( "A valid, non-null value must be provided for parameter: " ) . TAO_AIG_TEST_GENERATOR_GROUP );
		}

		if (! array_key_exists ( TAO_AIG_TEST_GENERATOR_CONTENT, $this->parameters )) {
			throw new PreConditionFailureException ( __ ( "The required parameter content was missing!" ) );
		}
		if (! isset ( $this->parameters [TAO_AIG_TEST_GENERATOR_CONTENT] )) {
			throw new PreConditionFailureException ( __ ( "A valid, non-null value must be provided for parameter: " ) . TAO_AIG_TEST_GENERATOR_CONTENT );
		}

		if (! array_key_exists ( TAO_AIG_TEST_GENERATOR_RSC_TYPE, $this->parameters )) {
			throw new PreConditionFailureException ( __ ( "The required parameter resourceType was missing!" ) );
		}
		if (! isset ( $this->parameters [TAO_AIG_TEST_GENERATOR_RSC_TYPE] )) {
			throw new PreConditionFailureException ( __ ( "A valid, non-null value must be provided for parameter: " ) . TAO_AIG_TEST_GENERATOR_RSC_TYPE );
		}

		if (! array_key_exists ( TAO_AIG_TEST_GENERATOR_RSC_URL, $this->parameters )) {
			throw new PreConditionFailureException ( __ ( "The required parameter resourceUrl was missing!" ) );
		}
		if (! isset ( $this->parameters [TAO_AIG_TEST_GENERATOR_RSC_URL] )) {
			throw new PreConditionFailureException ( __ ( "A valid, non-null value must be provided for parameter: " ) . TAO_AIG_TEST_GENERATOR_RSC_URL );
		}
	}
	protected function getRootClass() {
		return $this->service->getRootClass ();
	}
	protected function htmlEntitiesDecode($html) {
		$html = html_entity_decode ( $html );
		// html_entity_decode replaces non breaking space entity by non breaking space character
		$html = str_replace ( "\xc2\xa0", ' ', $html );
		return $html;
	}
	public function generateTest() {
		$instance = $this->getCurrentInstance ();

		$formContainer = new Form ( array (
				'class' => $this->getCurrentClass (),
				'instance' => $instance,
				'data-lg' => \common_session_SessionManager::getSession ()->getDataLanguage (),
				Form::ELEMENT_SOURCE_TYPE => $this->parameters [TAO_AIG_TEST_GENERATOR_RSC_TYPE],
				Form::ELEMENT_SOURCE_URL => $this->parameters [TAO_AIG_TEST_GENERATOR_RSC_URL]
		), null );

		$form = $formContainer->getForm ();

		if ($form->isValid () && $form->isSubmited ()) {
				
			$name = $form->getValue ( Form::ELEMENT_TEST_NAME );
			if (empty ( $name )) {
				$name = 'AIG (default test name, please rename)';
			}
				
			$groupRsc = self::getGroup ( $this->parameters [TAO_AIG_TEST_GENERATOR_GROUP] );
				
			$numberOfQuestions = $form->getValue ( Form::ELEMENT_NUMBER_OF_QUESTIONS );
			if (empty ( $numberOfQuestions ) || ! is_numeric ( $numberOfQuestions ) && $numberOfQuestions <= 0) {
				throw new PreConditionFailureException ( __ ( 'The number of desired questions must be provided!' ) );
			}
			$numberOfQuestions = intval ( $numberOfQuestions );
				
			$typeOfInteractionElement = $form->getElement ( Form::ELEMENT_TYPE_OF_INTERACTION );
			$typeOfInteractionOptions = $typeOfInteractionElement->getOptions ();
			$typeOfInteractionId = $typeOfInteractionElement->getRawValue ();
			if (! array_key_exists ( $typeOfInteractionId, $typeOfInteractionOptions )) {
				throw new PreConditionFailureException ( __ ( 'Unsupported index: ' ) . $typeOfInteractionId );
			}
			$typeOfInteraction = $typeOfInteractionOptions [$typeOfInteractionId];
				
			$typeOfConstructElement = $form->getElement ( Form::ELEMENT_TYPE_OF_CONSTRUCT );
			$typeOfConstructOptions = $typeOfConstructElement->getOptions ();
			$typeOfConstructId = $typeOfConstructElement->getRawValue ();
			if (! array_key_exists ( $typeOfConstructId, $typeOfConstructOptions )) {
				throw new PreConditionFailureException ( __ ( 'Unsupported index: ' ) . $typeOfConstructId );
			}
			$typeOfConstruct = $typeOfConstructOptions [$typeOfConstructId];
				
			$content = $this->parameters [TAO_AIG_TEST_GENERATOR_CONTENT];
				
			$report = new \common_report_SuccessElement ( __ ( 'Test generation complete' ) );
				
			// prepare data
			$typeOfInteractionForFacade = null;
			$template = null;
			$resourceType = $this->parameters [TAO_AIG_TEST_GENERATOR_RSC_TYPE];
			switch ($typeOfInteraction) {
				case 'Inline Choice' :
					$typeOfInteractionForFacade = FacadeService::TYPE_OF_CONSTRUCT_INLINE;
					$template = FacadeService::TEMPLATE_INLINE;
					// add virtual body to use html parser of cloze library
					switch ($resourceType) {
						case 'OER' :
							// HTML content
							// keep content as is
							break;
						case 'WIKI' :
							// Markdown content
							$content = \Parsedown::instance ()->text ( $content );
							\common_Logger::t ( $content );
							break;
						default :
							throw new PreConditionFailureException ( __ ( 'Unsupported type of resource type: ' ) . $resourceType );
					}
					$content = '<div class="body">' . $content . '</div>';
					$inputs = [
							"text" => $content
					];
					$xmlItems = self::generateItems ( 10, $typeOfInteractionForFacade, $template, $inputs );
					if (empty ( $xmlItems )) {
						throw new PreConditionFailureException ( __ ( 'No item generated' ) );
					}
					// get randomly only one item
					$xmlItems = [
							$xmlItems [array_rand ( $xmlItems )]
					];
					$report->add ( new \common_report_Report ( \common_report_Report::TYPE_INFO, 'Number of "' . $typeOfInteraction . '" Items generated: ' . count ( $xmlItems ) ) );
					break;
				case 'Multiple Choice' :
					$typeOfInteractionForFacade = FacadeService::TYPE_OF_CONSTRUCT_MULTIPLE_CHOICE;
					$template = FacadeService::TEMPLATE_MULTIPLE_CHOICE;
					\common_Logger::w(\Parsedown::instance ()->text ( $content ));
					// break;
				case 'Mix' :
				default :
					throw new PreConditionFailureException ( __ ( 'Unsupported type of interaction: ' ) );
			}
				
			$qtiItemsAsResources = $this->importQtiItems ( $xmlItems, $this->itemClass->getUri () );
			$testAsResource = TestBuilderService::singleton ()->generateTest ( $name, $qtiItemsAsResources );
				
			$this->createDeliveryFor ( $testAsResource, $groupRsc );
				
			$report->setData ( $testAsResource->getUri () );
				
			$this->setData ( 'report', $report );
			$this->setView ( 'report.tpl', 'tao' );
		} else {
			$this->wizard ( $form );
		}
	}
	protected function generateItems($numberOfQuestions, $typeOfInteractionForFacade, $template, $inputs) {
		$xmlResponse = $this->facadeService->sendGenerationRequest ( $typeOfInteractionForFacade, $template, $inputs );

		$xmlLayers = ItemsParser::getNFirstItemsFromString ( $xmlResponse, $numberOfQuestions );

		$xmlItems = [ ];
		foreach ( $xmlLayers as $xmlLayer ) {
			$xmlItems [] = ItemsParser::getItem ( $xmlLayer );
		}
		return $xmlItems;
	}
	protected static function getGroup($groupName) {
		$groupRscs = GroupService::searchGroupsByName ( $groupName );
		if (empty ( $groupRscs )) {
			throw new \common_exception_PreConditionFailure ( __ ( 'Group not found: ' ) . $groupName );
		}
		if (count ( $groupRscs ) > 1) {
			throw new \common_exception_PreConditionFailure ( 'More than one group matching the name: ' . $groupName );
		}
		return reset ( $groupRscs );
	}
	private function wizard(\tao_helpers_form_Form $form) {
		$this->setData ( 'myForm', $form->render () );
		$this->setData ( 'formTitle', __ ( 'Test Parameters' ) );
		$this->setView ( 'form_testGenerationParameters.tpl' );
	}
	private function importQtiItems($itemXmls = array(), $parentClass) {
		$rscs = [ ];
		foreach ( $itemXmls as $itemXml ) {
			$itemRsc = ItemBuilderService::singleton ()->importQtiItem ( ItemsParser::simpleXmlToXmlString ( $itemXml ), $parentClass );
			$rscs [] = $itemRsc;
		}
		return $rscs;
	}
	private function createDeliveryFor(\core_kernel_classes_Resource $test, \core_kernel_classes_Resource $groupRsc) {
		$date = date ( "Y-m-d H:i:s");
		$delivery = DeliveryBuilderService::singleton ()->deliverTest ( $test, $groupRsc, 'AIG '.$date);
		$delivery->setPropertyValue ( new \core_kernel_classes_Property ( TAO_AIG_FACADE_MODEL_PROPERTY_SESSION ), $this->parameters [TAO_AIG_TEST_GENERATOR_SESSION_ID] );
	}
}
