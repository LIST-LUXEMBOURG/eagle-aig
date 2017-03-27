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

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.maltparser.MaltParserService;

//import is2.parser.Parser;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;
import net.sf.hfst.FormatException;
import net.sf.hfst.Transducer;
import net.sf.hfst.TransducerAlphabet;
import net.sf.hfst.TransducerHeader;
import net.sf.hfst.UnweightedTransducer;
import net.sf.hfst.WeightedTransducer;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class ModelLoader {
	static Properties properties = NLPPropertiesFetcher.fetchProperties();
	protected static final Logger logger = Logger.getLogger(ModelLoader.class.getName());

	private static POSTaggerME englishPosTagger;
	private static POSTaggerME germanPosTagger;
	private static POSTaggerME frenchPosTagger;

	private static SentenceDetectorME englishSentenceTagger;
	private static SentenceDetectorME germanSentenceTagger;
	private static SentenceDetectorME frenchSentenceTagger;

	private static Transducer englishLemmaTagger;
	private static Transducer germanLemmaTagger;
	private static Transducer frenchLemmaTagger;

	private static MaltParserService englishDependency;
	private static MaltParserService frenchDependency;
//	private static Parser germanDependency;

	// TODO real singleton

	private static void initDependency() throws IOException {
		try {
			String resourceDir = properties.getProperty("dependency");
			Path workingDirectoryPath = Resources.createTmpDirectory(resourceDir);
			String configEnFileName = properties.getProperty("dependency.en");
			// create tmp file
			Resources.getFile(resourceDir + '/' + configEnFileName);
			String configFrFileName = properties.getProperty("dependency.fr");
			// create tmp file
			Resources.getFile(resourceDir + '/' + configFrFileName);

			logger.log(Level.INFO, "MaltParser working directory: " + workingDirectoryPath.toString());

			// TODO Remove info console stuff
			englishDependency = new MaltParserService(0);
			englishDependency.initializeParserModel("-c " + configEnFileName + " -m parse -w "
					+ workingDirectoryPath.toString() + " -lfi " + "parser-en.log");

			frenchDependency = new MaltParserService(1);
			frenchDependency.initializeParserModel("-c " + configFrFileName + " -m parse -w "
					+ workingDirectoryPath.toString() + " -lfi " + "parser-fr.log");

//			String configDeFileName = properties.getProperty("dependency.de");
//			// create tmp file
//			File deModel = Resources.getFile(resourceDir + '/' + configDeFileName);
//			germanDependency = new Parser(deModel.getPath());

		} catch (Exception e) {
			logger.log(Level.SEVERE, "MaltParser exception", e);
		}

	}

	private static void initPosTaggers() throws URISyntaxException {

		POSModel germanModel = new POSModelLoader()
				.load(Resources.getFile(properties.getProperty("opennlp.pos.german")));
		germanPosTagger = new POSTaggerME(germanModel);

		POSModel frenchModel = new POSModelLoader()
				.load(Resources.getFile(properties.getProperty("opennlp.pos.french")));
		frenchPosTagger = new POSTaggerME(frenchModel);

		POSModel englishModel = new POSModelLoader()
				.load(Resources.getFile(properties.getProperty("opennlp.pos.english")));
		englishPosTagger = new POSTaggerME(englishModel);
	}

	private static void initSentenceModels() throws IOException {

		InputStream germanModelFile = ModelLoader.class
				.getResourceAsStream(properties.getProperty("opennlp.sentence.german"));
		SentenceModel germanSentenceModel = new SentenceModel(germanModelFile);
		germanSentenceTagger = new SentenceDetectorME(germanSentenceModel);

		InputStream frenchModelFile = ModelLoader.class
				.getResourceAsStream(properties.getProperty("opennlp.sentence.french"));
		SentenceModel frenchSentenceModel = new SentenceModel(frenchModelFile);
		frenchSentenceTagger = new SentenceDetectorME(frenchSentenceModel);

		InputStream englishModelFile = ModelLoader.class
				.getResourceAsStream(properties.getProperty("opennlp.sentence.english"));
		SentenceModel englishSentModel = new SentenceModel(englishModelFile);
		englishSentenceTagger = new SentenceDetectorME(englishSentModel);
	}

	private static void initLemmaModels() throws IOException, FormatException {
		FileInputStream germanLemmafile = Resources.getFileInputStream(properties.getProperty("opennlp.lemma.german"));
		TransducerHeader germanTransducerHeader = new TransducerHeader(germanLemmafile);
		DataInputStream germanCharstream = new DataInputStream(germanLemmafile);
		TransducerAlphabet germanAlphabet = new TransducerAlphabet(germanCharstream,
				germanTransducerHeader.getSymbolCount());
		if (germanTransducerHeader.isWeighted()) {
			germanLemmaTagger = new WeightedTransducer(germanLemmafile, germanTransducerHeader, germanAlphabet);
		} else {
			germanLemmaTagger = new UnweightedTransducer(germanLemmafile, germanTransducerHeader, germanAlphabet);
		}

		FileInputStream frenchLemmafile = Resources.getFileInputStream(properties.getProperty("opennlp.lemma.french"));
		TransducerHeader frenchTransducerHeader = new TransducerHeader(frenchLemmafile);
		DataInputStream frenchCharstream = new DataInputStream(frenchLemmafile);
		TransducerAlphabet frenchAlphabet = new TransducerAlphabet(frenchCharstream,
				frenchTransducerHeader.getSymbolCount());
		if (frenchTransducerHeader.isWeighted()) {
			frenchLemmaTagger = new WeightedTransducer(frenchLemmafile, frenchTransducerHeader, frenchAlphabet);
		} else {
			frenchLemmaTagger = new UnweightedTransducer(frenchLemmafile, frenchTransducerHeader, frenchAlphabet);
		}

		FileInputStream englishLemmafile = Resources
				.getFileInputStream(properties.getProperty("opennlp.lemma.english"));
		TransducerHeader englishTransducerHeader = new TransducerHeader(englishLemmafile);
		DataInputStream englishCharstream = new DataInputStream(englishLemmafile);
		TransducerAlphabet englishAlphabet = new TransducerAlphabet(englishCharstream,
				englishTransducerHeader.getSymbolCount());
		if (englishTransducerHeader.isWeighted()) {
			englishLemmaTagger = new WeightedTransducer(englishLemmafile, englishTransducerHeader, englishAlphabet);
		} else {
			englishLemmaTagger = new UnweightedTransducer(englishLemmafile, englishTransducerHeader, englishAlphabet);
		}
	}

	static {
		try {
			initPosTaggers();
			initSentenceModels();
			initLemmaModels();
			initDependency();
		} catch (IOException | URISyntaxException | FormatException e) {
			logger.log(Level.SEVERE, "Model initialisation failed!", e);
		}
	}

	/**
	 * Returns the Lemmatizer models for English, German and French.
	 * 
	 * @param language
	 * @return
	 * @throws IOException
	 */
	public static Transducer getLemmaModel(Language language) throws IOException {
		switch (language) {
		case DE:
			return germanLemmaTagger;
		case FR:
			return frenchLemmaTagger;
		case EN:
			return englishLemmaTagger;
		default:
			logger.log(Level.INFO, "Language could not be recognized. Hence, no dependency Model could be returned.");
			return null;
		}
	}

	/**
	 * Returns the sentence detection models for English, German and French.
	 * 
	 * @param language
	 */
	public static SentenceDetectorME getSentenceModel(Language language) {
		switch (language) {
		case DE:
			return germanSentenceTagger;
		case FR:
			return frenchSentenceTagger;
		case EN:
			return englishSentenceTagger;
		default:
			logger.log(Level.INFO, "Language could not be recognized. Hence, no dependency Model could be returned.");
			return null;
		}
	}

	/**
	 * Returns the pos tagger models for English, German and French.
	 * 
	 * @param language
	 */
	public static POSTaggerME getPosModel(Language language) {
		switch (language) {
		case DE:
			return germanPosTagger;
		case FR:
			return frenchPosTagger;
		case EN:
			return englishPosTagger;
		default:
			logger.log(Level.INFO, "Language could not be recognized. Hence, no dependency Model could be returned.");
			return null;
		}
	}

	/**
	 * Returns the dependency model for German, which is based on the
	 * MateParser. The dependency models for French and English, are based on
	 * the MaltParser. Hence, it can be obtained by calling the
	 * "getDependencyModels" function.
	 * 
	 * @param language
	 */
	public static Object getGermanDependencyModel(Language language) {
		switch (language) {
		case DE:
			logger.log(Level.WARNING,
					"The german dependency parser is not yet available (waiting for dependency webservice due to parser licenses)");
			return null;
		default:
			logger.log(Level.INFO,
					"To get the dependency Parser for English and French, please use getDependencyModels, which are based on the maltparser.");
			return null;
		}
	}

	/**
	 * Returns the dependency models for French and English, which are based on
	 * the MaltParser. The dependency model for German is based on the
	 * MateParser. Hence, it can be obtained by calling the
	 * "getGermanDependencyModel" function.
	 * 
	 * @param language
	 */
	public static MaltParserService getDependencyModels(Language language) {
		switch (language) {
		case DE:
			logger.log(Level.INFO,
					"The german dependency parser is not yet available with the maltparser. Please use getGermanDepencyModel, which is based on Mate Parser.");
			return null;
		case FR:
			return frenchDependency;
		case EN:
			return englishDependency;
		default:
			logger.log(Level.INFO, "Language could not be recognized. Hence, no dependency Model could be returned.");
			return null;
		}
	}
}
