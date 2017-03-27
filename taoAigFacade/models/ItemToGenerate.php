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

use itis\taoAigFacade\models\request\Word;
use itis\taoAigFacade\models\siren\Siren;

class ItemToGenerate {
	/**
	 *
	 * @var Siren $type Type of item to be generated
	 */
	protected $type = null;
	/**
	 *
	 * @var \core_kernel_classes_Resource $rsc Item
	 */
	protected $rsc = null;
	/**
	 *
	 * @var Word $word
	 */
	protected $word = null;
	/**
	 *
	 * @param Word $word        	
	 * @param string $type        	
	 * @see FacadeService
	 */
	public function __construct(Word $word, $type = FacadeService::ITEM_TYPE_VOC_TR_MCQ) {
		$this->word = $word;
		$this->type = $type;
	}
	/**
	 *
	 * @return Siren
	 */
	public function getType() {
		return $this->type;
	}
	/**
	 *
	 * @param unknown $type        	
	 */
	public function setType($type) {
		$this->type = $type;
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
	 * @return \core_kernel_classes_Resource
	 */
	public function getRsc() {
		return $this->rsc;
	}
	/**
	 *
	 * @param \core_kernel_classes_Resource $rsc        	
	 */
	public function setRsc(\core_kernel_classes_Resource $rsc) {
		$this->rsc = $rsc;
	}
}

	