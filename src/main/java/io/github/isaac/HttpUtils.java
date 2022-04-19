package io.github.isaac;

import com.intellij.openapi.util.io.StreamUtil;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.net.URI;
import java.net.URL;
import java.util.List;

public class HttpUtils {
    public static String doHttpGet(String strUrl) {
        try {
            URL url = new URL(strUrl);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), null);
            CloseableHttpClient client = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(uri);
            HttpResponse resp = client.execute((HttpUriRequest) httpGet);
            return StreamUtil.readText(resp.getEntity().getContent(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String doHttpGet(String strUrl, Header[] headers) {
        try {
            URL url = new URL(strUrl);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), null);
            CloseableHttpClient client = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(uri);
            httpGet.setHeaders(headers);
            HttpResponse resp = client.execute((HttpUriRequest) httpGet);
            return StreamUtil.readText(resp.getEntity().getContent(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String doHttpPost(String strUrl, List<NameValuePair> params) {
        try {
            URL url = new URL(strUrl);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), null);
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(uri);
            httpPost.setEntity((HttpEntity) new UrlEncodedFormEntity(params, "UTF-8"));
            HttpResponse resp = client.execute((HttpUriRequest) httpPost);
            return StreamUtil.readText(resp.getEntity().getContent(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String doHttpPost(String strUrl, String json) {
        try {
            URL url = new URL(strUrl);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), null);
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(uri);
            httpPost.setEntity((HttpEntity) new StringEntity(json));
            httpPost.setHeader("Content-type", "application/json");
            HttpResponse resp = client.execute((HttpUriRequest) httpPost);
            return StreamUtil.readText(resp.getEntity().getContent(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String doHttpPost(String strUrl, String xmlBody, Header[] headers) {
        try {
            URL url = new URL(strUrl);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), null);
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(uri);
            httpPost.setHeaders(headers);
            httpPost.setEntity((HttpEntity) new StringEntity(xmlBody, "UTF-8"));
            HttpResponse resp = client.execute((HttpUriRequest) httpPost);
            return StreamUtil.readText(resp.getEntity().getContent(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

