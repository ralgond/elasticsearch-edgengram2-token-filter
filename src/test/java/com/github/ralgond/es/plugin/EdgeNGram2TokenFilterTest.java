package com.github.ralgond.es.plugin;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.LinkedList;

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

	static class Term {
		public String s;
		public int start_offset;
		public int end_offset;
		public String type;
		public int pos;
		public Term(String s, int start_offset, int end_offset, String type, int pos) {
			super();
			this.s = s;
			this.start_offset = start_offset;
			this.end_offset = end_offset;
			this.type = type;
			this.pos = pos;
		}
	}
	
	public class EdgeNGram2Analyzer extends Analyzer {

		@Override
		protected TokenStreamComponents createComponents(String fieldName) {
			StandardTokenizer tokenizer = new StandardTokenizer();
			TokenFilter filters = new EdgeNGram2TokenFilter(tokenizer);
			return new TokenStreamComponents(tokenizer, filters);
		}
	}
	
	@Test
	public void test0() throws IOException {
		
		LinkedList<Term> termList = new LinkedList<>();
		termList.add(new Term("t", 0, 1, "<ALPHANUM>", 1));
		termList.add(new Term("th", 0, 2, "<ALPHANUM>", 0));
		termList.add(new Term("the", 0, 3, "<ALPHANUM>", 0));
		termList.add(new Term("E", 4, 5, "<ALPHANUM>", 1));
		termList.add(new Term("EV", 4, 6, "<ALPHANUM>", 0));
		termList.add(new Term("EVA", 4, 7, "<ALPHANUM>", 0));
		termList.add(new Term("E", 8, 9, "<ALPHANUM>", 1));
		termList.add(new Term("EG", 8, 10, "<ALPHANUM>", 0));
		termList.add(new Term("EGG", 8, 11, "<ALPHANUM>", 0));
		termList.add(new Term("京", 11, 12, "<IDEOGRAPHIC>", 1));
		
		EdgeNGram2Analyzer analyzer = new EdgeNGram2Analyzer();
		TokenStream ts = analyzer.tokenStream("name", "the EVA EGG京");
		CharTermAttribute term = ts.addAttribute(CharTermAttribute.class);
		OffsetAttribute offset = ts.addAttribute(OffsetAttribute.class);
		TypeAttribute typeAtt = ts.addAttribute(TypeAttribute.class);
		PositionIncrementAttribute pos = ts.addAttribute(PositionIncrementAttribute.class);
		ts.reset();
		while (ts.incrementToken()) {
			//System.out.println(term.toString()+"\t"+offset.startOffset()+"\t"+offset.endOffset()+"\t"+typeAtt.type()+"\t"+pos.getPositionIncrement());
			
			Term t = termList.poll();
			assertEquals(t.s, term.toString());
			assertEquals(t.start_offset, offset.startOffset());
			assertEquals(t.end_offset, offset.endOffset());
			assertEquals(t.type, typeAtt.type());
			assertEquals(t.pos, pos.getPositionIncrement());
		}
		
		analyzer.close();
	}
}
