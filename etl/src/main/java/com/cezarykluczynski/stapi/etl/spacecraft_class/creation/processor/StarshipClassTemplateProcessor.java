package com.cezarykluczynski.stapi.etl.spacecraft_class.creation.processor;

import com.cezarykluczynski.stapi.etl.template.starship_class.dto.StarshipClassTemplate;
import com.cezarykluczynski.stapi.model.common.service.UidGenerator;
import com.cezarykluczynski.stapi.model.spacecraft_class.entity.SpacecraftClass;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;

@Service
public class StarshipClassTemplateProcessor implements ItemProcessor<StarshipClassTemplate, SpacecraftClass> {

	private final UidGenerator uidGenerator;

	public StarshipClassTemplateProcessor(UidGenerator uidGeneratorMock) {
		this.uidGenerator = uidGeneratorMock;
	}

	@Override
	public SpacecraftClass process(StarshipClassTemplate item) throws Exception {
		SpacecraftClass spacecraftClass = new SpacecraftClass();

		spacecraftClass.setName(item.getName());
		spacecraftClass.setPage(item.getPage());
		spacecraftClass.setUid(uidGenerator.generateFromPage(item.getPage(), SpacecraftClass.class));
		spacecraftClass.setAffiliatedSpecies(item.getAffiliatedSpecies());
		spacecraftClass.setAffiliatedOrganization(item.getAffiliatedOrganization());
		spacecraftClass.setSpacecraftType(item.getSpacecraftType());
		spacecraftClass.setNumberOfDecks(item.getNumberOfDecks());
		spacecraftClass.setWarpCapable(item.getWarpCapable());
		spacecraftClass.setActiveFrom(item.getActiveFrom());
		spacecraftClass.setActiveTo(item.getActiveTo());

		return spacecraftClass;
	}

}
