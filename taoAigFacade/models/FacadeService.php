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
 * Facade Service definition
 *
 * @author Olivier Pedretti
 */
namespace itis\taoAigFacade\models;

use itis\taoAigFacade\models\exceptions\InsufficientKnowledgeException;
use itis\taoAigFacade\models\request\DeliveryRequest;
use itis\taoAigFacade\models\request\ItemRequest;
use itis\taoAigFacade\models\request\TestRequest;
use itis\taoAigFacade\models\request\User;
use itis\taoAigFacade\models\request\Word;
use itis\taoAigFacade\models\siren\ItemsParser;
use itis\taoAigFacade\models\siren\OptionsParser;
use oat\taoGroups\models\GroupsService;
use oat\taoTestTaker\models\TestTakerService;
use itis\taoAigFacade\models\exceptions\ConnectFailedException;

class FacadeService extends \tao_models_classes_Service {
	// external services
	protected $itemBuilder = null;
	protected $testBuilder = null;
	protected $deliveryBuilder = null;
	protected $groupService = null;
	protected $userService = null;
	protected $itemClass = null;
	// item templates type
	const ITEM_TYPE_VOC_TR_MCQ = 'VocabularyTranslationMCQ';
	const ITEM_TYPE_VOC_DEF_MCQ = 'VocabularyDefinitionMCQ';
	const ITEM_TYPE_VOC_TR_MATCH = 'VocabularyTranslationMatch';
	const TYPE_OF_CONSTRUCT_INLINE = 'cloze';
	const TEMPLATE_INLINE = 'eagle1.1';
	const TYPE_OF_CONSTRUCT_MULTIPLE_CHOICE = 'mcq';
	const TEMPLATE_MULTIPLE_CHOICE = 'mcq';
	protected function __construct() {
		parent::__construct ();
		$this->init ();
	}
	
	/**
	 * Init necessary services
	 */
	protected function init() {
		$extension = \common_ext_ExtensionsManager::singleton ()->getExtensionById ( 'taoAigFacade' );
		$extension->load ();
		
		$this->itemBuilder = ItemBuilderService::singleton ();
		$this->testBuilder = TestBuilderService::singleton ();
		$this->deliveryBuilder = DeliveryBuilderService::singleton ();
		$this->groupService = GroupsService::singleton ();
		$this->userService = TestTakerService::singleton ();
		$this->itemClass = new \core_kernel_classes_Class ( TAO_AIG_FACADE_MODEL_ITEMCLASS );
	}
	/**
	 * Generate items described in the ItemRequest
	 *
	 * @param ItemRequest $request        	
	 * @return \common_report_Report Report where data is a list of generated items uris
	 */
	public function generateItems(ItemRequest $request) {
		\common_Logger::t ( __METHOD__ );
		$itemGenerator = new ItemGenerator ();
		$report = $itemGenerator->generate ( $request );
		return $report;
	}
	/**
	 * Create a delivery based on user request
	 *
	 * @param DeliveryRequest $request        	
	 * @param \core_kernel_classes_Resource $createdTestRsc
	 *        	Generated test
	 * @param \core_kernel_classes_Resource[] $createdItemRscs
	 *        	Generated items
	 * @param \core_kernel_classes_Resource $createdGroupRsc
	 *        	Generated Group
	 * @throws \InvalidArgumentException
	 * @throws \common_exception_Error
	 * @return \core_kernel_classes_Resource Generated delivery
	 */
	public function createDelivery(DeliveryRequest $request, &$createdTestRsc, &$createdItemRscs, &$createdGroupRsc) {
		\common_Logger::t ( __METHOD__ );
		
		// check test-taker credentials
		$userRsc = $this->getUser ( $request->getUser () );
		
		if ($userRsc->isInstanceOf ( new \core_kernel_classes_Class ( TAO_SUBJECT_CLASS ) ) === false) {
			throw new \InvalidArgumentException ( 'not a test taker resource' );
		}
		
		// set item types
		$itemList = $this->setWantedItemTypes ( $request );
		
		$testRsc = $this->getTest ( $request, $itemList, $createdTestRsc, $createdItemRscs );
		if (empty ( $testRsc )) {
			throw new \common_exception_Error ( 'failed to get test' );
		}
		
		// create a group to isolate the user
		$groupName = __CLASS__ . \tao_helpers_Uri::getUniqueId ( \common_Utils::getNewUri () );
		$createdGroupRsc = $this->groupService->createInstance ( $this->groupService->getRootClass (), $groupName );
		
		// add the user to this group
		$this->groupService->setRelatedSubjects ( $createdGroupRsc, array (
				$userRsc 
		) );
		
		// generate delivery
		$deliveryRsc = $this->deliveryBuilder->deliverTest ( $testRsc, $createdGroupRsc );
		
		\common_Logger::t ( __METHOD__ . ' ' . $deliveryRsc->getUri () );
		
		return $deliveryRsc;
	}
	public function createTestWithTestRequest(TestRequest $request) {
		\common_Logger::t ( __METHOD__ );
		$testGenerator = new TestGenerator ();
		$report = $testGenerator->generate ( $request );
		return $report;
	}
	public function createTestWithItemClass(\core_kernel_classes_Class $class) {
		// check if class OK
		$baseClass = new \core_kernel_classes_Class ( TAO_ITEM_CLASS );
		if (! $class->isSubClassOf ( $baseClass )) {
			throw new \common_exception_NotAcceptable ( 'not an item class: ' . $class->getUri () );
		}
		// check if items
		$itemRscs = $class->getInstances ( false );
		if (! count ( $itemRscs )) {
			throw new \common_exception_NoContent ( 'no items' );
		}
		// create test
		$classLabel = $class->getLabel ();
		\common_Logger::t ( 'create test: ' . $classLabel );
		$testRsc = $this->testBuilder->generateTest ( $classLabel, $itemRscs );
		return $testRsc;
	}
	/**
	 * Get Test-taker by checking its credentials
	 *
	 * @param User $user        	
	 * @return \core_kernel_classes_Resource The user
	 */
	public function getUser(User $user) {
		$userService = \tao_models_classes_UserService::singleton ();
		
		$adapter = new \core_kernel_users_AuthAdapter ( $user->getLogin (), $user->getPwd () );
		$userRsc = new \core_kernel_classes_Resource ( $adapter->authenticate ()->getIdentifier () );
		
		return $userRsc;
	}
	/**
	 * Delete a delivery (just the delivery)
	 *
	 * @param \core_kernel_classes_Resource $deliveryRsc        	
	 * @return bool False on error, true otherwise
	 */
	public function deleteDelivery(\core_kernel_classes_Resource $deliveryRsc) {
		$deleted = $this->deliveryBuilder->deleteDelivery ( $deliveryRsc );
		return $deleted;
	}
	/**
	 * Define a type for item based on Interlingua project algorithm
	 *
	 * @param Word $word
	 *        	The word for which an item will be generated
	 * @return string @see FacadeService constants
	 */
	protected function defineAType(Word $word) {
		return self::ITEM_TYPE_VOC_DEF_MCQ;
		
		$rand = rand ( 0, 100 );
		if ($rand <= 60) {
			return self::ITEM_TYPE_VOC_TR_MCQ;
		} else if ($rand <= 80) {
			return self::ITEM_TYPE_VOC_TR_MATCH;
		} else if (! empty ( $word->getDBpediaURI () )) {
			return self::ITEM_TYPE_VOC_DEF_MCQ;
		} else {
			return self::ITEM_TYPE_VOC_TR_MCQ;
		}
	}
	/**
	 * Return a list of item information for item generation
	 *
	 * @param DeliveryRequest $request        	
	 * @return Item[]
	 */
	protected function setWantedItemTypes(DeliveryRequest $request) {
		$list = array ();
		foreach ( $request->getWords () as $word ) {
			array_push ( $list, new ItemToGenerate ( $word, $this->defineAType ( $word ) ) );
		}
		return $list;
	}
	/**
	 * Get a test, if doesn't exist test is created
	 *
	 * @param DeliveryRequest $request        	
	 * @param Item[] $itemList        	
	 * @param \core_kernel_classes_Resource $createdTestRsc        	
	 * @param \core_kernel_classes_Resource[] $createdItemRscs        	
	 * @throws \common_exception_Error
	 * @return \core_kernel_classes_Resource The test
	 */
	protected function getTest(DeliveryRequest $request, $itemList, &$createdTestRsc, &$createdItemRscs) {
		// check if a test already exists
		$testRsc = $this->hasTest ( $request );
		if (empty ( $testRsc )) {
			\common_Logger::t ( __METHOD__ . ' test does not exist' );
			// the test doesn't exist, create it
			// find if some items already exist
			$this->findAndSetItemRsc ( $request, $itemList );
			// generate not present items
			$this->generateMissingItems ( $request, $itemList, $createdItemRscs );
			if (empty ( $itemList )) {
				throw new InsufficientKnowledgeException ();
			}
			// build list of items
			$itemRscs = $this->getListOfItemRscs ( $itemList );
			// create test
			$testRsc = $this->testBuilder->generateTest ( 'AigTest', $itemRscs );
			// tell caller that the test did not previously exist
			$createdTestRsc = $testRsc;
		}
		return $testRsc;
	}
	/**
	 *
	 * @param ItemRequest $request        	
	 * @param ItemToGenerate $item        	
	 * @return \core_kernel_classes_Resource Item generated
	 */
	protected function generateItem(ItemRequest $request, ItemToGenerate $item) {
		$xmlResponse = $this->sendRequestForItems ( $request, $item );
		$itemXmlStr = ItemsParser::getAnItemFromString ( $xmlResponse );
		$itemRsc = $this->itemBuilder->importQtiItem ( $itemXmlStr, $request->getParentClass () );
		return $itemRsc;
	}
	/**
	 *
	 * @param DeliveryRequest $request        	
	 * @param Item[] $itemList
	 *        	The list can be updated
	 * @param \core_kernel_classes_Resource[] $createdItemRscs        	
	 */
	protected function generateMissingItems(DeliveryRequest $request, &$itemList, &$createdItemRscs) {
		$createdItemRscs = array ();
		foreach ( $itemList as $item ) {
			$itemRsc = $item->getRsc ();
			if (empty ( $itemRsc )) {
				try {
					$itemRsc = $this->generateItem ( $request, $item );
				} catch ( \common_exception_Error $e ) {
					$defaultType = self::ITEM_TYPE_VOC_TR_MCQ;
					if ($item->getType () != $defaultType) {
						try {
							\common_Logger::w ( __METHOD__ . ' force type: ' . $defaultType );
							$item->setType ( $defaultType );
							$itemRsc = $this->generateItem ( $request, $item );
						} catch ( \common_exception_Error $e ) {
							\common_Logger::w ( __METHOD__ . ' discards item' );
							continue;
						}
					} else {
						\common_Logger::w ( __METHOD__ . ' discards item' );
						continue;
					}
				}
				array_push ( $createdItemRscs, $itemRsc );
				// register resource
				$item->setRsc ( $itemRsc );
			}
		}
		// remove not geneterad items
		$items = array ();
		foreach ( $itemList as $item ) {
			if (! empty ( $item->getRsc () )) {
				array_push ( $items, $item );
			}
		}
		$itemList = $items;
	}
	protected function findAndSetItemRsc($request, $itemList) {
		// TODO to be implemented
		// set rsc in $itemList
	}
	protected function hasItem(Word $word) {
		// TODO to be implemented
		return false;
	}
	protected function hasTest(DeliveryRequest $request) {
		// TODO to be implemented
		return null;
	}
	protected function generateDataSet(Word $word, DeliveryRequest $request) {
		$data = $this->sendRequestForOptions ( $word, $request );
		$distractors = OptionsParser::getDistractorsFromString ( $data );
		return $distractors;
	}
	protected function sendRequestForOptions(Word $word, DeliveryRequest $request) {
		$conceptURI = $word->getConceptURI ();
		$label = $word->getLabel ();
		$lang = substr ( $request->getLanguage (), 0, 2 );
		$requestUrl = 'http://' . TAO_AIG_FACADE_SIREN_HOST . ':' . TAO_AIG_FACADE_SIREN_PORT . '/SIREN/options?uri=' . $conceptURI . '&lang=' . $lang . '&label=' . $label . '&number=3';
		$responseData = $this->sendRequest ( $requestUrl );
		return $responseData;
	}
	
	/**
	 * Method used to send a request to parse an OER to SIREN.
	 * The request will expect an encoded (Percent-Encoding) url to pass as parameter to the request.
	 *
	 * @param
	 *        	The URL to the source OER, encoded using percent-encoding.
	 * @see rawurlencode($string)
	 * @return The function will return the value received as response from the server, in this case, an XML document containing, depening n the success of the parsing, possibly multiple items.
	 */
	public function sendGenerationRequest( $typeOfInteraction, $template, $inputs) {
		$requestUrl = 'http://' . TAO_AIG_FACADE_SIREN_HOST . ':' . TAO_AIG_FACADE_SIREN_PORT . '/SIREN/templates/' . $typeOfInteraction . '/generate?name=' . $template . '&input=' . urlencode ( json_encode ( $inputs, JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES ) );
		return $this->sendRequest ( $requestUrl );
	}
	
	/**
	 * Method used to send a request to parse an OER to SIREN.
	 * The request will expect an encoded (Percent-Encoding) url to pass as parameter to the request.
	 *
	 * @param
	 *        	The URL to the source OER, encoded using percent-encoding.
	 * @see rawurlencode($string)
	 * @return The function will return the value received as response from the server, in this case, an XML document containing, depening n the success of the parsing, possibly multiple items.
	 */
	public function sendParseRequest($sourceOer) {
		$requestUrl = 'http://' . TAO_AIG_FACADE_SIREN_HOST . ':' . TAO_AIG_FACADE_SIREN_PORT . '/SIREN/templates/definition/generate?wikiUrl=' . $sourceOer;
		return $this->sendRequest ( $requestUrl );
	}
	
	/**
	 * Make an HTTP request
	 *
	 * @param string $requestUrl        	
	 * @throws \common_exception_Error
	 * @throws ConnectFailedException
	 * @return mixed
	 */
	private function sendRequest($requestUrl) {
		\common_Logger::t ( __METHOD__ . ' ' . $requestUrl );
		$session = curl_init ( $requestUrl );
		if ($session === false) {
			throw new \common_exception_Error ( 'session not set' );
		}
		$returnValue = curl_setopt_array ( $session, array (
				
				CURLOPT_HTTPGET => 1,
				
				CURLOPT_HTTPHEADER => array (
						"Accept: text/xml" 
				),
				
				// return the transfer as a string of the return value of curl_exec() instead of outputting it out directly.
				CURLOPT_RETURNTRANSFER => 1,
				
				// Modify the value of the execution timeout
				CURLOPT_TIMEOUT => TAO_AIG_FACADE_CURL_TIMEOUT 
		) );
		if ($returnValue === false) {
			throw new \common_exception_Error ( 'cannot set curl options' );
		}
		
		// send request to server
		$returnedData = curl_exec ( $session );
		if (curl_errno ( $session )) {
			throw new ConnectFailedException ( 'session error: ' . curl_errno ( $session ) . ':' . curl_error ( $session ) );
		}
		
		// check the http code returned
		$httpCode = curl_getinfo ( $session, CURLINFO_HTTP_CODE );
		curl_close ( $session );
		if ($httpCode !== 200) {
			throw new \common_exception_Error ( 'unexpected http code from server:' . $httpCode . ' request:' . $requestUrl . ' data: ' . $returnedData );
		}
		return $returnedData;
	}
}