package testroot.service;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

@SuppressWarnings("deprecation")
public class TestHttpRequestFactory extends
		HttpComponentsClientHttpRequestFactory {

	private static final int DEFAULT_MAX_TOTAL_CONNECTIONS = 300;

	private static final int DEFAULT_TIMEOUT_MILLISECONDS = 5 * 1000;
	private static final String DEFAULT_SIGN_NAME = "sign";
	private static final Log logger = LogFactory
			.getLog(TestHttpRequestFactory.class);

	public TestHttpRequestFactory(int maxConnectionsPerRoute) {
		super();
		PoolingClientConnectionManager connectionManager = new PoolingClientConnectionManager();
		connectionManager.setMaxTotal(DEFAULT_MAX_TOTAL_CONNECTIONS);
		connectionManager.setDefaultMaxPerRoute(maxConnectionsPerRoute);
		HttpClient httpClient = new DefaultHttpClient(connectionManager);
		super.setHttpClient(httpClient);
		setReadTimeout(DEFAULT_TIMEOUT_MILLISECONDS);
		setConnectTimeout(DEFAULT_TIMEOUT_MILLISECONDS);
	}


}