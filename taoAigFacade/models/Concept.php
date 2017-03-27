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
namespace itis\taoAigFacade\models;

use itis\taoAigFacade\models\request\Word;

class Concept {
	const STATUS_SUCCESS = 'SUCCESS';
	const STATUS_FAILED = 'FAILED';
	protected $type = null;
	protected $rscs = array ();
	protected $word = null;
	protected $nbOfItemsWanted = 1;
	protected $template = null;
	protected $category = null;
	protected $parentClass = null;
	public function __construct(Word $word, $template, $category, $parentClass, $nbOfItemsWanted = 1) {
		$this->word = $word;
		$this->nbOfItemsWanted = $nbOfItemsWanted;
		$this->template = $template;
		$this->category = $category;
		$this->parentClass = $parentClass;
	}
	public function getParentClass() {
		return $this->parentClass;
	}
	public function getTemplate() {
		return $this->template;
	}
	public function getCategory() {
		return $this->category;
	}
	public function getType() {
		return $this->type;
	}
	public function setType($type) {
		$this->type = $type;
		$toto = array ();
	}
	public function getNbOfItemsWanted() {
		return $this->nbOfItemsWanted;
	}
	/**
	 *
	 * @return Word
	 */
	public function getWord() {
		return $this->word;
	}
	/**
	 *
	 * @return array[\core_kernel_classes_Resource]
	 */
	public function getRscs() {
		return $this->rscs;
	}
	/**
	 *
	 * @param
	 *        	array[\core_kernel_classes_Resource]
	 */
	public function setRscs($rscs = array()) {
		$this->rscs = $rscs;
	}
}

	