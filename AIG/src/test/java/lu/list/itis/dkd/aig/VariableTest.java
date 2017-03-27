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

import lu.list.itis.dkd.aig.resolution.TemplateConsistencyException;
import lu.list.itis.dkd.aig.resolution.TemplateParseException;
import lu.list.itis.dkd.aig.util.Externalization;

import com.google.common.collect.ArrayListMultimap;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Test class for {@link Variable}.
 *
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.8
 * @version 0.8.0
 */
@SuppressWarnings("nls")
public class VariableTest {

    private static Variable kitty;
    private static Variable kittyToo;
    private static Variable bunny;
    private static Variable turtle;
    private static Variable cat;
    private static Variable leChat;
    private static URI bunnyUri;
    private static URI kittyUri;
    private static URI turtleUri;
    private static URI turtleMediaUri;

    private static Value kittyUriValue;

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        final ArrayListMultimap<String, String> parameters = ArrayListMultimap.create();
        final ArrayList<Value> values = new ArrayList<>();
        final String french = Locale.FRENCH.getLanguage();
        final String german = Locale.GERMAN.getLanguage();
        final String english = Locale.ENGLISH.getLanguage();
        VariableTest.bunnyUri = new URI("http://www.bunny-uri.com");
        VariableTest.kittyUri = new URI("http://www.kitty-uri.com");
        VariableTest.turtleUri = new URI("http://www.turtle-uri.com");
        VariableTest.turtleMediaUri = new URI("http://www.turtle-media-uri.com");
        VariableTest.kittyUriValue = new Value(new URI("http://www.kitty-uri.com"), ValueType.URI, VariableTest.kittyUri);


        parameters.put(Externalization.IDENTIFIER_ELEMENT, "http://www.kitty.com");
        parameters.put(Externalization.LANGUAGE_ELEMENT, english);
        parameters.put(Externalization.INITIALIZATION_ELEMENT, "http://www.kitty-initialization-process.com");
        values.add(VariableTest.kittyUriValue);
        values.add(new Value(new URI("http://www.kitty-label.com"), ValueType.TEXT, "Kitty"));
        values.add(new Value(new URI("http://www.kitty-media.com"), ValueType.MEDIA));
        VariableTest.kitty = new Variable(parameters, values);
        VariableTest.kittyToo = new Variable(parameters, values);

        values.clear();
        parameters.clear();

        parameters.put(Externalization.IDENTIFIER_ELEMENT, "http://www.kitty.com");
        parameters.put(Externalization.LANGUAGE_ELEMENT, english);
        values.add(new Value(new URI("http://www.cat-uri.com"), ValueType.URI, new URI("http://www.cat-uri.com")));
        values.add(new Value(new URI("http://www.cat-label.com"), ValueType.TEXT, "Cat"));
        values.add(new Value(new URI("http://www.cat-media.com"), ValueType.MEDIA));
        VariableTest.cat = new Variable(parameters, values);

        values.clear();
        parameters.clear();

        parameters.put(Externalization.IDENTIFIER_ELEMENT, "http://www.kitty.com");
        parameters.put(Externalization.LANGUAGE_ELEMENT, french);
        parameters.put(Externalization.INITIALIZATION_ELEMENT, "http://www.leChat-initialization-process.com");
        values.add(new Value(new URI("http://www.leChat-uri.com"), ValueType.URI, new URI("http://www.leChat-uri.com")));
        values.add(new Value(new URI("http://www.leChat-label.com"), ValueType.TEXT, "Chat"));
        values.add(new Value(new URI("http://www.leChat-media.com"), ValueType.MEDIA));
        VariableTest.leChat = new Variable(parameters, values);

        values.clear();
        parameters.clear();

        parameters.put(Externalization.IDENTIFIER_ELEMENT, "http://www.bunny.com");
        parameters.put(Externalization.INITIALIZATION_ELEMENT, "http://www.bunny-initialization-process.com");
        values.add(new Value(new URI("http://www.bunny-uri.com"), ValueType.URI, VariableTest.bunnyUri));
        values.add(new Value(new URI("http://www.bunny-number.com"), ValueType.NUMBER, 1.0));
        values.add(new Value(new URI("http://www.bunny-label.com"), ValueType.TEXT, "Bunny"));
        VariableTest.bunny = new Variable(parameters, values);

        values.clear();
        parameters.clear();

        parameters.put(Externalization.IDENTIFIER_ELEMENT, "http://www.turtle.com");
        parameters.put(Externalization.LANGUAGE_ELEMENT, german);
        parameters.put(Externalization.INITIALIZATION_ELEMENT, "http://www.turtle-initialization-process.com");
        values.add(new Value(new URI("http://www.turtle-uri.com"), ValueType.URI, VariableTest.turtleUri));
        values.add(new Value(new URI("http://www.turtle-label.com"), ValueType.TEXT, "Turtle"));
        values.add(new Value(new URI("http://www.turtle-media.com"), ValueType.MEDIA, VariableTest.turtleMediaUri));
        values.add(new Value(new URI("http://www.turtle-number.com"), ValueType.NUMBER, 10.0));
        VariableTest.turtle = new Variable(parameters, values);
    }

    /**
     * Test method for {@link lu.list.itis.dkd.aig.Variable#hashCode()}.
     */
    @Test
    public final void testHashCode() {
        Assert.assertEquals("enhttp://www.kitty.com".hashCode(), VariableTest.kitty.hashCode());
        Assert.assertEquals("frhttp://www.kitty.com".hashCode(), VariableTest.leChat.hashCode());
        Assert.assertEquals(VariableTest.kitty.hashCode(), VariableTest.cat.hashCode());
    }

    /**
     * Test method for
     * {@link lu.list.itis.dkd.aig.Variable#Variable(com.google.common.collect.ArrayListMultimap, java.util.List)}
     * .
     */
    @Test
    public final void testVariable() {
        // Implicitly tested by the constructors in the set-up method.
    }

    /**
     * Test method for {@link lu.list.itis.dkd.aig.Variable#setValue(lu.list.itis.dkd.aig.Value)}.
     *
     * @throws URISyntaxException
     */
    @SuppressWarnings("null")
    @Test
    public final void testSetValue() throws URISyntaxException {
        final Value newTurtleUriForKitty = new Value(VariableTest.kittyUri, ValueType.URI, VariableTest.turtleUri);

        Assert.assertEquals(VariableTest.kittyUriValue, VariableTest.kitty.getValue(VariableTest.kittyUriValue.getIdentifier()));
        VariableTest.kitty.setValue(newTurtleUriForKitty);
        Assert.assertEquals(newTurtleUriForKitty, VariableTest.kitty.getValue(VariableTest.kittyUriValue.getIdentifier()));

        Assert.assertEquals(1.0, VariableTest.bunny.getOneValue(ValueType.NUMBER).getValue());
        Assert.assertEquals(1.0, VariableTest.bunny.getValue(new URI("http://www.bunny-number.com")).getValue());
        final Value newNumberValue = new Value(new URI("http://www.bunny-number.com"), ValueType.NUMBER, 42.0);
        VariableTest.bunny.setValue(newNumberValue);
        Assert.assertEquals(42.0, VariableTest.bunny.getOneValue(ValueType.NUMBER).getValue());
        Assert.assertEquals(42.0, VariableTest.bunny.getValue(new URI("http://www.bunny-number.com")).getValue());
    }

    /**
     * Test method for {@link lu.list.itis.dkd.aig.Variable#getValue(java.net.URI)}.
     *
     * @throws URISyntaxException
     */
    @SuppressWarnings("null")
    @Test
    public final void testGetValueURI() throws URISyntaxException {
        Assert.assertEquals(VariableTest.kittyUri, VariableTest.kitty.getValue(VariableTest.kittyUri).getValue());
        Assert.assertEquals(VariableTest.bunnyUri, VariableTest.bunny.getValue(VariableTest.bunnyUri).getValue());
    }

    /**
     * Test method for
     * {@link lu.list.itis.dkd.aig.Variable#getOneValue(lu.list.itis.dkd.aig.ValueType)}.
     */
    @SuppressWarnings("null")
    @Test
    public final void testGetValueValueType() {
        Assert.assertEquals(VariableTest.kittyUri, VariableTest.kitty.getOneValue(ValueType.URI).getValue());
        Assert.assertEquals(VariableTest.bunnyUri, VariableTest.bunny.getOneValue(ValueType.URI).getValue());
        Assert.assertEquals("Kitty", VariableTest.kitty.getOneValue(ValueType.TEXT).getValue());
    }

    /**
     * Test method for {@link lu.list.itis.dkd.aig.Variable#getValueByIdentifier(java.lang.String)}.
     *
     * @throws URISyntaxException
     * @throws TemplateParseException
     * @throws TemplateConsistencyException
     */
    @SuppressWarnings("null")
    @Test
    public final void testGetValueByIdentifier() throws URISyntaxException, TemplateParseException, TemplateConsistencyException {
        Assert.assertEquals(VariableTest.kittyUri, VariableTest.kitty.getValueByIdentifier(VariableTest.kittyUri.toString()).getValue());
        Assert.assertEquals(VariableTest.bunnyUri, VariableTest.bunny.getValueByIdentifier(VariableTest.bunnyUri.toString()).getValue());
    }

    /**
     * Test method for {@link lu.list.itis.dkd.aig.Variable#getLanguage()}.
     */
    @Test
    public final void testGetLanguage() {
        Assert.assertEquals(Locale.ENGLISH.getLanguage(), VariableTest.kitty.getLanguage());
        Assert.assertEquals(Locale.FRENCH.getLanguage(), VariableTest.leChat.getLanguage());
    }

    /**
     * Test method for {@link lu.list.itis.dkd.aig.Variable#getTextContent()}.
     */
    @Test
    public final void testGetTextContent() {
        Assert.assertEquals("Kitty", VariableTest.kitty.getTextContent());
        Assert.assertEquals("Turtle", VariableTest.turtle.getTextContent());
    }

    /**
     * Test method for {@link lu.list.itis.dkd.aig.Variable#getSemanticContent()}.
     */
    @Test
    public final void testGetSemanticContent() {
        Assert.assertEquals(VariableTest.kittyUri, VariableTest.kitty.getSemanticContent());
        Assert.assertEquals(VariableTest.bunnyUri, VariableTest.bunny.getSemanticContent());
    }

    /**
     * Test method for {@link lu.list.itis.dkd.aig.Variable#getNumericalContent()}.
     */
    @SuppressWarnings("null")
    @Test
    public final void testGetNumericalContent() {
        Assert.assertEquals(10.0, VariableTest.turtle.getNumericalContent(), 0.001);
        Assert.assertEquals(1.0, VariableTest.bunny.getNumericalContent(), 0.001);
    }

    /**
     * Test method for {@link lu.list.itis.dkd.aig.Variable#getMediaContent()}.
     */
    @Test
    public final void testGetMediaContent() {
        Assert.assertEquals(VariableTest.turtleMediaUri, VariableTest.turtle.getMediaContent());
    }

    /**
     * Test method for {@link lu.list.itis.dkd.aig.Variable#getIdentifier()}.
     */
    @Test
    public final void testGetIdentifier() {
        Assert.assertEquals(VariableTest.kitty.getIdentifier(), VariableTest.cat.getIdentifier());
        Assert.assertEquals("http://www.kitty.com", VariableTest.kitty.getIdentifier().toString());
        Assert.assertEquals("http://www.turtle.com", VariableTest.turtle.getIdentifier().toString());
        Assert.assertFalse(VariableTest.kitty.getIdentifier().equals(VariableTest.turtle.getIdentifier()));
    }

    /**
     * Test method for {@link lu.list.itis.dkd.aig.Variable#getInitializationProcessReference()}.
     */
    @SuppressWarnings("null")
    @Test
    public final void testGetInitializationProcessReference() {
        Assert.assertEquals("http://www.leChat-initialization-process.com", VariableTest.leChat.getInitializationProcessReference().toString());
    }

    /**
     * Test method for {@link lu.list.itis.dkd.aig.Variable#equals(java.lang.Object)}.
     */
    @Test
    public final void testEqualsObject() {
        Assert.assertEquals(VariableTest.kitty, VariableTest.kittyToo);
        Assert.assertEquals(VariableTest.kittyToo, VariableTest.kitty);
        Assert.assertFalse(VariableTest.kitty.equals(VariableTest.leChat));
        Assert.assertFalse(VariableTest.kittyToo.equals(VariableTest.leChat));
        Assert.assertFalse(VariableTest.kitty.equals(VariableTest.cat));
        Assert.assertFalse(VariableTest.cat.equals(VariableTest.kitty));
    }

    /**
     * Test method for
     * {@link lu.list.itis.dkd.aig.Variable#getSimilarityTo(lu.list.itis.dkd.aig.Variable)}.
     */
    @Test
    public final void testGetSimilarityTo() {
        Assert.fail("Not yet implemented"); // TODO
    }
}