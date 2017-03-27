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
namespace itis\taoAigFacade\service;

/**
 * Class managing group related calls that provide a common interest to more than one service.
 *
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 1.0
 * @version 1.0.44
 */
class UserService {
	
		/**
	 * function used to retireve a user given their login.
	 *
	 * @param $login The
	 *        	login of the user to retrieve.
	 * @return The user resource matching the provided login.
	 */
	static function getUserByLogin($login) {
		$class = new \core_kernel_classes_Class ( CLASS_GENERIS_USER );
		$users = $class->searchInstances ( array (
				PROPERTY_USER_LOGIN => $login
		), array (
				'like' => false,
				'recursive' => true
		) );
		return current ( $users );
	}
}