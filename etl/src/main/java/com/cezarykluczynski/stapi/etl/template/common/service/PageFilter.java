package com.cezarykluczynski.stapi.etl.template.common.service;

import com.cezarykluczynski.stapi.sources.mediawiki.dto.Page;

public interface PageFilter {

	boolean shouldBeFilteredOut(Page page);

}
