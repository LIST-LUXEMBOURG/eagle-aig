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
 *
 * @author Olivier Pedretti
 */
namespace itis\taoAigFacade\controller;

use itis\taoAigFacade\models\CrudFacadeService;
use itis\taoAigFacade\models\FacadeService;
use itis\taoAigFacade\models\request\DeliveryRequest;
use itis\taoAigFacade\models\request\User;
use itis\taoAigFacade\models\DeliveryBuilderService;

class RestFacade extends \tao_actions_CommonRestModule {
	protected $facadeService = null;
	public function __construct() {
		parent::__construct ();
		$this->service = CrudFacadeService::singleton ();
		$this->facadeService = FacadeService::singleton ();
	}
	protected function getParametersAliases() {
		return array_merge ( parent::getParametersAliases (), array () );
		// "member"=> TAO_GROUP_MEMBERS_PROP
	}
	/**
	 *
	 * @param User $userFromRequest        	
	 * @return \core_kernel_classes_Resource User
	 */
	protected function getUser(User $userFromRequest) {
		$login = $userFromRequest->getLogin ();
		$pwd = $userFromRequest->getPwd ();
		$userService = \tao_models_classes_UserService::singleton ();
		
		$adapter = new \core_kernel_users_AuthAdapter ( $login, $pwd );
		$userRsc = new \core_kernel_classes_Resource ( $adapter->authenticate ()->getIdentifier () );
		
		return $userRsc;
	}
	/**
	 *
	 * {@inheritDoc}
	 *
	 * @see tao_actions_CommonRestModule::get()
	 */
	public function get($uri = null) {
		$delivRsc = null;
		try {
			// check if request file is present
			if (! ($this->hasRequestParameter ( TAO_AIG_FACADE_REST_REQUEST_IDENTIFIER ))) {
				throw new \common_exception_MissingParameter ( TAO_AIG_FACADE_REST_REQUEST_IDENTIFIER );
			}
			$requestJson = $this->getRequestParameter ( TAO_AIG_FACADE_REST_REQUEST_IDENTIFIER );
			// fix html encoding
			$cleanJson = htmlspecialchars_decode ( $requestJson );
			$request = DeliveryRequest::fromJson ( $cleanJson );
			
			$delivRsc = $this->facadeService->createDelivery ( $request, $createdTestRsc, $createdItemRscs, $createdGroupRsc );
		} catch ( \Exception $e ) {
			$this->returnFailure ( $e );
		}
		return $this->returnSuccess ( $delivRsc->getUri () );
	}
	public function post() {
		$delivRsc = null;
		try {
			// check if request file is present
			if (! ($this->hasRequestParameter ( TAO_AIG_FACADE_REST_REQUEST_FILENAME ))) {
				throw new \common_exception_MissingParameter ( TAO_AIG_FACADE_REST_REQUEST_FILENAME );
			}
			$requestXml = $this->getRequestParameter ( TAO_AIG_FACADE_REST_REQUEST_FILENAME );
			$request = DeliveryRequest::fromXml ( $requestXml );
			$delivRsc = $this->facadeService->createDelivery ( $request, $createdTestRsc, $createdItemRscs, $createdGroupRsc );
		} catch ( \Exception $e ) {
			$this->returnFailure ( $e );
		}
		return $this->returnSuccess ( $delivRsc->getUri () );
	}
	public function put($uri = NULL) {
		$this->returnFailure ( new \common_exception_NotImplemented () );
	}
	public function delete($uri = null) {
		try {
			if (! is_null ( $uri )) {
				if (! \common_Utils::isUri ( $uri )) {
					throw new \common_exception_InvalidArgumentType ();
				}
				if (! ($this->service->isInScope ( $uri ))) {
					throw new \common_exception_PreConditionFailure ( "The URI must be a valid resource under the root Class" );
				}
				$success = DeliveryBuilderService::singleton ()->deleteDelivery ( new \core_kernel_classes_Resource ( $uri ) );
			}
		} catch ( \Exception $e ) {
			return $this->returnFailure ( $e );
		}
		return $this->returnSuccess ( $success );
	}
	
	/**
	 * Optionnal Requirements for parameters to be sent on every service
	 */
	protected function getParametersRequirements() {
		return array ();
	/**
	 * you may use either the alias or the uri, if the parameter identifier
	 * is set it will become mandatory for the method/operation in $key
	 * Default Parameters Requirents are applied
	 * type by default is not required and the root class type is applied
	 *
	 * @example :"post"=> array("login", "password")
	 */
	}
}
