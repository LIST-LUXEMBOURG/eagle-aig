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
namespace itis\taoAigFacade\models;

/**
 *
 * @author Olivier Pedretti
 *        
 */
class TestBuilderService extends \taoQtiTest_models_classes_QtiTestService {
	public function __construct() {
		parent::__construct ();
	}
	/**
	 *
	 * @param string $name
	 *        	Label of the test
	 * @param \core_kernel_classes_Resource[] $items
	 *        	Items to add to the test
	 * @throws \InvalidArgumentException if argument $items is not of type 'array'
	 * @throws \common_exception_Error if failed to add items to the test
	 * @return \core_kernel_classes_Resource The generated test
	 */
	public function generateTest($name, $items = array()) {
		if (! is_array ( $items )) {
			throw new \InvalidArgumentException ( 'not an array' );
		}
		if (! $this->areResources ( $items )) {
			throw new \InvalidArgumentException ( 'not resources' );
		}
		$testClass = $this->getRootclass ();
		$test = $this->createInstance ( $testClass, $name );
		$result = $this->setItems ( $test, $items );
		if (! $result) {
			throw new \common_exception_Error ( 'failed to set items' );
		}
		return $test;
	}
	protected function areResources($rs) {
		foreach ( $rs as $rsc ) {
			if (! ($rsc instanceof \core_kernel_classes_Resource)) {
				return false;
			}
		}
		return true;
	}
}