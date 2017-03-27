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
package lu.list.itis.dkd.aig;

import lu.list.itis.dkd.aig.cloze.AllClozeTests;
import lu.list.itis.dkd.aig.match.AllMatchTests;
import lu.list.itis.dkd.aig.mcq.McqTests;
import lu.list.itis.dkd.aig.resolution.ResolutionTests;
import lu.list.itis.dkd.aig.util.AllUtilityTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test suite to run all tests in the main AIG test package.
 *
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.0
 * @version 0.9.0
 */
@RunWith(Suite.class)
@SuiteClasses({VariableTest.class, SimilarityProviderTest.class,
                AllMatchTests.class, McqTests.class, ResolutionTests.class, AllUtilityTests.class, AllClozeTests.class})
public class AigTests {
    /** Test suits feature an empty body! */
}