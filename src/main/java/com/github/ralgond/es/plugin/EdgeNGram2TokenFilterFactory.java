package com.github.ralgond.es.plugin;

import org.apache.lucene.analysis.TokenStream;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;
import org.elasticsearch.index.analysis.AbstractTokenFilterFactory;
import org.elasticsearch.index.analysis.TokenFilterFactory;


public class EdgeNGram2TokenFilterFactory extends AbstractTokenFilterFactory {

	public EdgeNGram2TokenFilterFactory(IndexSettings indexSettings, String name, Environment env, Settings settings) {
		super(indexSettings, name, settings);
	}
	
	public static TokenFilterFactory getTokenFilterFactory(IndexSettings indexSettings, Environment environment, String name, Settings settings) {
        return new EdgeNGram2TokenFilterFactory(indexSettings, name, environment, settings);
    }

	public TokenStream create(TokenStream tokenStream) {
		return new EdgeNGram2TokenFilter(tokenStream);
	}

}
