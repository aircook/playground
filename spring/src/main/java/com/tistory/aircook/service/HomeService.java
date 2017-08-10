package com.tistory.aircook.service;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class HomeService {

	private static final Logger logger = LoggerFactory.getLogger(HomeService.class);

	@Autowired
	private ServletContext servletContext;

	/**
	 * ContextPath, RealPath 가져오기
	 * @return
	 */
	public String getContextPath() {

		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
				.currentRequestAttributes();

		String contextPath = attributes.getRequest().getContextPath();
		HttpSession session = attributes.getRequest().getSession(true);

		String realPath = servletContext.getRealPath("/");

		logger.debug("context path is [{}]", contextPath);
		logger.debug("context session id is [{}]", session.getId());
		logger.debug("context realPath is [{}]", realPath);

		return contextPath;

	}

	/**
	 * RestTemplate 사용법
	 * @return
	 */
	public Map<String, String> getPlanetary() {

		UriComponents uriComponents = UriComponentsBuilder.newInstance().scheme("https")
				.host("jsonplaceholder.typicode.com").path("/posts/{id}").build();

		logger.debug("url is [{}]", uriComponents.toString());

		RestTemplate restTemplate = new RestTemplate();

		// System.setProperty("https.protocols", "TLSv1");

		Map result = restTemplate.getForObject(uriComponents.toString(), Map.class, "1");

		logger.debug("result is [{}]", result);

		return result;

	}

}
