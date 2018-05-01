package com.playground.ner;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import edu.stanford.nlp.ie.NERClassifierCombiner;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.DefaultPaths;
import edu.stanford.nlp.util.Triple;

public class NERSample {

	public static void main(String[] args) throws Exception {
		//String serializedClassifier = "/Users/stingleff/Downloads/stanford-ner-2017-06-09/classifiers/english.all.3class.distsim.crf.ser.gz";
		//CRFClassifier<CoreLabel> classifier = CRFClassifier.getClassifier(serializedClassifier);
		ner();
	}

	private static void ner() throws Exception {
		Properties props = new Properties();
		props.setProperty("ner.applyNumericClassifiers", "false");
		props.setProperty("ner.useSUTime", "false");
		//props.setProperty("ner.model", DefaultPaths.DEFAULT_NER_THREECLASS_MODEL);
		props.setProperty("ner.model", "/Users/stingleff/Software/developer/stanford-ner-2017-06-09/classifiers/english.all.3class.distsim.crf.ser.gz");
		NERClassifierCombiner ner = NERClassifierCombiner.createNERClassifierCombiner("ner", props);

		String text = IOUtils.slurpInputStream(NERSample.class.getResourceAsStream("/html-text.txt"), "UTF-8");
		/*List<Triple<String,Integer,Integer>> foo = ner.classifyToCharacterOffsets(text);
		for (Triple<String,Integer,Integer> t : foo) {
			System.err.println(t);
			System.err.println(text.substring(t.second, t.third));
		}*/

		List<List<CoreLabel>> results = ner.classify(text);
		for (List<CoreLabel> coreLabels : results) {
			String prevLabel = null;
			String prevToken = null;
			for (CoreLabel coreLabel : coreLabels) {
				String word = coreLabel.word();
				String annotation = coreLabel.get(CoreAnnotations.AnswerAnnotation.class);
				if (!"O".equals(annotation)) {
					if (prevLabel == null) {
						prevLabel = annotation;
						prevToken = word;
					} else {
						if (prevLabel.equals(annotation)) {
							prevToken += " " + word;
						} else {
							prevLabel = annotation;
							prevToken = word;
						}
					}
				} else {
					if (prevLabel != null) {
						System.err.println("token: (" + prevToken + ") / label: (" + prevLabel + ")");
						prevLabel = null;
					}
				}
			}
		}
		/*List<List<CoreLabel>> labels = ner
				.classify(IOUtils.slurpInputStream(NERSample.class.getResourceAsStream("/html-text.txt"), "UTF-8"));
		for (List<CoreLabel> l : labels) {
			for (CoreLabel cl : l) {
				Set<Class<?>> keySet = cl.keySet();
				if (keySet.contains(CoreAnnotations.AnswerAnnotation.class)) {
					Object obj = cl.get(CoreAnnotations.AnswerAnnotation.class);
					if ((obj != null) && (!"O".equals(obj))) {
						System.err.println(cl.originalText());
						System.err.println(obj.getClass());
						System.err.println(obj.toString());
					}
				}
			}
		}*/
	}
}
