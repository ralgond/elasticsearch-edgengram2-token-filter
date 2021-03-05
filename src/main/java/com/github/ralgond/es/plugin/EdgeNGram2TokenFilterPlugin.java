package com.github.ralgond.es.plugin;

import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.index.analysis.TokenFilterFactory;
import org.elasticsearch.indices.analysis.AnalysisModule.AnalysisProvider;
import org.elasticsearch.plugins.AnalysisPlugin;
import org.elasticsearch.plugins.Plugin;

public class EdgeNGram2TokenFilterPlugin extends Plugin implements AnalysisPlugin {
	final public static String PLUGIN_NAME = "edgengram2-token-filter";
	
	@Override
	public Map<String, AnalysisProvider<TokenFilterFactory>> getTokenFilters() {
		Map<String, AnalysisProvider<TokenFilterFactory>> ret = new HashMap<>();
		ret.put("edge_ngram2", EdgeNGram2TokenFilterFactory::getTokenFilterFactory);
		return ret;
	}
	
}
