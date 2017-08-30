package com.cezarykluczynski.stapi.etl.template.starship.processor;

import com.cezarykluczynski.stapi.etl.common.dto.EnrichablePair;
import com.cezarykluczynski.stapi.etl.common.processor.ItemEnrichingProcessor;
import com.cezarykluczynski.stapi.etl.common.processor.organization.WikitextToOrganizationsProcessor;
import com.cezarykluczynski.stapi.etl.common.processor.spacecraft_class.WikitextToSpacecraftClassesProcessor;
import com.cezarykluczynski.stapi.etl.template.starship.dto.StarshipTemplate;
import com.cezarykluczynski.stapi.etl.template.starship.dto.StarshipTemplateParameter;
import com.cezarykluczynski.stapi.model.organization.entity.Organization;
import com.cezarykluczynski.stapi.model.spacecraft_class.entity.SpacecraftClass;
import com.cezarykluczynski.stapi.sources.mediawiki.dto.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
@Slf4j
public class StarshipTemplateRelationsEnrichingProcessor implements ItemEnrichingProcessor<EnrichablePair<Template, StarshipTemplate>> {

	private final WikitextToOrganizationsProcessor wikitextToOrganizationsProcessor;

	private final WikitextToSpacecraftClassesProcessor wikitextToSpacecraftClassesProcessor;

	@Inject
	public StarshipTemplateRelationsEnrichingProcessor(WikitextToOrganizationsProcessor wikitextToOrganizationsProcessor,
			WikitextToSpacecraftClassesProcessor wikitextToSpacecraftClassesProcessor) {
		this.wikitextToOrganizationsProcessor = wikitextToOrganizationsProcessor;
		this.wikitextToSpacecraftClassesProcessor = wikitextToSpacecraftClassesProcessor;
	}

	@Override
	public void enrich(EnrichablePair<Template, StarshipTemplate> enrichablePair) throws Exception {
		Template template = enrichablePair.getInput();
		StarshipTemplate starshipTemplate = enrichablePair.getOutput();
		String starshipName = starshipTemplate.getName();

		for (Template.Part part : template.getParts()) {
			String key = part.getKey();
			String value = part.getValue();

			switch (key) {
				case StarshipTemplateParameter.OWNER:
					List<Organization> ownerList = wikitextToOrganizationsProcessor.process(value);
					if (!ownerList.isEmpty()) {
						starshipTemplate.setOwner(ownerList.iterator().next());
						if (ownerList.size() > 1) {
							log.info("More than one organization found for starship {} for owner value {}, using the first value",
									starshipName, value);
						}
					}
					break;
				case StarshipTemplateParameter.OPERATOR:
					List<Organization> operatorList = wikitextToOrganizationsProcessor.process(value);
					if (!operatorList.isEmpty()) {
						starshipTemplate.setOperator(operatorList.iterator().next());
						if (operatorList.size() > 1) {
							log.info("More than one organization found for starship {} for operator value {}, using the first value",
									starshipName, value);
						}
					}
					break;
				case StarshipTemplateParameter.CLASS:
					List<SpacecraftClass> classList = wikitextToSpacecraftClassesProcessor.process(value);
					if (!classList.isEmpty()) {
						starshipTemplate.setSpacecraftClass(classList.iterator().next());
						if (classList.size() > 1) {
							log.info("More than one spacecraft class found for starship {} for operator value {}, using the first value",
									starshipName, value);
						}
					} else {
						// TODO parsing class template
					}
					break;
				default:
					break;
			}
		}
	}

}
