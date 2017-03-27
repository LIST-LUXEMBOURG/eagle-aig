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
namespace itis\taoTestGeneration\models\form;

/**
 * Form class to show the test generation form once a user requests to generate a test.
 * The form exposes the following elements:<br/>
 * <ul>
 * <li>The name of the test that is to be generated;</li>
 * <li>The number of questions that should be added to the test. Should the generation feature less questions, the parameter acts as a maximum;</li>
 * <li>The interaction type of the questions to generate;</li>
 * <li>The construct type of the questions to test;</li>
 * <li>The url of the source OER to use as a source for the automated item generation.</li>
 * </ul>
 *
 * @author Eric Tobias [eric.tobias@list.lu]
 * @author Olivier Pedretti [olivier.pedretti@list.lu]
 * @since 1.0
 * @version 1.0.1
 */
class AutomatedTestGenerator extends \tao_helpers_form_FormContainer {
	const ELEMENT_TEST_NAME = 'TestName';
	const ELEMENT_NUMBER_OF_QUESTIONS = 'NumberOfQuestions';
	const ELEMENT_TYPE_OF_INTERACTION = 'TypeOfInteraction';
	const ELEMENT_TYPE_OF_CONSTRUCT = 'TypeOfConstruct';
	const ELEMENT_SOURCE_URL = 'SourceUrl';
	const ELEMENT_SOURCE_TYPE = 'SourceType';
	protected function initForm() {
		$this->form = new \tao_helpers_form_xhtml_Form ( __CLASS__ );
		$this->form->setDecorators ( array (
				'element' => new \tao_helpers_form_xhtml_TagWrapper ( array (
						'tag' => 'div' 
				) ),
				'group' => new \tao_helpers_form_xhtml_TagWrapper ( array (
						'tag' => 'div',
						'cssClass' => 'form-group' 
				) ),
				'error' => new \tao_helpers_form_xhtml_TagWrapper ( array (
						'tag' => 'div',
						'cssClass' => 'form-error ui-state-error ui-corner-all' 
				) ),
				'actions-bottom' => new \tao_helpers_form_xhtml_TagWrapper ( array (
						'tag' => 'div',
						'cssClass' => 'form-toolbar' 
				) ),
				'actions-top' => new \tao_helpers_form_xhtml_TagWrapper ( array (
						'tag' => 'div',
						'cssClass' => 'form-toolbar' 
				) ) 
		) );
	}
	protected function initElements() {
		$clazz = $this->data ['class'];
		$instance = $this->data ['instance'];
		$datalg = $this->data ['data-lg'];
		$sourceUrl = $this->data [self::ELEMENT_SOURCE_URL];
		$sourceType = $this->data [self::ELEMENT_SOURCE_TYPE];
		
		$generateElt = \tao_helpers_form_FormFactory::getElement ( 'generate', 'Free' );
		$generateElt->setValue ( '<button class="btn-info small" type="submit" id="generateButton">' . \tao_helpers_Icon::iconExport () . __ ( 'Generate' ) . '</button>' );
		
		$this->form->setActions ( array (
				$generateElt 
		), 'bottom' );
		
		$element = \tao_helpers_form_FormFactory::getElement ( self::ELEMENT_SOURCE_URL, 'Textbox' );
		$element->addAttribute ( 'title', $sourceUrl );
		$element->addAttribute ( 'disabled', 'disabled' );
		$element->setDescription ( __ ( 'Source:' ) );
		$element->setValue ( $sourceUrl );
		$this->form->addElement ( $element );
		
		$element = \tao_helpers_form_FormFactory::getElement ( self::ELEMENT_SOURCE_TYPE, 'Label' );
		$element->setDescription ( __ ( 'Source Type:' ) );
		$element->setValue ( $sourceType );
		$this->form->addElement ( $element );
		
		$testName = \tao_helpers_form_FormFactory::getElement ( self::ELEMENT_TEST_NAME, 'Textbox' );
		$testName->setDescription ( __ ( 'Test Name:' ) );
		$testName->addValidator ( \tao_helpers_form_FormFactory::getValidator ( 'NotEmpty' ) );
		$this->form->addElement ( $testName );
		
		$numberOfQuestions = \tao_helpers_form_FormFactory::getElement ( self::ELEMENT_NUMBER_OF_QUESTIONS, 'Textbox' );
		$numberOfQuestions->setDescription ( __ ( 'Number of Items:' ) );
		$numberOfQuestions->addValidator ( \tao_helpers_form_FormFactory::getValidator ( 'NotEmpty' ) );
		$numberOfQuestions->addValidator ( \tao_helpers_form_FormFactory::getValidator ( 'Integer', [ 
				'min' => 1 
		] ) );
		$this->form->addElement ( $numberOfQuestions );
		
		$typeOfConstruct = \tao_helpers_form_FormFactory::getElement ( self::ELEMENT_TYPE_OF_CONSTRUCT, 'Combobox' );
		$typeOfConstruct->setDescription ( __ ( 'Type of Construct:' ) );
		$levels = array (
				' 0' => 'Information Literacy' 
		);
		$typeOfConstruct->setOptions ( $levels );
		$typeOfConstruct->setValue ( ' 0' );
		$this->form->addElement ( $typeOfConstruct );
		
		$typeOfInteraction = \tao_helpers_form_FormFactory::getElement ( self::ELEMENT_TYPE_OF_INTERACTION, 'Combobox' );
		$typeOfInteraction->setDescription ( __ ( 'Type of Interaction:' ) );
		// ' 2' => 'Associate',
		// ' 3' => 'Match',
		// ' 4' => 'Gap Match'
		$levels = [ 
				' 0' => 'Inline Choice',
// 				' 1' => 'Multiple Choice',
// 				' 2' => 'Mix' 
		];
		
		$typeOfInteraction->setOptions ( $levels );
		$typeOfInteraction->setValue ( ' 0' );
		$this->form->addElement ( $typeOfInteraction );
		
		$classUriElt = \tao_helpers_form_FormFactory::getElement ( 'classUri', 'Hidden' );
		$classUriElt->setValue ( \tao_helpers_Uri::encode ( $clazz->getUri () ) );
		$this->form->addElement ( $classUriElt );
		
		$uriElt = \tao_helpers_form_FormFactory::getElement ( 'uri', 'Hidden' );
		$uriElt->setValue ( \tao_helpers_Uri::encode ( $instance->getUri () ) );
		$this->form->addElement ( $uriElt );
	}
}