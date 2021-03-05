package com.github.ralgond.es.plugin;

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;

public final class EdgeNGram2TokenFilter extends TokenFilter {

	protected EdgeNGram2TokenFilter(TokenStream input) {
		super(input);
	}
	
	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
	private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
	private final PositionIncrementAttribute posIncrAtt = addAttribute(PositionIncrementAttribute.class);
	
	static class EdgeNGram2Context {
		public boolean open = false;
		public int start;
		public int len;
		public int curr;
		public char[] buffer = null;
	}
	
	EdgeNGram2Context ctx = new EdgeNGram2Context();
	
	@Override
	public boolean incrementToken() throws IOException {
		while (true) {
			if (ctx.open) {
				ctx.curr++;
				if (ctx.curr > ctx.len) {
					ctx.open = false;
					ctx.buffer = null;
					continue;
				}
				
				if (ctx.curr == 1)
					posIncrAtt.setPositionIncrement(1);
				else
					posIncrAtt.setPositionIncrement(0);
				
				offsetAtt.setOffset(ctx.start, ctx.start + ctx.curr);
				termAtt.copyBuffer(ctx.buffer, 0, ctx.curr);
				
				return true;
			} else {
				if (!input.incrementToken())
					return false;
				
				ctx.open = true;
				ctx.start = offsetAtt.startOffset();
				ctx.len = termAtt.length();
				ctx.curr = 0;
				ctx.buffer = termAtt.buffer().clone();
				
				continue;
			}
		}
	}

}
