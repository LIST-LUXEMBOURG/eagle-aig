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
package lu.list.itis.dkd.assess.opennlp.util;

import org.junit.Test;

import org.junit.Assert;

import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

public class LanguageHelperTest {

	@Test
	public void testDetection() {

		Assert.assertEquals(
				LanguageHelper.detectLanguage(
						"La route commença à s’enfoncer. On ne voyait plus les grillages sur les cotés, mais juste des rochers. Et soudain, les voitures s’arrêtèrent devant une énorme porte en métal entourée de béton. D’énormes blocs de béton. David avait visité d’anciennes fortifications de la ligne Maginot, mais rien de semblable. Même le Simserhof, situé à proximité de Bitche, semblait petit à côté de cette porte. Mais David n’était pas au bout de sa surprise.Je m’en rappellerais si j’avais créé un programme capable de parler. Et puis tiens, je suis en train de taper la causette avec un ordinateur ! Je deviens vraiment cinglé ! C’est fini, j’arrête l’informatique !C’est lui aussi qui était à la base du dernier processeur, le sphéro. Un processeur ayant une architecture en forme de sphère et capable de traiter les informations à une vitesse jamais atteinte. Tous les ordinateurs en étaient équipés. Le créateur officiel, le Dr. Stewart Davis, n’était bien sûr pas au courant de la présence de Prélude dans son projet. Prélude avait simplement suggéré légèrement au Dr. En modifiant légèrement ses documents.Florence est très excité à l’idée de se brancher sur un réseau militaire, mais en même temps, elle sait que cela va lui apporter des ennuis. Au moins, elle saura. Elle saura si David l’aime. Et en préparant le matériel demandé par Prélude, tout en pensant à David, elle se rappelle comment elle en est arrivée là.« A la base militaire du 57e RA ? Mais qu’est-ce que j’ai à voir avec les militaires ? » David se rappelle y avoir fait un séjour alors qu’il avait vingt-quatre ans. Il avait fait tout son possible pour éviter le service militaire, encore en vogue à l’époque, mais lorsqu’on lui avait proposé de travailler sur des projets informatiques secret défense, il n’avait pas su résister. Non pas que c’était passionnant, mais au moins, il ne faisait pas trop de sortie et il était tranquillement installé dans un bureau avec le matériel dont il rêvait."),
				Language.FR);

		Assert.assertEquals(
				LanguageHelper.detectLanguage(
						"Berlin Zum Anhören bitte klicken! [bɛɐ̯ˈliːn] ist die Bundeshauptstadt der Bundesrepublik Deutschland[13] und zugleich eines ihrer Länder. Die Stadt Berlin ist mit gut 3,5 Millionen Einwohnern die bevölkerungsreichste und mit 892 Quadratkilometern die flächengrößte Gemeinde Deutschlands. Sie bildet das Zentrum der Metropolregion Berlin/Brandenburg (6 Millionen Einw.) und der Agglomeration Berlin (4,4 Millionen Einw.). Der Stadtstaat besteht aus zwölf Bezirken. Neben den Flüssen Spree und Havel befinden sich im Stadtgebiet kleinere Fließgewässer sowie zahlreiche Seen und Wälder.Urkundlich erstmals im 13. Jahrhundert erwähnt, war Berlin im Verlauf der Geschichte und in verschiedenen Staatsformen Residenz- und Hauptstadt Brandenburgs, Preußens und des Deutschen Reichs. Ab 1949 war der Ostteil der Stadt faktisch Hauptstadt der Deutschen Demokratischen Republik. Mit der deutschen Wiedervereinigung im Jahr 1990 wurde Berlin wieder gesamtdeutsche Hauptstadt und in der Folge Sitz der Bundesregierung, des Bundespräsidenten, des Bundestages, des Bundesrates sowie zahlreicher Bundesministerien und Botschaften.Zu den bedeutenden Wirtschaftszweigen in Berlin gehören unter anderem der Tourismus, die Kreativ- und Kulturwirtschaft, die Biotechnologie und Gesundheitswirtschaft mit Medizintechnik und pharmazeutischer Industrie, die Informations- und Kommunikationstechnologien, die Bau- und Immobilienwirtschaft, der Handel, die Optoelektronik, die Energietechnik sowie die Messe- und Kongresswirtschaft. Die Stadt ist ein europäischer Knotenpunkt des Schienen- und Luftverkehrs. Berlin zählt zu den aufstrebenden, internationalen Zentren für innovative Unternehmensgründer und verzeichnet jährlich hohe Zuwachsraten bei der Zahl der Erwerbstätigen.[14]Berlin gilt als Weltstadt der Kultur, Politik, Medien und Wissenschaften.[15][16][17][18] Die Universitäten, Forschungseinrichtungen, Sportereignisse[19] und Museen Berlins genießen internationalen Ruf. Die Metropole trägt den UNESCO-Titel Stadt des Designs und ist eines der meistbesuchten Zentren des Kontinents.[20] Berlins Architektur, Festivals, Nachtleben und vielfältige Lebensbedingungen sind weltweit bekannt."),
				Language.DE);

	}
}
