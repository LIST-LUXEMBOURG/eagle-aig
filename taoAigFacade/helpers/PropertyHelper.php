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
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 1.0
 * @version 1.0.36
 */
namespace itis\taoAigFacade\helpers;

class PropertyHelper {
	public static function updatePropertyValue(\core_kernel_classes_Resource $resource, \core_kernel_classes_Property $property, $value = null) {
		if(empty($value)) {
			\common_Logger::w ( 'Empty value, leaving default value.' );
			return;
		}
		
		if(empty($property)) {
			\common_Logger::w ( 'The provided property may not be empty.' );
			return;
		}
		
		$oldValue = $resource->getPropertyValues ( $property ) [0];
		$resource->removePropertyValues ( $property );
		$resource->setPropertyValue ( $property, $value );
		return $oldValue;
	}	
}
	