package app.yhpl.kit.application;

import java.security.KeyStore;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.util.Log;
import app.yhpl.kit.services.BaseAccessResponse;
import app.yhpl.kit.services.network.HippoRequest;
import app.yhpl.kit.services.network.HippoRequestQueue;
import app.yhpl.kit.services.network.http.SSLSocketFactoryEx;
import app.yhpl.kit.services.network.toolbox.HttpNetwork;

/**
 * @author Hubert He
 * @version V1.0
 * @Description
 * @Createdate 14-9-21 11:04
 */
public class HttpApplicationController extends ABApplication {

	public static final String TAG = HttpApplicationController.class.getSimpleName();

	/**
	 * 请求队列
	 */
	protected HippoRequestQueue httpRequestQueue;

	/**
	 * 实例
	 */
	private static HttpApplicationController instance;

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		Log.d(TAG, "On application create");
	}

	public static synchronized HttpApplicationController getInstance() {
		return instance;
	}

	protected HippoRequestQueue getHttpRequestQueue() {
		if (httpRequestQueue == null) {
			HttpNetwork networkExecutor = new HttpNetwork(getSSLHttpClient(80, 9094, 20000, 20000));
			httpRequestQueue = HippoRequestQueue.newHippoRequestQueue(networkExecutor);
		}
		return httpRequestQueue;
	}

	/**
	 * Adds the specified request to the global queue, if tag is specified then
	 * it is used else Default TAG is used.
	 *
	 * @param req
	 */
	public <T extends BaseAccessResponse> void addToRequestQueue(HippoRequest<T> req) {
		// set the default tag if tag is empty
		getHttpRequestQueue().add(req);
	}

	/**
	 * 获取SSL连接
	 *
	 * @return
	 */
	protected HttpClient getSSLHttpClient(int httpPort, int httpsPort, int soTimeout, int connectionTimeout) {
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null, null);

			SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			HttpParams params = new BasicHttpParams();

			// 设置超时时间
			HttpConnectionParams.setConnectionTimeout(params, connectionTimeout);
			HttpConnectionParams.setSoTimeout(params, soTimeout);
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", sf, httpPort));
			registry.register(new Scheme("https", sf, httpsPort));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

			return new DefaultHttpClient(ccm, params);
		} catch (Exception e) {
			return new DefaultHttpClient();
		}
	}
}
