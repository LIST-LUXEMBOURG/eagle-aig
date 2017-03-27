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
 * Helper to handle all resources related to items involved in the test generation
 *
 * @author Olivier Pedretti
 *        
 */
namespace itis\taoAigFacade\models;

class ItemForTest {
	/**
	 *
	 * @var string
	 */
	protected $uri = null;
	protected $properties = null;
	public function __construct($uri, $properties = array()) {
		$this->uri = $uri;
		$this->properties = $properties;
	}
	public function getUri() {
		return $this->uri;
	}
	public function getProperties() {
		return $this->properties;
	}
}

	