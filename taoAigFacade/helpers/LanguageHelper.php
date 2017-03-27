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
namespace itis\taoAigFacade\helpers;

class LanguageHelper {
	public static function setDataLanguage($language = null) {
		// check language format
		if (! empty ( $language )) {
			if (self::checkDataLanguage ( $language )) {
				\common_Logger::w ( 'unsupported language format: ' . $language );
				$language = null;
			}
		}
		$session = \common_session_SessionManager::getSession ();
		$usrRsc = new \core_kernel_classes_Resource ( $session->getUserUri () );
		$propUserDataLg = new \core_kernel_classes_Property ( PROPERTY_USER_DEFLG );
		
		// get current
		$oldValue = $usrRsc->getPropertyValues ( $propUserDataLg ) [0];
		// remove all
		$usrRsc->removePropertyValues ( $propUserDataLg );
		// define new value, by default force english
		$newValue = empty ( $language ) ? 'http://www.tao.lu/Ontologies/TAO.rdf#Langen-US' : $language;
		$usrRsc->setPropertyValue ( $propUserDataLg, $newValue );
		$session->refresh ();
		return $oldValue;
	}
	public static function checkDataLanguage($language) {
		if (preg_match ( '/^[a-z]{2,2}-[A-Z]{2,2}$/', $language ) !== 1) {
			return false;
		}
		return true;
	}
}

	