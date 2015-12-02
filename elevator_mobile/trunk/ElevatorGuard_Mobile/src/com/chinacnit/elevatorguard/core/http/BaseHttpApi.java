
package com.chinacnit.elevatorguard.core.http;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;

import com.chinacnit.elevatorguard.mobile.util.LogUtils;
import com.chinacnit.elevatorguard.mobile.util.LogUtils.LogTag;

public class BaseHttpApi {
	
	private static final LogTag LOG_TAG = LogUtils.getLogTag(BaseHttpApi.class.getSimpleName(), true);

    protected int BUFFER_SIZE = 8192;
    protected String CHARENCODE = "UTF-8";
    protected int HTTPS_PORT = 443;
    protected int HTTP_PORT = 80;
    protected int TIMEOUT = 8000;
    protected boolean USEGZIP = true;
    private final DefaultHttpClient client = createDefHttpClient();

    private ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

        @Override
        public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                Header header = entity.getContentEncoding();
                if (header != null) {
                    String value = header.getValue();
                    if (value != null && value.contains("gzip")) {
                        entity = new GzipDecompressingEntity(entity);
                    }
                }

                String charsetName = EntityUtils.getContentCharSet(entity);
                if (charsetName == null) {
                    charsetName = CHARENCODE;
                }
                String ret = new String(EntityUtils.toByteArray(entity), charsetName);
                return ret;
            }
            return null;
        }
    };

    public BaseHttpApi() {
        super();
        TIMEOUT = setConnectTimeOut();
        CHARENCODE = setCharEncode();
        USEGZIP = setUSEGIZP();
        HTTP_PORT = setHttpPort();
        HTTPS_PORT = setHttpsPort();
    }

    public String setCharEncode() {
        return "UTF-8";
    }

    public int setConnectTimeOut() {
        return 8000;
    }

    public int setHttpPort() {
        return 80;
    }

    public int setHttpsPort() {
        return 443;
    }

    public boolean setUSEGIZP() {
        return true;
    }

    public void setEncode(String s) {
        CHARENCODE = s;
    }


    /**
     * 
     * @Title: genrateGetUrl
     * @Description: 拼接参数
     * @param url
     * @param list
     * @return
     * @return String
     * @throws
     */
    protected String genrateGetUrl(String url, List<NameValuePair> list) {
        if (list != null && list.size() > 0) {
            url = (new StringBuilder(String.valueOf(url))).append("?").append(URLEncodedUtils.format(list, CHARENCODE)).toString();
        }
        return url;
    }

    /**
     * 
     * @Title: convertToParamMap
     * @Description: 将parm pair 转换成map
     * @param list
     * @return
     * @return Map<String,String>
     * @throws
     */
    protected Map<String, String> convertToParamMap(List<NameValuePair> list) {
        Map<String, String> retMap = new LinkedHashMap<String, String>();
        if (list == null || list.size() == 0) {
            return retMap;
        }

        for (NameValuePair pair : list) {
            retMap.put(pair.getName(), pair.getValue());

        }
        return retMap;
    }

    protected List<NameValuePair> covertToParmPair(Map<String, String> map) {
        List<NameValuePair> retList = new ArrayList<NameValuePair>();
        if (map == null || map.size() == 0) {
            return retList;
        }

        for (Map.Entry<String, String> entry : map.entrySet()) {
            retList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        return retList;
    }

    private final HttpParams createHttpParams() {
        BasicHttpParams parms = new BasicHttpParams();
        HttpConnectionParams.setStaleCheckingEnabled(parms, false);
        HttpConnectionParams.setConnectionTimeout(parms, TIMEOUT);
        HttpConnectionParams.setSoTimeout(parms, TIMEOUT);
        HttpConnectionParams.setSocketBufferSize(parms, BUFFER_SIZE);
        HttpProtocolParams.setVersion(parms, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(parms, "UTF-8");
        return parms;
    }

    /**
     * 
     * @Title: createHttpParams
     * @Description: 生成http parm 参数
     * @param timeout
     * @return
     * @return HttpParams
     * @throws
     */
    private final HttpParams createHttpParams(int timeout) {
        BasicHttpParams parms = new BasicHttpParams();
        HttpConnectionParams.setStaleCheckingEnabled(parms, false);
        HttpConnectionParams.setConnectionTimeout(parms, timeout);
        HttpConnectionParams.setSoTimeout(parms, TIMEOUT);
        HttpConnectionParams.setSocketBufferSize(parms, BUFFER_SIZE);
        HttpProtocolParams.setVersion(parms, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(parms, "UTF-8");
        return parms;
    }

    public DefaultHttpClient createDefClient() {
        SchemeRegistry schemeregistry = new SchemeRegistry();
        schemeregistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), HTTP_PORT));
        schemeregistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), HTTPS_PORT));
        HttpParams parms = createHttpParams();
        return new DefaultHttpClient(new ThreadSafeClientConnManager(parms, schemeregistry), parms);
    }

    public DefaultHttpClient createDefClient(HttpParams parms) {
        SchemeRegistry schemeregistry = new SchemeRegistry();
        schemeregistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), HTTP_PORT));
        schemeregistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), HTTPS_PORT));
        return new DefaultHttpClient(new ThreadSafeClientConnManager(parms, schemeregistry), parms);
    }

    /**
     * @Title: createDefHttpClient
     * @Description: 创建默认的http client
     * @return
     * @return DefaultHttpClient
     * @throws
     */
    public DefaultHttpClient createDefHttpClient() {
        DefaultHttpClient client;
        try {
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(null, null);
            SSLSocketFactoryEx sslsocketfactoryex = new SSLSocketFactoryEx(keystore);
            sslsocketfactoryex.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            SchemeRegistry schemeregistry = new SchemeRegistry();
            schemeregistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), HTTP_PORT));
            schemeregistry.register(new Scheme("https", sslsocketfactoryex, HTTPS_PORT));
            HttpParams parms = createHttpParams();
            client = new DefaultHttpClient(new ThreadSafeClientConnManager(parms, schemeregistry), parms);
        } catch (Exception exception) {
            exception.printStackTrace();
            client = createDefClient();
        }
        return client;
    }

    public DefaultHttpClient createHttpClientWithHttpParams(HttpParams parms) {
        DefaultHttpClient client;
        try {
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(null, null);
            SSLSocketFactoryEx sslsocketfactoryex = new SSLSocketFactoryEx(keystore);
            sslsocketfactoryex.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            SchemeRegistry schemeregistry = new SchemeRegistry();
            schemeregistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), HTTP_PORT));
            schemeregistry.register(new Scheme("https", sslsocketfactoryex, HTTPS_PORT));
            client = new DefaultHttpClient(new ThreadSafeClientConnManager(parms, schemeregistry), parms);
        } catch (Exception exception) {
            exception.printStackTrace();
            client = createDefClient();
        }
        return client;
    }

    public BasicNameValuePair generateGetParameter(String name, String value) {
        return new BasicNameValuePair(name, value);
    }

    public InputStream getUngzippedStream(HttpEntity httpentity) throws IOException {
        InputStream obj = httpentity.getContent();
        if (obj != null) {
            Header header = httpentity.getContentEncoding();
            if (header != null) {
                String value = header.getValue();
                if (value.contains("gzip")) {
                    obj = new GZIPInputStream(obj);
                }
            }
        }
        return obj;
    }

    /**
     * 
     * @Title: get
     * @Description: 请求url然后 +　请求参数
     * @param url
     * @param list
     * @return
     * @throws ParseException
     * @throws IOException
     * @return String
     * @throws
     */
    public String get(String url, List<NameValuePair> list) throws ParseException, IOException {
        String uri = genrateGetUrl(url, list);
        LogUtils.d(LOG_TAG, "get", "uri:" + uri);
        HttpGet httpget = new HttpGet(uri);
        setDeaultRequestHeader(httpget);
        long start = System.currentTimeMillis();
        String ret = client.execute(httpget, responseHandler);
        LogUtils.d(LOG_TAG, "get", "result:" + ret);
        LogUtils.d(LOG_TAG, "get", "request-response took time :" + (System.currentTimeMillis() - start) + "ms");
        return ret;
    }

    public String get(String url, List<NameValuePair> list, int timeout) throws ParseException, IOException {
        String uri = genrateGetUrl(url, list);
        LogUtils.d(LOG_TAG, "get", "uri:" + uri);
        HttpGet httpget = new HttpGet(uri);
        setDeaultRequestHeader(httpget);
        String ret = createHttpClientWithHttpParams(createHttpParams(timeout)).execute(httpget, responseHandler);
        LogUtils.d(LOG_TAG, "get", "result:" + ret);
        return ret;
    }

    public String post(String url, List<NameValuePair> list) throws ClientProtocolException, IOException {
        return post(url, new UrlEncodedFormEntity(list, CHARENCODE));
    }

    public String post(String url, HttpEntity entity) throws ClientProtocolException, IOException {
        LogUtils.d(LOG_TAG, "post", "url:" + url);
        HttpPost httppost = new HttpPost(url);
        setDeaultRequestHeader(httppost);
        httppost.setEntity(entity);
        String ret = client.execute(httppost, responseHandler);
        LogUtils.d(LOG_TAG, "post", "post whith result:" + ret);
        return ret;
    }

    public String post(String url, HttpEntity entity, int timeout) throws ClientProtocolException, IOException {
        LogUtils.d(LOG_TAG, "post", "url:" + url);
        HttpPost httppost = new HttpPost(url);
        setDeaultRequestHeader(httppost);
        httppost.setEntity(entity);
        String ret = createHttpClientWithHttpParams(createHttpParams(timeout)).execute(httppost, responseHandler);
        LogUtils.d(LOG_TAG, "post", "return : " + ret);
        return ret;
    }

    public HttpEntity postWithHttpEntity(String url, HttpEntity entity) throws ClientProtocolException, IOException {
        LogUtils.d(LOG_TAG, "post", "postWithHttpEntity: " + url);
        HttpPost httppost = new HttpPost(url);
        setDeaultRequestHeader(httppost);
        httppost.setEntity(entity);
        return client.execute(httppost).getEntity();
    }

    public InputStream postWithStream(String url, HttpEntity entity) throws ClientProtocolException, IOException {
        return getUngzippedStream(postWithHttpEntity(url, entity));
    }

    /**
     * 
     * @Title: setDeaultRequestHeader
     * @Description: 是否添加gzip参数
     * @param abstracthttpmessage
     * @return void
     * @throws
     */
    protected void setDeaultRequestHeader(AbstractHttpMessage httpMessage) {
        if (USEGZIP) {
        	httpMessage.addHeader("Accept-Encoding", "gzip");
        }
//        httpMessage.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//        httpMessage.addHeader("Accept", "text/xml");
    }
}
