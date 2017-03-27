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
namespace itis\taoAigFacade\test;

use itis\taoAigFacade\models\DeliveryBuilderService;
use itis\taoAigFacade\models\exceptions\ConnectFailedException;
use itis\taoAigFacade\models\FacadeService;
use itis\taoAigFacade\models\ItemBuilderService;
use itis\taoAigFacade\models\request\ItemRequest;
use itis\taoAigFacade\models\request\TestRequest;
use itis\taoAigFacade\models\TestBuilderService;
use oat\tao\test\TaoPhpUnitTestRunner;
use oat\taoGroups\models\GroupsService;

require_once dirname ( __FILE__ ) . '/../includes/raw_start.php';
/**
 * Unit tests (required phpunit >=4.8.5)
 *
 * @author Olivier Pedretti
 *        
 */
class taoAigFacadeTest extends TaoPhpUnitTestRunner {
	protected $itemRequestXmlFileName = 'restItemGeneration.xml';
	protected $itemRequestJsonFileName = 'restItemGeneration.json';
	protected $testRequestXmlFileName = 'restTestGeneration.xml';
	protected $testRequestJsonFileName = 'restTestGeneration.json';
	// define services
	protected $itemBuilderService = null;
	protected $testBuilderService = null;
	protected $aigFacadeService = null;
	protected $deliveryBuilderService = null;
	protected $groupsService = null;
	// define resources to clean up
	protected $facadeMgrUserRsc = null;
	protected $testTakerUserRsc = null;
	protected $groupRsc = null;
	protected $itemsAsRscs = null;
	protected $testRsc = null;
	protected $deliveryRsc = null;
	// others
	protected $facadeMgrUserName = null;
	protected $facadeMgrUserPwd = null;
	protected $testTakerUserName = null;
	protected $testTakerUserPwd = null;
	protected $createdItems = null;
	/**
	 * Called by phpunit before running tests
	 */
	protected function setUp() {
		TaoPhpUnitTestRunner::initTest ();
		$this->setServices ();
		
		// $this->createFacadeMgrUser ();
		// $this->createTestTakerUser ();
		
		parent::setUp ();
	}
	/**
	 * Called by phpunit after running tests whatever the test succeeded or failed
	 */
	protected function tearDown() {
		$this->cleanUpResources ();
		
		parent::tearDown ();
	}
	/**
	 * init required services
	 */
	private function setServices() {
		$this->itemBuilderService = ItemBuilderService::singleton ();
		$this->testBuilderService = TestBuilderService::singleton ();
		$this->aigFacadeService = FacadeService::singleton ();
		$this->deliveryBuilderService = DeliveryBuilderService::singleton ();
		$this->groupsService = GroupsService::singleton ();
		$this->assertInstanceOf ( FacadeService::class, $this->aigFacadeService );
	}
	/**
	 * Create Facade Manager User in TAO for test purpose
	 */
	private function createFacadeMgrUser() {
		$this->facadeMgrUserName = __CLASS__ . '_User_FacadeMgrTest';
		$this->facadeMgrUserPwd = md5 ( rand () );
		$taoUserRole = new \core_kernel_classes_Resource ( TAO_AIG_FACADE_INSTANCE_ROLE_AIGFACADEMANAGER );
		$this->facadeMgrUserRsc = \tao_models_classes_UserService::singleton ()->addUser ( $this->facadeMgrUserName, $this->facadeMgrUserPwd, $taoUserRole );
		$this->assertInstanceOf ( \core_kernel_classes_Resource::class, $this->facadeMgrUserRsc );
	}
	/**
	 * Create a TestTaker in TAO for test purpose
	 */
	private function createTestTakerUser() {
		$this->testTakerUserName = __CLASS__ . '_User_TestTakerTest';
		$this->testTakerUserPwd = md5 ( rand () );
		$taoUserRole = new \core_kernel_classes_Resource ( INSTANCE_ROLE_BASEUSER );
		$taoUserClass = new \core_kernel_classes_Class ( TAO_SUBJECT_CLASS );
		$this->testTakerUserRsc = \tao_models_classes_UserService::singleton ()->addUser ( $this->testTakerUserName, $this->testTakerUserPwd, $taoUserRole, $taoUserClass );
		$this->assertInstanceOf ( \core_kernel_classes_Resource::class, $this->testTakerUserRsc );
	}
	/**
	 * Remove generated resources during test
	 */
	private function cleanUpResources() {
		if ($this->facadeMgrUserRsc !== null) {
			$deleted = $this->facadeMgrUserRsc->delete ();
			$this->assertTrue ( $deleted, 'user not deleted successfully:' . $this->facadeMgrUserName );
		}
		if ($this->testTakerUserRsc !== null) {
			$deleted = $this->testTakerUserRsc->delete ();
			$this->assertTrue ( $deleted, 'user not deleted successfully:' . $this->testTakerUserName );
		}
		if ($this->testRsc !== null) {
			$deleteOK = $this->testBuilderService->deleteTest ( $this->testRsc );
			$this->assertTrue ( $deleteOK );
		}
		if ($this->itemsAsRscs !== null) {
			foreach ( $this->itemsAsRscs as $rsc ) {
				$deleteOK = $this->itemBuilderService->deleteItem ( $rsc );
				$this->assertTrue ( $deleteOK );
			}
		}
		if ($this->groupRsc !== null) {
			$deleteOK = $this->groupRsc->delete ();
			$this->assertTrue ( $deleteOK );
		}
		if ($this->deliveryRsc !== null) {
			$deleted = $this->aigFacadeService->deleteDelivery ( $this->deliveryRsc );
			$this->assertTrue ( $deleted );
		}
	}
	/**
	 * Create a delivery (and all its content)
	 */
	public function testDeliveryBuilder() {
		// create a group
		$this->groupRsc = $this->groupsService->createInstance ( $this->groupsService->getRootClass (), __CLASS__ );
		// create items
		$this->createItems ();
		// create a test
		$this->testRsc = $this->testBuilderService->generateTest ( __CLASS__ . '_Test', $this->itemsAsRscs );
		// create a delivery
		$this->deliveryRsc = $this->deliveryBuilderService->deliverTest( $this->testRsc, $this->groupRsc);
	}
	public function testItemGenerationFromXml() {
		$requestXml = file_get_contents ( dirname ( __FILE__ ) . DIRECTORY_SEPARATOR . $this->itemRequestXmlFileName );
		$this->assertNotFalse ( $requestXml );
		
		$request = ItemRequest::fromXml ( $requestXml );
		
		$this->runItemGeneration ( $request );
	}
	public function testItemGenerationFromJson() {
		$requestJson = file_get_contents ( dirname ( __FILE__ ) . DIRECTORY_SEPARATOR . $this->itemRequestJsonFileName );
		$this->assertNotFalse ( $requestJson );
		
		$request = ItemRequest::fromJson ( $requestJson );
		
		$this->runItemGeneration ( $request );
	}
	public function _testTestGenerationFromJson() {
		$requestJson = file_get_contents ( dirname ( __FILE__ ) . DIRECTORY_SEPARATOR . $this->testRequestJsonFileName );
		
		$cleanJson = $requestJson;
		$request = TestRequest::fromJson ( $cleanJson );
		
		$this->runTestGeneration ( $request );
	}
	public function _testTestGenerationFromXml() {
		$requestXml = file_get_contents ( dirname ( __FILE__ ) . DIRECTORY_SEPARATOR . $this->testRequestXmlFileName );
		
		$request = TestRequest::fromXml ( $requestXml );
		
		$this->runTestGeneration ( $request );
	}
	/**
	 * Test the ItemBuilder
	 */
	public function testItemBuilder() {
		$createdItems = $this->createItems ();
		
		// check item titles
		$count = 0;
		foreach ( $this->itemsAsRscs as $rsc ) {
			$this->assertTrue ( \common_Utils::isUri ( $rsc->getUri () ) );
			$this->assertEquals ( $rsc->getLabel (), $createdItems [$count] ['title'] );
			$count ++;
		}
	}
	/**
	 * Generate a test
	 */
	public function testTestBuilder() {
		// create items
		$this->createItems ();
		// create a test
		$this->testRsc = $this->testBuilderService->generateTest ( __CLASS__ . '_Test', $this->itemsAsRscs );
	}
	/**
	 * Create some items
	 *
	 * @return core_kernel_classes_Resource[]
	 */
	protected function createItems() {
		$item1 = [ 
				'title' => 'The games characters 1',
				'prompt' => 'Which one of the following games characters is a plumber?',
				'correct' => 'Mario',
				'choices' => [ 
						'Rayman',
						'Mario',
						'Mickey Mouse',
						'Batman' 
				] 
		];
		$item2 = [ 
				'title' => 'The games characters 2',
				'prompt' => 'Which one of the following games characters is yellow',
				'correct' => 'Pickachu',
				'choices' => [ 
						'Pickachu',
						'Taz',
						'Lara Croft' 
				] 
		];
		$createdItems = [ 
				$item1,
				$item2 
		];
		$this->itemsAsRscs = $this->itemBuilderService->buildItems ( $createdItems );
		return $createdItems;
	}
	protected function runItemGeneration(ItemRequest $request) {
		$expectedNbOfItems = intval ( floor ( count ( $request->getWords () ) / $request->getConceptsPerItem () ) * $request->getItemsPerConcept () );
		// $request->getUser ()->setCredentials ( $this->testTakerUserName, $this->testTakerUserPwd );
		
		$report = $this->aigFacadeService->generateItems ( $request );
		$this->assertEquals ( $report->getType (), \common_report_Report::TYPE_SUCCESS );
		
		// register for deletion
		$this->itemsAsRscs = $this->buildRscsFromUris ( $report->getData () );
		
		$this->assertCount ( $expectedNbOfItems, $this->itemsAsRscs );
	}
	protected function runTestGeneration(TestRequest $request) {
		$report = $this->aigFacadeService->createTestWithTestRequest ( $request );
		$this->assertInstanceOf ( \common_report_Report::class, $report );
		$this->assertEquals ( $report->getType (), \common_report_Report::TYPE_SUCCESS );
		$testUri = $report->getData ();
		$this->assertTrue ( \common_Utils::isUri ( $testUri ) );
		$testRsc = new \core_kernel_classes_Resource ( $testUri );
		$this->assertTrue ( $testRsc->exists () );
		// register for deletion
		$this->testRsc = $testRsc;
	}
	protected function buildRscsFromUris($uris) {
		$rscs = [ ];
		foreach ( $uris as $uri ) {
			$rscs [] = new \core_kernel_classes_Resource ( $uri );
		}
		return $rscs;
	}
	
	/**
	 * Test the facade via REST XML API
	 * Remove the "_" prefix method name to activate this unit test
	 *
	 * @throws \common_exception_Error
	 * @throws ConnectFailedException If failed to connect to REST API
	 * @throws \common_Exception
	 */
	public function _testRestFacadePostXmlFile() {
		$serviceUrl = BASE_URL . '/RestFacade';
		\common_Logger::e ( $serviceUrl );
		
		$session = curl_init ( $serviceUrl );
		if ($session === false) {
			throw new \common_exception_Error ( 'session not set' );
		}
		
		$post = [ 
				'request' => curl_file_create ( dirname ( __FILE__ ) . DIRECTORY_SEPARATOR . $this->testRequestXmlFileName, 'text/xml' ) 
		];
		
		$returnValue = curl_setopt_array ( $session, [
				
				// post method
				CURLOPT_POST => 1,
				
				CURLOPT_HTTPHEADER => [ 
						"Accept: application/json" 
				],
				CURLOPT_USERPWD => $this->facadeMgrUserName . ':' . $this->facadeMgrUserPwd,
				
				CURLOPT_POSTFIELDS => $post,
				
				// return the transfer as a string of the return value of curl_exec() instead of outputting it out directly.
				CURLOPT_RETURNTRANSFER => 1 
		] );
		if ($returnValue === false) {
			throw new \common_exception_Error ( 'cannot set curl options' );
		}
		
		// send request to server
		$returnedData = curl_exec ( $session );
		if (curl_errno ( $session )) {
			throw new ConnectFailedException ( curl_errno ( $session ) . ':' . curl_error ( $session ) );
		}
		
		// check the http code returned
		$httpCode = curl_getinfo ( $session, CURLINFO_HTTP_CODE );
		curl_close ( $session );
		if ($httpCode !== 200) {
			throw new \common_exception_Error ( 'unexpected http code from server:' . $httpCode . ' data:' . $returnedData );
		}
		
		$msg = json_decode ( $returnedData, true );
		if ($msg === null) {
			throw new \common_Exception ( 'json_decode failed, data:' . var_export ( $returnedData, true ) );
		}
		
		if ($msg ['success'] === false) {
			throw new \common_Exception ( 'Request failed: ' . var_export ( $msg, true ) );
		}
		$uri = $msg ['data'];
		$this->assertTrue ( \common_Utils::isUri ( $uri ) );
	}
	/**
	 * Test the facade via REST JSON API
	 * Remove the "_" prefix method name to activate this unit test
	 *
	 * @throws \common_exception_Error
	 * @throws ConnectFailedException If failed to connect to REST API
	 * @throws \common_Exception
	 */
	public function _testRestFacadeGetWithJson() {
		$serviceUrl = BASE_URL . '/RestFacade';
		\common_Logger::e ( $serviceUrl );
		
		$jsonStr = file_get_contents ( dirname ( __FILE__ ) . DIRECTORY_SEPARATOR . $this->testRequestJsonFileName );
		
		$params = [ 
				'request' => $jsonStr 
		];
		$serviceUrl .= '?' . http_build_query ( $params );
		
		$session = curl_init ( $serviceUrl );
		if ($session === false) {
			throw new \common_exception_Error ( 'session not set' );
		}
		
		$returnValue = curl_setopt_array ( $session, [
				
				// post method
				CURLOPT_HTTPGET => 1,
				
				CURLOPT_HTTPHEADER => [ 
						"Accept: application/json" 
				],
				
				CURLOPT_USERPWD => $this->facadeMgrUserName . ':' . $this->facadeMgrUserPwd,
				
				// return the transfer as a string of the return value of curl_exec() instead of outputting it out directly.
				CURLOPT_RETURNTRANSFER => 1 
		] );
		if ($returnValue === false) {
			throw new \common_exception_Error ( 'cannot set curl options' );
		}
		
		// send request to server
		$returnedData = curl_exec ( $session );
		if (curl_errno ( $session )) {
			throw new ConnectFailedException ( curl_errno ( $session ) . ':' . curl_error ( $session ) );
		}
		
		// check the http code returned
		$httpCode = curl_getinfo ( $session, CURLINFO_HTTP_CODE );
		curl_close ( $session );
		if ($httpCode !== 200) {
			throw new \common_exception_Error ( 'unexpected http code from server:' . $httpCode . ' data:' . $returnedData );
		}
		
		$msg = json_decode ( $returnedData, true );
		if ($msg === null) {
			throw new \common_Exception ( 'json_decode failed, data:' . var_export ( $returnedData, true ) );
		}
		
		if ($msg ['success'] === false) {
			throw new \common_Exception ( 'Request failed: ' . var_export ( $msg, true ) );
		}
		$uri = $msg ['data'];
		$this->assertTrue ( \common_Utils::isUri ( $uri ) );
	}
}