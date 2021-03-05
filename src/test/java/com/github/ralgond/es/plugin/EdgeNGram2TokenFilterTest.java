package com.github.ralgond.es.plugin;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Analyzer.TokenStreamComponents;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.ngram.EdgeNGramTokenFilter;
import org.apache.lucene.analysis.ngram.NGramTokenFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.junit.Test;

public class EdgeNGram2TokenFilterTest {

	public class EdgeNGram2Analyzer extends Analyzer {

		@Override
		protected TokenStreamComponents createComponents(String fieldName) {
			StandardTokenizer tokenizer = new StandardTokenizer();
//			TokenFilter filters = new EdgeNGramTokenFilter(tokenizer, 1, 20, false);
			TokenFilter filters = new EdgeNGram2TokenFilter(tokenizer);
			return new TokenStreamComponents(tokenizer, filters);
		}
	}
	
	@Test
	public void test0() throws IOException {
		EdgeNGram2Analyzer analyzer = new EdgeNGram2Analyzer();
		TokenStream ts = analyzer.tokenStream("name", "the EVA EGG北京");
		CharTermAttribute term = ts.addAttribute(CharTermAttribute.class);
		OffsetAttribute offset = ts.addAttribute(OffsetAttribute.class);
		TypeAttribute typeAtt = ts.addAttribute(TypeAttribute.class);
		PositionIncrementAttribute pos = ts.addAttribute(PositionIncrementAttribute.class);
		ts.reset();
		while (ts.incrementToken()) {
			System.out.println(term.toString()+"\t"+offset.startOffset()+"\t"+offset.endOffset()+"\t"+typeAtt.type()+"\t"+pos.getPositionIncrement());
			
//			Term t = termList.poll();
//			assertEquals(t.s, term.toString());
//			assertEquals(t.start_offset, offset.startOffset());
//			assertEquals(t.end_offset, offset.endOffset());
//			assertEquals(t.type, typeAtt.type());
		}
		
		analyzer.close();
	}
}
