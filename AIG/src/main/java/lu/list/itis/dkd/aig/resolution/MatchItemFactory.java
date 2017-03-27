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
package lu.list.itis.dkd.aig.resolution;

import lu.list.itis.dkd.aig.match.Match;
import lu.list.itis.dkd.dbc.annotation.NonNullByDefault;

import org.apache.jena.query.QueryParseException;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.xml.xpath.XPathExpressionException;

/**
 *
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.0
 * @version 0.8.0
 */
@NonNullByDefault
public class MatchItemFactory extends ItemFactory {

    private final List<Match> matches = new ArrayList<>();

    /**
     * Constructor initialising all fields.
     *
     * @param template
     *        The template XML that will be parsed and stored locally.
     * @param input
     *        A map of inputs. The keys should correspond to inputs variables as defined by the
     *        provided template. All variables need to be present.
     * @throws ResolutionException
     *         Thrown when at least one of the outcome keys was not backed by a variable declaration
     *         which caused the variable to be unknown to the system.
     * @throws TemplateConsistencyException
     *         Thrown when inconsistencies in the provided variables, their identifiers, or their
     *         mapping caused an exception while trying to construct an item.
     */
    public MatchItemFactory(final Template template, final Map<String, String> input, final int maximumNumberOfItems) throws ResolutionException, TemplateConsistencyException {
        super(template, input, maximumNumberOfItems);

        try {
            groundProvidedChoices();

            if (matches.isEmpty()) {
                ItemFactory.logger.log(Level.WARNING, "No matches were produced, hence, no choices were ground from the inputs!");
            }

        } catch (final URISyntaxException e) {
            ItemFactory.logger.log(Level.SEVERE, "Unable to ground matches! Error while constructing a URI. No items generated!", e);
            return;
        } catch (final XPathExpressionException e) {
            ItemFactory.logger.log(Level.SEVERE, "Unable to compile XPath to extract match resources. No items generated!", e);
            return;
        } catch (final QueryParseException e) {
            ItemFactory.logger.log(Level.SEVERE, "Unable to partse the query. Unallowed symbols. Please verify your inputs arguments and remove all prohibited characters!", e);
            return;
        }
    }

    /**
     * Method used to resolve provided matches.
     *
     * @throws XPathExpressionException
     * @throws URISyntaxException
     */
    @Deprecated
    private void groundProvidedChoices() throws XPathExpressionException, URISyntaxException {
        // XPath xPath = XPathFactory.newInstance().newXPath();
        // XPathExpression expression = xPath.compile(MatchItemFactory.PROVIDED_MATCH_QUERY_XPATH);
        //
        // final String queryString = (String) expression.evaluate(template, XPathConstants.STRING);
        // final Query query = QueryFactory.create(resolveQueryInput(queryString));
        //
        // xPath = XPathFactory.newInstance().newXPath();
        // expression = xPath.compile(MatchItemFactory.PROVIDED_MATCH_DATASOURCE_XPATH);
        // final QueryExecution queryExecution = QueryExecutionFactory.sparqlService((String)
        // expression.evaluate(template, XPathConstants.STRING), query);
        //
        // try {
        // final ResultSet result = queryExecution.execSelect();
        // while (result.hasNext()) {
        // final QuerySolution solution = result.nextSolution();
        //
        // final String[] concept =
        // solution.get(MatchItemFactory.CONCEPT_LABEL).toString().replace("&", "&amp;").split("@");
        // final URI conceptURI = new URI(solution.get(MatchItemFactory.CONCEPT_URI).toString());
        //
        // final String[] correctOption =
        // solution.get(MatchItemFactory.CORRECT_OPTION_LABEL).toString().replace("&",
        // "&amp;").split("@");
        // final URI correctOptionURI = new
        // URI(solution.get(MatchItemFactory.CORRECT_OPTION_URI).toString());
        //
        // Objects.requireNonNull(correctOption[0]);
        // Objects.requireNonNull(concept[0]);
        //
        // final Match match = new Match(new CompositeVariable(concept[0], concept.length > 1 ?
        // concept[1] : "en", conceptURI), new CompositeVariable(correctOption[0],
        // correctOption.length > 1 ? correctOption[1] : "en", correctOptionURI), null);
        // matches.add(match);
        // }
        // } finally {
        // queryExecution.close();
        // }
    }

    /** {@inheritDoc} */
    @Override
    public List<String> buildItems() throws TemplateParseException, TemplateConsistencyException, ResolutionException {
        final List<String> items = new ArrayList<>();


        return items;
    }


    /**
     * {@inheritDoc} <br>
     * <br>
     * The method will return false if the number of remaining matches does not permit to populate a
     * full set of variables for the matching process.
     */
    @Deprecated
    public boolean resolveVariables() {
        // if (matches.isEmpty() || (matches.size() < 4)) {
        // return false;
        // }
        //
        // final List<Integer> mapping = Lists.newArrayList(1, 2, 3, 4);
        // Collections.shuffle(matches);
        // Collections.shuffle(mapping);
        //
        // // One cannot couple the number to the length of the matches as the template expects four
        // // variables to have been populated.
        // for (int i = 1; i <= 4; i++) {
        // final Match match = matches.get(i - 1);
        //
        // variables.get("conceptCode" + i).setValue("_" + match.getKeyVariable().getLabel());
        // variables.get("conceptLabel" + i).setValue(match.getKeyVariable().getLabel());
        //
        // variables.get("correctOptionCode" + i).setValue("_" +
        // match.getAnswerVariable().getLabel());
        // variables.get("optionCode" + mapping.get(i - 1)).setValue("_" +
        // match.getAnswerVariable().getLabel());
        // variables.get("optionLabel" + mapping.get(i -
        // 1)).setValue(match.getAnswerVariable().getLabel());
        //
        // }
        // matches.clear();
        return true;
    }
}