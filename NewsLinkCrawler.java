package com.gdufs.newx.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class NewsLinkCrawler 
{
	public static final String DEF_CHATSET = "UTF-8";
    public static final int DEF_CONN_TIMEOUT = 30000;
    public static final int DEF_READ_TIMEOUT = 30000;
    public static String userAgent =  "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";
	public String getPageSource(String code,String link) throws UnsupportedEncodingException, IOException
	{
		URL pageUrl=new URL(link);
		BufferedReader reader =
				new BufferedReader(new InputStreamReader(pageUrl.openStream(),code));

		// Read page into buffer.
		String line;
		StringBuffer pageBuffer = new StringBuffer();
		while ((line = reader.readLine()) != null) {
			pageBuffer.append(line);
		
		}
		String content=pageBuffer.toString();
		
		content =content.trim().replace("\n", "").replace("\t", "");
		return content;
	}
	public ArrayList getAllNewsLink(String content)
	{
		//System.out.println(content);
		Pattern p=Pattern.compile("list.*");
		Pattern p2=Pattern.compile(": \".*,type");
		Matcher matcher=p.matcher(content);
		ArrayList newsLinks=new ArrayList();
		while(matcher.find())
		{
			//System.out.println(matcher.group(0));
			String allContent=matcher.group(0);
			String resultArray[] =allContent.split("url");
			for(int i =0 ;i<resultArray.length;i++)
			{
				Matcher matcher2=p2.matcher(resultArray[i]);
				while(matcher2.find())
				{
					String url = matcher2.group(0);
					url=url.replace(": \"", "");
					url=url.replace("\",type","");
					//System.out.println(url);
					newsLinks.add(url);
				}
			}
			
		}
		return newsLinks;
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException, IOException
	{
		NewsLinkCrawler newsLinkCrawler=new NewsLinkCrawler();
		String content=newsLinkCrawler.getPageSource("gbk","http://roll.news.sina.com.cn/interface/rollnews_ch_out_interface.php?col=89&spec=&type=&ch=01&k=&offset_page=0&offset_num=0&num=500&asc=&page=1");
		ArrayList newsLinks = newsLinkCrawler.getAllNewsLink(content);
		/*
		 * 遍历方法
		 //方法1
        Iterator it1 = list.iterator();
        while(it1.hasNext()){
            System.out.println(it1.next());
        }
        //方法2
        for(Iterator it2 = list.iterator();it2.hasNext();){
             System.out.println(it2.next());
        }
        //方法3
        for(String tmp:list){
            System.out.println(tmp);
        }
        //方法4
        for(int i = 0;i < list.size(); i ++){
            System.out.println(list.get(i));
        }
		 */
		for(int i = 0;i < newsLinks.size(); i ++){
			 System.out.println(""+ i+":"+ newsLinks.get(i) );
		}
	
	}
}
