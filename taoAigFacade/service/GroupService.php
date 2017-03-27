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

use oat\taoGroups\models\GroupsService;

/**
 * Class managing group related calls that provide a common interest to more than one service.
 *
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 1.0
 * @version 1.0.45
 */
class GroupService {
	
	/**
	 * Method used to create a group if it does not already exist.
	 * The call will also add the user with the provided login if provided.
	 *
	 * @param String $groupName
	 *        	The name of the group to create.
	 * @param String $login
	 *        	The login of the user to add.
	 * @throws \common_exception_PreConditionFailure Thrown if more than one group with the given name existed already. This is a logical error and should never occur. It is due to how TAO handles resource properties.
	 */
	public static function createGroup($groupName, $login = null) {
		$groupService = GroupsService::singleton ();
		$groupResource = null;
		// Retrieve all groups matching the given group name.
		$groupResourcesMatchingLabel = $groupService->searchInstances ( array (
				RDFS_LABEL => $groupName 
		), GroupsService::singleton ()->getRootClass (), array (
				'recursive' => true,
				'like' => false
				
		) );
		
		if (empty ( $groupResourcesMatchingLabel )) { // Create group if it does not exist.
			$groupResource = $groupService->createInstance ( $groupService->getRootClass (), $groupName );
		} else { // Get the first element of the groups matching the label and throw an exception if there are more than one (logical fallacy).
			if (sizeof ( $groupResourcesMatchingLabel ) > 1) {
				throw new \common_exception_PreConditionFailure ( 'More than one group matching the name existed!' );
			}
			$groupResource = current ( $groupResourcesMatchingLabel );
		}
		
		if (isset ( $login )) {
			$groupService->addUser ( UserService::getUserByLogin ( $login ), $groupResource );
		}
	}
	public static function searchGroupsByName($groupName) {
		$groupService = GroupsService::singleton ();
		$groupResourcesMatchingLabel = $groupService->searchInstances ( [ 
				RDFS_LABEL => $groupName 
		], GroupsService::singleton ()->getRootClass (), [ 
				'recursive' => true,
				'like' => false 
		] );
		return $groupResourcesMatchingLabel;
	}
}
