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
 * Add namespaces to TAO autoloader file
 */
$autoloaderFile = VENDOR_PATH . 'composer/autoload_psr4.php';
$content = file_get_contents ( $autoloaderFile );
$arrayStartInFile = 'return array(';
if (strpos ( $content, $arrayStartInFile ) === false) {
	throw new common_exception_PreConditionFailure ( 'Bad content in: ' . $autoloaderFile );
}

$extension = common_ext_ExtensionsManager::singleton ()->getExtensionById ( EXT_NAME );

$composerFile = $extension->getDir () . DIRECTORY_SEPARATOR . 'composer.json';
$composerFileContent = file_get_contents ( $composerFile );
$composerJson = json_decode ( $composerFileContent, true );

$namespaces = array_keys ( $composerJson ['autoload'] ['psr-4'] );
if (empty ( $namespaces )) {
	common_Logger::w ( 'no namespace to add found' );
	return;
}
$modifyFile = false;
foreach ( $namespaces as $namespace ) {
	$line = '    \'' . str_replace ( '\\', '\\\\', $namespace ) . '\' => array($baseDir . \'/' . $extension->getId () . '\'),';
	if (strpos ( $content, $line ) !== false) {
		common_Logger::w ( 'namespace already exists: ' . $namespace );
		continue;
	}
	$lineToAdd = PHP_EOL . '    ' . $line;
	$content = str_replace ( $arrayStartInFile, $arrayStartInFile . $lineToAdd, $content );
	$modifyFile = true;
	common_Logger::d ( 'namespace added: ' . $namespace );
}
if ($modifyFile) {
	$content = file_put_contents ( $autoloaderFile, $content );
}
