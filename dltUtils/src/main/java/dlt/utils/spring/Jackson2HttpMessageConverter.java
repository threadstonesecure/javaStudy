
package dlt.utils.spring;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;

import dlt.utils.JsonUtils;

@Service
public class Jackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {


	public Jackson2HttpMessageConverter() {
		super();
		setObjectMapper(JsonUtils.getMapper());
	}


}
