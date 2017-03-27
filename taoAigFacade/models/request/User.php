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
 * User element of the request
 *
 * @author Olivier Pedretti
 */
namespace itis\taoAigFacade\models\request;

class User {
	/**
	 *
	 * @var string
	 */
	protected $login = null;
	/**
	 *
	 * @var string
	 */
	protected $pwd = null;
	protected function __construct($login, $pwd) {
		$this->login = $login;
		$this->pwd = $pwd;
	}
	/**
	 * Helper that generate 'User' from XML
	 *
	 * @param SimpleXMLElement $xmlEl        	
	 * @throws \common_Exception
	 * @return User
	 */
	public static function fromSimpleXMLElement(\SimpleXMLElement $xmlEl) {
		if (! ($xmlEl instanceof \SimpleXMLElement)) {
			throw new \common_Exception ( 'not instanceof ' . \SimpleXMLElement::class );
		}
		if ($xmlEl->getName () !== 'user') {
			throw new \common_Exception ( 'user: invalid xml' );
		}
		$login = null;
		$pwd = null;
		foreach ( $xmlEl->attributes () as $attr ) {
			switch ($attr->getName ()) {
				case 'login' :
					$login = $attr->__toString ();
					break;
				case 'pwd' :
					$pwd = $attr->__toString ();
					break;
				default :
					\common_Logger::t ( __METHOD__ . ' unsupported attribute: ' . $attr->getName () );
					break;
			}
		}
		
		return new User ( $login, $pwd );
	}
	/**
	 * Generate 'User' from an array of parameters
	 *
	 * @param array $array
	 *        	{
	 *        	user information
	 *        	@info string $login
	 *        	@info string $pwd
	 *        	}
	 * @throws \common_Exception
	 * @return User
	 */
	public static function fromArray($array) {
		if (! is_array ( $array )) {
			throw new \common_Exception ( 'user: not an array' );
		}
		if (! array_key_exists ( 'login', $array )) {
			throw new \common_Exception ( 'user: invalid format: missing: login' );
		}
		$login = $array ['login'];
		if (! array_key_exists ( 'pwd', $array )) {
			throw new \common_Exception ( 'user: invalid format: missing: pwd' );
		}
		$pwd = $array ['pwd'];
		
		return new User ( $login, $pwd );
	}
	/**
	 *
	 * @return string
	 */
	public function getLogin() {
		return $this->login;
	}
	/**
	 *
	 * @return string
	 */
	public function getPwd() {
		return $this->pwd;
	}
	/**
	 *
	 * @param string $login        	
	 * @param string $pwd        	
	 */
	public function setCredentials($login, $pwd) {
		$this->login = $login;
		$this->pwd = $pwd;
	}
}
