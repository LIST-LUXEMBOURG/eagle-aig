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
package lu.list.itis.dkd.aig.cloze.gap;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import lu.list.itis.dkd.aig.cloze.generation.AllClozeGenerationTests;
import lu.list.itis.dkd.aig.cloze.koda.KodaTest;

/**
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.9
 * @version 0.9.2
 */
@RunWith(Suite.class)
@SuiteClasses({GapFinderTest.class, AllClozeGenerationTests.class, KodaTest.class})
public class AllGapTests {}