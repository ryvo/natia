package cz.ryvo.natia.converter;

import cz.ryvo.natia.api.CatalogueInfo;
import cz.ryvo.natia.domain.ParameterVO;
import cz.ryvo.natia.domain.ParameterVO.ParameterEnum;

import java.time.LocalDateTime;
import java.util.Map;

import static cz.ryvo.natia.domain.ParameterVO.ParameterEnum.FILENAME_OF_IMPORTED_CATALOGUE;
import static cz.ryvo.natia.domain.ParameterVO.ParameterEnum.NUMBER_OF_IMPORTED_CATALOGUE_ITEMS;
import static cz.ryvo.natia.domain.ParameterVO.ParameterEnum.TIME_OF_CATALOGUE_IMPORT;
import static org.springframework.util.CollectionUtils.isEmpty;

@Converter
public class CatalogueInfoConverter implements ApiConverter<Map<ParameterEnum, String>, CatalogueInfo> {

    public CatalogueInfo toApi(Map<ParameterEnum, String> domains) {
        if (isEmpty(domains)) {
            return null;
        }
        CatalogueInfo info = new CatalogueInfo();
        info.setDateAndTimeOfImport(nullSafeLocalDateTime(domains.get(TIME_OF_CATALOGUE_IMPORT)));
        info.setCurrentDateAndTime(LocalDateTime.now());
        info.setFileName(domains.get(FILENAME_OF_IMPORTED_CATALOGUE));
        info.setNumberOfImportedItems(nullSafeLong(domains.get(NUMBER_OF_IMPORTED_CATALOGUE_ITEMS)));

        return info;
    }

    private LocalDateTime nullSafeLocalDateTime(String value) {
        if (value == null) {
            return null;
        }
        return LocalDateTime.parse(value);
    }

    private Long nullSafeLong(String value) {
        if (value == null) {
            return null;
        }
        return Long.parseLong(value);
    }

}
