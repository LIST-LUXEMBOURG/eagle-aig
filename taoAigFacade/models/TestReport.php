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

use itis\taoAigFacade\helpers\XmlHelper;
use itis\taoAigFacade\models\TestGenerator;
use oat\taoQtiItem\model\qti\ImportService;

class TestReport {
	public static function includeReport(\DOMElement $destination, $addPrintLink, $testName, $itemListByTypes) {
		$reportUri = self::getReportUri ( $testName, $addPrintLink );
		
		$template = '<assessmentSection identifier="inTestReport" required="false" fixed="false" title="Rapport" visible="true" keepTogether="true">
			<itemSessionControl maxAttempts="1" showFeedback="false" allowReview="true" showSolution="false" allowComment="true" allowSkipping="false" validateResponses="false"/>
			<rubricBlock view="candidate" use="sharedstimulus">
				<stylesheet href="rubrickBlock.css" title="" type="text/css" media="all"/>
			</rubricBlock>
			<assessmentItemRef identifier="inTestReport0i0" required="true" fixed="false" href="' . $reportUri . '"/>
		</assessmentSection>';
		$assessmentSection = XmlHelper::addNodeFromXmlString ( $destination, $template, false );
		
		$rubricBlock = $assessmentSection->getElementsByTagName ( 'rubricBlock' )->item ( 0 );
		
		$template = '<div>
						<p>Votre score est de <strong><printedVariable identifier="' . TestGenerator::TEST_VARIABLE_GLOBAL_SCORE . '" format="%i" /></strong> bonne(s) réponse(s) sur <printedVariable identifier="' . TestGenerator::TEST_VARIABLE_NB_ITEMS_PRESENTED . '" format="%i" />.</p>
					</div>';
		$div = XmlHelper::addNodeFromXmlString ( $rubricBlock, $template );
		
		// add results by item types, only if more than 1 type
		if (count ( $itemListByTypes ) > 1) {
			foreach ( $itemListByTypes as list ( $itemType, $itemsForTest, $sectionId ) ) {
				$template = '<p>' . $itemType->getValue () . ' : <printedVariable identifier="' . TestGenerator::getSectionScoreVariableName ( $sectionId ) . '" format="%i" /> bonnes réponses sur <printedVariable identifier="' . TestGenerator::getSectionNbItemsPresentedVariableName ( $sectionId ) . '" format="%i" /></p>';
				XmlHelper::addNodeFromXmlString ( $div, $template );
			}
		}
		
		$template = '<p>Afin d\'enregistrer vos résultats, merci de cliquer sur "Terminer le test" !</p>';
		XmlHelper::addNodeFromXmlString ( $div, $template );
	}
	protected static function getReportUri($testName, $addPrintLink) {
		$uri = self::createReport ( $testName, $addPrintLink );
		return $uri;
	}
	protected static function copyReportContent(\core_kernel_classes_Resource $item) {
		$testContentFolder = \taoItems_models_classes_ItemsService::singleton ()->getItemFolder ( $item, \common_session_SessionManager::getSession ()->getDataLanguage () );
		
		$filename = 'printReport.html';
		$srcPath = DIR_VIEWS . 'html' . DIRECTORY_SEPARATOR . $filename;
		$destPath = $testContentFolder . $filename;
		if (! copy ( $srcPath, $destPath )) {
			throw new \common_exception_FileSystemError ( __METHOD__ . ' failed, src: ' . $srcPath . ' dest: ' . $destPath );
		}
		
		$filename = 'printReport.png';
		$srcPath = DIR_VIEWS . 'img' . DIRECTORY_SEPARATOR . $filename;
		$destPath = $testContentFolder . $filename;
		if (! copy ( $srcPath, $destPath )) {
			throw new \common_exception_FileSystemError ( __METHOD__ . ' failed, src: ' . $srcPath . ' dest: ' . $destPath );
		}
		
		$filename = 'reportItem.css';
		$srcPath = DIR_VIEWS . 'css' . DIRECTORY_SEPARATOR . $filename;
		$destPath = $testContentFolder . 'style' . DIRECTORY_SEPARATOR . 'custom' . DIRECTORY_SEPARATOR . $filename;
		$destDir = dirname ( $destPath );
		if (! is_dir ( $destDir )) {
			if (! mkdir ( $destDir, 0770, true )) {
				throw new \common_exception_FileSystemError ( __METHOD__ . ' failed mkdir: ' . $destDir );
			}
		}
		if (! copy ( $srcPath, $destPath )) {
			throw new \common_exception_FileSystemError ( __METHOD__ . ' failed, src: ' . $srcPath . ' dest: ' . $destPath );
		}
	}
	protected static function createReport($testName, $addPrintLink) {
		$dataLg = \common_session_SessionManager::getSession ()->getDataLanguage ();
		$title = $testName;
		$printLink = ($addPrintLink) ? '<object data="printReport.html" type="text/html"/>' : '';
		$template = '<assessmentItem xmlns="http://www.imsglobal.org/xsd/imsqti_v2p1" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.imsglobal.org/xsd/imsqti_v2p1  http://www.imsglobal.org/xsd/qti/qtiv2p1/imsqti_v2p1.xsd" identifier="REPORT" title="' . $title . '" label="" xml:lang="' . $dataLg . '" adaptive="false" timeDependent="false" toolName="TAO" toolVersion="2.6.7">
		 		<stylesheet href="style/custom/reportItem.css" type="text/css" media="all" title="" />
				<outcomeDeclaration identifier="SCORE" cardinality="single" baseType="float" />
		  		<itemBody><div>' . $printLink . '</div></itemBody>
		  <responseProcessing/></assessmentItem>';
		
		$tmpDoc = new \DOMDocument ();
		$tmpDoc->loadXML ( $template );
		$reportsClass = new \core_kernel_classes_Class ( TAO_AIG_FACADE_MODEL_TESTREPORTCLASS );
		// $oldValue = self::setDataLanguage();
		$report = ImportService::singleton ()->importQTIFile ( $tmpDoc->saveXML (), $reportsClass, false );
		$itemRsc = self::parseReport ( $report );
		self::copyReportContent ( $itemRsc );
		// self::setDataLanguage($oldValue);
		
		$uri = $itemRsc->getUri ();
		return $uri;
	}
	protected static function parseReport(\common_report_Report $report) {
		if ($report->containsError ()) {
			throw new \common_exception_Error ( $report->getMessage () );
		}
		return $report->getData ();
	}
}
