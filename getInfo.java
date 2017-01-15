package com.gdufs.newx.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewsInfoCrawler {
	/*
	 * 
	 * Get Title
	 */
	public String getTitle(String content)
	{
		Pattern titlePattern = Pattern.compile("<meta property=\"og:title\" content=\"(.*?)\" />");
		Matcher titleMatcher = titlePattern.matcher(content);
		String title =null;
		while (titleMatcher.find())
		{
			title = titleMatcher.group(0);
			title=title.replace("<meta property=\"og:title\" content=\"", "");
			title=title.replace("\" />", "");
			
		}
		return title;
	}
	/*
	 * Get Tags
	 * <meta name="keywords" content="贵阳,检讨书,安全管理" />
	 * <meta name="tags" content="库里,勇士,骑士,詹姆斯">
	
	*/
	public String getTags(String content)
	{
		Pattern tagsPattern = Pattern.compile("<meta name=\"tags\" content=.*?>");
		Matcher tagsMatcher = tagsPattern.matcher(content);
		String tags=null;
		
		Pattern keywordsPattern = Pattern.compile("<meta name=\"keywords\" content=.*?>");
		Matcher keywordsMatcher = keywordsPattern.matcher(content);
		if(tagsMatcher.find())
		{
			tags=tagsMatcher.group(0);
			tags=tags.replace("<meta name=\"tags\" content=\"", "");
			tags=tags.trim();
			tags=tags.replace("/", "");
			tags=tags.replace("\"","");
			tags=tags.replace(">","");
			tags=tags.trim();
			
		}
		else
		{
			if(keywordsMatcher.find())
			{
				tags=keywordsMatcher.group(0);
				tags=tags.replace("<meta name=\"keywords\" content=\"", "");
				tags=tags.trim();
				tags=tags.replace("/", "");
				tags=tags.replace("\"","");
				tags=tags.replace(">","");
				tags=tags.trim();
			}
		}
		return tags;
	}
	public String getDate(String link,String content)
	{
		String date = null;
		Pattern datePattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
		Matcher dateMatcher = datePattern.matcher(link);
		Matcher dateMatcher2 = datePattern.matcher(content);
		
		Pattern datePattern2 = Pattern.compile("\\d{4}"+"年"+"\\d{2}"+"月"+"\\d{2}"+"日");
		Matcher dateMatcher3 = datePattern2.matcher(content);
		if(dateMatcher.find())
		{
			date = dateMatcher.group(0);
			
		}
		else if(dateMatcher2.find())
			{
				date=dateMatcher2.group(0);
			}
		else
		{
			if(dateMatcher3.find())
			{
				date=dateMatcher3.group(0);
			}
		}
		
		return date;
	}
	public String getType(String link)
	{
		String type=null;
		link=link.trim();
		String links[]=link.split("\\.");
		if(link.matches("http://slide.*"))
		{
			type =links[1];
		}
		else
		{
			
			type = links[0];
			type=type.replace("http://","");
		}
		return type;
	}
	public String getContent(String source)
	{
		String content = null;
		Pattern contentPattern = Pattern.compile("id=\"artibody\".*?<p>.*?</div>");
		Matcher contentMatcher = contentPattern.matcher(source);
		while(contentMatcher.find())
		{
			content=contentMatcher.group(0);
			//System.out.println("test"+content);
			String contentArray[] =content.split("<p>");
			String temp ="";
			for(int i=1;i<contentArray.length;i++){
					temp +=contentArray[i];
				//System.out.println(contentArray[i]);
			}
			temp=temp.replaceAll("<strong>", "").replaceAll("</strong>", "").replaceAll(" ","").replaceAll("&nbsp;","").replaceAll("&quot;","").replaceAll("&gt;",">").replaceAll("&yen;","¥").replaceAll("</div>", "").replaceAll("<div>", "");;
			temp=temp.replace("<p>", "").replace("</p>", "").replaceAll("<!--.*?-->", "").replaceAll("<a.*?</a>", "").replaceAll("<span>","").replaceAll("<span>","").replaceAll("<div.*?</div>","");
			temp =temp.replaceAll("<embedsrc.*?>", "").replaceAll("<spanid=.*?</span>", "").replaceAll("<imgsrc=.*?>", "").replaceAll("<spanclass.*?</span>", "");
			temp=temp.replaceAll("<divclass=.*?>", "").replaceAll("<divdata.*?>", "");
			content =temp;
			content=content.trim();
		}
		return content;
	}
	public String getSlideContent(String source)
	{
		String content = null ;
		Pattern contentPattern = Pattern.compile("<meta property=\"og:description\" content=\"(.*?)\" />");
		Matcher contentMatcher = contentPattern.matcher(source);
		while(contentMatcher.find())
		{
			content=contentMatcher.group(0);
			content=content.replace("<meta property=\"og:description\" content=\"", "");
			content=content.replace("\" />", "");
		}
		return content;
		
	}
	public static void main(String[] args) throws UnsupportedEncodingException, IOException 
	{
		NewsLinkCrawler newsLinkCrawler=new NewsLinkCrawler();
		//NewsInfoCrawler newsInfo = new NewsInfoCrawler();
		String allLinkscontent=newsLinkCrawler.getPageSource("gbk","http://roll.news.sina.com.cn/interface/rollnews_ch_out_interface.php?col=89&spec=&type=&ch=01&k=&offset_page=0&offset_num=0&num=100&asc=&page=1");
		ArrayList allLinks =newsLinkCrawler.getAllNewsLink(allLinkscontent);
		
		/*NewsInfoCrawler newsInfo = new NewsInfoCrawler();
		String testlink ="http://sports.sina.com.cn/basketball/nba/2017-01-15/doc-ifxzqnip1206208.shtml";
		System.out.println(newsInfo.getContent(newsLinkCrawler.getPageSource("utf-8", testlink)));*/
		
		
		
		for (int i=0;i<allLinks.size();i++)
		{	
			NewsInfoCrawler newsInfo = new NewsInfoCrawler();
			String link = (String) allLinks.get(i);
			String source =null;
			String content =null;
			if(link.matches("http://slide.*"))
			{
				source= newsLinkCrawler.getPageSource("gbk",link);
				if(source!=null)
				{
					content=newsInfo.getSlideContent(source);
				}
				
			}
			else
			{
				source = newsLinkCrawler.getPageSource("utf-8",link);
				if(source!=null)
				{
					content =newsInfo.getContent(source);
				}
			}
			
			String title = newsInfo.getTitle(source);
			String tags = newsInfo.getTags(source);
			String date = newsInfo.getDate(link,source);
			String type = newsInfo.getType(link);
			//System.out.println(i+":"+link+":"+type);
			System.out.println(i+":"+link+":"+content);
			//System.out.println(link+","+title+","+tags+","+date);
		}
		/*
		 * http://slide.ent.sina.com.cn/star/w/slide_4_704_161223.html
		 * slide test
		 */
		
		/*String source =newsLinkCrawler.getPageSource("gbk", "http://slide.ent.sina.com.cn/z/y/w/slide_4_704_161221.html#p=4");
		System.out.println(newsInfo.getSlideContent(source));*/
	}
}
