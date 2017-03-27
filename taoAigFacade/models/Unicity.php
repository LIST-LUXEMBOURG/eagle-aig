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
 *        
 */
namespace itis\taoAigFacade\models;

class Unicity {
	const UNICITY_SEPARATOR = ' - ';
	protected $constraints = null;
	protected $list = [ ];
	public function __construct($constraints = array()) {
		$this->constraints = $constraints;
		if (empty ( $constraints )) {
			\common_Logger::w ( 'no parameter for unicity' );
		}
	}
	public function getList() {
		return $this->list;
	}
	protected function buildUnicityString($infos) {
		$result = '';
		foreach ( $this->constraints as $constraint ) {
			if (isset ( $infos [$constraint] )) {
				$result .= $infos [$constraint];
			}
			$result .= self::UNICITY_SEPARATOR;
		}
		return $result;
	}
	public function addToList($properties = array()) {
		$unicityString = self::buildUnicityString ( $properties );
		if (isset ( $this->list [$unicityString] )) {
			return false;
		}
		$this->list [$unicityString] = 1;
		return true;
	}
}